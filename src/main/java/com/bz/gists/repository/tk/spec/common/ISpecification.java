package com.bz.gists.repository.tk.spec.common;

/**
 * Created on 2019/1/23
 *
 * @author zhongyongbin
 */
public interface ISpecification {

    @SuppressWarnings("unchecked")
    default <Q> Q toQuery(Class<Q> queryType) {
        Object query = toQueryInternal();
        if (query.getClass() != queryType) {
            throw new IllegalArgumentException("specification query type illegal");
        }

        return (Q) query;
    }

    Object toQueryInternal();
}
