package io.dcloud.sdk.activity;

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
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.webkit.internal.AssetHelper;
import com.alibaba.fastjson.asm.Opcodes;
import io.dcloud.WebviewActivity;
import io.dcloud.base.R;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.sdk.activity.a.a;
import io.dcloud.sdk.core.util.ReflectUtil;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WebViewActivity extends Activity implements View.OnClickListener {
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
    public ArrayList<String> k = new ArrayList<>();
    /* access modifiers changed from: private */
    public boolean l = false;
    /* access modifiers changed from: private */
    public boolean m = false;
    public Map<String, String> n = new a();

    class a extends HashMap<String, String> {
        a() {
            put("X-Requested-With", "");
        }
    }

    class b extends WebChromeClient {
        b() {
        }

        public void onGeolocationPermissionsShowPrompt(String str, GeolocationPermissions.Callback callback) {
            super.onGeolocationPermissionsShowPrompt(str, callback);
            if (WebViewActivity.this.m) {
                callback.invoke(str, false, false);
            } else {
                callback.invoke(str, true, false);
            }
        }

        public void onProgressChanged(WebView webView, int i) {
            super.onProgressChanged(webView, i);
            if (!WebViewActivity.this.l) {
                if (WebViewActivity.this.f.getParent() == null) {
                    WebViewActivity.this.g.addView(WebViewActivity.this.f);
                    WebViewActivity.this.f.c();
                } else if (WebViewActivity.this.i) {
                    boolean unused = WebViewActivity.this.i = false;
                    WebViewActivity.this.f.c();
                }
                WebViewActivity.this.f.setVisibility(0);
                if (WebViewActivity.this.f.b() <= i) {
                    WebViewActivity.this.f.a(i);
                }
            }
        }

        public void onReceivedTitle(WebView webView, String str) {
            super.onReceivedTitle(webView, str);
            if (WebViewActivity.this.c != null && !TextUtils.isEmpty(str) && !str.startsWith("http") && !str.startsWith("https")) {
                WebViewActivity.this.c.setText(str);
            }
        }
    }

    class c extends WebViewClient {

        class a implements DialogInterface.OnClickListener {
            final /* synthetic */ Intent a;

            a(Intent intent) {
                this.a = intent;
            }

            public void onClick(DialogInterface dialogInterface, int i) {
                WebViewActivity.this.startActivity(this.a);
            }
        }

        c() {
        }

        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
            WebViewActivity.this.b.setVisibility(webView.canGoBack() ? 0 : 4);
        }

        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            if (sslErrorHandler != null) {
                WebViewActivity.this.a(sslErrorHandler, "proceed", new Class[0], new Object[0]);
            }
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            Intent intent;
            String str2;
            CharSequence charSequence = "";
            String lowerCase = !TextUtils.isEmpty(str) ? str.toLowerCase(Locale.ENGLISH) : charSequence;
            if (TextUtils.isEmpty(lowerCase) || str.startsWith("streamapp://") || lowerCase.startsWith(DeviceInfo.HTTP_PROTOCOL) || lowerCase.startsWith(DeviceInfo.HTTPS_PROTOCOL) || lowerCase.contains("streamapp://")) {
                webView.loadUrl(str, WebViewActivity.this.n);
                boolean unused = WebViewActivity.this.i = true;
                return true;
            }
            if (!TextUtils.isEmpty(lowerCase)) {
                Iterator<String> it = WebViewActivity.this.k.iterator();
                while (it.hasNext()) {
                    if (lowerCase.startsWith(it.next() + ":")) {
                        try {
                            Intent intent2 = new Intent("android.intent.action.VIEW", Uri.parse(str));
                            intent2.setSelector((Intent) null);
                            intent2.setComponent((ComponentName) null);
                            intent2.addCategory("android.intent.category.BROWSABLE");
                            WebViewActivity.this.startActivity(intent2);
                        } catch (Exception unused2) {
                        }
                        return true;
                    }
                }
            }
            try {
                if (lowerCase.startsWith("intent://")) {
                    intent = Intent.parseUri(str, 1);
                    intent.addCategory("android.intent.category.BROWSABLE");
                    intent.setComponent((ComponentName) null);
                    if (Build.VERSION.SDK_INT >= 15) {
                        intent.setSelector((Intent) null);
                    }
                } else {
                    intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
                }
                PackageManager packageManager = WebViewActivity.this.getPackageManager();
                List<ResolveInfo> queryIntentActivities = packageManager.queryIntentActivities(intent, 0);
                if (queryIntentActivities != null && queryIntentActivities.size() > 0) {
                    CharSequence charSequence2 = charSequence;
                    if (1 == queryIntentActivities.size()) {
                        charSequence2 = queryIntentActivities.get(0).loadLabel(packageManager);
                    }
                    if (!TextUtils.isEmpty(charSequence2)) {
                        str2 = "即将打开\"" + charSequence2 + "\"应用, \n立即打开?";
                    } else {
                        str2 = "即将打开\"Android system\"应用, \n立即打开?";
                    }
                    new AlertDialog.Builder(WebViewActivity.this).setMessage(str2).setPositiveButton("打开", new a(intent)).setNegativeButton("取消", (DialogInterface.OnClickListener) null).create().show();
                }
            } catch (Exception unused3) {
            }
            return true;
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
                    WebViewActivity.this.b();
                } else if (i == 3) {
                    WebViewActivity.this.d();
                } else if (i == 4) {
                    WebViewActivity.this.e();
                }
            } else if (WebViewActivity.this.h != null) {
                boolean unused = WebViewActivity.this.i = true;
                WebViewActivity.this.h.reload();
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
            this.b = (float) WebViewActivity.pxFromDp(2.0f, getResources().getDisplayMetrics());
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

    public static int pxFromDp(float f2, DisplayMetrics displayMetrics) {
        return Math.round(TypedValue.applyDimension(1, f2, displayMetrics));
    }

    public void finish() {
        super.finish();
        getIntent();
    }

    public void onClick(View view) {
        if (view == this.a) {
            if (!a()) {
                finish();
            }
        } else if (view == this.b) {
            finish();
        } else if (view == this.d) {
            c();
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        requestWindowFeature(1);
        super.onCreate(bundle);
        setContentView(R.layout.dcloud_ad_activity_webview);
        f();
        g();
        if (!TextUtils.isEmpty(this.j)) {
            this.h.loadUrl(this.j, this.n);
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
        boolean a2 = a();
        return !a2 ? super.onKeyDown(i2, keyEvent) : a2;
    }

    /* access modifiers changed from: private */
    public void b() {
        ((ClipboardManager) getSystemService("clipboard")).setText(this.h.getUrl());
        Toast.makeText(this, "拷贝到剪切板" + this.h.getUrl(), 1).show();
    }

    private void c() {
        io.dcloud.sdk.activity.a.a aVar;
        if (Build.VERSION.SDK_INT >= 21) {
            aVar = new io.dcloud.sdk.activity.a.a(this);
        } else {
            aVar = new io.dcloud.sdk.activity.a.a(this, 16973837);
        }
        JSONArray jSONArray = new JSONArray();
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(AbsoluteConst.JSON_KEY_TITLE, "刷新");
            jSONArray.put(jSONObject);
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put(AbsoluteConst.JSON_KEY_TITLE, "复制链接");
            jSONArray.put(jSONObject2);
            JSONObject jSONObject3 = new JSONObject();
            jSONObject3.put(AbsoluteConst.JSON_KEY_TITLE, "使用浏览器打开");
            jSONArray.put(jSONObject3);
            JSONObject jSONObject4 = new JSONObject();
            jSONObject4.put(AbsoluteConst.JSON_KEY_TITLE, "分享页面");
            jSONArray.put(jSONObject4);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        aVar.a("取消");
        aVar.a(jSONArray);
        aVar.a((a.b) new d());
        aVar.a(true);
        aVar.i();
    }

    /* access modifiers changed from: private */
    public void d() {
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setData(Uri.parse(this.h.getUrl()));
            startActivity(Intent.createChooser(intent, "打开网页"));
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: private */
    public void e() {
        try {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType(AssetHelper.DEFAULT_MIME_TYPE);
            intent.putExtra("android.intent.extra.SUBJECT", this.h.getTitle());
            intent.putExtra("android.intent.extra.TEXT", this.h.getUrl());
            intent.setFlags(268435456);
            startActivity(Intent.createChooser(intent, "分享"));
        } catch (Exception unused) {
        }
    }

    private void f() {
        if (getIntent() != null) {
            if (getIntent().hasExtra("url")) {
                this.j = getIntent().getStringExtra("url");
            }
            if (getIntent().hasExtra(WebviewActivity.isLocalHtmlParam)) {
                this.l = getIntent().getBooleanExtra(WebviewActivity.isLocalHtmlParam, false);
            }
            if (getIntent().hasExtra(WebviewActivity.noPermissionAllowParam)) {
                this.m = getIntent().getBooleanExtra(WebviewActivity.noPermissionAllowParam, false);
            }
        }
        this.k.add("weixin");
        this.k.add("alipay");
        this.k.add("alipays");
        this.k.add("alipayqr");
    }

    private void g() {
        this.a = (TextView) findViewById(R.id.back);
        this.b = (TextView) findViewById(R.id.close);
        this.c = (TextView) findViewById(R.id.title);
        this.e = (TextView) findViewById(R.id.refresh);
        this.d = (TextView) findViewById(R.id.menu);
        this.g = (FrameLayout) findViewById(R.id.content);
        e eVar = new e(this);
        this.f = eVar;
        if (this.l) {
            eVar.setVisibility(8);
            this.d.setVisibility(4);
        }
        WebView webView = (WebView) findViewById(R.id.webview);
        this.h = webView;
        a(webView);
        int pxFromDp = pxFromDp(23.0f, getResources().getDisplayMetrics());
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

    private void h() {
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
            ReflectUtil.invokeMethod(settings, io.dcloud.h.c.c.a.e.d.c("e218SWRkZ39OYWRtSWtrbXt7"), clsArr, objArr);
            settings.setAppCacheEnabled(true);
            settings.setSavePassword(false);
            settings.setJavaScriptEnabled(true);
            webView.setFocusable(true);
            webView.removeJavascriptInterface("searchBoxJavaBridge_");
            webView.removeJavascriptInterface("accessibilityTraversal");
            webView.removeJavascriptInterface("accessibility");
            webView.setWebChromeClient(new b());
            webView.setWebViewClient(new c());
        }
        h();
    }

    private boolean a() {
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

    public Object a(Object obj, String str, Class<?>[] clsArr, Object... objArr) {
        Method method;
        if (obj == null) {
            return null;
        }
        try {
            Class<?> cls = obj.getClass();
            if (Build.VERSION.SDK_INT > 10) {
                method = cls.getMethod(str, clsArr);
            } else {
                method = cls.getDeclaredMethod(str, clsArr);
            }
            method.setAccessible(true);
            if (objArr.length == 0) {
                objArr = null;
            }
            return method.invoke(obj, objArr);
        } catch (Throwable unused) {
            return null;
        }
    }
}
