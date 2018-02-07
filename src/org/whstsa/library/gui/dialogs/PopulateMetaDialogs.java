package org.whstsa.library.gui.dialogs;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.GridPane;
import org.whstsa.library.api.Callback;
import org.whstsa.library.api.ObservableReference;
import org.whstsa.library.gui.components.Element;
import org.whstsa.library.gui.components.Table;
import org.whstsa.library.gui.factories.DialogBuilder;
import org.whstsa.library.gui.factories.DialogUtils;
import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.gui.factories.LibraryManagerUtils;
import org.whstsa.library.util.DayGenerator;

import java.util.List;
import java.util.Map;

public class PopulateMetaDialogs {

    private static final String BOOKS = "Populate Books";
    private static final String MEMBERS = "Populate Members";
    private static final String LIBRARY = "Library:";


    public static void populateMenu(Callback<Integer> callback) { //TODO LIBRARY THING
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Auto-Populate Books/Members")
                .addLabel("Please fill in one and/or the other")
                .addTextField(BOOKS)
                .addTextField(MEMBERS)
                .build();
        DialogUtils.getDialogResults(dialog , (results) -> {
            int amount;
            if (results.get(BOOKS).getString() != null) {
                try {
                    amount = Integer.parseInt(results.get(BOOKS).getString());
                    if (amount > 500) {
                        DialogUtils.createDialog("Error: Too Many People/Books", "You have exceeded the amount of people/books allowed to be added at once, reducing amount to 500.", null, Alert.AlertType.ERROR).show();
                        amount = 500;
                    }
                } catch (NumberFormatException | NullPointerException ex) {
                    amount = 0;
                }
                displayPopulateTable(amount, true);
                callback.callback(amount);
            }
            if (results.get(MEMBERS).getString() != null) {
                try {
                    amount = Integer.parseInt(results.get(MEMBERS).getString());
                    if (amount > 500) {
                        DialogUtils.createDialog("Error: Too Many People/Books", "You have exceeded the amount of people/books allowed to be added at once, reducing amount to 500.", null, Alert.AlertType.ERROR).show();
                        amount = 500;
                    }
                } catch (NumberFormatException | NullPointerException ex) {
                    amount = 0;
                }
                displayPopulateTable(amount, false);
                callback.callback(amount);
            }
            }, BOOKS, MEMBERS);
    }

    public static Table<String> populateTable(Table<String> table, int amount, boolean forBook) {
        table.addColumn("Results", cellData -> new ReadOnlyStringWrapper(cellData.getValue()) , false, TableColumn.SortType.DESCENDING, 2500);
        List<String> tableItems = FXCollections.observableArrayList();
        for (int index = 0; index < amount;index++) {
            tableItems.add(forBook ? DayGenerator.generateBook() : DayGenerator.generateMember(DayGenerator.randomLibrary()));
        }
        ObservableReference<List<String>> observableReference = () -> tableItems;
        table.setReference(observableReference);
        table.getTable().setSelectionModel(null);
        return table;
    }

    public static void displayPopulateTable(int amount, boolean forBook) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Results")
                .build();
        Table<String> populateTable =  new Table<>();
        populateTable = populateTable(populateTable, amount, forBook);
        GridPane dialogPane = (GridPane) dialog.getDialogPane().getContent();
        dialogPane.addRow(0, GuiUtils.createLabel("Added " + amount + " people/books in all libraries.                                   "));
        dialogPane.addRow(1, populateTable.getTable());
        DialogUtils.getDialogResults(dialog, (results) -> {

        });
    }

}
