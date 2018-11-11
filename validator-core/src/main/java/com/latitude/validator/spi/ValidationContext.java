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

import java.util.Optional;

/**
 * The context of the execution of the global validation process for a subject. Useful to allow the communication
 * between different validators in a {@link ChainedValidator}.
 * 
 * @author vautiero
 *
 */
public interface ValidationContext extends Iterable<ValidatorExecution> {

    /**
     * Try to resolve a value its name.
     * 
     * @param name the name of the object stored
     * @return an {@link Optional} containg the resolved value
     */
    public <T> Optional<T> tryResolveValue(String name, Class<T> expectedType);

    /**
     * Put a value in the context
     * 
     * @param name the key used to store the value
     * @param vaule the object to store
     */
    public void putValue(String name, Object value);

    /**
     * Get the {@link ExitStatus} of the validation
     * 
     * @return the {@link ExitStatus}
     */
    public ExitStatus getExitStatus();

}
