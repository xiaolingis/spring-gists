package com.bz.gists.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created on 2019/7/4
 *
 * @author zhongyongbin
 */
public final class RandomKeyUtil {

    private static final String[] CHARS = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a",
            "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
            "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
            "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private static final Long LEFT = 63176045467600L;

    private static final Long RIGHT = 218340105584895L;

    private RandomKeyUtil() {
    }

    /**
     * 随机生成密钥，由数字，小写字母，大写字母构成
     *
     * @param length 密钥长度，最大不超过 1024
     * @return 随机密钥
     */
    public static String getRandomKey(int length) {
        if (length > 1024) {
            throw new IllegalArgumentException("length too large, the max large is 1024");
        }

        StringBuilder key = new StringBuilder();
        while (key.length() < length) {
            long count = ThreadLocalRandom.current().nextLong(LEFT, RIGHT);
            while (count > 0 && key.length() + 1 <= length) {
                long mod = count % 62L;
                count /= 62L;
                key.append(CHARS[(int) mod]);
            }
        }

        return key.toString();
    }
}
