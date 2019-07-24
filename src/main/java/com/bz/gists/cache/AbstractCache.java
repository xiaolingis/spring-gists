package com.bz.gists.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.util.StopWatch;

import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractCache implements ICache {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractCache.class);

    private volatile long lastMaxModifyTime = Long.MIN_VALUE;

    private String cacheName;

    private int order;

    private final AtomicLong version = new AtomicLong(0);

    public AbstractCache(String cacheName) {
        this(cacheName, Ordered.LOWEST_PRECEDENCE);
    }

    public AbstractCache(String cacheName, int order) {
        this.cacheName = cacheName;
        this.order = order;
    }

    /**
     * 更新数据的最后更改时间，用于判断是否需要刷新缓存
     *
     * @param modifyTimestamp 更改时间
     * @return true: 最后更改时间发生变化 ; false: 最后更改时间没有变化
     */
    protected final boolean updateLastModifyTime(long modifyTimestamp) {
        if (modifyTimestamp > lastMaxModifyTime) {
            lastMaxModifyTime = modifyTimestamp;
            return true;
        } else {
            return false;
        }
    }

    public final long getLastMaxModifyTime() {
        return lastMaxModifyTime;
    }

    @Override
    public void refresh() {
        try {
            if (refreshNeeded()) {
                StopWatch watch = new StopWatch();
                LOGGER.info("start to refresh cache {}, version {}", cacheName, version.get());
                watch.start();
                load();
                version.getAndIncrement();
                watch.stop();
                LOGGER.info("end to refresh cache {} , eclipsed time {} s, version {}", cacheName, watch.getTotalTimeSeconds(), version.get());
                return;
            }
            LOGGER.info("no need to refresh cache {}", cacheName);
        } catch (Exception e) {
            LOGGER.error("occur error while refresh cache {}. stacktrace:", cacheName, e);
        }
    }

    @Override
    public long version() {
        return version.get();
    }

    @Override
    public int getOrder() {
        return this.order;
    }
}
