package org.whstsa.library.gui.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import org.whstsa.library.api.Callback;
import org.whstsa.library.api.exceptions.CannotDeregisterException;
import org.whstsa.library.api.library.IMember;
import org.whstsa.library.db.Loader;
import org.whstsa.library.gui.factories.DialogBuilder;
import org.whstsa.library.gui.factories.DialogUtils;

public class ExitMetaDialogs {

    public static void exitConfirm() {
        Dialog dialog = new DialogBuilder()
                .setTitle("Quit?")
                .addButton(ButtonType.YES, true, event -> {
                    System.out.println("Exiting (in theory)");//TODO
                })
                .addButton(ButtonType.NO, true, event -> {
                    System.out.println("kk");//TODO
                })
                .setIsCancellable(false)
                .build();
        dialog.show();
    }

}
