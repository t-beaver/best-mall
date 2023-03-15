package com.taobao.weex;

public interface IWXStatisticsListener {
    void onException(String str, String str2, String str3);

    void onFirstScreen();

    void onFirstView();

    void onHeadersReceived();

    void onHttpFinish();

    void onHttpStart();

    void onJsFrameworkReady();

    void onJsFrameworkStart();

    void onSDKEngineInitialize();
}
