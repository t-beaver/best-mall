package io.dcloud.feature.weex.extend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.R;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.bridge.SimpleJSCallback;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.ui.component.DCWXInput;
import com.taobao.weex.ui.component.WXImage;
import io.dcloud.application.DCLoudApplicationImpl;
import io.dcloud.common.DHInterface.IActivityHandler;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaUniWebView;
import io.dcloud.common.adapter.ui.webview.WebResUtil;
import io.dcloud.common.adapter.ui.webview.WebViewFactory;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.EventActionInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.PermissionUtil;
import io.dcloud.common.constant.IntentConst;
import io.dcloud.common.ui.blur.DCBlurDraweeView;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.IOUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.StringUtil;
import io.dcloud.common.util.TelephonyUtil;
import io.dcloud.common.util.ThreadPool;
import io.dcloud.common.util.language.LanguageUtil;
import io.dcloud.feature.internal.sdk.SDK;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.weex.WXBaseWrapper;
import io.dcloud.feature.weex.WXServiceWrapper;
import io.dcloud.feature.weex.WeexInstanceMgr;
import io.dcloud.feature.weex.extend.result.Result;
import io.dcloud.feature.weex.extend.result.SecureNetworkResult;
import io.dcloud.weex.WXDotDataUtil;
import io.src.dcloud.adapter.DCloudAdapterUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PlusModule extends WXModule {
    private static final String HELP_LOG_HASH = "HELP_LOG_HASH_";
    private String EVENTS_DOCUMENT_EXECUTE = "javascript:!function(){(window.__html5plus__&&__html5plus__.isReady?__html5plus__:navigator.plus&&navigator.plus.isReady?navigator.plus:window.plus)||window.__load__plus__&&window.__load__plus__();var _=document.createEvent(\"HTMLEvents\");_.initEvent(\"%s\",!1,!0),_.targetId=\"%s\",_.originId=\"%s\",_.data=%s,document.dispatchEvent(_)}();";
    ArrayList<JsData> chs = new ArrayList<>();

    @JSMethod
    public void postMessage(String str, String str2) {
        IWebview iWebview;
        WXServiceWrapper findWXServiceWrapper;
        IWebview findWebview = WeexInstanceMgr.self().findWebview(this.mWXSDKInstance);
        String str3 = this.EVENTS_DOCUMENT_EXECUTE;
        Object[] objArr = new Object[4];
        objArr[0] = "uniNViewMessage";
        String str4 = "";
        objArr[1] = str4;
        objArr[2] = findWebview != null ? findWebview.obtainFrameId() : str4;
        objArr[3] = str;
        String format = StringUtil.format(str3, objArr);
        if (findWebview == null && (findWXServiceWrapper = WeexInstanceMgr.self().findWXServiceWrapper(this.mWXSDKInstance)) != null && !BaseInfo.isWeexUniJs(findWXServiceWrapper.obtanApp())) {
            findWXServiceWrapper.findWebViewToLoadUrL(format, str2);
        } else if (findWebview != null) {
            if (PdrUtil.isEmpty(str2)) {
                iWebview = findWebview;
            } else {
                iWebview = WeexInstanceMgr.self().findWebview(findWebview, findWebview.obtainApp(), findWebview.obtainApp().obtainAppId(), str2);
            }
            if (iWebview == null) {
                return;
            }
            if (iWebview instanceof AdaUniWebView) {
                HashMap hashMap = new HashMap();
                hashMap.put("targetId", str2);
                if (findWebview != null) {
                    str4 = findWebview.obtainFrameId();
                }
                hashMap.put("originId", str4);
                try {
                    hashMap.put("data", JSONObject.parseObject(str));
                } catch (Exception unused) {
                    hashMap.put("data", str);
                }
                ((AdaUniWebView) iWebview).fireEvent(new EventActionInfo("uniNViewMessage", (Map<String, Object>) hashMap));
                return;
            }
            iWebview.loadUrl(format);
        }
    }

    @JSMethod(uiThread = true)
    public void exec(String str, String str2) {
        if (this.mWXSDKInstance == null || !this.mWXSDKInstance.isDestroy()) {
            IWebview findWebview = WeexInstanceMgr.self().findWebview(this.mWXSDKInstance);
            if (findWebview instanceof AdaUniWebView) {
                ((AdaUniWebView) findWebview).prompt(str, str2);
            }
        }
    }

    class JsData {
        public String data;
        public String value;

        JsData(String str, String str2) {
            this.data = str;
            this.value = str2;
        }
    }

    @JSMethod(uiThread = false)
    public String execSync(String str, String str2) {
        if (this.mWXSDKInstance != null && this.mWXSDKInstance.isDestroy()) {
            return "";
        }
        IWebview findWebview = WeexInstanceMgr.self().findWebview(this.mWXSDKInstance);
        if (findWebview instanceof AdaUniWebView) {
            return ((AdaUniWebView) findWebview).prompt(str, str2);
        }
        return "";
    }

    @JSMethod(uiThread = true)
    public void uniReady() {
        WXBaseWrapper findWXBaseWrapper = WeexInstanceMgr.self().findWXBaseWrapper(this.mWXSDKInstance);
        if (findWXBaseWrapper != null) {
            findWXBaseWrapper.onReady();
        }
    }

    @JSMethod(uiThread = false)
    public void setLanguage(String str) {
        if (this.mWXSDKInstance != null && this.mWXSDKInstance.getContext() != null && !SDK.isUniMPSDK() && !this.mWXSDKInstance.isDestroy() && Build.VERSION.SDK_INT >= 21) {
            if ("auto".equalsIgnoreCase(str)) {
                str = "";
            }
            LanguageUtil.updateLanguage(this.mWXSDKInstance.getContext(), str);
            LocalBroadcastManager.getInstance(this.mWXSDKInstance.getContext()).sendBroadcast(new Intent(LanguageUtil.LanguageBroadCastIntent));
        }
    }

    @JSMethod(uiThread = false)
    public String getLanguage() {
        return (this.mWXSDKInstance == null || this.mWXSDKInstance.getContext() == null) ? "" : LanguageUtil.getCurrentLocaleLanguage(this.mWXSDKInstance.getContext());
    }

    private void runChData() {
        if (!this.chs.isEmpty()) {
            ArrayList arrayList = new ArrayList();
            Iterator<JsData> it = this.chs.iterator();
            while (it.hasNext()) {
                JsData next = it.next();
                exec(next.data, next.value);
                arrayList.add(next);
            }
            this.chs.removeAll(arrayList);
        }
    }

    @JSMethod(uiThread = false)
    public Object getConfigInfo() {
        IWebview findWebview = WeexInstanceMgr.self().findWebview(this.mWXSDKInstance);
        if (findWebview instanceof AdaUniWebView) {
            return ((AdaUniWebView) findWebview).getConfigInfo();
        }
        return null;
    }

    @JSMethod(uiThread = false)
    public Object getRedirectInfo() {
        JSONObject parseObject;
        IWebview findWebview = WeexInstanceMgr.self().findWebview(this.mWXSDKInstance);
        JSONObject jSONObject = null;
        if (findWebview != null) {
            if (Boolean.valueOf(findWebview.obtainApp().obtainConfigProperty(IApp.ConfigProperty.UNI_RESTART_TO_DIRECT)).booleanValue() && (parseObject = JSON.parseObject(findWebview.obtainApp().obtainConfigProperty(AbsoluteConst.JSON_KEY_DEBUG_REFRESH))) != null && parseObject.containsKey("arguments")) {
                try {
                    jSONObject = JSON.parseObject(parseObject.getString("arguments"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                findWebview.obtainApp().setConfigProperty(AbsoluteConst.JSON_KEY_DEBUG_REFRESH, "");
            }
            JSONObject parseObject2 = JSON.parseObject(findWebview.obtainApp().obtainConfigProperty(IntentConst.UNIMP_RUN_EXTRA_INFO));
            if (parseObject2 != null) {
                if (jSONObject == null) {
                    jSONObject = new JSONObject();
                }
                jSONObject.putAll(parseObject2);
                findWebview.obtainApp().setConfigProperty(IntentConst.UNIMP_RUN_EXTRA_INFO, "");
            }
        }
        return jSONObject;
    }

    @JSMethod(uiThread = false)
    public void setDefaultFontSize(String str) {
        int intValue = Integer.valueOf(str).intValue();
        if (intValue > 0) {
            this.mWXSDKInstance.setDefaultFontSize(intValue);
        }
    }

    @JSMethod(uiThread = true)
    public void log(String str) {
        Logger.d("console", "[LOG] " + str);
    }

    @JSMethod(uiThread = false)
    public String getValue(String str) {
        return ((DCWXInput) WXSDKManager.getInstance().getWXRenderManager().getWXComponent(this.mWXSDKInstance.getInstanceId(), str)).getValue();
    }

    @JSMethod(uiThread = true)
    public void sendNativeEvent(String str, Object obj, JSCallback jSCallback) {
        IWebview findWebview = WeexInstanceMgr.self().findWebview(this.mWXSDKInstance);
        if (findWebview != null && findWebview.getActivity() != null && (findWebview.getActivity() instanceof IActivityHandler)) {
            Bundle bundle = new Bundle();
            bundle.putString("event", str);
            bundle.putString("dataType", "String");
            if (obj instanceof String) {
                bundle.putString("data", String.valueOf(obj));
            } else if (obj instanceof JSON) {
                bundle.putString("data", ((JSON) obj).toJSONString());
                bundle.putString("dataType", "JSON");
            }
            if (jSCallback instanceof SimpleJSCallback) {
                bundle.putString("instanceId", this.mWXSDKInstance.getInstanceId());
                bundle.putString(SDK.UNIMP_EVENT_CALLBACKID, ((SimpleJSCallback) jSCallback).getCallbackId());
            }
            ((IActivityHandler) findWebview.getActivity()).callBack(SDK.UNIMP_JS_TO_NATIVE, bundle);
        }
    }

    @JSMethod(uiThread = false)
    public JSONObject getDotData() {
        JSONObject deviceInfo = WXDotDataUtil.getDeviceInfo();
        if (BaseInfo.SyncDebug) {
            deviceInfo.put("maxMemory", (Object) (Runtime.getRuntime().maxMemory() / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED) + "M");
            deviceInfo.put("totalMemory", (Object) (Runtime.getRuntime().totalMemory() / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED) + "M");
            deviceInfo.put("appRuningTitme", (Object) Long.valueOf(BaseInfo.splashCloseTime - BaseInfo.startTime));
        }
        return deviceInfo;
    }

    @JSMethod(uiThread = false)
    public JSONObject pushDebugData(Object obj) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("data", obj);
        return jSONObject;
    }

    @JSMethod
    public void preloadReady(String str) {
        ICallBack preUniMPCallBack = WeexInstanceMgr.self().getPreUniMPCallBack(str);
        if (preUniMPCallBack != null) {
            preUniMPCallBack.onCallBack(1, str);
        }
    }

    @JSMethod(uiThread = true)
    public void evalJSFiles(String str, final JSCallback jSCallback) {
        final HashMap hashMap = new HashMap();
        if (str != null) {
            final JSONArray parseArray = JSON.parseArray(str);
            if (parseArray != null) {
                ThreadPool.self().addThreadTask(new Runnable() {
                    public void run() {
                        IWebview findWebview = WeexInstanceMgr.self().findWebview(PlusModule.this.mWXSDKInstance);
                        if (findWebview != null) {
                            byte obtainRunningAppMode = findWebview.obtainApp().obtainRunningAppMode();
                            String str = "";
                            for (int i = 0; i < parseArray.size(); i++) {
                                String string = parseArray.getString(i);
                                File file = new File(string);
                                InputStream inputStream = null;
                                if (file.exists()) {
                                    try {
                                        inputStream = new FileInputStream(file);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    if (string.startsWith("/storage") || obtainRunningAppMode != 1) {
                                        string = findWebview.obtainApp().convert2WebviewFullPath(findWebview.obtainFullUrl(), string);
                                    } else {
                                        string = findWebview.obtainApp().convert2AbsFullPath(findWebview.obtainFullUrl(), string);
                                        if (string.startsWith("/")) {
                                            string = string.substring(1, string.length());
                                        }
                                    }
                                    inputStream = WebResUtil.getEncryptionInputStream(string, findWebview.obtainApp());
                                }
                                if (inputStream != null) {
                                    try {
                                        String str2 = new String(IOUtil.toString(inputStream));
                                        if (!TextUtils.isEmpty(str2)) {
                                            str = str + str2;
                                        }
                                    } catch (Exception unused) {
                                    }
                                } else if (jSCallback != null) {
                                    hashMap.put("type", -1);
                                    hashMap.put(NotificationCompat.CATEGORY_MESSAGE, string + DCLoudApplicationImpl.self().getContext().getString(R.string.dcloud_feature_weex_msg_cannot_find_file_by_path));
                                    jSCallback.invoke(hashMap);
                                }
                            }
                            if (!TextUtils.isEmpty(str)) {
                                WXBridgeManager.getInstance().syncExecJsOnInstanceWithResult(PlusModule.this.mWXSDKInstance.getInstanceId(), str, -1);
                                if (jSCallback != null) {
                                    hashMap.put("type", 1);
                                    jSCallback.invoke(hashMap);
                                }
                            }
                        } else if (jSCallback != null) {
                            hashMap.put("type", -1);
                            hashMap.put(NotificationCompat.CATEGORY_MESSAGE, DCLoudApplicationImpl.self().getContext().getString(R.string.dcloud_feature_weex_msg_page_destroyed));
                            jSCallback.invoke(hashMap);
                        }
                    }
                }, true);
            } else if (jSCallback != null) {
                hashMap.put("type", -1);
                hashMap.put(NotificationCompat.CATEGORY_MESSAGE, DCLoudApplicationImpl.self().getContext().getString(R.string.dcloud_feature_weex_msg_param_invalid));
                jSCallback.invoke(hashMap);
            }
        } else if (jSCallback != null) {
            hashMap.put("type", -1);
            hashMap.put(NotificationCompat.CATEGORY_MESSAGE, DCLoudApplicationImpl.self().getContext().getString(R.string.dcloud_feature_weex_msg_param_empty));
            jSCallback.invoke(hashMap);
        }
    }

    @JSMethod
    public void getHostInfo(JSCallback jSCallback) {
        JSONObject parseObject;
        if (jSCallback != null) {
            JSONObject jSONObject = new JSONObject();
            IWebview findWebview = WeexInstanceMgr.self().findWebview(this.mWXSDKInstance);
            if (findWebview == null || findWebview.obtainApp() == null) {
                jSCallback.invoke(jSONObject);
                return;
            }
            String obtainAppInfo = findWebview.obtainApp().obtainAppInfo();
            if (PdrUtil.isEmpty(obtainAppInfo)) {
                jSCallback.invoke(jSONObject);
                return;
            }
            if (PdrUtil.isUniMPHostForUniApp()) {
                if (!SDK.isUniMP) {
                    JSONObject parseObject2 = JSON.parseObject(obtainAppInfo);
                    if (parseObject2 != null) {
                        jSONObject.putAll(parseObject2);
                    }
                } else if (!PdrUtil.isEmpty(SDK.mHostInfo) && (parseObject = JSON.parseObject(SDK.mHostInfo)) != null) {
                    jSONObject.putAll(parseObject);
                }
            }
            jSONObject.put("nativeName", (Object) AndroidResources.appName);
            jSONObject.put("nativeAppid", (Object) AndroidResources.packageName);
            jSONObject.put("nativeVersionName", (Object) AndroidResources.versionName);
            jSONObject.put("nativeVersionCode", (Object) Integer.valueOf(AndroidResources.versionCode));
            jSCallback.invoke(jSONObject);
        }
    }

    @JSMethod(uiThread = false)
    public int getAppState() {
        IActivityHandler iActivityHandler;
        IWebview findWebview = WeexInstanceMgr.self().findWebview(this.mWXSDKInstance);
        if (findWebview == null || (iActivityHandler = DCloudAdapterUtil.getIActivityHandler(findWebview.getActivity())) == null) {
            return 0;
        }
        return iActivityHandler.getActivityState();
    }

    @JSMethod(uiThread = false)
    public JSONObject getSystemInfoSync() {
        JSONObject jSONObject = new JSONObject();
        try {
            IWebview findWebview = WeexInstanceMgr.self().findWebview(this.mWXSDKInstance);
            if (!(findWebview == null || findWebview.obtainApp() == null)) {
                jSONObject = JSON.parseObject(findWebview.obtainApp().getSystemInfo());
                boolean booleanValue = DeviceInfo.isSystemNightMode(findWebview.getActivity()).booleanValue();
                String str = DCBlurDraweeView.DARK;
                jSONObject.put("osTheme", (Object) booleanValue ? str : DCBlurDraweeView.LIGHT);
                jSONObject.put("ua", (Object) findWebview.getWebviewProperty(IWebview.USER_AGENT));
                String str2 = "";
                if (DeviceInfo.oaids != null) {
                    String[] split = DeviceInfo.oaids.split("\\|");
                    if (split.length > 0) {
                        str2 = split[0];
                    }
                    jSONObject.put("oaid", (Object) str2);
                } else {
                    jSONObject.put("oaid", (Object) str2);
                }
                jSONObject.put("browserVersion", (Object) WebViewFactory.getWebViewUserAgentVersion(findWebview.getContext()));
                String str3 = "portrait";
                if (findWebview.getActivity().getResources().getConfiguration().orientation == 2) {
                    str3 = "landscape";
                }
                jSONObject.put("deviceOrientation", (Object) str3);
                jSONObject.put("deviceId", (Object) TelephonyUtil.getDCloudDeviceID(findWebview.getActivity()));
                if (SDK.isUniMP) {
                    jSONObject.put("hostPackageName", (Object) findWebview.getContext().getPackageName());
                    jSONObject.put("hostVersion", (Object) AndroidResources.versionName);
                    jSONObject.put("hostName", (Object) AndroidResources.appName);
                    if (!SDK.hostAppThemeDark) {
                        str = DCBlurDraweeView.LIGHT;
                    }
                    jSONObject.put("hostTheme", (Object) str);
                    if (PdrUtil.isUniMPHostForUniApp()) {
                        jSONObject.put("hostLanguage", (Object) LanguageUtil.getDeviceDefLocalLanguage());
                        boolean z = SDK.isUniMP;
                    } else {
                        jSONObject.put("hostLanguage", (Object) LanguageUtil.getDeviceDefLocalLanguage());
                    }
                }
            }
        } catch (Exception unused) {
        }
        return jSONObject;
    }

    @JSMethod(uiThread = true)
    public void getSystemInfo(UniJSCallback uniJSCallback) {
        uniJSCallback.invoke(getSystemInfoSync());
    }

    @JSMethod(uiThread = false)
    public JSONObject getSystemSetting() {
        Context context = this.mWXSDKInstance.getContext();
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("bluetoothEnabled", (Object) Boolean.valueOf(DeviceInfo.blueToothEnable(context)));
        } catch (Exception e) {
            e.printStackTrace();
            jSONObject.put("bluetoothError", (Object) "Missing permissions required by BluetoothAdapter.isEnabled: android.permission.BLUETOOTH");
        }
        jSONObject.put("locationEnabled", (Object) Boolean.valueOf(DeviceInfo.locationEnable(context)));
        jSONObject.put("wifiEnabled", (Object) Boolean.valueOf(DeviceInfo.wifiEnable(context)));
        jSONObject.put("deviceOrientation", (Object) DeviceInfo.deviceOrientation(context));
        return jSONObject;
    }

    @JSMethod(uiThread = false)
    public JSONObject getAppAuthorizeSetting() {
        Context context = this.mWXSDKInstance.getContext();
        JSONObject jSONObject = new JSONObject();
        boolean checkPermissions = PermissionUtil.checkPermissions(context, new String[]{"android.permission.CAMERA"});
        String str = IApp.AUTHORITY_AUTHORIZED;
        String str2 = checkPermissions ? str : IApp.AUTHORITY_DENIED;
        String str3 = "config error";
        if (!checkPermissions && !PermissionUtil.hasDefinedInManifest(context, "android.permission.CAMERA")) {
            str2 = str3;
        }
        jSONObject.put("cameraAuthorized", (Object) str2);
        boolean checkPermissions2 = PermissionUtil.checkPermissions(context, new String[]{"android.permission.ACCESS_COARSE_LOCATION"});
        String str4 = checkPermissions2 ? str : IApp.AUTHORITY_DENIED;
        if (!checkPermissions2 && !PermissionUtil.hasDefinedInManifest(context, "android.permission.ACCESS_COARSE_LOCATION")) {
            str4 = str3;
        }
        jSONObject.put("locationAuthorized", (Object) str4);
        boolean checkPermissions3 = PermissionUtil.checkPermissions(context, new String[]{"android.permission.ACCESS_FINE_LOCATION"});
        String str5 = checkPermissions2 ? "reduced" : "unsupported";
        if (checkPermissions2 && checkPermissions3) {
            str5 = "full";
        }
        jSONObject.put("locationAccuracy", (Object) str5);
        boolean checkPermissions4 = PermissionUtil.checkPermissions(context, new String[]{"android.permission.RECORD_AUDIO"});
        String str6 = checkPermissions4 ? str : IApp.AUTHORITY_DENIED;
        if (checkPermissions4 || PermissionUtil.hasDefinedInManifest(context, "android.permission.RECORD_AUDIO")) {
            str3 = str6;
        }
        jSONObject.put("microphoneAuthorized", (Object) str3);
        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            str = IApp.AUTHORITY_DENIED;
        }
        jSONObject.put("notificationAuthorized", (Object) str);
        jSONObject.put("albumAuthorized", (Object) Constants.Name.UNDEFINED);
        jSONObject.put("bluetoothAuthorized", (Object) Constants.Name.UNDEFINED);
        jSONObject.put("locationReducedAccuracy", (Object) Constants.Name.UNDEFINED);
        jSONObject.put("notificationAlertAuthorized", (Object) Constants.Name.UNDEFINED);
        jSONObject.put("notificationBadgeAuthorized", (Object) Constants.Name.UNDEFINED);
        jSONObject.put("notificationSoundAuthorized", (Object) Constants.Name.UNDEFINED);
        return jSONObject;
    }

    @JSMethod(uiThread = false)
    public void openAppAuthorizeSetting(JSCallback jSCallback) {
        JSONObject jSONObject = new JSONObject();
        try {
            Context context = this.mWXSDKInstance.getContext();
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), (String) null));
            context.startActivity(intent);
            jSONObject.put("type", (Object) WXImage.SUCCEED);
            jSONObject.put("code", (Object) 0);
        } catch (Exception e) {
            e.printStackTrace();
            jSONObject.put("type", (Object) Constants.Event.FAIL);
        }
        jSCallback.invoke(jSONObject);
    }

    @JSMethod(uiThread = false)
    public void hasClientKey(JSONObject jSONObject, JSCallback jSCallback) {
        if (SDK.isUniMPSDK() || BaseInfo.isStandardBase(this.mWXSDKInstance.getContext())) {
            jSCallback.invoke(Result.boxFailResult(SecureNetworkResult.NOT_SUPPORT_MP_OR_BASE));
            return;
        }
        String str = (String) jSONObject.get("spaceId");
        String str2 = (String) jSONObject.get("provider");
        if (PdrUtil.isEmpty(str) || PdrUtil.isEmpty(str2)) {
            jSCallback.invoke(Result.boxFailResult(SecureNetworkResult.PARAMS_IS_NULL));
        } else {
            jSCallback.invoke(Result.boxSuccessResult(Boolean.valueOf(!PdrUtil.isEmpty(getClientKey(str, str2)))));
        }
    }

    @JSMethod(uiThread = false)
    public void encryptGetClientKeyPayload(JSONObject jSONObject, JSCallback jSCallback) {
        if (SDK.isUniMPSDK() || BaseInfo.isStandardBase(this.mWXSDKInstance.getContext())) {
            jSCallback.invoke(Result.boxFailResult(SecureNetworkResult.NOT_SUPPORT_MP_OR_BASE));
        } else if (jSONObject != null && jSCallback != null) {
            String metaValue = AndroidResources.getMetaValue("dcloud_sn_appkey");
            String jSONString = jSONObject.toJSONString();
            if (PdrUtil.isEmpty(metaValue)) {
                jSCallback.invoke(Result.boxFailResult(SecureNetworkResult.APP_KEY_IS_NULL));
            } else if (!PdrUtil.isEmpty(jSONString)) {
                String encryptGetClientKeyPayload = WXBridgeManager.getInstance().encryptGetClientKeyPayload(BaseInfo.sDefaultBootApp, jSONString, metaValue);
                if (PdrUtil.isEmpty(encryptGetClientKeyPayload)) {
                    jSCallback.invoke(Result.boxFailResult(SecureNetworkResult.ENCRYPT_CLIENT_KEY_PAYLOAD_ERROR));
                    return;
                }
                JSONObject parseObject = JSON.parseObject(encryptGetClientKeyPayload);
                if (parseObject != null) {
                    jSCallback.invoke(Result.boxSuccessResult(parseObject));
                } else {
                    jSCallback.invoke(Result.boxFailResult(SecureNetworkResult.NATIVE_JSON_FORMAT_ERROR));
                }
            } else {
                jSCallback.invoke(Result.boxFailResult(SecureNetworkResult.JSON_FORMAT_ERROR));
            }
        } else if (jSCallback != null) {
            jSCallback.invoke(Result.boxFailResult(SecureNetworkResult.PARAMS_IS_NULL));
        }
    }

    @JSMethod(uiThread = false)
    public void setClientKey(JSONObject jSONObject, JSCallback jSCallback) {
        if (SDK.isUniMPSDK() || BaseInfo.isStandardBase(this.mWXSDKInstance.getContext())) {
            jSCallback.invoke(Result.boxFailResult(SecureNetworkResult.NOT_SUPPORT_MP_OR_BASE));
        } else if (jSONObject != null) {
            String metaValue = AndroidResources.getMetaValue("dcloud_sn_appkey");
            String jSONString = jSONObject.toJSONString();
            String str = (String) jSONObject.get("spaceId");
            String str2 = (String) jSONObject.get("provider");
            if (PdrUtil.isEmpty(str) || PdrUtil.isEmpty(str2)) {
                jSCallback.invoke(Result.boxFailResult(SecureNetworkResult.PARAMS_IS_NULL));
            } else if (PdrUtil.isEmpty(metaValue)) {
                jSCallback.invoke(Result.boxFailResult(SecureNetworkResult.APP_KEY_IS_NULL));
            } else if (WXBridgeManager.getInstance().verifyClientKeyPayload(BaseInfo.sDefaultBootApp, metaValue, jSONString)) {
                Context context = this.mWXSDKInstance.getContext();
                SharedPreferences.Editor edit = context.getSharedPreferences(HELP_LOG_HASH + BaseInfo.sDefaultBootApp + str2 + str, 0).edit();
                edit.putString("HELP_LOG_HASH", jSONString);
                edit.commit();
                jSCallback.invoke(Result.boxSuccessResult(true));
            } else {
                jSCallback.invoke(Result.boxFailResult(SecureNetworkResult.CLIENT_KEY_ILLEGAL));
            }
        } else if (jSCallback != null) {
            jSCallback.invoke(Result.boxFailResult(SecureNetworkResult.PARAMS_IS_NULL));
        }
    }

    private String getClientKey(String str, String str2) {
        Context context = this.mWXSDKInstance.getContext();
        return context.getSharedPreferences(HELP_LOG_HASH + BaseInfo.sDefaultBootApp + str2 + str, 0).getString("HELP_LOG_HASH", "");
    }

    @JSMethod(uiThread = false)
    public void encrypt(JSONObject jSONObject, JSCallback jSCallback) {
        if (SDK.isUniMPSDK() || BaseInfo.isStandardBase(this.mWXSDKInstance.getContext())) {
            jSCallback.invoke(Result.boxFailResult(SecureNetworkResult.NOT_SUPPORT_MP_OR_BASE));
        } else if (jSONObject != null && jSCallback != null) {
            String metaValue = AndroidResources.getMetaValue("dcloud_sn_appkey");
            String str = (String) jSONObject.get("data");
            String str2 = (String) jSONObject.get("spaceId");
            String str3 = (String) jSONObject.get("provider");
            if (PdrUtil.isEmpty(str) || PdrUtil.isEmpty(str2) || PdrUtil.isEmpty(str3)) {
                jSCallback.invoke(Result.boxFailResult(SecureNetworkResult.PARAMS_IS_NULL));
                return;
            }
            String clientKey = getClientKey(str2, str3);
            if (PdrUtil.isEmpty(metaValue)) {
                jSCallback.invoke(Result.boxFailResult(SecureNetworkResult.APP_KEY_IS_NULL));
            } else if (PdrUtil.isEmpty(clientKey)) {
                jSCallback.invoke(Result.boxFailResult(SecureNetworkResult.CLIENT_KEY_IS_NULL));
            } else {
                String encrypt = WXBridgeManager.getInstance().encrypt(BaseInfo.sDefaultBootApp, str, metaValue, clientKey);
                if (PdrUtil.isEmpty(encrypt)) {
                    jSCallback.invoke(Result.boxFailResult(SecureNetworkResult.ENCRYPT_ERROR));
                } else {
                    jSCallback.invoke(Result.boxSuccessResult(JSON.parse(encrypt)));
                }
            }
        } else if (jSCallback != null) {
            jSCallback.invoke(Result.boxFailResult(SecureNetworkResult.PARAMS_IS_NULL));
        }
    }

    @JSMethod(uiThread = false)
    public void decrypt(JSONObject jSONObject, JSCallback jSCallback) {
        if (SDK.isUniMPSDK() || BaseInfo.isStandardBase(this.mWXSDKInstance.getContext())) {
            jSCallback.invoke(Result.boxFailResult(SecureNetworkResult.NOT_SUPPORT_MP_OR_BASE));
        } else if (jSONObject != null && jSCallback != null) {
            String metaValue = AndroidResources.getMetaValue("dcloud_sn_appkey");
            String str = (String) jSONObject.get(IApp.ConfigProperty.CONFIG_KEY);
            String str2 = (String) jSONObject.get("spaceId");
            String str3 = (String) jSONObject.get("provider");
            if (PdrUtil.isEmpty((String) jSONObject.get("data")) || PdrUtil.isEmpty(str) || PdrUtil.isEmpty(str2) || PdrUtil.isEmpty(str3)) {
                jSCallback.invoke(Result.boxFailResult(SecureNetworkResult.PARAMS_IS_NULL));
                return;
            }
            String clientKey = getClientKey(str2, str3);
            if (PdrUtil.isEmpty(metaValue)) {
                jSCallback.invoke(Result.boxFailResult(SecureNetworkResult.APP_KEY_IS_NULL));
            } else if (PdrUtil.isEmpty(clientKey)) {
                jSCallback.invoke(Result.boxFailResult(SecureNetworkResult.CLIENT_KEY_IS_NULL));
            } else {
                String decrypt = WXBridgeManager.getInstance().decrypt(BaseInfo.sDefaultBootApp, jSONObject.toJSONString(), metaValue, clientKey);
                if (PdrUtil.isEmpty(decrypt)) {
                    jSCallback.invoke(Result.boxFailResult(SecureNetworkResult.DECRYPT_ERROR));
                    return;
                }
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("data", (Object) decrypt);
                jSCallback.invoke(Result.boxSuccessResult(jSONObject2));
            }
        } else if (jSCallback != null) {
            jSCallback.invoke(Result.boxFailResult(SecureNetworkResult.PARAMS_IS_NULL));
        }
    }
}
