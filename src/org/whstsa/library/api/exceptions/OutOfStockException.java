package org.whstsa.library.api.exceptions;

import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.library.ILibrary;

public class OutOfStockException extends Exception {

    private IBook book;
    private ILibrary library;

    public OutOfStockException(IBook book, ILibrary library) {
        this.book = book;
        this.library = library;
    }

    @Override
    public String getMessage() {
        return library.getName() + "does not have any more " + book.getTitle();
    }

}
