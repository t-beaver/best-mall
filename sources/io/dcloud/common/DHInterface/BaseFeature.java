package io.dcloud.common.DHInterface;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.feature.internal.sdk.SDK;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BaseFeature implements IFeature, IBoot, IDPlugin, ISysEventListener, IReflectAble {
    protected Context mApplicationContext = null;
    private Activity mDPluginActivity = null;
    private Context mDPluginContext = null;
    protected AbsMgr mFeatureMgr = null;
    protected String mFeatureName = null;
    protected ArrayList<BaseModule> mModules = null;

    public static abstract class BaseModule {
        public String description = null;
        public String featureName = null;
        public String id = null;
        public Context mApplicationContext;
        public String name = null;

        public String getFullDescription() {
            return this.featureName + this.description;
        }

        public void init(Context context) {
            this.mApplicationContext = context;
        }

        public abstract JSONObject toJSONObject() throws JSONException;
    }

    public void dispose(String str) {
    }

    public boolean doHandleAction(String str) {
        return false;
    }

    public String execute(IWebview iWebview, String str, JSONArray jSONArray) {
        return null;
    }

    public String execute(IWebview iWebview, String str, String[] strArr) {
        return null;
    }

    public BaseModule getBaseModuleById(String str) {
        ArrayList<BaseModule> arrayList = this.mModules;
        if (arrayList == null) {
            return null;
        }
        Iterator<BaseModule> it = arrayList.iterator();
        while (it.hasNext()) {
            BaseModule next = it.next();
            if (next.id.equals(str)) {
                return next;
            }
        }
        return null;
    }

    public Activity getDPluginActivity() {
        return this.mDPluginActivity;
    }

    public Context getDPluginContext() {
        Context context = this.mDPluginContext;
        return context == null ? this.mApplicationContext : context;
    }

    public void init(AbsMgr absMgr, String str) {
        this.mFeatureMgr = absMgr;
        this.mApplicationContext = absMgr.getContext();
        this.mFeatureName = str;
    }

    public void initDPlugin(Context context, Activity activity) {
        this.mDPluginContext = context;
        this.mDPluginActivity = activity;
    }

    public boolean isOldMode() {
        return false;
    }

    public ArrayList<BaseModule> loadModules() {
        ArrayList<BaseModule> arrayList = this.mModules;
        if (arrayList != null) {
            return arrayList;
        }
        this.mModules = new ArrayList<>();
        HashMap hashMap = (HashMap) this.mFeatureMgr.processEvent(IMgr.MgrType.FeatureMgr, 4, this.mFeatureName);
        if (SDK.isUniMPSDK() && hashMap != null && hashMap.containsKey("oauth-igetui")) {
            hashMap.remove("oauth-igetui");
        }
        if (hashMap != null && !hashMap.isEmpty()) {
            for (String str : hashMap.keySet()) {
                try {
                    Object newInstance = Class.forName((String) hashMap.get(str)).newInstance();
                    if (newInstance instanceof BaseModule) {
                        BaseModule baseModule = (BaseModule) newInstance;
                        baseModule.init(this.mApplicationContext);
                        baseModule.name = str;
                        baseModule.featureName = this.mFeatureName;
                        if (baseModule.id == null) {
                            baseModule.id = str;
                        }
                        this.mModules.add(baseModule);
                    }
                } catch (ClassNotFoundException e) {
                    Logger.e(e.getLocalizedMessage());
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return this.mModules;
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
    }

    public boolean onEventExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
        return false;
    }

    public final boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
        if (sysEventType == ISysEventListener.SysEventType.onActivityResult) {
            Object[] objArr = (Object[]) obj;
            onActivityResult(((Integer) objArr[0]).intValue(), ((Integer) objArr[1]).intValue(), (Intent) objArr[2]);
        } else if (sysEventType == ISysEventListener.SysEventType.onStart) {
            Object[] objArr2 = (Object[]) obj;
            onStart((Context) objArr2[0], (Bundle) objArr2[1], (String[]) objArr2[3]);
        } else if (sysEventType == ISysEventListener.SysEventType.onPause) {
            onPause();
        } else if (sysEventType == ISysEventListener.SysEventType.onStop) {
            onStop();
        } else if (sysEventType == ISysEventListener.SysEventType.onResume) {
            onResume();
        } else if (sysEventType == ISysEventListener.SysEventType.onNewIntent) {
            onNewIntent();
        } else if (sysEventType == ISysEventListener.SysEventType.onSaveInstanceState) {
            if (obj instanceof Bundle) {
                onSaveInstanceState((Bundle) obj);
            }
        } else if (sysEventType != ISysEventListener.SysEventType.onRequestPermissionsResult) {
            return onEventExecute(sysEventType, obj);
        } else {
            Object[] objArr3 = (Object[]) obj;
            onRequestPermissionsResult(((Integer) objArr3[0]).intValue(), (String[]) objArr3[1], (int[]) objArr3[2]);
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void onLowMemory() {
    }

    /* access modifiers changed from: protected */
    public void onNewIntent() {
    }

    public void onPause() {
    }

    public void onReceiver(Intent intent) {
    }

    /* access modifiers changed from: protected */
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
    }

    public void onRestart(Context context) {
    }

    public void onResume() {
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
    }

    public void onStart(Context context, Bundle bundle, String[] strArr) {
    }

    public void onStop() {
    }

    public final void registerSysEvent(IWebview iWebview, ISysEventListener.SysEventType sysEventType) {
        registerSysEvent(iWebview.obtainApp(), sysEventType);
    }

    /* access modifiers changed from: protected */
    public JSONArray toModuleJSONArray() throws JSONException {
        JSONArray jSONArray = new JSONArray();
        ArrayList<BaseModule> arrayList = this.mModules;
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                jSONArray.put(this.mModules.get(i).toJSONObject());
            }
        }
        return jSONArray;
    }

    public final void unregisterSysEvent(IWebview iWebview, ISysEventListener.SysEventType sysEventType) {
        unregisterSysEvent(iWebview.obtainApp(), sysEventType);
    }

    public final void registerSysEvent(IApp iApp, ISysEventListener.SysEventType sysEventType) {
        iApp.registerSysEventListener(this, sysEventType);
    }

    public final void unregisterSysEvent(IApp iApp, ISysEventListener.SysEventType sysEventType) {
        iApp.unregisterSysEventListener(this, sysEventType);
    }

    public final void registerSysEvent(IWebview iWebview) {
        registerSysEvent(iWebview, ISysEventListener.SysEventType.AllSystemEvent);
    }

    public final void unregisterSysEvent(IWebview iWebview) {
        unregisterSysEvent(iWebview, ISysEventListener.SysEventType.AllSystemEvent);
    }

    public final void registerSysEvent(IApp iApp) {
        registerSysEvent(iApp, ISysEventListener.SysEventType.AllSystemEvent);
    }

    public final void unregisterSysEvent(IApp iApp) {
        unregisterSysEvent(iApp, ISysEventListener.SysEventType.AllSystemEvent);
    }
}
