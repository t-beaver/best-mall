package com.dcloud.android.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.Region;
import android.text.TextUtils;
import android.view.MotionEvent;
import com.dcloud.android.widget.SlideLayout;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.adapter.util.ViewOptions;
import io.dcloud.common.core.ui.g;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.StringUtil;
import org.json.JSONObject;

public class AbsoluteLayout extends SlideLayout {
    static final String STATE_CHANGED_TEMPLATE = "{status:'%s',offset:'%s'}";
    boolean canDoMaskClickEvent = true;
    float downX;
    float downY;
    private boolean isAnimate;
    private boolean isCanDrag = false;
    IApp mAppHandler = null;
    String mCallBackID;
    g mDrag;
    AdaFrameView mFrameView = null;
    private int mRegionBottom;
    private int mRegionLeft;
    private RectF mRegionRect;
    private int mRegionRight;
    private int mRegionTop;
    ViewOptions mViewOptions = null;

    public AbsoluteLayout(Context context, AdaFrameView adaFrameView, IApp iApp) {
        super(context);
        this.mDrag = new g(adaFrameView, context);
        this.mFrameView = adaFrameView;
        this.mAppHandler = iApp;
        this.mViewOptions = adaFrameView.obtainFrameOptions();
        setOnStateChangeListener(new SlideLayout.OnStateChangeListener() {
            public void onStateChanged(String str, String str2) {
                AbsoluteLayout.this.mFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_SLIDE_BOUNCE, StringUtil.format(AbsoluteLayout.STATE_CHANGED_TEMPLATE, str, str2));
            }
        });
    }

    private void doClickEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.canDoMaskClickEvent = true;
            this.downX = motionEvent.getX();
            this.downY = motionEvent.getY();
        } else if (motionEvent.getAction() == 1) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            if (this.canDoMaskClickEvent) {
                float f = (float) 10;
                if (Math.abs(this.downX - x) <= f && Math.abs(this.downY - y) <= f) {
                    this.mFrameView.dispatchFrameViewEvents(AbsoluteConst.EVENTS_MASK_CLICK, (Object) null);
                }
            }
        } else if (motionEvent.getAction() == 2) {
            float x2 = motionEvent.getX();
            float y2 = motionEvent.getY();
            if (this.canDoMaskClickEvent) {
                float f2 = (float) 10;
                if (Math.abs(this.downX - x2) > f2 && Math.abs(this.downY - y2) > f2) {
                    this.canDoMaskClickEvent = false;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void endAnimatecallback(IWebview iWebview, String str) {
        if (!TextUtils.isEmpty(str)) {
            Deprecated_JSUtil.execCallback(iWebview, str, (String) null, JSUtil.OK, false, false);
        }
    }

    /* access modifiers changed from: private */
    public void runDrawRectF(IWebview iWebview, String str, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
        int i10 = i3;
        int i11 = i9;
        if (!this.isAnimate) {
            endAnimatecallback(iWebview, str);
            return;
        }
        if (this.mRegionRect == null) {
            this.mRegionRect = new RectF();
        }
        RectF rectF = this.mRegionRect;
        rectF.left = (float) i;
        rectF.right = (float) (this.mViewOptions.width - i2);
        rectF.top = (float) i10;
        if (i11 == i7) {
            rectF.bottom = (float) ((i6 * i11) + i10 + i8);
        } else {
            rectF.bottom = (float) ((i6 * i11) + i10);
        }
        final int i12 = i9;
        final int i13 = i7;
        final IWebview iWebview2 = iWebview;
        final String str2 = str;
        final int i14 = i;
        final int i15 = i2;
        final int i16 = i3;
        final int i17 = i4;
        final int i18 = i5;
        final int i19 = i6;
        final int i20 = i8;
        postDelayed(new Runnable() {
            public void run() {
                AbsoluteLayout.this.invalidate();
                int i = i12;
                int i2 = i13;
                if (i == i2) {
                    AbsoluteLayout.this.endAnimatecallback(iWebview2, str2);
                } else {
                    AbsoluteLayout.this.runDrawRectF(iWebview2, str2, i14, i15, i16, i17, i18, i19, i2, i20, i + 1);
                }
            }
        }, (long) i5);
    }

    public void animate(IWebview iWebview, String str, String str2) {
        IWebview iWebview2 = iWebview;
        String str3 = str2;
        if (this.mViewOptions != null) {
            this.mCallBackID = str3;
            try {
                JSONObject jSONObject = new JSONObject(str);
                String optString = jSONObject.optString("type");
                int optInt = jSONObject.optInt("duration", 200);
                int optInt2 = jSONObject.optInt("frames", 12);
                JSONObject optJSONObject = jSONObject.optJSONObject("region");
                if (optJSONObject != null) {
                    String optString2 = optJSONObject.optString("left");
                    ViewOptions viewOptions = this.mViewOptions;
                    this.mRegionLeft = PdrUtil.convertToScreenInt(optString2, viewOptions.width, 0, viewOptions.mWebviewScale);
                    String optString3 = optJSONObject.optString("right");
                    ViewOptions viewOptions2 = this.mViewOptions;
                    this.mRegionRight = PdrUtil.convertToScreenInt(optString3, viewOptions2.width, 0, viewOptions2.mWebviewScale);
                    String optString4 = optJSONObject.optString("top");
                    ViewOptions viewOptions3 = this.mViewOptions;
                    this.mRegionTop = PdrUtil.convertToScreenInt(optString4, viewOptions3.height, 0, viewOptions3.mWebviewScale);
                    String optString5 = optJSONObject.optString("bottom");
                    ViewOptions viewOptions4 = this.mViewOptions;
                    this.mRegionBottom = PdrUtil.convertToScreenInt(optString5, viewOptions4.height, 0, viewOptions4.mWebviewScale);
                }
                int i = optInt / optInt2;
                ViewOptions viewOptions5 = this.mViewOptions;
                int i2 = viewOptions5.height - ((this.mRegionTop + viewOptions5.top) + this.mRegionBottom);
                int i3 = i2 / optInt2;
                int i4 = i2 - (i3 * optInt2);
                if (!TextUtils.isEmpty(optString) && optString.equals("shrink")) {
                    this.isAnimate = true;
                    int i5 = this.mRegionLeft;
                    int i6 = this.mRegionRight;
                    int i7 = this.mRegionTop;
                    ViewOptions viewOptions6 = this.mViewOptions;
                    runDrawRectF(iWebview, str2, i5, i6, i7 + viewOptions6.top, viewOptions6.height - this.mRegionBottom, i, i3, optInt2, i4, 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
                endAnimatecallback(iWebview2, str3);
            }
        } else {
            endAnimatecallback(iWebview2, str3);
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        canvas.save();
        RectF rectF = this.mRegionRect;
        if (rectF != null) {
            canvas.clipRect(rectF, Region.Op.DIFFERENCE);
        }
        ViewOptions viewOptions = this.mViewOptions;
        if (viewOptions != null && !viewOptions.isTabHasBg() && !this.mViewOptions.hasBackground() && !this.mViewOptions.isTransparent() && !this.mViewOptions.hasMask() && this.mViewOptions.mUniNViewJson != null) {
            canvas.drawColor(-1);
        }
        this.mFrameView.paint(canvas);
        try {
            super.dispatchDraw(canvas);
            canvas.restore();
            ViewOptions viewOptions2 = this.mViewOptions;
            if (viewOptions2 != null && viewOptions2.hasMask()) {
                canvas.drawColor(this.mViewOptions.maskColor);
            }
            this.mFrameView.onDrawAfter(canvas);
        } catch (Exception unused) {
        }
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        try {
            if (!this.mFrameView.interceptTouchEvent) {
                return false;
            }
            ViewOptions viewOptions = this.mViewOptions;
            if (viewOptions == null || !viewOptions.hasMask()) {
                ViewOptions viewOptions2 = this.mViewOptions;
                if (viewOptions2 == null || !viewOptions2.hasBackground()) {
                    return super.dispatchTouchEvent(motionEvent);
                }
                super.dispatchTouchEvent(motionEvent);
                return true;
            }
            doClickEvent(motionEvent);
            if (motionEvent.getAction() == 0) {
                this.isCanDrag = false;
            }
            if (!this.isCanDrag) {
                this.isCanDrag = this.mDrag.a(motionEvent);
            }
            if (this.isCanDrag) {
                onTouchEvent(motionEvent);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public g getDrag() {
        return this.mDrag;
    }

    public AdaFrameView getFrameView() {
        return this.mFrameView;
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mFrameView.onConfigurationChanged();
        if (this.isAnimate) {
            this.isAnimate = false;
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.mRegionRect != null) {
            canvas.save();
            ViewOptions viewOptions = this.mViewOptions;
            int i = viewOptions.left;
            int i2 = viewOptions.top;
            canvas.clipRect(i, i2, viewOptions.width + i, viewOptions.height + i2);
            canvas.restore();
        }
        super.onDraw(canvas);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!this.mDrag.a(motionEvent)) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        return true;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean c = this.mDrag.c(motionEvent);
        if (this.mViewOptions.isTransparent()) {
            return super.onTouchEvent(motionEvent);
        }
        AdaFrameView adaFrameView = this.mFrameView;
        if ((adaFrameView == null || !adaFrameView.isTouchEvent) && !c) {
            return super.onTouchEvent(motionEvent);
        }
        return true;
    }

    public void restore() {
        this.isAnimate = false;
        this.mRegionRect = null;
        invalidate();
    }

    public String toString() {
        AdaFrameView adaFrameView = this.mFrameView;
        if (adaFrameView != null) {
            return adaFrameView.toString();
        }
        return super.toString();
    }
}
