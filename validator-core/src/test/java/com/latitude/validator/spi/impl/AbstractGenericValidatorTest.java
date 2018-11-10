package com.latitude.validator.spi.impl;

import org.junit.Test;

import com.latitude.validator.spi.ValidationContext;

public class AbstractGenericValidatorTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfNotSupportsSubject() {
        new ValidatorImpl("test", false).validate(new Object(), new DefaultValidationContext());
    }

    @Test
    public void shouldThrowExceptionIfSupportsSubject() {
        new ValidatorImpl("test", true).validate(new Object(), new DefaultValidationContext());
    }

    public static class ValidatorImpl extends AbstractGenericValidator<Object> {

        private final boolean supports;

        public ValidatorImpl(String validatorName, boolean supports) {
            super(validatorName);
            this.supports = supports;
        }

        @Override
        public boolean doSupports(Object subject) {
            return supports;
        }

        @Override
        public void doValidation(Object subject, ValidationContext context) {
        }

    }

}
