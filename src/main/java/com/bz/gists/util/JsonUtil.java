package com.bz.gists.util;

import com.bz.gists.exception.UnexpectedException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created on 2019/1/19
 *
 * @author zhongyongbin
 */
@Component
public final class JsonUtil implements ApplicationContextAware {

    private static ObjectMapper objectMapper;

    private JsonUtil() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        JsonUtil.objectMapper = applicationContext.getBean(ObjectMapper.class);
    }

    /**
     * 将对象序列化为 json 字符串
     *
     * @param obj 待序列化对象
     * @return json 字符串
     */
    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new UnexpectedException("json serialization error.", e);
        }
    }

    /**
     * 将对象序列化为格式化的 json 字符串
     *
     * @param obj 待序列化对象
     * @return json 字符串
     */
    public static String toPrettyJson(Object obj) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            throw new UnexpectedException("json serialization error.", e);
        }
    }

    /**
     * 将 json 字符串反序列化为对象
     *
     * @param json  json 字符串
     * @param clazz 对象类型
     * @return 通过反序列化得到的对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new UnexpectedException("json deserialization error.", e);
        }
    }

    /**
     * 将 json 字符串反序列化为泛型对象
     *
     * @param json             json 字符串
     * @param clazz            对象类型
     * @param parameterClasses 泛型参数类型
     * @return 通过反序列化得到的对象
     */
    public static <T> T fromJson(String json, Class<?> clazz, Class<?>... parameterClasses) {
        try {

            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(clazz, parameterClasses);
            return objectMapper.readValue(json, javaType);
        } catch (Exception e) {

            throw new UnexpectedException("json deserialization error.", e);
        }
    }
}
