package io.dcloud.sdk.core.util;

import android.content.Context;
import android.text.TextUtils;
import io.dcloud.h.a.e.e;
import io.dcloud.sdk.poly.base.utils.PrivacyManager;
import java.util.Map;

public class AdUtil {
    public static <T> T getOrDefault(Map<String, T> map, Object obj, T t) {
        if (map == null) {
            return t;
        }
        T t2 = map.get(obj);
        return (t2 != null || map.containsKey(obj)) ? t2 : t;
    }

    public static boolean getPersonalAd(Context context) {
        String a = e.a(context, "dcloud-ads", "PersonalizedAdEnable");
        if (TextUtils.isEmpty(a)) {
            a = AbsoluteConst.TRUE;
        }
        return Boolean.parseBoolean(a);
    }

    public static Map<String, Boolean> getPrivacyConfig() {
        return PrivacyManager.getInstance().getPrivacyMap();
    }

    public static void setPersonalAd(Context context, boolean z) {
        e.a(context, "dcloud-ads", "PersonalizedAdEnable", String.valueOf(z));
    }
}
