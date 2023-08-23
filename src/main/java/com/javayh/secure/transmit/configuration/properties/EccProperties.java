package com.javayh.secure.transmit.configuration.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(value = "secure.transmit.ecc")
public class EccProperties extends BaseProperties {

}
