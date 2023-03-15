package io.dcloud.feature.internal.sdk;

import android.content.Context;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICore;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameItem;
import io.dcloud.common.ui.b;
import io.dcloud.common.util.PdrUtil;
import java.util.ArrayList;
import java.util.Iterator;

public class SDK {
    public static final String ANDROID_ASSET = "file:///android_asset/";
    public static final String DEFAULT_APPID = "Default_Appid";
    public static final String UNIMP_CAPSULE_BUTTON_CLICK = "unimp_capsule_button_click";
    public static final String UNIMP_ERROR_KEY = "UniMP_Error";
    public static final String UNIMP_EVENT_CALLBACKID = "callbackId";
    public static final String UNIMP_EVENT_CALL_INSTANCEID = "instanceId";
    public static final String UNIMP_JS_TO_NATIVE = "unimp_js_to_native";
    public static final String UNIMP_OPEN = "open_unimp";
    public static final int UNI_CODE_ERROR_APPID = -1003;
    public static final int UNI_CODE_ERROR_NOT_RES = -1001;
    public static final int UNI_CODE_ERROR_NO_V3 = -1002;
    public static String customOAID = "";
    public static boolean hostAppThemeDark = false;
    public static boolean isCapsule = false;
    public static boolean isEnableBackground = false;
    public static boolean isNJS = true;
    public static boolean isUniMP = false;
    public static String mHostInfo = null;
    static ICore sCore = null;
    public static String sDefaultMenuButton = null;
    public static boolean uniMPSilentMode = false;

    public enum IntegratedMode {
        WEBVIEW,
        WEBAPP,
        RUNTIME
    }

    private SDK() {
    }

    public static void closeWebView(IWebview iWebview) {
        if (!isUniMPSDK()) {
            ((AdaFrameItem) iWebview.obtainFrameView()).getAnimOptions().mOption = 1;
            sCore.dispatchEvent(IMgr.MgrType.WindowMgr, 2, iWebview.obtainFrameView());
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v2, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static io.dcloud.common.DHInterface.IWebview createWebView(io.dcloud.common.DHInterface.IApp r4, java.lang.String r5, org.json.JSONObject r6, io.dcloud.common.DHInterface.IFrameView r7, io.dcloud.common.DHInterface.IEventCallback r8) {
        /*
            boolean r0 = isUniMPSDK()
            if (r0 == 0) goto L_0x0008
            r4 = 0
            return r4
        L_0x0008:
            r0 = 5
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r1 = 0
            java.lang.Integer r2 = java.lang.Integer.valueOf(r1)
            r0[r1] = r2
            r2 = 1
            r0[r2] = r4
            r4 = 2
            java.lang.Object[] r3 = new java.lang.Object[r4]
            r3[r1] = r5
            r3[r2] = r6
            r0[r4] = r3
            r4 = 3
            r0[r4] = r7
            r6 = 4
            r0[r6] = r8
            io.dcloud.common.DHInterface.ICore r6 = sCore
            io.dcloud.common.DHInterface.IMgr$MgrType r7 = io.dcloud.common.DHInterface.IMgr.MgrType.WindowMgr
            java.lang.Object r4 = r6.dispatchEvent(r7, r4, r0)
            io.dcloud.common.DHInterface.IFrameView r4 = (io.dcloud.common.DHInterface.IFrameView) r4
            io.dcloud.common.DHInterface.IWebview r4 = r4.obtainWebView()
            r4.loadUrl(r5)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.internal.sdk.SDK.createWebView(io.dcloud.common.DHInterface.IApp, java.lang.String, org.json.JSONObject, io.dcloud.common.DHInterface.IFrameView, io.dcloud.common.DHInterface.IEventCallback):io.dcloud.common.DHInterface.IWebview");
    }

    public static void initSDK(ICore iCore) {
        sCore = iCore;
    }

    public static boolean isAgreePrivacy(Context context) {
        if (context == null) {
            return false;
        }
        return !b.a().b(context);
    }

    public static boolean isUniMPSDK() {
        if (PdrUtil.isUniMPHostForUniApp()) {
            return isUniMP;
        }
        return false;
    }

    public static ArrayList<IWebview> obtainAllIWebview(String str) {
        ArrayList arrayList = (ArrayList) sCore.dispatchEvent(IMgr.MgrType.WindowMgr, 6, str);
        if (arrayList == null || arrayList.size() <= 0) {
            return null;
        }
        ArrayList<IWebview> arrayList2 = new ArrayList<>();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            arrayList2.add(((IFrameView) it.next()).obtainWebView());
        }
        return arrayList2;
    }

    public static IApp obtainCurrentApp() {
        if (isUniMPSDK()) {
            return null;
        }
        return (IApp) sCore.dispatchEvent(IMgr.MgrType.AppMgr, 6, obtainCurrentRunnbingAppId());
    }

    public static String obtainCurrentRunnbingAppId() {
        if (isUniMPSDK()) {
            return null;
        }
        return String.valueOf(sCore.dispatchEvent(IMgr.MgrType.AppMgr, 11, (Object) null));
    }

    public static IWebview obtainWebview(String str, String str2) {
        Iterator<IWebview> it = obtainAllIWebview(str).iterator();
        while (it.hasNext()) {
            IWebview next = it.next();
            if (PdrUtil.isEquals(str2, next.getWebviewUUID())) {
                return next;
            }
        }
        return null;
    }

    public static void popWebView(IWebview iWebview) {
        if (!isUniMPSDK()) {
            ((AdaFrameItem) iWebview.obtainFrameView()).getAnimOptions().mOption = 1;
            sCore.dispatchEvent(IMgr.MgrType.WindowMgr, 21, iWebview.obtainFrameView());
        }
    }

    public static void registerJsApi(String str, String str2, String str3) {
        if (!isUniMPSDK()) {
            sCore.dispatchEvent(IMgr.MgrType.FeatureMgr, 5, new String[]{str, str2, str3});
        }
    }

    public static void requestAllFeature() {
        if (!isUniMPSDK()) {
            sCore.dispatchEvent(IMgr.MgrType.FeatureMgr, 7, (Object) null);
        }
    }

    public static void requestFeature(String str, String str2, boolean z) {
        if (!isUniMPSDK()) {
            sCore.dispatchEvent(IMgr.MgrType.FeatureMgr, 6, new String[]{str, str2, String.valueOf(z)});
        }
    }

    public static void setAgreePrivacy(Context context, boolean z) {
        if (context != null) {
            b.a().a(context, z);
        }
    }

    public static void setUniMPSilentMode(boolean z) {
        uniMPSilentMode = z;
    }

    public static ArrayList<IWebview> obtainAllIWebview() {
        if (isUniMPSDK()) {
            return null;
        }
        return obtainAllIWebview(obtainCurrentRunnbingAppId());
    }
}
