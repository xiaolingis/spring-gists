package com.bz.gists.repository.tk.spec.common;

/**
 * Created on 2019/1/23
 *
 * 规格接口
 *
 * @author zhongyongbin
 */
public interface ISpecification {

    /**
     * 返回指定类型的查询对象
     *
     * @param queryType 查询对象的类型
     * @return 查询对象
     */
    @SuppressWarnings("unchecked")
    default <Q> Q toQuery(Class<Q> queryType) {
        Object query = toQuery();
        if (query.getClass() != queryType) {
            throw new IllegalArgumentException("specification query type illegal");
        }

        return (Q) query;
    }

    /**
     * 返回查询对象
     *
     * @return 查询对象
     */
    Object toQuery();

    /**
     * 返回查询对象的唯一 key ，用于标记查询条件
     *
     * @return 查询条件的 key
     */
    default String toQueryKey() {
        return toQuery().toString();
    }
}
