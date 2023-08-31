package com.javayh.secure.transmit.encrypt;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 *
 * </p>
 *
 * @author hai ji
 * @version 1.0.0
 * @since 2023-08-18
 */
public interface SecureTransmitTemplate {

    /**
     * 数据加密
     *
     * @param key  加密的key
     * @param data 加密的数据
     * @return {@link String} 最终的结果
     */
    String encrypt(String key, String data);

    /**
     * 数据解密
     *
     * @param key  解密的key
     * @param data 待解密的数据
     * @return {@link String} 最终的结果
     */
    String decrypt(String key, String data);

}
