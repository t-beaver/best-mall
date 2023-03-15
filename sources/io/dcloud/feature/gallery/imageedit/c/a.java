package io.dcloud.feature.gallery.imageedit.c;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import io.dcloud.feature.gallery.imageedit.c.g.a;
import java.util.ArrayList;
import java.util.List;

public class a {
    private static final Bitmap a = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
    private List<c> A;
    private Paint B;
    private Paint C;
    private Paint D;
    private Matrix E;
    private List<c> F;
    private Bitmap b;
    private Bitmap c;
    private b d;
    private RectF e = new RectF();
    private RectF f = new RectF();
    private RectF g = new RectF();
    private RectF h = new RectF();
    private float i = 0.0f;
    private float j = 0.0f;
    private float k = 0.0f;
    private boolean l = false;
    private boolean m = false;
    private a.C0040a n;
    private boolean o = true;
    private Path p = new Path();
    private io.dcloud.feature.gallery.imageedit.c.g.b q = new io.dcloud.feature.gallery.imageedit.c.g.b();
    private boolean r = false;
    private b s;
    private boolean t;
    private RectF u;
    private boolean v;
    private boolean w;
    private io.dcloud.feature.gallery.imageedit.c.j.a x;
    private List<io.dcloud.feature.gallery.imageedit.c.j.a> y;
    private List<c> z;

