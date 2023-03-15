package io.dcloud.sdk.core.v2.feed;

public interface DCFeedAOLListener {
    void onClick();

    void onClosed(String str);

    void onRenderFail();

    void onRenderSuccess();

    void onShow();

    void onShowError();
}
