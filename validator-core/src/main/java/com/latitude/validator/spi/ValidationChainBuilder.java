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
