package com.taobao.weex.ui.component;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.view.WXEditText;

@Component(lazyload = false)
public class WXInput extends AbstractEditComponent {
    @Deprecated
    public WXInput(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, String str, boolean z, BasicComponentData basicComponentData) {
        this(wXSDKInstance, wXVContainer, z, basicComponentData);
    }

    public WXInput(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, z, basicComponentData);
    }

    /* access modifiers changed from: protected */
    public void appleStyleAfterCreated(WXEditText wXEditText) {
        super.appleStyleAfterCreated(wXEditText);
        wXEditText.setSingleLine();
    }
}
