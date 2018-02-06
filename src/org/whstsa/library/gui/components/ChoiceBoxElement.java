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

public class ChoiceBoxElement<T, U> extends ChoiceBox implements Element{

    private Label label;
    private String id;
    private ObservableList<T> checkoutList;
    private Map<T, U> items;
    private boolean map;

    public ChoiceBoxElement(String id, String label, ObservableList<T> items, boolean useLabel, int selected) {
        super();
        this.id = id;
        this.label = useLabel ? GuiUtils.createLabel(label) : null;
        this.map = false;
        this.setItems(items);
        if (selected != -1) {
            super.getSelectionModel().select(selected);
        }
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
}
