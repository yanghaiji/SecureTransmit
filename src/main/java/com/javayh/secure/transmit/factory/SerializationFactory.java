package com.javayh.secure.transmit.factory;

import com.javayh.secure.transmit.serialize.Serialization;

import java.util.ServiceLoader;

/**
 * 采用了 spring 托管，减少代码和后期的维护
 * <p>
 * 最新的处理逻辑可以参考{@link SerializationFactoryBean}
 *
 * @author haiji
 */
@Deprecated
public class SerializationFactory {

    private static final ServiceLoader<Serialization> STRATEGIES = ServiceLoader.load(Serialization.class);

    /**
     * 寻找序列化的实现
     */
    public static Serialization getSerializationStrategy() {
        // 默认加载第一个实现类
        return STRATEGIES.iterator().next();
    }
}
