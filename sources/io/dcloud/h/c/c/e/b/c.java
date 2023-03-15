package io.dcloud.h.c.c.e.b;

import android.app.Activity;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.h.c.c.d.a;
import io.dcloud.sdk.core.adapter.IAdAdapter;
import io.dcloud.sdk.core.entry.DCloudAdSlot;
import io.dcloud.sdk.core.module.DCBaseAOL;
import io.dcloud.sdk.core.module.DCBaseAOLLoader;
import io.dcloud.sdk.poly.base.config.ThirdSlotConfig;
import io.dcloud.sdk.poly.base.utils.e;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONArray;
import org.json.JSONObject;

public class c implements io.dcloud.h.c.c.e.b.d.b, a.C0060a {
    private boolean a = false;
    protected volatile boolean b = false;
    private final List<ThirdSlotConfig> c = new ArrayList();
    private final AtomicInteger d = new AtomicInteger(0);
    private final b e = new b();
    protected List<DCBaseAOL> f = new ArrayList();
    protected List<DCBaseAOLLoader> g = new ArrayList();
    private final List<DCBaseAOLLoader> h = new ArrayList();
    protected Map<DCBaseAOL, List<? extends DCBaseAOL>> i = new HashMap();
    protected DCBaseAOL j;
    protected List<DCBaseAOL> k = new ArrayList();
    private final List<Integer> l = new ArrayList();
    private final Map<Integer, AtomicInteger> m = new HashMap();
    private final List<io.dcloud.sdk.poly.base.config.b> n = new ArrayList();
    protected int o;
    protected boolean p = false;
    private io.dcloud.h.c.c.e.b.d.a q;
    protected final DCloudAdSlot r;
    protected final Activity s;
    protected int t = Integer.MIN_VALUE;
    private String u = "";
    private boolean v = false;

    private static class b {
        private Integer a;
        private Integer b;

        private b() {
            this.a = 0;
            this.b = 0;
        }

        public boolean a(Integer num) {
            boolean z = num.compareTo(this.a) >= 0;
            boolean z2 = num.compareTo(this.b) <= 0;
            if (!z || !z2) {
                return false;
            }
            return true;
        }

        public boolean b(Integer num) {
            return num.compareTo(this.b) <= 0;
        }

        public void c(Integer num) {
            if (a(num)) {
                return;
            }
            if (num.intValue() > this.b.intValue()) {
                this.b = num;
            } else if (num.intValue() < this.a.intValue()) {
                this.a = num;
            }
        }

        public String toString() {
            return String.format("[%s, %s]", new Object[]{this.a, this.b});
        }
    }

    public c(DCloudAdSlot dCloudAdSlot, Activity activity) {
        this.r = dCloudAdSlot;
        this.s = activity;
    }

    private void b() {
        e.d("ordered request list");
        n();
        if (k()) {
            p();
        }
    }

    private void n() {
        if (this.p || this.b) {
            return;
        }
        if (this.c.size() > 0) {
            ThirdSlotConfig remove = this.c.remove(0);
            DCBaseAOLLoader b2 = b(remove);
            if (b2 == null) {
                e.b("load sub slot fail cfg:" + remove.toString());
                this.d.decrementAndGet();
                n();
                return;
            }
            b2.a((a.C0060a) this);
            this.h.add(b2);
            b2.a(remove);
            b2.c(this.u);
            b2.a((Map<String, Object>) null);
            return;
        }
        p();
    }

    private void o() {
        if (!d()) {
            ArrayList arrayList = new ArrayList();
            if (this.i.size() > 0) {
                if (this.l.size() <= 0) {
                    for (List next : this.i.values()) {
                        if (next != null) {
                            arrayList.addAll(next);
                        }
                        if (arrayList.size() >= this.r.getCount()) {
                            break;
                        }
                    }
                } else {
                    for (Integer next2 : this.l) {
                        for (DCBaseAOL next3 : this.f) {
                            if (next3.k() == next2.intValue()) {
                                List list = this.i.get(next3);
                                if (list != null) {
                                    arrayList.addAll(list);
                                }
                                if (arrayList.size() >= this.r.getCount()) {
                                    break;
                                }
                            }
                        }
                        if (a(next2) > 0) {
                            break;
                        }
                    }
                }
            }
            if (!arrayList.isEmpty()) {
                if (arrayList.size() >= this.r.getCount()) {
                    this.k.addAll(arrayList.subList(0, this.r.getCount()));
                    q();
                } else if (k()) {
                    this.k.addAll(arrayList);
                    q();
                }
            }
        }
        r();
    }

