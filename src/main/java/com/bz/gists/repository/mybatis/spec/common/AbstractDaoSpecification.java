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

    protected abstract String getMapperMethodName();

    protected abstract Object[] getMapperParameters();

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
