package com.bz.gists.manager.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Created on 2019/7/2
 *
 * @author zhongyongbin
 */
public abstract class AbstractRedisManager {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractRedisManager.class);

    @Autowired
    protected StringRedisTemplate redisTemplate;
}
