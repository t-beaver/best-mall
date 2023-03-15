package com.asus.msa.sdid;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.asus.msa.SupplementaryDID.IDidAidlInterface;

public class SupplementaryDIDManager {
    public static boolean DEBUG = false;
    public static final String TAG = "SupplementaryDIDManager";
    public boolean isBinded = false;
    public Context mContext;
    public IDidAidlInterface mDidService;
    public IDIDBinderStatusListener mListener;
    public ServiceConnection mServiceConnection = new ServiceConnection() {
        public native void onServiceConnected(ComponentName componentName, IBinder iBinder);

        public native void onServiceDisconnected(ComponentName componentName);
    };

    public SupplementaryDIDManager(Context context) {
        this.mContext = context;
    }

    public static native /* synthetic */ boolean access$000();

    public static native /* synthetic */ IDidAidlInterface access$102(SupplementaryDIDManager supplementaryDIDManager, IDidAidlInterface iDidAidlInterface);

    public static native /* synthetic */ void access$200(SupplementaryDIDManager supplementaryDIDManager, boolean z);

    private native void notifyAllListeners(boolean z);

    public native void deInit();

    public native void init(IDIDBinderStatusListener iDIDBinderStatusListener);

    public native void showLog(boolean z);
}
