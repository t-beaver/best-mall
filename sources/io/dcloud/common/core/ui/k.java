package io.dcloud.common.core.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import com.dcloud.android.v4.view.ViewCompat;
import com.taobao.weex.common.Constants;
import io.dcloud.PdrR;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.IMgr;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.adapter.ui.DHImageView;
import io.dcloud.common.adapter.ui.FrameSwitchView;
import io.dcloud.common.adapter.util.ViewOptions;
import io.dcloud.common.core.ui.j;
import io.dcloud.common.util.BaseInfo;
import java.util.ArrayList;

public class k extends FrameLayout {
    /* access modifiers changed from: private */
    public int a;
    /* access modifiers changed from: private */
    public float b = 0.3f;
    private boolean c = true;
    /* access modifiers changed from: private */
    public View d;
    /* access modifiers changed from: private */
    public j e;
    /* access modifiers changed from: private */
    public float f;
    /* access modifiers changed from: private */
    public b g;
    /* access modifiers changed from: private */
    public Drawable h;
    private float i;
    private Rect j = new Rect();
    DHImageView k;
    DHImageView l;
    a m = null;
    /* access modifiers changed from: private */
    public b n;
    /* access modifiers changed from: private */
    public b o;
    private final int p = 1;
    private final int q = 2;
    /* access modifiers changed from: private */
    public int r = 1;
    /* access modifiers changed from: private */
    public int s;
    /* access modifiers changed from: private */
    public boolean t = false;

    public interface b {
        void a();

        void a(int i);

        void a(int i, float f);
    }

    public k(Context context, a aVar) {
        super(context);
        this.m = aVar;
        j jVar = new j(this, new c(), aVar);
        this.e = jVar;
        jVar.b(getResources().getDisplayMetrics().density * 400.0f);
        setEdgeTrackingEnabled(1);
        this.k = new DHImageView(context);
        this.l = new DHImageView(context);
        addView(this.k, new FrameLayout.LayoutParams(-1, -1));
        addView(this.l, new FrameLayout.LayoutParams(-1, -1));
        a(PdrR.DRAWEBL_SHADOW_LEFT, 1);
        this.l.setVisibility(8);
        this.k.setVisibility(8);
    }

