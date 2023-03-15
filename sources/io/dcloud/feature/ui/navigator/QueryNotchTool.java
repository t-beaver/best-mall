package io.dcloud.feature.ui.navigator;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.DisplayCutout;
import android.view.View;
import android.view.WindowInsets;
import io.dcloud.common.adapter.util.MobilePhoneModel;
import java.lang.reflect.Method;

public class QueryNotchTool {
    public static final int NOTCH_IN_SCREEN_VOIO = 32;
    public static final int ROUNDED_IN_SCREEN_VOIO = 8;

    public static boolean hasNotchInHuawei(Context context) {
        try {
            Class<?> loadClass = context.getClassLoader().loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method method = loadClass.getMethod("hasNotchInScreen", new Class[0]);
            if (method != null) {
                return ((Boolean) method.invoke(loadClass, new Object[0])).booleanValue();
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean hasNotchInOppo(Context context) {
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    public static boolean hasNotchInScreen(Activity activity) {
        if (MobilePhoneModel.isAppointPhone(MobilePhoneModel.XIAOMI)) {
            return hasNotchInXiaomi(activity);
        }
        if (MobilePhoneModel.isAppointPhone(MobilePhoneModel.VIVO)) {
            return hasNotchInVoio(activity);
        }
        if (MobilePhoneModel.isAppointPhone(MobilePhoneModel.OPPO)) {
            return hasNotchInOppo(activity);
        }
        if (MobilePhoneModel.isAppointPhone(MobilePhoneModel.HUAWEI) || MobilePhoneModel.isAppointPhone(MobilePhoneModel.HONOR)) {
            return hasNotchInHuawei(activity);
        }
        return isAndroidP(activity) != null;
    }

    public static boolean hasNotchInVoio(Context context) {
        try {
            Class<?> loadClass = context.getClassLoader().loadClass("com.util.FtFeature");
            return ((Boolean) loadClass.getMethod("isFeatureSupport", new Class[]{Integer.TYPE}).invoke(loadClass, new Object[]{32})).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean hasNotchInXiaomi(Context context) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            if (((Integer) cls.getDeclaredMethod("getInt", new Class[]{String.class, Integer.TYPE}).invoke((Object) null, new Object[]{"ro.miui.notch", 0})).intValue() == 1) {
                return true;
            }
            return false;
        } catch (Exception unused) {
        }
    }

    public static DisplayCutout isAndroidP(Activity activity) {
        WindowInsets rootWindowInsets;
        View decorView = activity.getWindow().getDecorView();
        if (decorView == null || Build.VERSION.SDK_INT < 28 || (rootWindowInsets = decorView.getRootWindowInsets()) == null) {
            return null;
        }
        return rootWindowInsets.getDisplayCutout();
    }
}
