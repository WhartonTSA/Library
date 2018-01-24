package org.whstsa.library.gui.api;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.whstsa.library.LibraryDB;
import org.whstsa.library.api.impl.Person;
import org.whstsa.library.db.ObjectDelegate;
import org.whstsa.library.gui.InterfaceManager;
import org.whstsa.library.gui.components.Table;
import org.whstsa.library.gui.components.tables.DatabaseManagementTables;
import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.util.ArrayUtils;
import org.whstsa.library.util.ClickHandler;

import java.util.ArrayList;
import java.util.List;

public class GuiMain implements Gui {

    private LibraryDB libraryDB;

    public GuiMain(LibraryDB libraryDB) {
        this.libraryDB = libraryDB;
    }

    @Override
    public Scene draw() {

        MenuBar menuBar = new MenuBar(new Menu("File"));

        StackPane libraryContainer = DatabaseManagementTables.libraryOverviewTable(this.libraryDB);

        StackPane personContainer = DatabaseManagementTables.personOverviewTable();

        VBox container = new VBox(menuBar);
        container.setSpacing(10);

        container.getChildren().add(GuiUtils.createTitledSplitPane("Library Manager", GuiUtils.Orientation.HORIZONTAL, libraryContainer, personContainer));

        return new Scene(container, 800, 512);
    }

    @Override
    public String getUUID() {
        return "GUI_MAIN";
    }

}
