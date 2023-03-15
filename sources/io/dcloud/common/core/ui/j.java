package io.dcloud.common.core.ui;

import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import androidx.core.view.VelocityTrackerCompat;
import com.dcloud.android.v4.view.MotionEventCompat;
import com.dcloud.android.v4.widget.ScrollerCompat;
import com.dcloud.android.widget.AbsoluteLayout;
import com.taobao.weex.el.parse.Operators;
import java.util.Arrays;

public class j {
    private static final Interpolator a = new a();
    private int A;
    private int b;
    private int c;
    private int d;
    private float[] e;
    private float[] f;
    private float[] g;
    private float[] h;
    private int[] i;
    private int[] j;
    private int[] k;
    private int l;
    private VelocityTracker m;
    private float n;
    private float o;
    private int p;
    private int q;
    private ScrollerCompat r;
    private final c s;
    private View t;
    private boolean u;
    private final ViewGroup v;
    private int w;
    private b x;
    private a y;
    private final Runnable z;

    class a implements Interpolator {
        a() {
        }

        public float getInterpolation(float f) {
            float f2 = f - 1.0f;
            return (f2 * f2 * f2 * f2 * f2) + 1.0f;
        }
    }

    class b implements Runnable {
        b() {
        }

        public void run() {
            j.this.d(0);
        }
    }

    public static abstract class c {
        public int a(int i) {
            return i;
        }

        public abstract int a(View view);

        public abstract int a(View view, int i, int i2);

        public void a(int i, int i2) {
        }

        public abstract void a(View view, float f, float f2);

        public void a(View view, int i) {
        }

        public void a(View view, int i, int i2, int i3, int i4) {
        }

        public abstract boolean a(b bVar);

        public abstract int b(View view);

        public void b(int i, int i2) {
        }

        public boolean b(int i) {
            return false;
        }

        public abstract boolean b(View view, int i);

        public void c(int i) {
        }
    }

    public j(ViewGroup viewGroup, c cVar, a aVar) {
        this(viewGroup.getContext(), viewGroup, cVar, aVar);
    }

    public void a(View view, int i2) {
        if (view.getParent() == this.v) {
            this.t = view;
            this.d = i2;
            this.s.a(view, i2);
            d(1);
            return;
        }
        throw new IllegalArgumentException("captureChildView: parameter must be a descendant of the SwipeBackHelper's tracked parent view (" + this.v + Operators.BRACKET_END_STR);
    }

    public void b(float f2) {
        this.o = f2;
    }

    public int c() {
        return this.b;
    }

    /* access modifiers changed from: package-private */
    public void d(int i2) {
        if (this.b != i2) {
            this.b = i2;
            this.s.c(i2);
            if (i2 == 0) {
                this.t = null;
            }
        }
    }

    public void e(int i2) {
        this.p = i2;
    }

    public void f(int i2) {
        this.q = i2;
    }

    public j(Context context, ViewGroup viewGroup, c cVar, a aVar) {
        this.d = -1;
        this.w = 170;
        this.y = null;
        this.z = new b();
        this.A = 0;
        if (viewGroup == null) {
            throw new IllegalArgumentException("Parent view may not be null");
        } else if (cVar != null) {
            this.y = aVar;
            this.v = viewGroup;
            this.s = cVar;
            ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
            this.p = (int) ((context.getResources().getDisplayMetrics().density * 20.0f) + 0.5f);
            this.c = viewConfiguration.getScaledTouchSlop();
            this.n = (float) viewConfiguration.getScaledMaximumFlingVelocity();
            this.o = (float) viewConfiguration.getScaledMinimumFlingVelocity();
            this.r = ScrollerCompat.create(context, a);
        } else {
            throw new IllegalArgumentException("Callback may not be null");
        }
    }

    private boolean b(int i2, int i3, int i4, int i5) {
        int left = this.t.getLeft();
        int top = this.t.getTop();
        int i6 = i2 - left;
        int i7 = i3 - top;
        if (i6 == 0 && i7 == 0) {
            this.r.abortAnimation();
            d(0);
            return false;
        }
        this.r.startScroll(left, top, i6, i7, a(this.t, i6, i7, i4, i5));
        d(2);
        return true;
    }

    public boolean c(int i2) {
        return ((1 << i2) & this.l) != 0;
    }

    public boolean e(int i2, int i3) {
        if (this.u) {
            return b(i2, i3, (int) VelocityTrackerCompat.getXVelocity(this.m, this.d), (int) VelocityTrackerCompat.getYVelocity(this.m, this.d));
        }
        throw new IllegalStateException("Cannot settleCapturedViewAt outside of a call to Callback#onViewReleased");
    }

