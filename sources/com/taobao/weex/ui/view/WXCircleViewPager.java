package com.taobao.weex.ui.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import androidx.viewpager.widget.ViewPager;
import com.taobao.weex.ui.view.gesture.WXGesture;
import com.taobao.weex.ui.view.gesture.WXGestureObservable;
import com.taobao.weex.utils.WXLogUtils;
import java.lang.reflect.Field;

public class WXCircleViewPager extends ViewPager implements WXGestureObservable {
    private final int SCROLL_TO_NEXT = 1;
    /* access modifiers changed from: private */
    public long intervalTime = 3000;
    private boolean isAutoScroll;
    private Handler mAutoScrollHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            if (message.what == 1) {
                WXLogUtils.d("[CircleViewPager] trigger auto play action");
                WXCircleViewPager.this.showNextItem();
                sendEmptyMessageDelayed(1, WXCircleViewPager.this.intervalTime);
                return;
            }
            super.handleMessage(message);
        }
    };
    private WXSmoothScroller mScroller;
    /* access modifiers changed from: private */
    public int mState = 0;
    /* access modifiers changed from: private */
    public boolean needLoop = true;
    private boolean scrollable = true;
    private WXGesture wxGesture;

    public WXCircleViewPager(Context context) {
        super(context);
        init();
    }

    private void init() {
        setOverScrollMode(2);
        addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int i, float f, int i2) {
            }

            public void onPageSelected(int i) {
            }

            public void onPageScrollStateChanged(int i) {
                int unused = WXCircleViewPager.this.mState = i;
                WXCirclePageAdapter circlePageAdapter = WXCircleViewPager.this.getCirclePageAdapter();
                int access$301 = WXCircleViewPager.super.getCurrentItem();
                if (WXCircleViewPager.this.needLoop && i == 0 && circlePageAdapter.getCount() > 1) {
                    if (access$301 == circlePageAdapter.getCount() - 1) {
                        WXCircleViewPager.this.superSetCurrentItem(1, false);
                        circlePageAdapter.getViews().get(0).setTranslationY(0.0f);
                        circlePageAdapter.getViews().get(0).setTranslationX(0.0f);
                    } else if (access$301 == 0) {
                        WXCircleViewPager.this.superSetCurrentItem(circlePageAdapter.getCount() - 2, false);
                        try {
                            circlePageAdapter.getViews().get((circlePageAdapter.getCount() - 2) - 1).setTranslationY(0.0f);
                            circlePageAdapter.getViews().get((circlePageAdapter.getCount() - 2) - 1).setTranslationX(0.0f);
                        } catch (Exception unused2) {
                        }
                    }
                }
            }
        });
        postInitViewPager();
    }

    private void postInitViewPager() {
        if (!isInEditMode()) {
            try {
                Field declaredField = ViewPager.class.getDeclaredField("mScroller");
                declaredField.setAccessible(true);
                Field declaredField2 = ViewPager.class.getDeclaredField("sInterpolator");
                declaredField2.setAccessible(true);
                WXSmoothScroller wXSmoothScroller = new WXSmoothScroller(getContext(), (Interpolator) declaredField2.get((Object) null));
                this.mScroller = wXSmoothScroller;
                declaredField.set(this, wXSmoothScroller);
            } catch (Exception e) {
                WXLogUtils.e("[CircleViewPager] postInitViewPager: ", (Throwable) e);
            }
        }
    }

    public WXSmoothScroller getmScroller() {
        return this.mScroller;
    }

    public WXCircleViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public int getCurrentItem() {
        return getRealCurrentItem();
    }

    public int superGetCurrentItem() {
        return super.getCurrentItem();
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        try {
            return this.scrollable && super.onInterceptTouchEvent(motionEvent);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        } catch (ArrayIndexOutOfBoundsException e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.scrollable) {
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    public void scrollTo(int i, int i2) {
        if (this.scrollable || this.mState != 1) {
            super.scrollTo(i, i2);
        }
    }

    public void startAutoScroll() {
        this.isAutoScroll = true;
        this.mAutoScrollHandler.removeCallbacksAndMessages((Object) null);
        this.mAutoScrollHandler.sendEmptyMessageDelayed(1, this.intervalTime);
    }

    public void pauseAutoScroll() {
        this.mAutoScrollHandler.removeCallbacksAndMessages((Object) null);
    }

    public void stopAutoScroll() {
        this.isAutoScroll = false;
        this.mAutoScrollHandler.removeCallbacksAndMessages((Object) null);
    }

    public boolean isAutoScroll() {
        return this.isAutoScroll;
    }

    public void setCurrentItem(int i) {
        setRealCurrentItem(i, true);
    }

    public void setCurrentItem(int i, boolean z) {
        setRealCurrentItem(i, z);
    }

    public WXCirclePageAdapter getCirclePageAdapter() {
        return (WXCirclePageAdapter) getAdapter();
    }

    public void setCirclePageAdapter(WXCirclePageAdapter wXCirclePageAdapter) {
        setAdapter(wXCirclePageAdapter);
    }

    public long getIntervalTime() {
        return this.intervalTime;
    }

    public void setIntervalTime(long j) {
        this.intervalTime = j;
    }

    public void setCircle(boolean z) {
        this.needLoop = z;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x000d, code lost:
        if (r0 != 3) goto L_0x0024;
     */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x002c A[Catch:{ Exception -> 0x0032 }] */
    /* JADX WARNING: Removed duplicated region for block: B:20:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean dispatchTouchEvent(android.view.MotionEvent r5) {
        /*
            r4 = this;
            int r0 = r5.getAction()
            if (r0 == 0) goto L_0x001e
            r1 = 1
            if (r0 == r1) goto L_0x0010
            r2 = 2
            if (r0 == r2) goto L_0x001e
            r2 = 3
            if (r0 == r2) goto L_0x0010
            goto L_0x0024
        L_0x0010:
            boolean r0 = r4.isAutoScroll()
            if (r0 == 0) goto L_0x0024
            android.os.Handler r0 = r4.mAutoScrollHandler
            long r2 = r4.intervalTime
            r0.sendEmptyMessageDelayed(r1, r2)
            goto L_0x0024
        L_0x001e:
            android.os.Handler r0 = r4.mAutoScrollHandler
            r1 = 0
            r0.removeCallbacksAndMessages(r1)
        L_0x0024:
            boolean r0 = super.dispatchTouchEvent(r5)     // Catch:{ Exception -> 0x0032 }
            com.taobao.weex.ui.view.gesture.WXGesture r1 = r4.wxGesture     // Catch:{ Exception -> 0x0032 }
            if (r1 == 0) goto L_0x0031
            boolean r5 = r1.onTouch(r4, r5)     // Catch:{ Exception -> 0x0032 }
            r0 = r0 | r5
        L_0x0031:
            return r0
        L_0x0032:
            r5 = 0
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.view.WXCircleViewPager.dispatchTouchEvent(android.view.MotionEvent):boolean");
    }

    public void destory() {
        this.mAutoScrollHandler.removeCallbacksAndMessages((Object) null);
    }

    public void registerGestureListener(WXGesture wXGesture) {
        this.wxGesture = wXGesture;
    }

    public WXGesture getGestureListener() {
        return this.wxGesture;
    }

    public int getRealCurrentItem() {
        return ((WXCirclePageAdapter) getAdapter()).getRealPosition(super.getCurrentItem());
    }

    private void setRealCurrentItem(int i, boolean z) {
        superSetCurrentItem(((WXCirclePageAdapter) getAdapter()).getFirst() + i, z);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x002c */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void superSetCurrentItem(int r3, boolean r4) {
        /*
            r2 = this;
            int r0 = r2.getRealCurrentItem()     // Catch:{ Exception -> 0x002c }
            int r0 = r3 - r0
            int r0 = java.lang.Math.abs(r0)     // Catch:{ Exception -> 0x002c }
            r1 = 3
            if (r0 <= r1) goto L_0x002c
            boolean r0 = r2.needLoop     // Catch:{ Exception -> 0x002c }
            if (r0 == 0) goto L_0x002c
            int r0 = r2.getRealCount()     // Catch:{ Exception -> 0x002c }
            r1 = 2
            if (r0 <= r1) goto L_0x002c
            com.taobao.weex.ui.view.WXCirclePageAdapter r0 = r2.getCirclePageAdapter()     // Catch:{ Exception -> 0x002c }
            int r0 = r0.getCount()     // Catch:{ Exception -> 0x002c }
            int r0 = r0 - r1
            if (r3 != r0) goto L_0x002c
            int r0 = r3 + -2
            r1 = 0
            super.setCurrentItem(r0, r1)     // Catch:{ Exception -> 0x002c }
            goto L_0x002c
        L_0x002a:
            r0 = move-exception
            goto L_0x0030
        L_0x002c:
            super.setCurrentItem(r3, r4)     // Catch:{ IllegalStateException -> 0x002a }
            goto L_0x0047
        L_0x0030:
            java.lang.String r0 = r0.toString()
            com.taobao.weex.utils.WXLogUtils.e(r0)
            androidx.viewpager.widget.PagerAdapter r0 = r2.getAdapter()
            if (r0 == 0) goto L_0x0047
            androidx.viewpager.widget.PagerAdapter r0 = r2.getAdapter()
            r0.notifyDataSetChanged()
            super.setCurrentItem(r3, r4)
        L_0x0047:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.view.WXCircleViewPager.superSetCurrentItem(int, boolean):void");
    }

    public int getRealCount() {
        return ((WXCirclePageAdapter) getAdapter()).getRealCount();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        try {
            super.onMeasure(i, i2);
        } catch (IllegalStateException e) {
            WXLogUtils.e(e.toString());
            if (getAdapter() != null) {
                getAdapter().notifyDataSetChanged();
                super.onMeasure(i, i2);
            }
        }
    }

    public boolean isScrollable() {
        return this.scrollable;
    }

    public void setScrollable(boolean z) {
        this.scrollable = z;
    }

    /* access modifiers changed from: private */
    public void showNextItem() {
        if (getCirclePageAdapter() == null || !getCirclePageAdapter().isRTL) {
            if (!this.needLoop && superGetCurrentItem() == getRealCount() - 1) {
                return;
            }
            if (getRealCount() == 2 && superGetCurrentItem() == 1) {
                superSetCurrentItem(0, true);
            } else {
                superSetCurrentItem(superGetCurrentItem() + 1, true);
            }
        } else if (!this.needLoop && superGetCurrentItem() == 0) {
        } else {
            if (getRealCount() == 2 && superGetCurrentItem() == 0) {
                superSetCurrentItem(1, true);
            } else {
                superSetCurrentItem(superGetCurrentItem() - 1, true);
            }
        }
    }
}
