package com.bz.gists.cron;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
public final class Scheduler implements ApplicationContextAware {

    @Autowired
    @Qualifier("serviceExecutor")
    private Executor serviceExecutor;

    private ApplicationContext applicationContext;

    private void executeCrontab(Class<? extends BaseCrontab> crontab) {
        serviceExecutor.execute(() -> applicationContext.getBean(crontab).execute());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
