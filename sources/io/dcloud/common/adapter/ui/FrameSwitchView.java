package io.dcloud.common.adapter.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import io.dcloud.common.DHInterface.IReflectAble;
import io.dcloud.common.DHInterface.IWaiter;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.FrameBitmapView;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import org.json.JSONObject;

public class FrameSwitchView implements IReflectAble {
    private static final String POP_IN = "pop-in";
    private static final String POP_OUT = "pop-out";
    private static final String SLIDE_IN_RIGHT = "slide-in-right";
    private static final String SLIDE_OUT_RIGHT = "slide-out-right";
    static FrameSwitchView mInstance;
    FrameBitmapView.ClearAnimationListener clearAnimationListener = new FrameBitmapView.ClearAnimationListener() {
        public void onAnimationEnd() {
            FrameSwitchView.this.endRefreshView();
        }
    };
    private boolean isInit = false;
    /* access modifiers changed from: private */
    public boolean isRuning = false;
    /* access modifiers changed from: private */
    public boolean isVisibility = false;
    private Activity mActivity;
    /* access modifiers changed from: private */
    public String mAniType = "pop-in";
    private int mAppScreenHeight;
    private int mAppScreenWidth;
    private String mCallbackId = null;
    private int mDuration = 300;
    /* access modifiers changed from: private */
    public FrameBitmapView mLeftFrameBpView;
    /* access modifiers changed from: private */
    public View mLeftView;
    /* access modifiers changed from: private */
    public FrameBitmapView mRightFrameBpView;
    /* access modifiers changed from: private */
    public View mRightView;
    private SwitchLayout mSwitchLayout;
    /* access modifiers changed from: private */
    public IWebview mWebViewImpl = null;

    private class SwitchLayout extends RelativeLayout implements IWaiter {
        public SwitchLayout(Context context) {
            super(context);
        }

        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            if (!TextUtils.isEmpty(FrameSwitchView.this.mAniType)) {
                if (FrameSwitchView.this.mAniType.equals("pop-in") && FrameSwitchView.this.mRightView != null && (FrameSwitchView.this.mRightView instanceof IWaiter)) {
                    return ((Boolean) ((IWaiter) FrameSwitchView.this.mRightView).doForFeature("checkTouch", motionEvent)).booleanValue();
                }
                if ((FrameSwitchView.this.mAniType.equals("slide-in-right") || FrameSwitchView.this.mAniType.equals("slide-out-right") || FrameSwitchView.this.mAniType.equals("pop-out")) && FrameSwitchView.this.mLeftView != null && (FrameSwitchView.this.mLeftView instanceof IWaiter)) {
                    return ((Boolean) ((IWaiter) FrameSwitchView.this.mLeftView).doForFeature("checkTouch", motionEvent)).booleanValue();
                }
                if ((FrameSwitchView.this.mLeftFrameBpView != null && FrameSwitchView.this.mLeftFrameBpView.isInit()) || (FrameSwitchView.this.mRightFrameBpView != null && FrameSwitchView.this.mRightFrameBpView.isInit())) {
                    return super.dispatchTouchEvent(motionEvent);
                }
            }
            return FrameSwitchView.this.isRuning;
        }

        public Object doForFeature(String str, Object obj) {
            return null;
        }

