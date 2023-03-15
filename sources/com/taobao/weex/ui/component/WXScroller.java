package com.taobao.weex.ui.component;

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
import android.widget.FrameLayout;
import androidx.core.view.ViewCompat;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.WXThread;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.ui.ComponentCreator;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.helper.ScrollStartEndHelper;
import com.taobao.weex.ui.component.helper.WXStickyHelper;
import com.taobao.weex.ui.view.IWXScroller;
import com.taobao.weex.ui.view.WXBaseRefreshLayout;
import com.taobao.weex.ui.view.WXHorizontalScrollView;
import com.taobao.weex.ui.view.WXScrollView;
import com.taobao.weex.ui.view.refresh.wrapper.BaseBounceView;
import com.taobao.weex.ui.view.refresh.wrapper.BounceScrollerView;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(lazyload = false)
public class WXScroller extends WXBaseScroller {
    public static final String DIRECTION = "direction";
    private static final int SWIPE_MIN_DISTANCE = 5;
    private static final int SWIPE_THRESHOLD_VELOCITY = 300;
    private Handler handler;
    private boolean isScrollable;
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
    /* access modifiers changed from: private */
    public Boolean mIslastDirectionRTL;
    private Point mLastReport;
    private int mOffsetAccuracy;
    private View.OnAttachStateChangeListener mOnAttachStateChangeListener;
    protected int mOrientation;
    private FrameLayout mRealView;
    private List<WXComponent> mRefreshs;
    private ScrollStartEndHelper mScrollStartEndHelper;
    private FrameLayout mScrollerView;
    private Map<String, Map<String, WXComponent>> mStickyMap;
    private boolean mlastDirectionRTL;
    private boolean pageEnable;
    /* access modifiers changed from: private */
    public int pageSize;
    private WXStickyHelper stickyHelper;

    public void onScrollStopped(WXScrollView wXScrollView, int i, int i2) {
    }

    public void onScrollToBottom(WXScrollView wXScrollView, int i, int i2) {
    }

    public static class Creator implements ComponentCreator {
        public WXComponent createInstance(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) throws IllegalAccessException, InvocationTargetException, InstantiationException {
            wXSDKInstance.setUseScroller(true);
            return new WXScroller(wXSDKInstance, wXVContainer, basicComponentData);
        }
    }

    @Deprecated
    public WXScroller(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, String str, boolean z, BasicComponentData basicComponentData) {
        this(wXSDKInstance, wXVContainer, basicComponentData);
    }

    public WXScroller(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
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
        this.mlastDirectionRTL = false;
        this.mAppearanceComponents = new HashMap();
        this.mStickyMap = new HashMap();
        this.mContentHeight = 0;
        this.mContentWidth = 0;
        this.handler = new Handler(Looper.getMainLooper());
        this.isScrollable = true;
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
        if (getHostView() instanceof BounceScrollerView) {
            return (ViewGroup) ((BounceScrollerView) getHostView()).getInnerView();
        }
        return (ViewGroup) getHostView();
    }

