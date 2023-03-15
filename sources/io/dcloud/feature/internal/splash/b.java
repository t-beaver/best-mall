package io.dcloud.feature.internal.splash;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

public class b extends View {
    private static float a = 2.0f;
    RectF A;
    Paint B = new Paint();
    Path C = new Path();
    int D = 0;
    PaintFlagsDrawFilter E = null;
    private DisplayMetrics b;
    Bitmap c = null;
    int d;
    int e;
    int f;
    int g;
    int h;
    int i;
    int j;
    int k;
    int l;
    int m;
    int n;
    int o;
    int p;
    int q;
    int r;
    int s;
    int t;
    int u;
    int v;
    int w;
    int x;
    int y;
    RectF z;

    public b(Context context, boolean z2) {
        super(context);
        if (z2) {
            a = 6.0f;
        }
        this.E = new PaintFlagsDrawFilter(0, 3);
    }

    private void b(Canvas canvas) {
        this.B.reset();
        this.B.setAntiAlias(true);
        this.B.setStyle(Paint.Style.STROKE);
        this.B.setStrokeWidth((float) this.i);
        this.B.setColor(this.x);
        canvas.drawArc(this.A, (float) this.u, (float) this.v, false, this.B);
        int i2 = (int) (((float) this.v) + a);
        this.v = i2;
        if (i2 > 360) {
            this.v = i2 - 360;
        }
        invalidate();
    }

    private void c(Canvas canvas) {
        this.B.reset();
        Bitmap bitmap = this.c;
        if (bitmap == null || bitmap.isRecycled()) {
            this.B.setColor(-1118482);
            this.B.setAntiAlias(true);
            this.B.setStyle(Paint.Style.FILL);
            RectF rectF = this.z;
            float width = rectF.left + (rectF.width() / 2.0f);
            RectF rectF2 = this.z;
            canvas.drawCircle(width, rectF2.top + (rectF2.height() / 2.0f), this.z.width() / 2.0f, this.B);
            return;
        }
        canvas.save();
        try {
            canvas.clipPath(this.C);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.B.setAntiAlias(true);
        canvas.setDrawFilter(this.E);
        canvas.drawBitmap(this.c, (Rect) null, this.z, this.B);
        canvas.restore();
        int i2 = Build.VERSION.SDK_INT > 19 ? 3 : 40;
        int i3 = (this.h * 4) + i2;
        this.B.setStrokeWidth((float) i3);
        this.B.setStyle(Paint.Style.STROKE);
        this.B.setAntiAlias(true);
        this.B.setColor(this.D);
        RectF rectF3 = this.z;
        float width2 = rectF3.left + (rectF3.width() / 2.0f);
        RectF rectF4 = this.z;
        canvas.drawCircle(width2, rectF4.top + (rectF4.height() / 2.0f), ((this.z.width() / 2.0f) + ((float) (i3 / 2))) - ((float) (i2 / 8)), this.B);
    }

    /* access modifiers changed from: package-private */
    public float a(float f2) {
        if (this.b == null) {
            this.b = getResources().getDisplayMetrics();
        }
        return TypedValue.applyDimension(1, f2, this.b);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        c(canvas);
        a(canvas);
        b(canvas);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i2, int i3) {
        super.onMeasure(i2, i3);
        setMeasuredDimension(this.d, this.e);
    }

    public void setBackgroundColor(int i2) {
        super.setBackgroundColor(i2);
        this.D = i2;
    }

    public void setBitmap(Bitmap bitmap) {
        Bitmap bitmap2 = this.c;
        if (bitmap2 != null && !bitmap2.isRecycled()) {
            this.c.recycle();
        }
        this.c = bitmap;
    }

    public void a(Bitmap bitmap, int i2, int i3, int i4, int i5, int i6) {
        int left = getLeft();
        int top = getTop();
        this.c = bitmap;
        this.d = i2;
        this.e = i3;
        this.h = i4;
        int i7 = i4 * 8;
        int i8 = i2 - i7;
        this.f = i8;
        int i9 = i3 - i7;
        this.g = i9;
        this.m = ((i2 - i8) / 2) + left;
        this.n = ((i3 - i9) / 2) + top;
        int i10 = this.m;
        int i11 = this.n;
        this.z = new RectF((float) i10, (float) i11, (float) (i10 + this.f), (float) (i11 + this.g));
        this.C.reset();
        this.C.addRoundRect(this.z, (float) (this.m + (this.f / 2)), (float) (this.n + (this.g / 2)), Path.Direction.CCW);
        int i12 = this.d;
        int i13 = i12 / 2;
        int i14 = this.h;
        this.l = i13 - i14;
        this.x = i6;
        this.y = i5;
        this.j = i13 + left;
        int i15 = this.e;
        this.k = (i15 / 2) + top;
        this.u = 270;
        this.w = 270;
        this.i = i14;
        int i16 = (i14 - i14) / 2;
        int i17 = (left + i14) - i16;
        this.o = i17;
        int i18 = (top + i14) - i16;
        this.p = i18;
        int i19 = i14 * 2;
        int i20 = i16 * 2;
        int i21 = (i12 - i19) + i20;
        this.s = i21;
        int i22 = (i15 - i19) + i20;
        this.t = i22;
        this.q = i17 + i21;
        this.r = i18 + i22;
        this.A = new RectF((float) this.o, (float) this.p, (float) this.q, (float) this.r);
    }

    private void a(Canvas canvas) {
        this.B.reset();
        this.B.setStrokeWidth((float) this.h);
        this.B.setStyle(Paint.Style.STROKE);
        this.B.setAntiAlias(true);
        this.B.setColor(this.y);
        canvas.drawCircle((float) this.j, (float) this.k, (float) this.l, this.B);
    }
}
