package io.dcloud.common.core.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.dcloud.android.widget.AbsoluteLayout;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.DHInterface.IWebAppRootView;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameItem;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.adapter.ui.AdaWebViewParent;
import io.dcloud.common.adapter.ui.AdaWebview;
import io.dcloud.common.adapter.util.AnimOptions;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.ViewOptions;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.feature.internal.sdk.SDK;
import io.dcloud.nineoldandroids.animation.Animator;
import io.dcloud.nineoldandroids.view.ViewHelper;
import java.util.ArrayList;

class b extends AdaFrameView {
    public static int a = 0;
    public static int b = 0;
    public static int c = 1;
    public static int d = 2;
    public static int e = 3;
    boolean A = true;
    private boolean B = false;
    boolean f = false;
    ArrayList<b> g;
    ArrayList<b> h;
    boolean i = false;
    private boolean j = true;
    private boolean k = false;
    boolean l = false;
    boolean m = false;
    boolean n = false;
    IApp o = null;
    a p = null;
    AdaWebview q = null;
    AdaWebViewParent r = null;
    byte s = 2;
    private boolean t = true;
    ViewOptions u = null;
    /* access modifiers changed from: private */
    public Boolean v = Boolean.FALSE;
    private int w = b;
    boolean x = false;
    Animator.AnimatorListener y = new a();
    boolean z = false;

    class a implements Animator.AnimatorListener {

        /* renamed from: io.dcloud.common.core.ui.b$a$a  reason: collision with other inner class name */
        class C0018a implements Runnable {
            C0018a() {
            }

            public void run() {
                a aVar = a.this;
                if (b.this.mWindowMgr != null) {
                    aVar.a();
                }
            }
        }

        a() {
        }

        public void onAnimationCancel(Animator animator) {
            BaseInfo.sDoingAnimation = false;
        }

        public void onAnimationEnd(Animator animator) {
            Logger.e("DHFrameView", "---------------------onAnimationEnd");
            AnimOptions animOptions = b.this.getAnimOptions();
            Boolean unused = b.this.v = Boolean.FALSE;
            byte b = animOptions.mOption;
            if (b == 1) {
                b.this.setVisibility(AdaFrameItem.GONE);
            } else if (b == 3) {
                b.this.m();
            }
            BaseInfo.sDoingAnimation = false;
            if (b.this.obtainMainView() != null) {
                b.this.obtainMainView().post(new C0018a());
            }
        }

        public void onAnimationRepeat(Animator animator) {
        }

        public void onAnimationStart(Animator animator) {
            Logger.e("DHFrameView", "---------------------onAnimationStart");
            BaseInfo.sDoingAnimation = true;
            boolean unused = b.this.mAnimationStarted = true;
            if (b.this.getAnimOptions().mOption == 2) {
                b.a(b.this.obtainMainView(), b.this.mViewOptions.left, b.this.mViewOptions.top, "onAnimationStart");
            } else if (BaseInfo.isUniAppAppid(b.this.obtainApp())) {
                b.this.obtainWebView().setIWebViewFocusable(false);
            }
        }

        /* access modifiers changed from: private */
        public void a() {
            b bVar = b.this;
            if (bVar.mWindowMgr != null) {
                byte b = bVar.getAnimOptions().mOption;
                if (b == 0) {
                    View view = i.a;
                    if (view != null) {
                        view.clearAnimation();
                    }
                    b.this.o();
                } else if (b == 1) {
                    b.this.j();
                } else if (b == 2) {
                    b.this.p();
                } else if (b == 3) {
                    b.this.l();
                } else if (b == 4) {
                    View view2 = i.a;
                    if (view2 != null) {
                        view2.clearAnimation();
                    }
                    b.this.n();
                }
                b.this.mWindowMgr.processEvent(IMgr.MgrType.WindowMgr, 70, (Object) null);
            }
        }
    }

    b(Context context, l lVar, IApp iApp, a aVar, int i2, Object obj) {
        super(context, i2, obj);
        this.lastShowTime = System.currentTimeMillis();
        a++;
        Logger.i("dhframeview", "construction Count=" + a);
        this.mWindowMgr = lVar;
        this.o = iApp;
        this.p = aVar;
        aVar.d().add(this);
        this.j = iApp.isVerticalScreen();
        this.k = iApp.isFullScreen();
    }

