package com.bz.gists.mybatis.handler;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Created on 2019/1/11
 *
 * Mybatis JSON 类型转换
 *
 * @author zhongyongbin
 */
public abstract class AbstractJsonTypeHandler<T> extends BaseTypeHandler<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractJsonTypeHandler.class);

    private final Type rawType;

    @Autowired
    private ObjectMapper objectMapper;

    AbstractJsonTypeHandler() {
        rawType = getSuperclassTypeParameter(getClass());
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, T t, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, toJson(t));
    }

    @Override
    public T getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return fromJson(resultSet.getString(s));
    }

    @Override
    public T getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return fromJson(resultSet.getString(i));
    }

    @Override
    public T getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return fromJson(callableStatement.getString(i));
    }

    public abstract TypeReference<T> getTypeReference();

    /**
     * 在 JSON 序列化之前对对象进行操作
     */
    protected void beforeToJson(T obj) {
    }

    /**
     * 在 JSON 反序列化之后对对象进行操作
     */
    protected void afterFromJson(T obj) {
    }

    private JavaType getJavaType() {
        return objectMapper.getTypeFactory().constructType(rawType);
    }

    private Type getSuperclassTypeParameter(Class<?> clazz) {
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof Class) {
            // try to climb up the hierarchy until meet something useful
            if (TypeReference.class != genericSuperclass) {
                return getSuperclassTypeParameter(clazz.getSuperclass());
            }

            throw new TypeException("'" + getClass() + "' extends TypeReference but misses the type parameter. "
                    + "Remove the extension or add a type parameter to it.");
        }

        Type rawType = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        // TODO remove this when Reflector is fixed to return Types
        if (rawType instanceof ParameterizedType) {
            rawType = ((ParameterizedType) rawType).getRawType();
        }

        return rawType;
    }

    private String toJson(T obj) {
        try {
            if (Objects.nonNull(obj)) {
                beforeToJson(obj);
                return objectMapper.writeValueAsString(obj);
            } else {
                return null;
            }
        } catch (Exception e) {
            LOGGER.error("json serialize fail!", e);
            return null;
        }
    }

    private T fromJson(String json) {
        try {
            T result = null;
            if (StringUtils.isNotBlank(json)) {
                result = objectMapper.readValue(json, getJavaType());
                afterFromJson(result);
            }
            return result;
        } catch (Exception e) {
            LOGGER.error("json deserialize  fail!", e);
            return null;
        }
    }
}
