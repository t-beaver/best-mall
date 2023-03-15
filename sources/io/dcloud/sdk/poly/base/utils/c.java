package io.dcloud.sdk.poly.base.utils;

import android.content.Context;
import android.os.Build;

public class c {
    public static boolean a(Context context) {
        int i = Build.VERSION.SDK_INT;
        return (i >= 29 && context.getApplicationInfo().targetSdkVersion >= 29) || i >= 30;
    }
}
