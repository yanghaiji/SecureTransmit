package com.javayh.secure.transmit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 这个名称结合了“安全”和“密码学”，能够清晰地传达加解密操作的安全性。
 * 暂时就先用这个名字
 * </p>
 *
 * @author haiji
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SecureCrypto {
}
