/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
