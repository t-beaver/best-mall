package io.dcloud.common.core.ui;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import com.alibaba.android.bindingx.core.internal.BindingXConstants;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.ui.view.gesture.WXGesture;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.INativeView;
import io.dcloud.common.adapter.util.DragBean;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.nineoldandroids.animation.Animator;
import io.dcloud.nineoldandroids.animation.ValueAnimator;
import io.dcloud.nineoldandroids.view.ViewHelper;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.regex.Pattern;
import org.json.JSONObject;

public class g {
    /* access modifiers changed from: private */
    public static boolean a = false;
    private int A = Integer.MAX_VALUE;
    /* access modifiers changed from: private */
    public String B;
    private int C = Integer.MAX_VALUE;
    /* access modifiers changed from: private */
    public String D;
    /* access modifiers changed from: private */
    public String E;
    private boolean F = false;
    private boolean G = true;
    private boolean H = true;
    private boolean I = true;
    private int J = -1;
    private IFrameView K;
    private String L;
    /* access modifiers changed from: private */
    public String M;
    private String N;
    private int O;
    private boolean P;
    private float Q;
    /* access modifiers changed from: private */
    public int R;
    private int S;
    /* access modifiers changed from: private */
    public boolean T = false;
    private boolean U = false;
    private boolean V = false;
    /* access modifiers changed from: private */
    public int W = Integer.MAX_VALUE;
    /* access modifiers changed from: private */
    public int X = 0;
    private DisplayMetrics Y;
    private float Z = 20.0f;
    private float a0;
    private boolean b = false;
    private float b0;
    private boolean c = false;
    private float c0;
    private boolean d = false;
    private float d0;
    private boolean e = false;
    private float e0;
    private boolean f = false;
    private float f0;
    private VelocityTracker g;
    boolean g0 = true;
    private boolean h = true;
    boolean h0;
    private int i;
    private String i0 = "^([1-9]|[1-9]\\d|100)$";
    private float j;
    private Pattern j0;
    private float k;
    private float l;
    private int m;
    /* access modifiers changed from: private */
    public int n = 0;
    /* access modifiers changed from: private */
    public int o = 0;
    /* access modifiers changed from: private */
    public int p = 0;
    /* access modifiers changed from: private */
    public int q = 0;
    /* access modifiers changed from: private */
    public b r;
    /* access modifiers changed from: private */
    public b s;
    /* access modifiers changed from: private */
    public View t = null;
    /* access modifiers changed from: private */
    public View u;
    private boolean v = false;
    private int w = Integer.MAX_VALUE;
    /* access modifiers changed from: private */
    public String x;
    private int y = Integer.MAX_VALUE;
    /* access modifiers changed from: private */
    public String z;

    class a implements ViewTreeObserver.OnGlobalLayoutListener {
        a() {
        }

        public void onGlobalLayout() {
            int i;
            if (Build.VERSION.SDK_INT >= 16) {
                g.this.u.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
            g gVar = g.this;
            boolean z = true;
            if (gVar.d(gVar.M)) {
                i = g.this.q;
            } else {
                g gVar2 = g.this;
                int b = gVar2.b(gVar2.u) / 2;
                Rect rect = new Rect();
                g.this.u.getGlobalVisibleRect(rect);
                if (rect.right - rect.left >= b) {
                    i = g.this.q;
                } else {
                    i = g.this.p;
                    z = false;
                }
            }
            g gVar3 = g.this;
            int c = gVar3.a(gVar3.u);
            g gVar4 = g.this;
            ValueAnimator a2 = gVar4.a(gVar4.u, c, i, z);
            if (a2 != null) {
                a2.start();
            }
        }
    }

    class b implements ViewTreeObserver.OnGlobalLayoutListener {
        b() {
        }