    public boolean c(MotionEvent motionEvent) {
        b bVar;
        View a2;
        View a3;
        int actionMasked = MotionEventCompat.getActionMasked(motionEvent);
        int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
        if (actionMasked == 0) {
            a();
        }
        if (this.m == null) {
            this.m = VelocityTracker.obtain();
        }
        this.m.addMovement(motionEvent);
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked != 2) {
                    if (actionMasked != 3) {
                        if (actionMasked == 5) {
                            this.A = 0;
                            int pointerId = MotionEventCompat.getPointerId(motionEvent, actionIndex);
                            float x2 = MotionEventCompat.getX(motionEvent, actionIndex);
                            float y2 = MotionEventCompat.getY(motionEvent, actionIndex);
                            b(x2, y2, pointerId);
                            int i2 = this.b;
                            if (i2 == 0) {
                                int i3 = this.i[pointerId] & this.q;
                                if (i3 != 0) {
                                    this.s.b(i3, pointerId);
                                }
                            } else if (i2 == 2 && (a3 = a((int) x2, (int) y2)) == this.t) {
                                b(a3, pointerId);
                            }
                        } else if (actionMasked == 6) {
                            this.A = 6;
                            a(MotionEventCompat.getPointerId(motionEvent, actionIndex));
                        }
                    }
                } else if (motionEvent.getX() > ((float) this.w) || (bVar = this.x) == null || bVar.obtainMainView() == null) {
                    return false;
                } else {
                    int i4 = this.A;
                    if ((i4 == 0 || i4 == 6) && !this.s.a(this.x)) {
                        return false;
                    }
                    this.A = 2;
                    int pointerCount = MotionEventCompat.getPointerCount(motionEvent);
                    int i5 = 0;
                    while (i5 < pointerCount) {
                        int pointerId2 = MotionEventCompat.getPointerId(motionEvent, i5);
                        float x3 = MotionEventCompat.getX(motionEvent, i5);
                        float y3 = MotionEventCompat.getY(motionEvent, i5);
                        float f2 = x3 - this.e[pointerId2];
                        float f3 = y3 - this.f[pointerId2];
                        if (Math.abs(f2) >= Math.abs(f3) && f2 >= 0.0f) {
                            a(f2, f3, pointerId2);
                            if (this.b == 1 || ((a2 = a((int) x3, (int) y3)) != null && a(a2, f2, f3) && b(a2, pointerId2))) {
                                break;
                            }
                            i5++;
                        } else {
                            return false;
                        }
                    }
                    b(motionEvent);
                }
            }
            this.A = 1;
            a();
        } else {
            this.A = 0;
            float x4 = motionEvent.getX();
            float y4 = motionEvent.getY();
            int pointerId3 = MotionEventCompat.getPointerId(motionEvent, 0);
            b(x4, y4, pointerId3);
            View a4 = a((int) x4, (int) y4);
            if (a4 == this.t && this.b == 2) {
                b(a4, pointerId3);
            }
            int i6 = this.i[pointerId3] & this.q;
            if (i6 != 0) {
                this.s.b(i6, pointerId3);
            }
        }
        if (this.b == 1) {
            return true;
        }
        return false;
    }

    public boolean d(int i2, int i3) {
        return c(i3) && (i2 & this.i[i3]) != 0;
    }

    private void d() {
        this.m.computeCurrentVelocity(1000, this.n);
        a(a(VelocityTrackerCompat.getXVelocity(this.m, this.d), this.o, this.n), a(VelocityTrackerCompat.getYVelocity(this.m, this.d), this.o, this.n));
    }

    public void a() {
        this.d = -1;
        b();
        VelocityTracker velocityTracker = this.m;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.m = null;
        }
    }

    private int a(View view, int i2, int i3, int i4, int i5) {
        float f2;
        float f3;
        float f4;
        float f5;
        int a2 = a(i4, (int) this.o, (int) this.n);
        int a3 = a(i5, (int) this.o, (int) this.n);
        int abs = Math.abs(i2);
        int abs2 = Math.abs(i3);
        int abs3 = Math.abs(a2);
        int abs4 = Math.abs(a3);
        int i6 = abs3 + abs4;
        int i7 = abs + abs2;
        if (a2 != 0) {
            f3 = (float) abs3;
            f2 = (float) i6;
        } else {
            f3 = (float) abs;
            f2 = (float) i7;
        }
        float f6 = f3 / f2;
        if (a3 != 0) {
            f5 = (float) abs4;
            f4 = (float) i6;
        } else {
            f5 = (float) abs2;
            f4 = (float) i7;
        }
        float f7 = f5 / f4;
        return (int) ((((float) b(i2, a2, this.s.a(view))) * f6) + (((float) b(i3, a3, this.s.b(view))) * f7));
    }

    private int b(int i2, int i3, int i4) {
        int i5;
        if (i2 == 0) {
            return 0;
        }
        int width = this.v.getWidth();
        float f2 = (float) (width / 2);
        float a2 = f2 + (a(Math.min(1.0f, ((float) Math.abs(i2)) / ((float) width))) * f2);
        int abs = Math.abs(i3);
        if (abs > 0) {
            i5 = Math.round(Math.abs(a2 / ((float) abs)) * 1000.0f) * 4;
        } else {
            i5 = (int) (((((float) Math.abs(i2)) / ((float) i4)) + 1.0f) * 256.0f);
        }
        return Math.min(i5, 600);
    }

    private int a(int i2, int i3, int i4) {
        int abs = Math.abs(i2);
        if (abs < i3) {
            return 0;
        }
        if (abs > i4) {
            return i2 > 0 ? i4 : -i4;
        }
        return i2;
    }

    private float a(float f2, float f3, float f4) {
        float abs = Math.abs(f2);
        if (abs < f3) {
            return 0.0f;
        }
        if (abs > f4) {
            return f2 > 0.0f ? f4 : -f4;
        }
        return f2;
    }

    private void b() {
        float[] fArr = this.e;
        if (fArr != null) {
            Arrays.fill(fArr, 0.0f);
            Arrays.fill(this.f, 0.0f);
            Arrays.fill(this.g, 0.0f);
            Arrays.fill(this.h, 0.0f);
            Arrays.fill(this.i, 0);
            Arrays.fill(this.j, 0);
            Arrays.fill(this.k, 0);
            this.l = 0;
        }
    }

    private float a(float f2) {
        double d2 = (double) (f2 - 0.5f);
        Double.isNaN(d2);
        return (float) Math.sin((double) ((float) (d2 * 0.4712389167638204d)));
    }

    public boolean a(boolean z2) {
        if (this.b == 2) {
            boolean computeScrollOffset = this.r.computeScrollOffset();
            int currX = this.r.getCurrX();
            int currY = this.r.getCurrY();
            int left = currX - this.t.getLeft();
            int top = currY - this.t.getTop();
            if (left != 0) {
                this.t.offsetLeftAndRight(left);
            }
            if (!(left == 0 && top == 0)) {
                this.s.a(this.t, currX, currY, left, top);
            }
            if (computeScrollOffset && currX == this.r.getFinalX() && currY == this.r.getFinalY()) {
                this.r.abortAnimation();
                computeScrollOffset = this.r.isFinished();
            }
            if (!computeScrollOffset) {
                if (z2) {
                    this.v.post(this.z);
                } else {
                    d(0);
                }
            }
        }
        if (this.b == 2) {
            return true;
        }
        return false;
    }

    private void b(int i2) {
        float[] fArr = this.e;
        if (fArr == null || fArr.length <= i2) {
            int i3 = i2 + 1;
            float[] fArr2 = new float[i3];
            float[] fArr3 = new float[i3];
            float[] fArr4 = new float[i3];
            float[] fArr5 = new float[i3];
            int[] iArr = new int[i3];
            int[] iArr2 = new int[i3];
            int[] iArr3 = new int[i3];
            if (fArr != null) {
                System.arraycopy(fArr, 0, fArr2, 0, fArr.length);
                float[] fArr6 = this.f;
                System.arraycopy(fArr6, 0, fArr3, 0, fArr6.length);
                float[] fArr7 = this.g;
                System.arraycopy(fArr7, 0, fArr4, 0, fArr7.length);
                float[] fArr8 = this.h;
                System.arraycopy(fArr8, 0, fArr5, 0, fArr8.length);
                int[] iArr4 = this.i;
                System.arraycopy(iArr4, 0, iArr, 0, iArr4.length);
                int[] iArr5 = this.j;
                System.arraycopy(iArr5, 0, iArr2, 0, iArr5.length);
                int[] iArr6 = this.k;
                System.arraycopy(iArr6, 0, iArr3, 0, iArr6.length);
            }
            this.e = fArr2;
            this.f = fArr3;
            this.g = fArr4;
            this.h = fArr5;
            this.i = iArr;
            this.j = iArr2;
            this.k = iArr3;
        }
    }

    private void a(float f2, float f3) {
        this.u = true;
        this.s.a(this.t, f2, f3);
        this.u = false;
        if (this.b == 1) {
            d(0);
        }
    }

    private void b(float f2, float f3, int i2) {
        b(i2);
        float[] fArr = this.e;
        this.g[i2] = f2;
        fArr[i2] = f2;
        float[] fArr2 = this.f;
        this.h[i2] = f3;
        fArr2[i2] = f3;
        this.i[i2] = b((int) f2, (int) f3);
        this.l |= 1 << i2;
        this.x = this.y.i();
    }

    private void a(int i2) {
        float[] fArr = this.e;
        if (fArr != null) {
            fArr[i2] = 0.0f;
            this.f[i2] = 0.0f;
            this.g[i2] = 0.0f;
            this.h[i2] = 0.0f;
            this.i[i2] = 0;
            this.j[i2] = 0;
            this.k[i2] = 0;
            this.l = ((1 << i2) ^ -1) & this.l;
        }
    }

    private void b(MotionEvent motionEvent) {
        int pointerCount = MotionEventCompat.getPointerCount(motionEvent);
        for (int i2 = 0; i2 < pointerCount; i2++) {
            int pointerId = MotionEventCompat.getPointerId(motionEvent, i2);
            float x2 = MotionEventCompat.getX(motionEvent, i2);
            float y2 = MotionEventCompat.getY(motionEvent, i2);
            this.g[pointerId] = x2;
            this.h[pointerId] = y2;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean b(View view, int i2) {
        if (view == this.t && this.d == i2) {
            return true;
        }
        if (view == null || !this.s.b(view, i2)) {
            return false;
        }
        this.d = i2;
        a(view, i2);
        return true;
    }

    public void a(MotionEvent motionEvent) {
        int i2;
        int actionMasked = MotionEventCompat.getActionMasked(motionEvent);
        int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
        if (actionMasked == 0) {
            a();
        }
        if (this.m == null) {
            this.m = VelocityTracker.obtain();
        }
        this.m.addMovement(motionEvent);
        int i3 = 0;
        if (actionMasked == 0) {
            float x2 = motionEvent.getX();
            float y2 = motionEvent.getY();
            int pointerId = MotionEventCompat.getPointerId(motionEvent, 0);
            View a2 = a((int) x2, (int) y2);
            b(x2, y2, pointerId);
            b(a2, pointerId);
            int i4 = this.i[pointerId] & this.q;
            if (i4 != 0) {
                this.s.b(i4, pointerId);
            }
        } else if (actionMasked == 1) {
            if (this.b == 1) {
                d();
            }
            a();
        } else if (actionMasked == 2) {
            b bVar = this.x;
            if (bVar != null && bVar.obtainMainView() != null) {
                if (this.b == 1) {
                    int findPointerIndex = MotionEventCompat.findPointerIndex(motionEvent, this.d);
                    float x3 = MotionEventCompat.getX(motionEvent, findPointerIndex);
                    float y3 = MotionEventCompat.getY(motionEvent, findPointerIndex);
                    float[] fArr = this.g;
                    int i5 = this.d;
                    int i6 = (int) (x3 - fArr[i5]);
                    int i7 = (int) (y3 - this.h[i5]);
                    a(this.t.getLeft() + i6, this.t.getTop() + i7, i6, i7);
                    b(motionEvent);
                    return;
                }
                int pointerCount = MotionEventCompat.getPointerCount(motionEvent);
                while (i3 < pointerCount) {
                    int pointerId2 = MotionEventCompat.getPointerId(motionEvent, i3);
                    float x4 = MotionEventCompat.getX(motionEvent, i3);
                    float y4 = MotionEventCompat.getY(motionEvent, i3);
                    float f2 = x4 - this.e[pointerId2];
                    float f3 = y4 - this.f[pointerId2];
                    a(f2, f3, pointerId2);
                    if (this.b != 1) {
                        View a3 = a((int) x4, (int) y4);
                        if (a(a3, f2, f3) && b(a3, pointerId2)) {
                            break;
                        }
                        i3++;
                    } else {
                        break;
                    }
                }
                b(motionEvent);
            }
        } else if (actionMasked == 3) {
            if (this.b == 1) {
                a(0.0f, 0.0f);
            }
            a();
        } else if (actionMasked == 5) {
            int pointerId3 = MotionEventCompat.getPointerId(motionEvent, actionIndex);
            float x5 = MotionEventCompat.getX(motionEvent, actionIndex);
            float y5 = MotionEventCompat.getY(motionEvent, actionIndex);
            b(x5, y5, pointerId3);
            if (this.b == 0) {
                b(a((int) x5, (int) y5), pointerId3);
                int i8 = this.i[pointerId3] & this.q;
                if (i8 != 0) {
                    this.s.b(i8, pointerId3);
                }
            } else if (c((int) x5, (int) y5)) {
                b(this.t, pointerId3);
            }
        } else if (actionMasked == 6) {
            int pointerId4 = MotionEventCompat.getPointerId(motionEvent, actionIndex);
            if (this.b == 1 && pointerId4 == this.d) {
                int pointerCount2 = MotionEventCompat.getPointerCount(motionEvent);
                while (true) {
                    if (i3 >= pointerCount2) {
                        i2 = -1;
                        break;
                    }
                    int pointerId5 = MotionEventCompat.getPointerId(motionEvent, i3);
                    if (pointerId5 != this.d) {
                        View a4 = a((int) MotionEventCompat.getX(motionEvent, i3), (int) MotionEventCompat.getY(motionEvent, i3));
                        View view = this.t;
                        if (a4 == view && b(view, pointerId5)) {
                            i2 = this.d;
                            break;
                        }
                    }
                    i3++;
                }
                if (i2 == -1) {
                    d();
                }
            }
            a(pointerId4);
        }
    }

    private int b(int i2, int i3) {
        int i4 = i2 < this.v.getLeft() + this.p ? 1 : 0;
        if (i3 < this.v.getTop() + this.p) {
            i4 |= 4;
        }
        if (i2 > this.v.getRight() - this.p) {
            i4 |= 2;
        }
        return i3 > this.v.getBottom() - this.p ? i4 | 8 : i4;
    }

    public boolean c(int i2, int i3) {
        return a(this.t, i2, i3);
    }

    private void a(float f2, float f3, int i2) {
        boolean a2 = a(f2, f3, i2, 1);
        if (a(f3, f2, i2, 4)) {
            a2 |= true;
        }
        if (a(f2, f3, i2, 2)) {
            a2 |= true;
        }
        if (a(f3, f2, i2, 8)) {
            a2 |= true;
        }
        if (a2) {
            int[] iArr = this.j;
            iArr[i2] = iArr[i2] | a2;
            this.s.a(a2 ? 1 : 0, i2);
        }
    }

    private boolean a(float f2, float f3, int i2, int i3) {
        float abs = Math.abs(f2);
        float abs2 = Math.abs(f3);
        if ((this.i[i2] & i3) != i3 || (this.q & i3) == 0 || (this.k[i2] & i3) == i3 || (this.j[i2] & i3) == i3) {
            return false;
        }
        float f4 = (float) this.c;
        if (abs <= f4 && abs2 <= f4) {
            return false;
        }
        if (abs < abs2 * 0.5f && this.s.b(i3)) {
            int[] iArr = this.k;
            iArr[i2] = iArr[i2] | i3;
            return false;
        } else if ((this.j[i2] & i3) != 0 || abs <= ((float) this.c)) {
            return false;
        } else {
            return true;
        }
    }

    private boolean a(View view, float f2, float f3) {
        if (view == null) {
            return false;
        }
        boolean z2 = this.s.a(view) > 0;
        boolean z3 = this.s.b(view) > 0;
        if (z2 && z3) {
            int i2 = this.c;
            if ((f2 * f2) + (f3 * f3) > ((float) (i2 * i2))) {
                return true;
            }
            return false;
        } else if (z2) {
            if (Math.abs(f2) > ((float) this.c)) {
                return true;
            }
            return false;
        } else if (!z3 || Math.abs(f3) <= ((float) this.c)) {
            return false;
        } else {
            return true;
        }
    }

    private void a(int i2, int i3, int i4, int i5) {
        int left = this.t.getLeft();
        int top = this.t.getTop();
        if (i4 != 0) {
            i2 = this.s.a(this.t, i2, i4);
            this.t.offsetLeftAndRight(i2 - left);
        }
        int i6 = i2;
        if (i4 != 0 || i5 != 0) {
            this.s.a(this.t, i6, i3, i6 - left, i3 - top);
        }
    }

    public boolean a(View view, int i2, int i3) {
        if (view != null && i2 >= view.getLeft() && i2 < view.getRight() && i3 >= view.getTop() && i3 < view.getBottom()) {
            return true;
        }
        return false;
    }

    public View a(int i2, int i3) {
        b bVar = this.x;
        if (bVar != null && bVar.obtainMainView() != null) {
            return this.x.obtainMainView();
        }
        for (int childCount = this.v.getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = this.v.getChildAt(this.s.a(childCount));
            if ((childAt instanceof AbsoluteLayout) && i2 >= childAt.getLeft() && i2 < childAt.getRight() && i3 >= childAt.getTop() && i3 < childAt.getBottom()) {
                return childAt;
            }
        }
        return null;
    }
}
