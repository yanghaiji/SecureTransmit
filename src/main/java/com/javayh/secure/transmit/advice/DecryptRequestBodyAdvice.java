package com.javayh.secure.transmit.advice;

import com.javayh.secure.transmit.annotation.Decrypt;
import com.javayh.secure.transmit.config.SecretProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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


    private boolean encrypt;

    @Autowired
    private SecretProperties secretKeyConfig;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (Objects.requireNonNull(methodParameter.getMethod()).isAnnotationPresent(Decrypt.class) && secretKeyConfig.getEnable()) {
            encrypt = true;
        }
        return encrypt;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType) {
        if (encrypt) {
            try {
                return new DecryptHttpInputMessage(inputMessage, secretKeyConfig.getPrivateKey(), "utf-8",secretKeyConfig.getIsShowLog());
            } catch (Exception e) {
                log.error("Decryption failed", e);
            }
        }
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }


}
