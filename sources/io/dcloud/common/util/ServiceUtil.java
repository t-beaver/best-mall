package io.dcloud.common.util;

import android.app.ActivityManager;
import android.content.Context;

public class ServiceUtil {
    public static boolean isServiceRunning(Context context, Class<?> cls) {
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) context.getSystemService("activity")).getRunningServices(1024)) {
            if (cls.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
