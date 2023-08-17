package com.javayh.secure.transmit.encrypt.rsa;

import com.javayh.secure.transmit.constant.EncryptConstantUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * <p>
 * 来自 zhao-baolin
 * https://github.com/yanghaiji/javayh-platform/blob/master/javayh-dependencies/javayh-common-starter/src/main/java/com/javayh/common/encrypt/rsa/
 * </p>
 *
 * @author Dylan-haiji
 * @version 1.0.0
 * @since 2020/3/5
 */
@Slf4j
public class RsaTools {

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 公钥模量
     */
    public static String publicModulus = null;

    /**
     * 公钥指数
     */
    public static String publicExponent = null;

    /**
     * 私钥模量
     */
    public static String privateModulus = null;

    /**
     * 私钥指数
     */
    public static String privateExponent = null;

    private static KeyFactory keyFactory = null;


    static {
        try {
            keyFactory = KeyFactory.getInstance(EncryptConstantUtils.KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public RsaTools() {
        try {
            /*
             * 指定key的大小(64的整数倍,最小512位)
             */
            int keySize = 1024;
            generateKeyPairString(keySize);
        } catch (Exception e) {
            log.error("generateKeyPairString {}", e.getMessage());
        }
    }

    public RsaTools(int keySize) {
        try {
            generateKeyPairString(keySize);
        } catch (Exception e) {
            log.error("generateKeyPairString {}", e.getMessage());
        }
    }

    /**
     * <p>
     * 生成密钥对字符串
     * </p>
     *
     * @param keySize
     * @return void
     * @version 1.0.0
     * @author Dylan-haiji
     * @since 2020/3/5
     */
    private void generateKeyPairString(int keySize) throws Exception {
        /* RSA算法要求有一个可信任的随机数源 */
        SecureRandom sr = new SecureRandom();
        /* 为RSA算法创建一个KeyPairGenerator对象 */
        KeyPairGenerator kpg = KeyPairGenerator
                .getInstance(EncryptConstantUtils.KEY_ALGORITHM);
        /* 利用上面的随机数据源初始化这个KeyPairGenerator对象 */
        kpg.initialize(keySize, sr);
        /* 生成密匙对 */
        KeyPair kp = kpg.generateKeyPair();
        /* 得到公钥 */
        Key publicKey = kp.getPublic();
        /* 得到私钥 */
        Key privateKey = kp.getPrivate();
        /* 用字符串将生成的密钥写入文件 获取算法 */
        String algorithm = publicKey.getAlgorithm();
        KeyFactory keyFact = KeyFactory.getInstance(algorithm);
        BigInteger prime = null;
        BigInteger exponent = null;
        RSAPublicKeySpec keySpec = keyFact.getKeySpec(publicKey, RSAPublicKeySpec.class);
        prime = keySpec.getModulus();
        exponent = keySpec.getPublicExponent();
        RsaTools.publicModulus = HexUtil.bytes2Hex(prime.toByteArray());
        RsaTools.publicExponent = HexUtil.bytes2Hex(exponent.toByteArray());

        RSAPrivateCrtKeySpec privateKeySpec = keyFact.getKeySpec(privateKey,
                RSAPrivateCrtKeySpec.class);
        BigInteger privateModulus = privateKeySpec.getModulus();
        BigInteger privateExponent = privateKeySpec.getPrivateExponent();
        RsaTools.privateModulus = HexUtil.bytes2Hex(privateModulus.toByteArray());
        RsaTools.privateExponent = HexUtil.bytes2Hex(privateExponent.toByteArray());
    }


    /**
     * 使用给定的公钥加密给定的字符串。
     *
     * @param key       给定的公钥。
     * @param plaintext 字符串。
     * @return 给定字符串的密文。
     */
    public static String encrypt(String key, String plaintext) {
        if (key == null || plaintext == null) {
            return null;
        }
        byte[] data = plaintext.getBytes();
        try {
            PublicKey publicKey = RSAEncrypt.getPublicKey(key);
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

    private static byte[] encrypt(Key key, byte[] data) throws Exception {
        Cipher ci = Cipher.getInstance(EncryptConstantUtils.ALGORITHM);
        ci.init(Cipher.ENCRYPT_MODE, key);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = ci.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = ci.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * 使用给定的公钥解密给定的字符串。
     *
     * @param key  给定的公钥
     * @param data 密文
     * @return 原文字符串。
     */
    public static String decrypt(String key, String data) {
        if (key == null || isBlank(data)) {
            return null;
        }
        try {
            PrivateKey privateKey = RSAEncrypt.getPrivateKey(key);
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
        Cipher ci = Cipher.getInstance(EncryptConstantUtils.ALGORITHM);
        ci.init(Cipher.DECRYPT_MODE, key);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = ci.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = ci.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
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