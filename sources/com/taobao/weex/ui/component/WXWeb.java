package com.taobao.weex.ui.component;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.Constants;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.view.IWebView;
import com.taobao.weex.ui.view.WXWebView;
import com.taobao.weex.utils.WXUtils;
import java.util.HashMap;
import java.util.Map;

@Component(lazyload = false)
public class WXWeb extends WXComponent {
    public static final String GO_BACK = "goBack";
    public static final String GO_FORWARD = "goForward";
    public static final String POST_MESSAGE = "postMessage";
    public static final String RELOAD = "reload";
    protected IWebView mWebView;

    @Deprecated
    public WXWeb(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, String str, boolean z, BasicComponentData basicComponentData) {
        this(wXSDKInstance, wXVContainer, z, basicComponentData);
    }

    public WXWeb(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, z, basicComponentData);
        createWebView();
    }

    /* access modifiers changed from: protected */
    public void createWebView() {
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
        this.mWebView = new WXWebView(getContext(), str);
    }

    /* access modifiers changed from: protected */
    public View initComponentHostView(Context context) {
        this.mWebView.setOnErrorListener(new IWebView.OnErrorListener() {
            public void onError(String str, Object obj) {
                WXWeb.this.fireEvent(str, obj);
            }
        });
        this.mWebView.setOnPageListener(new IWebView.OnPageListener() {
            public void onReceivedTitle(String str) {
                if (WXWeb.this.getEvents().contains(Constants.Event.RECEIVEDTITLE)) {
                    HashMap hashMap = new HashMap();
                    hashMap.put(AbsoluteConst.JSON_KEY_TITLE, str);
                    WXWeb.this.fireEvent(Constants.Event.RECEIVEDTITLE, hashMap);
                }
            }

            public void onPageStart(String str) {
                if (WXWeb.this.getEvents().contains(Constants.Event.PAGESTART)) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("url", str);
                    WXWeb.this.fireEvent(Constants.Event.PAGESTART, hashMap);
                }
            }

            public void onPageFinish(String str, boolean z, boolean z2) {
                if (WXWeb.this.getEvents().contains(Constants.Event.PAGEFINISH)) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("url", str);
                    hashMap.put("canGoBack", Boolean.valueOf(z));
                    hashMap.put("canGoForward", Boolean.valueOf(z2));
                    WXWeb.this.fireEvent(Constants.Event.PAGEFINISH, hashMap);
                }
            }
        });
        this.mWebView.setOnMessageListener(new IWebView.OnMessageListener() {
            public void onMessage(Map<String, Object> map) {
                WXWeb.this.fireEvent("message", map);
            }
        });
        return this.mWebView.getView();
    }

    public void destroy() {
        super.destroy();
        getWebView().destroy();
        this.mWebView = null;
    }

    /* access modifiers changed from: protected */
    public boolean setProperty(String str, Object obj) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -896505829:
                if (str.equals("source")) {
                    c = 0;
                    break;
                }
                break;
            case 114148:
                if (str.equals("src")) {
                    c = 1;
                    break;
                }
                break;
            case 537088620:
                if (str.equals(Constants.Name.SHOW_LOADING)) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                String string = WXUtils.getString(obj, (String) null);
                if (string != null) {
                    setSource(string);
                }
                return true;
            case 1:
                String string2 = WXUtils.getString(obj, (String) null);
                if (string2 != null) {
                    setUrl(string2);
                }
                return true;
            case 2:
                Boolean bool = WXUtils.getBoolean(obj, (Boolean) null);
                if (bool != null) {
                    setShowLoading(bool.booleanValue());
                }
                return true;
            default:
                return super.setProperty(str, obj);
        }
    }

    @WXComponentProp(name = "show-loading")
    public void setShowLoading(boolean z) {
        getWebView().setShowLoading(z);
    }

    @WXComponentProp(name = "src")
    public void setUrl(String str) {
        if (!TextUtils.isEmpty(str) && getHostView() != null && !TextUtils.isEmpty(str)) {
            loadUrl(getInstance().rewriteUri(Uri.parse(str), "web").toString());
        }
    }

    @WXComponentProp(name = "source")
    public void setSource(String str) {
        if (!TextUtils.isEmpty(str) && getHostView() != null) {
            loadDataWithBaseURL(str);
        }
    }

    public void setAction(String str, Object obj) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (str.equals(GO_BACK)) {
            goBack();
        } else if (str.equals(GO_FORWARD)) {
            goForward();
        } else if (str.equals(RELOAD)) {
            reload();
        } else if (str.equals(POST_MESSAGE)) {
            postMessage(obj);
        }
    }

    /* access modifiers changed from: private */
    public void fireEvent(String str, Object obj) {
        if (getEvents().contains("error")) {
            HashMap hashMap = new HashMap();
            hashMap.put("type", str);
            hashMap.put("errorMsg", obj);
            fireEvent("error", hashMap);
        }
    }

    /* access modifiers changed from: protected */
    public void loadUrl(String str) {
        getWebView().loadUrl(str);
    }

    private void loadDataWithBaseURL(String str) {
        getWebView().loadDataWithBaseURL(str);
    }

    @JSMethod
    public void reload() {
        getWebView().reload();
    }

    @JSMethod
    public void goForward() {
        getWebView().goForward();
    }

    @JSMethod
    public void goBack() {
        getWebView().goBack();
    }

    @JSMethod
    public void postMessage(Object obj) {
        getWebView().postMessage(obj);
    }

    private IWebView getWebView() {
        return this.mWebView;
    }
}
