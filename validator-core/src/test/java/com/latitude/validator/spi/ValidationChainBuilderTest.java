package com.latitude.validator.spi;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

import com.latitude.validator.spi.impl.AbstractGenericChainedValidator;

public class ValidationChainBuilderTest {

    private ValidationChainBuilder subject;

    @Test
    public void shouldBuildChain() {
        subject = new ValidationChainBuilder(new ChainedValidatorImpl("FIRST_VALIDATOR"));
        ValidationChain chain = subject.withNext(new ChainedValidatorImpl("SECOND_VALIDATOR"))
                .withNext(new ChainedValidatorImpl("THIRD_VALIDATOR")).build();
        Iterator<ChainedValidator> iterator = chain.iterator();
        Assert.assertEquals("FIRST_VALIDATOR", iterator.next().getName());
        Assert.assertEquals("SECOND_VALIDATOR", iterator.next().getName());
        Assert.assertEquals("THIRD_VALIDATOR", iterator.next().getName());
        Assert.assertFalse(iterator.hasNext());
    }

    public static class ChainedValidatorImpl extends AbstractGenericChainedValidator<Object> {

        public ChainedValidatorImpl(String validatorName) {
            super(validatorName);
        }

        @Override
        public boolean doSupports(Object subject) {
            return true;
        }

        @Override
        public void doValidation(Object subject, ValidationContext context) {
        }

    }

}
