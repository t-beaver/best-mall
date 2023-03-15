package io.dcloud.feature.weex.extend;

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
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXIndicator;
import com.taobao.weex.ui.component.WXVContainer;
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
public class DCWXSlider extends WXVContainer<FrameLayout> {
    public static final String INDEX = "index";
    public static final String INFINITE = "infinite";
    public static final String SOURCE = "source";
    /* access modifiers changed from: private */
    public int initIndex;
    /* access modifiers changed from: private */
    public Runnable initRunnable;
    protected boolean isDrag;
    private boolean isInfinite;
    private boolean keepIndex;
    protected WXCirclePageAdapter mAdapter;
    protected WXIndicator mIndicator;
    protected ViewPager.OnPageChangeListener mPageChangeListener;
    protected boolean mShowIndicators;
    DCWXCircleViewPager mViewPager;
    private float offsetXAccuracy;
    Map<String, Object> params;

    public static class Creator implements ComponentCreator {
        public WXComponent createInstance(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) throws IllegalAccessException, InvocationTargetException, InstantiationException {
            return new DCWXSlider(wXSDKInstance, wXVContainer, basicComponentData);
        }
    }

    @Deprecated
    public DCWXSlider(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, String str, boolean z, BasicComponentData basicComponentData) {
        this(wXSDKInstance, wXVContainer, basicComponentData);
    }

    public DCWXSlider(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
        this.isInfinite = true;
        this.params = new HashMap();
        this.offsetXAccuracy = 0.1f;
        this.initIndex = -1;
        this.keepIndex = true;
        this.mShowIndicators = true;
        this.mPageChangeListener = new SliderPageChangeListener();
        this.isDrag = false;
    }

