package io.dcloud.common.adapter.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.webkit.CookieManager;
import android.webkit.WebView;
import com.taobao.weex.common.WXConfig;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.utils.tools.TimeCalculator;
import dc.squareup.cookie.CookieCenter;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.IJsInterface;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.IUniNView;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.ReceiveJSValue;
import io.dcloud.common.adapter.ui.webview.WebViewFactory;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.EventActionInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.core.permission.PermissionControler;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.language.LanguageUtil;
import java.util.HashMap;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdaUniWebView extends AdaWebview {
    MessageHandler.IMessages iMessages = new MessageHandler.IMessages() {
        public void execute(Object obj) {
            Object[] objArr = (Object[]) obj;
            AdaUniWebView.this.exec(String.valueOf(objArr[0]), String.valueOf(objArr[1]), (JSONArray) objArr[2]);
        }
    };
    boolean isUniService = false;
    IApp mApp;
    String mFullUrl;
    float mScale = 3.0f;
    IUniNView mUniNView;
    String mUrl;
    private String mUserAgent = null;
    ViewGroup mViewImpl;
    AbsMgr mWinMgr = null;

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v2, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public AdaUniWebView(android.content.Context r5, io.dcloud.common.DHInterface.IApp r6, io.dcloud.common.adapter.ui.AdaFrameView r7, java.lang.String r8, java.lang.String r9, org.json.JSONObject r10, boolean r11) {
        /*
            r4 = this;
            r4.<init>(r5)
            r0 = 0
            r4.mWinMgr = r0
            r1 = 1077936128(0x40400000, float:3.0)
            r4.mScale = r1
            r4.mUserAgent = r0
            r1 = 0
            r4.isUniService = r1
            io.dcloud.common.adapter.ui.AdaUniWebView$1 r2 = new io.dcloud.common.adapter.ui.AdaUniWebView$1
            r2.<init>()
            r4.iMessages = r2
            if (r8 != 0) goto L_0x001a
            java.lang.String r8 = ""
        L_0x001a:
            org.json.JSONObject r2 = new org.json.JSONObject
            r2.<init>()
            java.lang.String r3 = "js"
            r2.put(r3, r8)     // Catch:{ JSONException -> 0x002a }
            java.lang.String r3 = "data"
            r2.put(r3, r10)     // Catch:{ JSONException -> 0x002a }
            goto L_0x002e
        L_0x002a:
            r10 = move-exception
            r10.printStackTrace()
        L_0x002e:
            r4.isUniService = r11
            r4.mApp = r6
            r4.mFrameView = r7
            java.lang.String r10 = r6.obtainAppId()
            r4.mAppid = r10
            r4.initSitemapState()
            r4.mUrl = r8
            boolean r8 = android.text.TextUtils.isEmpty(r8)
            if (r8 == 0) goto L_0x0048
            java.lang.String r8 = r4.mUrl
            goto L_0x0050
        L_0x0048:
            io.dcloud.common.DHInterface.IApp r8 = r4.mApp
            java.lang.String r10 = r4.mUrl
            java.lang.String r8 = r8.convert2WebviewFullPath(r0, r10)
        L_0x0050:
            r4.mFullUrl = r8
            io.dcloud.common.DHInterface.AbsMgr r7 = r7.obtainWindowMgr()
            r4.mWinMgr = r7
            android.content.res.Resources r7 = r5.getResources()
            android.util.DisplayMetrics r7 = r7.getDisplayMetrics()
            float r7 = r7.density
            r4.mScale = r7
            io.dcloud.common.adapter.ui.AdaWebViewParent r7 = new io.dcloud.common.adapter.ui.AdaWebViewParent
            r7.<init>(r5, r1)
            r4.mWebViewParent = r7
            r4.initUserAgent(r6)
            r5 = 4
            java.lang.Object[] r7 = new java.lang.Object[r5]
            r7[r1] = r4
            io.dcloud.common.adapter.ui.AdaWebViewParent r8 = r4.mWebViewParent
            android.view.ViewGroup r8 = r8.obtainMainViewGroup()
            r10 = 1
            r7[r10] = r8
            r8 = 2
            r7[r8] = r2
            r11 = 3
            r7[r11] = r9
            io.dcloud.common.DHInterface.AbsMgr r9 = r4.mWinMgr
            io.dcloud.common.DHInterface.IMgr$MgrType r0 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            java.lang.Object[] r5 = new java.lang.Object[r5]
            r5[r1] = r6
            java.lang.String r6 = "weex,io.dcloud.feature.weex.WeexFeature"
            r5[r10] = r6
            java.lang.String r6 = "createUniNView"
            r5[r8] = r6
            r5[r11] = r7
            r6 = 10
            java.lang.Object r5 = r9.processEvent(r0, r6, r5)
            io.dcloud.common.DHInterface.IUniNView r5 = (io.dcloud.common.DHInterface.IUniNView) r5
            r4.mUniNView = r5
            if (r5 == 0) goto L_0x00aa
            android.view.ViewGroup r5 = r5.obtainMainView()
            r4.mViewImpl = r5
            r4.setMainView(r5)
        L_0x00aa:
            io.dcloud.common.adapter.ui.AdaWebViewParent r5 = r4.mWebViewParent
            r5.fillsWithWebview(r4)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.ui.AdaUniWebView.<init>(android.content.Context, io.dcloud.common.DHInterface.IApp, io.dcloud.common.adapter.ui.AdaFrameView, java.lang.String, java.lang.String, org.json.JSONObject, boolean):void");
    }

    public void addJsInterface(String str, IJsInterface iJsInterface) {
    }

    public void addJsInterface(String str, Object obj) {
    }

    public void addJsInterface(String str, String str2) {
    }

    public boolean canGoBack() {
        return false;
    }

    public boolean canGoForward() {
        return false;
    }

    public void dispose() {
        super.dispose();
        this.mUniNView = null;
        this.mViewImpl = null;
    }

    public void endWebViewEvent(String str) {
        if (PdrUtil.isEquals(str, AbsoluteConst.PULL_DOWN_REFRESH)) {
            IUniNView iUniNView = this.mUniNView;
            if (iUniNView != null) {
                iUniNView.endPullToRefresh();
                return;
            }
            return;
        }
        super.endWebViewEvent(str);
    }

    public void evalJS(String str) {
        evalJSToUniNative(str);
    }

    public void evalJSToUniNative(String str) {
        if (this.mUniNView != null && !PdrUtil.isEmpty(str)) {
            HashMap hashMap = new HashMap();
            hashMap.put("data", str);
            this.mUniNView.fireGlobalEvent("nativeToUniJs", hashMap);
        }
    }

    public String exec(String str, String str2, JSONArray jSONArray) {
        if (getContext() == null) {
            return "";
        }
        try {
            if ("syncExecMethod".equalsIgnoreCase(str2)) {
                str2 = AbsoluteConst.UNI_SYNC_EXEC_METHOD;
            }
            return String.valueOf(this.mWinMgr.processEvent(IMgr.MgrType.FeatureMgr, 1, new Object[]{this, str, str2, jSONArray}));
        } catch (Exception e) {
            Logger.w("JsInterfaceImpl.exec pApiFeatureName=" + str + ";pActionName=" + str2 + ";pArgs=" + String.valueOf(jSONArray), e);
            return null;
        }
    }

    public void executeScript(String str) {
        evalJS(str);
    }

    public void fireEvent(EventActionInfo eventActionInfo) {
        if (this.mUniNView != null && eventActionInfo != null) {
            if (!TextUtils.isEmpty(eventActionInfo.getEvalJs())) {
                evalJSToUniNative(eventActionInfo.getEvalJs());
            }
            this.mUniNView.fireGlobalEvent(eventActionInfo.getEventAction(), eventActionInfo.getParams());
        }
    }

    public Object getConfigInfo() {
        if (TextUtils.isEmpty(DeviceInfo.sIMEI)) {
            DeviceInfo.initGsmCdmaCell();
        }
        HashMap hashMap = new HashMap();
        hashMap.put("__HtMl_Id__", getWebviewUUID());
        if (PermissionControler.checkPermission(this.mApp.obtainAppId(), IFeature.F_DEVICE.toLowerCase(Locale.ENGLISH)) || !this.mApp.manifestBeParsed()) {
            HashMap hashMap2 = new HashMap();
            hashMap2.put("imei", DeviceInfo.sIMEI);
            hashMap2.put("imsi", DeviceInfo.sIMSI);
            hashMap2.put("model", DeviceInfo.sModel);
            hashMap2.put("vendor", DeviceInfo.sVendor);
            hashMap2.put("uuid", DeviceInfo.sIMEI);
            hashMap.put("device", hashMap2);
            HashMap hashMap3 = new HashMap();
            hashMap3.put(IApp.ConfigProperty.CONFIG_LANGUAGE, LanguageUtil.getDeviceDefLocalLanguage());
            hashMap3.put("version", Build.VERSION.RELEASE);
            hashMap3.put("name", TimeCalculator.PLATFORM_ANDROID);
            hashMap3.put("vendor", "Google");
            hashMap.put(WXConfig.os, hashMap3);
            int i = this.mApp.getInt(2);
            int i2 = this.mApp.getInt(0);
            int i3 = this.mApp.getInt(1);
            HashMap hashMap4 = new HashMap();
            float scale = getScale();
            hashMap4.put("resolutionHeight", Integer.valueOf((int) (((float) i) / scale)));
            int i4 = (int) (((float) i2) / scale);
            hashMap4.put("resolutionWidth", Integer.valueOf(i4));
            hashMap4.put("scale", Float.valueOf(scale));
            hashMap4.put("dpiX", Float.valueOf(DeviceInfo.dpiX));
            hashMap4.put("dpiY", Float.valueOf(DeviceInfo.dpiY));
            hashMap4.put("height", Integer.valueOf(i));
            hashMap4.put("width", Integer.valueOf(i2));
            hashMap.put("screen", hashMap4);
            HashMap hashMap5 = new HashMap();
            hashMap5.put("resolutionHeight", Integer.valueOf((int) (((float) i3) / scale)));
            hashMap5.put("resolutionWidth", Integer.valueOf(i4));
            hashMap.put("display", hashMap5);
        }
        if (PermissionControler.checkPermission(this.mApp.obtainAppId(), IFeature.F_RUNTIME) || !this.mApp.manifestBeParsed()) {
            String obtainConfigProperty = this.mApp.obtainConfigProperty(IApp.ConfigProperty.CONFIG_LOADED_TIME);
            HashMap hashMap6 = new HashMap();
            hashMap6.put("innerVersion", "1.9.9.81676");
            hashMap6.put("appid", this.mApp.obtainAppId());
            hashMap6.put("launchLoadedTime", obtainConfigProperty);
            if (BaseInfo.ISAMU) {
                hashMap6.put("version", this.mApp.obtainAppVersionName());
            } else {
                hashMap6.put("version", AndroidResources.mApplicationInfo.versionName);
            }
            hashMap6.put("arguments", this.mApp.obtainRuntimeArgs(false));
            hashMap6.put("launcher", BaseInfo.getLauncherData(this.mApp.obtainAppId()));
            hashMap6.put(AbsoluteConst.XML_CHANNEL, BaseInfo.getAnalysisChannel());
            hashMap6.put("startupTime", String.valueOf(BaseInfo.getStartupTimeData(this.mApp.obtainAppId())));
            hashMap6.put("processId", Long.valueOf(BaseInfo.sProcessId));
            hashMap6.put("uniVersion", BaseInfo.uniVersionV3);
            hashMap6.put("versionCode", Integer.valueOf(AndroidResources.versionCode));
            Activity activity = this.mApp.getActivity();
            String bundleData = SP.getBundleData((Context) activity, "pdr", this.mApp.obtainAppId() + AbsoluteConst.LAUNCHTYPE);
            if (TextUtils.isEmpty(bundleData)) {
                bundleData = "default";
            }
            hashMap6.put("origin", bundleData);
            hashMap.put("runtime", hashMap6);
        }
        HashMap hashMap7 = new HashMap();
        hashMap7.put("__isUniPush__", Boolean.valueOf(AndroidResources.getMetaValue("DCLOUD_UNIPUSH")));
        hashMap.put("push", hashMap7);
        return hashMap;
    }

    public String getCookie(String str) {
        if (WebViewFactory.isIsOtherInitSuccess()) {
            return CookieCenter.getCookies(str);
        }
        return CookieManager.getInstance().getCookie(str);
    }

    public String getOriginalUrl() {
        return this.mUrl;
    }

    public float getScale() {
        return this.mScale;
    }

    public float getScaleOfOpenerWebview() {
        return 0.0f;
    }

    public String getTitle() {
        return "";
    }

    public String getWebviewProperty(String str) {
        if (IWebview.USER_AGENT.equals(str)) {
            return this.mUserAgent;
        }
        return super.getWebviewProperty(str);
    }

    /* access modifiers changed from: package-private */
    public void initUserAgent(IApp iApp) {
        if (!PdrUtil.isEmpty(AdaWebview.sCustomUserAgent)) {
            this.mUserAgent = AdaWebview.sCustomUserAgent;
            return;
        }
        this.mUserAgent = WebViewFactory.getDefWebViewUA(getContext());
        String obtainConfigProperty = iApp.obtainConfigProperty(IApp.ConfigProperty.CONFIG_USER_AGENT);
        boolean parseBoolean = Boolean.parseBoolean(iApp.obtainConfigProperty(IApp.ConfigProperty.CONFIG_CONCATENATE));
        if (Boolean.parseBoolean(iApp.obtainConfigProperty(IApp.ConfigProperty.CONFIG_funSetUA))) {
            parseBoolean = false;
        }
        if (!parseBoolean && !PdrUtil.isEmpty(obtainConfigProperty)) {
            this.mUserAgent = obtainConfigProperty;
        } else if (!PdrUtil.isEmpty(obtainConfigProperty)) {
            this.mUserAgent += Operators.SPACE_STR + obtainConfigProperty.trim();
        }
        boolean booleanValue = Boolean.valueOf(iApp.obtainConfigProperty(AbsoluteConst.JSONKEY_STATUSBAR_IMMERSED)).booleanValue();
        if (iApp.obtainStatusBarMgr() != null && iApp.obtainStatusBarMgr().checkImmersedStatusBar(getActivity(), booleanValue)) {
            this.mUserAgent += (" (Immersed/" + (((float) DeviceInfo.sStatusBarHeight) / this.mScale) + Operators.BRACKET_END_STR);
        }
    }

    public boolean isUniService() {
        return this.isUniService;
    }

    public boolean isUniWebView() {
        return true;
    }

    public void loadContentData(String str, String str2, String str3, String str4) {
    }

    public void loadUrl(String str) {
        if (!PdrUtil.isNetPath(str) && !PdrUtil.isFilePath(str)) {
            evalJS(str);
        }
    }

    public IApp obtainApp() {
        return this.mApp;
    }

    public IFrameView obtainFrameView() {
        return this.mFrameView;
    }

    public String obtainFullUrl() {
        return this.mFullUrl;
    }

    public String obtainPageTitle() {
        return "";
    }

    public String obtainUrl() {
        return this.mUrl;
    }

    public WebView obtainWebview() {
        return null;
    }

    public ViewGroup obtainWindowView() {
        return this.mViewImpl;
    }

    public String prompt(String str, String str2) {
        if (str2 != null && str2.length() > 3 && str2.substring(0, 4).equals("pdr:")) {
            try {
                JSONArray jSONArray = new JSONArray(str2.substring(4));
                String string = jSONArray.getString(0);
                String string2 = jSONArray.getString(1);
                boolean z = jSONArray.getBoolean(2);
                JSONArray createJSONArray = JSONUtil.createJSONArray(str);
                if (!z) {
                    return exec(string, string2, createJSONArray);
                }
                MessageHandler.sendMessage(this.iMessages, new Object[]{string, string2, createJSONArray});
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void reload() {
        IUniNView iUniNView = this.mUniNView;
        if (iUniNView != null) {
            iUniNView.reload();
        }
    }

    public void reload(String str) {
    }

    public void setNVuePath(String str) {
        this.mUrl = str;
        this.mFullUrl = TextUtils.isEmpty(str) ? this.mUrl : this.mApp.convert2WebviewFullPath((String) null, this.mUrl);
    }

    public void setOriginalUrl(String str) {
        this.mUrl = str;
    }

    public void setWebViewEvent(String str, Object obj) {
        if (PdrUtil.isEquals(str, AbsoluteConst.PULL_DOWN_REFRESH)) {
            JSONObject jSONObject = (JSONObject) obj;
            IUniNView iUniNView = this.mUniNView;
            if (iUniNView != null) {
                iUniNView.initRefresh(jSONObject);
            }
        } else if (PdrUtil.isEquals(str, AbsoluteConst.PULL_REFRESH_BEGIN)) {
            IUniNView iUniNView2 = this.mUniNView;
            if (iUniNView2 != null) {
                iUniNView2.beginPullRefresh();
            }
        } else {
            super.setWebViewEvent(str, obj);
        }
    }

    public void show(Animation animation) {
    }

    public void titleNViewRefresh() {
        IUniNView iUniNView = this.mUniNView;
        if (iUniNView != null) {
            iUniNView.titleNViewRefresh();
        }
    }

    public void updateScreenAndDisplay() {
        if (obtainApp() != null && this.mUniNView != null) {
            int i = obtainApp().getInt(2);
            int i2 = obtainApp().getInt(0);
            int i3 = obtainApp().getInt(1);
            HashMap hashMap = new HashMap();
            float scale = getScale();
            hashMap.put("resolutionHeight", Double.valueOf(Math.ceil((double) (((float) i) / scale))));
            double d = (double) (((float) i2) / scale);
            hashMap.put("resolutionWidth", Double.valueOf(Math.ceil(d)));
            hashMap.put("dpiX", Float.valueOf(DeviceInfo.dpiX));
            hashMap.put("dpiY", Float.valueOf(DeviceInfo.dpiY));
            HashMap hashMap2 = new HashMap();
            hashMap2.put("resolutionHeight", Double.valueOf(Math.ceil((double) (((float) i3) / scale))));
            hashMap2.put("resolutionWidth", Double.valueOf(Math.ceil(d)));
            StringBuilder sb = new StringBuilder();
            for (String str : hashMap.keySet()) {
                sb.append("plus.screen.");
                sb.append(str);
                sb.append("=");
                sb.append(hashMap.get(str));
                sb.append(";");
            }
            for (String str2 : hashMap2.keySet()) {
                sb.append("plus.display.");
                sb.append(str2);
                sb.append("=");
                sb.append(hashMap2.get(str2));
                sb.append(";");
            }
            evalJSToUniNative(sb.toString());
        }
    }

    public void evalJS(String str, ReceiveJSValue.ReceiveJSValueCallback receiveJSValueCallback) {
        evalJS(str);
    }

    public void reload(boolean z) {
        reload();
    }
}
