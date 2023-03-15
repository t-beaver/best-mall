package io.dcloud.feature.nativeObj;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import io.dcloud.common.util.PdrUtil;

public class NativeViewBackButtonDrawable extends Drawable {
    private Paint mPaint;
    private String widthStr;

    public NativeViewBackButtonDrawable(int i) {
        Paint paint = new Paint(1);
        this.mPaint = paint;
        paint.setColor(i);
    }

    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        float exactCenterX = bounds.exactCenterX();
        float exactCenterY = bounds.exactCenterY();
        if (PdrUtil.isEmpty(this.widthStr) || !this.widthStr.equals("backButton")) {
            if (PdrUtil.isEmpty(this.widthStr) || (!this.widthStr.equals("auto") && !this.widthStr.endsWith("px"))) {
                canvas.drawCircle(exactCenterX, exactCenterY, Math.min(exactCenterX, exactCenterY), this.mPaint);
                return;
            }
            if (this.widthStr.endsWith("px")) {
                String str = this.widthStr;
                try {
                    if (Integer.parseInt(str.substring(0, str.indexOf("px"))) <= 44) {
                        canvas.drawCircle(exactCenterX, exactCenterY, Math.min(exactCenterX, exactCenterY), this.mPaint);
                        return;
                    }
                } catch (Exception unused) {
                }
            }
            canvas.drawRoundRect(new RectF(bounds), Math.min(exactCenterX, exactCenterY), Math.min(exactCenterX, exactCenterY), this.mPaint);
        } else if (bounds.width() <= bounds.height()) {
            canvas.drawCircle(exactCenterX, exactCenterY, Math.min(exactCenterX, exactCenterY), this.mPaint);
        } else {
            canvas.drawRoundRect(new RectF(bounds), Math.min(exactCenterX, exactCenterY), Math.min(exactCenterX, exactCenterY), this.mPaint);
        }
    }

    public int getAlpha() {
        return this.mPaint.getAlpha();
    }

    public int getDrawableAlpha() {
        return this.mPaint.getAlpha();
    }

    public int getDrawableColor() {
        Paint paint = this.mPaint;
        if (paint != null) {
            return paint.getColor();
        }
        return 0;
    }

    public int getOpacity() {
        return -3;
    }

    public void setAlpha(int i) {
        this.mPaint.setAlpha(i);
        invalidateSelf();
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    public void setDrawableColor(int i) {
        Paint paint = this.mPaint;
        if (paint != null) {
            paint.setColor(i);
        }
        invalidateSelf();
    }

    public void setWidth(String str) {
        this.widthStr = str;
    }
}
