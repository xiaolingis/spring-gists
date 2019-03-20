package com.bz.gists.rest.advice;

import com.bz.gists.common.Constants;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * Created on 2019/2/15
 *
 * @author zhongyongbin
 */
@ControllerAdvice
public class RequestBodyAttributeAdvice extends RequestBodyAdviceAdapter {

    public static final String REQUEST_BODY_ATTRIBUTE_NAME = "key.request.body." + RequestBodyAttributeAdvice.class.getCanonicalName();

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        StringBuilder payloadHolder = new StringBuilder();
        try (InputStream in = inputMessage.getBody()) {
            payloadHolder.append(StreamUtils.copyToString(in, Constants.DEFAULT_CHARSET));
        }
        String payload = payloadHolder.toString();
        if (Objects.nonNull(RequestContextHolder.getRequestAttributes())) {
            RequestContextHolder.getRequestAttributes().setAttribute(REQUEST_BODY_ATTRIBUTE_NAME, payload, RequestAttributes.SCOPE_REQUEST);
        }

        return new HttpInputMessage() {
            @Override
            public InputStream getBody() {
                return new ByteArrayInputStream(payload.getBytes(Constants.DEFAULT_CHARSET));
            }

            @Override
            public HttpHeaders getHeaders() {
                return inputMessage.getHeaders();
            }
        };
    }
}
