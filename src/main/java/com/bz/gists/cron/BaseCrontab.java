package com.bz.gists.cron;

import com.bz.gists.util.LogUtil;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * Created on 2019/1/21
 *
 * 定时任务基础类，所有定时任务都需要实现该类
 *
 * @author zhongyongbin
 */
public abstract class BaseCrontab<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseCrontab.class);

    private static final Logger CRONTAB_DATA_LOGGER = LoggerFactory.getLogger("CRONTAB_DATA_LOGGER");

    @Autowired
    private ObjectMapper objectMapper;

    final void execute() {
        this.execute(null);
    }

    final void execute(String json) {
        long startTime = System.currentTimeMillis();
        long endTime;
        try {

            T parameterObject = StringUtils.isNotBlank(json) ? objectMapper.readValue(json, getJavaType()) : null;
            CrontabResult result = this.executeInternal(parameterObject);
            endTime = System.currentTimeMillis();
            LogUtil.dataLog(CRONTAB_DATA_LOGGER,
                    Pair.of("crontab", this.getClass().getSimpleName()),
                    Pair.of("parameter", parameterObject),
                    Pair.of("start_time", startTime),
                    Pair.of("end_time", endTime),
                    Pair.of("duration_time", endTime - startTime),
                    Pair.of("result", result));
        } catch (Exception e) {
            LOGGER.error("crontab=[{}] ; parameter_json=[{}] ; stacktrace: ", this.getClass().getSimpleName(), json, e);
            endTime = System.currentTimeMillis();
            LogUtil.dataLog(CRONTAB_DATA_LOGGER,
                    Pair.of("crontab", this.getClass().getSimpleName()),
                    Pair.of("start_time", startTime),
                    Pair.of("end_time", endTime),
                    Pair.of("duration_time", endTime - startTime),
                    Pair.of("parameter", json),
                    Pair.of("result", CrontabResult.ofException().setMessage(String.format("%s: %s", e.getClass().getSimpleName(), e.getMessage()))));
        }
    }

    /**
     * 定时任务需要用到参数类型
     */
    protected Class<T> getParameterType() {
        return null;
    }

    /**
     * 定时任务执行方法
     *
     * @param parameterObject 任务执行参数，主要用于主动触发时传递参数。如果不会用到参数则无视。如果参数为泛型，实现方法 {@link
     *                        BaseCrontab#getParametricType()} 返回泛型类型
     * @return true : 执行成功 ; false : 执行失败
     * @throws Exception 执行任务时可能抛出的异常
     */
    protected abstract CrontabResult executeInternal(T parameterObject) throws Exception;

    private JavaType getJavaType() {
        Class<?>[] parametricType = getParametricType();
        return Objects.nonNull(parametricType) && parametricType.length > 0 ? objectMapper.getTypeFactory().constructParametricType(getType(), parametricType) : objectMapper.getTypeFactory().constructType(getType());
    }

    /**
     * 如果参数类型为泛型，则覆盖该方法并返回泛型类型
     */
    protected Class<?>[] getParametricType() {
        return null;
    }

    private Class<?> getType() {
        Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        if (type instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) type).getRawType();
        }
        return (Class<?>) type;
    }

    public static class CrontabResult {

        private final CrontabStatus status;

        private Object message;

        public CrontabResult(CrontabStatus status) {
            this.status = status;
        }

        public static CrontabResult ofFail() {
            return new CrontabResult(CrontabStatus.FAIL);
        }

        public static CrontabResult ofSuccess() {
            return new CrontabResult(CrontabStatus.SUCCESS);
        }

        public static CrontabResult ofException() {
            return new CrontabResult(CrontabStatus.EXCEPTION);
        }

        public Object getMessage() {
            return message;
        }

        public CrontabResult setMessage(Object message) {
            this.message = message;
            return this;
        }

        public CrontabStatus getStatus() {
            return status;
        }
    }
}
