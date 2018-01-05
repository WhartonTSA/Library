package org.whstsa.library.gui.components;


import javafx.scene.Node;
        import javafx.scene.control.Label;
        import org.whstsa.library.gui.factories.GuiUtils;

public class LabelElement extends Label implements Element {

    private String label;
    private String id;

    public LabelElement(String id, String label) {
        super();
        this.id = id;
        this.label = label;
    }

    public Node getComputedElement() {
        if (this.label == null) {
            return this;
        }
        super.setText(label);
        return GuiUtils.createSplitPane(GuiUtils.Orientation.HORIZONTAL, this);
    }

    public String getID() {
        return this.id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public Boolean getResult() {
        return null;
    }

    public String getString() { return super.getText(); }

    public boolean getBoolean() {
        return false;
    }
}
