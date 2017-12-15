package org.whstsa.library.api.library;

import org.whstsa.library.api.IPerson;
import org.whstsa.library.api.Loadable;
import org.whstsa.library.api.books.IBook;
import org.whstsa.library.api.books.IBookContainer;
import org.whstsa.library.api.exceptions.BookNotRegisteredException;
import org.whstsa.library.api.exceptions.CannotDeregisterException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ILibrary extends IBookContainer, Loadable {

    /**
     * Creates a checkout object for a member and book
     *
     * @param member the member checking a book out
     * @param book   the book being checked out
     * @return the checkout object
     * @throws BookNotRegisteredException if {@code !books.contain(book)}
     */
    ICheckout reserveBook(IMember member, IBook book) throws BookNotRegisteredException;

    /**
     * Creates a member object and adds them to this library
     *
     * @param person the person joining the library
     * @return the member object
     */
    IMember addMember(IPerson person);

    /**
     * Changes the name of the library
     *
     * @param name the new name of the library
     */
    void setName(String name);

    /**
     * Removes a member from the library
     *
     * @param member the member to remove
     * @throws CannotDeregisterException if {@code !members.contains(member)}
     */
    void removeMember(UUID member) throws CannotDeregisterException;

    /**
     * Removes a member from the library
     *
     * @param person the person to search for and remove
     * @throws CannotDeregisterException if no member can be found with this criteria
     */
    void removeMember(IPerson person) throws CannotDeregisterException;

    /**
     * Removes a member from the library
     *
     * @param member the member to remove
     * @throws CannotDeregisterException if {@code member.getFine() > 0} or {@code member.getBooks().size() != 0}
     */
    void removeMember(IMember member) throws CannotDeregisterException;

    /**
     * Returns whether the given person is a member of this library
     *
     * @param person the person to check for
     * @return whether the person is a member
     */
    boolean isMember(IPerson person);

    /**
     * Returns a list of all members of this library
     *
     * @return the list of members
     */
    List<IMember> getMembers();

    /**
     * Returns a list of all ids of members of this library
     *
     * @return the list of ids
     */
    List<UUID> getMemberIDs();

    /**
     * Returns a map of Key:UUID and Value:IMember with the UUID representing IDs of members
     *
     * @return the map of members
     */
    Map<UUID, IMember> getMemberMap();

    /**
     * Returns the name of this library
     *
     * @return the name of this library
     */
    String getName();

    /**
     * Returns a map that maps books to a list of checkouts for that book
     *
     * @return the checkout map
     */
    Map<IBook, List<ICheckout>> getCheckouts();

}
