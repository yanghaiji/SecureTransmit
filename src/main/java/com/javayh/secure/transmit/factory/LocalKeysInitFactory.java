package com.javayh.secure.transmit.factory;

import com.javayh.secure.transmit.bean.SecretType;
import com.javayh.secure.transmit.configuration.properties.*;
import com.javayh.secure.transmit.exception.InvalidAlgorithmException;

/**
 * <p>
 * 初始化key 值
 * </p>
 *
 * @author hai ji
 * @version 1.0.0
 * @since 2023-08-24
 */
public class LocalKeysInitFactory {

    /**
     * 初始化 加密辅助key，将其变成 thread local
     *
     * @param secretProperties 安全信息配置 {@link SecretProperties}
     * @param publicKey        local public key
     * @param privateKey       local private key
     */
    public static void initLocalKeys(SecretProperties secretProperties,
                                     ThreadLocal<String> publicKey,
                                     ThreadLocal<String> privateKey) {
        RsaProperties rsa = secretProperties.getRsa();
        AesProperties aes = secretProperties.getAes();
        EccProperties ecc = secretProperties.getEcc();
        GcmProperties gcm = secretProperties.getGcm();
        SecretType type = secretProperties.getType();
        switch (type) {
            case RSA:
                publicKey.set(rsa.getPublicKey());
                privateKey.set(rsa.getPrivateKey());
                break;
            case AES:
                String key = aes.getKey();
                publicKey.set(key);
                privateKey.set(key);
                break;
            case GCM:
                String gcmKey = gcm.getKey();
                publicKey.set(gcmKey);
                privateKey.set(gcmKey);
                break;
            case ECC:
                publicKey.set(ecc.getPublicKey());
                privateKey.set(ecc.getPrivateKey());
                break;
            case DES:
            case DES3:
            case TF:
            case BF:
            case DH:
            default:
                throw new InvalidAlgorithmException("Invalid algorithm: " + type.name());
        }
    }
}
