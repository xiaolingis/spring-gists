package com.bz.gists.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created on 2019/5/23
 *
 * @author zhongyongbin
 */
public final class MapUtil {

    /**
     * 转换 Map 的键的类型
     *
     * @param map                需要转换的 map
     * @param keyConvertFunction 键类型转换方法
     * @param <K>                键要转换的类型
     * @param <OK>               待转换 map 的键类型
     * @param <OV>               待转换 map 的值类型
     * @return 转换后得到的新的 Map
     */
    public static <K, OK, OV> Map<K, OV> convertMapKeyType(Map<OK, OV> map, Function<OK, K> keyConvertFunction) {
        return convertMapType(map, keyConvertFunction, Function.identity());
    }

    /**
     * 转换 Map 的键的类型
     *
     * @param map                  需要转换的 map
     * @param valueConvertFunction 值类型转换方法
     * @param <V>                  键要转换的类型
     * @param <OK>                 待转换 map 的键类型
     * @param <OV>                 待转换 map 的值类型
     * @return 转换后得到的新的 Map
     */
    public static <V, OK, OV> Map<OK, V> convertMapValueType(Map<OK, OV> map, Function<OV, V> valueConvertFunction) {
        return convertMapType(map, Function.identity(), valueConvertFunction);
    }

    /**
     * 将 Map 的键与值的类型进行转换
     *
     * @param map                  需要转换的 Map
     * @param keyConvertFunction   键类型转换方法
     * @param valueConvertFunction 值类型转换方法
     * @param <K>                  键要转换的类型
     * @param <V>                  值要转换的类型
     * @param <OK>                 待转换 map 的键类型
     * @param <OV>                 待转换 map 的值类型
     * @return 转换后得到的新的 Map
     */
    public static <K, V, OK, OV> Map<K, V> convertMapType(Map<OK, OV> map, Function<OK, K> keyConvertFunction, Function<OV, V> valueConvertFunction) {
        Map<K, V> newMap = new HashMap<>(map.size());
        map.forEach((k, v) -> newMap.put(keyConvertFunction.apply(k), valueConvertFunction.apply(v)));

        return newMap;
    }
}