    /* renamed from: io.dcloud.feature.gallery.imageedit.c.a$a  reason: collision with other inner class name */
    static /* synthetic */ class C0039a {
        static final /* synthetic */ int[] a;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        static {
            /*
                io.dcloud.feature.gallery.imageedit.c.b[] r0 = io.dcloud.feature.gallery.imageedit.c.b.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                a = r0
                io.dcloud.feature.gallery.imageedit.c.b r1 = io.dcloud.feature.gallery.imageedit.c.b.DOODLE     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = a     // Catch:{ NoSuchFieldError -> 0x001d }
                io.dcloud.feature.gallery.imageedit.c.b r1 = io.dcloud.feature.gallery.imageedit.c.b.MOSAIC     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.gallery.imageedit.c.a.C0039a.<clinit>():void");
        }
    }

    public interface b {
        void a();

        void b();
    }

    public interface c {
        void a();
    }

    public a() {
        b bVar = b.NONE;
        this.s = bVar;
        b bVar2 = b.CLIP;
        this.t = bVar == bVar2;
        this.u = new RectF();
        this.v = false;
        this.w = false;
        this.y = new ArrayList();
        this.z = new ArrayList();
        this.A = new ArrayList();
        this.E = new Matrix();
        this.p.setFillType(Path.FillType.WINDING);
        Paint paint = new Paint(1);
        this.B = paint;
        paint.setStyle(Paint.Style.STROKE);
        this.B.setStrokeWidth(14.0f);
        this.B.setColor(-65536);
        this.B.setPathEffect(new CornerPathEffect(14.0f));
        this.B.setStrokeCap(Paint.Cap.ROUND);
        this.B.setStrokeJoin(Paint.Join.ROUND);
        this.F = new ArrayList();
        this.b = a;
        if (this.s == bVar2) {
            g();
        }
    }

    private void b(float f2) {
        this.E.setRotate(f2, this.f.centerX(), this.f.centerY());
        for (io.dcloud.feature.gallery.imageedit.c.j.a next : this.y) {
            this.E.mapRect(next.getFrame());
            next.setRotation(next.getRotation() + f2);
            next.setX(next.getFrame().centerX() - next.getPivotX());
            next.setY(next.getFrame().centerY() - next.getPivotY());
        }
    }

    private void g() {
        if (this.D == null) {
            Paint paint = new Paint(1);
            this.D = paint;
            paint.setColor(-872415232);
            this.D.setStyle(Paint.Style.FILL);
        }
    }

    private void l() {
        Bitmap bitmap;
        if (this.c == null && (bitmap = this.b) != null && this.s == b.MOSAIC) {
            int round = Math.round(((float) bitmap.getWidth()) / 64.0f);
            int round2 = Math.round(((float) this.b.getHeight()) / 64.0f);
            int max = Math.max(round, 8);
            int max2 = Math.max(round2, 8);
            if (this.C == null) {
                Paint paint = new Paint(1);
                this.C = paint;
                paint.setFilterBitmap(false);
                this.C.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            }
            this.c = Bitmap.createScaledBitmap(this.b, max, max2, false);
        }
    }

    private void n() {
        this.v = false;
        h(this.u.width(), this.u.height());
        if (this.s == b.CLIP) {
            this.q.a(this.f, f());
        }
    }

    private void o() {
        if (this.s == b.CLIP) {
            this.q.a(this.f, f());
        }
    }

    private void v() {
        if (!this.f.isEmpty()) {
            float min = Math.min(this.u.width() / this.f.width(), this.u.height() / this.f.height());
            this.E.setScale(min, min, this.f.centerX(), this.f.centerY());
            this.E.postTranslate(this.u.centerX() - this.f.centerX(), this.u.centerY() - this.f.centerY());
            this.E.mapRect(this.e);
            this.E.mapRect(this.f);
        }
    }

    public void a(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            this.b = bitmap;
            Bitmap bitmap2 = this.c;
            if (bitmap2 != null) {
                bitmap2.recycle();
            }
            this.c = null;
            l();
            n();
        }
    }

    public b c() {
        return this.s;
    }

    public void d(io.dcloud.feature.gallery.imageedit.c.j.a aVar) {
        b(aVar);
    }

    public void e(io.dcloud.feature.gallery.imageedit.c.j.a aVar) {
        if (this.x == aVar) {
            this.x = null;
        } else {
            this.y.remove(aVar);
        }
    }

    public void f(io.dcloud.feature.gallery.imageedit.c.j.a aVar) {
        if (this.x != aVar) {
            c(aVar);
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        super.finalize();
        Bitmap bitmap = a;
        if (bitmap != null) {
            bitmap.recycle();
        }
    }

    public boolean h() {
        return this.z.isEmpty();
    }

    public boolean i() {
        return this.t;
    }

    public boolean j() {
        return this.w;
    }

    public boolean k() {
        return this.A.isEmpty();
    }

    public boolean m() {
        return this.q.b();
    }

    public void p() {
    }

    public void q() {
    }

    public void r() {
        Bitmap bitmap = this.b;
        if (bitmap != null && !bitmap.isRecycled()) {
            this.b.recycle();
        }
    }

    public void s() {
        e(d() - (d() % 360.0f));
        this.f.set(this.e);
        this.q.a(this.f, f());
    }

    public void t() {
        b(this.x);
    }

    public void u() {
        this.E.setScale(e(), e());
        Matrix matrix = this.E;
        RectF rectF = this.e;
        matrix.postTranslate(rectF.left, rectF.top);
        this.E.mapRect(this.f, this.h);
        float d2 = d() % 360.0f;
        if (Math.abs(d2) >= 180.0f) {
            d2 = 360.0f - Math.abs(d2);
        }
        c(d2);
        e(this.i);
        this.l = true;
    }

    public void w() {
        if (!this.z.isEmpty()) {
            List<c> list = this.z;
            list.remove(list.size() - 1);
        }
    }

    public void x() {
        if (!this.A.isEmpty()) {
            List<c> list = this.A;
            list.remove(list.size() - 1);
        }
    }

    private void d(float f2, float f3) {
        this.e.set(0.0f, 0.0f, (float) this.b.getWidth(), (float) this.b.getHeight());
        this.f.set(this.e);
        this.q.d(f2, f3);
        if (!this.f.isEmpty()) {
            v();
            this.v = true;
            o();
        }
    }

    public io.dcloud.feature.gallery.imageedit.c.i.a c(float f2, float f3) {
        return new io.dcloud.feature.gallery.imageedit.c.i.a(f2, f3, e(), d());
    }

    public void h(float f2, float f3) {
        if (f2 != 0.0f && f3 != 0.0f) {
            this.u.set(0.0f, 0.0f, f2, f3);
            if (!this.v) {
                d(f2, f3);
            } else {
                this.E.setTranslate(this.u.centerX() - this.f.centerX(), this.u.centerY() - this.f.centerY());
                this.E.mapRect(this.e);
                this.E.mapRect(this.f);
            }
            this.q.d(f2, f3);
            this.w = true;
            a();
        }
    }

    private void c(io.dcloud.feature.gallery.imageedit.c.j.a aVar) {
        if (aVar != null) {
            b(this.x);
            if (aVar.b()) {
                this.x = aVar;
                this.y.remove(aVar);
                return;
            }
            aVar.a();
        }
    }

    public void f(Canvas canvas) {
        if (!this.y.isEmpty()) {
            canvas.save();
            for (io.dcloud.feature.gallery.imageedit.c.j.a next : this.y) {
                if (!next.b()) {
                    float x2 = next.getX() + next.getPivotX();
                    float y2 = next.getY() + next.getPivotY();
                    canvas.save();
                    this.E.setTranslate(next.getX(), next.getY());
                    this.E.postScale(next.getScale(), next.getScale(), x2, y2);
                    this.E.postRotate(next.getRotation(), x2, y2);
                    canvas.concat(this.E);
                    next.a(canvas);
                    canvas.restore();
                }
            }
            canvas.restore();
        }
    }

    public void e(Canvas canvas) {
        this.E.setRotate(d(), this.f.centerX(), this.f.centerY());
        this.E.mapRect(this.g, this.q.c() ? this.e : this.f);
        canvas.clipRect(this.g);
    }

    public void g(float f2, float f3) {
        b bVar;
        b bVar2 = this.s;
        if ((bVar2 == b.DOODLE || bVar2 == b.MOSAIC) && (bVar = this.d) != null) {
            bVar.a();
        }
        if (this.n != null) {
            this.n = null;
        }
    }

    public RectF b() {
        return this.f;
    }

    public io.dcloud.feature.gallery.imageedit.c.i.a b(float f2, float f3) {
        io.dcloud.feature.gallery.imageedit.c.i.a aVar = new io.dcloud.feature.gallery.imageedit.c.i.a(f2, f3, e(), f());
        if (this.s == b.CLIP) {
            RectF rectF = new RectF(this.q.a());
            rectF.offset(f2, f3);
            if (this.q.f()) {
                RectF rectF2 = new RectF();
                this.E.setRotate(f(), this.f.centerX(), this.f.centerY());
                this.E.mapRect(rectF2, this.f);
                aVar.a(io.dcloud.feature.gallery.imageedit.c.k.b.b(rectF, rectF2));
            } else {
                RectF rectF3 = new RectF();
                if (this.q.d()) {
                    this.E.setRotate(f() - d(), this.f.centerX(), this.f.centerY());
                    this.E.mapRect(rectF3, this.q.b(f2, f3));
                    aVar.a(io.dcloud.feature.gallery.imageedit.c.k.b.b(rectF, rectF3, this.f.centerX(), this.f.centerY()));
                } else {
                    this.E.setRotate(f(), this.f.centerX(), this.f.centerY());
                    this.E.mapRect(rectF3, this.e);
                    aVar.a(io.dcloud.feature.gallery.imageedit.c.k.b.a(rectF, rectF3, this.f.centerX(), this.f.centerY()));
                }
            }
        } else {
            RectF rectF4 = new RectF();
            this.E.setRotate(f(), this.f.centerX(), this.f.centerY());
            this.E.mapRect(rectF4, this.f);
            RectF rectF5 = new RectF(this.u);
            rectF5.offset(f2, f3);
            aVar.a(io.dcloud.feature.gallery.imageedit.c.k.b.a(rectF5, rectF4, this.l));
            this.l = false;
        }
        return aVar;
    }

    public void e(float f2, float f3) {
        this.o = true;
        m();
        this.q.d(true);
    }

    public int c(Canvas canvas) {
        int saveLayer = canvas.saveLayer(this.e, (Paint) null, 31);
        if (!k()) {
            canvas.save();
            float e2 = e();
            RectF rectF = this.e;
            canvas.translate(rectF.left, rectF.top);
            canvas.scale(e2, e2);
            for (c b2 : this.A) {
                b2.b(canvas, this.B);
            }
            canvas.restore();
        }
        return saveLayer;
    }

    public void e(float f2) {
        this.k = f2;
    }

    public float e() {
        return (this.e.width() * 1.0f) / ((float) this.b.getWidth());
    }

    public void d(Canvas canvas) {
        if (this.s == b.CLIP && this.o) {
            this.p.reset();
            Path path = this.p;
            RectF rectF = this.e;
            path.addRect(rectF.left - 2.0f, rectF.top - 2.0f, rectF.right + 2.0f, rectF.bottom + 2.0f, Path.Direction.CW);
            this.p.addRect(this.f, Path.Direction.CCW);
            canvas.drawPath(this.p, this.D);
        }
    }

    public void a(b bVar) {
        if (this.s != bVar) {
            b(this.x);
            b bVar2 = b.CLIP;
            if (bVar == bVar2) {
                c(true);
            }
            this.s = bVar;
            if (bVar == bVar2) {
                g();
                this.i = d();
                this.h.set(this.f);
                float e2 = 1.0f / e();
                Matrix matrix = this.E;
                RectF rectF = this.e;
                matrix.setTranslate(-rectF.left, -rectF.top);
                this.E.postScale(e2, e2);
                this.E.mapRect(this.h);
                this.q.a(this.f, f());
                return;
            }
            if (bVar == b.MOSAIC) {
                l();
            }
            this.q.a(false);
        }
    }

    public void c(float f2) {
        this.j = f2;
    }

    public float d() {
        return this.j;
    }

    public void f(float f2, float f3) {
        b bVar;
        this.o = false;
        b(this.x);
        b bVar2 = this.s;
        if (bVar2 == b.CLIP) {
            this.n = this.q.a(f2, f3);
            this.q.d(false);
        } else if ((bVar2 == b.DOODLE || bVar2 == b.MOSAIC) && (bVar = this.d) != null) {
            bVar.b();
        }
    }

    private void c(boolean z2) {
        if (z2 != this.t) {
            b(z2 ? -d() : f());
            this.t = z2;
        }
    }

    public void d(float f2) {
        b(f2, this.f.centerX(), this.f.centerY());
    }

    public float f() {
        return this.k;
    }

    public io.dcloud.feature.gallery.imageedit.c.i.a a(float f2, float f3) {
        RectF b2 = this.q.b(f2, f3);
        this.E.setRotate(-d(), this.f.centerX(), this.f.centerY());
        this.E.mapRect(this.f, b2);
        return new io.dcloud.feature.gallery.imageedit.c.i.a(f2 + (this.f.centerX() - b2.centerX()), f3 + (this.f.centerY() - b2.centerY()), e(), d());
    }

    private void b(io.dcloud.feature.gallery.imageedit.c.j.a aVar) {
        if (aVar != null) {
            if (!aVar.b()) {
                if (!this.y.contains(aVar)) {
                    this.y.add(aVar);
                }
                if (this.x == aVar) {
                    this.x = null;
                    return;
                }
                return;
            }
            aVar.dismiss();
        }
    }

    public <S extends io.dcloud.feature.gallery.imageedit.c.j.a> void a(S s2) {
        if (s2 != null) {
            c((io.dcloud.feature.gallery.imageedit.c.j.a) s2);
        }
    }

    public void a(c cVar, float f2, float f3) {
        if (cVar != null) {
            float e2 = 1.0f / e();
            this.E.setTranslate(f2, f3);
            this.E.postRotate(-d(), this.f.centerX(), this.f.centerY());
            Matrix matrix = this.E;
            RectF rectF = this.e;
            matrix.postTranslate(-rectF.left, -rectF.top);
            this.E.postScale(e2, e2);
            cVar.a(this.E);
            int i2 = C0039a.a[cVar.b().ordinal()];
            if (i2 == 1) {
                cVar.a(cVar.d() * e2);
                this.z.add(cVar);
            } else if (i2 == 2) {
                cVar.a(cVar.d() * e2);
                this.A.add(cVar);
            }
        }
    }

    public void b(c cVar) {
        List<c> list = this.F;
        if (list != null) {
            list.remove(cVar);
        }
    }

    public void b(Canvas canvas) {
        canvas.clipRect(this.q.c() ? this.e : this.f);
        canvas.drawBitmap(this.b, (Rect) null, this.e, (Paint) null);
    }

    public void b(float f2, float f3, float f4) {
        a(f2 / e(), f3, f4);
    }

    public void a(c cVar) {
        if (this.F == null) {
            this.F = new ArrayList();
        }
        this.F.add(cVar);
    }

    public void b(boolean z2) {
        this.m = false;
        this.r = true;
    }

    public void b(int i2) {
        this.q.a(i2);
    }

    private void a() {
        List<c> list = this.F;
        if (list != null) {
            for (c next : list) {
                if (next != null) {
                    next.a();
                }
            }
        }
    }

    public void a(Canvas canvas, int i2) {
        canvas.drawBitmap(this.c, (Rect) null, this.e, this.C);
        canvas.restoreToCount(i2);
    }

    public void a(Canvas canvas) {
        if (!h()) {
            canvas.save();
            float e2 = e();
            RectF rectF = this.e;
            canvas.translate(rectF.left, rectF.top);
            canvas.scale(e2, e2);
            for (c a2 : this.z) {
                a2.a(canvas, this.B);
            }
            canvas.restore();
        }
    }

    public void a(Canvas canvas, float f2, float f3) {
        if (this.s == b.CLIP) {
            this.q.a(canvas);
        }
    }

    public io.dcloud.feature.gallery.imageedit.c.i.a a(float f2, float f3, float f4, float f5) {
        if (this.s != b.CLIP) {
            return null;
        }
        this.q.d(false);
        a.C0040a aVar = this.n;
        if (aVar == null) {
            return null;
        }
        this.q.a(aVar, f4, f5);
        RectF rectF = new RectF();
        this.E.setRotate(d(), this.f.centerX(), this.f.centerY());
        this.E.mapRect(rectF, this.e);
        RectF b2 = this.q.b(f2, f3);
        io.dcloud.feature.gallery.imageedit.c.i.a aVar2 = new io.dcloud.feature.gallery.imageedit.c.i.a(f2, f3, e(), f());
        aVar2.a(io.dcloud.feature.gallery.imageedit.c.k.b.a(b2, rectF, this.f.centerX(), this.f.centerY()));
        return aVar2;
    }

    public void a(int i2) {
        this.k = (float) (Math.round((this.j + ((float) i2)) / 90.0f) * 90);
        if (this.q.e()) {
            this.f.set(this.e);
        }
        this.q.a(this.f, f());
    }

    public void a(float f2, float f3, float f4) {
        if (f2 != 1.0f) {
            if (Math.max(this.f.width(), this.f.height()) >= 10000.0f || Math.min(this.f.width(), this.f.height()) <= 500.0f) {
                f2 += (1.0f - f2) / 2.0f;
            }
            this.E.setScale(f2, f2, f3, f4);
            this.E.mapRect(this.e);
            this.E.mapRect(this.f);
            this.e.contains(this.f);
            for (io.dcloud.feature.gallery.imageedit.c.j.a next : this.y) {
                this.E.mapRect(next.getFrame());
                float x2 = next.getX() + next.getPivotX();
                float y2 = next.getY() + next.getPivotY();
                next.a(f2);
                next.setX((next.getX() + next.getFrame().centerX()) - x2);
                next.setY((next.getY() + next.getFrame().centerY()) - y2);
            }
        }
    }

    public void a(float f2) {
        this.q.a(f2);
    }

    public boolean a(float f2, float f3, boolean z2) {
        this.r = true;
        if (this.s == b.CLIP) {
            boolean z3 = !this.m;
            this.q.b(false);
            this.q.a(true);
            this.q.c(false);
            return z3;
        }
        if (this.t && !this.m) {
            c(false);
        }
        return false;
    }

    public void a(boolean z2) {
        this.m = true;
        Log.d("IMGImage", "Homing cancel");
    }

    public void a(int i2, int i3) {
        io.dcloud.feature.gallery.imageedit.c.g.b bVar = this.q;
        if (bVar != null) {
            bVar.e((float) i2, (float) i3);
        }
    }

    public void a(b bVar) {
        this.d = bVar;
    }
}
