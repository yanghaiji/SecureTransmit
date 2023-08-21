package com.javayh.secure.transmit.encrypt.rsa;

import com.javayh.secure.transmit.constant.EncryptConstant;
import com.javayh.secure.transmit.encrypt.DataKeyGenerator;
import com.javayh.secure.transmit.encrypt.EnDeTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * <p>
 * https://github.com/yanghaiji/javayh-platform/blob/master/javayh-dependencies/javayh-common-starter/src/main/java/com/javayh/common/encrypt/rsa/
 * </p>
 *
 * @author Dylan-haiji
 * @version 1.0.0
 * @since 2020/3/5
 */
@Slf4j
public class RsaCrypto implements EnDeTemplate {

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 使用给定的公钥加密给定的字符串。
     *
     * @param key       给定的公钥。
     * @param plaintext 字符串。
     * @return 给定字符串的密文。
     */
    @Override
    public String encrypt(String key, String plaintext) {
        if (key == null || plaintext == null) {
            return null;
        }
        byte[] data = plaintext.getBytes();
        try {
            PublicKey publicKey = DataKeyGenerator.RSA.getPublicKey(key);
            byte[] enData = encrypt(publicKey, data);
            return Base64.encodeBase64String(enData);
        } catch (Exception ex) {
            log.error("encryptString error {}", ex.getMessage());
        }
        return null;
    }

    /**
     * 使用指定的公钥加密数据。
     *
     * @param key  给定的公钥。
     * @param data 要加密的数据。
     * @return 加密后的数据。
     */

    private byte[] encrypt(Key key, byte[] data) throws Exception {
        Cipher ci = Cipher.getInstance(EncryptConstant.ALGORITHM);
        ci.init(Cipher.ENCRYPT_MODE, key);
        return getEnDeData(data, ci, MAX_ENCRYPT_BLOCK);
    }

    /**
     * 使用给定的公钥解密给定的字符串。
     *
     * @param key  给定的公钥
     * @param data 密文
     * @return 原文字符串。
     */
    @Override
    public String decrypt(String key, String data) {
        if (key == null || isBlank(data)) {
            return null;
        }
        try {
            PrivateKey privateKey = DataKeyGenerator.RSA.getPrivateKey(key);
            byte[] enData = Base64.decodeBase64(data);
            return new String(decrypt(privateKey, enData));
        } catch (Exception ex) {
            log.error(" Decryption failed. Cause: {} , error {}", data, ex.getCause().getMessage());
        }
        return null;
    }

    /**
     * 使用指定的公钥解密数据。
     *
     * @param key  指定的公钥
     * @param data 要解密的数据
     * @return 原数据
     */
    private static byte[] decrypt(Key key, byte[] data) throws Exception {
        Cipher ci = Cipher.getInstance(EncryptConstant.ALGORITHM);
        ci.init(Cipher.DECRYPT_MODE, key);
        return getEnDeData(data, ci, MAX_DECRYPT_BLOCK);
    }

    /**
     * 数据加解密
     */
    private static byte[] getEnDeData(byte[] data, Cipher ci, int maxBlock) throws Exception {
        int inputLen = data.length;
        byte[] edData;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > maxBlock) {
                    cache = ci.doFinal(data, offSet, maxBlock);
                } else {
                    cache = ci.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * maxBlock;
            }
            edData = out.toByteArray();
        }
        return edData;
    }

    /**
     * 判断非空字符串
     *
     * @param cs 待判断的CharSequence序列
     * @return 是否非空
     */
    private static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}