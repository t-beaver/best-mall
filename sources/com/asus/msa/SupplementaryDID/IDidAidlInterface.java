package com.asus.msa.SupplementaryDID;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

public interface IDidAidlInterface extends IInterface {

    public static abstract class Stub extends Binder implements IDidAidlInterface {

        public static class Proxy implements IDidAidlInterface {
            public IBinder a;

            public Proxy(IBinder iBinder) {
                this.a = iBinder;
            }

            public native boolean a();

            public native IBinder asBinder();

            public native String getAAID();

            public native String getOAID();

            public native String getUDID();

            public native String getVAID();
        }

        public Stub() {
            attachInterface(this, "com.asus.msa.SupplementaryDID.IDidAidlInterface");
        }

        public static native IDidAidlInterface a(IBinder iBinder);

        public native IBinder asBinder();

        public native boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2);
    }

    boolean a();

    String getAAID();

    String getOAID();

    String getUDID();

    String getVAID();
}
