package com.taobao.weex.appfram.storage;

import com.taobao.weex.bridge.JSCallback;

interface IWXStorage {
    void getAllKeys(JSCallback jSCallback);

    void getItem(String str, JSCallback jSCallback);

    void length(JSCallback jSCallback);

    void removeItem(String str, JSCallback jSCallback);

    void setItem(String str, String str2, JSCallback jSCallback);

    void setItemPersistent(String str, String str2, JSCallback jSCallback);
}
