package com.taobao.weex.appfram.clipboard;

import com.taobao.weex.bridge.JSCallback;

interface IWXClipboard {
    void getString(JSCallback jSCallback);

    void setString(String str);
}
