package com.samsung.android.deviceidservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public interface IDeviceIdService extends IInterface {

    public static abstract class Stub extends Binder implements IDeviceIdService {

        private static class Proxy implements IDeviceIdService {
            private IBinder a;

            Proxy(IBinder iBinder) {
                this.a = iBinder;
            }

            public native IBinder asBinder();

            public native String getAAID(String str);

            public native String getOAID();

            public native String getVAID(String str);
        }

        public Stub() {
            attachInterface(this, "com.samsung.android.deviceidservice.IDeviceIdService");
        }

        public static native IDeviceIdService a(IBinder iBinder);

        public native IBinder asBinder();

        public native boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2);
    }

    String getAAID(String str);

    String getOAID();

    String getVAID(String str);
}