        /* JADX WARNING: Removed duplicated region for block: B:54:0x016c  */
        /* JADX WARNING: Removed duplicated region for block: B:65:0x01b1  */
        /* JADX WARNING: Removed duplicated region for block: B:66:0x01c1  */
        /* JADX WARNING: Removed duplicated region for block: B:69:0x01da  */
        /* JADX WARNING: Removed duplicated region for block: B:71:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onGlobalLayout() {
            /*
                r10 = this;
                int r0 = android.os.Build.VERSION.SDK_INT
                r1 = 16
                if (r0 < r1) goto L_0x0013
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r0 = r0.t
                android.view.ViewTreeObserver r0 = r0.getViewTreeObserver()
                r0.removeOnGlobalLayoutListener(r10)
            L_0x0013:
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                java.lang.String r1 = r0.M
                boolean r0 = r0.d((java.lang.String) r1)
                java.lang.String r1 = "left"
                java.lang.String r2 = "right"
                r3 = 0
                r4 = 1
                if (r0 == 0) goto L_0x002e
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r0 = r0.o
            L_0x002b:
                r5 = 1
                goto L_0x0164
            L_0x002e:
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r0 = r0.u
                if (r0 == 0) goto L_0x00ea
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r5 = r0.u
                int r0 = r0.b((android.view.View) r5)
                int r0 = r0 / 2
                android.graphics.Rect r5 = new android.graphics.Rect
                r5.<init>()
                io.dcloud.common.core.ui.g r6 = io.dcloud.common.core.ui.g.this
                android.view.View r6 = r6.u
                r6.getGlobalVisibleRect(r5)
                android.graphics.Rect r6 = new android.graphics.Rect
                r6.<init>()
                io.dcloud.common.core.ui.g r7 = io.dcloud.common.core.ui.g.this
                android.view.View r7 = r7.t
                r7.getGlobalVisibleRect(r6)
                int r7 = r5.left
                if (r7 != 0) goto L_0x009d
                int r5 = r6.left
                int r5 = r5 - r7
                io.dcloud.common.core.ui.g r6 = io.dcloud.common.core.ui.g.this
                java.lang.String r6 = r6.M
                boolean r6 = r2.equals(r6)
                if (r6 == 0) goto L_0x0081
                if (r5 < r0) goto L_0x007a
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r0 = r0.o
                goto L_0x002b
            L_0x007a:
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r0 = r0.n
                goto L_0x00e7
            L_0x0081:
                io.dcloud.common.core.ui.g r6 = io.dcloud.common.core.ui.g.this
                java.lang.String r6 = r6.M
                boolean r6 = r1.equals(r6)
                if (r6 == 0) goto L_0x00e6
                if (r5 > r0) goto L_0x0096
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r0 = r0.o
                goto L_0x002b
            L_0x0096:
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r0 = r0.n
                goto L_0x00e7
            L_0x009d:
                int r7 = r5.right
                io.dcloud.common.core.ui.g r8 = io.dcloud.common.core.ui.g.this
                int r8 = r8.R
                if (r7 != r8) goto L_0x00e6
                int r5 = r5.right
                int r6 = r6.right
                int r5 = r5 - r6
                io.dcloud.common.core.ui.g r6 = io.dcloud.common.core.ui.g.this
                java.lang.String r6 = r6.M
                boolean r6 = r2.equals(r6)
                if (r6 == 0) goto L_0x00c9
                if (r5 > r0) goto L_0x00c2
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r0 = r0.o
                goto L_0x002b
            L_0x00c2:
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r0 = r0.n
                goto L_0x00e7
            L_0x00c9:
                io.dcloud.common.core.ui.g r6 = io.dcloud.common.core.ui.g.this
                java.lang.String r6 = r6.M
                boolean r6 = r1.equals(r6)
                if (r6 == 0) goto L_0x00e6
                if (r5 < r0) goto L_0x00df
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r0 = r0.o
                goto L_0x002b
            L_0x00df:
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r0 = r0.n
                goto L_0x00e7
            L_0x00e6:
                r0 = 0
            L_0x00e7:
                r5 = 0
                goto L_0x0164
            L_0x00ea:
                android.graphics.Rect r0 = new android.graphics.Rect
                r0.<init>()
                io.dcloud.common.core.ui.g r5 = io.dcloud.common.core.ui.g.this
                android.view.View r5 = r5.t
                r5.getGlobalVisibleRect(r0)
                int r5 = r0.right
                int r0 = r0.left
                int r5 = r5 - r0
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r0 = r0.t
                int r0 = r0.getWidth()
                int r0 = r0 / 2
                io.dcloud.common.core.ui.g r6 = io.dcloud.common.core.ui.g.this
                java.lang.String r6 = r6.M
                boolean r6 = r2.equals(r6)
                if (r6 == 0) goto L_0x012e
                io.dcloud.common.core.ui.g r6 = io.dcloud.common.core.ui.g.this
                int r6 = r6.n
                if (r6 >= 0) goto L_0x012e
                if (r5 < r0) goto L_0x0127
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r0 = r0.o
                goto L_0x002b
            L_0x0127:
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r0 = r0.n
                goto L_0x00e7
            L_0x012e:
                io.dcloud.common.core.ui.g r6 = io.dcloud.common.core.ui.g.this
                java.lang.String r6 = r6.M
                boolean r6 = r1.equals(r6)
                if (r6 == 0) goto L_0x0153
                io.dcloud.common.core.ui.g r6 = io.dcloud.common.core.ui.g.this
                int r6 = r6.o
                if (r6 < 0) goto L_0x0153
                if (r5 < r0) goto L_0x014c
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r0 = r0.o
                goto L_0x002b
            L_0x014c:
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r0 = r0.n
                goto L_0x00e7
            L_0x0153:
                if (r5 > r0) goto L_0x015d
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r0 = r0.o
                goto L_0x002b
            L_0x015d:
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r0 = r0.n
                goto L_0x00e7
            L_0x0164:
                io.dcloud.common.core.ui.g r6 = io.dcloud.common.core.ui.g.this
                android.view.View r6 = r6.u
                if (r6 == 0) goto L_0x01a2
                android.graphics.Rect r6 = new android.graphics.Rect
                r6.<init>()
                io.dcloud.common.core.ui.g r7 = io.dcloud.common.core.ui.g.this
                android.view.View r7 = r7.u
                r7.getGlobalVisibleRect(r6)
                int r7 = r6.left
                if (r7 != 0) goto L_0x018b
                io.dcloud.common.core.ui.g r2 = io.dcloud.common.core.ui.g.this
                java.lang.String r2 = r2.M
                boolean r1 = r1.equals(r2)
                if (r1 == 0) goto L_0x01a2
                goto L_0x01a3
            L_0x018b:
                int r1 = r6.right
                io.dcloud.common.core.ui.g r6 = io.dcloud.common.core.ui.g.this
                int r6 = r6.R
                if (r1 != r6) goto L_0x01a2
                io.dcloud.common.core.ui.g r1 = io.dcloud.common.core.ui.g.this
                java.lang.String r1 = r1.M
                boolean r1 = r2.equals(r1)
                if (r1 == 0) goto L_0x01a2
                goto L_0x01a3
            L_0x01a2:
                r4 = 0
            L_0x01a3:
                io.dcloud.common.core.ui.g r1 = io.dcloud.common.core.ui.g.this
                java.lang.String r1 = r1.E
                java.lang.String r2 = "bounce"
                boolean r1 = r2.equalsIgnoreCase(r1)
                if (r1 == 0) goto L_0x01c1
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r0 = r0.n
                io.dcloud.common.core.ui.g r1 = io.dcloud.common.core.ui.g.this
                r2 = 0
                java.lang.String unused = r1.E = r2
                r7 = r0
                r8 = 0
                r9 = 0
                goto L_0x01c4
            L_0x01c1:
                r7 = r0
                r9 = r4
                r8 = r5
            L_0x01c4:
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r0.t
                int r6 = r0.a((android.view.View) r1)
                io.dcloud.common.core.ui.g r4 = io.dcloud.common.core.ui.g.this
                android.view.View r5 = r4.t
                io.dcloud.nineoldandroids.animation.ValueAnimator r0 = r4.a((android.view.View) r5, (int) r6, (int) r7, (boolean) r8, (boolean) r9)
                if (r0 == 0) goto L_0x01dd
                r0.start()
            L_0x01dd:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.core.ui.g.b.onGlobalLayout():void");
        }
    }

    class c implements ViewTreeObserver.OnGlobalLayoutListener {
        c() {
        }

        /* JADX WARNING: Removed duplicated region for block: B:56:0x0208  */
        /* JADX WARNING: Removed duplicated region for block: B:59:0x021d  */
        /* JADX WARNING: Removed duplicated region for block: B:71:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onGlobalLayout() {
            /*
                r7 = this;
                int r0 = android.os.Build.VERSION.SDK_INT
                r1 = 16
                if (r0 < r1) goto L_0x0013
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r0 = r0.t
                android.view.ViewTreeObserver r0 = r0.getViewTreeObserver()
                r0.removeOnGlobalLayoutListener(r7)
            L_0x0013:
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                java.lang.String r1 = r0.M
                boolean r0 = r0.d((java.lang.String) r1)
                r1 = 0
                java.lang.String r2 = "bounce"
                r3 = 1
                r4 = 0
                if (r0 == 0) goto L_0x0052
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r0 = r0.o
                io.dcloud.common.core.ui.g r5 = io.dcloud.common.core.ui.g.this
                int r5 = r5.q
                io.dcloud.common.core.ui.g r6 = io.dcloud.common.core.ui.g.this
                java.lang.String r6 = r6.E
                boolean r2 = r2.equalsIgnoreCase(r6)
                if (r2 == 0) goto L_0x004e
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r0 = r0.n
                io.dcloud.common.core.ui.g r2 = io.dcloud.common.core.ui.g.this
                int r2 = r2.p
                io.dcloud.common.core.ui.g r3 = io.dcloud.common.core.ui.g.this
                java.lang.String unused = r3.E = r1
                goto L_0x006f
            L_0x004e:
                r4 = r0
                r0 = r5
                goto L_0x01f2
            L_0x0052:
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                java.lang.String r0 = r0.E
                boolean r0 = r2.equalsIgnoreCase(r0)
                if (r0 == 0) goto L_0x0074
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r0 = r0.n
                io.dcloud.common.core.ui.g r2 = io.dcloud.common.core.ui.g.this
                int r2 = r2.p
                io.dcloud.common.core.ui.g r3 = io.dcloud.common.core.ui.g.this
                java.lang.String unused = r3.E = r1
            L_0x006f:
                r4 = r0
                r0 = r2
            L_0x0071:
                r3 = 0
                goto L_0x01f2
            L_0x0074:
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r0 = r0.R
                io.dcloud.common.core.ui.g r1 = io.dcloud.common.core.ui.g.this
                android.view.View r2 = r1.u
                int r1 = r1.a((android.view.View) r2)
                io.dcloud.common.core.ui.g r2 = io.dcloud.common.core.ui.g.this
                android.view.View r5 = r2.u
                int r2 = r2.b((android.view.View) r5)
                int r2 = r2 + r1
                io.dcloud.common.core.ui.g r5 = io.dcloud.common.core.ui.g.this
                android.view.View r6 = r5.t
                int unused = r5.a((android.view.View) r6)
                io.dcloud.common.core.ui.g r5 = io.dcloud.common.core.ui.g.this
                android.view.View r5 = r5.t
                r5.getWidth()
                io.dcloud.common.core.ui.g r5 = io.dcloud.common.core.ui.g.this
                android.view.View r5 = r5.t
                int r5 = r5.getWidth()
                io.dcloud.common.core.ui.g r6 = io.dcloud.common.core.ui.g.this
                int r6 = r6.R
                if (r5 >= r6) goto L_0x0120
                android.graphics.Rect r0 = new android.graphics.Rect
                r0.<init>()
                io.dcloud.common.core.ui.g r1 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r1.t
                r1.getGlobalVisibleRect(r0)
                int r1 = r0.right
                int r2 = r0.left
                int r1 = r1 - r2
                io.dcloud.common.core.ui.g r2 = io.dcloud.common.core.ui.g.this
                android.view.View r2 = r2.t
                int r2 = r2.getWidth()
                int r2 = r2 / 2
                int r5 = r0.left
                if (r5 != 0) goto L_0x00f6
                if (r1 > r2) goto L_0x00e6
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r4 = r0.o
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r0 = r0.q
                goto L_0x01f2
            L_0x00e6:
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r0 = r0.n
                io.dcloud.common.core.ui.g r1 = io.dcloud.common.core.ui.g.this
                int r1 = r1.p
            L_0x00f2:
                r4 = r0
                r0 = r1
                goto L_0x0071
            L_0x00f6:
                io.dcloud.common.core.ui.g r5 = io.dcloud.common.core.ui.g.this
                int r5 = r5.R
                int r0 = r0.right
                if (r5 != r0) goto L_0x011d
                if (r1 > r2) goto L_0x0110
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r4 = r0.o
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r0 = r0.q
                goto L_0x01f2
            L_0x0110:
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r0 = r0.n
                io.dcloud.common.core.ui.g r1 = io.dcloud.common.core.ui.g.this
                int r1 = r1.p
                goto L_0x00f2
            L_0x011d:
                r0 = 0
                goto L_0x0071
            L_0x0120:
                if (r1 > 0) goto L_0x01b6
                if (r2 <= 0) goto L_0x01b6
                int r2 = r2 - r4
                if (r2 <= 0) goto L_0x015c
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r0.u
                int r0 = r0.b((android.view.View) r1)
                if (r2 >= r0) goto L_0x015c
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r0.u
                int r0 = r0.b((android.view.View) r1)
                int r0 = r0 / 2
                if (r2 < r0) goto L_0x014f
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r4 = r0.o
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r0 = r0.q
                goto L_0x01f2
            L_0x014f:
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r0 = r0.n
                io.dcloud.common.core.ui.g r1 = io.dcloud.common.core.ui.g.this
                int r1 = r1.p
                goto L_0x00f2
            L_0x015c:
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r0.u
                int r0 = r0.b((android.view.View) r1)
                if (r2 != r0) goto L_0x018f
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                r0.b((boolean) r3)
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r0.t
                io.dcloud.common.core.ui.g r2 = io.dcloud.common.core.ui.g.this
                int r2 = r2.o
                r0.a((android.view.View) r1, (int) r2)
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r0.u
                io.dcloud.common.core.ui.g r2 = io.dcloud.common.core.ui.g.this
                int r2 = r2.q
                r0.a((android.view.View) r1, (int) r2)
                boolean unused = io.dcloud.common.core.ui.g.a = r4
                return
            L_0x018f:
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                r0.b((boolean) r4)
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r0.t
                io.dcloud.common.core.ui.g r2 = io.dcloud.common.core.ui.g.this
                int r2 = r2.n
                r0.a((android.view.View) r1, (int) r2)
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r0.u
                io.dcloud.common.core.ui.g r2 = io.dcloud.common.core.ui.g.this
                int r2 = r2.p
                r0.a((android.view.View) r1, (int) r2)
                boolean unused = io.dcloud.common.core.ui.g.a = r4
                return
            L_0x01b6:
                if (r1 >= r0) goto L_0x0287
                if (r0 > r2) goto L_0x0287
                int r0 = r0 - r1
                if (r0 <= 0) goto L_0x022d
                io.dcloud.common.core.ui.g r1 = io.dcloud.common.core.ui.g.this
                android.view.View r2 = r1.u
                int r1 = r1.b((android.view.View) r2)
                if (r0 >= r1) goto L_0x022d
                io.dcloud.common.core.ui.g r1 = io.dcloud.common.core.ui.g.this
                android.view.View r2 = r1.u
                int r1 = r1.b((android.view.View) r2)
                int r1 = r1 / 2
                if (r0 < r1) goto L_0x01e4
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r4 = r0.o
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r0 = r0.q
                goto L_0x01f2
            L_0x01e4:
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r0 = r0.n
                io.dcloud.common.core.ui.g r1 = io.dcloud.common.core.ui.g.this
                int r1 = r1.p
                goto L_0x00f2
            L_0x01f2:
                io.dcloud.common.core.ui.g r1 = io.dcloud.common.core.ui.g.this
                android.view.View r2 = r1.t
                int r1 = r1.a((android.view.View) r2)
                io.dcloud.common.core.ui.g r2 = io.dcloud.common.core.ui.g.this
                android.view.View r5 = r2.t
                io.dcloud.nineoldandroids.animation.ValueAnimator r1 = r2.a((android.view.View) r5, (int) r1, (int) r4, (boolean) r3)
                if (r1 == 0) goto L_0x020b
                r1.start()
            L_0x020b:
                io.dcloud.common.core.ui.g r1 = io.dcloud.common.core.ui.g.this
                android.view.View r2 = r1.u
                int r1 = r1.a((android.view.View) r2)
                io.dcloud.common.core.ui.g r2 = io.dcloud.common.core.ui.g.this
                android.view.View r2 = r2.u
                if (r2 == 0) goto L_0x022c
                io.dcloud.common.core.ui.g r2 = io.dcloud.common.core.ui.g.this
                android.view.View r4 = r2.u
                io.dcloud.nineoldandroids.animation.ValueAnimator r0 = r2.a((android.view.View) r4, (int) r1, (int) r0, (boolean) r3)
                if (r0 == 0) goto L_0x022c
                r0.start()
            L_0x022c:
                return
            L_0x022d:
                io.dcloud.common.core.ui.g r1 = io.dcloud.common.core.ui.g.this
                android.view.View r2 = r1.u
                int r1 = r1.b((android.view.View) r2)
                if (r0 != r1) goto L_0x0260
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                r0.b((boolean) r3)
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r0.t
                io.dcloud.common.core.ui.g r2 = io.dcloud.common.core.ui.g.this
                int r2 = r2.o
                r0.a((android.view.View) r1, (int) r2)
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r0.u
                io.dcloud.common.core.ui.g r2 = io.dcloud.common.core.ui.g.this
                int r2 = r2.q
                r0.a((android.view.View) r1, (int) r2)
                boolean unused = io.dcloud.common.core.ui.g.a = r4
                return
            L_0x0260:
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                r0.b((boolean) r4)
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r0.t
                io.dcloud.common.core.ui.g r2 = io.dcloud.common.core.ui.g.this
                int r2 = r2.n
                r0.a((android.view.View) r1, (int) r2)
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r0.u
                io.dcloud.common.core.ui.g r2 = io.dcloud.common.core.ui.g.this
                int r2 = r2.p
                r0.a((android.view.View) r1, (int) r2)
                boolean unused = io.dcloud.common.core.ui.g.a = r4
                return
            L_0x0287:
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                r0.b((boolean) r4)
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r0.t
                io.dcloud.common.core.ui.g r2 = io.dcloud.common.core.ui.g.this
                int r2 = r2.n
                r0.a((android.view.View) r1, (int) r2)
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r0.u
                io.dcloud.common.core.ui.g r2 = io.dcloud.common.core.ui.g.this
                int r2 = r2.p
                r0.a((android.view.View) r1, (int) r2)
                boolean unused = io.dcloud.common.core.ui.g.a = r4
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.core.ui.g.c.onGlobalLayout():void");
        }
    }

    class d implements Animator.AnimatorListener {
        final /* synthetic */ boolean a;
        final /* synthetic */ boolean b;
        final /* synthetic */ View c;
        final /* synthetic */ int d;

        d(boolean z, boolean z2, View view, int i) {
            this.a = z;
            this.b = z2;
            this.c = view;
            this.d = i;
        }

        public void onAnimationCancel(Animator animator) {
        }

