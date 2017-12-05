package org.whstsa.library.api.exceptions;

import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.library.ILibrary;

/**
 * Created by eric on 11/18/17.
 */
public class BookNotRegisteredException extends RuntimeException {

    private ILibrary library;
    private IBook book;

    public BookNotRegisteredException(ILibrary library, IBook book) {
        super();
        this.library = library;
        this.book = book;
    }

    @Override
    public String getMessage() {
        return this.book.getTitle() + " is not in the \"" + this.library.getName() + "\" book registry.";
    }

}
