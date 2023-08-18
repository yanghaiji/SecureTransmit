package com.javayh.secure.transmit.processor;

import com.javayh.secure.transmit.config.SecretProperties;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author haiji
 */
@Aspect
public class EncryptDecryptAspect {

    private final EncryptDecryptProcessor encryptionProcessor;

    public EncryptDecryptAspect(SecretProperties secretProperties) {
        this.encryptionProcessor = new EncryptDecryptProcessor(secretProperties);
    }

    @Pointcut("@annotation(com.javayh.secure.transmit.annotation.EncryptDecrypt)")
    public void encryptDecryptMethods() {
    }


    /**
     * 前置解密处理
     */
    @Before("encryptDecryptMethods()")
    public void decryptReturnValues(JoinPoint joinPoint) throws Exception {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            encryptionProcessor.decryptFields(arg);
        }
    }


    /**
     * 后置最终的加密处理
     */
    @AfterReturning(pointcut = "encryptDecryptMethods()", returning = "returnValue")
    public Object encryptReturnValues(JoinPoint joinPoint, Object returnValue) throws Exception {
        return encryptionProcessor.encryptFields(returnValue);
    }


}
