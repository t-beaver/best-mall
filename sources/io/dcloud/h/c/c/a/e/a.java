package io.dcloud.h.c.c.a.e;

import android.content.Context;
import android.text.TextUtils;
import io.dcloud.sdk.core.DCloudAOLManager;
import java.lang.reflect.Field;

public class a {
    public static Object a() {
        try {
            Class<?> cls = Class.forName("io.dcloud.common.util.BaseInfo");
            Field declaredField = cls.getDeclaredField("sBaseVersion");
            declaredField.setAccessible(true);
            return declaredField.get(cls);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException unused) {
            return DCloudAOLManager.getVersion();
        }
    }

    public static String a(Context context) {
        String str;
        String packageName = context.getPackageName();
        try {
            str = context.getPackageManager().getApplicationInfo(packageName, 128).metaData.getString("DCLOUD_STREAMAPP_CHANNEL");
        } catch (Exception unused) {
            str = null;
        }
        if (!TextUtils.isEmpty(str)) {
            return str;
        }
        return packageName + "|" + io.dcloud.h.c.a.d().b().getAppId() + "|" + io.dcloud.h.c.a.d().b().getAdId();
    }
}
