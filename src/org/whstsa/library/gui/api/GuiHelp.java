package org.whstsa.library.gui.api;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.whstsa.library.LibraryDB;
import org.whstsa.library.api.ObservableReference;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.gui.components.LabelElement;
import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.gui.text.AboutText;
import org.whstsa.library.gui.text.HelpText;

public class GuiHelp implements Gui {

    private LibraryDB libraryDB;
    private BorderPane window;
    private HelpText helpText;
    private int pages;

    public GuiHelp(LibraryDB libraryDB, ObservableReference<ILibrary> library) {
        this.libraryDB = libraryDB;

        Button backButton = GuiUtils.createButton("Back to Main Menu", true,
                library == null ? event -> libraryDB.getInterfaceManager().display(new GuiMain(libraryDB)) :
                        event -> libraryDB.getInterfaceManager().display(new GuiLibraryManager(library.poll(), this.libraryDB)));

        LabelElement title = GuiUtils.createTitle("About");

        helpText = new HelpText();

        Text content = AboutText.getText();
        TextFlow mainTextFlow = new TextFlow(content);
        mainTextFlow.setMaxWidth(800);

        Pagination pageSelect = new Pagination();
        pageSelect.setPageFactory(this::displayPage);

        BorderPane container = new BorderPane();
        container.setCenter(content);
        container.setTop(backButton);
        container.setBottom(pageSelect);

        this.window = container;
    }

    private VBox displayPage(int pageIndex) {
        return helpText.getContent(pageIndex);
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
