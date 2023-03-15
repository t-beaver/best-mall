package io.dcloud.sdk.base.dcloud;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.text.TextUtils;
import android.view.MotionEvent;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.dcloud.android.widget.dialog.DCloudAlertDialog;
import com.taobao.weex.common.Constants;
import com.taobao.weex.ui.component.WXBasicComponentType;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.feature.uniapp.adapter.AbsURIAdapter;
import io.dcloud.h.c.c.a.b.b;
import io.dcloud.sdk.base.dcloud.ADHandler;
import io.dcloud.sdk.base.dcloud.k.a;
import io.dcloud.sdk.core.util.ReflectUtil;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

public class ADHandler {
    /* access modifiers changed from: private */
    public static LinkedList<File> a;

    class a implements Runnable {
        final /* synthetic */ j a;

        a(j jVar) {
            this.a = jVar;
        }

        public void run() {
            this.a.execute();
        }
    }

    class b implements Runnable {
        final /* synthetic */ g a;
        final /* synthetic */ Context b;
        final /* synthetic */ String c;
        final /* synthetic */ String d;

        b(g gVar, Context context, String str, String str2) {
            this.a = gVar;
            this.b = context;
            this.c = str;
            this.d = str2;
        }

        public void run() {
            int i = this.a.e() ? 45 : 40;
            JSONObject c2 = this.a.c();
            if (c2 != null && c2.has("ua")) {
                c2.optString("ua");
            }
            ADHandler.b(i, this.b, this.c, this.d, this.a, io.dcloud.h.c.a.d().b().getAppId());
        }
    }

    class c implements Runnable {
        final /* synthetic */ g a;
        final /* synthetic */ Context b;
        final /* synthetic */ String c;
        final /* synthetic */ String d;

        c(g gVar, Context context, String str, String str2) {
            this.a = gVar;
            this.b = context;
            this.c = str;
            this.d = str2;
        }

        public void run() {
            JSONObject c2 = this.a.c();
            if (c2 != null && c2.has("ua")) {
                c2.optString("ua");
            }
            ADHandler.b(50, this.b, this.c, this.d, this.a, io.dcloud.h.c.a.d().b().getAppId());
        }
    }

    class d implements Runnable {
        final /* synthetic */ g a;
        final /* synthetic */ Context b;
        final /* synthetic */ String c;
        final /* synthetic */ String d;

        d(g gVar, Context context, String str, String str2) {
            this.a = gVar;
            this.b = context;
            this.c = str;
            this.d = str2;
        }

        public void run() {
            JSONObject jSONObject;
            int i;
            if (!this.a.d()) {
                jSONObject = ADHandler.b(this.a);
                i = 41;
            } else {
                jSONObject = null;
                i = 46;
            }
            JSONObject c2 = this.a.c();
            String optString = (c2 == null || !c2.has("ua")) ? "" : c2.optString("ua");
            Context context = this.b;
            g gVar = this.a;
            io.dcloud.h.c.c.a.b.b.a(context, gVar.h, this.c, this.d, i, (String) null, (String) null, jSONObject, (String) null, (String) null, gVar.k, optString, (HashMap<String, Object>) null);
        }
    }

    class e implements h<File> {
        final /* synthetic */ g a;
        final /* synthetic */ Context b;

        e(g gVar, Context context) {
            this.a = gVar;
            this.b = context;
        }

        /* renamed from: a */
        public void operate(File file) {
            ADHandler.a.add(file);
            ADHandler.b(this.b, file, this.a);
        }

        public boolean find() {
            return this.a.a();
        }
    }

    class f implements j {
        final /* synthetic */ JSONObject a;
        final /* synthetic */ String b;
        final /* synthetic */ String c;
        final /* synthetic */ i d;

        f(JSONObject jSONObject, String str, String str2, i iVar) {
            this.a = jSONObject;
            this.b = str;
            this.c = str2;
            this.d = iVar;
        }

