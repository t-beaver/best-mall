package io.dcloud.common.DHInterface;

import android.app.Application;

public interface INativeAppInfo {
    Application getApplication();

    IConfusionMgr getCofusionMgr();
}
