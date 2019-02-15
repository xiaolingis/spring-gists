package com.bz.gists.rest.advice;

import com.bz.gists.common.Constants;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * Created on 2019/2/15
 *
 * @author zhongyongbin
 */
@ControllerAdvice
public class RequestBodyAttributeAdvice extends RequestBodyAdviceAdapter {

    public static final String REQUEST_BODY_ATTRIBUTE_NAME = "key.to.requestBody";

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
        if (StringUtils.isNotBlank(payload)) {
            RequestContextHolder.getRequestAttributes().setAttribute(REQUEST_BODY_ATTRIBUTE_NAME, payload, RequestAttributes.SCOPE_REQUEST);
        } else {
            RequestContextHolder.getRequestAttributes().setAttribute(REQUEST_BODY_ATTRIBUTE_NAME, "", RequestAttributes.SCOPE_REQUEST);
        }

        return super.beforeBodyRead(inputMessage, parameter, targetType, converterType);
    }
}
