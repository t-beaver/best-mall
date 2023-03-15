package com.taobao.weex.ui.component;

import android.content.Context;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.viewpager.widget.ViewPager;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.WXEvent;
import com.taobao.weex.ui.ComponentCreator;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.list.template.TemplateDom;
import com.taobao.weex.ui.view.BaseFrameLayout;
import com.taobao.weex.ui.view.WXCircleIndicator;
import com.taobao.weex.ui.view.WXCirclePageAdapter;
import com.taobao.weex.ui.view.WXCircleViewPager;
import com.taobao.weex.ui.view.gesture.WXGestureType;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Component(lazyload = false)
public class WXSlider extends WXVContainer<FrameLayout> {
    public static final String INDEX = "index";
    public static final String INFINITE = "infinite";
    /* access modifiers changed from: private */
    public int initIndex;
    /* access modifiers changed from: private */
    public Runnable initRunnable;
    private boolean isInfinite;
    private boolean keepIndex;
    protected WXCirclePageAdapter mAdapter;
    protected WXIndicator mIndicator;
    protected ViewPager.OnPageChangeListener mPageChangeListener;
    protected boolean mShowIndicators;
    WXCircleViewPager mViewPager;
    /* access modifiers changed from: private */
    public float offsetXAccuracy;
    Map<String, Object> params;

    public static class Creator implements ComponentCreator {
        public WXComponent createInstance(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) throws IllegalAccessException, InvocationTargetException, InstantiationException {
            return new WXSlider(wXSDKInstance, wXVContainer, basicComponentData);
        }
    }

    @Deprecated
    public WXSlider(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, String str, boolean z, BasicComponentData basicComponentData) {
        this(wXSDKInstance, wXVContainer, basicComponentData);
    }

    public WXSlider(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
        this.isInfinite = true;
        this.params = new HashMap();
        this.offsetXAccuracy = 0.1f;
        this.initIndex = -1;
        this.keepIndex = false;
        this.mPageChangeListener = new SliderPageChangeListener();
    }

