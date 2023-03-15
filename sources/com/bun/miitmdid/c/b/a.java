package com.bun.miitmdid.c.b;

import android.content.Context;
import android.os.IBinder;
import com.asus.msa.SupplementaryDID.IDidAidlInterface;
import com.asus.msa.sdid.IDIDBinderStatusListener;
import com.asus.msa.sdid.SupplementaryDIDManager;
import com.bun.supplier.InnerIdSupplier;
import com.bun.supplier.SupplierListener;

public class a implements InnerIdSupplier, IDIDBinderStatusListener {
    private SupplierListener a;
    private String b = "";
    private String c = "";
    private String d = "";
    private String e = "";
    private SupplementaryDIDManager f;
    private boolean g = false;
    private boolean h = false;

    public a(Context context, SupplierListener supplierListener) {
        this.a = supplierListener;
        this.f = new SupplementaryDIDManager(context);
    }

    public native void a(IDidAidlInterface iDidAidlInterface);

    public native void a(SupplierListener supplierListener);

    public native boolean a();

    public native IBinder asBinder();

    public native void b();

    public native String getAAID();

    public native String getOAID();

    public native String getUDID();

    public native String getVAID();

    public native boolean isSupported();

    public native void shutDown();
}
