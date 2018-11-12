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

/**
 * Represents an error during the execution of the logic for a {@link Validator}.
 * 
 * @author Vincenzo Autiero
 *
 */
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 6894014187430021637L;

    private LocalDateTime failureOn;

    /**
     * Creates a new instance
     */
    public ValidationException() {
        super();
    }

    /**
     * Creates a new instance
     * 
     * @param message the error message
     * @param cause the {@link Throwable} causing this error
     * 
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        this.failureOn = LocalDateTime.now();
    }

    /**
     * Creates a new instance
     * 
     * @param message the error message
     */
    public ValidationException(String message) {
        super(message);
        this.failureOn = LocalDateTime.now();
    }

    /**
     * Creates a new instance
     * 
     * @param cause the {@link Throwable} causing this error
     */
    public ValidationException(Throwable cause) {
        super(cause);
    }

    public LocalDateTime getFailureOn() {
        return failureOn;
    }

}
