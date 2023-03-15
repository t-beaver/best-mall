package com.dcloud.android.v4.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import com.dcloud.android.v4.view.NestedScrollingChild;
import com.dcloud.android.v4.view.NestedScrollingChildHelper;
import com.dcloud.android.v4.view.NestedScrollingParent;
import com.dcloud.android.v4.view.NestedScrollingParentHelper;
import com.dcloud.android.v4.view.ViewCompat;
import com.dcloud.android.v4.widget.IRefreshAble;
import io.dcloud.common.util.PdrUtil;
import org.json.JSONObject;

public class SwipeRefreshLayout extends ViewGroup implements NestedScrollingParent, NestedScrollingChild, IRefreshAble {
    private static final int ALPHA_ANIMATION_DURATION = 300;
    private static final int ANIMATE_TO_START_DURATION = 200;
    private static final int ANIMATE_TO_TRIGGER_DURATION = 200;
    private static final int CIRCLE_BG_LIGHT = -328966;
    private static final int CIRCLE_DIAMETER = 40;
    private static final int CIRCLE_DIAMETER_LARGE = 56;
    private static final float DECELERATE_INTERPOLATION_FACTOR = 2.0f;
    public static final int DEFAULT = 1;
    private static final int DEFAULT_CIRCLE_TARGET = 64;
    private static final float DRAG_RATE = 0.5f;
    private static final int INVALID_POINTER = -1;
    public static final int LARGE = 0;
    private static final int[] LAYOUT_ATTRS = {16842766};
    private static final String LOG_TAG = "SwipeRefreshLayout";
    private static final int MAX_ALPHA = 255;
    private static final float MAX_PROGRESS_ANGLE = 0.8f;
    static final int PULL_BOTTOM = -1;
    static final int PULL_DEGREE_GAP = 40;
    private static final int SCALE_DOWN_DURATION = 150;
    private static final int STARTING_PROGRESS_ALPHA = 76;
    private int F_OriginalOffsetTop;
    private float F_SpinnerFinalOffset;
    private float F_TotalDragDistance;
    private boolean isSetOffset;
    private Animation mAlphaMaxAnimation;
    private Animation mAlphaStartAnimation;
    private final Animation mAnimateToCorrectPosition;
    private final Animation mAnimateToStartPosition;
    boolean mBeginRefresh;
    private int mCircleHeight;
    /* access modifiers changed from: private */
    public CircleImageView mCircleView;
    private int mCircleViewIndex;
    private int mCircleWidth;
    /* access modifiers changed from: private */
    public int mCurrentTargetOffsetTop;
    private final DecelerateInterpolator mDecelerateInterpolator;
    View mDrawParentView;
    protected int mFrom;
    boolean mHandledDown;
    private float mInitialDownX;
    private float mInitialDownY;
    private float mInitialMotionY;
    private boolean mIsBeingDragged;
    JSONObject mJsonData;
    /* access modifiers changed from: private */
    public IRefreshAble.OnRefreshListener mListener;
    private int mMediumAnimationDuration;
    private final NestedScrollingChildHelper mNestedScrollingChildHelper;
    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    /* access modifiers changed from: private */
    public boolean mNotify;
    private boolean mOriginalOffsetCalculated;
    protected int mOriginalOffsetTop;
    private final int[] mParentScrollConsumed;
    View mParentView;
    private final Animation mPeek;
    /* access modifiers changed from: private */
    public boolean mPlusRefreshing;
    /* access modifiers changed from: private */
    public MaterialProgressDrawable mProgress;
    int mPullDirect;
    private boolean mRefreshEnable;
    private Animation.AnimationListener mRefreshListener;
    /* access modifiers changed from: private */
    public boolean mRefreshing;
    private boolean mReturningToStart;
    /* access modifiers changed from: private */
    public boolean mScale;
    private Animation mScaleAnimation;
    private Animation mScaleDownAnimation;
    private Animation mScaleDownToStartAnimation;
    /* access modifiers changed from: private */
    public float mSpinnerFinalOffset;
    /* access modifiers changed from: private */
    public float mStartingScale;
    private View mTarget;
    /* access modifiers changed from: private */
    public float mTotalDragDistance;
    private float mTotalUnconsumed;
    private int mTouchSlop;
    boolean mUseSys;
    /* access modifiers changed from: private */
    public boolean mUsingCustomStart;

