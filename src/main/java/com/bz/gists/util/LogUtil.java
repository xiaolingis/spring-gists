package com.bz.gists.util;

import org.slf4j.Logger;
import org.springframework.util.Assert;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

    public static void dataLog(Logger logger, Object data) {
        Map<String, Object> log = new LinkedHashMap<>();
        log.put(TIMESTAMP_KEY, LOG_TIME_FORMATTER.format(LocalDateTime.now()));
        log.put(SOURCE_KEY, data);
        logger.info(JsonUtil.toJson(log));
    }

    public static void dataLog(Logger logger, String[] keys, Object[] values) {
        Assert.notEmpty(keys, "keys can not be empty");
        Assert.notEmpty(values, "values can not be null");
        if (keys.length != values.length) {
            throw new IllegalArgumentException("the key length should be the same as the value length");
        }

        int dataLength = keys.length;

        Map<String, Object> data = new LinkedHashMap<>(dataLength);

        for (int i = 0; i < dataLength; i++) {
            data.put(keys[i], values[i]);
        }

        logger.info(JsonUtil.toJson(new LinkedHashMap<String, Object>() {{
            this.put(TIMESTAMP_KEY, LOG_TIME_FORMATTER.format(LocalDateTime.now()));
            this.put(SOURCE_KEY, data);
        }}));
    }

    public static String formatTimestamp(long timestamp) {
        return LOG_TIME_FORMATTER.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()));
    }
}
