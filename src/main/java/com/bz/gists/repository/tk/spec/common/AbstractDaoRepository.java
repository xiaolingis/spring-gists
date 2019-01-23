package com.bz.gists.repository.tk.spec.common;

import org.apache.ibatis.session.RowBounds;
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
    protected List<T> findALLPaginationInternal(ISpecification specification, int page, int count) {
        return mapper.selectByExampleAndRowBounds(specification.toQuery(Example.class), new RowBounds(page, count));
    }

    public final void saveIncludeNull(T entity) {
        mapper.insert(entity);
    }

    public final void updateIncludeNull(T entity) {
        mapper.updateByPrimaryKey(entity);
    }
}
