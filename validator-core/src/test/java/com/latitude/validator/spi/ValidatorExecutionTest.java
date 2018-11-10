package com.latitude.validator.spi;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

public class ValidatorExecutionTest {

    @Test
    public void shouldBeContinuable() {
        Assert.assertTrue(new ValidatorExecution("test", ExitStatus.COMPLETED, ExecutionMode.STRICT).isContinuable());
        Assert.assertFalse(new ValidatorExecution("test", ExitStatus.FAILED, ExecutionMode.STRICT).isContinuable());
        Assert.assertFalse(new ValidatorExecution("test", ExitStatus.SKIPPED, ExecutionMode.STRICT).isContinuable());

        Assert.assertTrue(new ValidatorExecution("test", ExitStatus.COMPLETED, ExecutionMode.LENIENT).isContinuable());
        Assert.assertTrue(new ValidatorExecution("test", ExitStatus.FAILED, ExecutionMode.LENIENT).isContinuable());
        Assert.assertFalse(new ValidatorExecution("test", ExitStatus.SKIPPED, ExecutionMode.LENIENT).isContinuable());
    }

    @Test
    public void shouldHoldFailureException() {
        ValidatorExecution execution = new ValidatorExecution("test", ExitStatus.COMPLETED, ExecutionMode.STRICT);
        execution.setFailureException(
                new ValidationException("Validation is not passed", new IllegalArgumentException()));
        Optional<ValidationException> exceptionHolder = execution.tryGetFailureException();
        Assert.assertTrue(exceptionHolder.isPresent());
        Assert.assertEquals(execution.getExecutionMessage(), "Validation is not passed");
        Assert.assertEquals(execution.getExitStatus(), ExitStatus.FAILED);
    }

}
