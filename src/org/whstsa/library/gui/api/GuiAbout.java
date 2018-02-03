package org.whstsa.library.gui.api;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import org.whstsa.library.LibraryDB;
import org.whstsa.library.api.ObservableReference;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.gui.components.LabelElement;
import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.gui.text.AboutText;

public class GuiAbout implements Gui {

    private LibraryDB libraryDB;
    private VBox window;

    public GuiAbout(LibraryDB libraryDB, ObservableReference<ILibrary> library) {
        this.libraryDB = libraryDB;

        Button backButton = GuiUtils.createButton("Back to Main Menu", true,
                library == null ? event -> libraryDB.getInterfaceManager().display(new GuiMain(libraryDB)) :
                        event -> libraryDB.getInterfaceManager().display(new GuiLibraryManager(library.poll(), this.libraryDB)));

        LabelElement title = GuiUtils.createTitle("About");

        AboutText text = new AboutText();
        TextFlow mainTextFlow = text.getTextFlow();
        System.out.println("Getting about text");
        mainTextFlow.setMaxWidth(800);

        VBox container = GuiUtils.createVBox(15, backButton, title, mainTextFlow);
        container.setSpacing(10);

        this.window = container;
    }


    @Override
    public Scene draw() {
        return new Scene(window, 800, 512);
    }

    @Override
    public String getUUID() {
        return "GUI_ABOUT";
    }
}
