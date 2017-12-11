package org.whstsa.library.gui.api;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.db.ObjectDelegate;
import org.whstsa.library.gui.api.Gui;
import sun.applet.Main;

import java.io.IOException;
import java.util.List;

public class GuiMain implements Gui {
    @FXML private TableView librariesTable;
    @Override
    public Scene draw() {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("org/whstsa/library/gui/scenes/fxml/FXMLgui.fxml"));
            Scene activeScene = new Scene(root);
            List<ILibrary> libraryList = ObjectDelegate.getLibraries();
            ObservableList<String> libraryData = FXCollections.observableArrayList();
            for (int i = 0; i < libraryList.size(); i++) {
                libraryData.add(libraryList.get(i).getName());
            }
            librariesTable = new TableView(libraryData);


            return activeScene;
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }


    }

    @Override
    public String getUUID() { return "GUI_JXML";
    }
}
