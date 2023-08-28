package com.javayh.secure.transmit.serialize;

import com.alibaba.fastjson.JSON;
import com.javayh.secure.transmit.exception.SerializationException;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 默认序列化方式
 * </p>
 *
 * @author haiji
 */
@Slf4j
public class FastJsonSerialization implements Serialization {

    @Override
    public String serialize(Object object) throws SerializationException {
        try {
            return JSON.toJSONString(object);
        } catch (Exception e) {
            log.error("FastJsonSerialization serialize 错误 {}", e.getMessage(), e);
            throw new SerializationException("Failed to serialize object using FastJSON");
        }
    }

    @Override
    public <T> T deserialize(String data, Class<T> clazz) throws SerializationException {
        try {
            return JSON.parseObject(data, clazz);
        } catch (Exception e) {
            log.error("FastJsonSerialization deserialize 错误 {}", e.getMessage(), e);
            throw new SerializationException("Failed to deserialize data using FastJSON");
        }
    }
}
