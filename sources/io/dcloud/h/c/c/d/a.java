package io.dcloud.h.c.c.d;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import com.taobao.weex.performance.WXInstanceApm;
import io.dcloud.h.c.c.e.b.c;
import io.dcloud.sdk.core.module.DCBaseAOL;
import io.dcloud.sdk.core.module.DCBaseAOLLoader;
import io.dcloud.sdk.core.util.AdErrorUtil;
import io.dcloud.sdk.poly.base.config.DCloudSlotConfig;
import io.dcloud.sdk.poly.base.config.ThirdSlotConfig;
import io.dcloud.sdk.poly.base.utils.a;
import io.dcloud.sdk.poly.base.utils.d;
import io.dcloud.sdk.poly.base.utils.e;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.json.JSONArray;

public abstract class a extends c implements io.dcloud.h.c.c.e.b.d.a {
    private final List<io.dcloud.h.c.c.e.b.d.b> f = new ArrayList();
    private final List<io.dcloud.h.c.c.e.b.d.b> g = new ArrayList();
    private io.dcloud.h.c.c.e.b.d.b h;
    private boolean i = false;
    private final Handler j = new b(d.a().getLooper());
    private final int k = 3;
    private final int l = 18000;
    private long m = 0;
    private String n = "";
    private boolean o = false;
    private DCloudSlotConfig p;

    /* renamed from: io.dcloud.h.c.c.d.a$a  reason: collision with other inner class name */
    public interface C0060a {
        void a(DCBaseAOLLoader dCBaseAOLLoader, io.dcloud.sdk.poly.base.config.b bVar);

        void a(DCBaseAOLLoader dCBaseAOLLoader, List<? extends DCBaseAOL> list, io.dcloud.sdk.poly.base.config.b bVar);
    }

    private class b extends Handler {
        public b(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            a.this.j();
        }
    }

    public a(Activity activity) {
        super(activity);
    }

    private void f() {
        ArrayList<io.dcloud.sdk.poly.base.config.b> arrayList = new ArrayList<>();
        for (io.dcloud.h.c.c.e.b.d.b g2 : this.g) {
            arrayList.addAll(g2.g());
        }
        JSONArray jSONArray = new JSONArray();
        if (arrayList.size() > 0) {
            JSONArray jSONArray2 = new JSONArray();
            for (io.dcloud.sdk.poly.base.config.b bVar : arrayList) {
                if (bVar.d()) {
                    if (bVar.c() == 0) {
                        jSONArray.put(bVar.b());
                    }
                    jSONArray2.put(bVar.e());
                }
            }
            a(this.d, this.b.getAdpid(), this.p.g() ? "1" : WXInstanceApm.VALUE_ERROR_CODE_DEFAULT, this.b.getEI(), jSONArray2);
        }
        a(jSONArray);
    }

    private void h() {
        long e = ((long) this.p.e()) - (SystemClock.elapsedRealtime() - this.m);
        if (e <= 0) {
            c(-5018, AdErrorUtil.getErrorMsg(-5018));
            return;
        }
        this.j.removeMessages(3);
        this.j.sendEmptyMessageDelayed(3, e);
        if (this.d != this.p.f()) {
            c(-5011, AdErrorUtil.getErrorMsg(-5011));
            return;
        }
        List<ThirdSlotConfig> d = this.p.d();
        if (d == null || d.isEmpty()) {
            c(-5019, AdErrorUtil.getErrorMsg(-5019));
        } else if (this.d != 1 && Collections.disjoint(io.dcloud.sdk.core.b.a.b().a().keySet(), this.p.b())) {
            c(-5012, AdErrorUtil.getErrorMsg(-5012));
        } else if (!a(this.p, d)) {
            e.a("level load finish.total:" + this.f.size());
            for (io.dcloud.h.c.c.e.b.d.b obj : this.f) {
                e.a(obj.toString());
            }
            i();
        }
    }

