package org.whstsa.library.api.library;

import org.whstsa.library.api.Serializable;
import org.whstsa.library.api.Unique;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.exceptions.CheckedInException;
import org.whstsa.library.api.exceptions.NotEnoughMoneyException;

import java.util.Date;

public interface ICheckout extends Serializable, Unique {

    /**
     * Returns the person who checked out the book
     *
     * @return the owner of this checkout
     */
    IMember getOwner();

    /**
     * Returns the book the owner checked out
     *
     * @return the book of this checkout
     */
    IBook getBook();

    /**
     * Returns the due date for this checkout
     *
     * @return the due date
     */
    Date getDueDate();

    /**
     * Resets the due date to an implementation-decided number of days
     * out from the current date.
     */
    void resetDueDate();

    /**
     * Returns the fine that must be paid off for this checkout
     *
     * @return the fine for this checkout
     */
    double getFine();

    /**
     * Pays off the fine, if any, of this checkout.
     *
     * @return the new balance
     * @throws NotEnoughMoneyException
     */
    double payFine() throws NotEnoughMoneyException;

    /**
     * Returns the days past the due date, or zero if it has not passed the due date
     *
     * @return the days past the due date
     */
    int getDaysPast();

    /**
     * Returns whether the book for this checkout has been returned
     *
     * @return the check-in status
     */
    boolean isReturned();
    
    /**
     * Returns whether this checkout is overdue
     * 
     * @return the overdue status
     */
    boolean isOverdue();

    /**
     * Checks the book in regardless of debt. Debt must be checked for and
     * paid off by other logic, as it is not the checkout objects responsibility.
     *
     * @throws CheckedInException thrown whenever checkout is already checked-in
     */
    void checkIn() throws CheckedInException;

}
