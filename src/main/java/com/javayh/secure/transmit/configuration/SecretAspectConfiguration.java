package com.javayh.secure.transmit.configuration;

import com.javayh.secure.transmit.annotation.SecureTransmit;
import com.javayh.secure.transmit.bean.SecretType;
import com.javayh.secure.transmit.configuration.properties.SecretProperties;
import com.javayh.secure.transmit.processor.SecureTransmitProcessor;
import lombok.SneakyThrows;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author haiji
 */
@Aspect
public class SecretAspectConfiguration {

    private final SecureTransmitProcessor encryptionProcessor;
    private final SecretProperties secretProperties;

    public SecretAspectConfiguration(SecretProperties secretProperties,
                                     SecretProperties secretProperties1) {
        this.encryptionProcessor = new SecureTransmitProcessor(secretProperties);
        this.secretProperties = secretProperties1;
    }

    @Pointcut(
            "@annotation(com.javayh.secure.transmit.annotation.SecureCrypto) || " +
                    "@annotation(com.javayh.secure.transmit.annotation.SecureTransmit)")
    public void secretMethods() {
    }


    /**
     * 前置解密处理
     */
    @Before("secretMethods()")
    public void decryptReturnValues(JoinPoint joinPoint) throws Exception {
        Object[] args = joinPoint.getArgs();
        SecureTransmit annotation = getAnnotation(joinPoint);
        // bug fix type is null
        SecretType type = secretProperties.getType();
        if (Objects.nonNull(annotation)) {
            type = annotation.type();
        }
        for (Object arg : args) {
            encryptionProcessor.decryptFields(arg, type);
        }
    }


    /**
     * 后置最终的加密处理
     */
    @AfterReturning(pointcut = "secretMethods()", returning = "returnValue")
    public Object encryptReturnValues(JoinPoint joinPoint, Object returnValue) throws Exception {
        SecureTransmit annotation = getAnnotation(joinPoint);
        // bug fix type is null , default is secretProperties.getType()
        SecretType type = secretProperties.getType();
        if (Objects.nonNull(annotation)) {
            type = annotation.type();
        }
        return encryptionProcessor.encryptFields(returnValue, type);
    }

    /**
     * 辅助方法：获取方法上的注解
     */
    @SneakyThrows
    private SecureTransmit getAnnotation(JoinPoint joinPoint) {
        // 获取目标方法的签名
        Signature signature = joinPoint.getSignature();
        // 获取目标类的 Class 对象
        Class<?> targetClass = signature.getDeclaringType();
        // 获取目标方法的方法名
        String methodName = signature.getName();
        // 获取目标方法的参数类型列表
        Class<?>[] parameterTypes = ((MethodSignature) signature).getParameterTypes();
        // 使用反射获取方法对象
        Method method = targetClass.getMethod(methodName, parameterTypes);
        // 获取方法上的注解
        return method.getAnnotation(SecureTransmit.class);
    }

}
