package com.dcloud.android.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.Scroller;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.PdrUtil;
import org.json.JSONObject;

public class SlideLayout extends AbsoluteLayout {
    private static String AFTER_SLIDE = "afterSlide";
    private static String BEFORE_SLIDE = "beforeSlide";
    private static String LEFT = "left";
    private static String RIGHT = "right";
    private static final int SCROLL_DURATION = 150;
    private static final int SNAP_VELOCITY = 1000;
    boolean isLeftSlide = false;
    boolean isRightSlide = false;
    boolean isSlideOpen = false;
    private boolean mCanDoSlideTransverseEvent = false;
    /* access modifiers changed from: private */
    public OnStateChangeListener mChangeListener;
    private float mFirstX = 0.0f;
    private boolean mInterceptEventEnable = true;
    private boolean mIsHandledTouchEvent = false;
    private float mLastMotionX = -1.0f;
    private Scroller mScroller = new Scroller(getContext());
    private int mSlideLeftPosition = -1;
    private int mSlideRightPosition = -1;
    private int mSlideTransverseLeftMaxWitch = 0;
    private int mSlideTransverseRightMaxWitch = 0;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;

    public interface OnStateChangeListener {
        void onStateChanged(String str, String str2);
    }

    public SlideLayout(Context context) {
        super(context);
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    private void setState(final String str, final String str2) {
        if (this.mChangeListener != null) {
            postDelayed(new Runnable() {
                public void run() {
                    SlideLayout.this.mChangeListener.onStateChanged(str2, str);
                }
            }, 150);
        }
    }

    private void smoothScrollTo(int i, int i2) {
        enableChildrenCache();
        this.mScroller.startScroll(getScrollX(), 0, i, 0, Math.abs(i) * 2);
        invalidate();
    }

    /* access modifiers changed from: package-private */
    public void clearChildrenCache() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).setDrawingCacheEnabled(false);
        }
    }

    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
            postInvalidate();
        } else {
            clearChildrenCache();
        }
        super.computeScroll();
    }

    /* access modifiers changed from: package-private */
    public void enableChildrenCache() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).setDrawingCacheEnabled(true);
        }
    }

    public void initSlideInfo(JSONObject jSONObject, float f, int i) {
        JSONObject jSONObject2 = JSONUtil.getJSONObject(jSONObject, AbsoluteConst.BOUNCE_SLIDEO_OFFSET);
        if (jSONObject2 != null) {
            JSONObject jSONObject3 = JSONUtil.getJSONObject(jSONObject, "position");
            if (jSONObject3 != null) {
                String optString = jSONObject3.optString(LEFT);
                String optString2 = jSONObject3.optString(RIGHT);
                if (!TextUtils.isEmpty(optString)) {
                    this.mSlideLeftPosition = PdrUtil.convertToScreenInt(optString, i, i / 2, f);
                }
                if (!TextUtils.isEmpty(optString2)) {
                    this.mSlideRightPosition = PdrUtil.convertToScreenInt(optString2, i, i / 2, f);
                }
            }
            boolean z = true;
            this.mInterceptEventEnable = jSONObject.optBoolean("preventTouchEvent", true);
            String string = JSONUtil.getString(jSONObject2, LEFT);
            if (!TextUtils.isEmpty(string)) {
                this.isLeftSlide = this.mSlideLeftPosition > 0;
                this.mSlideTransverseLeftMaxWitch = PdrUtil.convertToScreenInt(string, i, i / 2, f);
            }
            String string2 = JSONUtil.getString(jSONObject2, RIGHT);
            if (!TextUtils.isEmpty(string2)) {
                if (this.mSlideRightPosition <= 0) {
                    z = false;
                }
                this.isRightSlide = z;
                this.mSlideTransverseRightMaxWitch = PdrUtil.convertToScreenInt(string2, i, i / 2, f);
            }
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (!this.mInterceptEventEnable) {
            return false;
        }
        if (!this.isLeftSlide && !this.isRightSlide) {
            return false;
        }
        if (action == 3 || action == 1) {
            this.mIsHandledTouchEvent = false;
            clearChildrenCache();
            return this.mIsHandledTouchEvent;
        } else if (action != 0 && this.mIsHandledTouchEvent) {
            return true;
        } else {
            if (action == 0) {
                this.mLastMotionX = motionEvent.getX();
                this.mFirstX = motionEvent.getX();
                this.mIsHandledTouchEvent = false;
                this.mCanDoSlideTransverseEvent = false;
            } else if (action == 2 && ((int) Math.abs(motionEvent.getX() - this.mFirstX)) > this.mTouchSlop) {
                enableChildrenCache();
                this.mIsHandledTouchEvent = true;
                this.mCanDoSlideTransverseEvent = true;
                requestDisallowInterceptTouchEvent(true);
            }
            return this.mIsHandledTouchEvent;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001c, code lost:
        if (r0 != 3) goto L_0x017c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(android.view.MotionEvent r8) {
        /*
            r7 = this;
            boolean r0 = r7.mCanDoSlideTransverseEvent
            if (r0 == 0) goto L_0x000e
            android.view.VelocityTracker r0 = r7.mVelocityTracker
            if (r0 != 0) goto L_0x000e
            android.view.VelocityTracker r0 = android.view.VelocityTracker.obtain()
            r7.mVelocityTracker = r0
        L_0x000e:
            int r0 = r8.getAction()
            r1 = 0
            if (r0 == 0) goto L_0x016d
            r2 = 1
            if (r0 == r2) goto L_0x00c6
            r3 = 2
            if (r0 == r3) goto L_0x0020
            r3 = 3
            if (r0 == r3) goto L_0x00c6
            goto L_0x017c
        L_0x0020:
            android.view.VelocityTracker r0 = r7.mVelocityTracker
            if (r0 == 0) goto L_0x0027
            r0.addMovement(r8)
        L_0x0027:
            float r0 = r8.getX()
            boolean r3 = r7.mCanDoSlideTransverseEvent
            if (r3 == 0) goto L_0x017c
            float r3 = r7.mLastMotionX
            float r3 = r3 - r0
            int r3 = (int) r3
            r7.mLastMotionX = r0
            int r0 = r7.getScrollX()
            int r4 = r7.getChildCount()
            int r4 = r4 - r2
            android.view.View r2 = r7.getChildAt(r4)
            int r2 = r2.getRight()
            int r2 = r2 - r0
            int r4 = r7.getWidth()
            int r2 = r2 - r4
            if (r0 != 0) goto L_0x0064
            boolean r0 = r7.isRightSlide
            if (r0 == 0) goto L_0x0059
            if (r3 <= 0) goto L_0x0059
            r7.scrollBy(r3, r1)
            goto L_0x017c
        L_0x0059:
            boolean r0 = r7.isLeftSlide
            if (r0 == 0) goto L_0x017c
            if (r3 >= 0) goto L_0x017c
            r7.scrollBy(r3, r1)
            goto L_0x017c
        L_0x0064:
            r4 = 4609434218613702656(0x3ff8000000000000, double:1.5)
            if (r0 <= 0) goto L_0x0096
            if (r2 >= 0) goto L_0x0096
            boolean r6 = r7.isRightSlide
            if (r6 == 0) goto L_0x0096
            int r0 = r0 + r3
            int r2 = java.lang.Math.abs(r0)
            int r6 = r7.mSlideRightPosition
            if (r2 > r6) goto L_0x017c
            if (r0 >= 0) goto L_0x007e
            r7.scrollBy(r1, r1)
            goto L_0x017c
        L_0x007e:
            int r0 = java.lang.Math.abs(r0)
            int r2 = r7.mSlideTransverseRightMaxWitch
            if (r0 < r2) goto L_0x0091
            double r2 = (double) r3
            java.lang.Double.isNaN(r2)
            double r2 = r2 / r4
            int r0 = (int) r2
            r7.scrollBy(r0, r1)
            goto L_0x017c
        L_0x0091:
            r7.scrollBy(r3, r1)
            goto L_0x017c
        L_0x0096:
            if (r0 >= 0) goto L_0x017c
            if (r2 <= 0) goto L_0x017c
            boolean r2 = r7.isLeftSlide
            if (r2 == 0) goto L_0x017c
            int r0 = r0 + r3
            int r2 = java.lang.Math.abs(r0)
            int r6 = r7.mSlideLeftPosition
            if (r2 > r6) goto L_0x017c
            if (r0 <= 0) goto L_0x00ae
            r7.scrollBy(r1, r1)
            goto L_0x017c
        L_0x00ae:
            int r0 = java.lang.Math.abs(r0)
            int r2 = r7.mSlideTransverseLeftMaxWitch
            if (r0 < r2) goto L_0x00c1
            double r2 = (double) r3
            java.lang.Double.isNaN(r2)
            double r2 = r2 / r4
            int r0 = (int) r2
            r7.scrollBy(r0, r1)
            goto L_0x017c
        L_0x00c1:
            r7.scrollBy(r3, r1)
            goto L_0x017c
        L_0x00c6:
            boolean r0 = r7.mIsHandledTouchEvent
            r3 = 0
            if (r0 == 0) goto L_0x0163
            r7.mIsHandledTouchEvent = r1
            boolean r0 = r7.mCanDoSlideTransverseEvent
            if (r0 == 0) goto L_0x0163
            r7.requestDisallowInterceptTouchEvent(r1)
            android.view.VelocityTracker r0 = r7.mVelocityTracker
            r4 = 1000(0x3e8, float:1.401E-42)
            r0.computeCurrentVelocity(r4)
            float r0 = r0.getXVelocity()
            int r0 = (int) r0
            int r5 = r7.getScrollX()
            if (r0 <= r4) goto L_0x011d
            if (r5 >= 0) goto L_0x0105
            boolean r0 = r7.isLeftSlide
            if (r0 == 0) goto L_0x0105
            int r0 = r7.mSlideLeftPosition
            int r4 = r7.mSlideTransverseLeftMaxWitch
            if (r0 < r4) goto L_0x0105
            int r0 = java.lang.Math.abs(r5)
            int r4 = r4 - r0
            int r0 = -r4
            r7.smoothScrollTo(r0, r1)
            r7.isSlideOpen = r2
            java.lang.String r0 = LEFT
            java.lang.String r1 = AFTER_SLIDE
            r7.setState(r0, r1)
            goto L_0x015a
        L_0x0105:
            if (r5 <= 0) goto L_0x0119
            boolean r0 = r7.isRightSlide
            if (r0 == 0) goto L_0x0119
            int r0 = -r5
            r7.smoothScrollTo(r0, r1)
            r7.isSlideOpen = r1
            java.lang.String r0 = RIGHT
            java.lang.String r1 = BEFORE_SLIDE
            r7.setState(r0, r1)
            goto L_0x015a
        L_0x0119:
            r7.upSlideTo(r5)
            goto L_0x015a
        L_0x011d:
            r4 = -1000(0xfffffffffffffc18, float:NaN)
            if (r0 >= r4) goto L_0x0157
            if (r5 >= 0) goto L_0x0135
            boolean r0 = r7.isLeftSlide
            if (r0 == 0) goto L_0x0135
            int r0 = -r5
            r7.smoothScrollTo(r0, r1)
            r7.isSlideOpen = r1
            java.lang.String r0 = LEFT
            java.lang.String r1 = BEFORE_SLIDE
            r7.setState(r0, r1)
            goto L_0x015a
        L_0x0135:
            if (r5 <= 0) goto L_0x0153
            boolean r0 = r7.isRightSlide
            if (r0 == 0) goto L_0x0153
            int r0 = r7.mSlideRightPosition
            int r4 = r7.mSlideTransverseRightMaxWitch
            if (r0 < r4) goto L_0x0153
            int r0 = java.lang.Math.abs(r5)
            int r4 = r4 - r0
            r7.smoothScrollTo(r4, r1)
            r7.isSlideOpen = r2
            java.lang.String r0 = RIGHT
            java.lang.String r1 = AFTER_SLIDE
            r7.setState(r0, r1)
            goto L_0x015a
        L_0x0153:
            r7.upSlideTo(r5)
            goto L_0x015a
        L_0x0157:
            r7.upSlideTo(r5)
        L_0x015a:
            android.view.VelocityTracker r0 = r7.mVelocityTracker
            if (r0 == 0) goto L_0x0163
            r0.recycle()
            r7.mVelocityTracker = r3
        L_0x0163:
            android.view.VelocityTracker r0 = r7.mVelocityTracker
            if (r0 == 0) goto L_0x017c
            r0.recycle()
            r7.mVelocityTracker = r3
            goto L_0x017c
        L_0x016d:
            r7.mIsHandledTouchEvent = r1
            android.widget.Scroller r0 = r7.mScroller
            boolean r0 = r0.isFinished()
            if (r0 != 0) goto L_0x017c
            android.widget.Scroller r0 = r7.mScroller
            r0.abortAnimation()
        L_0x017c:
            boolean r8 = super.onTouchEvent(r8)
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dcloud.android.widget.SlideLayout.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public void reset() {
        int scrollX = getScrollX();
        if (scrollX != 0) {
            smoothScrollTo(-scrollX, 0);
            if (scrollX < 0) {
                setState(LEFT, BEFORE_SLIDE);
            } else {
                setState(RIGHT, BEFORE_SLIDE);
            }
        }
    }

    public void setHeight(int i) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            layoutParams.height = i;
            requestLayout();
        }
    }

    public void setInterceptTouchEventEnabled(boolean z) {
        this.mIsHandledTouchEvent = z;
    }

    public void setOffset(String str, String str2, float f) {
        int convertToScreenInt = PdrUtil.convertToScreenInt(str2, getWidth(), 0, f);
        int scrollX = getScrollX();
        if (str.equals(LEFT)) {
            if (convertToScreenInt != 0) {
                int i = this.mSlideLeftPosition;
                if (convertToScreenInt > i) {
                    convertToScreenInt = i;
                }
                int abs = convertToScreenInt - Math.abs(scrollX);
                smoothScrollTo(-abs, 0);
                postDelayed(new Runnable() {
                    public void run() {
                        SlideLayout slideLayout = SlideLayout.this;
                        slideLayout.upSlideTo(slideLayout.getScrollX());
                    }
                }, (long) ((abs * 2) + 200));
            } else if (scrollX != 0) {
                smoothScrollTo(-scrollX, 0);
                setState(LEFT, BEFORE_SLIDE);
            }
        } else if (convertToScreenInt != 0) {
            int i2 = this.mSlideRightPosition;
            if (convertToScreenInt > i2) {
                convertToScreenInt = i2;
            }
            int abs2 = convertToScreenInt - Math.abs(scrollX);
            smoothScrollTo(abs2, 0);
            postDelayed(new Runnable() {
                public void run() {
                    SlideLayout slideLayout = SlideLayout.this;
                    slideLayout.upSlideTo(slideLayout.getScrollX());
                }
            }, (long) ((abs2 * 2) + 200));
        } else if (scrollX != 0) {
            smoothScrollTo(-scrollX, 0);
            setState(RIGHT, BEFORE_SLIDE);
        }
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.mChangeListener = onStateChangeListener;
    }

    public void setWidth(int i) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = i;
            requestLayout();
        }
    }

    public void upSlideTo(int i) {
        if (i < 0) {
            int abs = Math.abs(i);
            int i2 = this.mSlideTransverseLeftMaxWitch;
            if (abs >= i2 / 2 && this.mSlideLeftPosition >= i2) {
                smoothScrollTo(-(i2 - Math.abs(i)), 0);
                this.isSlideOpen = true;
                setState(LEFT, AFTER_SLIDE);
                return;
            }
        }
        if (i > 0) {
            int abs2 = Math.abs(i);
            int i3 = this.mSlideTransverseRightMaxWitch;
            if (abs2 >= i3 / 2 && this.mSlideRightPosition >= i3) {
                smoothScrollTo(i3 - Math.abs(i), 0);
                this.isSlideOpen = true;
                setState(RIGHT, AFTER_SLIDE);
                return;
            }
        }
        if (i > 0) {
            smoothScrollTo(-i, 0);
            setState(RIGHT, BEFORE_SLIDE);
        } else {
            smoothScrollTo(-i, 0);
            setState(LEFT, BEFORE_SLIDE);
        }
        this.isSlideOpen = false;
    }
}
