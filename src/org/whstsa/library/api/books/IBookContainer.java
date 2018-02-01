package org.whstsa.library.api.books;

import org.whstsa.library.api.Serializable;
import org.whstsa.library.api.Unique;

import java.util.UUID;

public interface IBookContainer extends IBookContainerReadonly, Serializable, Unique {

    /**
     * Adds a book to the book-container, and the amount of copies for that book
     *
     * @param book the book to add
     * @param quantity the amount of copies for book
     */
    void addBook(IBook book, int quantity);

    /**
     * Removes a book from the book-container
     *
     * @param book the book to remove
     */
    void removeBook(IBook book);

    /**
     * Adds a book to the book-container by its UUID (this method is discouraged because the book-container must fetch the book by its id)
     *
     * @param id the id of the book to add
     */
    void addBook(UUID id);

    /**
     * Removes a book from the book-container by its UUID (this method is discouraged because the book-container must fetch the book by its id)
     *
     * @param id the id of the book to remove
     */
    void removeBook(UUID id);
}
