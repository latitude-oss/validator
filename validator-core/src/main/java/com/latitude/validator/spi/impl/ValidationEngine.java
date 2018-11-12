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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.latitude.validator.spi.ChainedValidator;
import com.latitude.validator.spi.ExecutionMode;
import com.latitude.validator.spi.ExitStatus;
import com.latitude.validator.spi.ValidationChain;
import com.latitude.validator.spi.ValidationContext;
import com.latitude.validator.spi.ValidationException;
import com.latitude.validator.spi.ValidationExecutionCallback;
import com.latitude.validator.spi.ValidationOperations;
import com.latitude.validator.spi.Validator;
import com.latitude.validator.spi.ValidatorExecution;
import com.latitude.validator.util.Preconditions;

/**
 * This class represents a template to execute the validation process.
 * 
 * Holds a set of {@link Validator} objects able to verify the requirements necessary for a subject to be valid.<br/>
 * <br/>
 * When the validation is performed, is elected the first validator able to apply the validation logic for the subject
 * in input and the validation is delegated to the latter. If a {@link ValidationException} is thrown, by default, the
 * validation process ends with an {@link ExitStatus#FAILED}, {@link ExitStatus#COMPLETED} otherwise. <br/>
 * <br/>
 * A "non-strict" mode can be activated to allow anyway the execution of all the {@link Validator}s in the validation
 * chain even if a {@link ValidationException} is thrown.
 * 
 * @author Vincenzo Autiero
 *
 */
public class ValidationEngine implements ValidationOperations {

    private final Logger logger = LoggerFactory.getLogger(ValidationEngine.class);

    private final Collection<Validator> validators;

    private ExecutionMode executionMode = ExecutionMode.STRICT;

    /**
     * Creates an instance
     */
    public ValidationEngine() {
        validators = new ArrayList<Validator>();
    }

    /**
     * Add a {@link Validator} to the set
     * 
     * @param validator the validator
     */
    public void addValidator(Validator validator) {
        Preconditions.notNull(validator, "Validator is required");
        validators.add(validator);
    }

    /**
     * @param chain
     */
    public void addValidationChain(ValidationChain chain) {
        Preconditions.notNull(chain, "ValidationChain is required");
        validators.add(chain.head());
    }

    @Override
    public ValidationContext validate(Object subject) {
        return validate(subject, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.latitude.validator.spi.ValidationOperations#validate(java.lang.Object,
     * com.latitude.validator.spi.ValidationExecutionCallback)
     */
    @Override
    public ValidationContext validate(Object subject, ValidationExecutionCallback callback) {
        Preconditions.notNull(subject, "Subject is required");
        DefaultValidationContext context = new DefaultValidationContext();
        notifyBeforeValidation(callback, subject);
        Optional<Validator> validatorHolder = tryFindValidator(subject);
        if (validatorHolder.isPresent()) {
            Validator validator = validatorHolder.get();
            ValidatorExecution execution = runValidator(validator, subject, context, callback);
            if (ChainedValidator.class.isAssignableFrom(validator.getClass())) {
                ChainedValidator chainedValidator = (ChainedValidator) validator;
                while (chainedValidator.hasNext()) {
                    chainedValidator = chainedValidator.next();
                    if (execution.isContinuable()) {
                        if (logger.isDebugEnabled()) {
                            logger.debug(
                                    "Validation execution is continuable, proceeding with next chained validator({}, {})",
                                    chainedValidator.getName(), chainedValidator.getClass());
                        }
                        execution = runValidator(chainedValidator, subject, context, callback);
                    }
                    else {
                        /*
                         * If execution is not continuable (execution is skipped or is failed and strict mode is active)
                         * next executions must be skipped.
                         */
                        if (logger.isDebugEnabled()) {
                            logger.debug("Validation execution is not continuable, skipping validator({}, {})",
                                    chainedValidator.getName(), chainedValidator.getClass());
                        }
                        execution = new ValidatorExecution(chainedValidator.getName(), ExitStatus.SKIPPED,
                                executionMode);
                        traceExecution(subject, context, execution, callback);
                    }
                }
            }

            notifyAfterValidation(callback, subject, context, execution);

            return context;
        }
        else {
            if (logger.isErrorEnabled()) {
                logger.error("Unable to resolve validator, subject {} is not supported", subject);
            }
            if (callback != null) {
                notifyUnrecognizedSubject(callback, subject);
                return context;
            }
            else {
                throw new IllegalArgumentException("Input subject is not supported");
            }
        }
    }

    /**
     * Sets the strict mode on/off. Turned on by default.
     * 
     * @param strict if <code>false</code> the strict-mode is deactivated
     */
    public void setExecutionMode(ExecutionMode executionMode) {
        Preconditions.notNull(executionMode, "ExecutionMode cannot be null");
        this.executionMode = executionMode;
        if (logger.isDebugEnabled()) {
            logger.debug("ExecutionMode set to {}", executionMode);
        }
    }

    /**
     * @param validatorName
     * @return
     */
    public Optional<Validator> tryFindValidator(final String validatorName) {
        return validators.stream().filter(new Predicate<Validator>() {
            @Override
            public boolean test(Validator candidate) {
                return candidate.getName().equals(validatorName);
            }
        }).findFirst();
    }

    private Optional<Validator> tryFindValidator(final Object subject) {
        Optional<Validator> result = validators.stream().filter(new Predicate<Validator>() {
            @Override
            public boolean test(Validator candidate) {
                return candidate.supports(subject);
            }
        }).findFirst();
        if (result.isPresent() && logger.isDebugEnabled()) {
            Validator validator = result.get();
            if (logger.isDebugEnabled()) {
                logger.debug("Validator({}, {}) properly resolved for subject {}", validator.getName(),
                        validator.getClass(), subject);
            }
        }
        return result;
    }

    private ValidatorExecution runValidator(Validator validator, Object subject, DefaultValidationContext context,
            ValidationExecutionCallback callback) {
        ValidatorExecution execution = null;
        try {
            validator.validate(subject, context);
            execution = new ValidatorExecution(validator.getName(), ExitStatus.COMPLETED, executionMode);
        }
        catch (ValidationException exception) {
            execution = new ValidatorExecution(validator.getName(), ExitStatus.FAILED, executionMode);
            execution.setFailureException(exception);
        }

        traceExecution(subject, context, execution, callback);
        return execution;
    }

    private void traceExecution(Object subject, DefaultValidationContext context, ValidatorExecution execution,
            ValidationExecutionCallback callback) {
        if (execution != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Tracing {}", execution);
            }
            context.traceExecution(execution);
            notifyAfterValidator(callback, subject, execution);
        }
    }

    private void notifyBeforeValidation(ValidationExecutionCallback callback, Object subject) {
        if (callback != null) {
            callback.beforeValidation(subject);
        }
    }

    private void notifyUnrecognizedSubject(ValidationExecutionCallback callback, Object subject) {
        if (callback != null) {
            callback.onUnrecognizedSubject(subject);
        }
    }

    private void notifyAfterValidator(ValidationExecutionCallback callback, Object subject,
            ValidatorExecution execution) {
        if (callback != null) {
            callback.afterValidator(subject, execution);
        }
    }

    private void notifyAfterValidation(ValidationExecutionCallback callback, Object subject,
            ValidationContext validationContext, ValidatorExecution execution) {
        if (callback != null) {
            callback.afterValidation(subject, validationContext, execution);
        }
    }

}
