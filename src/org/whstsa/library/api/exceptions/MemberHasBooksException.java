package org.whstsa.library.api.exceptions;

import org.whstsa.library.api.library.IMember;

/**
 * Thrown when a library attempts to remove a member but
 * they still have books checked-out.
 */
public class MemberHasBooksException extends CannotDeregisterException {

    private IMember member;

    public MemberHasBooksException(IMember member) {
        this.member = member;
    }

    @Override
    public String getMessage() {
        return member.getPerson().getName() + " has " + member.getBookIDs().size() + " books checked out. They cannot be de-registered until they check them back in.";
    }
}
