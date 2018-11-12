/*
 * Copyright 2018 Latitude Srls
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
