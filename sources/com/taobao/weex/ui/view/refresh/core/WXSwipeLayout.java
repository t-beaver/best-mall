package com.taobao.weex.ui.view.refresh.core;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewParentCompat;
import com.taobao.weex.ui.view.listview.WXRecyclerView;
import java.util.LinkedList;
import java.util.List;

public class WXSwipeLayout extends FrameLayout implements NestedScrollingParent, NestedScrollingChild {
    private static final float DAMPING = 0.4f;
    private static final int INVALID = -1;
    private static final int LOAD_MORE = 1;
    private static final int PULL_REFRESH = 0;
    private static final float overFlow = 1.0f;
    /* access modifiers changed from: private */
    public WXRefreshView footerView;
    /* access modifiers changed from: private */
    public WXRefreshView headerView;
    private boolean isConfirm = false;
    private volatile float loadingViewFlowHeight = 0.0f;
    private volatile float loadingViewHeight = 0.0f;
    private int mCurrentAction = -1;
    private ViewParent mNestedScrollAcceptedParent;
    private boolean mNestedScrollInProgress;
    private NestedScrollingChildHelper mNestedScrollingChildHelper;
    private NestedScrollingParentHelper mNestedScrollingParentHelper;
    private final int[] mParentOffsetInWindow = new int[2];
    private final int[] mParentScrollConsumed = new int[2];
    private int mProgressBgColor;
    private int mProgressColor;
    private boolean mPullLoadEnable = false;
    private boolean mPullRefreshEnable = false;
    private final List<OnRefreshOffsetChangedListener> mRefreshOffsetChangedListeners = new LinkedList();
    private int mRefreshViewBgColor;
    private volatile boolean mRefreshing = false;
    private View mTargetView;
    /* access modifiers changed from: private */
    public WXOnLoadingListener onLoadingListener;
    /* access modifiers changed from: private */
    public WXOnRefreshListener onRefreshListener;
    private volatile float refreshViewFlowHeight = 0.0f;
    private volatile float refreshViewHeight = 0.0f;

    public interface OnRefreshOffsetChangedListener {
        void onOffsetChanged(int i);
    }

    public interface WXOnLoadingListener {
        void onLoading();

        void onPullingUp(float f, int i, float f2);
    }

    public interface WXOnRefreshListener {
        void onPullingDown(float f, int i, float f2);

        void onRefresh();
    }

    static class WXRefreshAnimatorListener implements Animator.AnimatorListener {
        public void onAnimationCancel(Animator animator) {
        }

        public void onAnimationEnd(Animator animator) {
        }

        public void onAnimationRepeat(Animator animator) {
        }

        public void onAnimationStart(Animator animator) {
        }

        WXRefreshAnimatorListener() {
        }
    }

    public WXSwipeLayout(Context context) {
        super(context);
        initAttrs(context, (AttributeSet) null);
    }

