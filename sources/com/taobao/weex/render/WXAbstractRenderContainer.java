package com.taobao.weex.render;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import com.taobao.weex.WXSDKInstance;
import java.lang.ref.WeakReference;

public class WXAbstractRenderContainer extends FrameLayout {
    protected boolean mHasConsumeEvent = false;
    protected WeakReference<WXSDKInstance> mSDKInstance;

    public void createInstanceRenderView(String str) {
    }

    public WXAbstractRenderContainer(Context context) {
        super(context);
    }

    public WXAbstractRenderContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public WXAbstractRenderContainer(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public WXAbstractRenderContainer(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public void setSDKInstance(WXSDKInstance wXSDKInstance) {
        this.mSDKInstance = new WeakReference<>(wXSDKInstance);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        WXSDKInstance wXSDKInstance;
        super.onSizeChanged(i, i2, i3, i4);
        WeakReference<WXSDKInstance> weakReference = this.mSDKInstance;
        if (weakReference != null && (wXSDKInstance = (WXSDKInstance) weakReference.get()) != null) {
            wXSDKInstance.setSize(i, i2);
        }
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        this.mHasConsumeEvent = true;
        return super.dispatchTouchEvent(motionEvent);
    }

    public boolean hasConsumeEvent() {
        return this.mHasConsumeEvent;
    }
}