        public void execute() {
            HashMap hashMap;
            byte[] bArr = null;
            if (!this.a.has("ua") || !this.a.optString("ua").equalsIgnoreCase("webview")) {
                hashMap = null;
            } else {
                hashMap = new HashMap();
                hashMap.put(IWebview.USER_AGENT, ADHandler.a("ua-webview"));
            }
            boolean z = true;
            try {
                bArr = io.dcloud.h.a.e.d.a(this.b, (HashMap<String, String>) hashMap, true);
            } catch (Exception unused) {
            }
            StringBuilder sb = new StringBuilder();
            sb.append("download file is nulll");
            if (bArr != null) {
                z = false;
            }
            sb.append(z);
            sb.append("src=");
            sb.append(this.b);
            ADHandler.a("shutao", sb.toString());
            if (bArr != null) {
                h.a(bArr, 0, this.c);
                this.d.a();
                return;
            }
            this.d.b();
        }
    }

    public static class g {
        MotionEvent a;
        MotionEvent b;
        String c;
        JSONObject d;
        public Object e;
        String f;
        String g;
        String h;
        int i = 0;
        int j = 0;
        String k;

        class a extends BroadcastReceiver {
            final /* synthetic */ i a;

            a(i iVar) {
                this.a = iVar;
            }

            public void onReceive(Context context, Intent intent) {
                try {
                    long longExtra = intent.getLongExtra("end", 0) - intent.getLongExtra("begin", 0);
                    ADHandler.a("ADReceive", "useTime=" + longExtra);
                    if (longExtra <= 3000) {
                        this.a.onReceiver((JSONObject) null);
                    }
                    ADHandler.a("ADReceive", "unregisterReceiver");
                    context.unregisterReceiver(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        class b implements Runnable {
            b() {
            }

            public void run() {
            }
        }

        class c implements a.C0075a {
            final /* synthetic */ Context a;
            final /* synthetic */ String b;
            final /* synthetic */ String c;
            final /* synthetic */ String d;

            c(Context context, String str, String str2, String str3) {
                this.a = context;
                this.b = str;
                this.c = str2;
                this.d = str3;
            }

            public void a(io.dcloud.sdk.base.dcloud.k.a aVar) {
                Context context = this.a;
                String str = this.b;
                String str2 = this.c;
                g gVar = g.this;
                ADHandler.b(32, context, str, str2, gVar, gVar.h);
            }

            public void b(io.dcloud.sdk.base.dcloud.k.a aVar) {
                Context context = this.a;
                String str = this.b;
                String str2 = this.c;
                g gVar = g.this;
                ADHandler.b(30, context, str, str2, gVar, gVar.h);
                PackageInfo f = io.dcloud.h.a.e.b.f(this.a, this.d);
                if (f != null) {
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
                    intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
                    intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
                    intentFilter.addDataScheme("package");
                    this.a.registerReceiver(new InstallReceiver(g.this, f.packageName), intentFilter);
                }
                Intent intent = new Intent();
                intent.addFlags(268435456);
                intent.setAction("android.intent.action.VIEW");
                if (Build.VERSION.SDK_INT >= 24) {
                    Context context2 = this.a;
                    Uri uriForFile = FileProvider.getUriForFile(context2, this.a.getPackageName() + ".dc.fileprovider", new File(this.d));
                    intent.addFlags(1);
                    intent.setDataAndType(uriForFile, "application/vnd.android.package-archive");
                } else {
                    intent.setDataAndType(Uri.fromFile(new File(this.d)), "application/vnd.android.package-archive");
                }
                this.a.startActivity(intent);
            }
        }

        public void a(String str) {
            this.k = str;
        }

        /* access modifiers changed from: package-private */
        public JSONObject b() {
            return this.d.optJSONObject("data");
        }

        /* access modifiers changed from: package-private */
        public JSONObject c() {
            return this.d;
        }

        /* access modifiers changed from: package-private */
        public boolean d() {
            return this.j == 1;
        }

        /* access modifiers changed from: package-private */
        public boolean e() {
            return this.i == 1;
        }

        /* access modifiers changed from: package-private */
        public JSONObject f() {
            return this.d.optJSONObject("report");
        }

        public boolean a() {
            return (this.d == null || this.e == null) ? false : true;
        }

        /* access modifiers changed from: package-private */
        public void a(Context context, i iVar) {
            if (iVar != null) {
                a aVar = new a(iVar);
                try {
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction("ad_receive");
                    LocalBroadcastManager.getInstance(context).registerReceiver(aVar, intentFilter);
                    ADHandler.a("ADReceive", "registerReceiver");
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }

        public void a(Context context, String str, String str2) {
            String str3;
            io.dcloud.h.a.e.b.a(context);
            io.dcloud.sdk.base.dcloud.k.b a2 = io.dcloud.sdk.base.dcloud.k.b.a(context);
            List<io.dcloud.sdk.base.dcloud.k.a> a3 = a2.a();
            JSONObject optJSONObject = this.d.optJSONObject("data");
            if (optJSONObject != null) {
                String optString = optJSONObject.optString("url");
                String optString2 = optJSONObject.optString("downloadAppName");
                for (io.dcloud.sdk.base.dcloud.k.a c2 : a3) {
                    if (c2.c().equals(optString)) {
                        return;
                    }
                }
                if (TextUtils.isEmpty(optString2)) {
                    str3 = null;
                } else {
                    str3 = optString2 + ".apk";
                }
                String a4 = io.dcloud.h.a.a.a(optString, ".apk", str3);
                String absolutePath = context.getExternalFilesDir((String) null).getAbsolutePath();
                StringBuilder sb = new StringBuilder();
                sb.append(absolutePath);
                String str4 = "/";
                if (absolutePath.endsWith(str4)) {
                    str4 = "";
                }
                sb.append(str4);
                String str5 = sb.toString() + "Download/" + a4;
                io.dcloud.h.a.e.f.a().a(new b());
                ADHandler.b(29, context, str, str2, this, this.h);
                io.dcloud.sdk.base.dcloud.k.a aVar = new io.dcloud.sdk.base.dcloud.k.a();
                aVar.a(context, optString, str5);
                aVar.a(new c(context, str, str2, str5));
                a2.a(aVar);
            }
        }
    }

    interface h<E> {
        boolean find();

        void operate(E e);
    }

    public interface i {
        void a();

        void b();
    }

    interface j {
        void execute();
    }

    public static class InstallReceiver extends BroadcastReceiver {
        g a;
        String b;

        public InstallReceiver(g gVar, String str) {
            this.a = gVar;
            this.b = str;
        }

        public void onReceive(Context context, Intent intent) {
            if (this.b.equals(intent.getData().getSchemeSpecificPart())) {
                String optString = this.a.b().optString("tid");
                String a2 = ADHandler.a(context, "adid");
                g gVar = this.a;
                ADHandler.b(31, context, optString, a2, gVar, gVar.h);
                context.unregisterReceiver(this);
            }
        }
    }

    static void a(String str, String str2) {
    }

    /* access modifiers changed from: private */
    public static void b(int i2, Context context, String str, String str2, g gVar, String str3) {
        io.dcloud.h.a.e.f.a().a(new Runnable(context, str3, str, str2, i2, gVar) {
            public final /* synthetic */ Context f$0;
            public final /* synthetic */ String f$1;
            public final /* synthetic */ String f$2;
            public final /* synthetic */ String f$3;
            public final /* synthetic */ int f$4;
            public final /* synthetic */ ADHandler.g f$5;

            {
                this.f$0 = r1;
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
                this.f$5 = r6;
            }

            public final void run() {
                b.a(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5.k);
            }
        });
    }

    static void c(Context context, g gVar, String str) {
        io.dcloud.h.a.e.f.a().a(new b(gVar, context, gVar.b().optString("tid"), str));
        if ("wanka".equals(gVar.c)) {
            a.f(context, gVar, str);
        } else if ("youdao".equals(gVar.c)) {
            b.f(context, gVar, str);
        } else if ("common".equals(gVar.c)) {
            e.a(context, gVar, str, "imptracker");
        }
    }

    static void b(Context context, g gVar, String str) {
        JSONObject b2 = gVar.b();
        String optString = gVar.b().optString("tid");
        if (!b2.has("dplk") || !io.dcloud.h.a.e.b.d(context, b2.optString("dplk"))) {
            String optString2 = b2.optString("action");
            if (TextUtils.equals("url", optString2)) {
                if (gVar.d()) {
                    c.a(context, b2.optString("url"));
                } else {
                    io.dcloud.h.a.e.b.e(context, b2.optString("url"));
                }
            } else if (TextUtils.equals(AbsoluteConst.SPNAME_DOWNLOAD, optString2)) {
                if (b2.has("expires")) {
                    try {
                        new SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(b2.optString("expires")).getTime();
                    } catch (ParseException e2) {
                        e2.printStackTrace();
                    }
                }
                String optString3 = gVar.c() != null ? gVar.c().optString("ua") : "";
                if (gVar.d()) {
                    c.a(context, gVar.h, optString, str, b2.optString("url"), b2.optString(AbsURIAdapter.BUNDLE), (io.dcloud.sdk.base.dcloud.k.d) null, optString3, gVar.k);
                } else {
                    gVar.a(context, optString, str);
                }
            } else if (TextUtils.equals("browser", optString2) && !gVar.d()) {
                io.dcloud.h.a.e.b.c(context, b2.optString("url"));
            }
        } else if (!gVar.d()) {
            io.dcloud.h.a.e.f.a().a(new c(gVar, context, optString, str));
            if ("wanka".equals(gVar.c)) {
                a.e(context, gVar, str);
            } else if ("youdao".equals(gVar.c)) {
                b.e(context, gVar, str);
            } else if ("common".equals(gVar.c)) {
                e.a(context, gVar, str, "dptracker");
            }
        }
    }

    public static String a(String str) {
        str.hashCode();
        str.hashCode();
        char c2 = 65535;
        switch (str.hashCode()) {
            case 2989182:
                if (str.equals("adid")) {
                    c2 = 0;
                    break;
                }
                break;
            case 93029116:
                if (str.equals("appid")) {
                    c2 = 1;
                    break;
                }
                break;
            case 1594810808:
                if (str.equals("ua-webview")) {
                    c2 = 2;
                    break;
                }
                break;
        }
        switch (c2) {
            case 0:
                return io.dcloud.h.c.a.d().b().getAdId();
            case 1:
                return io.dcloud.h.c.a.d().b().getAppId();
            case 2:
                return io.dcloud.h.a.d.b.h.e(io.dcloud.h.c.a.d().c());
            default:
                return null;
        }
    }

    public static String a(Context context, String str) {
        return a(str);
    }

    private static void a(j jVar) {
        if (jVar == null) {
            return;
        }
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            io.dcloud.h.a.e.f.a().a(new a(jVar));
        } else {
            jVar.execute();
        }
    }

    static String a(String str, JSONObject jSONObject) {
        try {
            str = str.replace("${User-Agent}", URLEncoder.encode(jSONObject.optString("u-a"), "utf-8"));
            str = str.replace("${click_id}", jSONObject.optString("click_id"));
            str = str.replace("${down_x}", String.valueOf(jSONObject.optInt("down_x", DCloudAlertDialog.DARK_THEME)));
            str = str.replace("${down_y}", String.valueOf(jSONObject.optInt("down_y", DCloudAlertDialog.DARK_THEME)));
            str = str.replace("${up_x}", String.valueOf(jSONObject.optInt("up_x", DCloudAlertDialog.DARK_THEME)));
            str = str.replace("${up_y}", String.valueOf(jSONObject.optInt("up_y", DCloudAlertDialog.DARK_THEME)));
            str = str.replace("${relative_down_x}", String.valueOf(jSONObject.optInt("relative_down_x", DCloudAlertDialog.DARK_THEME)));
            str = str.replace("${relative_down_y}", String.valueOf(jSONObject.optInt("relative_down_y", DCloudAlertDialog.DARK_THEME)));
            str = str.replace("${relative_up_x}", String.valueOf(jSONObject.optInt("relative_up_x", DCloudAlertDialog.DARK_THEME)));
            return str.replace("${relative_up_y}", String.valueOf(jSONObject.optInt("relative_up_y", DCloudAlertDialog.DARK_THEME)));
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
            return str;
        }
    }

    static void a(Context context, g gVar, String str) {
        io.dcloud.h.a.e.f.a().a(new d(gVar, context, gVar.b().optString("tid"), str));
        if ("wanka".equals(gVar.c)) {
            a.d(context, gVar, str);
        } else if ("youdao".equals(gVar.c)) {
            b.d(context, gVar, str);
        } else if ("common".equals(gVar.c)) {
            e.d(context, gVar, str);
        } else {
            b(context, gVar, str);
        }
    }

    private static void a(File[] fileArr) {
        if (fileArr != null) {
            for (int i2 = 0; i2 < fileArr.length - 1; i2++) {
                int i3 = 0;
                while (i3 < (fileArr.length - 1) - i2) {
                    int i4 = i3 + 1;
                    if (Long.parseLong(fileArr[i3].getName()) < Long.parseLong(fileArr[i4].getName())) {
                        File file = fileArr[i3];
                        fileArr[i3] = fileArr[i4];
                        fileArr[i4] = file;
                    }
                    i3 = i4;
                }
            }
        }
    }

    private static void a(Context context, h<File> hVar) {
        File file = new File(a(context));
        if (!file.exists()) {
            file.mkdirs();
        }
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            a(listFiles);
            for (File file2 : listFiles) {
                if (!hVar.find()) {
                    File[] listFiles2 = file2.listFiles();
                    for (File file3 : listFiles2) {
                        if (Long.parseLong(file3.getName()) <= System.currentTimeMillis()) {
                            h.a((Object) file3);
                        } else if (!hVar.find()) {
                            File[] listFiles3 = file3.listFiles();
                            for (File operate : listFiles3) {
                                hVar.operate(operate);
                                if (hVar.find()) {
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    h.a((Object) file2);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public static JSONObject b(g gVar) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(WXBasicComponentType.IMG, gVar.g);
            JSONObject c2 = gVar.c();
            jSONObject.put("dw", c2.optInt("dw"));
            jSONObject.put("dh", c2.optInt("dh"));
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("dx", c2.optInt("down_x"));
            jSONObject2.put(Constants.Name.DISTANCE_Y, c2.optInt("down_y"));
            jSONObject2.put("ux", c2.optInt("up_x"));
            jSONObject2.put("uy", c2.optInt("up_y"));
            jSONObject2.put("rdx", c2.optInt("relative_down_x"));
            jSONObject2.put("rdy", c2.optInt("relative_down_y"));
            jSONObject2.put("rux", c2.optInt("relative_up_x"));
            jSONObject2.put("ruy", c2.optInt("relative_up_y"));
            jSONObject.put("click_coord", jSONObject2);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jSONObject;
    }

    public static g b(Context context, String str) {
        return a(context, str, new g());
    }

    /* access modifiers changed from: private */
    public static void b(Context context, File file, g gVar) {
        JSONObject jSONObject;
        JSONObject optJSONObject;
        try {
            String str = new String(h.e(file.getAbsolutePath() + "/" + "data.json"));
            if (!TextUtils.isEmpty(str) && (optJSONObject = jSONObject.optJSONObject("data")) != null) {
                gVar.c = (jSONObject = new JSONObject(str)).optString("provider");
                gVar.d = jSONObject;
                gVar.i = jSONObject.optInt("es", 0);
                gVar.j = jSONObject.optInt("ec", 0);
                String optString = optJSONObject.optString("src");
                gVar.g = optString;
                boolean endsWith = optString.toLowerCase(Locale.ENGLISH).endsWith(".gif");
                StringBuilder sb = new StringBuilder();
                sb.append(file.getAbsolutePath());
                sb.append("/");
                sb.append(endsWith ? "img.gif" : "img.png");
                String sb2 = sb.toString();
                String str2 = file.getAbsolutePath() + "/" + "s.txt";
                if (new File(sb2).exists() && !new File(str2).exists()) {
                    if (endsWith) {
                        gVar.e = ReflectUtil.newInstance("pl.droidsonroids.gif.GifDrawable", new Class[]{String.class}, new Object[]{jSONObject.optString("srcPath")});
                    } else {
                        Bitmap decodeFile = BitmapFactory.decodeFile(sb2);
                        if (decodeFile != null) {
                            gVar.e = decodeFile;
                        }
                    }
                    gVar.f = sb2;
                    new File(str2).createNewFile();
                    h.a((Object) file.getAbsolutePath());
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    static g a(Context context, String str, g gVar) {
        gVar.h = str;
        a = new LinkedList<>();
        a(context, (h<File>) new e(gVar, context));
        if (!gVar.a() && a.size() != 0) {
            for (int i2 = 0; i2 < a.size(); i2++) {
                new File(a.get(i2).getAbsolutePath() + "/" + "s.txt").delete();
                if (i2 == 0) {
                    b(context, a.get(i2), gVar);
                }
            }
        }
        return gVar;
    }

    static void b(Context context, JSONObject jSONObject, long j2, i iVar) throws Exception {
        String a2 = a(context);
        JSONObject optJSONObject = jSONObject.optJSONObject("data");
        Date parse = new SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(optJSONObject.optString("expires"));
        if (parse.getTime() > System.currentTimeMillis()) {
            String optString = optJSONObject.optString("src");
            String encode = URLEncoder.encode(optString, "utf-8");
            a(context, jSONObject, optJSONObject.optString("tid"), optString, a2 + j2 + "/" + parse.getTime() + "/" + encode.hashCode() + "/", iVar);
            return;
        }
        iVar.b();
    }

    private static String a(Context context) {
        File externalCacheDir = context.getExternalCacheDir();
        if (externalCacheDir == null) {
            return "/sdcard/Android/data/" + context.getPackageName() + "/cache/ad/";
        }
        return externalCacheDir.getAbsolutePath() + "/ad/";
    }

    private static void a(Context context, JSONObject jSONObject, String str, String str2, String str3, i iVar) throws Exception {
        if (jSONObject == null || !jSONObject.has("es") || jSONObject.getInt("es") != 1) {
            System.currentTimeMillis();
            byte[] bytes = str.getBytes();
            h.a(bytes, 0, str3 + "tid.txt");
            StringBuilder sb = new StringBuilder();
            sb.append(str3);
            sb.append(str2.endsWith(".gif") ? "img.gif" : "img.png");
            String sb2 = sb.toString();
            jSONObject.put("srcPath", sb2);
            byte[] bytes2 = jSONObject.toString().getBytes();
            h.a(bytes2, 0, str3 + "data.json");
            a((j) new f(jSONObject, str2, sb2, iVar));
            return;
        }
        new c(context, jSONObject).d();
    }

    public static void a(Context context, JSONObject jSONObject, long j2, i iVar) {
        try {
            b(context, jSONObject, j2, iVar);
        } catch (Exception unused) {
            iVar.b();
        }
    }
}
