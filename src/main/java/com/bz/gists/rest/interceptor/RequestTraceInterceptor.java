package com.bz.gists.rest.interceptor;

import com.bz.gists.rest.advice.RequestBodyAttributeAdvice;
import com.bz.gists.util.LogUtil;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created on 2019/2/25
 *
 * 记录请求日志
 *
 * @author zhongyongbin
 */
public class RequestTraceInterceptor extends HandlerInterceptorAdapter {

    private static final Logger ACCESS_LOGGER = LoggerFactory.getLogger("ACCESS_LOGGER");

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        LogUtil.dataLog(ACCESS_LOGGER,
                Pair.of("uri", request.getRequestURI()),
                Pair.of("parameter", request.getParameterMap()),
                Pair.of("request_body", request.getAttribute(RequestBodyAttributeAdvice.REQUEST_BODY_ATTRIBUTE_NAME)));
    }
}
