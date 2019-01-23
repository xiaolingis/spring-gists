package com.bz.gists.repository.tk.spec.common;

import com.github.pagehelper.Page;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

/**
 * Created on 2019/1/18
 *
 * @author zhongyongbin
 */
public abstract class AbstractDaoRepository<T, ID, M extends Mapper<T>> extends BaseRepository<T, ID> {

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

    @Override
    protected T findOneInternal(ISpecification specification) {
        return mapper.selectOneByExample(specification.toQuery(Example.class));
    }

    @Override
    protected List<T> findALLInternal(ISpecification specification) {
        return mapper.selectByExample(specification.toQuery(Example.class));
    }

    @Override
    protected Page<T> findALLInternal(ISpecification specification, int offset, int count) {
        return (Page<T>) mapper.selectByExample(specification.toQuery(Example.class));
    }

    @Override
    protected long countInternal(ISpecification specification) {
        return mapper.selectCountByExample(specification.toQuery(Example.class));
    }

    public final void saveIncludeNull(T entity) {
        mapper.insert(entity);
    }

    public final void updateIncludeNull(T entity) {
        mapper.updateByPrimaryKey(entity);
    }
}
