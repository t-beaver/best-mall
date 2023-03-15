package io.src.dcloud.adapter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import io.dcloud.common.DHInterface.IReflectAble;

public class DCloudBaseService extends Service implements IReflectAble {
    public Service that = this;

    public final IBinder onBind(Intent intent) {
        return onBindImpl(intent);
    }

    public IBinder onBindImpl(Intent intent) {
        return null;
    }

    public final void onCreate() {
        super.onCreate();
        onCreateImpl();
    }

    public void onCreateImpl() {
    }

    public final void onDestroy() {
        super.onDestroy();
        onDestroyImpl();
    }

    public void onDestroyImpl() {
    }

    public final int onStartCommand(Intent intent, int i, int i2) {
        return onStartCommandImpl(intent, i, i2);
    }

    public int onStartCommandImpl(Intent intent, int i, int i2) {
        return super.onStartCommand(intent, i, i2);
    }
}
