package io.dcloud.common.util;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import java.lang.reflect.Method;

public class ProcessUtil {
    private static String currentProcessName;

    public static String getCurrentProcessName(Context context) {
        if (!TextUtils.isEmpty(currentProcessName)) {
            return currentProcessName;
        }
        String currentProcessNameByApplication = getCurrentProcessNameByApplication();
        currentProcessName = currentProcessNameByApplication;
        if (!TextUtils.isEmpty(currentProcessNameByApplication)) {
            return currentProcessName;
        }
        String currentProcessNameByActivityThread = getCurrentProcessNameByActivityThread();
        currentProcessName = currentProcessNameByActivityThread;
        if (!TextUtils.isEmpty(currentProcessNameByActivityThread)) {
            return currentProcessName;
        }
        String currentProcessNameByCmdline = getCurrentProcessNameByCmdline();
        currentProcessName = currentProcessNameByCmdline;
        if (!TextUtils.isEmpty(currentProcessNameByCmdline)) {
            return currentProcessName;
        }
        String packageName = context.getPackageName();
        currentProcessName = packageName;
        return packageName;
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

    /* JADX WARNING: Removed duplicated region for block: B:11:0x001f A[Catch:{ all -> 0x0033 }] */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x002f A[SYNTHETIC, Splitter:B:17:0x002f] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x003c A[SYNTHETIC, Splitter:B:25:0x003c] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getCurrentProcessNameByCmdline() {
        /*
            java.lang.String r0 = "/proc/self/cmdline"
            r1 = 0
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch:{ all -> 0x0035 }
            r2.<init>(r0)     // Catch:{ all -> 0x0035 }
            r0 = 256(0x100, float:3.59E-43)
            byte[] r3 = new byte[r0]     // Catch:{ all -> 0x0033 }
            r4 = 0
            r5 = 0
        L_0x000e:
            int r6 = r2.read()     // Catch:{ all -> 0x0033 }
            if (r6 <= 0) goto L_0x001d
            if (r5 >= r0) goto L_0x001d
            int r7 = r5 + 1
            byte r6 = (byte) r6     // Catch:{ all -> 0x0033 }
            r3[r5] = r6     // Catch:{ all -> 0x0033 }
            r5 = r7
            goto L_0x000e
        L_0x001d:
            if (r5 <= 0) goto L_0x002f
            java.lang.String r0 = new java.lang.String     // Catch:{ all -> 0x0033 }
            java.lang.String r6 = "UTF-8"
            r0.<init>(r3, r4, r5, r6)     // Catch:{ all -> 0x0033 }
            r2.close()     // Catch:{ IOException -> 0x002a }
            goto L_0x002e
        L_0x002a:
            r1 = move-exception
            r1.printStackTrace()
        L_0x002e:
            return r0
        L_0x002f:
            r2.close()     // Catch:{ IOException -> 0x0040 }
            goto L_0x0044
        L_0x0033:
            r0 = move-exception
            goto L_0x0037
        L_0x0035:
            r0 = move-exception
            r2 = r1
        L_0x0037:
            r0.printStackTrace()     // Catch:{ all -> 0x0045 }
            if (r2 == 0) goto L_0x0044
            r2.close()     // Catch:{ IOException -> 0x0040 }
            goto L_0x0044
        L_0x0040:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0044:
            return r1
        L_0x0045:
            r0 = move-exception
            if (r2 == 0) goto L_0x0050
            r2.close()     // Catch:{ IOException -> 0x004c }
            goto L_0x0050
        L_0x004c:
            r1 = move-exception
            r1.printStackTrace()
        L_0x0050:
            goto L_0x0052
        L_0x0051:
            throw r0
        L_0x0052:
            goto L_0x0051
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.ProcessUtil.getCurrentProcessNameByCmdline():java.lang.String");
    }
}
