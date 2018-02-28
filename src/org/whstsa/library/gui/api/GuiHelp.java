package org.whstsa.library.gui.api;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.whstsa.library.LibraryDB;
import org.whstsa.library.api.ObservableReference;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.gui.text.HelpText;

public class GuiHelp implements Gui {

    private LibraryDB libraryDB;
    private BorderPane window;
    private HelpText helpText;

    public GuiHelp(LibraryDB libraryDB, ObservableReference<ILibrary> library) {
        this.libraryDB = libraryDB;

        Button backButton = GuiUtils.createButton("Back to Main Menu", true,
                library == null ? event -> libraryDB.getInterfaceManager().display(new GuiMain(libraryDB)) :
                        event -> libraryDB.getInterfaceManager().display(new GuiLibraryManager(library.poll(), this.libraryDB)));
        VBox buttonBar = GuiUtils.createVBox(backButton);
        buttonBar.setAlignment(Pos.CENTER);

        helpText = new HelpText();

        Pagination pageSelect = new Pagination();
        pageSelect.setPageFactory(this::displayPage);
        pageSelect.setPageCount(helpText.getPageAmount());

        BorderPane container = new BorderPane();
        container.setCenter(pageSelect);
        container.setTop(buttonBar);

        this.window = container;
    }

    private VBox displayPage(int pageIndex) {
        return helpText.getContent(pageIndex);
    }


    @Override
    public Scene draw() {
        return new Scene(window, 800, 600);
    }

    @Override
    public String getUUID() {
        return "GUI_HELP";
    }
}
