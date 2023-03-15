package io.dcloud.sdk.base.dcloud;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import io.dcloud.sdk.base.dcloud.ADHandler;

public class g extends View {
    Bitmap a = null;
    int b = 0;
    int c = 0;
    ADHandler.g d = null;
    Paint e = new Paint();
    Rect f = null;
    RectF g = null;
    Bitmap h = null;
    RectF i = null;

    public g(Context context, Bitmap bitmap, ADHandler.g gVar) {
        super(context);
        this.a = bitmap;
        this.d = gVar;
    }

    private void a(Canvas canvas) {
        float height = (float) this.a.getHeight();
        float width = (float) this.a.getWidth();
        float height2 = (float) (getHeight() > 0 ? getHeight() : canvas.getHeight());
        float width2 = (float) (getWidth() > 0 ? getWidth() : canvas.getWidth());
        float f2 = height / width;
        float f3 = (height2 / width2) - f2;
        if (((double) f3) > 0.15d) {
            float f4 = (height2 - (f2 * width2)) / 2.0f;
            this.g = new RectF(0.0f, f4, width2, height2 - f4);
            if (this.i == null) {
                int i2 = (int) width;
                int i3 = (int) height;
                this.h = Bitmap.createBitmap(i2, i3, Bitmap.Config.ARGB_8888);
                Canvas canvas2 = new Canvas(this.h);
                canvas2.drawBitmap(this.a, new Rect(0, 0, i2, i3), new Rect(0, 0, i2, i3), new Paint());
                canvas2.drawColor(b(this.a) >= 128 ? 1711276032 : 1728053247);
                float f5 = (f3 * width) / 2.0f;
                this.i = new RectF(-f5, 0.0f, width2 + f5, height2);
                a(this.h);
                return;
            }
            return;
        }
        this.g = new RectF(0.0f, 0.0f, width2, height2);
    }

    public int b(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < width; i4++) {
            for (int i5 = 0; i5 < height; i5++) {
                i3++;
                int pixel = bitmap.getPixel(i4, i5);
                double d2 = (double) i2;
                double d3 = (double) (((-16711681 | pixel) >> 16) & 255);
                Double.isNaN(d3);
                Double.isNaN(d2);
                double d4 = (double) (((-65281 | pixel) >> 8) & 255);
                Double.isNaN(d4);
                double d5 = (double) ((pixel | -256) & 255);
                Double.isNaN(d5);
                i2 = (int) (d2 + (d3 * 0.299d) + (d4 * 0.587d) + (d5 * 0.114d));
            }
            Bitmap bitmap2 = bitmap;
        }
        return i2 / i3;
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.h = null;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        Bitmap bitmap = this.a;
        if (bitmap != null && !bitmap.isRecycled()) {
            a(canvas);
            Bitmap bitmap2 = this.h;
            if (bitmap2 != null) {
                canvas.drawBitmap(bitmap2, this.f, this.i, this.e);
            }
            canvas.drawBitmap(this.a, this.f, this.g, this.e);
        }
    }

    private void a(Bitmap bitmap) {
        RenderScript create = RenderScript.create(getContext());
        Allocation createFromBitmap = Allocation.createFromBitmap(create, bitmap);
        ScriptIntrinsicBlur create2 = ScriptIntrinsicBlur.create(create, createFromBitmap.getElement());
        create2.setInput(createFromBitmap);
        create2.setRadius(20.0f);
        create2.forEach(createFromBitmap);
        createFromBitmap.copyTo(bitmap);
        create.destroy();
    }
}
