package com.taobao.weex.ui.component;

import android.content.Context;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.view.WXFrameLayout;

@Component(lazyload = false)
public class WXBaseRefresh extends WXVContainer<WXFrameLayout> {
    private WXLoadingIndicator mLoadingIndicator;

    public WXBaseRefresh(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, z, basicComponentData);
    }

    public void addChild(WXComponent wXComponent) {
        super.addChild(wXComponent);
        checkLoadingIndicator(wXComponent);
    }

    /* access modifiers changed from: protected */
    public WXFrameLayout initComponentHostView(Context context) {
        return new WXFrameLayout(context);
    }

    public void addChild(WXComponent wXComponent, int i) {
        super.addChild(wXComponent, i);
        checkLoadingIndicator(wXComponent);
    }

    private void checkLoadingIndicator(WXComponent wXComponent) {
        if (wXComponent instanceof WXLoadingIndicator) {
            this.mLoadingIndicator = (WXLoadingIndicator) wXComponent;
        }
    }
}
