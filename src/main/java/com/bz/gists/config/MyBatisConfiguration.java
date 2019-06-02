package com.bz.gists.config;

import com.github.pagehelper.PageInterceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created on 2019/6/2
 *
 * @author zhongyongbin
 */
@Configuration
public class MyBatisConfiguration {

    @Bean
    public PageInterceptor pageInterceptor() {
        return new PageInterceptor();
    }
}
