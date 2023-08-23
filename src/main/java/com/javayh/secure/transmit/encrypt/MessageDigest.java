package com.javayh.secure.transmit.encrypt;

import com.javayh.secure.transmit.bean.SecretType;
import com.javayh.secure.transmit.configuration.properties.SecretProperties;
import com.javayh.secure.transmit.encrypt.aes.AESCrypto;
import com.javayh.secure.transmit.encrypt.ecc.EccCrypto;
import com.javayh.secure.transmit.encrypt.rsa.RsaCrypto;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *
 * </p>
 *
 * @author hai ji
 * @version 1.0.0
 * @since 2023-08-18
 */
@Slf4j
public class MessageDigest {


    public static EnDeTemplate getInstance(SecretProperties secretProperties) {
        SecretType type = secretProperties.getType();
        if (SecretType.RSA.equals(type)) {
            return new RsaCrypto();
        } else if (SecretType.AES.equals(type)) {
            return new AESCrypto(secretProperties);
        } else if (SecretType.ECC.equals(type)) {
            return new EccCrypto();
        } else {
            log.error("请选择您的加解密算法，参考 EnDeTemplate 的实现类");
            throw new SecurityException("请选择您的加解密算法...");
        }
    }
}
