package io.dcloud.h.a.d.c;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

public class a extends Drawable {
    Paint a = new Paint();
    RectF b = null;
    Bitmap c = null;
    RectF d = null;
    Bitmap e;
    Context f;

    public a(Bitmap bitmap, Context context) {
        this.e = bitmap;
        this.f = context;
    }

    private void a() {
        Rect bounds = getBounds();
        float height = (float) this.e.getHeight();
        float width = (float) this.e.getWidth();
        float height2 = (float) bounds.height();
        float width2 = (float) bounds.width();
        float f2 = height / width;
        float f3 = (height2 / width2) - f2;
        if (((double) f3) > 0.15d) {
            float f4 = (height2 - (f2 * width2)) / 2.0f;
            this.b = new RectF(0.0f, f4, width2, height2 - f4);
            if (this.d == null) {
                this.c = this.e.copy(Bitmap.Config.ARGB_8888, true);
                Canvas canvas = new Canvas(this.c);
                canvas.scale(0.25f, 0.25f, width / 2.0f, height / 2.0f);
                canvas.drawColor(1711276032);
                float f5 = (f3 * width) / 2.0f;
                this.d = new RectF(-f5, 0.0f, width2 + f5, height2);
                a(this.c);
                return;
            }
            return;
        }
        this.b = new RectF(0.0f, 0.0f, width2, height2);
    }

    public void draw(Canvas canvas) {
        Bitmap bitmap = this.e;
        if (bitmap != null && !bitmap.isRecycled()) {
            if (this.c == null) {
                a();
            }
            Bitmap bitmap2 = this.c;
            if (bitmap2 != null) {
                canvas.drawBitmap(bitmap2, (Rect) null, this.d, this.a);
            }
            canvas.drawBitmap(this.e, (Rect) null, this.b, this.a);
        }
    }

    public int getOpacity() {
        return 0;
    }

    public void setAlpha(int i) {
    }

    public void setColorFilter(ColorFilter colorFilter) {
    }

    private void a(Bitmap bitmap) {
        RenderScript create = RenderScript.create(this.f);
        Allocation createFromBitmap = Allocation.createFromBitmap(create, bitmap);
        ScriptIntrinsicBlur create2 = ScriptIntrinsicBlur.create(create, createFromBitmap.getElement());
        create2.setInput(createFromBitmap);
        create2.setRadius(20.0f);
        create2.forEach(createFromBitmap);
        createFromBitmap.copyTo(bitmap);
        create.destroy();
    }
}
