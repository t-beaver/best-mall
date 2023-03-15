package com.huawei.android.hms.pps.a;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import java.util.concurrent.LinkedBlockingQueue;

public final class a implements ServiceConnection {
    public boolean a = false;
    public final LinkedBlockingQueue<IBinder> b = new LinkedBlockingQueue<>(1);

    public final native void onServiceConnected(ComponentName componentName, IBinder iBinder);

    public final native void onServiceDisconnected(ComponentName componentName);
}