    public void c(boolean z2) {
        this.p.l();
        if (!this.isChildOfFrameView) {
            this.p.b(this);
            onPushToStack(isAutoPop());
            a aVar = this.p;
            if (aVar != null) {
                if (!aVar.e().contains(this)) {
                    aVar.e(this);
                } else {
                    aVar.m();
                }
            }
        } else if (getParentFrameItem() != null) {
            this.p.h(this);
        }
        a(z2);
    }

    public int d() {
        return this.w;
    }

    public void dispose() {
        Logger.e("DHFrameView", "dispose");
        super.dispose();
        if (this.p != null) {
            int frameType = getFrameType();
            if (frameType == 2) {
                this.p.f = null;
            } else if (frameType == 4) {
                this.p.g = null;
            } else if (frameType == 5) {
                this.p.e = null;
            }
            this.p.e().remove(this);
            s();
        }
        this.mWindowMgr = null;
        this.o = null;
        this.mParentFrameItem = null;
        this.p = null;
        this.q = null;
        this.y = null;
    }

    /* access modifiers changed from: package-private */
    public boolean e() {
        ArrayList<b> arrayList = this.h;
        return arrayList != null && arrayList.size() > 0;
    }

    /* access modifiers changed from: package-private */
    public boolean f() {
        ArrayList<b> arrayList = this.g;
        return arrayList != null && arrayList.size() > 0;
    }

    public boolean g() {
        return this.B;
    }

    public AdaFrameItem getParent() {
        return this.p;
    }

