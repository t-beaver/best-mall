package com.taobao.weex.ui.action;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.component.WXComponent;

public class GraphicActionRenderSuccess extends BasicGraphicAction {
    public GraphicActionRenderSuccess(WXSDKInstance wXSDKInstance) {
        super(wXSDKInstance, "");
    }

    public void executeAction() {
        int i;
        WXSDKInstance wXSDKIntance = getWXSDKIntance();
        if (wXSDKIntance != null && wXSDKIntance.getContext() != null) {
            WXComponent rootComponent = wXSDKIntance.getRootComponent();
            int i2 = 0;
            if (rootComponent != null) {
                i2 = (int) rootComponent.getLayoutWidth();
                i = (int) rootComponent.getLayoutHeight();
            } else {
                i = 0;
            }
            wXSDKIntance.onRenderSuccess(i2, i);
        }
    }
}
