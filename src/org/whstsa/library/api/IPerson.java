package org.whstsa.library.api;

import javafx.beans.property.StringProperty;
import org.whstsa.library.api.books.IBookContainerReadonly;
import org.whstsa.library.api.exceptions.MemberMismatchException;
import org.whstsa.library.api.exceptions.NotEnoughMoneyException;
import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.api.library.IMember;

import java.util.List;

public interface IPerson extends IBookContainerReadonly, Loadable, Serializable, Unique {

    /**
     * Returns the full name of this person
     *
     * @return the full name
     */
    String getName();

    /**
     * Returns the first name of this person
     *
     * @return the first name
     */
    String getFirstName();

    /**
     * Returns the last name of this person
     *
     * @return the last name
     */
    String getLastName();

    /**
     * Returns whether this person is a teacher
     *
     * @return the teacher status
     */
    boolean isTeacher();

    /**
     * Returns a list of library memberships
     *
     * @return the list of memberships
     */
    List<IMember> getMemberships();

    /**
     * Adds a membership to this person and returns the newly-created member object
     *
     * @param member the member to add
     * @return the newly-created member object
     * @throws MemberMismatchException if {@code member.getPerson() != this}
     */
    IMember addMembership(IMember member) throws MemberMismatchException;

    /**
     * Creates a membership object and returns it
     *
     * @param library the library the person is joining
     * @return the newly-created member object
     */
    IMember addMembership(ILibrary library);

    /**
     * Returns the current balance of this person
     *
     * @return the balance
     */
    double getWallet();

    /**
     * Adds the given amount of money to the person and returns
     * the new balance
     *
     * @param money the money to add
     * @return the new balance
     */
    double addMoney(double money);

    /**
     * Deducts the given amount of money from the person and
     * returns the new balance
     *
     * @param money the money to deduct
     * @return the new balance
     * @throws NotEnoughMoneyException if {@code money > balance}
     */
    double deductMoney(double money) throws NotEnoughMoneyException;

    /**
     * Sets whether the person is now a teacher
     *
     * @param teacher teacher state
     */
    void setTeacher(boolean teacher);

    /**
     * Sets the first name of the person
     *
     * @param firstName the first name
     */
    void setFirstName(String firstName);

    /**
     * Sets the last name of the person
     *
     * @param lastName the last name
     */
    void setLastName(String lastName);

    /**
     * Returns whether this person is eligible to be removed
     *
     * @return whether the person can be removed
     */
    boolean isRemovable();

}
