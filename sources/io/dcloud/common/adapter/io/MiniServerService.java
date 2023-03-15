package io.dcloud.common.adapter.io;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import io.dcloud.common.adapter.util.DeviceInfo;

public class MiniServerService extends Service {
    String mServiceName = null;

    private void startServer() {
        AdaService serviceListener;
        if (!TextUtils.isEmpty(this.mServiceName) && (serviceListener = AdaService.getServiceListener(this.mServiceName)) != null) {
            serviceListener.onExecute();
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
    }

    public void onDestroy() {
        AdaService serviceListener;
        if (!TextUtils.isEmpty(this.mServiceName) && (serviceListener = AdaService.getServiceListener(this.mServiceName)) != null) {
            serviceListener.onDestroy();
            AdaService.getServiceListener(this.mServiceName);
        }
        super.onDestroy();
    }

    public void onStart(Intent intent, int i) {
        if (DeviceInfo.sDeviceSdkVer <= 5 && intent != null && !TextUtils.isEmpty(this.mServiceName)) {
            this.mServiceName = intent.getStringExtra(AbsoluteConst.MINI_SERVER_NAME);
            startServer();
        }
        super.onStart(intent, i);
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        if (DeviceInfo.sDeviceSdkVer > 5 && intent != null) {
            String stringExtra = intent.getStringExtra(AbsoluteConst.MINI_SERVER_NAME);
            this.mServiceName = stringExtra;
            if (!TextUtils.isEmpty(stringExtra)) {
                startServer();
            }
        }
        return super.onStartCommand(intent, i, i2);
    }
}
