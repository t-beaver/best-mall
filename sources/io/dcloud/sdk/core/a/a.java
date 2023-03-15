package io.dcloud.sdk.core.a;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import io.dcloud.h.c.c.b.c;
import io.dcloud.sdk.core.DCloudAOLManager;

public class a extends DCloudAOLManager {
    public static void a(Context context, DCloudAOLManager.InitConfig initConfig, c cVar) {
        if (context == null || initConfig == null) {
            throw new NullPointerException("context or config is null");
        } else if (((context instanceof Activity) || (context instanceof Application)) && !DCloudAOLManager.c.get()) {
            DCloudAOLManager.c.set(true);
            if (DCloudAOLManager.b == null) {
                io.dcloud.h.c.a d = io.dcloud.h.c.a.d();
                d.b(context);
                d.a(initConfig);
                d.a(cVar);
                DCloudAOLManager.b = d;
                d.a(context);
            }
        }
    }
}
