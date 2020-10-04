package com.github.skunk.core.utils;

import java.util.UUID;

public class UUIDUtils {

    public static String getRandomUUID() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    public static String get8UUID() {
        String uuidValue = UUID.randomUUID().toString();
        String[] uuidParts = uuidValue.split("-");
        return uuidParts[0].toLowerCase();
    }

    public static String get12UUID() {
        String uuidValue = UUID.randomUUID().toString();
        String[] uuidParts = uuidValue.split("-");
        return uuidParts[0].concat(uuidParts[1]).toLowerCase();
    }
}
