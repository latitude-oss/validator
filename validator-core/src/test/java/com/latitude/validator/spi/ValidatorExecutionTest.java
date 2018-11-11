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