    public void a(ThirdSlotConfig thirdSlotConfig) {
        this.e.c(Integer.valueOf(thirdSlotConfig.b()));
        this.c.add(thirdSlotConfig);
    }

    public int c() {
        if (m()) {
            List<DCBaseAOL> list = this.k;
            if (list == null || list.size() <= 0) {
                return -1;
            }
            List<DCBaseAOL> list2 = this.k;
            return list2.get(list2.size() - 1).g();
        }
        DCBaseAOL dCBaseAOL = this.j;
        if (dCBaseAOL != null) {
            return dCBaseAOL.g();
        }
        return -1;
    }

    public boolean d() {
        return false;
    }

    public void e() {
        if (!(this instanceof a)) {
            this.a = true;
        }
    }

    public int f() {
        return this.t;
    }

    public List<io.dcloud.sdk.poly.base.config.b> g() {
        return this.n;
    }

    public void h() {
        this.p = true;
        if (!this.b) {
            l();
            if (this.f.isEmpty()) {
                p();
                return;
            }
            Collections.sort(this.f, $$Lambda$c$i33VTk87AvEDAtwkUfe_rfuXEBA.INSTANCE);
            if (m()) {
                ArrayList arrayList = new ArrayList();
                for (DCBaseAOL dCBaseAOL : this.f) {
                    List list = this.i.get(dCBaseAOL);
                    if (list != null) {
                        arrayList.addAll(list);
                    }
                    if (arrayList.size() >= this.r.getCount()) {
                        break;
                    }
                }
                if (arrayList.size() <= 0) {
                    p();
                } else if (arrayList.size() > this.r.getCount()) {
                    this.k.addAll(arrayList.subList(0, this.r.getCount()));
                } else {
                    this.k.addAll(arrayList);
                }
            } else {
                this.j = this.f.get(0);
                q();
            }
        }
    }

    public List<DCBaseAOL> i() {
        if (m()) {
            return this.k;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.j);
        return arrayList;
    }

    public boolean k() {
        return this.p || this.d.get() <= 0;
    }

    /* access modifiers changed from: protected */
    public void l() {
        this.h.removeAll(this.f);
        this.h.removeAll(this.g);
        for (DCBaseAOLLoader p2 : this.h) {
            this.n.add(p2.p());
        }
    }

    /* access modifiers changed from: package-private */
    public boolean m() {
        int i2 = this.o;
        return i2 == 10 || i2 == 4;
    }

