package com.latitude.validator.spi;

import java.util.Optional;

/**
 * The context of the execution of the global validation process for a subject. Useful to allow the communication
 * between different validators in a {@link ChainedValidator}.
 * 
 * @author vautiero
 *
 */
public interface ValidationContext extends Iterable<ValidatorExecution> {

    /**
     * Try to resolve a value its name.
     * 
     * @param name the name of the object stored
     * @return an {@link Optional} containg the resolved value
     */
    public <T> Optional<T> tryResolveValue(String name, Class<T> expectedType);

    /**
     * Put a value in the context
     * 
     * @param name the key used to store the value
     * @param vaule the object to store
     */
    public void putValue(String name, Object value);

    /**
     * Get the {@link ExitStatus} of the validation
     * 
     * @return the {@link ExitStatus}
     */
    public ExitStatus getExitStatus();

}
