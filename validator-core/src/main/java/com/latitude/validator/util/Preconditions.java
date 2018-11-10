package com.latitude.validator.util;

public class Preconditions {

    public static void notNull(Object argument, String errorMessage) {
        if (argument == null) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void hasText(String argument, String errorMessage) {
        if (argument == null || argument.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

}
