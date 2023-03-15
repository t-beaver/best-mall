package io.dcloud.sdk.core.v2.splash;

import android.view.View;
import android.widget.FrameLayout;
import io.dcloud.sdk.core.v2.base.DCBaseAOLLoadListener;
import org.json.JSONObject;

public interface DCSplashAOLLoadListener extends DCBaseAOLLoadListener {
    void onSplashAdLoad();

    void pushAd(JSONObject jSONObject);

    void redBag(View view, FrameLayout.LayoutParams layoutParams);
}
