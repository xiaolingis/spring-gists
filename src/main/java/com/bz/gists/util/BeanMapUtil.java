package com.bz.gists.util;

import com.google.common.base.CaseFormat;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

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

    private static KeyType keyType = KeyType.SNAKE;

    public static Map<String, Object> describe(Object bean, String... excludedProperties) {
        Map<String, Object> map = new HashMap<>();
        new BeanMap(bean).forEach((k, v) -> {
            String key = String.valueOf(k);
            if (!StringUtils.equals("class", key) && Arrays.stream(excludedProperties).noneMatch(attr -> StringUtils.equals(attr, key))) {
                map.put(key(key), String.valueOf(v));
            }
        });

        return map;
    }

    public static Map<String, Object> describeExcludedPropertiesPrefix(Object bean, String excludedPropertiesPrefix) {
        Map<String, Object> map = new HashMap<>();
        new BeanMap(bean).forEach((k, v) -> {
            String key = String.valueOf(k);
            if (!StringUtils.equals("class", key) && !StringUtils.startsWith(key, excludedPropertiesPrefix)) {
                map.put(key(key), String.valueOf(v));
            }
        });

        return map;
    }

    public static Map<String, Object> describeExcludedPropertiesSuffix(Object bean, String excludedPropertiesSuffix) {
        Map<String, Object> map = new HashMap<>();
        new BeanMap(bean).forEach((k, v) -> {
            String key = String.valueOf(k);
            if (!StringUtils.equals("class", key) && !StringUtils.endsWith(key, excludedPropertiesSuffix)) {
                map.put(key(key), String.valueOf(v));
            }
        });

        return map;
    }

    public static <T> Optional<T> populate(Map<String, ?> beanMap, Class<T> type) throws ReflectiveOperationException {
        if (Objects.nonNull(beanMap) && beanMap.size() > 0) {
            T instance = type.newInstance();
            BeanUtils.populate(instance, toCamelMap(beanMap));
            return Optional.of(instance);
        } else {
            return Optional.empty();
        }
    }

    public static void setKeyType(KeyType keyType) {
        BeanMapUtil.keyType = keyType;
    }

    private static String key(String key) {
        switch (keyType) {
            case SNAKE:
                return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, key);
            case CAMEL:
                return key;
            default:
                return null;
        }
    }

    private static Map<String, ?> toCamelMap(Map<String, ?> beanMap) {
        if (keyType == KeyType.SNAKE) {
            Map<String, Object> data = new HashMap<>(beanMap.size());
            beanMap.forEach((k, v) -> data.put(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, k), v));

            return data;
        }
        return beanMap;
    }

    public enum KeyType {
        SNAKE, CAMEL
    }
}
