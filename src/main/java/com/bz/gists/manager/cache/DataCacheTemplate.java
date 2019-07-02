package com.bz.gists.manager.cache;

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

    private Consumer<T> masterSaveOperation;

    private Consumer<T> secondarySaveOperation;

    private Consumer<T> masterUpdateOperation;

    private Consumer<T> secondaryUpdateOperation;

    private Consumer<T> secondaryDeleteOperation;

    private Runnable secondarySpecificDeleteOperation;

    private Supplier<T> masterGetOperation;

    private Supplier<T> secondaryGetOperation;

    private SaveOps<T> saveOps = new SaveOps<>(this);

    private UpdateOps<T> updateOps = new UpdateOps<>(this);

    private UpdateAndSpecificDeleteOps<T> updateAndSpecificDeleteOps = new UpdateAndSpecificDeleteOps<>(this);

    private GetOps<T> getOps = new GetOps<>(this);

    public SaveOps<T> opsForSave() {
        return saveOps;
    }

    public UpdateOps<T> opsForUpdate() {
        return updateOps;
    }

    public UpdateAndSpecificDeleteOps<T> opsForUpdateAndSpecificDelete() {
        return updateAndSpecificDeleteOps;
    }

    public GetOps<T> opsForGet() {
        return getOps;
    }

    public DataCacheTemplate<T> withMasterSaveOperation(Consumer<T> masterSaveOperation) {
        this.masterSaveOperation = masterSaveOperation;
        return this;
    }

    public DataCacheTemplate<T> withSecondarySaveOperation(Consumer<T> secondarySaveOperation) {
        this.secondarySaveOperation = secondarySaveOperation;
        return this;
    }

    public DataCacheTemplate<T> withMasterUpdateOperation(Consumer<T> masterUpdateOperation) {
        this.masterUpdateOperation = masterUpdateOperation;
        return this;
    }

    public DataCacheTemplate<T> withSecondaryUpdateOperation(Consumer<T> secondaryUpdateOperation) {
        this.secondaryUpdateOperation = secondaryUpdateOperation;
        return this;
    }

    public DataCacheTemplate<T> withSecondaryDeleteOperation(Consumer<T> secondaryDeleteOperation) {
        this.secondaryDeleteOperation = secondaryDeleteOperation;
        return this;
    }

    public DataCacheTemplate<T> withSecondarySpecificDeleteOperation(Runnable secondarySpecificDeleteOperation) {
        this.secondarySpecificDeleteOperation = secondarySpecificDeleteOperation;
        return this;
    }

    public DataCacheTemplate<T> withMasterGetOperation(Supplier<T> masterGetOperation) {
        this.masterGetOperation = masterGetOperation;
        return this;
    }

    public DataCacheTemplate<T> withSecondaryGetOperation(Supplier<T> secondaryGetOperation) {
        this.secondaryGetOperation = secondaryGetOperation;
        return this;
    }

    public static final class SaveOps<T> {

        private DataCacheTemplate<T> template;

        private SaveOps(DataCacheTemplate<T> template) {
            this.template = template;
        }

        public void save(T data) {
            Objects.requireNonNull(template.masterSaveOperation, "master save operation is null");
            Objects.requireNonNull(template.secondarySaveOperation, "secondary save operation is null");
            try {
                template.masterSaveOperation.accept(data);
                template.secondarySaveOperation.accept(data);
            } catch (Exception e) {
                throw new DataCacheOperatorException("save master data and save secondary data fail!", e);
            }
        }
    }

    public static final class UpdateOps<T> {

        private DataCacheTemplate<T> template;

        private UpdateOps(DataCacheTemplate<T> template) {
            this.template = template;
        }

        public void updateAndUpdate(T data) {
            Objects.requireNonNull(template.masterUpdateOperation, "master update operation is null");
            Objects.requireNonNull(template.secondaryUpdateOperation, "secondary update operation is null");
            try {
                template.masterUpdateOperation.accept(data);
                template.secondaryUpdateOperation.accept(data);
            } catch (Exception e) {
                throw new DataCacheOperatorException("update master data and update secondary data fail!", e);
            }
        }

        public void updateAndDelete(T data) {
            Objects.requireNonNull(template.masterUpdateOperation, "master update operation is null");
            Objects.requireNonNull(template.secondaryDeleteOperation, "secondary delete operation is null");
            try {
                template.masterUpdateOperation.accept(data);
                template.secondaryDeleteOperation.accept(data);
            } catch (Exception e) {
                throw new DataCacheOperatorException("update master data and delete secondary data fail!", e);
            }
        }

        public void updateAndDoubleDelete(T data) {
            Objects.requireNonNull(template.masterUpdateOperation, "master update operation is null");
            Objects.requireNonNull(template.secondaryDeleteOperation, "secondary delete operation is null");
            try {
                template.secondaryDeleteOperation.accept(data);
                template.masterUpdateOperation.accept(data);
                template.secondaryDeleteOperation.accept(data);
            } catch (Exception e) {
                throw new DataCacheOperatorException("update master data and double delete secondary data fail!", e);
            }
        }
    }

    public static final class UpdateAndSpecificDeleteOps<T> {

        private DataCacheTemplate<T> template;

        private UpdateAndSpecificDeleteOps(DataCacheTemplate<T> template) {
            this.template = template;
        }

        public void updateAndDelete(T data) {
            Objects.requireNonNull(template.masterUpdateOperation, "master update operation is null");
            Objects.requireNonNull(template.secondarySpecificDeleteOperation, "secondary specific delete operation is null");
            try {
                template.masterUpdateOperation.accept(data);
                template.secondarySpecificDeleteOperation.run();
            } catch (Exception e) {
                throw new DataCacheOperatorException("update master data and delete secondary data fail!", e);
            }
        }

        public void updateAndDoubleDelete(T data) {
            Objects.requireNonNull(template.masterUpdateOperation, "master update operation is null");
            Objects.requireNonNull(template.secondarySpecificDeleteOperation, "secondary delete operation is null");
            try {
                template.secondarySpecificDeleteOperation.run();
                template.masterUpdateOperation.accept(data);
                template.secondarySpecificDeleteOperation.run();
            } catch (Exception e) {
                throw new DataCacheOperatorException("update master data and double delete secondary data fail!", e);
            }
        }
    }

    public static final class GetOps<T> {

        private DataCacheTemplate<T> template;

        private GetOps(DataCacheTemplate<T> template) {
            this.template = template;
        }

        public Optional<T> get() {
            Objects.requireNonNull(template.masterGetOperation, "master get operation is null");
            Objects.requireNonNull(template.secondaryGetOperation, "secondary get operation is null");
            Objects.requireNonNull(template.secondarySaveOperation, "secondary save operation is null");
            try {
                T data = template.secondaryGetOperation.get();
                if (Objects.isNull(data)) {
                    data = template.masterGetOperation.get();
                    if (Objects.nonNull(data)) {
                        template.secondarySaveOperation.accept(data);
                    }
                }
                return Optional.ofNullable(data);
            } catch (Exception e) {
                throw new DataCacheOperatorException("get data fail!", e);
            }
        }
    }

    public static class DataCacheOperatorException extends RuntimeException {
        DataCacheOperatorException(String message, Throwable e) {
            super(message, e);
        }
    }
}
