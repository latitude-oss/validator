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