    public WXSwipeLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initAttrs(context, attributeSet);
    }

    public WXSwipeLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initAttrs(context, attributeSet);
    }

    public WXSwipeLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        initAttrs(context, attributeSet);
    }

    private void initAttrs(Context context, AttributeSet attributeSet) {
        if (getChildCount() <= 1) {
            this.mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
            this.mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
            setNestedScrollingEnabled(false);
            if (!isInEditMode() || attributeSet != null) {
                this.mRefreshViewBgColor = 0;
                this.mProgressBgColor = 0;
                this.mProgressColor = -65536;
                return;
            }
            return;
        }
        throw new RuntimeException("WXSwipeLayout should not have more than one child");
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mTargetView == null && getChildCount() > 0) {
            this.mTargetView = getChildAt(0);
        }
        if (this.mTargetView == null) {
            return;
        }
        if (this.headerView == null || this.footerView == null) {
            setRefreshView();
        }
    }

    public void addTargetView(View view) {
        addView(view, new FrameLayout.LayoutParams(-1, -1));
        setRefreshView();
    }

    private void setRefreshView() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, 0);
        WXRefreshView wXRefreshView = new WXRefreshView(getContext());
        this.headerView = wXRefreshView;
        wXRefreshView.setStartEndTrim(0.0f, 0.75f);
        this.headerView.setBackgroundColor(this.mRefreshViewBgColor);
        this.headerView.setProgressBgColor(this.mProgressBgColor);
        this.headerView.setProgressColor(this.mProgressColor);
        this.headerView.setContentGravity(80);
        addView(this.headerView, layoutParams);
        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(-1, 0);
        layoutParams2.gravity = 80;
        WXRefreshView wXRefreshView2 = new WXRefreshView(getContext());
        this.footerView = wXRefreshView2;
        wXRefreshView2.setStartEndTrim(0.5f, 1.25f);
        this.footerView.setBackgroundColor(this.mRefreshViewBgColor);
        this.footerView.setProgressBgColor(this.mProgressBgColor);
        this.footerView.setProgressColor(this.mProgressColor);
        this.footerView.setContentGravity(48);
        addView(this.footerView, layoutParams2);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if ((this.mPullRefreshEnable || this.mPullLoadEnable) && isEnabled() && !canChildScrollUp() && !this.mRefreshing && !this.mNestedScrollInProgress) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        return false;
    }

    public void setNestedScrollingEnabled(boolean z) {
        this.mNestedScrollingChildHelper.setNestedScrollingEnabled(z);
    }

    public boolean isNestedScrollingEnabled() {
        return this.mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    public boolean startNestedScroll(int i) {
        boolean startNestedScroll = this.mNestedScrollingChildHelper.startNestedScroll(i);
        if (startNestedScroll && this.mNestedScrollAcceptedParent == null) {
            ViewParent parent = getParent();
            View view = this;
            while (true) {
                if (parent == null) {
                    break;
                } else if (ViewParentCompat.onStartNestedScroll(parent, view, this, i)) {
                    this.mNestedScrollAcceptedParent = parent;
                    break;
                } else {
                    if (parent instanceof View) {
                        view = (View) parent;
                    }
                    parent = parent.getParent();
                }
            }
        }
        return startNestedScroll;
    }

    public void stopNestedScroll() {
        this.mNestedScrollingChildHelper.stopNestedScroll();
        if (this.mNestedScrollAcceptedParent != null) {
            this.mNestedScrollAcceptedParent = null;
        }
    }

    public boolean hasNestedScrollingParent() {
        return this.mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    public boolean dispatchNestedScroll(int i, int i2, int i3, int i4, int[] iArr) {
        return this.mNestedScrollingChildHelper.dispatchNestedScroll(i, i2, i3, i4, iArr);
    }

    public boolean dispatchNestedPreScroll(int i, int i2, int[] iArr, int[] iArr2) {
        return this.mNestedScrollingChildHelper.dispatchNestedPreScroll(i, i2, iArr, iArr2);
    }

    public boolean dispatchNestedFling(float f, float f2, boolean z) {
        return this.mNestedScrollingChildHelper.dispatchNestedFling(f, f2, z);
    }

    public boolean dispatchNestedPreFling(float f, float f2) {
        return this.mNestedScrollingChildHelper.dispatchNestedPreFling(f, f2);
    }

    public boolean onNestedPreFling(View view, float f, float f2) {
        if (isNestedScrollingEnabled()) {
            return dispatchNestedPreFling(f, f2);
        }
        return false;
    }

    public boolean onNestedFling(View view, float f, float f2, boolean z) {
        if (isNestedScrollingEnabled()) {
            return dispatchNestedFling(f, f2, z);
        }
        return false;
    }

    public boolean onStartNestedScroll(View view, View view2, int i) {
        boolean z = true;
        boolean z2 = isEnabled() && !this.mRefreshing && (i & 2) != 0;
        if (!(view2 instanceof WXRecyclerView)) {
            return z2;
        }
        WXRecyclerView wXRecyclerView = (WXRecyclerView) view2;
        if (!wXRecyclerView.isNestScroll()) {
            return z2;
        }
        if (!z2 || !wXRecyclerView.isScrollTop()) {
            z = false;
        }
        return z;
    }

    public void onNestedScrollAccepted(View view, View view2, int i) {
        this.mNestedScrollingParentHelper.onNestedScrollAccepted(view, view2, i);
        if (isNestedScrollingEnabled()) {
            startNestedScroll(i & 2);
            this.mNestedScrollInProgress = true;
        }
    }

    public void onNestedPreScroll(View view, int i, int i2, int[] iArr) {
        ViewParent viewParent;
        int[] iArr2 = this.mParentScrollConsumed;
        if (isNestedScrollingEnabled() && dispatchNestedPreScroll(i - iArr[0], i2 - iArr[1], iArr2, (int[]) null)) {
            iArr[0] = iArr[0] + iArr2[0];
            iArr[1] = iArr[1] + iArr2[1];
        } else if (this.mPullRefreshEnable || this.mPullLoadEnable) {
            if (!canChildScrollUp() && isNestedScrollingEnabled() && (viewParent = this.mNestedScrollAcceptedParent) != null && viewParent != this.mTargetView) {
                ViewGroup viewGroup = (ViewGroup) viewParent;
                if (viewGroup.getChildCount() > 0) {
                    int childCount = viewGroup.getChildCount();
                    int i3 = 0;
                    while (true) {
                        if (i3 >= childCount) {
                            break;
                        }
                        View childAt = viewGroup.getChildAt(i3);
                        if (childAt.getVisibility() == 8 || childAt.getMeasuredHeight() <= 0) {
                            i3++;
                        } else if (childAt.getTop() < 0) {
                            return;
                        }
                    }
                }
            }
            int calculateDistanceY = (int) calculateDistanceY(view, i2);
            this.mRefreshing = false;
            if (!this.isConfirm) {
                if (calculateDistanceY < 0 && !canChildScrollUp()) {
                    this.mCurrentAction = 0;
                    this.isConfirm = true;
                } else if (calculateDistanceY > 0 && !canChildScrollDown() && !this.mRefreshing) {
                    this.mCurrentAction = 1;
                    this.isConfirm = true;
                }
            }
            if (!moveSpinner((float) (-calculateDistanceY))) {
                return;
            }
            if (!canChildScrollUp() && this.mPullRefreshEnable && this.mTargetView.getTranslationY() > 0.0f && i2 > 0) {
                iArr[1] = iArr[1] + i2;
            } else if (canChildScrollDown() || !this.mPullLoadEnable || this.mTargetView.getTranslationY() >= 0.0f || i2 >= 0) {
                iArr[1] = iArr[1] + calculateDistanceY;
            } else {
                iArr[1] = iArr[1] + i2;
            }
        }
    }

    public int getNestedScrollAxes() {
        return this.mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    public void onStopNestedScroll(View view) {
        this.mNestedScrollingParentHelper.onStopNestedScroll(view);
        handlerAction();
        if (isNestedScrollingEnabled()) {
            this.mNestedScrollInProgress = true;
            stopNestedScroll();
        }
    }

    public void onNestedScroll(View view, int i, int i2, int i3, int i4) {
        if (isNestedScrollingEnabled()) {
            dispatchNestedScroll(i, i2, i3, i4, this.mParentOffsetInWindow);
        }
    }

    private double calculateDistanceY(View view, int i) {
        int measuredHeight = view.getMeasuredHeight();
        double abs = (double) (((float) measuredHeight) - Math.abs(view.getY()));
        Double.isNaN(abs);
        double d = (double) measuredHeight;
        Double.isNaN(d);
        double d2 = ((abs / 1.0d) / d) * 0.4000000059604645d;
        if (d2 <= 0.01d) {
            d2 = 0.01d;
        }
        double d3 = (double) i;
        Double.isNaN(d3);
        return d2 * d3;
    }

    private boolean moveSpinner(float f) {
        if (this.mRefreshing) {
            return false;
        }
        if (!canChildScrollUp() && this.mPullRefreshEnable && this.mCurrentAction == 0) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.headerView.getLayoutParams();
            layoutParams.height = (int) (((float) layoutParams.height) + f);
            if (layoutParams.height < 0) {
                layoutParams.height = 0;
            }
            if (layoutParams.height == 0) {
                this.isConfirm = false;
                this.mCurrentAction = -1;
            }
            this.headerView.setLayoutParams(layoutParams);
            this.onRefreshListener.onPullingDown(f, layoutParams.height, this.refreshViewFlowHeight);
            notifyOnRefreshOffsetChangedListener(layoutParams.height);
            this.headerView.setProgressRotation(((float) layoutParams.height) / this.refreshViewFlowHeight);
            moveTargetView((float) layoutParams.height);
            return true;
        } else if (canChildScrollDown() || !this.mPullLoadEnable || this.mCurrentAction != 1) {
            return false;
        } else {
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.footerView.getLayoutParams();
            layoutParams2.height = (int) (((float) layoutParams2.height) - f);
            if (layoutParams2.height < 0) {
                layoutParams2.height = 0;
            }
            if (layoutParams2.height == 0) {
                this.isConfirm = false;
                this.mCurrentAction = -1;
            }
            this.footerView.setLayoutParams(layoutParams2);
            this.onLoadingListener.onPullingUp(f, layoutParams2.height, this.loadingViewFlowHeight);
            this.footerView.setProgressRotation(((float) layoutParams2.height) / this.loadingViewFlowHeight);
            moveTargetView((float) (-layoutParams2.height));
            return true;
        }
    }

    /* access modifiers changed from: private */
    public void moveTargetView(float f) {
        this.mTargetView.setTranslationY(f);
    }

    private void handlerAction() {
        if (!isRefreshing()) {
            this.isConfirm = false;
            if (this.mPullRefreshEnable && this.mCurrentAction == 0) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.headerView.getLayoutParams();
                if (((float) layoutParams.height) >= this.refreshViewHeight) {
                    startRefresh(layoutParams.height);
                } else if (layoutParams.height > 0) {
                    resetHeaderView(layoutParams.height);
                } else {
                    resetRefreshState();
                }
            }
            if (this.mPullLoadEnable && this.mCurrentAction == 1) {
                FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.footerView.getLayoutParams();
                if (((float) layoutParams2.height) >= this.loadingViewHeight) {
                    startLoadmore(layoutParams2.height);
                } else if (layoutParams2.height > 0) {
                    resetFootView(layoutParams2.height);
                } else {
                    resetLoadmoreState();
                }
            }
        }
    }

    private void startRefresh(int i) {
        this.mRefreshing = true;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{(float) i, this.refreshViewHeight});
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) WXSwipeLayout.this.headerView.getLayoutParams();
                layoutParams.height = (int) ((Float) valueAnimator.getAnimatedValue()).floatValue();
                WXSwipeLayout.this.notifyOnRefreshOffsetChangedListener(layoutParams.height);
                WXSwipeLayout.this.headerView.setLayoutParams(layoutParams);
                WXSwipeLayout.this.moveTargetView((float) layoutParams.height);
            }
        });
        ofFloat.addListener(new WXRefreshAnimatorListener() {
            public void onAnimationEnd(Animator animator) {
                WXSwipeLayout.this.headerView.startAnimation();
                if (WXSwipeLayout.this.onRefreshListener != null) {
                    WXSwipeLayout.this.onRefreshListener.onRefresh();
                }
            }
        });
        ofFloat.setDuration(300);
        ofFloat.start();
    }

    private void resetHeaderView(int i) {
        this.headerView.stopAnimation();
        this.headerView.setStartEndTrim(0.0f, 0.75f);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{(float) i, 0.0f});
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) WXSwipeLayout.this.headerView.getLayoutParams();
                layoutParams.height = (int) ((Float) valueAnimator.getAnimatedValue()).floatValue();
                WXSwipeLayout.this.notifyOnRefreshOffsetChangedListener(layoutParams.height);
                WXSwipeLayout.this.headerView.setLayoutParams(layoutParams);
                WXSwipeLayout.this.moveTargetView((float) layoutParams.height);
            }
        });
        ofFloat.addListener(new WXRefreshAnimatorListener() {
            public void onAnimationEnd(Animator animator) {
                WXSwipeLayout.this.resetRefreshState();
            }
        });
        ofFloat.setDuration(300);
        ofFloat.start();
    }

    /* access modifiers changed from: private */
    public void resetRefreshState() {
        this.mRefreshing = false;
        this.isConfirm = false;
        this.mCurrentAction = -1;
    }

    private void startLoadmore(int i) {
        this.mRefreshing = true;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{(float) i, this.loadingViewHeight});
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) WXSwipeLayout.this.footerView.getLayoutParams();
                layoutParams.height = (int) ((Float) valueAnimator.getAnimatedValue()).floatValue();
                WXSwipeLayout.this.footerView.setLayoutParams(layoutParams);
                WXSwipeLayout.this.moveTargetView((float) (-layoutParams.height));
            }
        });
        ofFloat.addListener(new WXRefreshAnimatorListener() {
            public void onAnimationEnd(Animator animator) {
                WXSwipeLayout.this.footerView.startAnimation();
                if (WXSwipeLayout.this.onLoadingListener != null) {
                    WXSwipeLayout.this.onLoadingListener.onLoading();
                }
            }
        });
        ofFloat.setDuration(300);
        ofFloat.start();
    }

    private void resetFootView(int i) {
        this.footerView.stopAnimation();
        this.footerView.setStartEndTrim(0.5f, 1.25f);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{(float) i, 0.0f});
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) WXSwipeLayout.this.footerView.getLayoutParams();
                layoutParams.height = (int) ((Float) valueAnimator.getAnimatedValue()).floatValue();
                WXSwipeLayout.this.footerView.setLayoutParams(layoutParams);
                WXSwipeLayout.this.moveTargetView((float) (-layoutParams.height));
            }
        });
        ofFloat.addListener(new WXRefreshAnimatorListener() {
            public void onAnimationEnd(Animator animator) {
                WXSwipeLayout.this.resetLoadmoreState();
            }
        });
        ofFloat.setDuration(300);
        ofFloat.start();
    }

    /* access modifiers changed from: private */
    public void resetLoadmoreState() {
        this.mRefreshing = false;
        this.isConfirm = false;
        this.mCurrentAction = -1;
    }

    public boolean canChildScrollUp() {
        if (this.mTargetView == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= 14) {
            return ViewCompat.canScrollVertically(this.mTargetView, -1);
        }
        View view = this.mTargetView;
        if (view instanceof AbsListView) {
            AbsListView absListView = (AbsListView) view;
            if (absListView.getChildCount() <= 0) {
                return false;
            }
            if (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop()) {
                return true;
            }
            return false;
        } else if (ViewCompat.canScrollVertically(view, -1) || this.mTargetView.getScrollY() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean canChildScrollDown() {
        if (this.mTargetView == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= 14) {
            return ViewCompat.canScrollVertically(this.mTargetView, 1);
        }
        View view = this.mTargetView;
        if (view instanceof AbsListView) {
            AbsListView absListView = (AbsListView) view;
            if (absListView.getChildCount() <= 0) {
                return false;
            }
            int bottom = absListView.getChildAt(absListView.getChildCount() - 1).getBottom();
            if (absListView.getLastVisiblePosition() != ((ListAdapter) absListView.getAdapter()).getCount() - 1 || bottom > absListView.getMeasuredHeight()) {
                return false;
            }
            return true;
        } else if (ViewCompat.canScrollVertically(view, 1) || this.mTargetView.getScrollY() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public float dipToPx(Context context, float f) {
        return TypedValue.applyDimension(1, f, context.getResources().getDisplayMetrics());
    }

    public void setOnLoadingListener(WXOnLoadingListener wXOnLoadingListener) {
        this.onLoadingListener = wXOnLoadingListener;
    }

    public void setOnRefreshListener(WXOnRefreshListener wXOnRefreshListener) {
        this.onRefreshListener = wXOnRefreshListener;
    }

    public void addOnRefreshOffsetChangedListener(OnRefreshOffsetChangedListener onRefreshOffsetChangedListener) {
        if (onRefreshOffsetChangedListener != null && !this.mRefreshOffsetChangedListeners.contains(onRefreshOffsetChangedListener)) {
            this.mRefreshOffsetChangedListeners.add(onRefreshOffsetChangedListener);
        }
    }

    public boolean removeOnRefreshOffsetChangedListener(OnRefreshOffsetChangedListener onRefreshOffsetChangedListener) {
        if (onRefreshOffsetChangedListener != null) {
            return this.mRefreshOffsetChangedListeners.remove(onRefreshOffsetChangedListener);
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void notifyOnRefreshOffsetChangedListener(int i) {
        int size = this.mRefreshOffsetChangedListeners.size();
        int i2 = 0;
        while (i2 < size && i2 < this.mRefreshOffsetChangedListeners.size()) {
            OnRefreshOffsetChangedListener onRefreshOffsetChangedListener = this.mRefreshOffsetChangedListeners.get(i2);
            if (onRefreshOffsetChangedListener != null) {
                onRefreshOffsetChangedListener.onOffsetChanged(i);
            }
            i2++;
        }
    }

    public void finishPullRefresh() {
        if (this.mCurrentAction == 0) {
            WXRefreshView wXRefreshView = this.headerView;
            resetHeaderView(wXRefreshView == null ? 0 : wXRefreshView.getMeasuredHeight());
        }
    }

    public void finishPullLoad() {
        if (this.mCurrentAction == 1) {
            WXRefreshView wXRefreshView = this.footerView;
            resetFootView(wXRefreshView == null ? 0 : wXRefreshView.getMeasuredHeight());
        }
    }

    public WXRefreshView getHeaderView() {
        return this.headerView;
    }

    public WXRefreshView getFooterView() {
        return this.footerView;
    }

    public boolean isPullLoadEnable() {
        return this.mPullLoadEnable;
    }

    public void setPullLoadEnable(boolean z) {
        this.mPullLoadEnable = z;
    }

    public boolean isPullRefreshEnable() {
        return this.mPullRefreshEnable;
    }

    public void setPullRefreshEnable(boolean z) {
        this.mPullRefreshEnable = z;
    }

    public boolean isRefreshing() {
        return this.mRefreshing;
    }

    public void setRefreshHeight(int i) {
        this.refreshViewHeight = (float) i;
        this.refreshViewFlowHeight = this.refreshViewHeight * overFlow;
    }

    public void setLoadingHeight(int i) {
        this.loadingViewHeight = (float) i;
        this.loadingViewFlowHeight = this.loadingViewHeight * overFlow;
    }

    public void setRefreshBgColor(int i) {
        this.headerView.setBackgroundColor(i);
    }

    public void setLoadingBgColor(int i) {
        this.footerView.setBackgroundColor(i);
    }
}
