package io.dcloud.common.DHInterface;

import android.app.Activity;
import android.content.Context;

public interface IDPlugin extends IFeature, IBoot {
    Activity getDPluginActivity();

    Context getDPluginContext();

    void initDPlugin(Context context, Activity activity);
}
