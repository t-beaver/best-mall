package io.dcloud.sdk.core.util;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;
import java.lang.reflect.Method;
import java.util.List;

public class ProcessUtil {
    private static String a;

    public static String getCurrentProcessName(Context context) {
        if (!TextUtils.isEmpty(a)) {
            return a;
        }
        String currentProcessNameByApplication = getCurrentProcessNameByApplication();
        a = currentProcessNameByApplication;
        if (!TextUtils.isEmpty(currentProcessNameByApplication)) {
            return a;
        }
        String currentProcessNameByActivityThread = getCurrentProcessNameByActivityThread();
        a = currentProcessNameByActivityThread;
        if (!TextUtils.isEmpty(currentProcessNameByActivityThread)) {
            return a;
        }
        String currentProcessNameByActivityManager = getCurrentProcessNameByActivityManager(context);
        a = currentProcessNameByActivityManager;
        return currentProcessNameByActivityManager;
    }

    public static String getCurrentProcessNameByActivityManager(Context context) {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses;
        if (context == null) {
            return null;
        }
        int myPid = Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        if (!(activityManager == null || (runningAppProcesses = activityManager.getRunningAppProcesses()) == null)) {
            for (ActivityManager.RunningAppProcessInfo next : runningAppProcesses) {
                if (next.pid == myPid) {
                    return next.processName;
                }
            }
        }
        return null;
    }

    public static String getCurrentProcessNameByActivityThread() {
        try {
            Method declaredMethod = Class.forName("android.app.ActivityThread", false, Application.class.getClassLoader()).getDeclaredMethod("currentProcessName", new Class[0]);
            declaredMethod.setAccessible(true);
            Object invoke = declaredMethod.invoke((Object) null, new Object[0]);
            if (invoke instanceof String) {
                return (String) invoke;
            }
            return null;
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public static String getCurrentProcessNameByApplication() {
        if (Build.VERSION.SDK_INT >= 28) {
            return Application.getProcessName();
        }
        return null;
    }

    public static boolean isMainProcess(Context context) {
        if (context == null) {
            return true;
        }
        try {
            return context.getPackageName().equals(getCurrentProcessName(context));
        } catch (Exception unused) {
            return false;
        }
    }
}
