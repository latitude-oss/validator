package com.latitude.validator.spi.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;

import com.latitude.validator.spi.ExitStatus;
import com.latitude.validator.spi.ValidationContext;
import com.latitude.validator.spi.ValidatorExecution;
import com.latitude.validator.util.Preconditions;

/**
 * @author Vincenzo Autiero
 *
 */
public class DefaultValidationContext extends HashMap<String, Object> implements ValidationContext {

    private static final long serialVersionUID = 4508593253444619462L;

    private final Collection<ValidatorExecution> validatorExecutions;

    /**
     * Creates a new instance
     */
    public DefaultValidationContext() {
        validatorExecutions = new ArrayList<ValidatorExecution>();
    }

    /**
     * Trace a {@link ValidatorExecution}.
     * 
     * @param validatorExecution the execution
     */
    public void traceExecution(ValidatorExecution validatorExecution) {
        Preconditions.notNull(validatorExecution, "ValidatorExecution is required");
        Optional<ValidatorExecution> executionHolder = tryFindValidatorExecution(validatorExecution.getValidatorName());
        if (executionHolder.isPresent()) {
            throw new IllegalStateException("ValidatorExecution already present!");
        }
        validatorExecutions.add(validatorExecution);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<ValidatorExecution> iterator() {
        return validatorExecutions.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.gse.engine.spi.validation.ValidationContext#putValue(java.lang.String, java.lang.Object)
     */
    @Override
    public void putValue(String name, Object value) {
        put(name, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.gse.engine.spi.validation.ValidationContext#tryResolveValue(java.lang.String, java.lang.Class)
     */
    @Override
    public <T> Optional<T> tryResolveValue(String name, Class<T> expectedType) {
        if (containsKey(name)) {
            Object value = get(name);
            if (value != null && expectedType.isAssignableFrom(value.getClass())) {
                T valueAsType = expectedType.cast(value);
                return Optional.of(valueAsType);
            }
            else if (value == null) {
                return Optional.empty();
            }
            throw new ClassCastException(String.format("Cannot cast from %s to %s", expectedType, value.getClass()));
        }
        return Optional.empty();
    }

    /**
     * Get the {@link ExitStatus} of the global validation execution.
     * 
     * @return the {@link ExitStatus}
     */
    @Override
    public ExitStatus getExitStatus() {
        ExitStatus exitStatus = ExitStatus.UNKNOWN;
        for (ValidatorExecution execution : validatorExecutions) {
            exitStatus = exitStatus.and(execution.getExitStatus());
        }
        return exitStatus;
    }

    @Override
    public String toString() {
        return String.format("ValidationContext(exitStatus=%s, context=%s)", getExitStatus(), super.toString());
    }

    private Optional<ValidatorExecution> tryFindValidatorExecution(final String validatorName) {
        return validatorExecutions.stream().filter(new Predicate<ValidatorExecution>() {
            @Override
            public boolean test(ValidatorExecution candidate) {
                return candidate.getValidatorName().equals(validatorName);
            }
        }).findFirst();
    }

}
