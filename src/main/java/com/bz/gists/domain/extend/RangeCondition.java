package com.bz.gists.domain.extend;

import org.apache.commons.lang3.StringUtils;

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

    /**
     * 范围条件助手类。根据值域规范进行字符串与对象的转换。inf 表示无限。
     */
    public static final class Helper {

        private static final String NEGATIVE_INFINITY = "-inf";

        private static final String INFINITY = "inf";

        private static final String RANGE_SPLIT = ",";

        private static final String LEFT_INCLUDE = "[";

        private static final String LEFT_EXCLUDE = "(";

        private static final String RIGHT_INCLUDE = "]";

        private static final String RIGHT_EXCLUDE = ")";

        private Helper() {
        }

        /**
         * 是否符合给定的范围条件
         */
        public static boolean match(RangeCondition condition, long number) {
            if (condition.getLeft() < number || (condition.isEqLeft() && number == condition.getLeft())) {
                return condition.getRight() > number || (condition.isEqRight() && number == condition.getRight());
            }

            return false;
        }

        /**
         * 根据值域规范将值域字符串转化为条件对象。
         */
        public static RangeCondition stringToCondition(String strRange) {
            if (!StringUtils.startsWith(strRange, LEFT_EXCLUDE) && !StringUtils.startsWith(strRange, LEFT_INCLUDE)) {
                throw new IllegalArgumentException("parameter did not qualify with range format");
            }

            if (!StringUtils.endsWith(strRange, RIGHT_INCLUDE) && !StringUtils.endsWith(strRange, RIGHT_EXCLUDE)) {
                throw new IllegalArgumentException("parameter did not qualify with range format");
            }

            if (!StringUtils.contains(strRange, RANGE_SPLIT)) {
                throw new IllegalArgumentException("parameter did not qualify with range format");
            }

            RangeCondition rangeCondition = new RangeCondition();
            String start = strRange.substring(0, 1);
            String numbersData = strRange.substring(1, strRange.length() - 1);
            String end = strRange.substring(strRange.length() - 1);
            if (StringUtils.equals(start, LEFT_INCLUDE)) {
                rangeCondition.setEqLeft(true);
            }
            if (StringUtils.equals(end, RIGHT_INCLUDE)) {
                rangeCondition.setEqRight(true);
            }

            String[] numbers = numbersData.split(RANGE_SPLIT);
            if (numbers.length != 2) {
                throw new IllegalArgumentException("parameter did not qualify with range format");
            }

            if (StringUtils.equals(numbers[0], NEGATIVE_INFINITY)) {
                rangeCondition.setLeft(Long.MIN_VALUE);
            } else if (StringUtils.equals(numbers[0], INFINITY)) {
                rangeCondition.setLeft(Long.MAX_VALUE);
            } else {
                rangeCondition.setLeft(Long.parseLong(numbers[0]));
            }

            if (StringUtils.equals(numbers[1], NEGATIVE_INFINITY)) {
                rangeCondition.setRight(Long.MIN_VALUE);
            } else if (StringUtils.equals(numbers[1], INFINITY)) {
                rangeCondition.setRight(Long.MAX_VALUE);
            } else {
                rangeCondition.setRight(Long.parseLong(numbers[1]));
            }

            return rangeCondition;
        }

        /**
         * 将条件对象转化为值域字符串
         */
        public static String conditionToString(RangeCondition condition) {
            StringBuilder range = new StringBuilder();
            if (condition.isEqLeft()) {
                range.append(LEFT_INCLUDE);
            } else {
                range.append(LEFT_EXCLUDE);
            }

            if (condition.getLeft() == Long.MIN_VALUE) {
                range.append(NEGATIVE_INFINITY);
            } else {
                range.append(condition.getLeft());
            }

            range.append(RANGE_SPLIT);

            if (condition.getRight() == Long.MAX_VALUE) {
                range.append(INFINITY);
            } else {
                range.append(condition.getRight());
            }

            if (condition.isEqRight()) {
                range.append(RIGHT_INCLUDE);
            } else {
                range.append(RIGHT_EXCLUDE);
            }

            return range.toString();
        }
    }
}