    /* access modifiers changed from: protected */
    public void p() {
        if (!this.b) {
            e.a("current level load fail.level:" + this.t);
            this.b = true;
            l();
            io.dcloud.h.c.c.e.b.d.a aVar = this.q;
            if (aVar != null) {
                aVar.a(this);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void q() {
        if (!this.b) {
            if (m()) {
                e.a("current level load success.level:" + this.t + ";count:" + this.k.size());
            } else {
                e.a("current level load success.level:" + this.t + ";slot:" + this.j.getSlotId() + ";ss:" + this.j.k());
            }
            this.b = true;
            l();
            io.dcloud.h.c.c.e.b.d.a aVar = this.q;
            if (aVar != null) {
                aVar.b(this);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void r() {
        if (k()) {
            if (this.f.isEmpty()) {
                p();
            }
        } else if (this.a) {
            b();
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("print all slot cfg:");
        sb.append("level:");
        sb.append(this.t);
        sb.append(";");
        for (ThirdSlotConfig next : this.c) {
            sb.append(next.e());
            sb.append(":");
            sb.append(next.g());
            sb.append(":show:");
            sb.append(next.f());
            sb.append(",");
        }
        return sb.toString();
    }

    private void f(int i2) {
        AtomicInteger atomicInteger = this.m.get(Integer.valueOf(i2));
        if (atomicInteger == null) {
            this.m.put(Integer.valueOf(i2), new AtomicInteger(0));
        } else {
            atomicInteger.decrementAndGet();
        }
    }

    public void a(io.dcloud.h.c.c.e.b.d.a aVar) {
        this.q = aVar;
    }

    public void e(int i2) {
        Iterator<ThirdSlotConfig> it = this.c.iterator();
        while (it.hasNext()) {
            if (it.next().b() <= i2) {
                it.remove();
            }
        }
    }

    public void a() {
        if (this.c.isEmpty()) {
            p();
        } else if (this.p) {
            p();
        } else {
            b(Operators.PLUS);
            if (d()) {
                b(false);
                return;
            }
            JSONObject jSONObject = new JSONObject();
            JSONObject jSONObject2 = new JSONObject();
            if (this.c.size() > 1) {
                LinkedHashMap linkedHashMap = new LinkedHashMap();
                for (ThirdSlotConfig next : this.c) {
                    this.d.incrementAndGet();
                    if (next.j()) {
                        try {
                            int i2 = next.i();
                            if (i2 > 0) {
                                jSONObject.put(next.g(), i2);
                            }
                            int d2 = next.d();
                            if (d2 > 0) {
                                JSONArray jSONArray = new JSONArray();
                                jSONArray.put(next.e());
                                jSONArray.put(d2);
                                jSONObject2.put(next.g(), jSONArray);
                            }
                        } catch (Exception unused) {
                        }
                    }
                    linkedHashMap.put(next.g(), next);
                }
                if (jSONObject.length() > 0 || jSONObject2.length() > 0) {
                    List<String> a2 = io.dcloud.sdk.poly.base.utils.a.a(this.s, new ArrayList(linkedHashMap.keySet()), this.r.getAdpid(), jSONObject, jSONObject2);
                    this.c.clear();
                    for (String str : a2) {
                        ThirdSlotConfig thirdSlotConfig = (ThirdSlotConfig) linkedHashMap.get(str);
                        if (thirdSlotConfig != null) {
                            this.c.add(thirdSlotConfig);
                        }
                    }
                    if (this.a) {
                        b();
                    } else {
                        b(true);
                    }
                } else if (this.a) {
                    b();
                } else {
                    b(this.v);
                }
            } else {
                this.d.incrementAndGet();
                b();
            }
        }
    }

    private void b(boolean z) {
        AtomicInteger atomicInteger;
        this.d.set(0);
        ArrayList<DCBaseAOLLoader> arrayList = new ArrayList<>();
        int i2 = 0;
        for (ThirdSlotConfig next : this.c) {
            DCBaseAOLLoader b2 = b(next);
            if (b2 != null) {
                this.d.incrementAndGet();
                if (z) {
                    next.a(i2);
                    i2++;
                }
                b2.a(next);
                b2.a((a.C0060a) this);
                arrayList.add(b2);
                if (!this.l.contains(Integer.valueOf(next.f()))) {
                    this.l.add(Integer.valueOf(next.f()));
                }
                if (this.m.containsKey(Integer.valueOf(next.f()))) {
                    atomicInteger = this.m.get(Integer.valueOf(next.f()));
                } else {
                    atomicInteger = new AtomicInteger(0);
                }
                atomicInteger.incrementAndGet();
                this.m.put(Integer.valueOf(next.f()), atomicInteger);
            } else {
                e.b("load sub slot fail cfg:" + next.toString());
            }
        }
        e.a("level start load.current:" + this.t + ",valid ads:" + arrayList.size());
        if (arrayList.size() > 0) {
            if (this.l.size() > 1) {
                Collections.sort(this.l);
            }
            this.h.addAll(arrayList);
            for (DCBaseAOLLoader dCBaseAOLLoader : arrayList) {
                dCBaseAOLLoader.c(this.u);
                dCBaseAOLLoader.a((Map<String, Object>) null);
            }
            return;
        }
        p();
    }

    public void c(int i2) {
        this.o = i2;
    }

    /* access modifiers changed from: protected */
    public DCBaseAOLLoader b(ThirdSlotConfig thirdSlotConfig) {
        io.dcloud.h.c.c.e.b.d.a aVar;
        IAdAdapter b2 = io.dcloud.sdk.core.b.a.b().b(thirdSlotConfig.e());
        io.dcloud.sdk.poly.base.config.a a2 = io.dcloud.h.c.c.a.a.a().a(thirdSlotConfig.e());
        if (this.o == 1 && b2 == null && !io.dcloud.sdk.core.b.a.b().a(thirdSlotConfig.e()) && a2 != null && (b2 = io.dcloud.sdk.core.b.a.b().b("dcloud")) == null) {
            b2 = new io.dcloud.h.c.b.a.a();
            io.dcloud.sdk.core.b.a.b().a("dcloud", b2);
        }
        if (b2 == null || !b2.isSupport()) {
            return null;
        }
        if (thirdSlotConfig.e().equalsIgnoreCase("dcloud") && (aVar = this.q) != null) {
            return aVar.b();
        }
        DCBaseAOLLoader ad = b2.getAd(this.s, this.r);
        if (a2 == null) {
            return null;
        }
        ad.a(a2.a(), a2.b());
        if (ad instanceof io.dcloud.h.c.b.a.b) {
            ((io.dcloud.h.c.b.a.b) ad).a(a2.d(), a2.c());
        }
        return ad;
    }

    public boolean b(int i2) {
        return this.e.b(Integer.valueOf(i2));
    }

    private void b(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < 70; i2++) {
            sb.append(str);
        }
        e.d(sb.toString());
    }

    public void a(int i2) {
        this.t = i2;
    }

    public void a(DCBaseAOLLoader dCBaseAOLLoader, List<? extends DCBaseAOL> list, io.dcloud.sdk.poly.base.config.b bVar) {
        if (this.u.equalsIgnoreCase(dCBaseAOLLoader.j()) && !this.p && !this.b) {
            this.d.decrementAndGet();
            this.f.add(dCBaseAOLLoader);
            this.n.add(bVar);
            f(dCBaseAOLLoader.k());
            e.d("level ad load success!current sub slot:" + dCBaseAOLLoader.getSlotId() + ";ss;" + dCBaseAOLLoader.k() + ";type;" + dCBaseAOLLoader.getType());
            if (m()) {
                this.i.put(dCBaseAOLLoader, list);
            }
            if (!m()) {
                if (!d()) {
                    boolean z = true;
                    if (this.l.size() > 1) {
                        if (!this.a) {
                            Iterator<Integer> it = this.l.iterator();
                            while (true) {
                                if (!it.hasNext()) {
                                    break;
                                }
                                Integer next = it.next();
                                if (dCBaseAOLLoader.k() > next.intValue()) {
                                    e.a("check ss smaller than this.ss:" + next + ";unfinished ss count:" + a(next));
                                    if (a(next) > 0) {
                                        z = false;
                                        break;
                                    }
                                }
                            }
                        }
                        if (z) {
                            this.j = dCBaseAOLLoader;
                            q();
                        }
                    } else {
                        this.j = dCBaseAOLLoader;
                        q();
                    }
                }
                r();
            } else if (this.a) {
                this.k.addAll(list);
                q();
                r();
            } else {
                o();
            }
            b(Operators.SUB);
        }
    }

    private int a(Integer num) {
        AtomicInteger atomicInteger = this.m.get(num);
        if (atomicInteger == null) {
            return 0;
        }
        return atomicInteger.get();
    }

    public void a(DCBaseAOLLoader dCBaseAOLLoader, io.dcloud.sdk.poly.base.config.b bVar) {
        if (this.u.equalsIgnoreCase(dCBaseAOLLoader.j()) && !this.p && !this.b) {
            this.d.decrementAndGet();
            this.g.add(dCBaseAOLLoader);
            this.n.add(bVar);
            f(dCBaseAOLLoader.k());
            e.b("level ad load fail.current sub slot:" + dCBaseAOLLoader.getSlotId() + ";ss:" + dCBaseAOLLoader.k() + ";type:" + dCBaseAOLLoader.getType());
            if (m()) {
                o();
            } else {
                if (this.l.size() > 1 && !this.a && !d() && this.f.size() > 0) {
                    for (Integer next : this.l) {
                        if (dCBaseAOLLoader.k() <= next.intValue()) {
                            Iterator<DCBaseAOL> it = this.f.iterator();
                            while (true) {
                                if (!it.hasNext()) {
                                    break;
                                }
                                DCBaseAOL next2 = it.next();
                                if (next2.k() == next.intValue()) {
                                    e.b("check ss large than this.slot:" + next2.getSlotId() + ";ss:" + next2.k());
                                    this.j = next2;
                                    q();
                                    break;
                                }
                            }
                            if (a(next) > 0) {
                                break;
                            }
                        } else if (dCBaseAOLLoader.k() > next.intValue()) {
                            e.b("check ss smaller than this.ss:" + next + ";unfinished ss count:" + a(next));
                            if (a(next) > 0) {
                                break;
                            }
                        } else {
                            continue;
                        }
                    }
                }
                r();
            }
            b(Operators.SUB);
        }
    }

    public void a(String str) {
        this.u = str;
    }

    public void a(boolean z) {
        this.v = z;
    }
}
