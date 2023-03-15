package com.taobao.weex.layout;

import java.io.Serializable;

public class MeasureSize implements Serializable {
    private float height;
    private float width;

    public float getWidth() {
        return this.width;
    }

    public void setWidth(float f) {
        this.width = f;
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float f) {
        this.height = f;
    }
}
