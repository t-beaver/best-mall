package com.taobao.weex.ui.view.border;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;

abstract class BorderCorner {
    static final float SWEEP_ANGLE = 45.0f;
    private boolean hasInnerCorner = false;
    private boolean hasOuterCorner = false;
    protected float mAngleBisector;
    private RectF mBorderBox;
    private float mCornerRadius = 0.0f;
    private float mOvalBottom;
    private float mOvalLeft;
    private float mOvalRight;
    private float mOvalTop;
    private float mPostBorderWidth = 0.0f;
    private float mPreBorderWidth = 0.0f;
    private float mRoundCornerEndX;
    private float mRoundCornerEndY;
    private float mRoundCornerStartX;
    private float mRoundCornerStartY;

    /* access modifiers changed from: protected */
    public abstract void prepareOval();

    /* access modifiers changed from: protected */
    public abstract void prepareRoundCorner();

    BorderCorner() {
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0022, code lost:
        r0 = r3.mBorderBox;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void set(float r4, float r5, float r6, android.graphics.RectF r7, float r8) {
        /*
            r3 = this;
            float r0 = r3.mCornerRadius
            boolean r0 = com.taobao.weex.base.FloatUtil.floatsEqual(r0, r4)
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L_0x002f
            float r0 = r3.mPreBorderWidth
            boolean r0 = com.taobao.weex.base.FloatUtil.floatsEqual(r0, r5)
            if (r0 == 0) goto L_0x002f
            float r0 = r3.mPostBorderWidth
            boolean r0 = com.taobao.weex.base.FloatUtil.floatsEqual(r0, r6)
            if (r0 == 0) goto L_0x002f
            float r0 = r3.mAngleBisector
            boolean r0 = com.taobao.weex.base.FloatUtil.floatsEqual(r0, r8)
            if (r0 == 0) goto L_0x002f
            android.graphics.RectF r0 = r3.mBorderBox
            if (r0 == 0) goto L_0x002d
            boolean r0 = r0.equals(r7)
            if (r0 == 0) goto L_0x002d
            goto L_0x002f
        L_0x002d:
            r0 = 0
            goto L_0x0030
        L_0x002f:
            r0 = 1
        L_0x0030:
            if (r0 == 0) goto L_0x0083
            r3.mCornerRadius = r4
            r3.mPreBorderWidth = r5
            r3.mPostBorderWidth = r6
            r3.mBorderBox = r7
            r3.mAngleBisector = r8
            r5 = 0
            int r6 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1))
            if (r6 <= 0) goto L_0x0049
            boolean r4 = com.taobao.weex.base.FloatUtil.floatsEqual(r5, r4)
            if (r4 != 0) goto L_0x0049
            r4 = 1
            goto L_0x004a
        L_0x0049:
            r4 = 0
        L_0x004a:
            r3.hasOuterCorner = r4
            if (r4 == 0) goto L_0x0077
            float r4 = r3.getPreBorderWidth()
            int r4 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1))
            if (r4 < 0) goto L_0x0077
            float r4 = r3.getPostBorderWidth()
            int r4 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1))
            if (r4 < 0) goto L_0x0077
            float r4 = r3.getOuterCornerRadius()
            float r5 = r3.getPreBorderWidth()
            int r4 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1))
            if (r4 <= 0) goto L_0x0077
            float r4 = r3.getOuterCornerRadius()
            float r5 = r3.getPostBorderWidth()
            int r4 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1))
            if (r4 <= 0) goto L_0x0077
            r1 = 1
        L_0x0077:
            r3.hasInnerCorner = r1
            boolean r4 = r3.hasOuterCorner
            if (r4 == 0) goto L_0x0080
            r3.prepareOval()
        L_0x0080:
            r3.prepareRoundCorner()
        L_0x0083:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.view.border.BorderCorner.set(float, float, float, android.graphics.RectF, float):void");
    }

    public final void drawRoundedCorner(Canvas canvas, Paint paint, float f) {
        if (hasOuterCorner()) {
            Paint paint2 = new Paint(paint);
            float abs = Math.abs(this.mOvalLeft - this.mOvalRight);
            if (paint.getStrokeWidth() > abs) {
                paint2.setStrokeWidth(abs);
            }
            paint2.setStrokeCap(Paint.Cap.ROUND);
            if (Build.VERSION.SDK_INT >= 21) {
                canvas.drawArc(this.mOvalLeft, this.mOvalTop, this.mOvalRight, this.mOvalBottom, f, SWEEP_ANGLE, false, paint2);
                return;
            }
            canvas.drawArc(new RectF(this.mOvalLeft, this.mOvalTop, this.mOvalRight, this.mOvalBottom), f, SWEEP_ANGLE, false, paint2);
        } else if (getRoundCornerStartX() != getRoundCornerEndX() || getRoundCornerStartY() != getRoundCornerEndY()) {
            canvas.drawLine(getRoundCornerStartX(), getRoundCornerStartY(), getRoundCornerEndX(), getRoundCornerEndY(), paint);
        }
    }

    public final float getRoundCornerStartX() {
        return this.mRoundCornerStartX;
    }

    /* access modifiers changed from: package-private */
    public final void setRoundCornerStartX(float f) {
        this.mRoundCornerStartX = f;
    }

    public final float getRoundCornerStartY() {
        return this.mRoundCornerStartY;
    }

    /* access modifiers changed from: package-private */
    public final void setRoundCornerStartY(float f) {
        this.mRoundCornerStartY = f;
    }

    public final float getRoundCornerEndX() {
        return this.mRoundCornerEndX;
    }

    /* access modifiers changed from: package-private */
    public final void setRoundCornerEndX(float f) {
        this.mRoundCornerEndX = f;
    }

    public final float getRoundCornerEndY() {
        return this.mRoundCornerEndY;
    }

    /* access modifiers changed from: package-private */
    public final void setRoundCornerEndY(float f) {
        this.mRoundCornerEndY = f;
    }

    /* access modifiers changed from: package-private */
    public final void setOvalLeft(float f) {
        this.mOvalLeft = f;
    }

    /* access modifiers changed from: package-private */
    public final void setOvalTop(float f) {
        this.mOvalTop = f;
    }

    /* access modifiers changed from: package-private */
    public final void setOvalRight(float f) {
        this.mOvalRight = f;
    }

    /* access modifiers changed from: package-private */
    public final void setOvalBottom(float f) {
        this.mOvalBottom = f;
    }

    /* access modifiers changed from: package-private */
    public boolean hasInnerCorner() {
        return this.hasInnerCorner;
    }

    /* access modifiers changed from: package-private */
    public boolean hasOuterCorner() {
        return this.hasOuterCorner;
    }

    /* access modifiers changed from: protected */
    public final float getPreBorderWidth() {
        return this.mPreBorderWidth;
    }

    /* access modifiers changed from: protected */
    public final float getPostBorderWidth() {
        return this.mPostBorderWidth;
    }

    /* access modifiers changed from: protected */
    public final float getOuterCornerRadius() {
        return this.mCornerRadius;
    }

    /* access modifiers changed from: protected */
    public final float getAngleBisectorDegree() {
        return this.mAngleBisector;
    }

    /* access modifiers changed from: protected */
    public final RectF getBorderBox() {
        return this.mBorderBox;
    }
}
