package io.dcloud.common.DHInterface.message.action;

import android.content.res.Configuration;

public class AppOnConfigChangedAction implements IAction {
    private Configuration newConfig;

    public AppOnConfigChangedAction(Configuration configuration) {
        this.newConfig = configuration;
    }

    public static AppOnConfigChangedAction obtain(Configuration configuration) {
        return new AppOnConfigChangedAction(configuration);
    }

    public Configuration getConfig() {
        return this.newConfig;
    }
}
