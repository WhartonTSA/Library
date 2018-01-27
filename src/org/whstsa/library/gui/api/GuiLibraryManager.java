package org.whstsa.library.gui.api;

import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.whstsa.library.LibraryDB;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.gui.components.tables.DatabaseManagementTables;
import org.whstsa.library.gui.components.SearchBarElement;

public class GuiLibraryManager implements Gui {

    private final ILibrary library;
    private final BorderPane window;
    private final String stageTitle;

    public GuiLibraryManager(ILibrary lib, LibraryDB libraryDB) {
        this.library = lib;
        this.stageTitle = lib.getName() + " - Library Manager 1.0";
        this.window = DatabaseManagementTables.libraryManagerTable(() -> this.library, libraryDB);
    }

    public ILibrary getActiveLibrary() {
        return library;
    }

    public BorderPane getWindow() {
        return window;
    }

    @Override
    public Scene draw() {
        return new Scene(window, 1200, 800);
    }

    @Override
    public String getUUID() {
        return "GUI_LIBRARY_MANAGER";
    }

    public String getStageTitle() { return stageTitle;}

}
