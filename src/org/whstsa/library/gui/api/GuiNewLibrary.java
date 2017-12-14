package org.whstsa.library.gui.api;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.util.ClickHandler;

public class GuiNewLibrary implements Gui {

    private ClickHandler defaultClickHandler = (arg0) -> {};

    @Override
    public Scene draw() {

        Button createNewLibraryButton = GuiUtils.createButton("Create New Library", this.defaultClickHandler);
        Button cancelButton = GuiUtils.createButton("Cancel", this.defaultClickHandler);
        cancelButton.addEventHandler(ActionEvent.ACTION, event -> GuiUtils.defaultCloseOperation(event));

        StackPane container = GuiUtils.createTitledSplitPane("Library Manager", GuiUtils.Orientation.HORIZONTAL, createNewLibraryButton, cancelButton);
        return new Scene(container, 500, 200);
    }
    @Override
    public String getUUID() {
        return "GUI_NEW_LIBRARY";
    }
}
