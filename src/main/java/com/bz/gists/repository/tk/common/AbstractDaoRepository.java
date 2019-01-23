package com.bz.gists.repository.tk.common;

import com.bz.gists.mapper.common.BaseMapper;
import com.bz.gists.repository.tk.spec.common.BaseRepository;

import org.springframework.beans.factory.annotation.Autowired;

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

    public final void saveIncludeNull(T entity) {
        mapper.insert(entity);
    }

    public final void updateIncludeNull(T entity) {
        mapper.updateByPrimaryKey(entity);
    }
}
