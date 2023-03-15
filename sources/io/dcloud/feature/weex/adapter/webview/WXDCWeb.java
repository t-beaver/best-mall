package io.dcloud.feature.weex.adapter.webview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.Constants;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.component.WXWeb;
import com.taobao.weex.ui.view.IWebView;
import com.taobao.weex.utils.WXResourceUtils;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaWebview;
import io.dcloud.common.adapter.ui.webview.WebViewFactory;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.feature.internal.sdk.SDK;
import io.dcloud.feature.weex.WeexInstanceMgr;
import java.util.HashMap;
import java.util.Map;

@Component(lazyload = false)
public class WXDCWeb extends WXWeb {
    IDCWebView mDCWebView;
    private JSONObject mWebStyles;

    public interface OnDCMessageListener {
        void onMessage(Map<String, Object> map, int i);
    }

    public WXDCWeb(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, z, basicComponentData);
    }

    /* access modifiers changed from: protected */
    public void createWebView() {
        Object newInstance;
        String str = null;
        try {
            Uri parse = Uri.parse(WXSDKManager.getInstance().getSDKInstance(getInstanceId()).getBundleUrl());
            String scheme = parse.getScheme();
            String authority = parse.getAuthority();
            if (!TextUtils.isEmpty(scheme) && !TextUtils.isEmpty(authority)) {
                str = scheme + "://" + authority;
            }
        } catch (Exception unused) {
        }
        if (WebViewFactory.isIsOtherInitSuccess() && (newInstance = PlatformUtil.newInstance("io.dcloud.feature.x5.DCWXX5WebView", new Class[]{Context.class, String.class, WXDCWeb.class}, new Object[]{getInstance().getUIContext(), str, this})) != null && (newInstance instanceof IWebView)) {
            IDCWebView iDCWebView = (IDCWebView) newInstance;
            this.mDCWebView = iDCWebView;
            this.mWebView = iDCWebView;
        }
        if (this.mWebView == null) {
            DCWXWebView dCWXWebView = new DCWXWebView(getInstance().getUIContext(), str, this);
            this.mDCWebView = dCWXWebView;
            this.mWebView = dCWXWebView;
        }
    }

    /* access modifiers changed from: protected */
    public View initComponentHostView(Context context) {
        AnonymousClass1 r6 = new OnDCMessageListener() {
            public void onMessage(Map<String, Object> map, int i) {
                WXSDKInstance findWXSDKInstance;
                if (i == 1) {
                    WXDCWeb.this.fireEvent("onPostMessage", map);
                } else if (i == 2 && (findWXSDKInstance = WeexInstanceMgr.self().findWXSDKInstance("__uniapp__service")) != null) {
                    map.put("ref", WXDCWeb.this.getRef());
                    map.put("id", WXDCWeb.this.getInstance().getInstanceId());
                    findWXSDKInstance.fireGlobalEventCallback("WebviewPostMessage", map);
                }
            }
        };
        PlatformUtil.invokeMethod(this.mWebView, "setOnDCMessageListener", new Class[]{OnDCMessageListener.class}, r6);
        this.mWebView.setOnErrorListener(new IWebView.OnErrorListener() {
            public void onError(String str, Object obj) {
                WXDCWeb.this.fireEvent(str, obj);
            }
        });
        this.mWebView.setOnPageListener(new IWebView.OnPageListener() {
            public void onReceivedTitle(String str) {
                if (WXDCWeb.this.getEvents().contains(Constants.Event.RECEIVEDTITLE)) {
                    HashMap hashMap = new HashMap();
                    hashMap.put(AbsoluteConst.JSON_KEY_TITLE, str);
                    HashMap hashMap2 = new HashMap();
                    hashMap2.put("detail", hashMap);
                    WXDCWeb.this.fireEvent(Constants.Event.RECEIVEDTITLE, hashMap2);
                }
            }

            public void onPageStart(String str) {
                if (WXDCWeb.this.getEvents().contains(Constants.Event.PAGESTART)) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("url", str);
                    HashMap hashMap2 = new HashMap();
                    hashMap2.put("detail", hashMap);
                    WXDCWeb.this.fireEvent(Constants.Event.PAGESTART, hashMap2);
                }
            }

            public void onPageFinish(String str, boolean z, boolean z2) {
                if (WXDCWeb.this.getEvents().contains(Constants.Event.PAGEFINISH)) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("url", str);
                    hashMap.put("canGoBack", Boolean.valueOf(z));
                    hashMap.put("canGoForward", Boolean.valueOf(z2));
                    HashMap hashMap2 = new HashMap();
                    hashMap2.put("detail", hashMap);
                    WXDCWeb.this.fireEvent(Constants.Event.PAGEFINISH, hashMap2);
                }
            }
        });
        this.mWebView.setOnMessageListener(new IWebView.OnMessageListener() {
            public void onMessage(Map<String, Object> map) {
                HashMap hashMap = new HashMap();
                hashMap.put("detail", map);
                WXDCWeb.this.fireEvent("message", hashMap);
            }
        });
        View view = this.mWebView.getView();
        if (!TextUtils.isEmpty(AdaWebview.sCustomUserAgent)) {
            this.mDCWebView.setUserAgent(AdaWebview.sCustomUserAgent, true);
        } else {
            IWebview findWebview = WeexInstanceMgr.self().findWebview(getInstance());
            if (findWebview != null) {
                this.mDCWebView.setUserAgent(findWebview.obtainApp().obtainConfigProperty(IApp.ConfigProperty.CONFIG_USER_AGENT), !Boolean.parseBoolean(findWebview.obtainApp().obtainConfigProperty(IApp.ConfigProperty.CONFIG_CONCATENATE)));
            }
        }
        return view;
    }

    /* access modifiers changed from: private */
    public void fireEvent(String str, Object obj) {
        if (getEvents().contains("error")) {
            HashMap hashMap = new HashMap();
            hashMap.put("type", str);
            hashMap.put("errorMsg", obj);
            HashMap hashMap2 = new HashMap();
            hashMap2.put("detail", hashMap);
            fireEvent("error", hashMap2);
        }
    }

    @JSMethod
    public void evalJs(String str) {
        if (this.mWebView != null) {
            if (!str.startsWith("javascript:(function(){")) {
                str = "javascript:(function(){" + str + ";})();";
            }
            this.mWebView.loadUrl(str);
        }
    }

    @JSMethod
    public void evalJS(String str) {
        evalJs(str);
    }

    /* access modifiers changed from: protected */
    public void loadUrl(String str) {
        if (!TextUtils.isEmpty(str) && str.startsWith("asset:///")) {
            str = str.replace("asset:///", SDK.ANDROID_ASSET);
        }
        super.loadUrl(str);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (this.mWebView != null) {
            this.mWebView.onActivityResult(i, i2, intent);
        }
    }

    @WXComponentProp(name = "webviewStyles")
    public void webviewStyles(String str) {
        if (this.mWebStyles == null) {
            this.mWebStyles = new JSONObject();
        }
        JSONObject parseObject = JSON.parseObject(str);
        if (parseObject != null && parseObject.containsKey("progress")) {
            Object obj = parseObject.get("progress");
            if (obj instanceof Boolean) {
                this.mWebStyles.put("isProgress", (Object) Boolean.valueOf(((Boolean) obj).booleanValue()));
            } else if (obj instanceof JSONObject) {
                JSONObject jSONObject = (JSONObject) obj;
                this.mWebStyles.put("isProgress", (Object) true);
                if (jSONObject.containsKey("color")) {
                    this.mWebStyles.put("progressColor", (Object) jSONObject.getString("color"));
                }
            }
        }
    }

    public JSONObject getWebStyles() {
        if (this.mWebStyles == null) {
            JSONObject jSONObject = new JSONObject();
            this.mWebStyles = jSONObject;
            jSONObject.put("isProgress", (Object) true);
        }
        return this.mWebStyles;
    }

    public void setBackgroundColor(String str) {
        super.setBackgroundColor(str);
        if (!TextUtils.isEmpty(str) && this.mDCWebView != null) {
            int color = WXResourceUtils.getColor(str);
            if (this.mDCWebView.getWebView() != null) {
                this.mDCWebView.getWebView().setBackgroundColor(color);
            }
        }
    }

    public void destroy() {
        super.destroy();
        this.mDCWebView = null;
    }
}
