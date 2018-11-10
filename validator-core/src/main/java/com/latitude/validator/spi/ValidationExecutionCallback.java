package com.latitude.validator.spi;

/**
 * @author Vincenzo Autiero
 *
 */
public interface ValidationExecutionCallback {

    /**
     * @param subject
     */
    public void beforeValidation(Object subject);

    /**
     * @param subject
     */
    public void onUnrecognizedSubject(Object subject);

    /**
     * @param subject
     * @param execution
     */
    public void afterValidator(Object subject, ValidatorExecution execution);

    /**
     * @param subject
     * @param validationContext
     * @param execution
     */
    public void afterValidation(Object subject, ValidationContext validationContext, ValidatorExecution execution);

}
