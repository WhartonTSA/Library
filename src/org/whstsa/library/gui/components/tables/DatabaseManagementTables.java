package org.whstsa.library.gui.components.tables;

import javafx.collections.ListChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.layout.StackPane;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.ObservableReference;
import org.whstsa.library.api.impl.library.Library;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.db.Loader;
import org.whstsa.library.db.ObjectDelegate;
import org.whstsa.library.gui.components.Table;
import org.whstsa.library.gui.dialogs.LibraryMetaDialogs;
import org.whstsa.library.gui.dialogs.PersonMetaDialogs;
import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManagementTables {

    public static StackPane libraryOverviewTable() {
        Table<ILibrary> libraryTable = new Table<>();
        libraryTable.addColumn("Library Name", "name", true, TableColumn.SortType.DESCENDING, 100);
        ObservableReference<List<ILibrary>> observableReference = () -> ObjectDelegate.getLibraries();
        libraryTable.setReference(observableReference);

        Button newLibraryButton = GuiUtils.createButton("New Library", (event) -> {
            LibraryMetaDialogs.createLibrary((library) -> {
                if (library == null) {
                    return;
                }
                Loader.getLoader().loadLibrary(library);
                libraryTable.pollItems();
            });
        });

        Button editLibraryButton = GuiUtils.createButton("Edit Library", (event) -> {
            ILibrary selectedLibrary = libraryTable.getSelected();
            if (selectedLibrary == null) {
                return;
            }
            LibraryMetaDialogs.updateLibrary(selectedLibrary, (library) -> {
                libraryTable.refresh();
            });
        });
        editLibraryButton.setDisable(true);

        Button deleteLibraryButton = GuiUtils.createButton("Delete Library", (event) -> {
            ILibrary selectedLibrary = libraryTable.getSelected();
            if (selectedLibrary == null) {
                return;
            }
            LibraryMetaDialogs.deleteLibrary(selectedLibrary, (library) -> {
                libraryTable.pollItems();
            });
        });
        Button openLibraryButton = GuiUtils.createButton("Open Library");

        libraryTable.getTable().getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            editLibraryButton.setDisable(newSelection == null);
        });

        StackPane libraryButtonContainer = GuiUtils.createSplitPane(GuiUtils.Orientation.VERTICAL, newLibraryButton, editLibraryButton, deleteLibraryButton, openLibraryButton);

        return GuiUtils.createSplitPane(GuiUtils.Orientation.HORIZONTAL, libraryButtonContainer, libraryTable.getTable());
    }

    public static StackPane personOverviewTable() {
        Table<IPerson> personTable = new Table<>();
        personTable.addColumn("First Name", "firstName", true, TableColumn.SortType.DESCENDING, 50);
        personTable.addColumn("Last Name", "lastName", true, TableColumn.SortType.DESCENDING, 50);
        ObservableReference<List<IPerson>> observableReference = () -> ObjectDelegate.getPeople();
        personTable.setReference(observableReference);

        Button newPersonButton = GuiUtils.createButton("New Person", event -> {
            PersonMetaDialogs.createPerson(person -> {
                if (person == null) {
                    return;
                }
                Loader.getLoader().loadPerson(person);
                personTable.pollItems();
            });
        });

        Button editPersonButton = GuiUtils.createButton("Edit Person", event -> {
            IPerson selectedPerson = personTable.getSelected();
            if (selectedPerson == null) {
                return;
            }
            PersonMetaDialogs.updatePerson(selectedPerson, person -> {
                personTable.refresh();
            });
        });
        editPersonButton.setDisable(true);

        Button deletePersonButton = GuiUtils.createButton("Delete Person", event -> {
            IPerson selectedPerson = personTable.getSelected();
            if (selectedPerson == null) {
                return;
            }
            PersonMetaDialogs.deletePerson(selectedPerson, person -> {
                personTable.pollItems();
            });
        });
        deletePersonButton.setDisable(true);

        personTable.getTable().getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean disabled = newSelection == null;
            editPersonButton.setDisable(disabled);
            deletePersonButton.setDisable(disabled);
        });

        StackPane personButtonContainer = GuiUtils.createSplitPane(GuiUtils.Orientation.VERTICAL, newPersonButton, editPersonButton, deletePersonButton);

        return GuiUtils.createSplitPane(GuiUtils.Orientation.HORIZONTAL, personTable.getTable(), personButtonContainer);
    }

}
