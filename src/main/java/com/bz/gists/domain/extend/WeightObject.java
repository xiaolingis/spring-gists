package com.bz.gists.domain.extend;

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
     * 权重，赋值后不变
     */
    private int weight;

    /**
     * 当前权重，用于需要更改权重的算法
     */
    private AtomicInteger currentWeight;

    /**
     * 封装对象
     */
    private final T object;

    public WeightObject(int weight, T object) {
        this.weight = weight;
        this.object = object;
        this.currentWeight = new AtomicInteger(weight);
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
