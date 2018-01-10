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
    String getAuthorName();

    /**
     * Set the book genre
     *
     * @param type BookType
     */
    void setType(BookType type);

    /**
     * Set the book title
     *
     * @param title String
     */
    void setTitle(String title);

    /**
     * Set the book author
     *
     * @param authorName String
     */
    void setAuthor(String authorName);

}
