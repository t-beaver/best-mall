package io.dcloud.sdk.core.api;

import android.app.Activity;
import io.dcloud.sdk.core.api.AOLLoader;

public interface InterstitialAOL {
    String getType();

    boolean isValid();

    void setInterstitialAdListener(AOLLoader.InterstitialAdListener interstitialAdListener);

    void show(Activity activity);
}
