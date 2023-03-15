package com.zui.deviceidservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public interface IDeviceidInterface extends IInterface {

    public static abstract class Stub extends Binder implements IDeviceidInterface {

        private static class Proxy implements IDeviceidInterface {
            private IBinder a;

            Proxy(IBinder iBinder) {
                this.a = iBinder;
            }

            public native boolean a();

            public native IBinder asBinder();

            public native String getAAID(String str);

            public native String getOAID();

            public native String getUDID();

            public native String getVAID(String str);
        }

        public Stub() {
            attachInterface(this, "com.zui.deviceidservice.IDeviceidInterface");
        }

        public static native IDeviceidInterface a(IBinder iBinder);

        public native IBinder asBinder();

        public native boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2);
    }

    boolean a();

    String getAAID(String str);

    String getOAID();

    String getUDID();

    String getVAID(String str);
}
