package com.taobao.weex.ui.view.border;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.SparseIntArray;
import com.taobao.weex.dom.CSSShorthand;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXViewUtils;
import io.dcloud.feature.uniapp.dom.AbsCSSShorthand;
import io.dcloud.feature.uniapp.ui.shadow.UniBoxShadowData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class BorderDrawable extends Drawable {
    public static final int BORDER_BOTTOM_LEFT_RADIUS = 3;
    public static final int BORDER_BOTTOM_RIGHT_RADIUS = 2;
    public static final int BORDER_RADIUS_ALL = 5;
    public static final int BORDER_TOP_LEFT_RADIUS = 0;
    public static final int BORDER_TOP_RIGHT_RADIUS = 1;
    static final int DEFAULT_BORDER_COLOR = -16777216;
    private static final BorderStyle DEFAULT_BORDER_STYLE = BorderStyle.SOLID;
    static final float DEFAULT_BORDER_WIDTH = 0.0f;
    private static final String TAG = "Border";
    private static BorderStyle[] sBorderStyle = BorderStyle.values();
    private int mAlpha = 255;
    private SparseIntArray mBorderColor;
    private final BorderEdge mBorderEdge = new BorderEdge();
    private CSSShorthand<CSSShorthand.CORNER> mBorderRadius;
    private SparseIntArray mBorderStyle;
    private CSSShorthand<CSSShorthand.EDGE> mBorderWidth;
    private BottomLeftCorner mBottomLeftCorner;
    private BottomRightCorner mBottomRightCorner;
    UniBoxShadowData mBoxShadowData = null;
    private int mColor = 0;
    private boolean mNeedUpdatePath = false;
    private CSSShorthand<CSSShorthand.CORNER> mOverlappingBorderRadius;
    private final Paint mPaint = new Paint(1);
    private Path mPathForBorderOutline;
    private RectF mRectBounds;
    private Shader mShader = null;
    private TopLeftCorner mTopLeftCorner;
    private TopRightCorner mTopRightCorner;

    public void setColorFilter(ColorFilter colorFilter) {
    }

    public void draw(Canvas canvas) {
        canvas.save();
        updateBorderOutline();
        this.mPaint.setAlpha(255);
        if (this.mPathForBorderOutline != null) {
            int multiplyColorAlpha = WXViewUtils.multiplyColorAlpha(this.mColor, this.mAlpha);
            Shader shader = this.mShader;
            if (shader != null) {
                this.mPaint.setShader(shader);
                this.mPaint.setStyle(Paint.Style.FILL);
                canvas.drawPath(this.mPathForBorderOutline, this.mPaint);
                this.mPaint.setShader((Shader) null);
            } else if ((multiplyColorAlpha >>> 24) != 0) {
                this.mPaint.setColor(multiplyColorAlpha);
                this.mPaint.setStyle(Paint.Style.FILL);
                canvas.drawPath(this.mPathForBorderOutline, this.mPaint);
                this.mPaint.setShader((Shader) null);
            }
        }
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeJoin(Paint.Join.ROUND);
        drawBorders(canvas);
        this.mPaint.setShader((Shader) null);
        canvas.restore();
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        this.mNeedUpdatePath = true;
    }

    public void setAlpha(int i) {
        if (i != this.mAlpha) {
            this.mAlpha = i;
            invalidateSelf();
        }
    }

    public int getAlpha() {
        return this.mAlpha;
    }

    public void updateBoxShadowData(UniBoxShadowData uniBoxShadowData) {
        this.mBoxShadowData = uniBoxShadowData;
        invalidateSelf();
    }

    public int getOpacity() {
        if (this.mShader != null) {
            return -1;
        }
        return WXViewUtils.getOpacityFromColor(WXViewUtils.multiplyColorAlpha(this.mColor, this.mAlpha));
    }

    public void getOutline(Outline outline) {
        if (Build.VERSION.SDK_INT >= 21) {
            if (this.mPathForBorderOutline == null) {
                this.mNeedUpdatePath = true;
            }
            updateBorderOutline();
            outline.setConvexPath(this.mPathForBorderOutline);
        }
    }

    public void setBorderWidth(CSSShorthand.EDGE edge, float f) {
        if (this.mBorderWidth == null) {
            this.mBorderWidth = new CSSShorthand<>();
        }
        if (this.mBorderWidth.get((Enum<? extends AbsCSSShorthand.CSSProperty>) edge) != f) {
            this.mBorderWidth.set((Enum<? extends AbsCSSShorthand.CSSProperty>) edge, f);
            this.mNeedUpdatePath = true;
            invalidateSelf();
        }
    }

    /* access modifiers changed from: package-private */
    public float getBorderWidth(CSSShorthand.EDGE edge) {
        return this.mBorderWidth.get((Enum<? extends AbsCSSShorthand.CSSProperty>) edge);
    }

    public void setBorderRadius(CSSShorthand.CORNER corner, float f) {
        if (this.mBorderRadius == null) {
            this.mBorderRadius = new CSSShorthand<>();
        }
        if (this.mBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) corner) == f) {
            if (corner != CSSShorthand.CORNER.ALL) {
                return;
            }
            if (f == this.mBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_TOP_LEFT) && f == this.mBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_TOP_RIGHT) && f == this.mBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_BOTTOM_RIGHT) && f == this.mBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_BOTTOM_LEFT)) {
                return;
            }
        }
        this.mBorderRadius.set((Enum<? extends AbsCSSShorthand.CSSProperty>) corner, f);
        this.mNeedUpdatePath = true;
        invalidateSelf();
    }

    public float[] getBorderRadius(RectF rectF) {
        prepareBorderRadius(rectF);
        if (this.mOverlappingBorderRadius == null) {
            this.mOverlappingBorderRadius = new CSSShorthand<>();
        }
        float f = this.mOverlappingBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_TOP_LEFT);
        float f2 = this.mOverlappingBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_TOP_RIGHT);
        float f3 = this.mOverlappingBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_BOTTOM_RIGHT);
        float f4 = this.mOverlappingBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_BOTTOM_LEFT);
        return new float[]{f, f, f2, f2, f3, f3, f4, f4};
    }

    public float[] getBorderInnerRadius(RectF rectF) {
        prepareBorderRadius(rectF);
        if (this.mOverlappingBorderRadius == null) {
            this.mOverlappingBorderRadius = new CSSShorthand<>();
        }
        float f = this.mOverlappingBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_TOP_LEFT);
        float f2 = this.mOverlappingBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_TOP_RIGHT);
        float f3 = this.mOverlappingBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_BOTTOM_RIGHT);
        float f4 = this.mOverlappingBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_BOTTOM_LEFT);
        CSSShorthand<CSSShorthand.EDGE> cSSShorthand = this.mBorderWidth;
        if (cSSShorthand != null) {
            f = Math.max(f - cSSShorthand.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.TOP), 0.0f);
            f2 = Math.max(f2 - this.mBorderWidth.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.TOP), 0.0f);
            f3 = Math.max(f3 - this.mBorderWidth.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.BOTTOM), 0.0f);
            f4 = Math.max(f4 - this.mBorderWidth.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.BOTTOM), 0.0f);
        }
        return new float[]{f, f, f2, f2, f3, f3, f4, f4};
    }

    public void setBorderColor(CSSShorthand.EDGE edge, int i) {
        if (this.mBorderColor == null) {
            SparseIntArray sparseIntArray = new SparseIntArray(5);
            this.mBorderColor = sparseIntArray;
            sparseIntArray.put(CSSShorthand.EDGE.ALL.ordinal(), -16777216);
        }
        if (getBorderColor(edge) != i) {
            BorderUtil.updateSparseArray(this.mBorderColor, edge.ordinal(), i);
            invalidateSelf();
        }
    }

    /* access modifiers changed from: package-private */
    public int getBorderColor(CSSShorthand.EDGE edge) {
        return BorderUtil.fetchFromSparseArray(this.mBorderColor, edge.ordinal(), -16777216);
    }

    public void setBorderStyle(CSSShorthand.EDGE edge, String str) {
        if (this.mBorderStyle == null) {
            SparseIntArray sparseIntArray = new SparseIntArray(5);
            this.mBorderStyle = sparseIntArray;
            sparseIntArray.put(CSSShorthand.EDGE.ALL.ordinal(), DEFAULT_BORDER_STYLE.ordinal());
        }
        try {
            int ordinal = BorderStyle.valueOf(str.toUpperCase(Locale.US)).ordinal();
            if (getBorderStyle(edge) != ordinal) {
                BorderUtil.updateSparseArray(this.mBorderStyle, edge.ordinal(), ordinal);
                invalidateSelf();
            }
        } catch (IllegalArgumentException e) {
            WXLogUtils.e(TAG, WXLogUtils.getStackTrace(e));
        }
    }

    /* access modifiers changed from: package-private */
    public int getBorderStyle(CSSShorthand.EDGE edge) {
        return BorderUtil.fetchFromSparseArray(this.mBorderStyle, edge.ordinal(), BorderStyle.SOLID.ordinal());
    }

    public int getColor() {
        return this.mColor;
    }

    public void setColor(int i) {
        this.mColor = i;
        invalidateSelf();
    }

    public void setImage(Shader shader) {
        this.mShader = shader;
        invalidateSelf();
    }

    public boolean hasImage() {
        return this.mShader != null;
    }

    public boolean isRounded() {
        CSSShorthand<CSSShorthand.CORNER> cSSShorthand = this.mBorderRadius;
        return (cSSShorthand == null || (cSSShorthand.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_TOP_LEFT) == 0.0f && this.mBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_TOP_RIGHT) == 0.0f && this.mBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_BOTTOM_RIGHT) == 0.0f && this.mBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_BOTTOM_LEFT) == 0.0f)) ? false : true;
    }

    public Path getContentPath(RectF rectF) {
        Path path = new Path();
        prepareBorderPath(0, 0, 0, 0, rectF, path);
        return path;
    }

    private void updateBorderOutline() {
        if (this.mNeedUpdatePath) {
            this.mNeedUpdatePath = false;
            if (this.mPathForBorderOutline == null) {
                this.mPathForBorderOutline = new Path();
            }
            this.mPathForBorderOutline.reset();
            prepareBorderPath(0, 0, 0, 0, new RectF(getBounds()), this.mPathForBorderOutline);
        }
    }

    private void prepareBorderPath(int i, int i2, int i3, int i4, RectF rectF, Path path) {
        UniBoxShadowData uniBoxShadowData = this.mBoxShadowData;
        if (uniBoxShadowData != null) {
            rectF.inset((float) (this.mBoxShadowData.getNormalLeft() > 0 ? this.mBoxShadowData.getNormalLeft() / 2 : 0), (float) (uniBoxShadowData.getNormalTop() > 0 ? this.mBoxShadowData.getNormalTop() / 2 : 0));
        }
        if (this.mBorderRadius != null) {
            prepareBorderRadius(rectF);
            if (this.mOverlappingBorderRadius == null) {
                this.mOverlappingBorderRadius = new CSSShorthand<>();
            }
            float f = this.mOverlappingBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_TOP_LEFT);
            float f2 = this.mOverlappingBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_TOP_RIGHT);
            float f3 = this.mOverlappingBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_BOTTOM_RIGHT);
            float f4 = this.mOverlappingBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_BOTTOM_LEFT);
            float f5 = (float) i4;
            float f6 = (float) i;
            float f7 = (float) i2;
            float f8 = (float) i3;
            path.addRoundRect(rectF, new float[]{f - f5, f - f6, f2 - f7, f2 - f6, f3 - f7, f3 - f8, f4 - f5, f4 - f8}, Path.Direction.CW);
            return;
        }
        path.addRect(rectF, Path.Direction.CW);
    }

    private void prepareBorderRadius(RectF rectF) {
        if (this.mBorderRadius != null) {
            float scaleFactor = getScaleFactor(rectF);
            if (this.mOverlappingBorderRadius == null) {
                this.mOverlappingBorderRadius = new CSSShorthand<>();
            }
            if (Float.isNaN(scaleFactor) || scaleFactor >= 1.0f) {
                this.mOverlappingBorderRadius.set((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_TOP_LEFT, this.mBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_TOP_LEFT));
                this.mOverlappingBorderRadius.set((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_TOP_RIGHT, this.mBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_TOP_RIGHT));
                this.mOverlappingBorderRadius.set((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_BOTTOM_RIGHT, this.mBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_BOTTOM_RIGHT));
                this.mOverlappingBorderRadius.set((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_BOTTOM_LEFT, this.mBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_BOTTOM_LEFT));
                return;
            }
            this.mOverlappingBorderRadius.set((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_TOP_LEFT, this.mBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_TOP_LEFT) * scaleFactor);
            this.mOverlappingBorderRadius.set((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_TOP_RIGHT, this.mBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_TOP_RIGHT) * scaleFactor);
            this.mOverlappingBorderRadius.set((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_BOTTOM_RIGHT, this.mBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_BOTTOM_RIGHT) * scaleFactor);
            this.mOverlappingBorderRadius.set((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_BOTTOM_LEFT, this.mBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_BOTTOM_LEFT) * scaleFactor);
        }
    }

    private float getScaleFactor(RectF rectF) {
        float f = this.mBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_TOP_LEFT) + this.mBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_TOP_RIGHT);
        float f2 = this.mBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_TOP_RIGHT) + this.mBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_BOTTOM_RIGHT);
        float f3 = this.mBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_BOTTOM_RIGHT) + this.mBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_BOTTOM_LEFT);
        float f4 = this.mBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_BOTTOM_LEFT) + this.mBorderRadius.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.CORNER.BORDER_TOP_LEFT);
        ArrayList arrayList = new ArrayList(4);
        updateFactor(arrayList, rectF.width(), f);
        updateFactor(arrayList, rectF.height(), f2);
        updateFactor(arrayList, rectF.width(), f3);
        updateFactor(arrayList, rectF.height(), f4);
        if (arrayList.isEmpty()) {
            return Float.NaN;
        }
        return ((Float) Collections.min(arrayList)).floatValue();
    }

    private void updateFactor(List<Float> list, float f, float f2) {
        if (f2 != 0.0f) {
            list.add(Float.valueOf(f / f2));
        }
    }

    private void drawBorders(Canvas canvas) {
        int i;
        RectF rectF = this.mRectBounds;
        if (rectF == null) {
            this.mRectBounds = new RectF(getBounds());
        } else {
            rectF.set(getBounds());
        }
        UniBoxShadowData uniBoxShadowData = this.mBoxShadowData;
        int i2 = 0;
        if (uniBoxShadowData != null) {
            i = uniBoxShadowData.getNormalTop() > 0 ? this.mBoxShadowData.getNormalTop() / 2 : 0;
            if (this.mBoxShadowData.getNormalLeft() > 0) {
                i2 = this.mBoxShadowData.getNormalLeft() / 2;
            }
            this.mRectBounds.inset((float) i2, (float) i);
        } else {
            i = 0;
        }
        CSSShorthand<CSSShorthand.EDGE> cSSShorthand = this.mBorderWidth;
        if (cSSShorthand != null) {
            float f = cSSShorthand.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.LEFT);
            float f2 = this.mBorderWidth.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.TOP);
            float f3 = this.mBorderWidth.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.BOTTOM);
            float f4 = this.mBorderWidth.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.RIGHT);
            if (this.mTopLeftCorner == null) {
                this.mTopLeftCorner = new TopLeftCorner();
            }
            this.mTopLeftCorner.set(getBorderRadius(CSSShorthand.CORNER.BORDER_TOP_LEFT), f, f2, this.mRectBounds);
            if (this.mTopRightCorner == null) {
                this.mTopRightCorner = new TopRightCorner();
            }
            this.mTopRightCorner.set(getBorderRadius(CSSShorthand.CORNER.BORDER_TOP_RIGHT), f2, f4, this.mRectBounds);
            if (this.mBottomRightCorner == null) {
                this.mBottomRightCorner = new BottomRightCorner();
            }
            this.mBottomRightCorner.set(getBorderRadius(CSSShorthand.CORNER.BORDER_BOTTOM_RIGHT), f4, f3, this.mRectBounds);
            if (this.mBottomLeftCorner == null) {
                this.mBottomLeftCorner = new BottomLeftCorner();
            }
            this.mBottomLeftCorner.set(getBorderRadius(CSSShorthand.CORNER.BORDER_BOTTOM_LEFT), f3, f, this.mRectBounds);
            if (!isDefAllDrawRoundRect(canvas, f, f2, f3, f4)) {
                canvas.translate((float) i2, (float) i);
                drawOneSide(canvas, this.mBorderEdge.set(this.mTopLeftCorner, this.mTopRightCorner, f2, CSSShorthand.EDGE.TOP));
                drawOneSide(canvas, this.mBorderEdge.set(this.mTopRightCorner, this.mBottomRightCorner, f4, CSSShorthand.EDGE.RIGHT));
                drawOneSide(canvas, this.mBorderEdge.set(this.mBottomRightCorner, this.mBottomLeftCorner, f3, CSSShorthand.EDGE.BOTTOM));
                drawOneSide(canvas, this.mBorderEdge.set(this.mBottomLeftCorner, this.mTopLeftCorner, f, CSSShorthand.EDGE.LEFT));
            }
        }
    }

    private boolean isDefAllDrawRoundRect(Canvas canvas, float f, float f2, float f3, float f4) {
        float outerCornerRadius = this.mTopLeftCorner.getOuterCornerRadius();
        if (outerCornerRadius <= 0.0f || outerCornerRadius != this.mTopRightCorner.getOuterCornerRadius() || outerCornerRadius != this.mBottomLeftCorner.getOuterCornerRadius() || outerCornerRadius != this.mBottomRightCorner.getOuterCornerRadius() || f <= 0.0f || f != f2 || f != f3 || f != f4) {
            return false;
        }
        int multiplyColorAlpha = WXViewUtils.multiplyColorAlpha(getBorderColor(CSSShorthand.EDGE.LEFT), this.mAlpha);
        int multiplyColorAlpha2 = WXViewUtils.multiplyColorAlpha(getBorderColor(CSSShorthand.EDGE.TOP), this.mAlpha);
        int multiplyColorAlpha3 = WXViewUtils.multiplyColorAlpha(getBorderColor(CSSShorthand.EDGE.BOTTOM), this.mAlpha);
        int multiplyColorAlpha4 = WXViewUtils.multiplyColorAlpha(getBorderColor(CSSShorthand.EDGE.RIGHT), this.mAlpha);
        if (multiplyColorAlpha != multiplyColorAlpha2 || multiplyColorAlpha != multiplyColorAlpha3 || multiplyColorAlpha != multiplyColorAlpha4) {
            return false;
        }
        preparePaint(this.mBorderEdge.set(this.mTopLeftCorner, this.mTopRightCorner, f2, CSSShorthand.EDGE.TOP).getEdge());
        this.mPaint.setStrokeWidth(f);
        RectF rectF = new RectF();
        float f5 = f / 2.0f;
        rectF.top = this.mRectBounds.top + f5;
        rectF.bottom = this.mRectBounds.bottom - f5;
        rectF.left = this.mRectBounds.left + f5;
        rectF.right = this.mRectBounds.right - f5;
        canvas.drawRoundRect(rectF, outerCornerRadius, outerCornerRadius, this.mPaint);
        return true;
    }

    private float getBorderRadius(CSSShorthand.CORNER corner) {
        CSSShorthand<CSSShorthand.CORNER> cSSShorthand = this.mOverlappingBorderRadius;
        if (cSSShorthand != null) {
            return cSSShorthand.get((Enum<? extends AbsCSSShorthand.CSSProperty>) corner);
        }
        return 0.0f;
    }

    private void drawOneSide(Canvas canvas, BorderEdge borderEdge) {
        if (0.0f != borderEdge.getBorderWidth()) {
            preparePaint(borderEdge.getEdge());
            borderEdge.drawEdge(canvas, this.mPaint);
        }
    }

    private void preparePaint(CSSShorthand.EDGE edge) {
        float f = this.mBorderWidth.get((Enum<? extends AbsCSSShorthand.CSSProperty>) edge);
        int multiplyColorAlpha = WXViewUtils.multiplyColorAlpha(getBorderColor(edge), this.mAlpha);
        this.mPaint.setShader(sBorderStyle[getBorderStyle(edge)].getLineShader(f, multiplyColorAlpha, edge));
        this.mPaint.setColor(multiplyColorAlpha);
        this.mPaint.setStrokeCap(Paint.Cap.BUTT);
    }
}
