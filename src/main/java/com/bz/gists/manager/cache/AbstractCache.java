package com.bz.gists.manager.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.util.StopWatch;

public abstract class AbstractCache implements ICache {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractCache.class);

    private long lastMaxModifyTime;

    private String cacheName;

    private int order;

    public AbstractCache(String cacheName) {
        this(cacheName, Ordered.LOWEST_PRECEDENCE);
    }

    public AbstractCache(String cacheName, int order) {
        this.cacheName = cacheName;
        this.order = order;
    }

    protected final boolean updateMaxUpdateTime(long modifyTimestamp) {
        if (modifyTimestamp > lastMaxModifyTime) {
            lastMaxModifyTime = modifyTimestamp;
            return true;
        }
        return false;
    }

    public final long getLastMaxModifyTime() {
        return lastMaxModifyTime;
    }

    @Override
    public void refresh() {
        try {
            if (refreshNeeded()) {
                StopWatch watch = new StopWatch();
                LOGGER.info("start to refresh cache {}", cacheName);
                watch.start();
                load();
                watch.stop();
                LOGGER.info("end to refresh cache {} , eclipsed time {} s", cacheName, watch.getTotalTimeSeconds());
                return;
            }
            LOGGER.info("no need to refresh cache {}", cacheName);
        } catch (Exception e) {
            LOGGER.error("occur error while refresh cache {}. stacktrace:", cacheName, e);
        }
    }

    @Override
    public int getOrder() {
        return this.order;
    }
}