        /* JADX WARNING: Removed duplicated region for block: B:60:0x014a  */
        /* JADX WARNING: Removed duplicated region for block: B:61:0x014c  */
        /* JADX WARNING: Removed duplicated region for block: B:63:0x014f  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onAnimationEnd(io.dcloud.nineoldandroids.animation.Animator r12) {
            /*
                r11 = this;
                io.dcloud.common.core.ui.g r12 = io.dcloud.common.core.ui.g.this
                boolean r12 = r12.T
                r0 = 0
                if (r12 == 0) goto L_0x0020
                io.dcloud.common.core.ui.g r12 = io.dcloud.common.core.ui.g.this
                boolean unused = r12.T = r0
                io.dcloud.common.core.ui.g r12 = io.dcloud.common.core.ui.g.this
                boolean r1 = r11.a
                boolean r2 = r11.b
                if (r1 == 0) goto L_0x0019
                java.lang.String r3 = "100"
                goto L_0x001b
            L_0x0019:
                java.lang.String r3 = "0"
            L_0x001b:
                java.lang.String r4 = "end"
                r12.a((java.lang.String) r4, (boolean) r1, (boolean) r2, (java.lang.String) r3)
            L_0x0020:
                boolean r12 = r11.a
                if (r12 == 0) goto L_0x0060
                android.view.View r12 = r11.c
                io.dcloud.common.core.ui.g r1 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r1.t
                if (r12 != r1) goto L_0x00e2
                io.dcloud.common.core.ui.g r12 = io.dcloud.common.core.ui.g.this
                io.dcloud.common.core.ui.b r12 = r12.r
                if (r12 == 0) goto L_0x00e2
                io.dcloud.common.core.ui.g r12 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r12.t
                int r12 = r12.a((android.view.View) r1)
                io.dcloud.common.core.ui.g r1 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r1.t
                int r1 = r1.getWidth()
                int r1 = -r1
                if (r12 <= r1) goto L_0x0055
                io.dcloud.common.core.ui.g r1 = io.dcloud.common.core.ui.g.this
                int r1 = r1.R
                if (r12 < r1) goto L_0x00e2
            L_0x0055:
                io.dcloud.common.core.ui.g r12 = io.dcloud.common.core.ui.g.this
                io.dcloud.common.core.ui.b r12 = r12.r
                r12.popFromViewStack()
                goto L_0x00e2
            L_0x0060:
                android.view.View r12 = r11.c
                io.dcloud.common.core.ui.g r1 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r1.u
                if (r12 != r1) goto L_0x00e2
                io.dcloud.common.core.ui.g r12 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r12.u
                int r12 = r12.a((android.view.View) r1)
                io.dcloud.common.core.ui.g r1 = io.dcloud.common.core.ui.g.this
                int r1 = r1.R
                int r1 = -r1
                if (r12 <= r1) goto L_0x0085
                io.dcloud.common.core.ui.g r1 = io.dcloud.common.core.ui.g.this
                int r1 = r1.R
                if (r12 < r1) goto L_0x00ac
            L_0x0085:
                io.dcloud.common.core.ui.g r1 = io.dcloud.common.core.ui.g.this
                int r1 = r1.W
                r2 = 2147483647(0x7fffffff, float:NaN)
                if (r2 == r1) goto L_0x00a7
                io.dcloud.common.core.ui.g r1 = io.dcloud.common.core.ui.g.this
                int r1 = r1.W
                if (r1 == r12) goto L_0x00a7
                io.dcloud.common.core.ui.g r12 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r12.u
                io.dcloud.common.core.ui.g r3 = io.dcloud.common.core.ui.g.this
                int r3 = r3.W
                r12.b((android.view.View) r1, (int) r3)
            L_0x00a7:
                io.dcloud.common.core.ui.g r12 = io.dcloud.common.core.ui.g.this
                int unused = r12.W = r2
            L_0x00ac:
                android.view.View r12 = r11.c
                boolean r12 = r12 instanceof io.dcloud.common.DHInterface.INativeView
                if (r12 != 0) goto L_0x00e2
                io.dcloud.common.core.ui.g r12 = io.dcloud.common.core.ui.g.this
                io.dcloud.common.core.ui.b r12 = r12.s
                if (r12 == 0) goto L_0x00e2
                io.dcloud.common.core.ui.g r12 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r12.u
                int r12 = r12.a((android.view.View) r1)
                io.dcloud.common.core.ui.g r1 = io.dcloud.common.core.ui.g.this
                android.view.View r2 = r1.u
                int r1 = r1.b((android.view.View) r2)
                int r1 = -r1
                if (r12 <= r1) goto L_0x00d9
                io.dcloud.common.core.ui.g r1 = io.dcloud.common.core.ui.g.this
                int r1 = r1.R
                if (r12 < r1) goto L_0x00e2
            L_0x00d9:
                io.dcloud.common.core.ui.g r12 = io.dcloud.common.core.ui.g.this
                io.dcloud.common.core.ui.b r12 = r12.s
                r12.popFromViewStack()
            L_0x00e2:
                io.dcloud.common.core.ui.g r12 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r11.c
                int r2 = r11.d
                r12.a((android.view.View) r1, (int) r2)
                android.view.View r12 = r11.c
                r1 = 0
                r2 = 1
                if (r12 == 0) goto L_0x01fe
                boolean r3 = r11.b
                if (r3 != 0) goto L_0x0137
                io.dcloud.common.core.ui.g r3 = io.dcloud.common.core.ui.g.this
                android.view.View r3 = r3.t
                if (r12 != r3) goto L_0x0115
                io.dcloud.common.core.ui.g r12 = io.dcloud.common.core.ui.g.this
                io.dcloud.common.core.ui.b r12 = r12.r
                boolean r3 = r11.a
                if (r3 == 0) goto L_0x010e
                io.dcloud.common.core.ui.g r3 = io.dcloud.common.core.ui.g.this
                java.lang.String r3 = r3.x
                goto L_0x0139
            L_0x010e:
                io.dcloud.common.core.ui.g r3 = io.dcloud.common.core.ui.g.this
                java.lang.String r3 = r3.z
                goto L_0x0139
            L_0x0115:
                android.view.View r12 = r11.c
                io.dcloud.common.core.ui.g r3 = io.dcloud.common.core.ui.g.this
                android.view.View r3 = r3.u
                if (r12 != r3) goto L_0x0137
                io.dcloud.common.core.ui.g r12 = io.dcloud.common.core.ui.g.this
                io.dcloud.common.core.ui.b r12 = r12.s
                boolean r3 = r11.a
                if (r3 == 0) goto L_0x0130
                io.dcloud.common.core.ui.g r3 = io.dcloud.common.core.ui.g.this
                java.lang.String r3 = r3.B
                goto L_0x0139
            L_0x0130:
                io.dcloud.common.core.ui.g r3 = io.dcloud.common.core.ui.g.this
                java.lang.String r3 = r3.D
                goto L_0x0139
            L_0x0137:
                r12 = r1
                r3 = r12
            L_0x0139:
                java.lang.String r4 = "hide"
                boolean r4 = r4.equalsIgnoreCase(r3)
                java.lang.String r5 = "close"
                if (r4 != 0) goto L_0x014c
                boolean r4 = r5.equalsIgnoreCase(r3)
                if (r4 == 0) goto L_0x014a
                goto L_0x014c
            L_0x014a:
                r4 = 0
                goto L_0x014d
            L_0x014c:
                r4 = 1
            L_0x014d:
                if (r4 == 0) goto L_0x01fe
                android.view.View r4 = r11.c
                boolean r6 = r4 instanceof io.dcloud.common.DHInterface.INativeView
                r7 = 3
                r8 = 2
                r9 = 4
                if (r6 == 0) goto L_0x01b7
                io.dcloud.common.DHInterface.INativeView r4 = (io.dcloud.common.DHInterface.INativeView) r4
                java.lang.String r12 = r4.getViewId()
                android.view.View r4 = r11.c
                io.dcloud.common.DHInterface.INativeView r4 = (io.dcloud.common.DHInterface.INativeView) r4
                java.lang.String r4 = r4.getViewUUId()
                boolean r5 = r3.equalsIgnoreCase(r5)
                if (r5 == 0) goto L_0x016f
                java.lang.String r3 = "view_close"
            L_0x016f:
                io.dcloud.common.core.ui.g r5 = io.dcloud.common.core.ui.g.this
                io.dcloud.common.core.ui.b r5 = r5.r
                io.dcloud.common.DHInterface.AbsMgr r5 = r5.obtainWindowMgr()
                io.dcloud.common.DHInterface.IMgr$MgrType r6 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
                java.lang.Object[] r9 = new java.lang.Object[r9]
                io.dcloud.common.core.ui.g r10 = io.dcloud.common.core.ui.g.this
                io.dcloud.common.core.ui.b r10 = r10.r
                io.dcloud.common.DHInterface.IWebview r10 = r10.obtainWebView()
                r9[r0] = r10
                java.lang.String r10 = "nativeobj"
                r9[r2] = r10
                r9[r8] = r3
                java.lang.StringBuilder r3 = new java.lang.StringBuilder
                r3.<init>()
                java.lang.String r8 = "['"
                r3.append(r8)
                r3.append(r12)
                java.lang.String r12 = "','"
                r3.append(r12)
                r3.append(r4)
                java.lang.String r12 = "']"
                r3.append(r12)
                java.lang.String r12 = r3.toString()
                org.json.JSONArray r12 = io.dcloud.common.util.JSONUtil.createJSONArray(r12)
                r9[r7] = r12
                r5.processEvent(r6, r2, r9)
                goto L_0x01fe
            L_0x01b7:
                if (r12 == 0) goto L_0x01fe
                io.dcloud.common.DHInterface.AbsMgr r4 = r12.obtainWindowMgr()
                io.dcloud.common.DHInterface.IMgr$MgrType r5 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
                java.lang.Object[] r6 = new java.lang.Object[r9]
                io.dcloud.common.DHInterface.IWebview r9 = r12.obtainWebView()
                r6[r0] = r9
                java.lang.String r9 = "ui"
                r6[r2] = r9
                java.lang.String r9 = "execMethod"
                r6[r8] = r9
                java.lang.StringBuilder r8 = new java.lang.StringBuilder
                r8.<init>()
                java.lang.String r9 = "[\"NWindow\",\""
                r8.append(r9)
                r8.append(r3)
                java.lang.String r3 = "\",[\""
                r8.append(r3)
                io.dcloud.common.DHInterface.IWebview r12 = r12.obtainWebView()
                java.lang.String r12 = r12.getWebviewUUID()
                r8.append(r12)
                java.lang.String r12 = "\",[null,null,null]]]"
                r8.append(r12)
                java.lang.String r12 = r8.toString()
                org.json.JSONArray r12 = io.dcloud.common.util.JSONUtil.createJSONArray(r12)
                r6[r7] = r12
                r4.processEvent(r5, r2, r6)
            L_0x01fe:
                io.dcloud.common.core.ui.g r12 = io.dcloud.common.core.ui.g.this
                int r12 = r12.X
                if (r12 > r2) goto L_0x020e
                boolean unused = io.dcloud.common.core.ui.g.a = r0
                io.dcloud.common.core.ui.g r12 = io.dcloud.common.core.ui.g.this
                android.view.View unused = r12.u = r1
            L_0x020e:
                io.dcloud.common.core.ui.g r12 = io.dcloud.common.core.ui.g.this
                int r12 = r12.X
                if (r12 < r2) goto L_0x021b
                io.dcloud.common.core.ui.g r12 = io.dcloud.common.core.ui.g.this
                io.dcloud.common.core.ui.g.h(r12)
            L_0x021b:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.core.ui.g.d.onAnimationEnd(io.dcloud.nineoldandroids.animation.Animator):void");
        }

        public void onAnimationRepeat(Animator animator) {
        }

        public void onAnimationStart(Animator animator) {
            g.g(g.this);
        }
    }

    class e implements ValueAnimator.AnimatorUpdateListener {
        final /* synthetic */ View a;

        e(View view) {
            this.a = view;
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            ViewGroup.LayoutParams layoutParams = this.a.getLayoutParams();
            View view = this.a;
            if (view instanceof INativeView) {
                g.this.b(view, ((Integer) valueAnimator.getAnimatedValue()).intValue());
                this.a.requestLayout();
                this.a.invalidate();
            } else if (layoutParams instanceof FrameLayout.LayoutParams) {
                ViewHelper.setX(view, ((Float) valueAnimator.getAnimatedValue()).floatValue());
            } else if (layoutParams instanceof AbsoluteLayout.LayoutParams) {
                try {
                    ViewHelper.setX(this.a, (float) ((Integer) valueAnimator.getAnimatedValue()).intValue());
                } catch (Exception unused) {
                    ViewHelper.setX(this.a, ((Float) valueAnimator.getAnimatedValue()).floatValue());
                }
                this.a.requestLayout();
            }
        }
    }

    class f implements ViewTreeObserver.OnGlobalLayoutListener {
        f() {
        }

