package io.dcloud.common.DHInterface;

import android.content.Context;

public interface IOnCreateSplashView {
    void onCloseSplash();

    Object onCreateSplash(Context context);
}
