package org.whstsa.library.api.exceptions;

import org.whstsa.library.api.library.IMember;

/**
 * Thrown when a member is attempted to be removed from a library
 * while they have outstanding fines (overdue books)
 */
public class OutstandingFinesException extends CannotDeregisterException {

    private IMember member;
    private Actions action;
    private double fine;

    public OutstandingFinesException(IMember member, Actions action, double fine) {
        this.member = member;
        this.action = action;
        this.fine = fine;
    }

    public OutstandingFinesException(IMember member, Actions action) {
        this.member = member;
        this.action = action;
        this.fine = this.member.getFine();
    }

    public OutstandingFinesException(IMember member) {
        this(member, Actions.DEREGISTER);
    }


    @Override
    public String getMessage() {
        return "Cannot " + this.action.getMessage() + " " + this.member.getName() + " because they have $" + this.fine + " in outstanding fines.";
    }

    public enum Actions {
        DEREGISTER, REMOVE_BOOK("remove book from"), CHECK_IN("check back in a book for");

        private String message;

        Actions() {
            this.message = this.name().toLowerCase();
        }

        Actions(String message) {
            this.message = message;
        }

        public String getMessage() {
            return this.message;
        }
    }

}
