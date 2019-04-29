package com.bz.gists.manager.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Created on 2019/4/29
 *
 * @author zhongyongbin
 */
public abstract class AbstractRedisManager {

    @Autowired
    protected StringRedisTemplate stringRedisTemplate;
}
