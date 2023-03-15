package com.taobao.weex.ui.action;

public class GraphicPosition {
    private float mBottom;
    private float mLeft;
    private float mRight;
    private float mTop;

    public GraphicPosition(float f, float f2, float f3, float f4) {
        this.mLeft = f;
        this.mTop = f2;
        this.mRight = f3;
        this.mBottom = f4;
    }

    public float getLeft() {
        return this.mLeft;
    }

    public void setLeft(float f) {
        this.mLeft = f;
    }

    public float getTop() {
        return this.mTop;
    }

    public void setTop(float f) {
        this.mTop = f;
    }

    public float getRight() {
        return this.mRight;
    }

    public void setRight(float f) {
        this.mRight = f;
    }

    public float getBottom() {
        return this.mBottom;
    }

    public void setBottom(float f) {
        this.mBottom = f;
    }

    public void update(float f, float f2, float f3, float f4) {
        this.mTop = f;
        this.mBottom = f2;
        this.mLeft = f3;
        this.mRight = f4;
    }
}
