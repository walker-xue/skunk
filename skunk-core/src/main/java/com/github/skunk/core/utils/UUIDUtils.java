package com.github.skunk.core.utils;

import java.util.UUID;

/**
 * @author walker
 * @since 1.0.1
 */
public class UUIDUtils {

    /**
     * 获取系统生成的UUID（32位）
     *
     * @return
     */
    public static String getRandomUUID() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    /**
     * 获取系统生成的UUID（8位）
     *
     * @return
     */
    public static String get8UUID() {
        String uuidValue = UUID.randomUUID().toString();
        String[] uuidParts = uuidValue.split("-");
        return uuidParts[0].toLowerCase();
    }

    /**
     * 获取系统生成的UUID（12位）
     *
     * @return
     */
    public static String get12UUID() {
        String uuidValue = UUID.randomUUID().toString();
        String[] uuidParts = uuidValue.split("-");
        return uuidParts[0].concat(uuidParts[1]).toLowerCase();
    }
}
