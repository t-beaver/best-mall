package io.dcloud.h.c.c.e.b;

import android.app.Activity;
import io.dcloud.h.c.c.e.b.d.a;
import io.dcloud.sdk.core.entry.DCloudAdSlot;
import io.dcloud.sdk.core.module.DCBaseAOL;
import io.dcloud.sdk.core.module.DCBaseAOLLoader;
import io.dcloud.sdk.poly.base.config.ThirdSlotConfig;
import io.dcloud.sdk.poly.base.utils.a;
import java.util.ArrayList;
import java.util.List;

public class b implements io.dcloud.h.c.c.e.b.d.b, a, io.dcloud.h.c.c.e.a.a {
    private c a;
    private boolean b = false;
    private boolean c = false;
    private a d;
    private boolean e = false;
    private boolean f = false;
    private int g;
    private a h;
    private int i;
    private boolean j = false;
    private boolean k = false;
    private boolean l = false;
    private boolean m = false;
    protected final DCloudAdSlot n;
    protected final Activity o;
    private final List<DCBaseAOL> p = new ArrayList();

    public b(DCloudAdSlot dCloudAdSlot, Activity activity) {
        this.n = dCloudAdSlot;
        this.o = activity;
    }

    private void m() {
        boolean z = this.e;
        if (z && this.b) {
            if (l()) {
                a.C0077a a2 = io.dcloud.sdk.poly.base.utils.a.a(this.n.getCount(), (List<DCBaseAOL>[]) new List[]{this.d.i(), this.a.i()});
                if (a2 == null) {
                    n();
                    return;
                }
                this.k = true;
                for (DCBaseAOL next : a2.d) {
                    if (next.isSlotSupportBidding()) {
                        next.biddingSuccess(a2.b, a2.c);
                    }
                }
                this.p.addAll(a2.d);
            } else if (this.d.c() >= this.a.c()) {
                this.j = true;
            } else {
                this.k = true;
                this.d.d(this.a.c());
            }
            o();
        } else if (!z || !this.c) {
            boolean z2 = this.f;
            if (z2 && this.b) {
                this.k = true;
                o();
            } else if (z2 && this.c) {
                n();
            }
        } else {
            this.j = true;
            o();
        }
    }

    public void a(c cVar) {
        this.a = cVar;
    }

    public void a(ThirdSlotConfig thirdSlotConfig) {
    }

    public DCBaseAOLLoader b() {
        return null;
    }

    public void b(io.dcloud.h.c.c.e.b.d.b bVar) {
        if (bVar == this.a) {
            this.b = true;
        } else if (bVar == this.d) {
            this.e = true;
        }
        m();
    }

    public boolean b(int i2) {
        return true;
    }

    public int c() {
        if (this.j) {
            return this.d.c();
        }
        if (this.k) {
            return this.a.c();
        }
        return -1;
    }

    public boolean d() {
        return this.j;
    }

    public void e() {
        if (this.j) {
            this.d.e();
        }
        if (this.k) {
            this.a.e();
        }
    }

    public void e(int i2) {
    }

    public int f() {
        return this.g;
    }

    public List<io.dcloud.sdk.poly.base.config.b> g() {
        ArrayList arrayList = new ArrayList();
        a aVar = this.d;
        if (aVar != null) {
            arrayList.addAll(aVar.g());
        }
        c cVar = this.a;
        if (cVar != null) {
            arrayList.addAll(cVar.g());
        }
        return arrayList;
    }

    public void h() {
        this.l = true;
        a aVar = this.d;
        if (aVar != null) {
            aVar.h();
        }
        c cVar = this.a;
        if (cVar != null) {
            cVar.h();
        }
    }

    public List<DCBaseAOL> i() {
        if (l()) {
            return this.p;
        }
        if (this.j) {
            return this.d.i();
        }
        if (this.k) {
            return this.a.i();
        }
        return null;
    }

    public void j() {
        if (d()) {
            this.d.j();
        }
    }

    public boolean k() {
        c cVar = this.a;
        return cVar != null && this.d != null && cVar.k() && this.d.k();
    }

    /* access modifiers changed from: package-private */
    public boolean l() {
        int i2 = this.i;
        return i2 == 10 || i2 == 4;
    }

    /* access modifiers changed from: protected */
    public void n() {
        if (!this.m) {
            this.m = true;
            io.dcloud.h.c.c.e.b.d.a aVar = this.h;
            if (aVar != null) {
                aVar.a(this);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void o() {
        if (!this.m) {
            this.m = true;
            io.dcloud.h.c.c.e.b.d.a aVar = this.h;
            if (aVar != null) {
                aVar.b(this);
            }
        }
    }

    public String toString() {
        return "Bidding:" + this.d.toString() + ",Usual:" + this.a.toString();
    }

    public void a(a aVar) {
        this.d = aVar;
    }

    public void d(int i2) {
        if (d()) {
            this.d.d(i2);
        }
    }

    public void a(io.dcloud.h.c.c.e.b.d.a aVar) {
        this.h = aVar;
    }

    public void a() {
        if (this.l) {
            n();
            return;
        }
        a aVar = this.d;
        if (aVar != null) {
            aVar.a((io.dcloud.h.c.c.e.b.d.a) this);
            this.d.a();
        }
        c cVar = this.a;
        if (cVar != null) {
            cVar.a((io.dcloud.h.c.c.e.b.d.a) this);
            this.a.a();
        }
    }

    public void c(int i2) {
        this.i = i2;
        a aVar = this.d;
        if (aVar != null) {
            aVar.c(i2);
        }
        c cVar = this.a;
        if (cVar != null) {
            cVar.c(i2);
        }
    }

    public void a(int i2) {
        this.g = i2;
    }

    public void a(String str) {
        a aVar = this.d;
        if (aVar != null) {
            aVar.a(str);
        }
        c cVar = this.a;
        if (cVar != null) {
            cVar.a(str);
        }
    }

    public void a(boolean z) {
        a aVar = this.d;
        if (aVar != null) {
            aVar.a(z);
        }
        c cVar = this.a;
        if (cVar != null) {
            cVar.a(z);
        }
    }

    public void a(io.dcloud.h.c.c.e.b.d.b bVar) {
        if (bVar == this.a) {
            this.c = true;
        } else if (bVar == this.d) {
            this.f = true;
        }
        m();
    }
}
