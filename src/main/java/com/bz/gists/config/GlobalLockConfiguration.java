package com.bz.gists.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;

/**
 * Created on 2019/11/11
 *
 * @author zhongyongbin
 */
@Configuration
public class GlobalLockConfiguration {

    private static final String REDLOCK_PREFIX = "redlock";

    @Bean
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory connectionFactory) {
        return new RedisLockRegistry(connectionFactory, REDLOCK_PREFIX);
    }
}
