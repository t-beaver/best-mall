package io.dcloud.sdk.core.api;

import android.app.Activity;

public interface FullScreenVideoAOL {
    String getType();

    boolean isValid();

    void show(Activity activity);
}
