package com.skunk.core.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 时间工具类型
 *
 * @author walker
 */
public class DateUtils {

    /**
     * 　获取当前时间
     *
     * @return　返回Date类型
     */
    public static Date nowDate() {
        return new Date();
    }

    /**
     * 　获取当前时间
     *
     * @return　返回LocalDateTime类型
     */
    public static LocalDateTime nowLocalDateTime() {
        return LocalDateTime.now();
    }

    /**
     * 获得纪元秒
     *
     * @return
     */
    public static Long nowEpochSecond() {
        return Instant.now().getEpochSecond();
    }

    /**
     * 获得纪元毫秒
     *
     * @return
     */
    public static Long nowEpochMilli() {
        return Instant.now().toEpochMilli();
    }
}
