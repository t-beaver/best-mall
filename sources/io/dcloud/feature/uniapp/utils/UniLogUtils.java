package io.dcloud.feature.uniapp.utils;

import com.taobao.weex.utils.WXLogUtils;

public class UniLogUtils extends WXLogUtils {
    public static final String UNI_PERF_TAG = "uni_perf";
    public static final String UNI_TAG = "uni";

    public static void v(String str) {
        v(UNI_TAG, str);
    }

    public static void d(String str) {
        d(UNI_TAG, str);
    }

    public static void d(String str, byte[] bArr) {
        d(str, new String(bArr));
    }

    public static void i(String str) {
        i(UNI_TAG, str);
    }

    public static void i(String str, byte[] bArr) {
        i(str, new String(bArr));
    }

    public static void info(String str) {
        i(UNI_TAG, str);
    }

    public static void w(String str) {
        w(UNI_TAG, str);
    }

    public static void w(String str, byte[] bArr) {
        w(str, new String(bArr));
    }

    public static void e(String str) {
        e(UNI_TAG, str);
    }

    public static void e(String str, byte[] bArr) {
        e(str, new String(bArr));
    }
}
