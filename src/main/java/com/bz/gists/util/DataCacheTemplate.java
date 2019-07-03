package com.bz.gists.util;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created on 2019/7/2
 *
 * 数据缓存模板操作。一般分为两个数据源操作，一个是主数据源，一个是用于存储缓存数据的备数据源。
 *
 * @author zhongyongbin
 */
public final class DataCacheTemplate {

    private DataCacheTemplate() {
    }

    /**
     * 保存数据操作
     *
     * @param data                   数据
     * @param masterSaveOperation    主数据源的保存操作
     * @param secondarySaveOperation 备数据源的保存操作
     * @param <T>                    数据类型
     */
    public static <T> void opsForSave(T data, Consumer<T> masterSaveOperation, Consumer<T> secondarySaveOperation) {
        new SaveOps<T>()
                .withMasterSaveOperation(masterSaveOperation)
                .withSecondarySaveOperation(secondarySaveOperation)
                .save(data);
    }

    /**
     * 更新数据，同时也对备数据源进行更新操作
     *
     * @param data                     数据
     * @param masterUpdateOperation    主数据源的更新操作
     * @param secondaryUpdateOperation 备数据源的更新操作
     * @param <T>                      数据类型
     */
    public static <T> void opsForUpdateAndUpdate(T data, Consumer<T> masterUpdateOperation, Consumer<T> secondaryUpdateOperation) {
        new UpdateOps<T>()
                .withMasterUpdateOperation(masterUpdateOperation)
                .withSecondaryUpdateOperation(secondaryUpdateOperation)
                .updateAndUpdate(data);
    }

    /**
     * 更新数据，更新后删除备数据源的数据
     *
     * @param data                     数据
     * @param masterUpdateOperation    主数据源的更新操作
     * @param secondaryDeleteOperation 备数据源的删除操作
     * @param <T>                      数据类型
     */
    public static <T> void opsForUpdateAndDelete(T data, Consumer<T> masterUpdateOperation, Consumer<T> secondaryDeleteOperation) {
        new UpdateOps<T>()
                .withMasterUpdateOperation(masterUpdateOperation)
                .withSecondaryDeleteOperation(secondaryDeleteOperation)
                .updateAndDelete(data);
    }

    /**
     * 更新数据，对备数据源进行双删操作
     *
     * @param data                     数据
     * @param masterUpdateOperation    主数据源的更新操作
     * @param secondaryDeleteOperation 备数据源的删除操作
     * @param <T>                      数据类型
     */
    public static <T> void opsForUpdateAndDoubleDelete(T data, Consumer<T> masterUpdateOperation, Consumer<T> secondaryDeleteOperation) {
        new UpdateOps<T>()
                .withMasterUpdateOperation(masterUpdateOperation)
                .withSecondaryDeleteOperation(secondaryDeleteOperation)
                .updateAndDoubleDelete(data);
    }

    /**
     * 更新数据，更新后删除备数据源的数据
     *
     * @param data                             数据
     * @param masterUpdateOperation            主数据源的更新操作
     * @param secondarySpecificDeleteOperation 给定详细的备数据源删除操作
     * @param <T>                              数据类型
     */
    public static <T> void opsForUpdateAndDelete(T data, Consumer<T> masterUpdateOperation, Runnable secondarySpecificDeleteOperation) {
        new UpdateAndSpecificDeleteOps<T>()
                .withMasterUpdateOperation(masterUpdateOperation)
                .withSecondaryDeleteOperation(secondarySpecificDeleteOperation)
                .updateAndDelete(data);
    }

    /**
     * 更新数据，对备数据源进行双删操作
     *
     * @param data                             数据
     * @param masterUpdateOperation            主数据源的更新操作
     * @param secondarySpecificDeleteOperation 给定详细的备数据源删除操作
     * @param <T>                              数据类型
     */
    public static <T> void opsForUpdateAndDoubleDelete(T data, Consumer<T> masterUpdateOperation, Runnable secondarySpecificDeleteOperation) {
        new UpdateAndSpecificDeleteOps<T>()
                .withMasterUpdateOperation(masterUpdateOperation)
                .withSecondaryDeleteOperation(secondarySpecificDeleteOperation)
                .updateAndDoubleDelete(data);
    }

    /**
     * 获取数据。先从备数据源中获取数据，获取不到的话再从主数据源中获取，之后会根据是否给定备数据源的保存操作来确定是否将数据保存到备数据源中
     *
     * @param secondaryGetOperation  从备数据源中获取数据操作
     * @param masterGetOperation     从主数据源中获取数据操作
     * @param secondarySaveOperation 备数据源保存操作
     * @param <T>                    数据类型
     * @return 获取的数据，如果主数据源中也没有对应的数据，将返回空
     */
    public static <T> T opsForGet(Supplier<T> secondaryGetOperation,
                                  Supplier<T> masterGetOperation,
                                  Consumer<T> secondarySaveOperation) {
        return new GetOps<T>()
                .withSecondaryGetOperation(secondaryGetOperation)
                .withMasterGetOperation(masterGetOperation)
                .withSecondarySaveOperation(secondarySaveOperation)
                .get();
    }

    /**
     * 获取数据。先从备数据源中获取数据，获取不到的话再从主数据源中获取
     *
     * @param secondaryGetOperation 从备数据源中获取数据操作
     * @param masterGetOperation    从主数据源中获取数据操作
     * @param <T>                   数据类型
     * @return 获取的数据，如果主数据源中也没有对应的数据，将返回空
     */
    public static <T> T opsForGet(Supplier<T> secondaryGetOperation,
                                  Supplier<T> masterGetOperation
    ) {
        return opsForGet(secondaryGetOperation, masterGetOperation, null);
    }

