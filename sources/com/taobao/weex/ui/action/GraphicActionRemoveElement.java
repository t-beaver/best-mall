package com.taobao.weex.ui.action;

import android.text.TextUtils;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXVContainer;

public class GraphicActionRemoveElement extends BasicGraphicAction {
    public GraphicActionRemoveElement(WXSDKInstance wXSDKInstance, String str) {
        super(wXSDKInstance, str);
    }

    public void executeAction() {
        WXComponent wXComponent = WXSDKManager.getInstance().getWXRenderManager().getWXComponent(getPageId(), getRef());
        if (wXComponent != null && wXComponent.getParent() != null && wXComponent.getInstance() != null) {
            clearRegistryForComponent(wXComponent);
            WXVContainer parent = wXComponent.getParent();
            if (wXComponent.getHostView() != null && !TextUtils.equals(wXComponent.getComponentType(), "video") && !TextUtils.equals(wXComponent.getComponentType(), "videoplus")) {
                wXComponent.getHostView().getLocationInWindow(new int[2]);
            }
            parent.remove(wXComponent, true);
        }
    }

    private void clearRegistryForComponent(WXComponent wXComponent) {
        WXComponent unregisterComponent = WXSDKManager.getInstance().getWXRenderManager().unregisterComponent(getPageId(), getRef());
        if (unregisterComponent != null) {
            unregisterComponent.removeAllEvent();
            unregisterComponent.removeStickyStyle();
        }
        if (wXComponent instanceof WXVContainer) {
            WXVContainer wXVContainer = (WXVContainer) wXComponent;
            for (int childCount = wXVContainer.childCount() - 1; childCount >= 0; childCount--) {
                clearRegistryForComponent(wXVContainer.getChild(childCount));
            }
        }
    }
}
