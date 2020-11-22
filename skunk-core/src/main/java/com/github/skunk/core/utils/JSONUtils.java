package com.github.skunk.core.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JSONUtils {

    /**
     * 如果有不识别的属性，不会报错，只会忽略。
     */
    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    private JSONUtils() {
    }

    /**
     * 将对象转为 json <br>
     *
     * @param object
     *     {@link Object}
     * @return json 字符串
     */
    public static String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("转换Json出错", e);
        }
    }

    /**
     * 将字符串 json 转为对象<br>
     *
     * @param <T>
     *     转换后的类型
     * @param content
     *     json 字符串
     * @param type
     *     转换后的类型
     * @return 转换后的结果
     */
    public static <T> T fromJson(String content, Class<T> type) {
        return toObject(mapper, content, type);
    }

    /**
     * 将字符串 json 转为对象<br>
     *
     * @param <T>
     *     转换后的类型
     * @param mapper
     *     {@link ObjectMapper}
     * @param content
     *     json 字符串
     * @param type
     *     转换后的类型
     * @return 转换后的结果
     */
    private static <T> T toObject(ObjectMapper mapper, String content, Class<T> type) {
        try {
            return mapper.readValue(content, type);
        } catch (IOException e) {
            throw new RuntimeException("json转换出错", e);
        }
    }
}