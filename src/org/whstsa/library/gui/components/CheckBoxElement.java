package org.whstsa.library.gui.components;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import org.whstsa.library.gui.factories.GuiUtils;

public class CheckBoxElement extends CheckBox implements Element {

    private Label label;
    private String id;

    public CheckBoxElement(String id, String label, boolean inline, boolean disabled) {
        super();
        this.id = id;
        this.label = GuiUtils.createLabel(label, 14);
        super.setDisable(disabled);
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
        return this.id;
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }

    @Override
    public Boolean getResult() {
        return this.isSelected();
    }

    @Override
    public String getString() {
        return null;
    }

    @Override
    public boolean getBoolean() {
        return this.getResult();
    }
}
