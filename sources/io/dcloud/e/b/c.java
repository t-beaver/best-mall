package io.dcloud.e.b;

import android.app.Activity;
import android.text.TextUtils;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.util.BaseInfo;
import java.util.ArrayList;
import java.util.Iterator;

class c {
    a a = null;
    private ArrayList<e> b = new ArrayList<>();
    private ArrayList<String> c;

    c(a aVar) {
        ArrayList<String> arrayList = new ArrayList<>();
        BaseInfo.sRunningApp = arrayList;
        this.c = arrayList;
        this.a = aVar;
    }

    /* access modifiers changed from: package-private */
    public boolean a(e eVar, ISysEventListener.SysEventType sysEventType, Object obj) {
        boolean z = eVar == null;
        int size = this.b.size();
        e eVar2 = null;
        int i = size - 1;
        boolean z2 = false;
        while (true) {
            if (i < 0) {
                break;
            }
            e eVar3 = this.b.get(i);
            if (!z ? eVar3 == eVar : z) {
                z2 |= eVar3.onExecute(sysEventType, obj);
                if (z2 && !e.a(sysEventType)) {
                    eVar2 = eVar3;
                    break;
                }
                eVar2 = eVar3;
            }
            i--;
        }
        if (z2 || !sysEventType.equals(ISysEventListener.SysEventType.onKeyUp) || size <= 1 || eVar2 == null || ((Integer) ((Object[]) obj)[0]).intValue() != 4) {
            return z2;
        }
        this.a.processEvent(IMgr.MgrType.WindowMgr, 20, eVar2);
        return true;
    }

    /* access modifiers changed from: package-private */
    public e b(String str) {
        e eVar;
        Iterator<e> it = this.b.iterator();
        while (true) {
            if (!it.hasNext()) {
                eVar = null;
                break;
            }
            eVar = it.next();
            if (TextUtils.equals(eVar.obtainAppId(), str)) {
                break;
            }
        }
        Logger.d("AppCache", "removeWebApp " + eVar + ";mAppIdList=" + this.c);
        this.b.remove(eVar);
        this.c.remove(str);
        return eVar;
    }

    public e c() {
        ArrayList<e> arrayList = this.b;
        if (arrayList != null && arrayList.size() == 1) {
            return this.b.get(0);
        }
        ArrayList<e> arrayList2 = this.b;
        if (arrayList2 == null || arrayList2.size() < 1) {
            return null;
        }
        e eVar = this.b.get(0);
        long j = eVar.h1;
        for (int i = 1; i < this.b.size(); i++) {
            e eVar2 = this.b.get(i);
            long j2 = eVar2.h1;
            if (j < j2) {
                eVar = eVar2;
                j = j2;
            }
        }
        return eVar;
    }

    public e d() {
        long currentTimeMillis = System.currentTimeMillis();
        e eVar = null;
        for (int i = 0; i < this.b.size(); i++) {
            e eVar2 = this.b.get(i);
            long j = eVar2.h1;
            if (j < currentTimeMillis) {
                eVar = eVar2;
                currentTimeMillis = j;
            }
        }
        return eVar;
    }

    /* access modifiers changed from: protected */
    public int e() {
        return this.b.size();
    }

    /* access modifiers changed from: protected */
    public e b() {
        long j = 0;
        e eVar = null;
        for (int size = this.b.size() - 1; size >= 0; size--) {
            e eVar2 = this.b.get(size);
            if (eVar2.u == 3) {
                long j2 = eVar2.h1;
                if (j2 > j) {
                    eVar = eVar2;
                    j = j2;
                }
            }
        }
        return eVar;
    }

    /* access modifiers changed from: protected */
    public e a(String str) {
        int indexOf = this.c.indexOf(str);
        if (indexOf >= 0) {
            return this.b.get(indexOf);
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void a(String str, e eVar) {
        this.c.add(str);
        this.b.add(eVar);
    }

    /* access modifiers changed from: protected */
    public e a(Activity activity, e eVar) {
        if (this.b.contains(eVar)) {
            return null;
        }
        System.currentTimeMillis();
        if (this.b.size() >= BaseInfo.s_Runing_App_Count_Max) {
            return d();
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void a() {
        this.b.clear();
        this.c.clear();
    }
}
