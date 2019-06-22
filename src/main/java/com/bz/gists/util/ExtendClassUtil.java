package com.bz.gists.util;

import com.bz.gists.exception.UnexpectedException;

import org.springframework.beans.BeanUtils;

/**
 * Created on 2019/1/15
 *
 * 数据库对象需要业务字段时，不要直接添加到类中，而是通过继承扩展。该工具类为快速获取扩展对象。
 *
 * @author zhongyongbin
 */
public final class ExtendClassUtil {

    public static <T> T getExtendInstance(Object source, Class<T> extendClass) {
        if (source.getClass().isAssignableFrom(extendClass)) {
            try {
                T extendInstance = extendClass.newInstance();
                BeanUtils.copyProperties(source, extendInstance);
                return extendInstance;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new UnexpectedException("exception occurs while instantiating", e);
            }
        } else {
            throw new IllegalArgumentException("no inheritance");
        }
    }
}
