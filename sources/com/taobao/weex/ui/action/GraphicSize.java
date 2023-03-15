package com.taobao.weex.ui.action;

public class GraphicSize {
    private float mHeight;
    private float mWidth;

    public GraphicSize(float f, float f2) {
        this.mWidth = f;
        this.mHeight = f2;
    }

    public float getWidth() {
        return this.mWidth;
    }

    public void setWidth(float f) {
        this.mWidth = f;
    }

    public float getHeight() {
        return this.mHeight;
    }

    public void setHeight(float f) {
        this.mHeight = f;
    }

    public void update(float f, float f2) {
        this.mWidth = f;
        this.mHeight = f2;
    }
}
