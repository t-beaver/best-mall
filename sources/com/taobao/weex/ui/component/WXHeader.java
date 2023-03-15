package com.taobao.weex.ui.component;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.list.WXCell;

@Component(lazyload = false)
public class WXHeader extends WXCell {
    public boolean isLazy() {
        return false;
    }

    @Deprecated
    public WXHeader(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, String str, boolean z, BasicComponentData basicComponentData) {
        this(wXSDKInstance, wXVContainer, z, basicComponentData);
    }

    public WXHeader(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, z, basicComponentData);
        String componentType = wXVContainer.getComponentType();
        if (WXBasicComponentType.LIST.equals(componentType) || WXBasicComponentType.RECYCLE_LIST.equals(componentType)) {
            getStyles().put("position", (Object) "sticky");
            setSticky("sticky");
        }
    }

    public boolean canRecycled() {
        return !isSticky();
    }
}
