package com.bz.gists.util;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.springframework.util.Assert;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

    private static final DateTimeFormatter LOG_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static void dataLog(Logger logger, Object source) {
        Map<String, Object> log = new LinkedHashMap<>();
        log.put(TIMESTAMP_KEY, LOG_TIME_FORMATTER.format(LocalDateTime.now()));
        log.put(SOURCE_KEY, source);
        logger.info(ObjectMapperUtil.toJson(log));
    }

    @SafeVarargs
    public static void dataLog(Logger logger, Pair<String, Object>... data) {
        Assert.notEmpty(data, "data can not be empty");

        Map<String, Object> source = new LinkedHashMap<>(data.length);
        Arrays.stream(data).forEach(pair -> source.put(pair.getKey(), pair.getValue()));

        logger.info(ObjectMapperUtil.toJson(new LinkedHashMap<String, Object>() {{
            this.put(TIMESTAMP_KEY, LOG_TIME_FORMATTER.format(LocalDateTime.now()));
            this.put(SOURCE_KEY, source);
        }}));
    }

    public static String formatTimestamp(long timestamp) {
        return LOG_TIME_FORMATTER.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()));
    }
}
