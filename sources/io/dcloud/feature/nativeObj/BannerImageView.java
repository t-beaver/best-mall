package io.dcloud.feature.nativeObj;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import io.dcloud.feature.nativeObj.data.NativeImageDataItem;

public class BannerImageView extends ImageView {
    NativeImageDataItem mImageData;
    Paint mPaint = new Paint();

    public BannerImageView(Context context, NativeImageDataItem nativeImageDataItem) {
        super(context);
        this.mImageData = nativeImageDataItem;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        float f;
        float f2;
        float f3;
        float f4;
        float height;
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        Drawable drawable = getDrawable();
        if (drawable != null && drawable.getIntrinsicWidth() != 0 && drawable.getIntrinsicHeight() != 0) {
            Bitmap bitmap = drawable instanceof BitmapDrawable ? ((BitmapDrawable) drawable).getBitmap() : null;
            if (bitmap != null) {
                int saveCount = canvas.getSaveCount();
                canvas.save();
                float f5 = getContext().getResources().getDisplayMetrics().density;
                float intrinsicWidth = (float) drawable.getIntrinsicWidth();
                float intrinsicHeight = (float) drawable.getIntrinsicHeight();
                if (!this.mImageData.height.equals("auto") || !this.mImageData.width.equals("auto")) {
                    if (this.mImageData.height.equals("auto")) {
                        float width = (float) this.mImageData.getWidth(getWidth(), f5);
                        height = intrinsicHeight * (width / intrinsicWidth);
                        f = width;
                    } else if (this.mImageData.width.equals("auto")) {
                        f2 = (float) this.mImageData.getHeight(getHeight(), f5);
                        f = intrinsicWidth * (f2 / intrinsicHeight);
                    } else {
                        height = (float) this.mImageData.getHeight(getHeight(), f5);
                        f = (float) this.mImageData.getWidth(getWidth(), f5);
                    }
                    f2 = height;
                } else {
                    float height2 = getHeight() < getWidth() ? ((float) getHeight()) / intrinsicHeight : ((float) getWidth()) / intrinsicWidth;
                    f = intrinsicWidth * height2;
                    f2 = height2 * intrinsicHeight;
                }
                float f6 = 0.0f;
                if (f == ((float) getWidth()) || this.mImageData.align.equals("left")) {
                    f4 = f;
                    f3 = 0.0f;
                } else if (this.mImageData.align.equals("right")) {
                    f4 = (float) getWidth();
                    f3 = f4 - f;
                } else {
                    float width2 = (((float) getWidth()) - f) / 2.0f;
                    float f7 = f + width2;
                    f3 = width2;
                    f4 = f7;
                }
                if (f2 != ((float) getHeight()) && !this.mImageData.verticalAlign.equals("top")) {
                    if (this.mImageData.verticalAlign.equals("bottom")) {
                        float height3 = (float) getHeight();
                        f6 = height3 - f2;
                        f2 = height3;
                    } else {
                        f6 = (((float) getHeight()) - f2) / 2.0f;
                        f2 += f6;
                    }
                }
                canvas.drawBitmap(bitmap, (Rect) null, new RectF(f3, f6, f4, f2), this.mPaint);
                canvas.restoreToCount(saveCount);
            }
        }
    }
}
