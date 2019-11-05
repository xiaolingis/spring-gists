package com.bz.gists.util;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Created on 2019/7/3
 *
 * @author zhongyongbin
 */
public final class AssertUtil {

    private AssertUtil() {
    }

    public static <X extends Throwable> void requireTrue(boolean bool, Supplier<X> supplier) throws X {
        if (!bool) {
            throw supplier.get();
        }
    }

    public static <X extends Throwable> void requireNonNull(Object object, Supplier<X> supplier) throws X {
        if (Objects.isNull(object)) {
            throw supplier.get();
        }
    }

    public static <X extends Throwable> void requireNumberInRange(int number, int leftOpenInterval, int rightOpenInterval, Supplier<X> supplier) throws X {
        if (!(number > leftOpenInterval && number < rightOpenInterval)) {
            throw supplier.get();
        }
    }

    public static <X extends Throwable> void requireNumberGtRange(int number, int leftOpenInterval, Supplier<X> supplier) throws X {
        if (!(number > leftOpenInterval)) {
            throw supplier.get();
        }
    }

    public static <X extends Throwable> void requireNumberLtRange(int number, int rightOpenInterval, Supplier<X> supplier) throws X {
        if (!(number < rightOpenInterval)) {
            throw supplier.get();
        }
    }

    public static <X extends Throwable> void requireNumberInRange(long number, long leftOpenInterval, long rightOpenInterval, Supplier<X> supplier) throws X {
        if (!(number > leftOpenInterval && number < rightOpenInterval)) {
            throw supplier.get();
        }
    }

    public static <X extends Throwable> void requireNumberGtRange(long number, long leftOpenInterval, Supplier<X> supplier) throws X {
        if (!(number > leftOpenInterval)) {
            throw supplier.get();
        }
    }

    public static <X extends Throwable> void requireNumberLtRange(long number, long rightOpenInterval, Supplier<X> supplier) throws X {
        if (!(number < rightOpenInterval)) {
            throw supplier.get();
        }
    }

    public static <X extends Throwable> void requireNotBlank(String string, Supplier<X> supplier) throws X {
        if (!(Objects.nonNull(string) && string.length() > 0)) {
            throw supplier.get();
        }
    }
}
