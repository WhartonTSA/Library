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
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.db.Loader;
import org.whstsa.library.db.ObjectDelegate;
import org.whstsa.library.gui.factories.LibraryManagerUtils;
import org.whstsa.library.gui.components.Element;
import org.whstsa.library.gui.components.Table;
import org.whstsa.library.gui.components.tables.BookStatusRow;
import org.whstsa.library.gui.factories.DialogBuilder;
import org.whstsa.library.gui.factories.DialogUtils;
import org.whstsa.library.util.BookStatus;

import java.util.List;
import java.util.Map;

public class BookMetaDialogs {

    private static final String TITLE = "Title";
    private static final String AUTHOR = "Author";
    private static final String GENRE = "Genre";


    public static void createBook(Callback<IBook> callback, ObservableReference<ILibrary> libraryReference) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("New Book")
                .addTextField(TITLE)
                .addTextField(AUTHOR)
                .addChoiceBox(GENRE, LibraryManagerUtils.toObservableList(BookType.getGenres()), true, -1)
                .build();
        DialogUtils.getDialogResults(dialog, (results) -> {
            String title = results.get(TITLE).getString();
            String author = results.get(AUTHOR).getString();
            String type = results.get(GENRE).getString();
            BookType genre = BookType.getGenre(type);
            IBook book = new Book(title, author, genre);
            Loader.getLoader().loadBook(book);
            libraryReference.poll().addBook(book);
            callback.callback(book);
        }, TITLE, AUTHOR, GENRE);
    }

    public static void updateBook(IBook book, Callback<IBook> callback) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Update Person")
                .addTextField(TITLE, book.getTitle())
                .addTextField(AUTHOR, book.getAuthorName())
                .addChoiceBox(GENRE, LibraryManagerUtils.toObservableList(BookType.getGenres()), true, BookType.getGenreIndex(book.getType().getGenre()))
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
                .addButton(ButtonType.NO, true, event -> {
                    callback.callback(null);
                })
                .setIsCancellable(false)
                .build();
        dialog.show();
    }

    public static void listCopies(Callback<IBook> callback, IBook book, ObservableReference<ILibrary> libraryReference) {
        Dialog<Map<String, Element>> dialog = new DialogBuilder()
                .setTitle("Copies")
                .addLabel("There are " + libraryReference.poll().getBooks().size() * 4 + " copies of \"" + book.getTitle() + ".\"")//TODO Add getQuantity (And add a ternary for copy/copies)
                .build();

        Table<BookStatusRow> copiesTable =  new Table<>();
        copiesTable = copiesManagerTable(copiesTable, libraryReference);

        GridPane dialogPane = (GridPane) dialog.getDialogPane().getContent();
        dialogPane.addRow(1, copiesTable.getTable());

        DialogUtils.getDialogResults(dialog, (results) -> {
        });

    }

    private static Table<BookStatusRow> copiesManagerTable(Table<BookStatusRow> mainTable, ObservableReference<ILibrary> libraryReference) {
        mainTable.addColumn("Copy", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getCopy() + ""), true, TableColumn.SortType.DESCENDING, 25);
        mainTable.addColumn("Status", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getStatus().getString()), true, TableColumn.SortType.DESCENDING, 55);
        mainTable.addColumn("Owner Name", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getOwnerName()), true, TableColumn.SortType.DESCENDING, 100);

        List<BookStatusRow> tableItems = FXCollections.observableArrayList();
        for (int i = 1, step = 0; step < libraryReference.poll().getBooks().size(); i+=4, step++) {//TODO change to getBookMap.filter.blahblahblah I just need the books
            tableItems.add(new BookStatusRow(i, BookStatus.AVAILABLE, "Nobody"));
            tableItems.add(new BookStatusRow(i + 1, BookStatus.CHECKED_OUT, "Somebody"));
            tableItems.add(new BookStatusRow(i + 2, BookStatus.RESERVED, "Somebody"));
            tableItems.add(new BookStatusRow(i + 3, BookStatus.UNAVAILABLE, "Who Knows"));
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
                        case UNAVAILABLE: setStyle("-fx-background-color: #ff7575;"); break;
                    }
                }
            }
        });

        return mainTable;
    }




}
