package io.dcloud.sdk.core.v2.splash;

public interface DCSplashAOLListener {
    void onClick();

    void onClose();

    void onShow();

    void onShowError(int i, String str);

    void onSkip();

    void onVideoPlayEnd();
}
