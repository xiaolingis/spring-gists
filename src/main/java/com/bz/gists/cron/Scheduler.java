package com.bz.gists.cron;

import com.bz.gists.util.SpringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;

/**
 * Created on 2019/1/21
 *
 * 所有定时任务集中在该类统一管理
 *
 * @author zhongyongbin
 */
@Service
public final class Scheduler {

    @Autowired
    @Qualifier("serviceExecutor")
    private Executor serviceExecutor;

    private void executeCrontab(Class<? extends BaseCrontab> crontab) {
        serviceExecutor.execute(() -> SpringUtil.getBean(crontab).execute());
    }
}
