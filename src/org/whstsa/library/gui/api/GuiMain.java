package org.whstsa.library.gui.api;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.whstsa.library.LibraryDB;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.gui.components.LabelElement;
import org.whstsa.library.gui.components.Table;
import org.whstsa.library.gui.components.tables.DatabaseManagementTables;
import org.whstsa.library.gui.factories.GuiUtils;

public class GuiMain implements Gui {

    private LibraryDB libraryDB;

    public GuiMain(LibraryDB libraryDB) {
        this.libraryDB = libraryDB;
    }

    @Override
    public Scene draw() {

        Table<ILibrary> libraryTable = new Table<>();
        Table<IPerson> personTable = new Table<>();

        GuiMenuBar menuBar = new GuiMenuBar(null, null, null, libraryTable, personTable, libraryDB, null);

        StackPane libraryContainer = DatabaseManagementTables.libraryOverviewTable(this.libraryDB, libraryTable);

        StackPane personContainer = DatabaseManagementTables.personOverviewTable(personTable);

        StackPane tableContainer = GuiUtils.createSplitPane(GuiUtils.Orientation.HORIZONTAL, libraryContainer, personContainer);

        LabelElement title = GuiUtils.createTitle("Library Manager");

        VBox container = new VBox(menuBar.getMenu(), title, tableContainer);
        container.setSpacing(10);
        container.setAlignment(Pos.TOP_CENTER);

        return new Scene(container, 800, 512);
    }

    @Override
    public String getUUID() {
        return "GUI_MAIN";
    }

}
