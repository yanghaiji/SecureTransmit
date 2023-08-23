package com.javayh.secure.transmit.configuration;

import com.javayh.secure.transmit.configuration.properties.SecretProperties;
import com.javayh.secure.transmit.processor.EncryptDecryptProcessor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author haiji
 */
@Aspect
public class SecretAspectConfiguration {

    private final EncryptDecryptProcessor encryptionProcessor;

    public SecretAspectConfiguration(SecretProperties secretProperties) {
        this.encryptionProcessor = new EncryptDecryptProcessor(secretProperties);
    }

    @Pointcut("@annotation(com.javayh.secure.transmit.annotation.SecureCrypto)")
    public void secretMethods() {
    }


    /**
     * 前置解密处理
     */
    @Before("secretMethods()")
    public void decryptReturnValues(JoinPoint joinPoint) throws Exception {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            encryptionProcessor.decryptFields(arg);
        }
    }


    /**
     * 后置最终的加密处理
     */
    @AfterReturning(pointcut = "secretMethods()", returning = "returnValue")
    public Object encryptReturnValues(Object returnValue) throws Exception {
        return encryptionProcessor.encryptFields(returnValue);
    }


}
