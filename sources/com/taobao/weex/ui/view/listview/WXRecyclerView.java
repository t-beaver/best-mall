package com.taobao.weex.ui.view.listview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.common.WXThread;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.component.list.WXListComponent;
import com.taobao.weex.ui.view.gesture.WXGesture;
import com.taobao.weex.ui.view.gesture.WXGestureObservable;
import com.taobao.weex.ui.view.listview.ExtendedLinearLayoutManager;
import com.taobao.weex.ui.view.refresh.wrapper.BounceRecyclerView;
import io.dcloud.common.ui.blur.AppEventForBlurManager;
import io.dcloud.feature.weex.extend.DCWXSlider;
import io.dcloud.weex.FlingHelper;

public class WXRecyclerView extends RecyclerView implements WXGestureObservable {
    public static final int TYPE_GRID_LAYOUT = 2;
    public static final int TYPE_LINEAR_LAYOUT = 1;
    public static final int TYPE_STAGGERED_GRID_LAYOUT = 3;
    private JSONObject NestInfo = null;
    private boolean hasTouch = false;
    private float headerHeight = 0.0f;
    public boolean isNestParent = false;
    boolean isStartFling = false;
    private Float lastY = Float.valueOf(0.0f);
    RecyclerView.OnScrollListener mChildScrollListener;
    /* access modifiers changed from: private */
    public int mCurrentDy = 0;
    private FlingHelper mFlingHelper;
    private WXGesture mGesture;
    private String mInstanceId;
    RecyclerView.OnScrollListener mParentScrollListener;
    private boolean scrollable = true;
    /* access modifiers changed from: private */
    public int totalDy = 0;
    private int velocityY = 0;

    static /* synthetic */ int access$212(WXRecyclerView wXRecyclerView, int i) {
        int i2 = wXRecyclerView.totalDy + i;
        wXRecyclerView.totalDy = i2;
        return i2;
    }

