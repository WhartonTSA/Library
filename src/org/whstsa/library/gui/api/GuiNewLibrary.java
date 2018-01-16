package org.whstsa.library.gui.api;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.util.ClickHandler;

public class GuiNewLibrary implements Gui {


    @Override
    public Scene draw() {

        Button createButton = GuiUtils.createButton("Create", GuiUtils.defaultClickHandler());
        Button cancelButton = GuiUtils.createButton("Cancel", GuiUtils.defaultClickHandler());
        cancelButton.addEventHandler(ActionEvent.ACTION, event -> GuiUtils.defaultCloseOperation(event));

       // InputGroup newLibraryTextField = GuiUtils.createInputGroup("New Library Text Field", true);

        //StackPane inputContainer = GuiUtils.createSplitPane(GuiUtils.Orientation.HORIZONTAL, newLibraryTextField.getNode());
        StackPane buttonContainer = GuiUtils.createSplitPane(GuiUtils.Orientation.HORIZONTAL, createButton, cancelButton);
      //  StackPane mainContainer = GuiUtils.createTitledSplitPane("Create a new Library", GuiUtils.Orientation.HORIZONTAL, inputContainer, buttonContainer);
    //    return new Scene(mainContainer, 500, 200);
        return null;
    }
    @Override
    public String getUUID() {
        return "GUI_NEW_LIBRARY";
    }
}
