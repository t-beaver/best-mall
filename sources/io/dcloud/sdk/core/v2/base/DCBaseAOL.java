package io.dcloud.sdk.core.v2.base;

import android.app.Activity;

public class DCBaseAOL {
    private Activity a;

    public DCBaseAOL(Activity activity) {
        this.a = activity;
    }

    public Activity getContext() {
        return this.a;
    }
}
