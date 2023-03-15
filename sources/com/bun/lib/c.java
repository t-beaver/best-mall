package com.bun.lib;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;

public interface c extends IInterface {

    public static abstract class a extends Binder implements c {

        /* renamed from: com.bun.lib.c$a$a  reason: collision with other inner class name */
        private static class C0000a implements c {
            private IBinder a;

            C0000a(IBinder iBinder) {
                this.a = iBinder;
            }

            public native IBinder asBinder();

            public native boolean c();

            public native String getAAID();

            public native String getOAID();

            public native String getVAID();

            public native boolean isSupported();

            public native void shutDown();
        }

        public static native c a(IBinder iBinder);
    }

    boolean c();

    String getAAID();

    String getOAID();

    String getVAID();

    boolean isSupported();

    void shutDown();
}
