package org.whstsa.library.gui.components;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.whstsa.library.gui.factories.GuiUtils;

import java.util.function.Consumer;

public class TextFieldElement extends TextField implements RequiredElement {

    private Label label;
    private String id;
    private boolean required = false;

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

    public TextFieldElement(String id, String label, boolean inline, boolean required) {
        this(id, label, inline);
        this.required = true;
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

    public boolean isRequired() {
        return this.required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public void setOnSatisfactionUpdate(Consumer<Boolean> onSatisfactionUpdate) {
        this.setOnKeyReleased(event -> {
            onSatisfactionUpdate.accept(this.isSatisfied());
        });
    }

    public final boolean isSatisfied() {
        String result = this.getResult();
        if (result == null) {
            return false;
        }
        return result.length() > 0;
    }

}
