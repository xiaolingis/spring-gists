package com.bz.gists.util;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created on 2019/5/23
 *
 * @author zhongyongbin
 */
public final class BeanMapUtil {

    public static Map<String, Object> describe(Object bean, String... excludedProperties) {
        Map<String, Object> map = new HashMap<>();
        new BeanMap(bean).forEach((k, v) -> {
            String key = String.valueOf(k);
            if (!StringUtils.equals("class", key) && Arrays.stream(excludedProperties).noneMatch(attr -> StringUtils.equals(attr, key))) {
                map.put(key, String.valueOf(v));
            }
        });

        return map;
    }

    public static Map<String, Object> describeExcludedPropertiesPrefix(Object bean, String excludedPropertiesPrefix) {
        Map<String, Object> map = new HashMap<>();
        new BeanMap(bean).forEach((k, v) -> {
            String key = String.valueOf(k);
            if (!StringUtils.equals("class", key) && !StringUtils.startsWith(key, excludedPropertiesPrefix)) {
                map.put(key, String.valueOf(v));
            }
        });

        return map;
    }

    public static Map<String, Object> describeExcludedPropertiesSuffix(Object bean, String excludedPropertiesSuffix) {
        Map<String, Object> map = new HashMap<>();
        new BeanMap(bean).forEach((k, v) -> {
            String key = String.valueOf(k);
            if (!StringUtils.equals("class", key) && !StringUtils.endsWith(key, excludedPropertiesSuffix)) {
                map.put(key, String.valueOf(v));
            }
        });

        return map;
    }

    public static <T> T populate(Map<String, ?> beanMap, Class<T> type) throws ReflectiveOperationException {
        if (Objects.nonNull(beanMap) && beanMap.size() > 0) {
            T instance = type.newInstance();
            BeanUtils.populate(instance, beanMap);
            return instance;
        } else {
            return null;
        }
    }
}
