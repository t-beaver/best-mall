package io.dcloud.common.adapter.ui.fresh;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import io.dcloud.common.adapter.ui.fresh.ILoadingLayout;
import io.dcloud.common.adapter.util.Logger;

public abstract class PullToRefreshBase<T extends View> extends LinearLayout implements IPullToRefresh<T> {
    private static final float OFFSET_RADIO = 2.5f;
    private static final int SCROLL_DURATION = 150;
    final int DOWN;
    final int LEFT;
    final int RIGHT;
    final int UP;
    private String mAppId;
    boolean mBeginPullRefresh;
    private boolean mCanDoPullDownEvent = false;
    private int mFooterHeight;
    /* access modifiers changed from: private */
    public LoadingLayout mFooterLayout;
    /* access modifiers changed from: private */
    public int mHeaderHeight;
    /* access modifiers changed from: private */
    public LoadingLayout mHeaderLayout;
    private int mHeaderPullDownMaxHeight;
    private boolean mInterceptEventEnable = true;
    private boolean mIsHandledTouchEvent = false;
    private float mLastMotionX = -1.0f;
    private float mLastMotionY = -1.0f;
    float mLastMotionY_pullup;
    OnPullUpListener mOnPullUpListener;
    OnStateChangeListener mOnStateChangeListener;
    private ILoadingLayout.State mPullDownState;
    private boolean mPullLoadEnabled = false;
    private boolean mPullRefreshEnabled = true;
    private ILoadingLayout.State mPullUpState;
    /* access modifiers changed from: private */
    public OnRefreshListener<T> mRefreshListener;
    T mRefreshableView;
    private boolean mScrollLoadEnabled = false;
    private PullToRefreshBase<T>.SmoothScrollRunnable mSmoothScrollRunnable;
    private int mTouchSlop;

    public interface OnPullUpListener {
        void onPlusScrollBottom();
    }

    public interface OnRefreshListener<V extends View> {
        void onPullDownToRefresh(PullToRefreshBase<V> pullToRefreshBase);

        void onPullUpToRefresh(PullToRefreshBase<V> pullToRefreshBase);
    }

    public interface OnStateChangeListener {
        void onStateChanged(ILoadingLayout.State state, boolean z);
    }

    final class SmoothScrollRunnable implements Runnable {
        private boolean mContinueRunning = true;
        private int mCurrentY = -1;
        private final long mDuration;
        private final Interpolator mInterpolator;
        private final int mScrollFromY;
        private final int mScrollToY;
        private long mStartTime = -1;

        public SmoothScrollRunnable(int i, int i2, long j) {
            this.mScrollFromY = i;
            this.mScrollToY = i2;
            this.mDuration = j;
            this.mInterpolator = new DecelerateInterpolator();
        }

        public void run() {
            if (this.mDuration <= 0) {
                PullToRefreshBase.this.setScrollTo(0, this.mScrollToY);
                return;
            }
            if (this.mStartTime == -1) {
                this.mStartTime = System.currentTimeMillis();
            } else {
                int round = this.mScrollFromY - Math.round(((float) (this.mScrollFromY - this.mScrollToY)) * this.mInterpolator.getInterpolation(((float) Math.max(Math.min(((System.currentTimeMillis() - this.mStartTime) * 1000) / this.mDuration, 1000), 0)) / 1000.0f));
                this.mCurrentY = round;
                PullToRefreshBase.this.setScrollTo(0, round);
            }
            if (this.mContinueRunning && this.mScrollToY != this.mCurrentY) {
                PullToRefreshBase.this.postDelayed(this, 16);
            }
        }

        public void stop() {
            this.mContinueRunning = false;
            PullToRefreshBase.this.removeCallbacks(this);
        }
    }

    public PullToRefreshBase(Context context) {
        super(context);
        ILoadingLayout.State state = ILoadingLayout.State.NONE;
        this.mPullDownState = state;
        this.mPullUpState = state;
        this.mLastMotionY_pullup = -1.0f;
        this.UP = 0;
        this.DOWN = 1;
        this.LEFT = 2;
        this.RIGHT = 3;
        this.mBeginPullRefresh = false;
    }