    /* access modifiers changed from: protected */
    public BaseFrameLayout initComponentHostView(Context context) {
        BaseFrameLayout baseFrameLayout = new BaseFrameLayout(context);
        if (getAttrs() != null) {
            this.isInfinite = WXUtils.getBoolean(getAttrs().get("infinite"), true).booleanValue();
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        DCWXCircleViewPager dCWXCircleViewPager = new DCWXCircleViewPager(context);
        this.mViewPager = dCWXCircleViewPager;
        dCWXCircleViewPager.setCircle(this.isInfinite);
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
        DCWXCircleViewPager dCWXCircleViewPager;
        super.addEvent(str);
        if ("scroll".equals(str) && (dCWXCircleViewPager = this.mViewPager) != null) {
            dCWXCircleViewPager.addOnPageChangeListener(new SliderOnScrollListener(this));
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
                            DCWXSlider dCWXSlider = DCWXSlider.this;
                            int unused = dCWXSlider.initIndex = dCWXSlider.getInitIndex();
                            DCWXSlider dCWXSlider2 = DCWXSlider.this;
                            DCWXSlider.this.mViewPager.setCurrentItem(dCWXSlider2.getRealIndex(dCWXSlider2.initIndex));
                            int unused2 = DCWXSlider.this.initIndex = -1;
                            Runnable unused3 = DCWXSlider.this.initRunnable = null;
                        }
                    };
                }
                int initIndex2 = getInitIndex();
                this.initIndex = initIndex2;
                this.mViewPager.setCurrentItem(getRealIndex(initIndex2));
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
        DCWXCircleViewPager dCWXCircleViewPager = this.mViewPager;
        if (dCWXCircleViewPager != null) {
            dCWXCircleViewPager.stopAutoScroll();
            this.mViewPager.removeAllViews();
            this.mViewPager.destory();
        }
    }

    public void onActivityResume() {
        super.onActivityResume();
        DCWXCircleViewPager dCWXCircleViewPager = this.mViewPager;
        if (dCWXCircleViewPager != null && dCWXCircleViewPager.isAutoScroll()) {
            this.mViewPager.startAutoScroll();
        }
    }

    public void onActivityStop() {
        super.onActivityStop();
        DCWXCircleViewPager dCWXCircleViewPager = this.mViewPager;
        if (dCWXCircleViewPager != null) {
            dCWXCircleViewPager.pauseAutoScroll();
        }
    }

    public int getCurrentIndex() {
        DCWXCircleViewPager dCWXCircleViewPager = this.mViewPager;
        if (dCWXCircleViewPager != null) {
            return dCWXCircleViewPager.superGetCurrentItem();
        }
        return -1;
    }

    public void addIndicator(WXIndicator wXIndicator) {
        FrameLayout frameLayout = (FrameLayout) getHostView();
        if (frameLayout != null) {
            this.mIndicator = wXIndicator;
            wXIndicator.setShowIndicators(this.mShowIndicators);
            WXCircleIndicator wXCircleIndicator = (WXCircleIndicator) wXIndicator.getHostView();
            if (wXCircleIndicator != null) {
                wXCircleIndicator.setCircleViewPager(this.mViewPager);
                frameLayout.addView(wXCircleIndicator);
            }
        }
    }

    /* access modifiers changed from: private */
    public int getInitIndex() {
        Object obj = getAttrs().get("index");
        if (obj == null) {
            return 0;
        }
        int intValue = WXUtils.getInteger(obj, Integer.valueOf(this.initIndex)).intValue();
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

    @WXComponentProp(name = "vertical")
    public void setVertical(boolean z) {
        DCWXCircleViewPager dCWXCircleViewPager = this.mViewPager;
        if (dCWXCircleViewPager != null) {
            dCWXCircleViewPager.setVertical(z);
        }
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
        DCWXCircleViewPager dCWXCircleViewPager = this.mViewPager;
        if (dCWXCircleViewPager != null && i > 0) {
            dCWXCircleViewPager.setIntervalTime((long) i);
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
            if (this.mAdapter.getRealCount() == 3 && realIndex == 2 && this.mViewPager.getCurrentItem() == 0) {
                this.mViewPager.setCurrentItem(1, false);
            }
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
        DCWXCircleViewPager dCWXCircleViewPager = this.mViewPager;
        if (dCWXCircleViewPager != null && this.mAdapter != null) {
            dCWXCircleViewPager.setScrollable(z);
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
            if (DCWXSlider.this.mAdapter.getRealPosition(i) != this.lastPos) {
                if (WXEnvironment.isApkDebugable()) {
                    WXLogUtils.d("onPageSelected >>>>" + DCWXSlider.this.mAdapter.getRealPosition(i) + " lastPos: " + this.lastPos);
                }
                if (DCWXSlider.this.mAdapter != null && DCWXSlider.this.mAdapter.getRealCount() != 0) {
                    int realPosition = DCWXSlider.this.mAdapter.getRealPosition(i);
                    if (DCWXSlider.this.mChildren != null && realPosition < DCWXSlider.this.mChildren.size() && DCWXSlider.this.getEvents().size() != 0) {
                        WXEvent events = DCWXSlider.this.getEvents();
                        String ref = DCWXSlider.this.getRef();
                        if (events.contains(Constants.Event.CHANGE) && WXViewUtils.onScreenArea(DCWXSlider.this.getHostView())) {
                            DCWXSlider.this.params.put("index", Integer.valueOf(realPosition));
                            String str = "touch";
                            DCWXSlider.this.params.put("source", DCWXSlider.this.isDrag ? str : Constants.Name.AUTOPLAY);
                            HashMap hashMap = new HashMap();
                            HashMap hashMap2 = new HashMap();
                            hashMap2.put("index", Integer.valueOf(realPosition));
                            if (!DCWXSlider.this.isDrag) {
                                str = Constants.Name.AUTOPLAY;
                            }
                            hashMap2.put("source", str);
                            hashMap.put(TemplateDom.KEY_ATTRS, hashMap2);
                            DCWXSlider.this.getAttrs().putAll(hashMap2);
                            WXSDKManager.getInstance().fireEvent(DCWXSlider.this.getInstanceId(), ref, Constants.Event.CHANGE, DCWXSlider.this.params, hashMap);
                        }
                        DCWXSlider.this.mViewPager.requestLayout();
                        ((FrameLayout) DCWXSlider.this.getHostView()).invalidate();
                        this.lastPos = DCWXSlider.this.mAdapter.getRealPosition(i);
                    }
                }
            }
        }

        public void onPageScrollStateChanged(int i) {
            FrameLayout frameLayout = (FrameLayout) DCWXSlider.this.getHostView();
            if (frameLayout != null) {
                frameLayout.invalidate();
            }
        }
    }

    protected static class SliderOnScrollListener implements ViewPager.OnPageChangeListener {
        private float lastPositionOffset = 99.0f;
        private float lastValue = 0.0f;
        private boolean pageSelected = false;
        private int preScrollstate = 0;
        private int scrollState = 0;
        private int selectedPosition;
        private DCWXSlider target;

        public SliderOnScrollListener(DCWXSlider dCWXSlider) {
            this.target = dCWXSlider;
            this.selectedPosition = dCWXSlider.mViewPager.superGetCurrentItem();
        }

        /* JADX WARNING: Code restructure failed: missing block: B:31:0x0067, code lost:
            if (r9.pageSelected != false) goto L_0x006b;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:0x0093, code lost:
            if (r9.lastValue < 0.0f) goto L_0x0043;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:53:0x009d, code lost:
            if (r9.lastValue > 0.0f) goto L_0x0043;
         */
        /* JADX WARNING: Removed duplicated region for block: B:66:0x00dc  */
        /* JADX WARNING: Removed duplicated region for block: B:70:0x0105  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onPageScrolled(int r10, float r11, int r12) {
            /*
                r9 = this;
                float r12 = r9.lastPositionOffset
                r0 = 1120272384(0x42c60000, float:99.0)
                int r0 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1))
                if (r0 != 0) goto L_0x000d
                r9.lastPositionOffset = r11
                r9.lastValue = r11
                return
            L_0x000d:
                float r12 = r11 - r12
                r0 = 0
                int r1 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1))
                if (r1 != 0) goto L_0x0017
                r9.lastPositionOffset = r11
                return
            L_0x0017:
                int r1 = r9.scrollState
                r2 = 2
                r3 = 0
                r4 = 1
                if (r1 != r4) goto L_0x0045
                r9.pageSelected = r3
                int r12 = (r11 > r0 ? 1 : (r11 == r0 ? 0 : -1))
                if (r12 != 0) goto L_0x0025
                return
            L_0x0025:
                io.dcloud.feature.weex.extend.DCWXSlider r12 = r9.target
                io.dcloud.feature.weex.extend.DCWXCircleViewPager r12 = r12.mViewPager
                int r12 = r12.superGetCurrentItem()
                if (r10 != r12) goto L_0x0035
                float r10 = java.lang.Math.abs(r11)
                goto L_0x00a0
            L_0x0035:
                io.dcloud.feature.weex.extend.DCWXSlider r12 = r9.target
                io.dcloud.feature.weex.extend.DCWXCircleViewPager r12 = r12.mViewPager
                int r12 = r12.superGetCurrentItem()
                if (r10 >= r12) goto L_0x004a
                float r10 = java.lang.Math.abs(r11)
            L_0x0043:
                float r10 = -r10
                goto L_0x00a0
            L_0x0045:
                if (r1 == r2) goto L_0x004c
                if (r1 != 0) goto L_0x004a
                goto L_0x004c
            L_0x004a:
                r10 = r11
                goto L_0x00a0
            L_0x004c:
                r10 = -1
                int r1 = (r11 > r0 ? 1 : (r11 == r0 ? 0 : -1))
                if (r1 != 0) goto L_0x006d
                float r12 = java.lang.Math.abs(r12)
                double r5 = (double) r12
                r7 = 4606281698874543309(0x3feccccccccccccd, double:0.9)
                int r12 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
                if (r12 <= 0) goto L_0x0065
                boolean r10 = r9.pageSelected
                if (r10 == 0) goto L_0x006a
                r10 = 1
                goto L_0x006b
            L_0x0065:
                boolean r12 = r9.pageSelected
                if (r12 == 0) goto L_0x006a
                goto L_0x006b
            L_0x006a:
                r10 = 0
            L_0x006b:
                float r10 = (float) r10
                goto L_0x0083
            L_0x006d:
                int r12 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1))
                if (r12 <= 0) goto L_0x0076
                boolean r12 = r9.pageSelected
                if (r12 == 0) goto L_0x007c
                goto L_0x007b
            L_0x0076:
                boolean r12 = r9.pageSelected
                if (r12 == 0) goto L_0x007b
                goto L_0x007c
            L_0x007b:
                r10 = 1
            L_0x007c:
                float r10 = (float) r10
                float r12 = java.lang.Math.abs(r11)
                float r10 = r10 * r12
            L_0x0083:
                int r12 = r9.preScrollstate
                if (r12 != r2) goto L_0x00a0
                int r12 = r9.scrollState
                if (r12 != r2) goto L_0x00a0
                int r12 = (r10 > r0 ? 1 : (r10 == r0 ? 0 : -1))
                if (r12 <= 0) goto L_0x0095
                float r12 = r9.lastValue
                int r12 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1))
                if (r12 < 0) goto L_0x0043
            L_0x0095:
                int r12 = (r10 > r0 ? 1 : (r10 == r0 ? 0 : -1))
                if (r12 >= 0) goto L_0x00a0
                float r12 = r9.lastValue
                int r12 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1))
                if (r12 <= 0) goto L_0x00a0
                goto L_0x0043
            L_0x00a0:
                int r12 = (r10 > r0 ? 1 : (r10 == r0 ? 0 : -1))
                if (r12 >= 0) goto L_0x00ae
                r12 = -1082130432(0xffffffffbf800000, float:-1.0)
                int r12 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
                if (r12 <= 0) goto L_0x00ae
                r12 = 1065353216(0x3f800000, float:1.0)
                float r10 = r10 + r12
                float r10 = -r10
            L_0x00ae:
                int r12 = r9.preScrollstate
                if (r12 != r2) goto L_0x00c8
                int r12 = r9.scrollState
                if (r12 != r4) goto L_0x00c8
                io.dcloud.feature.weex.extend.DCWXSlider r12 = r9.target
                io.dcloud.feature.weex.extend.DCWXCircleViewPager r12 = r12.mViewPager
                com.taobao.weex.ui.view.WXSmoothScroller r12 = r12.getmScroller()
                r12.forceFinished(r4)
                io.dcloud.feature.weex.extend.DCWXSlider r12 = r9.target
                java.lang.String r0 = "scrollend"
                r12.fireEvent(r0)
            L_0x00c8:
                int r12 = r9.scrollState
                r9.preScrollstate = r12
                r9.lastValue = r10
                io.dcloud.feature.weex.extend.DCWXSlider r12 = r9.target
                io.dcloud.feature.weex.extend.DCWXCircleViewPager r12 = r12.mViewPager
                boolean r12 = r12.isVertical()
                java.lang.String r0 = "scroll"
                java.lang.String r1 = "drag"
                if (r12 == 0) goto L_0x0105
                java.util.HashMap r12 = new java.util.HashMap
                r12.<init>(r4)
                float r10 = -r10
                java.lang.Float r10 = java.lang.Float.valueOf(r10)
                java.lang.String r2 = "offsetYRatio"
                r12.put(r2, r10)
                io.dcloud.feature.weex.extend.DCWXSlider r10 = r9.target
                io.dcloud.feature.weex.extend.DCWXCircleViewPager r10 = r10.mViewPager
                int r10 = r10.getPointCounr()
                if (r10 <= 0) goto L_0x00f6
                r3 = 1
            L_0x00f6:
                java.lang.Boolean r10 = java.lang.Boolean.valueOf(r3)
                r12.put(r1, r10)
                io.dcloud.feature.weex.extend.DCWXSlider r10 = r9.target
                r10.fireEvent(r0, r12)
                r9.lastPositionOffset = r11
                goto L_0x012d
            L_0x0105:
                java.util.HashMap r12 = new java.util.HashMap
                r12.<init>(r4)
                float r10 = -r10
                java.lang.Float r10 = java.lang.Float.valueOf(r10)
                java.lang.String r2 = "offsetXRatio"
                r12.put(r2, r10)
                io.dcloud.feature.weex.extend.DCWXSlider r10 = r9.target
                io.dcloud.feature.weex.extend.DCWXCircleViewPager r10 = r10.mViewPager
                int r10 = r10.getPointCounr()
                if (r10 <= 0) goto L_0x011f
                r3 = 1
            L_0x011f:
                java.lang.Boolean r10 = java.lang.Boolean.valueOf(r3)
                r12.put(r1, r10)
                io.dcloud.feature.weex.extend.DCWXSlider r10 = r9.target
                r10.fireEvent(r0, r12)
                r9.lastPositionOffset = r11
            L_0x012d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.weex.extend.DCWXSlider.SliderOnScrollListener.onPageScrolled(int, float, int):void");
        }

        public void onPageSelected(int i) {
            this.pageSelected = true;
            this.selectedPosition = i;
        }

        public void onPageScrollStateChanged(int i) {
            this.scrollState = i;
            if (i == 0) {
                this.lastPositionOffset = 99.0f;
                this.lastValue = 99.0f;
                if (this.preScrollstate == 2) {
                    this.preScrollstate = 0;
                    this.target.isDrag = false;
                    this.target.fireEvent(Constants.Event.SCROLL_END);
                }
            } else if (i == 1) {
                this.target.isDrag = true;
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
