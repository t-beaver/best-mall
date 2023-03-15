package io.dcloud.common.util;

import android.app.Activity;
import android.app.Application;
import android.app.UiModeManager;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.INativeAppInfo;
import io.dcloud.common.DHInterface.IUniInstanceMgr;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaUniWebView;
import io.dcloud.common.adapter.ui.webview.DCWebView;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.EventActionInfo;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.core.ui.TabBarWebview;
import io.dcloud.common.ui.b;
import io.dcloud.common.ui.blur.DCBlurDraweeView;
import io.dcloud.feature.internal.sdk.SDK;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppRuntime {
    private static int sBatteryLevel = 100;
    private static int sTemperature = 20;
    private static String sUniStatistics;

    public static void applyWebViewDarkMode(Context context, WebView webView) {
        if (Build.VERSION.SDK_INT >= 29) {
            if (WebViewFeature.isFeatureSupported("FORCE_DARK")) {
                if (getAppDarkMode(context)) {
                    WebSettingsCompat.setForceDark(webView.getSettings(), 2);
                } else {
                    WebSettingsCompat.setForceDark(webView.getSettings(), 0);
                }
            }
            if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK_STRATEGY)) {
                WebSettingsCompat.setForceDarkStrategy(webView.getSettings(), 1);
            }
        }
    }

    public static void checkPrivacyComplianceAndPrompt(Context context, String str) {
        boolean z = BaseInfo.SyncDebug;
    }

    private static List<View> findAllWebView(View view) {
        ArrayList arrayList = new ArrayList();
        if (view instanceof DCWebView) {
            arrayList.add(view);
            return arrayList;
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                arrayList.addAll(findAllWebView(viewGroup.getChildAt(i)));
            }
        }
        return arrayList;
    }

    public static boolean getAppDarkMode(Context context) {
        if (Build.VERSION.SDK_INT < 29) {
            return false;
        }
        String bundleData = SP.getBundleData(context, SP.DARK_MODE_BUNDLE_FORMAT, SP.DARK_MODE_KEY);
        String metaValue = AndroidResources.getMetaValue("DCLOUD_DARK_MODE");
        if (PdrUtil.isEmpty(metaValue)) {
            metaValue = DCBlurDraweeView.LIGHT;
        }
        if (PdrUtil.isEmpty(bundleData)) {
            bundleData = metaValue;
        }
        bundleData.hashCode();
        if (!bundleData.equals("auto")) {
            if (!bundleData.equals(DCBlurDraweeView.DARK)) {
                return false;
            }
            return true;
        } else if (((UiModeManager) context.getSystemService("uimode")).getNightMode() == 2) {
            return true;
        } else {
            return false;
        }
    }

    public static int getBatteryLevel() {
        return sBatteryLevel;
    }

    public static String getDCloudDeviceID(Context context) {
        return TelephonyUtil.getDCloudDeviceID(context);
    }

    public static IUniInstanceMgr getInstanceMgr() {
        return (IUniInstanceMgr) PlatformUtil.invokeMethod("io.dcloud.feature.weex.WeexInstanceMgr", "self");
    }

    public static int getTemperature() {
        return sTemperature;
    }

    public static String getUniStatistics() {
        return sUniStatistics;
    }

    public static boolean hasPrivacyForNotShown(Context context) {
        if (SDK.isUniMPSDK()) {
            return SDK.uniMPSilentMode;
        }
        return b.a().b(context);
    }

    public static void initUTS() {
        Class<?> cls;
        Object obj;
        Method method = null;
        try {
            cls = Class.forName("io.dcloud.uts.android.AndroidUTSContext");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            cls = null;
        }
        try {
            obj = cls.getField("INSTANCE").get((Object) null);
        } catch (Exception e2) {
            e2.printStackTrace();
            obj = null;
        }
        if (cls != null && obj != null) {
            try {
                method = cls.getMethod("initApp", new Class[0]);
            } catch (NoSuchMethodException unused) {
            }
            if (method != null) {
                try {
                    method.invoke(obj, new Object[0]);
                } catch (IllegalAccessException e3) {
                    e3.printStackTrace();
                } catch (InvocationTargetException e4) {
                    e4.printStackTrace();
                }
            }
        }
    }

    public static void initUniappPlugin(Application application) {
        IUniInstanceMgr instanceMgr = getInstanceMgr();
        if (instanceMgr != null) {
            instanceMgr.initUniappPlugin(application);
        }
    }

    public static void initWeex(INativeAppInfo iNativeAppInfo) {
        IUniInstanceMgr iUniInstanceMgr = (IUniInstanceMgr) PlatformUtil.invokeMethod("io.dcloud.feature.weex.WeexInstanceMgr", "self");
        if (iUniInstanceMgr != null) {
            iUniInstanceMgr.initWeexEnv(iNativeAppInfo);
        }
    }

    public static void initX5(Application application, boolean z, ICallBack iCallBack) {
        PlatformUtil.invokeMethod("io.dcloud.feature.x5.X5InitImpl", "init", (Object) null, new Class[]{Application.class, Boolean.class, ICallBack.class}, new Object[]{application, Boolean.valueOf(z), iCallBack});
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x00ae A[Catch:{ Exception -> 0x00bf }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean isAppResourcesInAssetsPath(android.content.Context r6, java.lang.String r7) {
        /*
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r7)
            r1 = 0
            if (r0 == 0) goto L_0x001b
            java.lang.String r7 = io.dcloud.common.util.BaseInfo.sDefaultBootApp
            boolean r7 = io.dcloud.common.util.PdrUtil.isEmpty(r7)
            if (r7 == 0) goto L_0x0012
            io.dcloud.common.util.BaseInfo.parseControl()
        L_0x0012:
            java.lang.String r7 = io.dcloud.common.util.BaseInfo.sDefaultBootApp
            boolean r0 = io.dcloud.common.util.PdrUtil.isEmpty(r7)
            if (r0 == 0) goto L_0x001b
            return r1
        L_0x001b:
            boolean r0 = io.dcloud.common.util.BaseInfo.isBase(r6)
            if (r0 == 0) goto L_0x0022
            return r1
        L_0x0022:
            boolean r0 = io.dcloud.common.util.BaseInfo.ISDEBUG
            r2 = 1
            if (r0 != 0) goto L_0x0030
            boolean r0 = io.dcloud.common.adapter.io.DHFile.hasFile()
            if (r0 == 0) goto L_0x002e
            goto L_0x0030
        L_0x002e:
            r0 = 0
            goto L_0x0031
        L_0x0030:
            r0 = 1
        L_0x0031:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "apps/"
            r3.append(r4)
            r3.append(r7)
            java.lang.String r4 = "/www/"
            r3.append(r4)
            java.lang.String r4 = io.dcloud.common.util.BaseInfo.sConfigXML
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            if (r0 == 0) goto L_0x006f
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.io.File r4 = android.os.Environment.getExternalStorageDirectory()
            java.lang.String r4 = r4.getPath()
            r0.append(r4)
            java.lang.String r4 = "/Android/data/"
            r0.append(r4)
            java.lang.String r4 = r6.getPackageName()
            r0.append(r4)
            java.lang.String r0 = r0.toString()
            goto L_0x0077
        L_0x006f:
            java.io.File r0 = r6.getFilesDir()
            java.lang.String r0 = r0.getPath()
        L_0x0077:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r4.append(r0)
            java.lang.String r0 = "/"
            r4.append(r0)
            r4.append(r3)
            java.lang.String r0 = r4.toString()
            org.json.JSONObject r2 = io.dcloud.common.util.PdrUtil.getConfigData(r6, r7, r3, r2)     // Catch:{ Exception -> 0x00bf }
            java.lang.String r3 = "name"
            java.lang.String r4 = "version"
            java.lang.String r5 = ""
            if (r2 == 0) goto L_0x00a7
            java.lang.String r2 = r2.getString(r4)     // Catch:{ Exception -> 0x00bf }
            org.json.JSONObject r2 = io.dcloud.common.util.JSONUtil.createJSONObject(r2)     // Catch:{ Exception -> 0x00bf }
            if (r2 == 0) goto L_0x00a7
            java.lang.String r2 = r2.getString(r3)     // Catch:{ Exception -> 0x00bf }
            goto L_0x00a8
        L_0x00a7:
            r2 = r5
        L_0x00a8:
            org.json.JSONObject r6 = io.dcloud.common.util.PdrUtil.getConfigData(r6, r7, r0, r1)     // Catch:{ Exception -> 0x00bf }
            if (r6 == 0) goto L_0x00ba
            java.lang.String r6 = r6.getString(r4)     // Catch:{ Exception -> 0x00bf }
            org.json.JSONObject r6 = io.dcloud.common.util.JSONUtil.createJSONObject(r6)     // Catch:{ Exception -> 0x00bf }
            java.lang.String r5 = r6.getString(r3)     // Catch:{ Exception -> 0x00bf }
        L_0x00ba:
            boolean r1 = io.dcloud.common.util.BaseInfo.BaseAppInfo.compareVersion(r2, r5)     // Catch:{ Exception -> 0x00bf }
            goto L_0x00c3
        L_0x00bf:
            r6 = move-exception
            r6.printStackTrace()
        L_0x00c3:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.util.AppRuntime.isAppResourcesInAssetsPath(android.content.Context, java.lang.String):boolean");
    }

    public static boolean isUniApp(String str) {
        return !TextUtils.isEmpty(str) && str.startsWith("__UNI__");
    }

    static /* synthetic */ void lambda$switchAllWebViewDarkMode$0(View view) {
        if (view instanceof DCWebView) {
            ((DCWebView) view).applyWebViewDarkMode();
        }
    }

    static /* synthetic */ void lambda$switchAllWebViewDarkMode$1(IWebview iWebview) {
        ViewGroup obtainWindowView = iWebview.obtainWindowView();
        if (iWebview instanceof TabBarWebview) {
            findAllWebView(obtainWindowView).forEach($$Lambda$AppRuntime$iOsiCkjBhiP8W7dUfTQnPok5DM.INSTANCE);
        }
        if (obtainWindowView instanceof DCWebView) {
            ((DCWebView) obtainWindowView).applyWebViewDarkMode();
        }
    }

    public static void loadDex(Application application) {
        IUniInstanceMgr instanceMgr = getInstanceMgr();
        if (instanceMgr != null) {
            PlatformUtil.invokeMethod(instanceMgr, "loadDex", new Class[]{Application.class}, application);
        }
    }

    public static InputStream loadWeexAsset(String str, Context context) {
        if ((str.startsWith("weex-main-jsfm") || str.startsWith("uni-jsframework")) && str.endsWith(".js") && context.getPackageName().equals("io.dcloud.HBuilder") && context.getExternalCacheDir() != null) {
            String path = context.getExternalCacheDir().getPath();
            File file = new File(path + File.separator + str);
            if (file.exists()) {
                try {
                    return new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void onSubProcess(Application application) {
        IUniInstanceMgr instanceMgr = getInstanceMgr();
        if (instanceMgr != null) {
            instanceMgr.onSubProcess(application);
        }
    }

    public static void preInitX5(Application application) {
        PlatformUtil.invokeMethod("io.dcloud.feature.x5.X5InitImpl", "preInit", (Object) null, new Class[]{Application.class}, new Object[]{application});
    }

    public static void restartWeex(Application application, ICallBack iCallBack, String str) {
        IUniInstanceMgr instanceMgr = getInstanceMgr();
        if (instanceMgr != null) {
            instanceMgr.restartWeex(application, iCallBack, str);
        }
    }

    public static void setAppDarkMode(Activity activity, IWebview iWebview, String str) {
        if (Build.VERSION.SDK_INT >= 29 && activity != null && !PdrUtil.isEmpty(str)) {
            if (!PdrUtil.isEquals(SP.getBundleData((Context) activity, SP.DARK_MODE_BUNDLE_FORMAT, SP.DARK_MODE_KEY), str)) {
                if (iWebview instanceof AdaUniWebView) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("uistyle", str);
                    ((AdaUniWebView) iWebview).fireEvent(new EventActionInfo(AbsoluteConst.EVENTS_UISTYLECHANGE, (Map<String, Object>) hashMap));
                } else {
                    iWebview.loadUrl(StringUtil.format(AbsoluteConst.EVENTS_DOCUMENT_EXECUTE_TEMPLATE_PARAMETER_OF_UISTYLE, AbsoluteConst.EVENTS_UISTYLECHANGE, str));
                }
            }
            SP.setBundleData(activity, SP.DARK_MODE_BUNDLE_FORMAT, SP.DARK_MODE_KEY, str);
            switchAllWebViewDarkMode();
        }
    }

    public static void setBatteryLevel(int i) {
        sBatteryLevel = i;
    }

    public static void setTemperature(int i) {
        sTemperature = i;
    }

    public static void setUniStatistics(String str) {
        sUniStatistics = str;
    }

    public static void switchAllWebViewDarkMode() {
        ArrayList<IWebview> obtainAllIWebview = SDK.obtainAllIWebview();
        if (obtainAllIWebview != null) {
            obtainAllIWebview.forEach($$Lambda$AppRuntime$_ZSq3SuXcfKmm3Jn1F1hS3RLdmo.INSTANCE);
        }
    }
}
