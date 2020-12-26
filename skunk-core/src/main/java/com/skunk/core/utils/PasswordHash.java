package com.skunk.core.utils;


import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * 负责密码哈希的生成和校验， 使用Salted SAH1哈希算法
 * <p>
 * 推荐使用Spring security Pwd
 */
@Deprecated
public class PasswordHash {

    private static final int SaltLength = 32;
    private static final int IterationCount = 65536;
    private static final int KeyLength = 128;

    /**
     * 计算密码的哈希值
     *
     * @param password 待哈希的密码
     * @return 返回密码的哈希值
     */
    public static String hash(String password) {
        if (password == null || password.length() == 0) {
            throw new IllegalArgumentException("不能计算空密码的哈希值。");
        }

        try {
            byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(SaltLength);
            return Base64.getEncoder().encodeToString(salt) + "$" + saltedSha1Hash(password, salt);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * 验证密码与哈希值是否匹配
     *
     * @param password 待校验的密码
     * @param hash     验证的哈希值
     * @return 密码与哈希是否匹配
     */
    public static boolean verify(String password, String hash) {
        if (password == null || password.length() == 0) {
            throw new IllegalArgumentException("不能验证空密码的哈希值。");
        }

        String[] saltAndPass = hash.split("\\$");
        if (saltAndPass.length != 2) {
            throw new IllegalArgumentException("传入哈希值的格式不正确。");
        }

        try {
            String hashOfInput = saltedSha1Hash(password, Base64.getDecoder().decode(saltAndPass[0]));
            return hashOfInput.equals(saltAndPass[1]);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * @param password
     * @param salt
     * @return
     * @throws Exception
     */
    private static String saltedSha1Hash(String password, byte[] salt) throws Exception {

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, IterationCount, KeyLength);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = factory.generateSecret(spec).getEncoded();

        return Base64.getEncoder().encodeToString(hash);
    }
}