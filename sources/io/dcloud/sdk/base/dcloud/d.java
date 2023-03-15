package io.dcloud.sdk.base.dcloud;

import android.app.Activity;
import android.content.Context;
import android.net.http.SslError;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import com.nostra13.dcloudimageloader.core.download.BaseImageDownloader;
import io.dcloud.sdk.core.util.ReflectUtil;
import java.util.Locale;

public class d {
    WebView a;
    ViewGroup b;

    class a extends WebViewClient {

        /* renamed from: io.dcloud.sdk.base.dcloud.d$a$a  reason: collision with other inner class name */
        class C0079a implements Runnable {
            final /* synthetic */ String a;

            C0079a(String str) {
                this.a = str;
            }

            public void run() {
                if (d.this.a != null) {
                    ADHandler.a("shutao", "onPageFinished-remove--url=" + this.a);
                    d dVar = d.this;
                    ViewGroup viewGroup = dVar.b;
                    if (viewGroup != null) {
                        viewGroup.removeView(dVar.a);
                        d.this.a = null;
                    }
                }
            }
        }

        a() {
        }

        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
            ADHandler.a("shutao", "onPageFinished---url=" + str);
            d.this.b.postDelayed(new C0079a(str), (long) c.a((int) BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT, (int) BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT));
        }

        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            if (sslErrorHandler != null) {
                ReflectUtil.invokeMethod(sslErrorHandler, "proceed", new Class[0], new Object[0]);
            }
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            return TextUtils.isEmpty(str) || !str.toLowerCase(Locale.ENGLISH).startsWith("http");
        }
    }

    class b implements DownloadListener {
        b() {
        }

        public void onDownloadStart(String str, String str2, String str3, String str4, long j) {
        }
    }

    public d(Context context) {
        if (context instanceof Activity) {
            this.b = (ViewGroup) ((Activity) context).getWindow().getDecorView();
        }
        WebView webView = new WebView(context);
        this.a = webView;
        webView.setVisibility(4);
        this.b.addView(this.a, new FrameLayout.LayoutParams(-1, -1));
        WebSettings settings = this.a.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheMaxSize(8388608);
        settings.setAppCachePath(context.getApplicationContext().getCacheDir().getAbsolutePath());
        settings.setAllowFileAccess(false);
        ReflectUtil.invokeMethod(settings, io.dcloud.h.c.c.a.e.d.c("e218SWRkZ39OYWRtSWtrbXt7"), new Class[]{Boolean.TYPE}, Boolean.TRUE);
        settings.setAppCacheEnabled(true);
        settings.setSavePassword(false);
        this.a.removeJavascriptInterface("searchBoxJavaBridge_");
        this.a.removeJavascriptInterface("accessibilityTraversal");
        this.a.removeJavascriptInterface("accessibility");
        this.a.setWebViewClient(new a());
        a();
        this.a.setDownloadListener(new b());
    }

    private void a() {
    }

    public void a(String str) {
        WebView webView = this.a;
        if (webView != null) {
            webView.loadUrl(str);
        }
    }
}
