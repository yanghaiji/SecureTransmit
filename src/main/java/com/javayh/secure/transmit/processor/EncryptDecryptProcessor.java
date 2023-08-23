package com.javayh.secure.transmit.processor;

import com.javayh.secure.transmit.annotation.DecryptField;
import com.javayh.secure.transmit.annotation.EncryptField;
import com.javayh.secure.transmit.bean.SecretType;
import com.javayh.secure.transmit.configuration.properties.AesProperties;
import com.javayh.secure.transmit.configuration.properties.EccProperties;
import com.javayh.secure.transmit.configuration.properties.RsaProperties;
import com.javayh.secure.transmit.configuration.properties.SecretProperties;
import com.javayh.secure.transmit.encrypt.MessageDigest;
import com.javayh.secure.transmit.exception.InvalidAlgorithmException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * <p>
 * 加解密处理器，用于实际的加解密操作
 * </p>
 *
 * @author haiji
 */
@Slf4j
public class EncryptDecryptProcessor {

    /**
     * 密钥和机密算法的配置
     */
    private String publicKey;
    private String privateKey;
    private final Boolean isShowLog;
    private final SecretProperties secretProperties;

    public EncryptDecryptProcessor(SecretProperties secretProperties) {
        this.isShowLog = secretProperties.getIsShowLog();
        this.secretProperties = secretProperties;
        initializeKeys();
    }


    /**
     * 对字段值进行加密处理
     *
     * @param object {@link Object} 需要处理的对象
     * @return {@link Object} 加密后的数据
     */
    public <T> T encryptFields(T object) throws Exception {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(EncryptField.class)) {
                field.setAccessible(true);
                Object fieldValue = field.get(object);
                if (Objects.nonNull(fieldValue)) {
                    field.set(object, MessageDigest.getInstance(secretProperties).encrypt(publicKey, fieldValue.toString()));
                }
                if (isShowLog) {
                    log.info("encrypt before fieldName = {},fieldValue = {} ; after fieldValue = {}", field, fieldValue, field.get(object));
                }
            }
        }
        return object;
    }

    /**
     * 对值对象的值进行解密
     *
     * @param object {@link Object} 需要解密的对象
     */
    public <T> void decryptFields(T object) throws Exception {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(DecryptField.class)) {
                field.setAccessible(true);
                Object fieldValue = field.get(object);
                if (Objects.nonNull(fieldValue)) {
                    field.set(object, MessageDigest.getInstance(secretProperties).decrypt(privateKey, fieldValue.toString()));
                }
                if (isShowLog) {
                    log.info("decrypt before fieldName = {},fieldValue = {} ; after fieldValue = {}", field, fieldValue, field.get(object));
                }
            }
        }
    }

    private void initializeKeys() {
        RsaProperties rsa = secretProperties.getRsa();
        AesProperties aes = secretProperties.getAes();
        EccProperties ecc = secretProperties.getEcc();
        SecretType type = secretProperties.getType();
        switch (type) {
            case RSA:
                this.publicKey = rsa.getPublicKey();
                this.privateKey = rsa.getPrivateKey();
                break;
            case AES:
                String key = aes.getKey();
                this.publicKey = key;
                this.privateKey = key;
                break;
            case ECC:
                this.publicKey = ecc.getPublicKey();
                this.privateKey = ecc.getPrivateKey();
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
