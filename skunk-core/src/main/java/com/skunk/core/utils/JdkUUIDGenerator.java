package com.skunk.core.utils;

import org.springframework.util.JdkIdGenerator;

import java.util.UUID;

/**
 * @author walker
 * @since 1.0.1
 */
public class JdkUUIDGenerator extends JdkIdGenerator {

    static final String SYMBOL = "";

    /**
     * 获取系统生成的UUID（32位）
     *
     * @return
     */
    public static String generateRandom() {
        return UUID.randomUUID().toString().replace(SYMBOL, "").toLowerCase();
    }

    /**
     * 获取系统生成的UUID（8位）
     *
     * @return
     */
    public static String generateRandom8() {
        String uuidValue = UUID.randomUUID().toString();
        String[] uuidParts = uuidValue.split(SYMBOL);
        return uuidParts[0].toLowerCase();
    }

    /**
     * 获取系统生成的UUID（12位）
     *
     * @return
     */
    public static String generateRandom12() {
        String uuidValue = UUID.randomUUID().toString();
        String[] uuidParts = uuidValue.split(SYMBOL);
        return uuidParts[0].concat(uuidParts[1]).toLowerCase();
    }
}
