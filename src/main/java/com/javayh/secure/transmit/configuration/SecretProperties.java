package com.javayh.secure.transmit.configuration;

import com.javayh.secure.transmit.bean.SecretType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * 安全信息配置
 * </p>
 *
 * @author hai ji
 * @version 1.0.0
 * @since 2023-08-17
 */
@Data
@ConfigurationProperties(value = "secure.transmit")
public class SecretProperties {

    /**
     * 加密的类型，默认 RAS
     */
    private SecretType type = SecretType.RSA;

    /**
     * 是否开启加密，默认开启
     */
    private Boolean enable = true;

    /**
     * 是否显示 logger , 开发和测试时可以开启，生产建议关闭
     */
    private Boolean isShowLog = false;

    /**
     * aes 配置
     */
    private AesProperties aes;

    /**
     * rsa 配置
     */
    private RsaProperties rsa;
}
