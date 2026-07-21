package io.github.mantasg6.mylo.core.exception;

/**
 * Base exception thrown when any entity is not found.
 *
 */
public class EntityNotFoundException extends RuntimeException {
    
    public EntityNotFoundException(String message) {
        super(message);
    }
}
