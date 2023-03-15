package io.dcloud.common.adapter.io;

import android.content.Context;
import java.util.HashMap;

public abstract class AdaService {
    static final String TAG = "AdaService";
    static HashMap<String, AdaService> mServicesHandler = new HashMap<>(2);
    protected Context mContextWrapper = null;
    private String mServiceName = null;

    protected AdaService(Context context, String str) {
        this.mContextWrapper = context;
        this.mServiceName = str;
    }

    public static final AdaService getServiceListener(String str) {
        return mServicesHandler.get(str);
    }

    public static final void removeServiceListener(String str) {
        mServicesHandler.remove(str);
    }

    public abstract void onDestroy();

    public abstract void onExecute();
}
