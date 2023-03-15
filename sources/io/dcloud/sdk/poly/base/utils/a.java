package io.dcloud.sdk.poly.base.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.taobao.weex.performance.WXInstanceApm;
import io.dcloud.feature.gg.AdSplashUtil;
import io.dcloud.h.a.e.e;
import io.dcloud.sdk.base.dcloud.h;
import io.dcloud.sdk.core.module.DCBaseAOL;
import java.io.File;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class a {

    /* renamed from: io.dcloud.sdk.poly.base.utils.a$a  reason: collision with other inner class name */
    public static class C0077a {
        public boolean a = true;
        public int b;
        public int c;
        public List<DCBaseAOL> d;
    }

    static class b {
        private double a;
        private double b;
        private String c;

        public String a() {
            return this.c;
        }

        public void a(double d, double d2, String str) {
            this.a = d;
            this.b = d2;
            this.c = str;
        }

        public boolean a(double d) {
            return d >= this.a && d < this.b;
        }
    }

    private static void a(Context context, Boolean bool) {
        String replaceAll = e(context).replaceAll("/ad/", "/");
        try {
            h.a((bool.booleanValue() ? "1" : WXInstanceApm.VALUE_ERROR_CODE_DEFAULT).getBytes(), 0, replaceAll + "AdEnable.dat");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void b(Context context, String str) {
        if (!TextUtils.isEmpty(str)) {
            boolean z = true;
            if (Integer.parseInt(str) != 1) {
                z = false;
            }
            a(context, Boolean.valueOf(z));
        }
    }

    public static String c(Context context) {
        return e.a(context, AdSplashUtil.showCountADReward, "servertime");
    }

    public static boolean d(Context context) {
        try {
            String f = f(context);
            if (f == null) {
                return b(context).booleanValue();
            }
            return "1".equals(f.replaceAll("\n", ""));
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    private static String e(Context context) {
        File externalCacheDir = context.getExternalCacheDir();
        if (externalCacheDir == null) {
            return "/sdcard/Android/data/" + context.getPackageName() + "/cache/ad/";
        }
        return externalCacheDir.getAbsolutePath() + "/ad/";
    }

    public static String f(Context context) {
        String str = e(context).replaceAll("/ad/", "/") + "AdEnable.dat";
        try {
            if (h.a(str)) {
                return new String(h.e(str));
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Boolean b(Context context) {
        try {
            return Boolean.valueOf(context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData.getBoolean("DCLOUD_AD_SPLASH", false));
        } catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }

    public static void a(Context context, String str, String str2) {
        JSONObject jSONObject;
        String a = e.a(context, AdSplashUtil.showCountADReward, str);
        try {
            if (TextUtils.isEmpty(a)) {
                jSONObject = new JSONObject();
            } else {
                jSONObject = new JSONObject(a);
            }
            if (jSONObject.has(str2)) {
                jSONObject.put(str2, jSONObject.optInt(str2) + 1);
            } else {
                jSONObject.put(str2, 1);
            }
            e.a(context, AdSplashUtil.showCountADReward, str, jSONObject.toString());
        } catch (JSONException unused) {
        }
    }

    public static void a(Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences(AdSplashUtil.showCountADReward, 0).edit();
        edit.clear();
        edit.apply();
    }

    public static void a(Context context, String str) {
        e.a(context, AdSplashUtil.showCountADReward, "servertime", str);
    }

    public static boolean a(long j, long j2) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(j);
        GregorianCalendar gregorianCalendar2 = new GregorianCalendar();
        gregorianCalendar2.setTimeInMillis(j2);
        if (gregorianCalendar.get(1) == gregorianCalendar2.get(1) && gregorianCalendar.get(2) == gregorianCalendar2.get(2) && gregorianCalendar.get(5) == gregorianCalendar2.get(5)) {
            return true;
        }
        return false;
    }

    public static List<String> a(Context context, List<String> list, String str, JSONObject jSONObject, JSONObject jSONObject2) {
        String str2;
        int i;
        e.a(String.valueOf(str) + "---STA---" + list.toString());
        if (jSONObject != null && jSONObject.length() > 0 && list.size() > 1) {
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                if (!list.contains(next)) {
                    keys.remove();
                } else if (jSONObject.optInt(next) <= 0) {
                    keys.remove();
                }
            }
            ArrayList arrayList = new ArrayList(a(jSONObject));
            if (list.size() != arrayList.size()) {
                for (String next2 : list) {
                    if (!arrayList.contains(next2)) {
                        arrayList.add(next2);
                    }
                }
            }
            list = arrayList;
        }
        e.a(String.valueOf(str) + "---PRE---" + list.toString());
        if (jSONObject2 != null && jSONObject2.length() > 0 && list.size() > 1) {
            JSONObject jSONObject3 = null;
            try {
                jSONObject3 = new JSONObject(e.a(context, AdSplashUtil.showCountADReward, str));
            } catch (Exception unused) {
            }
            boolean z = true;
            int i2 = 0;
            while (jSONObject3 != null && z && i2 < list.size()) {
                i2++;
                JSONArray optJSONArray = jSONObject2.optJSONArray(list.get(0));
                if (optJSONArray == null || optJSONArray.length() <= 1) {
                    str2 = "";
                    i = 0;
                } else {
                    str2 = optJSONArray.optString(0);
                    i = optJSONArray.optInt(1);
                }
                if (i > jSONObject3.optInt(str2) || i == 0) {
                    z = false;
                } else {
                    list.add(list.remove(0));
                }
            }
        }
        e.a(String.valueOf(str) + "---AFT---" + list.toString());
        return list;
    }

    private static List<String> a(JSONObject jSONObject) {
        ArrayList arrayList = new ArrayList();
        if (!(jSONObject == null || jSONObject.length() == 0)) {
            if (jSONObject.length() == 1) {
                arrayList.add(jSONObject.keys().next());
                return arrayList;
            }
            ArrayList arrayList2 = new ArrayList();
            for (int i = 0; i < jSONObject.length(); i++) {
                arrayList2.add(new b());
            }
            while (arrayList2.size() > 1) {
                b a = a((ArrayList<b>) arrayList2, a(jSONObject, (ArrayList<b>) arrayList2));
                if (a != null) {
                    jSONObject.remove(a.a());
                    arrayList2.remove(a);
                    arrayList.add(a.a());
                }
            }
            arrayList.add(((b) arrayList2.get(0)).a());
        }
        return arrayList;
    }

    private static int a(JSONObject jSONObject, ArrayList<b> arrayList) {
        Iterator<String> keys = jSONObject.keys();
        int i = 0;
        int i2 = 0;
        while (keys.hasNext()) {
            String next = keys.next();
            double d = (double) i;
            i += jSONObject.optInt(next);
            arrayList.get(i2).a(d, (double) i, next);
            i2++;
        }
        return i;
    }

    private static b a(ArrayList<b> arrayList, int i) {
        double random = Math.random();
        double d = (double) i;
        Double.isNaN(d);
        double d2 = random * d;
        Iterator<b> it = arrayList.iterator();
        while (it.hasNext()) {
            b next = it.next();
            if (next.a(d2)) {
                return next;
            }
        }
        return null;
    }

    public static void a(List<DCBaseAOL> list) {
        if (list.size() > 1) {
            for (int i = 0; i < list.size() - 1; i++) {
                int i2 = 0;
                while (i2 < (list.size() - i) - 1) {
                    int i3 = i2 + 1;
                    if (list.get(i3).g() > list.get(i2).g()) {
                        list.set(i2, list.get(i3));
                        list.set(i3, list.get(i2));
                    }
                    i2 = i3;
                }
            }
        }
    }

    @SafeVarargs
    public static C0077a a(int i, List<DCBaseAOL>... listArr) {
        int i2;
        int i3;
        ArrayList arrayList = new ArrayList();
        boolean z = false;
        for (List<DCBaseAOL> addAll : listArr) {
            arrayList.addAll(addAll);
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        a((List<DCBaseAOL>) arrayList);
        ArrayList arrayList2 = new ArrayList();
        if (arrayList.size() > i) {
            arrayList2.addAll(arrayList.subList(0, i));
            List<DCBaseAOL> subList = arrayList.subList(i, arrayList.size());
            i3 = ((DCBaseAOL) arrayList2.get(0)).g();
            i2 = ((DCBaseAOL) subList.get(0)).g();
            for (DCBaseAOL dCBaseAOL : subList) {
                if (dCBaseAOL.isSlotSupportBidding()) {
                    dCBaseAOL.biddingFail(i3, i2, 2);
                }
            }
        } else {
            arrayList2.addAll(arrayList);
            i3 = ((DCBaseAOL) arrayList2.get(0)).g();
            i2 = 0;
        }
        Iterator it = arrayList2.iterator();
        while (true) {
            if (it.hasNext()) {
                if (!((DCBaseAOL) it.next()).isSlotSupportBidding()) {
                    break;
                }
            } else {
                z = true;
                break;
            }
        }
        C0077a aVar = new C0077a();
        aVar.a = z;
        aVar.b = i3;
        aVar.c = i2;
        aVar.d = arrayList2;
        return aVar;
    }
}
