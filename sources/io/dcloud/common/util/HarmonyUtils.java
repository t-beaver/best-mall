package io.dcloud.common.util;

import android.os.Build;
import android.text.TextUtils;

public class HarmonyUtils {
    public static String getHarmonyDisplayVersion() {
        return Build.DISPLAY;
    }

    public static String getHarmonyVersion() {
        return getProp("hw_sc.build.platform.version", "");
    }

    private static String getProp(String str, String str2) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            String str3 = (String) cls.getDeclaredMethod("get", new Class[]{String.class}).invoke(cls, new Object[]{str});
            return TextUtils.isEmpty(str3) ? str2 : str3;
        } catch (Throwable th) {
            th.printStackTrace();
            return str2;
        }
    }

    public static boolean isHarmonyOs() {
        try {
            Class<?> cls = Class.forName("com.huawei.system.BuildEx");
            return "Harmony".equalsIgnoreCase(cls.getMethod("getOsBrand", new Class[0]).invoke(cls, new Object[0]).toString());
        } catch (Throwable unused) {
            return false;
        }
    }
}
