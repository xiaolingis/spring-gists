package com.bz.gists.helper;

import com.bz.gists.domain.extend.WeightRoundData;
import com.bz.gists.domain.extend.WeightObject;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created on 2019/12/2
 *
 * WeightRound-Robin 算法实现
 *
 * @author zhongyongbin
 */
public final class WeightRoundRobinHelper {

    private static volatile WeightRoundRobinHelper instance;

    private WeightRoundRobinHelper() {
    }

    public static WeightRoundRobinHelper getInstance() {
        if (Objects.isNull(instance)) {
            synchronized (WeightRoundRobinHelper.class) {
                if (Objects.isNull(instance)) {
                    instance = new WeightRoundRobinHelper();
                }
            }
        }

        return instance;
    }

    public <T> Optional<T> getObject(WeightRoundData<T> weightRoundData) {
        if (Objects.isNull(weightRoundData)) {
            return Optional.empty();
        }

        List<WeightObject<T>> weightObjects = weightRoundData.getWeightObjects();
        if (CollectionUtils.isEmpty(weightObjects)) {
            return Optional.empty();
        }

        if (weightObjects.size() == 1) {
            return Optional.of(weightObjects.get(0).getObject());
        }

        WeightObject<T> result = null;
        int currentMaxWeight = 0;

        for (WeightObject<T> weightObject : weightObjects) {
            int currentWeight = weightObject.getCurrentWeight().addAndGet(weightObject.getWeight());
            if (Objects.isNull(result) || currentWeight > currentMaxWeight) {
                result = weightObject;
                currentMaxWeight = currentWeight;
            }
        }

        if (Objects.isNull(result)) {
            return Optional.empty();
        }

        result.getCurrentWeight().addAndGet(-weightRoundData.getTotalWeight());

        return Optional.of(result.getObject());
    }
}
