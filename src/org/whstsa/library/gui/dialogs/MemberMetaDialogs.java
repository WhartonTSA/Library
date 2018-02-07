package org.whstsa.library.gui.dialogs;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.whstsa.library.api.Callback;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.ObservableReference;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.exceptions.CannotDeregisterException;
import org.whstsa.library.api.library.ICheckout;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.api.library.IMember;
import org.whstsa.library.db.Loader;
import org.whstsa.library.gui.components.Table;
import org.whstsa.library.gui.components.tables.BookStatusRow;
import org.whstsa.library.gui.factories.LibraryManagerUtils;
import org.whstsa.library.gui.components.Element;
import org.whstsa.library.gui.factories.DialogBuilder;
import org.whstsa.library.gui.factories.DialogUtils;
import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.util.BookStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MemberMetaDialogs {

    private static final String FIRST_NAME = "First Name";
    private static final String LAST_NAME = "Last Name";
    private static final String TEACHER = "Teacher?";
    private static final String EXISTING = "Person:";

    public static void createMember(Callback<IPerson> callback, ObservableReference<ILibrary> libraryReference) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Add Member")
                .addChoiceBox(EXISTING, LibraryManagerUtils.toObservableList(LibraryManagerUtils.getNames(LibraryManagerUtils.getPeopleWithoutLibrary(libraryReference.poll()))), true, -1)
                .build();
        GridPane dialogPane = (GridPane) dialog.getDialogPane().getContent();
        if (LibraryManagerUtils.getPeopleWithoutLibrary(libraryReference.poll()).size() < 1) {
            dialogPane.add(GuiUtils.createLabel("There are no people to create a member from.\n Create a new person before trying to make a new member", 16, Color.RED), 0, 1);
        }
        DialogUtils.getDialogResults(dialog, (results) -> {
            IPerson person = LibraryManagerUtils.getPersonFromName(results.get(EXISTING).getString());
            person.addMembership(libraryReference.poll());
            libraryReference.poll().addMember(person);
            callback.callback(person);
        }, EXISTING);
    }

    public static void updateMember(IMember member, Callback<IMember> callback) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Edit Person")
                .addTextField(FIRST_NAME, member.getPerson().getFirstName())
                .addTextField(LAST_NAME, member.getPerson().getLastName())
                .addCheckBox(TEACHER, member.getPerson().isTeacher())
                .build();
        DialogUtils.getDialogResults(dialog, (results) -> {
            String firstName = results.get(FIRST_NAME).getString();
            String lastName = results.get(LAST_NAME).getString();
            boolean teacher = results.get(TEACHER).getBoolean();
            member.getPerson().setFirstName(firstName);
            member.getPerson().setLastName(lastName);
            member.getPerson().setTeacher(teacher);
            callback.callback(member);
        });
    }

    public static void deleteMember(IMember member, Callback<IMember> callback) {
        Dialog dialog = new DialogBuilder()
                .setTitle("Remove Member")
                .addButton(ButtonType.YES, true, event -> {
                    if (!member.getPerson().isRemovable()) {
                        DialogUtils.createDialog("Person Still Active", member.getName() + " is ineligible to be withdrawn because they have books checked out.", null, Alert.AlertType.ERROR).showAndWait();
                        callback.callback(null);
                        return;
                    }
                    try {
                        member.getPerson().getMemberships().stream().map(IMember::getLibrary).forEach(library -> {
                            library.removeMember(member);
                        });
                    } catch (CannotDeregisterException ex) {
                        DialogUtils.createDialog("Couldn't Deregister", ex.getMessage(), null, Alert.AlertType.ERROR).show();
                        callback.callback(null);
                        return;
                    }
                    Loader.getLoader().unloadPerson(member.getID());
                    callback.callback(member);
                })
                .addButton(ButtonType.NO, true, event -> {
                    callback.callback(null);
                })
                .setIsCancellable(false)
                .build();
        dialog.show();
    }


    public static void listBooks(IMember member) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle(member.getName() + "'s Books")
                .build();
            GridPane dialogPane = (GridPane) dialog.getDialogPane().getContent();
            if (member.getBooks().size() > 0) {
                dialogPane.add(booksTable(member.getBooks()).getTable(), 0, 0);
            }
            else {
                dialogPane.add(GuiUtils.createLabel(member.getName() + " has no checked-out books."), 0, 0);
            }
        DialogUtils.getDialogResults(dialog, (results) -> {});
    }

    private static Table<IBook> booksTable(List<IBook> books) {
        Table<IBook> mainTable = new Table<>();

        mainTable.addColumn("Books", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getName()), true, TableColumn.SortType.DESCENDING, 50);
        mainTable.addColumn("Due Date", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getName()), true, TableColumn.SortType.DESCENDING, 25);

        ObservableReference<List<IBook>> observableReference = () -> books;
        mainTable.setReference(observableReference);
        mainTable.getTable().setSelectionModel(null);
        return mainTable;
    }
}
