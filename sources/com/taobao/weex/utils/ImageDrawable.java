package com.taobao.weex.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.widget.ImageView;

public class ImageDrawable extends PaintDrawable {
    private int bitmapHeight;
    private int bitmapWidth;
    private float[] radii;

    public static Drawable createImageDrawable(Drawable drawable, ImageView.ScaleType scaleType, float[] fArr, int i, int i2, boolean z) {
        Bitmap bitmap;
        if (!z && i > 0 && i2 > 0) {
            if ((drawable instanceof BitmapDrawable) && (bitmap = ((BitmapDrawable) drawable).getBitmap()) != null) {
                ImageDrawable imageDrawable = new ImageDrawable();
                imageDrawable.getPaint().setFilterBitmap(true);
                imageDrawable.bitmapWidth = bitmap.getWidth();
                imageDrawable.bitmapHeight = bitmap.getHeight();
                BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                updateShaderAndSize(scaleType, i, i2, imageDrawable, bitmapShader);
                imageDrawable.getPaint().setShader(bitmapShader);
                return imageDrawable;
            } else if (drawable instanceof ImageDrawable) {
                ImageDrawable imageDrawable2 = (ImageDrawable) drawable;
                if (imageDrawable2.getPaint() != null && (imageDrawable2.getPaint().getShader() instanceof BitmapShader)) {
                    updateShaderAndSize(scaleType, i, i2, imageDrawable2, (BitmapShader) imageDrawable2.getPaint().getShader());
                    return imageDrawable2;
                }
            }
        }
        return drawable;
    }

    private static void updateShaderAndSize(ImageView.ScaleType scaleType, int i, int i2, ImageDrawable imageDrawable, BitmapShader bitmapShader) {
        Matrix createShaderMatrix = createShaderMatrix(scaleType, i, i2, imageDrawable.bitmapWidth, imageDrawable.bitmapHeight);
        if (scaleType == ImageView.ScaleType.FIT_CENTER) {
            RectF rectF = new RectF(0.0f, 0.0f, (float) imageDrawable.bitmapWidth, (float) imageDrawable.bitmapHeight);
            RectF rectF2 = new RectF();
            createShaderMatrix.mapRect(rectF2, rectF);
            i = (int) rectF2.width();
            i2 = (int) rectF2.height();
            createShaderMatrix = createShaderMatrix(scaleType, i, i2, imageDrawable.bitmapWidth, imageDrawable.bitmapHeight);
        }
        imageDrawable.setIntrinsicWidth(i);
        imageDrawable.setIntrinsicHeight(i2);
        bitmapShader.setLocalMatrix(createShaderMatrix);
    }

    private static Matrix createShaderMatrix(ImageView.ScaleType scaleType, int i, int i2, int i3, int i4) {
        float f;
        float f2;
        float f3;
        if (i3 * i2 > i4 * i) {
            f3 = ((float) i2) / ((float) i4);
            f2 = (((float) i) - (((float) i3) * f3)) * 0.5f;
            f = 0.0f;
        } else {
            f3 = ((float) i) / ((float) i3);
            f = (((float) i2) - (((float) i4) * f3)) * 0.5f;
            f2 = 0.0f;
        }
        Matrix matrix = new Matrix();
        if (scaleType == ImageView.ScaleType.FIT_XY) {
            matrix.setScale(((float) i) / ((float) i3), ((float) i2) / ((float) i4));
        } else if (scaleType == ImageView.ScaleType.FIT_CENTER) {
            matrix.setRectToRect(new RectF(0.0f, 0.0f, (float) i3, (float) i4), new RectF(0.0f, 0.0f, (float) i, (float) i2), Matrix.ScaleToFit.CENTER);
        } else if (scaleType == ImageView.ScaleType.CENTER_CROP) {
            matrix.setScale(f3, f3);
            matrix.postTranslate(f2 + 0.5f, f + 0.5f);
        }
        return matrix;
    }

    private ImageDrawable() {
    }

    public void setCornerRadii(float[] fArr) {
        this.radii = fArr;
        super.setCornerRadii(fArr);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Shape shape, Canvas canvas, Paint paint) {
        if (Build.VERSION.SDK_INT == 21) {
            paint.setAntiAlias(false);
        }
        super.onDraw(shape, canvas, paint);
    }

    public float[] getCornerRadii() {
        return this.radii;
    }

    public int getBitmapHeight() {
        return this.bitmapHeight;
    }

    public int getBitmapWidth() {
        return this.bitmapWidth;
    }
}
