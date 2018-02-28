package org.whstsa.library.gui.components;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import org.whstsa.library.gui.factories.GuiUtils;

import java.util.function.Consumer;

public class CheckBoxElement extends CheckBox implements RequiredElement {

    private Node label;
    private String id;
    private boolean required;

    public CheckBoxElement(String id, String label, boolean inline, boolean disabled, boolean required) {
        super();
        this.id = id;
        this.label = GuiUtils.createLabel(label, 14);
        this.required = required;
        super.setDisable(disabled);
    }

    public CheckBoxElement(String id, String label, boolean inline, boolean disabled) {
        this(id, label, inline, disabled, false);
    }

    @Override
    public Node getComputedElement() {
        if (this.label == null) {
            return this;
        }
        return GuiUtils.createSplitPane(GuiUtils.Orientation.HORIZONTAL, this.label, this);
    }

    public void setLabel(Node label) {
        this.label = label;
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

    @Override
    public boolean isRequired() {
        return this.required;
    }

    @Override
    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public boolean isSatisfied() {
        return this.selectedProperty().get();
    }

    @Override
    public void setOnSatisfactionUpdate(Consumer<Boolean> onSatisfactionUpdate) {
        this.selectedProperty().addListener((obs, oldal, newVal) -> onSatisfactionUpdate.accept(newVal));
    }
}
