package org.whstsa.library.gui.components;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.library.IMember;
import org.whstsa.library.gui.factories.GuiUtils;

public class SearchBarElement extends ToolBar implements Element{

    private LabelElement labelElement;
    private String id;

    public SearchBarElement(String id, String label, ObservableList<String> items, BorderPane mainContainer, Table<IBook> table) {
        super();
        this.id = id;
        this.labelElement = GuiUtils.createLabel(label, 12);

        Label searchLabel = GuiUtils.createLabel(label);
        //ChoiceBoxElement field = GuiUtils.createChoiceBox("Book:", items, true, 0);

        TextField searchField = new TextField();

        Label filteredLabel = GuiUtils.createLabel("", 12);

        ObservableList<IBook> originalData = table.getTable().getItems();

        searchField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (oldValue != null && (newValue.length() < oldValue.length())) {
                table.getTable().setItems(originalData);
            }
            String value = newValue.toLowerCase();
            ObservableList<IBook> subentries = FXCollections.observableArrayList();

            long count = table.getTable().getColumns().size();
            for (int i = 0; i < table.getItems().size(); i++) {
                for (int j = 0; j < count; j++) {
                    String entry = "" + table.getTable().getColumns().get(j).getCellData(i);
                    if (entry.toLowerCase().contains(value)) {
                        subentries.add(table.getTable().getItems().get(i));
                        break;
                    }
                }
            }
            table.getTable().setItems(subentries);

            if (!subentries.equals(table.getItems())) {
                filteredLabel.setText("Results have been filtered.");
            }
            else {
                filteredLabel.setText("");
            }

        });

        HBox mainSpacer = new HBox(new Label(""));
        HBox.setHgrow(mainSpacer, Priority.ALWAYS);//HBox that always grows to maximum width, keeps X button on right side of toolBar

        Button closeButton = GuiUtils.createButton("X", false, 5, Pos.CENTER_RIGHT, event -> {//TODO Ugly close button
            mainContainer.setTop(null);
            table.refresh();
        });

        super.getItems().addAll(searchLabel, searchField, filteredLabel, mainSpacer, closeButton);
        super.setStyle("-fx-base: #bbedbc;");
    }

    public SearchBarElement(String id, String label, ObservableList<String> items, BorderPane mainContainer, Table<IMember> table, String literallyPutAnyStringHereIDCYoureFavoriteWordMaybe) {
        super();
        this.id = id;
        this.labelElement = GuiUtils.createLabel(label, 12);

        Label searchLabel = GuiUtils.createLabel(label);
        //ChoiceBoxElement field = GuiUtils.createChoiceBox("Book:", items, true, 0);

        TextField searchField = new TextField();

        Label filteredLabel = GuiUtils.createLabel("", 12);

        ObservableList<IMember> originalData = table.getTable().getItems();

        searchField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (oldValue != null && (newValue.length() < oldValue.length())) {
                table.getTable().setItems(originalData);
            }
            String value = newValue.toLowerCase();
            ObservableList<IMember> subentries = FXCollections.observableArrayList();

            long count = table.getTable().getColumns().size();
            for (int i = 0; i < table.getItems().size(); i++) {
                for (int j = 0; j < count; j++) {
                    String entry = "" + table.getTable().getColumns().get(j).getCellData(i);
                    if (entry.toLowerCase().contains(value)) {
                        subentries.add(table.getTable().getItems().get(i));
                        break;
                    }
                }
            }
            table.getTable().setItems(subentries);

            if (!subentries.equals(table.getItems())) {
                filteredLabel.setText("Results have been filtered.");
            }
            else {
                filteredLabel.setText("");
            }

        });

        HBox mainSpacer = new HBox(new Label(""));
        HBox.setHgrow(mainSpacer, Priority.ALWAYS);//HBox that always grows to maximum width, keeps X button on right side of toolBar

        Button closeButton = GuiUtils.createButton("X", false, 5, Pos.CENTER_RIGHT, event -> {//TODO Ugly close button
            mainContainer.setTop(null);
            table.refresh();
        });

        super.getItems().addAll(searchLabel, searchField, filteredLabel, mainSpacer, closeButton);
    }

    public Node getComputedElement() {
        if (this.labelElement == null) {
            return this;
        }
        return GuiUtils.createHBox(10, this);
    }

    public String getID() {
        return this.id;
    }

    public Object getResult() { return null; }

    @Override
    public String getString() {
        return this.labelElement.toString();
    }

    @Override
    public boolean getBoolean() {
        return false;
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }

}
