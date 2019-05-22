package com.bz.gists.cron;

/**
 * Created on 2019/5/22
 *
 * @author zhongyongbin
 */
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
