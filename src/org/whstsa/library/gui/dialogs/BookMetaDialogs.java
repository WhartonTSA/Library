package org.whstsa.library.gui.dialogs;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.whstsa.library.api.BookType;
import org.whstsa.library.api.Callback;
import org.whstsa.library.api.ObservableReference;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.exceptions.InCirculationException;
import org.whstsa.library.api.impl.Book;
import org.whstsa.library.api.library.ICheckout;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.db.Loader;
import org.whstsa.library.db.ObjectDelegate;
import org.whstsa.library.gui.factories.GuiUtils;
import org.whstsa.library.gui.factories.LibraryManagerUtils;
import org.whstsa.library.gui.components.Element;
import org.whstsa.library.gui.components.Table;
import org.whstsa.library.gui.components.tables.BookStatusRow;
import org.whstsa.library.gui.factories.DialogBuilder;
import org.whstsa.library.gui.factories.DialogUtils;
import org.whstsa.library.util.BookStatus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookMetaDialogs {

    private static final String TITLE = "Title";
    private static final String AUTHOR = "Author";
    private static final String GENRE = "Genre";
    private static final String QUANTITY = "Copies";


    public static void createBook(Callback<IBook> callback, ObservableReference<ILibrary> libraryReference) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("New Book")
                .addTextField(TITLE)
                .addTextField(AUTHOR)
                .addChoiceBox(GENRE, LibraryManagerUtils.toObservableList(BookType.getGenres()), true, -1)
                .addSpinner(QUANTITY, true,0,100,5)
                .build();
        DialogUtils.getDialogResults(dialog, (results) -> {
            String title = results.get(TITLE).getString();
            String author = results.get(AUTHOR).getString();
            String type = results.get(GENRE).getString();
            BookType genre = BookType.getGenre(type);
            if (title != null && author != null && type != null) {
                IBook book = new Book(title, author, genre);
                int quantity = (int) results.get(QUANTITY).getResult();
                Loader.getLoader().loadBook(book);
                libraryReference.poll().addBook(book, quantity);
                callback.callback(book);
            }
        }, TITLE, AUTHOR, GENRE);
    }

    public static void updateBook(IBook book, Callback<IBook> callback, ObservableReference<ILibrary> libraryReference) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Update Person")
                .addTextField(TITLE, book.getName())
                .addTextField(AUTHOR, book.getAuthorName())
                .addChoiceBox(GENRE, LibraryManagerUtils.toObservableList(BookType.getGenres()), true, BookType.getGenreIndex(book.getType().getGenre()))
                .addSpinner(QUANTITY,true,0,100, libraryReference.poll().getQuantity(book.getID()))
                .build();
        DialogUtils.getDialogResults(dialog, (results) -> {
            String title = results.get(TITLE).getString();
            String author = results.get(AUTHOR).getString();
            BookType type = BookType.getGenre((String) results.get(GENRE).getResult());
            book.setTitle(title);
            book.setAuthor(author);
            book.setType(type);
            callback.callback(book);
        });
    }

    public static void deleteBook(IBook book, Callback<IBook> callback) {
        Dialog dialog = new DialogBuilder()
                .setTitle("Delete Member")
                .addButton(ButtonType.YES, true, event -> {
                    try {
                        ObjectDelegate.getLibraries().get(0).removeBook(book);
                    } catch (InCirculationException ex) {
                        DialogUtils.createDialog("Couldn't Remove Book. Book is currently checked out.", ex.getMessage(), null, Alert.AlertType.ERROR).show();
                        callback.callback(null);
                        return;
                    }
                    Loader.getLoader().unloadBook(book.getID());
                })
                .addButton(ButtonType.NO, true, event -> callback.callback(null))
                .setIsCancellable(false)
                .build();
        dialog.show();
    }

    public static void listCopies(IBook book, ObservableReference<ILibrary> libraryReference) {
        int availableCopies = libraryReference.poll().getCheckouts().get(book) != null ? libraryReference.poll().getQuantity(book.getID()) - libraryReference.poll().getCheckouts().get(book).size() : libraryReference.poll().getQuantity(book.getID());
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Copies")
                .addLabel("There are " + (availableCopies > 0 ? availableCopies : 0) + " available copy(s) of \"" + book.getName() + ".\"")
                .build();

        Table<BookStatusRow> copiesTable =  new Table<>();
        copiesTable = copiesManagerTable(copiesTable, book, libraryReference);

        GridPane dialogPane = (GridPane) dialog.getDialogPane().getContent();
        dialogPane.addRow(1, copiesTable.getTable());

        DialogUtils.getDialogResults(dialog, (results) -> {
        });
    }

    private static Table<BookStatusRow> copiesManagerTable(Table<BookStatusRow> mainTable, IBook book, ObservableReference<ILibrary> libraryReference) {
        DateFormat formattedDate = new SimpleDateFormat("MM/dd/yyyy");
        mainTable.addColumn("Copy", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getCopy() + ""), true, TableColumn.SortType.DESCENDING, 25);
        mainTable.addColumn("Owner Name", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getOwnerName()), true, TableColumn.SortType.DESCENDING, 100);
        mainTable.addColumn("Due Date", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getDueDate() == null ?  "N/A" : formattedDate.format(cellData.getValue().getDueDate())), true, TableColumn.SortType.DESCENDING, 50);
        mainTable.addColumn("Status", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getStatus().getString()), true, TableColumn.SortType.DESCENDING, 55);

        List<BookStatusRow> tableItems = FXCollections.observableArrayList();
        List<ICheckout> library = libraryReference.poll().getCheckouts().get(book);
        if (library == null) {
            library = new ArrayList<>();
        }
        for (int counter = 1; counter <= library.size(); counter++) {
            if (library.get(counter - 1).isOverdue()) {
                tableItems.add(new BookStatusRow(counter, BookStatus.OVERDUE, library.get(counter - 1).getOwner().getName(), library.get(counter - 1).getDueDate()));
            }
            else {
                tableItems.add(new BookStatusRow(counter, BookStatus.CHECKED_OUT, library.get(counter - 1).getOwner().getName(), library.get(counter - 1).getDueDate()));//This is where the data for the table is created
            }
        }
        for (int counter = library.size() + 1; counter <= libraryReference.poll().getBookQuantity().get(book.getID());counter++) {
            tableItems.add(new BookStatusRow(counter, BookStatus.AVAILABLE, "Nobody", null));
        }
        ObservableReference<List<BookStatusRow>> observableReference = () -> tableItems;
        mainTable.setReference(observableReference);
        mainTable.getTable().setSelectionModel(null);

        mainTable.getTable().setRowFactory(row -> new TableRow<BookStatusRow>() {
            @Override
            public void updateItem(BookStatusRow item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setStyle("");
                }
                else {
                    switch (item.getStatus()) {
                        case AVAILABLE: setStyle("-fx-background-color: #95edaf;"); break;
                        case CHECKED_OUT: setStyle("-fx-background-color: #ebff89;"); break;
                        case RESERVED: setStyle("-fx-background-color: #ffba75;"); break;
                        case OVERDUE: setStyle("-fx-background-color: #ff7575;"); break;
                    }
                    setTooltip(GuiUtils.createToolTip("Green indicates a book is available.\n " +
                            "Yellow indicates a book is checked out. \n" +
                            "Red indicates a book is overdue."));
                }
            }
        });

        return mainTable;
    }




}
