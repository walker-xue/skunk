package com.skunk.core.utils;


import java.time.format.DateTimeFormatter;

/**
 *
 */
public class DateFormatUtils {

    public static final DateTimeFormatter ISO_SIMPLIFY_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static final DateTimeFormatter ISO_DATETIME_FORMAT_24 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

}
