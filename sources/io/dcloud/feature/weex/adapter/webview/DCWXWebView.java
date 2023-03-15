package io.dcloud.feature.weex.adapter.webview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.PermissionRequest;
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
import androidx.core.content.FileProvider;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.dcloud.android.widget.DCWebViewProgressBar;
import com.dcloud.zxing2.common.StringUtils;
import com.facebook.common.util.UriUtil;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.ui.view.IWebView;
import com.taobao.weex.utils.WXLogUtils;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.IActivityDelegate;
import io.dcloud.common.DHInterface.IActivityHandler;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.FileChooseDialog;
import io.dcloud.common.adapter.ui.webview.WebViewFactory;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.PermissionUtil;
import io.dcloud.common.ui.blur.AppEventForBlurManager;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.feature.weex.WeexInstanceMgr;
import io.dcloud.feature.weex.adapter.webview.WXDCWeb;
import io.dcloud.feature.weex.adapter.webview.video.FullscreenHolder;
import io.src.dcloud.adapter.DCloudAdapterUtil;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;

public class DCWXWebView implements IDCWebView {
    private static final String BRIDGE_NAME = "__dcloud_weex_";
    private static final int POST_MESSAGE = 1;
    private static final int POST_MESSAGE_TO_CONTROL = 2;
    private static final int SDK_VERSION = Build.VERSION.SDK_INT;
    DCWXChromeClient chromeClient;
    private View customView;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private int defaultSystemUI = 0;
    private FrameLayout fullscreenContainer;
    /* access modifiers changed from: private */
    public boolean isStart = false;
    /* access modifiers changed from: private */
    public Context mContext;
    private WeakReference<WXDCWeb> mDCWeb;
    private String mDefUserAgent;
    private Handler mMessageHandler;
    /* access modifiers changed from: private */
    public IWebView.OnErrorListener mOnErrorListener;
    /* access modifiers changed from: private */
    public WXDCWeb.OnDCMessageListener mOnMessageListener;
    /* access modifiers changed from: private */
    public IWebView.OnPageListener mOnPageListener;
    private String mOrigin;
    public int mProgress = 0;
    private FrameLayout mRootView;
    private boolean mShowLoading = true;
    String mSslType = "refuse";
    /* access modifiers changed from: private */
    public DCWebViewProgressBar mWebProgressView;
    private WebView mWebView;

    public void setOnMessageListener(IWebView.OnMessageListener onMessageListener) {
    }

    public DCWXWebView(Context context, String str, WXDCWeb wXDCWeb) {
        this.mContext = context;
        this.mOrigin = str;
        this.mDCWeb = new WeakReference<>(wXDCWeb);
        IWebview findWebview = WeexInstanceMgr.self().findWebview(wXDCWeb.getInstance());
        if (findWebview != null && findWebview.obtainApp() != null) {
            this.mSslType = findWebview.obtainApp().obtainConfigProperty(IApp.ConfigProperty.CONFIG_UNTRUSTEDCA);
        }
    }

    public View getView() {
        FrameLayout frameLayout = new FrameLayout(this.mContext);
        this.mRootView = frameLayout;
        frameLayout.setBackgroundColor(-1);
        this.mWebView = new WebView(this.mContext) {
            /* access modifiers changed from: protected */
            public void onScrollChanged(int i, int i2, int i3, int i4) {
                super.onScrollChanged(i, i2, i3, i4);
                AppEventForBlurManager.onScrollChanged(i, i2);
            }
        };
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        layoutParams.gravity = 17;
        this.mWebView.setLayoutParams(layoutParams);
        this.mRootView.addView(this.mWebView);
        this.mWebView.setBackgroundColor(0);
        initWebView(this.mWebView);
        this.mMessageHandler = new MessageHandler();
        return this.mRootView;
    }

