package io.dcloud.feature.weex.adapter;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXVContainer;

public class FrescoImageComponentU extends FrescoImageComponent {
    public FrescoImageComponentU(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
        this.mIsUni = true;
    }
}
