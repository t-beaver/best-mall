package com.taobao.weex.appfram.storage;

import android.text.TextUtils;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.appfram.storage.IWXStorageAdapter;
import com.taobao.weex.bridge.JSCallback;
import java.util.Map;

public class WXStorageModule extends WXSDKEngine.DestroyableModule implements IWXStorage {
    IWXStorageAdapter mStorageAdapter;

    private IWXStorageAdapter ability() {
        IWXStorageAdapter iWXStorageAdapter = this.mStorageAdapter;
        if (iWXStorageAdapter != null) {
            return iWXStorageAdapter;
        }
        IWXStorageAdapter iWXStorageAdapter2 = WXSDKEngine.getIWXStorageAdapter();
        this.mStorageAdapter = iWXStorageAdapter2;
        return iWXStorageAdapter2;
    }

    @JSMethod(uiThread = false)
    public void setItem(String str, String str2, final JSCallback jSCallback) {
        if (TextUtils.isEmpty(str) || str2 == null) {
            StorageResultHandler.handleInvalidParam(jSCallback);
            return;
        }
        IWXStorageAdapter ability = ability();
        if (ability == null) {
            StorageResultHandler.handleNoHandlerError(jSCallback);
        } else {
            ability.setItem(str, str2, new IWXStorageAdapter.OnResultReceivedListener() {
                public void onReceived(Map<String, Object> map) {
                    JSCallback jSCallback = jSCallback;
                    if (jSCallback != null) {
                        jSCallback.invoke(map);
                    }
                }
            });
        }
    }

    @JSMethod(uiThread = false)
    public void getItem(String str, final JSCallback jSCallback) {
        if (TextUtils.isEmpty(str)) {
            StorageResultHandler.handleInvalidParam(jSCallback);
            return;
        }
        IWXStorageAdapter ability = ability();
        if (ability == null) {
            StorageResultHandler.handleNoHandlerError(jSCallback);
        } else {
            ability.getItem(str, new IWXStorageAdapter.OnResultReceivedListener() {
                public void onReceived(Map<String, Object> map) {
                    JSCallback jSCallback = jSCallback;
                    if (jSCallback != null) {
                        jSCallback.invoke(map);
                    }
                }
            });
        }
    }

    @JSMethod(uiThread = false)
    public void removeItem(String str, final JSCallback jSCallback) {
        if (TextUtils.isEmpty(str)) {
            StorageResultHandler.handleInvalidParam(jSCallback);
            return;
        }
        IWXStorageAdapter ability = ability();
        if (ability == null) {
            StorageResultHandler.handleNoHandlerError(jSCallback);
        } else {
            ability.removeItem(str, new IWXStorageAdapter.OnResultReceivedListener() {
                public void onReceived(Map<String, Object> map) {
                    JSCallback jSCallback = jSCallback;
                    if (jSCallback != null) {
                        jSCallback.invoke(map);
                    }
                }
            });
        }
    }

    @JSMethod(uiThread = false)
    public void length(final JSCallback jSCallback) {
        IWXStorageAdapter ability = ability();
        if (ability == null) {
            StorageResultHandler.handleNoHandlerError(jSCallback);
        } else {
            ability.length(new IWXStorageAdapter.OnResultReceivedListener() {
                public void onReceived(Map<String, Object> map) {
                    JSCallback jSCallback = jSCallback;
                    if (jSCallback != null) {
                        jSCallback.invoke(map);
                    }
                }
            });
        }
    }

    @JSMethod(uiThread = false)
    public void getAllKeys(final JSCallback jSCallback) {
        IWXStorageAdapter ability = ability();
        if (ability == null) {
            StorageResultHandler.handleNoHandlerError(jSCallback);
        } else {
            ability.getAllKeys(new IWXStorageAdapter.OnResultReceivedListener() {
                public void onReceived(Map<String, Object> map) {
                    JSCallback jSCallback = jSCallback;
                    if (jSCallback != null) {
                        jSCallback.invoke(map);
                    }
                }
            });
        }
    }

    @JSMethod(uiThread = false)
    public void setItemPersistent(String str, String str2, final JSCallback jSCallback) {
        if (TextUtils.isEmpty(str) || str2 == null) {
            StorageResultHandler.handleInvalidParam(jSCallback);
            return;
        }
        IWXStorageAdapter ability = ability();
        if (ability == null) {
            StorageResultHandler.handleNoHandlerError(jSCallback);
        } else {
            ability.setItemPersistent(str, str2, new IWXStorageAdapter.OnResultReceivedListener() {
                public void onReceived(Map<String, Object> map) {
                    JSCallback jSCallback = jSCallback;
                    if (jSCallback != null) {
                        jSCallback.invoke(map);
                    }
                }
            });
        }
    }

    public void destroy() {
        IWXStorageAdapter ability = ability();
        if (ability != null) {
            ability.close();
        }
    }
}
