package io.dcloud.common.adapter.ui.webview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.CookieSyncManager;
import android.webkit.MimeTypeMap;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsoluteLayout;
import android.widget.ProgressBar;
import androidx.webkit.internal.AssetHelper;
import com.nostra13.dcloudimageloader.core.ImageLoaderL;
import com.nostra13.dcloudimageloader.core.download.BaseImageDownloader;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IDCloudWebviewClientListener;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.ITitleNView;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.adapter.ui.AdaWebview;
import io.dcloud.common.adapter.ui.ReceiveJSValue;
import io.dcloud.common.adapter.ui.WaitingView;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.DCloudTrustManager;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.InvokeExecutorHelper;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.constant.IntentConst;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.DLGeolocation;
import io.dcloud.common.util.IOUtil;
import io.dcloud.common.util.ImageLoaderUtil;
import io.dcloud.common.util.Md5Utils;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.StringUtil;
import io.dcloud.common.util.TitleNViewUtil;
import io.src.dcloud.adapter.DCloudAdapterUtil;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class WebLoadEvent extends WebViewClient {
    private static final String DIFFERENT_VERSION_JS = "window.plus && (plus.android.import=plus.android.importClass);";
    public static final String ENABLE = "enable";
    private static final String ERROR_TEMPLATE = "javascript:(function(){var b=document.createEvent('HTMLEvents');var a='%s';b.url='%s';b.href='%s';b.initEvent(a,false,true);console.error(a);document.dispatchEvent(b);})();";
    private static final String IF_LOAD_TEMPLATE = "(function(){/*console.log('eval js loading href=' + location.href);*/if(location.__page__load__over__){return 2}if(location.__plusready__||window.__html5plus__){return 1}return 0})();";
    private static final String IF_PLUSREADY_EVENT_TEMPLATE = "(function(){/*console.log('plusready event loading href=' + location.href);*/if(location.__page__load__over__){return 2}if(location.__plusready__||window.__html5plus__){if(!location.__plusready__event__){location.__plusready__event__=true;return 1}else{return 2}}return 0})();";
    private static final String IF_PLUSREADY_TEMPLATE = "(function(){/*console.log('all.js loading href=' + location.href);*/if(location.__page__load__over__){return 2}if(!location.__plusready__){location.__plusready__=true;return 1}else{return 2}return 0})();";
    private static final String IF_PRELOAD_TEMPLATE = "(function(){/*console.log( 'preload js loading href=' + location.href);*/if(location.__page__load__over__){return 2}var jsfile='%s';if(location.__plusready__||window.__html5plus__){location.__preload__=location.__preload__||[];if(location.__preload__.indexOf(jsfile)<0){location.__preload__.push(jsfile);return 1}else{return 2}}return 0})();";
    private static final int LOADABLE = 1;
    private static final int LOADED = 2;
    private static final int NOLOAD = 0;
    public static String PAGE_FINISHED_FLAG = "javascript:setTimeout(function(){location.__page__load__over__ = true;},2000);";
    public static final String PLUSREADY = "html5plus://ready";
    static final String TAG = "WebLoadEvent";
    static final int Timeout_Page_Finish = 6000;
    static final int Timeout_Plus_Inject = 3000;
    public static final String UNIAPP_READY = "uniapp://ready";
    String TYPE_CSS = "type_css";
    String TYPE_JS = "type_js";
    Runnable Timeout_Page_Finish_Runnable = null;
    Runnable Timeout_Plus_Inject_Runnable = null;
    boolean isInitAmapGEO = false;
    AdaWebview mAdaWebview = null;
    String mAppid = null;
    private boolean mClearCache = false;
    private String mLastPageUrl = "";
    private OnPageFinishedCallack mPageFinishedCallack = null;
    String mPlusJS = null;
    long mShowLoadingTime;
    private Runnable mTitleNViewProgressStop = null;
    ProgressBar mWaitingForWapPage = null;
    WaitingView mWap2AppBlockDialog = null;
    ISysEventListener mWap2AppBlockDialogSysEventListener = null;
    private IDCloudWebviewClientListener mdcloudwebviewclientlister = null;
    boolean needResponseRedirect = true;
    boolean printLog = true;

    class CatchFile {
        String mContentType = null;
        String mEncoding = null;
        boolean mExist = false;
        File mFile = null;

        CatchFile() {
        }
    }

    static class TitleNViewProgressStop implements Runnable {
        private WeakReference<AdaWebview> mAdaWebview;

        public TitleNViewProgressStop(AdaWebview adaWebview) {
            this.mAdaWebview = new WeakReference<>(adaWebview);
        }

        public void run() {
            WeakReference<AdaWebview> weakReference = this.mAdaWebview;
            if (weakReference != null && weakReference.get() != null && ((AdaWebview) this.mAdaWebview.get()).obtainFrameView() != null) {
                Object titleNView = TitleNViewUtil.getTitleNView(((AdaWebview) this.mAdaWebview.get()).obtainFrameView().obtainWindowMgr(), ((AdaWebview) this.mAdaWebview.get()).obtainFrameView().obtainWebView(), ((AdaWebview) this.mAdaWebview.get()).obtainFrameView(), TitleNViewUtil.getTitleNViewId(((AdaWebview) this.mAdaWebview.get()).obtainFrameView()));
                if (titleNView instanceof ITitleNView) {
                    TitleNViewUtil.stopProcess((ITitleNView) titleNView);
                }
            }
        }
    }

    public WebLoadEvent(AdaWebview adaWebview) {
        this.mAdaWebview = adaWebview;
        this.mAppid = adaWebview.obtainApp().obtainAppId();
        String obtainConfigProperty = adaWebview.obtainApp().obtainConfigProperty(IApp.ConfigProperty.CONFIG_RAM_CACHE_MODE);
        if (BaseInfo.isBase(adaWebview.getContext()) && !ENABLE.equalsIgnoreCase(obtainConfigProperty)) {
            this.mClearCache = true;
        }
        reset();
        this.isInitAmapGEO = DLGeolocation.checkGeo(adaWebview.getContext());
    }

    private boolean checkCssFile(String str) {
        return !TextUtils.isEmpty(str) && str.contains(".css");
    }

    private boolean checkJsFile(String str) {
        return !TextUtils.isEmpty(str) && str.contains(".js") && !str.contains(".jsp");
    }

    private WebResourceResponse checkWebResourceResponseRedirect(WebView webView, String str) {
        AdaWebview adaWebview;
        JSONObject obtainThridInfo;
        String str2;
        if (!this.needResponseRedirect) {
            return null;
        }
        try {
            if (!URLUtil.isNetworkUrl(str) || !BaseInfo.existsStreamEnv() || (adaWebview = this.mAdaWebview) == null || adaWebview.obtainFrameView().obtainApp() == null || (obtainThridInfo = this.mAdaWebview.obtainFrameView().obtainApp().obtainThridInfo(IApp.ConfigProperty.ThridInfo.URDJsonData)) == null) {
                return null;
            }
            JSONArray optJSONArray = obtainThridInfo.optJSONObject("data").optJSONArray(InvokeExecutorHelper.create("io.dcloud.appstream.rules.util.Tools").invoke("getTopDomainInHost", new URL(str).getHost()));
            if (optJSONArray == null) {
                return null;
            }
            int i = 0;
            boolean z = false;
            while (true) {
                if (i >= optJSONArray.length()) {
                    str2 = null;
                    break;
                }
                JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                JSONArray optJSONArray2 = optJSONObject.optJSONArray("match");
                str2 = optJSONObject.optString("redirect");
                int i2 = 0;
                while (true) {
                    if (i2 >= optJSONArray2.length()) {
                        break;
                    } else if (Pattern.compile(optJSONArray2.optString(i)).matcher(str).matches()) {
                        z = true;
                        break;
                    } else {
                        i2++;
                    }
                }
                if (z) {
                    break;
                }
                i++;
            }
            return downloadResponse(webView, str2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* access modifiers changed from: private */
    public final void completeLoadJs(WebView webView, String str, String str2, String[] strArr, String str3, Object... objArr) {
        final SoftReference softReference = new SoftReference(this.mAdaWebview);
        final String str4 = str2;
        final WebView webView2 = webView;
        final String str5 = str;
        final String[] strArr2 = strArr;
        final String str6 = str3;
        final Object[] objArr2 = objArr;
        this.mAdaWebview.executeScript(ReceiveJSValue.registerCallback((AdaWebview) softReference.get(), StringUtil.format(str3, objArr), new ReceiveJSValue.ReceiveJSValueCallback() {
            public String callback(JSONArray jSONArray) {
                try {
                    int i = jSONArray.getInt(1);
                    if (i == 0 && !PdrUtil.isEquals(str4, "onPageFinished")) {
                        WebLoadEvent.this.completeLoadJs(webView2, str5, str4, strArr2, str6, objArr2);
                        return null;
                    } else if (1 != i) {
                        return null;
                    } else {
                        for (String str : strArr2) {
                            if (softReference.get() != null) {
                                ((AdaWebview) softReference.get()).executeScript(str);
                            }
                        }
                        return null;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }));
    }

    private boolean directPageIsLaunchPage(IApp iApp) {
        return iApp != null && !TextUtils.isEmpty(iApp.getOriginalDirectPage()) && !iApp.obtainWebAppIntent().hasExtra(IntentConst.DIRECT_PAGE);
    }

    private WebResourceResponse downloadResponse(final WebView webView, final String str) {
        if (!URLUtil.isNetworkUrl(str)) {
            return null;
        }
        try {
            MessageHandler.sendMessage(new MessageHandler.IMessages() {
                public void execute(Object obj) {
                    webView.stopLoading();
                    WebLoadEvent.this.needResponseRedirect = false;
                    webView.loadUrl(str);
                }
            }, (Object) null);
            return new WebResourceResponse((String) null, (String) null, new ByteArrayInputStream("".getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:76:0x016b A[Catch:{ Exception -> 0x01eb }] */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x01a0 A[Catch:{ Exception -> 0x01eb }] */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x01b1 A[Catch:{ Exception -> 0x01eb }] */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x01b7  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x01bb A[SYNTHETIC, Splitter:B:89:0x01bb] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.webkit.WebResourceResponse downloadResponseInjection(android.webkit.WebResourceResponse r16, java.lang.String r17, java.lang.String r18, java.lang.String r19, java.lang.String r20) {
        /*
            r15 = this;
            r1 = r15
            r2 = r17
            r3 = r20
            long r4 = java.lang.System.currentTimeMillis()
            java.lang.String r0 = r1.TYPE_JS
            boolean r0 = r3.equals(r0)
            r6 = 4096(0x1000, float:5.74E-42)
            java.lang.String r7 = ";url="
            java.lang.String r8 = "WebLoadEvent"
            r9 = 0
            r10 = 1
            if (r0 == 0) goto L_0x01f4
            io.dcloud.common.adapter.ui.AdaWebview r0 = r1.mAdaWebview
            java.lang.String r11 = r0.getPreLoadJsString()
            io.dcloud.common.adapter.ui.AdaWebview r0 = r1.mAdaWebview
            boolean r12 = r0.mForceAHeadJsFileLoaded
            r13 = 0
            if (r12 != 0) goto L_0x004d
            java.lang.String r0 = r0.mForceAHeadJsFile
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x004d
            io.dcloud.common.adapter.ui.AdaWebview r0 = r1.mAdaWebview     // Catch:{ IOException -> 0x0043 }
            java.lang.String r12 = r0.mForceAHeadJsFile     // Catch:{ IOException -> 0x0043 }
            io.dcloud.common.adapter.ui.AdaFrameView r0 = r0.mFrameView     // Catch:{ IOException -> 0x0043 }
            io.dcloud.common.DHInterface.IApp r0 = r0.obtainApp()     // Catch:{ IOException -> 0x0043 }
            java.io.InputStream r0 = io.dcloud.common.adapter.ui.webview.WebResUtil.getEncryptionInputStream(r12, r0)     // Catch:{ IOException -> 0x0043 }
            if (r0 == 0) goto L_0x004d
            byte[] r0 = io.dcloud.common.util.IOUtil.getBytes(r0)     // Catch:{ IOException -> 0x0043 }
            goto L_0x004e
        L_0x0043:
            r0 = move-exception
            java.lang.String r0 = r0.getMessage()
            java.lang.String r12 = "Exception"
            io.dcloud.common.adapter.util.Logger.e(r12, r0)
        L_0x004d:
            r0 = r13
        L_0x004e:
            boolean r12 = android.text.TextUtils.isEmpty(r11)
            if (r12 == 0) goto L_0x005f
            if (r0 != 0) goto L_0x005f
            java.lang.String r12 = r1.mPlusJS
            boolean r12 = android.text.TextUtils.isEmpty(r12)
            if (r12 == 0) goto L_0x005f
            return r13
        L_0x005f:
            boolean r12 = android.webkit.URLUtil.isNetworkUrl(r17)     // Catch:{ Exception -> 0x01eb }
            if (r12 == 0) goto L_0x0163
            io.dcloud.common.adapter.ui.AdaWebview r12 = r1.mAdaWebview     // Catch:{ Exception -> 0x01eb }
            boolean r12 = r12.mNeedInjection     // Catch:{ Exception -> 0x01eb }
            if (r12 != 0) goto L_0x0071
            boolean r12 = r1.isInitAmapGEO     // Catch:{ Exception -> 0x01eb }
            if (r12 != 0) goto L_0x0071
            if (r0 == 0) goto L_0x0163
        L_0x0071:
            io.dcloud.common.adapter.ui.webview.WebLoadEvent$CatchFile r3 = r15.getUrlFile(r2, r3)     // Catch:{ Exception -> 0x01eb }
            if (r3 == 0) goto L_0x01ab
            java.io.ByteArrayOutputStream r12 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x01eb }
            r12.<init>()     // Catch:{ Exception -> 0x01eb }
            if (r0 == 0) goto L_0x0085
            r12.write(r0)     // Catch:{ Exception -> 0x01eb }
            io.dcloud.common.adapter.ui.AdaWebview r0 = r1.mAdaWebview     // Catch:{ Exception -> 0x01eb }
            r0.mForceAHeadJsFileLoaded = r10     // Catch:{ Exception -> 0x01eb }
        L_0x0085:
            java.lang.String r0 = r1.mPlusJS     // Catch:{ Exception -> 0x01eb }
            boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Exception -> 0x01eb }
            java.lang.String r13 = "ahead"
            if (r0 != 0) goto L_0x00a8
            io.dcloud.common.adapter.ui.AdaWebview r0 = r1.mAdaWebview     // Catch:{ Exception -> 0x01eb }
            java.lang.String r0 = r0.mPlusrequire     // Catch:{ Exception -> 0x01eb }
            boolean r0 = r0.equals(r13)     // Catch:{ Exception -> 0x01eb }
            if (r0 == 0) goto L_0x00a8
            io.dcloud.common.adapter.ui.AdaWebview r0 = r1.mAdaWebview     // Catch:{ Exception -> 0x01eb }
            r0.mPlusInjectTag = r2     // Catch:{ Exception -> 0x01eb }
            r0.mPlusLoading = r10     // Catch:{ Exception -> 0x01eb }
            java.lang.String r0 = r1.mPlusJS     // Catch:{ Exception -> 0x01eb }
            byte[] r0 = r0.getBytes()     // Catch:{ Exception -> 0x01eb }
            r12.write(r0)     // Catch:{ Exception -> 0x01eb }
        L_0x00a8:
            boolean r0 = android.text.TextUtils.isEmpty(r11)     // Catch:{ Exception -> 0x01eb }
            if (r0 != 0) goto L_0x00d7
            io.dcloud.common.adapter.ui.AdaWebview r0 = r1.mAdaWebview     // Catch:{ Exception -> 0x01eb }
            java.lang.String r0 = r0.mPlusrequire     // Catch:{ Exception -> 0x01eb }
            boolean r0 = r0.equals(r13)     // Catch:{ Exception -> 0x01eb }
            if (r0 == 0) goto L_0x00d7
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01eb }
            r0.<init>()     // Catch:{ Exception -> 0x01eb }
            java.lang.String r14 = "mPlusrequire=ahead;this="
            r0.append(r14)     // Catch:{ Exception -> 0x01eb }
            r0.append(r15)     // Catch:{ Exception -> 0x01eb }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x01eb }
            io.dcloud.common.adapter.util.Logger.i(r8, r0)     // Catch:{ Exception -> 0x01eb }
            io.dcloud.common.adapter.ui.AdaWebview r0 = r1.mAdaWebview     // Catch:{ Exception -> 0x01eb }
            r0.mPreloadJsLoading = r10     // Catch:{ Exception -> 0x01eb }
            byte[] r0 = r11.getBytes()     // Catch:{ Exception -> 0x01eb }
            r12.write(r0)     // Catch:{ Exception -> 0x01eb }
        L_0x00d7:
            boolean r0 = r1.isInitAmapGEO     // Catch:{ Exception -> 0x01eb }
            if (r0 == 0) goto L_0x00fe
            io.dcloud.common.adapter.ui.AdaWebview r0 = r1.mAdaWebview     // Catch:{ Exception -> 0x01eb }
            java.lang.String r0 = r0.mInjectGEO     // Catch:{ Exception -> 0x01eb }
            boolean r0 = io.dcloud.common.util.DLGeolocation.checkInjectGeo(r0)     // Catch:{ Exception -> 0x01eb }
            if (r0 == 0) goto L_0x00fe
            io.dcloud.common.adapter.ui.AdaWebview r0 = r1.mAdaWebview     // Catch:{ Exception -> 0x01eb }
            java.lang.String r0 = r0.mPlusrequire     // Catch:{ Exception -> 0x01eb }
            boolean r0 = r0.equals(r13)     // Catch:{ Exception -> 0x01eb }
            if (r0 != 0) goto L_0x00fe
            io.dcloud.common.adapter.ui.AdaWebview r0 = r1.mAdaWebview     // Catch:{ Exception -> 0x01eb }
            r0.mInjectGeoLoaded = r10     // Catch:{ Exception -> 0x01eb }
            java.lang.String r0 = io.dcloud.common.util.DLGeolocation.getGEOJS()     // Catch:{ Exception -> 0x01eb }
            byte[] r0 = r0.getBytes()     // Catch:{ Exception -> 0x01eb }
            r12.write(r0)     // Catch:{ Exception -> 0x01eb }
        L_0x00fe:
            boolean r0 = r3.mExist     // Catch:{ Exception -> 0x01eb }
            if (r0 == 0) goto L_0x011d
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ IOException -> 0x0119 }
            java.io.File r10 = r3.mFile     // Catch:{ IOException -> 0x0119 }
            r0.<init>(r10)     // Catch:{ IOException -> 0x0119 }
            byte[] r6 = new byte[r6]     // Catch:{ IOException -> 0x0119 }
        L_0x010b:
            int r10 = r0.read(r6)     // Catch:{ IOException -> 0x0119 }
            if (r10 <= 0) goto L_0x0115
            r12.write(r6, r9, r10)     // Catch:{ IOException -> 0x0119 }
            goto L_0x010b
        L_0x0115:
            r0.close()     // Catch:{ IOException -> 0x0119 }
            goto L_0x011d
        L_0x0119:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Exception -> 0x01eb }
        L_0x011d:
            java.lang.String r0 = r3.mEncoding     // Catch:{ Exception -> 0x01eb }
            boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Exception -> 0x01eb }
            if (r0 != 0) goto L_0x0128
            java.lang.String r0 = r3.mEncoding     // Catch:{ Exception -> 0x01eb }
            goto L_0x012a
        L_0x0128:
            r0 = r19
        L_0x012a:
            java.lang.String r6 = r3.mContentType     // Catch:{ Exception -> 0x01eb }
            boolean r6 = android.text.TextUtils.isEmpty(r6)     // Catch:{ Exception -> 0x01eb }
            if (r6 != 0) goto L_0x0135
            java.lang.String r3 = r3.mContentType     // Catch:{ Exception -> 0x01eb }
            goto L_0x0137
        L_0x0135:
            r3 = r18
        L_0x0137:
            java.io.ByteArrayInputStream r6 = new java.io.ByteArrayInputStream     // Catch:{ Exception -> 0x01eb }
            byte[] r9 = r12.toByteArray()     // Catch:{ Exception -> 0x01eb }
            r6.<init>(r9)     // Catch:{ Exception -> 0x01eb }
            io.dcloud.common.adapter.ui.AdaWebview r9 = r1.mAdaWebview     // Catch:{ Exception -> 0x01eb }
            java.lang.String r9 = r9.mPlusrequire     // Catch:{ Exception -> 0x01eb }
            boolean r9 = r9.equals(r13)     // Catch:{ Exception -> 0x01eb }
            if (r9 == 0) goto L_0x0161
            io.dcloud.common.adapter.ui.AdaWebview r9 = r1.mAdaWebview     // Catch:{ Exception -> 0x01eb }
            io.dcloud.common.adapter.ui.webview.DCWebView r9 = r9.getDCWebView()     // Catch:{ Exception -> 0x01eb }
            android.view.ViewGroup r9 = r9.getWebView()     // Catch:{ Exception -> 0x01eb }
            android.webkit.WebView r9 = (android.webkit.WebView) r9     // Catch:{ Exception -> 0x01eb }
            io.dcloud.common.adapter.ui.AdaWebview r10 = r1.mAdaWebview     // Catch:{ Exception -> 0x01eb }
            java.lang.String r10 = r10.obtainFullUrl()     // Catch:{ Exception -> 0x01eb }
            java.lang.String r11 = "inject_with_js"
            r15.listenPlusInjectTimeout(r9, r10, r11)     // Catch:{ Exception -> 0x01eb }
        L_0x0161:
            r13 = r6
            goto L_0x01af
        L_0x0163:
            java.lang.String r0 = "html5plus://ready"
            boolean r0 = r0.equals(r2)     // Catch:{ Exception -> 0x01eb }
            if (r0 == 0) goto L_0x01a0
            java.lang.String r0 = r1.mPlusJS     // Catch:{ Exception -> 0x01eb }
            boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Exception -> 0x01eb }
            if (r0 != 0) goto L_0x01ab
            java.lang.String r0 = "PLUSREADY"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01eb }
            r3.<init>()     // Catch:{ Exception -> 0x01eb }
            io.dcloud.common.adapter.ui.AdaWebview r6 = r1.mAdaWebview     // Catch:{ Exception -> 0x01eb }
            java.lang.String r6 = r6.getOriginalUrl()     // Catch:{ Exception -> 0x01eb }
            r3.append(r6)     // Catch:{ Exception -> 0x01eb }
            r3.append(r7)     // Catch:{ Exception -> 0x01eb }
            r3.append(r2)     // Catch:{ Exception -> 0x01eb }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x01eb }
            io.dcloud.common.adapter.util.Logger.i(r0, r3)     // Catch:{ Exception -> 0x01eb }
            io.dcloud.common.adapter.ui.AdaWebview r0 = r1.mAdaWebview     // Catch:{ Exception -> 0x01eb }
            r0.mPlusLoading = r10     // Catch:{ Exception -> 0x01eb }
            java.io.ByteArrayInputStream r13 = new java.io.ByteArrayInputStream     // Catch:{ Exception -> 0x01eb }
            java.lang.String r0 = r1.mPlusJS     // Catch:{ Exception -> 0x01eb }
            byte[] r0 = r0.getBytes()     // Catch:{ Exception -> 0x01eb }
            r13.<init>(r0)     // Catch:{ Exception -> 0x01eb }
            goto L_0x01ab
        L_0x01a0:
            boolean r0 = io.dcloud.common.util.PdrUtil.isDeviceRootDir(r17)     // Catch:{ Exception -> 0x01eb }
            if (r0 == 0) goto L_0x01ab
            java.io.FileInputStream r13 = new java.io.FileInputStream     // Catch:{ Exception -> 0x01eb }
            r13.<init>(r2)     // Catch:{ Exception -> 0x01eb }
        L_0x01ab:
            r3 = r18
            r0 = r19
        L_0x01af:
            if (r13 == 0) goto L_0x01b7
            android.webkit.WebResourceResponse r6 = new android.webkit.WebResourceResponse     // Catch:{ Exception -> 0x01eb }
            r6.<init>(r3, r0, r13)     // Catch:{ Exception -> 0x01eb }
            goto L_0x01b9
        L_0x01b7:
            r6 = r16
        L_0x01b9:
            if (r6 == 0) goto L_0x01f1
            io.dcloud.common.adapter.ui.AdaWebview r3 = r1.mAdaWebview     // Catch:{ Exception -> 0x01e9 }
            r3.mEncoding = r0     // Catch:{ Exception -> 0x01e9 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01e9 }
            r0.<init>()     // Catch:{ Exception -> 0x01e9 }
            java.lang.String r3 = "提前注入JS成功耗时："
            r0.append(r3)     // Catch:{ Exception -> 0x01e9 }
            long r9 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x01e9 }
            long r9 = r9 - r4
            r0.append(r9)     // Catch:{ Exception -> 0x01e9 }
            io.dcloud.common.adapter.ui.AdaWebview r3 = r1.mAdaWebview     // Catch:{ Exception -> 0x01e9 }
            java.lang.String r3 = r3.getOriginalUrl()     // Catch:{ Exception -> 0x01e9 }
            r0.append(r3)     // Catch:{ Exception -> 0x01e9 }
            r0.append(r7)     // Catch:{ Exception -> 0x01e9 }
            r0.append(r2)     // Catch:{ Exception -> 0x01e9 }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x01e9 }
            io.dcloud.common.adapter.util.Logger.i(r8, r0)     // Catch:{ Exception -> 0x01e9 }
            goto L_0x01f1
        L_0x01e9:
            r0 = move-exception
            goto L_0x01ee
        L_0x01eb:
            r0 = move-exception
            r6 = r16
        L_0x01ee:
            r0.printStackTrace()
        L_0x01f1:
            r11 = r6
            goto L_0x0290
        L_0x01f4:
            java.lang.String r0 = r1.TYPE_CSS
            boolean r0 = r0.equals(r3)
            if (r0 == 0) goto L_0x028e
            io.dcloud.common.adapter.ui.AdaWebview r0 = r1.mAdaWebview
            java.lang.String r0 = r0.getCssString()
            io.dcloud.common.adapter.ui.AdaWebview r11 = r1.mAdaWebview
            boolean r11 = r11.mIsAdvanceCss
            if (r11 != 0) goto L_0x028e
            boolean r11 = android.text.TextUtils.isEmpty(r0)
            if (r11 != 0) goto L_0x028e
            java.io.ByteArrayOutputStream r11 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x0287 }
            r11.<init>()     // Catch:{ Exception -> 0x0287 }
            io.dcloud.common.adapter.ui.webview.WebLoadEvent$CatchFile r3 = r15.getUrlFile(r2, r3)     // Catch:{ Exception -> 0x0287 }
            if (r3 == 0) goto L_0x023a
            boolean r12 = r3.mExist     // Catch:{ Exception -> 0x0287 }
            if (r12 == 0) goto L_0x0233
            java.io.FileInputStream r12 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0287 }
            java.io.File r13 = r3.mFile     // Catch:{ Exception -> 0x0287 }
            r12.<init>(r13)     // Catch:{ Exception -> 0x0287 }
            byte[] r6 = new byte[r6]     // Catch:{ Exception -> 0x0287 }
        L_0x0226:
            int r13 = r12.read(r6)     // Catch:{ Exception -> 0x0287 }
            if (r13 <= 0) goto L_0x0230
            r11.write(r6, r9, r13)     // Catch:{ Exception -> 0x0287 }
            goto L_0x0226
        L_0x0230:
            r12.close()     // Catch:{ Exception -> 0x0287 }
        L_0x0233:
            byte[] r0 = r0.getBytes()     // Catch:{ Exception -> 0x0287 }
            r11.write(r0)     // Catch:{ Exception -> 0x0287 }
        L_0x023a:
            java.lang.String r0 = r3.mEncoding     // Catch:{ Exception -> 0x0287 }
            boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Exception -> 0x0287 }
            if (r0 != 0) goto L_0x0245
            java.lang.String r0 = r3.mEncoding     // Catch:{ Exception -> 0x0287 }
            goto L_0x0247
        L_0x0245:
            r0 = r19
        L_0x0247:
            java.lang.String r3 = "text/css"
            java.io.ByteArrayInputStream r6 = new java.io.ByteArrayInputStream     // Catch:{ Exception -> 0x0287 }
            byte[] r11 = r11.toByteArray()     // Catch:{ Exception -> 0x0287 }
            r6.<init>(r11)     // Catch:{ Exception -> 0x0287 }
            android.webkit.WebResourceResponse r11 = new android.webkit.WebResourceResponse     // Catch:{ Exception -> 0x0287 }
            r11.<init>(r3, r0, r6)     // Catch:{ Exception -> 0x0287 }
            io.dcloud.common.adapter.ui.AdaWebview r3 = r1.mAdaWebview     // Catch:{ Exception -> 0x0289 }
            r3.mIsAdvanceCss = r10     // Catch:{ Exception -> 0x0289 }
            r3.mEncoding = r0     // Catch:{ Exception -> 0x0289 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0289 }
            r0.<init>()     // Catch:{ Exception -> 0x0289 }
            java.lang.String r3 = "提前注入CSS成功耗时："
            r0.append(r3)     // Catch:{ Exception -> 0x0289 }
            long r12 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x0289 }
            long r12 = r12 - r4
            r0.append(r12)     // Catch:{ Exception -> 0x0289 }
            io.dcloud.common.adapter.ui.AdaWebview r3 = r1.mAdaWebview     // Catch:{ Exception -> 0x0289 }
            java.lang.String r3 = r3.getOriginalUrl()     // Catch:{ Exception -> 0x0289 }
            r0.append(r3)     // Catch:{ Exception -> 0x0289 }
            r0.append(r7)     // Catch:{ Exception -> 0x0289 }
            r0.append(r2)     // Catch:{ Exception -> 0x0289 }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x0289 }
            io.dcloud.common.adapter.util.Logger.i(r8, r0)     // Catch:{ Exception -> 0x0289 }
            goto L_0x0290
        L_0x0287:
            r11 = r16
        L_0x0289:
            io.dcloud.common.adapter.ui.AdaWebview r0 = r1.mAdaWebview
            r0.mIsAdvanceCss = r9
            goto L_0x0290
        L_0x028e:
            r11 = r16
        L_0x0290:
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.ui.webview.WebLoadEvent.downloadResponseInjection(android.webkit.WebResourceResponse, java.lang.String, java.lang.String, java.lang.String, java.lang.String):android.webkit.WebResourceResponse");
    }

    private String getCacheLocalFilePath(String str, String str2) {
        IApp obtainApp;
        AdaWebview adaWebview = this.mAdaWebview;
        if (adaWebview == null || (obtainApp = adaWebview.obtainApp()) == null) {
            return null;
        }
        if (this.TYPE_JS.equals(str2)) {
            return obtainApp.obtainAppTempPath() + "__plus__cache__/" + Md5Utils.md5(str) + ".js";
        }
        return obtainApp.obtainAppTempPath() + "__plus__cache__/" + Md5Utils.md5(str) + ".css";
    }

    public static String getMimeType(String str) {
        String fileExtensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(str);
        String mimeTypeFromExtension = fileExtensionFromUrl != null ? MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtensionFromUrl) : null;
        return TextUtils.isEmpty(mimeTypeFromExtension) ? AssetHelper.DEFAULT_MIME_TYPE : mimeTypeFromExtension;
    }

    private CatchFile getUrlFile(String str, String str2) throws Exception {
        String cacheLocalFilePath = getCacheLocalFilePath(str, str2);
        try {
            if (DHFile.isExist(cacheLocalFilePath)) {
                CatchFile catchFile = new CatchFile();
                File file = new File(cacheLocalFilePath);
                catchFile.mFile = file;
                catchFile.mExist = file.exists();
                return catchFile;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            URL url = new URL(str);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            if (httpURLConnection instanceof HttpsURLConnection) {
                SSLSocketFactory sSLSocketFactory = DCloudTrustManager.getSSLSocketFactory();
                if (sSLSocketFactory != null) {
                    ((HttpsURLConnection) httpURLConnection).setSSLSocketFactory(sSLSocketFactory);
                }
                ((HttpsURLConnection) httpURLConnection).setHostnameVerifier(DCloudTrustManager.getHostnameVerifier(true));
            }
            httpURLConnection.setConnectTimeout(BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT);
            httpURLConnection.setReadTimeout(BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            int responseCode = httpURLConnection.getResponseCode();
            String contentType = httpURLConnection.getContentType();
            if (!TextUtils.isEmpty(contentType) && (((str2.equals(this.TYPE_JS) && contentType.contains("javascript")) || (str2.equals(this.TYPE_CSS) && (contentType.contains("text/css") || url.getPath().endsWith(".css")))) && (responseCode == 200 || responseCode == 206))) {
                InputStream inputStream = httpURLConnection.getInputStream();
                boolean writeFile = DHFile.writeFile(inputStream, cacheLocalFilePath);
                IOUtil.close(inputStream);
                if (writeFile) {
                    CatchFile catchFile2 = new CatchFile();
                    File file2 = new File(cacheLocalFilePath);
                    catchFile2.mFile = file2;
                    catchFile2.mExist = file2.exists();
                    catchFile2.mEncoding = httpURLConnection.getContentEncoding();
                    catchFile2.mContentType = contentType;
                    return catchFile2;
                }
                File file3 = new File(cacheLocalFilePath);
                if (file3.exists()) {
                    file3.delete();
                }
            }
            return null;
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        } catch (IOException e3) {
            e3.printStackTrace();
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0007, code lost:
        r0 = io.dcloud.common.adapter.ui.webview.WebResUtil.getEncryptionInputStream(r3, r2.mAdaWebview.obtainApp());
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.webkit.WebResourceResponse handleDecode(java.lang.String r3, android.webkit.WebResourceResponse r4) {
        /*
            r2 = this;
            boolean r0 = android.text.TextUtils.isEmpty(r3)
            if (r0 == 0) goto L_0x0007
            return r4
        L_0x0007:
            io.dcloud.common.adapter.ui.AdaWebview r0 = r2.mAdaWebview
            io.dcloud.common.DHInterface.IApp r0 = r0.obtainApp()
            java.io.InputStream r0 = io.dcloud.common.adapter.ui.webview.WebResUtil.getEncryptionInputStream(r3, r0)
            if (r0 == 0) goto L_0x001e
            android.webkit.WebResourceResponse r4 = new android.webkit.WebResourceResponse
            java.lang.String r3 = getMimeType(r3)
            java.lang.String r1 = "UTF-8"
            r4.<init>(r3, r1, r0)
        L_0x001e:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.ui.webview.WebLoadEvent.handleDecode(java.lang.String, android.webkit.WebResourceResponse):android.webkit.WebResourceResponse");
    }

    private void hideLoading() {
        this.mAdaWebview.obtainMainView().post(new Runnable() {
            public void run() {
                if (WebLoadEvent.this.mAdaWebview != null) {
                    long currentTimeMillis = System.currentTimeMillis();
                    WebLoadEvent webLoadEvent = WebLoadEvent.this;
                    if (currentTimeMillis - webLoadEvent.mShowLoadingTime < 1000) {
                        webLoadEvent.mAdaWebview.getDCWebView().getWebView().postDelayed(this, currentTimeMillis - WebLoadEvent.this.mShowLoadingTime);
                        return;
                    }
                    AdaFrameView adaFrameView = webLoadEvent.mAdaWebview.mFrameView;
                    adaFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_HIDE_LOADING, adaFrameView);
                }
            }
        });
    }

    private void listenPlusInjectTimeout(final WebView webView, final String str, final String str2) {
        AdaWebview adaWebview = this.mAdaWebview;
        if (adaWebview != null || !adaWebview.mPlusrequire.equals("none")) {
            Runnable runnable = this.Timeout_Plus_Inject_Runnable;
            if (runnable != null) {
                MessageHandler.removeCallbacks(runnable);
            }
            AnonymousClass7 r0 = new Runnable() {
                public void run() {
                    AdaWebview adaWebview = WebLoadEvent.this.mAdaWebview;
                    if (adaWebview != null && !adaWebview.isRealInject(str)) {
                        Logger.i("WebViewData", "listenPlusInjectTimeout url=" + str);
                        WebLoadEvent webLoadEvent = WebLoadEvent.this;
                        WebView webView = webView;
                        String str = str;
                        boolean unused = webLoadEvent.onLoadPlusJSContent(webView, str, "plus_inject_timeout_" + str2);
                        WebLoadEvent webLoadEvent2 = WebLoadEvent.this;
                        webLoadEvent2.mAdaWebview.mPreloadJsLoading = false;
                        webLoadEvent2.Timeout_Plus_Inject_Runnable = null;
                    }
                }
            };
            this.Timeout_Plus_Inject_Runnable = r0;
            MessageHandler.postDelayed(r0, 3000);
        }
    }

    private void loadAllJSContent(WebView webView, String str, String str2) {
        if (onLoadPlusJSContent(webView, str, str2)) {
            injectScript(webView, str, str2);
        }
    }

    private void onExecuteEvalJSStatck(WebView webView, String str, String str2) {
        String str3 = this.mAdaWebview.get_eval_js_stack();
        if (!PdrUtil.isEmpty(str3)) {
            completeLoadJs(webView, str, str2, new String[]{str3}, IF_LOAD_TEMPLATE, str);
        }
    }

    private void onLoadCssContent() {
        AdaWebview adaWebview = this.mAdaWebview;
        if (adaWebview.mIsAdvanceCss) {
            Logger.i(TAG, "已经提前注入CSS完成。不需要再注入了" + this.mAdaWebview.getOriginalUrl());
        } else if (adaWebview.loadCssFile()) {
            Logger.i(TAG, "提前注入CSS完成" + this.mAdaWebview.getOriginalUrl());
        }
    }

    /* access modifiers changed from: private */
    public boolean onLoadPlusJSContent(final WebView webView, final String str, final String str2) {
        if (this.mAdaWebview.mPlusrequire.equals("none")) {
            return false;
        }
        if (this.mAdaWebview.isRealInject(str)) {
            Logger.i(TAG, "all.js已经注入完成。不需要再注入了" + this.mAdaWebview.getOriginalUrl());
            return true;
        }
        Logger.i(TAG, "onLoadPlusJSContent all.js注入 " + this.mAdaWebview.getOriginalUrl() + ";tag=" + str2 + ";mAdaWebview.mPlusrequire=" + this.mAdaWebview.mPlusrequire);
        if (!this.mAdaWebview.mPlusrequire.equals("later") || !str2.equals("onPageFinished")) {
            AdaWebview adaWebview = this.mAdaWebview;
            adaWebview.mPlusInjectTag = str2;
            adaWebview.mPlusLoading = true;
            completeLoadJs(webView, str, str2, new String[]{this.mPlusJS, DIFFERENT_VERSION_JS}, IF_PLUSREADY_TEMPLATE, str);
        } else {
            webView.postDelayed(new Runnable() {
                public void run() {
                    WebLoadEvent webLoadEvent = WebLoadEvent.this;
                    AdaWebview adaWebview = webLoadEvent.mAdaWebview;
                    if (adaWebview != null) {
                        String str = str2;
                        adaWebview.mPlusInjectTag = str;
                        adaWebview.mPlusLoading = true;
                        WebView webView = webView;
                        String str2 = str;
                        webLoadEvent.completeLoadJs(webView, str2, str, new String[]{webLoadEvent.mPlusJS, WebLoadEvent.DIFFERENT_VERSION_JS}, WebLoadEvent.IF_PLUSREADY_TEMPLATE, str2);
                    }
                }
            }, 2000);
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void onPlusreadyEvent(WebView webView, String str, String str2) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(StringUtil.format(AbsoluteConst.EVENTS_DOCUMENT_EXECUTE_TEMPLATE, AbsoluteConst.EVENTS_PLUSREADY));
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append(StringUtil.format(AbsoluteConst.EVENTS_IFRAME_DOUCMENT_EXECUTE_TEMPLATE, AbsoluteConst.EVENTS_PLUSREADY));
        completeLoadJs(webView, str, str2, new String[]{stringBuffer.toString(), stringBuffer2.toString(), "plus.webview.currentWebview().__needTouchEvent__()"}, IF_PLUSREADY_EVENT_TEMPLATE, str);
    }

    private void printOpenLog(WebView webView, String str) {
        IApp obtainApp;
        String url = webView.getUrl();
        if (BaseInfo.isBase(webView.getContext()) && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(url) && (obtainApp = this.mAdaWebview.mFrameView.obtainApp()) != null && !str.startsWith(DeviceInfo.HTTP_PROTOCOL) && !url.startsWith(DeviceInfo.HTTP_PROTOCOL) && !str.startsWith(DeviceInfo.HTTPS_PROTOCOL) && !url.startsWith(DeviceInfo.HTTPS_PROTOCOL)) {
            Log.i(AbsoluteConst.HBUILDER_TAG, StringUtil.format(AbsoluteConst.OPENLOG, WebResUtil.getHBuilderPrintUrl(obtainApp.convert2RelPath(WebResUtil.getOriginalUrl(url))), WebResUtil.getHBuilderPrintUrl(obtainApp.convert2RelPath(WebResUtil.getOriginalUrl(str)))));
        }
    }

    private void printResourceLog(WebView webView, IApp iApp, String str, String str2) {
        if (!TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str) && webView != null && iApp != null && BaseInfo.isBase(webView.getContext()) && !str.equalsIgnoreCase(str2) && !TextUtils.isEmpty(str2)) {
            if (this.mClearCache && !this.mLastPageUrl.equalsIgnoreCase(str)) {
                webView.clearCache(true);
            }
            this.mLastPageUrl = str;
            String originalUrl = WebResUtil.getOriginalUrl(str);
            if (!str2.startsWith(DeviceInfo.HTTP_PROTOCOL) && !str2.startsWith(DeviceInfo.HTTPS_PROTOCOL)) {
                Log.i(AbsoluteConst.HBUILDER_TAG, StringUtil.format(AbsoluteConst.RESOURCELOG, WebResUtil.getHBuilderPrintUrl(iApp.convert2RelPath(originalUrl)), WebResUtil.getHBuilderPrintUrl(iApp.convert2RelPath(WebResUtil.getOriginalUrl(str2)))));
            }
        }
    }

    private boolean shouldRuntimeHandle(String str) {
        return PdrUtil.isDeviceRootDir(str) || PdrUtil.isNetPath(str) || str.startsWith(DeviceInfo.FILE_PROTOCOL);
    }

    private void showLoading() {
        this.mAdaWebview.getDCWebView().getWebView().post(new Runnable() {
            public void run() {
                AdaWebview adaWebview = WebLoadEvent.this.mAdaWebview;
                if (adaWebview != null) {
                    AdaFrameView adaFrameView = adaWebview.mFrameView;
                    adaFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_SHOW_LOADING, adaFrameView);
                }
            }
        });
        this.mShowLoadingTime = System.currentTimeMillis();
    }

    private void startTryLoadAllJSContent(WebView webView, String str, String str2) {
        loadAllJSContent(webView, str, str2);
    }

    public void closeWap2AppBlockDialog(boolean z) {
        WaitingView waitingView = this.mWap2AppBlockDialog;
        if (waitingView != null) {
            waitingView.close();
            this.mAdaWebview.obtainApp().unregisterSysEventListener(this.mWap2AppBlockDialogSysEventListener, ISysEventListener.SysEventType.onKeyUp);
            this.mWap2AppBlockDialog = null;
            this.mWap2AppBlockDialogSysEventListener = null;
            if (z) {
                AdaWebview adaWebview = this.mAdaWebview;
                adaWebview.loadUrl(adaWebview.mRecordLastUrl);
            }
        }
    }

    public void destroy() {
        this.mAdaWebview = null;
        this.mPlusJS = null;
        this.mTitleNViewProgressStop = null;
        this.mWap2AppBlockDialog = null;
        this.mWaitingForWapPage = null;
    }

    public void doUpdateVisitedHistory(WebView webView, String str, boolean z) {
        if (!PdrUtil.isEmpty(this.mdcloudwebviewclientlister)) {
            this.mdcloudwebviewclientlister.doUpdateVisitedHistory(webView, str, z);
        }
    }

    /* access modifiers changed from: package-private */
    public String getErrorPage() {
        String str = this.mAdaWebview.mFrameView.obtainFrameOptions().errorPage;
        if (URLUtil.isNetworkUrl(str)) {
            return str;
        }
        if (!TextUtils.isEmpty(str)) {
            IApp obtainApp = this.mAdaWebview.obtainApp();
            if ("none".equals(str)) {
                return str;
            }
            String convert2AbsFullPath = obtainApp.convert2AbsFullPath(this.mAdaWebview.obtainFullUrl(), str);
            File file = new File(convert2AbsFullPath);
            if (file.exists()) {
                return obtainApp.convert2WebviewFullPath(this.mAdaWebview.obtainFullUrl(), str);
            }
            if (BaseInfo.isWap2AppAppid(obtainApp.obtainAppId())) {
                String relPath = WebResUtil.getRelPath(PdrUtil.stripQuery(PdrUtil.stripAnchor(convert2AbsFullPath)), obtainApp);
                if (WebResUtil.isWap2appTemplateFile(obtainApp, relPath)) {
                    str = WebResUtil.handleWap2appTemplateFilePath(relPath);
                    file = new File(str);
                }
            }
            if (file.exists()) {
                return DeviceInfo.FILE_PROTOCOL + str;
            }
            String obtainConfigProperty = obtainApp.obtainConfigProperty("error");
            if (!"none".equals(obtainConfigProperty)) {
                return obtainApp.convert2WebviewFullPath((String) null, obtainConfigProperty);
            }
        } else {
            String obtainConfigProperty2 = this.mAdaWebview.obtainApp().obtainConfigProperty("error");
            if (!"none".equals(obtainConfigProperty2)) {
                return this.mAdaWebview.obtainApp().convert2WebviewFullPath((String) null, obtainConfigProperty2);
            }
        }
        return "none";
    }

    /* access modifiers changed from: package-private */
    public void injectScript(final WebView webView, final String str, final String str2) {
        if (!str2.equals("onPageFinished") || !this.mAdaWebview.mPlusrequire.equals("later")) {
            onPreloadJSContent(webView, str, str2);
            onPlusreadyEvent(webView, str, str2);
        } else {
            webView.postDelayed(new Runnable() {
                public void run() {
                    WebLoadEvent webLoadEvent = WebLoadEvent.this;
                    if (webLoadEvent.mAdaWebview != null) {
                        webLoadEvent.onPreloadJSContent(webView, str, str2);
                        WebLoadEvent.this.onPlusreadyEvent(webView, str, str2);
                    }
                }
            }, 2000);
        }
        onLoadCssContent();
    }

    public void listenPageFinishTimeout(final WebView webView, final String str, final String str2) {
        AdaWebview adaWebview = this.mAdaWebview;
        if (!adaWebview.mLoaded || !adaWebview.isRealInject(str)) {
            Runnable runnable = this.Timeout_Page_Finish_Runnable;
            if (runnable != null) {
                MessageHandler.removeCallbacks(runnable);
            }
            AnonymousClass8 r0 = new Runnable() {
                public void run() {
                    AdaWebview adaWebview = WebLoadEvent.this.mAdaWebview;
                    if (adaWebview != null && !adaWebview.mLoaded && adaWebview.isRealInject(str)) {
                        WebLoadEvent webLoadEvent = WebLoadEvent.this;
                        WebView webView = webView;
                        String str = str;
                        webLoadEvent.injectScript(webView, str, "page_finished_timeout_" + str2);
                        WebLoadEvent.this.Timeout_Page_Finish_Runnable = null;
                    }
                }
            };
            this.Timeout_Page_Finish_Runnable = r0;
            MessageHandler.postDelayed(r0, 6000);
            return;
        }
        injectScript(webView, str, str2);
    }

    public void onLoadResource(WebView webView, String str) {
        if (this.mAdaWebview != null) {
            if (this.printLog) {
                Logger.i(TAG, "onLoadResource url=" + str);
            }
            this.needResponseRedirect = true;
            printResourceLog(webView, this.mAdaWebview.mFrameView.obtainApp(), webView.getUrl(), str);
            IFrameView obtainFrameView = this.mAdaWebview.obtainFrameView();
            if (obtainFrameView.obtainStatus() != 3) {
                obtainFrameView.onLoading();
            }
            if (this.mAdaWebview.checkResourceLoading(str)) {
                AdaFrameView adaFrameView = this.mAdaWebview.mFrameView;
                adaFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_LISTEN_RESOURCE_LOADING, "{url:'" + str + "'}");
            }
            this.mAdaWebview.dispatchWebviewStateEvent(2, str);
            super.onLoadResource(webView, str);
        }
    }

    public void onPageFinished(WebView webView, String str) {
        boolean z;
        if (this.mAdaWebview != null) {
            Logger.d(TAG, "onPageFinished=" + str);
            if (PdrUtil.isEmpty(this.mAdaWebview.mFrameView.obtainApp())) {
                Logger.e(TAG, "mAdaWebview.mFrameView.obtainApp()===null");
            } else if (this.mAdaWebview.hadClearHistory(str)) {
                this.mAdaWebview.hasErrorPage = false;
            } else {
                if (this.mAdaWebview.hasErrorPage) {
                    String errorPage = getErrorPage();
                    if (PdrUtil.isEquals(str, errorPage) || ("data:text/html,chromewebdata".equals(str) && "none".equals(errorPage))) {
                        z = true;
                    } else {
                        return;
                    }
                } else {
                    z = false;
                }
                if (this.mAdaWebview.unReceiveTitle) {
                    Logger.i(TAG, "onPageFinished will exe titleUpdate =" + str);
                    AdaWebview adaWebview = this.mAdaWebview;
                    adaWebview.mFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_TITLE_UPDATE, adaWebview.getDCWebView().getTitle());
                    this.mAdaWebview.unReceiveTitle = false;
                }
                CookieSyncManager.getInstance().sync();
                Logger.i(TAG, "onPageFinished" + this.mAdaWebview.getOriginalUrl());
                this.mAdaWebview.dispatchWebviewStateEvent(1, str);
                this.mAdaWebview.loadForceAHeadJs();
                onLoadPlusJSContent(webView, str, "onPageFinished");
                if (this.mAdaWebview.isRealInject(str)) {
                    injectScript(webView, str, "onPageFinished");
                }
                AdaWebview adaWebview2 = this.mAdaWebview;
                adaWebview2.mFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_LOADED, adaWebview2);
                if (z) {
                    this.mAdaWebview.executeScript(StringUtil.format(ERROR_TEMPLATE, "error", this.mAdaWebview.getOriginalUrl(), this.mAdaWebview.errorPageUrl));
                    AdaWebview adaWebview3 = this.mAdaWebview;
                    adaWebview3.errorPageUrl = null;
                    adaWebview3.hasErrorPage = false;
                }
                AdaFrameView adaFrameView = this.mAdaWebview.mFrameView;
                if (adaFrameView.obtainStatus() != 3) {
                    adaFrameView.onPreShow((IFrameView) null);
                }
                AdaWebview adaWebview4 = this.mAdaWebview;
                if (!adaWebview4.mLoaded) {
                    adaWebview4.mLoaded = true;
                    adaWebview4.mPlusLoaded = true;
                }
                super.onPageFinished(webView, str);
                if (this.mAdaWebview.justClearOption && !str.startsWith("data:")) {
                    Logger.d(TAG, "onPageFinished mWebViewImpl.clearHistory url=" + str);
                    this.mAdaWebview.getDCWebView().clearHistory();
                    this.mAdaWebview.justClearOption = false;
                }
                this.mAdaWebview.getDCWebView().webReload(false);
                OnPageFinishedCallack onPageFinishedCallack = this.mPageFinishedCallack;
                if (onPageFinishedCallack != null) {
                    onPageFinishedCallack.onLoad();
                }
                if (this.mWaitingForWapPage != null) {
                    try {
                        ((ViewGroup) this.mAdaWebview.obtainFrameView().obtainMainView()).removeView(this.mWaitingForWapPage);
                        this.mWaitingForWapPage = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                this.mAdaWebview.checkInjectSitemap();
                if (!PdrUtil.isEmpty(this.mdcloudwebviewclientlister)) {
                    this.mdcloudwebviewclientlister.onPageFinished(webView, str);
                }
            }
        }
    }

    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        AdaWebview adaWebview = this.mAdaWebview;
        if (adaWebview != null) {
            if (adaWebview.hasErrorPage) {
                String errorPage = getErrorPage();
                if (!PdrUtil.isEquals(str, errorPage) && ((!"data:text/html,chromewebdata".equals(str) || !"none".equals(errorPage)) && (PdrUtil.isEmpty(this.mAdaWebview.errorPageUrl) || !this.mAdaWebview.errorPageUrl.equals(str)))) {
                    AdaWebview adaWebview2 = this.mAdaWebview;
                    adaWebview2.hasErrorPage = false;
                    adaWebview2.errorPageUrl = null;
                }
            }
            Logger.i(TAG, "onPageStarted url=" + str);
            this.mAdaWebview.onPageStarted();
            printOpenLog(webView, str);
            if (!this.mAdaWebview.hadClearHistory(str)) {
                if (this.mAdaWebview.mPlusrequire.equals("ahead")) {
                    listenPlusInjectTimeout(webView, str, "onPageStarted");
                }
                if (!str.startsWith("data:")) {
                    this.mAdaWebview.getDCWebView().setUrlStr(str);
                }
                this.mAdaWebview.resetPlusLoadSaveData();
                if (!PdrUtil.isEmpty(this.mAdaWebview.getDCWebView().getUrlStr())) {
                    AdaWebview adaWebview3 = this.mAdaWebview;
                    adaWebview3.mFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_WINDOW_CLOSE, adaWebview3);
                }
                this.mAdaWebview.dispatchWebviewStateEvent(0, str);
                AdaWebview adaWebview4 = this.mAdaWebview;
                AdaFrameView adaFrameView = adaWebview4.mFrameView;
                adaFrameView.dispatchFrameViewEvents("loading", adaWebview4);
                if (adaFrameView.obtainStatus() != 3) {
                    adaFrameView.onPreLoading();
                }
                super.onPageStarted(webView, str, bitmap);
                if (this.mAdaWebview.mFrameView.getFrameType() == 3) {
                    try {
                        if (this.mWaitingForWapPage == null) {
                            this.mWaitingForWapPage = new ProgressBar(this.mAdaWebview.getContext());
                            int i = AndroidResources.mResources.getDisplayMetrics().widthPixels;
                            int i2 = AndroidResources.mResources.getDisplayMetrics().heightPixels;
                            int parseInt = PdrUtil.parseInt("7%", i, -1);
                            ((ViewGroup) this.mAdaWebview.obtainFrameView().obtainMainView()).addView(this.mWaitingForWapPage, new AbsoluteLayout.LayoutParams(parseInt, parseInt, (i - parseInt) / 2, (i2 - parseInt) / 2));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Object titleNView = TitleNViewUtil.getTitleNView(this.mAdaWebview.obtainFrameView().obtainWindowMgr(), this.mAdaWebview.obtainFrameView().obtainWebView(), this.mAdaWebview.obtainFrameView(), TitleNViewUtil.getTitleNViewId(this.mAdaWebview.obtainFrameView()));
                if (titleNView instanceof ITitleNView) {
                    if (this.mTitleNViewProgressStop != null) {
                        TitleNViewUtil.stopProcess((ITitleNView) titleNView);
                        this.mAdaWebview.obtainWindowView().removeCallbacks(this.mTitleNViewProgressStop);
                        this.mTitleNViewProgressStop = null;
                    }
                    this.mTitleNViewProgressStop = new TitleNViewProgressStop(this.mAdaWebview);
                    TitleNViewUtil.startProcess((ITitleNView) titleNView);
                    this.mAdaWebview.obtainWindowView().postDelayed(this.mTitleNViewProgressStop, 6000);
                }
                if (!PdrUtil.isEmpty(this.mdcloudwebviewclientlister)) {
                    this.mdcloudwebviewclientlister.onPageStarted(webView, str, bitmap);
                }
            }
        }
    }

    public void onPreloadJSContent(WebView webView, String str, String str2) {
        if (this.mAdaWebview.obtainFrameView().obtainApp() == null || this.mAdaWebview.obtainFrameView().obtainApp().manifestBeParsed()) {
            AdaWebview adaWebview = this.mAdaWebview;
            if (adaWebview.mPreloadJsLoaded) {
                Logger.i(TAG, "mPreloadJs 已经提前注入JS完成。不需要再注入了" + this.mAdaWebview.getOriginalUrl());
                return;
            }
            String preLoadJsString = adaWebview.getPreLoadJsString();
            if (!PdrUtil.isEmpty(preLoadJsString)) {
                this.mAdaWebview.mPreloadJsLoading = true;
                Logger.i(TAG, " tag=" + str2 + ";url=" + str);
                completeLoadJs(webView, str, str2, new String[]{preLoadJsString}, IF_PRELOAD_TEMPLATE, this.mAdaWebview.mPreloadJsFile);
                this.mAdaWebview.mPreloadJsLoaded = true;
            }
        }
    }

    public void onReceivedError(WebView webView, int i, String str, final String str2) {
        if (this.mAdaWebview != null) {
            Logger.e(TAG, "onReceivedError description=" + str + ";failingUrl=" + str2 + ";errorCode=" + i);
            this.mAdaWebview.dispatchWebviewStateEvent(5, str);
            AdaWebview adaWebview = this.mAdaWebview;
            adaWebview.mFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_FAILED, adaWebview);
            AdaWebview adaWebview2 = this.mAdaWebview;
            adaWebview2.hasErrorPage = true;
            adaWebview2.errorPageUrl = str2;
            final IApp obtainApp = adaWebview2.mFrameView.obtainApp();
            if (obtainApp != null) {
                try {
                    if (BaseInfo.isWap2AppAppid(obtainApp.obtainAppId())) {
                        if (this.mAdaWebview.mFrameView.getFrameType() == 2 && !TextUtils.equals("none", obtainApp.obtainConfigProperty("launchError"))) {
                            Context context = this.mAdaWebview.getContext();
                            final AlertDialog create = new AlertDialog.Builder(context).create();
                            create.setTitle(R.string.dcloud_common_tips);
                            create.setCanceledOnTouchOutside(false);
                            create.setMessage(context.getString(R.string.dcloud_common_no_network_tips));
                            AnonymousClass3 r3 = new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (i == -2) {
                                        WebLoadEvent.this.mAdaWebview.getActivity().startActivity(new Intent("android.settings.SETTINGS"));
                                    } else if (i == -3) {
                                        Logger.e(WebLoadEvent.TAG, "onReceivedError try again");
                                        DCloudAdapterUtil.getIActivityHandler(WebLoadEvent.this.mAdaWebview.getActivity());
                                        WebLoadEvent.this.mAdaWebview.loadUrl(str2);
                                    } else if (i == -1) {
                                        Activity activity = WebLoadEvent.this.mAdaWebview.getActivity();
                                        DCloudAdapterUtil.getIActivityHandler(activity).updateParam("closewebapp", activity);
                                    }
                                    create.dismiss();
                                }
                            };
                            create.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                                    if (i != 4) {
                                        return false;
                                    }
                                    create.dismiss();
                                    Activity activity = WebLoadEvent.this.mAdaWebview.getActivity();
                                    DCloudAdapterUtil.getIActivityHandler(activity).updateParam("closewebapp", activity);
                                    return false;
                                }
                            });
                            create.setButton(-2, context.getString(R.string.dcloud_common_set_network), r3);
                            create.setButton(-3, context.getString(R.string.dcloud_common_retry), r3);
                            create.setButton(-1, context.getString(R.string.dcloud_common_exit), r3);
                            create.show();
                            obtainApp.registerSysEventListener(new ISysEventListener() {
                                public boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
                                    AdaWebview adaWebview;
                                    if (ISysEventListener.SysEventType.onResume != sysEventType || (adaWebview = WebLoadEvent.this.mAdaWebview) == null) {
                                        return false;
                                    }
                                    adaWebview.obtainMainView().postDelayed(new Runnable() {
                                        public void run() {
                                            Logger.e(WebLoadEvent.TAG, "onReceivedError 500ms retry after the onResume");
                                            DCloudAdapterUtil.getIActivityHandler(WebLoadEvent.this.mAdaWebview.getActivity());
                                            AnonymousClass5 r0 = AnonymousClass5.this;
                                            WebLoadEvent.this.mAdaWebview.loadUrl(str2);
                                        }
                                    }, 500);
                                    obtainApp.unregisterSysEventListener(this, sysEventType);
                                    return false;
                                }
                            }, ISysEventListener.SysEventType.onResume);
                            Logger.e(TAG, "onReceivedError do clearHistory");
                            this.mAdaWebview.clearHistory();
                        }
                    }
                    String errorPage = getErrorPage();
                    if (!"none".equals(errorPage)) {
                        Logger.e(TAG, "onReceivedError  load errorPage " + errorPage);
                        this.mAdaWebview.loadUrl(errorPage);
                    } else {
                        this.mAdaWebview.hasErrorPage = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (!PdrUtil.isEmpty(this.mdcloudwebviewclientlister)) {
                this.mdcloudwebviewclientlister.onReceivedError(webView, i, str, str2);
            }
        }
    }

    public void onReceivedSslError(WebView webView, final SslErrorHandler sslErrorHandler, final SslError sslError) {
        AdaWebview adaWebview = this.mAdaWebview;
        if (adaWebview != null && adaWebview.obtainApp() != null) {
            String obtainConfigProperty = this.mAdaWebview.obtainApp().obtainConfigProperty(IApp.ConfigProperty.CONFIG_UNTRUSTEDCA);
            Logger.i("onReceivedSslError", "onReceivedSslError++type====" + obtainConfigProperty);
            if (PdrUtil.isEquals(obtainConfigProperty, "refuse")) {
                sslErrorHandler.cancel();
            } else if (PdrUtil.isEquals(obtainConfigProperty, "warning")) {
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
                AnonymousClass2 r2 = new DialogInterface.OnClickListener() {
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
                create.setButton(-2, context.getResources().getString(17039360), r2);
                create.setButton(-1, context.getResources().getString(17039370), r2);
                create.show();
            } else {
                WebViewFactory.setSslHandlerState(sslErrorHandler, 1);
            }
            if (!PdrUtil.isEmpty(this.mdcloudwebviewclientlister)) {
                this.mdcloudwebviewclientlister.onReceivedSslError(webView, sslErrorHandler, sslError);
            }
        }
    }

    public void onUnhandledKeyEvent(WebView webView, KeyEvent keyEvent) {
        super.onUnhandledKeyEvent(webView, keyEvent);
        if (webView instanceof SysWebView) {
            SysWebView sysWebView = (SysWebView) webView;
            if (keyEvent.getAction() == 0) {
                sysWebView.doKeyDownAction(keyEvent.getKeyCode(), keyEvent);
            } else if (keyEvent.getAction() == 1) {
                sysWebView.doKeyUpAction(keyEvent.getKeyCode(), keyEvent);
            }
        }
    }

    public void onUpdatePlusData(WebView webView, String str, String str2) {
        AdaWebview adaWebview = this.mAdaWebview;
        adaWebview.executeScript(adaWebview.getScreenAndDisplayJson(adaWebview));
        onExecuteEvalJSStatck(webView, str, str2);
    }

    public void reset() {
        this.mPlusJS = "(function(){/*console.log('all.js loading href=' + location.href);*/if(location.__page__load__over__){return 2}if(!location.__plusready__){location.__plusready__=true;return 1}else{return 2}return 0})();\n" + this.mAdaWebview.mFrameView.obtainPrePlusreadyJs() + "\n" + DIFFERENT_VERSION_JS;
    }

    public void setDcloudwebviewclientListener(IDCloudWebviewClientListener iDCloudWebviewClientListener) {
        this.mdcloudwebviewclientlister = iDCloudWebviewClientListener;
    }

    public void setPageFinishedCallack(OnPageFinishedCallack onPageFinishedCallack) {
        this.mPageFinishedCallack = onPageFinishedCallack;
    }

    public WebResourceResponse shouldInterceptRequest(WebView webView, String str) {
        String str2;
        JSONObject jSONObject;
        String str3;
        File file;
        File file2;
        AdaWebview.OverrideResourceRequestItem overrideResourceRequestItem = null;
        if (this.mAdaWebview == null) {
            return null;
        }
        WebResourceResponse shouldInterceptRequest = super.shouldInterceptRequest(webView, str);
        WebResourceResponse shouldInterceptRequest2 = super.shouldInterceptRequest(webView, str);
        if (!PdrUtil.isEmpty(this.mdcloudwebviewclientlister)) {
            shouldInterceptRequest2 = this.mdcloudwebviewclientlister.shouldInterceptRequest(webView, str);
        }
        WebResourceResponse checkWebResourceResponseRedirect = checkWebResourceResponseRedirect(webView, str);
        if (checkWebResourceResponseRedirect != null) {
            return checkWebResourceResponseRedirect;
        }
        String str4 = "image/gif";
        if (!PdrUtil.isEmpty(str)) {
            if (str.startsWith("plusfile://")) {
                String replace = str.replace("plusfile://", "");
                if (replace.startsWith(BaseInfo.REL_PUBLIC_DOWNLOADS_DIR) || replace.startsWith(BaseInfo.REL_PRIVATE_DOC_DIR) || replace.startsWith(BaseInfo.REL_PUBLIC_DOCUMENTS_DIR)) {
                    String convert2WebviewFullPath = this.mAdaWebview.obtainApp().convert2WebviewFullPath((String) null, replace);
                    if (convert2WebviewFullPath.startsWith("file:///")) {
                        convert2WebviewFullPath = convert2WebviewFullPath.substring(7);
                    }
                    if (convert2WebviewFullPath.startsWith(DeviceInfo.FILE_PROTOCOL)) {
                        convert2WebviewFullPath = convert2WebviewFullPath.substring(6);
                    }
                    file2 = new File(convert2WebviewFullPath);
                } else {
                    file2 = new File(replace);
                }
                if (file2.exists()) {
                    try {
                        FileInputStream fileInputStream = new FileInputStream(file2);
                        String mimeType = PdrUtil.getMimeType(str);
                        if (str.contains(".jpg")) {
                            mimeType = "image/jpeg";
                        } else if (str.contains(".png")) {
                            mimeType = "image/png";
                        } else if (str.contains(".gif")) {
                            mimeType = str4;
                        }
                        return new WebResourceResponse(mimeType, (String) null, fileInputStream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } else if (str.contains("h5pscript://")) {
                InputStream encryptionInputStream = WebResUtil.getEncryptionInputStream(str.substring(str.indexOf("h5pscript://") + 12), this.mAdaWebview.obtainApp());
                String mimeType2 = PdrUtil.getMimeType(str);
                if (encryptionInputStream != null) {
                    if (str.contains(".jpg")) {
                        str4 = "image/jpeg";
                    } else if (str.contains(".png")) {
                        str4 = "image/png";
                    } else if (!str.contains(".gif")) {
                        str4 = mimeType2;
                    }
                    return new WebResourceResponse(str4, (String) null, encryptionInputStream);
                }
            } else if (str.startsWith("plus-confusion://")) {
                InputStream encryptionInputStream2 = WebResUtil.getEncryptionInputStream(this.mAdaWebview.obtainApp().convert2WebviewFullPath(this.mAdaWebview.obtainFullUrl(), str.substring(17)), this.mAdaWebview.obtainApp());
                String mimeType3 = PdrUtil.getMimeType(str);
                if (encryptionInputStream2 != null) {
                    return new WebResourceResponse(mimeType3, (String) null, encryptionInputStream2);
                }
            }
        }
        if (ImageLoaderUtil.isDownload(str) && (file = ImageLoaderL.getInstance().getDiscCache().get(str)) != null && file.exists()) {
            String mimeType4 = PdrUtil.getMimeType(str);
            if (str.contains(".jpg")) {
                str4 = "image/jpeg";
            } else if (str.contains(".png")) {
                str4 = "image/png";
            } else if (!str.contains(".gif")) {
                str4 = mimeType4;
            }
            try {
                return new WebResourceResponse(str4, (String) null, new FileInputStream(file));
            } catch (FileNotFoundException e2) {
                e2.printStackTrace();
            }
        }
        AdaWebview adaWebview = this.mAdaWebview;
        if (adaWebview != null) {
            overrideResourceRequestItem = adaWebview.checkResourceRequestUrl(str);
        }
        AdaWebview adaWebview2 = this.mAdaWebview;
        if (adaWebview2 == null) {
            return shouldInterceptRequest;
        }
        String str5 = adaWebview2.mEncoding;
        if (overrideResourceRequestItem != null) {
            str = overrideResourceRequestItem.redirect;
            str5 = overrideResourceRequestItem.encoding;
            str2 = overrideResourceRequestItem.mime;
        } else {
            str2 = "application/x-javascript";
        }
        try {
            Logger.i(TAG, "shouldInterceptRequest url=" + str + ";withJs=" + this.mAdaWebview.mInjectPlusWidthJs);
            shouldInterceptRequest = handleDecode(str, shouldInterceptRequest);
            if (shouldInterceptRequest == null) {
                if (this.mAdaWebview.mPlusrequire.equals("ahead") && this.mAdaWebview.hasPreLoadJsFile() && (((str3 = this.mAdaWebview.mInjectPlusWidthJs) == null || TextUtils.equals(str3, str)) && PdrUtil.isNetPath(str) && checkJsFile(str))) {
                    shouldInterceptRequest = downloadResponseInjection(shouldInterceptRequest, str, str2, str5, this.TYPE_JS);
                    if (shouldInterceptRequest != null) {
                        this.mAdaWebview.mInjectPlusWidthJs = str;
                    }
                } else if (!TextUtils.isEmpty(this.mAdaWebview.getCssString()) && !this.mAdaWebview.mIsAdvanceCss && PdrUtil.isNetPath(str) && checkCssFile(str)) {
                    str2 = "text/css";
                    shouldInterceptRequest = downloadResponseInjection(shouldInterceptRequest, str, str2, str5, this.TYPE_CSS);
                } else if (this.isInitAmapGEO) {
                    AdaWebview adaWebview3 = this.mAdaWebview;
                    if (!adaWebview3.mInjectGeoLoaded && DLGeolocation.checkInjectGeo(adaWebview3.mInjectGEO)) {
                        shouldInterceptRequest = downloadResponseInjection(shouldInterceptRequest, str, str2, str5, this.TYPE_JS);
                    }
                }
            }
            if (shouldInterceptRequest == null && !BaseInfo.isWap2AppAppid(this.mAppid) && PLUSREADY.equals(str) && !this.mAdaWebview.mPlusLoaded) {
                shouldInterceptRequest = downloadResponseInjection(shouldInterceptRequest, str, str2, str5, this.TYPE_JS);
            }
            if (shouldInterceptRequest == null) {
                BaseInfo.isUniAppAppid(this.mAdaWebview.obtainApp());
            }
            if (shouldInterceptRequest == null && overrideResourceRequestItem != null) {
                try {
                    shouldInterceptRequest = new WebResourceResponse(str2, str5, new FileInputStream(str));
                } catch (FileNotFoundException e3) {
                    e3.printStackTrace();
                }
            }
            if (shouldInterceptRequest != null && Build.VERSION.SDK_INT >= 21) {
                Map responseHeaders = shouldInterceptRequest.getResponseHeaders();
                if (responseHeaders == null) {
                    responseHeaders = new HashMap();
                }
                responseHeaders.put("Access-Control-Allow-Credentials", AbsoluteConst.TRUE);
                responseHeaders.put("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
                responseHeaders.put("Access-Control-Allow-Origin", "*");
                if (!(overrideResourceRequestItem == null || (jSONObject = overrideResourceRequestItem.headerJson) == null)) {
                    Iterator<String> keys = jSONObject.keys();
                    if (overrideResourceRequestItem.headerJson.length() > 0) {
                        while (keys.hasNext()) {
                            String next = keys.next();
                            responseHeaders.put(next, overrideResourceRequestItem.headerJson.opt(next).toString());
                        }
                    }
                }
                shouldInterceptRequest.setResponseHeaders(responseHeaders);
                return shouldInterceptRequest;
            }
        } catch (Exception e4) {
            e4.printStackTrace();
            Logger.e(this.mAppid + ";url=" + str);
        }
        return (PdrUtil.isEmpty(this.mdcloudwebviewclientlister) || PdrUtil.isEmpty(shouldInterceptRequest2)) ? shouldInterceptRequest : shouldInterceptRequest2;
    }

    public boolean shouldOverrideKeyEvent(WebView webView, KeyEvent keyEvent) {
        return false;
    }

    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        if (this.mAdaWebview == null) {
            return false;
        }
        Logger.e(TAG, "shouldOverrideUrlLoading url=" + str);
        AdaWebview adaWebview = this.mAdaWebview;
        adaWebview.mProgressIntValue = 0;
        adaWebview.mRecordLastUrl = str;
        if (adaWebview.checkOverrideUrl(str)) {
            Logger.e(TAG, "检测拦截回调shouldOverrideUrlLoading url=" + str);
            AdaFrameView adaFrameView = this.mAdaWebview.mFrameView;
            adaFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_OVERRIDE_URL_LOADING, "{url:'" + str + "'}");
            return true;
        }
        if (this.mAdaWebview.mFrameView.getFrameType() == 5 || (this.mAdaWebview.mFrameView.getFrameType() == 2 && directPageIsLaunchPage(this.mAdaWebview.obtainApp()))) {
            this.mAdaWebview.obtainApp().updateDirectPage(str);
        }
        if (!shouldRuntimeHandle(str) && this.mAdaWebview.mFrameView.getFrameType() != 6) {
            try {
                if (str.startsWith("sms:")) {
                    int indexOf = str.indexOf("sms:");
                    int indexOf2 = str.indexOf(Operators.CONDITION_IF_STRING);
                    if (indexOf2 == -1) {
                        this.mAdaWebview.getActivity().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
                        return true;
                    }
                    String substring = str.substring(indexOf + 4, indexOf2);
                    String substring2 = str.substring(indexOf2 + 1);
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("sms:" + substring));
                    intent.putExtra("address", substring);
                    intent.putExtra("sms_body", substring2);
                    this.mAdaWebview.getActivity().startActivity(intent);
                } else if (str.startsWith("intent://")) {
                    Intent parseUri = Intent.parseUri(str, 1);
                    parseUri.addCategory("android.intent.category.BROWSABLE");
                    parseUri.setComponent((ComponentName) null);
                    if (Build.VERSION.SDK_INT >= 15) {
                        parseUri.setSelector((Intent) null);
                    }
                    if (this.mAdaWebview.getActivity().getPackageManager().queryIntentActivities(parseUri, 0).size() > 0) {
                        this.mAdaWebview.getActivity().startActivityIfNeeded(parseUri, -1);
                    }
                } else {
                    AdaWebview adaWebview2 = this.mAdaWebview;
                    if (!(adaWebview2 == null || adaWebview2.getActivity() == null || !this.mAdaWebview.obtainApp().checkSchemeWhite(str))) {
                        this.mAdaWebview.getActivity().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
                    }
                }
            } catch (Exception unused) {
                Logger.e(TAG, "ActivityNotFoundException url=" + str);
            }
            return true;
        } else if (!PdrUtil.isEmpty(this.mdcloudwebviewclientlister)) {
            return this.mdcloudwebviewclientlister.shouldOverrideUrlLoading(webView, str);
        } else {
            return false;
        }
    }

    /* JADX WARNING: type inference failed for: r0v7, types: [java.net.URLConnection] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x008c  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x009b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.webkit.WebResourceResponse downloadResponse(android.webkit.WebView r11, java.lang.String r12, java.lang.String r13, android.webkit.WebResourceResponse r14, java.io.File r15, boolean r16) {
        /*
            r10 = this;
            r8 = r14
            boolean r0 = android.webkit.URLUtil.isNetworkUrl(r13)
            if (r0 == 0) goto L_0x00a2
            if (r15 != 0) goto L_0x000b
            goto L_0x00a2
        L_0x000b:
            r10.showLoading()
            r1 = 0
            java.net.URL r0 = new java.net.URL     // Catch:{ Exception -> 0x0086 }
            r4 = r13
            r0.<init>(r13)     // Catch:{ Exception -> 0x0086 }
            java.net.URLConnection r0 = r0.openConnection()     // Catch:{ Exception -> 0x0086 }
            r9 = r0
            java.net.HttpURLConnection r9 = (java.net.HttpURLConnection) r9     // Catch:{ Exception -> 0x0086 }
            r0 = 5000(0x1388, float:7.006E-42)
            r9.setConnectTimeout(r0)     // Catch:{ Exception -> 0x0080, all -> 0x007c }
            r9.setReadTimeout(r0)     // Catch:{ Exception -> 0x0080, all -> 0x007c }
            java.lang.String r0 = "GET"
            r9.setRequestMethod(r0)     // Catch:{ Exception -> 0x0080, all -> 0x007c }
            r0 = 1
            r9.setDoInput(r0)     // Catch:{ Exception -> 0x0080, all -> 0x007c }
            int r0 = r9.getResponseCode()     // Catch:{ Exception -> 0x0080, all -> 0x007c }
            r1 = 200(0xc8, float:2.8E-43)
            if (r0 == r1) goto L_0x0053
            r1 = 206(0xce, float:2.89E-43)
            if (r0 != r1) goto L_0x003a
            goto L_0x0053
        L_0x003a:
            r1 = 400(0x190, float:5.6E-43)
            if (r0 < r1) goto L_0x0042
            r1 = 500(0x1f4, float:7.0E-43)
            if (r0 < r1) goto L_0x004f
        L_0x0042:
            if (r16 == 0) goto L_0x004f
            r7 = 0
            r1 = r10
            r2 = r11
            r3 = r12
            r4 = r13
            r5 = r14
            r6 = r15
            r1.downloadResponse(r2, r3, r4, r5, r6, r7)     // Catch:{ Exception -> 0x0080, all -> 0x007c }
            goto L_0x0075
        L_0x004f:
            r10.hideLoading()     // Catch:{ Exception -> 0x0080, all -> 0x007c }
            goto L_0x0075
        L_0x0053:
            java.io.InputStream r0 = r9.getInputStream()     // Catch:{ Exception -> 0x0080, all -> 0x007c }
            java.lang.String r1 = r15.getAbsolutePath()     // Catch:{ Exception -> 0x0080, all -> 0x007c }
            boolean r0 = io.dcloud.common.adapter.io.DHFile.writeFile(r0, r1)     // Catch:{ Exception -> 0x0080, all -> 0x007c }
            if (r0 == 0) goto L_0x0065
            r10.hideLoading()     // Catch:{ Exception -> 0x0080, all -> 0x007c }
            goto L_0x0075
        L_0x0065:
            if (r16 == 0) goto L_0x0072
            r7 = 0
            r1 = r10
            r2 = r11
            r3 = r12
            r4 = r13
            r5 = r14
            r6 = r15
            r1.downloadResponse(r2, r3, r4, r5, r6, r7)     // Catch:{ Exception -> 0x0080, all -> 0x007c }
            goto L_0x0075
        L_0x0072:
            r10.hideLoading()     // Catch:{ Exception -> 0x0080, all -> 0x007c }
        L_0x0075:
            r9.disconnect()
            r10.hideLoading()
            goto L_0x0092
        L_0x007c:
            r0 = move-exception
            r2 = r10
            r1 = r9
            goto L_0x0099
        L_0x0080:
            r0 = move-exception
            r1 = r9
            goto L_0x0087
        L_0x0083:
            r0 = move-exception
            r2 = r10
            goto L_0x0099
        L_0x0086:
            r0 = move-exception
        L_0x0087:
            r0.printStackTrace()     // Catch:{ all -> 0x0083 }
            if (r1 == 0) goto L_0x008f
            r1.disconnect()
        L_0x008f:
            r10.hideLoading()
        L_0x0092:
            r2 = r10
            r1 = r12
            android.webkit.WebResourceResponse r0 = r10.handleDecode(r12, r14)
            return r0
        L_0x0099:
            if (r1 == 0) goto L_0x009e
            r1.disconnect()
        L_0x009e:
            r10.hideLoading()
            throw r0
        L_0x00a2:
            r2 = r10
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.ui.webview.WebLoadEvent.downloadResponse(android.webkit.WebView, java.lang.String, java.lang.String, android.webkit.WebResourceResponse, java.io.File, boolean):android.webkit.WebResourceResponse");
    }
}