        /* access modifiers changed from: protected */
        public void onConfigurationChanged(Configuration configuration) {
            super.onConfigurationChanged(configuration);
            if (FrameSwitchView.this.mWebViewImpl != null && FrameSwitchView.this.isVisibility) {
                FrameSwitchView.this.stopAnimation();
            }
        }

        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (getVisibility() != 0) {
                return super.onTouchEvent(motionEvent);
            }
            if (FrameSwitchView.this.mLeftFrameBpView != null && FrameSwitchView.this.mLeftFrameBpView.isInit()) {
                return true;
            }
            if (FrameSwitchView.this.mRightFrameBpView == null || !FrameSwitchView.this.mRightFrameBpView.isInit()) {
                return super.onTouchEvent(motionEvent);
            }
            return true;
        }
    }

    private FrameSwitchView() {
    }

    private void addView(View view) {
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (!(viewGroup instanceof SwitchLayout)) {
            if (viewGroup != null) {
                viewGroup.removeView(view);
            }
            this.mSwitchLayout.addView(view);
        }
    }

    /* access modifiers changed from: private */
    public void endAnimationLayout(View view, int i) {
        int left = view.getLeft() + i;
        int top = view.getTop();
        int width = view.getWidth();
        int height = view.getHeight();
        view.clearAnimation();
        view.layout(left, top, width + left, height + top);
    }

    private TranslateAnimation getAnimation(int i, int i2, int i3, Animation.AnimationListener animationListener) {
        TranslateAnimation translateAnimation = new TranslateAnimation((float) i, (float) i2, 0.0f, 0.0f);
        translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        translateAnimation.setDuration((long) i3);
        translateAnimation.setAnimationListener(animationListener);
        return translateAnimation;
    }

    public static FrameSwitchView getInstance(Activity activity) {
        if (mInstance == null) {
            synchronized (FrameSwitchView.class) {
                if (mInstance == null) {
                    mInstance = new FrameSwitchView(activity);
                }
            }
        }
        return mInstance;
    }

    private void initScreenData() {
        int i;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= 17) {
            this.mActivity.getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
            i = displayMetrics.heightPixels - ((!PdrUtil.isNavigationBarShow(this.mActivity) || PdrUtil.navigationGestureEnabled(this.mActivity)) ? 0 : PdrUtil.getNavigationBarHeight(this.mActivity));
        } else {
            this.mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            i = displayMetrics.heightPixels;
        }
        int i2 = displayMetrics.widthPixels;
        int[] iArr = new int[2];
        this.mWebViewImpl.obtainFrameView().obtainWebAppRootView().obtainMainView().getLocationOnScreen(iArr);
        this.mAppScreenWidth = i2 - iArr[0];
        this.mAppScreenHeight = i - iArr[1];
        try {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mSwitchLayout.getLayoutParams();
            layoutParams.height = this.mAppScreenHeight;
            layoutParams.topMargin = iArr[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runingAnimation(final IWebview iWebview, int i, int i2, String str, final String str2) {
        initScreenData();
        this.isRuning = true;
        if (this.mSwitchLayout.getVisibility() != 0) {
            this.mSwitchLayout.setVisibility(0);
        }
        final View view = this.mLeftView;
        if (view == null) {
            view = this.mLeftFrameBpView;
        }
        final View view2 = this.mRightView;
        if (view2 == null) {
            view2 = this.mRightFrameBpView;
        }
        view.setVisibility(0);
        if (str.equals("pop-in")) {
            view2.setVisibility(0);
            this.isVisibility = true;
            view.startAnimation(getAnimation(0, -(i / 6), this.mDuration, new Animation.AnimationListener() {
                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(8);
                    boolean unused = FrameSwitchView.this.isRuning = false;
                    BaseInfo.sDoingAnimation = false;
                    Deprecated_JSUtil.execCallback(iWebview, str2, (String) null, JSUtil.OK, false, false);
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                    BaseInfo.sDoingAnimation = true;
                }
            }));
            view2.startAnimation(getAnimation(i, 0, this.mDuration, new Animation.AnimationListener() {
                public void onAnimationEnd(Animation animation) {
                    FrameSwitchView.this.endAnimationLayout(view2, 0);
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                }
            }));
        } else if (str.equals("pop-out")) {
            view2.setVisibility(0);
            this.isVisibility = true;
            view.startAnimation(getAnimation(-(i / 2), 0, this.mDuration, new Animation.AnimationListener() {
                public void onAnimationEnd(Animation animation) {
                    FrameSwitchView.this.endAnimationLayout(view, 0);
                    boolean unused = FrameSwitchView.this.isRuning = false;
                    BaseInfo.sDoingAnimation = false;
                    Deprecated_JSUtil.execCallback(iWebview, str2, (String) null, JSUtil.OK, false, false);
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                    BaseInfo.sDoingAnimation = true;
                }
            }));
            view2.startAnimation(getAnimation(0, i, this.mDuration, new Animation.AnimationListener() {
                public void onAnimationEnd(Animation animation) {
                    view2.setVisibility(8);
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                }
            }));
        } else if (str.equals("slide-in-right")) {
            if (view2 != null) {
                view2.setVisibility(8);
            }
            this.isVisibility = true;
            view.startAnimation(getAnimation(i, 0, this.mDuration, new Animation.AnimationListener() {
                public void onAnimationEnd(Animation animation) {
                    FrameSwitchView.this.endAnimationLayout(view, 0);
                    boolean unused = FrameSwitchView.this.isRuning = false;
                    BaseInfo.sDoingAnimation = false;
                    Deprecated_JSUtil.execCallback(iWebview, str2, (String) null, JSUtil.OK, false, false);
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                    BaseInfo.sDoingAnimation = true;
                }
            }));
        } else if (str.equals("slide-out-right")) {
            if (view2 != null) {
                view2.setVisibility(8);
            }
            this.isVisibility = true;
            final View view3 = view;
            final int i3 = i;
            final IWebview iWebview2 = iWebview;
            final String str3 = str2;
            view.startAnimation(getAnimation(0, i, this.mDuration, new Animation.AnimationListener() {
                public void onAnimationEnd(Animation animation) {
                    FrameSwitchView.this.endAnimationLayout(view3, i3);
                    boolean unused = FrameSwitchView.this.isRuning = false;
                    BaseInfo.sDoingAnimation = false;
                    Deprecated_JSUtil.execCallback(iWebview2, str3, (String) null, JSUtil.OK, false, false);
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                    BaseInfo.sDoingAnimation = true;
                }
            }));
        } else {
            this.isRuning = false;
            Deprecated_JSUtil.execCallback(iWebview, str2, (String) null, JSUtil.OK, false, false);
        }
    }

    public void clearData() {
        mInstance = null;
        this.mSwitchLayout = null;
        this.isInit = false;
        this.mWebViewImpl = null;
        this.mLeftFrameBpView = null;
        this.mRightFrameBpView = null;
        this.mLeftView = null;
        this.mRightView = null;
        this.mActivity = null;
    }

    public void clearSwitchAnimation(String str) {
        endRefreshView();
    }

    public void endRefreshView() {
        if (this.isVisibility) {
            this.mLeftFrameBpView.clearAnimation();
            this.mRightFrameBpView.clearAnimation();
            this.isVisibility = false;
        }
        if (this.mSwitchLayout.getVisibility() == 0) {
            this.mSwitchLayout.setVisibility(8);
            View view = this.mLeftView;
            if (view != null) {
                ((IWaiter) view).doForFeature("clearAnimate", (Object) null);
                this.mSwitchLayout.removeView(this.mLeftView);
                this.mLeftView = null;
            }
            View view2 = this.mRightView;
            if (view2 != null) {
                ((IWaiter) view2).doForFeature("clearAnimate", (Object) null);
                this.mSwitchLayout.removeView(this.mRightView);
                this.mRightView = null;
            }
            this.mLeftFrameBpView.clearData();
            this.mRightFrameBpView.clearData();
            this.mLeftFrameBpView.requestLayout();
            this.mRightFrameBpView.requestLayout();
        }
    }

    public void initView() {
        if (!this.isInit) {
            this.isInit = true;
            this.mSwitchLayout = new SwitchLayout(this.mActivity);
            this.mLeftFrameBpView = new FrameBitmapView(this.mActivity);
            this.mRightFrameBpView = new FrameBitmapView(this.mActivity);
            this.mSwitchLayout.addView(this.mLeftFrameBpView);
            this.mSwitchLayout.addView(this.mRightFrameBpView);
            this.mSwitchLayout.setVisibility(8);
            ((ViewGroup) this.mActivity.getWindow().getDecorView()).addView(this.mSwitchLayout);
        }
    }

    public boolean isInit() {
        return this.isInit;
    }

    public void startAnimation(IWebview iWebview, String str, Object obj, String str2, Object obj2, String str3, String str4) {
        try {
            this.mWebViewImpl = iWebview;
            JSONObject jSONObject = new JSONObject(str);
            this.mAniType = jSONObject.optString("type", "pop-in");
            this.mDuration = jSONObject.optInt("duration", this.mDuration);
            initScreenData();
            if (obj != null) {
                if (obj instanceof View) {
                    View view = (View) obj;
                    this.mLeftView = view;
                    addView(view);
                } else {
                    this.mLeftFrameBpView.injectionData(obj, str2, this.mAppScreenWidth, this.mAppScreenHeight, iWebview.getScale());
                }
                if (obj2 != null) {
                    if (obj2 instanceof View) {
                        this.mRightView = (View) obj2;
                        addView((View) obj2);
                    } else {
                        this.mRightFrameBpView.injectionData(obj2, str3, this.mAppScreenWidth, this.mAppScreenHeight, iWebview.getScale());
                    }
                } else if (this.mAniType.equals("pop-in")) {
                    this.mAniType = "slide-in-right";
                } else if (this.mAniType.equals("pop-out")) {
                    this.mAniType = "slide-out-right";
                }
                runingAnimation(iWebview, this.mAppScreenWidth, this.mAppScreenHeight, this.mAniType, str4);
            }
        } catch (Exception unused) {
        }
    }

    public void stopAnimation() {
        initScreenData();
        FrameBitmapView frameBitmapView = this.mLeftFrameBpView;
        if (frameBitmapView != null && frameBitmapView.isInit()) {
            this.mLeftFrameBpView.setStopAnimation(true);
            this.mLeftFrameBpView.configurationChanged(this.mAppScreenWidth, this.mAppScreenHeight);
        }
        FrameBitmapView frameBitmapView2 = this.mRightFrameBpView;
        if (frameBitmapView2 != null && frameBitmapView2.isInit()) {
            this.mRightFrameBpView.setStopAnimation(true);
            this.mRightFrameBpView.configurationChanged(this.mAppScreenWidth, this.mAppScreenHeight);
        }
        if ("pop-in".equals(this.mAniType)) {
            View view = this.mRightView;
            if (view == null) {
                view = this.mRightFrameBpView;
            }
            endAnimationLayout(view, 0);
            View view2 = this.mLeftView;
            if (view2 == null) {
                view2 = this.mLeftFrameBpView;
            }
            view2.setVisibility(8);
        } else if ("pop-out".equals(this.mAniType)) {
            View view3 = this.mRightView;
            if (view3 == null) {
                view3 = this.mRightFrameBpView;
            }
            view3.setVisibility(8);
            View view4 = this.mLeftView;
            if (view4 == null) {
                view4 = this.mLeftFrameBpView;
            }
            endAnimationLayout(view4, 0);
        } else {
            endRefreshView();
        }
    }

    public static FrameSwitchView getInstance() {
        return mInstance;
    }

    private FrameSwitchView(Activity activity) {
        this.mActivity = activity;
    }
}
