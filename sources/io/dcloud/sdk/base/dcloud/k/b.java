package io.dcloud.sdk.base.dcloud.k;

import android.content.Context;
import io.dcloud.h.a.e.f;
import java.util.ArrayList;
import java.util.List;

public class b {
    private static b a;
    private static List<a> b = new ArrayList();

    private b() {
    }

    public static b a(Context context) {
        if (a == null) {
            synchronized (b.class) {
                if (a == null) {
                    a = new b();
                }
            }
        }
        return a;
    }

    public void b(a aVar) {
        b.remove(aVar);
    }

    public void a(a aVar) {
        b.add(aVar);
        f.a().a(aVar);
    }

    public List<a> a() {
        return b;
    }
}
