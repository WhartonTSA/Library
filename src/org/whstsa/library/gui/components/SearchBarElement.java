package org.whstsa.library.gui.components;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.whstsa.library.gui.factories.GuiUtils;

public class SearchBarElement extends HBox implements Element{

    private LabelElement labelElement;
    private String id;

    public SearchBarElement(String id, String label, ObservableList<String> items, BorderPane container, Table<?> table) {
        super();
        this.id = id;
        this.labelElement = GuiUtils.createLabel(label, 12);

        Button exitButton = GuiUtils.createButton("X", false, 10, event -> container.setTop(null));
        StackPane stackPane = new StackPane(exitButton);
        stackPane.setAlignment(Pos.CENTER_RIGHT);
        ComboBox comboBox = GuiUtils.createComboBox(items, true);
        super.getChildren().addAll(this.labelElement, comboBox, stackPane);
        super.setPadding(new Insets(10, 10, 10, 10));
        super.setMargin(exitButton, new Insets(0, 5, 0, 0));
        super.setMargin(comboBox, new Insets(0, 0, 0, 0));
        super.setAlignment(Pos.CENTER_LEFT);

        ObservableList data =  table.getItems();
        comboBox.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (oldValue != null && (newValue.length() < oldValue.length())) {
                table.setItems(data);
            }
            String value = newValue.toLowerCase();
            ObservableList<?> subentries = FXCollections.observableArrayList();

            long count = table.getTable().getColumns().stream().count();
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
        });
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
    };

    @Override
    public boolean getBoolean() {
        return false;
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }

}
