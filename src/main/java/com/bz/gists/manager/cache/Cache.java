package com.bz.gists.manager.cache;

import java.time.Duration;

/**
 * Created on 2019/4/26
 *
 * @author zhongyongbin
 */
public interface Cache {
    /**
     * 是否需要定时刷新
     *
     * @return true : 需要 ; false : 不需要
     */
    boolean refreshNeeded();

    /**
     * 缓存刷新
     */
    default void refresh() {
        this.load();
    }

    /**
     * 缓存加载
     */
    void load();

    /**
     * 缓存启动刷新顺序
     *
     * @return 序号，序号越小，越先刷新
     */
    int order();

    /**
     * 缓存有效时间，失效后将根据 {@link Cache#refreshNeeded()} 来决定是否调用刷新方法 {@link Cache#refresh()}
     *
     * @return 有效时间，单位为秒
     */
    default long effectiveTime() {
        return Duration.ofMinutes(5).getSeconds();
    }
}
