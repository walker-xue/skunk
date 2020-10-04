package com.github.skunk.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.github.skunk.core.validation.ValidateUtils;

import java.util.Map;

/**
 * JSON 工具
 *
 * @author walker
 * @since 0.0.1
 */
public class JSONUtils {

    /**
     * 判断是否JSON对象
     *
     * @param json
     *     字符串
     * @return 返回是否是正确JSON
     */
    public static boolean isJson(String json) {

        ValidateUtils.notBlank(json, "Json not null.");

        try {
            JSON.parse(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断是否JSON数组
     *
     * @param json
     *     字符串
     * @return 返回是否是正确JSON
     */
    public static boolean isJsonArray(String json) {

        ValidateUtils.notBlank(json, "Json not null.");

        try {
            JSON.parseArray(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * JSON字符串转换成JAVA map 对象
     *
     * @param json
     * @return
     */
    public static Map<String, String> toMap(String json) {
        ValidateUtils.notBlank(json, "Json not null.");

        return JSON.parseObject(json, new TypeReference<Map<String, String>>() {

        });
    }

}
