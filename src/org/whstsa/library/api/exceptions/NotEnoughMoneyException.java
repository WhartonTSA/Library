package org.whstsa.library.api.exceptions;

import org.whstsa.library.api.IPerson;

/**
 * Thrown when a transaction is attempted to be made that would
 * give a person a negative balance
 */
public class NotEnoughMoneyException extends Exception {

    private IPerson person;
    private double transaction;

    public NotEnoughMoneyException(IPerson person, double transaction) {
        this.person = person;
        this.transaction = transaction;
    }

    @Override
    public String getMessage() {
        return this.person.getName() + " cannot pay " + this.transaction + " (They have $" + this.person.getWallet() + ")";
    }

    public double getTransaction() {
        return this.transaction;
    }

    public IPerson getPerson() {
        return this.person;
    }
}
