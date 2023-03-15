package io.dcloud.h.c.c.e.c.c;

import android.app.Activity;
import io.dcloud.h.c.c.d.a;
import io.dcloud.sdk.core.entry.DCloudAdSlot;
import io.dcloud.sdk.core.module.DCBaseAOL;
import io.dcloud.sdk.core.util.MainHandlerUtil;
import io.dcloud.sdk.poly.base.utils.d;
import io.dcloud.sdk.poly.base.utils.e;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;

public class b extends a {
    protected io.dcloud.h.c.c.e.a.b q;

    public b(Activity activity, int i) {
        super(activity);
        this.d = i;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void c(int i, String str, JSONArray jSONArray) {
        io.dcloud.h.c.c.e.a.b bVar = this.q;
        if (bVar != null) {
            bVar.onError(i, str, jSONArray);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void d(List list) {
        io.dcloud.h.c.c.e.a.b bVar = this.q;
        if (bVar != null) {
            bVar.onLoaded(list);
        }
    }

    /* access modifiers changed from: protected */
    public void a(int i, String str, JSONArray jSONArray) {
        e.b("uniAd-loadError", "code:" + i + ";message:" + str + ";detail:" + String.valueOf(jSONArray));
        MainHandlerUtil.getMainHandler().post(new Runnable(i, str, jSONArray) {
            public final /* synthetic */ int f$1;
            public final /* synthetic */ String f$2;
            public final /* synthetic */ JSONArray f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            public final void run() {
                b.this.c(this.f$1, this.f$2, this.f$3);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void b(List<DCBaseAOL> list) {
        MainHandlerUtil.getMainHandler().post(new Runnable(c(list)) {
            public final /* synthetic */ List f$1;

            {
                this.f$1 = r2;
            }

            public final void run() {
                b.this.d(this.f$1);
            }
        });
    }

    public void a(DCloudAdSlot dCloudAdSlot, io.dcloud.h.c.c.e.a.b bVar) {
        a(dCloudAdSlot);
        this.q = bVar;
        d.a().post(this);
    }

    /* access modifiers changed from: protected */
    public List<a> c(List<DCBaseAOL> list) {
        ArrayList arrayList = new ArrayList();
        if (list != null && list.size() > 0) {
            for (DCBaseAOL aVar : list) {
                arrayList.add(new a(aVar, a()));
            }
        }
        return arrayList;
    }
}
