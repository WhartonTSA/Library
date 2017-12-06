package org.whstsa.library.gui.dialogs;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import org.whstsa.library.api.Callback;
import org.whstsa.library.gui.factories.DialogUtils;

import java.util.Map;

public class NewLibraryDialog {

    private static final String LIBRARY_FIELD = "Library Name";

    public static void getLibraryName(Callback<String> callback) {
        Dialog<Map<String, String>> dialog = DialogUtils.createInputDialog(ButtonType.FINISH, true, "Please provide a library name", LIBRARY_FIELD);
        DialogUtils.getDialogResults(dialog, (results) -> {
            callback.callback(results.get(LIBRARY_FIELD));
        });
    }

}
