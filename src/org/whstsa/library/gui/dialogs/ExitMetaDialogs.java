package org.whstsa.library.gui.dialogs;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import org.whstsa.library.gui.factories.DialogBuilder;

public class ExitMetaDialogs {

    public static void exitConfirm(boolean unsaved) {
        Dialog dialog = new DialogBuilder()
                .setTitle("Quit?")
                .addLabel(unsaved ? "Your data is unsaved.\n Do you still want to exit?" : null)
                .addButton(ButtonType.YES, true, event -> {
                    System.exit(0);
                })
                .addButton(ButtonType.NO, true, event -> {
                })
                .setIsCancellable(false)
                .build();
        dialog.show();
    }

}
