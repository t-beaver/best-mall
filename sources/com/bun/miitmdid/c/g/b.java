package com.bun.miitmdid.c.g;

import android.content.Context;
import com.bun.miitmdid.c.e.a;
import com.bun.supplier.InnerIdSupplier;
import com.bun.supplier.SupplierListener;

public class b implements InnerIdSupplier, a {
    private a a;
    private SupplierListener b;

    public b(Context context, SupplierListener supplierListener) {
        this.b = supplierListener;
        this.a = new a(context, this);
    }

    public native void a(SupplierListener supplierListener);

    public native void a(boolean z);

    public native boolean a();

    public native void b();

    public native String getAAID();

    public native String getOAID();

    public native String getUDID();

    public native String getVAID();

    public native boolean isSupported();

    public native void shutDown();
}
