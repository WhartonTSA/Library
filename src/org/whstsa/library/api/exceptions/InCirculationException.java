package org.whstsa.library.api.exceptions;

import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.library.ILibrary;

/**
 * Thrown whenever a book is attempted to be removed from a library
 * but is currently checked out, meaning it is still in-circulation
 * and cannot be pulled from the registry.
 */
public class InCirculationException extends RuntimeException {

    private IBook book;
    private ILibrary library;

    public InCirculationException(ILibrary library, IBook book) {
        this.book = book;
        this.library = library;
    }

    @Override
    public String getMessage() {
        return "Cannot pull " + book.getName() + " from " + this.library.getName() + " registry as it is still in circulation.";
    }

}
