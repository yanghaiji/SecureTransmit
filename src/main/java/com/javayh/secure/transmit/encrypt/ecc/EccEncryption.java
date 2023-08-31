package com.javayh.secure.transmit.encrypt.ecc;

import com.javayh.secure.transmit.configuration.properties.EccProperties;
import com.javayh.secure.transmit.constant.EncryptConstant;
import com.javayh.secure.transmit.encrypt.SecureTransmitTemplate;
import com.javayh.secure.transmit.encrypt.base.Base64Util;
import com.javayh.secure.transmit.exception.EncryptionException;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * <p>
 * ecc 加密
 * </p>
 *
 * @author hai ji
 * @version 1.0.0
 * @since 2023-08-22
 */
@Slf4j
public class EccEncryption implements SecureTransmitTemplate {

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    /**
     * 数据加密
     *
     * @param pubKey    {@link EccProperties#getPublicKey()} 加密公钥
     * @param plaintext 代加密的数据
     */
    @Override
    public String encrypt(String pubKey, String plaintext) {
        PublicKey publicKey = stringToKey(pubKey);
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(EncryptConstant.ECIES, EncryptConstant.BC);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
            return Base64Util.encode(encryptedBytes);
        } catch (NoSuchAlgorithmException |
                NoSuchProviderException |
                NoSuchPaddingException |
                IllegalBlockSizeException |
                BadPaddingException |
                InvalidKeyException e) {
            log.error("EccEncryption 解密失败 {}", e.getMessage());
            throw new EncryptionException(e.getMessage());
        }

    }

    /**
     * @param priKey        解密私钥 {@link EccProperties#getPrivateKey()}
     * @param encryptedData 待解密的数据
     */
    @Override
    public String decrypt(String priKey, String encryptedData) {
        PrivateKey privateKey = stringToPrivateKey(priKey);
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(EncryptConstant.ECIES, EncryptConstant.BC);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] encryptedBytes = Base64Util.decode(encryptedData);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return Base64Util.encode(decryptedBytes);
        } catch (NoSuchAlgorithmException |
                NoSuchProviderException |
                NoSuchPaddingException |
                IllegalBlockSizeException |
                BadPaddingException |
                InvalidKeyException e) {
            log.error("EccEncryption 解密失败 {}", e.getMessage());
            throw new EncryptionException(e.getMessage());
        }

    }

    /**
     * 反算公钥
     */
    private static PublicKey stringToKey(String keyString) {
        byte[] keyBytes = Base64.getDecoder().decode(keyString);
        KeyFactory keyFactory;
        try {
            keyFactory = KeyFactory.getInstance(EncryptConstant.EC, EncryptConstant.BC);
            return keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException e) {
            log.error("EccEncryption 解密失败 {}", e.getMessage());
            throw new EncryptionException(e.getMessage());
        }
    }

    /**
     * 反算私钥
     */
    private static PrivateKey stringToPrivateKey(String keyString) {
        byte[] keyBytes = Base64Util.decode(keyString);
        KeyFactory keyFactory;
        try {
            keyFactory = KeyFactory.getInstance(EncryptConstant.EC, EncryptConstant.BC);
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException e) {
            log.error("EccEncryption 解密失败 {}", e.getMessage());
            throw new EncryptionException(e.getMessage());
        }
    }

}
