package com.bz.gists.repository.mybatis.spec.common;

import com.bz.gists.exception.UnexpectedException;
import com.bz.gists.mapper.common.BaseMapper;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

/**
 * Created on 2019/1/18
 *
 * @author zhongyongbin
 */
public abstract class AbstractDaoRepository<T, ID, M extends BaseMapper<T, ID>> extends BaseRepository<T, ID> {

    @Autowired
    protected M mapper;

    @Override
    protected void saveInternal(T entity) {
        mapper.insertSelective(entity);
    }

    @Override
    protected T getInternal(ID id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    protected void updateInternal(T entity) {
        mapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    protected void removeInternal(ID id) {
        mapper.deleteByPrimaryKey(id);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected T findOneInternal(ISpecification specification) {
        return (T) invokeMapper(specification.toQuery(AbstractDaoSpecification.MapperQuery.class));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAll(ISpecification specification) {
        return (List<T>) invokeMapper(specification.toQuery(AbstractDaoSpecification.MapperQuery.class));
    }

    public final void saveIncludeNull(T entity) {
        mapper.insert(entity);
    }

    public final void updateIncludeNull(T entity) {
        mapper.updateByPrimaryKey(entity);
    }

    private Object invokeMapper(AbstractDaoSpecification.MapperQuery query) {
        Method method = BeanUtils.findMethodWithMinimalParameters(mapper.getClass(), query.getMethodName());
        if (Objects.nonNull(method)) {
            try {
                return method.invoke(mapper, query.getParameters());
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new UnexpectedException("mapper method invoke fail!", e);
            }
        } else {
            throw new UnexpectedException(String.format("mapper doesn't have method [%s]", query.getMethodName()));
        }

    }
}
