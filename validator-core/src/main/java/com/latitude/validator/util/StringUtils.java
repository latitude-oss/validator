package com.latitude.validator.util;

public class StringUtils {

    public static boolean hasText(String input) {
        return input != null && input.trim().length() > 0;
    }

}
