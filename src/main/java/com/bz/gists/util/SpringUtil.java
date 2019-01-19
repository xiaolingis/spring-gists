package com.bz.gists.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public final class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
    }

    public static List<String> getBeanNamesForType(Class<?> type) {
        return Arrays.asList(applicationContext.getBeanNamesForType(type));
    }

    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return applicationContext.getBean(name, requiredType);
    }

    public static Object getBean(String name, Object... args) throws BeansException {
        return applicationContext.getBean(name, args);
    }

    public static <T> T getBean(Class<T> requiredType) throws BeansException {
        return applicationContext.getBean(requiredType);
    }

    public static <T> T getBean(Class<T> requiredType, Object... args) throws BeansException {
        return applicationContext.getBean(requiredType, args);
    }

    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.isSingleton(name);
    }

    public static boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.isPrototype(name);
    }

    public static boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException {
        return applicationContext.isTypeMatch(name, typeToMatch);
    }

    public static boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException {
        return applicationContext.isTypeMatch(name, typeToMatch);
    }

    public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.getType(name);
    }

    public static String[] getAliases(String name) {
        return applicationContext.getAliases(name);
    }
}
