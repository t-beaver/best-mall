package com.taobao.weex.dom;

public class CSSConstants {
    public static final float UNDEFINED = Float.NaN;

    public static boolean isUndefined(float f) {
        return Float.compare(f, Float.NaN) == 0;
    }
}
