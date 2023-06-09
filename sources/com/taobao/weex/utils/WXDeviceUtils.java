package com.taobao.weex.utils;

import android.content.Context;
import android.os.Build;

public class WXDeviceUtils {
    public static boolean isAutoResize(Context context) {
        if (context == null) {
            return false;
        }
        return isMateX(context) || isGalaxyFold(context);
    }

    public static boolean isMateX(Context context) {
        return "HUAWEI".equalsIgnoreCase(Build.BRAND) && ("unknownRLI".equalsIgnoreCase(Build.DEVICE) || "HWTAH".equalsIgnoreCase(Build.DEVICE));
    }

    public static boolean isGalaxyFold(Context context) {
        return "samsung".equalsIgnoreCase(Build.BRAND) && "SM-F9000".equalsIgnoreCase(Build.MODEL);
    }
}
