package org.whstsa.library.api.exceptions;

/**
 * Thrown whenever a member is requested by ID but cannot be
 * found.
 */
public class MemberNotFoundException extends CannotDeregisterException {

    private String context;

    public MemberNotFoundException(String ctx) {
        this.context = ctx;
    }

    @Override
    public String getMessage() {
        return "Could not find member" + (this.context == null ? "" : "(context: '" + this.context + "')");
    }
}
