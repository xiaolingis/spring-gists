package com.bz.gists.cache;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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

    @Autowired(required = false)
    private List<ICache> caches;

    @PostConstruct
    public void init() {
        if (!CollectionUtils.isEmpty(caches)) {
            caches.sort(Comparator.comparingInt(ICache::getOrder));
            caches.forEach(cache -> {
                long initDelay = cache.isInitSync() ? cache.effectiveTime() : 0;
                scheduler.scheduleWithFixedDelay(cache::refresh, initDelay, cache.effectiveTime(), TimeUnit.SECONDS);
            });
        }
    }
}
