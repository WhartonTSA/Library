package org.whstsa.library.gui.api;

import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.whstsa.library.gui.api.Gui;
import sun.applet.Main;

import java.io.IOException;

public class GuiJXML implements Gui {
    @FXML private TableView tableView;
    @Override
    public Scene draw() {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("org/whstsa/library/gui/scenes/fxml/FXMLgui.fxml"));
            Scene activeScene = new Scene(root);

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
