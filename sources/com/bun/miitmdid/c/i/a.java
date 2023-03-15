package com.bun.miitmdid.c.i;

import android.content.Context;
import com.bun.supplier.InnerIdSupplier;
import com.bun.supplier.SupplierListener;
import com.heytap.openid.sdk.OpenIDSDK;

public class a implements InnerIdSupplier {
    private Context a;

    /* renamed from: com.bun.miitmdid.c.i.a$a  reason: collision with other inner class name */
    class C0005a implements Runnable {
        final /* synthetic */ SupplierListener a;

        C0005a(SupplierListener supplierListener) {
            this.a = supplierListener;
        }

        public native void run();
    }

    public a(Context context) {
        OpenIDSDK.d(context);
        this.a = context;
    }

    public native void a(SupplierListener supplierListener);

    public native boolean a();

    public native String getAAID();

    public native String getOAID();

    public native String getUDID();

    public native String getVAID();

    public native boolean isSupported();

    public native void shutDown();
}
