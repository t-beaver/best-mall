package com.taobao.weex.utils;

import java.util.HashMap;

public class WXDataStructureUtil {
    private static final int MAX_POWER_OF_TWO = 1073741824;

    public static <K, V> HashMap<K, V> newHashMapWithExpectedSize(int i) {
        return new HashMap<>(capacity(i));
    }

    private static int capacity(int i) {
        if (i < 3) {
            checkNonnegative(i, "expectedSize");
            return i + 1;
        } else if (i < MAX_POWER_OF_TWO) {
            return (int) ((((float) i) / 0.75f) + 1.0f);
        } else {
            return Integer.MAX_VALUE;
        }
    }

    private static int checkNonnegative(int i, String str) {
        if (i >= 0) {
            return i;
        }
        throw new IllegalArgumentException(str + " cannot be negative but was: " + i);
    }
}
