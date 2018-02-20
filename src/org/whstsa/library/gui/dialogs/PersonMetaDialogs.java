package org.whstsa.library.gui.dialogs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import org.whstsa.library.api.Callback;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.exceptions.CannotDeregisterException;
import org.whstsa.library.api.impl.Person;
import org.whstsa.library.db.Loader;
import org.whstsa.library.gui.components.Element;
import org.whstsa.library.gui.factories.DialogBuilder;
import org.whstsa.library.gui.factories.DialogUtils;

import java.util.Map;

public class PersonMetaDialogs {

    private static final String FIRST_NAME = "First Name";
    private static final String LAST_NAME = "Last Name";
    private static final String TEACHER = "Teacher?";

    public static void createPerson(Callback<IPerson> callback) {
        ObservableList<String> roleSelectionItems = FXCollections.observableArrayList();
        roleSelectionItems.addAll("Teacher", "Student");
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("New Person")
                .addTextField(FIRST_NAME, null, false, true)
                .addTextField(LAST_NAME, null, false, true)
                .addRequiredChoiceBox(TEACHER, roleSelectionItems, true, 1, false)
                .build();
        DialogUtils.getDialogResults(dialog, (results) -> {
            String firstName = results.get(FIRST_NAME).getString();
            String lastName = results.get(LAST_NAME).getString();
            boolean teacher = results.get(TEACHER).getResult().toString().equals("Teacher");
            if (firstName != null && lastName != null) {
                IPerson person = new Person(firstName, lastName, teacher);
                Loader.getLoader().loadPerson(person);
                callback.callback(person);
            }
        }, FIRST_NAME, LAST_NAME);
    }

    public static void updatePerson(IPerson person, Callback<IPerson> callback) {
        ObservableList<String> roleSelectionItems = FXCollections.observableArrayList();
        roleSelectionItems.addAll("Teacher", "Student");
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Update Person")
                .addTextField(FIRST_NAME, person.getFirstName(), false, true)
                .addTextField(LAST_NAME, person.getLastName(), false, true)
                .addRequiredChoiceBox(TEACHER, roleSelectionItems, true, person.isTeacher() ? 0 : 1, false)
                .build();
        DialogUtils.getDialogResults(dialog, (results) -> {
            String firstName = results.get(FIRST_NAME).getString();
            String lastName = results.get(LAST_NAME).getString();
            boolean teacher = results.get(TEACHER).getResult().toString().equals("Teacher");
            person.setFirstName(firstName);
            person.setLastName(lastName);
            person.setTeacher(teacher);
            callback.callback(person);
        });
    }

    public static void deletePerson(IPerson person, Callback<IPerson> callback) {
        Dialog dialog = new DialogBuilder()
                .setTitle("Delete Person")
                .addButton(ButtonType.YES, true, event -> {
                    if (!person.isRemovable()) {
                        DialogUtils.createDialog("Person Still Active", person.getName() + " is ineligible to be withdrawn because they have books checked out.", null, Alert.AlertType.ERROR).showAndWait();
                        callback.callback(null);
                        return;
                    }
                    try {
                        person.getMemberships().forEach(member -> {
                            member.getLibrary().removeMember(member);
                        });
                    } catch (CannotDeregisterException ex) {
                        DialogUtils.createDialog("Couldn't Deregister", ex.getMessage(), null, Alert.AlertType.ERROR).show();
                        callback.callback(null);
                        return;
                    }
                    Loader.getLoader().unloadPerson(person.getID());
                    callback.callback(person);
                })
                .addButton(ButtonType.NO, true, event -> {
                    callback.callback(null);
                })
                .setIsCancellable(false)
                .build();

        dialog.show();
    }
}
