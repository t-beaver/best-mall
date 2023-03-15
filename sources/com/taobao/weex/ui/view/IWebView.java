package com.taobao.weex.ui.view;

import android.content.Intent;
import android.view.View;
import java.util.Map;

public interface IWebView {

    public interface OnErrorListener {
        void onError(String str, Object obj);
    }

    public interface OnMessageListener {
        void onMessage(Map<String, Object> map);
    }

    public interface OnPageListener {
        void onPageFinish(String str, boolean z, boolean z2);

        void onPageStart(String str);

        void onReceivedTitle(String str);
    }

    void destroy();

    View getView();

    void goBack();

    void goForward();

    void loadDataWithBaseURL(String str);

    void loadUrl(String str);

    void onActivityResult(int i, int i2, Intent intent);

    void postMessage(Object obj);

    void reload();

    void setOnErrorListener(OnErrorListener onErrorListener);

    void setOnMessageListener(OnMessageListener onMessageListener);

    void setOnPageListener(OnPageListener onPageListener);

    void setShowLoading(boolean z);
}
