package dev.springforge.core;

/**
 * Signals that a request conflicts with the current state of a resource,
 * such as attempting to create a record that already exists.
 *
 * {@link GlobalExceptionHandler} converts this exception to HTTP 409.
 */
public class ResourceConflictException extends RuntimeException {

    public ResourceConflictException(String message) {
        super(message);
    }
}
