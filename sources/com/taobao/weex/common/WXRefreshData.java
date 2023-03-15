package com.taobao.weex.common;

public class WXRefreshData {
    public String data;
    public boolean isDirty;

    public WXRefreshData(String str, boolean z) {
        this.data = str;
        this.isDirty = z;
    }
}
