package com.bz.gists.rest.filter;

import com.bz.gists.rest.advice.RequestBodyAttributeAdvice;
import com.bz.gists.util.LogUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created on 2019/2/28
 *
 * @author zhongyongbin
 */
@WebFilter(filterName = "accessTraceFilter", urlPatterns = "/*")
public class TraceAccessFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TraceAccessFilter.class);

    private static final Logger ACCESS_LOGGER = LoggerFactory.getLogger("ACCESS_LOGGER");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        long requestTime = System.currentTimeMillis();
        chain.doFilter(request, response);
        long responseTime = System.currentTimeMillis();

        trace(request, response, requestTime, responseTime);
    }

    private void trace(ServletRequest request, ServletResponse response, long requestTime, long responseTime) {
        try {
            HttpServletRequest httpRequest = WebUtils.getNativeRequest(request, HttpServletRequest.class);
            HttpServletResponse httpResponse = WebUtils.getNativeResponse(response, HttpServletResponse.class);
            TraceData traceData = new TraceData();
            traceData.requestTime = requestTime;
            traceData.responseTime = responseTime;
            traceData.duration = responseTime - requestTime;
            traceData.uri = httpRequest.getRequestURI();
            traceData.method = httpRequest.getMethod();
            traceData.requestParameter = httpRequest.getParameterMap();
            traceData.requestBody = String.valueOf(httpRequest.getAttribute(RequestBodyAttributeAdvice.REQUEST_BODY_ATTRIBUTE_NAME));
            traceData.statusCode = httpResponse.getStatus();

            LogUtil.dataLog(ACCESS_LOGGER, traceData);
        } catch (Exception e) {
            LOGGER.error("trace access error!", e);
        }
    }

    class TraceData {

        /**
         * 访问时间
         */
        private long requestTime;

        /**
         * 响应时间
         */
        private long responseTime;

        /**
         * 执行过程时间
         */
        private long duration;

        /**
         * 访问的 URI
         */
        private String uri;

        /**
         * 访问方法
         */
        private String method;

        /**
         * 请求参数
         */
        private Map<String, String[]> requestParameter;

        /**
         * 请求体
         */
        private String requestBody;

        /**
         * 响应码
         */
        private int statusCode;

        public long getRequestTime() {
            return requestTime;
        }

        public void setRequestTime(long requestTime) {
            this.requestTime = requestTime;
        }

        public long getResponseTime() {
            return responseTime;
        }

        public void setResponseTime(long responseTime) {
            this.responseTime = responseTime;
        }

        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public Map<String, String[]> getRequestParameter() {
            return requestParameter;
        }

        public void setRequestParameter(Map<String, String[]> requestParameter) {
            this.requestParameter = requestParameter;
        }

        public String getRequestBody() {
            return requestBody;
        }

        public void setRequestBody(String requestBody) {
            this.requestBody = requestBody;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }
    }
}
