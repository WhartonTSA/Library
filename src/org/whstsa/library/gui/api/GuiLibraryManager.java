package org.whstsa.library.gui.api;

import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import org.whstsa.library.gui.factories.GuiUtils;

public class GuiLibraryManager implements Gui {

    @Override
    public Scene draw() {

        TableView mainTable = new TableView();

        StackPane mainContainer = GuiUtils.createSplitPane(GuiUtils.Orientation.HORIZONTAL, mainTable);

        return new Scene(mainContainer, 1200, 800);
    }

    @Override
    public String getUUID() {
        return "GUI_LIBRARY_MANAGER";
    }

}
