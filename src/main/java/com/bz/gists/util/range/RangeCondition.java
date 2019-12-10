package com.bz.gists.util.range;

/**
 * Created on 2019/8/14
 *
 * 范围条件
 *
 * @author zhongyongbin
 */
public class RangeCondition {

    private long left;

    private long right;

    private boolean eqLeft;

    private boolean eqRight;

    public long getLeft() {
        return left;
    }

    public void setLeft(long left) {
        this.left = left;
    }

    public long getRight() {
        return right;
    }

    public void setRight(long right) {
        this.right = right;
    }

    public boolean isEqLeft() {
        return eqLeft;
    }

    public void setEqLeft(boolean eqLeft) {
        this.eqLeft = eqLeft;
    }

    public boolean isEqRight() {
        return eqRight;
    }

    public void setEqRight(boolean eqRight) {
        this.eqRight = eqRight;
    }
}
