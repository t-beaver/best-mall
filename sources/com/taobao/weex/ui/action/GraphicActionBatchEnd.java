package com.taobao.weex.ui.action;

import com.taobao.weex.WXSDKInstance;

public class GraphicActionBatchEnd extends BasicGraphicAction {
    public void executeAction() {
    }

    public GraphicActionBatchEnd(WXSDKInstance wXSDKInstance, String str) {
        super(wXSDKInstance, str);
        this.mActionType = 2;
    }
}
