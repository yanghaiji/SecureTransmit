package com.javayh.secure.transmit.encrypt.aes;

import com.javayh.secure.transmit.configuration.properties.SecretProperties;
import com.javayh.secure.transmit.constant.EncryptConstant;
import com.javayh.secure.transmit.encrypt.SecureTransmitTemplate;
import com.javayh.secure.transmit.encrypt.base.Base64Util;
import com.javayh.secure.transmit.exception.EncryptionException;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * <p>
 * * https://github.com/yanghaiji/javayh-platform/blob/master/javayh-dependencies/javayh-common-starter/src/main/java/com/javayh/common/encrypt/
 * 加密
 * </p>
 *
 * @author Dylan-haiji
 * @version 1.0.0
 */
@Slf4j
public class AESEncryption implements SecureTransmitTemplate {

    private static SecretKeySpec key;

    private static Cipher encCipher = null;

    private static Cipher decCipher = null;

    private SecretProperties secretProperties;

    public AESEncryption(SecretProperties secretProperties) {
        this.secretProperties = secretProperties;
    }

    /**
     * @param key     加密的key
     * @param encData
     * @return
     */
    @Override
    public String encrypt(String key, String encData) {
        key = setSuffix(key, secretProperties.getAes().getIv());
        byte[] result = new byte[0];
        try {
            result = encCipher(key).doFinal(encData.getBytes());
        } catch (IllegalBlockSizeException e) {
            log.error(" IllegalBlockSizeException is error {}", e.getMessage());
            throw new EncryptionException(e.getMessage());
        } catch (BadPaddingException e) {
            log.error(" BadPaddingException is error {}", e.getMessage());
            throw new EncryptionException(e.getMessage());
        }
        return Base64Util.encode(result);
    }


    /**
     * @param key  解密的key
     * @param data 待解密的数据
     */
    @Override
    public String decrypt(String key, String data) {
        key = setSuffix(key, secretProperties.getAes().getIv());
        byte[] result = new byte[0];
        try {
            result = decCipher(key).doFinal(Base64Util.decode(data));
        } catch (Exception e) {
            log.error(" error {}", e.getMessage());
        }
        return new String(result);
    }

    private static Cipher encCipher(String iv) {
        byte[] enCodeFormat = Arrays.copyOf(iv.getBytes(), 16);
        key = new SecretKeySpec(enCodeFormat, EncryptConstant.ALGORITHM_AES);
        try {
            encCipher = Cipher.getInstance(EncryptConstant.INSTANCE);
            encCipher.init(Cipher.ENCRYPT_MODE, key);
            return encCipher;
        } catch (NoSuchAlgorithmException e) {
            log.error(" NoSuchAlgorithmException is error {}", e.getMessage());
        } catch (NoSuchPaddingException e) {
            log.error(" NoSuchPaddingException is error {}", e.getMessage());
        } catch (InvalidKeyException e) {
            log.error(" InvalidKeyException is error {}", e.getMessage());
        }
        return encCipher;
    }

    private static Cipher decCipher(String iv) {
        byte[] enCodeFormat = Arrays.copyOf(iv.getBytes(), 16);
        key = new SecretKeySpec(enCodeFormat, EncryptConstant.ALGORITHM_AES);
        try {
            decCipher = Cipher.getInstance(EncryptConstant.INSTANCE);
            decCipher.init(Cipher.DECRYPT_MODE, key);
            return decCipher;
        } catch (NoSuchAlgorithmException e) {
            log.error(" NoSuchAlgorithmException is error {}", e.getMessage());
        } catch (NoSuchPaddingException e) {
            log.error(" NoSuchPaddingException is error {}", e.getMessage());
        } catch (InvalidKeyException e) {
            log.error(" InvalidKeyException is error {}", e.getMessage());
        }
        return decCipher;
    }

    private String setSuffix(String key, String suffix) {
        return key + suffix;
    }

    public void setSecretProperties(SecretProperties secretProperties) {
        this.secretProperties = secretProperties;
    }
}
