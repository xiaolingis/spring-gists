package com.bz.gists.util;

import com.bz.gists.exception.UnexpectedException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

/**
 * Created on 2019/1/19
 *
 * @author zhongyongbin
 */
public final class JsonUtil {

    private static ObjectMapper objectMapper;

    private JsonUtil() {
    }

    /**
     * 将对象序列化为 json 字符串
     *
     * @param obj 待序列化对象
     * @return json 字符串
     */
    public static String toJson(Object obj) {
        try {
            return getObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new UnexpectedException("json serialization error.", e);
        }
    }

    public static ObjectMapper getObjectMapper() {
        return Objects.requireNonNull(objectMapper, "objectMapper uninitialized");
    }

    public static void setObjectMapper(ObjectMapper objectMapper) {
        JsonUtil.objectMapper = objectMapper;
    }

    /**
     * 将对象序列化为格式化的 json 字符串
     *
     * @param obj 待序列化对象
     * @return json 字符串
     */
    public static String toPrettyJson(Object obj) {
        try {
            return getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(obj);
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
            return getObjectMapper().readValue(json, clazz);
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
            JavaType javaType = getObjectMapper().getTypeFactory().constructParametricType(clazz, parameterClasses);
            return getObjectMapper().readValue(json, javaType);
        } catch (Exception e) {

            throw new UnexpectedException("json deserialization error.", e);
        }
    }
}
