package com.elegion.tracktor.util;

public class StringUtil {

    public static String getDistanceText(double value) {
        return round(value, 0) + " м.";
    }

    public static String round(double value, int places) {
        return String.format("%." + places + "f", value);
    }
}
