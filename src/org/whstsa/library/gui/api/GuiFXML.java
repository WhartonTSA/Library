package org.whstsa.library.gui.api;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.db.ObjectDelegate;
import org.whstsa.library.gui.api.Gui;
import org.whstsa.library.util.Logger;
import sun.applet.Main;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GuiFXML {

    @FXML
    private HBox hboxContainer;

    @FXML
    public void initialize() {
        List<ILibrary> libraryList = ObjectDelegate.getLibraries();
        ObservableList<String> libraryData = FXCollections.observableList(libraryList.stream().map(library -> library.getName()).collect(Collectors.toList()));
        Logger.DEFAULT_LOGGER.debug((this.hboxContainer == null) + "");


        this.getLibraryTableView().setItems(libraryData);

        /*nameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                String[] x = p.getValue();
                if (x != null && x.length>0) {
                    return new SimpleStringProperty(x[0]);
                } else {
                    return new SimpleStringProperty("<no name>");
                }
            }
        });*/
    }

    public String getFXMLFile() {
        return "org/whstsa/library/gui/scenes/fxml/FXMLgui.fxml";
    }

    public String getUUID() {
        return "GUI_JXML";
    }

    private TableView getLibraryTableView() {
        return this.getTableViewFromHBOX("librariesTable");
    }

    private TableView getTableViewFromHBOX(String id) {
        return this.getTableViewFromParent(this.hboxContainer, id);
    }

    private TableView getTableViewFromParent(Parent parent, String id) {
        for (Node node1 : parent.getChildrenUnmodifiable()) {
            Logger.DEFAULT_LOGGER.debug(node1.getId());
            if (node1 instanceof TableView) {
                TableView tableView = (TableView) node1;
                if (tableView.getId().equals(id)) {
                    return tableView;
                }
            } else if (node1 instanceof Parent) {
                TableView tableView = this.getTableViewFromParent((Parent) node1, id);
                if (tableView != null && tableView.getId().equals(id)) {
                    return tableView;
                }
            }
        }
        return null;
    }

    private TableView getTableViewFromParent(ObservableList<Node> parents, String id) {
        for (Node node : parents) {
            if (node instanceof Parent) {
                TableView tableView = this.getTableViewFromParent((Parent) node, id);
                if (tableView != null) {
                    return tableView;
                }
            } else if (node instanceof TableView) {
                return (TableView) node;
            }
        }
        return null;
    }
}
