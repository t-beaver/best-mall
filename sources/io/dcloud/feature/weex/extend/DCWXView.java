package io.dcloud.feature.weex.extend;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.ComponentCreator;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXDiv;
import com.taobao.weex.ui.component.WXVContainer;

public class DCWXView extends WXDiv {
    public DCWXView(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, String str, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, str, z, basicComponentData);
    }

    public DCWXView(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    public static class Ceator implements ComponentCreator {
        public WXComponent createInstance(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
            return new DCWXView(wXSDKInstance, wXVContainer, basicComponentData);
        }
    }
}
