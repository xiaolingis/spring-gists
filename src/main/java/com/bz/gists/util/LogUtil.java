package com.bz.gists.util;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created at 2019/1/2
 *
 * @author zhongyongbin
 */
public final class LogUtil {

    private static final String SOURCE_KEY = "_source";

    private static final String TIMESTAMP_KEY = "_timestamp";

    public static void dataLog(Logger logger, Object source) {
        Map<String, Object> log = new LinkedHashMap<>();
        log.put(TIMESTAMP_KEY, TemporalUtil.timeToString(LocalDateTime.now(), TemporalUtil.yyyy_MM_dd_HH_mm_ss_SSS));
        log.put(SOURCE_KEY, source);
        logger.info(ObjectMapperUtil.transferToString(log));
    }

    @SafeVarargs
    public static void dataLog(Logger logger, Pair<String, Object>... data) {
        Assert.notEmpty(data, "data can not be empty");

        Map<String, Object> source = new LinkedHashMap<>(data.length);
        Arrays.stream(data).forEach(pair -> source.put(pair.getKey(), pair.getValue()));

        logger.info(ObjectMapperUtil.transferToString(new LinkedHashMap<String, Object>() {{
            this.put(TIMESTAMP_KEY, TemporalUtil.timeToString(LocalDateTime.now(), TemporalUtil.yyyy_MM_dd_HH_mm_ss_SSS));
            this.put(SOURCE_KEY, source);
        }}));
    }
}
