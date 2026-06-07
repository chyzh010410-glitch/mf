package com.mf.common.util;
import java.time.LocalDate;

public final class SeasonUtil {
    private SeasonUtil() {}
    public static String currentSeason() {
        return seasonOf(LocalDate.now().getMonthValue());
    }
    public static String seasonOf(int month) {
        if (month >= 3 && month <= 5) return "spring";
        if (month >= 6 && month <= 8) return "summer";
        if (month >= 9 && month <= 11) return "autumn";
        return "winter";
    }
}
