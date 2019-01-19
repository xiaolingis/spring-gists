package com.bz.gists.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created at 2019/1/2
 *
 * @author zhongyongbin
 */
public final class DataLogUtil {

    private static final String ERROR_TEMPLATE = "{\"_status\":\"error\",\"_stacktrace\":\"%s\"}";

    private static final String SOURCE_KEY = "_source";

    private static final String TIMESTAMP_KEY = "_timestamp";

    private static ObjectMapper objectMapper = SpringUtil.getBean(ObjectMapper.class);

    public static void log(Logger logger, Object obj) {
        try {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put(TIMESTAMP_KEY, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(LocalDateTime.now()));
            data.put(SOURCE_KEY, obj);
            logger.info(objectMapper.writeValueAsString(data));
        } catch (Exception e) {
            final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);
            e.printStackTrace(printWriter);
            logger.info(String.format(ERROR_TEMPLATE, result.toString()));
        }
    }
}
