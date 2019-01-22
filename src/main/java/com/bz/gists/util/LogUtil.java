package com.bz.gists.util;

import org.slf4j.Logger;

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

    public static void dataLog(Logger logger, Object obj) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put(TIMESTAMP_KEY, LOG_TIME_FORMATTER.format(LocalDateTime.now()));
        data.put(SOURCE_KEY, obj);
        logger.info(JsonUtil.toJson(data));
    }

    public static String formatTimestamp(long timestamp) {
        return LOG_TIME_FORMATTER.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()));
    }
}
