package com.bz.gists.helper;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.bz.gists.domain.extend.WeightObject;
import com.bz.gists.domain.extend.WeightRoundData;

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
 * 加权轮询随机算法实现。
 *
 * @author zhongyongbin
 */
public final class WeightRoundRandomHelper {

    private static volatile WeightRoundRandomHelper instance;

    /**
     * 用户缓存算法用到的中间对象
     */
    private final Cache<String, Object> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(15))
            .build();

    private WeightRoundRandomHelper() {
    }

    public static WeightRoundRandomHelper getInstance() {
        if (Objects.isNull(instance)) {
            synchronized (WeightRoundRandomHelper.class) {
                if (Objects.isNull(instance)) {
                    instance = new WeightRoundRandomHelper();
                }
            }
        }

        return instance;
    }

    @SuppressWarnings("unchecked")
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

        try {
            TreeMap<Integer, List<T>> data = (TreeMap<Integer, List<T>>) cache.get(weightRoundData.getUuid(), () -> {
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
            int score = ThreadLocalRandom.current().nextInt(1, weightRoundData.getTotalWeight() + 1);
            List<T> result = score > maxWeight ? data.lowerEntry(score).getValue() : data.ceilingEntry(score).getValue();

            return Optional.of(result.get(ThreadLocalRandom.current().nextInt(0, result.size())));
        } catch (ExecutionException e) {
            ReflectionUtils.rethrowRuntimeException(e);
            return Optional.empty();
        }
    }
}
