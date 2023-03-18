package com.bun.miitmdid.supplier.sumsung;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.samsung.android.deviceidservice.IDeviceIdService;
import java.util.Objects;

public class SumsungCore {
    private static boolean DBG = false;
    private static String SAMSUNGTAG = "Samsung_DeviceIdService";
    private static String TAG = "SumsungCore library";
    private com.bun.miitmdid.c.e.a mCallerCallBack = null;
    private ServiceConnection mConnection;
    private Context mContext = null;
    private IDeviceIdService mDeviceidInterface;

    class a implements ServiceConnection {
        a() {
        }

        public native synchronized void onServiceConnected(ComponentName componentName, IBinder iBinder);

        public native void onServiceDisconnected(ComponentName componentName);
    }

    public SumsungCore(Context context, com.bun.miitmdid.c.e.a aVar) {
        Objects.requireNonNull(context, "Context can not be null.");
        this.mContext = context;
        this.mCallerCallBack = aVar;
        this.mConnection = new a();
        Intent intent = new Intent();
        intent.setClassName("com.samsung.android.deviceidservice", "com.samsung.android.deviceidservice.DeviceIdService");
        if (this.mContext.bindService(intent, this.mConnection, 1)) {
            com.bun.lib.a.b(TAG, "bindService Successful!");
            return;
        }
        this.mContext.unbindService(this.mConnection);
        com.bun.lib.a.b(TAG, "bindService Failed!");
        com.bun.miitmdid.c.e.a aVar2 = this.mCallerCallBack;
        if (aVar2 != null) {
            aVar2.b();
        }
    }

    static native /* synthetic */ IDeviceIdService access$002(SumsungCore sumsungCore, IDeviceIdService iDeviceIdService);

    static native /* synthetic */ com.bun.miitmdid.c.e.a access$100(SumsungCore sumsungCore);

    static native /* synthetic */ String access$200();

    public native String getAAID();

    public native String getOAID();

    public native String getUDID();

    public native String getVAID();

    public native boolean isSupported();

    public native void shutdown();
}
