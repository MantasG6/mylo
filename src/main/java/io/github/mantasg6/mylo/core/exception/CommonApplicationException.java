package io.github.mantasg6.mylo.core.exception;

/**
 * Common application exception that contains a message for the user.
 *
 */
public class CommonApplicationException extends RuntimeException {

    public CommonApplicationException(String message) {
        super(message);
    }
}