    private void i() {
        if (this.f.size() <= 0 || this.i) {
            io.dcloud.h.c.c.e.b.d.b bVar = this.h;
            if (bVar != null) {
                ((io.dcloud.h.c.c.e.a.a) bVar).j();
                a(this.h.i());
            }
            f();
            return;
        }
        io.dcloud.h.c.c.e.b.d.b remove = this.f.remove(0);
        io.dcloud.h.c.c.e.b.d.b bVar2 = this.h;
        if (bVar2 == null || bVar2.c() <= 0) {
            this.g.add(remove);
            remove.a(this.n);
            remove.a();
            return;
        }
        boolean b2 = remove.b(this.h.c());
        e.a("is necessary to load next:" + b2 + ",next level:" + remove.f());
        if (!remove.b(this.h.c())) {
            i();
            return;
        }
        remove.e(this.h.c());
        this.g.add(remove);
        remove.a(this.n);
        remove.a();
    }

    /* access modifiers changed from: private */
    public void j() {
        this.i = true;
        e.b("this slot:time out");
        if (this.g.isEmpty()) {
            a(new JSONArray());
            return;
        }
        for (io.dcloud.h.c.c.e.b.d.b next : this.g) {
            if (!next.k()) {
                next.h();
            }
        }
    }

    /* access modifiers changed from: protected */
    public abstract void a(int i2, String str, JSONArray jSONArray);

    public DCBaseAOLLoader b() {
        return null;
    }

    public void b(io.dcloud.h.c.c.e.b.d.b bVar) {
        e.a("level load success.current:" + bVar.f() + ",is bid:" + bVar.d());
        if (bVar.d()) {
            this.h = bVar;
            i();
            return;
        }
        io.dcloud.h.c.c.e.b.d.b bVar2 = this.h;
        if (bVar2 == null || !(bVar2 instanceof io.dcloud.h.c.c.e.a.a)) {
            a(bVar.i());
        } else if (g()) {
            a.C0077a a = io.dcloud.sdk.poly.base.utils.a.a(this.b.getCount(), (List<DCBaseAOL>[]) new List[]{this.h.i(), bVar.i()});
            if (a == null) {
                this.h = null;
                i();
                return;
            }
            for (DCBaseAOL next : a.d) {
                if (next.isSlotSupportBidding()) {
                    next.biddingSuccess(a.b, a.c);
                }
            }
            a(a.d);
        } else if (bVar.c() > this.h.c()) {
            ((io.dcloud.h.c.c.e.a.a) this.h).d(bVar.c());
            a(bVar.i());
        } else {
            ((io.dcloud.h.c.c.e.a.a) this.h).j();
            a(this.h.i());
        }
        f();
    }

    /* access modifiers changed from: protected */
    public abstract void b(List<DCBaseAOL> list);

    /* access modifiers changed from: protected */
    public boolean c() {
        return 1 == this.p.c();
    }

    /* access modifiers changed from: package-private */
    public boolean g() {
        int i2 = this.d;
        return i2 == 10 || i2 == 4;
    }

    public void run() {
        this.n = UUID.randomUUID().toString();
        this.g.clear();
        this.f.clear();
        this.h = null;
        this.i = false;
        this.o = false;
        if (this.d != 1) {
            Set<String> keySet = io.dcloud.sdk.core.b.a.b().a().keySet();
            if (keySet.isEmpty() || (keySet.size() == 1 && keySet.contains("dcloud"))) {
                c(-5004, AdErrorUtil.getErrorMsg(-5004));
                return;
            }
        }
        this.j.sendEmptyMessageDelayed(3, 18000);
        this.m = SystemClock.elapsedRealtime();
        super.run();
    }

    private void c(int i2, String str) {
        b(i2, str, (JSONArray) null);
    }

    /* access modifiers changed from: protected */
    public void a(DCloudSlotConfig dCloudSlotConfig) {
        this.p = dCloudSlotConfig;
        this.b.setAdpid(dCloudSlotConfig.a());
        h();
    }

