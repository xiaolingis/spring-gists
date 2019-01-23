package com.bz.gists.repository.mybatis.spec.common;

/**
 * Created on 2019/1/23
 *
 * @author zhongyongbin
 */
public abstract class AbstractDaoSpecification implements ISpecification {

    @Override
    public Object toQuery() {
        return new MapperQuery(getMapperMethodName(), getMapperParameters());
    }

    /**
     * 返回要使用的 Mapper 的方法
     */
    protected abstract String getMapperMethodName();

    /**
     * 返回 Mapper 方法的参数
     */
    protected abstract Object[] getMapperParameters();

    /**
     * Mapper 查询对象，主要利用 mapper 的方法名和参数构造成查询体用于查询
     */
    public static class MapperQuery {
        private final String methodName;

        private final Object[] parameters;

        public MapperQuery(String methodName, Object[] parameters) {
            this.methodName = methodName;
            this.parameters = parameters;
        }

        public String getMethodName() {
            return methodName;
        }

        public Object[] getParameters() {
            return parameters;
        }
    }
}
