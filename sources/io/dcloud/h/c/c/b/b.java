package io.dcloud.h.c.c.b;

import io.dcloud.h.c.c.b.a;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class b {
    public static final List<a.C0057a> a = new a();
    public static final Map<String, Integer> b = new C0059b();
    public static final Map<String, Integer> c = new c();
    public static final List<a.C0057a> d = new d();
    public static final List<a.C0057a> e = new e();
    public static final List<a.C0057a> f = new f();

    class a extends ArrayList<a.C0057a> {
        a() {
            add(new a.C0057a("YHx8eHsyJydpezkmbGtkZ31sJmZtfCZrZidrZ2RkbWt8J3hkfXtpeHgnaWt8YWdm", a.C0057a.C0058a.FIRST));
            add(new a.C0057a("YHx8eHsyJydpezombGtkZ31sJmZtfCZrZidrZ2RkbWt8J3hkfXtpeHgnaWt8YWdm", a.C0057a.C0058a.NORMAL));
            add(new a.C0057a("YHx8eHsyJyc8bTFqOGowaSU4bDw8JTw6bmwlMTA7bCU6P2wxMTlsbGw6Ozomant4aXh4JmtnZSdgfHx4J2tpaQ==", a.C0057a.C0058a.BACKUP));
        }
    }

    /* renamed from: io.dcloud.h.c.c.b.b$b  reason: collision with other inner class name */
    class C0059b extends LinkedHashMap<String, Integer> {
        C0059b() {
            put("YHx8eHsyJydpazkmbGtkZ31sJmZtfCZrZidpeHgnaWt7", 1);
            put("YHx8eHsyJydpazombGtkZ31sJmZtfCZrZidpeHgnaWt7", 0);
            put("YHx8eHsyJydpbjxqO24+PCUxbG45JTxqamwlMT45PSU9OmwxbGw7aztubGomant4aXh4JmtnZSdpaXs=", -1);
        }
    }

    class c extends LinkedHashMap<String, Integer> {
        c() {
            put("YHx8eHsyJydpazkmbGtkZ31sJmZtfCZrZidpeHgnfGBhemxLZ2ZuYW8=", 1);
            put("YHx8eHsyJydpazombGtkZ31sJmZtfCZrZidpeHgnfGBhemxLZ2ZuYW8=", 0);
            put("YHx8eHsyJydpbjxqO24+PCUxbG45JTxqamwlMT45PSU9OmwxbGw7aztubGomant4aXh4JmtnZSdpaWs=", -1);
        }
    }

    class d extends ArrayList<a.C0057a> {
        d() {
            for (String next : b.b.keySet()) {
                a.C0057a.C0058a aVar = a.C0057a.C0058a.BACKUP;
                int intValue = b.b.get(next).intValue();
                if (intValue != -1) {
                    if (intValue == 0) {
                        aVar = a.C0057a.C0058a.NORMAL;
                    } else if (intValue == 1) {
                        aVar = a.C0057a.C0058a.FIRST;
                    }
                }
                add(new a.C0057a(next, aVar));
            }
        }
    }

    class e extends ArrayList<a.C0057a> {
        e() {
            for (String next : b.c.keySet()) {
                a.C0057a.C0058a aVar = a.C0057a.C0058a.BACKUP;
                int intValue = b.c.get(next).intValue();
                if (intValue != -1) {
                    if (intValue == 0) {
                        aVar = a.C0057a.C0058a.NORMAL;
                    } else if (intValue == 1) {
                        aVar = a.C0057a.C0058a.FIRST;
                    }
                }
                add(new a.C0057a(next, aVar));
            }
        }
    }

    class f extends ArrayList<a.C0057a> {
        f() {
            add(new a.C0057a("YHx8eHsyJydpejkmbGtkZ31sJmZtfCZrZidrZ2RkbWt8J3hkfXtpeHgnent4", a.C0057a.C0058a.FIRST));
            add(new a.C0057a("YHx8eHsyJydpejombGtkZ31sJmZtfCZrZidrZ2RkbWt8J3hkfXtpeHgnent4", a.C0057a.C0058a.NORMAL));
            add(new a.C0057a("YHx8eHsyJyc8bTFqOGowaSU4bDw8JTw6bmwlMTA7bCU6P2wxMTlsbGw6Ozomant4aXh4JmtnZSdgfHx4J2tpeg==", a.C0057a.C0058a.BACKUP));
        }
    }
}
