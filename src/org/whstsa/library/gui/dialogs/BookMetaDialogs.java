package org.whstsa.library.gui.dialogs;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.whstsa.library.LibraryDB;
import org.whstsa.library.api.BookType;
import org.whstsa.library.api.Callback;
import org.whstsa.library.api.ObservableReference;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.exceptions.InCirculationException;
import org.whstsa.library.api.impl.Book;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.api.library.IMember;
import org.whstsa.library.db.Loader;
import org.whstsa.library.db.ObjectDelegate;
import org.whstsa.library.gui.api.LibraryManagerUtils;
import org.whstsa.library.gui.components.Element;
import org.whstsa.library.gui.components.Table;
import org.whstsa.library.gui.components.tables.BookStatusRow;
import org.whstsa.library.gui.factories.DialogBuilder;
import org.whstsa.library.gui.factories.DialogUtils;
import org.whstsa.library.gui.factories.GuiUtils;

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
                .setTitle("There are " + " copies of \"" + book.getTitle() + "\"")//TODO Add getQuantity
                .build();
        GridPane dialogPane = (GridPane) dialog.getDialogPane().getContent();
        VBox bookBox = GuiUtils.createVBox(5, null);
        for (int i = 0; i < libraryReference.poll().getBooks().size(); i++) {//TODO replace with getQuantity
            TableView table = new TableView();//TODO Use CSS to change the background color of the HBox when the book is checked out/reserved/available
        }
        DialogUtils.getDialogResults(dialog, (results) -> {
        });

    }

    public static Table<BookStatusRow> memberManagerTable(Table<BookStatusRow> mainTable, ObservableReference<ILibrary> libraryReference) {
        mainTable.addColumn("Copy", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getCopy() + ""), true, TableColumn.SortType.DESCENDING, 100);
        mainTable.addColumn("Last Name", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getStatus().getString()), true, TableColumn.SortType.DESCENDING, 100);
        mainTable.addColumn("Teacher", (cellData) -> new ReadOnlyStringWrapper(cellData.getValue().getOwnerName()), true, TableColumn.SortType.DESCENDING, 50);
        ObservableReference<List<IMember>> observableReference = () -> libraryReference.poll().getMembers();//TODO change to getBookMap.filter.blahblahblah I just need the books
        //mainTable.setReference(observableReference);
        return mainTable;
    }




}
