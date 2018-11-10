package com.latitude.validator.spi;

/**
 * A validator able to apply the validation logic This interface defines the methods that a validator must provide.
 * 
 * @author Vincenzo Autiero
 * 
 */
public interface Validator {

    /**
     * Get the logical name of the validator
     * @return a {@link String} with the name of the validator
     */
    public String getName();

    /**
     * Execute the validation
     * 
     * @param subject the subject of the validation
     * @param context the {@link ValidationContext}
     * @throws ValidationException if the validation constraints are not satisfied
     */
    public void validate(Object subject, ValidationContext context) throws ValidationException;

    /**
     * Indicates if this validator supports the provided subject.
     * 
     * @param subject the subject of the validation
     * @return <code>true</code> if this validator supports the subject otherwise <code>false</code>
     */
    public boolean supports(Object subject);

}