    public void setNestInfo(JSONObject jSONObject) {
        this.NestInfo = jSONObject;
        if (jSONObject != null) {
            this.isNestParent = jSONObject.getBooleanValue("isNestParent");
            this.mInstanceId = jSONObject.getString("instanceId");
            if (this.mFlingHelper == null) {
                this.mFlingHelper = new FlingHelper(getContext());
            }
            if (this.isNestParent) {
                setDescendantFocusability(131072);
                if (this.mParentScrollListener == null) {
                    this.headerHeight = jSONObject.getFloat("headerHeight").floatValue();
                    AnonymousClass1 r3 = new RecyclerView.OnScrollListener() {
                        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                            super.onScrollStateChanged(recyclerView, i);
                            if (i == 0) {
                                int unused = WXRecyclerView.this.mCurrentDy = 0;
                                WXRecyclerView.this.dispatchChildFling();
                            }
                        }

                        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                            super.onScrolled(recyclerView, i, i2);
                            int unused = WXRecyclerView.this.mCurrentDy = i2;
                            if (WXRecyclerView.this.isStartFling) {
                                int unused2 = WXRecyclerView.this.totalDy = 0;
                                WXRecyclerView.this.isStartFling = false;
                            }
                            WXRecyclerView.access$212(WXRecyclerView.this, i2);
                        }
                    };
                    this.mParentScrollListener = r3;
                    addOnScrollListener(r3);
                }
            } else if (this.mChildScrollListener == null) {
                AnonymousClass2 r32 = new RecyclerView.OnScrollListener() {
                    public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                        if (i == 0) {
                            int unused = WXRecyclerView.this.mCurrentDy = 0;
                            WXRecyclerView.this.dispatchParentFling();
                        }
                        super.onScrollStateChanged(recyclerView, i);
                    }

                    public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                        super.onScrolled(recyclerView, i, i2);
                        int unused = WXRecyclerView.this.mCurrentDy = i2;
                        if (WXRecyclerView.this.isStartFling) {
                            int unused2 = WXRecyclerView.this.totalDy = 0;
                            WXRecyclerView.this.isStartFling = false;
                        }
                        WXRecyclerView.access$212(WXRecyclerView.this, i2);
                    }
                };
                this.mChildScrollListener = r32;
                addOnScrollListener(r32);
            }
        }
    }

    public int getCurrentDy() {
        return this.mCurrentDy;
    }

    public void callBackNestParent(String str, String str2, float f) {
        WXRecyclerView parentRecyclerView = getParentRecyclerView();
        if (parentRecyclerView != null) {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("isNestParent", (Object) true);
            jSONObject.put("instanceId", (Object) str2);
            jSONObject.put("nestChildRef", (Object) str);
            jSONObject.put("headerHeight", (Object) Float.valueOf(f));
            parentRecyclerView.setNestInfo(jSONObject);
        }
    }

    public WXRecyclerView(Context context) {
        super(context);
    }

    public WXRecyclerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public boolean isScrollable() {
        return this.scrollable;
    }

    public void setScrollable(boolean z) {
        this.scrollable = z;
    }

    public void initView(Context context, int i, int i2) {
        initView(context, i, 1, 32.0f, i2);
    }

    public void initView(Context context, int i, int i2, float f, final int i3) {
        if (i == 2) {
            final int i4 = i3;
            setLayoutManager(new GridLayoutManager(context, i2, i3, false) {
                public boolean canScrollVertically() {
                    try {
                        return WXRecyclerView.this.canParentScrollVertically(i4, super.canScrollVertically());
                    } catch (Exception unused) {
                        return true;
                    }
                }

                public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                    try {
                        super.onLayoutChildren(recycler, state);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (i == 3) {
            setLayoutManager(new ExtendedStaggeredGridLayoutManager(i2, i3) {
                public boolean canScrollVertically() {
                    try {
                        return WXRecyclerView.this.canParentScrollVertically(i3, super.canScrollVertically());
                    } catch (Exception unused) {
                        return true;
                    }
                }

                public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                    try {
                        super.onLayoutChildren(recycler, state);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (i == 1) {
            final int i5 = i3;
            setLayoutManager(new LinearLayoutManager(context, i3, false) {
                public boolean canScrollVertically() {
                    try {
                        return WXRecyclerView.this.canParentScrollVertically(i5, super.canScrollVertically());
                    } catch (Exception unused) {
                        return true;
                    }
                }

                public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                    try {
                        super.onLayoutChildren(recycler, state);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public boolean canParentScrollVertically(int i, boolean z) {
        if (i == 1 && i == 1) {
            try {
                if (this.NestInfo != null && this.isNestParent) {
                    WXRecyclerView childRecylerView = getChildRecylerView();
                    if (childRecylerView == null) {
                        return true;
                    }
                    Rect rect = new Rect();
                    childRecylerView.getLocalVisibleRect(rect);
                    if (childRecylerView.getHeight() - rect.bottom != 0 || childRecylerView.isScrollTop()) {
                        return true;
                    }
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return z;
    }

    public void registerGestureListener(WXGesture wXGesture) {
        this.mGesture = wXGesture;
    }

    public WXGesture getGestureListener() {
        return this.mGesture;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        View view;
        WXRecyclerView childRecylerView;
        int floatValue;
        if (!this.scrollable) {
            return true;
        }
        if (this.NestInfo != null && this.isNestParent) {
            if (this.lastY.floatValue() == 0.0f) {
                this.lastY = Float.valueOf(motionEvent.getY());
            }
            if (!(!isScrollEnd() || (childRecylerView = getChildRecylerView()) == null || (floatValue = (int) (this.lastY.floatValue() - motionEvent.getY())) == 0)) {
                childRecylerView.scrollBy(0, floatValue);
            }
            this.lastY = Float.valueOf(motionEvent.getY());
        }
        if (motionEvent.getAction() == 1 && (view = (View) getParent().getParent()) != null && view.hasOnClickListeners() && (view instanceof BounceRecyclerView) && pointInView(motionEvent.getX(), motionEvent.getY(), 0.0f) && getScrollState() == 0) {
            view.performClick();
        }
        return super.onTouchEvent(motionEvent);
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        this.hasTouch = true;
        if (this.NestInfo != null) {
            if (this.isNestParent) {
                if (motionEvent != null && motionEvent.getAction() == 0) {
                    this.velocityY = 0;
                    stopScroll();
                }
                if (!(motionEvent == null || motionEvent.getAction() == 2)) {
                    this.lastY = Float.valueOf(0.0f);
                }
            } else if (motionEvent != null && motionEvent.getAction() == 0) {
                this.velocityY = 0;
            }
        }
        boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
        WXGesture wXGesture = this.mGesture;
        return wXGesture != null ? dispatchTouchEvent | wXGesture.onTouch(this, motionEvent) : dispatchTouchEvent;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return super.onInterceptTouchEvent(motionEvent);
    }

    public void scrollTo(boolean z, int i, final int i2, final int i3) {
        try {
            RecyclerView.LayoutManager layoutManager = getLayoutManager();
            stopScroll();
            if (z) {
                if (i3 == 1) {
                    if (layoutManager instanceof LinearLayoutManager) {
                        int findFirstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                        int findLastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                        if (i >= findFirstVisibleItemPosition && i <= findLastVisibleItemPosition) {
                            smoothScrollBy(0, getChildAt(i - findFirstVisibleItemPosition).getTop() + i2);
                            return;
                        }
                    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                        int[] findFirstVisibleItemPositions = ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions((int[]) null);
                        int[] findLastCompletelyVisibleItemPositions = ((StaggeredGridLayoutManager) layoutManager).findLastCompletelyVisibleItemPositions((int[]) null);
                        if (i >= findFirstVisibleItemPositions[0] && i <= findLastCompletelyVisibleItemPositions[0]) {
                            smoothScrollBy(0, getChildAt(i - findFirstVisibleItemPositions[0]).getTop() + i2);
                            return;
                        }
                    }
                }
                smoothScrollToPosition(i);
                if (i2 != 0) {
                    setOnSmoothScrollEndListener(new ExtendedLinearLayoutManager.OnSmoothScrollEndListener() {
                        public void onStop() {
                            WXRecyclerView.this.post(WXThread.secure((Runnable) new Runnable() {
                                public void run() {
                                    if (i3 == 1) {
                                        WXRecyclerView.this.smoothScrollBy(0, i2);
                                    } else {
                                        WXRecyclerView.this.smoothScrollBy(i2, 0);
                                    }
                                }
                            }));
                        }
                    });
                }
            } else if (layoutManager instanceof LinearLayoutManager) {
                ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(i, -i2);
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                ((StaggeredGridLayoutManager) layoutManager).scrollToPositionWithOffset(i, -i2);
            }
        } catch (Exception unused) {
        }
    }

    public void scrollTo(final int i, final int i2, final int i3) {
        postDelayed(WXThread.secure((Runnable) new Runnable() {
            public void run() {
                int i;
                RecyclerView.LayoutManager layoutManager = WXRecyclerView.this.getLayoutManager();
                if (i3 == 1) {
                    i = i2;
                } else {
                    i = i;
                }
                if (layoutManager instanceof LinearLayoutManager) {
                    ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(0, -i);
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    ((StaggeredGridLayoutManager) layoutManager).scrollToPositionWithOffset(0, -i);
                }
            }
        }), 100);
    }

    public void setOnSmoothScrollEndListener(final ExtendedLinearLayoutManager.OnSmoothScrollEndListener onSmoothScrollEndListener) {
        addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                if (i == 0) {
                    recyclerView.removeOnScrollListener(this);
                    ExtendedLinearLayoutManager.OnSmoothScrollEndListener onSmoothScrollEndListener = onSmoothScrollEndListener;
                    if (onSmoothScrollEndListener != null) {
                        onSmoothScrollEndListener.onStop();
                    }
                }
            }
        });
    }

    private WXRecyclerView getParentRecyclerView() {
        WXComponent wXComponentById;
        WXListComponent listComponent;
        JSONObject jSONObject = this.NestInfo;
        if (jSONObject == null || this.isNestParent || (wXComponentById = WXSDKManager.getInstance().getWXRenderManager().getWXComponentById(this.mInstanceId, jSONObject.getString("listParentId"))) == null || (listComponent = getListComponent(wXComponentById)) == null || listComponent.getHostView() == null) {
            return null;
        }
        return (WXRecyclerView) ((BounceRecyclerView) listComponent.getHostView()).getInnerView();
    }

    private WXRecyclerView getChildRecylerView() {
        WXComponent wXComponent;
        WXListComponent listComponent;
        JSONObject jSONObject = this.NestInfo;
        if (jSONObject == null || !this.isNestParent) {
            return null;
        }
        if (jSONObject.getBooleanValue("isSwipelist")) {
            wXComponent = WXSDKManager.getInstance().getWXRenderManager().getWXComponentById(this.mInstanceId, this.NestInfo.getString("swipeId"));
            if (!(wXComponent == null || (wXComponent = getDCWXSliderComponent(wXComponent)) == null || !(wXComponent instanceof DCWXSlider))) {
                DCWXSlider dCWXSlider = (DCWXSlider) wXComponent;
                wXComponent = dCWXSlider.getChild(dCWXSlider.getCurrentIndex());
            }
        } else {
            wXComponent = WXSDKManager.getInstance().getWXRenderManager().getWXComponent(this.mInstanceId, this.NestInfo.getString("nestChildRef"));
        }
        if (wXComponent == null || (listComponent = getListComponent(wXComponent)) == null || listComponent.getHostView() == null) {
            return null;
        }
        return (WXRecyclerView) ((BounceRecyclerView) listComponent.getHostView()).getInnerView();
    }

    public WXListComponent getListComponent(WXComponent wXComponent) {
        if (wXComponent instanceof WXListComponent) {
            return (WXListComponent) wXComponent;
        }
        if (!(wXComponent instanceof WXVContainer)) {
            return null;
        }
        WXVContainer wXVContainer = (WXVContainer) wXComponent;
        if (wXVContainer.getChildCount() <= 0) {
            return null;
        }
        for (int i = 0; i < wXVContainer.getChildCount(); i++) {
            WXListComponent listComponent = getListComponent(wXVContainer.getChild(i));
            if (listComponent != null) {
                return listComponent;
            }
        }
        return null;
    }

    public DCWXSlider getDCWXSliderComponent(WXComponent wXComponent) {
        if (wXComponent instanceof DCWXSlider) {
            return (DCWXSlider) wXComponent;
        }
        if (!(wXComponent instanceof WXVContainer)) {
            return null;
        }
        WXVContainer wXVContainer = (WXVContainer) wXComponent;
        if (wXVContainer.getChildCount() <= 0) {
            return null;
        }
        for (int i = 0; i < wXVContainer.getChildCount(); i++) {
            DCWXSlider dCWXSliderComponent = getDCWXSliderComponent(wXVContainer.getChild(i));
            if (dCWXSliderComponent != null) {
                return dCWXSliderComponent;
            }
        }
        return null;
    }

    public boolean fling(int i, int i2) {
        if (!isAttachedToWindow()) {
            return false;
        }
        boolean fling = super.fling(i, i2);
        if (this.NestInfo != null) {
            if (this.isNestParent) {
                if (!fling || i2 <= 0) {
                    this.velocityY = 0;
                } else {
                    this.isStartFling = true;
                    this.velocityY = i2;
                }
            } else if (!fling || i2 >= 0) {
                this.velocityY = 0;
            } else {
                this.isStartFling = true;
                this.velocityY = i2;
            }
        }
        return fling;
    }

    public boolean isNestScroll() {
        return this.NestInfo != null;
    }

    public boolean isScrollTop() {
        return !canScrollVertically(-1);
    }

    private boolean isScrollEnd() {
        return true ^ canScrollVertically(1);
    }

    /* access modifiers changed from: private */
    public void dispatchChildFling() {
        int i;
        if (isScrollEnd() && (i = this.velocityY) != 0) {
            double splineFlingDistance = this.mFlingHelper.getSplineFlingDistance(i);
            int i2 = this.totalDy;
            if (splineFlingDistance > ((double) i2)) {
                FlingHelper flingHelper = this.mFlingHelper;
                double d = (double) i2;
                Double.isNaN(d);
                childFling(flingHelper.getVelocityByDistance(splineFlingDistance - d));
            }
        }
        this.totalDy = 0;
        this.velocityY = 0;
    }

    /* access modifiers changed from: private */
    public void dispatchParentFling() {
        int i;
        WXRecyclerView parentRecyclerView = getParentRecyclerView();
        if (parentRecyclerView != null && isScrollTop() && (i = this.velocityY) != 0) {
            double splineFlingDistance = this.mFlingHelper.getSplineFlingDistance(i);
            if (splineFlingDistance > ((double) Math.abs(this.totalDy))) {
                FlingHelper flingHelper = this.mFlingHelper;
                double d = (double) this.totalDy;
                Double.isNaN(d);
                parentRecyclerView.fling(0, -flingHelper.getVelocityByDistance(splineFlingDistance + d));
            }
            this.totalDy = 0;
            this.velocityY = 0;
        }
    }

    private void childFling(int i) {
        WXRecyclerView childRecylerView = getChildRecylerView();
        if (childRecylerView != null) {
            childRecylerView.fling(0, i);
        }
    }

    public void scrollToPosition(final int i) {
        if (!this.isNestParent || this.NestInfo == null) {
            super.scrollToPosition(i);
            return;
        }
        WXRecyclerView childRecylerView = getChildRecylerView();
        if (childRecylerView != null) {
            childRecylerView.scrollToPosition(i);
        }
        postDelayed(new Runnable() {
            public void run() {
                WXRecyclerView.super.scrollToPosition(i);
            }
        }, 50);
    }

    public boolean onStartNestedScroll(View view, View view2, int i) {
        if (!this.isNestParent || this.NestInfo == null || view2 == null || !(view2 instanceof WXRecyclerView) || ((WXRecyclerView) view2).isNestParent) {
            return super.onStartNestedScroll(view, view2, i);
        }
        return true;
    }

    public void onNestedPreScroll(View view, int i, int i2, int[] iArr) {
        if (this.isNestParent && this.NestInfo != null) {
            WXRecyclerView childRecylerView = getChildRecylerView();
            boolean z = i2 > 0 && !isScrollEnd();
            boolean z2 = i2 < 0 && childRecylerView != null && childRecylerView.isScrollTop();
            if (z || z2) {
                scrollBy(0, i2);
                iArr[1] = i2;
            }
        }
    }

    public boolean onNestedFling(View view, float f, float f2, boolean z) {
        if (!this.isNestParent || this.NestInfo == null) {
            return super.onNestedFling(view, f, f2, z);
        }
        return true;
    }

    public boolean onNestedPreFling(View view, float f, float f2) {
        if (!this.isNestParent || this.NestInfo == null) {
            return super.onNestedPreFling(view, f, f2);
        }
        WXRecyclerView childRecylerView = getChildRecylerView();
        boolean z = f2 > 0.0f && !isScrollEnd();
        boolean z2 = f2 < 0.0f && childRecylerView != null && childRecylerView.isScrollTop();
        if (!z && !z2) {
            return false;
        }
        fling(0, (int) f2);
        return true;
    }

    public boolean pointInView(float f, float f2, float f3) {
        float f4 = -f3;
        return f >= f4 && f2 >= f4 && f < ((float) (getRight() - getLeft())) + f3 && f2 < ((float) (getBottom() - getTop())) + f3;
    }

    /* access modifiers changed from: protected */
    public void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
        AppEventForBlurManager.onScrollChanged(i, i2);
    }
}
