package com.nostra13.dcloudimageloader.utils;

import android.util.Log;
import com.nostra13.dcloudimageloader.core.ImageLoader;

public final class L {
    private static volatile boolean DISABLED = false;
    private static final String LOG_FORMAT = "%1$s\n%2$s";

    private L() {
    }

    public static void d(String str, Object... objArr) {
        log(3, (Throwable) null, str, objArr);
    }

    public static void disableLogging() {
        DISABLED = true;
    }

    public static void e(Throwable th) {
        log(6, th, (String) null, new Object[0]);
    }

    public static void enableLogging() {
        DISABLED = false;
    }

    public static void i(String str, Object... objArr) {
        log(4, (Throwable) null, str, objArr);
    }

    private static void log(int i, Throwable th, String str, Object... objArr) {
        if (!DISABLED) {
            if (objArr.length > 0) {
                str = String.format(str, objArr);
            }
            if (th != null) {
                if (str == null) {
                    str = th.getMessage();
                }
                str = String.format(LOG_FORMAT, new Object[]{str, Log.getStackTraceString(th)});
            }
            Log.println(i, ImageLoader.TAG, str);
        }
    }

    public static void w(String str, Object... objArr) {
        log(5, (Throwable) null, str, objArr);
    }

    public static void e(String str, Object... objArr) {
        log(6, (Throwable) null, str, objArr);
    }

    public static void e(Throwable th, String str, Object... objArr) {
        log(6, th, str, objArr);
    }
}
