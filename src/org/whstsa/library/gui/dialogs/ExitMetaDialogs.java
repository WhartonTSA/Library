package org.whstsa.library.gui.dialogs;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import org.whstsa.library.gui.factories.DialogBuilder;

public class ExitMetaDialogs {

    public static void exitConfirm(boolean unsaved) {
        Dialog dialog = new DialogBuilder()
                .setTitle("Quit?")
                .addLabel(unsaved ? "Your data is unsaved.\n Do you still want to exit?" : "")
                .addButton(ButtonType.YES, true, event -> {
                    System.out.println("Exiting");//TODO
                    System.exit(0);
                })
                .addButton(ButtonType.NO, true, event -> {
                    System.out.println("kk");//TODO
                })
                .setIsCancellable(false)
                .build();
        dialog.show();
    }

}
