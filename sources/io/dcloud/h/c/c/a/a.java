package io.dcloud.h.c.c.a;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.facebook.common.callercontext.ContextChain;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.ui.component.WXComponent;
import io.dcloud.h.a.e.e;
import io.dcloud.h.c.c.a.e.d;
import io.dcloud.h.c.c.b.c;
import io.dcloud.sdk.core.util.AdErrorUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class a implements c.a {
    private static volatile a a;
    private final AtomicBoolean b = new AtomicBoolean(false);
    private final Queue<C0055a> c = new ConcurrentLinkedQueue();
    private final Map<String, io.dcloud.h.c.c.a.c.a> d = new HashMap();
    private JSONObject e;
    private final Map<String, io.dcloud.sdk.poly.base.config.a> f = new HashMap();
    private String g = "";
    private Context h;
    private final String i = "uniad_config";
    private final String j = "S_C";
    private final String k = "pap";
    private final String l = "dpap";

    /* renamed from: io.dcloud.h.c.c.a.a$a  reason: collision with other inner class name */
    public static abstract class C0055a {
        String a;

        public C0055a(String str) {
            this.a = str;
        }

        public abstract void a(int i, String str);

        public abstract void a(JSONObject jSONObject);
    }

    public static abstract class b extends C0055a {
        /* access modifiers changed from: private */
        public boolean b = false;

        public b(String str) {
            super(str);
        }

        public abstract void a(JSONArray jSONArray);

        public abstract void a(JSONArray jSONArray, boolean z);

        public void a(JSONObject jSONObject) {
            this.b = true;
        }

        public void a(int i, String str) {
            this.b = true;
        }

        public boolean a() {
            return this.b;
        }
    }

    private a() {
    }

    public static a a() {
        if (a == null) {
            synchronized (a.class) {
                if (a == null) {
                    a = new a();
                    a aVar = a;
                    return aVar;
                }
            }
        }
        return a;
    }

    private JSONObject b(Context context) {
        try {
            Bundle bundle = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData;
            Set<String> keySet = bundle.keySet();
            JSONObject jSONObject = new JSONObject();
            JSONObject jSONObject2 = new JSONObject();
            JSONObject jSONObject3 = new JSONObject();
            for (String str : keySet) {
                if (str.startsWith("UNIAD")) {
                    if (str.endsWith("APPID")) {
                        a(bundle, str, jSONObject);
                    } else if (str.endsWith("SPLASH")) {
                        a(bundle, str, jSONObject3);
                    } else if (str.endsWith("APPKEY")) {
                        a(bundle, str, jSONObject2);
                    } else if (str.endsWith("ADPID")) {
                        a(bundle, str, jSONObject3);
                    }
                }
            }
            if (jSONObject3.length() > 0) {
                Iterator<String> keys = jSONObject3.keys();
                StringBuilder sb = new StringBuilder();
                sb.append(keys.next());
                while (keys.hasNext()) {
                    sb.append(",");
                    sb.append(keys.next());
                }
                jSONObject3.put("_psp_", sb.toString());
                jSONObject3.put("_adpid_", jSONObject3.optString("adpid"));
                jSONObject3.put("_fs_", bundle.getString("UNIAD_FULL_SPLASH", WXInstanceApm.VALUE_ERROR_CODE_DEFAULT));
            }
            io.dcloud.h.c.c.a.d.a aVar = new io.dcloud.h.c.c.a.d.a();
            aVar.put("697878616C", (Object) jSONObject);
            aVar.put("697878436D71", (Object) jSONObject2);
            aVar.put("7B7864697B60", (Object) jSONObject3);
            return b(aVar);
        } catch (Exception unused) {
            return null;
        }
    }

    public void a(Context context, int i2, C0055a aVar) {
        JSONObject jSONObject;
        if (TextUtils.isEmpty(aVar.a) && i2 != 1 && i2 != 3) {
            aVar.a(-5001, AdErrorUtil.getErrorMsg(-5001));
        } else if (i2 == 3) {
            JSONObject jSONObject2 = this.e;
            if (jSONObject2 == null || !jSONObject2.has("frsplash")) {
                aVar.a(-5015, AdErrorUtil.getErrorMsg(-5015));
            } else {
                aVar.a(this.e.optJSONObject("frsplash"));
            }
        } else if (i2 == 1) {
            JSONObject jSONObject3 = this.e;
            io.dcloud.h.c.c.a.d.a aVar2 = null;
            if (jSONObject3 == null) {
                String a2 = e.a(context, "uniad_config", "S_C");
                if (TextUtils.isEmpty(a2)) {
                    JSONObject b2 = b(context);
                    if (b2 == null) {
                        aVar.a(-5000, "");
                    } else {
                        aVar.a(b2);
                    }
                } else {
                    try {
                        aVar2 = new io.dcloud.h.c.c.a.d.a(d.c(a2));
                    } catch (JSONException unused) {
                    }
                    if (aVar2 != null && aVar2.length() > 0) {
                        if (aVar2.has("7B78") || aVar2.has("697878436D71")) {
                            JSONObject b3 = b(aVar2);
                            if (b3 != null) {
                                aVar.a(b3);
                            }
                        } else {
                            a(aVar2.c("697878616C"));
                            if (io.dcloud.sdk.poly.base.utils.a.d(context)) {
                                JSONObject c2 = aVar2.c("7B7864697B60");
                                if (c2 != null) {
                                    aVar.a(c2);
                                }
                            } else {
                                JSONObject c3 = aVar2.c("7B7864697B60");
                                if (c3 != null) {
                                    aVar.a(c3);
                                }
                            }
                        }
                    }
                }
                this.c.add(aVar);
                if (!this.b.get()) {
                    a(context, i2);
                }
            } else if (!jSONObject3.has("splash")) {
                aVar.a(-5019, AdErrorUtil.getErrorMsg(-5019));
            } else if (io.dcloud.sdk.poly.base.utils.a.d(context)) {
                aVar.a(this.e.optJSONObject("splash"));
                if (aVar instanceof b) {
                    ((b) aVar).a((JSONArray) null, true);
                }
            } else {
                aVar.a(this.e.optJSONObject("splash"));
                if (aVar instanceof b) {
                    ((b) aVar).a((JSONArray) null, true);
                }
            }
        } else if (this.f.size() <= 0 || (jSONObject = this.e) == null || jSONObject.length() <= 0) {
            this.c.add(aVar);
            if (!this.b.get()) {
                a(context, i2);
            }
        } else if (TextUtils.isEmpty(aVar.a)) {
            aVar.a(-5001, AdErrorUtil.getErrorMsg(-5001));
        } else if (this.e.has(aVar.a)) {
            aVar.a(this.e.optJSONObject(aVar.a));
        } else {
            aVar.a(-5002, AdErrorUtil.getErrorMsg(-5002) + aVar.a);
        }
    }

    private JSONObject b(io.dcloud.h.c.c.a.d.a aVar) {
        io.dcloud.sdk.poly.base.config.a aVar2;
        JSONObject b2 = aVar.b("appid");
        JSONObject b3 = aVar.b("appKey");
        JSONObject b4 = aVar.b("splash");
        JSONObject b5 = aVar.b("sp");
        HashMap hashMap = new HashMap();
        if (b2 != null) {
            Iterator<String> keys = b2.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                io.dcloud.sdk.poly.base.config.a aVar3 = new io.dcloud.sdk.poly.base.config.a();
                aVar3.c(next);
                aVar3.a(b2.optString(next));
                if (b3 != null && b3.has(next)) {
                    aVar3.b(b2.optString(next));
                }
                hashMap.put(next, aVar3);
            }
        }
        if (b5 != null) {
            Iterator<String> keys2 = b5.keys();
            while (keys2.hasNext()) {
                String next2 = keys2.next();
                JSONObject optJSONObject = b5.optJSONObject(next2);
                if (optJSONObject != null) {
                    if (hashMap.containsKey(next2)) {
                        aVar2 = (io.dcloud.sdk.poly.base.config.a) hashMap.get(next2);
                    } else {
                        aVar2 = new io.dcloud.sdk.poly.base.config.a();
                        aVar2.c(next2);
                    }
                    io.dcloud.h.c.c.a.c.a aVar4 = new io.dcloud.h.c.c.a.c.a();
                    aVar4.a(next2);
                    aVar4.a(optJSONObject);
                    aVar2.a(aVar4);
                    hashMap.put(next2, aVar2);
                }
            }
        }
        this.f.putAll(hashMap);
        JSONObject jSONObject = null;
        if (b4 != null) {
            String optString = b4.optString("_psp_");
            if (TextUtils.isEmpty(optString)) {
                return null;
            }
            jSONObject = new JSONObject();
            try {
                jSONObject.put("adpid", b4.optString("_adpid_"));
                jSONObject.put("type", 1);
                jSONObject.put("cpadpid", b4.optString("cp_adp_id"));
                jSONObject.put("fs", b4.optString("_fs_"));
                jSONObject.put("fwt", b4.optString("_fwt_"));
                jSONObject.put("ord", b4.optString("_ord_"));
                jSONObject.put("sr", b4.optString("_sr_"));
                JSONObject optJSONObject2 = b4.optJSONObject("_w_");
                if (optJSONObject2 == null) {
                    optJSONObject2 = new JSONObject();
                }
                JSONObject optJSONObject3 = b4.optJSONObject("_m_");
                if (optJSONObject3 == null) {
                    optJSONObject3 = new JSONObject();
                }
                String[] split = TextUtils.split(optString, ",");
                JSONArray jSONArray = new JSONArray();
                for (String str : split) {
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put(ContextChain.TAG_PRODUCT, str);
                    jSONObject2.put("sid", b4.optString(str));
                    if (optJSONObject2.has(str)) {
                        jSONObject2.put(WXComponent.PROP_FS_WRAP_CONTENT, optJSONObject2.optString(str));
                    }
                    if (optJSONObject3.has(str)) {
                        jSONObject2.put(WXComponent.PROP_FS_MATCH_PARENT, optJSONObject3.optString(str));
                    }
                    jSONArray.put(jSONObject2);
                }
                jSONObject.put("cfgs", jSONArray);
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
        return jSONObject;
    }

    private void a(Context context, int i2) {
        this.h = context.getApplicationContext();
        this.b.set(true);
        io.dcloud.h.c.a.d().a().a(context, i2, this);
    }

    public void a(io.dcloud.h.c.c.a.d.a aVar) {
        this.b.set(false);
        io.dcloud.sdk.poly.base.utils.a.b(this.h, aVar.optString("786978", WXInstanceApm.VALUE_ERROR_CODE_DEFAULT));
        String optString = aVar.optString("6C786978", WXInstanceApm.VALUE_ERROR_CODE_DEFAULT);
        this.g = optString;
        e.a(this.h, "uniad_config", "dpap", optString);
        io.dcloud.h.c.c.a.d.a a2 = aVar.optJSONObject("6C697C69");
        JSONObject jSONObject = null;
        if (a2 == null || !a2.has("7D6661696C")) {
            Iterator it = this.c.iterator();
            while (it.hasNext()) {
                C0055a aVar2 = (C0055a) it.next();
                if (aVar2 instanceof b) {
                    b bVar = (b) aVar2;
                    if (!bVar.a()) {
                        aVar2.a(-5007, AdErrorUtil.getErrorMsg(-5007));
                    }
                    bVar.a((JSONArray) null, false);
                } else {
                    aVar2.a(-5007, AdErrorUtil.getErrorMsg(-5007));
                }
                it.remove();
            }
            return;
        }
        io.dcloud.h.c.c.a.d.a a3 = a2.optJSONObject("7D6661696C");
        if (a3 == null || a3.length() <= 0) {
            Iterator it2 = this.c.iterator();
            while (it2.hasNext()) {
                C0055a aVar3 = (C0055a) it2.next();
                if (aVar3 instanceof b) {
                    b bVar2 = (b) aVar3;
                    if (!bVar2.a()) {
                        aVar3.a(-5007, AdErrorUtil.getErrorMsg(-5007));
                    }
                    bVar2.a((JSONArray) null, false);
                } else {
                    aVar3.a(-5007, AdErrorUtil.getErrorMsg(-5007));
                }
                it2.remove();
            }
            return;
        }
        JSONObject c2 = a3.has("6B606966666D64") ? a3.c("6B606966666D64") : new JSONObject();
        this.f.clear();
        a(c2);
        JSONArray optJSONArray = a3.has("787A") ? a3.optJSONArray("787A") : null;
        JSONObject c3 = a3.c("696C78616C7B");
        this.e = c3;
        if (c3 != null) {
            jSONObject = c3.optJSONObject("splash");
        }
        io.dcloud.h.c.c.a.d.a aVar4 = new io.dcloud.h.c.c.a.d.a();
        try {
            aVar4.put("697878616C", (Object) c2);
            aVar4.put("7B7864697B60", (Object) jSONObject);
        } catch (JSONException unused) {
        }
        a(aVar4.toString(), this.h);
        JSONArray optJSONArray2 = aVar.optJSONArray("787B697B");
        ArrayList<b> arrayList = new ArrayList<>();
        JSONObject jSONObject2 = this.e;
        if (jSONObject2 == null || jSONObject2.length() <= 0) {
            Iterator it3 = this.c.iterator();
            while (it3.hasNext()) {
                C0055a aVar5 = (C0055a) it3.next();
                if (aVar5 instanceof b) {
                    arrayList.add((b) aVar5);
                } else {
                    aVar5.a(-5007, AdErrorUtil.getErrorMsg(-5007));
                }
                it3.remove();
            }
        } else {
            Iterator it4 = this.c.iterator();
            while (it4.hasNext()) {
                C0055a aVar6 = (C0055a) it4.next();
                if (aVar6 instanceof b) {
                    arrayList.add((b) aVar6);
                } else if (this.e.has(aVar6.a)) {
                    aVar6.a(this.e.optJSONObject(aVar6.a));
                } else {
                    aVar6.a(-5002, AdErrorUtil.getErrorMsg(-5002) + aVar6.a);
                }
                it4.remove();
            }
        }
        for (b bVar3 : arrayList) {
            if (!bVar3.b) {
                if (!io.dcloud.sdk.poly.base.utils.a.d(this.h)) {
                    bVar3.a(jSONObject);
                } else if (jSONObject == null || jSONObject.length() <= 0) {
                    bVar3.a(-5007, AdErrorUtil.getErrorMsg(-5007));
                } else {
                    bVar3.a(jSONObject);
                }
            }
            bVar3.a(optJSONArray2, true);
            bVar3.a(optJSONArray);
        }
    }

    private void a(JSONObject jSONObject) {
        if (jSONObject != null) {
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                JSONObject optJSONObject = jSONObject.optJSONObject(next);
                io.dcloud.sdk.poly.base.config.a aVar = new io.dcloud.sdk.poly.base.config.a();
                aVar.c(next);
                if (aVar.a(optJSONObject)) {
                    this.f.put(next, aVar);
                } else {
                    this.f.remove(next);
                }
            }
        }
    }

    public void a(int i2, String str) {
        int i3;
        String str2;
        String str3;
        this.e = new JSONObject();
        e.a(this.h, "uniad_config", "S_C", "");
        this.b.set(false);
        try {
            i3 = Integer.parseInt(str);
        } catch (Exception unused) {
            i3 = -1;
        }
        Iterator it = this.c.iterator();
        while (it.hasNext()) {
            C0055a aVar = (C0055a) it.next();
            if (aVar instanceof b) {
                b bVar = (b) aVar;
                if (!bVar.a()) {
                    if (i3 != -1) {
                        str3 = "http:" + str;
                    } else {
                        str3 = str;
                    }
                    aVar.a(-5007, str3);
                }
                bVar.a((JSONArray) null, false);
            } else {
                if (i3 != -1) {
                    str2 = "http:" + str;
                } else {
                    str2 = str;
                }
                aVar.a(-5007, str2);
            }
            it.remove();
        }
    }

    private void a(String str, Context context) {
        if (context != null) {
            e.a(context, "uniad_config", "S_C", d.e(str));
        }
    }

    private void a(Bundle bundle, String str, JSONObject jSONObject) {
        String string = bundle.getString(str);
        if (!TextUtils.isEmpty(string)) {
            String[] split = string.split("_");
            if (split.length > 1 && !TextUtils.isEmpty(split[0]) && !TextUtils.isEmpty(split[1])) {
                try {
                    jSONObject.put(split[0], split[1]);
                } catch (JSONException unused) {
                }
            }
        }
    }

    public io.dcloud.sdk.poly.base.config.a a(String str) {
        if (this.f.size() == 0 || TextUtils.isEmpty(str) || !this.f.containsKey(str)) {
            return null;
        }
        return this.f.get(str);
    }

    public boolean a(Context context) {
        if (TextUtils.isEmpty(this.g)) {
            this.g = e.a(context, "uniad_config", "dpap");
        }
        return "1".equals(this.g);
    }
}
