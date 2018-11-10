package com.latitude.validator.spi;

/**
 * @author Vincenzo Autiero
 *
 */
public interface ValidationOperations {

    /**
     * @param subject
     * @return
     */
    public ValidationContext validate(Object subject);

    /**
     * @param subject
     * @param callback
     * @return
     */
    public ValidationContext validate(Object subject, ValidationExecutionCallback callback);

}
