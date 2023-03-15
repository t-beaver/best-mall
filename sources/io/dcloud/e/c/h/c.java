package io.dcloud.e.c.h;

import android.os.Build;
import android.text.TextUtils;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.utils.tools.TimeCalculator;

public class c {
    private static String a = "";
    private static String b = "";

    private static boolean a() {
        try {
            Class<?> cls = Class.forName("com.huawei.system.BuildEx");
            return !TextUtils.isEmpty((String) cls.getMethod("getOsBrand", new Class[0]).invoke(cls, new Object[0]));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String b(String str) {
        if (TextUtils.isEmpty(a)) {
            e(str);
        }
        return a;
    }

    public static String c(String str) {
        if (TextUtils.isEmpty(a)) {
            e(str);
        }
        return b;
    }

    private static String d(String str) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getDeclaredMethod("get", new Class[]{String.class}).invoke(cls, new Object[]{str});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void e(String str) {
        try {
            String a2 = a(str);
            char c = 65535;
            switch (a2.hashCode()) {
                case -1881642058:
                    if (a2.equals("REALME")) {
                        c = 4;
                        break;
                    }
                    break;
                case -1706170181:
                    if (a2.equals("XIAOMI")) {
                        c = 2;
                        break;
                    }
                    break;
                case -602397472:
                    if (a2.equals("ONEPLUS")) {
                        c = 7;
                        break;
                    }
                    break;
                case 2432928:
                    if (a2.equals("OPPO")) {
                        c = 5;
                        break;
                    }
                    break;
                case 2634924:
                    if (a2.equals("VIVO")) {
                        c = 6;
                        break;
                    }
                    break;
                case 68924490:
                    if (a2.equals("HONOR")) {
                        c = 1;
                        break;
                    }
                    break;
                case 73239724:
                    if (a2.equals("MEIZU")) {
                        c = 8;
                        break;
                    }
                    break;
                case 74632627:
                    if (a2.equals("NUBIA")) {
                        c = 9;
                        break;
                    }
                    break;
                case 77852109:
                    if (a2.equals("REDMI")) {
                        c = 3;
                        break;
                    }
                    break;
                case 2141820391:
                    if (a2.equals("HUAWEI")) {
                        c = 0;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    if (a()) {
                        b = d("hw_sc.build.platform.version");
                        a = "HarmonyOS";
                        return;
                    }
                    a = "EMUI";
                    b = d("ro.build.version.emui");
                    return;
                case 1:
                    if (a()) {
                        a = "HarmonyOS";
                        if (!TextUtils.isEmpty(d("hw_sc.build.platform.version"))) {
                            b = d("hw_sc.build.platform.version");
                            return;
                        } else {
                            b = "";
                            return;
                        }
                    } else if (!TextUtils.isEmpty(d("ro.build.version.magic"))) {
                        a = "MagicUI";
                        b = d("ro.build.version.magic");
                        return;
                    } else {
                        a = "EMUI";
                        b = d("ro.build.version.emui");
                        return;
                    }
                case 2:
                case 3:
                    a = "MIUI";
                    b = d("ro.miui.ui.version.name");
                    return;
                case 4:
                case 5:
                    a = "ColorOS";
                    b = d("ro.build.version.opporom");
                    return;
                case 6:
                    a = "Funtouch";
                    b = d("ro.vivo.os.version");
                    return;
                case 7:
                    a = "HydrogenOS";
                    b = d("ro.rom.version");
                    return;
                case 8:
                    a = "Flyme";
                    b = d("ro.build.display.id");
                    return;
                case 9:
                    a = d("ro.build.nubia.rom.name");
                    b = d("ro.build.nubia.rom.code");
                    return;
                default:
                    a = TimeCalculator.PLATFORM_ANDROID;
                    b = Build.VERSION.RELEASE;
                    return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        e.printStackTrace();
    }

    public static String a(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return str.replaceAll(Operators.SPACE_STR, "").toUpperCase();
    }
}
