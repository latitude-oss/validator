package com.latitude.validator.spi;

import java.time.LocalDateTime;

/**
 * Represents an error during the execution of the logic for a {@link Validator}.
 * 
 * @author Vincenzo Autiero
 *
 */
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 6894014187430021637L;

    private LocalDateTime failureOn;

    /**
     * Creates a new instance
     */
    public ValidationException() {
        super();
    }

    /**
     * Creates a new instance
     * 
     * @param message the error message
     * @param cause the {@link Throwable} causing this error
     * 
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        this.failureOn = LocalDateTime.now();
    }

    /**
     * Creates a new instance
     * 
     * @param message the error message
     */
    public ValidationException(String message) {
        super(message);
        this.failureOn = LocalDateTime.now();
    }

    /**
     * Creates a new instance
     * 
     * @param cause the {@link Throwable} causing this error
     */
    public ValidationException(Throwable cause) {
        super(cause);
    }

    public LocalDateTime getFailureOn() {
        return failureOn;
    }

}
