package androidx.webkit;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewRenderProcess;
import android.webkit.WebViewRenderProcessClient;
import androidx.webkit.internal.WebMessagePortImpl;
import androidx.webkit.internal.WebViewFeatureInternal;
import androidx.webkit.internal.WebViewGlueCommunicator;
import androidx.webkit.internal.WebViewProviderAdapter;
import androidx.webkit.internal.WebViewProviderFactory;
import androidx.webkit.internal.WebViewRenderProcessClientFrameworkAdapter;
import androidx.webkit.internal.WebViewRenderProcessImpl;
import com.taobao.weex.el.parse.Operators;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import org.chromium.support_lib_boundary.WebViewProviderBoundaryInterface;

public class WebViewCompat {
    private static final Uri EMPTY_URI = Uri.parse("");
    private static final Uri WILDCARD_URI = Uri.parse("*");

    public interface VisualStateCallback {
        void onComplete(long j);
    }

    public interface WebMessageListener {
        void onPostMessage(WebView webView, WebMessageCompat webMessageCompat, Uri uri, boolean z, JavaScriptReplyProxy javaScriptReplyProxy);
    }

    private WebViewCompat() {
    }

    public static void postVisualStateCallback(WebView webView, long j, final VisualStateCallback visualStateCallback) {
        WebViewFeatureInternal feature = WebViewFeatureInternal.getFeature("VISUAL_STATE_CALLBACK");
        if (feature.isSupportedByFramework()) {
            webView.postVisualStateCallback(j, new WebView.VisualStateCallback() {
                public void onComplete(long j) {
                    VisualStateCallback.this.onComplete(j);
                }
            });
        } else if (feature.isSupportedByWebView()) {
            checkThread(webView);
            getProvider(webView).insertVisualStateCallback(j, visualStateCallback);
        } else {
            throw WebViewFeatureInternal.getUnsupportedOperationException();
        }
    }

    public static void startSafeBrowsing(Context context, ValueCallback<Boolean> valueCallback) {
        WebViewFeatureInternal feature = WebViewFeatureInternal.getFeature("START_SAFE_BROWSING");
        if (feature.isSupportedByFramework()) {
            WebView.startSafeBrowsing(context, valueCallback);
        } else if (feature.isSupportedByWebView()) {
            getFactory().getStatics().initSafeBrowsing(context, valueCallback);
        } else {
            throw WebViewFeatureInternal.getUnsupportedOperationException();
        }
    }

    public static void setSafeBrowsingWhitelist(List<String> list, ValueCallback<Boolean> valueCallback) {
        WebViewFeatureInternal feature = WebViewFeatureInternal.getFeature("SAFE_BROWSING_WHITELIST");
        if (feature.isSupportedByFramework()) {
            WebView.setSafeBrowsingWhitelist(list, valueCallback);
        } else if (feature.isSupportedByWebView()) {
            getFactory().getStatics().setSafeBrowsingWhitelist(list, valueCallback);
        } else {
            throw WebViewFeatureInternal.getUnsupportedOperationException();
        }
    }

    public static Uri getSafeBrowsingPrivacyPolicyUrl() {
        WebViewFeatureInternal feature = WebViewFeatureInternal.getFeature("SAFE_BROWSING_PRIVACY_POLICY_URL");
        if (feature.isSupportedByFramework()) {
            return WebView.getSafeBrowsingPrivacyPolicyUrl();
        }
        if (feature.isSupportedByWebView()) {
            return getFactory().getStatics().getSafeBrowsingPrivacyPolicyUrl();
        }
        throw WebViewFeatureInternal.getUnsupportedOperationException();
    }

