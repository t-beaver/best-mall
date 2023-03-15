package com.taobao.weex;

import android.content.Context;
import android.util.AttributeSet;
import com.taobao.weex.WeexFrameRateControl;
import com.taobao.weex.render.WXAbstractRenderContainer;

public class RenderContainer extends WXAbstractRenderContainer implements WeexFrameRateControl.VSyncListener {
    private WeexFrameRateControl mFrameRateControl = new WeexFrameRateControl(this);

    public RenderContainer(Context context) {
        super(context);
    }

    public RenderContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public RenderContainer(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public RenderContainer(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        WeexFrameRateControl weexFrameRateControl = this.mFrameRateControl;
        if (weexFrameRateControl != null) {
            weexFrameRateControl.start();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        WeexFrameRateControl weexFrameRateControl = this.mFrameRateControl;
        if (weexFrameRateControl != null) {
            weexFrameRateControl.stop();
        }
    }

    public void dispatchWindowVisibilityChanged(int i) {
        WeexFrameRateControl weexFrameRateControl;
        super.dispatchWindowVisibilityChanged(i);
        if (i == 8) {
            WeexFrameRateControl weexFrameRateControl2 = this.mFrameRateControl;
            if (weexFrameRateControl2 != null) {
                weexFrameRateControl2.stop();
            }
        } else if (i == 0 && (weexFrameRateControl = this.mFrameRateControl) != null) {
            weexFrameRateControl.start();
        }
    }

    public void OnVSync() {
        if (this.mSDKInstance != null && this.mSDKInstance.get() != null) {
            ((WXSDKInstance) this.mSDKInstance.get()).OnVSync();
        }
    }
}
