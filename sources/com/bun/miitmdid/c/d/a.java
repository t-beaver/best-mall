package com.bun.miitmdid.c.d;

import android.content.Context;
import com.bun.supplier.InnerIdSupplier;
import com.bun.supplier.SupplierListener;

public class a implements InnerIdSupplier {
    private Context a;
    private String b = "";
    private String c = "";
    private String d = "";
    private boolean e = false;
    private SupplierListener f;

    /* renamed from: com.bun.miitmdid.c.d.a$a  reason: collision with other inner class name */
    class C0003a implements Runnable {
        C0003a() {
        }

        public native void run();
    }

    public a(Context context) {
        this.a = context;
    }

    static native /* synthetic */ Context a(a aVar);

    static native /* synthetic */ String a(a aVar, String str);

    private native void b();

    static native /* synthetic */ void b(a aVar);

    public native void a(SupplierListener supplierListener);

    public native boolean a();

    public native String getAAID();

    public native String getOAID();

    public native String getUDID();

    public native String getVAID();

    public native boolean isSupported();

    public native void shutDown();
}
