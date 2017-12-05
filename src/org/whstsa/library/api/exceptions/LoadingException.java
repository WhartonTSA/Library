package org.whstsa.library.api.exceptions;

/**
 * Thrown when an error is occurred while converting a JSONObject
 * to a meaningful Java object
 */
public class LoadingException extends Exception {

    private String message = "";

    private Exception exception = null;

    public LoadingException(Exception e) {
        this.exception = e;
    }

    public LoadingException(String e) {
        this.message = e;
    }

    @Override
    public String getMessage() {
        if (this.exception != null) {
            return this.exception.getMessage();
        }
        return this.message;
    }

    public Exception getException() {
        return this.exception;
    }

}
