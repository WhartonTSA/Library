package org.whstsa.library.api.books;

import org.whstsa.library.api.BookType;
import org.whstsa.library.api.Loadable;
import org.whstsa.library.api.Serializable;
import org.whstsa.library.api.Unique;

public interface IBook extends Loadable, Serializable, Unique {

    /**
     * Gets the book genre
     *
     * @return the book genre
     */
    BookType getType();

    /**
     * Gets the book title
     *
     * @return the book title
     */
    String getTitle();

    /**
     * Get the author of a book
     *
     * @return the author of the book
     */
    String getAuthor();

}
