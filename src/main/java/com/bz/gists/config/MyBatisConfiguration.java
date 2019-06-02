package com.bz.gists.config;

import com.github.pagehelper.PageInterceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tk.mybatis.spring.annotation.MapperScan;

/**
 * Created on 2019/6/2
 *
 * @author zhongyongbin
 */
@MapperScan("com.bz.gists")
@Configuration
public class MyBatisConfiguration {

    @Bean
    public PageInterceptor pageInterceptor() {
        return new PageInterceptor();
    }
}
