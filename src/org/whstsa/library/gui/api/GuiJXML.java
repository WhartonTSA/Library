package org.whstsa.library.gui.api;

import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import org.whstsa.library.gui.api.Gui;

import java.io.IOException;

public class GuiJXML implements Gui {
    @Override
    public Scene draw() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("FXMLgui.fxml"));
            Scene primaryScene = new Scene(root);

            return primaryScene;
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }


    }

    @Override
    public String getUUID() {
        return "GUI_JXML";
    }
}
