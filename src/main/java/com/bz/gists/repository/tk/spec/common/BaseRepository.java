package com.bz.gists.repository.tk.spec.common;

import com.bz.gists.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Created on 2019/1/18
 *
 * 基于 DDD 实现，以 Specification 作为检索机制
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
        return Optional.ofNullable(entity).orElseThrow(() -> new NotFoundException("id has no corresponding entity"));
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

    /**
     * 查找一个对象
     *
     * @param specification 规格
     */
    public Optional<T> findOne(ISpecification specification) {
        return Optional.ofNullable(findOneInternal(specification));
    }

    /**
     * 查找对象集合
     *
     * @param specification 规格
     */
    public List<T> findAll(ISpecification specification) {
        return findALLInternal(specification);
    }

    /**
     * 查找对象集合，带分页
     *
     * @param specification 规格
     * @param page          页数
     * @param count         数量
     */
    public List<T> findAllPagination(ISpecification specification, int page, int count) {
        return findALLPaginationInternal(specification, page, count);
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

    protected T findOneInternal(ISpecification specification) {
        throw new UnsupportedOperationException("find one method is not implemented");
    }

    protected List<T> findALLInternal(ISpecification specification) {
        throw new UnsupportedOperationException("find all method is not implemented");
    }

    protected List<T> findALLPaginationInternal(ISpecification specification, int page, int count) {
        throw new UnsupportedOperationException("find all pagination method is not implemented");
    }
}
