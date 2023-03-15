package io.dcloud.feature.gg.dcloud;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import com.nostra13.dcloudimageloader.core.download.BaseImageDownloader;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.adapter.ui.webview.WebViewFactory;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.PdrUtil;
import java.lang.reflect.Method;
import java.util.Locale;

public class ADWebView {
    ViewGroup mRootView;
    WebView mWebView;

    public ADWebView(Context context) {
        if (context instanceof Activity) {
            this.mRootView = (ViewGroup) ((Activity) context).getWindow().getDecorView();
        } else {
            this.mRootView = (ViewGroup) ((Activity) DeviceInfo.sApplicationContext).getWindow().getDecorView();
        }
        WebView webView = new WebView(context);
        this.mWebView = webView;
        webView.setVisibility(4);
        this.mRootView.addView(this.mWebView, new FrameLayout.LayoutParams(-1, -1));
        WebSettings settings = this.mWebView.getSettings();
        WebViewFactory.openJSEnabled(settings, (IApp) null);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheMaxSize(8388608);
        settings.setAppCachePath(context.getApplicationContext().getCacheDir().getAbsolutePath());
        settings.setAllowFileAccess(false);
        WebViewFactory.setFileAccess(settings, true);
        settings.setAppCacheEnabled(true);
        settings.setSavePassword(false);
        this.mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
        this.mWebView.removeJavascriptInterface("accessibilityTraversal");
        this.mWebView.removeJavascriptInterface("accessibility");
        this.mWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView webView, final String str) {
                super.onPageFinished(webView, str);
                ADHandler.log("shutao", "onPageFinished---url=" + str);
                ADWebView.this.mRootView.postDelayed(new Runnable() {
                    public void run() {
                        if (ADWebView.this.mWebView != null) {
                            ADHandler.log("shutao", "onPageFinished-remove--url=" + str);
                            ADWebView aDWebView = ADWebView.this;
                            ViewGroup viewGroup = aDWebView.mRootView;
                            if (viewGroup != null) {
                                viewGroup.removeView(aDWebView.mWebView);
                                ADWebView.this.mWebView = null;
                            }
                        }
                    }
                }, (long) ADSim.getRandomInt(BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT, BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT));
            }

            public void onReceivedSslError(WebView webView, final SslErrorHandler sslErrorHandler, final SslError sslError) {
                if (sslErrorHandler != null) {
                    String str = BaseInfo.untrustedca;
                    if (PdrUtil.isEquals(str, "refuse")) {
                        sslErrorHandler.cancel();
                    } else if (PdrUtil.isEquals(str, "warning")) {
                        Context context = webView.getContext();
                        final AlertDialog create = new AlertDialog.Builder(context).create();
                        create.setIcon(17301601);
                        create.setTitle("安全警告");
                        create.setCanceledOnTouchOutside(false);
                        String str2 = null;
                        if (Build.VERSION.SDK_INT >= 14) {
                            str2 = sslError.getUrl();
                        }
                        String str3 = "此站点安全证书存在问题,是否继续?";
                        if (!TextUtils.isEmpty(str2)) {
                            str3 = str2 + "\n" + str3;
                        }
                        create.setMessage(str3);
                        AnonymousClass2 r1 = new DialogInterface.OnClickListener() {
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
                }
            }

            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                return TextUtils.isEmpty(str) || !str.toLowerCase(Locale.ENGLISH).startsWith("http");
            }
        });
        removeUnSafeJavascriptInterface();
        this.mWebView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String str, String str2, String str3, String str4, long j) {
            }
        });
    }

    private void removeUnSafeJavascriptInterface() {
        try {
            int i = Build.VERSION.SDK_INT;
            if (i >= 11 && i < 17) {
                Method method = getClass().getMethod("removeJavascriptInterface", new Class[]{String.class});
                WebView webView = this.mWebView;
                String[] strArr = {"searchBoxJavaBridge_", "accessibility", "ccessibilityaversal"};
                for (int i2 = 0; i2 < 3; i2++) {
                    method.invoke(webView, new Object[]{strArr[i2]});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadUrl(String str) {
        WebView webView = this.mWebView;
        if (webView != null) {
            webView.loadUrl(str);
        }
    }
}
