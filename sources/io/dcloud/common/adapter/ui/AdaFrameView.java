package io.dcloud.common.adapter.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.dcloud.android.v4.widget.IRefreshAble;
import com.dcloud.android.widget.AbsoluteLayout;
import com.dcloud.android.widget.StatusBarView;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.IEventCallback;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.INativeBitmap;
import io.dcloud.common.DHInterface.INativeView;
import io.dcloud.common.DHInterface.IWaiter;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameItem;
import io.dcloud.common.adapter.util.AnimOptions;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.PlatformUtil;
import io.dcloud.common.adapter.util.ViewOptions;
import io.dcloud.common.adapter.util.ViewRect;
import io.dcloud.common.core.ui.l;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.nineoldandroids.animation.Animator;
import io.dcloud.nineoldandroids.view.ViewHelper;
import java.util.ArrayList;
import java.util.Locale;

public abstract class AdaFrameView extends AdaContainerFrameItem implements IFrameView {
    private static final int ERROR = 0;
    private static final int SUCCESS = 1;
    public boolean inStack = true;
    public boolean interceptTouchEvent = true;
    public boolean isChildOfFrameView = false;
    private boolean isTabItem = false;
    public boolean isTouchEvent = true;
    public String mAccelerationType = "auto";
    public boolean mAnimationCapture = false;
    Animation.AnimationListener mAnimationListener = new Animation.AnimationListener() {
        public void onAnimationEnd(Animation animation) {
            Animator.AnimatorListener animatorListener = AdaFrameView.this.mAnimatorListener;
            if (animatorListener != null) {
                animatorListener.onAnimationEnd((Animator) null);
            }
        }

        public void onAnimationRepeat(Animation animation) {
            Animator.AnimatorListener animatorListener = AdaFrameView.this.mAnimatorListener;
            if (animatorListener != null) {
                animatorListener.onAnimationRepeat((Animator) null);
            }
        }

        public void onAnimationStart(Animation animation) {
            Animator.AnimatorListener animatorListener = AdaFrameView.this.mAnimatorListener;
            if (animatorListener != null) {
                animatorListener.onAnimationStart((Animator) null);
            }
        }
    };
    protected boolean mAnimationStarted = false;
    BounceView mBounceView = null;
    /* access modifiers changed from: private */
    public Handler mCaptureHandler = new Handler() {
        public void handleMessage(Message message) {
            if (message.what != 1) {
                if (AdaFrameView.this.mErrCallBack != null) {
                    AdaFrameView.this.mErrCallBack.onCallBack(message.arg1, message.obj);
                }
            } else if (AdaFrameView.this.mSucCallBack != null) {
                AdaFrameView.this.mSucCallBack.onCallBack(0, (Object) null);
            }
            super.handleMessage(message);
        }
    };
    private IRefreshAble mCircleRefreshView = null;
    private Context mContext;
    /* access modifiers changed from: private */
    public ICallBack mErrCallBack = null;
    protected byte mFrameStatus;
    private int mFrameType = 0;
    private int mLastScreenHeight = 0;
    private ArrayList<IEventCallback> mListeners = null;
    public Bitmap mLoadingSnapshot = null;
    public String mNativeViewAction = "none";
    /* access modifiers changed from: private */
    public DHImageView mPageCImageView = null;
    RefreshView mRefreshView = null;
    public Bitmap mSnapshot = null;
    /* access modifiers changed from: private */
    public ICallBack mSucCallBack = null;
    public l mWindowMgr;

    public interface OnAnimationEnd {
        void onDone();
    }

    protected AdaFrameView(Context context, int i, Object obj) {
        super(context);
        this.mFrameType = i;
        initMainView(context, i, obj);
        this.mContext = context;
        this.mNeedOrientationUpdate = true;
        this.mLastScreenHeight = PlatformUtil.SCREEN_HEIGHT(context);
    }

    private void addCaptureImageView(ViewGroup viewGroup, DHImageView dHImageView, Bitmap bitmap) {
        if (dHImageView.getParent() != viewGroup) {
            if (dHImageView.getParent() != null) {
                ((ViewGroup) dHImageView.getParent()).removeView(dHImageView);
            }
            viewGroup.addView(dHImageView);
        }
        dHImageView.bringToFront();
        dHImageView.setImageBitmap(bitmap);
        dHImageView.removeNativeView();
        dHImageView.setVisibility(AdaFrameItem.VISIBLE);
    }

