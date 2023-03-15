package io.dcloud.common.adapter.ui;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import io.dcloud.common.DHInterface.INativeBitmap;
import io.dcloud.common.adapter.util.CanvasHelper;
import io.dcloud.common.util.PdrUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class FrameBitmapView extends View {
    public static String BOLD = "bold";
    public static String ITALIC = "italic";
    public static String NORMAL = "normal";
    private boolean isInit = false;
    private float mBitmapCX;
    private float mBitmapCY;
    /* access modifiers changed from: private */
    public int mCutIndex = 0;
    /* access modifiers changed from: private */
    public RectF mCutRectF;
    private float mFontCX;
    private float mFontCY;
    private int mHeight;
    /* access modifiers changed from: private */
    public ClearAnimationListener mListener;
    /* access modifiers changed from: private */
    public INativeBitmap mNativeBitmap;
    private Paint mPaint = new Paint();
    private float mScale;
    /* access modifiers changed from: private */
    public boolean mStopAnimation = false;
    RectF mTextRect = null;
    private String mTextValue;
    private String mTexts;
    private int mWidth;

    public interface ClearAnimationListener {
        void onAnimationEnd();
    }

    public FrameBitmapView(Activity activity) {
        super(activity);
    }

    static /* synthetic */ int access$208(FrameBitmapView frameBitmapView) {
        int i = frameBitmapView.mCutIndex;
        frameBitmapView.mCutIndex = i + 1;
        return i;
    }

    public static float getFontHeight(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return (float) ((int) Math.ceil((double) (fontMetrics.bottom - fontMetrics.top)));
    }

    public static float getFontLeading(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return fontMetrics.leading - fontMetrics.ascent;
    }

    public static float getFontlength(Paint paint, String str) {
        return paint.measureText(str);
    }

    private void initBitmapXY() {
        float f = (float) this.mWidth;
        float f2 = (float) this.mHeight;
        if (this.mNativeBitmap.getBitmap() != null) {
            this.mBitmapCX = (f / 2.0f) - ((float) (this.mNativeBitmap.getBitmap().getWidth() / 2));
            this.mBitmapCY = (f2 / 2.0f) - ((float) (this.mNativeBitmap.getBitmap().getHeight() / 2));
        }
    }

    private void initTextData() {
        String str;
        String str2;
        String str3;
        String str4 = "";
        if (!TextUtils.isEmpty(this.mTexts)) {
            try {
                JSONObject jSONObject = new JSONObject(this.mTexts);
                this.mTextValue = jSONObject.optString("value", str4);
                String str5 = this.mWidth + "px";
                String str6 = "44px";
                String str7 = "16px";
                String str8 = "#000000";
                String str9 = NORMAL;
                String str10 = "center";
                String str11 = "0px";
                if (jSONObject.has("textRect")) {
                    JSONObject jSONObject2 = jSONObject.getJSONObject("textRect");
                    str2 = jSONObject2.optString("top", str11);
                    str = jSONObject2.optString("left", str11);
                    str5 = jSONObject2.optString("width", str5);
                    str6 = jSONObject2.optString("height", str6);
                } else {
                    str2 = str11;
                    str = str2;
                }
                if (jSONObject.has("textStyles")) {
                    JSONObject jSONObject3 = jSONObject.getJSONObject("textStyles");
                    str7 = jSONObject3.optString(AbsoluteConst.JSON_KEY_SIZE, str7);
                    str8 = jSONObject3.optString("color", str8);
                    str3 = jSONObject3.optString("weight", str9);
                    str9 = jSONObject3.optString("style", str9);
                    str4 = jSONObject3.optString("family", str4);
                    str10 = jSONObject3.optString(AbsoluteConst.JSON_KEY_ALIGN, str10);
                    str11 = jSONObject3.optString("margin", str11);
                } else {
                    str3 = str9;
                }
                int i = this.mWidth;
                int convertToScreenInt = PdrUtil.convertToScreenInt(str, i, i, this.mScale);
                int i2 = this.mHeight;
                int convertToScreenInt2 = PdrUtil.convertToScreenInt(str2, i2, i2, this.mScale);
                int i3 = this.mWidth;
                int convertToScreenInt3 = PdrUtil.convertToScreenInt(str5, i3, i3, this.mScale);
                int i4 = this.mHeight;
                int convertToScreenInt4 = PdrUtil.convertToScreenInt(str6, i4, i4, this.mScale);
                int i5 = this.mHeight;
                int convertToScreenInt5 = PdrUtil.convertToScreenInt(str7, i5, i5, this.mScale);
                int convertToScreenInt6 = PdrUtil.convertToScreenInt(str11, convertToScreenInt3, convertToScreenInt4, this.mScale);
                this.mPaint.setTextSize((float) convertToScreenInt5);
                this.mPaint.setColor(PdrUtil.stringToColor(str8));
                if (!TextUtils.isEmpty(str4)) {
                    this.mPaint.setTypeface(Typeface.create(str4, 0));
                }
                this.mTextRect = new RectF((float) (convertToScreenInt + convertToScreenInt6), (float) (convertToScreenInt2 + convertToScreenInt6), (float) (convertToScreenInt3 - convertToScreenInt6), (float) (convertToScreenInt4 - convertToScreenInt6));
                this.mPaint.setFakeBoldText(str3.equals(BOLD));
                if (str9.equals(ITALIC)) {
                    this.mPaint.setTextSkewX(-0.5f);
                }
                float fontlength = getFontlength(this.mPaint, this.mTextValue);
                float fontHeight = getFontHeight(this.mPaint);
                if (str10.equals("right")) {
                    RectF rectF = this.mTextRect;
                    this.mFontCX = rectF.right - fontlength;
                    this.mFontCY = rectF.top + ((float) (((int) (rectF.height() - fontHeight)) / 2));
                } else if (str10.equals("left")) {
                    RectF rectF2 = this.mTextRect;
                    this.mFontCX = rectF2.left;
                    this.mFontCY = rectF2.top + ((float) (((int) (rectF2.height() - fontHeight)) / 2));
                } else {
                    RectF rectF3 = this.mTextRect;
                    this.mFontCX = rectF3.left + ((float) (((int) (rectF3.width() - fontlength)) / 2));
                    RectF rectF4 = this.mTextRect;
                    this.mFontCY = rectF4.top + ((float) (((int) (rectF4.height() - fontHeight)) / 2));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (this.mNativeBitmap == null) {
            setVisibility(8);
        }
    }

    public void clearData() {
        this.mNativeBitmap = null;
        this.mTextValue = null;
        this.mFontCX = 0.0f;
        this.mFontCY = 0.0f;
        this.mCutIndex = 0;
        this.mCutRectF = null;
        this.mListener = null;
        this.mStopAnimation = false;
        this.isInit = false;
    }

    public void configurationChanged(int i, int i2) {
        if (this.mNativeBitmap != null) {
            this.mWidth = i;
            this.mHeight = i2;
            initBitmapXY();
            initTextData();
            invalidate();
        }
    }

    public void injectionData(Object obj, String str, int i, int i2, float f) {
        this.mWidth = i;
        this.mHeight = i2;
        this.mTexts = str;
        this.mScale = f;
        this.mNativeBitmap = (INativeBitmap) obj;
        this.isInit = true;
        initBitmapXY();
        initTextData();
        bringToFront();
        invalidate();
    }

    public boolean isInit() {
        return this.isInit;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        this.mPaint.setAntiAlias(true);
        canvas.save();
        RectF rectF = this.mCutRectF;
        if (rectF != null) {
            canvas.clipRect(rectF);
        }
        canvas.restore();
        INativeBitmap iNativeBitmap = this.mNativeBitmap;
        if (!(iNativeBitmap == null || iNativeBitmap.getBitmap() == null)) {
            canvas.save();
            canvas.drawBitmap(this.mNativeBitmap.getBitmap(), this.mBitmapCX, this.mBitmapCY, this.mPaint);
            canvas.restore();
        }
        if (!TextUtils.isEmpty(this.mTextValue)) {
            canvas.save();
            canvas.clipRect(this.mTextRect);
            CanvasHelper.drawString(canvas, this.mTextValue, (int) this.mFontCX, (int) this.mFontCY, 17, this.mPaint);
            canvas.restore();
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        setMeasuredDimension(this.mWidth, this.mHeight);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return true;
    }

    public void runClearAnimation(int i, int i2, ClearAnimationListener clearAnimationListener) {
        this.mListener = clearAnimationListener;
        runClearAnimation(i, i2);
    }

    public void setStopAnimation(boolean z) {
        this.mStopAnimation = z;
    }

    public void runClearAnimation(final int i, final int i2) {
        postDelayed(new Runnable() {
            public void run() {
                if (!FrameBitmapView.this.mStopAnimation) {
                    FrameBitmapView.access$208(FrameBitmapView.this);
                    int height = FrameBitmapView.this.mNativeBitmap.getBitmap().getHeight();
                    RectF unused = FrameBitmapView.this.mCutRectF = new RectF(0.0f, (float) ((height / i) * FrameBitmapView.this.mCutIndex), (float) FrameBitmapView.this.mNativeBitmap.getBitmap().getWidth(), (float) height);
                    FrameBitmapView.this.invalidate();
                    int access$200 = FrameBitmapView.this.mCutIndex;
                    int i = i;
                    if (access$200 < i) {
                        FrameBitmapView.this.runClearAnimation(i, i2);
                    } else if (FrameBitmapView.this.mListener != null) {
                        FrameBitmapView.this.mListener.onAnimationEnd();
                    }
                } else if (FrameBitmapView.this.mListener != null) {
                    FrameBitmapView.this.mListener.onAnimationEnd();
                }
            }
        }, (long) i2);
    }
}
