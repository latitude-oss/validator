package com.latitude.validator.spi;

import com.latitude.validator.util.StringUtils;

/**
 * Value object used to carry information about the status of a {@link ValidatorExecution}.
 *
 * ExitStatus is immutable and therefore thread-safe.
 * 
 * @author Vincenzo Autiero
 *
 */
public class ExitStatus implements Comparable<ExitStatus> {

    /**
     * Convenient constant value representing unknown state.
     */
    public static final ExitStatus UNKNOWN = new ExitStatus("UNKNOWN");

    /**
     * Convenient constant value representing completed state.
     */
    public static final ExitStatus COMPLETED = new ExitStatus("COMPLETED");

    /**
     * Convenient constant value representing failed state.
     */
    public static final ExitStatus FAILED = new ExitStatus("FAILED");

    /**
     * Convenient constant value representing skipped state.
     */
    public static final ExitStatus SKIPPED = new ExitStatus("SKIPPED");

    private final String exitCode;

    private final String exitDescription;

    public ExitStatus(String exitCode) {
        this(exitCode, "");
    }

    public ExitStatus(String exitCode, String exitDescription) {
        super();
        this.exitCode = exitCode;
        this.exitDescription = exitDescription == null ? "" : exitDescription;
    }

    /**
     * Getter for the exit code (defaults to blank).
     *
     * @return the exit code.
     */
    public String getExitCode() {
        return exitCode;
    }

    /**
     * Getter for the exit description (defaults to blank)
     *
     * @return {@link String} containing the exit description.
     */
    public String getExitDescription() {
        return exitDescription;
    }

    /**
     * Create a new {@link ExitStatus} with a logical combination of the exit code, and a concatenation of the
     * descriptions. If either value has a higher severity then its exit code will be used in the result. In the case of
     * equal severity, the exit code is replaced if the new value is alphabetically greater.<br>
     * <br>
     *
     * Severity is defined by the exit code:
     * <ul>
     * <li>Codes beginning with COMPLETED have severity 1</li>
     * <li>Codes beginning with SKIPPED have severity 2</li>
     * <li>Codes beginning with FAILED have severity 3</li>
     * <li>Codes beginning with UNKNOWN have severity 4</li>
     * </ul>
     * Others have severity 5, so custom exit codes always win.<br>
     *
     * If the input is null just return this.
     *
     * @param status an {@link ExitStatus} to combine with this one.
     * @return a new {@link ExitStatus} combining the current value and the argument provided.
     */
    public ExitStatus and(ExitStatus status) {
        if (status == null) {
            return this;
        }
        ExitStatus result = addExitDescription(status.exitDescription);
        if (compareTo(status) < 0) {
            result = result.replaceExitCode(status.exitCode);
        }
        return result;
    }

    /**
     * Add an exit code to an existing {@link ExitStatus}. If there is already a code present tit will be replaced.
     *
     * @param code the code to add
     * @return a new {@link ExitStatus} with the same properties but a new exit code.
     */
    public ExitStatus replaceExitCode(String code) {
        return new ExitStatus(code, exitDescription);
    }

    /**
     * Add an exit description to an existing {@link ExitStatus}. If there is already a description present the two will
     * be concatenated with a semicolon.
     *
     * @param description the description to add
     * @return a new {@link ExitStatus} with the same properties but a new exit description
     */
    public ExitStatus addExitDescription(String description) {
        StringBuilder buffer = new StringBuilder();
        boolean changed = StringUtils.hasText(description) && !exitDescription.equals(description);
        if (StringUtils.hasText(exitDescription)) {
            buffer.append(exitDescription);
            if (changed) {
                buffer.append("; ");
            }
        }
        if (changed) {
            buffer.append(description);
        }
        return new ExitStatus(exitCode, buffer.toString());
    }

    /**
     * @param status an {@link ExitStatus} to compare
     * @return greater than zero, 0, less than zero according to the severity and exit code
     * @see java.lang.Comparable
     */
    @Override
    public int compareTo(ExitStatus status) {
        if (severity(status) > severity(this)) {
            return -1;
        }
        if (severity(status) < severity(this)) {
            return 1;
        }
        return this.getExitCode().compareTo(status.getExitCode());
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("exitCode=%s, exitDescription=%s", exitCode, exitDescription);
    }

    /**
     * Compare the fields one by one.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        return toString().equals(obj.toString());
    }

    /**
     * Compatible with the equals implementation.
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * @param status
     * @return
     */
    private int severity(ExitStatus status) {
        if (status.exitCode.startsWith(UNKNOWN.exitCode)) {
            return 0;
        }
        if (status.exitCode.startsWith(COMPLETED.exitCode)) {
            return 1;
        }
        if (status.exitCode.startsWith(SKIPPED.exitCode)) {
            return 2;
        }
        if (status.exitCode.startsWith(FAILED.exitCode)) {
            return 3;
        }
        return 4;
    }

}