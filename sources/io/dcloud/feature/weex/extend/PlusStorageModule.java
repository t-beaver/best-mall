package io.dcloud.feature.weex.extend;

import android.content.SharedPreferences;
import android.text.TextUtils;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.appfram.storage.StorageResultHandler;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.feature.weex.WeexInstanceMgr;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlusStorageModule extends WXModule {
    HashMap<String, SharedPreferences> mAllAppNStorages = new HashMap<>(1);

    @JSMethod(uiThread = false)
    public void setItem(String str, String str2, JSCallback jSCallback) {
        Map<String, Object> map;
        if (TextUtils.isEmpty(str) || str2 == null) {
            StorageResultHandler.handleInvalidParam(jSCallback);
            return;
        }
        SharedPreferences appNStorage = getAppNStorage();
        if (appNStorage != null) {
            PlatformUtil.setBundleData(appNStorage, str, str2);
            map = StorageResultHandler.setItemResult(true);
        } else {
            map = StorageResultHandler.setItemResult(false);
        }
        if (jSCallback != null) {
            jSCallback.invoke(map);
        }
    }

    @JSMethod(uiThread = false)
    public void getItem(String str, JSCallback jSCallback) {
        Map<String, Object> map;
        if (TextUtils.isEmpty(str)) {
            StorageResultHandler.handleInvalidParam(jSCallback);
            return;
        }
        SharedPreferences appNStorage = getAppNStorage();
        if (appNStorage != null) {
            map = StorageResultHandler.getItemResult(appNStorage.getString(str, (String) null));
        } else {
            map = StorageResultHandler.getItemResult((String) null);
        }
        if (jSCallback != null) {
            jSCallback.invoke(map);
        }
    }

    @JSMethod(uiThread = false)
    public void removeItem(String str, JSCallback jSCallback) {
        Map<String, Object> map;
        if (TextUtils.isEmpty(str)) {
            StorageResultHandler.handleInvalidParam(jSCallback);
            return;
        }
        SharedPreferences appNStorage = getAppNStorage();
        if (appNStorage != null) {
            PlatformUtil.removeBundleData(appNStorage, str);
            map = StorageResultHandler.removeItemResult(true);
        } else {
            map = StorageResultHandler.removeItemResult(false);
        }
        if (jSCallback != null) {
            jSCallback.invoke(map);
        }
    }

    @JSMethod(uiThread = false)
    public void length(JSCallback jSCallback) {
        SharedPreferences appNStorage = getAppNStorage();
        if (appNStorage != null) {
            Map<String, Object> lengthResult = StorageResultHandler.getLengthResult((long) getBundleDataSize(appNStorage));
            if (jSCallback != null) {
                jSCallback.invoke(lengthResult);
                return;
            }
            return;
        }
        StorageResultHandler.handleNoHandlerError(jSCallback);
    }

    @JSMethod(uiThread = false)
    public void getAllKeys(JSCallback jSCallback) {
        SharedPreferences appNStorage = getAppNStorage();
        if (appNStorage != null) {
            Map<String, Object> allkeysResult = StorageResultHandler.getAllkeysResult(getBundleKeys(appNStorage));
            if (jSCallback != null) {
                jSCallback.invoke(allkeysResult);
                return;
            }
            return;
        }
        StorageResultHandler.handleNoHandlerError(jSCallback);
    }

    private int getBundleDataSize(SharedPreferences sharedPreferences) {
        try {
            return sharedPreferences.getAll().size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private List<String> getBundleKeys(SharedPreferences sharedPreferences) {
        Map<String, ?> all = sharedPreferences.getAll();
        if (all.size() > 0) {
            return new ArrayList(all.keySet());
        }
        return null;
    }

    private SharedPreferences initAppNStorages(String str) {
        return PlatformUtil.getOrCreateBundle(str + "_storages");
    }

    private SharedPreferences getAppNStorage() {
        IWebview findWebview = WeexInstanceMgr.self().findWebview(this.mWXSDKInstance);
        if (findWebview == null) {
            return null;
        }
        String obtainAppId = findWebview.obtainFrameView().obtainApp().obtainAppId();
        SharedPreferences sharedPreferences = this.mAllAppNStorages.get(obtainAppId);
        if (sharedPreferences != null) {
            return sharedPreferences;
        }
        SharedPreferences initAppNStorages = initAppNStorages(obtainAppId);
        this.mAllAppNStorages.put(obtainAppId, initAppNStorages);
        return initAppNStorages;
    }
}
