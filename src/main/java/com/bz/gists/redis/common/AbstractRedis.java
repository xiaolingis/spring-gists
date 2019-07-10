package com.bz.gists.redis.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Created on 2019/7/2
 *
 * @author zhongyongbin
 */
public abstract class AbstractRedis {

    @Autowired
    protected StringRedisTemplate redisTemplate;
}
