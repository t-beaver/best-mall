package io.dcloud.common.adapter.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AbsoluteLayout;
import com.dcloud.android.widget.StatusBarView;
import com.taobao.weex.common.Constants;
import io.dcloud.common.DHInterface.INativeView;
import io.dcloud.common.adapter.util.AnimOptions;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.ViewOptions;
import io.dcloud.common.adapter.util.ViewRect;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.TitleNViewUtil;
import io.dcloud.nineoldandroids.animation.Animator;
import io.dcloud.nineoldandroids.animation.AnimatorSet;
import io.dcloud.nineoldandroids.animation.ObjectAnimator;
import io.dcloud.nineoldandroids.view.ViewHelper;
import java.util.HashMap;

public class AdaFrameItem {
    public static int GONE = 8;
    public static int INVISIBLE = 4;
    static final String TAG = "AdaFrameItem";
    public static int VISIBLE;
    public boolean isSlipping = false;
    public long lastShowTime = 0;
    protected AnimOptions mAnimOptions = null;
    private Animation mAnimation = null;
    public Animator.AnimatorListener mAnimatorListener = null;
    private boolean mAutoPop = false;
    private boolean mAutoPush = false;
    private Context mContextWrapper = null;
    protected boolean mLongPressed = false;
    public INativeView mNativeView = null;
    public boolean mNeedOrientationUpdate = false;
    public AdaContainerFrameItem mParentFrameItem = null;
    public int mPosition = ViewRect.POSITION_ABSOLUTE;
    protected boolean mPressed = false;
    public boolean mStranslate = false;
    protected View mViewImpl = null;
    protected ViewOptions mViewOptions = null;
    protected ViewOptions mViewOptions_animate = null;
    protected ViewOptions mViewOptions_birth = null;
    public int mZIndex = 0;

    private class DefaultView extends View {
        public DefaultView(Context context) {
            super(context);
        }

        /* access modifiers changed from: protected */
        public void onConfigurationChanged(Configuration configuration) {
            super.onConfigurationChanged(configuration);
            AdaFrameItem.this.onResize();
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            AdaFrameItem.this.paint(canvas);
        }
    }

    public static class LayoutParamsUtil {
        public static ViewGroup.LayoutParams createLayoutParams(int i, int i2, int i3, int i4) {
            return new AbsoluteLayout.LayoutParams(i3, i4, i, i2);
        }

        private static void preAndroid30SetViewLayoutParams(final View view, final int i, final int i2) {
            if (!(i == 0 && i2 == 0) && DeviceInfo.sDeviceSdkVer <= 10) {
                AnimatorSet animatorSet = new AnimatorSet();
                ObjectAnimator objectAnimator = new ObjectAnimator();
                objectAnimator.setPropertyName(Constants.Name.X);
                objectAnimator.setFloatValues((float) (i - 1), (float) i);
                animatorSet.playTogether(objectAnimator);
                ObjectAnimator objectAnimator2 = new ObjectAnimator();
                objectAnimator2.setPropertyName(Constants.Name.Y);
                objectAnimator2.setFloatValues((float) (i2 - 1), (float) i2);
                animatorSet.playTogether(objectAnimator2);
                animatorSet.setDuration(5);
                animatorSet.setTarget(view);
                animatorSet.addListener(new Animator.AnimatorListener() {
                    public void onAnimationCancel(Animator animator) {
                        view.postDelayed(new Runnable() {
                            public void run() {
                                AnonymousClass1 r0 = AnonymousClass1.this;
                                ViewHelper.setX(view, (float) i);
                                AnonymousClass1 r02 = AnonymousClass1.this;
                                ViewHelper.setY(view, (float) i2);
                            }
                        }, 10);
                    }

                    public void onAnimationEnd(Animator animator) {
                    }

                    public void onAnimationRepeat(Animator animator) {
                    }

                    public void onAnimationStart(Animator animator) {
                    }
                });
                animatorSet.start();
            }
        }

