package org.whstsa.library.gui.components;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.whstsa.library.gui.factories.GuiUtils;

public class TextFieldElement extends TextField implements Element {

    private Label label;
    private String id;

    public TextFieldElement(String id, String label, boolean inline) {
        super();
        if (inline) {
            this.setPromptText(label);
            this.label = null;
        } else {
            this.label = GuiUtils.createLabel(label, 14);
        }
        this.id = id;
    }

    public Node getComputedElement() {
        if (this.label == null) {
            return this;
        }
        return GuiUtils.createSplitPane(GuiUtils.Orientation.HORIZONTAL, this.label, this);
    }

    public Integer getNumber() {
        try {
            return Integer.parseInt(this.getResult());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public String getResult() {
        return this.getText();
    }

    public String getID() {
        return this.id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getString() {
        return this.getText();
    }

    public boolean getBoolean() {
        return false;
    }

}
