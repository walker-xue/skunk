package com.skunk.core.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lombok.extern.slf4j.Slf4j;

/**
 * 获取文件MD5值
 *
 *
 * @author nanfeng
 * @date 2019年12月10日
 * @since 0.0.1
 */
@Slf4j
public class FileMD5Utils {

    public static final String MD5 = "MD5";

    /**
     * 获取byte MD5
     *
     * @param fileByte
     * @return
     */
    public static String fileToMD5(byte[] fileByte) {
        try {
            MessageDigest md5 = MessageDigest.getInstance(MD5);
            md5.update(fileByte);
            BigInteger bi = new BigInteger(1, md5.digest());
            return bi.toString(16);
        } catch (NoSuchAlgorithmException e) {
            log.error("error:{}", e.getMessage());
            return null;
        }
    }

    /**
     * 获取文件MD5
     *
     * @param file
     * @return
     */
    public static String getStringMd5(File file) {
        try (FileInputStream in = new FileInputStream(file)) {
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance(MD5);
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            return bi.toString(16);
        } catch (Exception e) {
            log.error("error:{}", e.getMessage());
            return null;
        }
    }

    /**
     * 获取文件MD5
     *
     * @param filePath
     * @return
     */
    public byte[] fileToBytes(String filePath) {
        byte[] buffer = null;
        try (FileInputStream fis = new FileInputStream(new File(filePath)); ByteArrayOutputStream bos = new ByteArrayOutputStream(1000)) {
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            buffer = bos.toByteArray();
        } catch (IOException e) {
            log.error("error:{}", e.getMessage());
        }
        return buffer;
    }
}