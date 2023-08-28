//package com.javayh.secure.transmit.factory;
//
//import com.javayh.secure.transmit.serialize.Serialization;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.util.ServiceLoader;
//
///**
// * @author haiji
// */
//public class SerializationBeanFactory {
//    private final String strategyClassName;
//
//    private Serialization strategy;
//
//    @Autowired
//    public SerializationBeanFactory(@Value("${secure.transmit.serialization}") String strategyClassName) {
//        this.strategyClassName = strategyClassName;
//    }
//
//    @PostConstruct
//    public void initializeStrategy() {
//        this.strategy = loadSerializationStrategy();
//    }
//
//    private Serialization loadSerializationStrategy() {
//        ServiceLoader<Serialization> strategies = ServiceLoader.load(Serialization.class);
//        for (Serialization strategy : strategies) {
//            if (strategy.getClass().getName().equals(strategyClassName)) {
//                return strategy;
//            }
//        }
//        throw new IllegalArgumentException("Invalid serialization strategy class name: " + strategyClassName);
//    }
//
//    public String serialize(Object object) {
//        return strategy.serialize(object);
//    }
//
//    public <T> T deserialize(String data, Class<T> clazz) {
//        return strategy.deserialize(data, clazz);
//    }
//}
