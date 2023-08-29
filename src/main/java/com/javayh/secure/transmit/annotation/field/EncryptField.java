package com.javayh.secure.transmit.annotation.field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author haiji
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptField {

    /**
     * 是否忽略此字段的解密操作
     * <p>
     * 比如 前端已经处理好的数据，已经加密，直接入库即可，二次加密反而会出现异常，无需在解密
     * <p>
     * 默认不开启忽略
     */
    boolean ignore() default false;

}
