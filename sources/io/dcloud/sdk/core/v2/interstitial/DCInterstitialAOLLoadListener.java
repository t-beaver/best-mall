package io.dcloud.sdk.core.v2.interstitial;

import io.dcloud.sdk.core.v2.base.DCBaseAOLLoadListener;

public interface DCInterstitialAOLLoadListener extends DCBaseAOLLoadListener {
    void onInterstitialAdLoad();
}
