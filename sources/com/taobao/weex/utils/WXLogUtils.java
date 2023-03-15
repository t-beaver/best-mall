package com.taobao.weex.utils;

import android.text.TextUtils;
import android.util.Log;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.performance.WXStateRecord;
import io.dcloud.feature.uniapp.utils.AbsLogLevel;
import io.dcloud.weex.ConsoleLogUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WXLogUtils {
    private static final String CLAZZ_NAME_LOG_UTIL = "com.taobao.weex.devtools.common.LogUtil";
    public static final String WEEX_PERF_TAG = "weex_perf";
    public static final String WEEX_TAG = "weex";
    private static StringBuilder builder = new StringBuilder(50);
    private static HashMap<String, Class> clazzMaps;
    private static boolean isDebug = true;
    private static List<JsLogWatcher> jsLogWatcherList = new ArrayList();
    private static LogWatcher sLogWatcher;

    public interface JsLogWatcher {
        void onJsLog(int i, String str);
    }

    public interface LogWatcher {
        void onLog(String str, String str2, String str3);
    }

    public static void performance(String str, byte[] bArr) {
    }

    static {
        HashMap<String, Class> hashMap = new HashMap<>(2);
        clazzMaps = hashMap;
        hashMap.put(CLAZZ_NAME_LOG_UTIL, loadClass(CLAZZ_NAME_LOG_UTIL));
    }

    public static void setIsDebug(boolean z) {
        isDebug = z;
    }

    private static Class loadClass(String str) {
        try {
            Class<?> cls = Class.forName(str);
            if (cls == null) {
                return cls;
            }
            try {
                clazzMaps.put(str, cls);
                return cls;
            } catch (ClassNotFoundException unused) {
                return cls;
            }
        } catch (ClassNotFoundException unused2) {
            return null;
        }
    }

    public static void renderPerformanceLog(String str, long j) {
        if (!WXEnvironment.isApkDebugable()) {
            WXEnvironment.isPerf();
        }
    }

    private static void log(String str, String str2, AbsLogLevel absLogLevel) {
        if (!TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str) && absLogLevel != null && !TextUtils.isEmpty(absLogLevel.getName()) && isDebug) {
            if (absLogLevel == LogLevel.ERROR && !TextUtils.isEmpty(str2) && str2.contains("IPCException")) {
                WXStateRecord.getInstance().recordIPCException("ipc", str2);
            }
            ConsoleLogUtils.consoleLog(str, str2, absLogLevel);
            LogWatcher logWatcher = sLogWatcher;
            if (logWatcher != null) {
                logWatcher.onLog(absLogLevel.getName(), str, str2);
            }
            if (WXEnvironment.isApkDebugable()) {
                if (absLogLevel.getValue() - WXEnvironment.sLogLevel.getValue() >= 0) {
                    Log.println(absLogLevel.getPriority(), str, str2);
                    writeConsoleLog(absLogLevel.getName(), str2);
                }
            } else if (absLogLevel.getValue() - LogLevel.WARN.getValue() >= 0 && absLogLevel.getValue() - WXEnvironment.sLogLevel.getValue() >= 0) {
                Log.println(absLogLevel.getPriority(), str, str2);
            }
        }
    }

    public static void v(String str) {
        v("weex", str);
    }

    public static void d(String str) {
        d("weex", str);
    }

    public static void d(String str, byte[] bArr) {
        d(str, new String(bArr));
    }

    public static void i(String str) {
        i("weex", str);
    }

    public static void i(String str, byte[] bArr) {
        i(str, new String(bArr));
    }

    public static void info(String str) {
        i("weex", str);
    }

    public static void w(String str) {
        w("weex", str);
    }

    public static void w(String str, byte[] bArr) {
        w(str, new String(bArr));
    }

    public static void e(String str) {
        e("weex", str);
    }

    public static void e(String str, byte[] bArr) {
        e(str, new String(bArr));
    }

    public static void wtf(String str) {
        wtf("weex", str);
    }

    public static void d(String str, String str2) {
        List<JsLogWatcher> list;
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            log(str, str2, LogLevel.DEBUG);
            if (WXEnvironment.isApkDebugable() && "jsLog".equals(str) && (list = jsLogWatcherList) != null && list.size() > 0) {
                for (JsLogWatcher next : jsLogWatcherList) {
                    if (str2.endsWith("__DEBUG")) {
                        next.onJsLog(3, str2.replace("__DEBUG", ""));
                    } else if (str2.endsWith("__INFO")) {
                        next.onJsLog(3, str2.replace("__INFO", ""));
                    } else if (str2.endsWith("__WARN")) {
                        next.onJsLog(3, str2.replace("__WARN", ""));
                    } else if (str2.endsWith("__ERROR")) {
                        next.onJsLog(3, str2.replace("__ERROR", ""));
                    } else {
                        next.onJsLog(3, str2);
                    }
                }
            }
        }
    }

    private static LogLevel getLogLevel(String str) {
        String trim = str.trim();
        trim.hashCode();
        char c = 65535;
        switch (trim.hashCode()) {
            case -1485211506:
                if (trim.equals("__INFO")) {
                    c = 0;
                    break;
                }
                break;
            case -1484806554:
                if (trim.equals("__WARN")) {
                    c = 1;
                    break;
                }
                break;
            case 90640196:
                if (trim.equals("__LOG")) {
                    c = 2;
                    break;
                }
                break;
            case 1198194259:
                if (trim.equals("__DEBUG")) {
                    c = 3;
                    break;
                }
                break;
            case 1199520264:
                if (trim.equals("__ERROR")) {
                    c = 4;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return LogLevel.INFO;
            case 1:
                return LogLevel.WARN;
            case 2:
                return LogLevel.INFO;
            case 3:
                return LogLevel.DEBUG;
            case 4:
                return LogLevel.ERROR;
            default:
                return LogLevel.DEBUG;
        }
    }

    public static void i(String str, String str2) {
        log(str, str2, LogLevel.INFO);
    }

    public static void v(String str, String str2) {
        log(str, str2, LogLevel.VERBOSE);
    }

    public static void w(String str, String str2) {
        log(str, str2, LogLevel.WARN);
    }

    public static void e(String str, String str2) {
        log(str, str2, LogLevel.ERROR);
    }

    public static void wtf(String str, String str2) {
        log(str, str2, LogLevel.WTF);
    }

    public static void p(String str) {
        d(WEEX_PERF_TAG, str);
    }

    public static void d(String str, Throwable th) {
        if (WXEnvironment.isApkDebugable()) {
            d(str + getStackTrace(th));
        }
    }

    public static void i(String str, Throwable th) {
        if (WXEnvironment.isApkDebugable()) {
            info(str + getStackTrace(th));
        }
    }

    public static void v(String str, Throwable th) {
        if (WXEnvironment.isApkDebugable()) {
            v(str + getStackTrace(th));
        }
    }

    public static void w(String str, Throwable th) {
        w(str + getStackTrace(th));
    }

    public static void e(String str, Throwable th) {
        e(str + getStackTrace(th));
    }

    public static void wtf(String str, Throwable th) {
        if (WXEnvironment.isApkDebugable()) {
            wtf(str + getStackTrace(th));
        }
    }

    public static void p(String str, Throwable th) {
        if (WXEnvironment.isApkDebugable()) {
            p(str + getStackTrace(th));
        }
    }

    public static void eTag(String str, Throwable th) {
        if (WXEnvironment.isApkDebugable()) {
            e(str, getStackTrace(th));
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0033 A[SYNTHETIC, Splitter:B:22:0x0033] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x003d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getStackTrace(java.lang.Throwable r3) {
        /*
            if (r3 != 0) goto L_0x0005
            java.lang.String r3 = ""
            return r3
        L_0x0005:
            r0 = 0
            java.io.StringWriter r1 = new java.io.StringWriter     // Catch:{ all -> 0x002f }
            r1.<init>()     // Catch:{ all -> 0x002f }
            java.io.PrintWriter r2 = new java.io.PrintWriter     // Catch:{ all -> 0x002b }
            r2.<init>(r1)     // Catch:{ all -> 0x002b }
            r3.printStackTrace(r2)     // Catch:{ all -> 0x0029 }
            r2.flush()     // Catch:{ all -> 0x0029 }
            r1.flush()     // Catch:{ all -> 0x0029 }
            r1.close()     // Catch:{ IOException -> 0x001d }
            goto L_0x0021
        L_0x001d:
            r3 = move-exception
            r3.printStackTrace()
        L_0x0021:
            r2.close()
            java.lang.String r3 = r1.toString()
            return r3
        L_0x0029:
            r3 = move-exception
            goto L_0x002d
        L_0x002b:
            r3 = move-exception
            r2 = r0
        L_0x002d:
            r0 = r1
            goto L_0x0031
        L_0x002f:
            r3 = move-exception
            r2 = r0
        L_0x0031:
            if (r0 == 0) goto L_0x003b
            r0.close()     // Catch:{ IOException -> 0x0037 }
            goto L_0x003b
        L_0x0037:
            r0 = move-exception
            r0.printStackTrace()
        L_0x003b:
            if (r2 == 0) goto L_0x0040
            r2.close()
        L_0x0040:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.utils.WXLogUtils.getStackTrace(java.lang.Throwable):java.lang.String");
    }

    private static void writeConsoleLog(String str, String str2) {
        if (WXEnvironment.isApkDebugable()) {
            try {
                Class cls = clazzMaps.get(CLAZZ_NAME_LOG_UTIL);
                if (cls != null) {
                    cls.getMethod("log", new Class[]{String.class, String.class}).invoke(cls, new Object[]{str, str2});
                }
            } catch (Exception unused) {
                Log.d("weex", "LogUtil not found!");
            }
        }
    }

    public static void setJsLogWatcher(JsLogWatcher jsLogWatcher) {
        if (!jsLogWatcherList.contains(jsLogWatcher)) {
            jsLogWatcherList.add(jsLogWatcher);
        }
    }

    public static void setLogWatcher(LogWatcher logWatcher) {
        sLogWatcher = logWatcher;
    }
}
