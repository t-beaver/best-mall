package io.dcloud.sdk.base.entry;

import android.content.Context;
import android.content.Intent;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.MotionEvent;
import androidx.core.app.JobIntentService;
import com.dcloud.android.widget.dialog.DCloudAlertDialog;
import com.nostra13.dcloudimageloader.core.download.BaseImageDownloader;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.h.a.d.b.g;
import io.dcloud.h.a.d.b.h;
import io.dcloud.h.a.e.f;
import io.dcloud.sdk.base.service.DownloadService;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdData implements Parcelable {
    public static final Parcelable.Creator<AdData> CREATOR = new d();
    private int a;
    private String action;
    /* access modifiers changed from: private */
    public String b;
    private String bundle;
    /* access modifiers changed from: private */
    public byte[] c;
    private String d;
    private String downloadAppName;
    private String dplk;
    /* access modifiers changed from: private */
    public String e;
    private String expires;
    /* access modifiers changed from: private */
    public String f;
    /* access modifiers changed from: private */
    public String g;
    /* access modifiers changed from: private */
    public String h;
    private List<TrackerBean> i;
    private List<TrackerBean> j;
    private List<TrackerBean> k;
    private MotionEvent l;
    private MotionEvent m;
    private boolean n;
    private RectF o;
    protected Context p;
    private String price;
    private String provider;
    /* access modifiers changed from: private */
    public String src;
    /* access modifiers changed from: private */
    public String tid;
    private String ua;
    private String url;

    class a implements Runnable {
        final /* synthetic */ String a;
        final /* synthetic */ e b;

        a(String str, e eVar) {
            this.a = str;
            this.b = eVar;
        }

        public void run() {
            byte[] a2 = io.dcloud.h.a.e.d.a(AdData.this.src, (HashMap<String, String>) null, true, new String[1]);
            if (a2 != null) {
                byte[] unused = AdData.this.c = a2;
                io.dcloud.h.a.e.c.a(AdData.this.c, 0, this.a);
                String unused2 = AdData.this.b = this.a;
                e eVar = this.b;
                if (eVar != null) {
                    eVar.a();
                    return;
                }
                return;
            }
            e eVar2 = this.b;
            if (eVar2 != null) {
                eVar2.a(60009, "图片下载失败");
            }
        }
    }

    class b implements Runnable {
        final /* synthetic */ Context a;

        b(Context context) {
            this.a = context;
        }

        public void run() {
            Context context = this.a;
            if (context != null) {
                io.dcloud.h.a.d.b.b.a(context, AdData.this.f, AdData.this.tid, "", 50, AdData.this.e, AdData.this.h, AdData.this.g, (HashMap<String, Object>) null);
            }
        }
    }

    class c implements Runnable {
        final /* synthetic */ TrackerBean a;

        c(TrackerBean trackerBean) {
            this.a = trackerBean;
        }

        public void run() {
            try {
                String a2 = AdData.this.a(this.a.a);
                HashMap hashMap = new HashMap();
                hashMap.put(IWebview.USER_AGENT, h.e(AdData.this.p));
                io.dcloud.h.a.e.d.a(a2, (HashMap<String, String>) hashMap, true);
            } catch (Exception unused) {
            }
        }
    }

    class d implements Parcelable.Creator<AdData> {
        d() {
        }

        /* renamed from: a */
        public AdData createFromParcel(Parcel parcel) {
            return new AdData(parcel);
        }

        /* renamed from: a */
        public AdData[] newArray(int i) {
            return new AdData[i];
        }
    }

    public interface e {
        void a();

        void a(int i, String str);
    }

    public static class ExtBean implements Parcelable {
        public static final Parcelable.Creator<ExtBean> CREATOR = new a();
        private String a;
        private int b;

        class a implements Parcelable.Creator<ExtBean> {
            a() {
            }

            /* renamed from: a */
            public ExtBean createFromParcel(Parcel parcel) {
                return new ExtBean(parcel);
            }

            /* renamed from: a */
            public ExtBean[] newArray(int i) {
                return new ExtBean[i];
            }
        }

        public ExtBean() {
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.a);
            parcel.writeInt(this.b);
        }

        protected ExtBean(Parcel parcel) {
            this.a = parcel.readString();
            this.b = parcel.readInt();
        }
    }

    public AdData() {
        this.a = BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT;
        this.e = "";
        this.i = new ArrayList();
        this.j = new ArrayList();
        this.k = new ArrayList();
        this.n = false;
        this.o = new RectF();
    }

    public int describeContents() {
        return 0;
    }

    public String h() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("expires", this.expires);
        } catch (JSONException unused) {
        }
        return jSONObject.toString();
    }

    public int i() {
        return this.a;
    }

    public String j() {
        return this.tid;
    }

    public String k() {
        return this.d;
    }

    public String l() {
        return this.url;
    }

    public boolean m() {
        if (this.p == null) {
            return false;
        }
        return new File(this.p.getCacheDir().getAbsolutePath() + "/dcloud_ad/" + this.src.hashCode()).exists();
    }

    public boolean n() {
        return !TextUtils.isEmpty(this.src) && !TextUtils.isEmpty(this.action) && !TextUtils.isEmpty(this.url);
    }

    public void writeToParcel(Parcel parcel, int i2) {
        parcel.writeString(this.provider);
        parcel.writeString(this.ua);
        parcel.writeString(this.src);
        parcel.writeString(this.action);
        parcel.writeString(this.url);
        parcel.writeString(this.bundle);
        parcel.writeString(this.downloadAppName);
        parcel.writeString(this.dplk);
        parcel.writeString(this.price);
        parcel.writeString(this.tid);
        parcel.writeString(this.expires);
        parcel.writeString(this.b);
        parcel.writeByteArray(this.c);
        parcel.writeString(this.d);
        parcel.writeString(this.e);
        parcel.writeString(this.f);
        parcel.writeString(this.g);
        parcel.writeString(this.h);
        parcel.writeTypedList(this.i);
        parcel.writeTypedList(this.j);
        parcel.writeTypedList(this.k);
        parcel.writeParcelable(this.l, i2);
        parcel.writeParcelable(this.m, i2);
        parcel.writeByte(this.n ? (byte) 1 : 0);
        parcel.writeParcelable(this.o, i2);
    }

    public static class TrackerBean implements Parcelable {
        public static final Parcelable.Creator<TrackerBean> CREATOR = new a();
        /* access modifiers changed from: private */
        public String a;

        class a implements Parcelable.Creator<TrackerBean> {
            a() {
            }

            /* renamed from: a */
            public TrackerBean createFromParcel(Parcel parcel) {
                return new TrackerBean(parcel);
            }

            /* renamed from: a */
            public TrackerBean[] newArray(int i) {
                return new TrackerBean[i];
            }
        }

        public TrackerBean(String str) {
            this.a = str;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.a);
        }

        protected TrackerBean(Parcel parcel) {
            this.a = parcel.readString();
        }
    }

    /* access modifiers changed from: package-private */
    public void b(JSONObject jSONObject) {
        g.a().a(this.p, this, jSONObject.toString());
    }

    public void c(Context context) {
        this.p = context;
    }

    public String d() {
        return this.g;
    }

    public String e() {
        return this.f;
    }

    public String f() {
        return this.b;
    }

    public byte[] g() {
        return this.c;
    }

    public void b(MotionEvent motionEvent) {
        this.m = motionEvent;
    }

    public String c() {
        return this.e;
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a(org.json.JSONObject r5, io.dcloud.sdk.base.entry.AdData.e r6) {
        /*
            r4 = this;
            java.lang.String r0 = "data"
            org.json.JSONObject r5 = r5.optJSONObject(r0)
            if (r5 == 0) goto L_0x00c5
            java.util.ArrayList r0 = new java.util.ArrayList
            java.lang.Class r1 = r4.getClass()
            java.lang.reflect.Field[] r1 = r1.getDeclaredFields()
            java.util.List r1 = java.util.Arrays.asList(r1)
            r0.<init>(r1)
            java.lang.Class r1 = r4.getClass()
            java.lang.Class r1 = r1.getSuperclass()
            if (r1 == 0) goto L_0x0039
            java.lang.Class r1 = r4.getClass()
            java.lang.Class r1 = r1.getSuperclass()
            java.lang.reflect.Field[] r1 = r1.getDeclaredFields()
            int r2 = r1.length
            if (r2 <= 0) goto L_0x0039
            java.util.List r1 = java.util.Arrays.asList(r1)
            r0.addAll(r1)
        L_0x0039:
            java.util.Iterator r0 = r0.iterator()
        L_0x003d:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x006d
            java.lang.Object r1 = r0.next()
            java.lang.reflect.Field r1 = (java.lang.reflect.Field) r1
            java.lang.Class r2 = r1.getType()     // Catch:{  }
            java.lang.Class<java.lang.String> r3 = java.lang.String.class
            if (r2 != r3) goto L_0x003d
            java.lang.String r2 = r1.getName()     // Catch:{  }
            boolean r2 = r5.has(r2)     // Catch:{  }
            if (r2 == 0) goto L_0x003d
            java.lang.String r2 = r1.getName()     // Catch:{  }
            java.lang.String r2 = r5.optString(r2)     // Catch:{  }
            r3 = 1
            r1.setAccessible(r3)     // Catch:{ Exception -> 0x006b }
            r1.set(r4, r2)     // Catch:{ Exception -> 0x006b }
            goto L_0x003d
        L_0x006b:
            goto L_0x003d
        L_0x006d:
            java.lang.String r5 = r4.src
            boolean r5 = android.text.TextUtils.isEmpty(r5)
            if (r5 != 0) goto L_0x00b9
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            android.content.Context r0 = r4.p
            java.io.File r0 = r0.getCacheDir()
            java.lang.String r0 = r0.getAbsolutePath()
            r5.append(r0)
            java.lang.String r0 = "/dcloud_ad/img/"
            r5.append(r0)
            java.lang.String r0 = r4.src
            int r0 = r0.hashCode()
            r5.append(r0)
            java.lang.String r5 = r5.toString()
            java.io.File r0 = new java.io.File
            r0.<init>(r5)
            boolean r0 = r0.exists()
            if (r0 == 0) goto L_0x00ac
            r4.b = r5
            if (r6 == 0) goto L_0x00ab
            r6.a()
        L_0x00ab:
            return
        L_0x00ac:
            io.dcloud.h.a.e.f r0 = io.dcloud.h.a.e.f.a()
            io.dcloud.sdk.base.entry.AdData$a r1 = new io.dcloud.sdk.base.entry.AdData$a
            r1.<init>(r5, r6)
            r0.a(r1)
            goto L_0x00d0
        L_0x00b9:
            if (r6 == 0) goto L_0x00d0
            r5 = 60008(0xea68, float:8.4089E-41)
            java.lang.String r0 = "图片资源路径异常"
            r6.a(r5, r0)
            goto L_0x00d0
        L_0x00c5:
            if (r6 == 0) goto L_0x00d0
            r5 = 60007(0xea67, float:8.4088E-41)
            java.lang.String r0 = "无广告填充"
            r6.a(r5, r0)
        L_0x00d0:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.sdk.base.entry.AdData.a(org.json.JSONObject, io.dcloud.sdk.base.entry.AdData$e):void");
    }

    private void b(Context context) {
        a(this.j);
        if (TextUtils.isEmpty(this.dplk) || !io.dcloud.h.a.e.b.d(context, this.dplk)) {
            String str = this.action;
            str.hashCode();
            str.hashCode();
            char c2 = 65535;
            switch (str.hashCode()) {
                case 116079:
                    if (str.equals("url")) {
                        c2 = 0;
                        break;
                    }
                    break;
                case 150940456:
                    if (str.equals("browser")) {
                        c2 = 1;
                        break;
                    }
                    break;
                case 1427818632:
                    if (str.equals(AbsoluteConst.SPNAME_DOWNLOAD)) {
                        c2 = 2;
                        break;
                    }
                    break;
            }
            switch (c2) {
                case 0:
                    try {
                        Intent intent = new Intent();
                        intent.setClass(context, Class.forName("io.dcloud.sdk.activity.WebViewActivity"));
                        intent.putExtra("url", this.url);
                        intent.setData(Uri.parse(this.url));
                        intent.setAction("android.intent.action.VIEW");
                        intent.setFlags(268435456);
                        context.startActivity(intent);
                        return;
                    } catch (Exception unused) {
                        return;
                    }
                case 1:
                    io.dcloud.h.a.e.b.c(context, this.url);
                    return;
                case 2:
                    Intent intent2 = new Intent();
                    intent2.putExtra("url", this.url);
                    intent2.putExtra("data", this);
                    JobIntentService.enqueueWork(context, DownloadService.class, 10010, intent2);
                    return;
                default:
                    return;
            }
        } else {
            a(this.k);
            f.a().a(new b(context));
        }
    }

    protected AdData(Parcel parcel) {
        this.a = BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT;
        this.e = "";
        this.i = new ArrayList();
        this.j = new ArrayList();
        this.k = new ArrayList();
        boolean z = false;
        this.n = false;
        this.o = new RectF();
        this.provider = parcel.readString();
        this.ua = parcel.readString();
        this.src = parcel.readString();
        this.action = parcel.readString();
        this.url = parcel.readString();
        this.bundle = parcel.readString();
        this.downloadAppName = parcel.readString();
        this.dplk = parcel.readString();
        this.price = parcel.readString();
        this.tid = parcel.readString();
        this.expires = parcel.readString();
        this.b = parcel.readString();
        this.c = parcel.createByteArray();
        this.d = parcel.readString();
        this.e = parcel.readString();
        this.f = parcel.readString();
        this.g = parcel.readString();
        this.h = parcel.readString();
        Parcelable.Creator<TrackerBean> creator = TrackerBean.CREATOR;
        this.i = parcel.createTypedArrayList(creator);
        this.j = parcel.createTypedArrayList(creator);
        this.k = parcel.createTypedArrayList(creator);
        this.l = (MotionEvent) parcel.readParcelable(MotionEvent.class.getClassLoader());
        this.m = (MotionEvent) parcel.readParcelable(MotionEvent.class.getClassLoader());
        this.n = parcel.readByte() != 0 ? true : z;
        this.o = (RectF) parcel.readParcelable(RectF.class.getClassLoader());
    }

    public String b() {
        return this.h;
    }

    private void a(JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject("report");
        if (optJSONObject != null) {
            a(optJSONObject.optJSONArray("imptracker"), this.i);
            a(optJSONObject.optJSONArray("clktracker"), this.j);
            a(optJSONObject.optJSONArray("dptracker"), this.k);
        }
    }

    private void a(JSONArray jSONArray, List<TrackerBean> list) {
        if (jSONArray != null) {
            for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                JSONObject optJSONObject = jSONArray.optJSONObject(i2);
                if (optJSONObject != null) {
                    list.add(new TrackerBean(optJSONObject.optString("url")));
                }
            }
        }
    }

    public void a(JSONObject jSONObject, e eVar, boolean z) {
        this.d = String.valueOf(jSONObject.hashCode());
        a(jSONObject);
        a(jSONObject, eVar);
        int optInt = jSONObject.optInt("skip", BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT);
        this.a = optInt;
        if (optInt <= 0) {
            this.a = BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT;
        }
        this.e = jSONObject.optString("appid", "");
        this.h = jSONObject.optString("adpid", "");
        this.tid = jSONObject.optString("tid", "");
        this.g = jSONObject.optString("adid", "");
        this.f = jSONObject.optString("did", "");
        if (n() && z && !TextUtils.isEmpty(this.expires)) {
            b(jSONObject);
        }
    }

    public void a(MotionEvent motionEvent) {
        this.l = motionEvent;
    }

    public void a(Context context) {
        b(context);
    }

    /* access modifiers changed from: package-private */
    public String a(String str) {
        try {
            str = str.replace("${User-Agent}", URLEncoder.encode(h.e(this.p), "utf-8")).replace("${click_id}", "");
            MotionEvent motionEvent = this.l;
            int i2 = DCloudAlertDialog.DARK_THEME;
            str = str.replace("${down_x}", String.valueOf(motionEvent != null ? Math.round(motionEvent.getX()) : DCloudAlertDialog.DARK_THEME));
            MotionEvent motionEvent2 = this.l;
            str = str.replace("${down_y}", String.valueOf(motionEvent2 != null ? Math.round(motionEvent2.getY()) : DCloudAlertDialog.DARK_THEME));
            MotionEvent motionEvent3 = this.m;
            str = str.replace("${up_x}", String.valueOf(motionEvent3 != null ? Math.round(motionEvent3.getX()) : DCloudAlertDialog.DARK_THEME));
            MotionEvent motionEvent4 = this.m;
            str = str.replace("${up_y}", String.valueOf(motionEvent4 != null ? Math.round(motionEvent4.getY()) : DCloudAlertDialog.DARK_THEME));
            MotionEvent motionEvent5 = this.l;
            str = str.replace("${relative_down_x}", String.valueOf(motionEvent5 != null ? Math.round(motionEvent5.getX() - this.o.left) : DCloudAlertDialog.DARK_THEME));
            MotionEvent motionEvent6 = this.l;
            str = str.replace("${relative_down_y}", String.valueOf(motionEvent6 != null ? Math.round(motionEvent6.getY() - this.o.top) : DCloudAlertDialog.DARK_THEME));
            MotionEvent motionEvent7 = this.m;
            str = str.replace("${relative_up_x}", String.valueOf(motionEvent7 != null ? Math.round(motionEvent7.getX() - this.o.left) : DCloudAlertDialog.DARK_THEME));
            MotionEvent motionEvent8 = this.m;
            if (motionEvent8 != null) {
                i2 = Math.round(motionEvent8.getY() - this.o.top);
            }
            str = str.replace("${relative_up_y}", String.valueOf(i2));
            return str.replace("${ts}", String.valueOf(System.currentTimeMillis()));
        } catch (UnsupportedEncodingException unused) {
            return str;
        }
    }

    private void a(List<TrackerBean> list) {
        for (TrackerBean cVar : list) {
            f.a().a(new c(cVar));
        }
    }

    public void a() {
        a(this.i);
    }

    public void a(RectF rectF) {
        this.o = rectF;
    }
}
