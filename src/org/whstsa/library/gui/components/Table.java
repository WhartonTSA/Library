package org.whstsa.library.gui.components;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.whstsa.library.api.ObservableReference;

import java.util.List;

public class Table<T> {

    private TableView<T> view;
    private ObservableReference<List<T>> observableReference;

    {
        view = new TableView<>();
    }

    public TableView<T> getTable() {
        return this.view;
    }

    public void clearItems() {
        this.view.getItems().clear();
    }

    public void setReference(ObservableReference<List<T>> observableReference) {
        this.observableReference = observableReference;
        this.getTable().setItems(this.getItems());
    }

    public void addColumn(String title, Callback<TableColumn.CellDataFeatures<T, String>, ObservableValue<String>> property, boolean sortable, TableColumn.SortType sortType, Integer width) {
        TableColumn<T, String> column = new TableColumn<>(title);
        column.setCellValueFactory(property);
        column.setSortable(sortable);
        column.setSortType(sortType);
        if (width != null) {
            this.view.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            column.setMaxWidth(1f * Integer.MAX_VALUE * width);
        }
        this.view.getColumns().add(column);
    }

    public void addColumn(String title, String property, boolean sortable, TableColumn.SortType sortType, Integer width) {
        this.addColumn(title, new PropertyValueFactory<>(property), sortable, sortType, width);
    }

    public void addColumn(String title, String property, boolean sortable, TableColumn.SortType sortType) {
        this.addColumn(title, property, sortable, sortType, null);
    }

    public void addColumn(String title, String property, boolean sortable) {
        this.addColumn(title, property, sortable, TableColumn.SortType.DESCENDING);
    }

    public void addColumn(String title, String property) {
        this.addColumn(title, property, false);
    }

    public T getSelected() {
        return this.getTable().getSelectionModel().getSelectedItem();
    }

    public List<T> getSelectedItems() {
        return this.getTable().getSelectionModel().getSelectedItems();
    }

    public void refresh() {
        this.pollItems();
        TableColumn<T, ?> tableColumn = this.view.getColumns().get(0);
        tableColumn.setVisible(false);
        tableColumn.setVisible(true);
    }

    public ObservableList<T> getItems() {
        if (this.observableReference == null) {
            return FXCollections.observableArrayList();
        }
        return FXCollections.observableList(observableReference.poll());
    }

    private void pollItems() {
        this.view.setItems(this.getItems());
    }

}
