package com.javayh.secure.transmit.configuration.properties;

import com.javayh.secure.transmit.configuration.properties.BaseProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * rsa 加密配置
 * </p>
 *
 * @author hai ji
 * @version 1.0.0
 * @since 2023-08-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(value = "secure.transmit.rsa")
public class RsaProperties extends BaseProperties {


}
