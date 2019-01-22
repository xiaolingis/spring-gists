package com.bz.gists.cron;

import com.bz.gists.util.JsonUtil;
import com.bz.gists.util.LogUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 2019/1/21
 *
 * 定时任务基础类，所有定时任务都需要实现该类
 *
 * @author zhongyongbin
 */
public abstract class BaseCrontab<T> {

    private static final Logger CRONTAB_INFO_LOGGER = LoggerFactory.getLogger("CRONTAB_INFO_LOGGER");

    private static final Logger CRONTAB_ERROR_LOGGER = LoggerFactory.getLogger("CRONTAB_ERROR_LOGGER");

    final void execute() {
        this.execute(null);
    }

    final void execute(String json) {
        try {
            long startTime = System.currentTimeMillis();

            T parameterObject = StringUtils.isNotBlank(json) ? JsonUtil.fromJson(json, getParameterType()) : null;
            CrontabResult result = this.executeInternal(parameterObject);

            long endTime = System.currentTimeMillis();

            CRONTAB_INFO_LOGGER.info("crontab=[{}] ; parameter=[{}] ; start_time=[{}] ; end_time=[{}] ; duration_time(ms)=[{}] ; result=[{}]",
                    this.getClass().getSimpleName(),
                    JsonUtil.toJson(parameterObject),
                    LogUtil.formatTimestamp(startTime),
                    LogUtil.formatTimestamp(endTime),
                    endTime - startTime,
                    JsonUtil.toJson(result));
        } catch (Exception e) {
            CRONTAB_ERROR_LOGGER.error("crontab=[{}] ; parameter_json=[{}] ; stacktrace: ", this.getClass().getSimpleName(), json, e);
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
        private final boolean status;

        private Object message;

        public CrontabResult(boolean status) {
            this.status = status;
        }

        public CrontabResult setMessage(Object message) {
            this.message = message;
            return this;
        }

        public Object getMessage() {
            return message;
        }

        public boolean getStatus() {
            return status;
        }

        public static CrontabResult ofFail() {
            return new CrontabResult(false);
        }

        public static CrontabResult ofSucess() {
            return new CrontabResult(true);
        }
    }
}
