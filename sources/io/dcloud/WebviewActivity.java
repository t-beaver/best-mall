package io.dcloud;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.webkit.internal.AssetHelper;
import com.alibaba.fastjson.asm.Opcodes;
import com.dcloud.android.widget.toast.ToastCompat;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ILoadCallBack;
import io.dcloud.common.adapter.ui.webview.WebViewFactory;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.DownloadUtil;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.DialogUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.LoadAppUtils;
import io.dcloud.common.util.NotificationUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.feature.ui.nativeui.a;
import io.src.dcloud.adapter.DCloudBaseActivity;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WebviewActivity extends DCloudBaseActivity implements View.OnClickListener {
    public static final String isLocalHtmlParam = "isLocalHtml";
    public static final String noPermissionAllowParam = "isNoPermissionAllowParam";
    private TextView a;
    /* access modifiers changed from: private */
    public TextView b;
    /* access modifiers changed from: private */
    public TextView c;
    private TextView d;
    private TextView e;
    /* access modifiers changed from: private */
    public e f;
    /* access modifiers changed from: private */
    public FrameLayout g;
    /* access modifiers changed from: private */
    public WebView h;
    /* access modifiers changed from: private */
    public boolean i;
    private String j = null;
    /* access modifiers changed from: private */
    public boolean k = false;
    /* access modifiers changed from: private */
    public boolean l = false;
    public ArrayList<String> mAppStreamSchemeWhiteDefaultList = new ArrayList<>();

    class a extends WebChromeClient {
        a() {
        }

        public void onGeolocationPermissionsShowPrompt(String str, GeolocationPermissions.Callback callback) {
            super.onGeolocationPermissionsShowPrompt(str, callback);
            if (WebviewActivity.this.l) {
                callback.invoke(str, false, false);
            } else {
                callback.invoke(str, true, false);
            }
        }

        public void onProgressChanged(WebView webView, int i) {
            super.onProgressChanged(webView, i);
            if (!WebviewActivity.this.k) {
                if (WebviewActivity.this.f.getParent() == null) {
                    WebviewActivity.this.g.addView(WebviewActivity.this.f);
                    WebviewActivity.this.f.c();
                } else if (WebviewActivity.this.i) {
                    boolean unused = WebviewActivity.this.i = false;
                    WebviewActivity.this.f.c();
                }
                WebviewActivity.this.f.setVisibility(0);
                if (WebviewActivity.this.f.b() <= i) {
                    WebviewActivity.this.f.a(i);
                }
            }
        }

        public void onReceivedTitle(WebView webView, String str) {
            super.onReceivedTitle(webView, str);
            if (WebviewActivity.this.c != null && !TextUtils.isEmpty(str) && !str.startsWith("http") && !str.startsWith("https")) {
                WebviewActivity.this.c.setText(str);
            }
        }
    }

    class b extends WebViewClient {

        class a implements View.OnClickListener {
            final /* synthetic */ Intent a;

            a(Intent intent) {
                this.a = intent;
            }

            public void onClick(View view) {
                WebviewActivity.this.startActivity(this.a);
            }
        }

        /* renamed from: io.dcloud.WebviewActivity$b$b  reason: collision with other inner class name */
        class C0009b implements DialogInterface.OnClickListener {
            final /* synthetic */ AlertDialog a;
            final /* synthetic */ SslError b;
            final /* synthetic */ SslErrorHandler c;

            C0009b(AlertDialog alertDialog, SslError sslError, SslErrorHandler sslErrorHandler) {
                this.a = alertDialog;
                this.b = sslError;
                this.c = sslErrorHandler;
            }

            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == -2) {
                    this.a.cancel();
                    this.a.dismiss();
                } else if (i == -3) {
                    this.b.getCertificate().getIssuedBy();
                } else if (i == -1) {
                    WebViewFactory.setSslHandlerState(this.c, 1);
                    this.a.dismiss();
                }
            }
        }

        b() {
        }

        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
            WebviewActivity.this.b.setVisibility(webView.canGoBack() ? 0 : 4);
        }

        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            if (sslErrorHandler != null) {
                String str = BaseInfo.untrustedca;
                if (PdrUtil.isEquals(str, "refuse")) {
                    sslErrorHandler.cancel();
                } else if (PdrUtil.isEquals(str, "warning")) {
                    AlertDialog create = new AlertDialog.Builder(webView.getContext()).create();
                    create.setIcon(17301601);
                    create.setTitle(WebviewActivity.this.getString(R.string.dcloud_common_safety_warning));
                    create.setCanceledOnTouchOutside(false);
                    String str2 = null;
                    if (Build.VERSION.SDK_INT >= 14) {
                        str2 = sslError.getUrl();
                    }
                    String string = WebviewActivity.this.getString(R.string.dcloud_common_certificate_continue);
                    if (!TextUtils.isEmpty(str2)) {
                        string = str2 + "\n" + string;
                    }
                    create.setMessage(string);
                    C0009b bVar = new C0009b(create, sslError, sslErrorHandler);
                    create.setButton(-2, WebviewActivity.this.getString(17039360), bVar);
                    create.setButton(-1, WebviewActivity.this.getString(17039370), bVar);
                    create.show();
                } else {
                    WebViewFactory.setSslHandlerState(sslErrorHandler, 1);
                }
            }
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            Intent intent;
            String str2 = str;
            CharSequence charSequence = "";
            String lowerCase = !TextUtils.isEmpty(str) ? str2.toLowerCase(Locale.ENGLISH) : charSequence;
            if (TextUtils.isEmpty(lowerCase) || str2.startsWith("streamapp://") || lowerCase.startsWith(DeviceInfo.HTTP_PROTOCOL) || lowerCase.startsWith(DeviceInfo.HTTPS_PROTOCOL) || lowerCase.contains("streamapp://")) {
                webView.loadUrl(str);
                boolean unused = WebviewActivity.this.i = true;
                return true;
            }
            if (!TextUtils.isEmpty(lowerCase)) {
                Iterator<String> it = WebviewActivity.this.mAppStreamSchemeWhiteDefaultList.iterator();
                while (it.hasNext()) {
                    if (lowerCase.startsWith(it.next() + ":")) {
                        try {
                            Intent intent2 = new Intent("android.intent.action.VIEW", Uri.parse(str));
                            if (BaseInfo.isDefense) {
                                intent2.setSelector((Intent) null);
                                intent2.setComponent((ComponentName) null);
                                intent2.addCategory("android.intent.category.BROWSABLE");
                            }
                            WebviewActivity.this.startActivity(intent2);
                        } catch (Exception unused2) {
                        }
                        return true;
                    }
                }
            }
            try {
                if (lowerCase.startsWith("intent://")) {
                    intent = Intent.parseUri(str2, 1);
                    intent.addCategory("android.intent.category.BROWSABLE");
                    intent.setComponent((ComponentName) null);
                    if (Build.VERSION.SDK_INT >= 15) {
                        intent.setSelector((Intent) null);
                    }
                } else {
                    intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
                }
                PackageManager packageManager = WebviewActivity.this.getPackageManager();
                List<ResolveInfo> queryIntentActivities = packageManager.queryIntentActivities(intent, 0);
                if (queryIntentActivities != null && queryIntentActivities.size() > 0) {
                    String str3 = WebviewActivity.this.getString(R.string.dcloud_common_soon_open) + "\"Android system\"" + WebviewActivity.this.getString(R.string.dcloud_common_app_open_now);
                    CharSequence charSequence2 = charSequence;
                    if (1 == queryIntentActivities.size()) {
                        charSequence2 = queryIntentActivities.get(0).loadLabel(packageManager);
                    }
                    if (!TextUtils.isEmpty(charSequence2)) {
                        str3 = WebviewActivity.this.getString(R.string.dcloud_common_soon_open) + JSUtil.QUOTE + charSequence2 + JSUtil.QUOTE + WebviewActivity.this.getString(R.string.dcloud_common_app_open_now);
                    }
                    String str4 = str3;
                    WebviewActivity webviewActivity = WebviewActivity.this;
                    Activity activity = webviewActivity.that;
                    String string = webviewActivity.getString(R.string.dcloud_common_open);
                    String string2 = WebviewActivity.this.getString(R.string.dcloud_common_cancel);
                    a aVar = new a(intent);
                    double d = (double) WebviewActivity.this.getResources().getDisplayMetrics().widthPixels;
                    Double.isNaN(d);
                    DialogUtil.showAlertDialog(activity, str4, string, string2, aVar, (View.OnClickListener) null, (DialogInterface.OnCancelListener) null, (DialogInterface.OnDismissListener) null, false, 1, 0, (int) (d * 0.9d));
                }
            } catch (Exception unused3) {
            }
            return true;
        }
    }

    class c implements DownloadListener {

        class a implements View.OnClickListener {
            final /* synthetic */ String a;
            final /* synthetic */ String b;
            final /* synthetic */ String c;
            final /* synthetic */ String d;

            /* renamed from: io.dcloud.WebviewActivity$c$a$a  reason: collision with other inner class name */
            class C0010a implements ILoadCallBack {
                C0010a() {
                }

                public Object onCallBack(int i, Context context, Object obj) {
                    if (obj == null && i == -1 && context == null) {
                        Intent intent = new Intent();
                        a aVar = a.this;
                        WebviewActivity webviewActivity = WebviewActivity.this;
                        NotificationUtil.showNotification(webviewActivity.that, aVar.d, webviewActivity.getString(R.string.dcloud_common_download_failed), intent, -1, -1, intent.hashCode(), true);
                        return null;
                    }
                    String valueOf = String.valueOf(obj);
                    String mimeType = PdrUtil.getMimeType(valueOf);
                    if (valueOf.startsWith(DeviceInfo.FILE_PROTOCOL)) {
                        valueOf = valueOf.substring(7);
                    }
                    if (valueOf.startsWith("content://")) {
                        valueOf = PlatformUtil.getFilePathFromContentUri(Uri.parse(valueOf), WebviewActivity.this.that.getContentResolver());
                        mimeType = PdrUtil.getMimeType(valueOf);
                    }
                    Intent dataAndTypeIntent = LoadAppUtils.getDataAndTypeIntent(context, valueOf, mimeType);
                    a aVar2 = a.this;
                    WebviewActivity webviewActivity2 = WebviewActivity.this;
                    NotificationUtil.showNotification(webviewActivity2.that, aVar2.d, webviewActivity2.getString(R.string.dcloud_common_download_complete), dataAndTypeIntent, PdrR.DRAWABLE_DCLOUD_WEBVIEW_DOWNLOAD_PIN_AROUND, PdrR.DRAWABLE_DCLOUD_WEBVIEW_DOWNLOAD_PIN, 1, true);
                    return null;
                }
            }

            a(String str, String str2, String str3, String str4) {
                this.a = str;
                this.b = str2;
                this.c = str3;
                this.d = str4;
            }

            public void onClick(View view) {
                DownloadUtil.getInstance(WebviewActivity.this.that).startRequest(WebviewActivity.this.that, this.a, this.b, this.c, this.d, new C0010a());
            }
        }

        c() {
        }

        /* JADX WARNING: Can't wrap try/catch for region: R(3:17|18|24) */
        /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
            r0 = new android.content.Intent("android.intent.action.VIEW");
            r0.setData(android.net.Uri.parse(r23));
            r7.a.startActivity(r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x00f2, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x00f3, code lost:
            r0.printStackTrace();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x00e0 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onDownloadStart(java.lang.String r23, java.lang.String r24, java.lang.String r25, java.lang.String r26, long r27) {
            /*
                r22 = this;
                r7 = r22
                r0 = r27
                java.lang.String r8 = "android.intent.action.VIEW"
                int r2 = io.dcloud.common.adapter.util.DeviceInfo.sDeviceSdkVer     // Catch:{ Exception -> 0x00de }
                r3 = 8
                if (r2 <= r3) goto L_0x00ca
                java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00de }
                r2.<init>()     // Catch:{ Exception -> 0x00de }
                java.io.File r3 = android.os.Environment.getExternalStorageDirectory()     // Catch:{ Exception -> 0x00de }
                java.lang.String r3 = r3.getPath()     // Catch:{ Exception -> 0x00de }
                r2.append(r3)     // Catch:{ Exception -> 0x00de }
                java.lang.String r3 = java.io.File.separator     // Catch:{ Exception -> 0x00de }
                r2.append(r3)     // Catch:{ Exception -> 0x00de }
                java.lang.String r3 = android.os.Environment.DIRECTORY_DOWNLOADS     // Catch:{ Exception -> 0x00de }
                r2.append(r3)     // Catch:{ Exception -> 0x00de }
                java.lang.String r5 = r2.toString()     // Catch:{ Exception -> 0x00de }
                r9 = r23
                r2 = r25
                r4 = r26
                java.lang.String r6 = io.dcloud.common.util.PdrUtil.getDownloadFilename(r2, r4, r9)     // Catch:{ Exception -> 0x00e0 }
                java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00e0 }
                r2.<init>()     // Catch:{ Exception -> 0x00e0 }
                io.dcloud.WebviewActivity r3 = io.dcloud.WebviewActivity.this     // Catch:{ Exception -> 0x00e0 }
                int r10 = io.dcloud.base.R.string.dcloud_common_download_do_file     // Catch:{ Exception -> 0x00e0 }
                java.lang.String r3 = r3.getString(r10)     // Catch:{ Exception -> 0x00e0 }
                r2.append(r3)     // Catch:{ Exception -> 0x00e0 }
                r2.append(r6)     // Catch:{ Exception -> 0x00e0 }
                java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x00e0 }
                r10 = 0
                int r3 = (r10 > r0 ? 1 : (r10 == r0 ? 0 : -1))
                if (r3 >= 0) goto L_0x0082
                java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00e0 }
                r3.<init>()     // Catch:{ Exception -> 0x00e0 }
                r3.append(r2)     // Catch:{ Exception -> 0x00e0 }
                java.lang.String r2 = "【"
                r3.append(r2)     // Catch:{ Exception -> 0x00e0 }
                java.math.BigDecimal r2 = new java.math.BigDecimal     // Catch:{ Exception -> 0x00e0 }
                r2.<init>(r0)     // Catch:{ Exception -> 0x00e0 }
                java.math.BigDecimal r0 = new java.math.BigDecimal     // Catch:{ Exception -> 0x00e0 }
                r10 = 1048576(0x100000, double:5.180654E-318)
                r0.<init>(r10)     // Catch:{ Exception -> 0x00e0 }
                r1 = 2
                r10 = 4
                java.math.BigDecimal r0 = r2.divide(r0, r1, r10)     // Catch:{ Exception -> 0x00e0 }
                float r0 = r0.floatValue()     // Catch:{ Exception -> 0x00e0 }
                r3.append(r0)     // Catch:{ Exception -> 0x00e0 }
                java.lang.String r0 = "MB】"
                r3.append(r0)     // Catch:{ Exception -> 0x00e0 }
                java.lang.String r2 = r3.toString()     // Catch:{ Exception -> 0x00e0 }
            L_0x0082:
                r11 = r2
                io.dcloud.WebviewActivity r0 = io.dcloud.WebviewActivity.this     // Catch:{ Exception -> 0x00e0 }
                android.app.Activity r10 = r0.that     // Catch:{ Exception -> 0x00e0 }
                int r1 = io.dcloud.base.R.string.dcloud_common_download     // Catch:{ Exception -> 0x00e0 }
                java.lang.String r12 = r0.getString(r1)     // Catch:{ Exception -> 0x00e0 }
                io.dcloud.WebviewActivity r0 = io.dcloud.WebviewActivity.this     // Catch:{ Exception -> 0x00e0 }
                int r1 = io.dcloud.base.R.string.dcloud_common_cancel     // Catch:{ Exception -> 0x00e0 }
                java.lang.String r13 = r0.getString(r1)     // Catch:{ Exception -> 0x00e0 }
                io.dcloud.WebviewActivity$c$a r14 = new io.dcloud.WebviewActivity$c$a     // Catch:{ Exception -> 0x00e0 }
                r1 = r14
                r2 = r22
                r3 = r23
                r4 = r26
                r1.<init>(r3, r4, r5, r6)     // Catch:{ Exception -> 0x00e0 }
                r15 = 0
                r16 = 0
                r17 = 0
                r18 = 0
                r19 = 0
                r20 = 80
                io.dcloud.WebviewActivity r0 = io.dcloud.WebviewActivity.this     // Catch:{ Exception -> 0x00e0 }
                android.content.res.Resources r0 = r0.getResources()     // Catch:{ Exception -> 0x00e0 }
                android.util.DisplayMetrics r0 = r0.getDisplayMetrics()     // Catch:{ Exception -> 0x00e0 }
                int r0 = r0.widthPixels     // Catch:{ Exception -> 0x00e0 }
                double r0 = (double) r0
                r2 = 4606281698874543309(0x3feccccccccccccd, double:0.9)
                java.lang.Double.isNaN(r0)
                double r0 = r0 * r2
                int r0 = (int) r0
                r21 = r0
                io.dcloud.common.util.DialogUtil.showAlertDialog(r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21)     // Catch:{ Exception -> 0x00e0 }
                goto L_0x00f6
            L_0x00ca:
                r9 = r23
                android.content.Intent r0 = new android.content.Intent     // Catch:{ Exception -> 0x00e0 }
                r0.<init>(r8)     // Catch:{ Exception -> 0x00e0 }
                android.net.Uri r1 = android.net.Uri.parse(r23)     // Catch:{ Exception -> 0x00e0 }
                r0.setData(r1)     // Catch:{ Exception -> 0x00e0 }
                io.dcloud.WebviewActivity r1 = io.dcloud.WebviewActivity.this     // Catch:{ Exception -> 0x00e0 }
                r1.startActivity(r0)     // Catch:{ Exception -> 0x00e0 }
                goto L_0x00f6
            L_0x00de:
                r9 = r23
            L_0x00e0:
                android.content.Intent r0 = new android.content.Intent     // Catch:{ Exception -> 0x00f2 }
                r0.<init>(r8)     // Catch:{ Exception -> 0x00f2 }
                android.net.Uri r1 = android.net.Uri.parse(r23)     // Catch:{ Exception -> 0x00f2 }
                r0.setData(r1)     // Catch:{ Exception -> 0x00f2 }
                io.dcloud.WebviewActivity r1 = io.dcloud.WebviewActivity.this     // Catch:{ Exception -> 0x00f2 }
                r1.startActivity(r0)     // Catch:{ Exception -> 0x00f2 }
                goto L_0x00f6
            L_0x00f2:
                r0 = move-exception
                r0.printStackTrace()
            L_0x00f6:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.WebviewActivity.c.onDownloadStart(java.lang.String, java.lang.String, java.lang.String, java.lang.String, long):void");
        }
    }

    class d implements a.b {
        d() {
        }

        public void initCancelText(TextView textView) {
        }

        public void initTextItem(int i, TextView textView, String str) {
        }

        public boolean onDismiss(int i) {
            return false;
        }

        public void onItemClick(int i) {
            if (i != 1) {
                if (i == 2) {
                    WebviewActivity.this.c();
                } else if (i == 3) {
                    WebviewActivity.this.e();
                } else if (i == 4) {
                    WebviewActivity.this.f();
                }
            } else if (WebviewActivity.this.h != null) {
                boolean unused = WebviewActivity.this.i = true;
                WebviewActivity.this.h.reload();
            }
        }
    }

    static class e extends View {
        int a;
        float b;
        int c = 0;
        Paint d = new Paint();
        int e = 0;
        int f = 0;
        int g = 255;
        int h;

        class a implements Runnable {
            a() {
            }

            public void run() {
                e eVar = e.this;
                int i = eVar.g - 5;
                eVar.g = i;
                if (i > 0) {
                    eVar.postDelayed(this, 5);
                } else {
                    ViewGroup viewGroup = (ViewGroup) eVar.getParent();
                    if (viewGroup != null) {
                        viewGroup.removeView(e.this);
                    }
                }
                e.this.invalidate();
            }
        }

        class b implements Runnable {
            b() {
            }

            public void run() {
                e eVar = e.this;
                int i = eVar.f;
                int i2 = eVar.e;
                int i3 = 10;
                int i4 = (i - i2) / 10;
                if (i4 <= 10) {
                    i3 = i4 < 1 ? 1 : i4;
                }
                int i5 = i2 + i3;
                eVar.e = i5;
                if (i > i5) {
                    eVar.postDelayed(this, 5);
                } else if (i >= eVar.a) {
                    eVar.a();
                }
                e.this.invalidate();
            }
        }

        e(Context context) {
            super(context);
            this.a = context.getResources().getDisplayMetrics().widthPixels;
            this.b = (float) PdrUtil.pxFromDp(2.0f, getResources().getDisplayMetrics());
        }

        /* access modifiers changed from: package-private */
        public void a() {
            postDelayed(new a(), 50);
        }

        public int b() {
            return this.h;
        }

        public void c() {
            this.g = 255;
            this.e = 0;
            this.f = 0;
            this.h = 0;
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            this.d.setColor(Color.argb(this.g, 0, Opcodes.IFEQ, 68));
            float f2 = (float) this.c;
            canvas.drawRect(0.0f, f2, (float) this.e, f2 + this.b, this.d);
        }

        /* access modifiers changed from: protected */
        public void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            setMeasuredDimension(this.a, this.c + ((int) this.b));
        }

        /* access modifiers changed from: package-private */
        public void a(int i) {
            this.h = i;
            int i2 = (this.a * i) / 100;
            if (this.e >= this.f) {
                postDelayed(new b(), 5);
            }
            this.f = i2;
        }
    }

    public void finish() {
        super.finish();
        Intent intent = getIntent();
        if (intent == null || !"POP".equals(intent.getStringExtra("ANIM"))) {
            overridePendingTransition(0, PdrR.ANIM_DCLOUD_SLIDE_OUT_TO_RIGHT);
        } else {
            overridePendingTransition(0, R.anim.dcloud_pop_out);
        }
    }

    public void onClick(View view) {
        if (view == this.a) {
            if (!b()) {
                finish();
            }
        } else if (view == this.b) {
            finish();
        } else if (view == this.d) {
            d();
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        setImmersive(this);
        super.onCreate(bundle);
        setContentView(PdrR.WEBVIEW_ACTIVITY_LAYOUT);
        g();
        h();
        if (!TextUtils.isEmpty(this.j)) {
            this.h.loadUrl(this.j);
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        try {
            WebView webView = this.h;
            if (webView != null) {
                if (webView.getParent() != null) {
                    ((ViewGroup) this.h.getParent()).removeView(this.h);
                }
                this.h.clearHistory();
                this.h.clearCache(true);
                this.h.destroy();
            }
            this.h = null;
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        super.onDestroy();
    }

    public boolean onKeyDown(int i2, KeyEvent keyEvent) {
        if (4 != i2) {
            return super.onKeyDown(i2, keyEvent);
        }
        boolean b2 = b();
        return !b2 ? super.onKeyDown(i2, keyEvent) : b2;
    }

    public void setImmersive(Activity activity) {
        int i2;
        if (activity != null && (i2 = Build.VERSION.SDK_INT) >= 21) {
            Window window = activity.getWindow();
            int systemUiVisibility = window.getDecorView().getSystemUiVisibility() | 1280;
            window.setStatusBarColor(0);
            if (i2 >= 23) {
                int i3 = 8192;
                try {
                    Class<?> cls = Class.forName("android.view.View");
                    i3 = cls.getField("SYSTEM_UI_FLAG_LIGHT_STATUS_BAR").getInt(cls);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                systemUiVisibility |= i3;
            }
            window.getDecorView().setSystemUiVisibility(systemUiVisibility);
        }
    }

    private boolean b() {
        WebView webView = this.h;
        if (webView == null) {
            return false;
        }
        if (webView.canGoBack()) {
            this.i = true;
            this.h.goBack();
            this.c.setText(this.h.getTitle());
            return true;
        } else if (this.h.canGoBack()) {
            return false;
        } else {
            finish();
            return true;
        }
    }

    /* access modifiers changed from: private */
    public void c() {
        ((ClipboardManager) getSystemService("clipboard")).setText(this.h.getUrl());
        ToastCompat.makeText((Context) this.that, (CharSequence) getString(R.string.dcloud_common_copy_clipboard) + this.h.getUrl(), 1).show();
    }

    private void d() {
        io.dcloud.feature.ui.nativeui.a aVar;
        setTheme(PdrR.WEBVIEW_ACTIVITY_LAYOUT_ACTS_STYLE_ActionSheetStyleIOS7);
        if (Build.VERSION.SDK_INT >= 21) {
            aVar = new io.dcloud.feature.ui.nativeui.a(this.that);
        } else {
            aVar = new io.dcloud.feature.ui.nativeui.a(this.that, 16973837);
        }
        JSONArray jSONArray = new JSONArray();
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(AbsoluteConst.JSON_KEY_TITLE, getString(R.string.dcloud_common_refresh));
            jSONArray.put(jSONObject);
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put(AbsoluteConst.JSON_KEY_TITLE, getString(R.string.dcloud_common_copy_link));
            jSONArray.put(jSONObject2);
            JSONObject jSONObject3 = new JSONObject();
            jSONObject3.put(AbsoluteConst.JSON_KEY_TITLE, getString(R.string.dcloud_common_open_browser));
            jSONArray.put(jSONObject3);
            JSONObject jSONObject4 = new JSONObject();
            jSONObject4.put(AbsoluteConst.JSON_KEY_TITLE, getString(R.string.dcloud_common_share_page));
            jSONArray.put(jSONObject4);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        aVar.b(getString(R.string.dcloud_common_cancel));
        aVar.a(jSONArray);
        aVar.a((a.b) new d());
        aVar.a(true);
        aVar.j();
    }

    /* access modifiers changed from: private */
    public void e() {
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setData(Uri.parse(this.h.getUrl()));
            startActivity(Intent.createChooser(intent, getString(R.string.dcloud_common_open_web)));
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: private */
    public void f() {
        try {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType(AssetHelper.DEFAULT_MIME_TYPE);
            intent.putExtra("android.intent.extra.SUBJECT", this.h.getTitle());
            intent.putExtra("android.intent.extra.TEXT", this.h.getUrl());
            intent.setFlags(268435456);
            startActivity(Intent.createChooser(intent, getString(R.string.dcloud_common_share)));
        } catch (Exception unused) {
        }
    }

    private void g() {
        if (getIntent() != null) {
            if (getIntent().hasExtra("url")) {
                this.j = getIntent().getStringExtra("url");
            }
            if (getIntent().hasExtra(isLocalHtmlParam)) {
                this.k = getIntent().getBooleanExtra(isLocalHtmlParam, false);
            }
            if (getIntent().hasExtra(noPermissionAllowParam)) {
                this.l = getIntent().getBooleanExtra(noPermissionAllowParam, false);
            }
        }
        this.mAppStreamSchemeWhiteDefaultList.add("weixin");
        this.mAppStreamSchemeWhiteDefaultList.add("alipay");
        this.mAppStreamSchemeWhiteDefaultList.add("alipays");
        this.mAppStreamSchemeWhiteDefaultList.add("alipayqr");
    }

    private void h() {
        findViewById(R.id.status_bar_view).setLayoutParams(new LinearLayout.LayoutParams(-1, DeviceInfo.getStatusHeight(this)));
        this.a = (TextView) findViewById(PdrR.WEBVIEW_ACTIVITY_LAYOUT_BACK);
        this.b = (TextView) findViewById(PdrR.WEBVIEW_ACTIVITY_LAYOUT_CLOSE);
        this.c = (TextView) findViewById(PdrR.WEBVIEW_ACTIVITY_LAYOUT_TITLE);
        this.e = (TextView) findViewById(PdrR.WEBVIEW_ACTIVITY_LAYOUT_REFRESH);
        this.d = (TextView) findViewById(PdrR.WEBVIEW_ACTIVITY_LAYOUT_MENU);
        this.g = (FrameLayout) findViewById(PdrR.WEBVIEW_ACTIVITY_LAYOUT_CONTENT);
        e eVar = new e(this.that);
        this.f = eVar;
        if (this.k) {
            eVar.setVisibility(8);
            this.d.setVisibility(4);
        }
        WebView webView = (WebView) findViewById(PdrR.WEBVIEW_ACTIVITY_LAYOUT_WEBVIEW);
        this.h = webView;
        a(webView);
        int pxFromDp = PdrUtil.pxFromDp(23.0f, getResources().getDisplayMetrics());
        Typeface createFromAsset = Typeface.createFromAsset(getAssets(), "fonts/dcloud_iconfont.ttf");
        this.a.setText("");
        this.a.setTypeface(createFromAsset);
        float f2 = (float) pxFromDp;
        this.a.getPaint().setTextSize(f2);
        this.b.setText("");
        this.b.setTypeface(createFromAsset);
        this.b.getPaint().setTextSize(f2);
        this.b.setVisibility(4);
        this.e.setText("");
        this.e.setTypeface(createFromAsset);
        this.e.getPaint().setTextSize(f2);
        this.d.setText("");
        this.d.setTypeface(createFromAsset);
        this.d.getPaint().setTextSize(f2);
        this.a.setOnClickListener(this);
        this.b.setOnClickListener(this);
        this.d.setOnClickListener(this);
    }

    private void i() {
        try {
            int i2 = Build.VERSION.SDK_INT;
            if (i2 >= 11 && i2 < 17) {
                Method method = getClass().getMethod("removeJavascriptInterface", new Class[]{String.class});
                WebView webView = this.h;
                String[] strArr = {"searchBoxJavaBridge_", "accessibility", "ccessibilityaversal"};
                for (int i3 = 0; i3 < 3; i3++) {
                    method.invoke(webView, new Object[]{strArr[i3]});
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void a(WebView webView) {
        if (webView != null) {
            WebSettings settings = webView.getSettings();
            settings.setDomStorageEnabled(true);
            settings.setAppCacheMaxSize(8388608);
            settings.setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
            Class[] clsArr = {Boolean.TYPE};
            Object[] objArr = {Boolean.TRUE};
            settings.setAllowFileAccess(false);
            PlatformUtil.invokeMethod(settings, io.dcloud.f.a.a("f2l4TWBgY3tKZWBpTW9vaX9/KjZhM2Q4OGZhLTRiYTAtNDc5Zi05NDIyLWU1YWFiZTE1ODk3Yjc2", true, 12), clsArr, objArr);
            settings.setAppCacheEnabled(true);
            settings.setSavePassword(false);
            WebViewFactory.openJSEnabled(settings, (IApp) null);
            webView.setFocusable(true);
            webView.removeJavascriptInterface("searchBoxJavaBridge_");
            webView.removeJavascriptInterface("accessibilityTraversal");
            webView.removeJavascriptInterface("accessibility");
            webView.setWebChromeClient(new a());
            webView.setWebViewClient(new b());
        }
        i();
        if (webView != null) {
            webView.setDownloadListener(new c());
        }
    }
}
