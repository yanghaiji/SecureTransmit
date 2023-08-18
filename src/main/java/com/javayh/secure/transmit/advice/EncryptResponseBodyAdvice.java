package com.javayh.secure.transmit.advice;

import com.alibaba.fastjson.JSON;
import com.javayh.secure.transmit.annotation.Encrypt;
import com.javayh.secure.transmit.bean.SecretType;
import com.javayh.secure.transmit.config.SecretProperties;
import com.javayh.secure.transmit.encrypt.base.Base64Util;
import com.javayh.secure.transmit.encrypt.rsa.RsaTools;
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

    public EncryptResponseBodyAdvice(SecretProperties secretProperties) {
        this.secretProperties = secretProperties;
    }

    private static ThreadLocal<Boolean> encryptLocal = new ThreadLocal<>();

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        encrypt = Objects.requireNonNull(returnType.getMethod()).isAnnotationPresent(Encrypt.class) && secretProperties.getEnable();
        return encrypt;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        Boolean status = encryptLocal.get();
        if (null != status && !status) {
            encryptLocal.remove();
            return body;
        }
        if (encrypt && secretProperties.getType().equals(SecretType.RSA)) {
            String publicKey = secretProperties.getPublicKey();
            try {
                String content = JSON.toJSONString(body);
                if (!StringUtils.hasText(publicKey)) {
                    throw new NullPointerException("Please configure secure.transmit.encrypt.publicKey parameter!");
                }
                String result = RsaTools.encrypt(publicKey, content);
                if (secretProperties.getIsShowLog()) {
                    log.info("Pre-encrypted data：{}，After encryption：{}", content, result);
                }
                return result;
            } catch (Exception e) {
                log.error("Encrypted data exception", e);
                throw new SecurityException("Encrypted data exception");
            }
        }
        return body;
    }
}
