package org.whstsa.library.gui.components;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import org.whstsa.library.gui.factories.GuiUtils;

public class SpinnerElement extends Spinner implements Element{//Only ints for now

    private String id;
    private LabelElement label;

    public SpinnerElement(String id, String label, boolean useLabel, int start, int end, int selectedIndex) {
        super();
        this.id = id;
        if (useLabel) {
            this.label = GuiUtils.createLabel(label, 14);
        }
        this.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(start, end, selectedIndex));
        this.setEditable(true);
        this.getEditor().textProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                return;
            }
            try {
                Integer.parseInt(newValue);
            }
            catch (NumberFormatException e) {
                Platform.runLater(() -> this.getEditor().setText(oldValue));
            }
        });
        this.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue) {
                this.increment(0);
            }
        }));

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
        this.getEditor().commitValue();
        return this.getValue();
    }

    @Override
    public String getString() {
        this.getEditor().commitValue();
        Object result = this.getResult();
        return result == null ? null : result.toString();
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
