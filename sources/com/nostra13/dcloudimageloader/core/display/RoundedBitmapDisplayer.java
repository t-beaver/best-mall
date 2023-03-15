package com.nostra13.dcloudimageloader.core.display;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.ImageView;
import com.nostra13.dcloudimageloader.core.assist.LoadedFrom;
import com.nostra13.dcloudimageloader.core.imageaware.ImageAware;
import com.nostra13.dcloudimageloader.core.imageaware.ImageViewAware;
import com.nostra13.dcloudimageloader.utils.L;

public class RoundedBitmapDisplayer implements BitmapDisplayer {
    private final int roundPixels;

    public RoundedBitmapDisplayer(int i) {
        this.roundPixels = i;
    }

    private static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int i, Rect rect, Rect rect2, int i2, int i3) {
        Bitmap createBitmap = Bitmap.createBitmap(i2, i3, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        RectF rectF = new RectF(rect2);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-16777216);
        float f = (float) i;
        canvas.drawRoundRect(rectF, f, f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rectF, paint);
        return createBitmap;
    }

    public static Bitmap roundCorners(Bitmap bitmap, ImageViewAware imageViewAware, int i) {
        int i2;
        int i3;
        Rect rect;
        int i4;
        int i5;
        Rect rect2;
        int i6;
        int i7;
        int i8;
        int i9;
        Rect rect3;
        Rect rect4;
        Rect rect5;
        ImageView wrappedView = imageViewAware.getWrappedView();
        if (wrappedView == null) {
            L.w("View is collected probably. Can't round bitmap corners without view properties.", new Object[0]);
            return bitmap;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int width2 = imageViewAware.getWidth();
        int height2 = imageViewAware.getHeight();
        if (width2 <= 0) {
            width2 = width;
        }
        if (height2 <= 0) {
            height2 = height;
        }
        ImageView.ScaleType scaleType = wrappedView.getScaleType();
        if (scaleType == null) {
            return bitmap;
        }
        int ordinal = scaleType.ordinal();
        if (ordinal != 1) {
            if (ordinal != 5) {
                if (ordinal == 6) {
                    rect5 = new Rect(0, 0, width, height);
                    rect = new Rect(0, 0, width2, height2);
                } else if (ordinal == 7 || ordinal == 8) {
                    i9 = Math.min(width2, width);
                    i6 = Math.min(height2, height);
                    int i10 = (width - i9) / 2;
                    int i11 = (height - i6) / 2;
                    rect3 = new Rect(i10, i11, i10 + i9, i11 + i6);
                    rect4 = new Rect(0, 0, i9, i6);
                } else {
                    float f = (float) width2;
                    float f2 = (float) height2;
                    float f3 = (float) width;
                    float f4 = (float) height;
                    if (f / f2 > f3 / f4) {
                        width2 = (int) (f3 / (f4 / f2));
                    } else {
                        height2 = (int) (f4 / (f3 / f));
                    }
                    rect5 = new Rect(0, 0, width, height);
                    rect = new Rect(0, 0, width2, height2);
                }
                i4 = width2;
                i5 = height2;
                rect2 = rect5;
            } else {
                float f5 = (float) width2;
                float f6 = (float) height2;
                float f7 = (float) width;
                float f8 = (float) height;
                if (f5 / f6 > f7 / f8) {
                    int i12 = (int) (f6 * (f7 / f5));
                    i7 = (height - i12) / 2;
                    i6 = i12;
                    i9 = width;
                    i8 = 0;
                } else {
                    i9 = (int) (f5 * (f8 / f6));
                    i8 = (width - i9) / 2;
                    i6 = height;
                    i7 = 0;
                }
                rect3 = new Rect(i8, i7, i8 + i9, i7 + i6);
                rect4 = new Rect(0, 0, i9, i6);
            }
            i4 = i9;
            i5 = i6;
            rect2 = rect3;
        } else {
            float f9 = (float) width;
            float f10 = (float) height;
            if (((float) width2) / ((float) height2) > f9 / f10) {
                i3 = Math.min(height2, height);
                i2 = (int) (f9 / (f10 / ((float) i3)));
            } else {
                int min = Math.min(width2, width);
                int i13 = (int) (f10 / (f9 / ((float) min)));
                i2 = min;
                i3 = i13;
            }
            int i14 = (width2 - i2) / 2;
            int i15 = (height2 - i3) / 2;
            Rect rect6 = new Rect(0, 0, width, height);
            rect = new Rect(i14, i15, i2 + i14, i3 + i15);
            i4 = width2;
            i5 = height2;
            rect2 = rect6;
        }
        try {
            return getRoundedCornerBitmap(bitmap, i, rect2, rect, i4, i5);
        } catch (OutOfMemoryError e) {
            L.e(e, "Can't create bitmap with rounded corners. Not enough memory.", new Object[0]);
            return bitmap;
        }
    }

    public Bitmap display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        if (imageAware instanceof ImageViewAware) {
            Bitmap roundCorners = roundCorners(bitmap, (ImageViewAware) imageAware, this.roundPixels);
            imageAware.setImageBitmap(roundCorners);
            return roundCorners;
        }
        throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
    }
}
