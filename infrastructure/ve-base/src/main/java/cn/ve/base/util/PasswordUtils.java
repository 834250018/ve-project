package cn.ve.base.util;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.crypto.symmetric.PBKDF2;

import java.nio.charset.StandardCharsets;

/**
 * @author ve
 * @date 2021/8/2
 */
public class PasswordUtils {

    /**
     * 生成PBKDF2WithHmacSHA1摘要值(用于密码登录)
     *
     * @param plaintext 原文
     * @param salt      盐
     * @return
     */
    public static String genPwdCiphertext(String plaintext, String salt) {
        // 构造，算法PBKDF2WithHmacSHA1，密文长度512，迭代次数1000
        PBKDF2 pbkdf2 = new PBKDF2();
        byte[] encrypt = pbkdf2.encrypt(plaintext.toCharArray(), salt.getBytes(StandardCharsets.UTF_8));
        return Base64Encoder.encode(encrypt);
    }

}
