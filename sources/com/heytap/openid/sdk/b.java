package com.heytap.openid.sdk;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class b implements ServiceConnection {
    public final /* synthetic */ c a;

    public b(c cVar) {
        this.a = cVar;
    }

    public native void onServiceConnected(ComponentName componentName, IBinder iBinder);

    public native void onServiceDisconnected(ComponentName componentName);
}
