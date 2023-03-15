package io.dcloud.sdk.core.v2.contentPage;

import io.dcloud.sdk.core.api.ContentPage;

public interface DCContentPageVideoListener {
    void onComplete(ContentPage.ContentPageItem contentPageItem);

    void onError(ContentPage.ContentPageItem contentPageItem);

    void onPause(ContentPage.ContentPageItem contentPageItem);

    void onResume(ContentPage.ContentPageItem contentPageItem);

    void onStart(ContentPage.ContentPageItem contentPageItem);
}