    /* access modifiers changed from: protected */
    public BaseFrameLayout initComponentHostView(Context context) {
        BaseFrameLayout baseFrameLayout = new BaseFrameLayout(context);
        if (getAttrs() != null) {
            this.isInfinite = WXUtils.getBoolean(getAttrs().get("infinite"), true).booleanValue();
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        WXCircleViewPager wXCircleViewPager = new WXCircleViewPager(context);
        this.mViewPager = wXCircleViewPager;
        wXCircleViewPager.setCircle(this.isInfinite);
        this.mViewPager.setLayoutParams(layoutParams);
        WXCirclePageAdapter wXCirclePageAdapter = new WXCirclePageAdapter(this.isInfinite);
        this.mAdapter = wXCirclePageAdapter;
        this.mViewPager.setAdapter(wXCirclePageAdapter);
        baseFrameLayout.addView(this.mViewPager);
        this.mViewPager.addOnPageChangeListener(this.mPageChangeListener);
        registerActivityStateListener();
        return baseFrameLayout;
    }

    public ViewGroup.LayoutParams getChildLayoutParams(WXComponent wXComponent, View view, int i, int i2, int i3, int i4, int i5, int i6) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new FrameLayout.LayoutParams(i, i2);
        } else {
            layoutParams.width = i;
            layoutParams.height = i2;
        }
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            if (wXComponent instanceof WXIndicator) {
                setMarginsSupportRTL((ViewGroup.MarginLayoutParams) layoutParams, i3, i5, i4, i6);
            } else {
                setMarginsSupportRTL((ViewGroup.MarginLayoutParams) layoutParams, 0, 0, 0, 0);
            }
        }
        return layoutParams;
    }

    public void addEvent(String str) {
        WXCircleViewPager wXCircleViewPager;
        super.addEvent(str);
        if ("scroll".equals(str) && (wXCircleViewPager = this.mViewPager) != null) {
            wXCircleViewPager.addOnPageChangeListener(new SliderOnScrollListener(this));
        }
    }

    public boolean containsGesture(WXGestureType wXGestureType) {
        return super.containsGesture(wXGestureType);
    }

    public ViewGroup getRealView() {
        return this.mViewPager;
    }

    public void addSubView(View view, int i) {
        WXCirclePageAdapter wXCirclePageAdapter;
        if (view != null && (wXCirclePageAdapter = this.mAdapter) != null && !(view instanceof WXCircleIndicator)) {
            wXCirclePageAdapter.addPageView(view);
            hackTwoItemsInfiniteScroll();
            if (this.initIndex != -1 && this.mAdapter.getRealCount() > this.initIndex) {
                if (this.initRunnable == null) {
                    this.initRunnable = new Runnable() {
                        public void run() {
                            WXSlider wXSlider = WXSlider.this;
                            int unused = wXSlider.initIndex = wXSlider.getInitIndex();
                            WXCircleViewPager wXCircleViewPager = WXSlider.this.mViewPager;
                            WXSlider wXSlider2 = WXSlider.this;
                            wXCircleViewPager.setCurrentItem(wXSlider2.getRealIndex(wXSlider2.initIndex));
                            int unused2 = WXSlider.this.initIndex = -1;
                            Runnable unused3 = WXSlider.this.initRunnable = null;
                        }
                    };
                }
                this.mViewPager.removeCallbacks(this.initRunnable);
                this.mViewPager.postDelayed(this.initRunnable, 50);
            } else if (!this.keepIndex) {
                this.mViewPager.setCurrentItem(getRealIndex(0));
            }
            WXIndicator wXIndicator = this.mIndicator;
            if (wXIndicator != null) {
                ((WXCircleIndicator) wXIndicator.getHostView()).forceLayout();
                ((WXCircleIndicator) this.mIndicator.getHostView()).requestLayout();
            }
        }
    }

    public void setLayout(WXComponent wXComponent) {
        WXCirclePageAdapter wXCirclePageAdapter = this.mAdapter;
        if (wXCirclePageAdapter != null) {
            wXCirclePageAdapter.setLayoutDirectionRTL(isLayoutRTL());
        }
        super.setLayout(wXComponent);
    }

    public void remove(WXComponent wXComponent, boolean z) {
        WXCirclePageAdapter wXCirclePageAdapter;
        if (wXComponent != null && wXComponent.getHostView() != null && (wXCirclePageAdapter = this.mAdapter) != null) {
            wXCirclePageAdapter.removePageView(wXComponent.getHostView());
            hackTwoItemsInfiniteScroll();
            super.remove(wXComponent, z);
        }
    }

    public void destroy() {
        super.destroy();
        WXCircleViewPager wXCircleViewPager = this.mViewPager;
        if (wXCircleViewPager != null) {
            wXCircleViewPager.stopAutoScroll();
            this.mViewPager.removeAllViews();
            this.mViewPager.destory();
        }
    }

    public void onActivityResume() {
        super.onActivityResume();
        WXCircleViewPager wXCircleViewPager = this.mViewPager;
        if (wXCircleViewPager != null && wXCircleViewPager.isAutoScroll()) {
            this.mViewPager.startAutoScroll();
        }
    }

    public void onActivityStop() {
        super.onActivityStop();
        WXCircleViewPager wXCircleViewPager = this.mViewPager;
        if (wXCircleViewPager != null) {
            wXCircleViewPager.pauseAutoScroll();
        }
    }

    public void addIndicator(WXIndicator wXIndicator) {
        FrameLayout frameLayout = (FrameLayout) getHostView();
        if (frameLayout != null) {
            this.mIndicator = wXIndicator;
            WXCircleIndicator wXCircleIndicator = (WXCircleIndicator) wXIndicator.getHostView();
            if (wXCircleIndicator != null) {
                wXCircleIndicator.setCircleViewPager(this.mViewPager);
                frameLayout.addView(wXCircleIndicator);
            }
        }
    }

    /* access modifiers changed from: private */
    public int getInitIndex() {
        int intValue = WXUtils.getInteger(getAttrs().get("index"), Integer.valueOf(this.initIndex)).intValue();
        WXCirclePageAdapter wXCirclePageAdapter = this.mAdapter;
        if (wXCirclePageAdapter == null || wXCirclePageAdapter.getCount() == 0) {
            return 0;
        }
        return intValue >= this.mAdapter.getRealCount() ? intValue % this.mAdapter.getRealCount() : intValue;
    }

    /* access modifiers changed from: private */
    public int getRealIndex(int i) {
        if (this.mAdapter.getRealCount() > 0) {
            if (i >= this.mAdapter.getRealCount()) {
                i = this.mAdapter.getRealCount() - 1;
            }
            if (isLayoutRTL()) {
                i = (this.mAdapter.getRealCount() - 1) - i;
            }
        }
        return i + 0;
    }

    /* access modifiers changed from: protected */
    public boolean setProperty(String str, Object obj) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1768064947:
                if (str.equals(Constants.Name.KEEP_INDEX)) {
                    c = 0;
                    break;
                }
                break;
            case 66669991:
                if (str.equals(Constants.Name.SCROLLABLE)) {
                    c = 1;
                    break;
                }
                break;
            case 100346066:
                if (str.equals("index")) {
                    c = 2;
                    break;
                }
                break;
            case 111972721:
                if (str.equals("value")) {
                    c = 3;
                    break;
                }
                break;
            case 570418373:
                if (str.equals("interval")) {
                    c = 4;
                    break;
                }
                break;
            case 996926241:
                if (str.equals(Constants.Name.SHOW_INDICATORS)) {
                    c = 5;
                    break;
                }
                break;
            case 1438608771:
                if (str.equals(Constants.Name.AUTO_PLAY)) {
                    c = 6;
                    break;
                }
                break;
            case 1565939262:
                if (str.equals(Constants.Name.OFFSET_X_ACCURACY)) {
                    c = 7;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                this.keepIndex = WXUtils.getBoolean(obj, false).booleanValue();
                return true;
            case 1:
                setScrollable(WXUtils.getBoolean(obj, true).booleanValue());
                return true;
            case 2:
                Integer integer = WXUtils.getInteger(obj, (Integer) null);
                if (integer != null) {
                    setIndex(integer.intValue());
                }
                return true;
            case 3:
                String string = WXUtils.getString(obj, (String) null);
                if (string != null) {
                    setValue(string);
                }
                return true;
            case 4:
                Integer integer2 = WXUtils.getInteger(obj, (Integer) null);
                if (integer2 != null) {
                    setInterval(integer2.intValue());
                }
                return true;
            case 5:
                String string2 = WXUtils.getString(obj, (String) null);
                if (string2 != null) {
                    setShowIndicators(string2);
                }
                return true;
            case 6:
                String string3 = WXUtils.getString(obj, (String) null);
                if (string3 != null) {
                    setAutoPlay(string3);
                }
                return true;
            case 7:
                Float f = WXUtils.getFloat(obj, Float.valueOf(0.1f));
                if (f.floatValue() != 0.0f) {
                    setOffsetXAccuracy(f.floatValue());
                }
                return true;
            default:
                return super.setProperty(str, obj);
        }
    }

    @WXComponentProp(name = "value")
    @Deprecated
    public void setValue(String str) {
        if (str != null && getHostView() != null) {
            try {
                setIndex(Integer.parseInt(str));
            } catch (NumberFormatException e) {
                WXLogUtils.e("", (Throwable) e);
            }
        }
    }

    @WXComponentProp(name = "autoPlay")
    public void setAutoPlay(String str) {
        if (TextUtils.isEmpty(str) || str.equals(AbsoluteConst.FALSE)) {
            this.mViewPager.stopAutoScroll();
            return;
        }
        this.mViewPager.stopAutoScroll();
        this.mViewPager.startAutoScroll();
    }

    @WXComponentProp(name = "showIndicators")
    public void setShowIndicators(String str) {
        if (TextUtils.isEmpty(str) || str.equals(AbsoluteConst.FALSE)) {
            this.mShowIndicators = false;
        } else {
            this.mShowIndicators = true;
        }
        WXIndicator wXIndicator = this.mIndicator;
        if (wXIndicator != null) {
            wXIndicator.setShowIndicators(this.mShowIndicators);
        }
    }

    @WXComponentProp(name = "interval")
    public void setInterval(int i) {
        WXCircleViewPager wXCircleViewPager = this.mViewPager;
        if (wXCircleViewPager != null && i > 0) {
            wXCircleViewPager.setIntervalTime((long) i);
        }
    }

    @WXComponentProp(name = "index")
    public void setIndex(int i) {
        WXCirclePageAdapter wXCirclePageAdapter;
        WXCirclePageAdapter wXCirclePageAdapter2;
        if (this.mViewPager != null && (wXCirclePageAdapter = this.mAdapter) != null) {
            if (i >= wXCirclePageAdapter.getRealCount() || i < 0) {
                this.initIndex = i;
                return;
            }
            int realIndex = getRealIndex(i);
            this.mViewPager.setCurrentItem(realIndex);
            WXIndicator wXIndicator = this.mIndicator;
            if (wXIndicator != null && wXIndicator.getHostView() != null && ((WXCircleIndicator) this.mIndicator.getHostView()).getRealCurrentItem() != realIndex) {
                WXLogUtils.d("setIndex >>>> correction indicator to " + realIndex);
                ((WXCircleIndicator) this.mIndicator.getHostView()).setRealCurrentItem(realIndex);
                ((WXCircleIndicator) this.mIndicator.getHostView()).invalidate();
                ViewPager.OnPageChangeListener onPageChangeListener = this.mPageChangeListener;
                if (onPageChangeListener != null && (wXCirclePageAdapter2 = this.mAdapter) != null) {
                    onPageChangeListener.onPageSelected(wXCirclePageAdapter2.getFirst() + realIndex);
                }
            }
        }
    }

    @WXComponentProp(name = "scrollable")
    public void setScrollable(boolean z) {
        WXCircleViewPager wXCircleViewPager = this.mViewPager;
        if (wXCircleViewPager != null && this.mAdapter != null) {
            wXCircleViewPager.setScrollable(z);
        }
    }

    @WXComponentProp(name = "offsetXAccuracy")
    public void setOffsetXAccuracy(float f) {
        this.offsetXAccuracy = f;
    }

    protected class SliderPageChangeListener implements ViewPager.OnPageChangeListener {
        private int lastPos = -1;

        public void onPageScrolled(int i, float f, int i2) {
        }

        protected SliderPageChangeListener() {
        }

        public void onPageSelected(int i) {
            if (WXSlider.this.mAdapter.getRealPosition(i) != this.lastPos) {
                if (WXEnvironment.isApkDebugable()) {
                    WXLogUtils.d("onPageSelected >>>>" + WXSlider.this.mAdapter.getRealPosition(i) + " lastPos: " + this.lastPos);
                }
                if (WXSlider.this.mAdapter != null && WXSlider.this.mAdapter.getRealCount() != 0) {
                    int realPosition = WXSlider.this.mAdapter.getRealPosition(i);
                    if (WXSlider.this.mChildren != null && realPosition < WXSlider.this.mChildren.size() && WXSlider.this.getEvents().size() != 0) {
                        WXEvent events = WXSlider.this.getEvents();
                        String ref = WXSlider.this.getRef();
                        if (events.contains(Constants.Event.CHANGE) && WXViewUtils.onScreenArea(WXSlider.this.getHostView())) {
                            WXSlider.this.params.put("index", Integer.valueOf(realPosition));
                            HashMap hashMap = new HashMap();
                            HashMap hashMap2 = new HashMap();
                            hashMap2.put("index", Integer.valueOf(realPosition));
                            hashMap.put(TemplateDom.KEY_ATTRS, hashMap2);
                            WXSDKManager.getInstance().fireEvent(WXSlider.this.getInstanceId(), ref, Constants.Event.CHANGE, WXSlider.this.params, hashMap);
                        }
                        WXSlider.this.mViewPager.requestLayout();
                        ((FrameLayout) WXSlider.this.getHostView()).invalidate();
                        this.lastPos = WXSlider.this.mAdapter.getRealPosition(i);
                    }
                }
            }
        }

        public void onPageScrollStateChanged(int i) {
            FrameLayout frameLayout = (FrameLayout) WXSlider.this.getHostView();
            if (frameLayout != null) {
                frameLayout.invalidate();
            }
        }
    }

    protected static class SliderOnScrollListener implements ViewPager.OnPageChangeListener {
        private float lastPositionOffset = 99.0f;
        private int selectedPosition;
        private WXSlider target;

        public SliderOnScrollListener(WXSlider wXSlider) {
            this.target = wXSlider;
            this.selectedPosition = wXSlider.mViewPager.superGetCurrentItem();
        }

        public void onPageScrolled(int i, float f, int i2) {
            float f2 = this.lastPositionOffset;
            if (f2 == 99.0f) {
                this.lastPositionOffset = f;
            } else if (Math.abs(f - f2) >= this.target.offsetXAccuracy) {
                int i3 = this.selectedPosition;
                if (i == i3) {
                    HashMap hashMap = new HashMap(1);
                    hashMap.put(Constants.Name.OFFSET_X_RATIO, Float.valueOf(-f));
                    this.target.fireEvent("scroll", hashMap);
                } else if (i < i3) {
                    HashMap hashMap2 = new HashMap(1);
                    hashMap2.put(Constants.Name.OFFSET_X_RATIO, Float.valueOf(1.0f - f));
                    this.target.fireEvent("scroll", hashMap2);
                }
                this.lastPositionOffset = f;
            }
        }

        public void onPageSelected(int i) {
            this.selectedPosition = i;
        }

        public void onPageScrollStateChanged(int i) {
            if (i == 0) {
                this.lastPositionOffset = 99.0f;
                this.target.fireEvent(Constants.Event.SCROLL_END);
            } else if (i == 1) {
                this.target.fireEvent(Constants.Event.SCROLL_START);
            }
        }
    }

    private void hackTwoItemsInfiniteScroll() {
        WXCirclePageAdapter wXCirclePageAdapter;
        if (this.mViewPager != null && (wXCirclePageAdapter = this.mAdapter) != null && this.isInfinite) {
            if (wXCirclePageAdapter.getRealCount() == 2) {
                final GestureDetector gestureDetector = new GestureDetector(getContext(), new FlingGestureListener(this.mViewPager));
                this.mViewPager.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return gestureDetector.onTouchEvent(motionEvent);
                    }
                });
                return;
            }
            this.mViewPager.setOnTouchListener((View.OnTouchListener) null);
        }
    }

    private static class FlingGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_MAX_OFF_PATH = WXViewUtils.dip2px(250.0f);
        private static final int SWIPE_MIN_DISTANCE = WXViewUtils.dip2px(50.0f);
        private static final int SWIPE_THRESHOLD_VELOCITY = WXViewUtils.dip2px(200.0f);
        private WeakReference<WXCircleViewPager> pagerRef;

        FlingGestureListener(WXCircleViewPager wXCircleViewPager) {
            this.pagerRef = new WeakReference<>(wXCircleViewPager);
        }

        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            WXCircleViewPager wXCircleViewPager = (WXCircleViewPager) this.pagerRef.get();
            if (wXCircleViewPager == null) {
                return false;
            }
            try {
                if (Math.abs(motionEvent.getY() - motionEvent2.getY()) > ((float) SWIPE_MAX_OFF_PATH)) {
                    return false;
                }
                float x = motionEvent.getX() - motionEvent2.getX();
                int i = SWIPE_MIN_DISTANCE;
                if (x <= ((float) i) || Math.abs(f) <= ((float) SWIPE_THRESHOLD_VELOCITY) || wXCircleViewPager.superGetCurrentItem() != 1) {
                    if (motionEvent2.getX() - motionEvent.getX() > ((float) i) && Math.abs(f) > ((float) SWIPE_THRESHOLD_VELOCITY) && wXCircleViewPager.superGetCurrentItem() == 0) {
                        wXCircleViewPager.setCurrentItem(1, false);
                        return true;
                    }
                    return false;
                }
                wXCircleViewPager.setCurrentItem(0, false);
                return true;
            } catch (Exception unused) {
            }
        }
    }
}
