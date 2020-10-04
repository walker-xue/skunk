package com.github.skunk.core.utils;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;

/**
 * @Author:xueyanjun@ksjgs.com
 * @Description:
 * @History:
 * @Date:2016年8月5日
 */
public class ChinaDateFormatUtils extends DateFormatUtils {

    public static final FastDateFormat ISO_SIMPLIFY_TIME_FORMAT = FastDateFormat.getInstance("yyyyMMdd");

    public static final FastDateFormat ISO_DATETIME_FORMAT_24 = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

}
