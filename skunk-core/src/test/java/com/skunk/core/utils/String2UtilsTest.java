package com.skunk.core.utils;

import org.junit.Test;

public class String2UtilsTest {

    @Test
    public void underscoreToCamelCase() {
        String userId = String2Utils.underscoreToCamelCase("user_id");
        System.out.println(String.format("underscoreToCamelCase: %s", userId));
    }
    @Test
    public void camelCaseToUnderscore() {
        String userId = String2Utils.camelCaseToUnderscore("userId");
        System.out.println(String.format("camelCaseToUnderscore: %s", userId));
    }
}