package io.dcloud.share;

import android.os.Build;
import android.webkit.WebView;
import io.dcloud.common.DHInterface.IReflectAble;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameItem;
import io.dcloud.common.adapter.util.PlatformUtil;

public class ShareAuthorizeView extends AdaFrameItem implements IReflectAble {
    private String a;
    private WebView b;
    private IWebview c;

    public ShareAuthorizeView(IWebview iWebview, String str) {
        super(iWebview.getContext());
        this.c = iWebview;
        this.a = str;
        WebView webView = new WebView(iWebview.getContext());
        this.b = webView;
        a(webView);
        setMainView(this.b);
    }

    private void a(WebView webView) {
        try {
            int i = Build.VERSION.SDK_INT;
            if (i >= 11 && i < 17) {
                webView.getClass().getMethod("removeJavascriptInterface", new Class[]{String.class}).invoke(webView, new Object[]{"searchBoxJavaBridge_"});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load(a aVar, String str) {
        AbsWebviewClient absWebviewClient = (AbsWebviewClient) PlatformUtil.invokeMethod(aVar.a(str), "getWebviewClient", (Object) null, new Class[]{ShareAuthorizeView.class}, new Object[]{this});
        this.b.setWebViewClient(absWebviewClient);
        this.b.loadUrl(absWebviewClient.getInitUrl());
    }

    public void onauthenticated(String str) {
        Deprecated_JSUtil.execCallback(this.c, this.a, "{type:'" + str + "'}", 1, true, true);
    }

    public void onerror(String str) {
        Deprecated_JSUtil.execCallback(this.c, this.a, str, 9, false, true);
    }

    public void onloaded() {
        Deprecated_JSUtil.execCallback(this.c, this.a, "{evt:'load'}", 1, true, true);
    }
}
