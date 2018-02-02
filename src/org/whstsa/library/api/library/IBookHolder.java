package org.whstsa.library.api.library;

import org.whstsa.library.api.Serializable;
import org.whstsa.library.api.Unique;
import org.whstsa.library.api.books.IBookContainer;

public interface IBookHolder extends IBookContainer, Serializable, Unique {

    /**
     * Returns the total dollar amount of fines the book-holder owes
     *
     * @return the total fines
     */
    double getFine();

}
