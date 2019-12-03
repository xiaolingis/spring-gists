package com.bz.gists.domain.extend;

import java.util.List;
import java.util.UUID;

/**
 * Created on 2019/12/2
 *
 * 加权轮询数据
 *
 * @author zhongyongbin
 */
public class WeightRoundData<T> {

    /**
     * 用于标识加权轮询数据对象
     */
    private final String uuid = UUID.randomUUID().toString();

    /**
     * 总权重
     */
    private int totalWeight;

    /**
     * 权重对象集合
     */
    private List<WeightObject<T>> weightObjects;

    public String getUuid() {
        return uuid;
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(int totalWeight) {
        this.totalWeight = totalWeight;
    }

    public List<WeightObject<T>> getWeightObjects() {
        return weightObjects;
    }

    public void setWeightObjects(List<WeightObject<T>> weightObjects) {
        this.weightObjects = weightObjects;
    }
}
