package org.whstsa.library.api.books;

import org.whstsa.library.api.Serializable;
import org.whstsa.library.api.Unique;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This interface represents a read-only book-container.
 */
public interface IBookContainerReadonly extends Serializable, Unique {

    /**
     * Returns a list of books this book-container has
     *
     * @return the list of books
     */
    List<IBook> getBooks();

    /**
     * Returns a map with the key being book UUIDs and the values being IBooks
     *
     * @return the map of books
     */
    Map<UUID, IBook> getBookMap();

    /**
     * Returns a list of UUIDs representing book UUIDs
     *
     * @return the list of UUIDs
     */
    List<UUID> getBookIDs();

    /**
     * Returns whether this book-container has a book with the given ID (discouraged because the book-container must locate a book with this id)
     *
     * @param id the id to check for
     * @return whether the container has the book
     */
    boolean hasBook(UUID id);

    /**
     * Returns whether this book-container has the given book
     *
     * @param book the book to check for
     * @return whether the container has the book
     */
    boolean hasBook(IBook book);
}
