package com.taobao.weex.ui.component.list;

import com.taobao.weex.common.IWXObject;

public class ListStanceCell implements IWXObject {
    private String backgroundColor = "";

    public ListStanceCell(String str) {
        this.backgroundColor = str;
    }

    public String getBackgroundColor() {
        return this.backgroundColor;
    }
}
