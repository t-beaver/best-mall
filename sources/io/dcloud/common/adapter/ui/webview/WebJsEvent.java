package io.dcloud.common.adapter.ui.webview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IActivityDelegate;
import io.dcloud.common.DHInterface.IActivityHandler;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.adapter.ui.AdaWebview;
import io.dcloud.common.adapter.ui.FileChooseDialog;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.CanvasHelper;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.PermissionUtil;
import io.dcloud.common.adapter.util.UEH;
import io.dcloud.common.util.AppConsoleLogUtil;
import io.dcloud.common.util.AppRuntime;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.ExifInterface;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import io.src.dcloud.adapter.DCloudAdapterUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class WebJsEvent extends WebChromeClient {
    static final FrameLayout.LayoutParams COVER_SCREEN_GRAVITY_CENTER = new FrameLayout.LayoutParams(-1, -1, 17);
    public static final int FILECHOOSER_RESULTCODE = 1;
    static final String TAG = "webview";
    private int defaultSystemUI = 0;
    FileChooseDialog dialog;
    private boolean isNeedCloseScreenWakelock = false;
    AdaWebview mAdaWebview = null;
    View mCustomView;
    WebChromeClient.CustomViewCallback mCustomViewCallback;
    private int mDefaultScreemOrientation = -2;
    private boolean mDefaultTitleBarVisibility = false;
    private Bitmap mDefaultVideoPoster = null;
    DialogListener mListener = null;
    private boolean mScreemOrientationChanged = false;
    ValueCallback<Uri> mUploadMessage;
    ValueCallback<Uri[]> mUploadMessage21Level;
    private boolean rptJsErr = true;

    class DialogListener implements DialogInterface.OnClickListener {
        JsResult mResult = null;

        DialogListener() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            this.mResult.cancel();
        }
    }

    public WebJsEvent(AdaWebview adaWebview) {
        this.mAdaWebview = adaWebview;
        adaWebview.mProgressIntValue = 0;
        this.rptJsErr = BaseInfo.getCmitInfo(BaseInfo.sLastRunApp).rptJse;
    }

    private void handleConsoleMessage(ConsoleMessage consoleMessage) {
        if (this.mAdaWebview != null) {
            String message = consoleMessage.message();
            if (!isFilterConsoleMessage(message)) {
                int lineNumber = consoleMessage.lineNumber();
                String sourceId = consoleMessage.sourceId();
                String name = consoleMessage.messageLevel().name();
                ConsoleMessage.MessageLevel messageLevel = consoleMessage.messageLevel();
                if (PdrUtil.isEmpty(sourceId)) {
                    AppConsoleLogUtil.DCLog(message, name);
                } else {
                    try {
                        sourceId = this.mAdaWebview.getDCWebView().convertRelPath(sourceId);
                    } catch (Exception unused) {
                    }
                    message = message + " at " + sourceId + ":" + lineNumber;
                    AppConsoleLogUtil.DCLog(message, name);
                }
                JSONObject createJSONObject = JSONUtil.createJSONObject(AppRuntime.getUniStatistics());
                boolean z = false;
                if (createJSONObject != null) {
                    String string = JSONUtil.getString(createJSONObject, "version");
                    boolean z2 = JSONUtil.getBoolean(createJSONObject, WebLoadEvent.ENABLE);
                    if (PdrUtil.isEquals(ExifInterface.GPS_MEASUREMENT_2D, string) && z2) {
                        z = true;
                    }
                }
                if (z && this.rptJsErr && messageLevel == ConsoleMessage.MessageLevel.ERROR && this.mAdaWebview.obtainApp() != null && !BaseInfo.isBase(this.mAdaWebview.getContext()) && this.mAdaWebview.obtainApp().isUniApp()) {
                    JSONObject jSONObject = this.mAdaWebview.mFrameView.obtainFrameOptions().mUniPageUrl;
                    String url = this.mAdaWebview.getDCWebView().getUrl();
                    if (jSONObject != null && jSONObject.has(AbsoluteConst.XML_PATH)) {
                        url = jSONObject.optString(AbsoluteConst.XML_PATH);
                        if (jSONObject.has("query")) {
                            String optString = jSONObject.optString("query");
                            if (!TextUtils.isEmpty(optString)) {
                                url = url + Operators.CONDITION_IF_STRING + optString;
                            }
                        }
                    }
                    UEH.commitUncatchException(this.mAdaWebview.getContext(), url, message, 2);
                }
            }
        }
    }

    private void handleMessage(JsPromptResult jsPromptResult, AdaWebview adaWebview, String str, String str2, String str3, boolean z) {
        jsPromptResult.confirm(this.mAdaWebview.execScript(str, str2, JSONUtil.createJSONArray(str3), z));
    }

    private void initUniLoadUrl() {
        if (!this.mAdaWebview.isDisposed() && this.mAdaWebview.obtainApp() != null && BaseInfo.isUniAppAppid(this.mAdaWebview.obtainApp())) {
            boolean isWeexUniJs = BaseInfo.isWeexUniJs(this.mAdaWebview.obtainApp());
        }
    }

    private boolean isCallbackId(String str) {
        return str != null && str.startsWith(IApp.ConfigProperty.CONFIG_PLUS);
    }

    private boolean isFilterConsoleMessage(String str) {
        return !PdrUtil.isEmpty(str) && (str.contains("viewport-fit") || str.contains("Ignored attempt to cancel a touchend event with cancelable=false"));
    }

    private boolean isUrlWhiteListed(String str) {
        return true;
    }

    private void openFileChooserLogic(ValueCallback<Uri> valueCallback, String str, String str2) {
        openFileChooserLogic(valueCallback, (ValueCallback<Uri[]>) null, str, str2, (WebChromeClient.FileChooserParams) null);
    }

    private void setStatusBarVisibility(Activity activity, boolean z) {
        if (z) {
            activity.getWindow().getDecorView().setSystemUiVisibility(this.defaultSystemUI);
            return;
        }
        this.defaultSystemUI = activity.getWindow().getDecorView().getSystemUiVisibility();
        activity.getWindow().getDecorView().setSystemUiVisibility(5894);
    }

    /* access modifiers changed from: private */
    public void showOpenFileChooser(ValueCallback<Uri> valueCallback, ValueCallback<Uri[]> valueCallback2, String str, String str2, WebChromeClient.FileChooserParams fileChooserParams) {
        this.mUploadMessage = valueCallback;
        this.mUploadMessage21Level = valueCallback2;
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.addCategory("android.intent.category.OPENABLE");
        if (!PdrUtil.isEmpty(str)) {
            intent.setType(str);
        } else {
            intent.setType("*/*");
        }
        if (Build.VERSION.SDK_INT >= 21 && fileChooserParams != null && fileChooserParams.getMode() == 1) {
            intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
        }
        this.dialog = new FileChooseDialog(this.mAdaWebview.getActivity(), this.mAdaWebview.getActivity(), intent);
        this.mAdaWebview.obtainFrameView().obtainApp().registerSysEventListener(new ISysEventListener() {
            private Uri getUri(Uri uri) {
                Cursor query = WebJsEvent.this.mAdaWebview.getContext().getContentResolver().query(uri, new String[]{"_data"}, (String) null, (String[]) null, (String) null);
                if (query != null) {
                    if (query.moveToFirst()) {
                        try {
                            int columnIndexOrThrow = query.getColumnIndexOrThrow("_data");
                            if (columnIndexOrThrow > -1) {
                                String string = query.getString(columnIndexOrThrow);
                                uri = Uri.parse(string);
                                if (PdrUtil.isEmpty(uri.getScheme())) {
                                    String str = string.startsWith("/") ? DeviceInfo.FILE_PROTOCOL : "file:///";
                                    uri = Uri.parse(str + string);
                                }
                            }
                        } catch (Exception unused) {
                        }
                    }
                    query.close();
                }
                return uri;
            }

            /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v7, resolved type: java.lang.Object} */
            /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v8, resolved type: android.net.Uri} */
            /* JADX WARNING: Multi-variable type inference failed */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public boolean onExecute(io.dcloud.common.DHInterface.ISysEventListener.SysEventType r8, java.lang.Object r9) {
                /*
                    r7 = this;
                    java.lang.Object[] r9 = (java.lang.Object[]) r9
                    r0 = 0
                    r1 = r9[r0]
                    java.lang.Integer r1 = (java.lang.Integer) r1
                    int r1 = r1.intValue()
                    r2 = 1
                    r3 = r9[r2]
                    java.lang.Integer r3 = (java.lang.Integer) r3
                    int r3 = r3.intValue()
                    io.dcloud.common.adapter.ui.webview.WebJsEvent r4 = io.dcloud.common.adapter.ui.webview.WebJsEvent.this
                    io.dcloud.common.adapter.ui.FileChooseDialog r4 = r4.dialog
                    if (r4 == 0) goto L_0x001d
                    r4.dismiss()
                L_0x001d:
                    io.dcloud.common.adapter.ui.webview.WebJsEvent r4 = io.dcloud.common.adapter.ui.webview.WebJsEvent.this
                    io.dcloud.common.adapter.ui.AdaWebview r4 = r4.mAdaWebview
                    io.dcloud.common.DHInterface.IFrameView r4 = r4.obtainFrameView()
                    io.dcloud.common.DHInterface.IApp r4 = r4.obtainApp()
                    io.dcloud.common.DHInterface.ISysEventListener$SysEventType r5 = io.dcloud.common.DHInterface.ISysEventListener.SysEventType.onActivityResult
                    r4.unregisterSysEventListener(r7, r5)
                    r4 = 0
                    if (r3 == 0) goto L_0x011f
                    if (r8 != r5) goto L_0x011f
                    r8 = 2
                    r9 = r9[r8]
                    android.content.Intent r9 = (android.content.Intent) r9
                    java.util.ArrayList r3 = new java.util.ArrayList
                    r3.<init>()
                    if (r1 != r2) goto L_0x009f
                    android.content.ClipData r8 = r9.getClipData()
                    java.lang.String r1 = "content"
                    if (r8 == 0) goto L_0x0070
                    android.content.ClipData r8 = r9.getClipData()
                    r9 = 0
                L_0x004c:
                    int r5 = r8.getItemCount()
                    if (r9 >= r5) goto L_0x00eb
                    android.content.ClipData$Item r5 = r8.getItemAt(r9)
                    android.net.Uri r5 = r5.getUri()
                    if (r5 == 0) goto L_0x006d
                    java.lang.String r6 = r5.getScheme()
                    boolean r6 = r1.equals(r6)
                    if (r6 == 0) goto L_0x006d
                    android.net.Uri r5 = r7.getUri(r5)
                    r3.add(r5)
                L_0x006d:
                    int r9 = r9 + 1
                    goto L_0x004c
                L_0x0070:
                    android.net.Uri r8 = r9.getData()
                    if (r8 == 0) goto L_0x0084
                    java.lang.String r9 = r8.getScheme()
                    boolean r9 = r1.equals(r9)
                    if (r9 == 0) goto L_0x0084
                    android.net.Uri r8 = r7.getUri(r8)
                L_0x0084:
                    r3.add(r8)
                    java.lang.StringBuilder r9 = new java.lang.StringBuilder
                    r9.<init>()
                    java.lang.String r1 = "openFileChooserLogic  OnActivityResult url="
                    r9.append(r1)
                    r9.append(r8)
                    java.lang.String r8 = r9.toString()
                    java.lang.String r9 = "webview"
                    io.dcloud.common.adapter.util.Logger.i(r9, r8)
                    goto L_0x00eb
                L_0x009f:
                    if (r1 != r8) goto L_0x00eb
                    io.dcloud.common.adapter.ui.webview.WebJsEvent r8 = io.dcloud.common.adapter.ui.webview.WebJsEvent.this
                    io.dcloud.common.adapter.ui.FileChooseDialog r8 = r8.dialog
                    java.util.List<java.io.File> r8 = r8.uris
                    if (r8 == 0) goto L_0x00eb
                    java.util.Iterator r8 = r8.iterator()
                L_0x00ad:
                    boolean r9 = r8.hasNext()
                    if (r9 == 0) goto L_0x00eb
                    java.lang.Object r9 = r8.next()
                    java.io.File r9 = (java.io.File) r9
                    boolean r1 = r9.exists()
                    if (r1 == 0) goto L_0x00ad
                    io.dcloud.common.adapter.ui.webview.WebJsEvent r8 = io.dcloud.common.adapter.ui.webview.WebJsEvent.this
                    io.dcloud.common.adapter.ui.AdaWebview r8 = r8.mAdaWebview
                    android.app.Activity r8 = r8.getActivity()
                    java.lang.StringBuilder r1 = new java.lang.StringBuilder
                    r1.<init>()
                    io.dcloud.common.adapter.ui.webview.WebJsEvent r5 = io.dcloud.common.adapter.ui.webview.WebJsEvent.this
                    io.dcloud.common.adapter.ui.AdaWebview r5 = r5.mAdaWebview
                    android.app.Activity r5 = r5.getActivity()
                    java.lang.String r5 = r5.getPackageName()
                    r1.append(r5)
                    java.lang.String r5 = ".dc.fileprovider"
                    r1.append(r5)
                    java.lang.String r1 = r1.toString()
                    android.net.Uri r8 = androidx.core.content.FileProvider.getUriForFile(r8, r1, r9)
                    r3.add(r8)
                L_0x00eb:
                    int r8 = r3.size()
                    if (r8 <= 0) goto L_0x00fe
                    int r8 = r3.size()
                    android.net.Uri[] r8 = new android.net.Uri[r8]
                    java.lang.Object[] r8 = r3.toArray(r8)
                    android.net.Uri[] r8 = (android.net.Uri[]) r8
                    goto L_0x00ff
                L_0x00fe:
                    r8 = r4
                L_0x00ff:
                    io.dcloud.common.adapter.ui.webview.WebJsEvent r9 = io.dcloud.common.adapter.ui.webview.WebJsEvent.this
                    android.webkit.ValueCallback<android.net.Uri[]> r1 = r9.mUploadMessage21Level
                    if (r1 == 0) goto L_0x0109
                    r1.onReceiveValue(r8)
                    goto L_0x011e
                L_0x0109:
                    android.webkit.ValueCallback<android.net.Uri> r8 = r9.mUploadMessage
                    if (r8 == 0) goto L_0x011e
                    boolean r9 = r3.isEmpty()
                    if (r9 == 0) goto L_0x0114
                    goto L_0x011b
                L_0x0114:
                    java.lang.Object r9 = r3.get(r0)
                    r4 = r9
                    android.net.Uri r4 = (android.net.Uri) r4
                L_0x011b:
                    r8.onReceiveValue(r4)
                L_0x011e:
                    return r2
                L_0x011f:
                    io.dcloud.common.adapter.ui.webview.WebJsEvent r8 = io.dcloud.common.adapter.ui.webview.WebJsEvent.this
                    android.webkit.ValueCallback<android.net.Uri[]> r9 = r8.mUploadMessage21Level
                    if (r9 == 0) goto L_0x0129
                    r9.onReceiveValue(r4)
                    goto L_0x0130
                L_0x0129:
                    android.webkit.ValueCallback<android.net.Uri> r8 = r8.mUploadMessage
                    if (r8 == 0) goto L_0x0130
                    r8.onReceiveValue(r4)
                L_0x0130:
                    return r0
                */
                throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.ui.webview.WebJsEvent.AnonymousClass10.onExecute(io.dcloud.common.DHInterface.ISysEventListener$SysEventType, java.lang.Object):boolean");
            }
        }, ISysEventListener.SysEventType.onActivityResult);
        try {
            this.dialog.show();
            this.dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialogInterface) {
                    try {
                        WebJsEvent webJsEvent = WebJsEvent.this;
                        ValueCallback<Uri[]> valueCallback = webJsEvent.mUploadMessage21Level;
                        if (valueCallback != null) {
                            valueCallback.onReceiveValue((Object) null);
                            return;
                        }
                        ValueCallback<Uri> valueCallback2 = webJsEvent.mUploadMessage;
                        if (valueCallback2 != null) {
                            valueCallback2.onReceiveValue((Object) null);
                        }
                    } catch (Exception unused) {
                    }
                }
            });
        } catch (Exception unused) {
            Logger.e("openFileChooserLogic Exception");
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0030 A[Catch:{ JSONException -> 0x00f2 }] */
    /* JADX WARNING: Removed duplicated region for block: B:43:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateTitleNViewTitle(java.lang.String r19) {
        /*
            r18 = this;
            r1 = r18
            java.lang.String r0 = "titletext"
            java.lang.String r2 = "titleText"
            io.dcloud.common.adapter.ui.AdaWebview r3 = r1.mAdaWebview
            io.dcloud.common.adapter.ui.AdaFrameView r3 = r3.mFrameView
            io.dcloud.common.adapter.util.ViewOptions r3 = r3.obtainFrameOptions()
            org.json.JSONObject r3 = r3.titleNView
            if (r3 == 0) goto L_0x00f6
            r4 = 0
            boolean r5 = r3.has(r2)     // Catch:{ JSONException -> 0x00f2 }
            if (r5 == 0) goto L_0x001f
            java.lang.Object r0 = r3.get(r2)     // Catch:{ JSONException -> 0x00f2 }
        L_0x001d:
            r4 = r0
            goto L_0x002a
        L_0x001f:
            boolean r2 = r3.has(r0)     // Catch:{ JSONException -> 0x00f2 }
            if (r2 == 0) goto L_0x002a
            java.lang.Object r0 = r3.get(r0)     // Catch:{ JSONException -> 0x00f2 }
            goto L_0x001d
        L_0x002a:
            if (r4 == 0) goto L_0x0030
            boolean r0 = r4 instanceof java.lang.String     // Catch:{ JSONException -> 0x00f2 }
            if (r0 != 0) goto L_0x00f6
        L_0x0030:
            java.lang.String r0 = "titleColor"
            java.lang.String r0 = r3.optString(r0)     // Catch:{ JSONException -> 0x00f2 }
            boolean r2 = android.text.TextUtils.isEmpty(r0)     // Catch:{ JSONException -> 0x00f2 }
            if (r2 == 0) goto L_0x0042
            java.lang.String r0 = "titlecolor"
            java.lang.String r0 = r3.optString(r0)     // Catch:{ JSONException -> 0x00f2 }
        L_0x0042:
            java.lang.String r2 = "titleSize"
            java.lang.String r2 = r3.optString(r2)     // Catch:{ JSONException -> 0x00f2 }
            boolean r4 = android.text.TextUtils.isEmpty(r2)     // Catch:{ JSONException -> 0x00f2 }
            if (r4 == 0) goto L_0x0054
            java.lang.String r2 = "titlesize"
            java.lang.String r2 = r3.optString(r2)     // Catch:{ JSONException -> 0x00f2 }
        L_0x0054:
            r8 = r2
            java.lang.String r2 = "titleOverflow"
            java.lang.String r9 = r3.optString(r2)     // Catch:{ JSONException -> 0x00f2 }
            java.lang.String r2 = "titleAlign"
            java.lang.String r10 = r3.optString(r2)     // Catch:{ JSONException -> 0x00f2 }
            java.lang.String r2 = "titleIcon"
            java.lang.String r11 = r3.optString(r2)     // Catch:{ JSONException -> 0x00f2 }
            java.lang.String r2 = "titleIconRadius"
            java.lang.String r12 = r3.optString(r2)     // Catch:{ JSONException -> 0x00f2 }
            java.lang.String r2 = "subtitleText"
            java.lang.String r13 = r3.optString(r2)     // Catch:{ JSONException -> 0x00f2 }
            java.lang.String r2 = "subtitleColor"
            java.lang.String r14 = r3.optString(r2)     // Catch:{ JSONException -> 0x00f2 }
            java.lang.String r2 = "subtitleSize"
            java.lang.String r15 = r3.optString(r2)     // Catch:{ JSONException -> 0x00f2 }
            java.lang.String r2 = "subtitleOverflow"
            java.lang.String r16 = r3.optString(r2)     // Catch:{ JSONException -> 0x00f2 }
            java.lang.String r2 = "titleIconWidth"
            java.lang.String r17 = r3.optString(r2)     // Catch:{ JSONException -> 0x00f2 }
            boolean r2 = android.text.TextUtils.isEmpty(r19)     // Catch:{ JSONException -> 0x00f2 }
            if (r2 != 0) goto L_0x00f6
            boolean r2 = android.text.TextUtils.isEmpty(r0)     // Catch:{ JSONException -> 0x00f2 }
            if (r2 != 0) goto L_0x00f6
            boolean r2 = android.text.TextUtils.isEmpty(r8)     // Catch:{ JSONException -> 0x00f2 }
            if (r2 != 0) goto L_0x00f6
            io.dcloud.common.adapter.ui.AdaWebview r2 = r1.mAdaWebview     // Catch:{ JSONException -> 0x00f2 }
            io.dcloud.common.adapter.ui.AdaFrameView r2 = r2.mFrameView     // Catch:{ JSONException -> 0x00f2 }
            io.dcloud.common.DHInterface.AbsMgr r2 = r2.obtainWindowMgr()     // Catch:{ JSONException -> 0x00f2 }
            io.dcloud.common.adapter.ui.AdaWebview r4 = r1.mAdaWebview     // Catch:{ JSONException -> 0x00f2 }
            io.dcloud.common.adapter.ui.AdaFrameView r4 = r4.mFrameView     // Catch:{ JSONException -> 0x00f2 }
            io.dcloud.common.DHInterface.IWebview r4 = r4.obtainWebView()     // Catch:{ JSONException -> 0x00f2 }
            io.dcloud.common.adapter.ui.AdaWebview r5 = r1.mAdaWebview     // Catch:{ JSONException -> 0x00f2 }
            io.dcloud.common.adapter.ui.AdaFrameView r6 = r5.mFrameView     // Catch:{ JSONException -> 0x00f2 }
            io.dcloud.common.DHInterface.IFrameView r5 = r5.obtainFrameView()     // Catch:{ JSONException -> 0x00f2 }
            java.lang.String r5 = io.dcloud.common.util.TitleNViewUtil.getTitleNViewId(r5)     // Catch:{ JSONException -> 0x00f2 }
            java.lang.Object r2 = io.dcloud.common.util.TitleNViewUtil.getTitleNView(r2, r4, r6, r5)     // Catch:{ JSONException -> 0x00f2 }
            boolean r4 = r2 instanceof io.dcloud.common.DHInterface.ITitleNView     // Catch:{ JSONException -> 0x00f2 }
            if (r4 == 0) goto L_0x00f6
            r5 = r2
            io.dcloud.common.DHInterface.ITitleNView r5 = (io.dcloud.common.DHInterface.ITitleNView) r5     // Catch:{ JSONException -> 0x00f2 }
            java.lang.String r2 = "transparent"
            java.lang.String r4 = "type"
            java.lang.String r3 = r3.optString(r4)     // Catch:{ JSONException -> 0x00f2 }
            boolean r2 = r2.equals(r3)     // Catch:{ JSONException -> 0x00f2 }
            if (r2 == 0) goto L_0x00e7
            boolean r2 = android.text.TextUtils.isEmpty(r0)     // Catch:{ JSONException -> 0x00f2 }
            if (r2 != 0) goto L_0x00e7
            int r2 = r5.getTitleColor()     // Catch:{ JSONException -> 0x00f2 }
            int r2 = android.graphics.Color.alpha(r2)     // Catch:{ JSONException -> 0x00f2 }
            if (r2 != 0) goto L_0x00e7
            r2 = 0
            java.lang.String r0 = io.dcloud.common.util.TitleNViewUtil.changeColorAlpha((java.lang.String) r0, (float) r2)     // Catch:{ JSONException -> 0x00f2 }
        L_0x00e7:
            r7 = r0
            io.dcloud.common.adapter.ui.AdaWebview r0 = r1.mAdaWebview     // Catch:{ JSONException -> 0x00f2 }
            io.dcloud.common.adapter.ui.AdaFrameView r4 = r0.mFrameView     // Catch:{ JSONException -> 0x00f2 }
            r6 = r19
            io.dcloud.common.util.TitleNViewUtil.drawTitle(r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17)     // Catch:{ JSONException -> 0x00f2 }
            goto L_0x00f6
        L_0x00f2:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00f6:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.ui.webview.WebJsEvent.updateTitleNViewTitle(java.lang.String):void");
    }

    public void destroy() {
        this.mAdaWebview = null;
    }

    public Bitmap getDefaultVideoPoster() {
        return super.getDefaultVideoPoster();
    }

    public void hideCustomView() {
        Log.d(TAG, "Hidding Custom View");
        if (this.mCustomView != null) {
            if (this.mAdaWebview.obtainMainView() != null) {
                this.mAdaWebview.obtainMainView().setVisibility(0);
            }
            this.mCustomView.setVisibility(8);
            ((ViewGroup) this.mCustomView.getParent()).removeView(this.mCustomView);
            Activity activity = null;
            this.mCustomView = null;
            this.mCustomViewCallback.onCustomViewHidden();
            if (this.mAdaWebview.obtainApp() != null) {
                activity = this.mAdaWebview.obtainApp().getActivity();
            }
            if (activity != null) {
                if (!(!this.isNeedCloseScreenWakelock || this.mAdaWebview.obtainFrameView() == null || this.mAdaWebview.obtainFrameView().obtainWindowMgr() == null)) {
                    this.mAdaWebview.obtainFrameView().obtainWindowMgr().processEvent(IMgr.MgrType.FeatureMgr, 1, new Object[]{this.mAdaWebview, "device", "setWakelock", new JSONArray().put(false)});
                }
                IApp obtainApp = this.mAdaWebview.obtainApp();
                if (!(obtainApp == null || obtainApp.obtainStatusBarMgr() == null)) {
                    if (!obtainApp.obtainStatusBarMgr().isFullScreenOrImmersive()) {
                        setStatusBarVisibility(activity, true);
                    } else if (!obtainApp.obtainStatusBarMgr().isFullScreen) {
                        setStatusBarVisibility(activity, true);
                    }
                    if (obtainApp.obtainStatusBarMgr().isImmersive) {
                        obtainApp.obtainStatusBarMgr().setImmersive(activity, true);
                    }
                }
                if (this.mScreemOrientationChanged) {
                    this.mScreemOrientationChanged = false;
                    AdaWebview.ScreemOrientationChangedNeedLayout = true;
                    activity.setRequestedOrientation(this.mDefaultScreemOrientation);
                }
                IActivityHandler iActivityHandler = DCloudAdapterUtil.getIActivityHandler(activity);
                if (iActivityHandler != null) {
                    iActivityHandler.setSideBarVisibility(0);
                }
            }
        }
    }

    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        handleConsoleMessage(consoleMessage);
        return true;
    }

    public void onExceededDatabaseQuota(String str, String str2, long j, long j2, long j3, WebStorage.QuotaUpdater quotaUpdater) {
        Logger.i(TAG, "onExceededDatabaseQuota url=" + str);
        quotaUpdater.updateQuota(j2 * 2);
    }

    public void onGeolocationPermissionsHidePrompt() {
        Logger.i(TAG, "onGeolocationPermissionsHidePrompt");
        super.onGeolocationPermissionsHidePrompt();
    }

    public void onGeolocationPermissionsShowPrompt(final String str, final GeolocationPermissions.Callback callback) {
        if (this.mAdaWebview != null) {
            Logger.i(TAG, "onGeolocationPermissionsShowPrompt origin=" + str);
            IApp obtainApp = this.mAdaWebview.obtainFrameView().obtainApp();
            if (obtainApp != null) {
                PermissionUtil.usePermission(this.mAdaWebview.getActivity(), "base", PermissionUtil.PMS_LOCATION, 2, new PermissionUtil.StreamPermissionRequest(obtainApp) {
                    public void onDenied(String str) {
                        callback.invoke(str, false, false);
                    }

                    public void onGranted(String str) {
                        callback.invoke(str, true, false);
                    }
                });
            }
        }
    }

    public void onHideCustomView() {
        hideCustomView();
    }

    public boolean onJsAlert(WebView webView, String str, String str2, final JsResult jsResult) {
        AdaWebview adaWebview = this.mAdaWebview;
        if (adaWebview == null || PdrUtil.isEmpty(adaWebview.getAppName())) {
            return super.onJsAlert(webView, str, str2, jsResult);
        }
        final AlertDialog create = new AlertDialog.Builder(this.mAdaWebview.getActivity()).create();
        create.setTitle(this.mAdaWebview.getAppName());
        create.setMessage(str2);
        if (this.mListener == null) {
            this.mListener = new DialogListener();
        }
        this.mListener.mResult = jsResult;
        create.setButton(AndroidResources.getString(17039370), this.mListener);
        create.setCanceledOnTouchOutside(false);
        create.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() != 1 || i != 4) {
                    return false;
                }
                jsResult.cancel();
                create.dismiss();
                return true;
            }
        });
        create.show();
        return true;
    }

    public boolean onJsBeforeUnload(WebView webView, String str, String str2, JsResult jsResult) {
        return super.onJsBeforeUnload(webView, str, str2, jsResult);
    }

    public boolean onJsConfirm(WebView webView, String str, String str2, final JsResult jsResult) {
        AdaWebview adaWebview = this.mAdaWebview;
        if (adaWebview == null || PdrUtil.isEmpty(adaWebview.getAppName())) {
            return super.onJsConfirm(webView, str, str2, jsResult);
        }
        try {
            final AlertDialog create = new AlertDialog.Builder(this.mAdaWebview.getActivity()).create();
            create.setMessage(str2);
            create.setTitle(this.mAdaWebview.getAppName());
            create.setButton(AndroidResources.getString(17039370), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    jsResult.confirm();
                }
            });
            create.setButton2(AndroidResources.getString(17039360), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    jsResult.cancel();
                }
            });
            create.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialogInterface) {
                    jsResult.cancel();
                }
            });
            create.setOnKeyListener(new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    if (keyEvent.getAction() != 0 || i != 4) {
                        return false;
                    }
                    jsResult.cancel();
                    create.dismiss();
                    return true;
                }
            });
            create.show();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return super.onJsConfirm(webView, str, str2, jsResult);
        }
    }

    public boolean onJsPrompt(WebView webView, String str, String str2, String str3, JsPromptResult jsPromptResult) {
        CharSequence charSequence;
        String str4;
        CharSequence charSequence2;
        CharSequence charSequence3;
        int i;
        String str5 = str2;
        String str6 = str3;
        JsPromptResult jsPromptResult2 = jsPromptResult;
        if (this.mAdaWebview == null) {
            return false;
        }
        boolean isUrlWhiteListed = isUrlWhiteListed(str);
        if (!isUrlWhiteListed || str6 == null || str3.length() <= 3 || !str6.substring(0, 4).equals("pdr:")) {
            CharSequence charSequence4 = "\\\"";
            CharSequence charSequence5 = JSUtil.QUOTE;
            String str7 = "pdr:";
            if (this.mAdaWebview.mReceiveJSValue_android42 == null || !isUrlWhiteListed || str6 == null || str3.length() <= 5 || !str6.substring(0, 5).equals("sync:")) {
                final JsPromptResult jsPromptResult3 = jsPromptResult;
                final AlertDialog create = new AlertDialog.Builder(this.mAdaWebview.getActivity()).create();
                create.setMessage(str5);
                create.setTitle(this.mAdaWebview.getAppName());
                final EditText editText = new EditText(this.mAdaWebview.getActivity());
                if (str6 != null) {
                    editText.setText(str6);
                    editText.setSelection(0, str3.length());
                    DeviceInfo.showIME(editText);
                }
                create.setView(editText);
                create.setButton(AndroidResources.getString(17039370), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        jsPromptResult3.confirm(editText.getText().toString());
                    }
                });
                create.setButton2(AndroidResources.getString(17039360), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        jsPromptResult3.cancel();
                    }
                });
                create.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                        if (AndroidResources.sIMEAlive || keyEvent.getAction() != 0 || i != 4) {
                            return false;
                        }
                        create.dismiss();
                        jsPromptResult3.cancel();
                        return true;
                    }
                });
                create.show();
                return true;
            }
            try {
                JSONArray jSONArray = new JSONArray(str6.substring(5));
                charSequence = charSequence5;
                try {
                    jsPromptResult.confirm(this.mAdaWebview.mReceiveJSValue_android42.__js__call__native__(jSONArray.getString(0), jSONArray.getString(1)));
                    return true;
                } catch (JSONException e) {
                    e = e;
                }
            } catch (JSONException e2) {
                e = e2;
                JsPromptResult jsPromptResult4 = jsPromptResult;
                charSequence = charSequence5;
                if (!PdrUtil.isEquals(str6, str6.replace(charSequence4, charSequence))) {
                    String replace = str5.replace(charSequence4, charSequence);
                    String substring = replace.substring(1, replace.length() - 1);
                    String substring2 = str6.replace(charSequence4, charSequence).substring(4);
                    onJsPrompt(webView, str, substring, str7 + substring2.substring(1, substring2.length() - 1), jsPromptResult);
                    return true;
                }
                e.printStackTrace();
                Logger.e(TAG, "onJsPrompt js->native message=" + str5 + ";defaultValue=" + str6);
                return true;
            }
        } else {
            try {
                JSONArray jSONArray2 = new JSONArray(str6.substring(4));
                String string = jSONArray2.getString(0);
                String string2 = jSONArray2.getString(1);
                boolean z = jSONArray2.getBoolean(2);
                JsPromptResult jsPromptResult5 = jsPromptResult;
                AdaWebview adaWebview = this.mAdaWebview;
                charSequence2 = "\\\"";
                String str8 = string;
                charSequence3 = JSUtil.QUOTE;
                i = 4;
                str4 = "pdr:";
                try {
                    handleMessage(jsPromptResult5, adaWebview, str8, string2, str2, z);
                    return true;
                } catch (JSONException e3) {
                    e = e3;
                }
            } catch (JSONException e4) {
                e = e4;
                charSequence2 = "\\\"";
                charSequence3 = JSUtil.QUOTE;
                str4 = "pdr:";
                i = 4;
                if (!PdrUtil.isEquals(str6, str6.replace(charSequence2, charSequence3))) {
                    String replace2 = str5.replace(charSequence2, charSequence3);
                    String substring3 = replace2.substring(1, replace2.length() - 1);
                    String substring4 = str6.replace(charSequence2, charSequence3).substring(i);
                    onJsPrompt(webView, str, substring3, str4 + substring4.substring(1, substring4.length() - 1), jsPromptResult);
                    return true;
                }
                e.printStackTrace();
                Logger.e(TAG, "onJsPrompt js->native message=" + str5 + ";defaultValue=" + str6);
                return true;
            }
        }
    }

    public void onPermissionRequest(PermissionRequest permissionRequest) {
        permissionRequest.grant(permissionRequest.getResources());
    }

    public void onProgressChanged(WebView webView, int i) {
        AdaWebview adaWebview = this.mAdaWebview;
        if (adaWebview != null) {
            if (i < 20 && !adaWebview.unReceiveTitle) {
                adaWebview.unReceiveTitle = true;
            }
            adaWebview.mProgressIntValue = i;
            adaWebview.dispatchWebviewStateEvent(3, Integer.valueOf(i));
            this.mAdaWebview.mFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_PROGRESS_CHANGED, Integer.valueOf(i));
            super.onProgressChanged(webView, i);
        }
    }

    public void onReachedMaxAppCacheSize(long j, long j2, WebStorage.QuotaUpdater quotaUpdater) {
        Logger.i(TAG, "onReachedMaxAppCacheSize");
        super.onReachedMaxAppCacheSize(j, j2, quotaUpdater);
    }

    public void onReceivedTitle(WebView webView, String str) {
        AdaWebview adaWebview = this.mAdaWebview;
        if (adaWebview != null) {
            adaWebview.unReceiveTitle = false;
            adaWebview.dispatchWebviewStateEvent(4, str);
            this.mAdaWebview.mFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_TITLE_UPDATE, str);
            this.mAdaWebview.getDCWebView().setPageTitle(str);
            Logger.i(TAG, "onReceivedTitle title=" + str);
            this.mAdaWebview.mLoadCompleted = true;
            updateTitleNViewTitle(str);
            initUniLoadUrl();
            super.onReceivedTitle(webView, str);
        }
    }

    public void onReceivedTouchIconUrl(WebView webView, String str, boolean z) {
        Logger.d("super.onReceivedTouchIconUrl(view, url, precomposed");
        super.onReceivedTouchIconUrl(webView, str, z);
    }

    public void onRequestFocus(WebView webView) {
        Logger.i(TAG, "onRequestFocus");
        super.onRequestFocus(webView);
    }

    public void onShowCustomView(View view, WebChromeClient.CustomViewCallback customViewCallback) {
        showCustomView(view, customViewCallback);
    }

    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        openFileChooserLogic((ValueCallback<Uri>) null, valueCallback, fileChooserParams.getAcceptTypes() != null ? fileChooserParams.getAcceptTypes()[0] : null, "", fileChooserParams);
        return true;
    }

    public void openFileChooser(ValueCallback<Uri> valueCallback) {
        openFileChooserLogic(valueCallback, (String) null, (String) null);
    }

    public void releaseDefaultVideoPoster() {
        Bitmap bitmap = this.mDefaultVideoPoster;
        if (bitmap != null) {
            bitmap.recycle();
            this.mDefaultVideoPoster = null;
        }
    }

    public void showCustomView(View view, WebChromeClient.CustomViewCallback customViewCallback) {
        View view2;
        Log.d(TAG, "showing Custom View");
        if (this.mCustomView != null) {
            customViewCallback.onCustomViewHidden();
            return;
        }
        view.setBackgroundDrawable(CanvasHelper.getDrawable());
        if (DeviceInfo.sModel.equals("HUAWEI MT1-U06") || DeviceInfo.sModel.equals("SM-T310") || DeviceInfo.sModel.equals("vivo Y51A")) {
            this.mAdaWebview.obtainFrameView().obtainApp().registerSysEventListener(new ISysEventListener() {
                public boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
                    ISysEventListener.SysEventType sysEventType2 = ISysEventListener.SysEventType.onKeyUp;
                    if (sysEventType != sysEventType2 || ((Integer) ((Object[]) obj)[0]).intValue() != 4) {
                        return false;
                    }
                    WebJsEvent.this.onHideCustomView();
                    WebJsEvent.this.mAdaWebview.obtainFrameView().obtainApp().unregisterSysEventListener(this, sysEventType2);
                    return true;
                }
            }, ISysEventListener.SysEventType.onKeyUp);
        }
        this.mCustomView = view;
        FrameLayout frameLayout = null;
        int i = 0;
        if (!(view instanceof ViewGroup) || ((ViewGroup) view).getChildCount() <= 0) {
            view2 = null;
        } else {
            view2 = ((ViewGroup) this.mCustomView).getChildAt(0);
            if (view2 instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view2;
                if (viewGroup.getChildCount() > 0) {
                    view2 = viewGroup.getChildAt(0);
                }
            }
        }
        if (view2 != null) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
            layoutParams.setMargins(0, 0, 0, 0);
            layoutParams.gravity = 17;
            view2.setPadding(0, 0, 0, 0);
            view2.setLayoutParams(layoutParams);
            view2.invalidate();
        }
        this.mCustomViewCallback = customViewCallback;
        Activity activity = this.mAdaWebview.obtainApp().getActivity();
        if (!(this.mAdaWebview.obtainFrameView() == null || this.mAdaWebview.obtainFrameView().obtainWindowMgr() == null)) {
            AbsMgr obtainWindowMgr = this.mAdaWebview.obtainFrameView().obtainWindowMgr();
            IMgr.MgrType mgrType = IMgr.MgrType.FeatureMgr;
            Object processEvent = obtainWindowMgr.processEvent(mgrType, 1, new Object[]{this.mAdaWebview, "device", "__isWakelockNative__", new JSONArray()});
            if (!(processEvent instanceof String ? Boolean.valueOf(String.valueOf(processEvent)).booleanValue() : false)) {
                this.isNeedCloseScreenWakelock = true;
                this.mAdaWebview.obtainFrameView().obtainWindowMgr().processEvent(mgrType, 1, new Object[]{this.mAdaWebview, "device", "setWakelock", new JSONArray().put(true)});
            }
        }
        if (activity != null) {
            IActivityHandler iActivityHandler = DCloudAdapterUtil.getIActivityHandler(activity);
            if (iActivityHandler != null) {
                frameLayout = iActivityHandler.obtainActivityContentView();
                iActivityHandler.closeSideBar();
                iActivityHandler.setSideBarVisibility(8);
            } else if (activity instanceof IActivityDelegate) {
                frameLayout = ((IActivityDelegate) activity).obtainActivityContentView();
            }
        }
        if (frameLayout != null) {
            frameLayout.addView(view, COVER_SCREEN_GRAVITY_CENTER);
            this.mAdaWebview.obtainMainView().setVisibility(8);
            setStatusBarVisibility(activity, false);
            String webviewProperty = this.mAdaWebview.getWebviewProperty(AbsoluteConst.JSON_KEY_VIDEO_FULL_SCREEN);
            if ("landscape".equals(webviewProperty)) {
                i = 6;
            } else if (!"landscape-primary".equals(webviewProperty)) {
                if ("landscape-secondary".equals(webviewProperty)) {
                    i = 8;
                } else if ("portrait-primary".equals(webviewProperty)) {
                    i = 1;
                } else if ("portrait-secondary".equals(webviewProperty)) {
                    i = 9;
                } else {
                    i = activity.getRequestedOrientation();
                }
            }
            if (activity.getRequestedOrientation() != i) {
                if (-2 == this.mDefaultScreemOrientation) {
                    this.mDefaultScreemOrientation = activity.getRequestedOrientation();
                }
                this.mScreemOrientationChanged = true;
                AdaWebview.ScreemOrientationChangedNeedLayout = true;
                activity.setRequestedOrientation(i);
            }
        }
    }

    private void openFileChooserLogic(ValueCallback<Uri> valueCallback, ValueCallback<Uri[]> valueCallback2, String str, String str2, WebChromeClient.FileChooserParams fileChooserParams) {
        AdaWebview adaWebview = this.mAdaWebview;
        if (adaWebview != null) {
            final ValueCallback<Uri> valueCallback3 = valueCallback;
            final ValueCallback<Uri[]> valueCallback4 = valueCallback2;
            final String str3 = str;
            final String str4 = str2;
            final WebChromeClient.FileChooserParams fileChooserParams2 = fileChooserParams;
            PermissionUtil.usePermission(adaWebview.getActivity(), "base", PermissionUtil.PMS_STORAGE, 2, new PermissionUtil.Request() {
                public void onDenied(String str) {
                }

                public void onGranted(String str) {
                    WebJsEvent.this.showOpenFileChooser(valueCallback3, valueCallback4, str3, str4, fileChooserParams2);
                }
            });
        }
    }

    public void openFileChooser(ValueCallback<Uri> valueCallback, String str) {
        openFileChooserLogic(valueCallback, str, (String) null);
    }

    public void openFileChooser(ValueCallback<Uri> valueCallback, String str, String str2) {
        openFileChooserLogic(valueCallback, str, str2);
    }
}