    /* access modifiers changed from: package-private */
    public boolean h() {
        AnimOptions animOptions = getAnimOptions();
        if (animOptions == null || animOptions.mOption == 1) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public void i() {
        if (this.j != this.o.isVerticalScreen() || this.k != this.o.isFullScreen()) {
            StringBuilder sb = new StringBuilder();
            sb.append("onPushToStack frame ");
            sb.append(this.j ? "调整为横屏状态" : "调整为竖屏状态");
            sb.append(this);
            Logger.d(Logger.ANIMATION_TAG, sb.toString());
            resize();
            this.j = this.o.isVerticalScreen();
            this.k = this.o.isFullScreen();
        }
    }

    /* access modifiers changed from: protected */
    public void initMainView(Context context, int i2, Object obj) {
        if (i2 != 1) {
            setMainView(new AbsoluteLayout(context, this, this.o));
        }
    }

    /* access modifiers changed from: package-private */
    public void j() {
        setVisibility(AdaFrameItem.GONE);
        if (BaseInfo.isUniAppAppid(obtainApp())) {
            obtainWebView().setIWebViewFocusable(false);
        }
        k();
        r();
        b();
        clearAnimInfo();
        Logger.d(Logger.ANIMATION_TAG, "onCloseAnimationEnd;" + this);
    }

    public void k() {
        b(true);
    }

    /* access modifiers changed from: package-private */
    public void l() {
        if (BaseInfo.isUniAppAppid(obtainApp())) {
            obtainWebView().setIWebViewFocusable(false);
        }
        dispatchFrameViewEvents("hide", (Object) null);
        if (!this.mViewOptions.hasBackground() && !this.isChildOfFrameView && obtainMainView() != null) {
            ViewHelper.setX(obtainMainView(), (float) this.mViewOptions.left);
            ViewHelper.setY(obtainMainView(), (float) this.mViewOptions.top);
            ViewHelper.setScaleX(obtainMainView(), 1.0f);
            ViewHelper.setScaleY(obtainMainView(), 1.0f);
            if (!this.mViewOptions.hasTransparentValue()) {
                ViewHelper.setAlpha(obtainMainView(), 1.0f);
            }
        }
        ViewOptions viewOptions = this.mViewOptions_animate;
        if (viewOptions != null) {
            updateFrameRelViewRect(viewOptions);
            this.mViewOptions_animate = null;
        }
        k();
        this.m = false;
        b();
        clearAnimInfo();
        Logger.d(Logger.ANIMATION_TAG, "onHideAnimationEnd;" + toString());
    }

    /* access modifiers changed from: package-private */
    public void m() {
        setVisible(false, true);
    }

    /* access modifiers changed from: package-private */
    public void n() {
        if (BaseInfo.isUniAppAppid(obtainApp())) {
            obtainWebView().setIWebViewFocusable(true);
        }
        if (BaseInfo.isUniAppAppid(obtainApp()) && !obtainWebView().isUniWebView()) {
            obtainWebView().obtainWindowView().requestFocus();
        }
        dispatchFrameViewEvents(AbsoluteConst.EVENTS_SHOW_ANIMATION_END, (Object) null);
        ViewOptions viewOptions = this.mViewOptions_animate;
        if (viewOptions != null) {
            updateFrameRelViewRect(viewOptions);
            this.mViewOptions_animate = null;
        }
        View obtainMainView = obtainMainView();
        ViewOptions viewOptions2 = this.mViewOptions;
        a(obtainMainView, viewOptions2.left, viewOptions2.top, "onHideShowAnimationEnd");
        k();
        this.inStack = true;
        b();
        clearAnimInfo();
        Logger.d(Logger.ANIMATION_TAG, "onHideShowAnimationEnd;" + toString());
    }

    /* access modifiers changed from: package-private */
    public void o() {
        if (BaseInfo.isUniAppAppid(obtainApp())) {
            obtainWebView().setIWebViewFocusable(true);
        }
        if (BaseInfo.isUniAppAppid(obtainApp()) && !obtainWebView().isUniWebView()) {
            obtainWebView().obtainWindowView().requestFocus();
        }
        dispatchFrameViewEvents(AbsoluteConst.EVENTS_SHOW_ANIMATION_END, (Object) null);
        ViewOptions viewOptions = this.mViewOptions_animate;
        if (viewOptions != null) {
            updateFrameRelViewRect(viewOptions);
            this.mViewOptions_animate = null;
        }
        k();
        this.inStack = true;
        b();
        clearAnimInfo();
        Logger.d(Logger.ANIMATION_TAG, "onShowAnimationEnd;" + this);
        if (!SDK.isUniMPSDK() && !BaseInfo.isUniAppAppid(this.o)) {
            io.dcloud.e.c.a.f().a((Context) this.o.getActivity());
        }
    }

    public IApp obtainApp() {
        return this.o;
    }

    public String obtainPrePlusreadyJs() {
        l lVar = this.mWindowMgr;
        if (lVar == null) {
            return "";
        }
        return (String) lVar.processEvent(IMgr.MgrType.FeatureMgr, 2, new Object[]{this.o, this});
    }

    public IWebAppRootView obtainWebAppRootView() {
        return this.p;
    }

    public IWebview obtainWebView() {
        return this.q;
    }

    public AdaWebViewParent obtainWebviewParent() {
        return this.r;
    }

    public AbsMgr obtainWindowMgr() {
        return this.mWindowMgr;
    }

    public void onConfigurationChanged() {
        super.onConfigurationChanged();
        resize();
        this.j = this.o.isVerticalScreen();
        this.k = this.o.isFullScreen();
        Logger.d(Logger.Android_System_TAG, "onConfigurationChanged", this);
    }

    public void onDestroy() {
        super.onDestroy();
        a--;
        Logger.i("dhframeview", "onDestroy Count=" + a);
    }

    public boolean onDispose() {
        if (getParentFrameItem() != null && (getParentFrameItem() instanceof c)) {
            ((c) getParentFrameItem()).removeFrameView(this);
        }
        return super.onDispose();
    }

    public void onInit() {
        super.onInit();
    }

    public void onLoading() {
        super.onLoading();
    }

    public void onPopFromStack(boolean z2) {
        super.onPopFromStack(z2);
        IApp iApp = this.o;
        String str = "竖屏出栈";
        if (iApp != null) {
            this.j = iApp.isVerticalScreen();
            this.k = this.o.isFullScreen();
            StringBuilder sb = new StringBuilder();
            sb.append("onPopFromStack ");
            if (!this.j) {
                str = "横屏出栈";
            }
            sb.append(str);
            sb.append(this);
            Logger.d(Logger.ANIMATION_TAG, sb.toString());
            return;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("已经提前出栈了 ");
        if (!this.j) {
            str = "横屏出栈";
        }
        sb2.append(str);
        sb2.append(this);
        Logger.d(Logger.ANIMATION_TAG, sb2.toString());
    }

    public void onPreLoading() {
        super.onPreLoading();
        if (this.s == 0) {
            u();
        }
    }

    public void onPreShow(IFrameView iFrameView) {
        super.onPreShow(iFrameView);
    }

    /* access modifiers changed from: package-private */
    public void p() {
        AdaFrameItem obtainWebviewParent = obtainFrameOptions().hasBackground() ? obtainWebviewParent() : this;
        ViewOptions obtainFrameOptions_Animate = obtainWebviewParent.obtainFrameOptions_Animate();
        if (obtainFrameOptions_Animate != null) {
            updateFrameRelViewRect(obtainFrameOptions_Animate);
            obtainWebviewParent.setFrameOptions_Animate((ViewOptions) null);
        }
        View obtainMainView = obtainMainView();
        ViewOptions viewOptions = this.mViewOptions;
        a(obtainMainView, viewOptions.left, viewOptions.top, "onStyleChangedAnimationEnd");
        k();
        b();
        clearAnimInfo();
        Logger.d(Logger.ANIMATION_TAG, "onStyleChangedAnimationEnd;" + obtainWebviewParent.toString());
    }

    public void popFromViewStack() {
        if (!this.isChildOfFrameView && this.inStack) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(this);
            this.mWindowMgr.processEvent(IMgr.MgrType.WindowMgr, 27, arrayList);
        }
    }

    public void pushToViewStack() {
        if (!this.isChildOfFrameView && !this.inStack) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(this);
            this.mWindowMgr.processEvent(IMgr.MgrType.WindowMgr, 28, arrayList);
        }
    }

