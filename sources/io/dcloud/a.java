package io.dcloud;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.View;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.IWaiter;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.language.LanguageUtil;
import io.dcloud.e.c.b;
import io.dcloud.feature.internal.sdk.SDK;

public class a {
    private static Class a;

    public static void a(Application application) {
        IWaiter a2 = a();
        if (a2 != null) {
            a2.doForFeature("onAppCreate", application);
        }
    }

    public static boolean b() {
        if (a != null) {
            return true;
        }
        try {
            a = Class.forName("io.dcloud.feature.gg.AdFeatureImplMgr");
        } catch (Exception unused) {
        }
        if (a != null) {
            return true;
        }
        return false;
    }

    public static void a(Context context) {
        IWaiter a2 = a();
        DeviceInfo.sApplicationContext = context;
        if (a2 != null) {
            a2.doForFeature("onAppAttachBaseContext", context);
        }
    }

    public static Object a(Context context, String str, String str2, Object obj) {
        IWaiter a2;
        if (SDK.isUniMPSDK()) {
            return null;
        }
        if ((PdrUtil.checkIntl() && !LanguageUtil.getDeviceDefCountry().equalsIgnoreCase(b.c().decryptStr("GJ", (byte) 4))) || (a2 = a()) == null) {
            return null;
        }
        return a2.doForFeature(str2, new Object[]{context, str, obj});
    }

    private static IWaiter a() {
        if (b()) {
            try {
                Object invoke = a.getMethod("self", new Class[0]).invoke((Object) null, new Object[0]);
                if (invoke instanceof IWaiter) {
                    return (IWaiter) invoke;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static View a(Activity activity, ICallBack iCallBack, String str) {
        IWaiter a2 = a();
        if (a2 == null) {
            return null;
        }
        return (View) a2.doForFeature("onCreateAdSplash", new Object[]{activity, iCallBack, str});
    }
}
