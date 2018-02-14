package org.whstsa.library.gui.components;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import org.whstsa.library.gui.factories.LibraryManagerUtils;
import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.util.ChoiceBoxProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ChoiceBoxElement<T, U> extends ChoiceBox implements RequiredElement {

    private Label label;
    private String id;
    private ObservableList<T> checkoutList;
    private Map<T, U> items;
    private boolean map;
    private boolean required;

    public ChoiceBoxElement(String id, String label, ObservableList<T> items, boolean useLabel, int selected, boolean disabled) {
        super();
        this.id = id;
        this.label = useLabel ? GuiUtils.createLabel(label, 14) : null;
        this.map = false;
        this.setItems(items);
        if (selected != -1) {
            super.getSelectionModel().select(selected);
        }
        this.setDisable(disabled);
    }

    public ChoiceBoxElement(String id, String label, Map<T, U> items, ChoiceBoxProperty<T> property, boolean useLabel, int selected) {
        super();
        this.id = id;
        this.label = useLabel ? GuiUtils.createLabel(label) : null;
        this.items = items;
        this.map = true;
        List<T> setList = new ArrayList<>(items.keySet());
        if (property != null) {
            setList.forEach(property::property);//Using ChoiceBoxProperty<> like ClickHandler or Callback
        }
        this.setItems(LibraryManagerUtils.toObservableList((List<String>) setList));//Ok so, like, make sure its a string...
        if (selected != -1) {
            super.getSelectionModel().select(selected);
        }
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

    @Override
    public boolean isRequired() {
        return this.required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public boolean isSatisfied() {
        return this.getResult() != null;
    }

    @Override
    public void setOnSatisfactionUpdate(Consumer<Boolean> onSatisfactionUpdate) {
        this.setOnMouseClicked(event -> onSatisfactionUpdate.accept(this.isSatisfied()));
    }
}
