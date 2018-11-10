package com.latitude.validator.spi;

/**
 * @author Vincenzo Autiero
 *
 */
public class ValidationExecutionCallbackAdapter implements ValidationExecutionCallback {

    @Override
    public void beforeValidation(Object subject) {
    }

    @Override
    public void onUnrecognizedSubject(Object subject) {
    }

    @Override
    public void afterValidator(Object subject, ValidatorExecution execution) {
    }

    @Override
    public void afterValidation(Object subject, ValidationContext validationContext, ValidatorExecution execution) {
    }

}
