package com.bz.gists.util;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2019/5/23
 *
 * @author zhongyongbin
 */
public final class BeanMapUtil {

    public static Map<String, String> describeBeanExcludePrefix(Object bean, String... excludedProperties) {
        Map<String, String> map = new HashMap<>();
        new BeanMap(bean).forEach((k, v) -> {
            String key = String.valueOf(k);
            if (!StringUtils.equals("class", key) && Arrays.stream(excludedProperties).noneMatch(attr -> StringUtils.equals(attr, key))) {
                map.put(key, String.valueOf(v));
            }
        });

        return map;
    }

    public static Map<String, String> describeBeanExcludePrefix(Object bean, String excludedPropertiesPrefix) {
        Map<String, String> map = new HashMap<>();
        new BeanMap(bean).forEach((k, v) -> {
            String key = String.valueOf(k);
            if (!StringUtils.equals("class", key) && !StringUtils.startsWith(key, excludedPropertiesPrefix)) {
                map.put(key, String.valueOf(v));
            }
        });

        return map;
    }

    public static Map<String, String> describeBeanExcludeSuffix(Object bean, String excludedPropertiesSuffix) {
        Map<String, String> map = new HashMap<>();
        new BeanMap(bean).forEach((k, v) -> {
            String key = String.valueOf(k);
            if (!StringUtils.equals("class", key) && !StringUtils.endsWith(key, excludedPropertiesSuffix)) {
                map.put(key, String.valueOf(v));
            }
        });

        return map;
    }
}
