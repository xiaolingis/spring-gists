package com.bz.gists.config;

import com.github.pagehelper.PageInterceptor;

import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Configuration;

/**
 * Created on 2019/6/2
 *
 * @author zhongyongbin
 */
@Configuration
public class MyBatisConfiguration implements ConfigurationCustomizer {
    @Override
    public void customize(org.apache.ibatis.session.Configuration configuration) {
        configuration.addInterceptor(new PageInterceptor());
    }
}
