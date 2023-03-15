package io.dcloud.feature.uniapp;

import android.app.Application;
import io.dcloud.weex.AppHookProxy;

public interface UniAppHookProxy extends AppHookProxy {
    void onSubProcessCreate(Application application);
}
