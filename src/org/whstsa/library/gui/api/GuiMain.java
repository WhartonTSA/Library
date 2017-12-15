package org.whstsa.library.gui.api;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.StackPane;
import org.whstsa.library.api.impl.Person;
import org.whstsa.library.api.impl.library.Library;
import org.whstsa.library.db.ObjectDelegate;
import org.whstsa.library.gui.InterfaceManager;
import org.whstsa.library.gui.components.Table;
import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.util.ArrayUtils;
import org.whstsa.library.util.ClickHandler;

import java.util.ArrayList;
import java.util.List;

public class GuiMain implements Gui {

    @Override
    public Scene draw() {


        Button editLibraryButton = GuiUtils.createButton("Edit Library", GuiUtils.defaultClickHandler());
        Button deleteLibraryButton = GuiUtils.createButton("Delete Library", GuiUtils.defaultClickHandler());
        Button openLibraryButton = GuiUtils.createButton("Open Library", GuiUtils.defaultClickHandler());

        Table<Library> libraryTable = new Table<>();
        libraryTable.addColumn("Library Name", "name", true, TableColumn.SortType.DESCENDING, 100);
        List<Library> libraryList = ArrayUtils.castList(ObjectDelegate.getLibraries(), new ArrayList<Library>());
        libraryTable.addItems(libraryList);

        Button newLibraryButton = GuiUtils.createButton("New Library", new ClickHandler() {
            @Override
            public void onclick(Button button) {
                libraryList.add(new Library("Libraryyy"));
                libraryTable.setItems(libraryList);
                //display(new GuiNewLibrary());
            }
        });

        StackPane libraryButtonContainer = GuiUtils.createSplitPane(GuiUtils.Orientation.VERTICAL, newLibraryButton, editLibraryButton, deleteLibraryButton, openLibraryButton);

        StackPane libraryContainer = GuiUtils.createSplitPane(GuiUtils.Orientation.HORIZONTAL, libraryButtonContainer, libraryTable.getTable());

        Button newPersonButton = GuiUtils.createButton("New Person", GuiUtils.defaultClickHandler());
        Button editPersonButton = GuiUtils.createButton("Edit Person", GuiUtils.defaultClickHandler());
        Button deletePersonButton = GuiUtils.createButton("Delete Person", GuiUtils.defaultClickHandler());
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
