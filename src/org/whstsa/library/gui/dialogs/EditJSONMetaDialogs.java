package org.whstsa.library.gui.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import org.whstsa.library.gui.factories.DialogBuilder;
import org.whstsa.library.gui.factories.DialogUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class EditJSONMetaDialogs {

    public static void editConfirm(File file) {
        Dialog dialog = new DialogBuilder()
                .setTitle("Edit Raw JSON?")
                .addLabel("Editing this data manually may make \n" +
                        "the file unreadable and unrecoverable. \n" +
                        "Do you want to edit the JSON?")
                .addButton(ButtonType.YES, true, event -> {
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        desktop.open(file);
                    } catch (IOException ex) {
                        DialogUtils.createDialog("Unable to open file", ex.getMessage(), null, Alert.AlertType.ERROR).show();
                    }
                })
                .addButton(ButtonType.CANCEL, true, event -> {
                })
                .setIsCancellable(false)
                .build();
        dialog.show();
    }

}
