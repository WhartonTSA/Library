package org.whstsa.library.gui.api;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.whstsa.library.api.ObservableReference;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.api.library.IMember;
import org.whstsa.library.db.ObjectDelegate;
import org.whstsa.library.gui.components.Table;
import org.whstsa.library.gui.factories.GuiUtils;

import java.awt.*;
import java.util.List;

import static org.whstsa.library.gui.components.tables.DatabaseManagementTables.bookManagerTable;
import static org.whstsa.library.gui.components.tables.DatabaseManagementTables.libraryManagerTable;
import static org.whstsa.library.gui.components.tables.DatabaseManagementTables.memberManagerTable;

public class GuiLibraryManager implements Gui {

    @Override
    public Scene draw() {
        return new Scene(libraryManagerTable(), 1200, 800);
    }

    @Override
    public String getUUID() {
        return "GUI_LIBRARY_MANAGER";
    }

}
