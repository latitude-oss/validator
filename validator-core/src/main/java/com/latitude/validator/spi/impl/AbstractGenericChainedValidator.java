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