    /* JADX WARNING: Removed duplicated region for block: B:59:0x0128  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x0134  */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x0185  */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x0188  */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x01a0  */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x01a6  */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x01c6  */
    /* JADX WARNING: Removed duplicated region for block: B:96:0x01e1  */
    /* JADX WARNING: Removed duplicated region for block: B:97:0x01f9  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean captureAnimation(io.dcloud.nineoldandroids.animation.Animator r17, int r18) {
        /*
            r16 = this;
            r6 = r16
            r7 = r17
            r2 = r18
            io.dcloud.common.adapter.util.ViewOptions r0 = r16.obtainFrameOptions()
            boolean r0 = r0.isAnimationOptimization
            r8 = 1
            if (r0 == 0) goto L_0x0016
            boolean r0 = io.dcloud.common.util.SubNViewsUtil.startAnimation(r16, r17, r18)
            if (r0 == 0) goto L_0x0016
            return r8
        L_0x0016:
            boolean r0 = r6.mAnimationCapture
            r1 = 0
            if (r0 == 0) goto L_0x020d
            boolean r0 = io.dcloud.common.util.BaseInfo.sAnimationCaptureC
            if (r0 != 0) goto L_0x0021
            goto L_0x020d
        L_0x0021:
            android.view.View r0 = r16.obtainMainView()
            r3 = r0
            android.view.ViewGroup r3 = (android.view.ViewGroup) r3
            boolean r0 = r16.checkITypeofAble()
            if (r0 == 0) goto L_0x002f
            return r1
        L_0x002f:
            io.dcloud.common.DHInterface.IWebAppRootView r0 = r16.obtainWebAppRootView()
            android.view.View r0 = r0.obtainMainView()
            r4 = r0
            io.dcloud.common.core.ui.k r4 = (io.dcloud.common.core.ui.k) r4
            io.dcloud.common.adapter.ui.DHImageView r0 = r6.mPageCImageView
            r5 = 0
            if (r0 == 0) goto L_0x0053
            int r0 = r4.getHeight()
            io.dcloud.common.adapter.ui.DHImageView r9 = r6.mPageCImageView
            int r9 = r9.getHeight()
            if (r0 == r9) goto L_0x0053
            io.dcloud.common.adapter.ui.DHImageView r0 = r6.mPageCImageView
            r0.clear()
            r6.mPageCImageView = r5
            return r1
        L_0x0053:
            io.dcloud.common.adapter.ui.DHImageView r0 = r6.mPageCImageView
            if (r0 != 0) goto L_0x0062
            io.dcloud.common.adapter.ui.DHImageView r0 = r4.getRightImageView()
            r6.mPageCImageView = r0
            android.widget.ImageView$ScaleType r9 = android.widget.ImageView.ScaleType.FIT_XY
            r0.setScaleType(r9)
        L_0x0062:
            io.dcloud.common.adapter.ui.DHImageView r0 = r6.mPageCImageView
            boolean r0 = r0.isSlipping()
            if (r0 == 0) goto L_0x006b
            return r1
        L_0x006b:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r9 = "C页面是否启用截图动画方案:"
            r0.append(r9)
            boolean r9 = r6.mAnimationCapture
            if (r9 == 0) goto L_0x007f
            boolean r9 = io.dcloud.common.util.BaseInfo.sAnimationCaptureC
            if (r9 == 0) goto L_0x007f
            r9 = 1
            goto L_0x0080
        L_0x007f:
            r9 = 0
        L_0x0080:
            r0.append(r9)
            java.lang.String r9 = " | "
            r0.append(r9)
            io.dcloud.common.adapter.util.AnimOptions r9 = r6.mAnimOptions
            java.lang.String r9 = r9.mAnimType
            r0.append(r9)
            java.lang.String r0 = r0.toString()
            java.lang.String r9 = "mabo"
            io.dcloud.common.adapter.util.Logger.e(r9, r0)
            long r10 = java.lang.System.currentTimeMillis()
            io.dcloud.common.DHInterface.INativeView r0 = r6.mNativeView
            if (r0 == 0) goto L_0x00ba
            boolean r0 = r0.isAnimate()
            if (r0 == 0) goto L_0x00ae
            io.dcloud.common.adapter.ui.DHImageView r0 = r6.mPageCImageView
            r0.removeNativeView()
            r6.mNativeView = r5
            goto L_0x00ba
        L_0x00ae:
            io.dcloud.common.adapter.ui.DHImageView r0 = r6.mPageCImageView
            r0.setImageBitmap(r5)
            io.dcloud.common.adapter.ui.DHImageView r0 = r6.mPageCImageView
            io.dcloud.common.DHInterface.INativeView r12 = r6.mNativeView
            r0.addNativeView(r6, r12)
        L_0x00ba:
            io.dcloud.common.DHInterface.INativeView r0 = r6.mNativeView
            r12 = 22
            if (r0 != 0) goto L_0x0107
            if (r2 != 0) goto L_0x00fc
            android.graphics.Bitmap r5 = r6.mLoadingSnapshot
            if (r5 == 0) goto L_0x00c7
            goto L_0x0107
        L_0x00c7:
            android.graphics.Bitmap r5 = r6.mSnapshot
            if (r5 == 0) goto L_0x00cc
            goto L_0x0107
        L_0x00cc:
            int r0 = android.os.Build.VERSION.SDK_INT
            if (r0 >= r12) goto L_0x00f6
            boolean r0 = r6.isChildOfFrameView
            if (r0 == 0) goto L_0x00f6
            int r0 = r3.getHeight()
            io.dcloud.common.adapter.util.ViewOptions r5 = r16.obtainFrameOptions()
            int r5 = r5.height
            if (r0 <= r5) goto L_0x00f6
            android.graphics.Rect r0 = new android.graphics.Rect
            int r5 = r3.getWidth()
            io.dcloud.common.adapter.util.ViewOptions r13 = r16.obtainFrameOptions()
            int r13 = r13.height
            r0.<init>(r1, r1, r5, r13)
            java.lang.String r5 = "ARGB"
            android.graphics.Bitmap r0 = io.dcloud.common.adapter.util.PlatformUtil.captureView(r3, r8, r8, r0, r5)
            goto L_0x00fa
        L_0x00f6:
            android.graphics.Bitmap r0 = io.dcloud.common.adapter.util.PlatformUtil.captureView(r3)
        L_0x00fa:
            r5 = r0
            goto L_0x0105
        L_0x00fc:
            android.graphics.Bitmap r5 = r6.mSnapshot
            if (r5 == 0) goto L_0x0101
            goto L_0x0107
        L_0x0101:
            android.graphics.Bitmap r5 = io.dcloud.common.adapter.util.PlatformUtil.captureView(r3)
        L_0x0105:
            r0 = 1
            goto L_0x0108
        L_0x0107:
            r0 = 0
        L_0x0108:
            long r13 = java.lang.System.currentTimeMillis()
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            java.lang.String r12 = "==============C截图耗时="
            r15.append(r12)
            long r13 = r13 - r10
            r15.append(r13)
            java.lang.String r10 = r15.toString()
            io.dcloud.common.adapter.util.Logger.i(r9, r10)
            int r9 = io.dcloud.common.util.BaseInfo.sTimeoutCapture
            long r9 = (long) r9
            int r11 = (r13 > r9 ? 1 : (r13 == r9 ? 0 : -1))
            if (r11 < 0) goto L_0x0134
            int r0 = io.dcloud.common.util.BaseInfo.sTimeOutCount
            int r0 = r0 + r8
            io.dcloud.common.util.BaseInfo.sTimeOutCount = r0
            int r9 = io.dcloud.common.util.BaseInfo.sTimeOutMax
            if (r0 <= r9) goto L_0x0138
            io.dcloud.common.util.BaseInfo.sAnimationCaptureC = r1
            goto L_0x0138
        L_0x0134:
            if (r0 == 0) goto L_0x0138
            io.dcloud.common.util.BaseInfo.sTimeOutCount = r1
        L_0x0138:
            io.dcloud.common.adapter.ui.DHImageView r0 = r6.mPageCImageView
            r0.refreshImagerView()
            io.dcloud.common.DHInterface.INativeView r0 = r6.mNativeView
            if (r0 != 0) goto L_0x014b
            if (r5 == 0) goto L_0x014a
            boolean r0 = io.dcloud.common.adapter.util.PlatformUtil.isWhiteBitmap(r5)
            if (r0 != 0) goto L_0x014a
            goto L_0x014b
        L_0x014a:
            return r1
        L_0x014b:
            int r0 = r3.getMeasuredWidth()
            if (r0 == 0) goto L_0x019c
            int r0 = r3.getMeasuredHeight()
            if (r0 == 0) goto L_0x019c
            android.widget.FrameLayout$LayoutParams r0 = new android.widget.FrameLayout$LayoutParams
            int r9 = r3.getMeasuredWidth()
            int r10 = r3.getMeasuredHeight()
            r0.<init>(r9, r10)
            int r9 = android.os.Build.VERSION.SDK_INT
            r10 = 22
            if (r9 >= r10) goto L_0x0197
            boolean r9 = r6.isChildOfFrameView
            if (r9 == 0) goto L_0x0197
            int r9 = r3.getMeasuredHeight()
            io.dcloud.common.adapter.util.ViewOptions r10 = r16.obtainFrameOptions()
            int r10 = r10.top
            int r9 = r9 + r10
            io.dcloud.common.adapter.ui.AdaContainerFrameItem r10 = r16.getParentFrameItem()
            io.dcloud.common.adapter.util.ViewOptions r10 = r10.obtainFrameOptions()
            boolean r10 = r10.isStatusbar
            if (r10 == 0) goto L_0x0188
            int r10 = io.dcloud.common.adapter.util.DeviceInfo.sStatusBarHeight
            goto L_0x0189
        L_0x0188:
            r10 = 0
        L_0x0189:
            int r9 = r9 - r10
            r0.height = r9
            io.dcloud.common.adapter.ui.DHImageView r9 = r6.mPageCImageView
            io.dcloud.common.adapter.util.ViewOptions r10 = r16.obtainFrameOptions()
            int r10 = r10.top
            r9.setPadding(r1, r10, r1, r1)
        L_0x0197:
            io.dcloud.common.adapter.ui.DHImageView r9 = r6.mPageCImageView
            r9.setLayoutParams(r0)
        L_0x019c:
            io.dcloud.common.DHInterface.INativeView r0 = r6.mNativeView
            if (r0 != 0) goto L_0x01a6
            io.dcloud.common.adapter.ui.DHImageView r0 = r6.mPageCImageView
            r6.addCaptureImageView(r4, r0, r5)
            goto L_0x01b0
        L_0x01a6:
            io.dcloud.common.adapter.ui.DHImageView r0 = r6.mPageCImageView
            r0.bringToFront()
            io.dcloud.common.adapter.ui.DHImageView r0 = r6.mPageCImageView
            r0.setVisibility(r1)
        L_0x01b0:
            r0 = 4
            r3.setVisibility(r0)
            io.dcloud.common.adapter.ui.DHImageView r0 = r6.mPageCImageView
            io.dcloud.common.adapter.util.ViewOptions r5 = r6.mViewOptions
            int r5 = r5.left
            float r5 = (float) r5
            io.dcloud.nineoldandroids.view.ViewHelper.setX(r0, r5)
            io.dcloud.common.adapter.util.ViewOptions r0 = r6.mViewOptions
            int r0 = r0.top
            boolean r5 = r6.isChildOfFrameView
            if (r5 == 0) goto L_0x01d5
            io.dcloud.common.adapter.ui.AdaContainerFrameItem r5 = r16.getParentFrameItem()
            io.dcloud.common.adapter.util.ViewOptions r5 = r5.obtainFrameOptions()
            boolean r5 = r5.isStatusbar
            if (r5 == 0) goto L_0x01d4
            int r1 = io.dcloud.common.adapter.util.DeviceInfo.sStatusBarHeight
        L_0x01d4:
            int r0 = r0 + r1
        L_0x01d5:
            io.dcloud.common.adapter.ui.DHImageView r1 = r6.mPageCImageView
            float r0 = (float) r0
            io.dcloud.nineoldandroids.view.ViewHelper.setY(r1, r0)
            io.dcloud.common.adapter.util.AnimOptions r0 = r6.mAnimOptions
            android.view.animation.Animation r0 = r0.mAnimator
            if (r0 != 0) goto L_0x01f9
            io.dcloud.common.adapter.ui.DHImageView r0 = r6.mPageCImageView
            r7.setTarget(r0)
            io.dcloud.common.adapter.ui.AdaFrameView$1 r9 = new io.dcloud.common.adapter.ui.AdaFrameView$1
            r0 = r9
            r1 = r16
            r2 = r18
            r5 = r17
            r0.<init>(r2, r3, r4, r5)
            r7.addListener(r9)
            r17.start()
            goto L_0x020c
        L_0x01f9:
            io.dcloud.common.adapter.ui.AdaFrameView$2 r1 = new io.dcloud.common.adapter.ui.AdaFrameView$2
            r1.<init>(r3, r2)
            r0.setAnimationListener(r1)
            io.dcloud.common.adapter.ui.DHImageView r0 = r6.mPageCImageView
            if (r0 == 0) goto L_0x020c
            io.dcloud.common.adapter.util.AnimOptions r1 = r6.mAnimOptions
            android.view.animation.Animation r1 = r1.mAnimator
            r0.startAnimation(r1)
        L_0x020c:
            return r8
        L_0x020d:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.ui.AdaFrameView.captureAnimation(io.dcloud.nineoldandroids.animation.Animator, int):boolean");
    }

    private int getFrameHeight() {
        int height = obtainMainView().getHeight();
        if (obtainMainView().getParent() == null) {
            return height;
        }
        if (obtainFrameOptions().height == -1) {
            return obtainWebAppRootView().obtainMainView().getHeight();
        }
        if (obtainFrameOptions().isStatusbarDodifyHeight) {
            return obtainFrameOptions().height + DeviceInfo.sStatusBarHeight;
        }
        return obtainFrameOptions().height;
    }

    private int indexOfViewInParent(View view, ViewGroup viewGroup) {
        int i = 0;
        while (i < viewGroup.getChildCount() && viewGroup.getChildAt(i) != view) {
            i++;
        }
        return i;
    }

    /* access modifiers changed from: private */
    public void sendErrorMessage(int i, String str) {
        Message message = new Message();
        message.what = 0;
        message.arg1 = i;
        message.obj = str;
        this.mCaptureHandler.sendMessage(message);
    }

