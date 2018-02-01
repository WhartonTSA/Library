package org.whstsa.library.gui.dialogs;

import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.whstsa.library.api.Callback;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.ObservableReference;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.db.Loader;
import org.whstsa.library.gui.components.Element;
import org.whstsa.library.gui.factories.DialogBuilder;
import org.whstsa.library.gui.factories.DialogUtils;
import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.gui.factories.LibraryManagerUtils;

import java.util.Map;

public class SimulateMetaDialogs {

    private static String SIMULATE = "Days";

    public static void createMember(Callback<IPerson> callback, ObservableReference<ILibrary> libraryReference) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Simulate Days")
                .addTextField(SIMULATE)
                .build();
        GridPane dialogPane = (GridPane) dialog.getDialogPane().getContent();
        if (LibraryManagerUtils.getPeopleWithoutLibrary(libraryReference).size() < 1) {
            dialogPane.add(GuiUtils.createLabel("There are no people to create a member from. Create a new person before trying to make a new member", 16, Color.RED), 0, 1);
        }
        DialogUtils.getDialogResults(dialog, (results) -> {
            IPerson person = LibraryManagerUtils.getPersonFromName(results.get(SIMULATE).getString());
            person.addMembership(libraryReference.poll());
            libraryReference.poll().addMember(person);
            callback.callback(person);
        }, SIMULATE);
    }

}
