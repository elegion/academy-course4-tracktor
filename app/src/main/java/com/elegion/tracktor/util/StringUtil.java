package com.elegion.tracktor.util;

import java.util.Locale;

public class StringUtil {

    public static String getTimeText(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        return String.format(Locale.ENGLISH, "%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static String getDistanceText(double value) {
        return round(value, 0) + " м.";
    }

    public static String round(double value, int places) {
        return String.format("%." + places + "f", value);
    }
}
