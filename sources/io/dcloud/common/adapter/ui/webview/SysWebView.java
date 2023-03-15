package io.dcloud.common.adapter.ui.webview;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.dcloud.android.v4.widget.IRefreshAble;
import com.dcloud.android.widget.SlideLayout;
import com.dcloud.zxing2.common.StringUtils;
import com.taobao.weex.el.parse.Operators;
import dc.squareup.HttpConstants;
import io.dcloud.a;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.IDCloudWebviewClientListener;
import io.dcloud.common.DHInterface.IKeyHandler;
import io.dcloud.common.DHInterface.ILoadCallBack;
import io.dcloud.common.DHInterface.INativeView;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.ITitleNView;
import io.dcloud.common.DHInterface.IVideoPlayer;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.ui.AdaContainerFrameItem;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.adapter.ui.AdaWebview;
import io.dcloud.common.adapter.ui.CustomeizedInputConnection;
import io.dcloud.common.adapter.ui.ReceiveJSValue;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.DownloadUtil;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MobilePhoneModel;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.ui.blur.AppEventForBlurManager;
import io.dcloud.common.util.AppRuntime;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.DialogUtil;
import io.dcloud.common.util.LoadAppUtils;
import io.dcloud.common.util.NotificationUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.StringUtil;
import io.dcloud.common.util.TitleNViewUtil;
import io.dcloud.feature.internal.sdk.SDK;
import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Pattern;
import org.json.JSONObject;

public class SysWebView extends WebView implements DCWebView, DownloadListener, IRefreshAble.OnRefreshListener {
    static final String PLUSSCROLLBOTTOM_JS_TEMPLATE = "(function(){var e = document.createEvent('HTMLEvents');var evt = 'plusscrollbottom';e.initEvent(evt, false, true);document.dispatchEvent(e);})();";
    static final String TAG = "webview";
    CookieManager cm;
    private boolean didTouch;
    boolean isToInvalidate;
    AdaWebview mAdaWebview;
    String mBaseUrl;
    private int mCacheMode;
    private int mContentHeight;
    Context mContext;
    private IDCloudWebviewClientListener mDcloudwebviewclientListener;
    int mDeafaltOverScrollMode;
    private int mEventX;
    private int mEventY;
    private boolean mIsBeingDragged;
    float mLastMotionX;
    float mLastMotionY;
    private long mLastScrollTimestamp;
    private int mLastScrollY;
    private OnPageFinishedCallack mPageFinishedCallack;
    private String mPageTitle;
    float mScale;
    private int mThreshold;
    private int mThresholdTime;
    private int mTouchSlop;
    String mUrl;
    HashMap<String, HashMap<String, String>> mUrlHeads;
    String mUserAgent;
    WebJsEvent mWebJsEvent;
    WebLoadEvent mWebLoadEvent;
    WebSettings webSettings;

    static class BorderDrawable extends Drawable {
        int mBackgroundColor;
        Paint mPaint;

        BorderDrawable(int i, int i2) {
            Paint paint = new Paint();
            this.mPaint = paint;
            this.mBackgroundColor = i;
            paint.setColor(i2);
        }

        public void draw(Canvas canvas) {
            canvas.drawColor(this.mBackgroundColor);
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaint.setStrokeWidth(3.0f);
            canvas.drawRect(getBounds(), this.mPaint);
        }

        public int getOpacity() {
            return this.mPaint.getAlpha();
        }

        public void setAlpha(int i) {
            this.mPaint.setAlpha(i);
        }

        public void setColorFilter(ColorFilter colorFilter) {
            this.mPaint.setColorFilter(colorFilter);
        }
    }

    public class CustomizedSelectActionModeCallback implements ActionMode.Callback {
        ActionMode.Callback callback;

