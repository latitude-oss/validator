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

import com.latitude.validator.util.Preconditions;

/**
 * @author Vincenzo Autiero
 *
 */
public class ValidationChainBuilder {

    private final ChainedValidator head;

    private ChainedValidator tail;

    /**
     * @param head
     */
    public ValidationChainBuilder(ChainedValidator head) {
        Preconditions.notNull(head, "Head of chain must not be null");
        this.head = head;
        this.tail = head;
    }

    /**
     * @param validator
     * @return
     */
    public ValidationChainBuilder withNext(ChainedValidator validator) {
        Preconditions.notNull(validator, "Next element cannot be null");
        tail.and(validator);
        tail = validator;
        return this;
    }

    /**
     * @return
     */
    public ValidationChain build() {
        return new ValidationChain(head);
    }

}
