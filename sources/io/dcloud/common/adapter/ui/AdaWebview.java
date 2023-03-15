package io.dcloud.common.adapter.ui;

import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.dcloud.android.widget.DCProgressView;
import com.dcloud.android.widget.DCWebViewProgressBar;
import com.nostra13.dcloudimageloader.core.ImageLoaderL;
import com.taobao.weex.common.Constants;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.ui.component.WXBasicComponentType;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.IDCloudWebviewClientListener;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.IJsInterface;
import io.dcloud.common.DHInterface.ITitleNView;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.DHInterface.IWebviewStateListener;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.ui.ReceiveJSValue;
import io.dcloud.common.adapter.ui.RecordView;
import io.dcloud.common.adapter.ui.webview.DCWebView;
import io.dcloud.common.adapter.ui.webview.OnPageFinishedCallack;
import io.dcloud.common.adapter.ui.webview.WebResUtil;
import io.dcloud.common.adapter.ui.webview.WebViewFactory;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.constant.IntentConst;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.IOUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.StringUtil;
import io.dcloud.common.util.TitleNViewUtil;
import io.dcloud.common.util.net.http.WebkitCookieManagerProxy;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class AdaWebview extends AdaContainerFrameItem implements IWebview {
    public static boolean ScreemOrientationChangedNeedLayout = false;
    public static RecordView mRecordView = null;
    public static String sCustomUserAgent = null;
    public static CustomeizedInputConnection sCustomeizedInputConnection = null;
    public static String sDefalutUserAgent = null;
    public static boolean setedWebViewData = false;
    public String errorPageUrl = null;
    public MessageHandler.IMessages executeScriptListener = new MessageHandler.IMessages() {
        public void execute(Object obj) {
            String str = (String) obj;
            DCWebView dCWebView = AdaWebview.this.mWebViewImpl;
            if (dCWebView != null) {
                if (!str.startsWith(AbsoluteConst.PROTOCOL_JAVASCRIPT)) {
                    str = AbsoluteConst.PROTOCOL_JAVASCRIPT + str;
                }
                dCWebView.loadUrl(str);
            }
        }
    };
    private String favoriteOptions = "";
    public boolean hasErrorPage = false;
    private boolean isDisposed = false;
    boolean isPause = false;
    private boolean isStart = false;
    public boolean justClearOption = false;
    String mAppid;
    String mCssString = "";
    public String mEncoding = null;
    String[] mEvalJsOptionStack = null;
    private int mFixBottomHeight;
    private Object mFlag = null;
    public String mForceAHeadJsFile = null;
    public boolean mForceAHeadJsFileLoaded = false;
    private String mFrameId = null;
    public AdaFrameView mFrameView = null;
    public String mInjectGEO = "none";
    public boolean mInjectGeoLoaded = false;
    String mInjectPlusLoadedUrl = null;
    public String mInjectPlusWidthJs;
    public boolean mIsAdvanceCss = false;
    IJsInterface mJsInterfaces = null;
    JSONObject mListenResourceLoadingOptions = null;
    public boolean mLoadCompleted = false;
    public boolean mLoaded = false;
    boolean mLoading = false;
    public MessageHandler.IMessages mMesssageListener = new MessageHandler.IMessages() {
        public void execute(Object obj) {
            Object[] objArr = (Object[]) obj;
            AdaWebview.this.mJsInterfaces.exec(String.valueOf(objArr[0]), String.valueOf(objArr[1]), (JSONArray) objArr[2]);
        }
    };
    public boolean mNeedInjection = true;
    boolean mNeedSitemapJson = false;
    private IWebview mOpener;
    JSONArray mOverrideResourceRequestOptions = null;
    JSONObject mOverrideUrlLoadingDataOptions = null;
    public String mPlusInjectTag = "page_finished";
    public boolean mPlusLoaded = false;
    public boolean mPlusLoading = false;
    public String mPlusrequire = "normal";
    public ArrayList<String> mPreloadJsFile = new ArrayList<>(2);
    public boolean mPreloadJsLoaded = false;
    public boolean mPreloadJsLoading = false;
    public int mProgress = 0;
    public int mProgressIntValue = 0;
    public View mProgressView;
    public ReceiveJSValue mReceiveJSValue_android42 = null;
    public String mRecordLastUrl = null;
    private boolean mShareable = true;
    /* access modifiers changed from: private */
    public ArrayList<IWebviewStateListener> mStateListeners = null;
    private String mVideoFullscreen = "auto";
    private DCWebViewProgressBar mWebProgressView;
    DCWebView mWebViewImpl = null;
    AdaWebViewParent mWebViewParent = null;
    private String mWebviewANID = null;
    private String mWebviewUUID = null;
    private String needTouchEvent = "";
    String originalUrl = null;
    private String shareOptions = "";
    public boolean unReceiveTitle = true;

    public interface IFExecutePreloadJSContentCallBack {
        void callback(String str, String str2);
    }

    public class OverrideResourceRequestItem {
        public String encoding = null;
        public JSONObject headerJson = null;
        public String mime = null;
        public String redirect = null;

        public OverrideResourceRequestItem() {
        }
    }

    class WebProgressView extends DCProgressView {
        public WebProgressView(Context context) {
            super(context);
        }
    }

    protected AdaWebview(Context context) {
        super(context);
        initANID();
    }

    private String checkRedCssline(String str) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes());
        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(byteArrayInputStream));
        while (true) {
            try {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    return stringBuffer.substring(0, stringBuffer.length() - 1);
                }
                stringBuffer.append(JSUtil.QUOTE + readLine + "\"\n+");
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static void clearData() {
        setedWebViewData = false;
        sCustomUserAgent = null;
        sDefalutUserAgent = null;
    }

    private void pushEvalJsOption(String str) {
        String[] strArr = this.mEvalJsOptionStack;
        if (strArr == null) {
            this.mEvalJsOptionStack = new String[1];
        } else {
            String[] strArr2 = new String[(strArr.length + 1)];
            this.mEvalJsOptionStack = strArr2;
            System.arraycopy(strArr, 0, strArr2, 0, strArr.length);
        }
        String[] strArr3 = this.mEvalJsOptionStack;
        strArr3[strArr3.length - 1] = str;
        Logger.d("adawebview", "webviewimp=(" + this.mWebViewImpl + ");pushEvalJs=" + str);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
        return;
     */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* JADX WARNING: Removed duplicated region for block: B:10:? A[ExcHandler: IllegalAccessException | NoSuchFieldException (unused java.lang.Throwable), SYNTHETIC, Splitter:B:6:0x002d] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void releaseConfigCallback() {
        /*
            r5 = this;
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 1
            java.lang.String r2 = "sConfigCallback"
            r3 = 0
            r4 = 16
            if (r0 >= r4) goto L_0x002b
            java.lang.Class<android.webkit.WebView> r0 = android.webkit.WebView.class
            java.lang.String r4 = "mWebViewCore"
            java.lang.reflect.Field r0 = r0.getDeclaredField(r4)     // Catch:{  }
            java.lang.Class r0 = r0.getType()     // Catch:{  }
            java.lang.String r4 = "mBrowserFrame"
            java.lang.reflect.Field r0 = r0.getDeclaredField(r4)     // Catch:{  }
            java.lang.Class r0 = r0.getType()     // Catch:{  }
            java.lang.reflect.Field r0 = r0.getDeclaredField(r2)     // Catch:{  }
            r0.setAccessible(r1)     // Catch:{  }
            r0.set(r3, r3)     // Catch:{  }
            goto L_0x003d
        L_0x002b:
            java.lang.String r0 = "android.webkit.BrowserFrame"
            java.lang.Class r0 = java.lang.Class.forName(r0)     // Catch:{ IllegalAccessException | NoSuchFieldException -> 0x003d, IllegalAccessException | NoSuchFieldException -> 0x003d }
            java.lang.reflect.Field r0 = r0.getDeclaredField(r2)     // Catch:{ IllegalAccessException | NoSuchFieldException -> 0x003d, IllegalAccessException | NoSuchFieldException -> 0x003d }
            if (r0 == 0) goto L_0x003d
            r0.setAccessible(r1)     // Catch:{ IllegalAccessException | NoSuchFieldException -> 0x003d, IllegalAccessException | NoSuchFieldException -> 0x003d }
            r0.set(r3, r3)     // Catch:{ IllegalAccessException | NoSuchFieldException -> 0x003d, IllegalAccessException | NoSuchFieldException -> 0x003d }
        L_0x003d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.ui.AdaWebview.releaseConfigCallback():void");
    }

    private void startWebProgress() {
        if (this.mFrameView.obtainFrameOptions().mProgressJson != null) {
            if (this.mWebProgressView == null) {
                this.mWebProgressView = new DCWebViewProgressBar(getContext());
            }
            JSONObject jSONObject = this.mFrameView.obtainFrameOptions().mProgressJson;
            String optString = jSONObject.optString("color", "#00FF00");
            String optString2 = jSONObject.optString("height", "2px");
            this.mWebProgressView.setColorInt(PdrUtil.stringToColor(optString));
            int convertToScreenInt = PdrUtil.convertToScreenInt(optString2, obtainWindowView().getMeasuredWidth(), 0, getScale());
            this.mWebProgressView.setVisibility(0);
            this.mWebProgressView.setAlphaInt(255);
            if (this.mWebProgressView.getParent() == null && getWebviewParent() != null) {
                ViewGroup viewGroup = (ViewGroup) getWebviewParent().obtainMainView();
                if (viewGroup != null) {
                    viewGroup.addView(this.mWebProgressView, new ViewGroup.LayoutParams(-1, convertToScreenInt));
                } else {
                    return;
                }
            }
            this.mWebProgressView.startProgress();
            return;
        }
        DCWebViewProgressBar dCWebViewProgressBar = this.mWebProgressView;
        if (dCWebViewProgressBar != null) {
            dCWebViewProgressBar.setVisibility(8);
            if (this.mWebProgressView.getParent() != null) {
                ((ViewGroup) this.mWebProgressView.getParent()).removeView(this.mWebProgressView);
            }
            this.mWebProgressView = null;
        }
    }

    public void addFrameItem(AdaFrameItem adaFrameItem) {
        super.addFrameItem(adaFrameItem);
    }

    public void addJsInterface(String str, String str2) {
        this.mWebViewImpl.addJavascriptInterface(str2, str);
    }

    public void addStateListener(IWebviewStateListener iWebviewStateListener) {
        if (this.mStateListeners == null) {
            this.mStateListeners = new ArrayList<>();
        }
        if (iWebviewStateListener != null) {
            this.mStateListeners.add(iWebviewStateListener);
        }
    }

    public void appendPreloadJsFile(String str) {
        this.mPreloadJsFile.add(str);
        Logger.d("AdaWebview", "appendPreloadJsFile mPreloadJsFile=" + this.mPreloadJsFile + ";this=" + this);
        if (this.mPlusLoaded) {
            Log.d("AdaWebview", "appendPreloadJsFile---=" + str);
            String loadFileContent = loadFileContent(str, this.mFrameView.obtainApp().obtainRunningAppMode() == 1 ? 0 : 2);
            if (!TextUtils.isEmpty(loadFileContent)) {
                loadUrl(AbsoluteConst.PROTOCOL_JAVASCRIPT + loadFileContent + ";");
            }
        }
    }

    public boolean backOrForward(int i) {
        return this.mWebViewImpl.canGoBackOrForward(i);
    }

    public boolean canGoBack() {
        boolean z = !this.justClearOption && this.mWebViewImpl.canGoBack();
        Logger.d("AdaFrameItem", "canGoBack" + this.mWebViewImpl.getUrlStr() + ";" + this.justClearOption + ";" + z);
        return z;
    }

    public boolean canGoForward() {
        return !this.justClearOption && this.mWebViewImpl.canGoForward();
    }

    public void checkIfNeedLoadOriginalUrl() {
        if (!this.mLoading && !this.mLoaded) {
            loadUrl(getOriginalUrl());
        }
    }

    public void checkInjectSitemap() {
        if (this.mNeedSitemapJson && this.mLoaded && this.mPreloadJsLoaded) {
            StringBuffer stringBuffer = new StringBuffer();
            File file = new File(BaseInfo.sBaseFsSitMapPath + File.separator + obtainApp().obtainAppId() + "_sitemap.json");
            if (file.exists()) {
                try {
                    stringBuffer.append(";window.__wap2app_sitemap=");
                    stringBuffer.append(IOUtil.toString(new FileInputStream(file)));
                    stringBuffer.append(";wap2app&wap2app.initSitemap();\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                executeScript(stringBuffer.toString());
                this.mNeedSitemapJson = false;
            }
        }
    }

    public boolean checkOverrideUrl(String str) {
        DCWebView dCWebView = this.mWebViewImpl;
        if (dCWebView == null) {
            return false;
        }
        return dCWebView.checkOverrideUrl(this.mOverrideUrlLoadingDataOptions, str);
    }

    public void checkPreLoadJsContent() {
        DCWebView dCWebView = this.mWebViewImpl;
        dCWebView.onPreloadJSContent("checkPreLoadJsContent " + this);
    }

    public boolean checkResourceLoading(String str) {
        JSONObject jSONObject = this.mListenResourceLoadingOptions;
        if (jSONObject == null || !jSONObject.has("match")) {
            return true;
        }
        try {
            return Pattern.compile(this.mListenResourceLoadingOptions.optString("match")).matcher(str).matches();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public OverrideResourceRequestItem checkResourceRequestUrl(String str) {
        if (this.mOverrideResourceRequestOptions == null || Build.VERSION.SDK_INT < 15) {
            return null;
        }
        int i = 0;
        while (i < this.mOverrideResourceRequestOptions.length()) {
            try {
                JSONObject optJSONObject = this.mOverrideResourceRequestOptions.optJSONObject(i);
                String optString = optJSONObject.optString("match", "");
                if (TextUtils.isEmpty(optString) || !Pattern.compile(optString).matcher(str).matches()) {
                    i++;
                } else {
                    String convert2AbsFullPath = obtainApp().convert2AbsFullPath(optJSONObject.optString("redirect"));
                    String optString2 = optJSONObject.optString("mime", PdrUtil.getMimeType(convert2AbsFullPath));
                    String optString3 = optJSONObject.optString("encoding", "utf-8");
                    JSONObject optJSONObject2 = optJSONObject.optJSONObject(WXBasicComponentType.HEADER);
                    OverrideResourceRequestItem overrideResourceRequestItem = new OverrideResourceRequestItem();
                    overrideResourceRequestItem.redirect = convert2AbsFullPath;
                    overrideResourceRequestItem.encoding = optString3;
                    overrideResourceRequestItem.mime = optString2;
                    overrideResourceRequestItem.headerJson = optJSONObject2;
                    return overrideResourceRequestItem;
                }
            } catch (Exception e) {
                Logger.e("AdaWebview", "checkResourceRequestUrl e==" + e.getMessage());
                return null;
            }
        }
        return null;
    }

    public boolean checkWhite(String str) {
        DCWebView dCWebView = this.mWebViewImpl;
        if (dCWebView != null) {
            return dCWebView.checkWhite(str);
        }
        return false;
    }

    public void clearHistory() {
        if (this.mWebViewImpl != null) {
            Logger.d("AdaFrameItem", "clearHistory url=" + this.mWebViewImpl.getUrlStr());
            this.justClearOption = true;
            this.mWebViewImpl.loadData("<html><head><meta charset=\"utf-8\"></head><body></body><html>", "text/html", "utf-8");
            this.mWebViewImpl.setUrlStr("");
        }
    }

    public void dispatchWebviewStateEvent(int i, Object obj) {
        if (i != 1) {
            if (i == 3) {
                int parseInt = Integer.parseInt(String.valueOf(obj));
                this.mProgress = parseInt;
                if (!this.isStart && parseInt < 100) {
                    startWebProgress();
                    this.isStart = true;
                }
                if (this.mProgress >= 100 && this.isStart) {
                    this.isStart = false;
                    DCWebViewProgressBar dCWebViewProgressBar = this.mWebProgressView;
                    if (dCWebViewProgressBar != null) {
                        dCWebViewProgressBar.finishProgress();
                    }
                }
            }
        } else if (this.isStart) {
            this.isStart = false;
            DCWebViewProgressBar dCWebViewProgressBar2 = this.mWebProgressView;
            if (dCWebViewProgressBar2 != null) {
                dCWebViewProgressBar2.finishProgress();
            }
        }
        ArrayList<IWebviewStateListener> arrayList = this.mStateListeners;
        if (arrayList != null) {
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                this.mStateListeners.get(size).onCallBack(i, obj);
            }
        }
    }

    public void dispose() {
        super.dispose();
        if (!this.isDisposed) {
            this.isDisposed = true;
            this.mProgressView = null;
            if (this.mFrameView.getFrameType() == 5 && !this.mLoaded && this.mProgressIntValue >= 50) {
                this.mFrameView.obtainApp().checkOrLoadlaunchWebview();
            }
            BaseInfo.s_Webview_Count--;
            try {
                DCWebView dCWebView = this.mWebViewImpl;
                if (dCWebView != null) {
                    dCWebView.stopLoading();
                }
            } catch (Exception unused) {
            }
            MessageHandler.sendMessage(new MessageHandler.IMessages() {
                public void execute(Object obj) {
                    try {
                        AdaWebview adaWebview = AdaWebview.this;
                        if (adaWebview.mFrameView != null) {
                            adaWebview.mFrameView = null;
                        }
                        DCWebView dCWebView = adaWebview.mWebViewImpl;
                        if (dCWebView != null) {
                            dCWebView.clearCache(false);
                            if (AdaWebview.this.mWebViewImpl.getWebView().getParent() != null) {
                                ((ViewGroup) AdaWebview.this.mWebViewImpl.getWebView().getParent()).removeView(AdaWebview.this.mWebViewImpl.getWebView());
                            }
                            AdaWebview.this.mWebViewImpl.destroyWeb();
                            AdaWebview.this.releaseConfigCallback();
                            AdaWebview.this.mWebViewImpl = null;
                        }
                        AdaWebview adaWebview2 = AdaWebview.this;
                        adaWebview2.mJsInterfaces = null;
                        adaWebview2.mMesssageListener = null;
                        adaWebview2.executeScriptListener = null;
                        adaWebview2.mWebViewParent = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (AdaWebview.this.mStateListeners != null) {
                        AdaWebview.this.mStateListeners.clear();
                    }
                    ArrayList unused = AdaWebview.this.mStateListeners = null;
                    System.gc();
                }
            }, (Object) null);
        }
    }

    public void endWebViewEvent(String str) {
        if (this.mWebViewParent != null) {
            if (PdrUtil.isEquals(str, AbsoluteConst.PULL_DOWN_REFRESH)) {
                this.mWebViewParent.endPullRefresh();
            } else if (PdrUtil.isEquals(str, AbsoluteConst.BOUNCE_REGISTER)) {
                this.mWebViewParent.resetBounce();
            }
        }
    }

    public void evalJS(String str) {
        if (this.mPlusLoaded) {
            executeScript(str);
        } else {
            pushEvalJsOption(str);
        }
    }

    public void evalJSSync(String str, ICallBack iCallBack) {
        if (this.mWebViewImpl != null) {
            if (!str.startsWith(AbsoluteConst.PROTOCOL_JAVASCRIPT)) {
                str = AbsoluteConst.PROTOCOL_JAVASCRIPT + str;
            }
            this.mWebViewImpl.evalJSSync(str, iCallBack);
        }
    }

    public String execScript(String str, String str2, JSONArray jSONArray, boolean z) {
        if (!z) {
            return this.mJsInterfaces.exec(str, str2, jSONArray);
        }
        MessageHandler.IMessages iMessages = this.mMesssageListener;
        if (iMessages == null) {
            return null;
        }
        MessageHandler.sendMessage(iMessages, new Object[]{str, str2, jSONArray});
        return null;
    }

    public void executeScript(String str) {
        MessageHandler.IMessages iMessages;
        if (str != null && (iMessages = this.executeScriptListener) != null) {
            MessageHandler.sendMessage(iMessages, str);
        }
    }

    /* access modifiers changed from: package-private */
    public void execute_eval_js_stack() {
        if (this.mEvalJsOptionStack != null) {
            Logger.d("adawebview", "webviewimp=" + this.mWebViewImpl + ";execute_eval_js_stack count=" + this.mEvalJsOptionStack.length);
            int i = 0;
            while (true) {
                String[] strArr = this.mEvalJsOptionStack;
                if (i < strArr.length) {
                    executeScript(strArr[i]);
                    i++;
                } else {
                    this.mEvalJsOptionStack = null;
                    return;
                }
            }
        }
    }

    public String getAppName() {
        AdaFrameView adaFrameView = this.mFrameView;
        return (adaFrameView == null || adaFrameView.obtainApp() == null) ? "" : this.mFrameView.obtainApp().obtainAppName();
    }

    public String getCookie(String str) {
        DCWebView dCWebView = this.mWebViewImpl;
        if (dCWebView != null) {
            return dCWebView.getCookie(str);
        }
        return null;
    }

    public String getCssString() {
        return this.mCssString;
    }

    public DCWebView getDCWebView() {
        return this.mWebViewImpl;
    }

    public int getFixBottom() {
        return this.mFixBottomHeight;
    }

    public Object getFlag() {
        return this.mFlag;
    }

    public IWebview getOpener() {
        return this.mOpener;
    }

    public String getOriginalUrl() {
        return this.originalUrl;
    }

    public String getPreLoadJsString() {
        IApp obtainApp;
        if (PdrUtil.isEmpty(this.mPreloadJsFile) || (obtainApp = this.mFrameView.obtainApp()) == null || this.mPreloadJsFile.size() <= 0) {
            return "";
        }
        int i = obtainApp.obtainRunningAppMode() == 1 ? 0 : 2;
        Iterator<String> it = this.mPreloadJsFile.iterator();
        String str = ";";
        while (it.hasNext()) {
            String next = it.next();
            if (!this.mPlusrequire.equals("none") || (!next.contains("__wap2app.js") && !next.contains("__wap2appconfig.js"))) {
                String wrapAppendJsFile = wrapAppendJsFile(next, i);
                if (!TextUtils.isEmpty(wrapAppendJsFile)) {
                    str = str + wrapAppendJsFile + "\n";
                }
            }
        }
        return str + "\n";
    }

    public float getScale() {
        return this.mWebViewImpl.getScale();
    }

    public float getScaleOfOpenerWebview() {
        return getScale();
    }

    public String getScreenAndDisplayJson(IWebview iWebview) {
        float scale = iWebview.getScale();
        IApp obtainApp = iWebview.obtainApp();
        int i = obtainApp.getInt(2);
        int i2 = obtainApp.getInt(0);
        int i3 = (int) (((float) i2) / scale);
        return StringUtil.format("(function(p){p.screen.scale=%f;p.screen.resolutionHeight=%d;p.screen.resolutionWidth=%d;p.screen.height=%d;p.screen.width=%d;p.screen.dpiX=%f;p.screen.dpiY=%f;p.display.resolutionHeight=%d;p.display.resolutionWidth=%d;})(((window.__html5plus__&&__html5plus__.isReady)?__html5plus__:(navigator.plus&&navigator.plus.isReady)?navigator.plus:window.plus));", Float.valueOf(scale), Integer.valueOf((int) (((float) i) / scale)), Integer.valueOf(i3), Integer.valueOf(i), Integer.valueOf(i2), Float.valueOf(DeviceInfo.dpiX), Float.valueOf(DeviceInfo.dpiY), Integer.valueOf((int) (((float) obtainApp.getInt(1)) / scale)), Integer.valueOf(i3));
    }

    public String getTitle() {
        return this.mWebViewImpl.getTitle();
    }

    public String getWebviewANID() {
        return this.mWebviewANID;
    }

    /* access modifiers changed from: protected */
    public AdaWebViewParent getWebviewParent() {
        return this.mWebViewParent;
    }

    public String getWebviewProperty(String str) {
        if ("getShareOptions".equals(str)) {
            return this.shareOptions;
        }
        if ("getFavoriteOptions".equals(str)) {
            return this.favoriteOptions;
        }
        if ("needTouchEvent".equals(str)) {
            return String.valueOf(this.needTouchEvent);
        }
        if (IWebview.USER_AGENT.equals(str)) {
            DCWebView dCWebView = this.mWebViewImpl;
            if (dCWebView != null) {
                return dCWebView.getUserAgentString();
            }
            return sDefalutUserAgent;
        } else if (AbsoluteConst.JSON_KEY_VIDEO_FULL_SCREEN.equals(str)) {
            return this.mVideoFullscreen;
        } else {
            if ("plusrequire".equals(str)) {
                return this.mPlusrequire;
            }
            if (!AbsoluteConst.JSON_KEY_SHAREABLE.equals(str)) {
                return null;
            }
            return this.mShareable + "";
        }
    }

    public final String getWebviewUUID() {
        return this.mWebviewUUID;
    }

    public String get_eval_js_stack() {
        StringBuffer stringBuffer = new StringBuffer();
        if (this.mEvalJsOptionStack != null) {
            int i = 0;
            while (true) {
                String[] strArr = this.mEvalJsOptionStack;
                if (i >= strArr.length) {
                    break;
                }
                String str = strArr[i];
                if (str.endsWith(";")) {
                    stringBuffer.append(str);
                } else {
                    stringBuffer.append(str);
                    stringBuffer.append(";");
                }
                i++;
            }
            this.mEvalJsOptionStack = null;
        }
        return stringBuffer.toString();
    }

    public void goBackOrForward(int i) {
        DCWebView dCWebView = this.mWebViewImpl;
        if (dCWebView != null) {
            dCWebView.goBackOrForward(i);
        }
    }

    public boolean hadClearHistory(String str) {
        return this.justClearOption && PdrUtil.isEquals(str, "data:text/html,<html><head><meta charset=\"utf-8\"></head><body></body><html>");
    }

    public boolean hasPreLoadJsFile() {
        return this.mPreloadJsFile.size() > 0;
    }

    public boolean hasWebViewEvent(String str) {
        AdaWebViewParent adaWebViewParent;
        if (!PdrUtil.isEquals(str, AbsoluteConst.PULL_DOWN_REFRESH) || (adaWebViewParent = this.mWebViewParent) == null) {
            return false;
        }
        return adaWebViewParent.isSetPull2Refresh;
    }

    public void init() {
        DCWebView dCWebView = this.mWebViewImpl;
        if (dCWebView != null) {
            dCWebView.init();
        }
    }

    /* access modifiers changed from: protected */
    public void initANID() {
        if (TextUtils.isEmpty(this.mWebviewANID)) {
            this.mWebviewANID = "AD_Webview" + System.currentTimeMillis();
        }
    }

    /* access modifiers changed from: protected */
    public void initSitemapState() {
        this.mNeedSitemapJson = (BaseInfo.isWap2AppAppid(this.mAppid) && this.mFrameView.getFrameType() == 2) || this.mFrameView.getFrameType() == 4;
    }

    public final void initWebviewUUID(String str) {
        this.mWebviewUUID = str;
    }

    public boolean isDisposed() {
        return this.isDisposed;
    }

    public boolean isIWebViewFocusable() {
        return obtainWindowView().isFocusable();
    }

    public boolean isLoaded() {
        return this.mLoaded;
    }

    public boolean isPause() {
        return this.isPause;
    }

    public boolean isRealInject(String str) {
        return this.mPlusLoaded && TextUtils.equals(PdrUtil.getUrlPathName(str), PdrUtil.getUrlPathName(this.mInjectPlusLoadedUrl));
    }

    public boolean isUniService() {
        return false;
    }

    public boolean isUniWebView() {
        return false;
    }

    public void loadContentData(String str, String str2, String str3, String str4) {
        this.mWebViewImpl.loadDataWithBaseURL(str, str2, str3, str4, str);
    }

    public boolean loadCssFile() {
        if (PdrUtil.isEmpty(this.mCssString)) {
            return false;
        }
        String replaceAll = this.mCssString.replaceAll(JSUtil.QUOTE, "'");
        this.mCssString = replaceAll;
        String checkRedCssline = checkRedCssline(replaceAll);
        loadUrl("javascript:var container = document.getElementsByTagName('head')[0];\nvar addStyle = document.createElement('style');\naddStyle.rel = 'stylesheet';\naddStyle.type = 'text/css';\naddStyle.innerHTML = " + checkRedCssline + ";\ncontainer.appendChild(addStyle);\nfirstNode = container.children[0];\n    container.appendChild(addStyle);\n");
        return true;
    }

    /* access modifiers changed from: package-private */
    public void loadFileContent(IFExecutePreloadJSContentCallBack iFExecutePreloadJSContentCallBack) {
        if (!PdrUtil.isEmpty(this.mPreloadJsFile)) {
            try {
                IApp obtainApp = this.mFrameView.obtainApp();
                if (obtainApp != null) {
                    int i = obtainApp.obtainRunningAppMode() == 1 ? 0 : 2;
                    Iterator<String> it = this.mPreloadJsFile.iterator();
                    while (it.hasNext()) {
                        String next = it.next();
                        String wrapAppendJsFile = wrapAppendJsFile(next, i);
                        if (!TextUtils.isEmpty(wrapAppendJsFile)) {
                            String str = AbsoluteConst.PROTOCOL_JAVASCRIPT + wrapAppendJsFile + ";";
                            if (iFExecutePreloadJSContentCallBack == null) {
                                loadUrl(str);
                            } else {
                                iFExecutePreloadJSContentCallBack.callback(this.mWebViewImpl.convertRelPath(next), str);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void loadForceAHeadJs() {
        if (!PdrUtil.isEmpty(this.mFrameView.obtainApp()) && !this.mForceAHeadJsFileLoaded && !TextUtils.isEmpty(this.mForceAHeadJsFile)) {
            String loadFileContent = loadFileContent(this.mForceAHeadJsFile, this.mFrameView.obtainApp().obtainRunningAppMode() == 1 ? 0 : 2);
            if (!TextUtils.isEmpty(loadFileContent)) {
                loadUrl(AbsoluteConst.PROTOCOL_JAVASCRIPT + loadFileContent + ";");
                this.mForceAHeadJsFileLoaded = true;
            }
        }
    }

    public void loadUrl(String str) {
        DCWebView dCWebView = this.mWebViewImpl;
        if (dCWebView != null && PdrUtil.isEmpty(dCWebView.getUrlStr()) && str != null && !str.startsWith(AbsoluteConst.PROTOCOL_JAVASCRIPT)) {
            this.mWebViewImpl.setUrlStr(str);
            this.hasErrorPage = false;
            this.errorPageUrl = null;
        }
        DCWebView dCWebView2 = this.mWebViewImpl;
        if (dCWebView2 != null) {
            dCWebView2.loadUrl(str);
        }
    }

    public String obtainFrameId() {
        return this.mFrameId;
    }

    public IFrameView obtainFrameView() {
        return this.mFrameView;
    }

    public String obtainFullUrl() {
        if (Build.VERSION.SDK_INT < 14 || TextUtils.isEmpty(this.mWebViewImpl.getUrlStr())) {
            return this.mWebViewImpl.getUrl();
        }
        return this.mWebViewImpl.getUrlStr();
    }

    public String obtainPageTitle() {
        try {
            if (Looper.myLooper() != null && Looper.getMainLooper() == Looper.myLooper()) {
                String title = this.mWebViewImpl.getTitle();
                return TextUtils.isEmpty(title) ? this.mWebViewImpl.getPageTitle() : title;
            }
        } catch (Exception e) {
            Logger.e("AdaWebview", "e.getMessage()==" + e.getMessage());
        }
        return this.mWebViewImpl.getPageTitle();
    }

    public String obtainUrl() {
        DCWebView dCWebView = this.mWebViewImpl;
        if (dCWebView == null) {
            return "";
        }
        if (dCWebView.getUrlStr() == null) {
            return this.mWebViewImpl.getUrl();
        }
        int indexOf = this.mWebViewImpl.getUrlStr().indexOf(this.mWebViewImpl.getBaseUrl());
        String urlStr = this.mWebViewImpl.getUrlStr();
        return indexOf >= 0 ? urlStr.substring(this.mWebViewImpl.getBaseUrl().length()) : urlStr;
    }

    public WebView obtainWebview() {
        if (this.mWebViewImpl.getWebView() instanceof WebView) {
            return (WebView) this.mWebViewImpl.getWebView();
        }
        return null;
    }

    public ViewGroup obtainWindowView() {
        return this.mWebViewImpl.getWebView();
    }

    public boolean onDispose() {
        View view = this.mProgressView;
        if (!(view == null || view.getParent() == null)) {
            ((ViewGroup) this.mProgressView.getParent()).removeView(this.mProgressView);
        }
        return super.onDispose();
    }

    public void onPageStarted() {
        this.mLoading = true;
        DCWebView dCWebView = this.mWebViewImpl;
        if (dCWebView != null) {
            dCWebView.onPageStarted();
        }
        try {
            if (this.mFrameView.getFrameType() == 5 && TextUtils.equals(this.mFrameView.obtainApp().obtainWebAppIntent().getStringExtra(IntentConst.DIRECT_PAGE), obtainUrl())) {
                obtainWindowView().postDelayed(new Runnable() {
                    public void run() {
                        AdaWebview adaWebview = AdaWebview.this;
                        if (!adaWebview.mLoaded && adaWebview.mFrameView.obtainApp() != null) {
                            AdaWebview.this.mFrameView.obtainApp().checkOrLoadlaunchWebview();
                        }
                    }
                }, 6000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onRootViewGlobalLayout(View view) {
        try {
            IApp obtainApp = obtainApp();
            if (obtainApp != null) {
                obtainApp.obtainWebAppRootView().onRootViewGlobalLayout(view);
            }
        } catch (Exception unused) {
        }
    }

    public void pause() {
        if (this.mWebViewImpl != null && Build.VERSION.SDK_INT >= 21) {
            this.isPause = true;
            obtainMainView().post(new Runnable() {
                public void run() {
                    AdaWebview.this.mWebViewImpl.onPause();
                }
            });
            AdaFrameView adaFrameView = this.mFrameView;
            if (adaFrameView != null) {
                adaFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_WEBVIEW_PAUSE, this);
            }
        }
    }

    public void reload() {
        if (!PdrUtil.isEmpty(this.mWebViewImpl.getUrlStr())) {
            removeAllFrameItem();
            try {
                if (BaseInfo.ISDEBUG) {
                    this.mWebViewImpl.clearCache(false);
                }
            } catch (Exception unused) {
            }
            this.mWebViewImpl.setDidTouch(false);
            this.mWebViewImpl.reload();
            StringBuilder sb = new StringBuilder();
            sb.append("reload url=");
            DCWebView dCWebView = this.mWebViewImpl;
            sb.append(dCWebView.convertRelPath(dCWebView.getUrlStr()));
            Logger.d("webview", sb.toString());
        }
    }

    public void removeAllCookie() {
        DCWebView dCWebView = this.mWebViewImpl;
        if (dCWebView != null) {
            dCWebView.removeAllCookie();
            return;
        }
        CookieHandler cookieHandler = CookieHandler.getDefault();
        if (cookieHandler instanceof WebkitCookieManagerProxy) {
            ((WebkitCookieManagerProxy) cookieHandler).removeAllCookie();
        }
    }

    public void removeSessionCookie() {
        DCWebView dCWebView = this.mWebViewImpl;
        if (dCWebView != null) {
            dCWebView.removeSessionCookie();
            return;
        }
        CookieHandler cookieHandler = CookieHandler.getDefault();
        if (cookieHandler instanceof WebkitCookieManagerProxy) {
            ((WebkitCookieManagerProxy) cookieHandler).removeSessionCookie();
        }
    }

    public void removeStateListener(IWebviewStateListener iWebviewStateListener) {
        ArrayList<IWebviewStateListener> arrayList = this.mStateListeners;
        if (arrayList != null) {
            arrayList.remove(iWebviewStateListener);
        }
    }

    public void resetPlusLoadSaveData() {
        this.mPlusLoaded = false;
        this.mPlusLoading = false;
        this.mPreloadJsLoaded = false;
        this.mPreloadJsLoading = false;
        this.mInjectPlusWidthJs = null;
        this.mLoaded = false;
        this.mIsAdvanceCss = false;
        this.mInjectGeoLoaded = false;
        this.mForceAHeadJsFileLoaded = false;
        this.mInjectPlusLoadedUrl = null;
        initSitemapState();
    }

    public void resume() {
        if (this.mWebViewImpl != null && Build.VERSION.SDK_INT >= 21) {
            this.isPause = false;
            obtainMainView().post(new Runnable() {
                public void run() {
                    AdaWebview.this.mWebViewImpl.onResume();
                }
            });
            AdaFrameView adaFrameView = this.mFrameView;
            if (adaFrameView != null) {
                adaFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_WEBVIEW_RESUME, this);
            }
        }
    }

    public void saveWebViewData(String str) {
        DCWebView dCWebView;
        if (this.mPlusLoading && (dCWebView = this.mWebViewImpl) != null) {
            if (TextUtils.isEmpty(dCWebView.getUrlStr())) {
                this.mWebViewImpl.setUrlStr(str);
            } else if (!TextUtils.isEmpty(str) && !TextUtils.equals(str, "about:blank")) {
                this.mWebViewImpl.setUrlStr(str);
            }
            Logger.i("AdaFrameItem", "saveWebViewData url=" + str);
            this.mPlusLoaded = true;
            this.mInjectPlusLoadedUrl = this.mWebViewImpl.getUrlStr();
            this.mPreloadJsLoaded = this.mPreloadJsLoading;
            this.mWebViewImpl.onUpdatePlusData("saveWebViewData");
            this.mWebViewImpl.listenPageFinishTimeout("saveWebViewData");
            if (this.mFrameView.getCircleRefreshView() != null && this.mFrameView.getCircleRefreshView().isRefreshing()) {
                this.mFrameView.getCircleRefreshView().endRefresh();
            }
        }
    }

    public void setAssistantType(String str) {
        int i;
        Logger.e("AssistantInput", "setAssistantType type=" + str);
        int convertInt = RecordView.Utils.convertInt(str);
        CustomeizedInputConnection.sDefaultInputType = convertInt;
        CustomeizedInputConnection customeizedInputConnection = sCustomeizedInputConnection;
        boolean z = true;
        if (customeizedInputConnection == null || (i = customeizedInputConnection.mInputType) == convertInt || i == 1 || i == 2) {
            z = false;
        }
        if (mRecordView != null && z && AndroidResources.sIMEAlive) {
            RecordView recordView = mRecordView;
            recordView.update(recordView.mAnchorY, convertInt);
        }
    }

    public void setCookie(String str, String str2) {
        DCWebView dCWebView = this.mWebViewImpl;
        if (dCWebView != null) {
            dCWebView.setCookie(str, str2);
            return;
        }
        try {
            CookieHandler cookieHandler = CookieHandler.getDefault();
            if (cookieHandler != null) {
                HashMap hashMap = new HashMap();
                ArrayList arrayList = new ArrayList();
                arrayList.add(str2);
                hashMap.put(IWebview.SET_COOKIE, arrayList);
                cookieHandler.put(URI.create(str), hashMap);
            }
        } catch (Exception unused) {
        }
    }

    public void setCssFile(String str, String str2) {
        if (!PdrUtil.isEmpty(str)) {
            this.mCssString = loadFileContent(str, this.mFrameView.obtainApp().obtainRunningAppMode() == 1 ? 0 : 2);
        } else {
            this.mCssString = str2;
        }
    }

    public void setFixBottom(int i) {
        this.mFixBottomHeight = i;
    }

    public void setFlag(Object obj) {
        this.mFlag = obj;
    }

    public void setFrameId(String str) {
        this.mFrameId = str;
    }

    public void setIWebViewFocusable(boolean z) {
        ViewGroup obtainWindowView = obtainWindowView();
        AdaFrameView adaFrameView = this.mFrameView;
        if (adaFrameView != null) {
            AbsMgr obtainWindowMgr = adaFrameView.obtainWindowMgr();
            IWebview obtainWebView = this.mFrameView.obtainWebView();
            AdaFrameView adaFrameView2 = this.mFrameView;
            Object titleNView = TitleNViewUtil.getTitleNView(obtainWindowMgr, obtainWebView, adaFrameView2, TitleNViewUtil.getTitleNViewId(adaFrameView2));
            if (titleNView instanceof ITitleNView) {
                ((ITitleNView) titleNView).setTitleNViewFocusable(z);
            }
        }
        if (obtainWindowView != null) {
            boolean isFocusable = obtainWindowView.isFocusable();
            if (z && !isFocusable) {
                obtainWindowView.setFocusable(true);
                obtainWindowView.setFocusableInTouchMode(true);
            } else if (!z && isFocusable) {
                obtainWindowView.setFocusable(false);
                obtainWindowView.setFocusableInTouchMode(false);
            }
        }
    }

    public void setJsInterface(IJsInterface iJsInterface) {
        if (this.mJsInterfaces == null) {
            this.mJsInterfaces = iJsInterface;
        }
    }

    public void setListenResourceLoading(JSONObject jSONObject) {
        this.mListenResourceLoadingOptions = jSONObject;
    }

    public void setLoadURLHeads(String str, HashMap<String, String> hashMap) {
        DCWebView dCWebView = this.mWebViewImpl;
        if (dCWebView != null) {
            dCWebView.putHeads(str, hashMap);
        }
    }

    public void setOpener(IWebview iWebview) {
        this.mOpener = iWebview;
    }

    public void setOriginalUrl(String str) {
        this.originalUrl = str;
    }

    public void setOverrideResourceRequest(JSONArray jSONArray) {
        this.mOverrideResourceRequestOptions = jSONArray;
    }

    public void setOverrideUrlLoadingData(JSONObject jSONObject) {
        this.mOverrideUrlLoadingDataOptions = jSONObject;
        Logger.d("AdaFrameItem", "setOverrideUrlLoadingData=" + jSONObject);
        if (this.mFrameView.getFrameType() == 2 || this.mFrameView.getFrameType() == 5) {
            this.mFrameView.obtainApp().setConfigProperty("wap2app_running_mode", AbsoluteConst.FALSE);
            DCWebView dCWebView = this.mWebViewImpl;
            if (dCWebView != null) {
                dCWebView.closeWap2AppBlockDialog(true);
            }
        }
    }

    public void setPreloadJsFile(String str, boolean z) {
        if (z) {
            this.mForceAHeadJsFileLoaded = false;
            this.mForceAHeadJsFile = str;
            if (this.mPlusLoaded) {
                loadForceAHeadJs();
                return;
            }
            return;
        }
        this.mPreloadJsFile.clear();
        this.mPreloadJsFile.add(str);
        Logger.d("AdaWebview", "setPreloadJsFile mPreloadJsFile=" + this.mPreloadJsFile);
        if (this.mPlusLoaded) {
            Log.d("AdaWebview", "setPreloadJsFile---=" + str);
            loadFileContent((IFExecutePreloadJSContentCallBack) null);
        }
    }

    public void setProgressView(View view) {
        this.mProgressView = view;
    }

    public void setScrollIndicator(String str) {
        if (this.mWebViewImpl == null) {
            return;
        }
        if (PdrUtil.isEquals(str, "none")) {
            this.mWebViewImpl.setHorizontalScrollBarEnabled(false);
            this.mWebViewImpl.setVerticalScrollBarEnabled(false);
        } else if (PdrUtil.isEquals(str, "vertical")) {
            this.mWebViewImpl.setHorizontalScrollBarEnabled(false);
            this.mWebViewImpl.setVerticalScrollBarEnabled(true);
        } else if (PdrUtil.isEquals(str, Constants.Value.HORIZONTAL)) {
            this.mWebViewImpl.setHorizontalScrollBarEnabled(true);
            this.mWebViewImpl.setVerticalScrollBarEnabled(false);
        } else {
            this.mWebViewImpl.setHorizontalScrollBarEnabled(true);
            this.mWebViewImpl.setVerticalScrollBarEnabled(true);
        }
    }

    public void setWebViewCacheMode(String str) {
        DCWebView dCWebView = this.mWebViewImpl;
        if (dCWebView != null) {
            dCWebView.setWebViewCacheMode(str);
        }
    }

    public void setWebViewEvent(String str, Object obj) {
        if (this.mWebViewParent != null) {
            if (PdrUtil.isEquals(str, AbsoluteConst.PULL_DOWN_REFRESH)) {
                this.mWebViewParent.parsePullToReFresh((JSONObject) obj);
            } else if (PdrUtil.isEquals(str, AbsoluteConst.PULL_REFRESH_BEGIN)) {
                this.mWebViewParent.beginPullRefresh();
            } else if (PdrUtil.isEquals(str, AbsoluteConst.BOUNCE_REGISTER)) {
                this.mWebViewParent.parseBounce((JSONObject) obj);
            }
        }
    }

    public void setWebviewProperty(String str, String str2) {
        if ("setShareOptions".equals(str)) {
            if (!TextUtils.isEmpty(str2)) {
                this.shareOptions = str2;
                try {
                    JSONObject jSONObject = new JSONObject(this.shareOptions);
                    if (jSONObject.has(AbsoluteConst.JSON_KEY_ICON)) {
                        String string = jSONObject.getString(AbsoluteConst.JSON_KEY_ICON);
                        if (!TextUtils.isEmpty(string)) {
                            ImageLoaderL.getInstance().loadImageSync(string);
                        }
                    }
                } catch (Exception unused) {
                }
            } else {
                this.shareOptions = "";
            }
        } else if ("setFavoriteOptions".equals(str)) {
            if (!TextUtils.isEmpty(str2)) {
                this.favoriteOptions = str2;
            } else {
                this.favoriteOptions = "";
            }
        } else if ("needTouchEvent".equals(str)) {
            if (!TextUtils.isEmpty(str2)) {
                this.needTouchEvent = str2;
            } else {
                this.needTouchEvent = "";
            }
        } else if (AbsoluteConst.JSON_KEY_SCALABLE.equals(str)) {
            if (this.mWebViewImpl != null) {
                this.mWebViewImpl.initScalable(PdrUtil.parseBoolean(str2, this.mFrameView.obtainFrameOptions().scalable, false));
            }
        } else if (IWebview.USER_AGENT.equals(str)) {
            if (this.mWebViewImpl != null) {
                if (Boolean.parseBoolean(this.mFrameView.obtainApp().obtainConfigProperty(IApp.ConfigProperty.CONFIG_H5PLUS)) && str2.indexOf(" Html5Plus/") < 0) {
                    str2 = str2 + DCWebView.UserAgentExtInfo;
                }
                sCustomUserAgent = str2;
                this.mWebViewImpl.getWebView().post(new Runnable() {
                    public void run() {
                        AdaWebview.this.mWebViewImpl.setUserAgentString(AdaWebview.sCustomUserAgent);
                    }
                });
            }
        } else if (AbsoluteConst.JSON_KEY_BLOCK_NETWORK_IMAGE.equals(str)) {
            if (this.mWebViewImpl != null) {
                this.mWebViewImpl.setBlockNetworkImage(PdrUtil.parseBoolean(str2, false, false));
            }
        } else if ("injection".equals(str)) {
            this.mNeedInjection = PdrUtil.parseBoolean(str2, true, false);
        } else if ("bounce".equals(str)) {
            if (this.mWebViewImpl != null && DeviceInfo.sDeviceSdkVer >= 9) {
                JSONObject jSONObject2 = this.mFrameView.obtainFrameOptions().titleNView;
                if (("vertical".equalsIgnoreCase(str2) || Constants.Value.HORIZONTAL.equalsIgnoreCase(str2) || "all".equalsIgnoreCase(str2)) && (jSONObject2 == null || !"transparent".equals(jSONObject2.optString("type")))) {
                    this.mWebViewImpl.getWebView().setOverScrollMode(0);
                } else {
                    this.mWebViewImpl.getWebView().setOverScrollMode(2);
                }
            }
        } else if (AbsoluteConst.JSON_KEY_VIDEO_FULL_SCREEN.equals(str)) {
            if (!TextUtils.isEmpty(str2)) {
                this.mVideoFullscreen = str2;
            }
        } else if ("plusrequire".equals(str)) {
            if (!TextUtils.isEmpty(str2)) {
                this.mPlusrequire = str2;
            }
        } else if ("geolocation".equals(str)) {
            if (!TextUtils.isEmpty(str2)) {
                this.mInjectGEO = str2;
            }
        } else if (AbsoluteConst.JSON_KEY_SHAREABLE.equals(str) && !TextUtils.isEmpty(str2)) {
            this.mShareable = PdrUtil.parseBoolean(str2, true, false);
        }
    }

    public void setWebviewclientListener(IDCloudWebviewClientListener iDCloudWebviewClientListener) {
        this.mWebViewImpl.setDcloudwebviewclientListener(iDCloudWebviewClientListener);
    }

    public void stopLoading() {
        DCWebView dCWebView = this.mWebViewImpl;
        if (dCWebView != null) {
            dCWebView.stopLoading();
        }
    }

    public String syncUpdateWebViewData(String str) {
        if (Build.VERSION.SDK_INT <= 19) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        String webviewUUID = getWebviewUUID();
        if (PdrUtil.isEmpty(webviewUUID)) {
            webviewUUID = String.valueOf(this.mFrameView.hashCode());
        }
        stringBuffer.append("window.__HtMl_Id__= '" + webviewUUID + "';");
        if (PdrUtil.isEmpty(obtainFrameId())) {
            stringBuffer.append("window.__WebVieW_Id__= undefined;");
        } else {
            stringBuffer.append("window.__WebVieW_Id__= '" + obtainFrameId() + "';");
        }
        Logger.e("WebViewData", "syncUpdateWebViewData url=" + this.mRecordLastUrl);
        stringBuffer.append("try{window.plus.__tag__='" + this.mPlusInjectTag + "';location.__plusready__=true;/*console.log(location);window.plus.__url__='" + str + "';*/}catch(e){console.log(e)}");
        return AbsoluteConst.PROTOCOL_JAVASCRIPT + stringBuffer.toString();
    }

    public String toString() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("<UUID=");
            sb.append(this.mWebviewUUID);
            sb.append(">;");
            sb.append(obtainMainView() != null ? obtainMainView().toString() : "view = null");
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return super.toString();
        }
    }

    public boolean unReceiveTitle() {
        return this.unReceiveTitle;
    }

    /* access modifiers changed from: package-private */
    public String wrapAppendJsFile(String str, int i) {
        if (!BaseInfo.isWap2AppAppid(this.mAppid) || str.endsWith("wap2app.js") || (!BaseInfo.SyncDebug && !BaseInfo.isBase(getContext()))) {
            return loadFileContent(str, i);
        }
        if (str.startsWith(DeviceInfo.FILE_PROTOCOL)) {
            str = str.substring(7);
        }
        if (PdrUtil.isNetPath(this.mWebViewImpl.getUrlStr())) {
            str = "/h5pscript://" + str;
        } else if (!str.startsWith("h5pscript://")) {
            str = "h5pscript://" + str;
        }
        return "javascript:(function(){var container = document.getElementsByTagName('head')[0];\nvar script = document.createElement('script');\nscript.type = 'text/javascript';\nscript.src = '" + str + "';\ncontainer.appendChild(script);\nfirstNode = container.children[0];\nif(firstNode == null || firstNode==undefined)\n{    container.appendChild(script);}\nelse{\n\tcontainer.insertBefore(script,container.children[0]);\n}})();";
    }

    public void addJsInterface(String str, Object obj) {
        this.mWebViewImpl.addJavascriptInterface(obj, str);
    }

    public static IJsInterface[] combineObj2Array(IJsInterface[] iJsInterfaceArr, IJsInterface iJsInterface) {
        IJsInterface[] iJsInterfaceArr2;
        if (iJsInterfaceArr == null) {
            iJsInterfaceArr2 = new IJsInterface[1];
        } else {
            int length = iJsInterfaceArr.length;
            IJsInterface[] iJsInterfaceArr3 = new IJsInterface[(length + 1)];
            System.arraycopy(iJsInterfaceArr, 0, iJsInterfaceArr3, 0, length);
            iJsInterfaceArr2 = iJsInterfaceArr3;
        }
        iJsInterfaceArr2[iJsInterfaceArr2.length] = iJsInterface;
        return iJsInterfaceArr2;
    }

    public void addJsInterface(String str, IJsInterface iJsInterface) {
        if (Build.VERSION.SDK_INT > 17) {
            this.mWebViewImpl.addJavascriptInterface(iJsInterface, str);
        }
        setJsInterface(iJsInterface);
    }

    public void evalJS(String str, ReceiveJSValue.ReceiveJSValueCallback receiveJSValueCallback) {
        if (receiveJSValueCallback != null) {
            str = ReceiveJSValue.registerCallback(str, receiveJSValueCallback);
        }
        evalJS(str);
    }

    public void reload(String str) {
        Logger.d("webview", "reload loadUrl url=" + str);
        this.mLoaded = false;
        this.mWebViewImpl.setUrlStr(str);
        this.mWebViewImpl.loadUrl(str);
    }

    public void reload(boolean z) {
        DCWebView dCWebView = this.mWebViewImpl;
        if (dCWebView != null) {
            dCWebView.webReload(z);
        }
        reload();
    }

    private String loadFileContent(String str, int i) {
        byte[] fileContent;
        StringBuffer stringBuffer = new StringBuffer();
        InputStream encryptionInputStream = WebResUtil.getEncryptionInputStream(str, this.mFrameView.obtainApp());
        if (encryptionInputStream != null) {
            try {
                stringBuffer.append(IOUtil.toString(encryptionInputStream));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtil.close(encryptionInputStream);
            }
        } else {
            str = this.mFrameView.obtainApp().convert2AbsFullPath(obtainFullUrl(), str);
            try {
                if (DHFile.isExist(str) && (fileContent = PlatformUtil.getFileContent(str, i)) != null) {
                    stringBuffer.append(new String(fileContent));
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        if (this.mNeedSitemapJson && str.endsWith("__wap2app.js")) {
            File file = new File(BaseInfo.sBaseFsSitMapPath + File.separator + obtainApp().obtainAppId() + "_sitemap.json");
            if (file.exists()) {
                try {
                    stringBuffer.insert(0, IOUtil.toString(new FileInputStream(file)) + ";\n");
                    stringBuffer.insert(0, ";window.__wap2app_sitemap=");
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
                this.mNeedSitemapJson = false;
            }
        }
        return stringBuffer.toString();
    }

    protected AdaWebview(Context context, AdaFrameView adaFrameView) {
        super(context);
        initANID();
        this.mFrameView = adaFrameView;
        this.mAppid = adaFrameView.obtainApp().obtainAppId();
        initSitemapState();
        Logger.d("AdaWebview");
        try {
            this.mWebViewImpl = WebViewFactory.getWebView(getActivity(), this);
        } catch (Exception e) {
            e.printStackTrace();
            this.mWebViewImpl = WebViewFactory.getWebView(getActivity(), this);
        }
        setMainView(this.mWebViewImpl.getWebView());
        AdaWebViewParent adaWebViewParent = new AdaWebViewParent(context);
        this.mWebViewParent = adaWebViewParent;
        adaWebViewParent.fillsWithWebview(this);
        if (adaFrameView.getFrameType() == 2) {
            this.mOverrideUrlLoadingDataOptions = this.mFrameView.obtainApp().obtainThridInfo(IApp.ConfigProperty.ThridInfo.OverrideUrlJsonData);
            JSONObject obtainThridInfo = this.mFrameView.obtainApp().obtainThridInfo(IApp.ConfigProperty.ThridInfo.OverrideResourceJsonData);
            if (obtainThridInfo != null) {
                this.mOverrideResourceRequestOptions = obtainThridInfo.optJSONArray(WXInstanceApm.VALUE_ERROR_CODE_DEFAULT);
            }
            this.mNeedInjection = PdrUtil.parseBoolean(this.mFrameView.obtainApp().obtainConfigProperty("injection"), this.mNeedInjection, false);
            String obtainConfigProperty = this.mFrameView.obtainApp().obtainConfigProperty(IApp.ConfigProperty.CONFIG_LPLUSERQUIRE);
            if (!TextUtils.isEmpty(obtainConfigProperty)) {
                this.mPlusrequire = obtainConfigProperty;
            }
            String obtainConfigProperty2 = this.mFrameView.obtainApp().obtainConfigProperty(IApp.ConfigProperty.CONFIG_LGEOLOCATION);
            if (!TextUtils.isEmpty(obtainConfigProperty2)) {
                this.mInjectGEO = obtainConfigProperty2;
            }
        } else if (adaFrameView.getFrameType() == 4) {
            String obtainConfigProperty3 = this.mFrameView.obtainApp().obtainConfigProperty(IApp.ConfigProperty.CONFIG_SPLUSERQUIRE);
            if (!TextUtils.isEmpty(obtainConfigProperty3)) {
                this.mPlusrequire = obtainConfigProperty3;
            }
            String obtainConfigProperty4 = this.mFrameView.obtainApp().obtainConfigProperty(IApp.ConfigProperty.CONFIG_SGEOLOCATION);
            if (!TextUtils.isEmpty(obtainConfigProperty4)) {
                this.mInjectGEO = obtainConfigProperty4;
            }
        }
    }

    protected AdaWebview(Context context, AdaFrameView adaFrameView, IDCloudWebviewClientListener iDCloudWebviewClientListener) {
        super(context);
        initANID();
        this.mFrameView = adaFrameView;
        this.mAppid = adaFrameView.obtainApp().obtainAppId();
        initSitemapState();
        Logger.d("AdaWebview");
        try {
            this.mWebViewImpl = WebViewFactory.getWebView(getActivity(), this, iDCloudWebviewClientListener);
        } catch (Exception e) {
            e.printStackTrace();
            this.mWebViewImpl = WebViewFactory.getWebView(getActivity(), this, iDCloudWebviewClientListener);
        }
        setMainView(this.mWebViewImpl.getWebView());
        AdaWebViewParent adaWebViewParent = new AdaWebViewParent(context);
        this.mWebViewParent = adaWebViewParent;
        adaWebViewParent.fillsWithWebview(this);
        if (adaFrameView.getFrameType() == 2) {
            this.mOverrideUrlLoadingDataOptions = this.mFrameView.obtainApp().obtainThridInfo(IApp.ConfigProperty.ThridInfo.OverrideUrlJsonData);
            JSONObject obtainThridInfo = this.mFrameView.obtainApp().obtainThridInfo(IApp.ConfigProperty.ThridInfo.OverrideResourceJsonData);
            if (obtainThridInfo != null) {
                this.mOverrideResourceRequestOptions = obtainThridInfo.optJSONArray(WXInstanceApm.VALUE_ERROR_CODE_DEFAULT);
            }
            this.mNeedInjection = PdrUtil.parseBoolean(this.mFrameView.obtainApp().obtainConfigProperty("injection"), this.mNeedInjection, false);
            String obtainConfigProperty = this.mFrameView.obtainApp().obtainConfigProperty(IApp.ConfigProperty.CONFIG_LPLUSERQUIRE);
            if (!TextUtils.isEmpty(obtainConfigProperty)) {
                this.mPlusrequire = obtainConfigProperty;
            }
            String obtainConfigProperty2 = this.mFrameView.obtainApp().obtainConfigProperty(IApp.ConfigProperty.CONFIG_LGEOLOCATION);
            if (!TextUtils.isEmpty(obtainConfigProperty2)) {
                this.mInjectGEO = obtainConfigProperty2;
            }
        } else if (adaFrameView.getFrameType() == 4) {
            String obtainConfigProperty3 = this.mFrameView.obtainApp().obtainConfigProperty(IApp.ConfigProperty.CONFIG_SPLUSERQUIRE);
            if (!TextUtils.isEmpty(obtainConfigProperty3)) {
                this.mPlusrequire = obtainConfigProperty3;
            }
            String obtainConfigProperty4 = this.mFrameView.obtainApp().obtainConfigProperty(IApp.ConfigProperty.CONFIG_SGEOLOCATION);
            if (!TextUtils.isEmpty(obtainConfigProperty4)) {
                this.mInjectGEO = obtainConfigProperty4;
            }
        }
    }

    protected AdaWebview(Context context, AdaFrameView adaFrameView, OnPageFinishedCallack onPageFinishedCallack) {
        super(context);
        initANID();
        this.mFrameView = adaFrameView;
        Logger.d("AdaWebview");
        try {
            this.mWebViewImpl = WebViewFactory.getWebView(getActivity(), this, onPageFinishedCallack);
        } catch (Exception e) {
            e.printStackTrace();
            this.mWebViewImpl = WebViewFactory.getWebView(getActivity(), this, onPageFinishedCallack);
        }
        setMainView(this.mWebViewImpl.getWebView());
        AdaWebViewParent adaWebViewParent = new AdaWebViewParent(context);
        this.mWebViewParent = adaWebViewParent;
        adaWebViewParent.fillsWithWebview(this);
    }
}