        public CustomizedSelectActionModeCallback(ActionMode.Callback callback2) {
            this.callback = callback2;
        }

        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return this.callback.onActionItemClicked(actionMode, menuItem);
        }

        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            this.callback.onCreateActionMode(actionMode, menu);
            int size = menu.size();
            for (int i = 0; i < size; i++) {
                MenuItem item = menu.getItem(i);
                String charSequence = item.getTitle().toString();
                if (charSequence.contains("搜索") || charSequence.toLowerCase(Locale.ENGLISH).contains("search")) {
                    menu.removeItem(item.getItemId());
                    return true;
                }
            }
            return true;
        }

        public void onDestroyActionMode(ActionMode actionMode) {
            this.callback.onDestroyActionMode(actionMode);
        }

        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return this.callback.onPrepareActionMode(actionMode, menu);
        }
    }

    public SysWebView(Context context, AdaWebview adaWebview) {
        super(context);
        this.mUserAgent = null;
        this.mAdaWebview = null;
        this.mWebLoadEvent = null;
        this.mWebJsEvent = null;
        this.mUrl = null;
        this.mScale = 0.0f;
        this.mContext = null;
        this.mBaseUrl = null;
        this.webSettings = getSettings();
        this.cm = null;
        this.mLastScrollY = 0;
        this.mContentHeight = 0;
        this.mThreshold = 2;
        this.mThresholdTime = 15;
        this.mLastScrollTimestamp = 0;
        this.mPageTitle = null;
        this.mDeafaltOverScrollMode = 0;
        this.mCacheMode = -1;
        this.didTouch = false;
        this.isToInvalidate = false;
        this.mUrlHeads = new HashMap<>();
        this.mEventY = 0;
        this.mEventX = 0;
        this.mTouchSlop = -1;
        this.mIsBeingDragged = true;
        this.mScale = getContext().getResources().getDisplayMetrics().density;
        Logger.d("WebViewImpl");
        this.mContext = context.getApplicationContext();
        BaseInfo.s_Webview_Count++;
        this.mAdaWebview = adaWebview;
    }

    private Bitmap captureWebView(WebView webView, Rect rect) {
        Bitmap createBitmap = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(createBitmap);
        canvas.translate((float) (-rect.left), (float) (-rect.top));
        AdaWebview adaWebview = this.mAdaWebview;
        if (adaWebview == null || adaWebview.obtainFrameView() == null || !(this.mAdaWebview.obtainFrameView() instanceof AdaContainerFrameItem) || ((AdaContainerFrameItem) this.mAdaWebview.obtainFrameView()).getChilds().size() <= 1) {
            if (Build.VERSION.SDK_INT < 19) {
                ((View) webView.getParent().getParent()).draw(canvas);
            } else {
                webView.draw(canvas);
            }
            return createBitmap;
        }
        if (this.mAdaWebview.obtainFrameView().obtainMainView() != null) {
            this.mAdaWebview.obtainFrameView().obtainMainView().draw(canvas);
        }
        return createBitmap;
    }

    private static String getStreamAppFlag() {
        String str;
        Object[] objArr = new Object[1];
        if (TextUtils.isEmpty(BaseInfo.sChannel)) {
            str = "";
        } else {
            str = " (" + BaseInfo.sChannel + ") ";
        }
        objArr[0] = str;
        return StringUtil.format(DCWebView.UserAgentStreamApp, objArr);
    }

    private void removeUnSafeJavascriptInterface() {
        removeJavascriptInterface("searchBoxJavaBridge_");
        removeJavascriptInterface("accessibilityTraversal");
        removeJavascriptInterface("accessibility");
    }

    public void applyWebViewDarkMode() {
        AppRuntime.applyWebViewDarkMode(this.mContext, this);
    }

    public boolean checkApkUrl(String str, String str2) {
        if ((TextUtils.isEmpty(str2) || !str2.toLowerCase(Locale.ENGLISH).contains(".apk")) && !str.toLowerCase(Locale.ENGLISH).contains(".apk")) {
            return false;
        }
        return true;
    }

    public boolean checkOverrideUrl(JSONObject jSONObject, String str) {
        if (jSONObject != null) {
            try {
                if (AbsoluteConst.EVENTS_WEBVIEW_ONTOUCH_START.equals(jSONObject.optString("effect", "instant")) && !isDidTouch()) {
                    return false;
                }
                int type = getHitTestResult().getType();
                if ("redirect".equalsIgnoreCase(jSONObject.optString("exclude")) && type == 0) {
                    return false;
                }
                String optString = jSONObject.optString("mode");
                boolean matches = jSONObject.has("match") ? Pattern.compile(jSONObject.optString("match")).matcher(str).matches() : true;
                if ("allow".equals(optString)) {
                    if (matches) {
                        return false;
                    }
                    return true;
                } else if (matches) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean checkWhite(String str) {
        Rect rect;
        if (getWidth() <= 0) {
            return true;
        }
        if (str.equals("center")) {
            int height = getHeight() / 2;
            rect = new Rect(0, height, getWidth(), height + 1);
        } else if (str.equals("top")) {
            int deivceSuitablePixel = DeviceInfo.getDeivceSuitablePixel(this.mAdaWebview.getActivity(), 20);
            rect = new Rect(0, deivceSuitablePixel, getWidth(), deivceSuitablePixel + 1);
        } else if (str.equals("bottom")) {
            int deivceSuitablePixel2 = DeviceInfo.getDeivceSuitablePixel(this.mAdaWebview.getActivity(), 25);
            rect = new Rect(0, (getHeight() - deivceSuitablePixel2) + 1, getWidth(), getHeight() - deivceSuitablePixel2);
        } else {
            int width = getWidth() / 2;
            rect = new Rect(width, 0, width + 5, getHeight());
        }
        Bitmap captureWebView = captureWebView(this, rect);
        if (captureWebView == null) {
            return false;
        }
        boolean isWhiteBitmap = str.equals("auto") ? PlatformUtil.isWhiteBitmap(captureWebView, !this.mAdaWebview.isLoaded(), true) : PlatformUtil.isLineWhiteBitmap(captureWebView, !this.mAdaWebview.isLoaded());
        captureWebView.recycle();
        return isWhiteBitmap;
    }

    public void closeWap2AppBlockDialog(boolean z) {
        this.mWebLoadEvent.closeWap2AppBlockDialog(z);
    }

    public String convertRelPath(String str) {
        if (str.indexOf(this.mBaseUrl) >= 0) {
            return str.substring(this.mBaseUrl.length());
        }
        String substring = this.mBaseUrl.substring(7);
        return str.indexOf(substring) >= 0 ? str.substring(substring.length()) : str;
    }

    public void destroyWeb() {
        setWebChromeClient((WebChromeClient) null);
        setWebViewClient((WebViewClient) null);
        setDownloadListener((DownloadListener) null);
        destroy();
        destroyDrawingCache();
        clearDisappearingChildren();
        if (this.mAdaWebview != null) {
            this.cm = null;
            setOnLongClickListener((View.OnLongClickListener) null);
            this.mAdaWebview = null;
            this.mWebLoadEvent.destroy();
            this.mWebLoadEvent = null;
            this.mWebJsEvent.destroy();
            this.mWebJsEvent = null;
            this.webSettings = null;
            this.mContext = null;
        }
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        for (int i = 0; i < getChildCount(); i++) {
            boolean dispatchKeyEvent = getChildAt(i).dispatchKeyEvent(keyEvent);
            if (dispatchKeyEvent) {
                return dispatchKeyEvent;
            }
        }
        return super.dispatchKeyEvent(keyEvent);
    }

    public boolean doKeyDownAction(int i, KeyEvent keyEvent) {
        boolean z;
        IKeyHandler iKeyHandler = (IKeyHandler) this.mAdaWebview.getActivity();
        if (keyEvent.getRepeatCount() == 0) {
            z = iKeyHandler.onKeyEventExecute(ISysEventListener.SysEventType.onKeyDown, i, keyEvent);
        } else {
            z = iKeyHandler.onKeyEventExecute(ISysEventListener.SysEventType.onKeyDown, i, keyEvent);
        }
        return z ? z : super.onKeyDown(i, keyEvent);
    }

    public boolean doKeyUpAction(int i, KeyEvent keyEvent) {
        boolean onKeyEventExecute = ((IKeyHandler) this.mAdaWebview.getActivity()).onKeyEventExecute(ISysEventListener.SysEventType.onKeyUp, i, keyEvent);
        return onKeyEventExecute ? onKeyEventExecute : super.onKeyUp(i, keyEvent);
    }

    /* access modifiers changed from: package-private */
    public long downloadFile(Context context, String str, String str2, String str3, String str4, ILoadCallBack iLoadCallBack) {
        final Context context2 = context;
        final String str5 = str2;
        final String str6 = str;
        final ILoadCallBack iLoadCallBack2 = iLoadCallBack;
        return DownloadUtil.getInstance(context).startRequest(context, str3, str4, DeviceInfo.sDeviceRootDir + "/Download/", str, new ILoadCallBack() {
            public Object onCallBack(int i, Context context, Object obj) {
                PendingIntent activity;
                Context context2 = context;
                SDK.IntegratedMode integratedMode = BaseInfo.sRuntimeMode;
                if (obj == null && i == -1 && context2 == null) {
                    Intent intent = new Intent();
                    Context context3 = context2;
                    String str = str5;
                    NotificationUtil.showNotification(context3, str, context2.getString(R.string.dcloud_common_download_failed) + Operators.SPACE_STR + str6, intent, -1, -1, intent.hashCode(), true);
                    return null;
                }
                if (iLoadCallBack2 != null) {
                    String valueOf = String.valueOf(obj);
                    String mimeType = PdrUtil.getMimeType(valueOf);
                    if (valueOf.startsWith(DeviceInfo.FILE_PROTOCOL)) {
                        valueOf = valueOf.substring(7);
                    }
                    if (valueOf.startsWith("content://")) {
                        valueOf = PlatformUtil.getFilePathFromContentUri(Uri.parse(valueOf), context2.getContentResolver());
                        mimeType = PdrUtil.getMimeType(valueOf);
                    }
                    if (AbsoluteConst.TRUE.equals(String.valueOf(iLoadCallBack2.onCallBack(0, context2, LoadAppUtils.getDataAndTypeIntent(context2, valueOf, mimeType))))) {
                        return null;
                    }
                }
                if (integratedMode == null) {
                    String valueOf2 = String.valueOf(obj);
                    String mimeType2 = PdrUtil.getMimeType(valueOf2);
                    if (valueOf2.startsWith(DeviceInfo.FILE_PROTOCOL)) {
                        valueOf2 = valueOf2.substring(7);
                    }
                    if (valueOf2.startsWith("content://")) {
                        valueOf2 = PlatformUtil.getFilePathFromContentUri(Uri.parse(valueOf2), context2.getContentResolver());
                        mimeType2 = PdrUtil.getMimeType(valueOf2);
                    }
                    File file = new File(valueOf2);
                    Intent dataAndTypeIntent = LoadAppUtils.getDataAndTypeIntent(context2, valueOf2, mimeType2);
                    if (file.exists() && file.getName().toLowerCase(Locale.ENGLISH).endsWith(".apk")) {
                        try {
                            PlatformUtil.APKInfo apkFileInfo = PlatformUtil.getApkFileInfo(context2, valueOf2);
                            Drawable drawable = apkFileInfo.mIcon;
                            String str2 = apkFileInfo.mAppName;
                            if (drawable instanceof BitmapDrawable) {
                                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                                String name = file.getName();
                                if (Build.VERSION.SDK_INT >= 23) {
                                    activity = PendingIntent.getActivity(context2, dataAndTypeIntent.hashCode(), dataAndTypeIntent, 1140850688);
                                } else {
                                    activity = PendingIntent.getActivity(context2, dataAndTypeIntent.hashCode(), dataAndTypeIntent, 1073741824);
                                }
                                PendingIntent pendingIntent = activity;
                                Context context4 = context2;
                                NotificationUtil.createCustomNotification(context4, str2 + Operators.SPACE_STR + context2.getString(R.string.dcloud_common_download_complete), bitmap, str2, name, dataAndTypeIntent.hashCode(), pendingIntent);
                                return null;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Context context5 = context2;
                    String str3 = str5;
                    NotificationUtil.showNotification(context5, str3, str6 + Operators.SPACE_STR + context2.getString(R.string.dcloud_common_download_complete), dataAndTypeIntent, -1, -1, dataAndTypeIntent.hashCode(), true);
                }
                return null;
            }
        });
    }

    public void evalJSSync(String str, final ICallBack iCallBack) {
        if (str.startsWith(AbsoluteConst.PROTOCOL_JAVASCRIPT) && Build.VERSION.SDK_INT >= 19) {
            try {
                evaluateJavascript(str, new ValueCallback<String>() {
                    public void onReceiveValue(String str) {
                        ICallBack iCallBack = iCallBack;
                        if (iCallBack != null) {
                            iCallBack.onCallBack(1, str);
                        }
                    }
                });
            } catch (Throwable th) {
                Logger.e(TAG, "e.getMessage()==" + th.getMessage());
                super.loadUrl(str);
                if (iCallBack != null) {
                    iCallBack.onCallBack(1, (Object) null);
                }
            }
        }
    }

    public String getBaseUrl() {
        return this.mBaseUrl;
    }

    public int getCacheMode() {
        return this.mCacheMode;
    }

    public String getCookie(String str) {
        CookieManager cookieManager = this.cm;
        if (cookieManager != null) {
            return cookieManager.getCookie(str);
        }
        return null;
    }

    public String getPageTitle() {
        return this.mPageTitle;
    }

    public IRefreshAble.OnRefreshListener getRefreshListener() {
        return this;
    }

    public float getScale() {
        return this.mScale;
    }

    public String getUrlStr() {
        return this.mUrl;
    }

    public String getUserAgentString() {
        return this.webSettings.getUserAgentString();
    }

    public ViewGroup getWebView() {
        return this;
    }

    public int getWebViewScrollY() {
        return getScrollY();
    }

    public void init() {
        setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View view) {
                try {
                    AdaWebview adaWebview = SysWebView.this.mAdaWebview;
                    if (adaWebview == null) {
                        return true;
                    }
                    if (adaWebview.mFrameView.getCircleRefreshView() == null || !SysWebView.this.mAdaWebview.mFrameView.getCircleRefreshView().hasRefreshOperator()) {
                        return true ^ SysWebView.this.mAdaWebview.mFrameView.obtainFrameOptions().isUserSelect;
                    }
                    return true;
                } catch (Exception unused) {
                    return true;
                }
            }
        });
        if (!AdaWebview.setedWebViewData) {
            boolean hasFile = DHFile.hasFile();
            boolean parseBoolean = Boolean.parseBoolean(this.mAdaWebview.obtainApp().obtainConfigProperty(IApp.ConfigProperty.CONFIG_USE_ENCRYPTION));
            boolean isUniAppAppid = BaseInfo.isUniAppAppid(this.mAdaWebview.obtainApp());
            String obtainConfigProperty = this.mAdaWebview.obtainApp().obtainConfigProperty(IApp.ConfigProperty.CONFIG_UNIAPP_CONTROL);
            boolean z = !isUniAppAppid;
            if (!TextUtils.isEmpty(obtainConfigProperty) && isUniAppAppid && obtainConfigProperty.equals(AbsoluteConst.UNI_V3)) {
                z = true;
            }
            if (!hasFile && (!BaseInfo.ISDEBUG || parseBoolean || !z)) {
                setWebViewData();
            } else if (Build.VERSION.SDK_INT >= 19) {
                PlatformUtil.invokeMethod("android.webkit.WebView", "setWebContentsDebuggingEnabled", (Object) null, new Class[]{Boolean.TYPE}, new Object[]{Boolean.TRUE});
            }
            AdaWebview.setedWebViewData = true;
        }
        setDownloadListener(this);
        if (DeviceInfo.sDeviceSdkVer >= 9) {
            this.mDeafaltOverScrollMode = getOverScrollMode();
        }
        try {
            CookieSyncManager.createInstance(this.mContext);
            CookieManager instance = CookieManager.getInstance();
            this.cm = instance;
            if (instance != null) {
                PlatformUtil.invokeMethod(CookieManager.class.getName(), "setAcceptThirdPartyCookies", this.cm, new Class[]{WebView.class, Boolean.TYPE}, new Object[]{this, Boolean.TRUE});
                this.cm.setAcceptCookie(true);
                this.cm.removeExpiredCookie();
                CookieSyncManager.getInstance().sync();
            }
        } catch (Throwable th) {
            Logger.e("WebViewImpl CookieManager.getInstance Exception =" + th);
        }
        this.mAdaWebview.obtainFrameView().onInit();
        IApp obtainApp = this.mAdaWebview.obtainFrameView().obtainApp();
        this.mBaseUrl = obtainApp.obtainWebviewBaseUrl();
        setScrollBarStyle(33554432);
        String str = AdaWebview.sCustomUserAgent;
        if (str != null) {
            this.webSettings.setUserAgentString(str);
        } else {
            initUserAgent(obtainApp);
        }
        this.webSettings.setAllowFileAccess(false);
        WebViewFactory.setFileAccess(this.webSettings, obtainApp, true);
        this.webSettings.setDefaultTextEncodingName(StringUtils.GB2312);
        this.webSettings.setDisplayZoomControls(false);
        this.webSettings.setCacheMode(getCacheMode());
        this.webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        this.webSettings.setSavePassword(false);
        this.webSettings.setSaveFormData(false);
        WebViewFactory.openJSEnabled(this.webSettings, obtainApp);
        boolean z2 = this.mAdaWebview.mFrameView.obtainFrameOptions().scalable;
        this.webSettings.supportZoom();
        this.webSettings.setBuiltInZoomControls(z2);
        this.webSettings.setSupportZoom(z2);
        this.webSettings.setUseWideViewPort(true);
        if (!Build.BRAND.equalsIgnoreCase(MobilePhoneModel.MEIZU)) {
            this.webSettings.setLoadWithOverviewMode(true);
        }
        this.webSettings.setDatabasePath(this.mAdaWebview.obtainFrameView().obtainApp().obtainAppWebCachePath());
        this.webSettings.setAppCacheEnabled(true);
        this.webSettings.setAppCachePath(this.mAdaWebview.obtainFrameView().obtainApp().obtainAppWebCachePath());
        this.webSettings.setDatabaseEnabled(true);
        if (DeviceInfo.sDeviceSdkVer >= 7) {
            long j = this.mContext.getSharedPreferences(this.mAdaWebview.obtainFrameView().obtainApp().obtainAppId(), 0).getLong("maxSize", 0);
            this.webSettings.setDomStorageEnabled(true);
            if (j != 0) {
                this.webSettings.setAppCacheMaxSize(j);
            }
        }
        int i = Build.VERSION.SDK_INT;
        if (i >= 17) {
            this.webSettings.setMediaPlaybackRequiresUserGesture(false);
        }
        this.webSettings.setGeolocationEnabled(true);
        this.webSettings.setGeolocationDatabasePath(this.mAdaWebview.obtainFrameView().obtainApp().obtainAppWebCachePath());
        if (i >= 21) {
            PlatformUtil.invokeMethod("android.webkit.WebSettings", "setMixedContentMode", this.webSettings, new Class[]{Integer.TYPE}, new Object[]{0});
        }
        WebJsEvent webJsEvent = new WebJsEvent(this.mAdaWebview);
        this.mWebJsEvent = webJsEvent;
        setWebChromeClient(webJsEvent);
        WebLoadEvent webLoadEvent = new WebLoadEvent(this.mAdaWebview);
        this.mWebLoadEvent = webLoadEvent;
        webLoadEvent.setPageFinishedCallack(this.mPageFinishedCallack);
        if (!PdrUtil.isEmpty(this.mDcloudwebviewclientListener)) {
            webLoadEvent.setDcloudwebviewclientListener(this.mDcloudwebviewclientListener);
        }
        setWebViewClient(webLoadEvent);
        ReceiveJSValue.addJavascriptInterface(this.mAdaWebview);
        requestFocus();
        setClickable(true);
        removeUnSafeJavascriptInterface();
        applyWebViewDarkMode();
    }

    public void initScalable(boolean z) {
        this.webSettings.supportZoom();
        this.webSettings.setBuiltInZoomControls(z);
        this.webSettings.setSupportZoom(z);
        this.webSettings.setUseWideViewPort(true);
        if (!Build.BRAND.equalsIgnoreCase(MobilePhoneModel.MEIZU)) {
            this.webSettings.setLoadWithOverviewMode(true);
        }
    }

    /* access modifiers changed from: package-private */
    public void initUserAgent(IApp iApp) {
        String str;
        String obtainConfigProperty = iApp.obtainConfigProperty(IApp.ConfigProperty.CONFIG_USER_AGENT);
        boolean parseBoolean = Boolean.parseBoolean(iApp.obtainConfigProperty(IApp.ConfigProperty.CONFIG_CONCATENATE));
        if (Boolean.parseBoolean(iApp.obtainConfigProperty(IApp.ConfigProperty.CONFIG_funSetUA))) {
            parseBoolean = false;
        }
        boolean parseBoolean2 = Boolean.parseBoolean(iApp.obtainConfigProperty(IApp.ConfigProperty.CONFIG_H5PLUS));
        if (PdrUtil.isEmpty(AdaWebview.sDefalutUserAgent)) {
            AdaWebview.sDefalutUserAgent = this.webSettings.getUserAgentString();
            HashMap hashMap = new HashMap(1);
            hashMap.put("ua", AdaWebview.sDefalutUserAgent);
            a.a(getContext(), iApp.obtainAppId(), "save", hashMap);
        }
        this.mUserAgent = AdaWebview.sDefalutUserAgent;
        if (!parseBoolean && !PdrUtil.isEmpty(obtainConfigProperty)) {
            this.mUserAgent = obtainConfigProperty;
        } else if (!PdrUtil.isEmpty(obtainConfigProperty)) {
            this.mUserAgent += Operators.SPACE_STR + obtainConfigProperty.trim();
        }
        boolean booleanValue = Boolean.valueOf(iApp.obtainConfigProperty(AbsoluteConst.JSONKEY_STATUSBAR_IMMERSED)).booleanValue();
        if (iApp.obtainStatusBarMgr() == null || !iApp.obtainStatusBarMgr().checkImmersedStatusBar(this.mAdaWebview.getActivity(), booleanValue)) {
            str = "";
        } else {
            str = " (Immersed/" + (((float) DeviceInfo.sStatusBarHeight) / this.mScale) + Operators.BRACKET_END_STR;
        }
        if (parseBoolean2 && this.mUserAgent.indexOf(DCWebView.UserAgentExtInfo) < 0) {
            if (!BaseInfo.ISAMU || !BaseInfo.isBase(getContext())) {
                this.mUserAgent += DCWebView.UserAgentExtInfo + str;
            } else {
                this.mUserAgent += " Html5Plus/1.0 StreamApp/1.0" + str;
            }
        }
        Logger.d(TAG, "userAgent=" + this.mUserAgent);
        if (this.mAdaWebview.obtainFrameView().getFrameType() != 6) {
            this.webSettings.setUserAgentString(this.mUserAgent);
        }
        HttpConstants.setUA(this.mUserAgent);
    }

    public void invalidate() {
        super.invalidate();
        try {
            if (getParent() != null) {
                float contentHeight = ((float) getContentHeight()) * this.mScale;
                if (contentHeight <= 0.0f) {
                    return;
                }
                if ((contentHeight > ((float) getHeight()) || (this.mAdaWebview.mProgress > 60 && contentHeight >= ((float) getHeight()))) && !this.isToInvalidate) {
                    this.mAdaWebview.dispatchWebviewStateEvent(6, Integer.valueOf(getContentHeight()));
                    this.mAdaWebview.mFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_RENDERING, Integer.valueOf(getContentHeight()));
                    this.isToInvalidate = true;
                }
            }
        } catch (Exception unused) {
        }
    }

    public boolean isChildSpeciaView(float f, float f2) {
        if (this.mAdaWebview.mFrameView.checkITypeofAble()) {
            return false;
        }
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof SlideLayout) {
                return false;
            }
            if (childAt instanceof INativeView) {
                return true;
            }
            if (childAt instanceof IVideoPlayer) {
                IVideoPlayer iVideoPlayer = (IVideoPlayer) childAt;
                if (!iVideoPlayer.isVideoHandleTouch() || iVideoPlayer.isPointInRect(f, f2)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isDidTouch() {
        return this.didTouch;
    }

    /* access modifiers changed from: protected */
    public boolean isReadyForPullUp(int i) {
        int floor = ((int) ((float) Math.floor((double) (((float) getContentHeight()) * this.mScale)))) - getHeight();
        long currentTimeMillis = System.currentTimeMillis();
        boolean z = (i >= floor || (i >= floor - this.mThreshold && currentTimeMillis - this.mLastScrollTimestamp > ((long) this.mThresholdTime))) && this.mLastScrollY < this.mContentHeight;
        this.mLastScrollY = i;
        this.mContentHeight = floor;
        long j = currentTimeMillis - this.mLastScrollTimestamp;
        if (j <= 500) {
            this.mLastScrollTimestamp = j;
        }
        return z;
    }

    public void listenPageFinishTimeout(String str) {
        this.mWebLoadEvent.listenPageFinishTimeout(this, getUrlStr(), str);
    }

    public void loadUrl(String str) {
        AdaWebview adaWebview = this.mAdaWebview;
        if (adaWebview != null && !adaWebview.isDisposed()) {
            if (!str.startsWith(AbsoluteConst.PROTOCOL_JAVASCRIPT)) {
                this.didTouch = false;
            } else if (Build.VERSION.SDK_INT >= 19) {
                try {
                    evaluateJavascript(str, (ValueCallback) null);
                    return;
                } catch (Throwable th) {
                    Logger.e(TAG, "e.getMessage()==" + th.getMessage());
                    super.loadUrl(str);
                    return;
                }
            }
            HashMap hashMap = this.mUrlHeads.get(str);
            if (hashMap != null) {
                super.loadUrl(str, hashMap);
            } else {
                super.loadUrl(str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (((AdaFrameView) this.mAdaWebview.obtainFrameView()).getCircleRefreshView() != null) {
            invalidate();
        }
    }

    public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        InputConnection inputConnection;
        Logger.e("AssistantInput", "onCreateInputConnection 00");
        try {
            inputConnection = super.onCreateInputConnection(editorInfo);
            try {
                if (!BaseInfo.AuxiliaryInput || inputConnection == null) {
                    return inputConnection;
                }
                CustomeizedInputConnection customeizedInputConnection = new CustomeizedInputConnection(this.mAdaWebview, inputConnection, editorInfo, AdaWebview.sCustomeizedInputConnection);
                try {
                    AdaWebview.sCustomeizedInputConnection = customeizedInputConnection;
                    return customeizedInputConnection;
                } catch (Throwable th) {
                    th = th;
                    inputConnection = customeizedInputConnection;
                    th.printStackTrace();
                    return inputConnection;
                }
            } catch (Throwable th2) {
                th = th2;
                th.printStackTrace();
                return inputConnection;
            }
        } catch (Throwable th3) {
            th = th3;
            inputConnection = null;
            th.printStackTrace();
            return inputConnection;
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        WebJsEvent webJsEvent = this.mWebJsEvent;
        if (webJsEvent != null) {
            webJsEvent.releaseDefaultVideoPoster();
        }
        if (((AdaFrameView) this.mAdaWebview.obtainFrameView()).getCircleRefreshView() != null && ((AdaFrameView) this.mAdaWebview.obtainFrameView()).getCircleRefreshView().isRefreshEnable()) {
            ((AdaFrameView) this.mAdaWebview.obtainFrameView()).getCircleRefreshView().endRefresh();
        }
    }

    public void onDownloadStart(String str, String str2, String str3, String str4, long j) {
        final String str5 = str;
        String str6 = str3;
        final String str7 = str4;
        long j2 = j;
        Logger.i(TAG, "onDownloadStart " + str5 + "userAgent= " + str2 + "contentDisposition= " + str6 + "mimetype= " + str7 + "contentLength= " + j2);
        try {
            Context context = getContext();
            if (DeviceInfo.sDeviceSdkVer <= 8) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse(str));
                context.startActivity(intent);
            } else if (context != null && (context instanceof Activity)) {
                final String downloadFilename = PdrUtil.getDownloadFilename(str6, str7, str5);
                String str8 = context.getString(R.string.dcloud_common_download_do_file) + downloadFilename;
                if (0 < j2) {
                    str8 = str8 + "【" + new BigDecimal(j2).divide(new BigDecimal(PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED), 2, 4).floatValue() + "MB】";
                }
                String str9 = str8;
                Activity activity = (Activity) context;
                String string = context.getString(R.string.dcloud_common_download);
                String string2 = context.getString(R.string.dcloud_common_cancel);
                AnonymousClass4 r14 = new View.OnClickListener() {
                    public void onClick(View view) {
                        SysWebView sysWebView = SysWebView.this;
                        sysWebView.downloadFile(sysWebView.getContext(), downloadFilename, SysWebView.this.mAdaWebview.getAppName(), str5, str7, (ILoadCallBack) null);
                    }
                };
                double d = (double) context.getResources().getDisplayMetrics().widthPixels;
                Double.isNaN(d);
                DialogUtil.showAlertDialog(activity, str9, string, string2, r14, (View.OnClickListener) null, (DialogInterface.OnCancelListener) null, (DialogInterface.OnDismissListener) null, false, 0, 80, (int) (d * 0.9d));
            }
        } catch (Exception e) {
            Logger.w("webview onDownloadStart", e);
            Logger.e(TAG, "browser will download url=" + str5);
            try {
                Intent intent2 = new Intent("android.intent.action.VIEW");
                intent2.setData(Uri.parse(str));
                this.mAdaWebview.getActivity().startActivity(intent2);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!isChildSpeciaView(motionEvent.getX(), motionEvent.getY())) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        int actionMasked = motionEvent.getActionMasked();
        float rawX = motionEvent.getRawX();
        float rawY = motionEvent.getRawY();
        if (actionMasked == 0) {
            this.mLastMotionY = rawY;
            this.mLastMotionX = rawX;
            this.mIsBeingDragged = false;
        } else if (actionMasked == 2) {
            float f = rawX - this.mLastMotionX;
            float f2 = rawY - this.mLastMotionY;
            if (Math.abs(f2) > Math.abs(f) && Math.abs(f2) > 20.0f) {
                motionEvent.setAction(0);
                onTouchEvent(motionEvent);
                this.mIsBeingDragged = true;
            }
        }
        boolean z = this.mIsBeingDragged;
        return z ? z : super.onInterceptTouchEvent(motionEvent);
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (BaseInfo.USE_ACTIVITY_HANDLE_KEYEVENT) {
            return super.onKeyDown(i, keyEvent);
        }
        return doKeyDownAction(i, keyEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (BaseInfo.USE_ACTIVITY_HANDLE_KEYEVENT) {
            return super.onKeyUp(i, keyEvent);
        }
        return doKeyUpAction(i, keyEvent);
    }

    /* access modifiers changed from: protected */
    public void onOverScrolled(int i, int i2, boolean z, boolean z2) {
        super.onOverScrolled(i, i2, z, z2);
        AppEventForBlurManager.onScrollChanged(i, i2);
    }

    public void onPageStarted() {
        this.isToInvalidate = false;
    }

    public void onPreloadJSContent(String str) {
        this.mWebLoadEvent.onPreloadJSContent(this, this.mUrl, str);
    }

    public void onRefresh(int i) {
        this.mAdaWebview.mFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_PULL_DOWN_EVENT, Integer.valueOf(i));
        this.mAdaWebview.mFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_PULL_TO_REFRESH, Integer.valueOf(i));
    }

    /* access modifiers changed from: protected */
    public void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
        if ((i != i3 || i2 != i4) && this.mAdaWebview != null) {
            if (!AndroidResources.sIMEAlive && isReadyForPullUp(i2)) {
                Logger.d("onPlusScrollBottom", "上拉事件  url=" + this.mAdaWebview.obtainUrl());
                this.mAdaWebview.executeScript(PLUSSCROLLBOTTOM_JS_TEMPLATE);
            }
            JSONObject jSONObject = this.mAdaWebview.mFrameView.obtainFrameOptions().titleNView;
            if (jSONObject != null && jSONObject.has("type") && "transparent".equals(jSONObject.optString("type"))) {
                int i5 = this.mAdaWebview.mFrameView.obtainFrameOptions().coverage;
                if (i5 >= i4 || i5 >= i2) {
                    Object titleNView = TitleNViewUtil.getTitleNView(this.mAdaWebview.obtainFrameView().obtainWindowMgr(), this.mAdaWebview.obtainFrameView().obtainWebView(), this.mAdaWebview.obtainFrameView(), TitleNViewUtil.getTitleNViewId(this.mAdaWebview.obtainFrameView()));
                    if (titleNView instanceof ITitleNView) {
                        TitleNViewUtil.updateTitleNViewStatus((ITitleNView) titleNView, this.mAdaWebview.obtainFrameView().obtainWebView(), (float) i2, jSONObject, (float) i5);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.didTouch = true;
        if (Build.VERSION.SDK_INT == 16 && !PdrUtil.isEquals(Build.BRAND, "samsung") && motionEvent.getAction() == 0) {
            int scrollY = getScrollY();
            scrollTo(getScrollX(), scrollY + 1);
            scrollTo(getScrollX(), scrollY);
        }
        if (motionEvent.getAction() == 0) {
            this.mAdaWebview.mFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_WEBVIEW_ONTOUCH_START, Integer.valueOf(getContentHeight()));
        }
        return super.onTouchEvent(motionEvent);
    }

    public void onUpdatePlusData(String str) {
        this.mWebLoadEvent.onUpdatePlusData(this, getUrlStr(), str);
    }

    public void putHeads(String str, HashMap<String, String> hashMap) {
        this.mUrlHeads.put(str, hashMap);
    }

    public void removeAllCookie() {
        CookieManager cookieManager = this.cm;
        if (cookieManager != null) {
            cookieManager.removeAllCookie();
        }
    }

    public void removeAllViews() {
        try {
            for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
                View childAt = getChildAt(childCount);
                if (childAt != ((AdaFrameView) this.mAdaWebview.obtainFrameView()).getCircleRefreshView()) {
                    removeView(childAt);
                }
            }
        } catch (Exception unused) {
            super.removeAllViews();
            if (((AdaFrameView) this.mAdaWebview.obtainFrameView()).getCircleRefreshView() != null) {
                addView((View) ((AdaFrameView) this.mAdaWebview.obtainFrameView()).getCircleRefreshView(), -1, -1);
            }
        }
    }

    public void removeSessionCookie() {
        CookieManager cookieManager = this.cm;
        if (cookieManager != null) {
            cookieManager.removeSessionCookie();
        }
    }

    public void setBlockNetworkImage(boolean z) {
        this.webSettings.setBlockNetworkImage(z);
    }

    public void setCookie(String str, String str2) {
        CookieManager cookieManager = this.cm;
        if (cookieManager != null) {
            cookieManager.setAcceptCookie(true);
            this.cm.setCookie(str, str2);
            CookieSyncManager.getInstance().sync();
        }
    }

    public void setDcloudwebviewclientListener(IDCloudWebviewClientListener iDCloudWebviewClientListener) {
        setDcloudwebviewclientListener(iDCloudWebviewClientListener);
    }

    public void setDidTouch(boolean z) {
        this.didTouch = z;
    }

    public void setPageTitle(String str) {
        this.mPageTitle = str;
    }

    public void setUrlStr(String str) {
        this.mUrl = str;
    }

    public void setUserAgentString(String str) {
        this.webSettings.setUserAgentString(str);
    }

    public void setWebViewCacheMode(String str) {
        if (str.equals("default")) {
            this.mCacheMode = -1;
        } else if (str.equals("cacheElseNetwork")) {
            this.mCacheMode = 1;
        } else if (str.equals("noCache")) {
            this.mCacheMode = 2;
        } else if (str.equals("cacheOnly")) {
            this.mCacheMode = 3;
        }
        this.webSettings.setCacheMode(this.mCacheMode);
    }

    public void setWebViewClient(WebViewClient webViewClient) {
        if ((webViewClient instanceof WebLoadEvent) || webViewClient == null) {
            super.setWebViewClient(webViewClient);
        }
    }

    public ActionMode startActionMode(ActionMode.Callback callback) {
        if (Build.VERSION.SDK_INT >= 11) {
            return super.startActionMode(new CustomizedSelectActionModeCallback(callback));
        }
        return super.startActionMode(callback);
    }

    public String toString() {
        String str;
        String str2 = this.mUrl;
        if (str2 == null || (str = this.mBaseUrl) == null) {
            return super.toString();
        }
        int indexOf = str2.indexOf(str);
        String str3 = this.mUrl;
        if (indexOf >= 0) {
            str3 = str3.substring(this.mBaseUrl.length());
        }
        return "<url=" + str3 + ">;<hashcode=" + hashCode() + Operators.G;
    }

    public void webReload(boolean z) {
        if (z) {
            WebLoadEvent webLoadEvent = this.mWebLoadEvent;
            if (webLoadEvent != null) {
                webLoadEvent.reset();
            }
            this.webSettings.setCacheMode(2);
            return;
        }
        this.webSettings.setCacheMode(this.mCacheMode);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
        return;
     */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* JADX WARNING: Removed duplicated region for block: B:10:? A[ExcHandler: NoSuchMethodException (unused java.lang.NoSuchMethodException), SYNTHETIC, Splitter:B:6:0x0020] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void setWebViewData() {
        /*
            r7 = this;
            r0 = 0
            java.lang.Class[] r1 = new java.lang.Class[r0]
            r2 = 1
            java.lang.Class[] r3 = new java.lang.Class[r2]
            java.lang.Class r4 = java.lang.Boolean.TYPE
            r3[r0] = r4
            java.lang.Class<android.webkit.WebView> r4 = android.webkit.WebView.class
            java.lang.String r5 = "getFactory"
            java.lang.reflect.Method r1 = r4.getDeclaredMethod(r5, r1)     // Catch:{  }
            if (r1 == 0) goto L_0x0036
            r1.setAccessible(r2)     // Catch:{  }
            java.lang.Object[] r4 = new java.lang.Object[r0]     // Catch:{  }
            java.lang.Object[] r5 = new java.lang.Object[r2]     // Catch:{  }
            java.lang.Boolean r6 = java.lang.Boolean.FALSE     // Catch:{  }
            r5[r0] = r6     // Catch:{  }
            r0 = 0
            java.lang.Object r0 = r1.invoke(r0, r4)     // Catch:{ NoSuchMethodException -> 0x0036, NoSuchMethodException -> 0x0036, NoSuchMethodException -> 0x0036 }
            java.lang.Class r1 = r0.getClass()     // Catch:{ NoSuchMethodException -> 0x0036, NoSuchMethodException -> 0x0036, NoSuchMethodException -> 0x0036 }
            java.lang.String r4 = "setWebContentsDebuggingEnabled"
            java.lang.reflect.Method r1 = r1.getDeclaredMethod(r4, r3)     // Catch:{ NoSuchMethodException -> 0x0036, NoSuchMethodException -> 0x0036, NoSuchMethodException -> 0x0036 }
            if (r1 == 0) goto L_0x0036
            r1.setAccessible(r2)     // Catch:{ NoSuchMethodException -> 0x0036, NoSuchMethodException -> 0x0036, NoSuchMethodException -> 0x0036 }
            r1.invoke(r0, r5)     // Catch:{ NoSuchMethodException -> 0x0036, NoSuchMethodException -> 0x0036, NoSuchMethodException -> 0x0036 }
        L_0x0036:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.ui.webview.SysWebView.setWebViewData():void");
    }

    public SysWebView(Context context, AdaWebview adaWebview, IDCloudWebviewClientListener iDCloudWebviewClientListener) {
        super(context);
        this.mUserAgent = null;
        this.mAdaWebview = null;
        this.mWebLoadEvent = null;
        this.mWebJsEvent = null;
        this.mUrl = null;
        this.mScale = 0.0f;
        this.mContext = null;
        this.mBaseUrl = null;
        this.webSettings = getSettings();
        this.cm = null;
        this.mLastScrollY = 0;
        this.mContentHeight = 0;
        this.mThreshold = 2;
        this.mThresholdTime = 15;
        this.mLastScrollTimestamp = 0;
        this.mPageTitle = null;
        this.mDeafaltOverScrollMode = 0;
        this.mCacheMode = -1;
        this.didTouch = false;
        this.isToInvalidate = false;
        this.mUrlHeads = new HashMap<>();
        this.mEventY = 0;
        this.mEventX = 0;
        this.mTouchSlop = -1;
        this.mIsBeingDragged = true;
        this.mScale = getContext().getResources().getDisplayMetrics().density;
        Logger.d("WebViewImpl");
        this.mContext = context.getApplicationContext();
        BaseInfo.s_Webview_Count++;
        this.mAdaWebview = adaWebview;
        this.mDcloudwebviewclientListener = iDCloudWebviewClientListener;
    }

    public SysWebView(Context context, AdaWebview adaWebview, OnPageFinishedCallack onPageFinishedCallack) {
        super(context);
        this.mUserAgent = null;
        this.mAdaWebview = null;
        this.mWebLoadEvent = null;
        this.mWebJsEvent = null;
        this.mUrl = null;
        this.mScale = 0.0f;
        this.mContext = null;
        this.mBaseUrl = null;
        this.webSettings = getSettings();
        this.cm = null;
        this.mLastScrollY = 0;
        this.mContentHeight = 0;
        this.mThreshold = 2;
        this.mThresholdTime = 15;
        this.mLastScrollTimestamp = 0;
        this.mPageTitle = null;
        this.mDeafaltOverScrollMode = 0;
        this.mCacheMode = -1;
        this.didTouch = false;
        this.isToInvalidate = false;
        this.mUrlHeads = new HashMap<>();
        this.mEventY = 0;
        this.mEventX = 0;
        this.mTouchSlop = -1;
        this.mIsBeingDragged = true;
        Logger.d("WebViewImpl");
        this.mContext = context.getApplicationContext();
        this.mAdaWebview = adaWebview;
        this.mPageFinishedCallack = onPageFinishedCallack;
        this.mScale = getContext().getResources().getDisplayMetrics().density;
    }
}
