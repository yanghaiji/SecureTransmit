package com.javayh.secure.transmit.processor;

import com.javayh.secure.transmit.annotation.DecryptField;
import com.javayh.secure.transmit.annotation.EncryptField;
import com.javayh.secure.transmit.bean.SecretType;
import com.javayh.secure.transmit.configuration.properties.AesProperties;
import com.javayh.secure.transmit.configuration.properties.EccProperties;
import com.javayh.secure.transmit.configuration.properties.RsaProperties;
import com.javayh.secure.transmit.configuration.properties.SecretProperties;
import com.javayh.secure.transmit.encrypt.SecureTransmitDigest;
import com.javayh.secure.transmit.exception.InvalidAlgorithmException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

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
public class SecureTransmitProcessor {

    /**
     * 密钥和机密算法的配置
     */
    private final ThreadLocal<String> publicKey = new ThreadLocal<>();
    private final ThreadLocal<String> privateKey = new ThreadLocal<>();
    ;
    private final Boolean isShowLog;
    private final SecretProperties secretProperties;

    public SecureTransmitProcessor(SecretProperties secretProperties) {
        this.isShowLog = secretProperties.getIsShowLog();
        this.secretProperties = secretProperties;
        SecretType type = secretProperties.getType();
        initializeKeys(type);
    }


    /**
     * 对字段值进行加密处理
     *
     * @param object {@link Object} 需要处理的对象
     * @return {@link Object} 加密后的数据
     */
    public <T> T encryptFields(T object, SecretType type) throws Exception {
        try {
            Field[] fields = object.getClass().getDeclaredFields();
            SecretProperties properties = new SecretProperties();
            BeanUtils.copyProperties(secretProperties, properties);
            if (Objects.nonNull(type)) {
                properties.setType(type);
                initializeKeys(type);
            }
            for (Field field : fields) {
                if (field.isAnnotationPresent(EncryptField.class)) {
                    field.setAccessible(true);
                    Object fieldValue = field.get(object);
                    if (Objects.nonNull(fieldValue)) {
                        field.set(object, SecureTransmitDigest.getInstance(properties).encrypt(publicKey.get(), fieldValue.toString()));
                    }
                    if (isShowLog) {
                        log.info("encrypt before fieldName = {},fieldValue = {} ; after fieldValue = {}", field, fieldValue, field.get(object));
                    }
                }
            }
        } catch (Exception e) {
            log.error("EncryptDecryptProcessor.encryptFields() 异常 {}", e.getMessage());
        } finally {
            cleanUp();
        }
        return object;
    }

    /**
     * 对值对象的值进行解密
     *
     * @param object {@link Object} 需要解密的对象
     */
    public <T> void decryptFields(T object, SecretType type) {
        try {
            Field[] fields = object.getClass().getDeclaredFields();
            SecretProperties properties = new SecretProperties();
            BeanUtils.copyProperties(secretProperties, properties);
            if (Objects.nonNull(type)) {
                properties.setType(type);
                initializeKeys(type);
            }
            for (Field field : fields) {
                if (field.isAnnotationPresent(DecryptField.class)) {
                    field.setAccessible(true);
                    Object fieldValue = field.get(object);
                    if (Objects.nonNull(fieldValue)) {
                        field.set(object, SecureTransmitDigest.getInstance(properties).decrypt(privateKey.get(), fieldValue.toString()));
                    }
                    if (isShowLog) {
                        log.info("decrypt before fieldName = {},fieldValue = {} ; after fieldValue = {}", field, fieldValue, field.get(object));
                    }
                }
            }
        } catch (Exception e) {
            log.error("EncryptDecryptProcessor.decryptFields() 异常 {}", e.getMessage());
        } finally {
            cleanUp();
        }
    }

    private void initializeKeys(SecretType type) {
        RsaProperties rsa = secretProperties.getRsa();
        AesProperties aes = secretProperties.getAes();
        EccProperties ecc = secretProperties.getEcc();
        switch (type) {
            case RSA:
                this.publicKey.set(rsa.getPublicKey());
                this.privateKey.set(rsa.getPrivateKey());
                break;
            case AES:
                String key = aes.getKey();
                this.publicKey.set(key);
                this.privateKey.set(key);
                break;
            case ECC:
                this.publicKey.set(ecc.getPublicKey());
                this.privateKey.set(ecc.getPrivateKey());
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

    /**
     * 清除当前的 缓存key
     */
    private void cleanUp() {
        privateKey.remove();
        publicKey.remove();
    }

}
