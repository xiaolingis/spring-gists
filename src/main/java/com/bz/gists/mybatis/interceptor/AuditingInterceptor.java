package com.bz.gists.mybatis.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * Created on 2018-11-01
 *
 * MyBatis 拦截器，主要用于设置审计字段值
 *
 * @author zhongyongbin
 */
@Intercepts(@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}))
public class AuditingInterceptor implements Interceptor {

    private static final String CREATE_TIME_FIELD = "createTime";

    private static final String UPDATE_TIME_FIELD = "updateTime";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement stmt = (MappedStatement) invocation.getArgs()[0];
        Object entity = invocation.getArgs()[1];
        if (Objects.isNull(stmt)) {
            return invocation.proceed();
        }
        SqlCommandType commandType = stmt.getSqlCommandType();
        switch (commandType) {
            case INSERT:
                recordCreateTime(entity);
                recordUpdateTime(entity);
                break;
            case UPDATE:
                recordUpdateTime(entity);
                break;
            default:
                break;
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    @SuppressWarnings("unchecked")
    private void invokeWriteMethod(Object entity, String fieldName, Object value) throws Exception {
        if (entity instanceof Map) {
            Map param = (Map) entity;
            param.put(fieldName, value);
        } else {
            BeanInfo beanInfo = Introspector.getBeanInfo(entity.getClass());
            for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
                if (StringUtils.equals(propertyDescriptor.getName(), fieldName)) {
                    Method setter = propertyDescriptor.getWriteMethod();
                    if (Objects.nonNull(setter)) {
                        setter.invoke(entity, value);
                    }
                    break;
                }
            }
        }
    }

    private void recordCreateTime(Object entity) throws Exception {
        invokeWriteMethod(entity, CREATE_TIME_FIELD, new Date());
    }

    private void recordUpdateTime(Object entity) throws Exception {
        invokeWriteMethod(entity, UPDATE_TIME_FIELD, new Date());
    }
}
