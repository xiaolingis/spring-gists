package com.bz.gists.config;

import com.bz.gists.mybatis.interceptor.AuditingInterceptor;

import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis 配置
 *
 * @author zhongyongbin
 */
@Configuration
public class MyBatisConfiguration implements ConfigurationCustomizer {

    @Bean
    public Interceptor autoSetFieldInterceptor() {
        return new AuditingInterceptor();
    }

    @Override
    public void customize(org.apache.ibatis.session.Configuration configuration) {
        // 进行 MyBatis 配置
    }
}
