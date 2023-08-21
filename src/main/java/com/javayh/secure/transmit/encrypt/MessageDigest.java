package com.javayh.secure.transmit.encrypt;

import com.javayh.secure.transmit.bean.SecretType;
import com.javayh.secure.transmit.configuration.SecretProperties;
import com.javayh.secure.transmit.encrypt.aes.AESCrypto;
import com.javayh.secure.transmit.encrypt.rsa.RsaCrypto;

/**
 * <p>
 *
 * </p>
 *
 * @author hai ji
 * @version 1.0.0
 * @since 2023-08-18
 */
public class MessageDigest {


    public static EnDeTemplate getInstance(SecretProperties secretProperties) {
        SecretType type = secretProperties.getType();
        if (SecretType.RSA.equals(type)) {
            return new RsaCrypto();
        } else if (SecretType.AES.equals(type)) {
            return new AESCrypto(secretProperties);
        } else {
            throw new SecurityException("请选择您的加解密算法...");
        }
    }
}
