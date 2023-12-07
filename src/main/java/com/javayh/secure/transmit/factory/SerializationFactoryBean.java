package com.javayh.secure.transmit.factory;

import com.javayh.secure.transmit.serialize.Serialization;

/**
 * spring 版本的控制实现
 *
 * 原有的实现可以参考 {@link SerializationFactory } {@link SerializationStrategy }
 *
 * @author haiji
 */
public class SerializationFactoryBean {

    private final Serialization serialization;

    public SerializationFactoryBean(Serialization serializationStrategy) {
        this.serialization = serializationStrategy;
    }

    public Serialization getSerialization() {
        return serialization;
    }

    /**
     * 序列化 {@link Serialization#serialize(Object)}
     *
     * @param object 需要序列化的数据
     */
    public String serialize(Object object) {
        return getSerialization().serialize(object);
    }

    /**
     * 反序列化 {@link Serialization#deserialize(String, Class)}
     *
     * @param data  需要反序列的数据
     * @param clazz 指定反序列化的类
     */
    public <T> T deserialize(String data, Class<T> clazz) {
        return getSerialization().deserialize(data, clazz);
    }
}