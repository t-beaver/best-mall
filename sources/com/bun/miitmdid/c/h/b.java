package com.bun.miitmdid.c.h;

import android.content.Context;
import com.bun.supplier.InnerIdSupplier;
import com.bun.supplier.SupplierListener;

public class b implements InnerIdSupplier {
    private Context a;

    class a implements Runnable {
        final /* synthetic */ SupplierListener a;

        a(SupplierListener supplierListener) {
            this.a = supplierListener;
        }

        public native void run();
    }

    public b(Context context) {
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