        /* JADX WARNING: Removed duplicated region for block: B:46:0x018c  */
        /* JADX WARNING: Removed duplicated region for block: B:51:0x01b6  */
        /* JADX WARNING: Removed duplicated region for block: B:61:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onGlobalLayout() {
            /*
                r9 = this;
                int r0 = android.os.Build.VERSION.SDK_INT
                r1 = 16
                if (r0 < r1) goto L_0x0013
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r0 = r0.t
                android.view.ViewTreeObserver r0 = r0.getViewTreeObserver()
                r0.removeOnGlobalLayoutListener(r9)
            L_0x0013:
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                java.lang.String r1 = r0.M
                boolean r0 = r0.d((java.lang.String) r1)
                r1 = 1
                r2 = 0
                if (r0 == 0) goto L_0x0054
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                java.lang.String r0 = r0.M
                java.lang.String r3 = "right"
                boolean r0 = r3.equals(r0)
                if (r0 == 0) goto L_0x0036
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r0 = r0.R
                goto L_0x0051
            L_0x0036:
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                java.lang.String r0 = r0.M
                java.lang.String r3 = "left"
                boolean r0 = r3.equals(r0)
                if (r0 == 0) goto L_0x0050
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r3 = r0.u
                int r0 = r0.b((android.view.View) r3)
            L_0x004e:
                int r0 = -r0
                goto L_0x0051
            L_0x0050:
                r0 = 0
            L_0x0051:
                r6 = 0
                goto L_0x0180
            L_0x0054:
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r0 = r0.t
                android.view.ViewParent r0 = r0.getParent()
                android.view.View r0 = (android.view.View) r0
                r3 = 2
                int[] r4 = new int[r3]
                int[] r5 = new int[r3]
                int[] r6 = new int[r3]
                r0.getLocationOnScreen(r4)
                io.dcloud.common.core.ui.g r7 = io.dcloud.common.core.ui.g.this
                android.view.View r7 = r7.t
                r7.getLocationOnScreen(r5)
                io.dcloud.common.core.ui.g r7 = io.dcloud.common.core.ui.g.this
                android.view.View r7 = r7.u
                r7.getLocationOnScreen(r6)
                r7 = r4[r2]
                int r0 = r0.getWidth()
                int r7 = r7 + r0
                r0 = r5[r2]
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r0 = r0.t
                r0.getWidth()
                r0 = r6[r2]
                io.dcloud.common.core.ui.g r5 = io.dcloud.common.core.ui.g.this
                android.view.View r8 = r5.u
                int r5 = r5.b((android.view.View) r8)
                int r0 = r0 + r5
                r5 = r6[r2]
                r8 = r4[r2]
                if (r5 > r8) goto L_0x0134
                r5 = r4[r2]
                if (r5 >= r0) goto L_0x0134
                r4 = r4[r2]
                int r0 = r0 - r4
                if (r0 <= 0) goto L_0x00dd
                io.dcloud.common.core.ui.g r4 = io.dcloud.common.core.ui.g.this
                android.view.View r5 = r4.u
                int r4 = r4.b((android.view.View) r5)
                if (r0 >= r4) goto L_0x00dd
                io.dcloud.common.core.ui.g r4 = io.dcloud.common.core.ui.g.this
                android.view.View r5 = r4.u
                int r4 = r4.b((android.view.View) r5)
                int r4 = r4 / r3
                if (r0 > r4) goto L_0x00ce
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r3 = r0.u
                int r0 = r0.b((android.view.View) r3)
                goto L_0x004e
            L_0x00ce:
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r0.u
                int r0 = r0.b((android.view.View) r1)
                r6 = r0
                r0 = 0
            L_0x00da:
                r1 = 0
                goto L_0x0180
            L_0x00dd:
                io.dcloud.common.core.ui.g r3 = io.dcloud.common.core.ui.g.this
                android.view.View r4 = r3.u
                int r3 = r3.b((android.view.View) r4)
                if (r0 != r3) goto L_0x010e
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                r0.a((boolean) r2, (boolean) r1)
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r0.t
                io.dcloud.common.core.ui.g r3 = io.dcloud.common.core.ui.g.this
                android.view.View r4 = r3.u
                int r3 = r3.b((android.view.View) r4)
                r0.a((android.view.View) r1, (int) r3)
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r0.u
                r0.a((android.view.View) r1, (int) r2)
                boolean unused = io.dcloud.common.core.ui.g.a = r2
                return
            L_0x010e:
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                r0.a((boolean) r1, (boolean) r1)
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r0.t
                r0.a((android.view.View) r1, (int) r2)
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r0.u
                io.dcloud.common.core.ui.g r3 = io.dcloud.common.core.ui.g.this
                android.view.View r4 = r3.u
                int r3 = r3.b((android.view.View) r4)
                int r3 = -r3
                r0.a((android.view.View) r1, (int) r3)
                boolean unused = io.dcloud.common.core.ui.g.a = r2
                return
            L_0x0134:
                r4 = r6[r2]
                if (r4 >= r7) goto L_0x021e
                if (r7 > r0) goto L_0x021e
                r0 = r6[r2]
                int r7 = r7 - r0
                if (r7 <= 0) goto L_0x01ba
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r4 = r0.u
                int r0 = r0.b((android.view.View) r4)
                if (r7 >= r0) goto L_0x01ba
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r4 = r0.u
                int r0 = r0.b((android.view.View) r4)
                int r0 = r0 / r3
                if (r7 > r0) goto L_0x0160
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                int r0 = r0.R
                goto L_0x0051
            L_0x0160:
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r0.u
                int r0 = r0.b((android.view.View) r1)
                int r0 = -r0
                io.dcloud.common.core.ui.g r1 = io.dcloud.common.core.ui.g.this
                int r1 = r1.R
                io.dcloud.common.core.ui.g r3 = io.dcloud.common.core.ui.g.this
                android.view.View r4 = r3.u
                int r3 = r3.b((android.view.View) r4)
                int r1 = r1 - r3
                r6 = r0
                r0 = r1
                goto L_0x00da
            L_0x0180:
                io.dcloud.common.core.ui.g r2 = io.dcloud.common.core.ui.g.this
                android.view.View r3 = r2.t
                int r5 = r2.a((android.view.View) r3)
                if (r5 == 0) goto L_0x019d
                io.dcloud.common.core.ui.g r3 = io.dcloud.common.core.ui.g.this
                android.view.View r4 = r3.t
                r8 = 1
                r7 = r1
                io.dcloud.nineoldandroids.animation.ValueAnimator r2 = r3.a((android.view.View) r4, (int) r5, (int) r6, (boolean) r7, (boolean) r8)
                if (r2 == 0) goto L_0x019d
                r2.start()
            L_0x019d:
                io.dcloud.common.core.ui.g r2 = io.dcloud.common.core.ui.g.this
                android.view.View r3 = r2.u
                int r5 = r2.a((android.view.View) r3)
                io.dcloud.common.core.ui.g r3 = io.dcloud.common.core.ui.g.this
                android.view.View r4 = r3.u
                r8 = 1
                r6 = r0
                r7 = r1
                io.dcloud.nineoldandroids.animation.ValueAnimator r0 = r3.a((android.view.View) r4, (int) r5, (int) r6, (boolean) r7, (boolean) r8)
                if (r0 == 0) goto L_0x01b9
                r0.start()
            L_0x01b9:
                return
            L_0x01ba:
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r3 = r0.u
                int r0 = r0.b((android.view.View) r3)
                if (r7 != r0) goto L_0x01fd
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                r0.a((boolean) r2, (boolean) r1)
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r0.t
                io.dcloud.common.core.ui.g r3 = io.dcloud.common.core.ui.g.this
                android.view.View r4 = r3.u
                int r3 = r3.b((android.view.View) r4)
                int r3 = -r3
                r0.a((android.view.View) r1, (int) r3)
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r0.u
                io.dcloud.common.core.ui.g r3 = io.dcloud.common.core.ui.g.this
                int r3 = r3.R
                io.dcloud.common.core.ui.g r4 = io.dcloud.common.core.ui.g.this
                android.view.View r5 = r4.u
                int r4 = r4.b((android.view.View) r5)
                int r3 = r3 - r4
                r0.a((android.view.View) r1, (int) r3)
                boolean unused = io.dcloud.common.core.ui.g.a = r2
                return
            L_0x01fd:
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                r0.a((boolean) r1, (boolean) r1)
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r0.t
                r0.a((android.view.View) r1, (int) r2)
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r0.u
                io.dcloud.common.core.ui.g r3 = io.dcloud.common.core.ui.g.this
                int r3 = r3.R
                r0.a((android.view.View) r1, (int) r3)
                boolean unused = io.dcloud.common.core.ui.g.a = r2
                return
            L_0x021e:
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                r0.a((boolean) r1, (boolean) r1)
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r0.t
                r0.a((android.view.View) r1, (int) r2)
                io.dcloud.common.core.ui.g r0 = io.dcloud.common.core.ui.g.this
                android.view.View r1 = r0.u
                io.dcloud.common.core.ui.g r3 = io.dcloud.common.core.ui.g.this
                int r3 = r3.R
                r0.a((android.view.View) r1, (int) r3)
                boolean unused = io.dcloud.common.core.ui.g.a = r2
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.core.ui.g.f.onGlobalLayout():void");
        }
    }

    public g(IFrameView iFrameView, Context context) {
        a = false;
        this.X = 0;
        if (iFrameView instanceof b) {
            this.r = (b) iFrameView;
            if (this.Y == null) {
                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                this.Y = displayMetrics;
                int i2 = displayMetrics.widthPixels;
                this.R = i2;
                this.S = i2;
            }
        }
        this.i = ViewConfiguration.get(context).getScaledTouchSlop();
        this.j0 = Pattern.compile(this.i0);
    }

    static /* synthetic */ int g(g gVar) {
        int i2 = gVar.X;
        gVar.X = i2 + 1;
        return i2;
    }

