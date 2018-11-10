package com.latitude.validator.spi;

import org.junit.Assert;
import org.junit.Test;

public class ExitStatusTest {

    @Test
    public void shouldCompoundExitStatus() {
        Assert.assertEquals(ExitStatus.COMPLETED, ExitStatus.COMPLETED.and(ExitStatus.UNKNOWN));
        Assert.assertEquals(ExitStatus.SKIPPED, ExitStatus.SKIPPED.and(ExitStatus.COMPLETED));
        Assert.assertEquals(ExitStatus.FAILED, ExitStatus.FAILED.and(ExitStatus.SKIPPED));
    }

}
