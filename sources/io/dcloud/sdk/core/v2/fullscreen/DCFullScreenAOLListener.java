package io.dcloud.sdk.core.v2.fullscreen;

public interface DCFullScreenAOLListener {
    void onClick();

    void onClose();

    void onShow();

    void onShowError(int i, String str);

    void onSkip();

    void onVideoPlayEnd();
}
