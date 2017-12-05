package org.whstsa.library.gui.api;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import org.whstsa.library.api.Callback;
import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.util.ClickHandler;

import java.awt.*;

public class GuiMain implements Gui {

    private ClickHandler defaultClickConsumer = (arg0) -> {};

    @Override
    public Scene draw() {

        Button newLibraryButton = GuiUtils.createButton("New Library", this.defaultClickConsumer);
        Button editLibraryButton = GuiUtils.createButton("Edit Library", this.defaultClickConsumer);
        Button deleteLibraryButton = GuiUtils.createButton("Delete Library", this.defaultClickConsumer);
        Button openLibraryButton = GuiUtils.createButton("Open Library", this.defaultClickConsumer);
        StackPane libraryButtonContainer = GuiUtils.createSplitPane(GuiUtils.Orientation.VERTICAL, newLibraryButton, editLibraryButton, deleteLibraryButton, openLibraryButton);

        Button newPersonButton = GuiUtils.createButton("New Person", this.defaultClickConsumer);
        Button editPersonButton = GuiUtils.createButton("Edit Person", this.defaultClickConsumer);
        Button deletePersonButton = GuiUtils.createButton("Delete Person", this.defaultClickConsumer);
        StackPane personButtonContainer = GuiUtils.createSplitPane(GuiUtils.Orientation.VERTICAL, newPersonButton, editPersonButton, deletePersonButton);

        StackPane container = GuiUtils.createTitledSplitPane("Library Manager", GuiUtils.Orientation.HORIZONTAL, libraryButtonContainer, personButtonContainer);

        return new Scene(container, 512, 512);
    }

    @Override
    public String getUUID() {
        return "GUI_MAIN";
    }
}
