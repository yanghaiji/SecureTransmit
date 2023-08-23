package com.javayh.secure.transmit.configuration.properties;

import lombok.Data;

/**
 * <p>
 * 基础公钥私钥
 * </p>
 *
 * @author hai ji
 * @version 1.0.0
 * @since 2023-08-22
 */
@Data
public class BaseProperties {

    /**
     * 公钥
     */
    private String publicKey;

    /**
     * 私钥
     */
    private String privateKey;

}
