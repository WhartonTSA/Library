package org.whstsa.library.gui.api;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.StackPane;
import org.whstsa.library.api.impl.Person;
import org.whstsa.library.db.ObjectDelegate;
import org.whstsa.library.gui.components.Table;
import org.whstsa.library.gui.components.tables.DatabaseManagementTables;
import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.util.ArrayUtils;
import org.whstsa.library.util.ClickHandler;

import java.util.ArrayList;
import java.util.List;

public class GuiMain implements Gui {

    private ClickHandler defaultClickConsumer = (arg0) -> {};

    @Override
    public Scene draw() {

        StackPane libraryContainer = DatabaseManagementTables.libraryOverviewTable();

        Button newPersonButton = GuiUtils.createButton("New Person", this.defaultClickConsumer);
        Button editPersonButton = GuiUtils.createButton("Edit Person", this.defaultClickConsumer);
        Button deletePersonButton = GuiUtils.createButton("Delete Person", this.defaultClickConsumer);
        StackPane personButtonContainer = GuiUtils.createSplitPane(GuiUtils.Orientation.VERTICAL, newPersonButton, editPersonButton, deletePersonButton);

        Table<Person> personTable = new Table<>();
        personTable.addColumn("First Name", "firstName", true, TableColumn.SortType.DESCENDING, 50);
        personTable.addColumn("Last Name", "lastName", true, TableColumn.SortType.DESCENDING, 50);
        List<Person> personList = ArrayUtils.castList(ObjectDelegate.getPeople(), new ArrayList<Person>());
        personTable.addItems(personList);

        StackPane personContainer = GuiUtils.createSplitPane(GuiUtils.Orientation.HORIZONTAL, personTable.getTable(), personButtonContainer);

        StackPane container = GuiUtils.createTitledSplitPane("Library Manager", GuiUtils.Orientation.HORIZONTAL, libraryContainer, personContainer);

        return new Scene(container, 800, 512);
    }

    @Override
    public String getUUID() {
        return "GUI_MAIN";
    }

}
