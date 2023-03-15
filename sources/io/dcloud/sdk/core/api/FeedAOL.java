package io.dcloud.sdk.core.api;

import android.app.Activity;
import android.view.View;

public interface FeedAOL {
    void destroy();

    View getExpressAdView(Activity activity);

    String getType();

    void render();
}
