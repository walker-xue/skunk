package com.skunk.code.generate.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.util.StringUtils;

/**
 * author 张新林
 * 时间 2019/6/12 21:28
 * 描述
 */
public class CommonUtils {

    /**
     * 去掉字符串指定的前缀
     *
     * @param str
     *     字符串名称
     * @param prefix
     *     前缀数组
     * @return
     */
    public static String removePrefix(String str, String[] prefix) {
        if (StringUtils.isEmpty(str)) {
            return "";
        } else {
            if (null != prefix) {
                String[] prefixArray = prefix;

                for (int i = 0; i < prefix.length; ++i) {
                    String pf = prefixArray[i];
                    if (str.toLowerCase().matches("^" + pf.toLowerCase() + ".*")) {
                        return str.substring(pf.length());
                    }
                }
            }

            return str;
        }
    }

    public static String getFormatTime(String pattern, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        return sdf.format(date == null ? new Date() : date);
    }
}
