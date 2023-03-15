package io.dcloud.feature.weex.adapter;

import android.content.Context;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.ComponentCreator;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXDiv;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.view.WXFrameLayout;

public class ScalableViewComponent extends WXDiv {
    private boolean isScalable = false;

    public ScalableViewComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    /* access modifiers changed from: protected */
    public WXFrameLayout initComponentHostView(Context context) {
        ScalableView scalableView = new ScalableView(context);
        scalableView.holdComponent((WXDiv) this);
        return scalableView;
    }

    public boolean isScalable() {
        return this.isScalable;
    }

    public void setScalable(boolean z) {
        this.isScalable = z;
    }

    public static class Ceator implements ComponentCreator {
        public WXComponent createInstance(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
            return new ScalableViewComponent(wXSDKInstance, wXVContainer, basicComponentData);
        }
    }

    /* access modifiers changed from: protected */
    public void setHostLayoutParams(WXFrameLayout wXFrameLayout, int i, int i2, int i3, int i4, int i5, int i6) {
        if (isScalable()) {
            super.setHostLayoutParams(wXFrameLayout, -1, -1, i3, i4, i5, i6);
        } else {
            super.setHostLayoutParams(wXFrameLayout, i, i2, i3, i4, i5, i6);
        }
    }
}
