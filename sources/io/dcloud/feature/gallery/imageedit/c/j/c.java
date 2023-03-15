package io.dcloud.feature.gallery.imageedit.c.j;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.view.View;
import io.dcloud.feature.gallery.imageedit.c.j.a;
import io.dcloud.feature.gallery.imageedit.c.j.e;

public class c<StickerView extends View & a> implements e, e.a {
    private RectF a;
    private StickerView b;
    private e.a c;
    private boolean d = false;

    public c(StickerView stickerview) {
        this.b = stickerview;
    }

    public boolean a() {
        if (b()) {
            return false;
        }
        this.d = true;
        c(this.b);
        return true;
    }

    public boolean b() {
        return this.d;
    }

    public boolean c() {
        return a(this.b);
    }

    public boolean dismiss() {
        if (!b()) {
            return false;
        }
        this.d = false;
        b(this.b);
        return true;
    }

    public RectF getFrame() {
        if (this.a == null) {
            this.a = new RectF(0.0f, 0.0f, (float) this.b.getWidth(), (float) this.b.getHeight());
            float x = this.b.getX() + this.b.getPivotX();
            float y = this.b.getY() + this.b.getPivotY();
            Matrix matrix = new Matrix();
            matrix.setTranslate(this.b.getX(), this.b.getY());
            matrix.postScale(this.b.getScaleX(), this.b.getScaleY(), x, y);
            matrix.mapRect(this.a);
        }
        return this.a;
    }

    public void b(e.a aVar) {
        this.c = null;
    }

    public <V extends View & a> void c(V v) {
        v.invalidate();
        e.a aVar = this.c;
        if (aVar != null) {
            aVar.c(v);
        }
    }

    public <V extends View & a> void b(V v) {
        this.a = null;
        v.invalidate();
        e.a aVar = this.c;
        if (aVar != null) {
            aVar.b(v);
        }
    }

    public void a(e.a aVar) {
        this.c = aVar;
    }

    public <V extends View & a> boolean a(V v) {
        e.a aVar = this.c;
        return aVar != null && aVar.a(v);
    }
}
