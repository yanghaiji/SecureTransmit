package com.javayh.secure.transmit.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * aes gcm 配置
 * </p>
 *
 * @author hai ji
 * @version 1.0.0
 * @since 2023-08-17
 */
@Data
@ConfigurationProperties(value = "secure.transmit.gcm")
public class GcmProperties {

    /**
     * 密钥
     */
    private String key;

    /**
     * iv 加盐模型 , 长度为10
     */
    private String iv = "2020020220";

}
