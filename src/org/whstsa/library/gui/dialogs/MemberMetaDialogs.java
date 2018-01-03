package org.whstsa.library.gui.dialogs;

import javafx.scene.control.Dialog;
import org.whstsa.library.api.Callback;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.impl.Person;
import org.whstsa.library.db.Loader;
import org.whstsa.library.db.ObjectDelegate;
import org.whstsa.library.gui.components.Element;
import org.whstsa.library.gui.factories.DialogBuilder;
import org.whstsa.library.gui.factories.DialogUtils;

import java.util.Map;

public class MemberMetaDialogs {

    private static final String FIRST_NAME = "First Name";
    private static final String LAST_NAME = "Last Name";
    private static final String TEACHER = "Teacher?";

    public static void createMember(Callback<IPerson> callback) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("New Person")
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
            person.addMembership(ObjectDelegate.getLibraries().get(0));//TODO change getLibraries().get() to getLibrary(UUID)
        }, FIRST_NAME, LAST_NAME);
    }
}
