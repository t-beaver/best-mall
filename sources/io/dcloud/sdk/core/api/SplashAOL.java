package io.dcloud.sdk.core.api;

import android.view.ViewGroup;
import io.dcloud.sdk.core.api.AOLLoader;

public interface SplashAOL {
    boolean isValid();

    void setSplashAdInteractionListener(AOLLoader.SplashAdInteractionListener splashAdInteractionListener);

    void showIn(ViewGroup viewGroup);
}
