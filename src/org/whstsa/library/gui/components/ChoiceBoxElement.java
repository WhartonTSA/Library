package org.whstsa.library.gui.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import org.whstsa.library.api.BookType;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.library.ICheckout;
import org.whstsa.library.gui.api.LibraryManagerUtils;
import org.whstsa.library.gui.factories.GuiUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ChoiceBoxElement extends ChoiceBox implements Element{

    private Label label;
    private String id;
    private ObservableList<ICheckout> checkoutList;
    private Map<IBook, List<ICheckout>> items;
    private boolean map;

    public ChoiceBoxElement(String id, String label, ObservableList<String> items, boolean useLabel, int selected) {
        super();
        this.id = id;
        if (useLabel) {
            this.label = GuiUtils.createLabel(label);
        }
        else {
            this.label = null;
        }
        this.map = false;
        this.setItems(items);
        if (selected != -1) {
            super.getSelectionModel().select(selected);
        }
    }

    public ChoiceBoxElement(String id, String label, Map<IBook, List<ICheckout>> items, boolean useLabel, int selected) {
        super();
        this.id = id;
        if (useLabel) {
            this.label = GuiUtils.createLabel(label);
        }
        else {
            this.label = null;
        }
        this.items = items;
        this.map = true;
        List<IBook> setList = new ArrayList<>(items.keySet());
        this.setItems(LibraryManagerUtils.getBookTitlesFromList(FXCollections.observableList(setList)));
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
        //return !map ? this.getSelectionModel().getSelectedItem() : items.get(this.getSelectionModel().getSelectedItem());//TODO checkout getter
        return this.getSelectionModel().getSelectedItem();
    }

    @Override
    public String getString() {
        return this.label.toString();
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