    public SwipeRefreshLayout(Context context, AttributeSet attributeSet, boolean z) {
        super(context);
        this.mRefreshing = false;
        this.mTotalDragDistance = -1.0f;
        this.mParentScrollConsumed = new int[2];
        this.mOriginalOffsetCalculated = false;
        this.mIsBeingDragged = false;
        this.mCircleViewIndex = -1;
        this.mPlusRefreshing = false;
        this.mRefreshListener = new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                if (SwipeRefreshLayout.this.mRefreshing) {
                    SwipeRefreshLayout.this.mProgress.setAlpha(255);
                    SwipeRefreshLayout.this.mProgress.start();
                    boolean unused = SwipeRefreshLayout.this.mPlusRefreshing = true;
                    if (SwipeRefreshLayout.this.mNotify && SwipeRefreshLayout.this.mListener != null) {
                        SwipeRefreshLayout.this.mListener.onRefresh(3);
                    }
                } else {
                    SwipeRefreshLayout.this.mProgress.stop();
                    boolean unused2 = SwipeRefreshLayout.this.mPlusRefreshing = false;
                    SwipeRefreshLayout.this.mCircleView.setVisibility(8);
                    SwipeRefreshLayout.this.setColorViewAlpha(255);
                    if (SwipeRefreshLayout.this.mScale) {
                        SwipeRefreshLayout.this.setAnimationProgress(0.0f);
                    } else {
                        SwipeRefreshLayout swipeRefreshLayout = SwipeRefreshLayout.this;
                        swipeRefreshLayout.setTargetOffsetTopAndBottom(swipeRefreshLayout.mOriginalOffsetTop - swipeRefreshLayout.mCurrentTargetOffsetTop, true);
                    }
                }
                SwipeRefreshLayout swipeRefreshLayout2 = SwipeRefreshLayout.this;
                int unused3 = swipeRefreshLayout2.mCurrentTargetOffsetTop = swipeRefreshLayout2.mCircleView.getTop();
            }

            public void onAnimationRepeat(Animation animation) {
                SwipeRefreshLayout.this.parentInvalidate();
            }

