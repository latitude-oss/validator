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

package com.latitude.validator.spi.impl;

import com.latitude.validator.spi.ChainedValidator;
import com.latitude.validator.util.Preconditions;

/**
 * @author Vincenzo Autiero
 *
 * @param <T>
 */
public abstract class AbstractGenericChainedValidator<T> extends AbstractGenericValidator<T>
        implements ChainedValidator {

    private ChainedValidator next;

    /**
     * @param validatorName
     */
    public AbstractGenericChainedValidator(String validatorName) {
        super(validatorName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.latitude.validator.spi.ChainedValidator#and(com.latitude.validator.spi.ChainedValidator)
     */
    @Override
    public ChainedValidator and(ChainedValidator next) {
        Preconditions.notNull(next, "Next validator is required!");
        this.next = next;
        return this.next;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.latitude.validator.spi.impl.AbstractGenericValidator#supports(java.lang.Object)
     */
    @Override
    public final boolean supports(Object subject) {
        boolean supports = doSupports(subject);
        if (supports && hasNext()) {
            supports = next.supports(subject);
        }
        return supports;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.latitude.validator.spi.ChainedValidator#hasNext()
     */
    public boolean hasNext() {
        return next != null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.latitude.validator.spi.ChainedValidator#next()
     */
    @Override
    public ChainedValidator next() {
        return next;
    }

}
