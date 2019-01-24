package com.bz.gists.repository.tk.common;

import com.bz.gists.exception.NotFoundException;

import java.util.Optional;

/**
 * Created on 2019/1/18
 *
 * @author zhongyongbin
 */
public abstract class BaseRepository<T, ID> {

    /**
     * 保存对象
     */
    public final void save(T entity) {
        saveInternal(entity);
    }

    /**
     * 根据 id 获取对象，获取的对象肯定不为空
     */
    public final T get(ID id) {
        T entity = getInternal(id);
        return Optional.ofNullable(entity).orElseThrow(() -> new NotFoundException("there is no corresponding entity for this id"));
    }

    /**
     * 更新对象
     */
    public void update(T entity) {
        updateInternal(entity);
    }

    /**
     * 根据 id 移除对象
     */
    public void remove(ID id) {
        removeInternal(id);
    }

    protected void saveInternal(T entity) {
        throw new UnsupportedOperationException("save method is not implemented");
    }

    protected T getInternal(ID id) {
        throw new UnsupportedOperationException("get method is not implemented");
    }

    protected void updateInternal(T entity) {
        throw new UnsupportedOperationException("update method is not implemented");
    }

    protected void removeInternal(ID id) {
        throw new UnsupportedOperationException("remove method is not implemented");
    }
}
