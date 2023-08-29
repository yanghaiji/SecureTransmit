package com.javayh.secure.transmit.advice;

import com.javayh.secure.transmit.annotation.json.Decrypt;
import com.javayh.secure.transmit.configuration.properties.SecretProperties;
import com.javayh.secure.transmit.exception.SecretException;
import com.javayh.secure.transmit.factory.LocalKeysInitFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * @author haiji
 */
@Slf4j
@ControllerAdvice
public class DecryptRequestBodyAdvice implements RequestBodyAdvice {

    /**
     * 是否开启加密 {@link SecretProperties#getEnable()}
     */
    private boolean encrypt;

    /**
     * 安全信息配置 {@link SecretProperties}
     */
    private final SecretProperties secretProperties;

    /**
     * 密钥和机密算法的配置
     */
    private final ThreadLocal<String> publicKey = new ThreadLocal<>();
    private final ThreadLocal<String> privateKey = new ThreadLocal<>();

    public DecryptRequestBodyAdvice(SecretProperties secretProperties) {
        this.secretProperties = secretProperties;
        LocalKeysInitFactory.initLocalKeys(secretProperties, this.publicKey, this.privateKey);
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (Objects.requireNonNull(methodParameter.getMethod()).isAnnotationPresent(Decrypt.class) && secretProperties.getEnable()) {
            encrypt = true;
        }
        return encrypt;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
                                  Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType) {
        if (encrypt) {
            try {
                LocalKeysInitFactory.initLocalKeys(secretProperties, this.publicKey, this.privateKey);
                return new DecryptHttpInputMessage(inputMessage, privateKey.get(), secretProperties, secretProperties.getIsShowLog());
            } catch (Exception e) {
                log.error("Decryption failed {}", e.getMessage(), e);
                throw new SecretException("DecryptRequestBodyAdvice.class error ");
            } finally {
                cleanUp();
            }
        }
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }


    /**
     * 清除当前的 缓存key
     */
    private void cleanUp() {
        privateKey.remove();
        publicKey.remove();
    }

}
