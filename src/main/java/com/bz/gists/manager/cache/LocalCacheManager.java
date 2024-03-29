package com.bz.gists.manager.cache;

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
public final class LocalCacheManager {

    private final ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("cache-refresh-scheduler-%d").build());

    @Autowired(required = false)
    private List<ILocalCache> caches;

    @PostConstruct
    public void init() {
        if (!CollectionUtils.isEmpty(caches)) {
            caches.sort(Comparator.comparingInt(ILocalCache::getOrder));
            caches.forEach(cache -> {
                long initDelay;
                if (cache.isInitSync()) {
                    cache.refresh();
                    initDelay = cache.effectiveTime();
                } else {
                    initDelay = 0;
                }
                scheduler.scheduleWithFixedDelay(cache::refresh, initDelay, cache.effectiveTime(), TimeUnit.SECONDS);
            });
        }
    }
}
