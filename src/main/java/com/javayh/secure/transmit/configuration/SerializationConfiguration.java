package com.javayh.secure.transmit.configuration;

import com.javayh.secure.transmit.configuration.properties.SecretProperties;
import com.javayh.secure.transmit.factory.SerializationFactoryBean;
import com.javayh.secure.transmit.serialize.Serialization;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author haiji
 */
@Configuration
public class SerializationConfiguration {


    private final SecretProperties secretProperties;

    public SerializationConfiguration(SecretProperties secretProperties) {
        this.secretProperties = secretProperties;
    }

    @Bean
    public Serialization serialization() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        Class<?> implementationClass = Class.forName(secretProperties.getSerialization());
        return (Serialization) implementationClass.newInstance();
    }

    @Bean
    public SerializationFactoryBean serializationFactory(Serialization serialization) {
        return new SerializationFactoryBean(serialization);
    }


}
