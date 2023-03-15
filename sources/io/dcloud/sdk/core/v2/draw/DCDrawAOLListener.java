package io.dcloud.sdk.core.v2.draw;

import io.dcloud.sdk.core.v2.feed.DCFeedAOLListener;

public interface DCDrawAOLListener extends DCFeedAOLListener {
    void onClick();

    void onClosed(String str);

    void onEnd();

    void onPause();

    void onRenderFail();

    void onRenderSuccess();

    void onResume();

    void onShow();

    void onShowError();

    void onStart();
}
