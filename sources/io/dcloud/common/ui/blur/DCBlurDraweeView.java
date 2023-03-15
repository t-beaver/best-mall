package io.dcloud.common.ui.blur;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.Choreographer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.ui.blur.AppEventForBlurManager;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.TitleNViewUtil;
import java.lang.ref.WeakReference;

public class DCBlurDraweeView extends FrameLayout implements AppEventForBlurManager.OnAppChangedCallBack {
    public static final String AUTOMATICALLY = "automatically";
    public static final String DARK = "dark";
    public static final String EXTRALIGHT = "extralight";
    public static final String LIGHT = "light";
    public static final String NONE = "none";
    public static final String SEMI_AUTOMATICALLY = "semi-automatic";
    public static final String STATIC = "static";
    private final String TAG = "DCBlurDraweeView";
    private Choreographer.FrameCallback invalidationLoop = new Choreographer.FrameCallback() {
        public void doFrame(long j) {
            if (!BaseInfo.sDoingAnimation) {
                DCBlurDraweeView.this.invalidate();
            }
            Choreographer.getInstance().postFrameCallbackDelayed(this, (long) (1000 / DCBlurDraweeView.this.mFPS));
        }
    };
    private boolean isBlur = false;
    private float mAlpha = Float.NaN;
    private boolean mAttachedToWindow;
    private String mBlurEffect = "none";
    private int mBlurRadius = 15;
    private String mBlurState = "static";
    private BlurLayoutChangeCallBack mChangeCB;
    private float mCornerRadius = 0.0f;
    private float mDownscaleFactor = 0.2f;
    /* access modifiers changed from: private */
    public int mFPS = 60;
    private int mGravityType = 17;
    private ImageView mImageView;
    private float mOverlayColorAlpha = 0.6f;
    private long mPostTime = 1500;
    private WeakReference<View> mRootView;
    /* access modifiers changed from: private */
    public boolean mRunning;
    /* access modifiers changed from: private */
    public int postDelayTime = 50;
    /* access modifiers changed from: private */
    public Choreographer.FrameCallback postInvalidationLoop = new Choreographer.FrameCallback() {
        public void doFrame(long j) {
            if (!DCBlurDraweeView.this.mRunning) {
                if (!BaseInfo.sDoingAnimation) {
                    DCBlurDraweeView.this.invalidate();
                }
                Choreographer.getInstance().postFrameCallbackDelayed(this, (long) DCBlurDraweeView.this.postDelayTime);
            }
        }
    };
    private Runnable removePostDelayed = new Runnable() {
        public void run() {
            Choreographer.getInstance().removeFrameCallback(DCBlurDraweeView.this.postInvalidationLoop);
        }
    };

    public interface BlurLayoutChangeCallBack {
        void setVisibility(int i);
    }

    public DCBlurDraweeView(Context context) {
        super(context);
    }

    private View getActivityView() {
        try {
            return ((Activity) getContext()).getWindow().getDecorView().findViewById(16908290);
        } catch (ClassCastException unused) {
            return null;
        }
    }

