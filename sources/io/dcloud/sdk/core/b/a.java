package io.dcloud.sdk.core.b;

import android.util.Log;
import io.dcloud.sdk.core.adapter.IAdAdapter;
import io.dcloud.sdk.core.util.Const;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class a {
    private static volatile a a;
    private final Map<String, IAdAdapter> b = new HashMap();
    private final Map<String, String> c;

    /* renamed from: io.dcloud.sdk.core.b.a$a  reason: collision with other inner class name */
    class C0076a extends HashMap<String, String> {
        C0076a() {
            put(Const.TYPE_BD, "io.dcloud.sdk.poly.adapter.bd.BDAdAdapter");
            put(Const.TYPE_CSJ, "io.dcloud.sdk.poly.adapter.csj.CSJAdAdapter");
            put(Const.TYPE_GDT, "io.dcloud.sdk.poly.adapter.gdt.GDTADAdapter");
            put(Const.TYPE_SGM, "io.dcloud.sdk.poly.adapter.sgm.SGMADAdapter");
            put(Const.TYPE_KS, "io.dcloud.sdk.poly.adapter.ks.KSADAdapter");
            put(Const.TYPE_HW, "io.dcloud.sdk.poly.adapter.hw.HWAdAdapter");
            put(Const.TYPE_GM, "io.dcloud.sdk.poly.adapter.gm.DGMAdAdapter");
        }
    }

    private a() {
        C0076a aVar = new C0076a();
        this.c = aVar;
        for (String str : aVar.keySet()) {
            IAdAdapter a2 = a(this.c.get(str), str);
            if (a2 != null) {
                this.b.put(str, a2);
            }
        }
    }

    public static a b() {
        if (a == null) {
            synchronized (a.class) {
                if (a == null) {
                    a = new a();
                }
            }
        }
        return a;
    }

    public Map<String, IAdAdapter> a() {
        return this.b;
    }

    public List<String> c() {
        ArrayList arrayList = new ArrayList();
        try {
            arrayList.addAll(this.b.keySet());
        } catch (Exception unused) {
        }
        if (!arrayList.contains("dcloud")) {
            arrayList.add("dcloud");
        }
        return arrayList;
    }

    private IAdAdapter a(String str, String str2) {
        try {
            IAdAdapter iAdAdapter = (IAdAdapter) Class.forName(str).newInstance();
            if (!iAdAdapter.getAdapterSDKVersion().equalsIgnoreCase("1.9.9.81676")) {
                Log.e("uni-AD", str2 + " adapter version not match");
            }
            return iAdAdapter;
        } catch (Exception unused) {
            return null;
        }
    }

    public boolean a(String str) {
        return this.c.containsKey(str);
    }

    public void a(String str, IAdAdapter iAdAdapter) {
        this.b.put(str, iAdAdapter);
    }

    public IAdAdapter b(String str) {
        return this.b.get(str);
    }
}
