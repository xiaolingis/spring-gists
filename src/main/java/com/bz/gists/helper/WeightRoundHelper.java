package com.bz.gists.helper;

import com.bz.gists.domain.extend.WeightObjectDataSet;
import com.bz.gists.domain.extend.WeightObject;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created on 2019/12/2
 *
 * 集权轮询 WeightRound-Robin 算法实现
 *
 * @author zhongyongbin
 */
public final class WeightRoundHelper {

    private static volatile WeightRoundHelper instance;

    private WeightRoundHelper() {
    }

    public static WeightRoundHelper getInstance() {
        if (Objects.isNull(instance)) {
            synchronized (WeightRoundHelper.class) {
                if (Objects.isNull(instance)) {
                    instance = new WeightRoundHelper();
                }
            }
        }

        return instance;
    }

    public <T> Optional<T> getObject(WeightObjectDataSet<T> weightObjectDataSet) {
        if (Objects.isNull(weightObjectDataSet)) {
            return Optional.empty();
        }

        List<WeightObject<T>> weightObjects = weightObjectDataSet.getWeightObjects()
                .stream()
                .filter(weightObject -> weightObject.getWeight() > 0)
                .collect(Collectors.toList());
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

        result.getCurrentWeight().addAndGet(-weightObjectDataSet.getTotalWeight());

        return Optional.of(result.getObject());
    }
}
