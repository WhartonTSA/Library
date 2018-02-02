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

    public static void simulateDay(ObservableReference<ILibrary> libraryReference) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Simulate Days")
                .addTextField(SIMULATE)
                .build();
        GridPane dialogPane = (GridPane) dialog.getDialogPane().getContent();
        DialogUtils.getDialogResults(dialog, (results) -> {
            int days = (int) results.get(SIMULATE).getResult();
            //Put SimulateDay code here
        }, SIMULATE);
    }

}
