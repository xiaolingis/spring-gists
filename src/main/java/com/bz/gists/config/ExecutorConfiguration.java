package com.bz.gists.config;

import com.alibaba.ttl.threadpool.TtlExecutors;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 统一线程池的配置
 */
@Configuration
public class ExecutorConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorConfiguration.class);

    @Bean("serviceExecutor")
    public Executor serviceExecutor() {
        return TtlExecutors.getTtlExecutor(new ThreadPoolExecutor(ServcieConstant.MAX_THREAD_COUNT,
                ServcieConstant.MAX_THREAD_COUNT, 0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new BasicThreadFactory.Builder()
                        .namingPattern(ServcieConstant.NAMING_PATTERN)
                        .uncaughtExceptionHandler((t, e) -> LOGGER.error("handle service fail!", e))
                        .build(),
                new ThreadPoolExecutor.AbortPolicy()));
    }

    private interface ServcieConstant {
        int MAX_THREAD_COUNT = 5;

        String NAMING_PATTERN = "service-thread-pool-%d";
    }
}
