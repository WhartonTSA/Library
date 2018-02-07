package org.whstsa.library.api.library;

import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.Identifiable;
import org.whstsa.library.api.Serializable;
import org.whstsa.library.api.Unique;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.exceptions.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IMember extends IBookHolder, Serializable, Unique, Identifiable {

    /**
     * Returns the library this member belongs to
     *
     * @return the library for this member
     */
    ILibrary getLibrary();

    /**
     * Returns the person that has this membership
     *
     * @return the person
     */
    IPerson getPerson();

    /**
     * Returns the list of checkouts this member has made for a given book
     *
     * @param book the book to look up
     * @return the list of checkouts
     */
    List<ICheckout> getCheckout(IBook book);

    /**
     * Adds a checkout to this member
     *
     * @param checkout the checkout to add
     * @throws BookNotRegisteredException if {@code !library.books.contains(checkout.book)}
     */
    void checkout(ICheckout checkout) throws BookNotRegisteredException, MemberMismatchException;

    /**
     * Returns a combined list of checkouts this member has
     *
     * @return the combined list of checkouts
     */
    List<ICheckout> getCheckouts();

    /**
     * Returns a combined list of checkouts this member has
     *
     * @param notReturned only not-returned checkouts
     * @return the combined list of checkouts
     */
    List<ICheckout> getCheckouts(boolean notReturned);

    /**
     * Returns a map that maps books to their list of checkouts
     *
     * @return the book-checkout map
     */
    Map<IBook, List<ICheckout>> getCheckoutMap();

    /**
     * Removes a book from this member
     *
     * @param book the book to remove
     * @throws OutstandingFinesException if {@code totalFines > 0}
     */
    @Override
    void removeBook(IBook book) throws OutstandingFinesException;

    /**
     * Removes a book from this member, finding it by its id
     *
     * @param id the id of the book to remove
     * @throws OutstandingFinesException if {@code totalFines > 0}
     */
    @Override
    void removeBook(UUID id) throws OutstandingFinesException;

    /**
     * Checks a checkout back in
     *
     * @param checkout The checkout to return
     * @throws OutstandingFinesException if {@code fine > 0}
     * @throws MemberMismatchException   if {@code checkout.member != this}
     * @throws CheckedInException        if {@code checkout.isReturned()}
     */
    void checkIn(ICheckout checkout) throws OutstandingFinesException, MemberMismatchException, CheckedInException;

    /**
     * Checks a checkout back in, paying any fines that it may have
     *
     * @param checkout The checkout to return
     * @throws MemberMismatchException if {@code checkout.member != this}
     * @throws CheckedInException      if {@code checkout.isReturned()}
     */
    void checkInAndPayFines(ICheckout checkout) throws MemberMismatchException, CheckedInException;
}
