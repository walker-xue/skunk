package com.github.skunk.core.utils;

import java.io.IOException;

/**
 * StringHex操作
 *
 * @author LCX
 */
public class HexUtils {

    /**
     * 转化十六进制编码为字符串
     *
     * @param s
     * @return
     * @throws IOException
     */
    public static String toStringHex(String s) throws IOException {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
        }
        return new String(baKeyword, "utf-8");
    }

}
