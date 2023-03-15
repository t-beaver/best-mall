package io.dcloud.common.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

public class RuningAcitvityUtil {
    private static HashMap<String, WeakReference<Activity>> RuningActivitys = new HashMap<>();
    public static boolean isRuningActivity = false;

    public static void StartActivity(Context context, Intent intent) {
        Intent intent2 = new Intent(intent);
        intent2.setAction("android.intent.action.VIEW");
        context.startActivity(intent2);
    }

    public static Activity getActivity(String str) {
        WeakReference weakReference;
        if (!RuningActivitys.containsKey(str) || (weakReference = RuningActivitys.get(str)) == null) {
            return null;
        }
        return (Activity) weakReference.get();
    }

    public static String getAppName(Context context) {
        return ProcessUtil.getCurrentProcessName(context);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0047, code lost:
        r0 = (android.app.Activity) io.dcloud.application.DCLoudApplicationImpl.self().topActiveMap.get(r0.topActivity.getClassName()).get();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.app.Activity getTopRuningActivity(android.app.Activity r3) {
        /*
            java.lang.String r0 = "activity"
            java.lang.Object r0 = r3.getSystemService(r0)
            android.app.ActivityManager r0 = (android.app.ActivityManager) r0
            r1 = 1
            java.util.List r0 = r0.getRunningTasks(r1)
            r1 = 0
            java.lang.Object r0 = r0.get(r1)
            android.app.ActivityManager$RunningTaskInfo r0 = (android.app.ActivityManager.RunningTaskInfo) r0
            java.util.HashMap<java.lang.String, java.lang.ref.WeakReference<android.app.Activity>> r1 = RuningActivitys
            android.content.ComponentName r2 = r0.topActivity
            java.lang.String r2 = r2.getClassName()
            boolean r1 = r1.containsKey(r2)
            if (r1 == 0) goto L_0x0039
            java.util.HashMap<java.lang.String, java.lang.ref.WeakReference<android.app.Activity>> r1 = RuningActivitys
            android.content.ComponentName r0 = r0.topActivity
            java.lang.String r0 = r0.getClassName()
            java.lang.Object r0 = r1.get(r0)
            java.lang.ref.WeakReference r0 = (java.lang.ref.WeakReference) r0
            if (r0 == 0) goto L_0x0062
            java.lang.Object r3 = r0.get()
            android.app.Activity r3 = (android.app.Activity) r3
            return r3
        L_0x0039:
            android.content.ComponentName r1 = r0.topActivity
            java.lang.String r1 = r1.getClassName()
            java.lang.String r2 = "com.g.gysdk.view.ELoginActivity"
            boolean r1 = r2.equals(r1)
            if (r1 == 0) goto L_0x0062
            io.dcloud.application.DCLoudApplicationImpl r1 = io.dcloud.application.DCLoudApplicationImpl.self()
            java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.ref.WeakReference<android.app.Activity>> r1 = r1.topActiveMap
            android.content.ComponentName r0 = r0.topActivity
            java.lang.String r0 = r0.getClassName()
            java.lang.Object r0 = r1.get(r0)
            java.lang.ref.WeakReference r0 = (java.lang.ref.WeakReference) r0
            java.lang.Object r0 = r0.get()
            android.app.Activity r0 = (android.app.Activity) r0
            if (r0 == 0) goto L_0x0062
            return r0
        L_0x0062:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.RuningAcitvityUtil.getTopRuningActivity(android.app.Activity):android.app.Activity");
    }

    public static boolean isRunningProcess(Context context, String str) {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses;
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        if (!(activityManager == null || TextUtils.isEmpty(str) || (runningAppProcesses = activityManager.getRunningAppProcesses()) == null)) {
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                if (TextUtils.equals(runningAppProcessInfo.processName, str)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void putRuningActivity(Activity activity) {
        String className = activity.getComponentName().getClassName();
        if (!RuningActivitys.containsKey(className)) {
            RuningActivitys.put(className, new WeakReference(activity));
        }
    }

    public static void removeRuningActivity(String str) {
        if (RuningActivitys.containsKey(str)) {
            RuningActivitys.remove(str);
        }
    }
}
