package io.dcloud.h.c.c.c;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

public class a implements Application.ActivityLifecycleCallbacks {
    boolean a = false;
    List<Activity> b = new ArrayList();

    public void onActivityCreated(Activity activity, Bundle bundle) {
        if (this.b.size() == 0) {
            this.a = true;
        }
        this.b.add(activity);
    }

    public void onActivityDestroyed(Activity activity) {
        this.b.remove(activity);
    }

    public void onActivityPaused(Activity activity) {
    }

    public void onActivityResumed(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityStopped(Activity activity) {
    }
}
