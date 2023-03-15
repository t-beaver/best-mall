package com.bun.miitmdid.supplier.msa;

import android.content.Context;
import android.text.TextUtils;
import com.bun.lib.sysParamters;
import com.bun.miitmdid.c.e.a;
import com.bun.supplier.InnerIdSupplier;
import com.bun.supplier.SupplierListener;

public class b implements InnerIdSupplier, a {
    public SupplierListener a;
    private MsaClient b;

    public b(Context context) {
        if (MsaClient.CheckService(context)) {
            String g = sysParamters.g();
            if (!TextUtils.isEmpty(g)) {
                MsaClient.StartMsaKlService(context, g);
            }
            this.b = new MsaClient(context, this);
        }
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
