package com.taobao.weex.ui.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.common.Constants;
import com.taobao.weex.ui.component.WXImage;
import com.taobao.weex.ui.component.WXWeb;
import com.taobao.weex.ui.view.IWebView;
import com.taobao.weex.utils.WXLogUtils;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.adapter.ui.webview.WebViewFactory;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class WXWebView implements IWebView {
    private static final String BRIDGE_NAME = "__WEEX_WEB_VIEW_BRIDGE";
    /* access modifiers changed from: private */
    public static final boolean DOWNGRADE_JS_INTERFACE;
    private static final int POST_MESSAGE = 1;
    private static final int SDK_VERSION;
    private Context mContext;
    private Handler mMessageHandler;
    /* access modifiers changed from: private */
    public IWebView.OnErrorListener mOnErrorListener;
    /* access modifiers changed from: private */
    public IWebView.OnMessageListener mOnMessageListener;
    /* access modifiers changed from: private */
    public IWebView.OnPageListener mOnPageListener;
    private String mOrigin;
    private ProgressBar mProgressBar;
    private boolean mShowLoading = true;
    private WebView mWebView;

    public void onActivityResult(int i, int i2, Intent intent) {
    }

    static {
        int i = Build.VERSION.SDK_INT;
        SDK_VERSION = i;
        DOWNGRADE_JS_INTERFACE = i < 17;
    }

    public WXWebView(Context context, String str) {
        this.mContext = context;
        this.mOrigin = str;
    }

    public View getView() {
        FrameLayout frameLayout = new FrameLayout(this.mContext);
        frameLayout.setBackgroundColor(-1);
        this.mWebView = new WebView(this.mContext);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        layoutParams.gravity = 17;
        this.mWebView.setLayoutParams(layoutParams);
        frameLayout.addView(this.mWebView);
        initWebView(this.mWebView);
        this.mProgressBar = new ProgressBar(this.mContext);
        showProgressBar(false);
        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(-2, -2);
        this.mProgressBar.setLayoutParams(layoutParams2);
        layoutParams2.gravity = 17;
        frameLayout.addView(this.mProgressBar);
        this.mMessageHandler = new MessageHandler();
        return frameLayout;
    }

    public void destroy() {
        if (getWebView() != null) {
            getWebView().setWebViewClient((WebViewClient) null);
            getWebView().setWebChromeClient((WebChromeClient) null);
            getWebView().removeAllViews();
            getWebView().destroy();
            this.mWebView = null;
            this.mOnMessageListener = null;
            this.mOnPageListener = null;
            this.mOnErrorListener = null;
        }
    }

    public void loadUrl(String str) {
        if (getWebView() != null) {
            getWebView().loadUrl(str);
        }
    }

    public void loadDataWithBaseURL(String str) {
        if (getWebView() != null) {
            getWebView().loadDataWithBaseURL(this.mOrigin, str, "text/html", "utf-8", (String) null);
        }
    }

    public void reload() {
        if (getWebView() != null) {
            getWebView().reload();
        }
    }

    public void goBack() {
        if (getWebView() != null) {
            getWebView().goBack();
        }
    }

    public void goForward() {
        if (getWebView() != null) {
            getWebView().goForward();
        }
    }

    public void postMessage(Object obj) {
        if (getWebView() != null) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("type", (Object) "message");
                jSONObject.put("data", obj);
                jSONObject.put("origin", (Object) this.mOrigin);
                evaluateJS("javascript:(function () {var initData = " + jSONObject.toString() + ";try {var event = new MessageEvent('message', initData);window.dispatchEvent(event);} catch (e) {}})();");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setShowLoading(boolean z) {
        this.mShowLoading = z;
    }

    public void setOnErrorListener(IWebView.OnErrorListener onErrorListener) {
        this.mOnErrorListener = onErrorListener;
    }

    public void setOnPageListener(IWebView.OnPageListener onPageListener) {
        this.mOnPageListener = onPageListener;
    }

    public void setOnMessageListener(IWebView.OnMessageListener onMessageListener) {
        this.mOnMessageListener = onMessageListener;
    }

    /* access modifiers changed from: private */
    public void showProgressBar(boolean z) {
        if (this.mShowLoading) {
            this.mProgressBar.setVisibility(z ? 0 : 8);
        }
    }

    /* access modifiers changed from: private */
    public void showWebView(boolean z) {
        this.mWebView.setVisibility(z ? 0 : 4);
    }

    private WebView getWebView() {
        return this.mWebView;
    }

    private void initWebView(WebView webView) {
        WebSettings settings = webView.getSettings();
        WebViewFactory.openJSEnabled(settings, (IApp) null);
        settings.setAppCacheEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setDomStorageEnabled(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setAllowFileAccess(false);
        settings.setSavePassword(false);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                webView.loadUrl(str);
                WXLogUtils.v("tag", "onPageOverride " + str);
                return true;
            }

            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                super.onPageStarted(webView, str, bitmap);
                WXLogUtils.v("tag", "onPageStarted " + str);
                if (WXWebView.this.mOnPageListener != null) {
                    WXWebView.this.mOnPageListener.onPageStart(str);
                }
            }

            public void onPageFinished(WebView webView, String str) {
                super.onPageFinished(webView, str);
                WXLogUtils.v("tag", "onPageFinished " + str);
                if (WXWebView.this.mOnPageListener != null) {
                    WXWebView.this.mOnPageListener.onPageFinish(str, webView.canGoBack(), webView.canGoForward());
                }
                if (WXWebView.this.mOnMessageListener != null) {
                    WXWebView wXWebView = WXWebView.this;
                    StringBuilder sb = new StringBuilder();
                    sb.append("javascript:(window.postMessage = function(message, targetOrigin) {if (message == null || !targetOrigin) return;");
                    sb.append(WXWebView.DOWNGRADE_JS_INTERFACE ? "prompt('__WEEX_WEB_VIEW_BRIDGE://postMessage?message=' + JSON.stringify(message) + '&targetOrigin=' + targetOrigin)" : "__WEEX_WEB_VIEW_BRIDGE.postMessage(JSON.stringify(message), targetOrigin);");
                    sb.append("})");
                    wXWebView.evaluateJS(sb.toString());
                }
            }

            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
                if (WXWebView.this.mOnErrorListener != null) {
                    WXWebView.this.mOnErrorListener.onError("error", "page error");
                }
            }

            public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
                super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
                if (WXWebView.this.mOnErrorListener != null) {
                    WXWebView.this.mOnErrorListener.onError("error", "http error");
                }
            }

            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                super.onReceivedSslError(webView, sslErrorHandler, sslError);
                if (WXWebView.this.mOnErrorListener != null) {
                    WXWebView.this.mOnErrorListener.onError("error", "ssl error");
                }
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                boolean z = true;
                WXWebView.this.showWebView(i == 100);
                WXWebView wXWebView = WXWebView.this;
                if (i == 100) {
                    z = false;
                }
                wXWebView.showProgressBar(z);
                WXLogUtils.v("tag", "onPageProgressChanged " + i);
            }

            public void onReceivedTitle(WebView webView, String str) {
                super.onReceivedTitle(webView, str);
                if (WXWebView.this.mOnPageListener != null) {
                    WXWebView.this.mOnPageListener.onReceivedTitle(webView.getTitle());
                }
            }

            public boolean onJsPrompt(WebView webView, String str, String str2, String str3, JsPromptResult jsPromptResult) {
                Uri parse = Uri.parse(str2);
                if (!TextUtils.equals(parse.getScheme(), WXWebView.BRIDGE_NAME)) {
                    return super.onJsPrompt(webView, str, str2, str3, jsPromptResult);
                }
                if (TextUtils.equals(parse.getAuthority(), WXWeb.POST_MESSAGE)) {
                    WXWebView.this.onMessage(parse.getQueryParameter("message"), parse.getQueryParameter("targetOrigin"));
                    jsPromptResult.confirm(WXImage.SUCCEED);
                    return true;
                }
                jsPromptResult.confirm(Constants.Event.FAIL);
                return true;
            }
        });
        if (!DOWNGRADE_JS_INTERFACE) {
            webView.addJavascriptInterface(new Object() {
                @JavascriptInterface
                public void postMessage(String str, String str2) {
                    WXWebView.this.onMessage(str, str2);
                }
            }, BRIDGE_NAME);
        }
    }

    /* access modifiers changed from: private */
    public void onMessage(String str, String str2) {
        if (str != null && str2 != null && this.mOnMessageListener != null) {
            try {
                HashMap hashMap = new HashMap();
                hashMap.put("data", JSON.parse(str));
                hashMap.put("origin", str2);
                hashMap.put("type", "message");
                Message message = new Message();
                message.what = 1;
                message.obj = hashMap;
                this.mMessageHandler.sendMessage(message);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /* access modifiers changed from: private */
    public void evaluateJS(String str) {
        if (SDK_VERSION < 19) {
            this.mWebView.loadUrl(str);
        } else {
            this.mWebView.evaluateJavascript(str, (ValueCallback) null);
        }
    }

    private static class MessageHandler extends Handler {
        private final WeakReference<WXWebView> mWv;

        private MessageHandler(WXWebView wXWebView) {
            this.mWv = new WeakReference<>(wXWebView);
        }

        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == 1 && this.mWv.get() != null && ((WXWebView) this.mWv.get()).mOnMessageListener != null) {
                ((WXWebView) this.mWv.get()).mOnMessageListener.onMessage((Map) message.obj);
            }
        }
    }
}
