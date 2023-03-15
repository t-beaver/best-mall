package io.dcloud;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.CookieSyncManager;
import android.widget.FrameLayout;
import com.dcloud.android.widget.toast.ToastCompat;
import io.dcloud.application.DCLoudApplicationImpl;
import io.dcloud.common.DHInterface.ICore;
import io.dcloud.common.DHInterface.IOnCreateSplashView;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.adapter.util.AndroidResources;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.core.ui.DCKeyboardManager;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.RuningAcitvityUtil;
import io.dcloud.e.c.d;
import io.dcloud.feature.internal.sdk.SDK;
import java.util.ArrayList;

public class EntryProxy {
    public static boolean a = false;
    private static EntryProxy b;
    private ArrayList<Activity> c = new ArrayList<>(1);
    boolean d = false;
    d e = null;

    private EntryProxy() {
    }

    private void clearData() {
        Logger.d("EntryProxy", " clearData");
        b = null;
        a = false;
        this.d = false;
        AndroidResources.clearData();
        BaseInfo.clearData();
        this.e = null;
    }

    public static EntryProxy getInstnace() {
        return b;
    }

    public static EntryProxy init(Activity activity, ICore.ICoreStatusListener iCoreStatusListener) {
        a = true;
        Context applicationContext = activity.getApplicationContext();
        DCLoudApplicationImpl.self().setContext(applicationContext);
        AndroidResources.initAndroidResources(applicationContext);
        EntryProxy entryProxy = b;
        if (entryProxy != null) {
            entryProxy.e.a().setmCoreListener(iCoreStatusListener);
            if (b.e.b() != applicationContext) {
                b.destroy(activity);
            }
        }
        if (b == null) {
            b = new EntryProxy();
            CookieSyncManager.createInstance(applicationContext);
            b.e = new d(applicationContext, iCoreStatusListener);
        }
        b.c.add(activity);
        return b;
    }

    public void destroy(Activity activity) {
        onStop(activity);
    }

    public boolean didCreate() {
        return this.d;
    }

    public ICore getCoreHandler() {
        d dVar = this.e;
        if (dVar != null) {
            return dVar.a();
        }
        return null;
    }

    public Activity getEntryActivity() {
        if (this.c.size() > 0) {
            return this.c.get(0);
        }
        return null;
    }

    public boolean onActivityExecute(Activity activity, ISysEventListener.SysEventType sysEventType, Object obj) {
        d dVar = this.e;
        if (dVar != null) {
            return dVar.a(activity, sysEventType, obj);
        }
        return false;
    }

    public void onConfigurationChanged(Activity activity, int i) {
        d dVar = this.e;
        if (dVar != null) {
            dVar.a(activity, i);
        }
    }

    @Deprecated
    public boolean onCreate(Bundle bundle, FrameLayout frameLayout, SDK.IntegratedMode integratedMode, IOnCreateSplashView iOnCreateSplashView) {
        return onCreate(bundle, integratedMode, iOnCreateSplashView);
    }

    public void onNewIntent(Activity activity, Intent intent) {
        d dVar = this.e;
        if (dVar != null) {
            dVar.a(activity, intent);
        }
    }

    public void onPause(Activity activity) {
        d dVar = this.e;
        if (dVar != null) {
            dVar.a(activity);
        }
        CookieSyncManager.getInstance().stopSync();
    }

    public void onResume(Activity activity) {
        d dVar = this.e;
        if (dVar != null) {
            dVar.b(activity);
        }
        CookieSyncManager.getInstance().startSync();
    }

    public void onStop(Activity activity) {
        try {
            DCKeyboardManager.getInstance().onStop();
            RuningAcitvityUtil.isRuningActivity = false;
            BaseInfo.isFirstRun = false;
            this.c.remove(activity);
            if (this.c.size() == 0) {
                d dVar = this.e;
                if (dVar == null) {
                    clearData();
                } else if (dVar.c(activity)) {
                    clearData();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Deprecated
    public boolean onCreate(Bundle bundle, SDK.IntegratedMode integratedMode, IOnCreateSplashView iOnCreateSplashView) {
        ArrayList<Activity> arrayList = this.c;
        return onCreate(arrayList.get(arrayList.size() - 1), bundle, integratedMode, iOnCreateSplashView);
    }

    public boolean onCreate(Activity activity, Bundle bundle, SDK.IntegratedMode integratedMode, IOnCreateSplashView iOnCreateSplashView) {
        RuningAcitvityUtil.isRuningActivity = true;
        DCKeyboardManager.getInstance().onCreate(activity);
        AndroidResources.initAndroidResources(activity.getBaseContext());
        this.e.a(activity, bundle, integratedMode, iOnCreateSplashView);
        if (BaseInfo.SyncDebug && !activity.getPackageName().equals(activity.getResources().getString(PdrR.DCLOUD_PACKAGE_NAME_BASE))) {
            ToastCompat.makeText((Context) activity, PdrR.DCLOUD_SYNC_DEBUD_MESSAGE, 0).show();
        }
        return true;
    }

    @Deprecated
    public boolean onCreate(Bundle bundle) {
        return onCreate(bundle, (FrameLayout) null, (SDK.IntegratedMode) null, (IOnCreateSplashView) null);
    }

    public static EntryProxy init(Activity activity) {
        return init(activity, (ICore.ICoreStatusListener) null);
    }
}