    public void destroy() {
        if (getWebView() != null) {
            hideCustomView();
            getWebView().setWebViewClient((WebViewClient) null);
            getWebView().setWebChromeClient((WebChromeClient) null);
            getWebView().removeAllViews();
            getWebView().destroy();
            this.mWebView = null;
            this.mDCWeb.clear();
            this.mOnMessageListener = null;
            this.mWebProgressView = null;
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
                evaluateJS("javascript:(function () {var initData = " + jSONObject.toString() + ";try {var event = new MessageEvent('onPostMessage', initData);window.dispatchEvent(event);} catch (e) {}})();");
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

    public void onActivityResult(int i, int i2, Intent intent) {
        DCWXChromeClient dCWXChromeClient = this.chromeClient;
        if (dCWXChromeClient != null) {
            dCWXChromeClient.onResult(i, i2, intent);
        }
    }

    public void setOnDCMessageListener(WXDCWeb.OnDCMessageListener onDCMessageListener) {
        this.mOnMessageListener = onDCMessageListener;
    }

    private void showWebView(boolean z) {
        this.mWebView.setVisibility(z ? 0 : 4);
    }

    public WebView getWebView() {
        return this.mWebView;
    }

    public void setUserAgent(String str, boolean z) {
        WebView webView = this.mWebView;
        if (webView != null) {
            WebSettings settings = webView.getSettings();
            if (!z) {
                if (TextUtils.isEmpty(this.mDefUserAgent)) {
                    this.mDefUserAgent = settings.getUserAgentString();
                }
                str = str + this.mDefUserAgent + Operators.SPACE_STR + str;
            }
            settings.setUserAgentString(str);
        }
    }

    private void initWebView(WebView webView) {
        WebSettings settings = webView.getSettings();
        WebViewFactory.openJSEnabled(settings, (IApp) null);
        settings.setAppCacheEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setDomStorageEnabled(true);
        settings.setSupportZoom(false);
        settings.setAllowFileAccess(false);
        settings.setBuiltInZoomControls(false);
        settings.setAllowContentAccess(true);
        settings.setSavePassword(false);
        if (Build.VERSION.SDK_INT >= 21) {
            settings.setMixedContentMode(0);
        }
        webView.removeJavascriptInterface("searchBoxJavaBridge_");
        webView.removeJavascriptInterface("accessibilityTraversal");
        webView.removeJavascriptInterface("accessibility");
        WebViewFactory.setFileAccess(settings, true);
        settings.setDefaultTextEncodingName(StringUtils.GB2312);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                if (PdrUtil.isDeviceRootDir(str) || PdrUtil.isNetPath(str) || str.startsWith(DeviceInfo.FILE_PROTOCOL)) {
                    return false;
                }
                try {
                    if (str.startsWith("intent://")) {
                        Intent parseUri = Intent.parseUri(str, 1);
                        parseUri.addCategory("android.intent.category.BROWSABLE");
                        parseUri.setComponent((ComponentName) null);
                        if (Build.VERSION.SDK_INT >= 15) {
                            parseUri.setSelector((Intent) null);
                        }
                        if (DCWXWebView.this.mContext.getPackageManager().queryIntentActivities(parseUri, 0).size() > 0) {
                            ((Activity) DCWXWebView.this.mContext).startActivityIfNeeded(parseUri, -1);
                        }
                    } else {
                        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
                        intent.setFlags(268435456);
                        DCWXWebView.this.mContext.startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }

            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                super.onPageStarted(webView, str, bitmap);
                WXLogUtils.v("tag", "onPageStarted " + str);
                if (DCWXWebView.this.mOnPageListener != null) {
                    DCWXWebView.this.mOnPageListener.onPageStart(str);
                }
            }

            public void onPageFinished(WebView webView, String str) {
                super.onPageFinished(webView, str);
                WXLogUtils.v("tag", "onPageFinished " + str);
                if (DCWXWebView.this.isStart) {
                    boolean unused = DCWXWebView.this.isStart = false;
                    if (DCWXWebView.this.mWebProgressView != null) {
                        DCWXWebView.this.mWebProgressView.finishProgress();
                    }
                }
                if (DCWXWebView.this.mOnPageListener != null) {
                    DCWXWebView.this.mOnPageListener.onPageFinish(str, webView.canGoBack(), webView.canGoForward());
                }
            }

            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
                if (DCWXWebView.this.mOnErrorListener != null) {
                    DCWXWebView.this.mOnErrorListener.onError("error", "page error");
                }
            }

            public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
                super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
                if (DCWXWebView.this.mOnErrorListener != null) {
                    DCWXWebView.this.mOnErrorListener.onError("error", "http error");
                }
            }

            public void onReceivedSslError(WebView webView, final SslErrorHandler sslErrorHandler, final SslError sslError) {
                if (PdrUtil.isEquals(DCWXWebView.this.mSslType, "refuse")) {
                    sslErrorHandler.cancel();
                } else if (PdrUtil.isEquals(DCWXWebView.this.mSslType, "warning")) {
                    Context context = webView.getContext();
                    final AlertDialog create = new AlertDialog.Builder(context).create();
                    create.setIcon(17301601);
                    create.setTitle(R.string.dcloud_common_safety_warning);
                    create.setCanceledOnTouchOutside(false);
                    String str = null;
                    if (Build.VERSION.SDK_INT >= 14) {
                        str = sslError.getUrl();
                    }
                    String string = context.getString(R.string.dcloud_common_certificate_continue);
                    if (!TextUtils.isEmpty(str)) {
                        string = str + "\n" + string;
                    }
                    create.setMessage(string);
                    AnonymousClass1 r1 = new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (i == -2) {
                                create.cancel();
                                create.dismiss();
                            } else if (i == -3) {
                                sslError.getCertificate().getIssuedBy();
                            } else if (i == -1) {
                                WebViewFactory.setSslHandlerState(sslErrorHandler, 1);
                                create.dismiss();
                            }
                        }
                    };
                    create.setButton(-2, context.getResources().getString(17039360), r1);
                    create.setButton(-1, context.getResources().getString(17039370), r1);
                    create.show();
                } else {
                    WebViewFactory.setSslHandlerState(sslErrorHandler, 1);
                }
                if (DCWXWebView.this.mOnErrorListener != null) {
                    DCWXWebView.this.mOnErrorListener.onError("error", "ssl error");
                }
            }
        });
        DCWXChromeClient dCWXChromeClient = new DCWXChromeClient();
        this.chromeClient = dCWXChromeClient;
        webView.setWebChromeClient(dCWXChromeClient);
        if (Build.VERSION.SDK_INT > 18) {
            webView.addJavascriptInterface(new Object() {
                @JavascriptInterface
                public void postMessage(String str) {
                    DCWXWebView.this.onMessage(str, 1);
                }

                @JavascriptInterface
                public void postMessageToService(String str) {
                    DCWXWebView.this.onMessage(str, 2);
                }
            }, BRIDGE_NAME);
        }
    }

    /* access modifiers changed from: private */
    public void startWebProgress() {
        if (this.mDCWeb.get() != null && ((WXDCWeb) this.mDCWeb.get()).getHostView() != null) {
            if (this.mDCWeb.get() == null || ((WXDCWeb) this.mDCWeb.get()).getWebStyles() == null || ((WXDCWeb) this.mDCWeb.get()).getWebStyles().getBooleanValue("isProgress")) {
                String url = getWebView().getUrl();
                if (!TextUtils.isEmpty(url) && !url.startsWith("file")) {
                    if (this.mWebProgressView == null) {
                        this.mWebProgressView = new DCWebViewProgressBar(this.mWebView.getContext());
                    }
                    int stringToColor = PdrUtil.stringToColor(((WXDCWeb) this.mDCWeb.get()).getWebStyles().containsKey("progressColor") ? ((WXDCWeb) this.mDCWeb.get()).getWebStyles().getString("progressColor") : "#00FF00");
                    int convertToScreenInt = PdrUtil.convertToScreenInt("2px", this.mWebView.getMeasuredWidth(), 0, this.mWebView.getScale());
                    this.mWebProgressView.setColorInt(stringToColor);
                    this.mWebProgressView.setVisibility(0);
                    this.mWebProgressView.setAlphaInt(255);
                    if (this.mWebProgressView.getParent() == null) {
                        this.mRootView.addView(this.mWebProgressView, new ViewGroup.LayoutParams(-1, convertToScreenInt));
                    }
                    this.mWebProgressView.startProgress();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onMessage(String str, int i) {
        Object obj;
        if (str != null && this.mOnMessageListener != null) {
            try {
                obj = JSON.parse(str);
            } catch (Exception unused) {
                obj = str;
            }
            Message message = new Message();
            message.what = i;
            message.obj = obj;
            this.mMessageHandler.sendMessage(message);
        }
    }

    private void evaluateJS(String str) {
        if (SDK_VERSION < 19) {
            this.mWebView.loadUrl(str);
        } else {
            this.mWebView.evaluateJavascript(str, (ValueCallback) null);
        }
    }

    private static class MessageHandler extends Handler {
        private final WeakReference<DCWXWebView> mWv;

        private MessageHandler(DCWXWebView dCWXWebView) {
            this.mWv = new WeakReference<>(dCWXWebView);
        }

        public void handleMessage(Message message) {
            super.handleMessage(message);
            int i = message.what;
            if (i != 1) {
                if (i == 2 && this.mWv.get() != null && ((DCWXWebView) this.mWv.get()).mOnMessageListener != null) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("data", message.obj);
                    ((DCWXWebView) this.mWv.get()).mOnMessageListener.onMessage(hashMap, 2);
                }
            } else if (this.mWv.get() != null && ((DCWXWebView) this.mWv.get()).mOnMessageListener != null) {
                HashMap hashMap2 = new HashMap();
                hashMap2.put("detail", message.obj);
                ((DCWXWebView) this.mWv.get()).mOnMessageListener.onMessage(hashMap2, 1);
            }
        }
    }

    private class DCWXChromeClient extends WebChromeClient {
        FileChooseDialog dialog;
        ValueCallback<Uri> mUploadMessage;
        ValueCallback<Uri[]> mUploadMessage21Level;

        private DCWXChromeClient() {
        }

        public void onPermissionRequest(PermissionRequest permissionRequest) {
            permissionRequest.grant(permissionRequest.getResources());
        }

        public void onProgressChanged(WebView webView, int i) {
            super.onProgressChanged(webView, i);
            DCWXWebView.this.mProgress = i;
            if (!DCWXWebView.this.isStart && DCWXWebView.this.mProgress < 100) {
                DCWXWebView.this.startWebProgress();
                boolean unused = DCWXWebView.this.isStart = true;
            }
            if (DCWXWebView.this.mProgress >= 100 && DCWXWebView.this.isStart) {
                boolean unused2 = DCWXWebView.this.isStart = false;
                if (DCWXWebView.this.mWebProgressView != null) {
                    DCWXWebView.this.mWebProgressView.finishProgress();
                }
            }
            WXLogUtils.v("tag", "onPageProgressChanged " + i);
        }

        public void onReceivedTitle(WebView webView, String str) {
            super.onReceivedTitle(webView, str);
            if (DCWXWebView.this.mOnPageListener != null) {
                DCWXWebView.this.mOnPageListener.onReceivedTitle(webView.getTitle());
            }
        }

        public boolean onJsPrompt(WebView webView, String str, String str2, String str3, JsPromptResult jsPromptResult) {
            return super.onJsPrompt(webView, str, str2, str3, jsPromptResult);
        }

        public void onGeolocationPermissionsShowPrompt(final String str, final GeolocationPermissions.Callback callback) {
            if (DCWXWebView.this.mContext instanceof Activity) {
                PermissionUtil.usePermission((Activity) DCWXWebView.this.mContext, "dc_weexsdk", PermissionUtil.PMS_LOCATION, 2, new PermissionUtil.Request() {
                    public void onGranted(String str) {
                        callback.invoke(str, true, false);
                    }

                    public void onDenied(String str) {
                        callback.invoke(str, false, false);
                    }
                });
            } else {
                super.onGeolocationPermissionsShowPrompt(str, callback);
            }
        }

        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            openFileChooserLogic(webView, (ValueCallback<Uri>) null, valueCallback, fileChooserParams.getAcceptTypes() != null ? fileChooserParams.getAcceptTypes()[0] : null, "");
            return true;
        }

        private void openFileChooserLogic(WebView webView, ValueCallback<Uri> valueCallback, ValueCallback<Uri[]> valueCallback2, String str, String str2) {
            if (DCWXWebView.this.mContext != null) {
                final WebView webView2 = webView;
                final ValueCallback<Uri> valueCallback3 = valueCallback;
                final ValueCallback<Uri[]> valueCallback4 = valueCallback2;
                final String str3 = str;
                final String str4 = str2;
                PermissionUtil.usePermission((Activity) DCWXWebView.this.mContext, "dc_weexsdk", PermissionUtil.PMS_STORAGE, 2, new PermissionUtil.Request() {
                    public void onDenied(String str) {
                    }

                    public void onGranted(String str) {
                        DCWXChromeClient.this.showOpenFileChooser(webView2, valueCallback3, valueCallback4, str3, str4);
                    }
                });
            }
        }

        /* access modifiers changed from: private */
        public void showOpenFileChooser(WebView webView, ValueCallback<Uri> valueCallback, ValueCallback<Uri[]> valueCallback2, String str, String str2) {
            this.mUploadMessage = valueCallback;
            this.mUploadMessage21Level = valueCallback2;
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            intent.addCategory("android.intent.category.OPENABLE");
            if (!PdrUtil.isEmpty(str)) {
                intent.setType(str);
            } else {
                intent.setType("*/*");
            }
            FileChooseDialog fileChooseDialog = new FileChooseDialog(webView.getContext(), (Activity) webView.getContext(), intent);
            this.dialog = fileChooseDialog;
            try {
                fileChooseDialog.show();
                this.dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialogInterface) {
                        try {
                            if (DCWXChromeClient.this.mUploadMessage21Level != null) {
                                DCWXChromeClient.this.mUploadMessage21Level.onReceiveValue((Object) null);
                            } else if (DCWXChromeClient.this.mUploadMessage != null) {
                                DCWXChromeClient.this.mUploadMessage.onReceiveValue((Object) null);
                            }
                        } catch (Exception unused) {
                        }
                    }
                });
            } catch (Exception unused) {
            }
        }

        public void onResult(int i, int i2, Intent intent) {
            Uri uri;
            FileChooseDialog fileChooseDialog;
            Cursor query;
            FileChooseDialog fileChooseDialog2 = this.dialog;
            if (fileChooseDialog2 != null) {
                fileChooseDialog2.dismiss();
                if (i2 != 0) {
                    if (i != 1) {
                        if (i == 2 && (fileChooseDialog = this.dialog) != null && fileChooseDialog.uris != null) {
                            Iterator<File> it = this.dialog.uris.iterator();
                            while (true) {
                                if (!it.hasNext()) {
                                    break;
                                }
                                File next = it.next();
                                if (next.exists()) {
                                    uri = FileProvider.getUriForFile(DCWXWebView.this.mContext, DCWXWebView.this.mContext.getPackageName() + ".dc.fileprovider", next);
                                    break;
                                }
                            }
                        }
                        uri = null;
                    } else if (intent == null) {
                        ValueCallback<Uri[]> valueCallback = this.mUploadMessage21Level;
                        if (valueCallback != null) {
                            valueCallback.onReceiveValue((Object) null);
                        } else {
                            ValueCallback<Uri> valueCallback2 = this.mUploadMessage;
                            if (valueCallback2 != null) {
                                valueCallback2.onReceiveValue((Object) null);
                            }
                        }
                        this.dialog = null;
                        return;
                    } else {
                        uri = intent.getData();
                        if (!(uri == null || !UriUtil.LOCAL_CONTENT_SCHEME.equals(uri.getScheme()) || (query = DCWXWebView.this.mContext.getContentResolver().query(uri, new String[]{"_data"}, (String) null, (String[]) null, (String) null)) == null)) {
                            if (query.moveToFirst()) {
                                try {
                                    int columnIndexOrThrow = query.getColumnIndexOrThrow("_data");
                                    if (columnIndexOrThrow > -1) {
                                        String string = query.getString(columnIndexOrThrow);
                                        uri = Uri.parse(string);
                                        if (PdrUtil.isEmpty(uri.getScheme())) {
                                            uri = Uri.parse((string.startsWith("/") ? DeviceInfo.FILE_PROTOCOL : "file:///") + string);
                                        }
                                    }
                                } catch (Exception unused) {
                                }
                            }
                            query.close();
                        }
                    }
                    Uri[] uriArr = uri != null ? new Uri[]{uri} : null;
                    ValueCallback<Uri[]> valueCallback3 = this.mUploadMessage21Level;
                    if (valueCallback3 != null) {
                        valueCallback3.onReceiveValue(uriArr);
                    } else {
                        ValueCallback<Uri> valueCallback4 = this.mUploadMessage;
                        if (valueCallback4 != null) {
                            valueCallback4.onReceiveValue(uri);
                        }
                    }
                } else {
                    ValueCallback<Uri[]> valueCallback5 = this.mUploadMessage21Level;
                    if (valueCallback5 != null) {
                        valueCallback5.onReceiveValue((Object) null);
                    } else {
                        ValueCallback<Uri> valueCallback6 = this.mUploadMessage;
                        if (valueCallback6 != null) {
                            valueCallback6.onReceiveValue((Object) null);
                        }
                    }
                }
                this.dialog = null;
            }
        }

        public void onShowCustomView(View view, WebChromeClient.CustomViewCallback customViewCallback) {
            DCWXWebView.this.showCustomView(view, customViewCallback);
        }

        public void onHideCustomView() {
            DCWXWebView.this.hideCustomView();
        }
    }

    /* access modifiers changed from: private */
    public void showCustomView(View view, WebChromeClient.CustomViewCallback customViewCallback2) {
        if (this.customView != null) {
            customViewCallback2.onCustomViewHidden();
            return;
        }
        Context context = this.mContext;
        if (context != null && (context instanceof Activity)) {
            Activity activity = (Activity) context;
            FrameLayout frameLayout = null;
            IActivityHandler iActivityHandler = DCloudAdapterUtil.getIActivityHandler(activity);
            if (iActivityHandler != null) {
                frameLayout = iActivityHandler.obtainActivityContentView();
                iActivityHandler.closeSideBar();
                iActivityHandler.setSideBarVisibility(8);
            } else if (activity instanceof IActivityDelegate) {
                frameLayout = ((IActivityDelegate) activity).obtainActivityContentView();
            }
            this.mWebView.setVisibility(8);
            this.fullscreenContainer = new FullscreenHolder(this.mContext);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
            this.fullscreenContainer.addView(view, layoutParams);
            frameLayout.addView(this.fullscreenContainer, layoutParams);
            this.customView = view;
            setStatusBarVisibility(activity, false);
            this.customViewCallback = customViewCallback2;
        }
    }

    /* access modifiers changed from: private */
    public void hideCustomView() {
        Context context;
        if (this.customView != null && (context = this.mContext) != null && (context instanceof Activity)) {
            setStatusBarVisibility((Activity) context, true);
            if (this.fullscreenContainer.getParent() != null) {
                ((ViewGroup) this.fullscreenContainer.getParent()).removeView(this.fullscreenContainer);
                this.fullscreenContainer.removeAllViews();
            }
            this.fullscreenContainer = null;
            this.customView = null;
            WebChromeClient.CustomViewCallback customViewCallback2 = this.customViewCallback;
            if (customViewCallback2 != null) {
                customViewCallback2.onCustomViewHidden();
                this.customViewCallback = null;
            }
            this.mWebView.setVisibility(0);
        }
    }

    private void setStatusBarVisibility(Activity activity, boolean z) {
        if (z) {
            activity.getWindow().getDecorView().setSystemUiVisibility(this.defaultSystemUI);
            return;
        }
        this.defaultSystemUI = activity.getWindow().getDecorView().getSystemUiVisibility();
        activity.getWindow().getDecorView().setSystemUiVisibility(5894);
    }
}
