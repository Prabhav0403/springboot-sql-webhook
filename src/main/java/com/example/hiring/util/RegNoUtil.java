package com.example.hiring.util;

public final class RegNoUtil {
    private RegNoUtil() {}

    /**
     * Extracts the last two digits in regNo (ignores non-digits) and returns true if odd.
     * Example: "REG12347" -> 47 -> odd.
     */
    public static boolean lastTwoDigitsOdd(String regNo) {
        if (regNo == null) return false;
        String digits = regNo.replaceAll("\\D+", "");
        if (digits.length() < 2) return false;
        int val = Integer.parseInt(digits.substring(digits.length() - 2));
        return (val % 2) == 1;
    }
}
