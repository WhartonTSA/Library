package org.whstsa.library.api.exceptions;

import org.whstsa.library.api.library.ICheckout;

/**
 * Thrown whenever checkIn() is called on a dead ICheckout object
 */
public class CheckedInException extends Exception {

    private ICheckout failedTransaction;

    public CheckedInException(ICheckout transaction) {
        this.failedTransaction = transaction;
    }

    @Override
    public String getMessage() {
        return "Cannot check-in an already checked-in book - Checkout ID " + failedTransaction.getID();
    }

}
