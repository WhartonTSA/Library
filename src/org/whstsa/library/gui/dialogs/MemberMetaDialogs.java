package org.whstsa.library.gui.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;
import org.whstsa.library.api.Callback;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.ObservableReference;
import org.whstsa.library.api.exceptions.CannotDeregisterException;
import org.whstsa.library.api.impl.Person;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.api.library.IMember;
import org.whstsa.library.db.Loader;
import org.whstsa.library.db.ObjectDelegate;
import org.whstsa.library.gui.api.LibraryManagerUtils;
import org.whstsa.library.gui.components.Element;
import org.whstsa.library.gui.factories.DialogBuilder;
import org.whstsa.library.gui.factories.DialogUtils;
import org.whstsa.library.gui.factories.GuiUtils;

import java.util.Map;

public class MemberMetaDialogs {

    private static final String FIRST_NAME = "First Name";
    private static final String LAST_NAME = "Last Name";
    private static final String TEACHER = "Teacher?";
    private static final String EXISTING = "Choose existing person";

    public static void createMember(Callback<IPerson> callback, ObservableReference<ILibrary> libraryReference) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Add Member")
                //.addChoiceBox("Person:", LibraryManagerUtils.getPeopleNames(), true, -1)
                .addTextField(FIRST_NAME)
                .addTextField(LAST_NAME)
                .addCheckBox(TEACHER)
                .build();
        DialogUtils.getDialogResults(dialog, (results) -> {
            String firstName = results.get(FIRST_NAME).getString();
            String lastName = results.get(LAST_NAME).getString();
            boolean teacher = results.get(TEACHER).getBoolean();
            IPerson person = new Person(firstName, lastName, teacher);
            Loader.getLoader().loadPerson(person);
            callback.callback(person);
            person.addMembership(libraryReference.poll());
            libraryReference.poll().addMember(person);
        }, FIRST_NAME, LAST_NAME, TEACHER);
    }

    public static void updateMember(IMember member, Callback<IMember> callback) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Edit Member")
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
                        member.getPerson().getMemberships().stream().map(person -> person.getLibrary()).forEach(library -> {
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


        public static void listBooks(Callback<IMember> callback, IMember member) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle(member.getName() + "'s books")
                .build();
            GridPane dialogPane = (GridPane) dialog.getDialogPane().getContent();
            for (int i = 0; i < member.getBooks().size(); i++) {
                dialogPane.add(GuiUtils.createLabel(member.getBooks().get(i).getTitle()), 0, i);
            }
        DialogUtils.getDialogResults(dialog, (results) -> {});
    }
}