    private boolean canDoPullDownEvent(float f, float f2) {
        float f3 = this.mLastMotionY;
        boolean z = true;
        if (f2 < f3) {
            return true;
        }
        if (!this.mCanDoPullDownEvent) {
            if (1 != getDirectionByAngle(getAngle(this.mLastMotionX, f3, f, f2))) {
                z = false;
            }
            this.mCanDoPullDownEvent = z;
        }
        return this.mCanDoPullDownEvent;
    }

    private double getAngle(float f, float f2, float f3, float f4) {
        return (Math.atan2((double) (f4 - f2), (double) (f3 - f)) * 180.0d) / 3.141592653589793d;
    }

    private int getDirectionByAngle(double d) {
        if (d < -45.0d && d > -135.0d) {
            return 0;
        }
        if (d >= 45.0d && d < 135.0d) {
            return 1;
        }
        if (d >= 135.0d || d <= -135.0d) {
            return 2;
        }
        return (d < -45.0d || d > 45.0d) ? -1 : 3;
    }

    /* access modifiers changed from: private */
    public int getScrollYValue() {
        return getScrollY();
    }

    private boolean handlePullUpEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 1) {
            float y = motionEvent.getY() - this.mLastMotionY_pullup;
            this.mLastMotionY_pullup = y;
            if (y < -3.0f && isReadyForPullUp()) {
                this.mOnPullUpListener.onPlusScrollBottom();
                return false;
            }
        } else if (action == 0) {
            this.mLastMotionY_pullup = motionEvent.getY();
        }
        return false;
    }

    private void setScrollBy(int i, int i2) {
        scrollBy(i, i2);
    }

    /* access modifiers changed from: private */
    public void setScrollTo(int i, int i2) {
        scrollTo(i, i2);
    }

    /* access modifiers changed from: protected */
    public void addHeaderAndFooter(Context context) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        LoadingLayout loadingLayout = this.mHeaderLayout;
        LoadingLayout loadingLayout2 = this.mFooterLayout;
        if (loadingLayout != null) {
            if (this == loadingLayout.getParent()) {
                removeView(loadingLayout);
            }
            addView(loadingLayout, 0, layoutParams);
        }
        if (loadingLayout2 != null) {
            if (this == loadingLayout2.getParent()) {
                removeView(loadingLayout2);
            }
            addView(loadingLayout2, -1, layoutParams);
        }
    }

    public void addRefreshableView(T t) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
        onAddRefreshableView(layoutParams);
        addView(t, layoutParams);
    }

    public void beginPullRefresh() {
        if (!this.mBeginPullRefresh) {
            postDelayed(new Runnable() {
                int deltaY = 0;

                public void run() {
                    int abs = Math.abs(PullToRefreshBase.this.getScrollYValue());
                    if (PullToRefreshBase.this.isPullRefreshEnabled() && PullToRefreshBase.this.isReadyForPullDown()) {
                        if (abs < PullToRefreshBase.this.mHeaderHeight) {
                            PullToRefreshBase.this.pullHeaderLayout(((float) this.deltaY) / PullToRefreshBase.OFFSET_RADIO);
                            this.deltaY += 3;
                            PullToRefreshBase.this.postDelayed(this, 5);
                            return;
                        }
                        PullToRefreshBase.this.startRefreshing();
                        PullToRefreshBase.this.mBeginPullRefresh = false;
                    }
                }
            }, 5);
            this.mBeginPullRefresh = true;
        }
    }

    /* access modifiers changed from: protected */
    public LoadingLayout createFooterLoadingLayout(Context context) {
        return null;
    }

    /* access modifiers changed from: protected */
    public LoadingLayout createHeaderLoadingLayout(Context context) {
        LoadingLayout loadingLayout = this.mHeaderLayout;
        return loadingLayout == null ? new HeaderLoadingLayout(context) : loadingLayout;
    }

    public void doPullRefreshing(final boolean z, long j) {
        postDelayed(new Runnable() {
            public void run() {
                int i = -PullToRefreshBase.this.mHeaderHeight;
                int i2 = z ? PullToRefreshBase.SCROLL_DURATION : 0;
                PullToRefreshBase.this.startRefreshing();
                PullToRefreshBase.this.smoothScrollTo(i, (long) i2, 0);
            }
        }, j);
    }

    public String getAppId() {
        return this.mAppId;
    }

    public LoadingLayout getFooterLoadingLayout() {
        return this.mFooterLayout;
    }

    public LoadingLayout getHeaderLoadingLayout() {
        return this.mHeaderLayout;
    }

    public T getRefreshableView() {
        return this.mRefreshableView;
    }

    /* access modifiers changed from: protected */
    public long getSmoothScrollDuration() {
        return 150;
    }

    public void init(Context context) {
        setOrientation(1);
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.mHeaderLayout = createHeaderLoadingLayout(context);
        this.mFooterLayout = createFooterLoadingLayout(context);
        addHeaderAndFooter(context);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                PullToRefreshBase.this.refreshLoadingViewsSize();
                PullToRefreshBase.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        smoothScrollTo(0);
    }

    public boolean isInterceptTouchEventEnabled() {
        return this.mInterceptEventEnable;
    }

    public boolean isPullLoadEnabled() {
        return this.mPullLoadEnabled && this.mFooterLayout != null;
    }

    /* access modifiers changed from: protected */
    public boolean isPullLoading() {
        return this.mPullUpState == ILoadingLayout.State.REFRESHING;
    }

    public boolean isPullRefreshEnabled() {
        return this.mPullRefreshEnabled && this.mHeaderLayout != null;
    }

    /* access modifiers changed from: protected */
    public boolean isPullRefreshing() {
        return this.mPullDownState == ILoadingLayout.State.REFRESHING;
    }

    /* access modifiers changed from: protected */
    public abstract boolean isReadyForPullDown();

    /* access modifiers changed from: protected */
    public abstract boolean isReadyForPullUp();

    public boolean isScrollLoadEnabled() {
        return this.mScrollLoadEnabled;
    }

    /* access modifiers changed from: protected */
    public void onAddRefreshableView(LinearLayout.LayoutParams layoutParams) {
    }

    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean z = false;
        if (!isInterceptTouchEventEnabled()) {
            return false;
        }
        if (!isPullLoadEnabled() && !isPullRefreshEnabled()) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action == 3 || action == 1) {
            this.mIsHandledTouchEvent = false;
            this.mCanDoPullDownEvent = false;
            return false;
        } else if (action != 0 && this.mIsHandledTouchEvent) {
            return true;
        } else {
            if (action == 0) {
                this.mLastMotionY = motionEvent.getY();
                this.mLastMotionX = motionEvent.getX();
                this.mIsHandledTouchEvent = false;
                this.mCanDoPullDownEvent = false;
            } else if (action == 2 && canDoPullDownEvent(motionEvent.getX(), motionEvent.getY())) {
                float y = motionEvent.getY() - this.mLastMotionY;
                if (Math.abs(y) > ((float) this.mTouchSlop) || isPullRefreshing() || !isPullLoading()) {
                    this.mLastMotionY = motionEvent.getY();
                    if (isPullRefreshEnabled() && isReadyForPullDown()) {
                        if (Math.abs(getScrollYValue()) > 0 || y > 0.5f) {
                            z = true;
                        }
                        this.mIsHandledTouchEvent = z;
                        if (z) {
                            this.mRefreshableView.onTouchEvent(motionEvent);
                            requestDisallowInterceptTouchEvent(true);
                        }
                    } else if (isPullLoadEnabled() && isReadyForPullUp()) {
                        if (Math.abs(getScrollYValue()) > 0 || y < -0.5f) {
                            z = true;
                        }
                        this.mIsHandledTouchEvent = z;
                        if (z) {
                            requestDisallowInterceptTouchEvent(true);
                        }
                    }
                }
            }
            boolean z2 = this.mIsHandledTouchEvent;
            return z2 ? z2 : super.onTouchEvent(motionEvent);
        }
    }

    public void onPullDownRefreshComplete() {
        if (isPullRefreshing()) {
            ILoadingLayout.State state = ILoadingLayout.State.RESET;
            this.mPullDownState = state;
            onStateChanged(state, true);
            postDelayed(new Runnable() {
                public void run() {
                    PullToRefreshBase.this.setInterceptTouchEventEnabled(true);
                    PullToRefreshBase.this.mHeaderLayout.setState(ILoadingLayout.State.RESET);
                }
            }, getSmoothScrollDuration());
            resetHeaderLayout();
            setInterceptTouchEventEnabled(false);
        }
    }

    public void onPullUpRefreshComplete() {
        if (isPullLoading()) {
            ILoadingLayout.State state = ILoadingLayout.State.RESET;
            this.mPullUpState = state;
            onStateChanged(state, false);
            postDelayed(new Runnable() {
                public void run() {
                    PullToRefreshBase.this.setInterceptTouchEventEnabled(true);
                    PullToRefreshBase.this.mFooterLayout.setState(ILoadingLayout.State.RESET);
                }
            }, getSmoothScrollDuration());
            resetFooterLayout();
            setInterceptTouchEventEnabled(false);
        }
    }

    /* access modifiers changed from: protected */
    public final void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        refreshLoadingViewsSize();
        refreshRefreshableViewSize(i, i2);
        post(new Runnable() {
            public void run() {
                PullToRefreshBase.this.requestLayout();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onStateChanged(ILoadingLayout.State state, boolean z) {
        OnStateChangeListener onStateChangeListener = this.mOnStateChangeListener;
        if (onStateChangeListener != null) {
            onStateChangeListener.onStateChanged(state, z);
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!isInterceptTouchEventEnabled()) {
            return false;
        }
        int action = motionEvent.getAction();
        boolean z = true;
        if (action != 0) {
            if (action != 1) {
                if (action == 2) {
                    float y = motionEvent.getY() - this.mLastMotionY;
                    this.mLastMotionY = motionEvent.getY();
                    if (isPullRefreshEnabled() && isReadyForPullDown()) {
                        pullHeaderLayout(y / OFFSET_RADIO);
                    } else if (!isPullLoadEnabled() || !isReadyForPullUp()) {
                        this.mIsHandledTouchEvent = false;
                        return false;
                    } else {
                        pullFooterLayout(y / OFFSET_RADIO);
                    }
                    return true;
                } else if (action != 3) {
                    return false;
                }
            }
            if (this.mIsHandledTouchEvent) {
                this.mIsHandledTouchEvent = false;
                if (isReadyForPullDown()) {
                    if (!this.mPullRefreshEnabled || this.mPullDownState != ILoadingLayout.State.RELEASE_TO_REFRESH) {
                        z = false;
                    } else {
                        startRefreshing();
                    }
                    resetHeaderLayout();
                } else if (isReadyForPullUp()) {
                    if (!isPullLoadEnabled() || this.mPullUpState != ILoadingLayout.State.RELEASE_TO_REFRESH) {
                        z = false;
                    } else {
                        startLoading();
                    }
                    resetFooterLayout();
                }
                requestDisallowInterceptTouchEvent(false);
                return z;
            }
            z = false;
            requestDisallowInterceptTouchEvent(false);
            return z;
        }
        this.mLastMotionY = motionEvent.getY();
        this.mIsHandledTouchEvent = false;
        return false;
    }

    /* access modifiers changed from: protected */
    public void pullFooterLayout(float f) {
        int scrollYValue = getScrollYValue();
        if (f <= 0.0f || ((float) scrollYValue) - f > 0.0f) {
            setScrollBy(0, -((int) f));
            if (!(this.mFooterLayout == null || this.mFooterHeight == 0)) {
                this.mFooterLayout.onPull(((float) Math.abs(getScrollYValue())) / ((float) this.mFooterHeight));
            }
            int abs = Math.abs(getScrollYValue());
            if (isPullLoadEnabled() && !isPullLoading()) {
                if (abs >= this.mFooterHeight) {
                    this.mPullUpState = ILoadingLayout.State.RELEASE_TO_REFRESH;
                } else {
                    this.mPullUpState = ILoadingLayout.State.PULL_TO_REFRESH;
                }
                this.mFooterLayout.setState(this.mPullUpState);
                onStateChanged(this.mPullUpState, false);
                return;
            }
            return;
        }
        setScrollTo(0, 0);
    }

    /* access modifiers changed from: protected */
    public void pullHeaderLayout(float f) {
        int scrollYValue = getScrollYValue();
        if (f > 0.0f && Math.abs(scrollYValue) >= this.mHeaderPullDownMaxHeight) {
            return;
        }
        if (f >= 0.0f || ((float) scrollYValue) - f < 0.0f) {
            setScrollBy(0, -((int) f));
            if (!(this.mHeaderLayout == null || this.mHeaderHeight == 0)) {
                this.mHeaderLayout.onPull(((float) Math.abs(getScrollYValue())) / ((float) this.mHeaderHeight));
            }
            int abs = Math.abs(getScrollYValue());
            if (isPullRefreshEnabled() && !isPullRefreshing()) {
                if (abs >= this.mHeaderHeight) {
                    this.mPullDownState = ILoadingLayout.State.RELEASE_TO_REFRESH;
                } else {
                    this.mPullDownState = ILoadingLayout.State.PULL_TO_REFRESH;
                }
                this.mHeaderLayout.setState(this.mPullDownState);
                onStateChanged(this.mPullDownState, true);
                return;
            }
            return;
        }
        setScrollTo(0, 0);
    }

    public void refreshLoadingViewsSize() {
        int i = this.mHeaderHeight;
        LoadingLayout loadingLayout = this.mFooterLayout;
        int i2 = 0;
        int contentSize = loadingLayout != null ? loadingLayout.getContentSize() : 0;
        if (i < 0) {
            i = 0;
        }
        if (contentSize < 0) {
            contentSize = 0;
        }
        this.mHeaderHeight = i;
        this.mFooterHeight = contentSize;
        LoadingLayout loadingLayout2 = this.mHeaderLayout;
        int measuredHeight = loadingLayout2 != null ? loadingLayout2.getMeasuredHeight() : 0;
        Logger.d(Logger.VIEW_VISIBLE_TAG, "PullToRefreshBase.refreshLoadingViewsSize mHeaderHeight=" + this.mHeaderHeight + ";headerHeight=" + measuredHeight);
        LoadingLayout loadingLayout3 = this.mFooterLayout;
        if (loadingLayout3 != null) {
            i2 = loadingLayout3.getMeasuredHeight();
        }
        if (i2 == 0) {
            i2 = this.mFooterHeight;
        }
        int paddingLeft = getPaddingLeft();
        getPaddingTop();
        int paddingRight = getPaddingRight();
        getPaddingBottom();
        setPadding(paddingLeft, -measuredHeight, paddingRight, -i2);
    }

    /* access modifiers changed from: protected */
    public void refreshRefreshableViewSize(int i, int i2) {
        T t = this.mRefreshableView;
        if (t != null) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) t.getLayoutParams();
            if (layoutParams.height != i2) {
                layoutParams.height = i2;
                this.mRefreshableView.requestLayout();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void resetFooterLayout() {
        int abs = Math.abs(getScrollYValue());
        boolean isPullLoading = isPullLoading();
        if (isPullLoading && abs <= this.mFooterHeight) {
            smoothScrollTo(0);
        } else if (isPullLoading) {
            smoothScrollTo(this.mFooterHeight);
        } else {
            smoothScrollTo(0);
        }
    }

    /* access modifiers changed from: protected */
    public void resetHeaderLayout() {
        int abs = Math.abs(getScrollYValue());
        boolean isPullRefreshing = isPullRefreshing();
        if (isPullRefreshing && abs <= this.mHeaderHeight) {
            return;
        }
        if (isPullRefreshing) {
            smoothScrollTo(-this.mHeaderHeight);
        } else {
            smoothScrollTo(0);
        }
    }

    public void setAppId(String str) {
        this.mAppId = str;
    }

    public void setHeaderHeight(int i) {
        this.mHeaderHeight = i;
    }

    public void setHeaderPullDownMaxHeight(int i) {
        this.mHeaderPullDownMaxHeight = i;
    }

    public void setInterceptTouchEventEnabled(boolean z) {
        this.mInterceptEventEnable = z;
    }

    public void setLastUpdatedLabel(CharSequence charSequence) {
        LoadingLayout loadingLayout = this.mHeaderLayout;
        if (loadingLayout != null) {
            loadingLayout.setLastUpdatedLabel(charSequence);
        }
        LoadingLayout loadingLayout2 = this.mFooterLayout;
        if (loadingLayout2 != null) {
            loadingLayout2.setLastUpdatedLabel(charSequence);
        }
    }

    public void setOnOnPullUpListener(OnPullUpListener onPullUpListener) {
        this.mOnPullUpListener = onPullUpListener;
    }

    public void setOnRefreshListener(OnRefreshListener<T> onRefreshListener) {
        this.mRefreshListener = onRefreshListener;
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.mOnStateChangeListener = onStateChangeListener;
    }

    public void setOrientation(int i) {
        if (1 == i) {
            super.setOrientation(i);
            return;
        }
        throw new IllegalArgumentException("This class only supports VERTICAL orientation.");
    }

    public void setPullLoadEnabled(boolean z) {
        this.mPullLoadEnabled = z;
    }

    public void setPullRefreshEnabled(boolean z) {
        this.mPullRefreshEnabled = z;
    }

    public void setRefreshableView(T t) {
        this.mRefreshableView = t;
    }

    public void setScrollLoadEnabled(boolean z) {
        this.mScrollLoadEnabled = z;
    }

    public void smoothScrollTo(int i) {
        smoothScrollTo(i, getSmoothScrollDuration(), 0);
    }

    /* access modifiers changed from: protected */
    public void startLoading() {
        if (!isPullLoading()) {
            ILoadingLayout.State state = ILoadingLayout.State.REFRESHING;
            this.mPullUpState = state;
            onStateChanged(state, false);
            LoadingLayout loadingLayout = this.mFooterLayout;
            if (loadingLayout != null) {
                loadingLayout.setState(state);
            }
            if (this.mRefreshListener != null) {
                postDelayed(new Runnable() {
                    public void run() {
                        PullToRefreshBase.this.mRefreshListener.onPullUpToRefresh(PullToRefreshBase.this);
                    }
                }, getSmoothScrollDuration());
            }
        }
    }

    /* access modifiers changed from: protected */
    public void startRefreshing() {
        if (!isPullRefreshing()) {
            ILoadingLayout.State state = ILoadingLayout.State.REFRESHING;
            this.mPullDownState = state;
            onStateChanged(state, true);
            LoadingLayout loadingLayout = this.mHeaderLayout;
            if (loadingLayout != null) {
                loadingLayout.setState(state);
            }
            if (this.mRefreshListener != null) {
                postDelayed(new Runnable() {
                    public void run() {
                        PullToRefreshBase.this.mRefreshListener.onPullDownToRefresh(PullToRefreshBase.this);
                    }
                }, getSmoothScrollDuration());
            }
        }
    }

    /* access modifiers changed from: private */
    public void smoothScrollTo(int i, long j, long j2) {
        PullToRefreshBase<T>.SmoothScrollRunnable smoothScrollRunnable = this.mSmoothScrollRunnable;
        if (smoothScrollRunnable != null) {
            smoothScrollRunnable.stop();
        }
        int scrollYValue = getScrollYValue();
        boolean z = scrollYValue != i;
        if (z) {
            this.mSmoothScrollRunnable = new SmoothScrollRunnable(scrollYValue, i, j);
        }
        if (!z) {
            return;
        }
        if (j2 > 0) {
            postDelayed(this.mSmoothScrollRunnable, j2);
        } else {
            post(this.mSmoothScrollRunnable);
        }
    }
}
