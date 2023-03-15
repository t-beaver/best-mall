package io.dcloud.sdk.core.util;

import android.content.Context;

public class ScreenUtil {
    public static int dh(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int dw(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }
}
