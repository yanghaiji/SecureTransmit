//package com.javayh.secure.transmit.factory;
//
//import com.javayh.secure.transmit.annotation.SecureCrypto;
//import com.javayh.secure.transmit.bean.SecretType;
//import com.javayh.secure.transmit.configuration.properties.SecretProperties;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.config.BeanPostProcessor;
//import org.springframework.context.ApplicationContext;
//
//import java.lang.reflect.Method;
//
///**
// * @author haiji
// */
//@Slf4j
//public class SecureCryptoProcessor implements BeanPostProcessor {
//
//    @Autowired
//    private ApplicationContext applicationContext;
//
//    @Override
//    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        if (bean instanceof SecretProperties) {
//            // 获取所有方法
//            Method[] methods = bean.getClass().getMethods();
//            // 遍历方法，找到标注了 @SecureCrypto 的方法
//            for (Method method : methods) {
//                SecretType defaultValue = applicationContext.getBean(SecretProperties.class).getType();
//                SecureCrypto secureCryptoAnnotation = method.getAnnotation(SecureCrypto.class);
//                if (secureCryptoAnnotation != null) {
//                    SecretType annotationValue = secureCryptoAnnotation.type();
//                    // 如果方法的 @SecureCrypto 注解的值为默认值，则用 MyAppProperties 中的默认值替换
//                    if (annotationValue.equals(SecretType.RSA)) {
//                        try {
//                            method.invoke(bean, defaultValue);
//                        } catch (Exception e) {
//                            log.error("SecureCryptoProcessor 初始化 失败 {}", e.getMessage());
//                        }
//                    }
//                }
//            }
//        }
//        return bean;
//    }
//
//    @Override
//    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        return bean;
//    }
//}