    public final void addFrameViewListener(IEventCallback iEventCallback) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList<>();
        }
        if (iEventCallback != null && !this.mListeners.contains(iEventCallback)) {
            this.mListeners.add(iEventCallback);
        }
    }

    public void animate(IWebview iWebview, String str, String str2) {
        if (obtainMainView() instanceof AbsoluteLayout) {
            ((AbsoluteLayout) obtainMainView()).animate(iWebview, str, str2);
        }
    }

    public void captureSnapshot(final String str, final ICallBack iCallBack, final ICallBack iCallBack2) {
        new Thread(new Runnable() {
            public void run() {
                if (AdaFrameView.this.getContext() != null) {
                    if (TextUtils.isEmpty(str) || !"loading".equals(str.toLowerCase(Locale.ENGLISH))) {
                        try {
                            AdaFrameView adaFrameView = AdaFrameView.this;
                            adaFrameView.mSnapshot = PlatformUtil.captureView(adaFrameView.obtainMainView());
                            AdaFrameView adaFrameView2 = AdaFrameView.this;
                            if (adaFrameView2.mSnapshot != null) {
                                ICallBack unused = adaFrameView2.mSucCallBack = iCallBack;
                                AdaFrameView.this.mCaptureHandler.sendEmptyMessage(1);
                                return;
                            }
                            ICallBack unused2 = adaFrameView2.mErrCallBack = iCallBack2;
                            AdaFrameView adaFrameView3 = AdaFrameView.this;
                            adaFrameView3.sendErrorMessage(-100, adaFrameView3.getContext().getString(R.string.dcloud_common_screenshot_fail));
                        } catch (Exception unused3) {
                            ICallBack unused4 = AdaFrameView.this.mErrCallBack = iCallBack2;
                            AdaFrameView adaFrameView4 = AdaFrameView.this;
                            adaFrameView4.sendErrorMessage(-100, adaFrameView4.getContext().getString(R.string.dcloud_common_screenshot_fail));
                        }
                    } else {
                        try {
                            AdaFrameView adaFrameView5 = AdaFrameView.this;
                            adaFrameView5.mLoadingSnapshot = PlatformUtil.captureView(adaFrameView5.obtainMainView());
                            AdaFrameView adaFrameView6 = AdaFrameView.this;
                            if (adaFrameView6.mLoadingSnapshot != null) {
                                ICallBack unused5 = adaFrameView6.mSucCallBack = iCallBack;
                                AdaFrameView.this.mCaptureHandler.sendEmptyMessage(1);
                                return;
                            }
                            ICallBack unused6 = adaFrameView6.mErrCallBack = iCallBack2;
                            AdaFrameView adaFrameView7 = AdaFrameView.this;
                            adaFrameView7.sendErrorMessage(-100, adaFrameView7.getContext().getString(R.string.dcloud_common_screenshot_fail));
                        } catch (Exception unused7) {
                            ICallBack unused8 = AdaFrameView.this.mErrCallBack = iCallBack2;
                            AdaFrameView adaFrameView8 = AdaFrameView.this;
                            adaFrameView8.sendErrorMessage(-100, adaFrameView8.getContext().getString(R.string.dcloud_common_screenshot_fail));
                        }
                    }
                }
            }
        }).start();
    }

    public void changeWebParentViewRect() {
        int i;
        ViewGroup viewGroup = (ViewGroup) obtainMainView();
        if (obtainWebviewParent() != null) {
            View obtainMainView = obtainWebviewParent().obtainMainView();
            if (viewGroup != null && obtainMainView != null) {
                try {
                    if (viewGroup.getHeight() == obtainMainView.getHeight()) {
                        if (viewGroup.getHeight() == obtainMainView.getHeight()) {
                            ViewOptions viewOptions = this.mViewOptions;
                            if (!viewOptions.isStatusbar && (viewOptions.titleNView == null || !obtainApp().obtainStatusBarMgr().isImmersive)) {
                                return;
                            }
                        } else {
                            return;
                        }
                    }
                    if (!obtainFrameOptions().hasBackground() || obtainMainView.getHeight() != obtainWebviewParent().obtainFrameOptions().height || this.mViewOptions.isStatusbar || obtainFrameOptions().titleNView != null) {
                        int frameHeight = getFrameHeight();
                        int i2 = 0;
                        for (int i3 = 0; i3 < viewGroup.getChildCount(); i3++) {
                            View childAt = viewGroup.getChildAt(i3);
                            if (childAt != obtainMainView) {
                                int height = childAt.getHeight();
                                if (childAt instanceof AbsoluteLayout) {
                                    if (((AbsoluteLayout) childAt).getFrameView().mPosition != ViewRect.DOCK_TOP) {
                                    }
                                } else if (childAt instanceof INativeView) {
                                    if (((INativeView) childAt).isDock()) {
                                        if (childAt.getTag() == null || ((!childAt.getTag().equals("NavigationBar") && !childAt.getTag().equals("titleNView")) || !((INativeView) childAt).isStatusBar())) {
                                            height = ((INativeView) childAt).getInnerHeight();
                                        } else {
                                            height = ((INativeView) childAt).getInnerHeight() + DeviceInfo.sStatusBarHeight;
                                        }
                                        if (!((INativeView) childAt).isDockTop()) {
                                            i = 0;
                                            frameHeight -= height;
                                            i2 += i;
                                        }
                                    }
                                } else if (!(childAt instanceof StatusBarView)) {
                                }
                                i = height;
                                frameHeight -= height;
                                i2 += i;
                            }
                        }
                        obtainMainView.getLayoutParams().height = frameHeight;
                        ViewHelper.setY(obtainMainView, (float) i2);
                        obtainMainView.requestLayout();
                        obtainMainView.invalidate();
                        if (!this.inStack) {
                            changeWebviewRect();
                        }
                    }
                } catch (Exception e) {
                    Log.e("AdaFrameItem", e.getMessage());
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void changeWebviewRect() {
        ViewGroup obtainWindowView = obtainWebView().obtainWindowView();
        if (obtainWindowView.getParent() instanceof FrameLayout) {
            obtainWindowView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        } else if (obtainWindowView.getParent() instanceof LinearLayout) {
            obtainWindowView.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        }
        obtainWindowView.requestLayout();
    }

    public void chkUseCaptureAnimation(boolean z, int i, boolean z2) {
        StringBuilder sb;
        String str;
        boolean z3;
        if (z) {
            sb = new StringBuilder();
            str = "B页面";
        } else {
            sb = new StringBuilder();
            str = "C页面";
        }
        sb.append(str);
        sb.append(i);
        String sb2 = sb.toString();
        boolean z4 = false;
        if (this.mAccelerationType.equals("none") && !z2) {
            this.mAnimationCapture = false;
        } else if (!this.mAccelerationType.equals(AbsoluteConst.ACCELERATION) && this.mSnapshot == null && this.mNativeView == null && Build.VERSION.SDK_INT >= 23) {
            this.mAnimationCapture = false;
            Logger.e("mabo", sb2 + "1是否启用截图动画方案:" + this.mAnimationCapture);
        } else if (this.mViewOptions.mUniNViewJson != null) {
            this.mAnimationCapture = false;
        } else {
            byte b = this.mAnimOptions.mOption;
            boolean z5 = true;
            boolean z6 = b == 3 || b == 1;
            if (this.mSnapshot == null && this.mNativeView == null && (!obtainWebView().isLoaded() || DeviceInfo.sDeviceSdkVer < 11)) {
                this.mAnimationCapture = false;
                Logger.e("mabo", sb2 + "1是否启用截图动画方案:" + this.mAnimationCapture);
            } else if (this.isChildOfFrameView) {
                this.mAnimationCapture = false;
                Logger.e("mabo", sb2 + "2是否启用截图动画方案:" + this.mAnimationCapture);
            } else {
                PlatformUtil.MESURE_SCREEN_STATUSBAR_HEIGHT(obtainWebView().getActivity());
                if (this.mLastScreenHeight != PlatformUtil.SCREEN_HEIGHT(this.mContext)) {
                    this.mLastScreenHeight = PlatformUtil.SCREEN_HEIGHT(this.mContext);
                    this.mAnimationCapture = false;
                    Logger.e("mabo", sb2 + "3是否启用截图动画方案:" + this.mAnimationCapture);
                    return;
                }
                boolean z7 = (this.mAccelerationType.equals("auto") && !PdrUtil.isEquals(this.mAnimOptions.mAnimType, AnimOptions.ANIM_FADE_IN) && z6 && !PdrUtil.isContains(this.mAnimOptions.mAnimType, "slide")) || this.mAccelerationType.equals(AbsoluteConst.CAPTURE);
                if (!z6) {
                    AnimOptions animOptions = this.mAnimOptions;
                    byte b2 = animOptions.mOption;
                    boolean z8 = b2 == 4 || b2 == 0;
                    if (z7 || PdrUtil.isEquals(animOptions.mAnimType, AnimOptions.ANIM_POP_IN) || PdrUtil.isEquals(this.mAnimOptions.mAnimType, AnimOptions.ANIM_ZOOM_FADE_OUT)) {
                        z4 = true;
                    }
                    z3 = z8 & z4;
                } else {
                    if (z7 || PdrUtil.isEquals(this.mAnimOptions.mAnimType_close, AnimOptions.ANIM_POP_OUT) || PdrUtil.isEquals(this.mAnimOptions.mAnimType, AnimOptions.ANIM_ZOOM_FADE_IN)) {
                        z4 = true;
                    }
                    z3 = true & z4;
                }
                if (this.isChildOfFrameView && PdrUtil.isEquals(this.mAnimOptions.mAnimType, AnimOptions.ANIM_FADE_IN)) {
                    z3 = true;
                }
                if (this.mSnapshot == null && this.mNativeView == null) {
                    z5 = z3;
                }
                this.mAnimationCapture = z5;
            }
        }
    }

    public void clearSnapshot(String str) {
        DHImageView dHImageView = this.mPageCImageView;
        if (dHImageView != null) {
            dHImageView.setImageBitmap((Bitmap) null);
        }
        if (TextUtils.isEmpty(str) || !"loading".equals(str.toLowerCase(Locale.ENGLISH))) {
            Bitmap bitmap = this.mSnapshot;
            if (bitmap != null) {
                try {
                    if (!bitmap.isRecycled()) {
                        this.mSnapshot.recycle();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.mSnapshot = null;
            return;
        }
        Bitmap bitmap2 = this.mLoadingSnapshot;
        if (bitmap2 != null) {
            try {
                if (!bitmap2.isRecycled()) {
                    this.mLoadingSnapshot.recycle();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        this.mLoadingSnapshot = null;
    }

    public final void dispatchFrameViewEvents(String str, Object obj) {
        if (this.mListeners != null) {
            Logger.d("AdaFrameView.dispatchFrameViewEvents type=" + str + ";args=" + obj);
            for (int size = this.mListeners.size() + -1; size >= 0; size--) {
                IEventCallback iEventCallback = this.mListeners.get(size);
                if (PdrUtil.isEquals(str, AbsoluteConst.EVENTS_CLOSE)) {
                    this.mListeners.remove(size);
                }
                iEventCallback.onCallBack(str, obj);
            }
        }
    }

    public void dispose() {
        super.dispose();
        if (this.mRefreshView != null) {
            this.mRefreshView = null;
        }
        if (this.mBounceView != null) {
            this.mBounceView = null;
        }
        if (this.mCircleRefreshView != null) {
            this.mCircleRefreshView = null;
        }
    }

    public synchronized void draw(View view, INativeBitmap iNativeBitmap, boolean z, boolean z2, Rect rect, String str, ICallBack iCallBack, ICallBack iCallBack2) {
        synchronized (this) {
            final View view2 = view;
            final boolean z3 = z;
            final boolean z4 = z2;
            final Rect rect2 = rect;
            final String str2 = str;
            final INativeBitmap iNativeBitmap2 = iNativeBitmap;
            final ICallBack iCallBack3 = iCallBack;
            final ICallBack iCallBack4 = iCallBack2;
            this.mCaptureHandler.post(new Runnable() {
                public void run() {
                    try {
                        if (AdaFrameView.this.getContext() != null) {
                            Bitmap captureView = PlatformUtil.captureView(view2, z3, z4, rect2, str2);
                            if (captureView != null) {
                                iNativeBitmap2.setBitmap(captureView);
                                ICallBack unused = AdaFrameView.this.mSucCallBack = iCallBack3;
                                AdaFrameView.this.mCaptureHandler.sendEmptyMessage(1);
                                return;
                            }
                            ICallBack unused2 = AdaFrameView.this.mErrCallBack = iCallBack4;
                            AdaFrameView adaFrameView = AdaFrameView.this;
                            adaFrameView.sendErrorMessage(-101, adaFrameView.getContext().getString(R.string.dcloud_common_screenshot_blank));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ICallBack unused3 = AdaFrameView.this.mErrCallBack = iCallBack4;
                        AdaFrameView adaFrameView2 = AdaFrameView.this;
                        adaFrameView2.sendErrorMessage(-100, adaFrameView2.getContext().getString(R.string.dcloud_common_screenshot_fail));
                    }
                }
            });
        }
    }

    public IFrameView findPageB() {
        return obtainWebAppRootView().findFrameViewB(this);
    }

    public IRefreshAble getCircleRefreshView() {
        return this.mCircleRefreshView;
    }

    public int getFrameType() {
        return this.mFrameType;
    }

    public void handleNativeViewByAction(DHImageView dHImageView, int i) {
        if (!"none".equals(this.mNativeViewAction) && ("hide".equals(this.mNativeViewAction) || AbsoluteConst.EVENTS_CLOSE.equals(this.mNativeViewAction))) {
            String viewUUId = this.mNativeView.getViewUUId();
            String str = this.mNativeViewAction;
            if (str.equalsIgnoreCase("hide")) {
                if (dHImageView != null) {
                    dHImageView.setVisibility(4);
                }
            } else if (this.mNativeViewAction.equalsIgnoreCase(AbsoluteConst.EVENTS_CLOSE)) {
                if (dHImageView != null) {
                    dHImageView.setVisibility(4);
                    dHImageView.setImageBitmap((Bitmap) null);
                    dHImageView.removeNativeView();
                }
                str = "view_close";
            }
            this.mNativeView = null;
            this.mNativeViewAction = "none";
            if (obtainWindowMgr() != null) {
                AbsMgr obtainWindowMgr = obtainWindowMgr();
                IMgr.MgrType mgrType = IMgr.MgrType.FeatureMgr;
                obtainWindowMgr.processEvent(mgrType, 1, new Object[]{obtainWebView(), "nativeobj", str, JSONUtil.createJSONArray("['" + viewUUId + "','" + viewUUId + "']")});
            }
        } else if (i == 1 && dHImageView != null) {
            dHImageView.clearAnimation();
            dHImageView.setVisibility(4);
            dHImageView.setImageBitmap((Bitmap) null);
            dHImageView.removeNativeView();
        }
    }

    /* access modifiers changed from: protected */
    public abstract void initMainView(Context context, int i, Object obj);

    public void interceptTouchEvent(boolean z) {
        this.interceptTouchEvent = z;
    }

    public boolean isSupportLongTouch() {
        return false;
    }

    public boolean isTabItem() {
        return this.isTabItem || obtainFrameOptions().isTabItem.booleanValue();
    }

    public boolean isWebviewCovered() {
        ViewGroup obtainWindowView;
        IWebview obtainWebView = obtainWebView();
        if (obtainWebView == null || (obtainWindowView = obtainWebView.obtainWindowView()) == null || obtainWindowView.getVisibility() != 0 || obtainWindowView.getParent() == null) {
            return true;
        }
        Rect rect = new Rect(0, 0, PlatformUtil.SCREEN_WIDTH(obtainWindowView.getContext()), PlatformUtil.SCREEN_HEIGHT(obtainWindowView.getContext()));
        Rect rect2 = new Rect();
        obtainWindowView.getGlobalVisibleRect(rect2);
        if (!rect.contains(rect2)) {
            return true;
        }
        while (obtainWindowView.getParent() instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) obtainWindowView.getParent();
            if (viewGroup.getVisibility() != 0) {
                return true;
            }
            for (int indexOfViewInParent = indexOfViewInParent(obtainWindowView, viewGroup) + 1; indexOfViewInParent < viewGroup.getChildCount(); indexOfViewInParent++) {
                View childAt = viewGroup.getChildAt(indexOfViewInParent);
                if (childAt.getVisibility() == 0 && !(childAt instanceof IWaiter)) {
                    Rect rect3 = new Rect();
                    childAt.getGlobalVisibleRect(rect3);
                    if (rect3.contains(rect2)) {
                        return true;
                    }
                }
            }
            obtainWindowView = viewGroup;
        }
        if (obtainWindowView.getParent() == null) {
            return true;
        }
        return false;
    }

    public abstract IApp obtainApp();

    public abstract String obtainPrePlusreadyJs();

    public byte obtainStatus() {
        return this.mFrameStatus;
    }

    public abstract IWebview obtainWebView();

    public abstract AbsMgr obtainWindowMgr();

    public void onConfigurationChanged() {
    }

    public void onDestroy() {
        this.mFrameStatus = 4;
        transition((byte) 4);
        dispose();
    }

    public boolean onDispose() {
        boolean onDispose = super.onDispose();
        dispatchFrameViewEvents(AbsoluteConst.EVENTS_CLOSE, obtainWebView());
        DHImageView dHImageView = this.mPageCImageView;
        if (dHImageView != null) {
            dHImageView.setImageBitmap((Bitmap) null);
        }
        return onDispose;
    }

    public void onDrawAfter(Canvas canvas) {
        IRefreshAble iRefreshAble;
        if (obtainMainView() != null && obtainMainView().getVisibility() == 0 && (iRefreshAble = this.mCircleRefreshView) != null && iRefreshAble.isRefreshEnable()) {
            canvas.save();
            int left = obtainWebviewParent().obtainMainView().getLeft();
            int y = (int) ViewHelper.getY(obtainWebviewParent().obtainMainView());
            ViewOptions viewOptions = this.mViewOptions;
            if (viewOptions != null && viewOptions.isStatusbar && viewOptions.titleNView == null) {
                y += DeviceInfo.sStatusBarHeight;
            }
            canvas.translate((float) left, (float) y);
            this.mCircleRefreshView.onSelfDraw(canvas);
            canvas.restore();
        }
    }

    public void onInit() {
        this.mFrameStatus = 0;
    }

    public void onLoading() {
        this.mFrameStatus = 2;
    }

    public void onPreLoading() {
        this.mFrameStatus = 1;
    }

    public void onPreShow(IFrameView iFrameView) {
        this.mFrameStatus = 3;
        transition((byte) 3);
    }

    /* access modifiers changed from: protected */
    public void onResize() {
        if (obtainApp() != null && obtainApp().manifestBeParsed()) {
            super.onResize();
            RefreshView refreshView = this.mRefreshView;
            if (refreshView != null) {
                refreshView.onResize();
            }
            BounceView bounceView = this.mBounceView;
            if (bounceView != null) {
                bounceView.onResize();
            }
            dispatchFrameViewEvents(AbsoluteConst.EVENTS_FRAME_ONRESIZE, (Object) null);
        }
    }

    public void paint(Canvas canvas) {
        super.paint(canvas);
        if (obtainMainView() != null && obtainMainView().getVisibility() == 0 && this.mRefreshView != null && !this.isSlipping) {
            Logger.d(Logger.VIEW_VISIBLE_TAG, "AdaFrameView.paint mRefreshView paint" + this);
            ViewOptions viewOptions = obtainWebviewParent().mViewOptions_birth;
            ViewOptions viewOptions2 = obtainWebviewParent().mViewOptions;
            if (viewOptions == null) {
                viewOptions = this.mViewOptions_birth;
            }
            if (viewOptions2 == null) {
                viewOptions2 = this.mViewOptions;
            }
            int i = viewOptions.left;
            int i2 = viewOptions2.left;
            int i3 = 0;
            if (i == i2) {
                i2 = 0;
            }
            int i4 = viewOptions.top;
            int i5 = viewOptions2.top;
            if (i4 != i5) {
                i3 = i5;
            }
            this.mRefreshView.paint(canvas, i2 + obtainWebviewParent().obtainMainView().getLeft(), (int) (((float) i3) + ViewHelper.getY(obtainWebviewParent().obtainMainView())));
        }
    }

    public final void removeFrameViewListener(IEventCallback iEventCallback) {
        ArrayList<IEventCallback> arrayList = this.mListeners;
        if (arrayList != null) {
            arrayList.remove(iEventCallback);
        }
    }

    public void restore() {
        if (obtainMainView() instanceof AbsoluteLayout) {
            ((AbsoluteLayout) obtainMainView()).restore();
        }
    }

    public void setAccelerationType(String str) {
        this.mAccelerationType = str;
    }

    public void setCircleRefreshView(IRefreshAble iRefreshAble) {
        this.mCircleRefreshView = iRefreshAble;
    }

    public void setSnapshot(Bitmap bitmap) {
        Bitmap bitmap2 = this.mSnapshot;
        if (bitmap2 != null && !bitmap2.isRecycled()) {
            this.mSnapshot.recycle();
        }
        this.mSnapshot = bitmap;
    }

    public void setSnapshotView(INativeView iNativeView, String str) {
        this.mNativeView = iNativeView;
        this.mNativeViewAction = str;
    }

    public void setTabItem(boolean z) {
        this.isTabItem = z;
    }

    public void setVisible(boolean z, boolean z2) {
        Logger.d(Logger.VIEW_VISIBLE_TAG, "AdaFrameView.setVisible pVisible" + z + "       " + this);
        setVisibility(z ? AdaFrameItem.VISIBLE : AdaFrameItem.INVISIBLE);
    }

    public void startAnimator(int i) {
        startAnimator((OnAnimationEnd) null, i);
    }

    public void transition(byte b) {
    }

    public void updateFrameRelViewRect(ViewRect viewRect) {
        ViewRect parentViewRect;
        ViewGroup viewGroup = (ViewGroup) obtainMainView();
        if (!this.mViewOptions.hasBackground()) {
            this.mViewOptions.updateViewData(viewRect);
            if (!(viewGroup == null || viewGroup.getVisibility() != 0 || this.mRefreshView == null)) {
                obtainWebviewParent().reInit();
            }
        } else if (obtainWebviewParent().obtainFrameOptions().allowUpdate) {
            obtainWebviewParent().obtainFrameOptions().updateViewData(viewRect);
        }
        ViewOptions viewOptions = this.mViewOptions;
        int i = viewOptions.top;
        int i2 = viewOptions.height;
        if (this.isChildOfFrameView && (parentViewRect = viewOptions.getParentViewRect()) != null && parentViewRect.isStatusbar && !this.mViewOptions.isStatusbar) {
            if (obtainFrameOptions().isBottomAbsolute()) {
                i2 -= DeviceInfo.sStatusBarHeight;
            }
            i += DeviceInfo.sStatusBarHeight;
        }
        View view = this.mViewImpl;
        ViewOptions viewOptions2 = this.mViewOptions;
        AdaFrameItem.LayoutParamsUtil.setViewLayoutParams(view, viewOptions2.left, i, viewOptions2.width, i2);
        changeWebParentViewRect();
        if (viewGroup != null) {
            obtainMainView().invalidate();
        }
    }

    public void startAnimator(final OnAnimationEnd onAnimationEnd, int i) {
        AnimOptions animOptions = this.mAnimOptions;
        if (animOptions != null) {
            animOptions.mUserFrameItem = this;
            animOptions.sScreenWidth = obtainApp().getInt(0);
            this.mAnimOptions.sScreenHeight = obtainApp().getInt(1);
            Animator createAnimation = this.mAnimOptions.createAnimation();
            if (obtainFrameOptions().hasBackground() && this.mAnimOptions.mOption == 2) {
                AdaWebViewParent obtainWebviewParent = obtainWebviewParent();
                this.mAnimOptions.mUserFrameItem = obtainWebviewParent;
                createAnimation.setTarget(obtainWebviewParent.obtainMainView());
                createAnimation.addListener(this.mAnimatorListener);
                createAnimation.start();
            } else if (!captureAnimation(createAnimation, i)) {
                DHImageView dHImageView = this.mPageCImageView;
                if (dHImageView != null && !dHImageView.isSlipping()) {
                    this.mPageCImageView.clearAnimation();
                    this.mPageCImageView.setVisibility(4);
                    this.mPageCImageView.setImageBitmap((Bitmap) null);
                }
                this.mViewImpl.bringToFront();
                Animation animation = this.mAnimOptions.mAnimator;
                if (animation == null) {
                    createAnimation.setTarget(this.mViewImpl);
                    createAnimation.addListener(this.mAnimatorListener);
                    createAnimation.start();
                } else {
                    animation.setAnimationListener(this.mAnimationListener);
                    this.mViewImpl.startAnimation(this.mAnimOptions.mAnimator);
                }
            }
            createAnimation.setInterpolator(new DecelerateInterpolator());
            AnimOptions animOptions2 = this.mAnimOptions;
            MessageHandler.sendMessage(new MessageHandler.IMessages() {
                public void execute(Object obj) {
                    Animator.AnimatorListener animatorListener;
                    AdaFrameView adaFrameView = AdaFrameView.this;
                    if (!adaFrameView.mAnimationStarted && (animatorListener = adaFrameView.mAnimatorListener) != null) {
                        animatorListener.onAnimationEnd((Animator) null);
                    }
                    OnAnimationEnd onAnimationEnd = onAnimationEnd;
                    if (onAnimationEnd != null) {
                        onAnimationEnd.onDone();
                    }
                    if (BaseInfo.sOpenedCount == 0) {
                        BaseInfo.sFullScreenChanged = false;
                    }
                }
            }, (long) Math.max(animOptions2.duration_show, Math.max(animOptions2.duration_close, animOptions2.duration)), (Object) null);
        }
    }
}
