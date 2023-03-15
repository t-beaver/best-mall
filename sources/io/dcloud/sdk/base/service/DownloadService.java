package io.dcloud.sdk.base.service;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import androidx.core.app.JobIntentService;
import androidx.core.content.FileProvider;
import dc.squareup.okhttp3.Call;
import dc.squareup.okhttp3.Callback;
import dc.squareup.okhttp3.OkHttpClient;
import dc.squareup.okhttp3.Request;
import dc.squareup.okhttp3.Response;
import dc.squareup.okhttp3.ResponseBody;
import io.dcloud.h.a.e.f;
import io.dcloud.sdk.base.entry.AdData;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DownloadService extends JobIntentService {
    private OkHttpClient a = new OkHttpClient.Builder().build();
    private List<b> b = new ArrayList();
    private final Object c = new Object();

    class a implements Runnable {
        final /* synthetic */ Context a;
        final /* synthetic */ AdData b;
        final /* synthetic */ int c;

        a(Context context, AdData adData, int i) {
            this.a = context;
            this.b = adData;
            this.c = i;
        }

        public void run() {
            io.dcloud.h.a.d.b.b.a(this.a, this.b.e(), this.b.j(), "", this.c, this.b.c(), this.b.b(), this.b.d(), (HashMap<String, Object>) null);
        }
    }

    private class b implements Callback {
        private AdData a;
        private boolean b = false;

        public b(AdData adData) {
            this.a = adData;
        }

        public String a() {
            AdData adData = this.a;
            return adData != null ? adData.k() : "";
        }

        public Request b() {
            return new Request.Builder().url(this.a.l()).build();
        }

        public boolean c() {
            return this.b;
        }

        public void onFailure(Call call, IOException iOException) {
            this.b = true;
            DownloadService.a(DownloadService.this.getApplicationContext(), this.a, 32);
        }

        public void onResponse(Call call, Response response) throws IOException {
            this.b = true;
            String str = DownloadService.this.getApplication().getExternalCacheDir() + "/dcloud_ad/apk/" + System.currentTimeMillis() + ".apk";
            ResponseBody body = response.body();
            if (body != null) {
                io.dcloud.h.a.e.c.a(body.bytes(), 0, str);
                DownloadService.this.a(str, this.a);
            }
            DownloadService.a(DownloadService.this.getApplicationContext(), this.a, 30);
        }
    }

    private class c extends BroadcastReceiver {
        private AdData a;
        private Application b;
        private long c = System.currentTimeMillis();

        public c(AdData adData, Application application) {
            this.a = adData;
            this.b = application;
        }

        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(intent.getAction(), "android.intent.action.PACKAGE_ADDED")) {
                if (System.currentTimeMillis() - this.c < 60000) {
                    DownloadService.a(DownloadService.this.getApplicationContext(), this.a, 31);
                }
                this.b.unregisterReceiver(this);
            }
        }
    }

    public void onCreate() {
        super.onCreate();
    }

    /* access modifiers changed from: protected */
    public void onHandleWork(Intent intent) {
        synchronized (this.c) {
            AdData adData = (AdData) intent.getParcelableExtra("data");
            for (b next : this.b) {
                if (next.a().equals(adData.k()) && !next.c()) {
                    return;
                }
            }
            b bVar = new b(adData);
            this.b.add(bVar);
            a(getApplicationContext(), adData, 29);
            this.a.newCall(bVar.b()).enqueue(bVar);
            a();
        }
    }

    private void a() {
        File[] listFiles;
        try {
            long currentTimeMillis = System.currentTimeMillis() - 604800000;
            File file = new File(getApplication().getExternalCacheDir() + "/dcloud_ad/apk/");
            if (file.isDirectory() && (listFiles = file.listFiles()) != null && listFiles.length > 0) {
                for (File file2 : listFiles) {
                    if (file2.lastModified() < currentTimeMillis) {
                        file2.delete();
                    }
                }
            }
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: private */
    public void a(String str, AdData adData) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addDataScheme("package");
        getApplication().registerReceiver(new c(adData, getApplication()), intentFilter);
        Intent intent = new Intent();
        intent.addFlags(268435456);
        intent.setAction("android.intent.action.VIEW");
        if (Build.VERSION.SDK_INT >= 24) {
            Application application = getApplication();
            Uri uriForFile = FileProvider.getUriForFile(application, getApplication().getPackageName() + ".dc.fileprovider", new File(str));
            intent.addFlags(1);
            intent.setDataAndType(uriForFile, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(str)), "application/vnd.android.package-archive");
        }
        getApplication().startActivity(intent);
    }

    public static void a(Context context, AdData adData, int i) {
        f.a().a(new a(context, adData, i));
    }
}
