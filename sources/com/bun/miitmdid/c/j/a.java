package com.bun.miitmdid.c.j;

import android.content.Context;
import com.bun.supplier.InnerIdSupplier;
import com.bun.supplier.SupplierListener;

public class a implements InnerIdSupplier {
    private String a = "";
    private Context b;

    /* renamed from: com.bun.miitmdid.c.j.a$a  reason: collision with other inner class name */
    class C0006a implements Runnable {
        final /* synthetic */ SupplierListener a;

        C0006a(SupplierListener supplierListener) {
            this.a = supplierListener;
        }

        public native void run();
    }

    public a(Context context) {
        this.b = context;
    }

    public native void a(SupplierListener supplierListener);

    public native void a(String str);

    public native boolean a();

    public native String getAAID();

    public native String getOAID();

    public native String getUDID();

    public native String getVAID();

    public native boolean isSupported();

    public native void shutDown();
}
