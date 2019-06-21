package com.bz.gists.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2019/5/23
 *
 * @author zhongyongbin
 */
public final class MapUtil {

    public static Map<String, String> toStringMap(Map<?, ?> map) {
        Map<String, String> stringMap = new HashMap<>(map.size());
        map.forEach((k, v) -> stringMap.put(String.valueOf(k), String.valueOf(v)));

        return stringMap;
    }

    public static Map<String, Object> toStringKeyMap(Map<?, ?> map) {
        Map<String, Object> stringKeyMap = new HashMap<>(map.size());
        map.forEach((k, v) -> stringKeyMap.put(String.valueOf(k), v));

        return stringKeyMap;
    }
}