    static /* synthetic */ int h(g gVar) {
        int i2 = gVar.X;
        gVar.X = i2 - 1;
        return i2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0046, code lost:
        if (r0 <= ((float) r2)) goto L_0x0048;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0056, code lost:
        if (r5 <= ((float) r2)) goto L_0x0082;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0070, code lost:
        if (r0 <= ((float) r2)) goto L_0x0072;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0080, code lost:
        if (r5 <= ((float) r2)) goto L_0x0082;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x000f, code lost:
        if (r5 != 3) goto L_0x008f;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean d(android.view.MotionEvent r5) {
        /*
            r4 = this;
            float r0 = r5.getRawX()
            int r5 = r5.getAction()
            r1 = 1
            if (r5 == r1) goto L_0x0090
            r2 = 2
            if (r5 == r2) goto L_0x0013
            r0 = 3
            if (r5 == r0) goto L_0x0090
            goto L_0x008f
        L_0x0013:
            float r5 = r4.j
            float r5 = r0 - r5
            int r5 = (int) r5
            float r5 = (float) r5
            r4.j = r0
            float r5 = r4.a((float) r5)
            android.view.View r0 = r4.t
            int r0 = r4.a((android.view.View) r0)
            float r0 = (float) r0
            float r0 = r0 + r5
            android.view.View r2 = r4.u
            int r2 = r4.a((android.view.View) r2)
            float r2 = (float) r2
            float r5 = r5 + r2
            java.lang.String r2 = r4.M
            java.lang.String r3 = "right"
            boolean r2 = r3.equals(r2)
            if (r2 == 0) goto L_0x0059
            int r2 = r4.o
            float r3 = (float) r2
            int r3 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r3 < 0) goto L_0x0041
            goto L_0x0048
        L_0x0041:
            int r2 = r4.n
            float r3 = (float) r2
            int r3 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r3 > 0) goto L_0x0049
        L_0x0048:
            float r0 = (float) r2
        L_0x0049:
            int r2 = r4.q
            float r3 = (float) r2
            int r3 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r3 < 0) goto L_0x0051
            goto L_0x0082
        L_0x0051:
            int r2 = r4.p
            float r3 = (float) r2
            int r3 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r3 > 0) goto L_0x0083
            goto L_0x0082
        L_0x0059:
            java.lang.String r2 = r4.M
            java.lang.String r3 = "left"
            boolean r2 = r3.equals(r2)
            if (r2 == 0) goto L_0x0083
            int r2 = r4.n
            float r3 = (float) r2
            int r3 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r3 < 0) goto L_0x006b
            goto L_0x0072
        L_0x006b:
            int r2 = r4.o
            float r3 = (float) r2
            int r3 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r3 > 0) goto L_0x0073
        L_0x0072:
            float r0 = (float) r2
        L_0x0073:
            int r2 = r4.p
            float r3 = (float) r2
            int r3 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r3 < 0) goto L_0x007b
            goto L_0x0082
        L_0x007b:
            int r2 = r4.q
            float r3 = (float) r2
            int r3 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r3 > 0) goto L_0x0083
        L_0x0082:
            float r5 = (float) r2
        L_0x0083:
            android.view.View r2 = r4.t
            int r0 = (int) r0
            r4.b((android.view.View) r2, (int) r0)
            android.view.View r0 = r4.u
            int r5 = (int) r5
            r4.b((android.view.View) r0, (int) r5)
        L_0x008f:
            return r1
        L_0x0090:
            r5 = 0
            r4.h = r5
            boolean r0 = r4.I
            if (r0 == 0) goto L_0x009a
            r4.a()
        L_0x009a:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.core.ui.g.d(android.view.MotionEvent):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0037, code lost:
        if (r0 < ((float) r4)) goto L_0x0053;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0051, code lost:
        if (r0 < ((float) r4)) goto L_0x0053;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x000f, code lost:
        if (r4 != 3) goto L_0x005a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean f(android.view.MotionEvent r4) {
        /*
            r3 = this;
            float r0 = r4.getRawX()
            int r4 = r4.getAction()
            r1 = 1
            if (r4 == r1) goto L_0x005b
            r2 = 2
            if (r4 == r2) goto L_0x0012
            r0 = 3
            if (r4 == r0) goto L_0x005b
            goto L_0x005a
        L_0x0012:
            float r4 = r3.j
            float r4 = r0 - r4
            r3.j = r0
            android.view.View r0 = r3.u
            int r0 = r3.a((android.view.View) r0)
            float r0 = (float) r0
            float r0 = r0 + r4
            java.lang.String r4 = r3.M
            java.lang.String r2 = "right"
            boolean r4 = r2.equals(r4)
            if (r4 == 0) goto L_0x003a
            int r4 = r3.q
            float r2 = (float) r4
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 <= 0) goto L_0x0032
            goto L_0x0053
        L_0x0032:
            int r4 = r3.p
            float r2 = (float) r4
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 >= 0) goto L_0x0054
            goto L_0x0053
        L_0x003a:
            java.lang.String r4 = r3.M
            java.lang.String r2 = "left"
            boolean r4 = r2.equals(r4)
            if (r4 == 0) goto L_0x0054
            int r4 = r3.p
            float r2 = (float) r4
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 <= 0) goto L_0x004c
            goto L_0x0053
        L_0x004c:
            int r4 = r3.q
            float r2 = (float) r4
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 >= 0) goto L_0x0054
        L_0x0053:
            float r0 = (float) r4
        L_0x0054:
            android.view.View r4 = r3.u
            int r0 = (int) r0
            r3.b((android.view.View) r4, (int) r0)
        L_0x005a:
            return r1
        L_0x005b:
            r4 = 0
            r3.h = r4
            boolean r0 = r3.I
            if (r0 == 0) goto L_0x0065
            r3.c()
        L_0x0065:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.core.ui.g.f(android.view.MotionEvent):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x003b, code lost:
        if (r0 < ((float) r4)) goto L_0x0057;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0055, code lost:
        if (r0 < ((float) r4)) goto L_0x0057;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x000f, code lost:
        if (r4 != 3) goto L_0x005e;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean g(android.view.MotionEvent r4) {
        /*
            r3 = this;
            float r0 = r4.getRawX()
            int r4 = r4.getAction()
            r1 = 1
            if (r4 == r1) goto L_0x005f
            r2 = 2
            if (r4 == r2) goto L_0x0012
            r0 = 3
            if (r4 == r0) goto L_0x005f
            goto L_0x005e
        L_0x0012:
            float r4 = r3.j
            float r4 = r0 - r4
            r3.j = r0
            float r4 = r3.a((float) r4)
            android.view.View r0 = r3.t
            int r0 = r3.a((android.view.View) r0)
            float r0 = (float) r0
            float r0 = r0 + r4
            java.lang.String r4 = r3.M
            java.lang.String r2 = "right"
            boolean r4 = r2.equals(r4)
            if (r4 == 0) goto L_0x003e
            int r4 = r3.o
            float r2 = (float) r4
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 <= 0) goto L_0x0036
            goto L_0x0057
        L_0x0036:
            int r4 = r3.n
            float r2 = (float) r4
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 >= 0) goto L_0x0058
            goto L_0x0057
        L_0x003e:
            java.lang.String r4 = r3.M
            java.lang.String r2 = "left"
            boolean r4 = r2.equals(r4)
            if (r4 == 0) goto L_0x0058
            int r4 = r3.n
            float r2 = (float) r4
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 <= 0) goto L_0x0050
            goto L_0x0057
        L_0x0050:
            int r4 = r3.o
            float r2 = (float) r4
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 >= 0) goto L_0x0058
        L_0x0057:
            float r0 = (float) r4
        L_0x0058:
            android.view.View r4 = r3.t
            int r0 = (int) r0
            r3.b((android.view.View) r4, (int) r0)
        L_0x005e:
            return r1
        L_0x005f:
            r4 = 0
            r3.h = r4
            boolean r0 = r3.I
            if (r0 == 0) goto L_0x0069
            r3.d()
        L_0x0069:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.core.ui.g.g(android.view.MotionEvent):boolean");
    }

    private boolean h() {
        this.n = a(this.t);
        if ("right".equals(this.M)) {
            int i2 = this.w;
            if (Integer.MAX_VALUE != i2) {
                this.o = i2;
                if (this.n == i2) {
                    return false;
                }
                return true;
            }
            this.o = this.R;
            View view = this.u;
            if (view != null) {
                if ("bounce".equalsIgnoreCase(this.E)) {
                    this.o = this.n + (b(this.t) / 2);
                    return true;
                } else if (b(this.u) >= this.R) {
                    return true;
                } else {
                    int a2 = a(this.t);
                    int a3 = a(this.u);
                    int a4 = a(this.u) + b(this.u);
                    if (a3 == 0 && a4 == a2) {
                        return false;
                    }
                    boolean z2 = this.H;
                    if (!z2 && a3 == 0) {
                        this.o = b(this.u);
                        return true;
                    } else if (z2 && a4 == 0) {
                        this.o = b(this.u);
                        return true;
                    } else if (this.R != a4) {
                        return true;
                    } else {
                        this.o = 0;
                        return true;
                    }
                }
            } else if (view != null || this.n >= 0) {
                return true;
            } else {
                this.o = 0;
                return true;
            }
        } else if (!"left".equals(this.M)) {
            return true;
        } else {
            int i3 = this.w;
            if (Integer.MAX_VALUE != i3) {
                this.o = i3;
                if (this.n == i3) {
                    return false;
                }
                return true;
            }
            this.o = -b(this.t);
            View view2 = this.u;
            if (view2 != null) {
                if ("bounce".equalsIgnoreCase(this.E)) {
                    this.o = this.n - (b(this.t) / 2);
                    return true;
                } else if (b(this.u) >= this.R) {
                    return true;
                } else {
                    int a5 = a(this.t) + b(this.t);
                    int a6 = a(this.u);
                    int a7 = a(this.u) + b(this.u);
                    int i4 = this.R;
                    if (i4 == a7 && a6 == a5) {
                        return false;
                    }
                    boolean z3 = this.H;
                    if (!z3 && i4 == a7) {
                        this.o = -b(this.u);
                        return true;
                    } else if (z3 && i4 == a6) {
                        this.o = -b(this.u);
                        return true;
                    } else if (a6 != 0) {
                        return true;
                    } else {
                        this.o = 0;
                        return true;
                    }
                }
            } else if (view2 != null || this.n <= 0) {
                return true;
            } else {
                this.o = 0;
                return true;
            }
        }
    }

    private void i() {
        float f2 = (float) this.n;
        this.a0 = f2;
        float f3 = (float) this.o;
        this.b0 = f3;
        this.d0 = f2;
        this.c0 = f2;
        float abs = Math.abs(f3 - f2);
        this.e0 = abs;
        this.f0 = (abs * this.Z) / 100.0f;
        this.g0 = true;
        this.h0 = false;
    }

    private boolean j() {
        if ("right".equals(this.M)) {
            if (a(this.u) == 0) {
                return false;
            }
            return true;
        } else if (!"left".equals(this.M) || a(this.u) + b(this.u) != this.R) {
            return true;
        } else {
            return false;
        }
    }

    private void k() {
        float a2 = (float) a(this.t);
        this.d0 = a2;
        if (this.g0) {
            float f2 = this.a0;
            if (a2 == f2) {
                this.h0 = true;
                this.g0 = false;
                this.c0 = f2;
                a(WXGesture.MOVE, false, WXInstanceApm.VALUE_ERROR_CODE_DEFAULT);
                return;
            }
        }
        if (Math.abs(a2 - this.c0) >= this.f0) {
            this.g0 = true;
            this.h0 = true;
            a(WXGesture.MOVE, false, "" + ((int) ((Math.abs(this.d0 - this.a0) / this.e0) * 100.0f)));
            this.c0 = this.d0;
        } else if (this.h0) {
            float f3 = this.d0;
            float f4 = this.b0;
            if (f3 == f4) {
                this.h0 = false;
                this.g0 = true;
                this.c0 = f4;
                a(WXGesture.MOVE, false, "100");
            }
        }
    }

    public HashMap<String, DragBean> e() {
        b bVar = this.r;
        if (bVar == null || bVar.obtainFrameOptions() == null) {
            return null;
        }
        return this.r.obtainFrameOptions().dragData;
    }

    public void c(boolean z2) {
        this.b = z2;
    }

    public boolean c(MotionEvent motionEvent) {
        View view;
        View view2;
        if (!this.h) {
            return false;
        }
        if (motionEvent.getPointerCount() > 1) {
            return true;
        }
        if (BaseInfo.sDoingAnimation) {
            if (this.V) {
                return false;
            }
            this.V = true;
            motionEvent.setAction(3);
        }
        if (this.b && !this.h) {
            return true;
        }
        if (this.g == null) {
            this.g = VelocityTracker.obtain();
        }
        if (this.J == -1 || a) {
            return false;
        }
        if (2 == motionEvent.getAction()) {
            k();
            if ("left".equals(this.M)) {
                if (motionEvent.getRawX() < this.j) {
                    this.g.addMovement(motionEvent);
                }
            } else if ("right".equals(this.M) && motionEvent.getRawX() > this.j) {
                this.g.addMovement(motionEvent);
            }
        }
        if (1 == motionEvent.getAction() || 3 == motionEvent.getAction()) {
            this.g.addMovement(motionEvent);
        }
        if (this.U) {
            return e(motionEvent);
        }
        boolean z2 = this.F;
        if (!z2 || !this.G || !this.H) {
            if (!z2 || this.G || !this.H) {
                if (!z2 || this.H || !this.G) {
                    if (!z2 && this.G && !a(this.r)) {
                        return g(motionEvent);
                    }
                } else if (!a(this.r) && !a(this.s)) {
                    return g(motionEvent);
                }
            } else if ((!a(this.r) && !a(this.s)) || ((view = this.u) != null && (view instanceof INativeView))) {
                return f(motionEvent);
            }
        } else if ((!a(this.r) && !a(this.s)) || ((view2 = this.u) != null && (view2 instanceof INativeView))) {
            return d(motionEvent);
        }
        return true;
    }

    private boolean e(String str) {
        JSONObject jSONObject;
        boolean z2;
        this.t = this.r.obtainMainView();
        this.w = Integer.MAX_VALUE;
        this.y = Integer.MAX_VALUE;
        this.x = null;
        this.z = null;
        HashMap<String, DragBean> e2 = e();
        if (e2 == null || !e2.containsKey(str)) {
            return false;
        }
        DragBean dragBean = e2.get(str);
        if (dragBean != null) {
            this.L = dragBean.dragCbId;
            this.K = dragBean.dragCallBackWebView;
            JSONObject jSONObject2 = dragBean.dragCurrentViewOp;
            try {
                this.N = JSONUtil.getString(jSONObject2, "direction");
                if (jSONObject2.has("moveMode")) {
                    String string = JSONUtil.getString(jSONObject2, "moveMode");
                    this.E = string;
                    if (!"followFinger".equalsIgnoreCase(string)) {
                        if (!"follow".equalsIgnoreCase(this.E)) {
                            if (!"bounce".equalsIgnoreCase(this.E)) {
                                z2 = false;
                                this.G = z2;
                            }
                        }
                    }
                    z2 = true;
                    this.G = z2;
                }
                this.Z = 20.0f;
                if (jSONObject2.has("callbackStep")) {
                    try {
                        String string2 = jSONObject2.getString("callbackStep");
                        if (this.j0.matcher(string2).find()) {
                            this.Z = (float) Integer.valueOf(string2).intValue();
                        }
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                }
                if (jSONObject2.has("over")) {
                    JSONObject jSONObject3 = JSONUtil.getJSONObject(jSONObject2, "over");
                    if (jSONObject3 != null) {
                        if (jSONObject3.has("left")) {
                            this.w = PdrUtil.parseInt(JSONUtil.getString(jSONObject3, "left"), this.R, Integer.MAX_VALUE);
                        }
                        if (jSONObject3.has("action")) {
                            this.x = JSONUtil.getString(jSONObject3, "action");
                        }
                    }
                }
                if (jSONObject2.has(BindingXConstants.STATE_CANCEL) && (jSONObject = JSONUtil.getJSONObject(jSONObject2, (String) BindingXConstants.STATE_CANCEL)) != null) {
                    if (jSONObject.has("left")) {
                        this.y = PdrUtil.parseInt(JSONUtil.getString(jSONObject, "left"), this.R, Integer.MAX_VALUE);
                    }
                    if (jSONObject.has("action")) {
                        this.z = JSONUtil.getString(jSONObject, "action");
                    }
                }
            } catch (Exception e4) {
                e4.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public void b(MotionEvent motionEvent) {
        DisplayMetrics displayMetrics = this.Y;
        if (displayMetrics != null) {
            this.R = displayMetrics.widthPixels;
        }
        this.Q = motionEvent.getRawX();
        float rawX = motionEvent.getRawX();
        this.j = rawX;
        this.l = rawX;
        this.k = motionEvent.getRawY();
        this.h = false;
        this.F = false;
        this.v = false;
        this.V = false;
        this.J = -1;
        this.r.obtainWebView().loadUrl("javascript:window.__needNotifyNative__=true;");
        this.r.obtainWebView().setWebviewProperty("needTouchEvent", AbsoluteConst.FALSE);
        this.P = false;
        this.O = this.r.obtainWebView().obtainWindowView().getScrollY();
        if (this.g == null) {
            this.g = VelocityTracker.obtain();
        }
        this.g.addMovement(motionEvent);
    }

    public boolean a(MotionEvent motionEvent) {
        View view;
        View view2;
        if (this.b) {
            return true;
        }
        if (e() == null || BaseInfo.sDoingAnimation || a) {
            this.h = false;
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        float rawX = motionEvent.getRawX();
        float rawY = motionEvent.getRawY();
        if (actionMasked == 0 && motionEvent.getEdgeFlags() != 0) {
            return false;
        }
        if (actionMasked == 0) {
            b(motionEvent);
        } else if (actionMasked == 1) {
            this.v = false;
            this.J = -1;
        } else if (actionMasked == 2) {
            if (this.O != this.r.obtainWebView().obtainWindowView().getScrollY()) {
                this.P = true;
            }
            if (this.P) {
                return false;
            }
            float f2 = rawX - this.j;
            if (Math.abs(f2) >= Math.abs(rawY - this.k)) {
                if (((float) (this.i * 3)) <= Math.abs(motionEvent.getRawX() - this.Q) && AbsoluteConst.FALSE.equals(this.r.obtainWebView().getWebviewProperty("needTouchEvent"))) {
                    String str = "right";
                    if (f2 < 0.0f) {
                        if (!this.v && e("left")) {
                            this.M = "left";
                            this.h = true;
                            this.v = true;
                        }
                        str = "left";
                    } else if (!this.v && e(str)) {
                        this.M = str;
                        this.h = true;
                        this.v = true;
                    }
                    if (-1 == this.J) {
                        this.j = rawX;
                        View a2 = a(str);
                        if (a2 != null) {
                            HashMap<String, DragBean> e2 = e();
                            String c2 = c(str);
                            if (e2 != null && e2.containsKey(c2)) {
                                a(e2.get(c2).dragBindViewOp);
                            }
                            this.M = str;
                            this.h = true;
                            this.u = a2;
                            this.U = true;
                            this.J = 1;
                        } else {
                            b(this.M);
                        }
                    }
                }
            }
        }
        if (this.h) {
            if (this.U) {
                this.h = j();
            } else {
                boolean z2 = this.F;
                if (!z2 || !this.G || !this.H) {
                    if (!z2 || this.G || !this.H) {
                        if (z2 || !this.G) {
                            if (!z2 || this.H || !this.G) {
                                this.h = false;
                            } else if (!a(this.r) && !a(this.s)) {
                                this.h = h();
                            }
                        } else if (!a(this.r)) {
                            h();
                        }
                    } else if ((!a(this.r) && !a(this.s)) || ((view = this.u) != null && (view instanceof INativeView))) {
                        this.h = f();
                    }
                } else if ((!a(this.r) && !a(this.s)) || ((view2 = this.u) != null && (view2 instanceof INativeView))) {
                    this.h = g();
                }
            }
        }
        if (this.h) {
            if (!a(this.r)) {
                this.c = this.r.obtainWebView().obtainWindowView().isVerticalScrollBarEnabled();
                this.d = this.r.obtainWebView().obtainWindowView().isHorizontalScrollBarEnabled();
                this.r.obtainWebView().obtainWindowView().setVerticalScrollBarEnabled(false);
                this.r.obtainWebView().obtainWindowView().setHorizontalScrollBarEnabled(false);
            }
            View view3 = this.u;
            if (view3 != null && (view3 instanceof com.dcloud.android.widget.AbsoluteLayout)) {
                ((com.dcloud.android.widget.AbsoluteLayout) view3).getDrag().c(true);
                if (!a(this.s)) {
                    this.e = this.s.obtainWebView().obtainWindowView().isVerticalScrollBarEnabled();
                    this.f = this.s.obtainWebView().obtainWindowView().isHorizontalScrollBarEnabled();
                    this.s.obtainWebView().obtainWindowView().setVerticalScrollBarEnabled(false);
                    this.s.obtainWebView().obtainWindowView().setHorizontalScrollBarEnabled(false);
                }
            }
            a("start", false, WXInstanceApm.VALUE_ERROR_CODE_DEFAULT);
            i();
        }
        return this.h;
    }

    private int b(String str) {
        int i2;
        View view;
        DragBean dragBean;
        int i3 = this.J;
        if (i3 != -1) {
            return i3;
        }
        this.s = null;
        this.u = null;
        HashMap<String, DragBean> e2 = e();
        if (!(e2 == null || !e2.containsKey(str) || (dragBean = e2.get(str)) == null)) {
            a(dragBean.dragBindViewOp);
            IFrameView iFrameView = dragBean.dragBindWebView;
            if (iFrameView == null || !(iFrameView instanceof b)) {
                View view2 = dragBean.nativeView;
                this.u = view2;
                if (view2 != null) {
                    this.F = true;
                }
            } else {
                this.F = true;
                b bVar = (b) iFrameView;
                this.s = bVar;
                this.u = bVar.obtainMainView();
            }
        }
        if (a(this.s) && ((view = this.u) == null || !(view instanceof INativeView))) {
            this.J = 0;
        } else if (this.u.getVisibility() != 0) {
            this.s = null;
            this.u = null;
            return 0;
        } else {
            if (this.u.getParent() == null && (this.t.getParent() instanceof FrameLayout) && !(this.u instanceof INativeView)) {
                this.s.pushToViewStack();
            }
            if (!a(this.t, this.u) && !(this.u instanceof INativeView)) {
                this.s = null;
                this.u = null;
                return 0;
            } else if ((this.u instanceof INativeView) || !this.H || this.t.getParent() == this.u.getParent()) {
                View view3 = this.u;
                if (view3 instanceof INativeView) {
                    view3.bringToFront();
                }
                int a2 = a(this.u);
                int a3 = a(this.t);
                if (a3 == 0 && this.t.getWidth() == (i2 = this.R) && (a2 >= i2 || a2 <= (-b(this.u)))) {
                    this.W = a2;
                    boolean z2 = this.G;
                    if (z2 && this.H) {
                        if ("right".equals(str)) {
                            this.m = a3 - b(this.u);
                        } else if ("left".equals(str)) {
                            this.m = a3 + this.t.getWidth();
                        }
                        b(this.u, this.m);
                    } else if (!z2 && this.H) {
                        if ("right".equals(str)) {
                            this.m = -b(this.u);
                        } else if ("left".equals(str)) {
                            this.m = this.t.getWidth();
                        }
                        b(this.u, this.m);
                    }
                }
                this.J = 1;
            } else {
                this.J = 0;
                return 0;
            }
        }
        return this.J;
    }

    private boolean f() {
        this.p = a(this.u);
        if ("right".equals(this.M)) {
            int i2 = this.A;
            if (Integer.MAX_VALUE != i2) {
                this.q = i2;
                if (this.p == i2) {
                    return false;
                }
                return true;
            }
            this.q = b(this.u);
            int i3 = this.p;
            if (i3 == 0 || i3 == this.R) {
                return false;
            }
            if ("bounce".equalsIgnoreCase(this.E)) {
                this.q = this.p + (b(this.t) / 2);
                return true;
            } else if (this.p >= 0) {
                return true;
            } else {
                this.q = 0;
                return true;
            }
        } else if (!"left".equals(this.M)) {
            return true;
        } else {
            int i4 = this.A;
            if (Integer.MAX_VALUE != i4) {
                this.q = i4;
                if (this.p == i4) {
                    return false;
                }
                return true;
            }
            int b2 = b(this.u);
            this.q = -b2;
            if ("bounce".equalsIgnoreCase(this.E)) {
                this.q = this.p - (b(this.t) / 2);
                return true;
            }
            int i5 = this.R;
            if (b2 < i5) {
                int a2 = a(this.u) + b(this.u);
                if (a2 == this.R || a2 == 0) {
                    return false;
                }
                Rect rect = new Rect();
                this.u.getGlobalVisibleRect(rect);
                int i6 = this.R;
                if (i6 == rect.left) {
                    this.q = i6 - b2;
                    return true;
                } else if (rect.right != 0) {
                    return true;
                } else {
                    this.q = 0;
                    return true;
                }
            } else if (b2 != i5 || this.p <= 0) {
                return true;
            } else {
                this.q = 0;
                return true;
            }
        }
    }

    private boolean g() {
        return h() && f();
    }

    private void d() {
        this.T = true;
        a = true;
        this.t.requestLayout();
        this.t.getViewTreeObserver().addOnGlobalLayoutListener(new b());
    }

    /* access modifiers changed from: private */
    public boolean d(String str) {
        VelocityTracker velocityTracker = this.g;
        if (velocityTracker == null) {
            return false;
        }
        velocityTracker.computeCurrentVelocity(1000, 1000.0f);
        float xVelocity = velocityTracker.getXVelocity();
        this.g.clear();
        this.g.recycle();
        this.g = null;
        return Math.abs(xVelocity) >= 200.0f;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:34:0x008c, code lost:
        if ((((float) r10) + r15) >= ((float) r12)) goto L_0x00b1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00af, code lost:
        if ((((float) r10) + r15) <= ((float) r12)) goto L_0x00b1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00b1, code lost:
        r15 = (float) (r12 - r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0015, code lost:
        if (r0 != 3) goto L_0x0161;
     */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x006d  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00bb  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00dc  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00ff  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x0123  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0133  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean e(android.view.MotionEvent r15) {
        /*
            r14 = this;
            int r0 = r15.getAction()
            float r1 = r15.getRawX()
            float r15 = r15.getRawY()
            r2 = 1
            if (r0 == 0) goto L_0x015d
            if (r0 == r2) goto L_0x0150
            r15 = 2
            if (r0 == r15) goto L_0x0019
            r15 = 3
            if (r0 == r15) goto L_0x0150
            goto L_0x0161
        L_0x0019:
            float r15 = r14.j
            float r15 = r1 - r15
            r14.j = r1
            android.view.View r0 = r14.t
            android.view.ViewGroup$LayoutParams r0 = r0.getLayoutParams()
            boolean r1 = r0 instanceof android.widget.FrameLayout.LayoutParams
            r3 = 0
            if (r1 == 0) goto L_0x0031
            android.view.View r4 = r14.t
            float r4 = io.dcloud.nineoldandroids.view.ViewHelper.getX(r4)
            goto L_0x003d
        L_0x0031:
            boolean r4 = r0 instanceof android.widget.AbsoluteLayout.LayoutParams
            if (r4 == 0) goto L_0x003c
            r4 = r0
            android.widget.AbsoluteLayout$LayoutParams r4 = (android.widget.AbsoluteLayout.LayoutParams) r4
            int r4 = r4.x
            float r4 = (float) r4
            goto L_0x003d
        L_0x003c:
            r4 = 0
        L_0x003d:
            android.view.View r5 = r14.u
            if (r5 == 0) goto L_0x0161
            android.view.ViewGroup$LayoutParams r5 = r5.getLayoutParams()
            android.view.View r6 = r14.u
            boolean r7 = r6 instanceof io.dcloud.common.DHInterface.INativeView
            if (r7 == 0) goto L_0x0051
            int r6 = r14.a((android.view.View) r6)
        L_0x004f:
            float r6 = (float) r6
            goto L_0x0065
        L_0x0051:
            boolean r7 = r5 instanceof android.widget.FrameLayout.LayoutParams
            if (r7 == 0) goto L_0x005a
            float r6 = io.dcloud.nineoldandroids.view.ViewHelper.getX(r6)
            goto L_0x0065
        L_0x005a:
            boolean r6 = r5 instanceof android.widget.AbsoluteLayout.LayoutParams
            if (r6 == 0) goto L_0x0064
            r6 = r5
            android.widget.AbsoluteLayout$LayoutParams r6 = (android.widget.AbsoluteLayout.LayoutParams) r6
            int r6 = r6.x
            goto L_0x004f
        L_0x0064:
            r6 = 0
        L_0x0065:
            java.lang.String r7 = "left"
            java.lang.String r8 = "right"
            int r9 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1))
            if (r9 == 0) goto L_0x00b3
            java.lang.String r10 = r14.M
            boolean r10 = r8.equals(r10)
            if (r10 == 0) goto L_0x008f
            float r10 = r4 + r15
            int r10 = (r10 > r3 ? 1 : (r10 == r3 ? 0 : -1))
            if (r10 < 0) goto L_0x007c
            goto L_0x009d
        L_0x007c:
            android.view.View r10 = r14.t
            int r10 = r10.getWidth()
            float r10 = (float) r10
            float r10 = r10 + r4
            int r10 = (int) r10
            float r11 = (float) r10
            float r11 = r11 + r15
            int r12 = r14.R
            float r13 = (float) r12
            int r11 = (r11 > r13 ? 1 : (r11 == r13 ? 0 : -1))
            if (r11 < 0) goto L_0x00b3
            goto L_0x00b1
        L_0x008f:
            java.lang.String r10 = r14.M
            boolean r10 = r7.equals(r10)
            if (r10 == 0) goto L_0x00b3
            float r10 = r4 + r15
            int r10 = (r10 > r3 ? 1 : (r10 == r3 ? 0 : -1))
            if (r10 > 0) goto L_0x009f
        L_0x009d:
            float r15 = -r4
            goto L_0x00b3
        L_0x009f:
            android.view.View r10 = r14.t
            int r10 = r10.getWidth()
            float r10 = (float) r10
            float r10 = r10 + r4
            int r10 = (int) r10
            float r11 = (float) r10
            float r11 = r11 + r15
            int r12 = r14.R
            float r13 = (float) r12
            int r11 = (r11 > r13 ? 1 : (r11 == r13 ? 0 : -1))
            if (r11 > 0) goto L_0x00b3
        L_0x00b1:
            int r12 = r12 - r10
            float r15 = (float) r12
        L_0x00b3:
            java.lang.String r10 = r14.M
            boolean r8 = r8.equals(r10)
            if (r8 == 0) goto L_0x00dc
            float r3 = r6 + r15
            int r7 = r14.R
            float r7 = (float) r7
            int r3 = (r3 > r7 ? 1 : (r3 == r7 ? 0 : -1))
            if (r3 < 0) goto L_0x00c7
            float r15 = r7 - r6
            goto L_0x00fd
        L_0x00c7:
            android.view.View r3 = r14.u
            int r3 = r14.b((android.view.View) r3)
            float r3 = (float) r3
            float r6 = r6 + r3
            int r3 = (int) r6
            float r6 = (float) r3
            float r6 = r6 + r15
            int r7 = r14.R
            float r8 = (float) r7
            int r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r6 > 0) goto L_0x00fd
            int r7 = r7 - r3
            float r15 = (float) r7
            goto L_0x00fd
        L_0x00dc:
            java.lang.String r8 = r14.M
            boolean r7 = r7.equals(r8)
            if (r7 == 0) goto L_0x00fd
            android.view.View r7 = r14.u
            int r7 = r14.b((android.view.View) r7)
            float r7 = (float) r7
            float r7 = r7 + r6
            int r7 = (int) r7
            float r8 = (float) r7
            float r8 = r8 + r15
            int r8 = (r8 > r3 ? 1 : (r8 == r3 ? 0 : -1))
            if (r8 > 0) goto L_0x00f6
            int r15 = -r7
            float r15 = (float) r15
            goto L_0x00fd
        L_0x00f6:
            float r7 = r6 + r15
            int r3 = (r7 > r3 ? 1 : (r7 == r3 ? 0 : -1))
            if (r3 < 0) goto L_0x00fd
            float r15 = -r6
        L_0x00fd:
            if (r9 == 0) goto L_0x011d
            if (r1 == 0) goto L_0x010c
            android.view.View r0 = r14.t
            float r1 = io.dcloud.nineoldandroids.view.ViewHelper.getX(r0)
            float r1 = r1 + r15
            io.dcloud.nineoldandroids.view.ViewHelper.setX(r0, r1)
            goto L_0x011d
        L_0x010c:
            boolean r1 = r0 instanceof android.widget.AbsoluteLayout.LayoutParams
            if (r1 == 0) goto L_0x011d
            android.widget.AbsoluteLayout$LayoutParams r0 = (android.widget.AbsoluteLayout.LayoutParams) r0
            int r1 = r0.x
            int r3 = (int) r15
            int r1 = r1 + r3
            r0.x = r1
            android.view.View r0 = r14.t
            r0.requestLayout()
        L_0x011d:
            android.view.View r0 = r14.u
            boolean r1 = r0 instanceof io.dcloud.common.DHInterface.INativeView
            if (r1 == 0) goto L_0x0133
            float r4 = r4 + r15
            int r15 = (int) r4
            r14.b((android.view.View) r0, (int) r15)
            android.view.View r15 = r14.u
            r15.requestLayout()
            android.view.View r15 = r14.u
            r15.invalidate()
            goto L_0x0161
        L_0x0133:
            boolean r1 = r5 instanceof android.widget.FrameLayout.LayoutParams
            if (r1 == 0) goto L_0x0140
            float r1 = io.dcloud.nineoldandroids.view.ViewHelper.getX(r0)
            float r1 = r1 + r15
            io.dcloud.nineoldandroids.view.ViewHelper.setX(r0, r1)
            goto L_0x0161
        L_0x0140:
            boolean r1 = r5 instanceof android.widget.AbsoluteLayout.LayoutParams
            if (r1 == 0) goto L_0x0161
            android.widget.AbsoluteLayout$LayoutParams r5 = (android.widget.AbsoluteLayout.LayoutParams) r5
            int r1 = r5.x
            int r15 = (int) r15
            int r1 = r1 + r15
            r5.x = r1
            r0.requestLayout()
            goto L_0x0161
        L_0x0150:
            r15 = 0
            r14.U = r15
            r14.h = r15
            boolean r0 = r14.I
            if (r0 == 0) goto L_0x015c
            r14.b()
        L_0x015c:
            return r15
        L_0x015d:
            r14.j = r1
            r14.k = r15
        L_0x0161:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.core.ui.g.e(android.view.MotionEvent):boolean");
    }

    private void c() {
        this.T = true;
        a = true;
        View view = this.u;
        if (view != null) {
            view.requestLayout();
            this.u.getViewTreeObserver().addOnGlobalLayoutListener(new a());
        }
    }

    private String c(String str) {
        if ("left".equals(str)) {
            return "right";
        }
        if ("right".equals(str)) {
            return "left";
        }
        return null;
    }

    /* access modifiers changed from: private */
    public void b(boolean z2) {
        if (this.T) {
            this.T = false;
            a("end", z2, z2 ? "100" : WXInstanceApm.VALUE_ERROR_CODE_DEFAULT);
        }
    }

    private void b() {
        this.T = true;
        a = true;
        if (this.u != null) {
            this.t.requestLayout();
            this.t.getViewTreeObserver().addOnGlobalLayoutListener(new f());
        }
    }

    /* access modifiers changed from: private */
    public int b(View view) {
        if (view == null) {
            return 0;
        }
        if (view instanceof INativeView) {
            return ((INativeView) view).getStyleWidth();
        }
        return view.getWidth();
    }

    /* access modifiers changed from: private */
    public void b(View view, int i2) {
        if (view == null) {
            return;
        }
        if (view instanceof INativeView) {
            ((INativeView) view).setStyleLeft(i2);
        } else if (view.getLayoutParams() instanceof AbsoluteLayout.LayoutParams) {
            AbsoluteLayout.LayoutParams layoutParams = (AbsoluteLayout.LayoutParams) view.getLayoutParams();
            layoutParams.height = view.getHeight();
            layoutParams.width = view.getWidth();
            ViewHelper.setX(view, (float) i2);
            view.requestLayout();
        } else if (view.getLayoutParams() instanceof FrameLayout.LayoutParams) {
            ViewHelper.setX(view, (float) i2);
        }
    }

    private void a(JSONObject jSONObject) {
        JSONObject jSONObject2;
        JSONObject jSONObject3;
        if (jSONObject != null) {
            this.A = Integer.MAX_VALUE;
            this.C = Integer.MAX_VALUE;
            this.B = null;
            this.D = null;
            this.H = "follow".equalsIgnoreCase(JSONUtil.getString(jSONObject, "moveMode"));
            if (jSONObject.has("over") && (jSONObject3 = JSONUtil.getJSONObject(jSONObject, "over")) != null) {
                if (jSONObject3.has("left")) {
                    this.A = PdrUtil.parseInt(JSONUtil.getString(jSONObject3, "left"), this.R, Integer.MAX_VALUE);
                }
                if (jSONObject3.has("action")) {
                    this.B = JSONUtil.getString(jSONObject3, "action");
                }
            }
            if (jSONObject.has(BindingXConstants.STATE_CANCEL) && (jSONObject2 = JSONUtil.getJSONObject(jSONObject, (String) BindingXConstants.STATE_CANCEL)) != null) {
                if (jSONObject2.has("left")) {
                    this.C = PdrUtil.parseInt(JSONUtil.getString(jSONObject2, "left"), this.R, Integer.MAX_VALUE);
                }
                if (jSONObject2.has("action")) {
                    this.D = JSONUtil.getString(jSONObject2, "action");
                }
            }
        }
    }

    private boolean a(View view, View view2) {
        if (!(view == null || view2 == null)) {
            ViewParent parent = view.getParent();
            ViewParent parent2 = view2.getParent();
            while (parent != null && parent != view2) {
                while (parent2 != null) {
                    if (parent2 == view) {
                        return false;
                    }
                    if (parent == parent2) {
                        return true;
                    }
                    parent2 = parent2.getParent();
                }
                parent2 = view2.getParent();
                parent = parent.getParent();
            }
            return false;
        }
        return false;
    }

    private void a() {
        this.T = true;
        a = true;
        this.t.requestLayout();
        this.t.getViewTreeObserver().addOnGlobalLayoutListener(new c());
    }

    /* access modifiers changed from: private */
    public void a(boolean z2, boolean z3) {
        if (this.T) {
            this.T = false;
            a("end", z2, z3, z2 ? "100" : WXInstanceApm.VALUE_ERROR_CODE_DEFAULT);
        }
    }

    /* access modifiers changed from: private */
    public void a(View view, int i2) {
        b bVar;
        b bVar2;
        if (view == null) {
            return;
        }
        if (view == this.t && (bVar2 = this.r) != null) {
            bVar2.obtainFrameOptions().left = i2;
            this.r.obtainFrameOptions().checkValueIsPercentage("left", i2, this.R, true, true);
        } else if (view == this.u && (bVar = this.s) != null) {
            bVar.obtainFrameOptions().left = i2;
            this.s.obtainFrameOptions().checkValueIsPercentage("left", i2, this.R, true, true);
        }
    }

    /* access modifiers changed from: private */
    public ValueAnimator a(View view, int i2, int i3, boolean z2) {
        return a(view, i2, i3, z2, false);
    }

    /* access modifiers changed from: private */
    public ValueAnimator a(View view, int i2, int i3, boolean z2, boolean z3) {
        ValueAnimator valueAnimator = null;
        if (view == null) {
            return null;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (view instanceof INativeView) {
            valueAnimator = ValueAnimator.ofInt(i2, i3);
        } else if (layoutParams instanceof AbsoluteLayout.LayoutParams) {
            valueAnimator = ValueAnimator.ofInt(i2, i3);
        } else if (layoutParams instanceof FrameLayout.LayoutParams) {
            valueAnimator = ValueAnimator.ofFloat((float) i2, (float) i3);
        }
        valueAnimator.setDuration(Math.min(Math.max(new BigDecimal(450).multiply(new BigDecimal(Math.abs(i3 - i2)).divide(new BigDecimal(this.R), 4, 4)).longValue(), 200), 250));
        valueAnimator.addListener(new d(z2, z3, view, i3));
        valueAnimator.addUpdateListener(new e(view));
        return valueAnimator;
    }

    private void a(String str, boolean z2, String str2) {
        a(str, z2, false, str2);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00ce  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00e0  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x0107  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x0111  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x0118  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0123  */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x0126  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x013a  */
    /* JADX WARNING: Removed duplicated region for block: B:85:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(java.lang.String r12, boolean r13, boolean r14, java.lang.String r15) {
        /*
            r11 = this;
            java.lang.String r0 = "end"
            boolean r1 = r0.equals(r12)
            r2 = 0
            if (r1 == 0) goto L_0x0068
            io.dcloud.common.core.ui.b r1 = r11.r
            boolean r1 = r11.a((io.dcloud.common.core.ui.b) r1)
            if (r1 != 0) goto L_0x002f
            io.dcloud.common.core.ui.b r1 = r11.r
            io.dcloud.common.DHInterface.IWebview r1 = r1.obtainWebView()
            android.view.ViewGroup r1 = r1.obtainWindowView()
            boolean r3 = r11.c
            r1.setVerticalScrollBarEnabled(r3)
            io.dcloud.common.core.ui.b r1 = r11.r
            io.dcloud.common.DHInterface.IWebview r1 = r1.obtainWebView()
            android.view.ViewGroup r1 = r1.obtainWindowView()
            boolean r3 = r11.d
            r1.setHorizontalScrollBarEnabled(r3)
        L_0x002f:
            android.view.View r1 = r11.u
            if (r1 == 0) goto L_0x0068
            boolean r3 = r1 instanceof com.dcloud.android.widget.AbsoluteLayout
            if (r3 == 0) goto L_0x0068
            com.dcloud.android.widget.AbsoluteLayout r1 = (com.dcloud.android.widget.AbsoluteLayout) r1
            io.dcloud.common.core.ui.g r1 = r1.getDrag()
            r1.c((boolean) r2)
            io.dcloud.common.core.ui.b r1 = r11.s
            boolean r1 = r11.a((io.dcloud.common.core.ui.b) r1)
            if (r1 != 0) goto L_0x0068
            io.dcloud.common.core.ui.b r1 = r11.s
            io.dcloud.common.DHInterface.IWebview r1 = r1.obtainWebView()
            android.view.ViewGroup r1 = r1.obtainWindowView()
            boolean r3 = r11.e
            r1.setVerticalScrollBarEnabled(r3)
            io.dcloud.common.core.ui.b r1 = r11.s
            io.dcloud.common.DHInterface.IWebview r1 = r1.obtainWebView()
            android.webkit.WebView r1 = r1.obtainWebview()
            if (r1 == 0) goto L_0x0068
            boolean r3 = r11.f
            r1.setHorizontalScrollbarOverlay(r3)
        L_0x0068:
            io.dcloud.common.DHInterface.IFrameView r1 = r11.K
            if (r1 == 0) goto L_0x0188
            java.lang.String r1 = r11.L
            boolean r1 = android.text.TextUtils.isEmpty(r1)
            if (r1 != 0) goto L_0x0188
            boolean r1 = android.text.TextUtils.isEmpty(r12)
            if (r1 != 0) goto L_0x0188
            android.view.View r1 = r11.u
            r3 = 0
            if (r1 == 0) goto L_0x009f
            boolean r4 = r1 instanceof io.dcloud.common.DHInterface.INativeView
            if (r4 == 0) goto L_0x008a
            io.dcloud.common.DHInterface.INativeView r1 = (io.dcloud.common.DHInterface.INativeView) r1
            java.lang.String r1 = r1.getViewId()
            goto L_0x00a0
        L_0x008a:
            io.dcloud.common.core.ui.b r1 = r11.s
            if (r1 == 0) goto L_0x009f
            io.dcloud.common.DHInterface.IWebview r1 = r1.obtainWebView()
            if (r1 == 0) goto L_0x009f
            io.dcloud.common.core.ui.b r1 = r11.s
            io.dcloud.common.DHInterface.IWebview r1 = r1.obtainWebView()
            java.lang.String r1 = r1.obtainFrameId()
            goto L_0x00a0
        L_0x009f:
            r1 = r3
        L_0x00a0:
            io.dcloud.common.core.ui.b r4 = r11.r
            if (r4 == 0) goto L_0x00b4
            io.dcloud.common.DHInterface.IWebview r4 = r4.obtainWebView()
            if (r4 == 0) goto L_0x00b4
            io.dcloud.common.core.ui.b r3 = r11.r
            io.dcloud.common.DHInterface.IWebview r3 = r3.obtainWebView()
            java.lang.String r3 = r3.obtainFrameId()
        L_0x00b4:
            if (r13 == 0) goto L_0x00b9
            if (r14 == 0) goto L_0x00bb
            goto L_0x00bd
        L_0x00b9:
            if (r14 == 0) goto L_0x00bd
        L_0x00bb:
            r14 = r1
            goto L_0x00be
        L_0x00bd:
            r14 = r3
        L_0x00be:
            io.dcloud.common.core.ui.b r4 = r11.s
            boolean r4 = r11.a((io.dcloud.common.core.ui.b) r4)
            if (r4 != 0) goto L_0x00e0
            io.dcloud.common.core.ui.b r4 = r11.s
            boolean r4 = r4.isWebviewCovered()
            if (r4 == 0) goto L_0x00e0
            io.dcloud.common.core.ui.b r4 = r11.r
            boolean r4 = r11.a((io.dcloud.common.core.ui.b) r4)
            if (r4 != 0) goto L_0x0101
            io.dcloud.common.core.ui.b r4 = r11.r
            boolean r4 = r4.isWebviewCovered()
            if (r4 != 0) goto L_0x0101
            r14 = r3
            goto L_0x0101
        L_0x00e0:
            io.dcloud.common.core.ui.b r4 = r11.r
            boolean r4 = r11.a((io.dcloud.common.core.ui.b) r4)
            if (r4 != 0) goto L_0x0101
            io.dcloud.common.core.ui.b r4 = r11.r
            boolean r4 = r4.isWebviewCovered()
            if (r4 == 0) goto L_0x0101
            io.dcloud.common.core.ui.b r4 = r11.s
            boolean r4 = r11.a((io.dcloud.common.core.ui.b) r4)
            if (r4 != 0) goto L_0x0101
            io.dcloud.common.core.ui.b r4 = r11.s
            boolean r4 = r4.isWebviewCovered()
            if (r4 != 0) goto L_0x0101
            r14 = r1
        L_0x0101:
            boolean r4 = android.text.TextUtils.isEmpty(r14)
            if (r4 == 0) goto L_0x0109
            java.lang.String r14 = "undefined"
        L_0x0109:
            boolean r4 = android.text.TextUtils.isEmpty(r3)
            java.lang.String r5 = ""
            if (r4 == 0) goto L_0x0112
            r3 = r5
        L_0x0112:
            boolean r4 = android.text.TextUtils.isEmpty(r1)
            if (r4 == 0) goto L_0x0119
            r1 = r5
        L_0x0119:
            java.lang.String r4 = r11.M
            java.lang.String r6 = "left"
            boolean r4 = r6.equals(r4)
            if (r4 == 0) goto L_0x0126
            java.lang.String r5 = "rtl"
            goto L_0x0132
        L_0x0126:
            java.lang.String r4 = r11.M
            java.lang.String r6 = "right"
            boolean r4 = r6.equals(r4)
            if (r4 == 0) goto L_0x0132
            java.lang.String r5 = "ltr"
        L_0x0132:
            io.dcloud.common.DHInterface.IFrameView r4 = r11.K
            io.dcloud.common.DHInterface.IWebview r4 = r4.obtainWebView()
            if (r4 == 0) goto L_0x0188
            boolean r0 = r0.equals(r12)
            r4 = 5
            r6 = 4
            r7 = 3
            r8 = 2
            r9 = 1
            r10 = 6
            if (r0 == 0) goto L_0x0163
            r0 = 7
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r0[r2] = r12
            java.lang.Boolean r12 = java.lang.Boolean.valueOf(r13)
            r0[r9] = r12
            r0[r8] = r14
            r0[r7] = r3
            r0[r6] = r1
            r0[r4] = r5
            r0[r10] = r15
            java.lang.String r12 = "{\"type\":\"%s\",\"result\":%b,\"id\":\"%s\",\"targetId\":\"%s\",\"otherId\":\"%s\",\"direction\":\"%s\",\"progress\":\"%s\"}"
            java.lang.String r12 = io.dcloud.common.util.StringUtil.format(r12, r0)
            goto L_0x0178
        L_0x0163:
            java.lang.Object[] r13 = new java.lang.Object[r10]
            r13[r2] = r12
            r13[r9] = r14
            r13[r8] = r3
            r13[r7] = r1
            r13[r6] = r5
            r13[r4] = r15
            java.lang.String r12 = "{\"type\":\"%s\",\"id\":\"%s\",\"targetId\":\"%s\",\"otherId\":\"%s\",\"direction\":\"%s\",\"progress\":\"%s\"}"
            java.lang.String r12 = io.dcloud.common.util.StringUtil.format(r12, r13)
        L_0x0178:
            r2 = r12
            io.dcloud.common.DHInterface.IFrameView r12 = r11.K
            io.dcloud.common.DHInterface.IWebview r0 = r12.obtainWebView()
            java.lang.String r1 = r11.L
            int r3 = io.dcloud.common.util.JSUtil.OK
            r4 = 1
            r5 = 1
            io.dcloud.common.util.Deprecated_JSUtil.execCallback(r0, r1, r2, r3, r4, r5)
        L_0x0188:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.core.ui.g.a(java.lang.String, boolean, boolean, java.lang.String):void");
    }

    public View a(String str) {
        DragBean dragBean;
        IFrameView iFrameView;
        View obtainMainView;
        String c2 = c(str);
        HashMap<String, DragBean> hashMap = this.r.obtainFrameOptions().dragData;
        if (hashMap == null || !hashMap.containsKey(c2) || (dragBean = hashMap.get(c2)) == null || (iFrameView = dragBean.dragBindWebView) == null || !"follow".equalsIgnoreCase(JSONUtil.getString(dragBean.dragBindViewOp, "moveMode")) || (obtainMainView = iFrameView.obtainMainView()) == null || obtainMainView.getVisibility() != 0 || obtainMainView.getWidth() >= this.R) {
            return null;
        }
        int a2 = a(obtainMainView);
        int width = obtainMainView.getWidth() + a2;
        if ((a2 < 0 || a2 >= this.R) && (width <= 0 || width > this.R)) {
            return null;
        }
        return obtainMainView;
    }

    private float a(float f2) {
        if (!"bounce".equalsIgnoreCase(this.E) || 0.0f == f2) {
            return f2;
        }
        boolean z2 = f2 < 0.0f;
        float floatValue = new BigDecimal((double) f2).multiply(new BigDecimal(Math.abs(this.o - a(this.t))).divide(new BigDecimal(this.o - this.n), 4, 4)).floatValue();
        if (z2) {
            floatValue = -floatValue;
        }
        return z2 ? Math.min(floatValue, -2.0f) : Math.max(floatValue, 2.0f);
    }

    /* access modifiers changed from: private */
    public int a(View view) {
        if (view == null) {
            return 0;
        }
        if (view instanceof INativeView) {
            return ((INativeView) view).getStyleLeft();
        }
        if (view.getLayoutParams() instanceof AbsoluteLayout.LayoutParams) {
            return (int) ViewHelper.getX(view);
        }
        if (view.getLayoutParams() instanceof FrameLayout.LayoutParams) {
            return (int) ViewHelper.getX(view);
        }
        return 0;
    }

    private boolean a(b bVar) {
        if (bVar == null) {
            return true;
        }
        if (bVar != null && bVar.obtainWebView() == null) {
            return true;
        }
        if (bVar == null || bVar.obtainMainView() != null) {
            return bVar != null && bVar.obtainWebView() == null && bVar.obtainMainView() == null;
        }
        return true;
    }
}
