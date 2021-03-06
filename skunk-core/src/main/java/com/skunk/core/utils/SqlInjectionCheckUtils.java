package com.skunk.core.utils;

/**
 * 检查是否存在SQL注入
 *
 * @author csf
 * @since 0.0.1
 */
public class SqlInjectionCheckUtils {

    private static final String injectionStr = "exec|insert|select|delete|grant|update|count|chr|mid|master|truncate|char|declare|;";

    /**
     * 是否存在sql注入
     *
     * @param value
     *     校验的字符串
     * @return 如果存在sql的关键字返回true
     */
    public static boolean isSqlInjection(String value) {
        return String2Utils.isNotEmpty(value) && isSqlInjection(value, injectionStr);
    }

    /**
     * 检查使用存在指定的字符
     *
     * @param value
     * @param injectionString
     * @return
     */
    public static boolean isSqlInjection(String value, String injectionString) {
        if (String2Utils.isNotEmpty(value) && String2Utils.isNotEmpty(injectionString)) {
            String[] injectionStrings = injectionString.split("\\|");
            for (String injection : injectionStrings) {
                if (value.contains(injection + " ")) {
                    return true;
                }
            }
        }
        return false;
    }
}
