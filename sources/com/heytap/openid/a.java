package com.heytap.openid;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;

public interface a extends IInterface {

    /* renamed from: com.heytap.openid.a$a  reason: collision with other inner class name */
    public static abstract class C0007a extends Binder implements a {

        /* renamed from: com.heytap.openid.a$a$a  reason: collision with other inner class name */
        private static class C0008a implements a {
            public IBinder a;

            public C0008a(IBinder iBinder) {
                this.a = iBinder;
            }

            public native String a(String str, String str2, String str3);

            public native IBinder asBinder();
        }

        public static native a a(IBinder iBinder);
    }
}
