package org.whstsa.library.gui.api;

import javafx.scene.Scene;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.gui.components.tables.DatabaseManagementTables;

public class GuiLibraryManager implements Gui {

    public final ILibrary library;

    GuiLibraryManager(ILibrary lib) {
        library = lib;
    }
        return library;
    }

    @Override
    public Scene draw() {
        return new Scene(DatabaseManagementTables.libraryManagerTable(), 1200, 800);
    }

    @Override
    public String getUUID() {
        return "GUI_LIBRARY_MANAGER";
    }

}
