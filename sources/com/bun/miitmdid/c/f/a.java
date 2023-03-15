package com.bun.miitmdid.c.f;

import android.content.Context;
import com.bun.supplier.InnerIdSupplier;
import com.bun.supplier.SupplierListener;

public class a implements InnerIdSupplier {
    private Context a;

    public a(Context context) {
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
