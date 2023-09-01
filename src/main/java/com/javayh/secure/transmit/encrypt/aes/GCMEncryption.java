package com.javayh.secure.transmit.encrypt.aes;

import com.javayh.secure.transmit.configuration.properties.SecretProperties;
import com.javayh.secure.transmit.constant.EncryptConstant;
import com.javayh.secure.transmit.encrypt.SecureTransmitTemplate;
import com.javayh.secure.transmit.encrypt.base.Base64Util;
import com.javayh.secure.transmit.exception.EncryptionException;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 * AES GCM  加密工具
 * </p>
 *
 * @author hai ji
 * @version 1.0.0
 * @since 2023-08-24
 */
@Slf4j
public class GCMEncryption implements SecureTransmitTemplate {

    private final String iv;

    public GCMEncryption(SecretProperties secretProperties) {
        this.iv = secretProperties.getGcm().getIv();
    }

    @Override
    public String encrypt(String keyAsString, String data) {
        SecretKey secretKey = convertStringToKey(keyAsString);
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(EncryptConstant.AES_GCM);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);
            return Base64Util.encode(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException |
                InvalidKeyException |
                InvalidAlgorithmParameterException |
                IllegalBlockSizeException |
                BadPaddingException e) {
            log.error("GCM 解密异常 {}", e.getMessage(), e);
            throw new EncryptionException(e.getMessage());
        }

    }


    @Override
    public String decrypt(String keyAsString, String encryptedData) {
        try {
            SecretKey secretKey = convertStringToKey(keyAsString);
            Cipher cipher = Cipher.getInstance(EncryptConstant.AES_GCM);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);

            byte[] decryptedBytes = cipher.doFinal(encryptedData.getBytes((StandardCharsets.UTF_8)));
            return Base64Util.encode(decryptedBytes);
        } catch (Exception e) {
            log.error("AESGCMEncryption 解密失败 {}", e.getMessage());
            throw new EncryptionException(e.getMessage());
        }
    }

    /**
     * 将字符串转换为密钥
     *
     * @param keyAsString 密钥key
     */
    private SecretKey convertStringToKey(String keyAsString) {
        byte[] decodedKey = Base64Util.decode(keyAsString);
        return new javax.crypto.spec.SecretKeySpec(decodedKey, EncryptConstant.ALGORITHM_AES);
    }
}
