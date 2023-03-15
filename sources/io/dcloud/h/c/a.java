package io.dcloud.h.c;

import android.content.Context;
import io.dcloud.h.c.c.a.a;
import io.dcloud.h.c.c.b.c;
import io.dcloud.sdk.core.DCloudAOLManager;
import io.dcloud.sdk.core.adapter.IAdAdapter;
import io.dcloud.sdk.core.api.AOLLoader;
import io.dcloud.sdk.core.util.AdUtil;
import io.dcloud.sdk.poly.base.utils.PrivacyManager;
import java.util.Map;
import org.json.JSONObject;

public class a implements AOLLoader {
    private static a a;
    private DCloudAOLManager.InitConfig b;
    private c c;
    private Context d;
    private io.dcloud.h.c.c.c.a e = new io.dcloud.h.c.c.c.a();

    /* renamed from: io.dcloud.h.c.a$a  reason: collision with other inner class name */
    class C0053a extends a.C0055a {
        C0053a(String str) {
            super(str);
        }

        public void a(int i, String str) {
        }

        public void a(JSONObject jSONObject) {
        }
    }

    private a() {
    }

    public static a d() {
        if (a == null) {
            synchronized (a.class) {
                if (a == null) {
                    a aVar = new a();
                    a = aVar;
                    aVar.c = new io.dcloud.h.c.c.a.b.c();
                }
            }
        }
        return a;
    }

    public void a(DCloudAOLManager.InitConfig initConfig) {
        this.b = initConfig;
    }

    public DCloudAOLManager.InitConfig b() {
        return this.b;
    }

    public Context c() {
        return this.d;
    }

    public boolean getPersonalAd(Context context) {
        return AdUtil.getPersonalAd(context);
    }

    public void setPersonalAd(Context context, boolean z) {
        AdUtil.setPersonalAd(context, z);
        Map<String, IAdAdapter> a2 = io.dcloud.sdk.core.b.a.b().a();
        for (String str : a2.keySet()) {
            a2.get(str).setPersonalAd(z);
        }
    }

    public void setPrivacyConfig(DCloudAOLManager.PrivacyConfig privacyConfig) {
        PrivacyManager.getInstance().updateConfig(privacyConfig);
    }

    public c a() {
        return this.c;
    }

    public void b(Context context) {
        this.d = context;
    }

    public void a(c cVar) {
        this.c = cVar;
    }

    public void a(Context context) {
        io.dcloud.h.c.c.a.a.a().a(context, 1, (a.C0055a) new C0053a(""));
    }
}
