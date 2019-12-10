package com.bz.gists.util.weight;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created on 2019/12/2
 *
 * 加权对象
 *
 * @author zhongyongbin
 */
public class WeightObject<T> {

    /**
     * 权重
     */
    private final int weight;

    /**
     * 当前权重，用于需要更改权重的算法
     */
    private final AtomicInteger currentWeight = new AtomicInteger(0);

    /**
     * 封装对象
     */
    private final T object;

    public WeightObject(int weight, T object) {
        this.weight = weight;
        this.object = object;
    }

    public int getWeight() {
        return weight;
    }

    public AtomicInteger getCurrentWeight() {
        return currentWeight;
    }

    public T getObject() {
        return object;
    }

}
