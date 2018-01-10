package org.whstsa.library.gui.api;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.gui.components.tables.DatabaseManagementTables;
import org.whstsa.library.gui.components.SearchBarElement;

public class GuiLibraryManager implements Gui {

    private final ILibrary library;
    public final BorderPane window;

    public GuiLibraryManager(ILibrary lib) {
        library = lib;
        window = DatabaseManagementTables.libraryManagerTable(() -> this.library);
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

}
