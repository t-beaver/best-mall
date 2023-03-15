package io.dcloud.feature.gallery.imageedit.c.g;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import io.dcloud.feature.gallery.imageedit.c.g.a;
import java.lang.reflect.Array;

public class b implements a {
    RectF e = new RectF();
    RectF f = new RectF();
    RectF g = new RectF();
    RectF h = new RectF();
    RectF i = new RectF();
    float[] j = new float[16];
    float[] k = new float[32];
    float[][] l = ((float[][]) Array.newInstance(float.class, new int[]{2, 4}));
    private boolean m = false;
    private boolean n = true;
    boolean o = false;
    private boolean p = false;
    Matrix q = new Matrix();
    Path r = new Path();
    Paint s;
    public int t;
    private float[] u;
    private boolean v;

    public b() {
        Paint paint = new Paint(1);
        this.s = paint;
        paint.setStyle(Paint.Style.STROKE);
        this.s.setStrokeCap(Paint.Cap.SQUARE);
        this.t = 0;
        this.u = new float[]{1.0f, 1.0f};
        this.v = false;
    }

    public void a(RectF rectF, float f2) {
        RectF rectF2 = new RectF();
        this.q.setRotate(f2, rectF.centerX(), rectF.centerY());
        this.q.mapRect(rectF2, rectF);
        c(rectF2.width(), rectF2.height());
    }

    public boolean b() {
        this.f.set(this.e);
        this.g.set(this.e);
        io.dcloud.feature.gallery.imageedit.c.k.b.a(this.h, this.g, 60.0f);
        boolean z = !this.g.equals(this.f);
        this.p = z;
        return z;
    }

    /* access modifiers changed from: package-private */
    public void c(float f2, float f3) {
        c(true);
        if (this.v) {
            float[] fArr = this.u;
            if ((f2 / fArr[0]) * fArr[1] > f3) {
                f2 = (f3 / fArr[1]) * fArr[0];
            } else {
                f3 = (f2 / fArr[0]) * fArr[1];
            }
        }
        this.e.set(0.0f, 0.0f, f2, f3);
        io.dcloud.feature.gallery.imageedit.c.k.b.a(this.h, this.e, 60.0f);
        this.g.set(this.e);
    }

    public void d(float f2, float f3) {
        this.i.set(0.0f, 0.0f, f2, f3);
        this.h.set(0.0f, (float) this.t, f2, f3 * 0.85f);
        if (!this.e.isEmpty()) {
            io.dcloud.feature.gallery.imageedit.c.k.b.a(this.h, this.e);
            this.g.set(this.e);
        }
    }

    public void e(float f2, float f3) {
        if (f2 > 0.0f && f3 > 0.0f) {
            this.u = new float[]{f2, f3};
            this.v = true;
        }
    }

    public boolean f() {
        return this.n;
    }

    public boolean e() {
        return this.v;
    }

    public void a(float f2) {
        if (this.p) {
            RectF rectF = this.e;
            RectF rectF2 = this.f;
            float f3 = rectF2.left;
            RectF rectF3 = this.g;
            float f4 = f3 + ((rectF3.left - f3) * f2);
            float f5 = rectF2.top;
            float f6 = f5 + ((rectF3.top - f5) * f2);
            float f7 = rectF2.right;
            float f8 = rectF2.bottom;
            rectF.set(f4, f6, f7 + ((rectF3.right - f7) * f2), f8 + ((rectF3.bottom - f8) * f2));
        }
    }

    public void b(boolean z) {
        this.p = z;
    }

    public RectF b(float f2, float f3) {
        RectF rectF = new RectF(this.e);
        rectF.offset(f2, f3);
        return rectF;
    }

    public void a(boolean z) {
        this.m = z;
    }

    public boolean d() {
        return this.p;
    }

    public RectF a() {
        return this.g;
    }

    public void d(boolean z) {
        this.o = z;
    }

    public void a(Canvas canvas) {
        if (!this.n) {
            int i2 = 0;
            float[] fArr = {this.e.width(), this.e.height()};
            for (int i3 = 0; i3 < this.l.length; i3++) {
                int i4 = 0;
                while (true) {
                    float[][] fArr2 = this.l;
                    if (i4 >= fArr2[i3].length) {
                        break;
                    }
                    fArr2[i3][i4] = fArr[i3] * a.a[i4];
                    i4++;
                }
            }
            int i5 = 0;
            while (true) {
                float[] fArr3 = this.j;
                if (i5 >= fArr3.length) {
                    break;
                }
                fArr3[i5] = this.l[i5 & 1][(1935858840 >>> (i5 << 1)) & 3];
                i5++;
            }
            while (true) {
                float[] fArr4 = this.k;
                if (i2 < fArr4.length) {
                    float f2 = this.l[i2 & 1][(179303760 >>> i2) & 1];
                    float[] fArr5 = a.c;
                    byte[] bArr = a.d;
                    fArr4[i2] = f2 + fArr5[bArr[i2] & 3] + a.b[bArr[i2] >> 2];
                    i2++;
                } else {
                    RectF rectF = this.e;
                    canvas.translate(rectF.left, rectF.top);
                    this.s.setStyle(Paint.Style.STROKE);
                    this.s.setColor(-2130706433);
                    this.s.setStrokeWidth(3.0f);
                    canvas.drawLines(this.j, this.s);
                    RectF rectF2 = this.e;
                    canvas.translate(-rectF2.left, -rectF2.top);
                    this.s.setColor(-1);
                    this.s.setStrokeWidth(4.0f);
                    canvas.drawRect(this.e, this.s);
                    RectF rectF3 = this.e;
                    canvas.translate(rectF3.left, rectF3.top);
                    this.s.setColor(-1);
                    this.s.setStrokeWidth(10.0f);
                    canvas.drawLines(this.k, this.s);
                    return;
                }
            }
        }
    }

    public boolean c() {
        return this.m;
    }

    public void c(boolean z) {
        this.n = z;
    }

    public a.C0040a a(float f2, float f3) {
        if (!a.C0040a.a(this.e, -48.0f, f2, f3) || a.C0040a.a(this.e, 48.0f, f2, f3)) {
            return null;
        }
        float[] a = a.C0040a.a(this.e, 0.0f, 0.0f);
        float[] fArr = {f2, f3};
        int i2 = 0;
        for (int i3 = 0; i3 < a.length; i3++) {
            if (Math.abs(a[i3] - fArr[i3 >> 1]) < 48.0f) {
                i2 |= 1 << i3;
            }
        }
        a.C0040a a2 = a.C0040a.a(i2);
        if (a2 != null) {
            this.p = false;
        }
        return a2;
    }

    public void a(a.C0040a aVar, float f2, float f3) {
        boolean z = this.v;
        aVar.a(z ? this.g : this.h, this.e, f2, f3, this.u, z);
    }

    public void a(int i2) {
        this.t = i2;
    }
}
