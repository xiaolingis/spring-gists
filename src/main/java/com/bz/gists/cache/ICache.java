package com.bz.gists.cache;

import org.springframework.core.Ordered;

import java.time.Duration;

/**
 * Created on 2019/4/26
 *
 * 本地缓存，尽量只实现于提供数据的功能。
 *
 * @author zhongyongbin
 */
public interface ICache extends Ordered {
    /**
     * 是否需要定时刷新
     *
     * @return true : 需要 ; false : 不需要
     */
    default boolean refreshNeeded() {
        return true;
    }

    /**
     * 缓存刷新
     */
    void refresh();

    /**
     * 缓存加载
     */
    void load() throws Exception;

    /**
     * 缓存有效时间，失效后将根据 {@link ICache#refreshNeeded()} 来决定是否调用刷新方法 {@link ICache#refresh()}
     *
     * @return 有效时间，单位为秒
     */
    default long effectiveTime() {
        return Duration.ofMinutes(5).getSeconds();
    }

    /**
     * 缓存版本
     */
    long version();

    /**
     * 初始化时是否同步
     */
    default boolean isInitSync() {
        return true;
    }
}
