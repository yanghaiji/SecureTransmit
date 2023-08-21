package com.javayh.secure.transmit.encrypt;

/**
 * <p>
 *
 * </p>
 *
 * @author hai ji
 * @version 1.0.0
 * @since 2023-08-18
 */
public interface EnDeTemplate {

    /**
     * 数据加密
     *
     * @param key  加密的key
     * @param data 加密的数据
     * @return {@link String} 最终的结果
     */
    String encrypt(String key, String data) throws Exception;

    /**
     * 数据解密
     *
     * @param key  解密的key
     * @param data 待解密的数据
     * @return {@link String} 最终的结果
     */
    String decrypt(String key, String data);

}
