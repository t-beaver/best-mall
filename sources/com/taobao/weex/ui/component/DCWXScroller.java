package com.taobao.weex.ui.component;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import androidx.core.view.ViewCompat;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.WXThread;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.ui.ComponentCreator;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.helper.ScrollStartEndHelper;
import com.taobao.weex.ui.view.IWXScroller;
import com.taobao.weex.ui.view.WXBaseRefreshLayout;
import com.taobao.weex.ui.view.refresh.wrapper.BaseBounceView;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import io.dcloud.common.util.StringUtil;
import io.dcloud.feature.weex_scroller.helper.DCScrollStartEndHelper;
import io.dcloud.feature.weex_scroller.view.DCBounceScrollerView;
import io.dcloud.feature.weex_scroller.view.DCWXHorizontalScrollView;
import io.dcloud.feature.weex_scroller.view.DCWXScrollView;
import io.dcloud.feature.weex_scroller.view.WXStickyHelper;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Component(lazyload = false)
public class DCWXScroller extends WXBaseScroller implements DCWXScrollView.WXScrollViewListener, Scrollable {
    public static final String DIRECTION = "direction";
    private static final int SWIPE_MIN_DISTANCE = 5;
    private static final int SWIPE_THRESHOLD_VELOCITY = 300;
    private boolean canScroll2Top;
    Map<String, WXComponent> childens;
    /* access modifiers changed from: private */
    public Handler handler;
    private boolean isAnimation;
    private boolean isScrollable;
    /* access modifiers changed from: private */
    public AtomicBoolean isViewLayoutFinished;
    /* access modifiers changed from: private */
    public int mActiveFeature;
    private Map<String, AppearanceHelper> mAppearanceComponents;
    private int mChildrenLayoutOffset;
    private int mContentHeight;
    private int mContentWidth;
    private boolean mForceLoadmoreNextTime;
    /* access modifiers changed from: private */
    public GestureDetector mGestureDetector;
    private boolean mHasAddScrollEvent;
    /* access modifiers changed from: private */
    public boolean mIsHostAttachedToWindow;
    private Point mLastReport;
    private int mOffsetAccuracy;
    private View.OnAttachStateChangeListener mOnAttachStateChangeListener;
    protected int mOrientation;
    /* access modifiers changed from: private */
    public FrameLayout mRealView;
    private List<WXComponent> mRefreshs;
    private ScrollStartEndHelper mScrollStartEndHelper;
    private FrameLayout mScrollerView;
    private Map<String, Map<String, WXComponent>> mStickyMap;
    private boolean pageEnable;
    /* access modifiers changed from: private */
    public int pageSize;
    private WXStickyHelper stickyHelper;

    public void onScrollStopped(DCWXScrollView dCWXScrollView, int i, int i2) {
    }

    public void onScrollToBottom(DCWXScrollView dCWXScrollView, int i, int i2) {
    }

    public void onScrollToTop(DCWXScrollView dCWXScrollView, int i, int i2) {
    }

    public static class Creator implements ComponentCreator {
        public WXComponent createInstance(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) throws IllegalAccessException, InvocationTargetException, InstantiationException {
            wXSDKInstance.setUseScroller(true);
            return new DCWXScroller(wXSDKInstance, wXVContainer, basicComponentData);
        }
    }

    @Deprecated
    public DCWXScroller(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, String str, boolean z, BasicComponentData basicComponentData) {
        this(wXSDKInstance, wXVContainer, basicComponentData);
    }

    public DCWXScroller(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
        this.mOrientation = 1;
        this.mRefreshs = new ArrayList();
        this.mChildrenLayoutOffset = 0;
        this.mForceLoadmoreNextTime = false;
        this.mOffsetAccuracy = 10;
        this.mLastReport = new Point(-1, -1);
        this.mHasAddScrollEvent = false;
        this.mActiveFeature = 0;
        this.pageSize = 0;
        this.pageEnable = false;
        this.mIsHostAttachedToWindow = false;
        this.isAnimation = false;
        this.canScroll2Top = false;
        this.mAppearanceComponents = new HashMap();
        this.mStickyMap = new HashMap();
        this.mContentHeight = 0;
        this.mContentWidth = 0;
        this.handler = new Handler(Looper.getMainLooper());
        this.isScrollable = true;
        this.childens = new HashMap();
        this.isViewLayoutFinished = new AtomicBoolean(false);
        this.stickyHelper = new WXStickyHelper(this);
        wXSDKInstance.getApmForInstance().updateDiffStats(WXInstanceApm.KEY_PAGE_STATS_SCROLLER_NUM, 1.0d);
    }

    public ViewGroup getRealView() {
        return this.mScrollerView;
    }

    public void createViewImpl() {
        super.createViewImpl();
        for (int i = 0; i < this.mRefreshs.size(); i++) {
            WXComponent wXComponent = this.mRefreshs.get(i);
            wXComponent.createViewImpl();
            checkRefreshOrLoading(wXComponent);
        }
    }

    public ViewGroup getInnerView() {
        if (getHostView() == null) {
            return null;
        }
        if (getHostView() instanceof DCBounceScrollerView) {
            return (ViewGroup) ((DCBounceScrollerView) getHostView()).getInnerView();
        }
        return (ViewGroup) getHostView();
    }

