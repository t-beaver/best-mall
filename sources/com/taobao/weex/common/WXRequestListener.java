package com.taobao.weex.common;

public interface WXRequestListener {
    void onError(int i, Object obj, WXResponse wXResponse);

    void onSuccess(int i, Object obj, WXResponse wXResponse);
}