    private Bitmap getDownscaledBitmapForView(View view, Rect rect, float f, int i) throws Exception {
        int i2;
        int i3;
        float f2 = f / 0.5f;
        if (f >= 0.5f) {
            f2 = 1.0f;
        } else {
            f = 0.5f;
        }
        int width = (int) (((float) rect.width()) * f);
        int height = (int) (((float) rect.height()) * f);
        if (view.getWidth() <= 0 || view.getHeight() <= 0 || width <= 0 || height <= 0) {
            throw new Exception("No screen available (width or height = 0)");
        }
        float f3 = ((float) (-rect.left)) * f;
        float f4 = ((float) ((-rect.top) + i)) * f;
        int i4 = this.mGravityType;
        if (i4 == 17) {
            i3 = rect.height();
            i *= 2;
        } else if (i4 != 48) {
            i3 = rect.height();
        } else {
            i2 = (int) (((float) (rect.height() + i)) * f);
            f4 -= ((float) i) * f;
            setViewVisibility(4);
            Bitmap createBitmap = Bitmap.createBitmap(width, i2, Bitmap.Config.ARGB_8888);
            BlurCanvas blurCanvas = new BlurCanvas(createBitmap);
            Matrix matrix = new Matrix();
            matrix.postScale(f, f);
            matrix.postTranslate(f3, f4);
            blurCanvas.setMatrix(matrix);
            view.draw(blurCanvas);
            blurCanvas.drawColor(getOverlayColor());
            blurCanvas.save();
            setViewVisibility(0);
            float f5 = (float) width;
            int i5 = (int) (f5 * f2);
            float f6 = (float) i2;
            int i6 = (int) (f2 * f6);
            Bitmap createBitmap2 = Bitmap.createBitmap(i5, i6, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap2);
            Matrix matrix2 = new Matrix();
            matrix2.setScale(((float) i5) / f5, ((float) i6) / f6);
            canvas.setMatrix(matrix2);
            canvas.drawColor(getBlurBGColor());
            Paint paint = new Paint();
            paint.setFlags(3);
            canvas.drawBitmap(createBitmap, 0.0f, 0.0f, paint);
            createBitmap.recycle();
            return createBitmap2;
        }
        i2 = (int) (((float) (i3 + i)) * f);
        setViewVisibility(4);
        Bitmap createBitmap3 = Bitmap.createBitmap(width, i2, Bitmap.Config.ARGB_8888);
        BlurCanvas blurCanvas2 = new BlurCanvas(createBitmap3);
        Matrix matrix3 = new Matrix();
        matrix3.postScale(f, f);
        matrix3.postTranslate(f3, f4);
        blurCanvas2.setMatrix(matrix3);
        view.draw(blurCanvas2);
        blurCanvas2.drawColor(getOverlayColor());
        blurCanvas2.save();
        setViewVisibility(0);
        float f52 = (float) width;
        int i52 = (int) (f52 * f2);
        float f62 = (float) i2;
        int i62 = (int) (f2 * f62);
        Bitmap createBitmap22 = Bitmap.createBitmap(i52, i62, Bitmap.Config.ARGB_8888);
        Canvas canvas2 = new Canvas(createBitmap22);
        Matrix matrix22 = new Matrix();
        matrix22.setScale(((float) i52) / f52, ((float) i62) / f62);
        canvas2.setMatrix(matrix22);
        canvas2.drawColor(getBlurBGColor());
        Paint paint2 = new Paint();
        paint2.setFlags(3);
        canvas2.drawBitmap(createBitmap3, 0.0f, 0.0f, paint2);
        createBitmap3.recycle();
        return createBitmap22;
    }

