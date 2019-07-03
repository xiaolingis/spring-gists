package com.bz.gists.util;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
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
     * 保存数据操作。在确认主数据保存成功后才会保存到备数据源
     *
     * @param data                   数据
     * @param masterSaveOperation    主数据源的保存操作
     * @param secondarySaveOperation 备数据源的保存操作
     * @param <T>                    数据类型
     */
    public static <T> void opsForSave(T data, Function<T, Boolean> masterSaveOperation, Consumer<T> secondarySaveOperation) {
        new SaveOps<T>()
                .withMasterSaveOperation(masterSaveOperation)
                .withSecondarySaveOperation(secondarySaveOperation)
                .save(data);
    }

    /**
     * 更新数据，采用先更后删策略
     *
     * @param data                     数据
     * @param masterUpdateOperation    主数据源的更新操作
     * @param secondaryDeleteOperation 备数据源的删除操作
     * @param <T>                      数据类型
     */
    public static <T> void opsForUpdate(T data, Consumer<T> masterUpdateOperation, Consumer<T> secondaryDeleteOperation) {
        new UpdateOps<T>()
                .withMasterUpdateOperation(masterUpdateOperation)
                .withSecondaryDeleteOperation(secondaryDeleteOperation)
                .updateAndDelete(data);
    }

    /**
     * 更新数据，采用先更后删策略
     *
     * @param data                             数据
     * @param masterUpdateOperation            主数据源的更新操作
     * @param secondarySpecificDeleteOperation 备数据源的删除操作
     * @param <T>                              数据类型
     */
    public static <T> void opsForUpdate(T data, Consumer<T> masterUpdateOperation, Runnable secondarySpecificDeleteOperation) {
        new UpdateOps<T>()
                .withMasterUpdateOperation(masterUpdateOperation)
                .withSecondarySpecificDeleteOperation(secondarySpecificDeleteOperation)
                .updateAndSpecificDelete(data);
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

    static final class SaveOps<T> {
        private Function<T, Boolean> masterSaveOperation;

        private Consumer<T> secondarySaveOperation;

        private SaveOps() {
        }

        void save(T data) {
            Objects.requireNonNull(masterSaveOperation, "master save operation is null");
            Objects.requireNonNull(secondarySaveOperation, "secondary save operation is null");
            try {
                if (masterSaveOperation.apply(data)) {
                    secondarySaveOperation.accept(data);
                }
            } catch (Exception e) {
                throw new DataCacheOperatorException("save master data and save secondary data fail!", e);
            }
        }

        SaveOps<T> withMasterSaveOperation(Function<T, Boolean> masterSaveOperation) {
            this.masterSaveOperation = masterSaveOperation;
            return this;
        }

        SaveOps<T> withSecondarySaveOperation(Consumer<T> secondarySaveOperation) {
            this.secondarySaveOperation = secondarySaveOperation;
            return this;
        }
    }

    static final class UpdateOps<T> {
        private Consumer<T> masterUpdateOperation;

        private Consumer<T> secondaryDeleteOperation;

        private Runnable secondarySpecificDeleteOperation;

        private UpdateOps() {
        }

        void updateAndSpecificDelete(T data) {
            Objects.requireNonNull(masterUpdateOperation, "master update operation is null");
            Objects.requireNonNull(secondarySpecificDeleteOperation, "secondary delete operation is null");
            try {
                masterUpdateOperation.accept(data);
                secondarySpecificDeleteOperation.run();
            } catch (Exception e) {
                throw new DataCacheOperatorException("update master data and delete secondary data fail!", e);
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

        UpdateOps<T> withMasterUpdateOperation(Consumer<T> masterUpdateOperation) {
            this.masterUpdateOperation = masterUpdateOperation;
            return this;
        }

        UpdateOps<T> withSecondaryDeleteOperation(Consumer<T> secondaryDeleteOperation) {
            this.secondaryDeleteOperation = secondaryDeleteOperation;
            return this;
        }

        UpdateOps<T> withSecondarySpecificDeleteOperation(Runnable secondaryDeleteOperation) {
            this.secondarySpecificDeleteOperation = secondaryDeleteOperation;
            return this;
        }
    }

    static final class GetOps<T> {

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
