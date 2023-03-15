package io.dcloud.e.c;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;
import android.widget.TextView;
import io.dcloud.common.adapter.util.CanvasHelper;
import io.dcloud.common.ui.c;
import io.dcloud.common.util.AESUtil;
import io.dcloud.common.util.Base64;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.language.LanguageUtil;
import io.dcloud.feature.internal.sdk.SDK;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;

public class a {
    private static a a;
    private volatile Context b;
    private boolean c = false;
    private Handler d = new C0030a();

    /* renamed from: io.dcloud.e.c.a$a  reason: collision with other inner class name */
    class C0030a extends Handler {
        C0030a() {
        }

        public void handleMessage(Message message) {
            try {
                a.this.a();
            } catch (Exception unused) {
            }
            sendEmptyMessageDelayed(1, Double.valueOf((Math.random() + 1.0d) * 1000.0d * 60.0d).longValue());
        }
    }

    class b extends Handler {
        b() {
        }

        public void handleMessage(Message message) {
            try {
                a.this.a();
            } catch (Exception e) {
                e.printStackTrace();
            }
            sendEmptyMessageDelayed(1, Double.valueOf((Math.random() + 1.0d) * 1000.0d * 60.0d).longValue());
        }
    }

    private a() {
        if (a != null) {
            throw new IllegalStateException();
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(8:19|18|20|21|22|23|(1:25)|26) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:22:0x0063 */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x007e A[Catch:{  }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.Map<java.lang.String, java.lang.Object> b() {
        /*
            r6 = this;
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            java.lang.String r1 = "app_id"
            java.lang.String r2 = ""
            r0.put(r1, r2)
            r2 = 1
            java.lang.String r2 = io.dcloud.e.d.b.a(r2)     // Catch:{ Exception -> 0x0083 }
            byte[] r2 = io.dcloud.common.util.Base64.decode2bytes(r2)     // Catch:{ Exception -> 0x0083 }
            r3 = 4
            if (r2 != 0) goto L_0x001c
            r6.a((int) r3)     // Catch:{ Exception -> 0x0083 }
            return r0
        L_0x001c:
            java.lang.String r4 = io.dcloud.f.a.e()     // Catch:{ Exception -> 0x0083 }
            java.lang.String r5 = io.dcloud.f.a.b()     // Catch:{ Exception -> 0x0083 }
            java.lang.String r2 = io.dcloud.common.util.AESUtil.decrypt((java.lang.String) r4, (java.lang.String) r5, (byte[]) r2)     // Catch:{ Exception -> 0x0083 }
            if (r2 != 0) goto L_0x002e
            r6.a((int) r3)     // Catch:{ Exception -> 0x0083 }
            return r0
        L_0x002e:
            java.lang.Class r1 = java.lang.Class.forName(r2)     // Catch:{ Exception -> 0x0048 }
            java.lang.Object r2 = r1.newInstance()     // Catch:{ Exception -> 0x0083 }
            java.lang.String r3 = "NM_getCustomInfo"
            r4 = 0
            java.lang.Class[] r5 = new java.lang.Class[r4]     // Catch:{ Exception -> 0x0083 }
            java.lang.reflect.Method r1 = r1.getMethod(r3, r5)     // Catch:{ Exception -> 0x0083 }
            java.lang.Object[] r3 = new java.lang.Object[r4]     // Catch:{ Exception -> 0x0083 }
            java.lang.Object r1 = r1.invoke(r2, r3)     // Catch:{ Exception -> 0x0083 }
            java.util.Map r1 = (java.util.Map) r1     // Catch:{ Exception -> 0x0083 }
            return r1
        L_0x0048:
            r2 = 0
            java.lang.String r3 = io.dcloud.f.a.e()     // Catch:{ Exception -> 0x0063 }
            java.lang.String r4 = io.dcloud.f.a.b()     // Catch:{ Exception -> 0x0063 }
            r5 = 9
            java.lang.String r5 = io.dcloud.e.d.b.a(r5)     // Catch:{ Exception -> 0x0063 }
            byte[] r5 = io.dcloud.common.util.Base64.decode2bytes(r5)     // Catch:{ Exception -> 0x0063 }
            java.lang.String r3 = io.dcloud.common.util.AESUtil.decrypt((java.lang.String) r3, (java.lang.String) r4, (byte[]) r5)     // Catch:{ Exception -> 0x0063 }
            java.lang.Class r2 = java.lang.Class.forName(r3)     // Catch:{ Exception -> 0x0063 }
        L_0x0063:
            java.lang.String r3 = io.dcloud.f.a.e()     // Catch:{  }
            java.lang.String r4 = io.dcloud.f.a.b()     // Catch:{  }
            r5 = 8
            java.lang.String r5 = io.dcloud.e.d.b.a(r5)     // Catch:{  }
            byte[] r5 = io.dcloud.common.util.Base64.decode2bytes(r5)     // Catch:{  }
            java.lang.String r3 = io.dcloud.common.util.AESUtil.decrypt((java.lang.String) r3, (java.lang.String) r4, (byte[]) r5)     // Catch:{  }
            java.lang.Class.forName(r3)     // Catch:{  }
            if (r2 != 0) goto L_0x0083
            java.lang.String r2 = "unknow"
            r0.put(r1, r2)     // Catch:{  }
        L_0x0083:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.e.c.a.b():java.util.Map");
    }

    private String c() {
        byte[] decode2bytes = Base64.decode2bytes(io.dcloud.e.d.b.a(2));
        if (decode2bytes == null) {
            a(4);
            return "";
        }
        String decrypt = AESUtil.decrypt(io.dcloud.f.a.e(), io.dcloud.f.a.b(), decode2bytes);
        if (decrypt == null) {
            a(4);
            return "";
        }
        for (List next : io.dcloud.e.d.b.b()) {
            try {
                Class<?> cls = Class.forName(io.dcloud.f.a.b((String) next.get(0)));
                return (String) cls.getMethod(io.dcloud.f.a.b((String) next.get(2)), new Class[0]).invoke(cls.getDeclaredMethod(io.dcloud.f.a.b((String) next.get(1)), new Class[0]).invoke((Object) null, new Object[0]), new Object[0]);
            } catch (Exception unused) {
            }
        }
        try {
            Class<?> cls2 = Class.forName(decrypt);
            Object invoke = cls2.getMethod("getAppStatus", new Class[0]).invoke(cls2.getMethod("getInstance", new Class[0]).invoke((Object) null, new Object[0]), new Object[0]);
            if (invoke != null) {
                return (String) invoke.getClass().getMethod("getAPPID", new Class[0]).invoke(invoke, new Object[0]);
            }
        } catch (Exception unused2) {
        }
        return "";
    }

    private String d() {
        byte[] decode2bytes = Base64.decode2bytes(io.dcloud.e.d.b.a(10));
        if (decode2bytes == null) {
            a(4);
            return "";
        }
        String decrypt = AESUtil.decrypt(io.dcloud.f.a.e(), io.dcloud.f.a.b(), decode2bytes);
        if (decrypt == null) {
            a(4);
            return "";
        }
        try {
            return (String) Class.forName(decrypt).getMethod("getAppId", new Class[0]).invoke((Object) null, new Object[0]);
        } catch (Exception unused) {
            return "";
        }
    }

    public static a f() {
        if (a == null) {
            synchronized (a.class) {
                if (a == null) {
                    a = new a();
                }
            }
        }
        return a;
    }

    public List<String> e() {
        ArrayList arrayList = new ArrayList();
        try {
            ActivityInfo[] activityInfoArr = this.b.getPackageManager().getPackageInfo(this.b.getPackageName(), 1).activities;
            if (activityInfoArr != null) {
                for (ActivityInfo activityInfo : activityInfoArr) {
                    arrayList.add(activityInfo.name);
                }
            }
        } catch (Exception unused) {
        }
        return arrayList;
    }

    public void g() {
        Handler handler = this.d;
        if (handler != null) {
            this.c = false;
            handler.removeMessages(1);
            this.d = null;
        }
    }

    public void a(Context context) {
        Handler handler;
        if (!PdrUtil.checkIntl() || LanguageUtil.getDeviceDefCountry().equalsIgnoreCase(io.dcloud.f.a.b("KF"))) {
            this.b = context;
            try {
                if (!SDK.isUniMPSDK()) {
                    if (Math.abs(Math.random() * 5.0d) == 5.0d && (handler = this.d) != null) {
                        handler.removeMessages(1);
                        this.d = null;
                    }
                    if (this.d == null) {
                        this.d = new b();
                    }
                    if (!this.d.hasMessages(1)) {
                        this.d.sendEmptyMessage(1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:105:0x01b1 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x011b A[Catch:{ Exception -> 0x0133 }] */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x013c A[Catch:{ Exception -> 0x0171 }] */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x0176 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x019f A[Catch:{ Exception -> 0x01f9 }] */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x01c8 A[SYNTHETIC, Splitter:B:93:0x01c8] */
    /* JADX WARNING: Removed duplicated region for block: B:96:0x01d2 A[Catch:{ Exception -> 0x01dc }] */
    /* JADX WARNING: Removed duplicated region for block: B:98:0x01d7 A[Catch:{ Exception -> 0x01dc }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a() {
        /*
            r24 = this;
            r0 = r24
            java.lang.String r1 = "1"
            java.lang.String r2 = "r"
            java.util.List r3 = r24.e()
            java.util.List r4 = io.dcloud.e.d.b.a()
            int r5 = r3.size()
            java.lang.String r6 = "csj"
            r8 = 1
            if (r5 == 0) goto L_0x003e
            java.lang.Object r5 = r4.get(r8)
            java.lang.String r5 = (java.lang.String) r5
            java.lang.String r5 = io.dcloud.f.a.b(r5)
            boolean r5 = r3.contains(r5)
            if (r5 != 0) goto L_0x003e
            r5 = 4
            java.lang.Object r5 = r4.get(r5)
            java.lang.String r5 = (java.lang.String) r5
            java.lang.String r5 = io.dcloud.f.a.b(r5)
            boolean r5 = r3.contains(r5)
            if (r5 == 0) goto L_0x0039
            goto L_0x003e
        L_0x0039:
            r5 = 0
            r9 = 0
            r10 = 1
            r11 = 0
            goto L_0x0056
        L_0x003e:
            java.util.Map r5 = r24.b()
            java.lang.String r9 = "app_id"
            java.lang.Object r9 = r5.remove(r9)
            java.lang.String r9 = (java.lang.String) r9
            java.lang.String r10 = "unknow"
            boolean r10 = r10.equals(r9)
            java.lang.String r11 = "UNIAD_CSJ_APPID"
            java.lang.String r11 = r0.a((java.lang.String) r11, (java.lang.String) r6)
        L_0x0056:
            int r12 = r3.size()
            java.lang.String r13 = "gdt"
            r14 = 0
            if (r12 == 0) goto L_0x0073
            java.lang.Object r12 = r4.get(r14)
            java.lang.String r12 = (java.lang.String) r12
            java.lang.String r12 = io.dcloud.f.a.b(r12)
            boolean r12 = r3.contains(r12)
            if (r12 == 0) goto L_0x0070
            goto L_0x0073
        L_0x0070:
            r12 = 0
            r15 = 0
            goto L_0x007d
        L_0x0073:
            java.lang.String r12 = r24.c()
            java.lang.String r15 = "UNIAD_GDT_APPID"
            java.lang.String r15 = r0.a((java.lang.String) r15, (java.lang.String) r13)
        L_0x007d:
            int r16 = r3.size()
            r7 = 2
            java.lang.String r14 = "ks"
            if (r16 == 0) goto L_0x009a
            java.lang.Object r4 = r4.get(r7)
            java.lang.String r4 = (java.lang.String) r4
            java.lang.String r4 = io.dcloud.f.a.b(r4)
            boolean r3 = r3.contains(r4)
            if (r3 == 0) goto L_0x0097
            goto L_0x009a
        L_0x0097:
            r3 = 0
            r4 = 0
            goto L_0x00a4
        L_0x009a:
            java.lang.String r3 = r24.d()
            java.lang.String r4 = "UNIAD_KS_APPID"
            java.lang.String r4 = r0.a((java.lang.String) r4, (java.lang.String) r14)
        L_0x00a4:
            org.json.JSONObject r7 = new org.json.JSONObject
            r7.<init>()
            org.json.JSONObject r8 = new org.json.JSONObject
            r8.<init>()
            r17 = r8
            java.util.HashMap r8 = new java.util.HashMap
            r8.<init>()
            java.lang.String r18 = "kWixxal"
            r19 = r14
            java.lang.String r14 = io.dcloud.f.a.b(r18)
            java.lang.String r18 = "{Wixxal"
            r20 = r4
            java.lang.String r4 = io.dcloud.f.a.b(r18)
            java.lang.String r18 = "{WixxalW`"
            r21 = r3
            java.lang.String r3 = io.dcloud.f.a.b(r18)
            r18 = r7
            boolean r22 = android.text.TextUtils.isEmpty(r9)     // Catch:{ Exception -> 0x0104 }
            if (r22 != 0) goto L_0x0104
            boolean r22 = r9.equals(r11)     // Catch:{ Exception -> 0x0104 }
            if (r22 != 0) goto L_0x0104
            org.json.JSONArray r7 = r0.a((java.lang.String) r6)     // Catch:{ Exception -> 0x0104 }
            boolean r23 = r0.a((java.lang.String) r9, (org.json.JSONArray) r7)     // Catch:{ Exception -> 0x0104 }
            if (r23 != 0) goto L_0x0104
            if (r10 == 0) goto L_0x00ed
            r10 = 6
            r0.a((int) r10)     // Catch:{ Exception -> 0x0104 }
        L_0x00ed:
            r8.put(r2, r1)     // Catch:{ Exception -> 0x0102 }
            r8.put(r14, r9)     // Catch:{ Exception -> 0x0102 }
            java.lang.String r9 = java.lang.String.valueOf(r11)     // Catch:{ Exception -> 0x0102 }
            r8.put(r4, r9)     // Catch:{ Exception -> 0x0102 }
            r8.put(r3, r7)     // Catch:{ Exception -> 0x0102 }
            if (r5 == 0) goto L_0x0102
            r8.putAll(r5)     // Catch:{ Exception -> 0x0102 }
        L_0x0102:
            r5 = 0
            goto L_0x0105
        L_0x0104:
            r5 = 1
        L_0x0105:
            boolean r7 = android.text.TextUtils.isEmpty(r12)     // Catch:{ Exception -> 0x0133 }
            if (r7 != 0) goto L_0x0133
            boolean r7 = r12.equals(r15)     // Catch:{ Exception -> 0x0133 }
            if (r7 != 0) goto L_0x0133
            org.json.JSONArray r7 = r0.a((java.lang.String) r13)     // Catch:{ Exception -> 0x0133 }
            boolean r9 = r0.a((java.lang.String) r12, (org.json.JSONArray) r7)     // Catch:{ Exception -> 0x0133 }
            if (r9 != 0) goto L_0x0133
            r9 = 6
            r0.a((int) r9)     // Catch:{ Exception -> 0x0133 }
            r9 = r18
            r9.put(r2, r1)     // Catch:{ Exception -> 0x0131 }
            r9.put(r14, r12)     // Catch:{ Exception -> 0x0131 }
            java.lang.String r10 = java.lang.String.valueOf(r15)     // Catch:{ Exception -> 0x0131 }
            r9.put(r4, r10)     // Catch:{ Exception -> 0x0131 }
            r9.put(r3, r7)     // Catch:{ Exception -> 0x0131 }
        L_0x0131:
            r7 = 0
            goto L_0x0136
        L_0x0133:
            r9 = r18
            r7 = 1
        L_0x0136:
            boolean r10 = android.text.TextUtils.isEmpty(r21)     // Catch:{ Exception -> 0x0171 }
            if (r10 != 0) goto L_0x0171
            r11 = r20
            r10 = r21
            boolean r12 = r10.equals(r11)     // Catch:{ Exception -> 0x0171 }
            if (r12 != 0) goto L_0x0171
            r12 = r19
            org.json.JSONArray r15 = r0.a((java.lang.String) r12)     // Catch:{ Exception -> 0x016f }
            boolean r18 = r0.a((java.lang.String) r10, (org.json.JSONArray) r15)     // Catch:{ Exception -> 0x016f }
            if (r18 != 0) goto L_0x016c
            r19 = r12
            r12 = 6
            r0.a((int) r12)     // Catch:{ Exception -> 0x0171 }
            r12 = r17
            r12.put(r2, r1)     // Catch:{ Exception -> 0x016a }
            r12.put(r14, r10)     // Catch:{ Exception -> 0x016a }
            java.lang.String r1 = java.lang.String.valueOf(r11)     // Catch:{ Exception -> 0x016a }
            r12.put(r4, r1)     // Catch:{ Exception -> 0x016a }
            r12.put(r3, r15)     // Catch:{ Exception -> 0x016a }
        L_0x016a:
            r1 = 0
            goto L_0x0174
        L_0x016c:
            r19 = r12
            goto L_0x0171
        L_0x016f:
            r19 = r12
        L_0x0171:
            r12 = r17
            r1 = 1
        L_0x0174:
            if (r5 == 0) goto L_0x017a
            if (r7 == 0) goto L_0x017a
            if (r1 != 0) goto L_0x01f9
        L_0x017a:
            boolean r2 = r0.c
            if (r2 != 0) goto L_0x01f9
            java.lang.String r2 = io.dcloud.f.a.e()     // Catch:{ Exception -> 0x01f9 }
            java.lang.String r3 = io.dcloud.f.a.b()     // Catch:{ Exception -> 0x01f9 }
            r4 = 7
            java.lang.String r4 = io.dcloud.e.d.b.a(r4)     // Catch:{ Exception -> 0x01f9 }
            byte[] r4 = io.dcloud.common.util.Base64.decode2bytes(r4)     // Catch:{ Exception -> 0x01f9 }
            java.lang.String r2 = io.dcloud.common.util.AESUtil.decrypt((java.lang.String) r2, (java.lang.String) r3, (byte[]) r4)     // Catch:{ Exception -> 0x01f9 }
            java.lang.Class r2 = java.lang.Class.forName(r2)     // Catch:{ Exception -> 0x01f9 }
            java.lang.reflect.Method[] r2 = r2.getDeclaredMethods()     // Catch:{ Exception -> 0x01f9 }
            int r3 = r2.length     // Catch:{ Exception -> 0x01f9 }
            r4 = 0
        L_0x019d:
            if (r4 >= r3) goto L_0x01b1
            r10 = r2[r4]     // Catch:{ Exception -> 0x01f9 }
            java.lang.String r11 = r10.getName()     // Catch:{ Exception -> 0x01f9 }
            java.lang.String r14 = "pr"
            boolean r11 = r11.equals(r14)     // Catch:{ Exception -> 0x01f9 }
            if (r11 == 0) goto L_0x01ae
            goto L_0x01b2
        L_0x01ae:
            int r4 = r4 + 1
            goto L_0x019d
        L_0x01b1:
            r10 = 0
        L_0x01b2:
            java.util.HashMap r2 = new java.util.HashMap     // Catch:{ Exception -> 0x01f9 }
            r2.<init>()     // Catch:{ Exception -> 0x01f9 }
            java.lang.String r3 = "t"
            r4 = 1
            java.lang.Integer r11 = java.lang.Integer.valueOf(r4)     // Catch:{ Exception -> 0x01f9 }
            r2.put(r3, r11)     // Catch:{ Exception -> 0x01f9 }
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch:{ Exception -> 0x01f9 }
            r3.<init>()     // Catch:{ Exception -> 0x01f9 }
            if (r5 != 0) goto L_0x01d0
            org.json.JSONObject r4 = new org.json.JSONObject     // Catch:{ Exception -> 0x01dc }
            r4.<init>(r8)     // Catch:{ Exception -> 0x01dc }
            r3.put(r6, r4)     // Catch:{ Exception -> 0x01dc }
        L_0x01d0:
            if (r7 != 0) goto L_0x01d5
            r3.put(r13, r9)     // Catch:{ Exception -> 0x01dc }
        L_0x01d5:
            if (r1 != 0) goto L_0x01dc
            r1 = r19
            r3.put(r1, r12)     // Catch:{ Exception -> 0x01dc }
        L_0x01dc:
            java.lang.String r1 = "rad"
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x01f9 }
            r2.put(r1, r3)     // Catch:{ Exception -> 0x01f9 }
            r1 = 1
            r10.setAccessible(r1)     // Catch:{ Exception -> 0x01f9 }
            r3 = 2
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ Exception -> 0x01f9 }
            android.content.Context r4 = r0.b     // Catch:{ Exception -> 0x01f9 }
            r5 = 0
            r3[r5] = r4     // Catch:{ Exception -> 0x01f9 }
            r3[r1] = r2     // Catch:{ Exception -> 0x01f9 }
            r2 = 0
            r10.invoke(r2, r3)     // Catch:{ Exception -> 0x01f9 }
            r0.c = r1     // Catch:{ Exception -> 0x01f9 }
        L_0x01f9:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.e.c.a.a():void");
    }

    private boolean a(String str, JSONArray jSONArray) {
        if (jSONArray != null && jSONArray.length() > 0) {
            for (int i = 0; i < jSONArray.length(); i++) {
                if (str.equals(jSONArray.optString(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    private String a(String str, String str2) {
        byte[] decode2bytes = Base64.decode2bytes(io.dcloud.e.d.b.a(3));
        if (decode2bytes == null) {
            a(4);
            return "";
        }
        String decrypt = AESUtil.decrypt(io.dcloud.f.a.e(), io.dcloud.f.a.b(), decode2bytes);
        if (decrypt == null) {
            a(4);
            return "";
        }
        try {
            return (String) Class.forName(decrypt).getMethod("da", new Class[]{String.class, String.class}).invoke((Object) null, new Object[]{str, str2});
        } catch (Exception unused) {
            return "";
        }
    }

    private JSONArray a(String str) {
        byte[] decode2bytes = Base64.decode2bytes(io.dcloud.e.d.b.a(3));
        if (decode2bytes == null) {
            a(4);
            return null;
        }
        String decrypt = AESUtil.decrypt(io.dcloud.f.a.e(), io.dcloud.f.a.b(), decode2bytes);
        if (decrypt == null) {
            a(4);
            return null;
        }
        try {
            return (JSONArray) Class.forName(decrypt).getMethod("dah", new Class[]{String.class}).invoke((Object) null, new Object[]{str});
        } catch (Exception unused) {
            return null;
        }
    }

    private void a(int i) {
        if (this.b != null) {
            io.dcloud.feature.ui.nativeui.b bVar = new io.dcloud.feature.ui.nativeui.b((Activity) this.b, "");
            TextView textView = new TextView(this.b);
            textView.setAutoLinkMask(1);
            textView.setClickable(true);
            textView.setText(c.a(this.b).a(AESUtil.decrypt(io.dcloud.f.a.e(), io.dcloud.f.a.b(), Base64.decode2bytes(io.dcloud.e.d.b.a(i)))));
            LinearLayout linearLayout = new LinearLayout(this.b);
            linearLayout.addView(textView);
            bVar.a(linearLayout, textView);
            bVar.setDuration(1);
            bVar.setGravity(80, bVar.getXOffset(), bVar.getYOffset());
            int dip2px = CanvasHelper.dip2px(this.b, 10.0f);
            int dip2px2 = CanvasHelper.dip2px(this.b, 8.0f);
            linearLayout.setPadding(dip2px, dip2px2, dip2px, dip2px2);
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setCornerRadius((float) dip2px2);
            gradientDrawable.setShape(0);
            gradientDrawable.setColor(-1308622848);
            linearLayout.setBackground(gradientDrawable);
            textView.setGravity(17);
            textView.setTextColor(Color.parseColor("#ffffffff"));
            bVar.show();
        }
    }
}
