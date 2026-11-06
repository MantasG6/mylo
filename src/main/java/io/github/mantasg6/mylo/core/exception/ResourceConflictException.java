package io.github.mantasg6.mylo.core.exception;

/**
 * Resource conflict exception that contains a message for the user.
 *
 */
public class ResourceConflictException extends RuntimeException {

    public ResourceConflictException(String message) {
        super(message);
    }
}
