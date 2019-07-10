package com.bz.gists.redis.common;

import com.bz.gists.util.BeanMapUtil;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Created on 2019/7/10
 *
 * @author zhongyongbin
 */
public abstract class AbstractCacheRedis<T> extends AbstractRedis {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCacheRedis.class);

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 从 Redis 中获取数据实体
     *
     * @param key Redis 键
     */
    protected final Optional<T> getInternal(String key) {
        String value = redisTemplate.opsForValue().get(key);
        if (StringUtils.isBlank(value)) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(fromJson(value));
        }
    }

    private T fromJson(String json) {
        try {
            T result = null;
            if (StringUtils.isNotBlank(json)) {
                result = objectMapper.readValue(json, getJavaType());
            }
            return result;
        } catch (Exception e) {
            LOGGER.error("json deserialize fail!", e);
            return null;
        }
    }

    private JavaType getJavaType() {
        Class<?>[] parametricType = getParametricType();
        return Objects.nonNull(parametricType) && parametricType.length > 0 ? objectMapper.getTypeFactory().constructParametricType(getType(), parametricType) : objectMapper.getTypeFactory().constructType(getType());
    }

    /**
     * 如果转换的类型为泛型，则覆盖该方法并返回泛型类型
     */
    protected Class<?>[] getParametricType() {
        return null;
    }

    private Class<?> getType() {
        Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        if (type instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) type).getRawType();
        }
        return (Class<?>) type;
    }

    /**
     * 保存数据实体。在保存时，会将实体转化为 Map 再序列化成 JSON ，防止受实体类的 JsonIgnore 影响
     *
     * @param key                Redis 键
     * @param entity             实体
     * @param timeout            Redis 键过期时间
     * @param excludedProperties 实体不需要保存的属性
     */
    protected final void saveInternal(String key, T entity, Duration timeout, String... excludedProperties) {
        String data = toJson(BeanMapUtil.describe(entity, excludedProperties));
        redisTemplate.opsForValue().set(key, StringUtils.isNotBlank(data) ? data : "", timeout);
    }

    private String toJson(Map<?, ?> obj) {
        try {
            if (Objects.nonNull(obj)) {
                return objectMapper.writeValueAsString(obj);
            } else {
                return null;
            }
        } catch (Exception e) {
            LOGGER.error("json serialize fail!", e);
            return null;
        }
    }

    /**
     * 保存数据实体。在保存时，会将实体转化为 Map 再序列化成 JSON ，防止受实体类的 JsonIgnore 影响
     *
     * @param key                Redis 键
     * @param entity             实体
     * @param excludedProperties 实体不需要保存的属性
     */
    protected final void saveInternal(String key, T entity, String... excludedProperties) {
        String data = toJson(BeanMapUtil.describe(entity, excludedProperties));
        redisTemplate.opsForValue().set(key, StringUtils.isNotBlank(data) ? data : "");
    }

    /**
     * 删除数据实体
     *
     * @param key Redis 键
     */
    protected final void removeInternal(String key) {
        redisTemplate.delete(key);
    }
}
