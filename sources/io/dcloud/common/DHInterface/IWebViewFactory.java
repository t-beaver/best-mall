package io.dcloud.common.DHInterface;

import android.app.Activity;
import android.content.Context;
import io.dcloud.common.adapter.ui.AdaWebview;
import io.dcloud.common.adapter.ui.webview.DCWebView;
import io.dcloud.common.adapter.ui.webview.OnPageFinishedCallack;

public interface IWebViewFactory {
    String getDefWebViewUA(Context context);

    DCWebView getWebView(Activity activity, AdaWebview adaWebview);

    DCWebView getWebView(Activity activity, AdaWebview adaWebview, IDCloudWebviewClientListener iDCloudWebviewClientListener);

    DCWebView getWebView(Activity activity, AdaWebview adaWebview, OnPageFinishedCallack onPageFinishedCallack);
}
