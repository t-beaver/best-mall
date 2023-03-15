package io.dcloud.sdk.core.util;

import android.os.Handler;
import android.os.Looper;

public class MainHandlerUtil {
    private static volatile Handler a = new Handler(Looper.getMainLooper());

    public static Handler getMainHandler() {
        if (a == null) {
            synchronized (MainHandlerUtil.class) {
                if (a == null) {
                    a = new Handler(Looper.getMainLooper());
                }
            }
        }
        return a;
    }
}
