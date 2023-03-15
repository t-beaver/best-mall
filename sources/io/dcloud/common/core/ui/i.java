package io.dcloud.common.core.ui;

import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.ITypeofAble;
import io.dcloud.common.adapter.ui.DHImageView;
import io.dcloud.common.adapter.util.AnimOptions;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.ViewOptions;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.nineoldandroids.view.ViewHelper;
import java.util.Iterator;

public class i {
    public static View a = null;
    public static DHImageView b = null;
    private static boolean c = false;

    class a implements Animation.AnimationListener {
        final /* synthetic */ b a;

        a(b bVar) {
            this.a = bVar;
        }

        public void onAnimationEnd(Animation animation) {
            BaseInfo.sDoingAnimation = false;
            DHImageView dHImageView = i.b;
            if (dHImageView == null || dHImageView.isNativeView()) {
                DHImageView dHImageView2 = i.b;
                if (dHImageView2 != null && dHImageView2.isNativeView()) {
                    this.a.handleNativeViewByAction(i.b, 0);
                    i.b = null;
                    return;
                }
                return;
            }
            DHImageView dHImageView3 = i.b;
            if (dHImageView3 != null) {
                dHImageView3.setIntercept(false);
                i.b.clearAnimation();
                i.b.setVisibility(4);
                i.b.setImageBitmap((Bitmap) null);
                i.b = null;
            }
            View view = i.a;
            if (view != null) {
                view.clearAnimation();
                i.a = null;
            }
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
            DHImageView dHImageView = i.b;
            if (dHImageView != null) {
                dHImageView.setIntercept(true);
            }
            BaseInfo.sDoingAnimation = true;
        }
    }

    class b implements Animation.AnimationListener {
        final /* synthetic */ b a;

        class a implements Runnable {
            a() {
            }

            public void run() {
                DHImageView dHImageView = i.b;
                if (dHImageView != null) {
                    dHImageView.clearAnimation();
                    i.b.setVisibility(4);
                    if (i.b.isNativeView()) {
                        b.this.a.handleNativeViewByAction(i.b, 1);
                    }
                    i.b.removeNativeView();
                    i.b.setImageBitmap((Bitmap) null);
                    i.b.setTag(0);
                    i.b = null;
                }
                View view = i.a;
                if (view != null) {
                    view.clearAnimation();
                    i.a = null;
                }
            }
        }

        b(b bVar) {
            this.a = bVar;
        }

        public void onAnimationEnd(Animation animation) {
            DHImageView dHImageView = i.b;
            int i = 0;
            if (dHImageView != null) {
                dHImageView.setIntercept(false);
                i.b.setNativeAnimationRuning(false);
            }
            BaseInfo.sDoingAnimation = false;
            DHImageView dHImageView2 = i.b;
            if (dHImageView2 == null || !dHImageView2.isNativeView()) {
                i = 320;
            }
            View view = i.a;
            if (view == null) {
                view = this.a.obtainMainView();
            }
            view.postDelayed(new a(), (long) i);
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
            DHImageView dHImageView = i.b;
            if (dHImageView != null) {
                dHImageView.setIntercept(true);
                i.b.setNativeAnimationRuning(true);
            }
            BaseInfo.sDoingAnimation = true;
        }
    }

    class c implements Animation.AnimationListener {
        final /* synthetic */ b a;

        c(b bVar) {
            this.a = bVar;
        }

        public void onAnimationEnd(Animation animation) {
            i.c(this.a);
            View view = i.a;
            if (view != null) {
                ViewHelper.setX(view, (float) this.a.obtainFrameOptions().left);
                ViewHelper.setY(i.a, (float) this.a.obtainFrameOptions().top);
                i.a.clearAnimation();
                i.a = null;
            }
            BaseInfo.sDoingAnimation = false;
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
            BaseInfo.sDoingAnimation = true;
        }
    }

    class d implements Animation.AnimationListener {
        final /* synthetic */ b a;

        d(b bVar) {
            this.a = bVar;
        }

        public void onAnimationEnd(Animation animation) {
            i.c(this.a);
            View view = i.a;
            if (view != null) {
                ViewHelper.setX(view, (float) this.a.obtainFrameOptions().left);
                ViewHelper.setY(i.a, (float) this.a.obtainFrameOptions().top);
                i.a.clearAnimation();
                i.a = null;
            }
            BaseInfo.sDoingAnimation = false;
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
            BaseInfo.sDoingAnimation = true;
        }
    }

