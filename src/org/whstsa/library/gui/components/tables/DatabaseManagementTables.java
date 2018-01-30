package org.whstsa.library.gui.components.tables;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.whstsa.library.LibraryDB;
import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.ObservableReference;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.library.*;
import org.whstsa.library.db.Loader;
import org.whstsa.library.db.ObjectDelegate;
import org.whstsa.library.gui.api.GuiLibraryManager;
import org.whstsa.library.gui.api.GuiMain;
import org.whstsa.library.gui.api.GuiMenuBar;
import org.whstsa.library.gui.components.LabelElement;
import org.whstsa.library.gui.factories.LibraryManagerUtils;
import org.whstsa.library.gui.components.Table;
import org.whstsa.library.gui.dialogs.*;

import org.whstsa.library.gui.factories.GuiUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseManagementTables {

    public static StackPane libraryOverviewTable(LibraryDB libraryDB) {
        return libraryOverviewTable(libraryDB, new Table<>());
    }

    public static StackPane libraryOverviewTable(LibraryDB libraryDB, Table<ILibrary> libraryTable) {
        libraryTable.addColumn("Library Name", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getName()), true, TableColumn.SortType.DESCENDING, 100);
        ObservableReference<List<ILibrary>> observableReference = ObjectDelegate::getLibraries;
        libraryTable.setReference(observableReference);
        libraryTable.getTable().setOnMouseEntered(event -> libraryTable.refresh());
        libraryTable.getTable().setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                ILibrary selectedLibrary = libraryTable.getSelected();
                if (selectedLibrary == null) {
                    return;
                }
                libraryDB.getInterfaceManager().display(new GuiLibraryManager(selectedLibrary, libraryDB));
            }
        });

        Button newLibraryButton = GuiUtils.createButton("New Library", (event) ->
                LibraryMetaDialogs.createLibrary((library) -> {
                    if (library == null) {
                        return;
                    }
                    Loader.getLoader().loadLibrary(library);
                    libraryTable.refresh();
                })
        );

        Button editLibraryButton = GuiUtils.createButton("Edit Library", (event) -> {
            ILibrary selectedLibrary = libraryTable.getSelected();
            if (selectedLibrary == null) {
                return;
            }
            LibraryMetaDialogs.updateLibrary(selectedLibrary, (library) ->
                    libraryTable.refresh()
            );
        });
        editLibraryButton.setDisable(true);

        Button deleteLibraryButton = GuiUtils.createButton("Delete Library", true, (event) -> {
            ILibrary selectedLibrary = libraryTable.getSelected();
            if (selectedLibrary == null) {
                return;
            }
            LibraryMetaDialogs.deleteLibrary(selectedLibrary, (library) ->
                    libraryTable.refresh()
            );
        });
        Button openLibraryButton = GuiUtils.createButton("Open Library", true, (event) -> {
            ILibrary selectedLibrary = libraryTable.getSelected();
            if (selectedLibrary == null) {
                return;
            }
            libraryDB.getInterfaceManager().display(new GuiLibraryManager(selectedLibrary, libraryDB));
        });
        openLibraryButton.setDisable(true);

        libraryTable.getTable().getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            editLibraryButton.setDisable(newSelection == null);
            openLibraryButton.setDisable(newSelection == null);
        });

        StackPane libraryButtonContainer = GuiUtils.createSplitPane(GuiUtils.Orientation.VERTICAL, newLibraryButton, editLibraryButton, deleteLibraryButton, openLibraryButton);

        return GuiUtils.createSplitPane(GuiUtils.Orientation.HORIZONTAL, libraryButtonContainer, libraryTable.getTable());
    }

    public static StackPane personOverviewTable() {
        return personOverviewTable(new Table<>());
    }

    public static StackPane personOverviewTable(Table<IPerson> personTable) {
        personTable.addColumn("First Name", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getFirstName()), true, TableColumn.SortType.DESCENDING, 50);
        personTable.addColumn("Last Name", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getLastName()), true, TableColumn.SortType.DESCENDING, 50);
        ObservableReference<List<IPerson>> observableReference = ObjectDelegate::getPeople;
        personTable.setReference(observableReference);
        personTable.getTable().setOnMouseEntered(event -> personTable.refresh());
        personTable.getTable().setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                IPerson selectedPerson = personTable.getSelected();
                if (selectedPerson == null) {
                    return;
                }
                PersonMetaDialogs.updatePerson(selectedPerson, person ->
                        personTable.refresh()
                );
            }
        });

        Button newPersonButton = GuiUtils.createButton("New Person", event ->
                PersonMetaDialogs.createPerson(person -> {
                    if (person == null) {
                        return;
                    }
                    Loader.getLoader().loadPerson(person);
                    personTable.refresh();
                })
        );

        Button editPersonButton = GuiUtils.createButton("Edit Person", event -> {
            IPerson selectedPerson = personTable.getSelected();
            if (selectedPerson == null) {
                return;
            }
            PersonMetaDialogs.updatePerson(selectedPerson, person ->
                    personTable.refresh()
            );
        });
        editPersonButton.setDisable(true);

        Button deletePersonButton = GuiUtils.createButton("Delete Person", true, event -> {
            IPerson selectedPerson = personTable.getSelected();
            if (selectedPerson == null) {
                return;
            }
            PersonMetaDialogs.deletePerson(selectedPerson, person ->
                    personTable.refresh()
            );
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

    public static BorderPane libraryManagerTable(ObservableReference<ILibrary> libraryReference, LibraryDB libraryDB) {

        BorderPane mainContainer = new BorderPane();
        mainContainer.setTop(new VBox(new MenuBar(), LibraryManagerUtils.createTitleBar(libraryReference.poll().getName() + " Members")));

        Table<IMember> mainMemberTable = new Table<>();
        memberManagerTable(mainMemberTable, libraryReference);
        TableView<IMember> memberTableView = mainMemberTable.getTable();
        memberTableView.setId("memberTable");
        LibraryManagerUtils.addTooltip(mainMemberTable.getTable(), "Double-click to see a member's checked-out books.");
        mainMemberTable.refresh();

        Table<IBook> mainBookTable = new Table<>();
        bookManagerTable(mainBookTable, libraryReference);
        TableView<IBook> bookTableView = mainBookTable.getTable();
        bookTableView.setId("bookTable");
        LibraryManagerUtils.addTooltip(mainBookTable.getTable(), "Double-click to see the status of the copies of this book.");
        mainBookTable.refresh();

        GuiMenuBar mainMenuBar = new GuiMenuBar(mainBookTable, mainMemberTable, libraryReference, null, null);
        ((VBox) mainContainer.getTop()).getChildren().set(0, mainMenuBar.getMenu());

        Button back = GuiUtils.createButton("Back to Main Menu", true, event ->
                libraryDB.getInterfaceManager().display(new GuiMain(libraryDB))
        );

        //Toggle Button Group
        ToggleGroup viewToggleGroup = new ToggleGroup();

        ToggleButton viewMembers = GuiUtils.createToggleButton("Members");
        viewMembers.setUserData(true);
        viewMembers.setToggleGroup(viewToggleGroup);
        viewMembers.setSelected(true);
        ToggleButton viewBooks = GuiUtils.createToggleButton("Books");
        viewBooks.setToggleGroup(viewToggleGroup);
        viewBooks.setUserData(false);
        viewMembers.setDisable(true);

        HBox viewButtons = GuiUtils.createHBox(0, viewMembers, viewBooks);

        Button checkout = GuiUtils.createButton("Checkout", event -> {
            IMember selectedMember = mainMemberTable.getSelected();
            CheckoutMetaDialogs.checkoutMember(member -> {
                mainMemberTable.refresh();
                mainBookTable.refresh();
                viewBooks.setDisable(true);
                viewMembers.setDisable(false);
                viewBooks.setSelected(true);
                viewMembers.setSelected(false);

            }, selectedMember, mainContainer, mainBookTable, viewBooks, viewMembers, libraryReference);
        });
        checkout.setDisable(true);
        checkout.setStyle("-fx-base:#91c4e2;");

        Button checkin = GuiUtils.createButton("Return", event -> {
            IMember selectedMember = mainMemberTable.getSelected();
            CheckoutMetaDialogs.checkinMember(member -> {
                mainMemberTable.refresh();
                mainBookTable.refresh();
            }, selectedMember, mainContainer, mainBookTable, viewBooks, viewMembers, libraryReference);
        });
        checkin.setDisable(true);
        checkin.setStyle("-fx-base: #91c4e2;");

        Label membersLabel = GuiUtils.createLabel("Members", 16);
        Button memberNew = GuiUtils.createButton("New", event ->
                MemberMetaDialogs.createMember(member -> {
                    if (member == null) {
                        return;
                    }
                    Loader.getLoader().loadPerson(member);
                    mainMemberTable.refresh();
                }, libraryReference)
        );
        Button memberEdit = GuiUtils.createButton("Edit", event -> {
            IMember selectedMember = mainMemberTable.getSelected();
            if (selectedMember == null) {
                return;
            }
            MemberMetaDialogs.updateMember(selectedMember, member ->
                    mainMemberTable.refresh()
            );
        });
        memberEdit.setDisable(true);
        Button memberSearch = GuiUtils.createButton("Search", event ->
                GuiUtils.createSearchBar("memberSearch", "Search for Member:", LibraryManagerUtils.getMemberNames(libraryReference), mainContainer, libraryReference, mainMemberTable, "")
        );
        Button memberDelete = GuiUtils.createButton("Remove", event ->
                MemberMetaDialogs.deleteMember(mainMemberTable.getSelected(), member -> {
                    if (member == null) {
                        return;
                    }
                    mainMemberTable.refresh();
                })
        );
        memberDelete.setDisable(true);


        Label booksLabel = GuiUtils.createLabel("Books", 16);
        Button bookAdd = GuiUtils.createButton("Add", event ->
                BookMetaDialogs.createBook(book -> {
                    if (book == null) {
                        return;
                    }
                    Loader.getLoader().loadBook(book);
                    mainBookTable.refresh();
                }, libraryReference)
        );
        Button bookEdit = GuiUtils.createButton("Edit", event -> {
            IBook selectedBook = mainBookTable.getSelected();
            if (selectedBook == null) {
                return;
            }
            BookMetaDialogs.updateBook(selectedBook, book ->
                    mainBookTable.refresh()
            );
        });
        bookEdit.setDisable(true);
        Button bookDelete = GuiUtils.createButton("Delete", event -> {
            IBook selectedBook = mainBookTable.getSelected();
            if (selectedBook == null) {
                return;
            }
            BookMetaDialogs.deleteBook(selectedBook, book ->
                    mainBookTable.refresh()
            );
        });
        bookDelete.setDisable(true);
        Button bookSearch = GuiUtils.createButton("Search", event ->
                GuiUtils.createSearchBar("bookSearch", "Search for Book:", LibraryManagerUtils.getMemberNames(libraryReference), mainContainer, libraryReference, mainBookTable)
        );

        Button settingsButton = GuiUtils.createButton("Settings", GuiUtils.defaultClickHandler());
        Button refreshButton = GuiUtils.createButton("Refresh (debug)", true, event -> {
            mainBookTable.refresh();
            mainMemberTable.refresh();
            viewBooks.setDisable(false);
            viewMembers.setDisable(false);
        });

        mainMemberTable.getTable().getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            checkout.setDisable(newSelection == null);
            checkin.setDisable(newSelection == null);
            memberEdit.setDisable(newSelection == null);
            memberDelete.setDisable(newSelection == null);
        });

        mainBookTable.getTable().getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            bookEdit.setDisable(newSelection == null);
            bookDelete.setDisable(newSelection == null);
        });

        VBox buttonGroup = GuiUtils.createVBox(15, back, viewButtons,
                GuiUtils.createSeparator(), membersLabel, checkout, checkin, GuiUtils.createSeparator(), memberNew, memberEdit, memberSearch, memberDelete,
                booksLabel, bookAdd, bookEdit, bookDelete, bookSearch, settingsButton,
                GuiUtils.createSeparator(), refreshButton);
        buttonGroup.setSpacing(5.0);

        mainContainer.setLeft(buttonGroup);
        mainContainer.setCenter(memberTableView);
        viewToggleGroup.selectedToggleProperty().addListener((ov, toggle, new_toggle) -> {
            if (new_toggle != null) {
                if ((boolean) viewToggleGroup.getSelectedToggle().getUserData()) {
                    LibraryDB.LOGGER.debug("Switching to Member table");
                    mainContainer.setCenter(memberTableView);
                    ((VBox) mainContainer.getTop()).getChildren().set(1, LibraryManagerUtils.createTitleBar(libraryReference.poll().getName() + " Members"));
                    viewMembers.setDisable(true);
                    viewBooks.setDisable(false);

                }
                else {
                    LibraryDB.LOGGER.debug("Switching to Book table");
                    mainContainer.setCenter(bookTableView);
                    ((VBox) mainContainer.getTop()).getChildren().set(1, LibraryManagerUtils.createTitleBar(libraryReference.poll().getName() + " Books"));
                    viewMembers.setDisable(false);
                    viewBooks.setDisable(true);
                }
            }
        });

        return mainContainer;
    }

    private static void memberManagerTable(Table<IMember> mainTable, ObservableReference<ILibrary> libraryReference) {
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
    }

    private static void bookManagerTable(Table<IBook> mainTable, ObservableReference<ILibrary> libraryReference) {
        mainTable.addColumn("Title", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getTitle()), true, TableColumn.SortType.DESCENDING, 200);
        mainTable.addColumn("Author", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getAuthorName()), true, TableColumn.SortType.DESCENDING, 100);
        mainTable.addColumn("Genre", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getType().getGenre()), true, TableColumn.SortType.DESCENDING, 50);
        mainTable.addColumn("Checked out", (cellData) -> {
            ILibrary library = libraryReference.poll();
            List<ICheckout> checkouts = library.getCheckouts().get(cellData.getValue());
            boolean isCheckedOut = checkouts != null && checkouts.size() > 0;
            return new ReadOnlyStringWrapper(isCheckedOut ? "True" : "False");
        }, true, TableColumn.SortType.DESCENDING, 30);
        mainTable.addColumn("Due Date", (cellData) -> {
            ILibrary library = libraryReference.poll();
            List<ICheckout> checkouts = library.getCheckouts().get(cellData.getValue());
            boolean isCheckedOut = checkouts != null && checkouts.size() > 0;
            if (!isCheckedOut) {
                return new ReadOnlyStringWrapper("N/A");//If book isn't checked out
            }
            else {
                TableColumn<IBook, String> dateColumn = (TableColumn<IBook, String>) mainTable.getTable().getColumns().get(4);
                dateColumn.setCellFactory(param -> new TableCell<IBook, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        if (!(item == null) || !empty) {
                            DateFormat formattedDate = new SimpleDateFormat("MM/dd/yyyy");
                            try {
                                List<ICheckout> overdue = checkouts.stream().filter(ICheckout::isOverdue).collect(Collectors.toList());//If book is overdue
                                setText(formattedDate.format(overdue.get(0).getDueDate()));
                                setTextFill(Color.RED);
                            } catch (NullPointerException | IndexOutOfBoundsException ex) {
                                try {
                                    if (checkouts.size() > 1) {
                                        setText(formattedDate.format(checkouts.get(0).getDueDate()) + "...");//If book isn't overdue and there is more than one copy checked out
                                        setTextFill(Color.GREEN);
                                    } else {
                                        setText(formattedDate.format(checkouts.get(0).getDueDate()));//If book isn't overdue and only one copy is checked out
                                        setTextFill(Color.GREEN);
                                    }
                                } catch (NullPointerException e) {
                                    LibraryDB.LOGGER.debug("There was an error finding the due date.");//If there was an error
                                    setText("");
                                }
                            }
                        } else {
                            setTextFill(Color.BLACK);//If cell has no content, leave it blank (Omitting this caused the repeating date issue)
                        }
                    }
                });
                return new ReadOnlyStringWrapper(isCheckedOut ? checkouts.get(0).getDueDate().toString() : "Not checked out");//Don't really need this, but the cellValueProperty needs a return statement
            }
        }, true, TableColumn.SortType.DESCENDING, 40);
        ObservableReference<List<IBook>> observableReference = () -> libraryReference.poll().getBooks();
        mainTable.setReference(observableReference);
        mainTable.getTable().setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && mainTable.getSelected() != null) {
                LibraryDB.LOGGER.debug("Listing member's books");
                BookMetaDialogs.listCopies(t -> {}, mainTable.getSelected(), libraryReference);
            }
        });
    }

}