    public void addEvent(String str) {
        super.addEvent(str);
        if (DCScrollStartEndHelper.isScrollEvent(str) && getInnerView() != null && !this.mHasAddScrollEvent) {
            this.mHasAddScrollEvent = true;
            if (getInnerView() instanceof DCWXScrollView) {
                ((DCWXScrollView) getInnerView()).addScrollViewListener(new DCWXScrollView.WXScrollViewListener() {
                    public void onScroll(DCWXScrollView dCWXScrollView, int i, int i2) {
                    }

                    public void onScrollChanged(DCWXScrollView dCWXScrollView, int i, int i2, int i3, int i4) {
                        DCWXScroller.this.getScrollStartEndHelper().onScrolled(i, i2);
                        if (DCWXScroller.this.getEvents().contains("scroll") && DCWXScroller.this.shouldReport(i, i2)) {
                            DCWXScroller.this.fireScrollEvent(dCWXScrollView.getContentFrame(), i, i2, i3, i4);
                        }
                    }

                    public void onScrollToBottom(DCWXScrollView dCWXScrollView, int i, int i2) {
                        HashMap hashMap = new HashMap(1);
                        hashMap.put("direction", "bottom");
                        HashMap hashMap2 = new HashMap(1);
                        hashMap2.put("detail", hashMap);
                        DCWXScroller.this.fireEvent("scrolltolower", hashMap2);
                    }

                    public void onScrollToTop(DCWXScrollView dCWXScrollView, int i, int i2) {
                        HashMap hashMap = new HashMap(1);
                        hashMap.put("direction", "top");
                        HashMap hashMap2 = new HashMap(1);
                        hashMap2.put("detail", hashMap);
                        DCWXScroller.this.fireEvent("scrolltoupper", hashMap2);
                    }

                    public void onScrollStopped(DCWXScrollView dCWXScrollView, int i, int i2) {
                        DCWXScroller.this.getScrollStartEndHelper().onScrolled(i, i2);
                        if (DCWXScroller.this.getEvents().contains("scroll")) {
                            DCWXScroller.this.fireScrollEvent(dCWXScrollView.getContentFrame(), i, i2, 0, 0);
                        }
                    }
                });
            } else if (getInnerView() instanceof DCWXHorizontalScrollView) {
                ((DCWXHorizontalScrollView) getInnerView()).addScrollViewListener(new DCWXHorizontalScrollView.ScrollViewListener() {
                    public void onScrollChanged(DCWXHorizontalScrollView dCWXHorizontalScrollView, int i, int i2, int i3, int i4) {
                        DCWXScroller.this.getScrollStartEndHelper().onScrolled(i, i2);
                        if (DCWXScroller.this.getEvents().contains("scroll") && DCWXScroller.this.shouldReport(i, i2)) {
                            DCWXScroller.this.fireScrollEvent(dCWXHorizontalScrollView.getContentFrame(), i, i2, i3, i4);
                        }
                    }

                    public void onScrolltoTop() {
                        HashMap hashMap = new HashMap(1);
                        hashMap.put("direction", "top");
                        HashMap hashMap2 = new HashMap(1);
                        hashMap2.put("detail", hashMap);
                        DCWXScroller.this.fireEvent("scrolltoupper", hashMap2);
                    }

                    public void onScrollToBottom() {
                        HashMap hashMap = new HashMap(1);
                        hashMap.put("direction", "bottom");
                        HashMap hashMap2 = new HashMap(1);
                        hashMap2.put("detail", hashMap);
                        DCWXScroller.this.fireEvent("scrolltolower", hashMap2);
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public void fireScrollEvent(Rect rect, int i, int i2, int i3, int i4) {
        fireEvent("scroll", getScrollEvent(i, i2, i3, i4));
    }

    public Map<String, Object> getScrollEvent(int i, int i2, int i3, int i4) {
        int i5;
        Rect rect = new Rect();
        int i6 = 0;
        if (getInnerView() instanceof DCWXScrollView) {
            rect = ((DCWXScrollView) getInnerView()).getContentFrame();
            i5 = getInnerView().getScrollY();
        } else {
            if (getInnerView() instanceof DCWXHorizontalScrollView) {
                rect = ((DCWXHorizontalScrollView) getInnerView()).getContentFrame();
                i6 = getInnerView().getScrollX();
            }
            i5 = 0;
        }
        HashMap hashMap = new HashMap(2);
        HashMap hashMap2 = new HashMap(6);
        new HashMap(2);
        float instanceViewPortWidthWithFloat = getInstance().getInstanceViewPortWidthWithFloat();
        hashMap2.put("scrollWidth", Float.valueOf(WXViewUtils.getWebPxByWidth((float) rect.width(), instanceViewPortWidthWithFloat)));
        hashMap2.put("scrollHeight", Float.valueOf(WXViewUtils.getWebPxByWidth((float) rect.height(), instanceViewPortWidthWithFloat)));
        hashMap2.put(Constants.Name.SCROLL_LEFT, Float.valueOf(WXViewUtils.getWebPxByWidth((float) i6, instanceViewPortWidthWithFloat)));
        hashMap2.put(Constants.Name.SCROLL_TOP, Float.valueOf(WXViewUtils.getWebPxByWidth((float) i5, instanceViewPortWidthWithFloat)));
        hashMap2.put("deltaX", Float.valueOf(WXViewUtils.getWebPxByWidth((float) i3, instanceViewPortWidthWithFloat)));
        hashMap2.put("deltaY", Float.valueOf(WXViewUtils.getWebPxByWidth((float) i4, instanceViewPortWidthWithFloat)));
        hashMap.put("detail", hashMap2);
        return hashMap;
    }

    public Map<String, Object> getScrollEvent(int i, int i2) {
        Rect rect = new Rect();
        if (getInnerView() instanceof DCWXScrollView) {
            rect = ((DCWXScrollView) getInnerView()).getContentFrame();
        } else if (getInnerView() instanceof DCWXHorizontalScrollView) {
            rect = ((DCWXHorizontalScrollView) getInnerView()).getContentFrame();
        }
        HashMap hashMap = new HashMap(2);
        HashMap hashMap2 = new HashMap(2);
        HashMap hashMap3 = new HashMap(2);
        float instanceViewPortWidthWithFloat = getInstance().getInstanceViewPortWidthWithFloat();
        hashMap2.put("width", Float.valueOf(WXViewUtils.getWebPxByWidth((float) rect.width(), instanceViewPortWidthWithFloat)));
        hashMap2.put("height", Float.valueOf(WXViewUtils.getWebPxByWidth((float) rect.height(), instanceViewPortWidthWithFloat)));
        hashMap3.put(Constants.Name.X, Float.valueOf(WXViewUtils.getWebPxByWidth((float) i, instanceViewPortWidthWithFloat)));
        hashMap3.put(Constants.Name.Y, Float.valueOf(WXViewUtils.getWebPxByWidth((float) i2, instanceViewPortWidthWithFloat)));
        hashMap.put(Constants.Name.CONTENT_SIZE, hashMap2);
        hashMap.put(Constants.Name.CONTENT_OFFSET, hashMap3);
        return hashMap;
    }

    /* access modifiers changed from: private */
    public boolean shouldReport(int i, int i2) {
        if (this.mLastReport.x == -1 && this.mLastReport.y == -1) {
            this.mLastReport.x = i;
            this.mLastReport.y = i2;
            return true;
        } else if (this.mOrientation == 0 && Math.abs(i - this.mLastReport.x) >= this.mOffsetAccuracy) {
            this.mLastReport.x = i;
            this.mLastReport.y = i2;
            return true;
        } else if (this.mOrientation != 1 || Math.abs(i2 - this.mLastReport.y) < this.mOffsetAccuracy) {
            return false;
        } else {
            this.mLastReport.x = i;
            this.mLastReport.y = i2;
            return true;
        }
    }

    public void addSubView(View view, int i) {
        FrameLayout frameLayout;
        if (view != null && (frameLayout = this.mRealView) != null && !(view instanceof WXBaseRefreshLayout)) {
            if (i >= frameLayout.getChildCount()) {
                i = -1;
            }
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
            if (i == -1) {
                this.mRealView.addView(view);
            } else {
                this.mRealView.addView(view, i);
            }
        }
    }

    /* access modifiers changed from: protected */
    public int getChildrenLayoutTopOffset() {
        int size;
        if (this.mChildrenLayoutOffset == 0 && (size = this.mRefreshs.size()) > 0) {
            for (int i = 0; i < size; i++) {
                this.mChildrenLayoutOffset += this.mRefreshs.get(i).getLayoutTopOffsetForSibling();
            }
        }
        return this.mChildrenLayoutOffset;
    }

    public void addChild(WXComponent wXComponent, int i) {
        if (wXComponent.getAttrs().containsKey("id")) {
            this.childens.put(wXComponent.getAttrs().get("id").toString(), wXComponent);
        }
        if ((wXComponent instanceof WXBaseRefresh) && checkRefreshOrLoading(wXComponent)) {
            this.mRefreshs.add(wXComponent);
        }
        super.addChild(wXComponent, i);
    }

    private boolean checkRefreshOrLoading(final WXComponent wXComponent) {
        boolean z;
        if (!(wXComponent instanceof WXRefresh) || getHostView() == null) {
            z = false;
        } else {
            ((BaseBounceView) getHostView()).setOnRefreshListener((WXRefresh) wXComponent);
            this.handler.postDelayed(WXThread.secure((Runnable) new Runnable() {
                public void run() {
                    ((BaseBounceView) DCWXScroller.this.getHostView()).setHeaderView(wXComponent);
                }
            }), 100);
            z = true;
        }
        if (!(wXComponent instanceof WXLoading) || getHostView() == null) {
            return z;
        }
        ((BaseBounceView) getHostView()).setOnLoadingListener((WXLoading) wXComponent);
        this.handler.postDelayed(WXThread.secure((Runnable) new Runnable() {
            public void run() {
                ((BaseBounceView) DCWXScroller.this.getHostView()).setFooterView(wXComponent);
            }
        }), 100);
        return true;
    }

    public void remove(WXComponent wXComponent, boolean z) {
        super.remove(wXComponent, z);
        if (wXComponent instanceof WXLoading) {
            ((BaseBounceView) getHostView()).removeFooterView(wXComponent);
        } else if (wXComponent instanceof WXRefresh) {
            ((BaseBounceView) getHostView()).removeHeaderView(wXComponent);
        }
    }

    public void destroy() {
        if (getInnerView() != null && (getInnerView() instanceof IWXScroller)) {
            ((IWXScroller) getInnerView()).destroy();
        }
        Map<String, AppearanceHelper> map = this.mAppearanceComponents;
        if (map != null) {
            map.clear();
        }
        Map<String, Map<String, WXComponent>> map2 = this.mStickyMap;
        if (map2 != null) {
            map2.clear();
        }
        if (!(this.mOnAttachStateChangeListener == null || getInnerView() == null)) {
            getInnerView().removeOnAttachStateChangeListener(this.mOnAttachStateChangeListener);
        }
        super.destroy();
    }

    public void setMarginsSupportRTL(ViewGroup.MarginLayoutParams marginLayoutParams, int i, int i2, int i3, int i4) {
        if (Build.VERSION.SDK_INT >= 17) {
            marginLayoutParams.setMargins(i, i2, i3, i4);
            marginLayoutParams.setMarginStart(i);
            marginLayoutParams.setMarginEnd(i3);
        } else if (marginLayoutParams instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) marginLayoutParams;
            if (isLayoutRTL()) {
                layoutParams.gravity = 53;
                marginLayoutParams.setMargins(i3, i2, i, i4);
                return;
            }
            layoutParams.gravity = 51;
            marginLayoutParams.setMargins(i, i2, i3, i4);
        } else {
            marginLayoutParams.setMargins(i, i2, i3, i4);
        }
    }

    public void setLayout(WXComponent wXComponent) {
        if (!TextUtils.isEmpty(wXComponent.getComponentType()) && !TextUtils.isEmpty(wXComponent.getRef()) && wXComponent.getLayoutPosition() != null && wXComponent.getLayoutSize() != null) {
            if (wXComponent.getHostView() != null) {
                ViewCompat.setLayoutDirection(wXComponent.getHostView(), wXComponent.isLayoutRTL() ? 1 : 0);
            }
            super.setLayout(wXComponent);
        }
    }

    /* access modifiers changed from: protected */
    public WXComponent.MeasureOutput measure(int i, int i2) {
        WXComponent.MeasureOutput measureOutput = new WXComponent.MeasureOutput();
        if (this.mOrientation == 0) {
            int screenWidth = WXViewUtils.getScreenWidth(WXEnvironment.sApplication);
            int weexWidth = WXViewUtils.getWeexWidth(getInstanceId());
            if (weexWidth < screenWidth) {
                screenWidth = weexWidth;
            }
            if (i > screenWidth) {
                i = -1;
            }
            measureOutput.width = i;
            measureOutput.height = i2;
        } else {
            int screenHeight = WXViewUtils.getScreenHeight((Context) WXEnvironment.sApplication);
            int weexHeight = WXViewUtils.getWeexHeight(getInstanceId());
            if (weexHeight < screenHeight) {
                screenHeight = weexHeight;
            }
            if (i2 > screenHeight) {
                i2 = -1;
            }
            measureOutput.height = i2;
            measureOutput.width = i;
        }
        return measureOutput;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v3, resolved type: io.dcloud.feature.weex_scroller.view.DCBounceScrollerView} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v10, resolved type: io.dcloud.feature.weex_scroller.view.DCBounceScrollerView} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v11, resolved type: io.dcloud.feature.weex_scroller.view.DCWXHorizontalScrollView} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v21, resolved type: io.dcloud.feature.weex_scroller.view.DCBounceScrollerView} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v22, resolved type: io.dcloud.feature.weex_scroller.view.DCBounceScrollerView} */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.view.ViewGroup initComponentHostView(android.content.Context r10) {
        /*
            r9 = this;
            com.taobao.weex.dom.WXAttr r0 = r9.getAttrs()
            boolean r0 = r0.isEmpty()
            java.lang.String r1 = "horizontal"
            java.lang.String r2 = "vertical"
            r3 = 0
            r4 = 1
            if (r0 == 0) goto L_0x0016
            r0 = 0
            r5 = 1
            r6 = 0
            goto L_0x00ca
        L_0x0016:
            com.taobao.weex.dom.WXAttr r0 = r9.getAttrs()
            java.lang.String r5 = "scrollX"
            boolean r0 = r0.containsKey(r5)
            if (r0 == 0) goto L_0x0033
            com.taobao.weex.dom.WXAttr r0 = r9.getAttrs()
            java.lang.Object r0 = r0.get(r5)
            java.lang.String r0 = r0.toString()
            boolean r0 = java.lang.Boolean.parseBoolean(r0)
            goto L_0x0034
        L_0x0033:
            r0 = 0
        L_0x0034:
            com.taobao.weex.dom.WXAttr r6 = r9.getAttrs()
            java.lang.String r7 = "scrollY"
            boolean r6 = r6.containsKey(r7)
            if (r6 == 0) goto L_0x0051
            com.taobao.weex.dom.WXAttr r6 = r9.getAttrs()
            java.lang.Object r6 = r6.get(r7)
            java.lang.String r6 = r6.toString()
            boolean r6 = java.lang.Boolean.parseBoolean(r6)
            goto L_0x0052
        L_0x0051:
            r6 = 0
        L_0x0052:
            java.lang.String r8 = "false"
            if (r6 == 0) goto L_0x0064
            com.taobao.weex.dom.WXAttr r5 = r9.getAttrs()
            java.lang.Object r5 = r5.get(r7)
            boolean r5 = r5.equals(r8)
            r5 = r5 ^ r4
            goto L_0x008b
        L_0x0064:
            if (r0 == 0) goto L_0x0076
            com.taobao.weex.dom.WXAttr r2 = r9.getAttrs()
            java.lang.Object r2 = r2.get(r5)
            boolean r2 = r2.equals(r8)
            r5 = r2 ^ 1
            r2 = r1
            goto L_0x008b
        L_0x0076:
            com.taobao.weex.dom.WXAttr r5 = r9.getAttrs()
            java.lang.String r7 = "scrollDirection"
            boolean r5 = r5.containsKey(r7)
            if (r5 == 0) goto L_0x008a
            com.taobao.weex.dom.WXAttr r2 = r9.getAttrs()
            java.lang.String r2 = r2.getScrollDirection()
        L_0x008a:
            r5 = 1
        L_0x008b:
            com.taobao.weex.dom.WXAttr r7 = r9.getAttrs()
            java.lang.String r8 = "pagingEnabled"
            java.lang.Object r7 = r7.get(r8)
            if (r7 == 0) goto L_0x00a3
            java.lang.String r7 = r7.toString()
            boolean r7 = java.lang.Boolean.parseBoolean(r7)
            if (r7 == 0) goto L_0x00a3
            r7 = 1
            goto L_0x00a4
        L_0x00a3:
            r7 = 0
        L_0x00a4:
            r9.pageEnable = r7
            com.taobao.weex.dom.WXAttr r7 = r9.getAttrs()
            java.lang.String r8 = "pageSize"
            java.lang.Object r7 = r7.get(r8)
            if (r7 == 0) goto L_0x00ca
            float r7 = com.taobao.weex.utils.WXUtils.getFloat(r7)
            com.taobao.weex.WXSDKInstance r8 = r9.getInstance()
            int r8 = r8.getInstanceViewPortWidth()
            float r7 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth((float) r7, (int) r8)
            r8 = 0
            int r8 = (r7 > r8 ? 1 : (r7 == r8 ? 0 : -1))
            if (r8 == 0) goto L_0x00ca
            int r7 = (int) r7
            r9.pageSize = r7
        L_0x00ca:
            boolean r1 = r1.equals(r2)
            r2 = -1
            if (r1 == 0) goto L_0x0135
            r9.mOrientation = r3
            io.dcloud.feature.weex_scroller.view.DCWXHorizontalScrollView r0 = new io.dcloud.feature.weex_scroller.view.DCWXHorizontalScrollView
            r0.<init>(r10)
            r0.setWAScroller(r9)
            r0.setScrollable(r5)
            android.widget.FrameLayout r1 = new android.widget.FrameLayout
            r1.<init>(r10)
            r9.mRealView = r1
            com.taobao.weex.ui.component.DCWXScroller$5 r10 = new com.taobao.weex.ui.component.DCWXScroller$5
            r10.<init>()
            r0.setScrollViewListener(r10)
            android.widget.FrameLayout$LayoutParams r10 = new android.widget.FrameLayout$LayoutParams
            r10.<init>(r2, r2)
            android.widget.FrameLayout r1 = r9.mRealView
            r0.addView(r1, r10)
            r0.setHorizontalScrollBarEnabled(r4)
            r9.mScrollerView = r0
            com.taobao.weex.WXSDKInstance r10 = r9.getInstance()
            android.content.Context r10 = r10.getContext()
            r1 = 1082130432(0x40800000, float:4.0)
            int r10 = io.dcloud.common.util.DensityUtils.dip2px(r10, r1)
            r0.setScrollBarSize(r10)
            com.taobao.weex.ui.component.DCWXScroller$6 r10 = new com.taobao.weex.ui.component.DCWXScroller$6
            r10.<init>(r0, r9)
            android.widget.FrameLayout r1 = r9.mRealView
            com.taobao.weex.ui.component.DCWXScroller$7 r2 = new com.taobao.weex.ui.component.DCWXScroller$7
            r2.<init>(r10)
            r1.addOnAttachStateChangeListener(r2)
            boolean r10 = r9.pageEnable
            if (r10 == 0) goto L_0x018a
            android.view.GestureDetector r10 = new android.view.GestureDetector
            com.taobao.weex.ui.component.DCWXScroller$MyGestureDetector r1 = new com.taobao.weex.ui.component.DCWXScroller$MyGestureDetector
            r1.<init>(r0)
            r10.<init>(r1)
            r9.mGestureDetector = r10
            com.taobao.weex.ui.component.DCWXScroller$8 r10 = new com.taobao.weex.ui.component.DCWXScroller$8
            r10.<init>(r0)
            r0.setOnTouchListener(r10)
            goto L_0x018a
        L_0x0135:
            r9.mOrientation = r4
            io.dcloud.feature.weex_scroller.view.DCBounceScrollerView r1 = new io.dcloud.feature.weex_scroller.view.DCBounceScrollerView
            int r7 = r9.mOrientation
            r1.<init>(r10, r7, r9)
            android.widget.FrameLayout r7 = new android.widget.FrameLayout
            r7.<init>(r10)
            r9.mRealView = r7
            android.view.View r10 = r1.getInnerView()
            io.dcloud.feature.weex_scroller.view.DCWXScrollView r10 = (io.dcloud.feature.weex_scroller.view.DCWXScrollView) r10
            r10.addScrollViewListener(r9)
            if (r0 != 0) goto L_0x0156
            if (r6 != 0) goto L_0x0156
            r10.setScrollable(r3)
            goto L_0x0159
        L_0x0156:
            r10.setScrollable(r5)
        L_0x0159:
            android.widget.FrameLayout$LayoutParams r0 = new android.widget.FrameLayout$LayoutParams
            r0.<init>(r2, r2)
            android.widget.FrameLayout r2 = r9.mRealView
            r10.addView(r2, r0)
            r10.setVerticalScrollBarEnabled(r4)
            r9.mScrollerView = r10
            com.taobao.weex.dom.WXAttr r0 = r9.getAttrs()
            java.lang.String r2 = "nestedScrollingEnabled"
            java.lang.Object r0 = r0.get(r2)
            java.lang.Boolean r2 = java.lang.Boolean.valueOf(r4)
            java.lang.Boolean r0 = com.taobao.weex.utils.WXUtils.getBoolean(r0, r2)
            boolean r0 = r0.booleanValue()
            r10.setNestedScrollingEnabled(r0)
            com.taobao.weex.ui.component.DCWXScroller$9 r0 = new com.taobao.weex.ui.component.DCWXScroller$9
            r0.<init>()
            r10.addScrollViewListener(r0)
            r0 = r1
        L_0x018a:
            android.view.ViewTreeObserver r10 = r0.getViewTreeObserver()
            com.taobao.weex.ui.component.DCWXScroller$10 r1 = new com.taobao.weex.ui.component.DCWXScroller$10
            r1.<init>()
            r10.addOnGlobalLayoutListener(r1)
            com.taobao.weex.ui.component.DCWXScroller$11 r10 = new com.taobao.weex.ui.component.DCWXScroller$11
            r10.<init>()
            r9.mOnAttachStateChangeListener = r10
            r0.addOnAttachStateChangeListener(r10)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.DCWXScroller.initComponentHostView(android.content.Context):android.view.ViewGroup");
    }

    public int getScrollY() {
        if (getInnerView() == null) {
            return 0;
        }
        return getInnerView().getScrollY();
    }

    public int getScrollX() {
        if (getInnerView() == null) {
            return 0;
        }
        return getInnerView().getScrollX();
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public Map<String, Map<String, WXComponent>> getStickMap() {
        return this.mStickyMap;
    }

    /* access modifiers changed from: protected */
    public boolean setProperty(String str, Object obj) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -223520855:
                if (str.equals(Constants.Name.SHOW_SCROLLBAR)) {
                    c = 0;
                    break;
                }
                break;
            case -101546095:
                if (str.equals("scrollWithAnimation")) {
                    c = 1;
                    break;
                }
                break;
            case -5620052:
                if (str.equals(Constants.Name.OFFSET_ACCURACY)) {
                    c = 2;
                    break;
                }
                break;
            case 66669991:
                if (str.equals(Constants.Name.SCROLLABLE)) {
                    c = 3;
                    break;
                }
                break;
            case 1926689579:
                if (str.equals("scrollX")) {
                    c = 4;
                    break;
                }
                break;
            case 1926689580:
                if (str.equals("scrollY")) {
                    c = 5;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                Boolean bool = WXUtils.getBoolean(obj, (Boolean) null);
                if (bool != null) {
                    setShowScrollbar(bool.booleanValue());
                }
                return true;
            case 1:
                setScrollWithAnimation(WXUtils.getBoolean(obj, false).booleanValue());
                break;
            case 2:
                setOffsetAccuracy(WXUtils.getInteger(obj, 10).intValue());
                return true;
            case 3:
                setScrollable(WXUtils.getBoolean(obj, true).booleanValue());
                return true;
            case 4:
            case 5:
                setScrollable(!String.valueOf(obj).equals(AbsoluteConst.FALSE));
                return true;
        }
        return super.setProperty(str, obj);
    }

    @WXComponentProp(name = "scrollIntoView")
    public void setScrollIntoView(final String str) {
        if (!this.isViewLayoutFinished.get()) {
            getInnerView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    if (DCWXScroller.this.getInstance() != null) {
                        WXComponent wXComponentById = WXSDKManager.getInstance().getWXRenderManager().getWXComponentById(DCWXScroller.this.getInstanceId(), str);
                        if (wXComponentById != null) {
                            DCWXScroller.this.scrollTo(wXComponentById, (Map<String, Object>) JSONObject.parseObject("{'animated':false}"));
                        }
                        DCWXScroller.this.isViewLayoutFinished.set(true);
                        DCWXScroller.this.handler.postDelayed(new Runnable() {
                            public void run() {
                                DCWXScroller.this.getInnerView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                        }, 100);
                    }
                }
            });
            return;
        }
        WXComponent wXComponentById = WXSDKManager.getInstance().getWXRenderManager().getWXComponentById(getInstanceId(), str);
        if (wXComponentById != null) {
            scrollTo(wXComponentById, (Map<String, Object>) JSONObject.parseObject(StringUtil.format("{'animated':%b}", Boolean.valueOf(this.isAnimation))));
        }
    }

    @JSMethod
    public void scrollTo(int i) {
        float realPxByWidth = WXViewUtils.getRealPxByWidth((float) i, getInstance().getInstanceViewPortWidth());
        if (getInnerView() instanceof DCWXScrollView) {
            ((DCWXScrollView) getInnerView()).smoothScrollTo(0, (int) realPxByWidth);
        } else if (getInnerView() instanceof DCWXHorizontalScrollView) {
            ((DCWXHorizontalScrollView) getInnerView()).smoothScrollTo((int) realPxByWidth, 0);
        }
    }

    @JSMethod
    public void scrollTo(String str, JSCallback jSCallback) {
        float realPxByWidth = WXViewUtils.getRealPxByWidth(WXUtils.getFloat(JSON.parseObject(str).getString(Constants.Name.SCROLL_TOP)), getInstance().getInstanceViewPortWidth());
        if (getInnerView() instanceof DCWXScrollView) {
            ((DCWXScrollView) getInnerView()).smoothScrollTo(0, (int) realPxByWidth);
        } else if (getInnerView() instanceof DCWXHorizontalScrollView) {
            ((DCWXHorizontalScrollView) getInnerView()).smoothScrollTo((int) realPxByWidth, 0);
        }
        if (jSCallback != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("type", WXImage.SUCCEED);
            jSCallback.invoke(hashMap);
        }
    }

    private WXComponent getViewById(String str) {
        if (this.childens.containsKey(str)) {
            return this.childens.get(str);
        }
        for (WXComponent next : this.childens.values()) {
            if (next.getAttrs().containsKey("id") && next.getAttrs().get("id").equals(str)) {
                return next;
            }
        }
        return null;
    }

    @WXComponentProp(name = "showScrollbar")
    public void setShowScrollbar(boolean z) {
        if (getInnerView() != null) {
            if (this.mOrientation == 1) {
                getInnerView().setVerticalScrollBarEnabled(z);
            } else {
                getInnerView().setHorizontalScrollBarEnabled(z);
            }
        }
    }

    @WXComponentProp(name = "scrollWithAnimation")
    public void setScrollWithAnimation(boolean z) {
        this.isAnimation = z;
    }

    @WXComponentProp(name = "upperThreshold")
    public void setUpperHeight(int i) {
        ViewGroup innerView = getInnerView();
        float realPxByWidth = WXViewUtils.getRealPxByWidth((float) i, getInstance().getInstanceViewPortWidth());
        if (innerView instanceof DCWXHorizontalScrollView) {
            ((DCWXHorizontalScrollView) innerView).setUpperLength(realPxByWidth);
        } else if (innerView instanceof DCWXScrollView) {
            ((DCWXScrollView) innerView).setUpperLength(realPxByWidth);
        }
    }

    @WXComponentProp(name = "enable-back-to-top")
    public void isEnableBackToTop(boolean z) {
        this.canScroll2Top = z;
    }

    @JSMethod
    public void scrollToTop() {
        if ((getInnerView() instanceof DCWXScrollView) && this.canScroll2Top) {
            ((DCWXScrollView) getInnerView()).smoothScrollTo(0, 0);
        }
    }

    @WXComponentProp(name = "scrollTop")
    public void setScrollTop(String str) {
        if (getInnerView() instanceof DCWXScrollView) {
            final float realPxByWidth = WXViewUtils.getRealPxByWidth(WXUtils.getFloat(str), getInstance().getInstanceViewPortWidth());
            if (!this.isViewLayoutFinished.get()) {
                getInnerView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        DCWXScroller.this.getInnerView().scrollTo(0, (int) realPxByWidth);
                        DCWXScroller.this.isViewLayoutFinished.set(true);
                        DCWXScroller.this.handler.postDelayed(new Runnable() {
                            public void run() {
                                DCWXScroller.this.getInnerView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                        }, 100);
                    }
                });
                return;
            }
            if (this.isAnimation) {
                if (getInnerView() instanceof DCWXScrollView) {
                    ((DCWXScrollView) getInnerView()).stopScroll();
                }
                ObjectAnimator.ofInt(getInnerView(), "scrollY", new int[]{getInnerView().getScrollY(), (int) realPxByWidth}).setDuration(200).start();
            } else {
                getInnerView().post(new Runnable() {
                    public void run() {
                        DCWXScroller.this.getInnerView().scrollTo(0, (int) realPxByWidth);
                    }
                });
            }
            this.isViewLayoutFinished.set(true);
        }
    }

    @WXComponentProp(name = "scrollLeft")
    public void setScrollLeft(String str) {
        if (getInnerView() instanceof DCWXHorizontalScrollView) {
            final float realPxByWidth = WXViewUtils.getRealPxByWidth(WXUtils.getFloat(str), getInstance().getInstanceViewPortWidth());
            if (!this.isViewLayoutFinished.get()) {
                getInnerView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        ObjectAnimator.ofInt(DCWXScroller.this.getInnerView(), "scrollX", new int[]{DCWXScroller.this.getInnerView().getScrollX(), (int) realPxByWidth}).setDuration(1).start();
                        DCWXScroller.this.isViewLayoutFinished.set(true);
                        DCWXScroller.this.handler.postDelayed(new Runnable() {
                            public void run() {
                                DCWXScroller.this.getInnerView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                        }, 100);
                    }
                });
                return;
            }
            if (this.isAnimation) {
                if (getInnerView() instanceof DCWXHorizontalScrollView) {
                    ((DCWXHorizontalScrollView) getInnerView()).stopScroll();
                }
                ObjectAnimator.ofInt(getInnerView(), "scrollX", new int[]{getInnerView().getScrollX(), (int) realPxByWidth}).setDuration(200).start();
            } else {
                ((DCWXHorizontalScrollView) getInnerView()).smoothScrollTo((int) realPxByWidth, 0);
            }
            this.isViewLayoutFinished.set(true);
        }
    }

    @WXComponentProp(name = "lowerThreshold")
    public void setLowwerHeight(int i) {
        ViewGroup innerView = getInnerView();
        float realPxByWidth = WXViewUtils.getRealPxByWidth((float) i, getInstance().getInstanceViewPortWidth());
        if (innerView instanceof DCWXHorizontalScrollView) {
            ((DCWXHorizontalScrollView) innerView).setLowwerLength(realPxByWidth);
        } else if (innerView instanceof DCWXScrollView) {
            ((DCWXScrollView) innerView).setLowwerLength(realPxByWidth);
        }
    }

    /* access modifiers changed from: protected */
    public void onFinishLayout() {
        super.onFinishLayout();
    }

    public void setScrollable(boolean z) {
        this.isScrollable = z;
        ViewGroup innerView = getInnerView();
        if (innerView instanceof DCWXHorizontalScrollView) {
            ((DCWXHorizontalScrollView) innerView).setScrollable(z);
        } else if (innerView instanceof DCWXScrollView) {
            ((DCWXScrollView) innerView).setScrollable(z);
        }
    }

    @WXComponentProp(name = "offsetAccuracy")
    public void setOffsetAccuracy(int i) {
        this.mOffsetAccuracy = (int) WXViewUtils.getRealPxByWidth((float) i, getInstance().getInstanceViewPortWidth());
    }

    @WXComponentProp(name = "decelerationRate")
    public void setDecelerationRate(float f) {
        if (getInnerView() instanceof DCWXScrollView) {
            ((DCWXScrollView) getInnerView()).setRate(f);
        }
    }

    public boolean isScrollable() {
        return this.isScrollable;
    }

    public void bindStickStyle(WXComponent wXComponent) {
        this.stickyHelper.bindStickStyle(wXComponent, this.mStickyMap);
    }

    public void unbindStickStyle(WXComponent wXComponent) {
        this.stickyHelper.unbindStickStyle(wXComponent, this.mStickyMap);
    }

    public void bindAppearEvent(WXComponent wXComponent) {
        setWatch(0, wXComponent, true);
    }

    private void setWatch(int i, WXComponent wXComponent, boolean z) {
        AppearanceHelper appearanceHelper = this.mAppearanceComponents.get(wXComponent.getRef());
        if (appearanceHelper == null) {
            appearanceHelper = new AppearanceHelper(wXComponent);
            this.mAppearanceComponents.put(wXComponent.getRef(), appearanceHelper);
        }
        appearanceHelper.setWatchEvent(i, z);
        procAppear(0, 0, 0, 0);
    }

    public void bindDisappearEvent(WXComponent wXComponent) {
        setWatch(1, wXComponent, true);
    }

    public void unbindAppearEvent(WXComponent wXComponent) {
        setWatch(0, wXComponent, false);
    }

    public void unbindDisappearEvent(WXComponent wXComponent) {
        setWatch(1, wXComponent, false);
    }

    public void scrollTo(WXComponent wXComponent, Map<String, Object> map) {
        int i;
        boolean z = true;
        float f = 0.0f;
        if (map != null) {
            String obj = map.get("offset") == null ? WXInstanceApm.VALUE_ERROR_CODE_DEFAULT : map.get("offset").toString();
            z = WXUtils.getBoolean(map.get(Constants.Name.ANIMATED), true).booleanValue();
            if (obj != null) {
                try {
                    f = WXViewUtils.getRealPxByWidth(Float.parseFloat(obj), getInstance().getInstanceViewPortWidth());
                } catch (Exception e) {
                    WXLogUtils.e("Float parseFloat error :" + e.getMessage());
                }
            }
        }
        if (this.pageEnable) {
            this.mActiveFeature = this.mChildren.indexOf(wXComponent);
        }
        int absoluteY = wXComponent.getAbsoluteY() - getAbsoluteY();
        if (isLayoutRTL()) {
            if (getInnerView().getChildCount() > 0) {
                i = (getInnerView().getChildAt(0).getWidth() - (wXComponent.getAbsoluteX() - getAbsoluteX())) - getInnerView().getMeasuredWidth();
            } else {
                i = wXComponent.getAbsoluteX() - getAbsoluteX();
            }
            f = -f;
        } else {
            i = wXComponent.getAbsoluteX() - getAbsoluteX();
        }
        int i2 = (int) f;
        scrollBy((i - getScrollX()) + i2, (absoluteY - getScrollY()) + i2, z);
    }

    public void scrollBy(int i, int i2) {
        scrollBy(i, i2, false);
    }

    public void scrollBy(final int i, final int i2, final boolean z) {
        if (getInnerView() != null) {
            getInnerView().postDelayed(new Runnable() {
                public void run() {
                    if (DCWXScroller.this.mOrientation == 1) {
                        if (z) {
                            ((DCWXScrollView) DCWXScroller.this.getInnerView()).smoothScrollBy(0, i2);
                        } else {
                            ((DCWXScrollView) DCWXScroller.this.getInnerView()).scrollBy(0, i2);
                        }
                    } else if (z) {
                        ((DCWXHorizontalScrollView) DCWXScroller.this.getInnerView()).smoothScrollBy(i, 0);
                    } else {
                        ((DCWXHorizontalScrollView) DCWXScroller.this.getInnerView()).scrollBy(i, 0);
                    }
                    DCWXScroller.this.getInnerView().invalidate();
                }
            }, 16);
        }
    }

    public void onScrollChanged(DCWXScrollView dCWXScrollView, int i, int i2, int i3, int i4) {
        procAppear(i, i2, i3, i4);
    }

    public void notifyAppearStateChange(String str, String str2) {
        if (containsEvent(Constants.Event.APPEAR) || containsEvent(Constants.Event.DISAPPEAR)) {
            HashMap hashMap = new HashMap();
            hashMap.put("direction", str2);
            fireEvent(str, hashMap);
        }
    }

    /* access modifiers changed from: private */
    public void procAppear(int i, int i2, int i3, int i4) {
        int appearStatus;
        if (this.mIsHostAttachedToWindow) {
            int i5 = i2 - i4;
            int i6 = i - i3;
            String str = i5 > 0 ? "up" : i5 < 0 ? "down" : null;
            if (this.mOrientation == 0 && i6 != 0) {
                str = i6 > 0 ? "right" : "left";
            }
            for (Map.Entry<String, AppearanceHelper> value : this.mAppearanceComponents.entrySet()) {
                AppearanceHelper appearanceHelper = (AppearanceHelper) value.getValue();
                if (appearanceHelper.isWatch() && (appearStatus = appearanceHelper.setAppearStatus(checkItemVisibleInScroller(appearanceHelper.getAwareChild()))) != 0) {
                    appearanceHelper.getAwareChild().notifyAppearStateChange(appearStatus == 1 ? Constants.Event.APPEAR : Constants.Event.DISAPPEAR, str);
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0034, code lost:
        if (r1 < getLayoutWidth()) goto L_0x0036;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0058, code lost:
        if (r1 < getLayoutHeight()) goto L_0x0036;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean checkItemVisibleInScroller(com.taobao.weex.ui.component.WXComponent r6) {
        /*
            r5 = this;
            r0 = 0
            r1 = 0
        L_0x0002:
            if (r6 == 0) goto L_0x0060
            boolean r2 = r6 instanceof com.taobao.weex.ui.component.DCWXScroller
            if (r2 != 0) goto L_0x0060
            com.taobao.weex.ui.component.WXVContainer r2 = r6.getParent()
            boolean r2 = r2 instanceof com.taobao.weex.ui.component.DCWXScroller
            if (r2 == 0) goto L_0x005b
            int r1 = r5.mOrientation
            r2 = 1
            r3 = 0
            if (r1 != 0) goto L_0x003a
            com.taobao.weex.ui.action.GraphicPosition r1 = r6.getLayoutPosition()
            float r1 = r1.getLeft()
            int r1 = (int) r1
            int r4 = r5.getScrollX()
            int r1 = r1 - r4
            float r1 = (float) r1
            float r4 = r6.getLayoutWidth()
            float r3 = r3 - r4
            int r3 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r3 <= 0) goto L_0x0038
            float r3 = r5.getLayoutWidth()
            int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r1 >= 0) goto L_0x0038
        L_0x0036:
            r1 = 1
            goto L_0x005b
        L_0x0038:
            r1 = 0
            goto L_0x005b
        L_0x003a:
            com.taobao.weex.ui.action.GraphicPosition r1 = r6.getLayoutPosition()
            float r1 = r1.getTop()
            int r1 = (int) r1
            int r4 = r5.getScrollY()
            int r1 = r1 - r4
            float r1 = (float) r1
            float r4 = r6.getLayoutHeight()
            float r3 = r3 - r4
            int r3 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r3 <= 0) goto L_0x0038
            float r3 = r5.getLayoutHeight()
            int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r1 >= 0) goto L_0x0038
            goto L_0x0036
        L_0x005b:
            com.taobao.weex.ui.component.WXVContainer r6 = r6.getParent()
            goto L_0x0002
        L_0x0060:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.DCWXScroller.checkItemVisibleInScroller(com.taobao.weex.ui.component.WXComponent):boolean");
    }

    /* access modifiers changed from: private */
    public void dispatchDisappearEvent() {
        int appearStatus;
        for (Map.Entry<String, AppearanceHelper> value : this.mAppearanceComponents.entrySet()) {
            AppearanceHelper appearanceHelper = (AppearanceHelper) value.getValue();
            if (appearanceHelper.isWatch() && (appearStatus = appearanceHelper.setAppearStatus(false)) != 0) {
                appearanceHelper.getAwareChild().notifyAppearStateChange(appearStatus == 1 ? Constants.Event.APPEAR : Constants.Event.DISAPPEAR, "");
            }
        }
    }

    public void onScroll(DCWXScrollView dCWXScrollView, int i, int i2) {
        onLoadMore(dCWXScrollView, i, i2);
    }

    /* access modifiers changed from: protected */
    public void onLoadMore(FrameLayout frameLayout, int i, int i2) {
        try {
            String loadMoreOffset = getAttrs().getLoadMoreOffset();
            if (!TextUtils.isEmpty(loadMoreOffset)) {
                int realPxByWidth = (int) WXViewUtils.getRealPxByWidth(Float.parseFloat(loadMoreOffset), getInstance().getInstanceViewPortWidth());
                if (frameLayout instanceof DCWXHorizontalScrollView) {
                    int width = frameLayout.getChildAt(0).getWidth();
                    if ((width - i) - frameLayout.getWidth() >= realPxByWidth) {
                        return;
                    }
                    if (this.mContentWidth != width || this.mForceLoadmoreNextTime) {
                        fireEvent(Constants.Event.LOADMORE);
                        this.mContentWidth = width;
                        this.mForceLoadmoreNextTime = false;
                        return;
                    }
                    return;
                }
                int height = frameLayout.getChildAt(0).getHeight();
                int height2 = (height - i2) - frameLayout.getHeight();
                if (height2 < realPxByWidth) {
                    if (WXEnvironment.isApkDebugable()) {
                        WXLogUtils.d("[WXScroller-onScroll] offScreenY :" + height2);
                    }
                    if (this.mContentHeight != height || this.mForceLoadmoreNextTime) {
                        fireEvent(Constants.Event.LOADMORE);
                        this.mContentHeight = height;
                        this.mForceLoadmoreNextTime = false;
                    }
                }
            }
        } catch (Exception e) {
            WXLogUtils.d("[DCWXScroller-onScroll] ", (Throwable) e);
        }
    }

    @JSMethod
    public void resetLoadmore() {
        this.mForceLoadmoreNextTime = true;
    }

    public ScrollStartEndHelper getScrollStartEndHelper() {
        if (this.mScrollStartEndHelper == null) {
            this.mScrollStartEndHelper = new ScrollStartEndHelper(this);
        }
        return this.mScrollStartEndHelper;
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        private final DCWXHorizontalScrollView scrollView;

        public DCWXHorizontalScrollView getScrollView() {
            return this.scrollView;
        }

        MyGestureDetector(DCWXHorizontalScrollView dCWXHorizontalScrollView) {
            this.scrollView = dCWXHorizontalScrollView;
        }

        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            int size = DCWXScroller.this.mChildren.size();
            try {
                if (motionEvent.getX() - motionEvent2.getX() <= 5.0f || Math.abs(f) <= 300.0f) {
                    if (motionEvent2.getX() - motionEvent.getX() > 5.0f && Math.abs(f) > 300.0f) {
                        int access$400 = DCWXScroller.this.pageSize;
                        DCWXScroller dCWXScroller = DCWXScroller.this;
                        int unused = dCWXScroller.mActiveFeature = dCWXScroller.mActiveFeature > 0 ? DCWXScroller.this.mActiveFeature - 1 : 0;
                        this.scrollView.smoothScrollTo(DCWXScroller.this.mActiveFeature * access$400, 0);
                        return true;
                    }
                    return false;
                }
                int access$4002 = DCWXScroller.this.pageSize;
                DCWXScroller dCWXScroller2 = DCWXScroller.this;
                int i = size - 1;
                if (dCWXScroller2.mActiveFeature < i) {
                    i = DCWXScroller.this.mActiveFeature + 1;
                }
                int unused2 = dCWXScroller2.mActiveFeature = i;
                this.scrollView.smoothScrollTo(DCWXScroller.this.mActiveFeature * access$4002, 0);
                return true;
            } catch (Exception e) {
                WXLogUtils.e("There was an error processing the Fling event:" + e.getMessage());
            }
        }
    }
}