    private int getOverlayColor() {
        String str = this.mBlurEffect;
        str.hashCode();
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case 3075958:
                if (str.equals(DARK)) {
                    c = 0;
                    break;
                }
                break;
            case 102970646:
                if (str.equals(LIGHT)) {
                    c = 1;
                    break;
                }
                break;
            case 759540486:
                if (str.equals(EXTRALIGHT)) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return Color.parseColor(TitleNViewUtil.changeColorAlpha("#454545", this.mOverlayColorAlpha));
            case 1:
                return Color.parseColor(TitleNViewUtil.changeColorAlpha("#F8F8F8", this.mOverlayColorAlpha));
            case 2:
                return Color.parseColor(TitleNViewUtil.changeColorAlpha(TitleNViewUtil.TRANSPARENT_BUTTON_TEXT_COLOR, this.mOverlayColorAlpha));
            default:
                return 0;
        }
    }

    private Point getPositionInScreen() {
        PointF positionInScreen = getPositionInScreen(this);
        return new Point((int) positionInScreen.x, (int) positionInScreen.y);
    }

    private void initImageView() {
        ImageView imageView = this.mImageView;
        if (imageView == null) {
            this.mImageView = new ImageView(getContext());
        } else if (imageView.getParent() == null) {
            ((ViewGroup) this.mImageView.getParent()).removeView(this.mImageView);
        }
        this.mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(this.mImageView, 0);
        setCornerRadius(this.mCornerRadius);
    }

    private void pauseBlur() {
        if (this.mRunning && this.isBlur) {
            this.mRunning = false;
            Choreographer.getInstance().removeFrameCallback(this.invalidationLoop);
        }
    }

    private void setViewVisibility(int i) {
        setVisibility(i);
        BlurLayoutChangeCallBack blurLayoutChangeCallBack = this.mChangeCB;
        if (blurLayoutChangeCallBack != null) {
            blurLayoutChangeCallBack.setVisibility(i);
        }
    }

    private void startBlur() {
        if (!this.mRunning && this.isBlur && this.mFPS > 0 && !this.mBlurState.equals("static")) {
            this.mRunning = true;
            Choreographer.getInstance().removeFrameCallback(this.postInvalidationLoop);
            Choreographer.getInstance().postFrameCallback(this.invalidationLoop);
        }
    }

    public boolean checkBlurEffect(String str) {
        if (!TextUtils.isEmpty(str)) {
            return str.equals(LIGHT) || str.equals(DARK) || str.equals(EXTRALIGHT);
        }
        return false;
    }

    public int getBlurBGColor() {
        String str = this.mBlurEffect;
        str.hashCode();
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case 3075958:
                if (str.equals(DARK)) {
                    c = 0;
                    break;
                }
                break;
            case 102970646:
                if (str.equals(LIGHT)) {
                    c = 1;
                    break;
                }
                break;
            case 759540486:
                if (str.equals(EXTRALIGHT)) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return Color.parseColor("#F2454545");
            case 1:
                return Color.parseColor("#F2F8F8F8");
            case 2:
                return Color.parseColor("#F2FFFFFF");
            default:
                return 0;
        }
    }

    public void invalidate() {
        Bitmap makeBlur;
        super.invalidate();
        if (this.mAttachedToWindow && this.mImageView != null && this.isBlur && (makeBlur = makeBlur()) != null) {
            this.mImageView.setImageBitmap(makeBlur);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00ac, code lost:
        if (r7 != 80) goto L_0x00ce;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.graphics.Bitmap makeBlur() {
        /*
            r11 = this;
            android.content.Context r0 = r11.getContext()
            r1 = 0
            if (r0 == 0) goto L_0x00d6
            boolean r0 = r11.isInEditMode()
            if (r0 != 0) goto L_0x00d6
            boolean r0 = r11.isBlur
            if (r0 != 0) goto L_0x0013
            goto L_0x00d6
        L_0x0013:
            java.lang.ref.WeakReference<android.view.View> r0 = r11.mRootView
            if (r0 == 0) goto L_0x001d
            java.lang.Object r0 = r0.get()
            if (r0 != 0) goto L_0x002f
        L_0x001d:
            java.lang.ref.WeakReference r0 = new java.lang.ref.WeakReference
            android.view.View r2 = r11.getActivityView()
            r0.<init>(r2)
            r11.mRootView = r0
            java.lang.Object r0 = r0.get()
            if (r0 != 0) goto L_0x002f
            return r1
        L_0x002f:
            r0 = 2
            int[] r2 = new int[r0]
            r11.getLocationOnScreen(r2)
            android.graphics.Point r2 = r11.getPositionInScreen()
            android.graphics.Rect r3 = new android.graphics.Rect
            r3.<init>()
            boolean r4 = r11.getGlobalVisibleRect(r3)
            if (r4 != 0) goto L_0x0045
            return r1
        L_0x0045:
            int r4 = r11.getHeight()
            int r5 = r11.getWidth()
            int r3 = r3.height()
            r6 = 0
            if (r3 < r4) goto L_0x0056
            r3 = 1
            goto L_0x0057
        L_0x0056:
            r3 = 0
        L_0x0057:
            if (r3 == 0) goto L_0x005c
            int r7 = r11.mBlurRadius
            goto L_0x005d
        L_0x005c:
            r7 = 0
        L_0x005d:
            java.lang.ref.WeakReference<android.view.View> r8 = r11.mRootView     // Catch:{ Exception -> 0x0077 }
            java.lang.Object r8 = r8.get()     // Catch:{ Exception -> 0x0077 }
            android.view.View r8 = (android.view.View) r8     // Catch:{ Exception -> 0x0077 }
            android.graphics.Rect r9 = new android.graphics.Rect     // Catch:{ Exception -> 0x0077 }
            int r10 = r2.x     // Catch:{ Exception -> 0x0077 }
            int r2 = r2.y     // Catch:{ Exception -> 0x0077 }
            int r5 = r5 + r10
            int r4 = r4 + r2
            r9.<init>(r10, r2, r5, r4)     // Catch:{ Exception -> 0x0077 }
            float r2 = r11.mDownscaleFactor     // Catch:{ Exception -> 0x0077 }
            android.graphics.Bitmap r1 = r11.getDownscaledBitmapForView(r8, r9, r2, r7)     // Catch:{ Exception -> 0x0077 }
            goto L_0x007b
        L_0x0077:
            r2 = move-exception
            r2.printStackTrace()
        L_0x007b:
            if (r1 == 0) goto L_0x00d6
            io.dcloud.common.ui.blur.BlurManager r2 = io.dcloud.common.ui.blur.BlurManager.getInstance()
            int r4 = r11.mBlurRadius
            android.graphics.Bitmap r1 = r2.processNatively(r1, r4, r6)
            float r2 = (float) r7
            float r4 = r11.mDownscaleFactor
            float r2 = r2 * r4
            int r2 = (int) r2
            if (r3 == 0) goto L_0x0091
            r4 = r2
            goto L_0x0092
        L_0x0091:
            r4 = 0
        L_0x0092:
            if (r3 == 0) goto L_0x009c
            int r5 = r1.getHeight()
            int r7 = r2 * 2
            int r5 = r5 - r7
            goto L_0x00a0
        L_0x009c:
            int r5 = r1.getHeight()
        L_0x00a0:
            int r7 = r11.mGravityType
            r8 = 17
            if (r7 == r8) goto L_0x00bf
            r8 = 48
            if (r7 == r8) goto L_0x00af
            r8 = 80
            if (r7 == r8) goto L_0x00bf
            goto L_0x00ce
        L_0x00af:
            r4 = 5
            if (r3 == 0) goto L_0x00b8
            int r0 = r1.getHeight()
            int r0 = r0 - r2
            goto L_0x00bc
        L_0x00b8:
            int r0 = r1.getHeight()
        L_0x00bc:
            int r5 = r0 + -5
            goto L_0x00ce
        L_0x00bf:
            if (r3 == 0) goto L_0x00ca
            int r3 = r1.getHeight()
            int r2 = r2 * 2
            int r5 = r3 - r2
            goto L_0x00ce
        L_0x00ca:
            int r5 = r1.getHeight()
        L_0x00ce:
            int r0 = r1.getWidth()
            android.graphics.Bitmap r1 = android.graphics.Bitmap.createBitmap(r1, r6, r4, r0, r5)
        L_0x00d6:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.ui.blur.DCBlurDraweeView.makeBlur():android.graphics.Bitmap");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x002d, code lost:
        if (r1.equals(SEMI_AUTOMATICALLY) == false) goto L_0x001a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onAttachedToWindow() {
        /*
            r4 = this;
            super.onAttachedToWindow()
            r0 = 1
            r4.mAttachedToWindow = r0
            boolean r1 = r4.isBlur
            if (r1 == 0) goto L_0x0056
            java.lang.String r1 = r4.mBlurState
            r1.hashCode()
            r1.hashCode()
            r2 = -1
            int r3 = r1.hashCode()
            switch(r3) {
                case -892481938: goto L_0x0030;
                case 1030681228: goto L_0x0027;
                case 1977933731: goto L_0x001c;
                default: goto L_0x001a;
            }
        L_0x001a:
            r0 = -1
            goto L_0x003a
        L_0x001c:
            java.lang.String r0 = "automatically"
            boolean r0 = r1.equals(r0)
            if (r0 != 0) goto L_0x0025
            goto L_0x001a
        L_0x0025:
            r0 = 2
            goto L_0x003a
        L_0x0027:
            java.lang.String r3 = "semi-automatic"
            boolean r1 = r1.equals(r3)
            if (r1 != 0) goto L_0x003a
            goto L_0x001a
        L_0x0030:
            java.lang.String r0 = "static"
            boolean r0 = r1.equals(r0)
            if (r0 != 0) goto L_0x0039
            goto L_0x001a
        L_0x0039:
            r0 = 0
        L_0x003a:
            switch(r0) {
                case 0: goto L_0x004e;
                case 1: goto L_0x0045;
                case 2: goto L_0x003e;
                default: goto L_0x003d;
            }
        L_0x003d:
            goto L_0x0056
        L_0x003e:
            io.dcloud.common.ui.blur.AppEventForBlurManager.removeEventChangedCallBack(r4)
            r4.startBlur()
            goto L_0x0056
        L_0x0045:
            long r0 = r4.mPostTime
            r4.postInvalidate(r0)
            io.dcloud.common.ui.blur.AppEventForBlurManager.addEventChangedCallBack(r4)
            goto L_0x0056
        L_0x004e:
            long r0 = r4.mPostTime
            r4.postInvalidate(r0)
            io.dcloud.common.ui.blur.AppEventForBlurManager.removeEventChangedCallBack(r4)
        L_0x0056:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.ui.blur.DCBlurDraweeView.onAttachedToWindow():void");
    }

    public void onContentScrollEnd() {
        pauseBlur();
    }

    public void onContentScrollStart() {
        startBlur();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mAttachedToWindow = false;
        pauseBlur();
        AppEventForBlurManager.removeEventChangedCallBack(this);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (this.isBlur) {
            postInvalidate(this.mPostTime);
        }
    }

    public void onSplashclosed() {
        postInvalidate(this.mPostTime);
    }

    public void postInvalidate(long j) {
        if (!this.mRunning && this.isBlur) {
            MessageHandler.removeCallbacks(this.removePostDelayed);
            Choreographer.getInstance().removeFrameCallback(this.postInvalidationLoop);
            Choreographer.getInstance().postFrameCallback(this.postInvalidationLoop);
            MessageHandler.postDelayed(this.removePostDelayed, j + 10);
        }
    }

    public void setBlur(boolean z) {
        this.isBlur = z;
        if (z && this.mImageView == null) {
            initImageView();
        }
    }

    public void setBlurEffect(String str) {
        this.mBlurEffect = str;
        if (this.isBlur || !this.mBlurState.equals("none")) {
            setBackgroundColor(0);
        } else {
            setBackgroundColor(getBlurBGColor());
        }
    }

    public void setBlurLayoutChangeCallBack(BlurLayoutChangeCallBack blurLayoutChangeCallBack) {
        this.mChangeCB = blurLayoutChangeCallBack;
    }

    public void setBlurRadius(int i) {
        if (i < 0) {
            i = 15;
        } else if (i > 25) {
            i = 25;
        }
        this.mBlurRadius = i;
        invalidate();
    }

    public void setBlurState(String str) {
        this.mBlurState = str;
    }

    public void setContentFocusable(boolean z) {
        String str = this.mBlurState;
        str.hashCode();
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -892481938:
                if (str.equals("static")) {
                    c = 0;
                    break;
                }
                break;
            case 1030681228:
                if (str.equals(SEMI_AUTOMATICALLY)) {
                    c = 1;
                    break;
                }
                break;
            case 1977933731:
                if (str.equals(AUTOMATICALLY)) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                AppEventForBlurManager.removeEventChangedCallBack(this);
                pauseBlur();
                return;
            case 1:
                if (z) {
                    AppEventForBlurManager.addEventChangedCallBack(this);
                    return;
                } else {
                    AppEventForBlurManager.removeEventChangedCallBack(this);
                    return;
                }
            case 2:
                if (z) {
                    startBlur();
                    return;
                } else {
                    pauseBlur();
                    return;
                }
            default:
                return;
        }
    }

    public void setCornerRadius(float f) {
        this.mCornerRadius = f;
        invalidate();
    }

    public void setDownscaleFactor(float f) {
        this.mDownscaleFactor = f;
        invalidate();
    }

    public void setFPS(int i) {
        if (this.mRunning) {
            pauseBlur();
        }
        this.mFPS = i;
    }

    public void setGravityType(int i) {
        this.mGravityType = i;
    }

    public void setOverlayColorAlpha(float f) {
        this.mOverlayColorAlpha = f;
    }

    public void setRootView(View view) {
        WeakReference<View> weakReference = this.mRootView;
        if (!(weakReference == null || weakReference.get() == null)) {
            this.mRootView.clear();
        }
        this.mRootView = new WeakReference<>(view);
    }

    private PointF getPositionInScreen(View view) {
        if (getParent() == null) {
            return new PointF();
        }
        if (this.mRootView.get() != null && view == this.mRootView.get()) {
            return new PointF();
        }
        try {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup == null) {
                return new PointF();
            }
            PointF positionInScreen = getPositionInScreen(viewGroup);
            positionInScreen.offset(view.getX(), view.getY());
            return positionInScreen;
        } catch (Exception unused) {
            return new PointF();
        }
    }

    public DCBlurDraweeView(Context context, boolean z, String str) {
        super(context);
        this.isBlur = z;
        this.mBlurState = str;
        if (z) {
            initImageView();
        }
    }
}
