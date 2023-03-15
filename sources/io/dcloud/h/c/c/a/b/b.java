package io.dcloud.h.c.c.a.b;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import com.facebook.common.callercontext.ContextChain;
import com.taobao.weex.common.WXConfig;
import com.taobao.weex.ui.component.WXBasicComponentType;
import io.dcloud.common.util.CreateShortResultReceiver;
import io.dcloud.common.util.net.NetWork;
import io.dcloud.h.a.d.b.h;
import io.dcloud.h.a.d.b.i;
import io.dcloud.h.a.e.d;
import io.dcloud.h.c.c.a.e.c;
import io.dcloud.h.c.c.b.a;
import io.dcloud.sdk.poly.base.utils.e;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONObject;

public class b {

    class a implements a.b {
        final /* synthetic */ String a;
        final /* synthetic */ HashMap b;
        final /* synthetic */ String[] c;

        a(String str, HashMap hashMap, String[] strArr) {
            this.a = str;
            this.b = hashMap;
            this.c = strArr;
        }

        public void a(a.C0057a aVar) {
        }

        public boolean b(a.C0057a aVar) {
            byte[] a2 = d.a(aVar.b(), this.a, (HashMap<String, String>) this.b, this.c);
            if (a2 == null) {
                return false;
            }
            String str = new String(a2, StandardCharsets.UTF_8);
            if (TextUtils.isEmpty(str)) {
                return true;
            }
            e.c("uniAD-Commit_A", str);
            return true;
        }

        public void onNoOnePicked() {
        }
    }

    /* renamed from: io.dcloud.h.c.c.a.b.b$b  reason: collision with other inner class name */
    class C0056b implements a.b {
        final /* synthetic */ String a;
        final /* synthetic */ HashMap b;

        C0056b(String str, HashMap hashMap) {
            this.a = str;
            this.b = hashMap;
        }

        public void a(a.C0057a aVar) {
        }

        public boolean b(a.C0057a aVar) {
            byte[] a2 = d.a(aVar.b(), this.a, (HashMap<String, String>) this.b);
            if (a2 == null) {
                return false;
            }
            String str = new String(a2, StandardCharsets.UTF_8);
            if (TextUtils.isEmpty(str)) {
                return true;
            }
            e.c("uniAD-Commit_F", str);
            return true;
        }

        public void onNoOnePicked() {
        }
    }

    public static void a(Context context, String str, String str2, String str3, int i, String str4) {
        a(context, (String) null, (String) null, str2, str, str3, i, str4, (HashMap<String, Object>) null);
    }

    private static Map<String, Object> b(Context context, String str, String str2, int i, String str3, HashMap<String, Object> hashMap) {
        String str4;
        String str5;
        try {
            str4 = URLEncoder.encode(Build.MODEL, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            str4 = "";
        }
        try {
            str5 = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e2) {
            e2.printStackTrace();
            str5 = "";
        }
        String a2 = io.dcloud.h.c.c.a.e.e.a(context, true, true);
        HashMap hashMap2 = new HashMap();
        hashMap2.put(ContextChain.TAG_PRODUCT, "a");
        hashMap2.put("appid", str);
        hashMap2.put(CreateShortResultReceiver.KEY_VERSIONNAME, str5);
        hashMap2.put("at", Integer.valueOf(i));
        hashMap2.put("psdk", 0);
        hashMap2.put(WXConfig.os, Integer.valueOf(Build.VERSION.SDK_INT));
        hashMap2.put("adpid", str3);
        if (a2.endsWith("&ie=1")) {
            a2 = a2.replace("&ie=1", "");
            hashMap2.put("ie", 1);
        } else if (a2.endsWith("&ie=0")) {
            a2 = a2.replace("&ie=0", "");
            hashMap2.put("ie", 0);
        }
        hashMap2.put("imei", a2);
        hashMap2.put("md", str4);
        hashMap2.put("vd", Build.MANUFACTURER);
        hashMap2.put("net", h.d(context));
        hashMap2.put("vb", io.dcloud.h.c.c.a.e.a.a());
        hashMap2.put("t", Long.valueOf(System.currentTimeMillis()));
        hashMap2.put("mc", io.dcloud.h.c.c.a.e.a.a(context));
        hashMap2.put("paid", str2);
        if (hashMap != null) {
            hashMap2.putAll(hashMap);
        }
        return hashMap2;
    }

    public static void a(Context context, String str, String str2, String str3, String str4, String str5, int i, String str6, HashMap<String, Object> hashMap) {
        a(context, str4, str3, str5, i, (String) null, (String) null, (JSONObject) null, str, str2, str6, (String) null, hashMap);
    }

    public static void a(Context context, String str, String str2, String str3, int i, String str4, String str5, JSONObject jSONObject, String str6, String str7, String str8, String str9, HashMap<String, Object> hashMap) {
        JSONObject optJSONObject;
        int i2 = i;
        JSONObject jSONObject2 = jSONObject;
        String str10 = str6;
        String str11 = str7;
        Map<String, Object> b = b(context, str, str3, i, str8, hashMap);
        if (str10 != null) {
            b.put("mediaId", str10);
        }
        if (str11 != null) {
            b.put("slotId", str11);
        }
        if (i2 == 32) {
            String str12 = str4;
            b.put("dec", str4);
            b.put("dem", str5);
        }
        if (i2 == 41 && jSONObject2 != null && str10 == null) {
            if (jSONObject2.has(WXBasicComponentType.IMG)) {
                b.put(WXBasicComponentType.IMG, c.a(jSONObject2.optString(WXBasicComponentType.IMG)).toLowerCase(Locale.ENGLISH));
            }
            if (jSONObject2.has("dw")) {
                b.put("dw", jSONObject2.optString("dw"));
            }
            if (jSONObject2.has("dh")) {
                b.put("dh", jSONObject2.optString("dh"));
            }
            if (jSONObject2.has("click_coord") && (optJSONObject = jSONObject2.optJSONObject("click_coord")) != null) {
                b.put("click_coord", optJSONObject.toString());
            }
        }
        String str13 = str2;
        b.put("tid", str2);
        HashMap hashMap2 = new HashMap();
        hashMap2.put(NetWork.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
        String encodeToString = Base64.encodeToString(io.dcloud.h.a.d.b.a.b(i.a(new JSONObject(b).toString()), io.dcloud.h.c.c.a.e.b.b(), io.dcloud.h.c.c.a.e.b.a()), 2);
        String str14 = null;
        try {
            str14 = URLEncoder.encode(encodeToString, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        io.dcloud.h.c.c.b.a a2 = io.dcloud.h.c.c.b.a.a();
        List<a.C0057a> list = io.dcloud.h.c.c.b.b.a;
        Context context2 = context;
        a2.a(context, list, "CAA_" + i, new a("edata=" + str14, hashMap2, new String[1]));
    }

    public static void a(Context context, String str, String str2, int i, String str3, HashMap<String, Object> hashMap) {
        String str4;
        try {
            str4 = URLEncoder.encode(Base64.encodeToString(io.dcloud.h.a.d.b.a.b(i.a(new JSONObject(b(context, str, str2, i, str3, hashMap)).toString()), io.dcloud.h.c.c.a.e.b.b(), io.dcloud.h.c.c.a.e.b.a()), 2), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            str4 = null;
        }
        HashMap hashMap2 = new HashMap();
        hashMap2.put(NetWork.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
        io.dcloud.h.c.c.b.a.a().a(context, io.dcloud.h.c.c.b.b.f, "RSP", new C0056b("edata=" + str4, hashMap2));
    }
}
