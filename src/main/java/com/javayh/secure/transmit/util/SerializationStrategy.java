package com.javayh.secure.transmit.util;

import com.javayh.secure.transmit.factory.SerializationFactory;
import com.javayh.secure.transmit.serialize.Serialization;

/**
 * 根据不同的实现，选择不同的序列化方式
 *
 * @author haiji
 */
public class SerializationStrategy {

    private final Serialization strategy;

    public SerializationStrategy() {
        this.strategy = SerializationFactory.getSerializationStrategy();
    }

    /**
     * 序列化 {@link Serialization#serialize(Object)}
     *
     * @param object 需要序列化的数据
     */
    public String serialize(Object object) {
        return strategy.serialize(object);
    }

    /**
     * 反序列化 {@link Serialization#deserialize(String, Class)}
     *
     * @param data  需要反序列的数据
     * @param clazz 指定反序列化的类
     */
    public <T> T deserialize(String data, Class<T> clazz) {
        return strategy.deserialize(data, clazz);
    }
}
