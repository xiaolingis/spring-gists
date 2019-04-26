package com.bz.gists.manager.cache;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

/**
 * Created on 2019/4/26
 *
 * @author zhongyongbin
 */
@Component
public final class CacheFactory {

    private final ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("cache-refresh-scheduler-%d").build());

    @Autowired
    private List<Cache> caches;

    @PostConstruct
    public void init() {
        caches.sort(Comparator.comparingInt(Cache::order));
        caches.forEach(cache -> scheduler.scheduleWithFixedDelay(cache::refresh, 0, cache.effectiveTime(), TimeUnit.SECONDS));
    }
}
