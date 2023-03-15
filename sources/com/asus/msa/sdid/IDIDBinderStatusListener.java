package com.asus.msa.sdid;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.asus.msa.SupplementaryDID.IDidAidlInterface;

public interface IDIDBinderStatusListener extends IInterface {

    public static abstract class Stub extends Binder implements IDIDBinderStatusListener {

        public static class Proxy implements IDIDBinderStatusListener {
            public IBinder a;

            public native void a(IDidAidlInterface iDidAidlInterface);

            public native IBinder asBinder();

            public native void b();
        }

        public Stub() {
            attachInterface(this, "com.asus.msa.sdid.IDIDBinderStatusListener");
        }

        public native IBinder asBinder();

        public native boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2);
    }

    void a(IDidAidlInterface iDidAidlInterface);

    void b();
}
