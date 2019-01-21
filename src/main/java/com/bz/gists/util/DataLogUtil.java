package com.bz.gists.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created at 2019/1/2
 *
 * @author zhongyongbin
 */
public final class DataLogUtil {

    private static final String ERROR_TEMPLATE = "{\"_status\":\"error\",\"_stacktrace\":\"%s\"}";

    private static final String SOURCE_KEY = "_source";

    private static final String TIMESTAMP_KEY = "_timestamp";

    private static final String STACKTRACE_KEY = "_stacktrace";

    private static final DateTimeFormatter DATA_LOG_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private static ObjectMapper objectMapper = SpringUtil.getBean(ObjectMapper.class);

    public static void log(Logger logger, Object obj) {
        log(logger, obj, null);
    }

    public static void log(Logger logger, Object obj, Throwable throwable) {
        try {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put(TIMESTAMP_KEY, DATA_LOG_TIME_FORMATTER.format(LocalDateTime.now()));
            data.put(SOURCE_KEY, obj);
            if (Objects.nonNull(throwable)) {
                data.put(STACKTRACE_KEY, formatException(throwable));
            }
            logger.info(objectMapper.writeValueAsString(data));
        } catch (Exception e) {
            logger.info(String.format(ERROR_TEMPLATE, formatException(e)));
        }
    }

    public static String formatTimestamp(long timestamp) {
        return DATA_LOG_TIME_FORMATTER.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()));
    }

    public static String formatException(Throwable throwable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        throwable.printStackTrace(printWriter);
        return result.toString();
    }
}
