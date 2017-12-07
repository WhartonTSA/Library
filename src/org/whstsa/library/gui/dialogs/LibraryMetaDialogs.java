package org.whstsa.library.gui.dialogs;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import org.whstsa.library.api.Callback;
import org.whstsa.library.api.impl.library.Library;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.gui.api.InputGroup;
import org.whstsa.library.gui.factories.DialogUtils;
import org.whstsa.library.gui.factories.GuiUtils;

import java.util.Map;

public class LibraryMetaDialogs {

    private static final String LIBRARY_FIELD = "Library Name";

    public static void createLibrary(Callback<ILibrary> callback) {
        Dialog<Map<String, String>> dialog = DialogUtils.createInputDialog(ButtonType.FINISH, true, "Please provide a library name", LIBRARY_FIELD);
        DialogUtils.getDialogResults(dialog, (results) -> {
            if (results.get(LIBRARY_FIELD) == null) {
                callback.callback(null);
                return;
            }
            ILibrary library = new Library(results.get(LIBRARY_FIELD));
            callback.callback(library);
        });
    }

    public static void updateLibrary(ILibrary library, Callback<ILibrary> callback) {
        InputGroup libraryFieldGroup = GuiUtils.createInputGroup(LIBRARY_FIELD);
        libraryFieldGroup.setValue(library.getName());
        Dialog<Map<String, String>> dialog = DialogUtils.createInputDialog(ButtonType.FINISH, true, "Updating Library", libraryFieldGroup);
        DialogUtils.getDialogResults(dialog, (results) -> {
            String newName = results.get(LIBRARY_FIELD);
            if (newName == null) {
                callback.callback(library);
                return;
            }
            library.setName(newName);
            callback.callback(library);
        });
    }

}
