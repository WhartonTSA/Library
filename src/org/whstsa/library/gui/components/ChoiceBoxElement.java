package org.whstsa.library.gui.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import org.whstsa.library.api.BookType;
import org.whstsa.library.gui.factories.GuiUtils;

public class ChoiceBoxElement extends ChoiceBox implements Element{

    private Label label;
    private String id;

    public ChoiceBoxElement(String id, String label, ObservableList<String> items, boolean useLabel) {
        super();
        this.id = id;
        if (useLabel) {
            this.label = GuiUtils.createLabel(label);
        }
        else {
            this.label = null;
        }
        this.setItems(items);
    }

    @Override
    public Node getComputedElement() {
        if (this.label == null) {
            return this;
        }
        return GuiUtils.createSplitPane(GuiUtils.Orientation.HORIZONTAL, this.label, this);
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public Object getResult() {
        return this.getSelectionModel().getSelectedItem();
    }

    @Override
    public String getString() {
        return this.label.toString();
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
