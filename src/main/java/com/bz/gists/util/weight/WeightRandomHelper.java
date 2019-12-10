package com.bz.gists.util.weight;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created on 2019/12/2
 *
 * 加权随机算法实现。
 *
 * @author zhongyongbin
 */
public final class WeightRandomHelper {

    private static volatile WeightRandomHelper instance;

    /**
     * 用户缓存算法用到的中间对象
     */
    private final Cache<String, Object> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(15))
            .build();

    private WeightRandomHelper() {
    }

    public static WeightRandomHelper getInstance() {
        if (Objects.isNull(instance)) {
            synchronized (WeightRandomHelper.class) {
                if (Objects.isNull(instance)) {
                    instance = new WeightRandomHelper();
                }
            }
        }

        return instance;
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getObject(WeightObjectDataSet<T> weightObjectDataSet) {
        if (Objects.isNull(weightObjectDataSet)) {
            return Optional.empty();
        }

        List<WeightObject<T>> weightObjects = weightObjectDataSet.getWeightObjects();
        if (CollectionUtils.isEmpty(weightObjects)) {
            return Optional.empty();
        }

        if (weightObjects.size() == 1) {
            return Optional.of(weightObjects.get(0).getObject());
        }

        try {
            TreeMap<Integer, List<T>> data = (TreeMap<Integer, List<T>>) cache.get(weightObjectDataSet.getUuid(), () -> {
                TreeMap<Integer, List<T>> newData = Maps.newTreeMap();
                for (WeightObject<T> weightObject : weightObjects) {
                    newData.computeIfPresent(weightObject.getWeight(), (k, v) -> {
                        v.add(weightObject.getObject());
                        return v;
                    });
                    newData.putIfAbsent(weightObject.getWeight(), Lists.newArrayList(weightObject.getObject()));
                }

                return newData;
            });

            int maxWeight = data.lastKey();
            int score = ThreadLocalRandom.current().nextInt(1, weightObjectDataSet.getTotalWeight() + 1);
            List<T> result = score > maxWeight ? data.get(maxWeight) : data.ceilingEntry(score).getValue();

            return Optional.of(result.get(ThreadLocalRandom.current().nextInt(0, result.size())));
        } catch (ExecutionException e) {
            ReflectionUtils.rethrowRuntimeException(e);
            return Optional.empty();
        }
    }
}