        public static void setViewLayoutParams(View view, int i, int i2, int i3, int i4) {
            if (view.getLayoutParams() instanceof AbsoluteLayout.LayoutParams) {
                ((AbsoluteLayout.LayoutParams) view.getLayoutParams()).x = 0;
                ((AbsoluteLayout.LayoutParams) view.getLayoutParams()).y = 0;
            }
            view.setTop(0);
            view.setLeft(0);
            ViewHelper.setX(view, (float) i);
            ViewHelper.setY(view, (float) i2);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(i3, i4);
            } else {
                layoutParams.width = i3;
                layoutParams.height = i4;
            }
            view.setLayoutParams(layoutParams);
        }
    }

    protected AdaFrameItem(Context context) {
        this.mContextWrapper = context;
        setFrameOptions(new ViewOptions());
        this.mViewOptions.mTag = this;
    }

    private void addStatusBar(AdaFrameItem adaFrameItem) {
        boolean z;
        int statusHeight;
        ViewOptions obtainFrameOptions = adaFrameItem.obtainFrameOptions();
        if (obtainFrameOptions.isStatusbar && ((z = adaFrameItem instanceof AdaFrameView)) && -1 != (statusHeight = DeviceInfo.getStatusHeight(adaFrameItem.getContext()))) {
            int hashCode = adaFrameItem.hashCode();
            int statusBarDefaultColor = z ? ((AdaFrameView) adaFrameItem).obtainApp().obtainStatusBarMgr().getStatusBarDefaultColor() : 0;
            if (!PdrUtil.isEmpty(obtainFrameOptions.mStatusbarColor)) {
                int stringToColor = PdrUtil.stringToColor(obtainFrameOptions.mStatusbarColor);
                if (PdrUtil.checkStatusbarColor(stringToColor)) {
                    statusBarDefaultColor = stringToColor;
                }
            }
            ViewGroup viewGroup = (ViewGroup) adaFrameItem.obtainMainView();
            if (viewGroup.findViewById(hashCode) == null && obtainFrameOptions.titleNView == null) {
                StatusBarView statusBarView = new StatusBarView(adaFrameItem.getContext());
                if (!(adaFrameItem.obtainFrameOptions() == null || adaFrameItem.obtainFrameOptions().titleNView == null || !"transparent".equals(adaFrameItem.obtainFrameOptions().titleNView.optString("type")))) {
                    statusBarDefaultColor = Color.argb(0, Color.red(statusBarDefaultColor), Color.green(statusBarDefaultColor), Color.blue(statusBarDefaultColor));
                }
                statusBarView.setStatusBarHeight(statusHeight);
                statusBarView.setBackgroundColor(statusBarDefaultColor);
                statusBarView.setId(hashCode);
                ViewGroup viewGroup2 = (ViewGroup) ((AdaFrameView) adaFrameItem).obtainWebviewParent().obtainMainView();
                viewGroup.addView(statusBarView);
            }
        }
    }

    public final void clearAnimInfo() {
        if (!this.isSlipping) {
            this.mAnimation = null;
            this.mAnimatorListener = null;
            View view = this.mViewImpl;
            if (view != null) {
                view.clearAnimation();
            }
        }
    }

    public void dispose() {
        onDispose();
        clearAnimInfo();
        View view = this.mViewImpl;
        if (view != null) {
            view.setVisibility(GONE);
            ViewGroup viewGroup = (ViewGroup) this.mViewImpl.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(this.mViewImpl);
            }
            this.mViewImpl = null;
        }
    }

    public AdaFrameItem findLastFrameItem(AdaFrameItem adaFrameItem) {
        return null;
    }

    public Activity getActivity() {
        return (Activity) this.mContextWrapper;
    }

    public AnimOptions getAnimOptions() {
        if (this.mAnimOptions == null) {
            this.mAnimOptions = new AnimOptions();
        }
        return this.mAnimOptions;
    }

    public Context getContext() {
        return this.mContextWrapper;
    }

    public AdaFrameItem getParent() {
        return null;
    }

    public AdaContainerFrameItem getParentFrameItem() {
        return this.mParentFrameItem;
    }

    public boolean isAutoPop() {
        return this.mAutoPop;
    }

    public boolean isAutoPush() {
        return this.mAutoPush;
    }

    public boolean isDisposed() {
        return this.mViewImpl == null;
    }

    public void makeViewOptions_animate() {
        obtainFrameOptions_Birth();
        ViewOptions obtainFrameOptions = obtainFrameOptions();
        ViewOptions viewOptions = this.mViewOptions_animate;
        if (viewOptions == null) {
            ViewOptions viewOptions2 = this.mViewOptions;
            viewOptions = ViewOptions.createViewOptionsData(viewOptions2, viewOptions2.getParentViewRect());
            this.mViewOptions_animate = viewOptions;
        }
        if (viewOptions.hasBackground()) {
            viewOptions.anim_top = 0;
            viewOptions.anim_left = 0;
            obtainFrameOptions.anim_top = 0;
            obtainFrameOptions.anim_left = 0;
        }
        AnimOptions animOptions = this.mAnimOptions;
        byte b = animOptions.mOption;
        if (b == 0 || b == 4) {
            String str = animOptions.mAnimType;
            if (PdrUtil.isEmpty(str)) {
                str = "none";
            }
            if (PdrUtil.isEquals(str, AnimOptions.ANIM_SLIDE_IN_RIGHT) || PdrUtil.isEquals(str, AnimOptions.ANIM_POP_IN)) {
                obtainFrameOptions.anim_left = this.mAnimOptions.sScreenWidth;
            } else if (PdrUtil.isEquals(str, AnimOptions.ANIM_SLIDE_IN_LEFT)) {
                int i = obtainFrameOptions.width;
                if (i == -1) {
                    i = this.mAnimOptions.sScreenWidth;
                }
                obtainFrameOptions.anim_left = -i;
            } else if (PdrUtil.isEquals(str, AnimOptions.ANIM_SLIDE_IN_TOP)) {
                int i2 = obtainFrameOptions.height;
                if (i2 == -1) {
                    i2 = this.mAnimOptions.sScreenHeight;
                }
                obtainFrameOptions.anim_top = -i2;
            } else if (PdrUtil.isEquals(str, AnimOptions.ANIM_SLIDE_IN_BOTTOM)) {
                obtainFrameOptions.anim_top = this.mAnimOptions.sScreenHeight;
            }
        } else if (b == 1 || 3 == b) {
            String str2 = animOptions.mAnimType_close;
            HashMap<String, String> hashMap = AnimOptions.mAnimTypes;
            if (!hashMap.containsValue(str2)) {
                str2 = hashMap.get(this.mAnimOptions.mAnimType);
            }
            if (PdrUtil.isEquals(str2, AnimOptions.ANIM_SLIDE_OUT_RIGHT) || PdrUtil.isEquals(str2, AnimOptions.ANIM_POP_OUT)) {
                viewOptions.anim_left = this.mAnimOptions.sScreenWidth;
            } else if (PdrUtil.isEquals(str2, AnimOptions.ANIM_SLIDE_OUT_LEFT)) {
                viewOptions.anim_left = -(viewOptions.hasBackground() ? this.mAnimOptions.sScreenWidth : viewOptions.width);
            } else if (PdrUtil.isEquals(str2, AnimOptions.ANIM_SLIDE_OUT_TOP)) {
                viewOptions.anim_top = -(viewOptions.hasBackground() ? this.mAnimOptions.sScreenHeight : viewOptions.height);
            } else if (PdrUtil.isEquals(str2, AnimOptions.ANIM_SLIDE_OUT_BOTTOM)) {
                viewOptions.anim_top = this.mAnimOptions.sScreenHeight;
            }
        }
    }

    public ViewOptions obtainFrameOptions() {
        return this.mViewOptions;
    }

    public ViewOptions obtainFrameOptions_Animate() {
        return this.mViewOptions_animate;
    }

    public ViewOptions obtainFrameOptions_Birth() {
        return this.mViewOptions_birth;
    }

    public View obtainMainView() {
        return this.mViewImpl;
    }

    public boolean onDispose() {
        return true;
    }

    public void onPopFromStack(boolean z) {
        this.mAutoPop = z;
    }

    public void onPushToStack(boolean z) {
        this.mAutoPush = z;
    }

    /* access modifiers changed from: protected */
    public void onResize() {
        if (this.mNeedOrientationUpdate && !isDisposed()) {
            addStatusBar(this);
            ViewOptions viewOptions = this.mViewOptions;
            boolean z = this instanceof AdaFrameView;
            boolean z2 = z && ((AdaFrameView) this).isChildOfFrameView;
            viewOptions.onScreenChanged();
            View obtainMainView = obtainMainView();
            ViewGroup.LayoutParams layoutParams = obtainMainView.getLayoutParams();
            int i = -1;
            if (!z || z2) {
                if (z) {
                    int i2 = viewOptions.height;
                    if (i2 > 0 || i2 == -1) {
                        layoutParams.height = i2;
                    }
                    int i3 = viewOptions.width;
                    if (i3 > 0 || i3 == -1) {
                        layoutParams.width = i3;
                    }
                    obtainMainView.setLayoutParams(layoutParams);
                }
                if (z2 && z) {
                    int i4 = viewOptions.top;
                    int i5 = viewOptions.left;
                    int i6 = layoutParams.height;
                    int i7 = layoutParams.width;
                    AdaContainerFrameItem parentFrameItem = getParentFrameItem();
                    if (parentFrameItem != null) {
                        if (this.mPosition == ViewRect.DOCK_TOP && parentFrameItem.obtainFrameOptions().titleNView != null && TitleNViewUtil.isTitleTypeForDef(parentFrameItem.obtainFrameOptions().titleNView)) {
                            i4 += PdrUtil.convertToScreenInt("44px", 0, 0, ((AdaFrameView) this).obtainWebView().getScale());
                        }
                        if (!viewOptions.isStatusbar && parentFrameItem.obtainFrameOptions().isStatusbar) {
                            i4 += DeviceInfo.sStatusBarHeight;
                            if (viewOptions.isBottomAbsolute()) {
                                i6 -= DeviceInfo.sStatusBarHeight;
                            }
                        }
                    }
                    if (viewOptions.isStatusbar && !viewOptions.isBottomAbsolute()) {
                        i6 += DeviceInfo.sStatusBarHeight;
                    }
                    if (i7 <= 0 && i7 != -1) {
                        i7 = obtainMainView.getWidth();
                    }
                    if (i6 <= 0 && i6 != -1) {
                        i6 = obtainMainView.getHeight();
                    }
                    LayoutParamsUtil.setViewLayoutParams(obtainMainView(), i5, i4, i7, i6);
                }
                if (z) {
                    ((AdaFrameView) this).changeWebParentViewRect();
                }
            } else if (layoutParams != null && z) {
                layoutParams.height = layoutParams.height == -1 ? -1 : viewOptions.height;
                if (viewOptions.isStatusbarDodifyHeight && (obtainMainView instanceof com.dcloud.android.widget.AbsoluteLayout)) {
                    layoutParams.height = viewOptions.height + DeviceInfo.sStatusBarHeight;
                }
                if (layoutParams.width != -1) {
                    i = viewOptions.width;
                }
                layoutParams.width = i;
                if (!viewOptions.hasBackground()) {
                    ViewHelper.setX(obtainMainView, (float) viewOptions.left);
                    ViewHelper.setY(obtainMainView, (float) viewOptions.top);
                }
                if (z) {
                    ((AdaFrameView) this).changeWebParentViewRect();
                }
                obtainMainView.requestLayout();
                obtainMainView.postInvalidate();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void paint(Canvas canvas) {
    }

    public void resize() {
        onResize();
    }

    public void scrollBy(int i, int i2) {
        this.mViewImpl.scrollBy(i, i2);
    }

    public void scrollTo(int i, int i2) {
        this.mViewImpl.scrollTo(i, i2);
    }

    public void setAnimOptions(AnimOptions animOptions) {
        this.mAnimOptions = animOptions;
    }

    public void setAnimatorLinstener(Animator.AnimatorListener animatorListener) {
        this.mAnimatorListener = animatorListener;
    }

    public void setBgcolor(int i) {
        this.mViewImpl.setBackgroundColor(i);
    }

    public void setFrameOptions(ViewOptions viewOptions) {
        this.mViewOptions = viewOptions;
    }

    public void setFrameOptions_Animate(ViewOptions viewOptions) {
        this.mViewOptions_animate = viewOptions;
    }

    public void setFrameOptions_Birth(ViewOptions viewOptions) {
        this.mViewOptions_birth = viewOptions;
    }

    public void setMainView(View view) {
        this.mViewImpl = view;
    }

    public void setParentFrameItem(AdaContainerFrameItem adaContainerFrameItem) {
        this.mParentFrameItem = adaContainerFrameItem;
    }

    public void setPosition(int i) {
        this.mPosition = i;
    }

    public void setSlipping(boolean z) {
        this.isSlipping = z;
    }

    public void setVisibility(int i) {
        View view = this.mViewImpl;
        if (view != null && view.getVisibility() != i) {
            this.mViewImpl.setVisibility(i);
        }
    }

    public void startAnimator(int i) {
    }

    public void updateViewRect(AdaFrameItem adaFrameItem, int[] iArr, int[] iArr2) {
        updateViewRect(adaFrameItem, iArr, iArr2, new boolean[]{true, true, true, true}, new boolean[]{true, true, true, false});
    }

    /* access modifiers changed from: protected */
    public void useDefaultMainView() {
        setMainView(new DefaultView(this.mContextWrapper));
    }

    public void updateViewRect(AdaFrameItem adaFrameItem, int[] iArr, int[] iArr2, boolean[] zArr, boolean[] zArr2) {
        ViewOptions viewOptions = this.mViewOptions;
        int i = iArr[0];
        viewOptions.left = i;
        viewOptions.checkValueIsPercentage("left", i, iArr2[0], zArr[0], zArr2[0]);
        ViewOptions viewOptions2 = this.mViewOptions;
        int i2 = iArr[1];
        viewOptions2.top = i2;
        viewOptions2.checkValueIsPercentage("top", i2, iArr2[1], zArr[1], zArr2[1]);
        ViewOptions viewOptions3 = this.mViewOptions;
        int i3 = iArr[2];
        viewOptions3.width = i3;
        viewOptions3.checkValueIsPercentage("width", i3, iArr2[0], zArr[2], zArr2[2]);
        ViewOptions viewOptions4 = this.mViewOptions;
        int i4 = iArr[3];
        viewOptions4.height = i4;
        viewOptions4.checkValueIsPercentage("height", i4, iArr2[1], zArr[3], zArr2[3]);
        this.mViewOptions.setParentViewRect(adaFrameItem.mViewOptions);
    }
}