    public void computeScroll() {
        this.i = 1.0f - this.f;
        if (this.e.a(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /* access modifiers changed from: protected */
    public boolean drawChild(Canvas canvas, View view, long j2) {
        boolean z = view == this.d;
        boolean drawChild = super.drawChild(canvas, view, j2);
        if (this.i > 0.0f && z && this.e.c() != 0) {
            a(canvas, view);
        }
        return drawChild;
    }

    public DHImageView getLeftImageView() {
        return this.k;
    }

    public DHImageView getRightImageView() {
        return this.l;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!this.c) {
            return false;
        }
        try {
            return this.e.c(motionEvent);
        } catch (Exception unused) {
            return false;
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.c) {
            return false;
        }
        b bVar = this.n;
        if (bVar == null || bVar.obtainMainView() == null) {
            return true;
        }
        try {
            this.e.a(motionEvent);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public void requestLayout() {
        j jVar = this.e;
        if (jVar == null) {
            super.requestLayout();
        } else if (jVar.c() == 0) {
            super.requestLayout();
        }
    }

    public void setEdgeSize(int i2) {
        this.e.e(i2);
    }

    public void setEdgeTrackingEnabled(int i2) {
        this.a = i2;
        this.e.f(i2);
    }

    public void setEnableGesture(boolean z) {
        this.c = z;
    }

    public void setScrollThresHold(float f2) {
        if (f2 >= 1.0f || f2 <= 0.0f) {
            throw new IllegalArgumentException("Threshold value should be between 0 and 1.0");
        }
        this.b = f2;
    }

    public void setSwipeListener(b bVar) {
        this.g = bVar;
    }

    public void c() {
        DHImageView dHImageView = this.k;
        if (dHImageView != null && dHImageView.getParent() == null) {
            addView(this.k, new FrameLayout.LayoutParams(-1, -1));
        }
        DHImageView dHImageView2 = this.l;
        if (dHImageView2 != null && dHImageView2.getParent() == null) {
            addView(this.l, new FrameLayout.LayoutParams(-1, -1));
        }
    }

    /* access modifiers changed from: private */
    public void b() {
        b bVar = this.o;
        if (bVar != null) {
            bVar.obtainMainView().setTranslationX(0.0f);
        }
        b bVar2 = this.n;
        if (bVar2 != null) {
            bVar2.obtainMainView().setLeft(0);
            this.n.obtainMainView().setTranslationX(0.0f);
        }
    }

    public void a(Drawable drawable, int i2) {
        if ((i2 & 1) != 0) {
            this.h = drawable;
        }
        invalidate();
    }

    public void a(int i2, int i3) {
        a(getResources().getDrawable(i2), i3);
    }

    private void a(Canvas canvas, View view) {
        Rect rect = this.j;
        view.getHitRect(rect);
        if ((this.a & 1) != 0) {
            Drawable drawable = this.h;
            double d2 = (double) rect.left;
            double intrinsicWidth = (double) drawable.getIntrinsicWidth();
            Double.isNaN(intrinsicWidth);
            Double.isNaN(d2);
            drawable.setBounds((int) (d2 - (intrinsicWidth * 0.6d)), rect.top, rect.left, rect.bottom);
            this.h.setAlpha((int) (this.i * 190.0f));
            this.h.draw(canvas);
        }
    }

    /* access modifiers changed from: private */
    public void a(b bVar) {
        if (bVar != null) {
            View obtainMainView = bVar.obtainMainView();
            double d2 = (double) this.f;
            Double.isNaN(d2);
            double width = (double) obtainMainView.getWidth();
            Double.isNaN(width);
            float f2 = (float) ((d2 - 0.95d) * 0.4210526315789474d * width);
            if (f2 > 0.0f) {
                f2 = 0.0f;
            }
            obtainMainView.setTranslationX(f2);
        }
    }

    private class c extends j.c {
        private boolean a;

        private c() {
        }

        public int a(View view) {
            return 1;
        }

        public void a(View view, int i, int i2, int i3, int i4) {
            super.a(view, i, i2, i3, i4);
            if (k.this.r != 1) {
                if ((k.this.s & 1) != 0) {
                    k kVar = k.this;
                    float unused = kVar.f = Math.abs(((float) i) / ((float) (kVar.d.getWidth() + k.this.h.getIntrinsicWidth())));
                }
                k.this.invalidate();
                if (k.this.f < k.this.b && !this.a) {
                    this.a = true;
                }
                if (k.this.g != null && k.this.e.c() == 1 && k.this.f >= k.this.b && this.a) {
                    this.a = false;
                    k.this.g.a();
                }
                k kVar2 = k.this;
                kVar2.a(kVar2.o);
            }
        }

        public int b(View view) {
            return 1;
        }

        public boolean b(View view, int i) {
            View unused = k.this.d = view;
            if (k.this.e.d(k.this.a, i)) {
                if (k.this.e.d(1, i)) {
                    int unused2 = k.this.s = 1;
                }
                if (k.this.g != null) {
                    k.this.g.a(k.this.s);
                }
                this.a = true;
            }
            return true;
        }

        public void c(int i) {
            super.c(i);
            if (k.this.g != null) {
                k.this.g.a(i, k.this.f);
            }
            if (i == 0) {
                k kVar = k.this;
                kVar.a(kVar.n, "end", Boolean.valueOf(k.this.f >= 1.0f));
                k.this.requestLayout();
                k.this.b();
                if (k.this.f >= 1.0f) {
                    if (k.this.o != null) {
                        k.this.o.setSlipping(false);
                    }
                    if (k.this.n != null) {
                        k.this.n.setSlipping(false);
                    }
                    k.this.a();
                } else if (k.this.r == 1 && k.this.f == 0.0f && k.this.n != null && k.this.n.obtainWebView().canGoBack()) {
                    k.this.n.obtainWebView().goBackOrForward(-1);
                }
                b unused = k.this.n = null;
                b unused2 = k.this.o = null;
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:5:0x0018, code lost:
            r3 = (r3 > 0.0f ? 1 : (r3 == 0.0f ? 0 : -1));
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void a(android.view.View r2, float r3, float r4) {
            /*
                r1 = this;
                io.dcloud.common.core.ui.k r4 = io.dcloud.common.core.ui.k.this
                int r4 = r4.r
                r0 = 1
                if (r4 != r0) goto L_0x000a
                return
            L_0x000a:
                int r2 = r2.getWidth()
                io.dcloud.common.core.ui.k r4 = io.dcloud.common.core.ui.k.this
                int r4 = r4.s
                r4 = r4 & r0
                r0 = 0
                if (r4 == 0) goto L_0x003d
                r4 = 0
                int r3 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1))
                if (r3 > 0) goto L_0x002f
                if (r3 != 0) goto L_0x003d
                io.dcloud.common.core.ui.k r3 = io.dcloud.common.core.ui.k.this
                float r3 = r3.f
                io.dcloud.common.core.ui.k r4 = io.dcloud.common.core.ui.k.this
                float r4 = r4.b
                int r3 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1))
                if (r3 <= 0) goto L_0x003d
            L_0x002f:
                io.dcloud.common.core.ui.k r3 = io.dcloud.common.core.ui.k.this
                android.graphics.drawable.Drawable r3 = r3.h
                int r3 = r3.getIntrinsicWidth()
                int r2 = r2 + r3
                int r2 = r2 + 10
                goto L_0x003e
            L_0x003d:
                r2 = 0
            L_0x003e:
                io.dcloud.common.core.ui.k r3 = io.dcloud.common.core.ui.k.this
                io.dcloud.common.core.ui.j r3 = r3.e
                r3.e(r2, r0)
                io.dcloud.common.core.ui.k r2 = io.dcloud.common.core.ui.k.this
                r2.invalidate()
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.core.ui.k.c.a(android.view.View, float, float):void");
        }

        public int a(View view, int i, int i2) {
            if (k.this.r == 1) {
                return 0;
            }
            return Math.min(view.getWidth(), Math.max(i, 0));
        }

        public boolean a(b bVar) {
            int i;
            int i2;
            if (bVar == null || 2 == bVar.getFrameType() || BaseInfo.sDoingAnimation || !k.this.t) {
                return false;
            }
            Rect rect = new Rect();
            bVar.obtainWebView().obtainWindowView().getGlobalVisibleRect(rect);
            ViewOptions obtainFrameOptions = bVar.obtainFrameOptions();
            b unused = k.this.n = bVar;
            int height = k.this.getHeight();
            if (!(bVar.obtainApp() == null || bVar.obtainApp().obtainStatusBarMgr() == null || !bVar.obtainApp().obtainStatusBarMgr().isHandledWhiteScreen)) {
                height--;
            }
            if (rect.left != 0 || (((i = obtainFrameOptions.width) != -1 && i < k.this.getWidth()) || (((i2 = obtainFrameOptions.height) != -1 && i2 < height) || bVar.obtainFrameOptions().popGesture.equals("none")))) {
                return false;
            }
            if ((bVar.obtainFrameOptions().historyBack.equals("all") || bVar.obtainFrameOptions().historyBack.equals("popGesture")) && bVar.obtainWebView() != null && bVar.obtainWebView().canGoBack()) {
                int unused2 = k.this.r = 1;
                return true;
            }
            k.this.c();
            ArrayList arrayList = new ArrayList();
            k.this.m.a((IFrameView) bVar, (ArrayList<b>) arrayList);
            bVar.mWindowMgr.processEvent(IMgr.MgrType.WindowMgr, 28, arrayList);
            b unused3 = k.this.o = null;
            if (arrayList.size() == 1) {
                b unused4 = k.this.o = (b) arrayList.get(0);
            }
            if (arrayList.size() > 0) {
                if (k.this.o != null) {
                    k.this.o.setSlipping(true);
                    k.this.o.obtainMainView().setVisibility(0);
                    k.this.o.obtainMainView().bringToFront();
                }
                bVar.setSlipping(true);
                bVar.obtainMainView().setVisibility(0);
                bVar.obtainMainView().bringToFront();
                int unused5 = k.this.r = 2;
                FrameSwitchView instance = FrameSwitchView.getInstance();
                if (instance != null) {
                    instance.endRefreshView();
                }
                k.this.a(bVar, "start", Constants.Name.UNDEFINED);
                return true;
            }
            return false;
        }
    }

    /* access modifiers changed from: private */
    public void a() {
        b bVar = this.n;
        if (bVar != null) {
            String str = bVar.obtainFrameOptions().popGesture;
            if (str.equals("hide")) {
                this.n.dispatchFrameViewEvents(AbsoluteConst.EVENTS_WEBAPP_SLIDE_HIDE, (Object) null);
                b bVar2 = this.n;
                bVar2.mWindowMgr.c(bVar2);
            } else if (str.equals(AbsoluteConst.EVENTS_CLOSE)) {
                this.n.dispatchFrameViewEvents(AbsoluteConst.EVENTS_WEBAPP_SLIDE_CLOSE, (Object) null);
                b bVar3 = this.n;
                bVar3.mWindowMgr.a(bVar3);
            }
        }
    }

    public void a(AdaFrameView adaFrameView, String str, Object obj) {
        adaFrameView.dispatchFrameViewEvents("popGesture", new Object[]{str, obj, this.n});
    }
}
