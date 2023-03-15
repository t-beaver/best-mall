package com.bun.miitmdid.supplier.sumsung;

import android.content.Context;
import com.bun.supplier.InnerIdSupplier;
import com.bun.supplier.SupplierListener;

public class a implements InnerIdSupplier, com.bun.miitmdid.c.e.a {
    public SupplierListener a;
    private SumsungCore b;

    public a(Context context, SupplierListener supplierListener) {
        this.a = supplierListener;
        this.b = new SumsungCore(context, this);
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
