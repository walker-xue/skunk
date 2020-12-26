package com.skunk.core.utils;

import org.springframework.util.Assert;


/**
 * StringHex操作
 *
 * @author LCX
 */
public class HexUtils {

    /**
     * 转化十六进制编码为字符串
     *
     * @param src
     * @return
     */
    public static String toHex(String src) {

        Assert.notNull(src, " to String hex is null");

        byte[] baKeyword = new byte[src.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            baKeyword[i] = (byte) (0xff & Integer.parseInt(src.substring(i * 2, i * 2 + 2), 16));
        }
        return new String(baKeyword, CharsetUtils.CHARSET_UTF_8);
    }

}
