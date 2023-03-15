package io.dcloud.common.DHInterface;

import android.view.View;

public interface IWebAppRootView {
    boolean didCloseSplash();

    IFrameView findFrameViewB(IFrameView iFrameView);

    void goHome(IFrameView iFrameView);

    View obtainMainView();

    void onAppActive(IApp iApp);

    void onAppStart(IApp iApp);

    void onAppStop(IApp iApp);

    void onAppUnActive(IApp iApp);

    void onRootViewGlobalLayout(View view);
}
