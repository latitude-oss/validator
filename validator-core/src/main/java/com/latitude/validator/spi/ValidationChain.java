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

import java.util.Iterator;

import com.latitude.validator.util.Preconditions;

/**
 * @author Vincenzo Autiero
 *
 */
public class ValidationChain implements Iterable<ChainedValidator> {

    private final ChainedValidator head;

    /**
     * @param head
     */
    public ValidationChain(ChainedValidator head) {
        Preconditions.notNull(head, "Head of chain cannot be null");
        this.head = head;
    }

    /**
     * @return
     */
    public ChainedValidator head() {
        return head;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<ChainedValidator> iterator() {
        return new IteratorImpl(head);
    }

    final static class IteratorImpl implements Iterator<ChainedValidator> {

        private ChainedValidator next;

        public IteratorImpl(ChainedValidator head) {
            Preconditions.notNull(head, "Head element cannot be null");
            this.next = head;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public ChainedValidator next() {
            if (!hasNext()) {
                throw new IllegalStateException("Illegal call to next, end of chain reached");
            }
            try {
                return next;
            }
            finally {
                if (next.hasNext()) {
                    next = next.next();
                }
                else {
                    next = null;
                }
            }
        }

    }

}
