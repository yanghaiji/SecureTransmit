package com.javayh.secure.transmit.annotation;

import com.javayh.secure.transmit.bean.SecretType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 支持自定义的算法类型的加解密
 * </p>
 *
 * @author hai ji
 * @version 1.0.0
 * @since 2023-08-23
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SecureTransmit {

    /**
     * 数据加密算法 ,具体赋值参考 {@link SecretType}
     */
    SecretType type();
}