    public void addEvent(String str) {
        super.addEvent(str);
        if (ScrollStartEndHelper.isScrollEvent(str) && getInnerView() != null && !this.mHasAddScrollEvent) {
            this.mHasAddScrollEvent = true;
            if (getInnerView() instanceof WXScrollView) {
                ((WXScrollView) getInnerView()).addScrollViewListener(new WXScrollView.WXScrollViewListener() {
                    public void onScroll(WXScrollView wXScrollView, int i, int i2) {
                    }

                    public void onScrollStopped(WXScrollView wXScrollView, int i, int i2) {
                    }

                    public void onScrollToBottom(WXScrollView wXScrollView, int i, int i2) {
                    }

                    public void onScrollChanged(WXScrollView wXScrollView, int i, int i2, int i3, int i4) {
                        WXScroller.this.getScrollStartEndHelper().onScrolled(i, i2);
                        if (WXScroller.this.getEvents().contains("scroll") && WXScroller.this.shouldReport(i, i2)) {
                            WXScroller.this.fireScrollEvent(wXScrollView.getContentFrame(), i, i2, i3, i4);
                        }
                    }
                });
            } else if (getInnerView() instanceof WXHorizontalScrollView) {
                ((WXHorizontalScrollView) getInnerView()).addScrollViewListener(new WXHorizontalScrollView.ScrollViewListener() {
                    public void onScrollChanged(WXHorizontalScrollView wXHorizontalScrollView, int i, int i2, int i3, int i4) {
                        WXScroller.this.getScrollStartEndHelper().onScrolled(i, i2);
                        if (WXScroller.this.getEvents().contains("scroll") && WXScroller.this.shouldReport(i, i2)) {
                            WXScroller.this.fireScrollEvent(wXHorizontalScrollView.getContentFrame(), i, i2, i3, i4);
                        }
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public void fireScrollEvent(Rect rect, int i, int i2, int i3, int i4) {
        fireEvent("scroll", getScrollEvent(i, i2));
    }

    public Map<String, Object> getScrollEvent(int i, int i2) {
        Rect rect = new Rect();
        if (getInnerView() instanceof WXScrollView) {
            rect = ((WXScrollView) getInnerView()).getContentFrame();
        } else if (getInnerView() instanceof WXHorizontalScrollView) {
            rect = ((WXHorizontalScrollView) getInnerView()).getContentFrame();
        }
        HashMap hashMap = new HashMap(2);
        HashMap hashMap2 = new HashMap(2);
        HashMap hashMap3 = new HashMap(2);
        float instanceViewPortWidthWithFloat = getInstance().getInstanceViewPortWidthWithFloat();
        hashMap2.put("width", Float.valueOf(WXViewUtils.getWebPxByWidth((float) rect.width(), instanceViewPortWidthWithFloat)));
        hashMap2.put("height", Float.valueOf(WXViewUtils.getWebPxByWidth((float) rect.height(), instanceViewPortWidthWithFloat)));
        hashMap3.put(Constants.Name.X, Float.valueOf(-WXViewUtils.getWebPxByWidth((float) i, instanceViewPortWidthWithFloat)));
        hashMap3.put(Constants.Name.Y, Float.valueOf(-WXViewUtils.getWebPxByWidth((float) i2, instanceViewPortWidthWithFloat)));
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
                    ((BaseBounceView) WXScroller.this.getHostView()).setHeaderView(wXComponent);
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
                ((BaseBounceView) WXScroller.this.getHostView()).setFooterView(wXComponent);
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
        super.destroy();
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
        if (getInnerView() != null && (getInnerView() instanceof IWXScroller)) {
            ((IWXScroller) getInnerView()).destroy();
        }
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

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v4, resolved type: com.taobao.weex.ui.view.refresh.wrapper.BounceScrollerView} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v6, resolved type: com.taobao.weex.ui.view.WXHorizontalScrollView} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v10, resolved type: com.taobao.weex.ui.view.refresh.wrapper.BounceScrollerView} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v11, resolved type: com.taobao.weex.ui.view.refresh.wrapper.BounceScrollerView} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v12, resolved type: com.taobao.weex.ui.view.refresh.wrapper.BounceScrollerView} */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.view.ViewGroup initComponentHostView(android.content.Context r6) {
        /*
            r5 = this;
            com.taobao.weex.dom.WXAttr r0 = r5.getAttrs()
            boolean r0 = r0.isEmpty()
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L_0x0010
            java.lang.String r0 = "vertical"
            goto L_0x0057
        L_0x0010:
            com.taobao.weex.dom.WXAttr r0 = r5.getAttrs()
            java.lang.String r0 = r0.getScrollDirection()
            com.taobao.weex.dom.WXAttr r3 = r5.getAttrs()
            java.lang.String r4 = "pagingEnabled"
            java.lang.Object r3 = r3.get(r4)
            if (r3 == 0) goto L_0x0030
            java.lang.String r3 = r3.toString()
            boolean r3 = java.lang.Boolean.parseBoolean(r3)
            if (r3 == 0) goto L_0x0030
            r3 = 1
            goto L_0x0031
        L_0x0030:
            r3 = 0
        L_0x0031:
            r5.pageEnable = r3
            com.taobao.weex.dom.WXAttr r3 = r5.getAttrs()
            java.lang.String r4 = "pageSize"
            java.lang.Object r3 = r3.get(r4)
            if (r3 == 0) goto L_0x0057
            float r3 = com.taobao.weex.utils.WXUtils.getFloat(r3)
            com.taobao.weex.WXSDKInstance r4 = r5.getInstance()
            float r4 = r4.getInstanceViewPortWidthWithFloat()
            float r3 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth((float) r3, (float) r4)
            r4 = 0
            int r4 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1))
            if (r4 == 0) goto L_0x0057
            int r3 = (int) r3
            r5.pageSize = r3
        L_0x0057:
            java.lang.String r3 = "horizontal"
            boolean r0 = r3.equals(r0)
            r3 = -1
            if (r0 == 0) goto L_0x00ad
            r5.mOrientation = r1
            com.taobao.weex.ui.view.WXHorizontalScrollView r0 = new com.taobao.weex.ui.view.WXHorizontalScrollView
            r0.<init>(r6)
            android.widget.FrameLayout r2 = new android.widget.FrameLayout
            r2.<init>(r6)
            r5.mRealView = r2
            com.taobao.weex.ui.component.WXScroller$5 r6 = new com.taobao.weex.ui.component.WXScroller$5
            r6.<init>()
            r0.setScrollViewListener(r6)
            android.widget.FrameLayout$LayoutParams r6 = new android.widget.FrameLayout$LayoutParams
            r6.<init>(r3, r3)
            android.widget.FrameLayout r2 = r5.mRealView
            r0.addView(r2, r6)
            r0.setHorizontalScrollBarEnabled(r1)
            r5.mScrollerView = r0
            com.taobao.weex.ui.component.WXScroller$6 r6 = new com.taobao.weex.ui.component.WXScroller$6
            r6.<init>(r0, r5)
            android.widget.FrameLayout r1 = r5.mRealView
            com.taobao.weex.ui.component.WXScroller$7 r2 = new com.taobao.weex.ui.component.WXScroller$7
            r2.<init>(r6)
            r1.addOnAttachStateChangeListener(r2)
            boolean r6 = r5.pageEnable
            if (r6 == 0) goto L_0x00f6
            android.view.GestureDetector r6 = new android.view.GestureDetector
            com.taobao.weex.ui.component.WXScroller$MyGestureDetector r1 = new com.taobao.weex.ui.component.WXScroller$MyGestureDetector
            r1.<init>(r0)
            r6.<init>(r1)
            r5.mGestureDetector = r6
            com.taobao.weex.ui.component.WXScroller$8 r6 = new com.taobao.weex.ui.component.WXScroller$8
            r6.<init>(r0)
            r0.setOnTouchListener(r6)
            goto L_0x00f6
        L_0x00ad:
            r5.mOrientation = r2
            com.taobao.weex.ui.view.refresh.wrapper.BounceScrollerView r0 = new com.taobao.weex.ui.view.refresh.wrapper.BounceScrollerView
            int r1 = r5.mOrientation
            r0.<init>(r6, r1, r5)
            android.widget.FrameLayout r1 = new android.widget.FrameLayout
            r1.<init>(r6)
            r5.mRealView = r1
            android.view.View r6 = r0.getInnerView()
            com.taobao.weex.ui.view.WXScrollView r6 = (com.taobao.weex.ui.view.WXScrollView) r6
            r6.addScrollViewListener(r5)
            android.widget.FrameLayout$LayoutParams r1 = new android.widget.FrameLayout$LayoutParams
            r1.<init>(r3, r3)
            r5.mScrollerView = r6
            android.widget.FrameLayout r3 = r5.mRealView
            r6.addView(r3, r1)
            r6.setVerticalScrollBarEnabled(r2)
            com.taobao.weex.dom.WXAttr r1 = r5.getAttrs()
            java.lang.String r3 = "nestedScrollingEnabled"
            java.lang.Object r1 = r1.get(r3)
            java.lang.Boolean r2 = java.lang.Boolean.valueOf(r2)
            java.lang.Boolean r1 = com.taobao.weex.utils.WXUtils.getBoolean(r1, r2)
            boolean r1 = r1.booleanValue()
            r6.setNestedScrollingEnabled(r1)
            com.taobao.weex.ui.component.WXScroller$9 r1 = new com.taobao.weex.ui.component.WXScroller$9
            r1.<init>()
            r6.addScrollViewListener(r1)
        L_0x00f6:
            android.view.ViewTreeObserver r6 = r0.getViewTreeObserver()
            com.taobao.weex.ui.component.WXScroller$10 r1 = new com.taobao.weex.ui.component.WXScroller$10
            r1.<init>()
            r6.addOnGlobalLayoutListener(r1)
            com.taobao.weex.ui.component.WXScroller$11 r6 = new com.taobao.weex.ui.component.WXScroller$11
            r6.<init>()
            r5.mOnAttachStateChangeListener = r6
            r0.addOnAttachStateChangeListener(r6)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.WXScroller.initComponentHostView(android.content.Context):android.view.ViewGroup");
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
            case -5620052:
                if (str.equals(Constants.Name.OFFSET_ACCURACY)) {
                    c = 1;
                    break;
                }
                break;
            case 66669991:
                if (str.equals(Constants.Name.SCROLLABLE)) {
                    c = 2;
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
                setOffsetAccuracy(WXUtils.getInteger(obj, 10).intValue());
                return true;
            case 2:
                setScrollable(WXUtils.getBoolean(obj, true).booleanValue());
                return true;
            default:
                return super.setProperty(str, obj);
        }
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

    @WXComponentProp(name = "scrollable")
    public void setScrollable(boolean z) {
        this.isScrollable = z;
        ViewGroup innerView = getInnerView();
        if (innerView instanceof WXHorizontalScrollView) {
            ((WXHorizontalScrollView) innerView).setScrollable(z);
        } else if (innerView instanceof WXScrollView) {
            ((WXScrollView) innerView).setScrollable(z);
        }
    }

    @WXComponentProp(name = "offsetAccuracy")
    public void setOffsetAccuracy(int i) {
        this.mOffsetAccuracy = (int) WXViewUtils.getRealPxByWidth((float) i, getInstance().getInstanceViewPortWidthWithFloat());
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
                    f = WXViewUtils.getRealPxByWidth(Float.parseFloat(obj), getInstance().getInstanceViewPortWidthWithFloat());
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
            if (wXComponent.getParent() == null || wXComponent.getParent() != this) {
                i = ((wXComponent.getAbsoluteX() - getAbsoluteX()) - getInnerView().getMeasuredWidth()) + ((int) wXComponent.getLayoutWidth());
            } else if (getInnerView().getChildCount() > 0) {
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
                    if (WXScroller.this.mOrientation == 1) {
                        if (z) {
                            ((WXScrollView) WXScroller.this.getInnerView()).smoothScrollBy(0, i2);
                        } else {
                            ((WXScrollView) WXScroller.this.getInnerView()).scrollBy(0, i2);
                        }
                    } else if (z) {
                        ((WXHorizontalScrollView) WXScroller.this.getInnerView()).smoothScrollBy(i, 0);
                    } else {
                        ((WXHorizontalScrollView) WXScroller.this.getInnerView()).scrollBy(i, 0);
                    }
                    WXScroller.this.getInnerView().invalidate();
                }
            }, 16);
        }
    }

    public void onScrollChanged(WXScrollView wXScrollView, int i, int i2, int i3, int i4) {
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
            boolean r2 = r6 instanceof com.taobao.weex.ui.component.WXScroller
            if (r2 != 0) goto L_0x0060
            com.taobao.weex.ui.component.WXVContainer r2 = r6.getParent()
            boolean r2 = r2 instanceof com.taobao.weex.ui.component.WXScroller
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
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.ui.component.WXScroller.checkItemVisibleInScroller(com.taobao.weex.ui.component.WXComponent):boolean");
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

    public void onScroll(WXScrollView wXScrollView, int i, int i2) {
        onLoadMore(wXScrollView, i, i2);
    }

    /* access modifiers changed from: protected */
    public void onLoadMore(FrameLayout frameLayout, int i, int i2) {
        try {
            String loadMoreOffset = getAttrs().getLoadMoreOffset();
            if (TextUtils.isEmpty(loadMoreOffset)) {
                loadMoreOffset = String.valueOf(1);
            }
            int realPxByWidth = (int) WXViewUtils.getRealPxByWidth(Float.parseFloat(loadMoreOffset), getInstance().getInstanceViewPortWidthWithFloat());
            if (frameLayout instanceof WXHorizontalScrollView) {
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
        } catch (Exception e) {
            WXLogUtils.d("[WXScroller-onScroll] ", (Throwable) e);
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
        private final WXHorizontalScrollView scrollView;

        public WXHorizontalScrollView getScrollView() {
            return this.scrollView;
        }

        MyGestureDetector(WXHorizontalScrollView wXHorizontalScrollView) {
            this.scrollView = wXHorizontalScrollView;
        }

        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            int size = WXScroller.this.mChildren.size();
            try {
                if (motionEvent.getX() - motionEvent2.getX() <= 5.0f || Math.abs(f) <= 300.0f) {
                    if (motionEvent2.getX() - motionEvent.getX() > 5.0f && Math.abs(f) > 300.0f) {
                        int access$400 = WXScroller.this.pageSize;
                        WXScroller wXScroller = WXScroller.this;
                        int unused = wXScroller.mActiveFeature = wXScroller.mActiveFeature > 0 ? WXScroller.this.mActiveFeature - 1 : 0;
                        this.scrollView.smoothScrollTo(WXScroller.this.mActiveFeature * access$400, 0);
                        return true;
                    }
                    return false;
                }
                int access$4002 = WXScroller.this.pageSize;
                WXScroller wXScroller2 = WXScroller.this;
                int i = size - 1;
                if (wXScroller2.mActiveFeature < i) {
                    i = WXScroller.this.mActiveFeature + 1;
                }
                int unused2 = wXScroller2.mActiveFeature = i;
                this.scrollView.smoothScrollTo(WXScroller.this.mActiveFeature * access$4002, 0);
                return true;
            } catch (Exception e) {
                WXLogUtils.e("There was an error processing the Fling event:" + e.getMessage());
            }
        }
    }
}
