package io.dcloud;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import io.dcloud.common.DHInterface.IActivityHandler;
import io.dcloud.common.DHInterface.IReflectAble;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.feature.internal.reflect.BroadcastReceiver;
import io.src.dcloud.adapter.DCloudBaseActivity;
import java.util.HashMap;

abstract class c extends DCloudBaseActivity implements IActivityHandler, IReflectAble {
    private int a = 0;
    private HashMap<String, d> b = new HashMap<>();
    private HashMap<String, d> c = new HashMap<>();

    c() {
    }

    private void b() {
        for (d unregisterReceiver : this.b.values()) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(unregisterReceiver);
        }
        this.b.clear();
        for (d unregisterReceiver2 : this.c.values()) {
            unregisterReceiver(unregisterReceiver2);
        }
        this.c.clear();
    }

    public void callBack(String str, Bundle bundle) {
    }

    public int getActivityState() {
        return this.a;
    }

    public Context getContext() {
        return this.that;
    }

    public String getUrlByFilePath(String str, String str2) {
        return "";
    }

    public boolean isMultiProcessMode() {
        return false;
    }

    public void onAsyncStartAppEnd(String str, Object obj) {
    }

    public Object onAsyncStartAppStart(String str) {
        return null;
    }

    public void onCreate(Bundle bundle) {
        this.a = 1;
        super.onCreate(bundle);
        AndroidResources.initAndroidResources(this.that);
    }

    public void onDestroy() {
        this.a = 0;
        super.onDestroy();
        try {
            b();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        this.a = 2;
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        this.a = 1;
        super.onResume();
    }

    public void registerLocalReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        d dVar = new d(broadcastReceiver, intentFilter);
        try {
            LocalBroadcastManager.getInstance(this).registerReceiver(dVar, intentFilter);
            this.b.put(broadcastReceiver.toString(), dVar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, String str, Handler handler) {
        Intent intent;
        d dVar = new d(broadcastReceiver, intentFilter);
        try {
            intent = registerReceiver(dVar, intentFilter, str, handler);
            try {
                this.c.put(broadcastReceiver.toString(), dVar);
            } catch (Exception e) {
                e = e;
            }
        } catch (Exception e2) {
            e = e2;
            intent = null;
            e.printStackTrace();
            return intent;
        }
        return intent;
    }

    public void sendLocalBroadcast(Intent intent) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public void unregisterReceiver(BroadcastReceiver broadcastReceiver) {
        d remove = this.b.remove(broadcastReceiver.toString());
        if (remove != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(remove);
        }
        d remove2 = this.c.remove(broadcastReceiver.toString());
        if (remove2 != null) {
            unregisterReceiver(remove2);
        }
    }

    public void registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        d dVar = new d(broadcastReceiver, intentFilter);
        try {
            registerReceiver(dVar, intentFilter);
            this.c.put(broadcastReceiver.toString(), dVar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
