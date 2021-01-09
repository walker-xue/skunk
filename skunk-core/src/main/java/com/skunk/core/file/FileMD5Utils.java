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
import java.util.Objects;

import com.skunk.core.utils.Objects2;

import lombok.extern.slf4j.Slf4j;

/**
 * 获取文件MD5值
 *
 * @author nanfeng
 * @date 2019年12月10日
 * @since 0.0.1
 */
@Slf4j
public class FileMD5Utils {

    public static final String MD5 = "MD5";

    /**
     * 获取文件对应的MD5
     *
     * @param bytes
     * @return
     */
    public static String fileToMD5(byte[] bytes) {

        Objects.requireNonNull(bytes);

        try {
            MessageDigest md5 = MessageDigest.getInstance(MD5);
            md5.update(bytes);
            BigInteger bi = new BigInteger(1, md5.digest());
            return bi.toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取文件MD5值
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
            throw new RuntimeException(e);
        }
    }

    /**
     * 文件转成byte
     *
     * @param filePath
     *     文件路经
     * @return
     */
    public byte[] fileToBytes(String filePath) {

        Objects2.requireNotBlank(filePath);

        try (FileInputStream fis = new FileInputStream(new File(filePath));
             ByteArrayOutputStream bos = new ByteArrayOutputStream(1000)) {
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            bos.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}