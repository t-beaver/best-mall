package io.dcloud.feature.nativeObj;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import pl.droidsonroids.gif.transforms.Transform;

public class GIFCornerRadiusTransform implements Transform {
    private int gifViewWidth = -1;
    private float mCornerRadius;
    private final RectF mDstRectF = new RectF();
    private Shader mShader;

    public GIFCornerRadiusTransform(float f, int i) {
        setCornerRadiusSafely(f);
        this.gifViewWidth = i;
    }

    private void setCornerRadiusSafely(float f) {
        float max = Math.max(0.0f, f);
        if (max != this.mCornerRadius) {
            this.mCornerRadius = max;
            this.mShader = null;
        }
    }

    public RectF getBounds() {
        return this.mDstRectF;
    }

    public float getCornerRadius() {
        return this.mCornerRadius;
    }

    public void onBoundsChange(Rect rect) {
        this.mDstRectF.set(rect);
        this.mShader = null;
    }

    public void onDraw(Canvas canvas, Paint paint, Bitmap bitmap) {
        if (this.mCornerRadius == 0.0f) {
            canvas.drawBitmap(bitmap, (Rect) null, this.mDstRectF, paint);
            return;
        }
        if (this.mShader == null) {
            Shader.TileMode tileMode = Shader.TileMode.CLAMP;
            this.mShader = new BitmapShader(bitmap, tileMode, tileMode);
            Matrix matrix = new Matrix();
            RectF rectF = this.mDstRectF;
            matrix.setTranslate(rectF.left, rectF.top);
            matrix.preScale(this.mDstRectF.width() / ((float) bitmap.getWidth()), this.mDstRectF.height() / ((float) bitmap.getHeight()));
            this.mShader.setLocalMatrix(matrix);
        }
        paint.setShader(this.mShader);
        float f = 1.0f;
        if (this.gifViewWidth > 0) {
            f = this.mDstRectF.width() / ((float) this.gifViewWidth);
        }
        paint.setAntiAlias(true);
        RectF rectF2 = this.mDstRectF;
        float f2 = this.mCornerRadius * f;
        canvas.drawRoundRect(rectF2, f2, f2, paint);
    }

    public void setCornerRadius(float f) {
        setCornerRadiusSafely(f);
    }
}
