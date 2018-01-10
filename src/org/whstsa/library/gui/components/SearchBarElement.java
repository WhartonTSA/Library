package org.whstsa.library.gui.components;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.whstsa.library.gui.factories.GuiUtils;

public class SearchBarElement extends HBox implements Element{

    private LabelElement label;
    private String id;

    public SearchBarElement(String id, String label, ObservableList<String> items, boolean useLabel) {
        super();
        this.id = id;
        if (useLabel) {
            this.label = GuiUtils.createLabel(label, 12);
        }
        else {
            this.label = null;
        }
        super.getChildren().addAll(GuiUtils.createLabel(label), GuiUtils.createComboBox(items, true));
        super.setPadding(new Insets(10, 10, 10, 10));
    }

    public Node getComputedElement() {
        if (this.label == null) {
            return this;
        }
        return GuiUtils.createHBox(10, this.label, this);
    }

    public String getID() {
        return this.id;
    }

    public Object getResult() { return null; }

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