    public static boolean b(b bVar) {
        ViewGroup viewGroup = (ViewGroup) bVar.obtainMainView();
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (viewGroup.getChildAt(i) instanceof ITypeofAble) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    public static void c(b bVar) {
        if (bVar != null && bVar.p != null) {
            bVar.mWindowMgr.processEvent(IMgr.MgrType.WindowMgr, 70, (Object) null);
            bVar.p.obtainMainView().setBackgroundColor(-1);
            Iterator it = bVar.p.e().iterator();
            while (it.hasNext()) {
                b bVar2 = (b) it.next();
                if (bVar2.g()) {
                    bVar2.d(false);
                    bVar2.obtainMainView().setVisibility(0);
                }
            }
        }
    }

    public static void a(b bVar, int i) {
        String str = bVar.getAnimOptions().mAnimType;
        String str2 = bVar.getAnimOptions().mAnimType_close;
        b bVar2 = (b) bVar.p.findFrameViewB(bVar);
        if (bVar2 != null) {
            if (i == 1) {
                String closeAnimType = AnimOptions.getCloseAnimType(str2);
                if (bVar.mAccelerationType.equals("auto") && PdrUtil.isContains(closeAnimType, "slide")) {
                    return;
                }
                if (bVar.mAccelerationType.equals("auto") && !PdrUtil.isEquals(closeAnimType, AnimOptions.ANIM_POP_OUT) && !BaseInfo.isDefaultAim && bVar2.mSnapshot == null) {
                    return;
                }
                if (bVar.mAccelerationType.equals("none") && !PdrUtil.isEquals(closeAnimType, AnimOptions.ANIM_POP_OUT) && bVar2.mSnapshot == null) {
                    return;
                }
                if (!bVar.mAccelerationType.equals("none") && bVar2.mSnapshot == null) {
                    BaseInfo.sOpenedCount--;
                }
            } else if (bVar.mAccelerationType.equals("auto") && PdrUtil.isContains(str2, "slide")) {
                return;
            } else {
                if (bVar.mAccelerationType.equals("auto") && !PdrUtil.isEquals(str, AnimOptions.ANIM_POP_IN) && !BaseInfo.isDefaultAim && bVar2.mSnapshot == null) {
                    return;
                }
                if (bVar.mAccelerationType.equals("none") && !PdrUtil.isEquals(str2, AnimOptions.ANIM_POP_IN) && bVar2.mSnapshot == null) {
                    return;
                }
                if (!bVar.mAccelerationType.equals("none") && bVar2.mSnapshot == null) {
                    int i2 = BaseInfo.sOpenedCount + 1;
                    BaseInfo.sOpenedCount = i2;
                    c = i2 > 1;
                }
            }
            a = bVar2.obtainMainView();
            bVar2.p.a(bVar2, bVar);
            bVar2.chkUseCaptureAnimation(true, bVar2.hashCode(), bVar2.mSnapshot != null);
            if (!bVar2.mAnimationCapture || !BaseInfo.sAnimationCaptureB || b(bVar2)) {
                Logger.e("mabo", "B页面是否启用截图动画方案:false | " + bVar2.getAnimOptions().mAnimType);
                b(i, bVar2, bVar);
            } else {
                Logger.e("mabo", "B页面是否启用截图动画方案:true | " + bVar2.getAnimOptions().mAnimType);
                a(i, bVar2, bVar);
            }
            if (BaseInfo.sOpenedCount == 0) {
                c = false;
            }
        }
    }

    private static void b(int i, b bVar, b bVar2) {
        Animation animation;
        Animation translateAnimation;
        int i2 = bVar2.obtainApp().getInt(0);
        if (i == 0) {
            if (!PdrUtil.isEquals(bVar2.getAnimOptions().mAnimType, AnimOptions.ANIM_POP_IN)) {
                animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                animation.setDuration((long) bVar2.getAnimOptions().duration_show);
            } else if (Build.VERSION.SDK_INT >= 23) {
                animation = AnimationUtils.loadAnimation(bVar.getContext(), R.anim.dcloud_page_open_exit);
                a(bVar, bVar2);
            } else {
                animation = new TranslateAnimation((float) bVar.obtainFrameOptions().left, (float) ((-i2) / 4), 0.0f, 0.0f);
                animation.setDuration((long) bVar2.getAnimOptions().duration_show);
                animation.setInterpolator(new DecelerateInterpolator());
            }
            animation.setAnimationListener(new c(bVar));
        } else {
            if (!PdrUtil.isEquals(AnimOptions.getCloseAnimType(bVar2.getAnimOptions().mAnimType_close), AnimOptions.ANIM_POP_OUT)) {
                translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                translateAnimation.setDuration((long) bVar2.getAnimOptions().duration_close);
            } else if (Build.VERSION.SDK_INT >= 23) {
                translateAnimation = AnimationUtils.loadAnimation(bVar.getContext(), R.anim.dcloud_page_close_enter);
                a(bVar, bVar2);
            } else {
                translateAnimation = new TranslateAnimation((float) ((-i2) / 4), (float) bVar2.obtainFrameOptions().left, 0.0f, 0.0f);
                translateAnimation.setDuration((long) bVar2.getAnimOptions().duration_close);
                translateAnimation.setInterpolator(new DecelerateInterpolator());
            }
            animation.setAnimationListener(new d(bVar));
        }
        View view = b;
        if (view == null) {
            view = a;
        }
        view.startAnimation(animation);
        bVar.p.f(bVar);
    }

    private static void a(int i, b bVar, b bVar2) {
        TranslateAnimation translateAnimation;
        TranslateAnimation translateAnimation2;
        int i2 = bVar2.obtainApp().getInt(0);
        DHImageView a2 = bVar2.p.a(bVar, i, c);
        b = a2;
        if (a2 == null) {
            b(i, bVar, bVar2);
            return;
        }
        if (i == 0) {
            a2.setTag(Integer.valueOf(bVar.hashCode()));
            if (PdrUtil.isEquals(bVar2.getAnimOptions().mAnimType, AnimOptions.ANIM_POP_IN)) {
                translateAnimation = new TranslateAnimation((float) bVar.obtainFrameOptions().left, (float) ((-i2) / 4), 0.0f, 0.0f);
                translateAnimation.setFillAfter(true);
                translateAnimation.setDuration(300);
            } else {
                translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                translateAnimation.setDuration((long) bVar2.getAnimOptions().duration_show);
            }
            translateAnimation.setInterpolator(new DecelerateInterpolator());
            translateAnimation.setAnimationListener(new a(bVar));
        } else {
            if (PdrUtil.isEquals(AnimOptions.getCloseAnimType(bVar2.getAnimOptions().mAnimType_close), AnimOptions.ANIM_POP_OUT)) {
                translateAnimation2 = new TranslateAnimation((float) ((-i2) / 4), (float) bVar.obtainFrameOptions().left, 0.0f, 0.0f);
                translateAnimation2.setFillAfter(true);
                translateAnimation2.setDuration(360);
            } else {
                translateAnimation2 = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                translateAnimation2.setDuration((long) bVar2.getAnimOptions().duration_close);
            }
            translateAnimation.setInterpolator(new DecelerateInterpolator());
            translateAnimation.setAnimationListener(new b(bVar));
        }
        View view = b;
        if (view == null) {
            view = a;
        }
        view.startAnimation(translateAnimation);
        bVar.p.f(bVar);
    }

    private static void a(b bVar, b bVar2) {
        ViewOptions obtainFrameOptions;
        if (bVar != null && bVar.p != null) {
            if (bVar instanceof c) {
                c cVar = (c) bVar;
                if (!(cVar.v() == null || (obtainFrameOptions = cVar.v().obtainFrameOptions()) == null || PdrUtil.isEmpty(obtainFrameOptions.animationAlphaBackground))) {
                    bVar.p.obtainMainView().setBackgroundColor(PdrUtil.stringToColor(obtainFrameOptions.animationAlphaBackground));
                }
            } else if (bVar.obtainFrameOptions() != null && !PdrUtil.isEmpty(bVar.obtainFrameOptions().animationAlphaBackground)) {
                bVar.p.obtainMainView().setBackgroundColor(PdrUtil.stringToColor(bVar.obtainFrameOptions().animationAlphaBackground));
            }
            Iterator it = bVar.p.e().iterator();
            while (it.hasNext()) {
                b bVar3 = (b) it.next();
                if (!(bVar3 == bVar || bVar2 == bVar3 || bVar3.obtainMainView().getVisibility() != 0)) {
                    bVar3.d(true);
                    bVar3.obtainMainView().setVisibility(4);
                }
            }
        }
    }
}
