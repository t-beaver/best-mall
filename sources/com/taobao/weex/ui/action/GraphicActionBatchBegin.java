package com.taobao.weex.ui.action;

import com.taobao.weex.WXSDKInstance;

public class GraphicActionBatchBegin extends BasicGraphicAction {
    public void executeAction() {
    }

    public GraphicActionBatchBegin(WXSDKInstance wXSDKInstance, String str) {
        super(wXSDKInstance, str);
        this.mActionType = 1;
    }
}
