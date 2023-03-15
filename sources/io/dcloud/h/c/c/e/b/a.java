package io.dcloud.h.c.c.e.b;

import android.app.Activity;
import io.dcloud.sdk.core.entry.DCloudAdSlot;
import io.dcloud.sdk.core.module.DCBaseAOL;
import io.dcloud.sdk.core.module.DCBaseAOLLoader;
import io.dcloud.sdk.poly.base.config.b;
import io.dcloud.sdk.poly.base.utils.e;
import java.util.ArrayList;
import java.util.List;

public class a extends c implements io.dcloud.h.c.c.e.a.a {
    private int w = 0;
    private int x = 0;

    public a(DCloudAdSlot dCloudAdSlot, Activity activity) {
        super(dCloudAdSlot, activity);
    }

    public void a(int i) {
        int i2 = this.t;
        if (i < i2 || i2 < 0) {
            this.t = i;
        }
    }

    public boolean b(int i) {
        return true;
    }

    public int c() {
        return super.c();
    }

    public void d(int i) {
        DCBaseAOL dCBaseAOL = this.j;
        if (dCBaseAOL != null) {
            dCBaseAOL.biddingFail(i, this.w, 2);
        }
    }

    public boolean d() {
        return true;
    }

    public void e(int i) {
    }

    public void h() {
        this.p = true;
        if (!this.b) {
            l();
            if (this.f.isEmpty()) {
                p();
            } else {
                r();
            }
        }
    }

    public void j() {
        List<DCBaseAOL> list = this.k;
        if (list != null) {
            for (DCBaseAOL biddingSuccess : list) {
                biddingSuccess.biddingSuccess(this.w, this.x);
            }
        }
        DCBaseAOL dCBaseAOL = this.j;
        if (dCBaseAOL != null) {
            dCBaseAOL.biddingSuccess(this.w, this.x);
        }
    }

    /* access modifiers changed from: protected */
    public void r() {
        if (!k() && !this.p) {
            return;
        }
        if (m()) {
            if (this.i.size() > 0) {
                ArrayList arrayList = new ArrayList();
                for (List<? extends DCBaseAOL> addAll : this.i.values()) {
                    arrayList.addAll(addAll);
                }
                io.dcloud.sdk.poly.base.utils.a.a((List<DCBaseAOL>) arrayList);
                e.a("level load success,total ads:" + arrayList.size());
                if (arrayList.size() > this.r.getCount()) {
                    List<DCBaseAOL> subList = arrayList.subList(0, this.r.getCount());
                    this.k = subList;
                    this.w = subList.get(0).g();
                    List<DCBaseAOL> subList2 = arrayList.subList(this.r.getCount(), arrayList.size());
                    this.x = ((DCBaseAOL) subList2.get(0)).g();
                    for (DCBaseAOL biddingFail : subList2) {
                        biddingFail.biddingFail(this.w, this.x, 2);
                    }
                } else {
                    this.w = ((DCBaseAOL) arrayList.get(0)).g();
                    this.k = arrayList;
                }
                q();
                return;
            }
            p();
        } else if (this.f.size() > 0) {
            io.dcloud.sdk.poly.base.utils.a.a(this.f);
            DCBaseAOL remove = this.f.remove(0);
            this.j = remove;
            this.w = remove.g();
            if (this.f.size() >= 1) {
                DCBaseAOL remove2 = this.f.remove(0);
                int g = remove2.g();
                this.x = g;
                remove2.biddingFail(this.w, g, 2);
                for (DCBaseAOL biddingFail2 : this.f) {
                    biddingFail2.biddingFail(this.w, this.x, 2);
                }
            }
            for (DCBaseAOLLoader biddingFail3 : this.g) {
                biddingFail3.biddingFail(this.w, this.x, 3);
            }
            q();
        } else {
            p();
        }
    }

    public void a(DCBaseAOLLoader dCBaseAOLLoader, List<? extends DCBaseAOL> list, b bVar) {
        if (this.p || this.b) {
            dCBaseAOLLoader.biddingFail(this.w, this.x, 1);
        }
        super.a(dCBaseAOLLoader, list, bVar);
    }

    public void a(DCBaseAOLLoader dCBaseAOLLoader, b bVar) {
        if (this.p || this.b) {
            if (bVar.a() == -5000) {
                dCBaseAOLLoader.biddingFail(this.w, this.x, 1);
            } else {
                dCBaseAOLLoader.biddingFail(this.w, this.x, 3);
            }
        }
        super.a(dCBaseAOLLoader, bVar);
    }
}
