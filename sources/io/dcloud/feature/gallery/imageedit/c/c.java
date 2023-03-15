package io.dcloud.feature.gallery.imageedit.c;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;

public class c {
    protected Path a;
    private int b;
    private float c;
    private b d;

    public c() {
        this(new Path());
    }

    public int a() {
        return this.b;
    }

    public b b() {
        return this.d;
    }

    public Path c() {
        return this.a;
    }

    public float d() {
        return this.c;
    }

    public c(Path path) {
        this(path, b.DOODLE);
    }

    public void a(int i) {
        this.b = i;
    }

    public void b(Canvas canvas, Paint paint) {
        if (this.d == b.MOSAIC) {
            paint.setStrokeWidth(this.c);
            canvas.drawPath(this.a, paint);
        }
    }

    public c(Path path, b bVar) {
        this(path, bVar, -65536);
    }

    public void a(b bVar) {
        this.d = bVar;
    }

    public c(Path path, b bVar, int i) {
        this(path, bVar, i, 72.0f);
    }

    public void a(float f) {
        this.c = f;
    }

    public c(Path path, b bVar, int i, float f) {
        this.b = -65536;
        this.c = 72.0f;
        this.d = b.DOODLE;
        this.a = path;
        this.d = bVar;
        this.b = i;
        this.c = f;
        if (bVar == b.MOSAIC) {
            path.setFillType(Path.FillType.EVEN_ODD);
        }
    }

    public void a(Canvas canvas, Paint paint) {
        if (this.d == b.DOODLE) {
            paint.setColor(this.b);
            paint.setStrokeWidth(this.c);
            canvas.drawPath(this.a, paint);
        }
    }

    public void a(Matrix matrix) {
        this.a.transform(matrix);
    }
}
