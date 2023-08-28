package com.javayh.secure.transmit.serialize;


import com.javayh.secure.transmit.exception.SerializationException;

/**
 * <p>
 * 序列化的标准接口，可以自定义实现这个接口，实现不同的序列化方式，方便支持不同的序列化
 * 默认提供了 FastJson的序列化方式 {@link FastJsonSerialization}
 * <p/>
 *
 * @author haiji
 */
public interface Serialization {

    /**
     * @param data 需要序列化的数据
     * @return 最终的返回数据 {@link String}
     */
    String serialize(Object data) throws SerializationException;

    /**
     * @param data  反序列化时的数据
     * @param clazz 反序列化指定的类型
     * @param <T>   自定义的泛型
     * @return 最终的返回数据
     * @throws SerializationException
     */
    <T> T deserialize(String data, Class<T> clazz) throws SerializationException;

}
