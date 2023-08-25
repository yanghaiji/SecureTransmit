package com.javayh.secure.transmit.encrypt.ecc;

import com.javayh.secure.transmit.configuration.properties.EccProperties;
import com.javayh.secure.transmit.constant.EncryptConstant;
import com.javayh.secure.transmit.encrypt.SecureTransmitTemplate;
import com.javayh.secure.transmit.encrypt.base.Base64Util;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
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
    public String encrypt(String pubKey, String plaintext) throws Exception {
        PublicKey publicKey = stringToKey(pubKey);
        Cipher cipher = Cipher.getInstance(EncryptConstant.ECIES, EncryptConstant.BC);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * @param priKey        {@link EccProperties#getPrivateKey()} ()} 解密私钥
     * @param encryptedData 待解密的数据
     */
    @Override
    @SneakyThrows
    public String decrypt(String priKey, String encryptedData) {
        PrivateKey privateKey = stringToPrivateKey(priKey);
        Cipher cipher = Cipher.getInstance(EncryptConstant.ECIES, EncryptConstant.BC);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] encryptedBytes = Base64Util.decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }

    /**
     * 反算公钥
     */
    @SneakyThrows
    private static PublicKey stringToKey(String keyString) {
        byte[] keyBytes = Base64.getDecoder().decode(keyString);
        KeyFactory keyFactory = KeyFactory.getInstance(EncryptConstant.EC, EncryptConstant.BC);
        return keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
    }

    /**
     * 反算私钥
     */
    @SneakyThrows
    private static PrivateKey stringToPrivateKey(String keyString) {
        byte[] keyBytes = Base64Util.decode(keyString);
        KeyFactory keyFactory = KeyFactory.getInstance(EncryptConstant.EC, EncryptConstant.BC);
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
    }

    public static void main(String[] args) throws Exception {
        EccEncryption eccCrypto = new EccEncryption();
        String encrypt = eccCrypto.encrypt("MIGbMBAGByqGSM49AgEGBSuBBAAjA4GGAAQBUx2lpwkU4Tsp7Ew3taAodarrru30NBysXqJJcO+58HwleWTfvQN+zMkGoPAY56+nNCD4qJlPt9B4MGEMHZsg2iEAk5tUd6TSe4QuHTs8jeKiPQhJd5/jqpBAH9fVYCwFjvlqF282rL7LKoHqyBhuGQsd4nOguZB0rymQ3gRW7J9ReZA=","test");
        String decrypt = eccCrypto.decrypt("MIH3AgEAMBAGByqGSM49AgEGBSuBBAAjBIHfMIHcAgEBBEIBaBKm6dbDCpu0+dfzuAZQWReoE3Se2Z/Ehtj2vvwnl98K/qEEKcbeTUSZECvmAPHhkVbjiTqO2ZH3wya9OxgvYtagBwYFK4EEACOhgYkDgYYABAFTHaWnCRThOynsTDe1oCh1quuu7fQ0HKxeoklw77nwfCV5ZN+9A37MyQag8Bjnr6c0IPiomU+30HgwYQwdmyDaIQCTm1R3pNJ7hC4dOzyN4qI9CEl3n+OqkEAf19VgLAWO+WoXbzasvssqgerIGG4ZCx3ic6C5kHSvKZDeBFbsn1F5kA== ", encrypt);

        System.out.println(encrypt);
        System.out.println(decrypt);
    }
}
