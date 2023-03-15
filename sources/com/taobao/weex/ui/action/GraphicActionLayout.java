package com.taobao.weex.ui.action;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.ui.component.WXComponent;

public class GraphicActionLayout extends BasicGraphicAction {
    private final boolean mIsLayoutRTL;
    private final GraphicPosition mLayoutPosition;
    private final GraphicSize mLayoutSize;

    public GraphicActionLayout(WXSDKInstance wXSDKInstance, String str, GraphicPosition graphicPosition, GraphicSize graphicSize, boolean z) {
        super(wXSDKInstance, str);
        this.mLayoutPosition = graphicPosition;
        this.mLayoutSize = graphicSize;
        this.mIsLayoutRTL = z;
    }

    public void executeAction() {
        WXComponent wXComponent = WXSDKManager.getInstance().getWXRenderManager().getWXComponent(getPageId(), getRef());
        if (wXComponent != null) {
            wXComponent.setIsLayoutRTL(this.mIsLayoutRTL);
            wXComponent.setDemission(this.mLayoutSize, this.mLayoutPosition);
            wXComponent.setSafeLayout(wXComponent);
            wXComponent.setPadding(wXComponent.getPadding(), wXComponent.getBorder());
        }
    }
}
