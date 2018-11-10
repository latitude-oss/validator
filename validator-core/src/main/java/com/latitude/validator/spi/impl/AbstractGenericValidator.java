package com.latitude.validator.spi.impl;

import com.latitude.validator.spi.ValidationContext;
import com.latitude.validator.spi.Validator;
import com.latitude.validator.util.Preconditions;

public abstract class AbstractGenericValidator<T> implements Validator {

    private final String validatorName;

    public AbstractGenericValidator(String validatorName) {
        Preconditions.hasText(validatorName, "Validator name is required");
        this.validatorName = validatorName;
    }

    @Override
    public String getName() {
        return validatorName;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void validate(Object subject, ValidationContext context) {
        if (!supports(subject)) {
            throw new IllegalArgumentException(String.format("Unsupported subject %s provided", subject));
        }
        doValidation((T) subject, context);
    }

    @Override
    public boolean supports(Object subject) {
        return doSupports(subject);
    }

    public abstract boolean doSupports(Object subject);

    public abstract void doValidation(T subject, ValidationContext context);

}
