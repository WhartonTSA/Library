package org.whstsa.library.api.books;

import org.whstsa.library.api.*;

public interface IBook extends Loadable, Serializable, Unique, Identifiable {

    /**
     * Gets the book genre
     *
     * @return the book genre
     */
    BookType getType();

    /**
     * Set the book genre
     *
     * @param type BookType
     */
    void setType(BookType type);

    /**
     * Get the author of a book
     *
     * @return the author of the book
     */
    String getAuthorName();

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
