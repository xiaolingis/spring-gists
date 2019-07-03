package com.bz.gists.util;

import com.google.common.base.CaseFormat;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


/**
 * Created on 2019/5/23
 *
 * @author zhongyongbin
 */
public final class BeanMapUtil {

    /**
     * 默认排除的字段
     */
    private static final String DEFAULT_EXCLUDE_KEY = "class";

    /**
     * 转换的 Map 的 Key 风格
     */
    private static KeyNameType keyNameType = KeyNameType.SNAKE;

    @SuppressWarnings("unchecked")
    private static BeanUtilsBean beanUtilsBean = new BeanUtilsBean(new ConvertUtilsBean() {
        @Override
        public Object convert(String value, Class clazz) {
            if (clazz.isEnum()) {
                return Enum.valueOf(clazz, value);
            } else {
                return super.convert(value, clazz);
            }
        }
    });

    /**
     * 将对象转化为Map
     *
     * @param bean               对象
     * @param excludedProperties 不想转化的属性名
     */
    public static Map<String, Object> describe(Object bean, String... excludedProperties) {
        Map<String, Object> map = new HashMap<>();
        new BeanMap(bean).forEach((k, v) -> {
            String key = String.valueOf(k);
            if (!StringUtils.equals(DEFAULT_EXCLUDE_KEY, key) && Arrays.stream(excludedProperties).noneMatch(attr -> StringUtils.equals(attr, key))) {
                map.put(keyName(key), String.valueOf(v));
            }
        });

        return map;
    }

    /**
     * 将对象转化为Map
     *
     * @param bean                     对象
     * @param excludedPropertiesPrefix 不想转化的属性名的前序
     */
    public static Map<String, Object> describeExcludedPropertiesPrefix(Object bean, String excludedPropertiesPrefix) {
        Map<String, Object> map = new HashMap<>();
        new BeanMap(bean).forEach((k, v) -> {
            String key = String.valueOf(k);
            if (!StringUtils.equals(DEFAULT_EXCLUDE_KEY, key) && !StringUtils.startsWith(key, excludedPropertiesPrefix)) {
                map.put(keyName(key), String.valueOf(v));
            }
        });

        return map;
    }

    /**
     * 将对象转化为Map
     *
     * @param bean                     对象
     * @param excludedPropertiesSuffix 不想转化的属性名的后序
     */
    public static Map<String, Object> describeExcludedPropertiesSuffix(Object bean, String excludedPropertiesSuffix) {
        Map<String, Object> map = new HashMap<>();
        new BeanMap(bean).forEach((k, v) -> {
            String key = String.valueOf(k);
            if (!StringUtils.equals(DEFAULT_EXCLUDE_KEY, key) && !StringUtils.endsWith(key, excludedPropertiesSuffix)) {
                map.put(keyName(key), String.valueOf(v));
            }
        });

        return map;
    }

    /**
     * 将 Map 转化为对象
     *
     * @param beanMap Bean Map
     * @param type    转换类型
     * @param <T>     转化类型
     * @return 转换得到的 Bean
     */
    public static <T> Optional<T> populate(Map<String, ?> beanMap, Class<T> type) {
        if (Objects.nonNull(beanMap) && beanMap.size() > 0) {
            try {
                T instance = type.newInstance();
                beanUtilsBean.populate(instance, toCamelMap(beanMap));
                return Optional.of(instance);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new BeanMapException("populate instance fail!", e);
            }
        } else {
            return Optional.empty();
        }
    }

    /**
     * 设置 Map 的 key 名称类型，分别有下划线（蛇形）以及驼峰。参看 {@link KeyNameType}
     *
     * @param keyNameType map key 风格
     */
    public static void setKeyNameType(KeyNameType keyNameType) {
        BeanMapUtil.keyNameType = keyNameType;
    }

    private static String keyName(String key) {
        switch (keyNameType) {
            case SNAKE:
                return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, key);
            case CAMEL:
                return key;
            default:
                return null;
        }
    }

    private static Map<String, ?> toCamelMap(Map<String, ?> beanMap) {
        if (keyNameType == KeyNameType.SNAKE) {
            Map<String, Object> data = new HashMap<>(beanMap.size());
            beanMap.forEach((k, v) -> data.put(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, k), v));

            return data;
        }
        return beanMap;
    }

    public enum KeyNameType {
        SNAKE, CAMEL
    }

    public static class BeanMapException extends RuntimeException {
        BeanMapException(String message, Throwable e) {
            super(message, e);
        }
    }
}
