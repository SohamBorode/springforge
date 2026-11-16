package dev.springforge.core;

/**
 * Signals that a requested resource could not be found.
 *
 * Applications may throw this exception from service or controller code;
 * {@link GlobalExceptionHandler} converts it to an HTTP 404 response.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
