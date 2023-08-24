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
public class SecureTransmitDigest {


    private static SecureTransmitTemplate rsaInstance;
    private static SecureTransmitTemplate aesInstance;
    private static SecureTransmitTemplate eccInstance;

    /**
     * 实例化加解密的算法
     *
     * @param secretProperties 安全相关配置 {@link SecretProperties}
     * @return 加解密的标注模板 {@link SecureTransmitTemplate}
     */
    public static SecureTransmitTemplate getInstance(SecretProperties secretProperties) {
        SecretType type = secretProperties.getType();
        if (SecretType.RSA.equals(type)) {
            if (rsaInstance == null) {
                rsaInstance = new RsaCrypto();
            }
            return rsaInstance;
        } else if (SecretType.AES.equals(type)) {
            if (aesInstance == null) {
                aesInstance = new AESCrypto(secretProperties);
            }
            return aesInstance;
        } else if (SecretType.ECC.equals(type)) {
            if (eccInstance == null) {
                eccInstance = new EccCrypto();
            }
            return eccInstance;
        } else {
            log.error("请选择您的加解密算法，参考 EnDeTemplate 的实现类");
            throw new SecurityException("请选择您的加解密算法...");
        }
    }
}
