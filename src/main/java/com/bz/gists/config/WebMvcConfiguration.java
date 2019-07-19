package com.bz.gists.config;

import com.bz.gists.web.filter.CachingRequestFilter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * WEB MVC 相关配置
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Bean
    public FilterRegistrationBean<CachingRequestFilter> filterFilterRegistrationBean(CachingRequestFilter filter) {
        FilterRegistrationBean<CachingRequestFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>(filter);
        filterFilterRegistrationBean.addUrlPatterns("/*");
        filterFilterRegistrationBean.setOrder(Ordered.LOWEST_PRECEDENCE - 1);
        return filterFilterRegistrationBean;
    }
}
