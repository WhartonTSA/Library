package org.whstsa.library.gui.components.tables;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.whstsa.library.LibraryDB;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.ObservableReference;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.impl.library.Library;
import org.whstsa.library.api.library.ICheckout;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.api.library.IMember;
import org.whstsa.library.db.Loader;
import org.whstsa.library.db.ObjectDelegate;
import org.whstsa.library.gui.api.GuiLibraryManager;
import org.whstsa.library.gui.api.LibraryManagerUtils;
import org.whstsa.library.gui.components.SearchBarElement;
import org.whstsa.library.gui.components.Table;
import org.whstsa.library.gui.dialogs.*;

import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.util.ArrayUtils;

import java.beans.EventHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DatabaseManagementTables {

    public static StackPane libraryOverviewTable(LibraryDB libraryDB) {
        Table<ILibrary> libraryTable = new Table<>();
        libraryTable.addColumn("Library Name", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getName()), true, TableColumn.SortType.DESCENDING, 100);
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

        Button deleteLibraryButton = GuiUtils.createButton("Delete Library", true, (event) -> {
            ILibrary selectedLibrary = libraryTable.getSelected();
            if (selectedLibrary == null) {
                return;
            }
            LibraryMetaDialogs.deleteLibrary(selectedLibrary, (library) -> {
                libraryTable.pollItems();
            });
        });
        Button openLibraryButton = GuiUtils.createButton("Open Library", true, (event) -> {
            ILibrary selectedLibrary = libraryTable.getSelected();
            if (selectedLibrary == null) {
                return;
            }
            libraryDB.getInterfaceManager().display(new GuiLibraryManager(selectedLibrary));
        });

        libraryTable.getTable().getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            editLibraryButton.setDisable(newSelection == null);
        });

        StackPane libraryButtonContainer = GuiUtils.createSplitPane(GuiUtils.Orientation.VERTICAL, newLibraryButton, editLibraryButton, deleteLibraryButton, openLibraryButton);

        return GuiUtils.createSplitPane(GuiUtils.Orientation.HORIZONTAL, libraryButtonContainer, libraryTable.getTable());
    }

    public static StackPane personOverviewTable() {
        Table<IPerson> personTable = new Table<>();
        personTable.addColumn("First Name", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getFirstName()), true, TableColumn.SortType.DESCENDING, 50);
        personTable.addColumn("Last Name", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getLastName()), true, TableColumn.SortType.DESCENDING, 50);
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

        Button deletePersonButton = GuiUtils.createButton("Delete Person", true, event -> {
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

    public static BorderPane libraryManagerTable(ObservableReference<ILibrary> libraryReference) {
        /*
        * Reminders for extra features
        *
        * TODO ability to edit member's wallets
        * TODO checkout dialog will check if member has enough money to pay for fine
        * TODO add member dialog will give user the option to create member from existing person
        * TODO fix formatting in dialogs and search bar
        * TODO add functionality to listbooks command
        * TODO add method for getting copies of books
        *
        */
        BorderPane mainContainer = new BorderPane();

        Table<IMember> mainMemberTable = new Table<>();
        memberManagerTable(mainMemberTable, libraryReference);
        TableView<IMember> memberTableView = mainMemberTable.getTable();
        memberTableView.setId("memberTable");

        Table<IBook> mainBookTable = new Table<>();
        bookManagerTable(mainBookTable, libraryReference);
        TableView<IBook> bookTableView = mainBookTable.getTable();
        bookTableView.setId("bookTable");

        //Toggle Button Group
        ToggleGroup viewButtons = new ToggleGroup();

        ToggleButton viewMembers = GuiUtils.createToggleButton("Members");
        viewMembers.setUserData(true);
        viewMembers.setToggleGroup(viewButtons);
        viewMembers.setSelected(true);
        ToggleButton viewBooks = GuiUtils.createToggleButton("Books");
        viewBooks.setToggleGroup(viewButtons);
        viewBooks.setUserData(false);


        HBox viewSwitch = GuiUtils.createHBox(2, viewMembers, viewBooks);

        Button checkout = GuiUtils.createButton("Checkout", event -> {
            IMember selectedMember = mainMemberTable.getSelected();
            CheckoutMetaDialogs.checkoutMember(member -> {
                mainMemberTable.refresh();
            }, selectedMember, libraryReference);
        });

        Label membersLabel = GuiUtils.createLabel("Members", 16);
        Button memberNew = GuiUtils.createButton("New", event -> {
            MemberMetaDialogs.createMember(member -> {
                if (member == null) {
                    return;
                }
                Loader.getLoader().loadPerson(member);
                mainMemberTable.refresh();
            }, libraryReference);
        });
        Button memberEdit = GuiUtils.createButton("Edit", event -> {
            IMember selectedMember = mainMemberTable.getSelected();
            if (selectedMember == null) {
                return;
            }
            MemberMetaDialogs.updateMember(selectedMember, member -> {
                mainMemberTable.refresh();
            });
        });
        Button memberSearch = GuiUtils.createButton("Search", event -> {
            GuiUtils.createSearchBar("Member:", LibraryManagerUtils.getMemberNames(libraryReference), mainContainer, libraryReference);
        });
        Button memberDelete = GuiUtils.createButton("Remove", event -> {
            MemberMetaDialogs.deleteMember(mainMemberTable.getSelected(), member -> {
                if (member == null) {
                    return;
                }
                mainMemberTable.pollItems();
            });
        });


        Label booksLabel = GuiUtils.createLabel("Books", 16);
        Button bookAdd = GuiUtils.createButton("Add", event -> {
            BookMetaDialogs.createBook(book -> {
                if (book == null) {
                return;
                }
                Loader.getLoader().loadBook(book);
                mainBookTable.pollItems();
            }, libraryReference);
        });
        Button bookEdit = GuiUtils.createButton("Edit", event -> {
            IBook selectedBook = mainBookTable.getSelected();
            if (selectedBook == null) {
                return;
            }
            BookMetaDialogs.updateBook(selectedBook, book -> {
                mainBookTable.refresh();
            });
        });
        Button bookDelete = GuiUtils.createButton("Delete", event -> {
            IBook selectedBook = mainBookTable.getSelected();
            if (selectedBook == null) {
                return;
            }
            BookMetaDialogs.deleteBook(selectedBook, book -> {
                mainBookTable.pollItems();
            });
        });
        Button bookSearch = GuiUtils.createButton("Search", event -> {
            GuiUtils.createSearchBar("Book:", LibraryManagerUtils.getMemberNames(libraryReference), mainContainer, libraryReference);
        });

        Button settingsButton = GuiUtils.createButton("Settings", GuiUtils.defaultClickHandler());
        Button refreshButton = GuiUtils.createButton("Refresh", event -> {
            mainBookTable.refresh();
            mainMemberTable.refresh();
        });

        mainMemberTable.getTable().getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            memberEdit.setDisable(newSelection == null);
        });

        mainBookTable.getTable().getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            bookEdit.setDisable(newSelection == null);
        });

        VBox buttonGroup = GuiUtils.createVBox(15, viewSwitch,
                GuiUtils.createSeparator(), membersLabel, checkout, GuiUtils.createSeparator(), memberNew, memberEdit, memberSearch, memberDelete,
                booksLabel, bookAdd, bookEdit, bookDelete, bookSearch, settingsButton,
                GuiUtils.createSeparator(), refreshButton);
        buttonGroup.setSpacing(5.0);

        mainContainer.setLeft(buttonGroup);
        mainContainer.setCenter(memberTableView);

        viewButtons.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) {
                if (new_toggle == null) {
                    return;
                }
                else {
                    if ((boolean) viewButtons.getSelectedToggle().getUserData()) {
                        LibraryDB.LOGGER.debug("Swictching to Member table");
                        mainContainer.setCenter(memberTableView);

                    }
                    else {
                        LibraryDB.LOGGER.debug("Swictching to Book table");
                        mainContainer.setCenter(bookTableView);
                    }
                }

            }
        });

        Consumer<Integer> switchView = (putAnyNumberHere) -> {
            if (mainContainer.getCenter().equals(memberTableView)) {
                mainContainer.setCenter(bookTableView);
            }
            else {
                mainContainer.setCenter(memberTableView);
            }
        };

        return mainContainer;
    }

    public static Table<IMember> memberManagerTable(Table<IMember> mainTable, ObservableReference<ILibrary> libraryReference) {
        mainTable.addColumn("First Name", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getPerson().getFirstName()), true, TableColumn.SortType.DESCENDING, 100);
        mainTable.addColumn("Last Name", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getPerson().getLastName()), true, TableColumn.SortType.DESCENDING, 100);
        mainTable.addColumn("Teacher", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getPerson().isTeacher() + ""), true, TableColumn.SortType.DESCENDING, 50);
        mainTable.addColumn("Fines", (cellData) -> new ReadOnlyStringWrapper("$" + cellData.getValue().getFine()), true, TableColumn.SortType.DESCENDING, 25);
        mainTable.addColumn("Books", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getBooks().size() + ""), true, TableColumn.SortType.DESCENDING, 25);
        ObservableReference<List<IMember>> observableReference = () -> libraryReference.poll().getMembers();
        mainTable.setReference(observableReference);
        mainTable.getTable().setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                LibraryDB.LOGGER.debug("Listing member's books");
                MemberMetaDialogs.listBooks(t -> {}, mainTable.getSelected());
            }
        });
        return mainTable;
    }

    public static Table<IBook> bookManagerTable(Table<IBook> mainTable, ObservableReference<ILibrary> libraryReference) {
        mainTable.addColumn("Title", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getTitle()), true, TableColumn.SortType.DESCENDING, 200);
        mainTable.addColumn("Author", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getAuthorName()), true, TableColumn.SortType.DESCENDING, 100);
        mainTable.addColumn("Genre", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getType().getGenre()), true, TableColumn.SortType.DESCENDING, 50);
        mainTable.addColumn("Copies", (cellData) -> new ReadOnlyStringWrapper("1"), true, TableColumn.SortType.DESCENDING, 25);
        mainTable.addColumn("Checked out", (cellData) -> {
            ILibrary library = libraryReference.poll();
            List<ICheckout> checkouts = library.getCheckouts().get(cellData.getValue());
            boolean isCheckedOut = checkouts != null && checkouts.size() > 0;
            return new ReadOnlyStringWrapper(isCheckedOut ? "True" : "False");
        }, true, TableColumn.SortType.DESCENDING, 30);
        ObservableReference<List<IBook>> observableReference = () -> libraryReference.poll().getBooks();
        mainTable.setReference(observableReference);
        return mainTable;
    }

}
