package io.dcloud.e.c;

import io.dcloud.common.DHInterface.IPdrModule;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class e {
    private static e a;
    private volatile ConcurrentMap<String, IPdrModule> b = new ConcurrentHashMap();

    private e() {
    }

    public static e a() {
        if (a == null) {
            a = new e();
        }
        return a;
    }

    public void b() {
        for (String remove : this.b.keySet()) {
            IPdrModule iPdrModule = (IPdrModule) this.b.remove(remove);
            if (iPdrModule != null) {
                iPdrModule.onDestroy();
            }
        }
    }

    public void a(Map<String, Class<? extends IPdrModule>> map) {
        if (map != null) {
            for (String next : map.keySet()) {
                a(next, map.get(next));
            }
        }
    }

    public void a(String str, Class<? extends IPdrModule> cls) {
        if (cls != null) {
            try {
                this.b.put(str, (IPdrModule) cls.newInstance());
            } catch (IllegalAccessException | InstantiationException unused) {
            }
        }
    }

    public IPdrModule a(String str) {
        if (this.b.containsKey(str)) {
            return (IPdrModule) this.b.get(str);
        }
        return null;
    }
}