    public void q() {
        this.z = true;
        Logger.d(Logger.ANIMATION_TAG, "onWillDoAnimation " + this);
        a aVar = this.p;
        if (aVar != null) {
            aVar.n.a(this);
        }
        if (this.i) {
            ViewOptions obtainFrameOptions_Animate = obtainFrameOptions_Animate();
            ViewOptions viewOptions = this.mViewOptions;
            viewOptions.opacity = obtainFrameOptions_Animate.opacity;
            viewOptions.background = obtainFrameOptions_Animate.background;
            viewOptions.strBackground = obtainFrameOptions_Animate.strBackground;
            b(viewOptions, this, obtainWebviewParent(), (AdaFrameItem) obtainWebView());
        }
    }

    /* access modifiers changed from: package-private */
    public void r() {
        a aVar = this.p;
        if (aVar != null) {
            aVar.g(this);
        }
    }

    public void s() {
        a aVar = this.p;
        if (aVar != null) {
            aVar.d().remove(this);
        }
    }

    public void setNeedRender(boolean z2) {
        this.f = z2;
    }

    public void setVisible(boolean z2, boolean z3) {
        super.setVisible(z2, z3);
        if (z2 && (getParentFrameItem() instanceof c)) {
            ((c) getParentFrameItem()).d(this);
        }
    }

    public void startAnimator(int i2) {
        chkUseCaptureAnimation(false, hashCode(), this.mSnapshot != null);
        super.startAnimator(i2);
    }

    /* access modifiers changed from: package-private */
    public void t() {
        setAnimatorLinstener(this.y);
    }

    public String toString() {
        AdaWebview adaWebview = this.q;
        return adaWebview != null ? adaWebview.toString() : super.toString();
    }

    public void transition(byte b2) {
        if (this.s == b2 && b2 == 2) {
            u();
        }
    }

    /* access modifiers changed from: package-private */
    public void u() {
        t();
    }

    /* access modifiers changed from: package-private */
    public void b(ViewOptions viewOptions, AdaFrameItem adaFrameItem, AdaFrameItem adaFrameItem2, AdaFrameItem adaFrameItem3) {
        if (DeviceInfo.sDeviceSdkVer >= 11 && viewOptions.opacity != -1.0f) {
            adaFrameItem.obtainMainView().setAlpha(viewOptions.opacity);
        }
        if (viewOptions.webviewBGTransparent) {
            adaFrameItem3.setBgcolor(0);
        }
        if (viewOptions.isTabItem.booleanValue() && !PdrUtil.isEmpty(viewOptions.strTabBG)) {
            adaFrameItem.setBgcolor(PdrUtil.stringToColor(viewOptions.strTabBG));
        } else if (viewOptions.hasBackground()) {
            adaFrameItem.setBgcolor(viewOptions.background);
        } else if (viewOptions.isTransparent()) {
            adaFrameItem2.setBgcolor(0);
            adaFrameItem3.setBgcolor(0);
            adaFrameItem.setBgcolor(0);
        }
    }

    public void d(boolean z2) {
        this.B = z2;
    }

    public void a(int i2) {
        this.w = i2;
    }

    public void a(boolean z2) {
        boolean z3 = true;
        boolean z4 = obtainMainView().getVisibility() == 0;
        setVisible(true, false);
        q();
        this.v = Boolean.TRUE;
        this.x = false;
        int i2 = obtainApp().getInt(0);
        int i3 = obtainApp().getInt(1);
        if ((i2 != obtainFrameOptions().width || obtainFrameOptions().height + 1 < i3) && !(obtainFrameOptions().width == -1 && obtainFrameOptions().height == -1)) {
            z3 = false;
        }
        if ((z2 || BaseInfo.isDefaultAim) && !this.isChildOfFrameView && !z4) {
            if (z3 && PdrUtil.isEquals(getAnimOptions().mAnimType, AnimOptions.ANIM_POP_IN)) {
                i.a(this, 0);
            }
            if (z2) {
                t();
                startAnimator(0);
            } else {
                o();
            }
        } else if (!z2 || !PdrUtil.isEquals(getAnimOptions().mAnimType, AnimOptions.ANIM_FADE_IN)) {
            o();
        } else {
            t();
            startAnimator(0);
        }
        this.p.i(this);
    }