    public static final class SaveOps<T> {
        private Consumer<T> masterSaveOperation;

        private Consumer<T> secondarySaveOperation;

        private SaveOps() {
        }

        void save(T data) {
            Objects.requireNonNull(masterSaveOperation, "master save operation is null");
            Objects.requireNonNull(secondarySaveOperation, "secondary save operation is null");
            try {
                masterSaveOperation.accept(data);
                secondarySaveOperation.accept(data);
            } catch (Exception e) {
                throw new DataCacheOperatorException("save master data and save secondary data fail!", e);
            }
        }

        SaveOps<T> withMasterSaveOperation(Consumer<T> masterSaveOperation) {
            this.masterSaveOperation = masterSaveOperation;
            return this;
        }

        SaveOps<T> withSecondarySaveOperation(Consumer<T> secondarySaveOperation) {
            this.secondarySaveOperation = secondarySaveOperation;
            return this;
        }
    }

    public static final class UpdateOps<T> {
        private Consumer<T> masterUpdateOperation;

        private Consumer<T> secondaryUpdateOperation;

        private Consumer<T> secondaryDeleteOperation;

        private UpdateOps() {
        }

        void updateAndUpdate(T data) {
            Objects.requireNonNull(masterUpdateOperation, "master update operation is null");
            Objects.requireNonNull(secondaryUpdateOperation, "secondary update operation is null");
            try {
                masterUpdateOperation.accept(data);
                secondaryUpdateOperation.accept(data);
            } catch (Exception e) {
                throw new DataCacheOperatorException("update master data and update secondary data fail!", e);
            }
        }

        void updateAndDelete(T data) {
            Objects.requireNonNull(masterUpdateOperation, "master update operation is null");
            Objects.requireNonNull(secondaryDeleteOperation, "secondary delete operation is null");
            try {
                masterUpdateOperation.accept(data);
                secondaryDeleteOperation.accept(data);
            } catch (Exception e) {
                throw new DataCacheOperatorException("update master data and delete secondary data fail!", e);
            }
        }

        void updateAndDoubleDelete(T data) {
            Objects.requireNonNull(masterUpdateOperation, "master update operation is null");
            Objects.requireNonNull(secondaryDeleteOperation, "secondary delete operation is null");
            try {
                secondaryDeleteOperation.accept(data);
                masterUpdateOperation.accept(data);
                secondaryDeleteOperation.accept(data);
            } catch (Exception e) {
                throw new DataCacheOperatorException("update master data and double delete secondary data fail!", e);
            }
        }

        UpdateOps<T> withMasterUpdateOperation(Consumer<T> masterUpdateOperation) {
            this.masterUpdateOperation = masterUpdateOperation;
            return this;
        }

        UpdateOps<T> withSecondaryUpdateOperation(Consumer<T> secondaryUpdateOperation) {
            this.secondaryUpdateOperation = secondaryUpdateOperation;
            return this;
        }

        UpdateOps<T> withSecondaryDeleteOperation(Consumer<T> secondaryDeleteOperation) {
            this.secondaryDeleteOperation = secondaryDeleteOperation;
            return this;
        }
    }

    public static final class UpdateAndSpecificDeleteOps<T> {
        private Consumer<T> masterUpdateOperation;

        private Runnable secondaryDeleteOperation;

        void updateAndDelete(T data) {
            Objects.requireNonNull(masterUpdateOperation, "master update operation is null");
            Objects.requireNonNull(secondaryDeleteOperation, "secondary delete operation is null");
            try {
                masterUpdateOperation.accept(data);
                secondaryDeleteOperation.run();
            } catch (Exception e) {
                throw new DataCacheOperatorException("update master data and delete secondary data fail!", e);
            }
        }

        void updateAndDoubleDelete(T data) {
            Objects.requireNonNull(masterUpdateOperation, "master update operation is null");
            Objects.requireNonNull(secondaryDeleteOperation, "secondary delete operation is null");
            try {
                secondaryDeleteOperation.run();
                masterUpdateOperation.accept(data);
                secondaryDeleteOperation.run();
            } catch (Exception e) {
                throw new DataCacheOperatorException("update master data and double delete secondary data fail!", e);
            }
        }

        UpdateAndSpecificDeleteOps<T> withMasterUpdateOperation(Consumer<T> masterUpdateOperation) {
            this.masterUpdateOperation = masterUpdateOperation;
            return this;
        }

        public UpdateAndSpecificDeleteOps<T> withSecondaryDeleteOperation(Runnable secondaryDeleteOperation) {
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

        T get() {
            Objects.requireNonNull(masterGetOperation, "master get operation is null");
            Objects.requireNonNull(secondaryGetOperation, "secondary get operation is null");
            try {
                T data = secondaryGetOperation.get();
                if (Objects.isNull(data)) {
                    data = masterGetOperation.get();
                    if (Objects.nonNull(data) && Objects.nonNull(secondarySaveOperation)) {
                        secondarySaveOperation.accept(data);
                    }
                }
                return data;
            } catch (Exception e) {
                throw new DataCacheOperatorException("get data fail!", e);
            }
        }

        GetOps<T> withMasterGetOperation(Supplier<T> masterGetOperation) {
            this.masterGetOperation = masterGetOperation;
            return this;
        }

        GetOps<T> withSecondaryGetOperation(Supplier<T> secondaryGetOperation) {
            this.secondaryGetOperation = secondaryGetOperation;
            return this;
        }

        GetOps<T> withSecondarySaveOperation(Consumer<T> secondarySaveOperation) {
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
