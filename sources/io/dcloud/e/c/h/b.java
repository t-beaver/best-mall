package io.dcloud.e.c.h;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.facebook.common.callercontext.ContextChain;
import com.taobao.weex.common.WXConfig;
import com.taobao.weex.ui.component.WXComponent;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.util.AESUtil;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.CreateShortResultReceiver;
import io.dcloud.common.util.IOUtil;
import io.dcloud.common.util.NetTool;
import io.dcloud.common.util.NetworkTypeUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.TelephonyUtil;
import io.dcloud.common.util.ThreadPool;
import io.dcloud.common.util.ZipUtils;
import io.dcloud.common.util.hostpicker.HostPicker;
import io.dcloud.common.util.net.NetWork;
import io.dcloud.e.c.g;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class b {

    class a implements HostPicker.HostPickCallback {
        final /* synthetic */ String a;
        final /* synthetic */ HashMap b;

        a(String str, HashMap hashMap) {
            this.a = str;
            this.b = hashMap;
        }

        public boolean doRequest(HostPicker.Host host) {
            byte[] httpPost = NetTool.httpPost(host.getRealHost(), this.a, (HashMap<String, String>) this.b, false);
            if (httpPost == null) {
                return false;
            }
            JSONObject jSONObject = null;
            try {
                jSONObject = new JSONObject(new String(httpPost, "utf-8"));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
            }
            Logger.d("commitTid", jSONObject);
            return true;
        }

        public void onNoOnePicked() {
        }

        public void onOneSelected(HostPicker.Host host) {
        }
    }

    /* renamed from: io.dcloud.e.c.h.b$b  reason: collision with other inner class name */
    class C0032b implements HostPicker.HostPickCallback {
        final /* synthetic */ String a;
        final /* synthetic */ HashMap b;

        C0032b(String str, HashMap hashMap) {
            this.a = str;
            this.b = hashMap;
        }

        public boolean doRequest(HostPicker.Host host) {
            byte[] httpPost = NetTool.httpPost(host.getRealHost(), this.a, (HashMap<String, String>) this.b, false);
            if (httpPost == null) {
                return false;
            }
            JSONObject jSONObject = null;
            try {
                jSONObject = new JSONObject(new String(httpPost, StandardCharsets.UTF_8));
            } catch (JSONException unused) {
            }
            Logger.d("commitTid", jSONObject);
            return true;
        }

        public void onNoOnePicked() {
        }

        public void onOneSelected(HostPicker.Host host) {
        }
    }

    class c implements Runnable {
        final /* synthetic */ IApp a;
        final /* synthetic */ int b;
        final /* synthetic */ String c;
        final /* synthetic */ String d;
        final /* synthetic */ ICallBack e;

        class a implements HostPicker.HostPickCallback {
            final /* synthetic */ String a;
            final /* synthetic */ HashMap b;
            final /* synthetic */ HashMap c;
            final /* synthetic */ SharedPreferences d;

            a(String str, HashMap hashMap, HashMap hashMap2, SharedPreferences sharedPreferences) {
                this.a = str;
                this.b = hashMap;
                this.c = hashMap2;
                this.d = sharedPreferences;
            }

            public boolean doRequest(HostPicker.Host host) {
                Object httpPost = NetTool.httpPost(host.getRealHost(), this.a, (HashMap<String, String>) this.b, false, true);
                if (httpPost == null) {
                    return false;
                }
                SP.setBundleData(c.this.a.getActivity(), "pdr", SP.REPORT_UNI_VERIFY_GYUID, "");
                if (this.c.containsKey(AbsoluteConst.XML_APPS)) {
                    this.d.edit().putLong(AbsoluteConst.COMMIT_APP_LIST_TIME, System.currentTimeMillis()).commit();
                }
                c cVar = c.this;
                ICallBack iCallBack = cVar.e;
                if (iCallBack != null) {
                    iCallBack.onCallBack(1, new Object[]{cVar.a, cVar.c, httpPost});
                }
                return true;
            }

            public void onNoOnePicked() {
            }

            public void onOneSelected(HostPicker.Host host) {
            }
        }

        c(IApp iApp, int i, String str, String str2, ICallBack iCallBack) {
            this.a = iApp;
            this.b = i;
            this.c = str;
            this.d = str2;
            this.e = iCallBack;
        }

        public void run() {
            String str;
            HashMap hashMap = new HashMap();
            HashMap hashMap2 = new HashMap();
            hashMap2.put(NetWork.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
            SharedPreferences orCreateBundle = SP.getOrCreateBundle((Context) this.a.getActivity(), AbsoluteConst.START_STATISTICS_DATA);
            HashMap<Object, Object> a2 = b.a(this.a, orCreateBundle);
            a2.put("gudi", Integer.valueOf(this.b));
            hashMap.putAll(a2);
            try {
                hashMap.put("paid", this.c);
                hashMap.put("urv", this.d);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            if (hashMap.size() != 0) {
                do {
                    str = null;
                } while (hashMap.values().remove((Object) null));
                do {
                } while (hashMap.values().remove("null"));
                try {
                    str = URLEncoder.encode(Base64.encodeToString(AESUtil.encrypt(this.a.getConfusionMgr().getSK(), this.a.getConfusionMgr().getSIV(), ZipUtils.zipString(new JSONObject(hashMap).toString())), 2), "utf-8");
                } catch (UnsupportedEncodingException e3) {
                    e3.printStackTrace();
                }
                ArrayList arrayList = new ArrayList();
                arrayList.add(new HostPicker.Host("YHx8eHsyJyd7OSZsa2RnfWwmZm18JmtmJ2tnZGRta3wneGR9e2l4eCd7fGl6fH14J346", HostPicker.Host.PriorityEnum.FIRST));
                arrayList.add(new HostPicker.Host("YHx8eHsyJyd7OiZsa2RnfWwmZm18JmtmJ2tnZGRta3wneGR9e2l4eCd7fGl6fH14J346", HostPicker.Host.PriorityEnum.NORMAL));
                arrayList.add(new HostPicker.Host("YHx8eHsyJydrO2k/Pzs7OCU7a207JTw5Pm0lMWowOSU9Pzk4aT9tbjhtaj8mant4aXh4JmtnZSdgfHx4J2tpezo=", HostPicker.Host.PriorityEnum.BACKUP));
                HostPicker.getInstance().pickSuitHost(this.a.getActivity(), arrayList, "StartUp", new a("edata=" + str, hashMap2, hashMap, orCreateBundle));
            }
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:17|18|19|20) */
    /* JADX WARNING: Code restructure failed: missing block: B:96:0x01c0, code lost:
        r14 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x01c1, code lost:
        r14.printStackTrace();
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x006e */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.HashMap<java.lang.Object, java.lang.Object> a(io.dcloud.common.DHInterface.IApp r14, android.content.SharedPreferences r15) {
        /*
            java.lang.String r0 = "geo_data"
            java.lang.String r1 = "pn"
            java.lang.String r2 = "r"
            java.util.HashMap r3 = new java.util.HashMap
            r3.<init>()
            android.app.Activity r4 = r14.getActivity()     // Catch:{ Exception -> 0x01c0 }
            boolean r5 = io.dcloud.common.util.AppRuntime.hasPrivacyForNotShown(r4)     // Catch:{ Exception -> 0x01c0 }
            java.lang.String r6 = r14.obtainAppId()     // Catch:{ Exception -> 0x01c0 }
            android.content.Intent r7 = r4.getIntent()     // Catch:{ Exception -> 0x01c0 }
            java.lang.String r7 = io.dcloud.common.util.BaseInfo.getLaunchType(r7)     // Catch:{ Exception -> 0x01c0 }
            r8 = 0
            java.util.HashMap r6 = io.dcloud.common.constant.DataInterface.getStartupUrlBaseData(r4, r6, r7, r8, r5)     // Catch:{ Exception -> 0x01c0 }
            r3.putAll(r6)     // Catch:{ Exception -> 0x01c0 }
            java.lang.String r6 = "st"
            long r9 = io.dcloud.common.util.BaseInfo.run5appEndTime     // Catch:{ Exception -> 0x01c0 }
            java.lang.Long r7 = java.lang.Long.valueOf(r9)     // Catch:{ Exception -> 0x01c0 }
            r3.put(r6, r7)     // Catch:{ Exception -> 0x01c0 }
            java.lang.String r6 = r4.getPackageName()     // Catch:{ Exception -> 0x01c0 }
            r3.put(r1, r6)     // Catch:{ Exception -> 0x01c0 }
            java.lang.String r6 = "v"
            java.lang.String r7 = r14.obtainAppVersionName()     // Catch:{ Exception -> 0x01c0 }
            r3.put(r6, r7)     // Catch:{ Exception -> 0x01c0 }
            java.lang.String r6 = "pv"
            java.lang.String r7 = io.dcloud.common.adapter.util.AndroidResources.versionName     // Catch:{ Exception -> 0x01c0 }
            r3.put(r6, r7)     // Catch:{ Exception -> 0x01c0 }
            java.lang.String r6 = "uat"
            boolean r7 = io.dcloud.common.util.BaseInfo.isUniAppAppid(r14)     // Catch:{ Exception -> 0x01c0 }
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)     // Catch:{ Exception -> 0x01c0 }
            r3.put(r6, r7)     // Catch:{ Exception -> 0x01c0 }
            boolean r6 = io.dcloud.feature.internal.sdk.SDK.isUniMPSDK()     // Catch:{ Exception -> 0x01c0 }
            java.lang.String r7 = "name"
            if (r6 == 0) goto L_0x0076
            android.content.pm.ApplicationInfo r6 = r4.getApplicationInfo()     // Catch:{ Exception -> 0x006e }
            android.content.pm.PackageManager r9 = r4.getPackageManager()     // Catch:{ Exception -> 0x006e }
            java.lang.CharSequence r6 = r6.loadLabel(r9)     // Catch:{ Exception -> 0x006e }
            r3.put(r7, r6)     // Catch:{ Exception -> 0x006e }
            goto L_0x007d
        L_0x006e:
            java.lang.String r6 = r14.obtainAppName()     // Catch:{ Exception -> 0x01c0 }
            r3.put(r7, r6)     // Catch:{ Exception -> 0x01c0 }
            goto L_0x007d
        L_0x0076:
            java.lang.String r6 = r14.obtainAppName()     // Catch:{ Exception -> 0x01c0 }
            r3.put(r7, r6)     // Catch:{ Exception -> 0x01c0 }
        L_0x007d:
            java.lang.String r6 = "pname"
            android.content.pm.ApplicationInfo r7 = r4.getApplicationInfo()     // Catch:{ Exception -> 0x008e }
            android.content.pm.PackageManager r9 = r4.getPackageManager()     // Catch:{ Exception -> 0x008e }
            java.lang.CharSequence r7 = r7.loadLabel(r9)     // Catch:{ Exception -> 0x008e }
            r3.put(r6, r7)     // Catch:{ Exception -> 0x008e }
        L_0x008e:
            java.lang.String r6 = "it"
            boolean r7 = io.dcloud.feature.internal.sdk.SDK.isUniMPSDK()     // Catch:{ Exception -> 0x01c0 }
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)     // Catch:{ Exception -> 0x01c0 }
            r3.put(r6, r7)     // Catch:{ Exception -> 0x01c0 }
            boolean r6 = io.dcloud.feature.internal.sdk.SDK.isUniMPSDK()     // Catch:{ Exception -> 0x01c0 }
            if (r6 == 0) goto L_0x010d
            io.dcloud.common.DHInterface.IConfusionMgr r6 = r14.getConfusionMgr()     // Catch:{ Exception -> 0x01c0 }
            java.lang.String r6 = r6.getGDTClassName()     // Catch:{ Exception -> 0x01c0 }
            boolean r6 = io.dcloud.common.adapter.util.PlatformUtil.checkClass(r6)     // Catch:{ Exception -> 0x01c0 }
            io.dcloud.common.DHInterface.IConfusionMgr r7 = r14.getConfusionMgr()     // Catch:{ Exception -> 0x01c0 }
            java.lang.String r7 = r7.getCSJClassName()     // Catch:{ Exception -> 0x01c0 }
            boolean r7 = io.dcloud.common.adapter.util.PlatformUtil.checkClass(r7)     // Catch:{ Exception -> 0x01c0 }
            io.dcloud.common.DHInterface.IConfusionMgr r9 = r14.getConfusionMgr()     // Catch:{ Exception -> 0x01c0 }
            java.lang.String r9 = r9.getKSClassName()     // Catch:{ Exception -> 0x01c0 }
            boolean r9 = io.dcloud.common.adapter.util.PlatformUtil.checkClass(r9)     // Catch:{ Exception -> 0x01c0 }
            org.json.JSONObject r10 = new org.json.JSONObject     // Catch:{ Exception -> 0x01c0 }
            r10.<init>()     // Catch:{ Exception -> 0x01c0 }
            org.json.JSONObject r11 = new org.json.JSONObject     // Catch:{ Exception -> 0x01c0 }
            r11.<init>()     // Catch:{ Exception -> 0x01c0 }
            java.lang.String r12 = "1"
            java.lang.String r13 = "0"
            if (r7 == 0) goto L_0x00d7
            r7 = r12
            goto L_0x00d8
        L_0x00d7:
            r7 = r13
        L_0x00d8:
            r11.put(r2, r7)     // Catch:{ Exception -> 0x01c0 }
            java.lang.String r7 = "csj"
            r10.put(r7, r11)     // Catch:{ Exception -> 0x01c0 }
            org.json.JSONObject r7 = new org.json.JSONObject     // Catch:{ Exception -> 0x01c0 }
            r7.<init>()     // Catch:{ Exception -> 0x01c0 }
            if (r6 == 0) goto L_0x00e9
            r6 = r12
            goto L_0x00ea
        L_0x00e9:
            r6 = r13
        L_0x00ea:
            r7.put(r2, r6)     // Catch:{ Exception -> 0x01c0 }
            java.lang.String r6 = "gdt"
            r10.put(r6, r7)     // Catch:{ Exception -> 0x01c0 }
            org.json.JSONObject r6 = new org.json.JSONObject     // Catch:{ Exception -> 0x01c0 }
            r6.<init>()     // Catch:{ Exception -> 0x01c0 }
            if (r9 == 0) goto L_0x00fa
            goto L_0x00fb
        L_0x00fa:
            r12 = r13
        L_0x00fb:
            r6.put(r2, r12)     // Catch:{ Exception -> 0x01c0 }
            java.lang.String r2 = "ks"
            r10.put(r2, r6)     // Catch:{ Exception -> 0x01c0 }
            java.lang.String r2 = "cad"
            java.lang.String r6 = r10.toString()     // Catch:{ Exception -> 0x01c0 }
            r3.put(r2, r6)     // Catch:{ Exception -> 0x01c0 }
            goto L_0x0117
        L_0x010d:
            java.lang.String r2 = "psdk"
            r6 = 0
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ Exception -> 0x01c0 }
            r3.put(r2, r6)     // Catch:{ Exception -> 0x01c0 }
        L_0x0117:
            java.lang.String r2 = "pdr"
            java.lang.String r6 = "report_uni_verify_GYUID"
            java.lang.String r2 = io.dcloud.common.adapter.util.SP.getBundleData((android.content.Context) r4, (java.lang.String) r2, (java.lang.String) r6)     // Catch:{ Exception -> 0x01c0 }
            boolean r6 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Exception -> 0x01c0 }
            if (r6 != 0) goto L_0x012a
            java.lang.String r6 = "uvs"
            r3.put(r6, r2)     // Catch:{ Exception -> 0x01c0 }
        L_0x012a:
            boolean r2 = io.dcloud.common.util.PdrUtil.isSupportOaid()     // Catch:{ Exception -> 0x01c0 }
            if (r2 == 0) goto L_0x013c
            java.lang.String r2 = "oaid"
            java.lang.String r6 = io.dcloud.common.adapter.util.DeviceInfo.oaids     // Catch:{ Exception -> 0x01c0 }
            if (r6 != 0) goto L_0x0139
            java.lang.String r6 = "||"
        L_0x0139:
            r3.put(r2, r6)     // Catch:{ Exception -> 0x01c0 }
        L_0x013c:
            boolean r2 = io.dcloud.common.util.BaseInfo.isUniAppAppid(r14)     // Catch:{ Exception -> 0x01c0 }
            if (r2 == 0) goto L_0x0145
            a((io.dcloud.common.DHInterface.IApp) r14, (java.util.HashMap<java.lang.Object, java.lang.Object>) r3)     // Catch:{ Exception -> 0x01c0 }
        L_0x0145:
            java.lang.String r14 = "dcid"
            java.lang.String r2 = io.dcloud.common.util.AppRuntime.getDCloudDeviceID(r4)     // Catch:{ Exception -> 0x01c0 }
            r3.put(r14, r2)     // Catch:{ Exception -> 0x01c0 }
            java.lang.String r14 = io.dcloud.common.util.BaseInfo.sChannel     // Catch:{ Exception -> 0x01c0 }
            boolean r14 = android.text.TextUtils.isEmpty(r14)     // Catch:{ Exception -> 0x01c0 }
            java.lang.String r2 = ""
            java.lang.String r6 = "mc"
            if (r14 == 0) goto L_0x0173
            java.lang.String r14 = "DCLOUD_STREAMAPP_CHANNEL"
            java.lang.String r8 = io.dcloud.common.adapter.util.AndroidResources.getMetaValue(r14)     // Catch:{ Exception -> 0x0161 }
            goto L_0x0165
        L_0x0161:
            r14 = move-exception
            r14.printStackTrace()     // Catch:{ Exception -> 0x01c0 }
        L_0x0165:
            boolean r14 = android.text.TextUtils.isEmpty(r8)     // Catch:{ Exception -> 0x01c0 }
            if (r14 != 0) goto L_0x016f
            r3.put(r6, r8)     // Catch:{ Exception -> 0x01c0 }
            goto L_0x0178
        L_0x016f:
            r3.put(r6, r2)     // Catch:{ Exception -> 0x01c0 }
            goto L_0x0178
        L_0x0173:
            java.lang.String r14 = io.dcloud.common.util.BaseInfo.sChannel     // Catch:{ Exception -> 0x01c0 }
            r3.put(r6, r14)     // Catch:{ Exception -> 0x01c0 }
        L_0x0178:
            if (r5 != 0) goto L_0x01c4
            java.lang.String r14 = "commit_app_list_time"
            r5 = 0
            long r5 = r15.getLong(r14, r5)     // Catch:{ Exception -> 0x01c0 }
            long r7 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x01c0 }
            long r7 = r7 - r5
            r5 = 100000(0x186a0, double:4.94066E-319)
            long r7 = r7 / r5
            r5 = 26000(0x6590, double:1.28457E-319)
            int r14 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
            if (r14 < 0) goto L_0x01c4
            boolean r14 = io.dcloud.common.util.BaseInfo.isChannelGooglePlay()     // Catch:{ Exception -> 0x01c0 }
            if (r14 != 0) goto L_0x01ac
            org.json.JSONObject r14 = new org.json.JSONObject     // Catch:{ Exception -> 0x01c0 }
            r14.<init>()     // Catch:{ Exception -> 0x01c0 }
            java.lang.String r4 = io.dcloud.common.util.LauncherUtil.getLauncherPackageName(r4)     // Catch:{ Exception -> 0x01c0 }
            r14.put(r1, r4)     // Catch:{ Exception -> 0x01c0 }
            java.lang.String r1 = "launcher"
            java.lang.String r14 = r14.toString()     // Catch:{ Exception -> 0x01c0 }
            r3.put(r1, r14)     // Catch:{ Exception -> 0x01c0 }
        L_0x01ac:
            java.lang.String r14 = r15.getString(r0, r2)     // Catch:{ Exception -> 0x01c0 }
            boolean r14 = android.text.TextUtils.isEmpty(r14)     // Catch:{ Exception -> 0x01c0 }
            if (r14 != 0) goto L_0x01c4
            java.lang.String r14 = "pos"
            java.lang.String r15 = r15.getString(r0, r2)     // Catch:{ Exception -> 0x01c0 }
            r3.put(r14, r15)     // Catch:{ Exception -> 0x01c0 }
            goto L_0x01c4
        L_0x01c0:
            r14 = move-exception
            r14.printStackTrace()
        L_0x01c4:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.e.c.h.b.a(io.dcloud.common.DHInterface.IApp, android.content.SharedPreferences):java.util.HashMap");
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
            str5 = DeviceInfo.sApplicationContext.getPackageManager().getPackageInfo(DeviceInfo.sApplicationContext.getPackageName(), 0).versionName;
        } catch (Exception e2) {
            e2.printStackTrace();
            str5 = "";
        }
        String imei = TelephonyUtil.getIMEI(context, true, true);
        HashMap hashMap2 = new HashMap();
        hashMap2.put(ContextChain.TAG_PRODUCT, "a");
        hashMap2.put("appid", str);
        hashMap2.put(CreateShortResultReceiver.KEY_VERSIONNAME, str5);
        hashMap2.put("at", Integer.valueOf(i));
        hashMap2.put(WXConfig.os, Integer.valueOf(Build.VERSION.SDK_INT));
        hashMap2.put("adpid", str3);
        if (imei.endsWith("&ie=1")) {
            imei = imei.replace("&ie=1", "");
            hashMap2.put("ie", 1);
        } else if (imei.endsWith("&ie=0")) {
            imei = imei.replace("&ie=0", "");
            hashMap2.put("ie", 0);
        }
        hashMap2.put("imei", imei);
        hashMap2.put("md", str4);
        hashMap2.put("vd", Build.MANUFACTURER);
        hashMap2.put("net", Integer.valueOf(NetworkTypeUtil.getNetworkType(DeviceInfo.sApplicationContext)));
        hashMap2.put("vb", "1.9.9.81676");
        hashMap2.put("t", Long.valueOf(System.currentTimeMillis()));
        if (TextUtils.isEmpty(BaseInfo.sChannel)) {
            String str6 = null;
            try {
                str6 = AndroidResources.getMetaValue("DCLOUD_STREAMAPP_CHANNEL");
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            if (!TextUtils.isEmpty(str6)) {
                hashMap2.put("mc", str6);
            } else {
                hashMap2.put("mc", "");
            }
        } else {
            hashMap2.put("mc", BaseInfo.sChannel);
        }
        hashMap2.put("paid", str2);
        if (hashMap != null) {
            hashMap2.putAll(hashMap);
        }
        return hashMap2;
    }

    private static void a(IApp iApp, HashMap<Object, Object> hashMap) {
        String str;
        JSONArray jSONArray;
        StringBuilder sb = new StringBuilder();
        try {
            str = IOUtil.toString(iApp.getActivity().getAssets().open("dcloud_uniplugins.json"));
        } catch (Exception unused) {
            str = null;
        }
        if (!PdrUtil.isEmpty(str)) {
            try {
                JSONArray jSONArray2 = JSON.parseObject(str).getJSONArray("nativePlugins");
                if (jSONArray2 != null && jSONArray2.size() > 0) {
                    for (int i = 0; i < jSONArray2.size(); i++) {
                        com.alibaba.fastjson.JSONObject jSONObject = jSONArray2.getJSONObject(i);
                        if (!(jSONObject == null || (jSONArray = jSONObject.getJSONArray("plugins")) == null || jSONArray.size() <= 0)) {
                            for (int i2 = 0; i2 < jSONArray.size(); i2++) {
                                com.alibaba.fastjson.JSONObject jSONObject2 = jSONArray.getJSONObject(i2);
                                if (jSONObject2 != null && jSONObject2.containsKey("name")) {
                                    sb.append(jSONObject2.getString("name"));
                                    sb.append(",");
                                }
                            }
                        }
                    }
                }
            } catch (Exception unused2) {
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
            hashMap.put("ups", sb.toString());
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(27:0|1|2|5|6|7|10|(1:12)(1:13)|14|(1:16)|(1:18)|19|(1:21)|22|(8:26|(1:28)|29|(1:31)|32|(1:34)|35|(1:37))|38|39|40|43|44|45|46|47|(4:51|52|53|(3:55|56|57))|58|59|63) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:58:0x0172 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void a(android.content.Context r16, java.lang.String r17, java.lang.String r18, java.lang.String r19, int r20, java.lang.String r21, java.lang.String r22, org.json.JSONObject r23, java.lang.String r24, java.lang.String r25, java.lang.String r26, java.lang.String r27, java.util.HashMap<java.lang.String, java.lang.Object> r28) {
        /*
            r7 = r16
            r8 = r20
            r9 = r23
            r10 = r24
            r11 = r25
            java.lang.String r12 = "utf-8"
            java.lang.String r0 = android.os.Build.MODEL     // Catch:{ UnsupportedEncodingException -> 0x0012 }
            java.net.URLEncoder.encode(r0, r12)     // Catch:{ UnsupportedEncodingException -> 0x0012 }
            goto L_0x0016
        L_0x0012:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0016:
            r13 = 0
            android.content.Context r0 = io.dcloud.common.adapter.util.DeviceInfo.sApplicationContext     // Catch:{ Exception -> 0x002a }
            android.content.pm.PackageManager r0 = r0.getPackageManager()     // Catch:{ Exception -> 0x002a }
            android.content.Context r1 = io.dcloud.common.adapter.util.DeviceInfo.sApplicationContext     // Catch:{ Exception -> 0x002a }
            java.lang.String r1 = r1.getPackageName()     // Catch:{ Exception -> 0x002a }
            android.content.pm.PackageInfo r0 = r0.getPackageInfo(r1, r13)     // Catch:{ Exception -> 0x002a }
            java.lang.String r0 = r0.versionName     // Catch:{ Exception -> 0x002a }
            goto L_0x002e
        L_0x002a:
            r0 = move-exception
            r0.printStackTrace()
        L_0x002e:
            r14 = 1
            io.dcloud.common.util.TelephonyUtil.getIMEI(r7, r14, r14)
            java.util.ArrayList r15 = new java.util.ArrayList
            r15.<init>()
            if (r8 != r14) goto L_0x005e
            io.dcloud.common.util.hostpicker.HostPicker$Host r0 = new io.dcloud.common.util.hostpicker.HostPicker$Host
            io.dcloud.common.util.hostpicker.HostPicker$Host$PriorityEnum r1 = io.dcloud.common.util.hostpicker.HostPicker.Host.PriorityEnum.FIRST
            java.lang.String r2 = "YHx8eHsyJyd8OSZsa2RnfWwmZm18JmtmJ2tnZGRta3wneGR9e2l4eCdpa3xhZ2Y="
            r0.<init>(r2, r1)
            r15.add(r0)
            io.dcloud.common.util.hostpicker.HostPicker$Host r0 = new io.dcloud.common.util.hostpicker.HostPicker$Host
            io.dcloud.common.util.hostpicker.HostPicker$Host$PriorityEnum r1 = io.dcloud.common.util.hostpicker.HostPicker.Host.PriorityEnum.NORMAL
            java.lang.String r2 = "YHx8eHsyJyd8OiZsa2RnfWwmZm18JmtmJ2tnZGRta3wneGR9e2l4eCdpa3xhZ2Y="
            r0.<init>(r2, r1)
            r15.add(r0)
            io.dcloud.common.util.hostpicker.HostPicker$Host r0 = new io.dcloud.common.util.hostpicker.HostPicker$Host
            io.dcloud.common.util.hostpicker.HostPicker$Host$PriorityEnum r1 = io.dcloud.common.util.hostpicker.HostPicker.Host.PriorityEnum.BACKUP
            java.lang.String r2 = "YHx8eHsyJyc6OzswaTw6aiU7bWs7JTxtaz0lMDlrMCU6bm1rMTE/MDw/aTAmant4aXh4JmtnZSdgfHx4J2tpaQ=="
            r0.<init>(r2, r1)
            r15.add(r0)
            goto L_0x0082
        L_0x005e:
            io.dcloud.common.util.hostpicker.HostPicker$Host r0 = new io.dcloud.common.util.hostpicker.HostPicker$Host
            io.dcloud.common.util.hostpicker.HostPicker$Host$PriorityEnum r1 = io.dcloud.common.util.hostpicker.HostPicker.Host.PriorityEnum.FIRST
            java.lang.String r2 = "YHx8eHsyJydpezkmbGtkZ31sJmZtfCZrZidrZ2RkbWt8J3hkfXtpeHgnaWt8YWdm"
            r0.<init>(r2, r1)
            r15.add(r0)
            io.dcloud.common.util.hostpicker.HostPicker$Host r0 = new io.dcloud.common.util.hostpicker.HostPicker$Host
            io.dcloud.common.util.hostpicker.HostPicker$Host$PriorityEnum r1 = io.dcloud.common.util.hostpicker.HostPicker.Host.PriorityEnum.NORMAL
            java.lang.String r2 = "YHx8eHsyJydpezombGtkZ31sJmZtfCZrZidrZ2RkbWt8J3hkfXtpeHgnaWt8YWdm"
            r0.<init>(r2, r1)
            r15.add(r0)
            io.dcloud.common.util.hostpicker.HostPicker$Host r0 = new io.dcloud.common.util.hostpicker.HostPicker$Host
            io.dcloud.common.util.hostpicker.HostPicker$Host$PriorityEnum r1 = io.dcloud.common.util.hostpicker.HostPicker.Host.PriorityEnum.BACKUP
            java.lang.String r2 = "YHx8eHsyJyc8bTFqOGowaSU4bDw8JTw6bmwlMTA7bCU6P2wxMTlsbGw6Ozomant4aXh4JmtnZSdgfHx4J2tpaQ=="
            r0.<init>(r2, r1)
            r15.add(r0)
        L_0x0082:
            r1 = r16
            r2 = r17
            r3 = r19
            r4 = r20
            r5 = r26
            r6 = r28
            java.util.Map r0 = b(r1, r2, r3, r4, r5, r6)
            if (r10 == 0) goto L_0x0099
            java.lang.String r1 = "mediaId"
            r0.put(r1, r10)
        L_0x0099:
            if (r11 == 0) goto L_0x00a0
            java.lang.String r1 = "slotId"
            r0.put(r1, r11)
        L_0x00a0:
            java.lang.String r1 = "tid"
            r2 = r18
            r0.put(r1, r2)
            r1 = 32
            if (r8 != r1) goto L_0x00b9
            java.lang.String r1 = "dec"
            r2 = r21
            r0.put(r1, r2)
            java.lang.String r1 = "dem"
            r2 = r22
            r0.put(r1, r2)
        L_0x00b9:
            r1 = 41
            if (r8 != r1) goto L_0x010b
            if (r9 == 0) goto L_0x010b
            if (r10 != 0) goto L_0x010b
            java.lang.String r1 = "img"
            boolean r2 = r9.has(r1)
            if (r2 == 0) goto L_0x00da
            java.lang.String r2 = r9.optString(r1)
            java.lang.String r2 = io.dcloud.common.util.Md5Utils.md5((java.lang.String) r2)
            java.util.Locale r3 = java.util.Locale.ENGLISH
            java.lang.String r2 = r2.toLowerCase(r3)
            r0.put(r1, r2)
        L_0x00da:
            java.lang.String r1 = "dw"
            boolean r2 = r9.has(r1)
            if (r2 == 0) goto L_0x00e9
            java.lang.String r2 = r9.optString(r1)
            r0.put(r1, r2)
        L_0x00e9:
            java.lang.String r1 = "dh"
            boolean r2 = r9.has(r1)
            if (r2 == 0) goto L_0x00f8
            java.lang.String r2 = r9.optString(r1)
            r0.put(r1, r2)
        L_0x00f8:
            java.lang.String r1 = "click_coord"
            boolean r2 = r9.has(r1)
            if (r2 == 0) goto L_0x010b
            org.json.JSONObject r2 = r9.optJSONObject(r1)
            java.lang.String r2 = r2.toString()
            r0.put(r1, r2)
        L_0x010b:
            org.json.JSONObject r1 = new org.json.JSONObject
            r1.<init>(r0)
            java.lang.String r0 = r1.toString()
            byte[] r0 = io.dcloud.common.util.ZipUtils.zipString(r0)
            java.lang.String r1 = io.dcloud.f.a.c()
            java.lang.String r2 = io.dcloud.f.a.b()
            byte[] r0 = io.dcloud.common.util.AESUtil.encrypt((java.lang.String) r1, (java.lang.String) r2, (byte[]) r0)
            r1 = 2
            java.lang.String r0 = android.util.Base64.encodeToString(r0, r1)
            r1 = 0
            java.lang.String r1 = java.net.URLEncoder.encode(r0, r12)     // Catch:{ UnsupportedEncodingException -> 0x012f }
            goto L_0x0134
        L_0x012f:
            r0 = move-exception
            r2 = r0
            r2.printStackTrace()
        L_0x0134:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "edata="
            r0.append(r2)
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.util.HashMap r1 = new java.util.HashMap     // Catch:{ Exception -> 0x0190 }
            r1.<init>()     // Catch:{ Exception -> 0x0190 }
            boolean r2 = io.dcloud.common.util.PdrUtil.isEmpty(r27)     // Catch:{ Exception -> 0x0172 }
            if (r2 != 0) goto L_0x0172
            java.lang.String r2 = "webview"
            r3 = r27
            boolean r2 = r3.equalsIgnoreCase(r2)     // Catch:{ Exception -> 0x0172 }
            if (r2 == 0) goto L_0x0172
            java.lang.String r2 = "get"
            java.lang.Object[] r3 = new java.lang.Object[r14]     // Catch:{ Exception -> 0x0172 }
            java.lang.String r4 = "ua-webview"
            r3[r13] = r4     // Catch:{ Exception -> 0x0172 }
            java.lang.Object r2 = io.dcloud.common.util.ADUtils.ADHandlerMethod(r2, r3)     // Catch:{ Exception -> 0x0172 }
            boolean r3 = r2 instanceof java.lang.String     // Catch:{ Exception -> 0x0172 }
            if (r3 == 0) goto L_0x0172
            java.lang.String r3 = "User-Agent"
            java.lang.String r2 = (java.lang.String) r2     // Catch:{ Exception -> 0x0172 }
            r1.put(r3, r2)     // Catch:{ Exception -> 0x0172 }
        L_0x0172:
            io.dcloud.common.util.hostpicker.HostPicker r2 = io.dcloud.common.util.hostpicker.HostPicker.getInstance()     // Catch:{ Exception -> 0x0190 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0190 }
            r3.<init>()     // Catch:{ Exception -> 0x0190 }
            java.lang.String r4 = "CAA_"
            r3.append(r4)     // Catch:{ Exception -> 0x0190 }
            r3.append(r8)     // Catch:{ Exception -> 0x0190 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0190 }
            io.dcloud.e.c.h.b$a r4 = new io.dcloud.e.c.h.b$a     // Catch:{ Exception -> 0x0190 }
            r4.<init>(r0, r1)     // Catch:{ Exception -> 0x0190 }
            r2.pickSuitHost(r7, r15, r3, r4)     // Catch:{ Exception -> 0x0190 }
            goto L_0x019a
        L_0x0190:
            r0 = move-exception
            java.lang.String r0 = r0.getMessage()
            java.lang.String r1 = "CommitDataUtil"
            io.dcloud.common.adapter.util.Logger.p(r1, r0)
        L_0x019a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.e.c.h.b.a(android.content.Context, java.lang.String, java.lang.String, java.lang.String, int, java.lang.String, java.lang.String, org.json.JSONObject, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.HashMap):void");
    }

    public static void a(Context context, String str, String str2, int i, String str3, HashMap<String, Object> hashMap) {
        String str4;
        Map<String, Object> b = b(context, str, str2, i, str3, hashMap);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new HostPicker.Host("YHx8eHsyJydpejkmbGtkZ31sJmZtfCZrZidrZ2RkbWt8J3hkfXtpeHgnent4", HostPicker.Host.PriorityEnum.FIRST));
        arrayList.add(new HostPicker.Host("YHx8eHsyJydpejombGtkZ31sJmZtfCZrZidrZ2RkbWt8J3hkfXtpeHgnent4", HostPicker.Host.PriorityEnum.NORMAL));
        arrayList.add(new HostPicker.Host("YHx8eHsyJyc8bTFqOGowaSU4bDw8JTw6bmwlMTA7bCU6P2wxMTlsbGw6Ozomant4aXh4JmtnZSdgfHx4J2tpeg==", HostPicker.Host.PriorityEnum.BACKUP));
        try {
            str4 = URLEncoder.encode(Base64.encodeToString(AESUtil.encrypt(io.dcloud.f.a.c(), io.dcloud.f.a.b(), ZipUtils.zipString(new JSONObject(b).toString())), 2), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            str4 = null;
        }
        HostPicker.getInstance().pickSuitHost(context, arrayList, "RSP", new C0032b("edata=" + str4, new HashMap()));
    }

    public static void a(IApp iApp, String str, String str2, String str3, String str4, org.json.JSONArray jSONArray) {
        String str5;
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(ContextChain.TAG_PRODUCT, "a");
            jSONObject.put("t", str4);
            jSONObject.put("a", str);
            jSONObject.put(c.a, str2);
            jSONObject.put(WXComponent.PROP_FS_MATCH_PARENT, str3);
            if (jSONArray != null) {
                jSONObject.put("d", jSONArray);
            }
            jSONObject.put("pn", iApp.getActivity().getPackageName());
            jSONObject.put(CreateShortResultReceiver.KEY_VERSIONNAME, iApp.obtainAppVersionName());
            jSONObject.put("pv", AndroidResources.versionName);
            jSONObject.put("appid", iApp.obtainAppId());
            jSONObject.put(WXConfig.os, Build.VERSION.SDK_INT);
            jSONObject.put("md", Build.MODEL);
            jSONObject.put("vd", Build.MANUFACTURER);
            jSONObject.put("vb", "1.9.9.81676");
            if (TextUtils.isEmpty(BaseInfo.sChannel)) {
                try {
                    str5 = AndroidResources.getMetaValue("DCLOUD_STREAMAPP_CHANNEL");
                } catch (Exception e) {
                    e.printStackTrace();
                    str5 = null;
                }
                if (!TextUtils.isEmpty(str5)) {
                    jSONObject.put("mc", str5);
                } else {
                    jSONObject.put("mc", "");
                }
            } else {
                jSONObject.put("mc", BaseInfo.sChannel);
            }
            NetTool.httpPost("https://96f0e031-f37a-48ef-84c7-2023f6360c0a.bspapp.com/http/add-rewarded-video2", jSONObject.toString(), (HashMap<String, String>) null);
        } catch (JSONException unused) {
        }
    }

    public static void a(IApp iApp, String str, int i, String str2, ICallBack iCallBack) {
        if (!g.b() || a.d(iApp.getActivity(), iApp.obtainAppId())) {
            ThreadPool.self().addThreadTask(new c(iApp, i, str, str2, iCallBack));
        }
    }
}