            public void onAnimationStart(Animation animation) {
            }
        };
        this.mUseSys = false;
        this.mBeginRefresh = false;
        this.isSetOffset = false;
        this.mJsonData = null;
        this.mDrawParentView = null;
        this.mParentView = null;
        this.mPullDirect = -1;
        this.mRefreshEnable = true;
        this.mHandledDown = false;
        this.mAnimateToCorrectPosition = new Animation() {
            public void applyTransformation(float f, Transformation transformation) {
                float f2;
                if (!SwipeRefreshLayout.this.mUsingCustomStart) {
                    f2 = SwipeRefreshLayout.this.mSpinnerFinalOffset - ((float) Math.abs(SwipeRefreshLayout.this.mOriginalOffsetTop));
                } else {
                    f2 = SwipeRefreshLayout.this.mSpinnerFinalOffset;
                }
                SwipeRefreshLayout swipeRefreshLayout = SwipeRefreshLayout.this;
                int i = swipeRefreshLayout.mFrom;
                SwipeRefreshLayout.this.setTargetOffsetTopAndBottom((i + ((int) (((float) (((int) f2) - i)) * f))) - swipeRefreshLayout.mCircleView.getTop(), false);
                SwipeRefreshLayout.this.mProgress.setArrowScale(1.0f - f);
            }
        };
        this.mPeek = new Animation() {
            public void applyTransformation(float f, Transformation transformation) {
                float f2;
                if (!SwipeRefreshLayout.this.mUsingCustomStart) {
                    f2 = SwipeRefreshLayout.this.mSpinnerFinalOffset - ((float) Math.abs(SwipeRefreshLayout.this.mOriginalOffsetTop));
                } else {
                    f2 = SwipeRefreshLayout.this.mSpinnerFinalOffset;
                }
                SwipeRefreshLayout swipeRefreshLayout = SwipeRefreshLayout.this;
                int i = swipeRefreshLayout.mFrom;
                SwipeRefreshLayout.this.setTargetOffsetTopAndBottom((i + ((int) (((float) (((int) f2) - i)) * f))) - swipeRefreshLayout.mCircleView.getTop(), false);
                SwipeRefreshLayout.this.mProgress.setArrowScale(1.0f - f);
            }
        };
        this.mAnimateToStartPosition = new Animation() {
            public void applyTransformation(float f, Transformation transformation) {
                SwipeRefreshLayout.this.moveToStart(f);
            }
        };
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.mUseSys = z;
        this.mMediumAnimationDuration = getResources().getInteger(17694721);
        setWillNotDraw(false);
        this.mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, LAYOUT_ATTRS);
            setEnabled(obtainStyledAttributes.getBoolean(0, true));
            obtainStyledAttributes.recycle();
        }
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int i = (int) (displayMetrics.density * 40.0f);
        this.mCircleWidth = i;
        this.mCircleHeight = i;
        createProgressView();
        ViewCompat.setChildrenDrawingOrderEnabled(this, true);
        float f = displayMetrics.density * 64.0f;
        this.mSpinnerFinalOffset = f;
        this.mTotalDragDistance = f;
        this.F_SpinnerFinalOffset = f;
        this.F_TotalDragDistance = f;
        this.mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        this.mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
    }

    private void animateOffsetToCorrectPosition(int i, Animation.AnimationListener animationListener) {
        this.mFrom = i;
        this.mAnimateToCorrectPosition.reset();
        this.mAnimateToCorrectPosition.setDuration(200);
        this.mAnimateToCorrectPosition.setInterpolator(this.mDecelerateInterpolator);
        if (animationListener != null) {
            this.mCircleView.setAnimationListener(animationListener);
        }
        this.mCircleView.clearAnimation();
        this.mCircleView.startAnimation(this.mAnimateToCorrectPosition);
    }

    private void animateOffsetToStartPosition(int i, Animation.AnimationListener animationListener) {
        if (this.mScale) {
            startScaleDownReturnToStartAnimation(i, animationListener);
            return;
        }
        this.mFrom = i;
        this.mAnimateToStartPosition.reset();
        this.mAnimateToStartPosition.setDuration(200);
        this.mAnimateToStartPosition.setInterpolator(this.mDecelerateInterpolator);
        if (animationListener != null) {
            this.mCircleView.setAnimationListener(animationListener);
        }
        this.mCircleView.clearAnimation();
        this.mCircleView.startAnimation(this.mAnimateToStartPosition);
    }

    private void cancelRefresh() {
        this.mRefreshing = false;
        this.mProgress.setStartEndTrim(0.0f, 0.0f);
        animateOffsetToStartPosition(this.mCurrentTargetOffsetTop, !this.mScale ? new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                if (SwipeRefreshLayout.this.mCircleView != null) {
                    SwipeRefreshLayout.this.mCircleView.setVisibility(8);
                }
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        } : null);
        this.mProgress.showArrow(false);
    }

    private void createProgressView() {
        this.mCircleView = new CircleImageView(getContext(), -328966, 20.0f, false);
        MaterialProgressDrawable materialProgressDrawable = new MaterialProgressDrawable(getContext(), this);
        this.mProgress = materialProgressDrawable;
        materialProgressDrawable.setBackgroundColor(-328966);
        this.mCircleView.setImageDrawable(this.mProgress);
        this.mCircleView.setVisibility(8);
        addView(this.mCircleView);
    }

    private void ensureTarget() {
        if (this.mTarget == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View childAt = getChildAt(i);
                if (!childAt.equals(this.mCircleView)) {
                    this.mTarget = childAt;
                    return;
                }
            }
        }
    }

    private void finishSpinner(float f) {
        if (f > this.mTotalDragDistance) {
            setRefreshing(true, true);
        } else {
            cancelRefresh();
        }
    }

    private boolean handleTouchEvent(MotionEvent motionEvent) {
        if (isRefreshing()) {
            return false;
        }
        if (motionEvent.getAction() == 0 || this.mHandledDown) {
            return true;
        }
        return false;
    }

    private boolean isAlphaUsedForScale() {
        return Build.VERSION.SDK_INT < 11;
    }

    private boolean isAnimationRunning(Animation animation) {
        return animation != null && animation.hasStarted() && !animation.hasEnded();
    }

    private boolean isDrawAble() {
        return this.mCircleView.getVisibility() == 0 && this.mCircleView.getTop() > this.mOriginalOffsetTop - this.mCircleView.getMeasuredHeight() && (this.mParentView.getScrollY() <= 0 || this.mPlusRefreshing);
    }

    /* access modifiers changed from: private */
    public void moveSpinner(float f) {
        float f2;
        this.mProgress.showArrow(true);
        float min = Math.min(1.0f, Math.abs(f / this.mTotalDragDistance));
        double d = (double) min;
        Double.isNaN(d);
        float max = (((float) Math.max(d - 0.4d, 0.0d)) * 5.0f) / 3.0f;
        float abs = Math.abs(f) - this.mTotalDragDistance;
        if (this.mUsingCustomStart) {
            f2 = this.mSpinnerFinalOffset - ((float) this.mOriginalOffsetTop);
        } else {
            f2 = this.mSpinnerFinalOffset;
        }
        double max2 = (double) (Math.max(0.0f, Math.min(abs, f2 * DECELERATE_INTERPOLATION_FACTOR) / f2) / 4.0f);
        double pow = Math.pow(max2, 2.0d);
        Double.isNaN(max2);
        float f3 = ((float) (max2 - pow)) * DECELERATE_INTERPOLATION_FACTOR;
        int i = this.mOriginalOffsetTop + ((int) ((f2 * min) + (f2 * f3 * DECELERATE_INTERPOLATION_FACTOR)));
        if (this.mCircleView.getVisibility() != 0) {
            this.mCircleView.setVisibility(0);
        }
        if (!this.mScale) {
            ViewCompat.setScaleX(this.mCircleView, 1.0f);
            ViewCompat.setScaleY(this.mCircleView, 1.0f);
        }
        float f4 = this.mTotalDragDistance;
        if (f < f4) {
            if (this.mScale) {
                setAnimationProgress(f / f4);
            }
            if (this.mProgress.getAlpha() > 76 && !isAnimationRunning(this.mAlphaStartAnimation)) {
                startProgressAlphaStartAnimation();
            }
            this.mProgress.setStartEndTrim(0.0f, Math.min(MAX_PROGRESS_ANGLE, max * MAX_PROGRESS_ANGLE));
            this.mProgress.setArrowScale(Math.min(1.0f, max));
        } else if (this.mProgress.getAlpha() < 255 && !isAnimationRunning(this.mAlphaMaxAnimation)) {
            startProgressAlphaMaxAnimation();
            this.mProgress.setStartEndTrim(0.0f, MAX_PROGRESS_ANGLE);
            this.mProgress.setArrowScale(1.0f);
        }
        this.mProgress.setProgressRotation((((max * 0.4f) - 16.0f) + (f3 * DECELERATE_INTERPOLATION_FACTOR)) * 0.5f);
        setTargetOffsetTopAndBottom((int) ((float) (i - this.mCurrentTargetOffsetTop)), true);
    }

    /* access modifiers changed from: private */
    public void moveToStart(float f) {
        int i = this.mFrom;
        setTargetOffsetTopAndBottom((i + ((int) (((float) (this.mOriginalOffsetTop - i)) * f))) - this.mCircleView.getTop(), false);
    }

    /* access modifiers changed from: private */
    public void parentInvalidate() {
        if (isDrawAble() && this.mParentView != null) {
            Log.d("parentInvalidate", "parentInvalidate");
            int width = ((this.mParentView.getWidth() - this.mCircleWidth) / 2) + this.mParentView.getScrollX();
            int scrollY = this.mOriginalOffsetTop + this.mCircleHeight + this.mParentView.getScrollY();
            this.mDrawParentView.invalidate(width, scrollY, this.mCircleWidth + width, this.mCircleView.getTop() + scrollY + this.mCircleHeight);
        }
    }

    private void peek(int i, Animation.AnimationListener animationListener) {
        this.mFrom = i;
        this.mPeek.reset();
        this.mPeek.setDuration(500);
        this.mPeek.setInterpolator(this.mDecelerateInterpolator);
        if (animationListener != null) {
            this.mCircleView.setAnimationListener(animationListener);
        }
        this.mCircleView.clearAnimation();
        this.mCircleView.startAnimation(this.mPeek);
    }

    /* access modifiers changed from: private */
    public void setAnimationProgress(float f) {
        if (isAlphaUsedForScale()) {
            setColorViewAlpha((int) (f * 255.0f));
            return;
        }
        ViewCompat.setScaleX(this.mCircleView, f);
        ViewCompat.setScaleY(this.mCircleView, f);
    }

    /* access modifiers changed from: private */
    public void setColorViewAlpha(int i) {
        this.mCircleView.getBackground().setAlpha(i);
        this.mProgress.setAlpha(i);
    }

    /* access modifiers changed from: private */
    public void setTargetOffsetTopAndBottom(int i, boolean z) {
        this.mCircleView.bringToFront();
        this.mCircleView.offsetTopAndBottom(i);
        this.mCurrentTargetOffsetTop = this.mCircleView.getTop();
        if (z && Build.VERSION.SDK_INT < 11) {
            invalidate();
        }
    }

    private Animation startAlphaAnimation(final int i, final int i2) {
        if (this.mScale && isAlphaUsedForScale()) {
            return null;
        }
        AnonymousClass4 r0 = new Animation() {
            public void applyTransformation(float f, Transformation transformation) {
                MaterialProgressDrawable access$200 = SwipeRefreshLayout.this.mProgress;
                int i = i;
                access$200.setAlpha((int) (((float) i) + (((float) (i2 - i)) * f)));
            }
        };
        r0.setDuration(300);
        this.mCircleView.setAnimationListener((Animation.AnimationListener) null);
        this.mCircleView.clearAnimation();
        this.mCircleView.startAnimation(r0);
        return r0;
    }

    private void startProgressAlphaMaxAnimation() {
        this.mAlphaMaxAnimation = startAlphaAnimation(this.mProgress.getAlpha(), 255);
    }

    private void startProgressAlphaStartAnimation() {
        this.mAlphaStartAnimation = startAlphaAnimation(this.mProgress.getAlpha(), 76);
    }

    private void startScaleDownAnimation(Animation.AnimationListener animationListener) {
        AnonymousClass3 r0 = new Animation() {
            public void applyTransformation(float f, Transformation transformation) {
                SwipeRefreshLayout.this.setAnimationProgress(1.0f - f);
            }
        };
        this.mScaleDownAnimation = r0;
        r0.setDuration(150);
        this.mCircleView.setAnimationListener(animationListener);
        this.mCircleView.clearAnimation();
        this.mCircleView.startAnimation(this.mScaleDownAnimation);
    }

    private void startScaleDownReturnToStartAnimation(int i, Animation.AnimationListener animationListener) {
        this.mFrom = i;
        if (isAlphaUsedForScale()) {
            this.mStartingScale = (float) this.mProgress.getAlpha();
        } else {
            this.mStartingScale = ViewCompat.getScaleX(this.mCircleView);
        }
        AnonymousClass10 r3 = new Animation() {
            public void applyTransformation(float f, Transformation transformation) {
                SwipeRefreshLayout.this.setAnimationProgress(SwipeRefreshLayout.this.mStartingScale + ((-SwipeRefreshLayout.this.mStartingScale) * f));
                SwipeRefreshLayout.this.moveToStart(f);
            }
        };
        this.mScaleDownToStartAnimation = r3;
        r3.setDuration(150);
        if (animationListener != null) {
            this.mCircleView.setAnimationListener(animationListener);
        }
        this.mCircleView.clearAnimation();
        this.mCircleView.startAnimation(this.mScaleDownToStartAnimation);
    }

    private void startScaleUpAnimation(Animation.AnimationListener animationListener) {
        this.mCircleView.setVisibility(0);
        if (Build.VERSION.SDK_INT >= 11) {
            this.mProgress.setAlpha(255);
        }
        AnonymousClass2 r0 = new Animation() {
            public void applyTransformation(float f, Transformation transformation) {
                SwipeRefreshLayout.this.setAnimationProgress(f);
            }
        };
        this.mScaleAnimation = r0;
        r0.setDuration((long) this.mMediumAnimationDuration);
        if (animationListener != null) {
            this.mCircleView.setAnimationListener(animationListener);
        }
        this.mCircleView.clearAnimation();
        this.mCircleView.startAnimation(this.mScaleAnimation);
    }

    public void beginRefresh() {
        if (!this.mBeginRefresh && this.mCircleView.getVisibility() != 0) {
            post(new Runnable() {
                int offset = 0;

                public void run() {
                    if (((float) this.offset) < SwipeRefreshLayout.this.mTotalDragDistance) {
                        SwipeRefreshLayout.this.moveSpinner((float) this.offset);
                        SwipeRefreshLayout.this.postDelayed(this, 1);
                        this.offset += 15;
                        return;
                    }
                    SwipeRefreshLayout.this.setRefreshing(true, true);
                    SwipeRefreshLayout.this.mBeginRefresh = false;
                }
            });
            this.mBeginRefresh = true;
        }
    }

    public boolean canChildScrollUp() {
        View view = this.mTarget;
        if (view == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= 14) {
            return ViewCompat.canScrollVertically(view, -1);
        }
        if (view instanceof AbsListView) {
            AbsListView absListView = (AbsListView) view;
            if (absListView.getChildCount() <= 0) {
                return false;
            }
            if (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop()) {
                return true;
            }
            return false;
        } else if (ViewCompat.canScrollVertically(view, -1) || this.mTarget.getScrollY() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        if (this.mUseSys) {
            super.dispatchDraw(canvas);
        } else {
            parentInvalidate();
        }
    }

    public boolean dispatchNestedFling(float f, float f2, boolean z) {
        return this.mNestedScrollingChildHelper.dispatchNestedFling(f, f2, z);
    }

    public boolean dispatchNestedPreFling(float f, float f2) {
        return this.mNestedScrollingChildHelper.dispatchNestedPreFling(f, f2);
    }

    public boolean dispatchNestedPreScroll(int i, int i2, int[] iArr, int[] iArr2) {
        return this.mNestedScrollingChildHelper.dispatchNestedPreScroll(i, i2, iArr, iArr2);
    }

    public boolean dispatchNestedScroll(int i, int i2, int i3, int i4, int[] iArr) {
        return this.mNestedScrollingChildHelper.dispatchNestedScroll(i, i2, i3, i4, iArr);
    }

    public void endRefresh() {
        setRefreshing(false);
    }

    /* access modifiers changed from: protected */
    public int getChildDrawingOrder(int i, int i2) {
        int i3 = this.mCircleViewIndex;
        if (i3 < 0) {
            return i2;
        }
        if (i2 == i - 1) {
            return i3;
        }
        return i2 >= i3 ? i2 + 1 : i2;
    }

    public int getNestedScrollAxes() {
        return this.mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    public int getProgressCircleDiameter() {
        CircleImageView circleImageView = this.mCircleView;
        if (circleImageView != null) {
            return circleImageView.getMeasuredHeight();
        }
        return 0;
    }

    public boolean hasNestedScrollingParent() {
        return this.mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    public boolean hasRefreshOperator() {
        return this.mIsBeingDragged || isRefreshing();
    }

    public boolean isNestedScrollingEnabled() {
        return this.mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    public boolean isRefreshEnable() {
        return this.mRefreshEnable;
    }

    public boolean isRefreshing() {
        return this.mRefreshing;
    }

    public void onInit(ViewGroup viewGroup, View view, IRefreshAble.OnRefreshListener onRefreshListener) {
        this.mParentView = view;
        this.mDrawParentView = view;
        setOnRefreshListener(onRefreshListener);
        viewGroup.addView(this, -1, -1);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int measuredWidth = getMeasuredWidth();
        getMeasuredHeight();
        if (getChildCount() != 0) {
            if (this.mTarget == null) {
                ensureTarget();
            }
            int measuredWidth2 = this.mCircleView.getMeasuredWidth();
            int measuredHeight = this.mCircleView.getMeasuredHeight();
            int i5 = measuredWidth / 2;
            int i6 = measuredWidth2 / 2;
            int i7 = this.mCurrentTargetOffsetTop;
            this.mCircleView.layout(i5 - i6, i7, i5 + i6, measuredHeight + i7);
        }
    }

    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.mTarget == null) {
            ensureTarget();
        }
        this.mCircleView.measure(View.MeasureSpec.makeMeasureSpec(this.mCircleWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(this.mCircleHeight, 1073741824));
        if (!this.mUsingCustomStart && !this.mOriginalOffsetCalculated) {
            this.mOriginalOffsetCalculated = true;
            int i3 = -this.mCircleView.getMeasuredHeight();
            this.mOriginalOffsetTop = i3;
            this.mCurrentTargetOffsetTop = i3;
            this.F_OriginalOffsetTop = i3;
        }
        this.mCircleViewIndex = -1;
        for (int i4 = 0; i4 < getChildCount(); i4++) {
            if (getChildAt(i4) == this.mCircleView) {
                this.mCircleViewIndex = i4;
                return;
            }
        }
    }

    public boolean onNestedFling(View view, float f, float f2, boolean z) {
        return false;
    }

    public boolean onNestedPreFling(View view, float f, float f2) {
        return false;
    }

    public void onNestedPreScroll(View view, int i, int i2, int[] iArr) {
        if (i2 > 0) {
            float f = this.mTotalUnconsumed;
            if (f > 0.0f) {
                float f2 = (float) i2;
                if (f2 > f) {
                    iArr[1] = i2 - ((int) f);
                    this.mTotalUnconsumed = 0.0f;
                } else {
                    this.mTotalUnconsumed = f - f2;
                    iArr[1] = i2;
                }
                moveSpinner(this.mTotalUnconsumed);
            }
        }
        int[] iArr2 = this.mParentScrollConsumed;
        if (dispatchNestedPreScroll(i - iArr[0], i2 - iArr[1], iArr2, (int[]) null)) {
            iArr[0] = iArr[0] + iArr2[0];
            iArr[1] = iArr[1] + iArr2[1];
        }
    }

    public void onNestedScroll(View view, int i, int i2, int i3, int i4) {
        if (i4 < 0) {
            float abs = this.mTotalUnconsumed + ((float) Math.abs(i4));
            this.mTotalUnconsumed = abs;
            moveSpinner(abs);
        }
        dispatchNestedScroll(i, i2, i3, i, (int[]) null);
    }

    public void onNestedScrollAccepted(View view, View view2, int i) {
        this.mNestedScrollingParentHelper.onNestedScrollAccepted(view, view2, i);
        this.mTotalUnconsumed = 0.0f;
    }

    public void onResize(int i, int i2, float f) {
        parseData(this.mJsonData, i, i2, f);
    }

    public void onSelfDraw(Canvas canvas) {
        if (isDrawAble()) {
            canvas.save();
            int measuredWidth = this.mCircleView.getMeasuredWidth();
            int measuredHeight = this.mCircleView.getMeasuredHeight();
            int width = ((this.mParentView.getWidth() - measuredWidth) / 2) + this.mParentView.getScrollX();
            int max = Math.max((this.mParentView.getScrollY() - measuredHeight) + this.mCircleView.getTop(), this.mOriginalOffsetTop);
            canvas.clipRect(width, max, measuredWidth + width, max + measuredHeight);
            canvas.translate((float) this.mParentView.getScrollX(), (float) (this.mParentView.getScrollY() - measuredHeight));
            super.dispatchDraw(canvas);
            canvas.restore();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0032, code lost:
        if (r0 != 3) goto L_0x00e0;
     */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00e3  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00ec A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:51:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onSelfTouchEvent(android.view.MotionEvent r10) {
        /*
            r9 = this;
            boolean r0 = r9.handleTouchEvent(r10)
            r1 = 0
            if (r0 != 0) goto L_0x0008
            return r1
        L_0x0008:
            int r0 = com.dcloud.android.v4.view.MotionEventCompat.getActionMasked(r10)
            boolean r2 = r9.mReturningToStart
            if (r2 == 0) goto L_0x0014
            if (r0 != 0) goto L_0x0014
            r9.mReturningToStart = r1
        L_0x0014:
            boolean r2 = r9.isEnabled()
            r3 = 1
            if (r2 == 0) goto L_0x00e0
            boolean r2 = r9.mReturningToStart
            if (r2 != 0) goto L_0x00e0
            boolean r2 = r9.canChildScrollUp()
            if (r2 == 0) goto L_0x0027
            goto L_0x00e0
        L_0x0027:
            if (r0 == 0) goto L_0x00b2
            r2 = 1056964608(0x3f000000, float:0.5)
            r4 = 0
            if (r0 == r3) goto L_0x0098
            r5 = 2
            if (r0 == r5) goto L_0x0036
            r5 = 3
            if (r0 == r5) goto L_0x0098
            goto L_0x00e0
        L_0x0036:
            float r0 = r10.getY()
            float r5 = r9.mInitialDownY
            float r5 = r0 - r5
            float r10 = r10.getX()
            int r6 = r9.mTouchSlop
            float r6 = (float) r6
            int r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1))
            if (r5 <= 0) goto L_0x0086
            boolean r5 = r9.mIsBeingDragged
            if (r5 != 0) goto L_0x0086
            float r5 = r9.mInitialDownX
            float r10 = r10 - r5
            float r10 = java.lang.Math.abs(r10)
            float r5 = r9.mInitialDownY
            float r5 = r0 - r5
            float r5 = java.lang.Math.abs(r5)
            float r10 = r10 / r5
            double r5 = (double) r10
            double r5 = java.lang.Math.atan(r5)
            r7 = 4640537203540230144(0x4066800000000000, double:180.0)
            double r5 = r5 * r7
            r7 = 4614256656552045848(0x400921fb54442d18, double:3.141592653589793)
            double r5 = r5 / r7
            r7 = 4630826316843712512(0x4044000000000000, double:40.0)
            int r10 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r10 >= 0) goto L_0x0086
            float r10 = r9.mInitialDownY
            int r5 = r9.mTouchSlop
            float r5 = (float) r5
            float r10 = r10 + r5
            r9.mInitialMotionY = r10
            r9.mIsBeingDragged = r3
            com.dcloud.android.v4.widget.MaterialProgressDrawable r10 = r9.mProgress
            r5 = 76
            r10.setAlpha(r5)
        L_0x0086:
            boolean r10 = r9.mIsBeingDragged
            if (r10 == 0) goto L_0x00e0
            float r10 = r9.mInitialMotionY
            float r0 = r0 - r10
            float r0 = r0 * r2
            int r10 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r10 <= 0) goto L_0x0096
            r9.moveSpinner(r0)
        L_0x0096:
            r10 = 1
            goto L_0x00e1
        L_0x0098:
            float r10 = r10.getY()
            boolean r0 = r9.mIsBeingDragged
            if (r0 == 0) goto L_0x00aa
            float r0 = r9.mInitialMotionY
            float r10 = r10 - r0
            float r10 = r10 * r2
            r9.finishSpinner(r10)
            r10 = 1
            goto L_0x00ab
        L_0x00aa:
            r10 = 0
        L_0x00ab:
            r9.mInitialDownY = r4
            r9.mIsBeingDragged = r1
            r9.mHandledDown = r1
            goto L_0x00e1
        L_0x00b2:
            boolean r0 = r9.mIsBeingDragged
            if (r0 != 0) goto L_0x00d8
            r9.mHandledDown = r3
            int r0 = r9.mOriginalOffsetTop
            com.dcloud.android.v4.widget.CircleImageView r2 = r9.mCircleView
            int r2 = r2.getTop()
            int r0 = r0 - r2
            r9.setTargetOffsetTopAndBottom(r0, r3)
            float r0 = r10.getY()
            r2 = -1082130432(0xffffffffbf800000, float:-1.0)
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 != 0) goto L_0x00cf
            goto L_0x00e0
        L_0x00cf:
            r9.mInitialDownY = r0
            float r10 = r10.getX()
            r9.mInitialDownX = r10
            goto L_0x00e0
        L_0x00d8:
            int r10 = r9.mOriginalOffsetTop
            float r10 = (float) r10
            r9.moveSpinner(r10)
            r9.mIsBeingDragged = r1
        L_0x00e0:
            r10 = 0
        L_0x00e1:
            if (r10 == 0) goto L_0x00e6
            r9.parentInvalidate()
        L_0x00e6:
            if (r10 != 0) goto L_0x00ec
            boolean r10 = r9.mUseSys
            if (r10 == 0) goto L_0x00ed
        L_0x00ec:
            r1 = 1
        L_0x00ed:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dcloud.android.v4.widget.SwipeRefreshLayout.onSelfTouchEvent(android.view.MotionEvent):boolean");
    }

    public boolean onStartNestedScroll(View view, View view2, int i) {
        int i2;
        if (!isEnabled() || (i2 = i & 2) == 0) {
            return false;
        }
        startNestedScroll(i2);
        return true;
    }

    public void onStopNestedScroll(View view) {
        this.mNestedScrollingParentHelper.onStopNestedScroll(view);
        float f = this.mTotalUnconsumed;
        if (f > 0.0f) {
            finishSpinner(f);
            this.mTotalUnconsumed = 0.0f;
        }
        stopNestedScroll();
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.mUseSys) {
            return onSelfTouchEvent(motionEvent);
        }
        return false;
    }

    public void parseData(JSONObject jSONObject, int i, int i2, float f) {
        if (f == 0.0f || f == 1.0f) {
            try {
                f = this.mParentView.getContext().getResources().getDisplayMetrics().density;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        this.mOriginalOffsetTop = this.F_OriginalOffsetTop;
        this.mSpinnerFinalOffset = this.F_SpinnerFinalOffset;
        this.mTotalDragDistance = this.F_TotalDragDistance;
        this.mJsonData = jSONObject;
        String optString = jSONObject.optString("offset");
        int i3 = this.mOriginalOffsetTop;
        if (!TextUtils.isEmpty(optString)) {
            i3 = PdrUtil.convertToScreenInt(optString, i2, i3, f);
        }
        String optString2 = jSONObject.optString("height");
        int i4 = (int) this.mTotalDragDistance;
        if (!TextUtils.isEmpty(optString2)) {
            i4 = PdrUtil.convertToScreenInt(optString2, i2, i4, f);
        }
        String optString3 = jSONObject.optString(AbsoluteConst.PULL_REFRESH_RANGE);
        int i5 = (int) this.mSpinnerFinalOffset;
        if (!TextUtils.isEmpty(optString3)) {
            i5 = PdrUtil.convertToScreenInt(optString3, i2, i5, f);
        }
        int i6 = i5 + i3;
        String optString4 = jSONObject.optString("color");
        int parseColor = Color.parseColor("#2BD009");
        if (!TextUtils.isEmpty(optString4) && optString4.startsWith("#")) {
            try {
                parseColor = Color.parseColor(optString4);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        setColorSchemeColors(parseColor);
        if (this.mOriginalOffsetTop != i3) {
            this.isSetOffset = false;
        }
        if (!this.isSetOffset) {
            this.isSetOffset = true;
            setProgressViewOffset(false, i3, i6, i4);
        }
    }

    public void requestDisallowInterceptTouchEvent(boolean z) {
        if (Build.VERSION.SDK_INT >= 21 || !(this.mTarget instanceof AbsListView)) {
            View view = this.mTarget;
            if (view == null || ViewCompat.isNestedScrollingEnabled(view)) {
                super.requestDisallowInterceptTouchEvent(z);
            }
        }
    }

    @Deprecated
    public void setColorScheme(int... iArr) {
        setColorSchemeResources(iArr);
    }

    public void setColorSchemeColors(int... iArr) {
        ensureTarget();
        this.mProgress.setColorSchemeColors(iArr);
    }

    public void setColorSchemeResources(int... iArr) {
        Resources resources = getResources();
        int[] iArr2 = new int[iArr.length];
        for (int i = 0; i < iArr.length; i++) {
            iArr2[i] = resources.getColor(iArr[i]);
        }
        setColorSchemeColors(iArr2);
    }

    public void setDistanceToTriggerSync(int i) {
        this.mTotalDragDistance = (float) i;
    }

    public void setNestedScrollingEnabled(boolean z) {
        this.mNestedScrollingChildHelper.setNestedScrollingEnabled(z);
    }

    public void setOnRefreshListener(IRefreshAble.OnRefreshListener onRefreshListener) {
        this.mListener = onRefreshListener;
    }

    @Deprecated
    public void setProgressBackgroundColor(int i) {
        setProgressBackgroundColorSchemeResource(i);
    }

    public void setProgressBackgroundColorSchemeColor(int i) {
        this.mCircleView.setBackgroundColor(i);
        this.mProgress.setBackgroundColor(i);
    }

    public void setProgressBackgroundColorSchemeResource(int i) {
        setProgressBackgroundColorSchemeColor(getResources().getColor(i));
    }

    public void setProgressViewEndTarget(boolean z, int i) {
        this.mSpinnerFinalOffset = (float) i;
        this.mScale = z;
        this.mCircleView.invalidate();
    }

    public void setProgressViewOffset(boolean z, int i, int i2, int i3) {
        this.mScale = z;
        this.mCircleView.setVisibility(8);
        this.mCurrentTargetOffsetTop = i;
        this.mOriginalOffsetTop = i;
        this.mSpinnerFinalOffset = (float) i2;
        this.mTotalDragDistance = (float) i3;
        this.mUsingCustomStart = true;
        this.mCircleView.invalidate();
    }

    public void setRefreshEnable(boolean z) {
        this.mRefreshEnable = z;
    }

    public void setRefreshing(boolean z) {
        float f;
        if (!z || this.mRefreshing == z) {
            setRefreshing(z, true);
            return;
        }
        this.mRefreshing = z;
        if (!this.mUsingCustomStart) {
            f = this.mSpinnerFinalOffset + ((float) this.mOriginalOffsetTop);
        } else {
            f = this.mSpinnerFinalOffset;
        }
        setTargetOffsetTopAndBottom(((int) f) - this.mCurrentTargetOffsetTop, true);
        this.mNotify = false;
        startScaleUpAnimation(this.mRefreshListener);
    }

    public void setSize(int i) {
        if (i == 0 || i == 1) {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            if (i == 0) {
                int i2 = (int) (displayMetrics.density * 56.0f);
                this.mCircleWidth = i2;
                this.mCircleHeight = i2;
            } else {
                int i3 = (int) (displayMetrics.density * 40.0f);
                this.mCircleWidth = i3;
                this.mCircleHeight = i3;
            }
            this.mCircleView.setImageDrawable((Drawable) null);
            this.mProgress.updateSizes(i);
            this.mCircleView.setImageDrawable(this.mProgress);
        }
    }

    public boolean startNestedScroll(int i) {
        return this.mNestedScrollingChildHelper.startNestedScroll(i);
    }

    public void stopNestedScroll() {
        this.mNestedScrollingChildHelper.stopNestedScroll();
    }

    /* access modifiers changed from: private */
    public void setRefreshing(boolean z, boolean z2) {
        if (this.mRefreshing != z) {
            this.mNotify = z2;
            ensureTarget();
            this.mRefreshing = z;
            if (z) {
                animateOffsetToCorrectPosition(this.mCurrentTargetOffsetTop, this.mRefreshListener);
            } else {
                startScaleDownAnimation(this.mRefreshListener);
            }
        }
    }

    public SwipeRefreshLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, true);
    }
}
