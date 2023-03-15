package io.dcloud.h.c.c.a.b;

import android.content.Context;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.feature.gg.dcloud.ADHandler;
import io.dcloud.h.c.a;
import io.dcloud.h.c.c.b.c;
import io.dcloud.sdk.core.util.MainHandlerUtil;
import io.dcloud.sdk.poly.base.utils.b;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class d extends a {
    c.a[] a;
    Context b;
    boolean c = false;
    JSONObject d = new JSONObject();

    public d() {
        if (DHFile.hasFile()) {
            b.a(true);
        }
    }

    public void a(Context context, int i, c.a... aVarArr) {
        this.c = true;
        if (i == 1) {
            this.a = aVarArr;
            this.b = context;
            return;
        }
        MainHandlerUtil.getMainHandler().post(new Runnable(context) {
            public final /* synthetic */ Context f$0;

            {
                this.f$0 = r1;
            }

            public final void run() {
                ADHandler.pull(this.f$0, a.d().b().getAppId());
            }
        });
    }

    public HashMap<String, Object> b(Context context) {
        return e.a(context);
    }

    public void a(JSONObject jSONObject) {
        String[] strArr;
        Context context;
        JSONArray names = jSONObject.names();
        io.dcloud.h.c.c.a.d.a aVar = null;
        if (names != null) {
            strArr = new String[names.length()];
            for (int i = 0; i < names.length(); i++) {
                strArr[i] = names.optString(i);
            }
        } else {
            strArr = null;
        }
        if (strArr != null) {
            try {
                aVar = new io.dcloud.h.c.c.a.d.a(jSONObject, strArr);
            } catch (JSONException unused) {
            }
        }
        if (!(aVar == null || !aVar.has("7C61656D") || (context = this.b) == null)) {
            a(aVar, context);
        }
        c.a[] aVarArr = this.a;
        if (aVarArr != null) {
            for (c.a aVar2 : aVarArr) {
                if (aVar == null) {
                    aVar2.a(-5007, "数据解析异常");
                } else {
                    aVar2.a(aVar);
                }
            }
        }
    }

    public void a(int i, String str) {
        c.a[] aVarArr = this.a;
        if (aVarArr != null) {
            for (c.a a2 : aVarArr) {
                a2.a(i, str);
            }
        }
    }
}
