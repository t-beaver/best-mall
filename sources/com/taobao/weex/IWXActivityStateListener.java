package com.taobao.weex;

@Deprecated
public interface IWXActivityStateListener {
    boolean onActivityBack();

    void onActivityCreate();

    void onActivityDestroy();

    void onActivityPause();

    void onActivityResume();

    void onActivityStart();

    void onActivityStop();
}
