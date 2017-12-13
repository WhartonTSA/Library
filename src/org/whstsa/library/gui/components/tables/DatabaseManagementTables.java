package org.whstsa.library.gui.components.tables;

import javafx.collections.ListChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.layout.StackPane;
import org.whstsa.library.api.impl.library.Library;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.db.Loader;
import org.whstsa.library.db.ObjectDelegate;
import org.whstsa.library.gui.components.Table;
import org.whstsa.library.gui.dialogs.LibraryMetaDialogs;
import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManagementTables {

    public static StackPane libraryOverviewTable() {
        Table<Library> libraryTable = new Table<>();
        libraryTable.addColumn("Library Name", "name", true, TableColumn.SortType.DESCENDING, 100);
        List<Library> libraryList = ArrayUtils.castList(ObjectDelegate.getLibraries(), new ArrayList<Library>());
        libraryTable.addItems(libraryList);

        Button newLibraryButton = GuiUtils.createButton("New Library", (event) -> {
            LibraryMetaDialogs.createLibrary((library) -> {
                if (library == null) {
                    return;
                }
                Loader.getLoader().loadLibrary(library);
                libraryTable.addItem((Library) library);
            });
        });
        Button editLibraryButton = GuiUtils.createButton("Edit Library", (event) -> {
            Library selectedLibrary = getSelectedLibrary(libraryTable);
            if (selectedLibrary == null) {
                return;
            }
            LibraryMetaDialogs.updateLibrary(getSelectedLibrary(libraryTable), (library) -> {
                libraryTable.refresh();
            });
        });
        editLibraryButton.setDisable(true);
        Button deleteLibraryButton = GuiUtils.createButton("Delete Library");
        Button openLibraryButton = GuiUtils.createButton("Open Library");

        libraryTable.getTable().getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            editLibraryButton.setDisable(newSelection == null);
        });

        StackPane libraryButtonContainer = GuiUtils.createSplitPane(GuiUtils.Orientation.VERTICAL, newLibraryButton, editLibraryButton, deleteLibraryButton, openLibraryButton);

        return GuiUtils.createSplitPane(GuiUtils.Orientation.HORIZONTAL, libraryButtonContainer, libraryTable.getTable());
    }

    private static Library getSelectedLibrary(Table<Library> libraryTable) {
        return libraryTable.getTable().getSelectionModel().getSelectedItem();
    }

}
