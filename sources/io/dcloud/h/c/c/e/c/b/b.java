package io.dcloud.h.c.c.e.c.b;

import android.app.Activity;
import io.dcloud.h.c.c.e.c.c.a;
import io.dcloud.sdk.core.module.DCBaseAOL;
import java.util.ArrayList;
import java.util.List;

public class b extends io.dcloud.h.c.c.e.c.c.b {
    public b(Activity activity) {
        super(activity, 10);
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
