package org.whstsa.library.api.exceptions;


import org.whstsa.library.api.library.ILibrary;
import org.whstsa.library.api.library.IMember;

public class MaximumCheckoutsException extends Exception {
    private IMember member;

    public MaximumCheckoutsException(IMember member) {
        this.member = member;
    }

    @Override
    public String getMessage() {
        return member.getName() + " can not checkout any more books.";
    }

}
