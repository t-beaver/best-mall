package com.nostra13.dcloudimageloader.utils;

import android.opengl.GLES10;
import com.nostra13.dcloudimageloader.core.assist.ImageSize;
import com.nostra13.dcloudimageloader.core.assist.ViewScaleType;
import com.nostra13.dcloudimageloader.core.imageaware.ImageAware;

public final class ImageSizeUtils {
    private static final int DEFAULT_MAX_BITMAP_DIMENSION = 2048;
    private static ImageSize maxBitmapSize;

    /* renamed from: com.nostra13.dcloudimageloader.utils.ImageSizeUtils$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$nostra13$dcloudimageloader$core$assist$ViewScaleType;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        static {
            /*
                com.nostra13.dcloudimageloader.core.assist.ViewScaleType[] r0 = com.nostra13.dcloudimageloader.core.assist.ViewScaleType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$nostra13$dcloudimageloader$core$assist$ViewScaleType = r0
                com.nostra13.dcloudimageloader.core.assist.ViewScaleType r1 = com.nostra13.dcloudimageloader.core.assist.ViewScaleType.FIT_INSIDE     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$nostra13$dcloudimageloader$core$assist$ViewScaleType     // Catch:{ NoSuchFieldError -> 0x001d }
                com.nostra13.dcloudimageloader.core.assist.ViewScaleType r1 = com.nostra13.dcloudimageloader.core.assist.ViewScaleType.CROP     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.nostra13.dcloudimageloader.utils.ImageSizeUtils.AnonymousClass1.<clinit>():void");
        }
    }

    private ImageSizeUtils() {
    }

    public static int computeImageSampleSize(ImageSize imageSize, ImageSize imageSize2, ViewScaleType viewScaleType, boolean z) {
        int i;
        int width = imageSize.getWidth();
        int height = imageSize.getHeight();
        int width2 = imageSize2.getWidth();
        int height2 = imageSize2.getHeight();
        int i2 = AnonymousClass1.$SwitchMap$com$nostra13$dcloudimageloader$core$assist$ViewScaleType[viewScaleType.ordinal()];
        int i3 = 1;
        if (i2 != 1) {
            if (i2 != 2) {
                i = 1;
            } else if (z) {
                int i4 = width / 2;
                int i5 = height / 2;
                i = 1;
                while (i4 / i > width2 && i5 / i > height2) {
                    i *= 2;
                }
            } else {
                i = Math.min(width / width2, height / height2);
            }
        } else if (z) {
            int i6 = width / 2;
            int i7 = height / 2;
            int i8 = 1;
            while (true) {
                if (i6 / i <= width2 && i7 / i <= height2) {
                    break;
                }
                i8 = i * 2;
            }
        } else {
            i = Math.max(width / width2, height / height2);
        }
        if (i >= 1) {
            i3 = i;
        }
        return considerMaxTextureSize(width, height, i3, z);
    }

    public static float computeImageScale(ImageSize imageSize, ImageSize imageSize2, ViewScaleType viewScaleType, boolean z) {
        int width = imageSize.getWidth();
        int height = imageSize.getHeight();
        int width2 = imageSize2.getWidth();
        int height2 = imageSize2.getHeight();
        float f = (float) width;
        float f2 = f / ((float) width2);
        float f3 = (float) height;
        float f4 = f3 / ((float) height2);
        if ((viewScaleType != ViewScaleType.FIT_INSIDE || f2 < f4) && (viewScaleType != ViewScaleType.CROP || f2 >= f4)) {
            width2 = (int) (f / f4);
        } else {
            height2 = (int) (f3 / f2);
        }
        if ((z || width2 >= width || height2 >= height) && (!z || width2 == width || height2 == height)) {
            return 1.0f;
        }
        return ((float) width2) / f;
    }

    public static int computeMinImageSampleSize(ImageSize imageSize) {
        int width = imageSize.getWidth();
        int height = imageSize.getHeight();
        return Math.max((int) Math.ceil((double) (((float) width) / ((float) maxBitmapSize.getWidth()))), (int) Math.ceil((double) (((float) height) / ((float) maxBitmapSize.getHeight()))));
    }

    private static int considerMaxTextureSize(int i, int i2, int i3, boolean z) {
        int width = maxBitmapSize.getWidth();
        int height = maxBitmapSize.getHeight();
        while (true) {
            if (i / i3 <= width && i2 / i3 <= height) {
                return i3;
            }
            i3 = z ? i3 * 2 : i3 + 1;
        }
    }

    public static ImageSize defineTargetSizeForView(ImageAware imageAware, ImageSize imageSize) {
        int i;
        int i2;
        int width = imageAware.getWidth();
        if (width <= 0) {
            i = imageSize.getWidth();
        } else {
            i = Math.min(width, imageSize.getWidth());
        }
        int height = imageAware.getHeight();
        if (height <= 0) {
            i2 = imageSize.getHeight();
        } else {
            i2 = Math.min(height, imageSize.getHeight());
        }
        return new ImageSize(i, i2);
    }

    static {
        int[] iArr = new int[1];
        GLES10.glGetIntegerv(3379, iArr, 0);
        int max = Math.max(iArr[0], 2048);
        maxBitmapSize = new ImageSize(max, max);
    }
}
