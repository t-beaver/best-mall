package io.dcloud.sdk.core.v2.interstitial;

public interface DCInterstitialAOLListener {
    void onClick();

    void onClose();

    void onShow();

    void onShowError(int i, String str);

    void onSkip();

    void onVideoPlayEnd();
}
