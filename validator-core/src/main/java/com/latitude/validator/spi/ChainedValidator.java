package com.latitude.validator.spi;

/**
 * @author Vincenzo Autiero
 *
 */
public interface ChainedValidator extends Validator {

    /**
     * @return
     */
    public boolean hasNext();

    /**
     * @return
     */
    public ChainedValidator next();

    /**
     * @param next
     * @return
     */
    public ChainedValidator and(ChainedValidator next);

}
