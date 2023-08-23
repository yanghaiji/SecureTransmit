package com.javayh.secure.transmit.configuration;


import com.javayh.secure.transmit.advice.DecryptRequestBodyAdvice;
import com.javayh.secure.transmit.advice.EncryptResponseBodyAdvice;
import com.javayh.secure.transmit.configuration.properties.SecretProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author haiji
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Configuration
@Import({SecretProperties.class,
        EncryptResponseBodyAdvice.class,
        DecryptRequestBodyAdvice.class,
        SecretAspectConfiguration.class})
public @interface SecureTransmitAutoConfiguration {

}
