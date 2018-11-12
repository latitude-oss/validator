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

package com.latitude.validator.spi;

import java.time.LocalDateTime;
import java.util.Optional;

import com.latitude.validator.util.Preconditions;

/**
 * Represents the execution of the validation logic applied by a {@link Validator}.
 * 
 * @author Vincenzo Autiero
 *
 */
public class ValidatorExecution {

    private final String validatorName;

    private final ExecutionMode executionMode;

    private ExitStatus exitStatus;

    private LocalDateTime executedOn;

    private String executionMessage;

    private ValidationException failureException;

    /**
     * Creates a new instance
     * 
     * @param validatorName the logical name of the validator
     * @param exitStatus the {@link ExitStatus} for this execution
     */
    public ValidatorExecution(String validatorName, ExitStatus exitStatus, ExecutionMode executionMode) {
        Preconditions.notNull(validatorName, "Validator name is required");
        Preconditions.notNull(exitStatus, "ExitStatus is required");
        Preconditions.notNull(executionMode, "ExecutionMode is required");
        this.validatorName = validatorName;
        this.exitStatus = exitStatus;
        this.executionMode = executionMode;
        this.executedOn = LocalDateTime.now();
    }

    /**
     * Get the logical name of the validator.
     * 
     * @return the name of the validator
     */
    public String getValidatorName() {
        return validatorName;
    }

    /**
     * Get an {@link Optional} containing the {@link ValidationException} responsible of the failure.
     * 
     * @return an holder with the failure exception
     */
    public Optional<ValidationException> tryGetFailureException() {
        return Optional.ofNullable(failureException);
    }

    /**
     * Sets the cause of failure
     * 
     * @param failureException the cause of failure
     */
    public void setFailureException(ValidationException failureException) {
        this.exitStatus = ExitStatus.FAILED;
        this.failureException = failureException;
        this.executedOn = failureException.getFailureOn();
        this.executionMessage = failureException.getMessage();
    }

    /**
     * Get the {@link ExitStatus} of this execution
     * 
     * @return the {@link ExitStatus}
     */
    public ExitStatus getExitStatus() {
        return exitStatus;
    }

    /**
     * Get the message of this execution
     * 
     * @return
     */
    public String getExecutionMessage() {
        return executionMessage;
    }

    /**
     * Get the date and time of this execution
     * 
     * @return
     */
    public LocalDateTime getExecutionDateTime() {
        return executedOn;
    }

    public void setExecutionMessage(String executionMessage) {
        this.executionMessage = executionMessage;
    }

    /**
     * Indicates if this execution ran with strict mode on.
     * 
     * @return true if this execution is strict
     */
    public boolean isStrict() {
        return ExecutionMode.STRICT.equals(executionMode);
    }

    /**
     * Indicates if this execution is failed
     * 
     * @return true if this execution is failed
     */
    public boolean isFailed() {
        return ExitStatus.FAILED.equals(exitStatus);
    }

    /**
     * Indicates if this execution is skipped
     * 
     * @return true if this execution is skipped
     */
    public boolean isSkipped() {
        return ExitStatus.SKIPPED.equals(exitStatus);
    }

    /**
     * Indicates if next executions in the chain can continue. If this execution is not skipped and strict mode is
     * deactivated chain can continue. If this execution is not failed and strict mode is active chain can continue.
     * Otherwise next executions in the chain cannot continue.
     * 
     * @return true if next executions can continue
     */
    public boolean isContinuable() {
        if (!isSkipped()) {
            if (isStrict()) {
                return !isFailed();
            }
            else {
                return true;
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("ValidatorExecution(%s, exitStatus=%s, continuable=%s)", validatorName, exitStatus,
                isContinuable());
    }

}
