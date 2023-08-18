package com.javayh.secure.transmit.processor;

import com.javayh.secure.transmit.annotation.DecryptField;
import com.javayh.secure.transmit.annotation.EncryptField;
import com.javayh.secure.transmit.bean.SecretType;
import com.javayh.secure.transmit.config.SecretProperties;
import com.javayh.secure.transmit.encrypt.rsa.RsaTools;
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
    private final String publicKey;
    private final String privateKey;
    private final SecretType secretType;
    private final Boolean isShowLog;

    public EncryptDecryptProcessor(SecretProperties secretProperties) {
        this.publicKey = secretProperties.getPublicKey();
        this.privateKey = secretProperties.getPrivateKey();
        this.secretType = secretProperties.getType();
        this.isShowLog = secretProperties.getIsShowLog();
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
                if (Objects.nonNull(fieldValue) && secretType.equals(SecretType.RSA)) {
                    field.set(object, RsaTools.encrypt(publicKey, fieldValue.toString()));
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
                if (Objects.nonNull(fieldValue) && secretType.equals(SecretType.RSA)) {
                    field.set(object, RsaTools.decrypt(privateKey, fieldValue.toString()));
                }
                if (isShowLog) {
                    log.info("decrypt before fieldName = {},fieldValue = {} ; after fieldValue = {}", field, fieldValue, field.get(object));
                }
            }
        }
    }

}
