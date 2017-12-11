package org.whstsa.library.gui.components;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import org.whstsa.library.gui.factories.GuiUtils;

public class CheckBoxElement extends CheckBox implements Element {

    private Label label;
    private String id;
    private boolean inline;

    public CheckBoxElement(String id, String label, boolean inline) {
        super();
        this.id = id;
        this.label = GuiUtils.createLabel(label);
        this.inline = inline;
    }

    @Override
    public Node getComputedElement() {
        if (this.label == null) {
            return this;
        }
        return GuiUtils.createSplitPane(this.inline ? GuiUtils.Orientation.HORIZONTAL : GuiUtils.Orientation.VERTICAL, this.label, this);
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
}