    /* access modifiers changed from: protected */
    public void a(int i2, String str) {
        b(i2, str, (JSONArray) null);
    }

    private boolean a(DCloudSlotConfig dCloudSlotConfig, List<ThirdSlotConfig> list) {
        HashMap hashMap = new HashMap();
        io.dcloud.h.c.c.e.b.a aVar = null;
        for (ThirdSlotConfig next : list) {
            int c = next.c();
            if (next.k()) {
                if (aVar == null) {
                    aVar = new io.dcloud.h.c.c.e.b.a(this.b, this.c);
                    aVar.c(dCloudSlotConfig.f());
                    aVar.a(dCloudSlotConfig.h());
                    if (dCloudSlotConfig.g()) {
                        aVar.e();
                    }
                }
                aVar.a(c);
                aVar.a(next);
            } else {
                io.dcloud.h.c.c.e.b.d.b bVar = (io.dcloud.h.c.c.e.b.d.b) hashMap.get(Integer.valueOf(c));
                if (bVar != null) {
                    bVar.a(next);
                } else {
                    c cVar = new c(this.b, this.c);
                    if (dCloudSlotConfig.g()) {
                        cVar.e();
                    }
                    cVar.a(dCloudSlotConfig.h());
                    cVar.c(dCloudSlotConfig.f());
                    cVar.a((io.dcloud.h.c.c.e.b.d.a) this);
                    cVar.a(next.c());
                    cVar.a(next);
                    hashMap.put(Integer.valueOf(next.c()), cVar);
                }
            }
        }
        if (aVar != null) {
            if (hashMap.containsKey(Integer.valueOf(aVar.f()))) {
                io.dcloud.h.c.c.e.b.b bVar2 = new io.dcloud.h.c.c.e.b.b(this.b, this.c);
                bVar2.a(aVar);
                bVar2.a((c) hashMap.get(Integer.valueOf(aVar.f())));
                bVar2.a(aVar.f());
                bVar2.a((io.dcloud.h.c.c.e.b.d.a) this);
                bVar2.c(dCloudSlotConfig.f());
                bVar2.a(dCloudSlotConfig.h());
                if (dCloudSlotConfig.g()) {
                    bVar2.e();
                }
                hashMap.put(Integer.valueOf(aVar.f()), bVar2);
            } else {
                aVar.a((io.dcloud.h.c.c.e.b.d.a) this);
                hashMap.put(Integer.valueOf(aVar.f()), aVar);
            }
        }
        this.f.addAll(hashMap.values());
        if (this.f.size() > 1) {
            Collections.sort(this.f, $$Lambda$a$ypV7ETivaw0oy3nUrfCu2H0ektA.INSTANCE);
            return false;
        } else if (hashMap.size() == 1) {
            return false;
        } else {
            c(-5020, AdErrorUtil.getErrorMsg(-5020));
            return true;
        }
    }

    private void b(int i2, String str, JSONArray jSONArray) {
        if (!this.o) {
            this.o = true;
            e.b("this slot:all fail");
            this.j.removeMessages(3);
            a(i2, str, jSONArray);
        }
    }

    /* access modifiers changed from: protected */
    public Activity a() {
        return this.c;
    }

    public void a(io.dcloud.h.c.c.e.b.d.b bVar) {
        i();
    }

    private void a(JSONArray jSONArray) {
        if (this.i) {
            b(-5018, AdErrorUtil.getErrorMsg(-5018), jSONArray);
        } else {
            b(-5005, AdErrorUtil.getErrorMsg(-5005), jSONArray);
        }
    }

    private void a(List<DCBaseAOL> list) {
        if (!this.o) {
            this.o = true;
            int i2 = 0;
            if (io.dcloud.sdk.poly.base.utils.b.a) {
                for (DCBaseAOL obj : list) {
                    e.a("success!index:" + i2 + ";ad:" + obj.toString());
                    i2++;
                }
            }
            this.j.removeMessages(3);
            b(list);
        }
    }
}
