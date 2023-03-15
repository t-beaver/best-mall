package io.dcloud.common.DHInterface;

import android.app.Activity;
import io.dcloud.common.adapter.util.ViewRect;

public interface IAppInfo extends IType_IntValue, IType_Layout_Changed {
    void clearMaskLayerCount();

    Activity getActivity();

    ViewRect getAppViewRect();

    /* synthetic */ int getInt(int i);

    int getMaskLayerCount();

    IOnCreateSplashView getOnCreateSplashView();

    int getRequestedOrientation();

    boolean isFullScreen();

    boolean isVerticalScreen();

    IWebAppRootView obtainWebAppRootView();

    void setFullScreen(boolean z);

    void setMaskLayer(boolean z);

    void setOnCreateSplashView(IOnCreateSplashView iOnCreateSplashView);

    void setRequestedOrientation(int i);

    void setRequestedOrientation(String str);

    void setWebAppRootView(IWebAppRootView iWebAppRootView);

    void updateScreenInfo(int i);
}
