package org.whstsa.library.gui.api;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import org.whstsa.library.LibraryDB;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.gui.components.tables.DatabaseManagementTables;

public class GuiLibraryManager implements Gui {

    private final ILibrary library;
    private final BorderPane window;

    public GuiLibraryManager(ILibrary lib, LibraryDB libraryDB) {
        this.library = lib;
        this.window = DatabaseManagementTables.libraryManagerTable(() -> this.library, libraryDB);
    }

    @Override
    public Scene draw() {
        return new Scene(window, 1200, 800);
    }

    @Override
    public String getUUID() {
        return "GUI_LIBRARY_MANAGER_" + library.getName().toUpperCase();
    }

}
