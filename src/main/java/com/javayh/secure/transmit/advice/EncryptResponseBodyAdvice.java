package com.javayh.secure.transmit.advice;

import com.alibaba.fastjson.JSON;
import com.javayh.secure.transmit.annotation.Encrypt;
import com.javayh.secure.transmit.configuration.properties.SecretProperties;
import com.javayh.secure.transmit.encrypt.SecureTransmitDigest;
import com.javayh.secure.transmit.factory.LocalKeysInitFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * @author haiji
 */
@Slf4j
@RestControllerAdvice
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private boolean encrypt;

    private final SecretProperties secretProperties;

    private final ThreadLocal<String> publicKey = new ThreadLocal<>();
    private final ThreadLocal<String> privateKey = new ThreadLocal<>();


    public EncryptResponseBodyAdvice(SecretProperties secretProperties) {
        this.secretProperties = secretProperties;
        LocalKeysInitFactory.initLocalKeys(secretProperties, this.publicKey, this.privateKey);
    }

    private static final ThreadLocal<Boolean> ENCRYPT_LOCAL = new ThreadLocal<>();

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        encrypt = Objects.requireNonNull(returnType.getMethod()).isAnnotationPresent(Encrypt.class) && secretProperties.getEnable();
        return encrypt;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        Boolean status = ENCRYPT_LOCAL.get();
        if (null != status && !status) {
            ENCRYPT_LOCAL.remove();
            return body;
        }
        if (encrypt) {
            try {
                // bug fix key is null
                LocalKeysInitFactory.initLocalKeys(secretProperties, this.publicKey, this.privateKey);
                String content = JSON.toJSONString(body);
                if (!StringUtils.hasText(publicKey.get())) {
                    throw new NullPointerException("Please configure secure.transmit.encrypt.publicKey parameter!");
                }
                String result = SecureTransmitDigest.getInstance(secretProperties).encrypt(publicKey.get(), content);
                if (secretProperties.getIsShowLog()) {
                    log.info("Pre-encrypted data：{}，After encryption：{}", content, result);
                }
                return result;
            } catch (Exception e) {
                log.error("Encrypted data exception {}", e.getMessage(), e);
                throw new SecurityException("Encrypted data exception");
            } finally {
                cleanUp();
            }
        }
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