    public void b(boolean z2) {
        this.z = false;
        this.mAnimationStarted = true;
        Logger.d(Logger.ANIMATION_TAG, "onDoneAnimation " + this);
        a aVar = this.p;
        if (aVar != null) {
            this.g = null;
            if (aVar.n.a() >= 1) {
                if (this.A) {
                    this.p.b(this);
                }
                this.A = true;
                if (!this.isChildOfFrameView) {
                    if (f()) {
                        Logger.d(Logger.ANIMATION_TAG, "on_Done_Animation 动画完后存在窗口入栈；" + this);
                        l lVar = this.mWindowMgr;
                        if (lVar != null) {
                            lVar.processEvent(IMgr.MgrType.WindowMgr, 28, this.g);
                        }
                    }
                    if (e()) {
                        Logger.d(Logger.ANIMATION_TAG, "on_Done_Animation 动画完后存在窗口出栈；" + this);
                        a(this.h);
                    } else {
                        ICallBack iCallBack = this.p.a;
                        if (iCallBack != null) {
                            iCallBack.onCallBack(-1, (Object) null);
                        }
                    }
                }
            }
            this.p.n.b(this);
            this.h = null;
            if (z2) {
                this.p.j();
            }
        }
        if (obtainApp() != null && obtainApp().needRefreshApp()) {
            obtainApp().obtainMgrData(IMgr.MgrType.FeatureMgr, 1, new Object[]{obtainWebView(), "UI", "updateAppFrameViews", null});
        }
    }

    /* access modifiers changed from: protected */
    public void c() {
        View view;
        try {
            view = a(obtainMainView());
        } catch (Exception e2) {
            e2.printStackTrace();
            view = null;
        }
        if (view == null) {
            return;
        }
        if (view instanceof RecyclerView) {
            ((RecyclerView) view).scrollToPosition(0);
        } else {
            view.scrollTo(0, 0);
        }
    }

    /* access modifiers changed from: package-private */
    public void a(ViewOptions viewOptions, AdaFrameItem adaFrameItem, AdaFrameItem adaFrameItem2, AdaFrameItem adaFrameItem3) {
        this.u = viewOptions;
        ((IWebview) adaFrameItem3).setScrollIndicator(viewOptions.getScrollIndicator());
        b(viewOptions, adaFrameItem, adaFrameItem2, adaFrameItem3);
    }

    static void a(View view, int i2, int i3, String str) {
        if (DeviceInfo.sDeviceSdkVer <= 10) {
            view.layout(i2, i3, view.getRight() + i2, view.getBottom() + i3);
        }
    }

    private void a(ArrayList<b> arrayList) {
        Logger.d(Logger.ANIMATION_TAG, "removeFrameViewFromViewStack DoAnimation Frame=" + this + ";Will PopFrames=" + arrayList);
        this.mWindowMgr.processEvent(IMgr.MgrType.WindowMgr, 27, arrayList);
        ICallBack iCallBack = this.p.a;
        if (iCallBack != null) {
            iCallBack.onCallBack(-1, (Object) null);
        }
    }

    public void a(int i2, int i3) {
        ViewGroup.LayoutParams layoutParams = obtainMainView().getLayoutParams();
        if (layoutParams == null) {
            obtainMainView().setLayoutParams(new ViewGroup.LayoutParams(i2, i3));
            return;
        }
        layoutParams.width = i2;
        layoutParams.height = i3;
    }

    private View a(View view) throws Exception {
        if (!(view instanceof ViewGroup)) {
            return null;
        }
        if (view.canScrollVertically(-1)) {
            return view;
        }
        int i2 = 0;
        while (true) {
            ViewGroup viewGroup = (ViewGroup) view;
            if (i2 >= viewGroup.getChildCount()) {
                return null;
            }
            View a2 = a(viewGroup.getChildAt(i2));
            if (a2 != null) {
                return a2;
            }
            i2++;
        }
    }

    private void b() {
        this.g = null;
        this.h = null;
        this.i = false;
    }
}
