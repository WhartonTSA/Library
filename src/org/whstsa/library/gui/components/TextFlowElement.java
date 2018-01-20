package org.whstsa.library.gui.components;

import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.whstsa.library.gui.factories.GuiUtils;

import java.util.List;

public class TextFlowElement extends TextFlow implements Element {

    private String id;
    private double size;
    private String css;
    private List<String> fields;

    public TextFlowElement(String id, double size, String css, List<String> fields) {
        super();
        this.id = id;
        this.size = size;
        this.fields = fields;

        this.fields.forEach(field -> super.getChildren().add(new Text(field)));
        super.setStyle(css);
    }

    @Override
    public Node getComputedElement() {
        return GuiUtils.createTextFlow(this.id, this.size, this.css, this.fields);
    }

    @Override
    public String getID() {
        return this.id;
    }

    @Override
    public Object getResult() {
        return null;
    }

    @Override
    public String getString() {
        return super.toString();
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
