package com.lkvcodestudio.pagination;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utils {
    public static LocalDate convertStrToDate(String dateStr, String dateFormat) {
        LocalDate date = null;
        if (dateStr != null) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
            date = LocalDate.parse(dateStr, dateTimeFormatter);
        }
        return date;
    }

    public static String convertDateToStr(LocalDate date, String dateFormat) {
        String dateStr = null;
        if (date != null) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
            dateStr = date.format(dateTimeFormatter);
        }
        return dateStr;
    }
}
