package com.taobao.weex.layout;

import com.taobao.weex.common.Destroyable;
import com.taobao.weex.ui.component.WXComponent;
import java.io.Serializable;

public abstract class ContentBoxMeasurement implements Serializable, Destroyable {
    protected WXComponent mComponent;
    protected boolean mMeasureExactly;
    protected float mMeasureHeight;
    protected float mMeasureWidth;

    public abstract void layoutAfter(float f, float f2);

    public abstract void layoutBefore();

    public abstract void measureInternal(float f, float f2, int i, int i2);

    public ContentBoxMeasurement() {
        this.mMeasureExactly = false;
        this.mComponent = null;
    }

    public ContentBoxMeasurement(WXComponent wXComponent) {
        this.mMeasureExactly = false;
        this.mComponent = wXComponent;
    }

    public final void measure(float f, float f2, int i, int i2) {
        measureInternal(f, f2, i, i2);
    }

    public float getWidth() {
        return this.mMeasureWidth;
    }

    public boolean getMeasureExactly() {
        return this.mMeasureExactly;
    }

    public float getHeight() {
        return this.mMeasureHeight;
    }

    public void destroy() {
        this.mComponent = null;
    }
}
