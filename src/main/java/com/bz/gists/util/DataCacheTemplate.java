package com.bz.gists.util;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created on 2019/7/2
 *
 * 数据缓存模板操作
 *
 * @author zhongyongbin
 */
public final class DataCacheTemplate<T> {

    public SaveOps<T> opsForSave() {
        return new SaveOps<>();
    }

    public UpdateOps<T> opsForUpdate() {
        return new UpdateOps<>();
    }

    public GetOps<T> opsForGet() {
        return new GetOps<>();
    }

    public static final class SaveOps<T> {
        private Consumer<T> masterSaveOperation;

        private Consumer<T> secondarySaveOperation;

        private SaveOps(){
        }

        public void save(T data) {
            Objects.requireNonNull(masterSaveOperation, "master save operation is null");
            Objects.requireNonNull(secondarySaveOperation, "secondary save operation is null");
            try {
                masterSaveOperation.accept(data);
                secondarySaveOperation.accept(data);
            } catch (Exception e) {
                throw new DataCacheOperatorException("save master data and save secondary data fail!", e);
            }
        }

        public SaveOps<T> withMasterSaveOperation(Consumer<T> masterSaveOperation) {
            this.masterSaveOperation = masterSaveOperation;
            return this;
        }

        public SaveOps<T> withSecondarySaveOperation(Consumer<T> secondarySaveOperation) {
            this.secondarySaveOperation = secondarySaveOperation;
            return this;
        }
    }

    public static final class UpdateOps<T> {
        private Consumer<T> masterUpdateOperation;

        private Consumer<T> secondaryUpdateOperation;

        private Consumer<T> secondaryDeleteOperation;

        private UpdateOps(){
        }

        public void updateAndUpdate(T data) {
            Objects.requireNonNull(masterUpdateOperation, "master update operation is null");
            Objects.requireNonNull(secondaryUpdateOperation, "secondary update operation is null");
            try {
                masterUpdateOperation.accept(data);
                secondaryUpdateOperation.accept(data);
            } catch (Exception e) {
                throw new DataCacheOperatorException("update master data and update secondary data fail!", e);
            }
        }

        public void updateAndDelete(T data) {
            Objects.requireNonNull(masterUpdateOperation, "master update operation is null");
            Objects.requireNonNull(secondaryDeleteOperation, "secondary update operation is null");
            try {
                masterUpdateOperation.accept(data);
                secondaryDeleteOperation.accept(data);
            } catch (Exception e) {
                throw new DataCacheOperatorException("update master data and delete secondary data fail!", e);
            }
        }

        public void updateAndDoubleDelete(T data) {
            Objects.requireNonNull(masterUpdateOperation, "master update operation is null");
            Objects.requireNonNull(secondaryDeleteOperation, "secondary update operation is null");
            try {
                secondaryDeleteOperation.accept(data);
                masterUpdateOperation.accept(data);
                secondaryDeleteOperation.accept(data);
            } catch (Exception e) {
                throw new DataCacheOperatorException("update master data and double delete secondary data fail!", e);
            }
        }

        public UpdateOps<T> withMasterUpdateOperation(Consumer<T> masterUpdateOperation) {
            this.masterUpdateOperation = masterUpdateOperation;
            return this;
        }

        public UpdateOps<T> withSecondaryUpdateOperation(Consumer<T> secondaryUpdateOperation) {
            this.secondaryUpdateOperation = secondaryUpdateOperation;
            return this;
        }

        public UpdateOps<T> withSecondaryDeleteOperation(Consumer<T> secondaryDeleteOperation) {
            this.secondaryDeleteOperation = secondaryDeleteOperation;
            return this;
        }
    }

    public static final class GetOps<T> {

        private Supplier<T> masterGetOperation;
        private Supplier<T> secondaryGetOperation;
        private Consumer<T> secondarySaveOperation;

        private GetOps() {
        }

        public Optional<T> get() {
            Objects.requireNonNull(masterGetOperation, "master get operation is null");
            Objects.requireNonNull(secondaryGetOperation, "secondary get operation is null");
            Objects.requireNonNull(secondarySaveOperation, "secondary save operation is null");
            try {
                T data = secondaryGetOperation.get();
                if (Objects.isNull(data)) {
                    data = masterGetOperation.get();
                    if (Objects.nonNull(data)) {
                        secondarySaveOperation.accept(data);
                    }
                }
                return Optional.ofNullable(data);
            } catch (Exception e) {
                throw new DataCacheOperatorException("get data fail!", e);
            }
        }

        public GetOps<T> withMasterGetOperation(Supplier<T> masterGetOperation) {
            this.masterGetOperation = masterGetOperation;
            return this;
        }

        public GetOps<T> withSecondaryGetOperation(Supplier<T> secondaryGetOperation) {
            this.secondaryGetOperation = secondaryGetOperation;
            return this;
        }

        public GetOps<T> withSecondarySaveOperation(Consumer<T> secondarySaveOperation) {
            this.secondarySaveOperation = secondarySaveOperation;
            return this;
        }
    }

    public static class DataCacheOperatorException extends RuntimeException {
        DataCacheOperatorException(String message, Throwable e) {
            super(message, e);
        }
    }
}
