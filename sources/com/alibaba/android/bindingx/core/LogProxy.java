package com.alibaba.android.bindingx.core;

import android.util.Log;
import com.alibaba.android.bindingx.core.internal.BindingXConstants;
import java.util.Map;

public final class LogProxy {
    private static final String KEY_DEBUG = "debug";
    public static boolean sEnableLog = false;

    public static void enableLogIfNeeded(Map<String, Object> map) {
        Object obj;
        if (map != null && (obj = map.get("debug")) != null) {
            boolean z = false;
            if (obj instanceof Boolean) {
                z = ((Boolean) obj).booleanValue();
            } else if (obj instanceof String) {
                z = AbsoluteConst.TRUE.equals((String) obj);
            }
            sEnableLog = z;
        }
    }

    public static void i(String str) {
        if (sEnableLog) {
            Log.i(BindingXConstants.TAG, str);
        }
    }

    public static void i(String str, Throwable th) {
        if (sEnableLog) {
            Log.i(BindingXConstants.TAG, str, th);
        }
    }

    public static void v(String str) {
        if (sEnableLog) {
            Log.v(BindingXConstants.TAG, str);
        }
    }

    public static void v(String str, Throwable th) {
        if (sEnableLog) {
            Log.v(BindingXConstants.TAG, str, th);
        }
    }

    public static void d(String str) {
        if (sEnableLog) {
            Log.d(BindingXConstants.TAG, str);
        }
    }

    public static void d(String str, Throwable th) {
        if (sEnableLog) {
            Log.d(BindingXConstants.TAG, str, th);
        }
    }

    public static void w(String str) {
        if (sEnableLog) {
            Log.w(BindingXConstants.TAG, str);
        }
    }

    public static void w(String str, Throwable th) {
        if (sEnableLog) {
            Log.w(BindingXConstants.TAG, str, th);
        }
    }

    public static void e(String str) {
        if (sEnableLog) {
            Log.e(BindingXConstants.TAG, str);
        }
    }

    public static void e(String str, Throwable th) {
        if (sEnableLog) {
            Log.e(BindingXConstants.TAG, str, th);
        }
    }
}
