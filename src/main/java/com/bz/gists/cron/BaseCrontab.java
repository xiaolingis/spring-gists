package com.bz.gists.cron;

import com.bz.gists.util.JsonUtil;
import com.bz.gists.util.LogUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;

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

    final void execute() {
        this.execute(null);
    }

    final void execute(String json) {
        try {
            long startTime = System.currentTimeMillis();

            T parameterObject = StringUtils.isNotBlank(json) ? JsonUtil.fromJson(json, getParameterType()) : null;
            CrontabResult result = this.executeInternal(parameterObject);

            long endTime = System.currentTimeMillis();

            LogUtil.dataLog(CRONTAB_DATA_LOGGER, new LinkedHashMap<String, Object>() {{
                this.put("crontab", this.getClass().getSimpleName());
                this.put("parameter", parameterObject);
                this.put("start_time", LogUtil.formatTimestamp(startTime));
                this.put("end_time", LogUtil.formatTimestamp(endTime));
                this.put("duration_time", endTime - startTime);
                this.put("result", result);
            }});
        } catch (Exception e) {
            LOGGER.error("crontab=[{}] ; parameter_json=[{}] ; stacktrace: ", this.getClass().getSimpleName(), json, e);

            LogUtil.dataLog(CRONTAB_DATA_LOGGER, new LinkedHashMap<String, Object>() {{
                this.put("crontab", this.getClass().getSimpleName());
                this.put("result", CrontabResult.ofException().setMessage(String.format("%s: %s", e.getClass().getSimpleName(), e.getMessage())));
            }});
        }
    }

    /**
     * 定时任务执行方法
     *
     * @param parameterObject 任务执行参数，主要用于主动触发时传递参数。如果不会用到参数则无视。否则需要重写方法 {@link
     *                        BaseCrontab#getParameterType()}
     * @return true : 执行成功 ; false : 执行失败
     * @throws Exception 执行任务时可能抛出的异常
     */
    protected abstract CrontabResult executeInternal(T parameterObject) throws Exception;

    /**
     * 定时任务需要用到参数类型
     */
    protected Class<T> getParameterType() {
        return null;
    }

    public static class CrontabResult {

        private final CrontabStatus status;

        private Object message;

        public CrontabResult(CrontabStatus status) {
            this.status = status;
        }

        public CrontabResult setMessage(Object message) {
            this.message = message;
            return this;
        }

        public Object getMessage() {
            return message;
        }

        public CrontabStatus getStatus() {
            return status;
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
    }

    enum CrontabStatus {
        /**
         * 执行成功
         */
        SUCCESS,
        /**
         * 执行失败
         */
        FAIL,
        /**
         * 发生异常
         */
        EXCEPTION
    }
}
