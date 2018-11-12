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
