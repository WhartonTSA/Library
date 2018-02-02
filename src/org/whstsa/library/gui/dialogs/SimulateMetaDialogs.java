package org.whstsa.library.gui.dialogs;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.whstsa.library.api.Callback;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.ObservableReference;
import org.whstsa.library.api.library.ICheckout;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.db.Loader;
import org.whstsa.library.gui.components.Element;
import org.whstsa.library.gui.components.Table;
import org.whstsa.library.gui.components.tables.BookStatusRow;
import org.whstsa.library.gui.factories.DialogBuilder;
import org.whstsa.library.gui.factories.DialogUtils;
import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.gui.factories.LibraryManagerUtils;
import org.whstsa.library.util.BookStatus;

import java.util.ArrayList;
import java.util.List;
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
            displaySimulateTable(libraryReference , days);
        }, SIMULATE);
    }

    public static Table<BookStatusRow> simulateTable (Table<BookStatusRow> table, ObservableReference<ILibrary> libraryReference, int days) {
        List<BookStatusRow> tableItems = FXCollections.observableArrayList();
        for (int index = 0; index < days;index++) {

        }
        return null;
    }

    public static void displaySimulateTable(ObservableReference<ILibrary> libraryReference, int days) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Simulate Days")
                .addTextField(SIMULATE)
                .build();
        Table<BookStatusRow> simulateTable =  new Table<>();
        simulateTable = simulateTable(simulateTable, libraryReference, days);
        GridPane dialogPane = (GridPane) dialog.getDialogPane().getContent();
        DialogUtils.getDialogResults(dialog, (results) -> {

        }, SIMULATE);
    }

}
