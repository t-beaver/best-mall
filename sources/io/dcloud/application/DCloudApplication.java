package io.dcloud.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import io.dcloud.application.DCLoudApplicationImpl;
import io.dcloud.common.DHInterface.message.ActionBus;
import io.dcloud.common.DHInterface.message.action.AppOnConfigChangedAction;
import io.dcloud.common.DHInterface.message.action.AppOnCreateAction;
import io.dcloud.common.DHInterface.message.action.AppOnTrimMemoryAction;
import io.dcloud.common.adapter.util.Logger;

public class DCloudApplication extends Application {
    private final String TAG = DCloudApplication.class.getSimpleName();

    public void addActivityStatusListener(DCLoudApplicationImpl.ActivityStatusListener activityStatusListener) {
        DCLoudApplicationImpl.self().addActivityStatusListener(activityStatusListener);
    }

    /* access modifiers changed from: protected */
    public void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        DCLoudApplicationImpl.self().attachBaseContext(context);
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        ActionBus.getInstance().sendToBus(AppOnConfigChangedAction.obtain(configuration));
    }

    public void onCreate() {
        super.onCreate();
        DCLoudApplicationImpl.self().onCreate(this);
        ActionBus.getInstance().sendToBus(AppOnCreateAction.obtain());
    }

    public void onLowMemory() {
        super.onLowMemory();
        String str = this.TAG;
        Logger.e(str, "onLowMemory" + Runtime.getRuntime().maxMemory());
    }

    public void onTrimMemory(int i) {
        super.onTrimMemory(i);
        ActionBus.getInstance().sendToBus(AppOnTrimMemoryAction.obtain(i));
    }

    public void removeActivityStatusListener(DCLoudApplicationImpl.ActivityStatusListener activityStatusListener) {
        DCLoudApplicationImpl.self().removeActivityStatusListener(activityStatusListener);
    }

    public void stopB2FOnce() {
        DCLoudApplicationImpl.self().stopActivityStatusListener();
    }
}
