package com.skunk.core.utils;


import java.time.format.DateTimeFormatter;

/**
 * 时间格式化定义
 *
 * @author walker
 */
public class DateFormatUtils {

    public static final DateTimeFormatter ISO_SIMPLIFY_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static final DateTimeFormatter ISO_EN_DATE_FORMAT = DateTimeFormatter.ofPattern("M/d/yyyy");
    public static final DateTimeFormatter ISO_EN_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("M/d/yyyy h:mm tt");

    public static final DateTimeFormatter ISO_zh_CH_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
    public static final DateTimeFormatter ISO_zh_CH_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分ss秒");

    public static final DateTimeFormatter ISO_zh_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter ISO_zh_DATE_time_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

}
