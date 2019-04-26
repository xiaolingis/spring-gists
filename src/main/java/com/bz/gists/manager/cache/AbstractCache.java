package com.bz.gists.manager.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

public abstract class AbstractCache implements Cache {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractCache.class);

    private long lastMaxModifyTime;

    private String cacheName;

    private int order;

    AbstractCache(String cacheName) {
        this(cacheName, Integer.MAX_VALUE);
    }

    AbstractCache(String cacheName, int order) {
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
    }

    @Override
    public int order() {
        return this.order;
    }
}