    public static PackageInfo getCurrentWebViewPackage(Context context) {
        if (Build.VERSION.SDK_INT < 21) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= 26) {
            return WebView.getCurrentWebViewPackage();
        }
        try {
            PackageInfo loadedWebViewPackageInfo = getLoadedWebViewPackageInfo();
            if (loadedWebViewPackageInfo != null) {
                return loadedWebViewPackageInfo;
            }
            return getNotYetLoadedWebViewPackageInfo(context);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException unused) {
            return null;
        }
    }

    private static PackageInfo getLoadedWebViewPackageInfo() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return (PackageInfo) Class.forName("android.webkit.WebViewFactory").getMethod("getLoadedPackageInfo", new Class[0]).invoke((Object) null, new Object[0]);
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static android.content.pm.PackageInfo getNotYetLoadedWebViewPackageInfo(android.content.Context r5) {
        /*
            r0 = 0
            int r1 = android.os.Build.VERSION.SDK_INT     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException -> 0x0047 }
            r2 = 21
            r3 = 0
            if (r1 < r2) goto L_0x0025
            int r1 = android.os.Build.VERSION.SDK_INT     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException -> 0x0047 }
            r2 = 23
            if (r1 > r2) goto L_0x0025
            java.lang.String r1 = "android.webkit.WebViewFactory"
            java.lang.Class r1 = java.lang.Class.forName(r1)     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException -> 0x0047 }
            java.lang.String r2 = "getWebViewPackageName"
            java.lang.Class[] r4 = new java.lang.Class[r3]     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException -> 0x0047 }
            java.lang.reflect.Method r1 = r1.getMethod(r2, r4)     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException -> 0x0047 }
            java.lang.Object[] r2 = new java.lang.Object[r3]     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException -> 0x0047 }
            java.lang.Object r1 = r1.invoke(r0, r2)     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException -> 0x0047 }
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException -> 0x0047 }
            goto L_0x003b
        L_0x0025:
            java.lang.String r1 = "android.webkit.WebViewUpdateService"
            java.lang.Class r1 = java.lang.Class.forName(r1)     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException -> 0x0047 }
            java.lang.String r2 = "getCurrentWebViewPackageName"
            java.lang.Class[] r4 = new java.lang.Class[r3]     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException -> 0x0047 }
            java.lang.reflect.Method r1 = r1.getMethod(r2, r4)     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException -> 0x0047 }
            java.lang.Object[] r2 = new java.lang.Object[r3]     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException -> 0x0047 }
            java.lang.Object r1 = r1.invoke(r0, r2)     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException -> 0x0047 }
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException -> 0x0047 }
        L_0x003b:
            if (r1 != 0) goto L_0x003e
            return r0
        L_0x003e:
            android.content.pm.PackageManager r5 = r5.getPackageManager()
            android.content.pm.PackageInfo r5 = r5.getPackageInfo(r1, r3)     // Catch:{  }
            return r5
        L_0x0047:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.webkit.WebViewCompat.getNotYetLoadedWebViewPackageInfo(android.content.Context):android.content.pm.PackageInfo");
    }

    private static WebViewProviderAdapter getProvider(WebView webView) {
        return new WebViewProviderAdapter(createProvider(webView));
    }

    public static WebMessagePortCompat[] createWebMessageChannel(WebView webView) {
        WebViewFeatureInternal feature = WebViewFeatureInternal.getFeature("CREATE_WEB_MESSAGE_CHANNEL");
        if (feature.isSupportedByFramework()) {
            return WebMessagePortImpl.portsToCompat(webView.createWebMessageChannel());
        }
        if (feature.isSupportedByWebView()) {
            return getProvider(webView).createWebMessageChannel();
        }
        throw WebViewFeatureInternal.getUnsupportedOperationException();
    }

    public static void postWebMessage(WebView webView, WebMessageCompat webMessageCompat, Uri uri) {
        if (WILDCARD_URI.equals(uri)) {
            uri = EMPTY_URI;
        }
        WebViewFeatureInternal feature = WebViewFeatureInternal.getFeature("POST_WEB_MESSAGE");
        if (feature.isSupportedByFramework()) {
            webView.postWebMessage(WebMessagePortImpl.compatToFrameworkMessage(webMessageCompat), uri);
        } else if (feature.isSupportedByWebView()) {
            getProvider(webView).postWebMessage(webMessageCompat, uri);
        } else {
            throw WebViewFeatureInternal.getUnsupportedOperationException();
        }
    }

    public static void addWebMessageListener(WebView webView, String str, Set<String> set, WebMessageListener webMessageListener) {
        if (WebViewFeatureInternal.getFeature("WEB_MESSAGE_LISTENER").isSupportedByWebView()) {
            getProvider(webView).addWebMessageListener(str, (String[]) set.toArray(new String[0]), webMessageListener);
            return;
        }
        throw WebViewFeatureInternal.getUnsupportedOperationException();
    }

    public static void removeWebMessageListener(WebView webView, String str) {
        if (WebViewFeatureInternal.getFeature("WEB_MESSAGE_LISTENER").isSupportedByWebView()) {
            getProvider(webView).removeWebMessageListener(str);
            return;
        }
        throw WebViewFeatureInternal.getUnsupportedOperationException();
    }

    public static ScriptReferenceCompat addDocumentStartJavascript(WebView webView, String str, Set<String> set) {
        if (WebViewFeatureInternal.getFeature("DOCUMENT_START_SCRIPT").isSupportedByWebView()) {
            return getProvider(webView).addDocumentStartJavascript(str, (String[]) set.toArray(new String[0]));
        }
        throw WebViewFeatureInternal.getUnsupportedOperationException();
    }

    public static WebViewClient getWebViewClient(WebView webView) {
        WebViewFeatureInternal feature = WebViewFeatureInternal.getFeature("GET_WEB_VIEW_CLIENT");
        if (feature.isSupportedByFramework()) {
            return webView.getWebViewClient();
        }
        if (feature.isSupportedByWebView()) {
            return getProvider(webView).getWebViewClient();
        }
        throw WebViewFeatureInternal.getUnsupportedOperationException();
    }

    public static WebChromeClient getWebChromeClient(WebView webView) {
        WebViewFeatureInternal feature = WebViewFeatureInternal.getFeature("GET_WEB_CHROME_CLIENT");
        if (feature.isSupportedByFramework()) {
            return webView.getWebChromeClient();
        }
        if (feature.isSupportedByWebView()) {
            return getProvider(webView).getWebChromeClient();
        }
        throw WebViewFeatureInternal.getUnsupportedOperationException();
    }

    public static WebViewRenderProcess getWebViewRenderProcess(WebView webView) {
        WebViewFeatureInternal feature = WebViewFeatureInternal.getFeature("GET_WEB_VIEW_RENDERER");
        if (feature.isSupportedByFramework()) {
            WebViewRenderProcess webViewRenderProcess = webView.getWebViewRenderProcess();
            if (webViewRenderProcess != null) {
                return WebViewRenderProcessImpl.forFrameworkObject(webViewRenderProcess);
            }
            return null;
        } else if (feature.isSupportedByWebView()) {
            return getProvider(webView).getWebViewRenderProcess();
        } else {
            throw WebViewFeatureInternal.getUnsupportedOperationException();
        }
    }

    public static void setWebViewRenderProcessClient(WebView webView, Executor executor, WebViewRenderProcessClient webViewRenderProcessClient) {
        WebViewFeatureInternal feature = WebViewFeatureInternal.getFeature("WEB_VIEW_RENDERER_CLIENT_BASIC_USAGE");
        if (feature.isSupportedByFramework()) {
            webView.setWebViewRenderProcessClient(executor, webViewRenderProcessClient != null ? new WebViewRenderProcessClientFrameworkAdapter(webViewRenderProcessClient) : null);
        } else if (feature.isSupportedByWebView()) {
            getProvider(webView).setWebViewRenderProcessClient(executor, webViewRenderProcessClient);
        } else {
            throw WebViewFeatureInternal.getUnsupportedOperationException();
        }
    }

    public static void setWebViewRenderProcessClient(WebView webView, WebViewRenderProcessClient webViewRenderProcessClient) {
        WebViewFeatureInternal feature = WebViewFeatureInternal.getFeature("WEB_VIEW_RENDERER_CLIENT_BASIC_USAGE");
        WebViewRenderProcessClientFrameworkAdapter webViewRenderProcessClientFrameworkAdapter = null;
        if (feature.isSupportedByFramework()) {
            if (webViewRenderProcessClient != null) {
                webViewRenderProcessClientFrameworkAdapter = new WebViewRenderProcessClientFrameworkAdapter(webViewRenderProcessClient);
            }
            webView.setWebViewRenderProcessClient(webViewRenderProcessClientFrameworkAdapter);
        } else if (feature.isSupportedByWebView()) {
            getProvider(webView).setWebViewRenderProcessClient((Executor) null, webViewRenderProcessClient);
        } else {
            throw WebViewFeatureInternal.getUnsupportedOperationException();
        }
    }

    public static WebViewRenderProcessClient getWebViewRenderProcessClient(WebView webView) {
        WebViewFeatureInternal feature = WebViewFeatureInternal.getFeature("WEB_VIEW_RENDERER_CLIENT_BASIC_USAGE");
        if (feature.isSupportedByFramework()) {
            WebViewRenderProcessClient webViewRenderProcessClient = webView.getWebViewRenderProcessClient();
            if (webViewRenderProcessClient == null || !(webViewRenderProcessClient instanceof WebViewRenderProcessClientFrameworkAdapter)) {
                return null;
            }
            return ((WebViewRenderProcessClientFrameworkAdapter) webViewRenderProcessClient).getFrameworkRenderProcessClient();
        } else if (feature.isSupportedByWebView()) {
            return getProvider(webView).getWebViewRenderProcessClient();
        } else {
            throw WebViewFeatureInternal.getUnsupportedOperationException();
        }
    }

    public static boolean isMultiProcessEnabled() {
        if (WebViewFeatureInternal.getFeature(WebViewFeature.MULTI_PROCESS).isSupportedByWebView()) {
            return getFactory().getStatics().isMultiProcessEnabled();
        }
        throw WebViewFeatureInternal.getUnsupportedOperationException();
    }

    private static WebViewProviderFactory getFactory() {
        return WebViewGlueCommunicator.getFactory();
    }

    private static WebViewProviderBoundaryInterface createProvider(WebView webView) {
        return getFactory().createWebView(webView);
    }

    private static void checkThread(WebView webView) {
        if (Build.VERSION.SDK_INT < 28) {
            try {
                Method declaredMethod = WebView.class.getDeclaredMethod("checkThread", new Class[0]);
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(webView, new Object[0]);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e2) {
                throw new RuntimeException(e2);
            } catch (InvocationTargetException e3) {
                throw new RuntimeException(e3);
            }
        } else if (webView.getWebViewLooper() != Looper.myLooper()) {
            throw new RuntimeException("A WebView method was called on thread '" + Thread.currentThread().getName() + "'. All WebView methods must be called on the same thread. (Expected Looper " + webView.getWebViewLooper() + " called on " + Looper.myLooper() + ", FYI main Looper is " + Looper.getMainLooper() + Operators.BRACKET_END_STR);
        }
    }
}
