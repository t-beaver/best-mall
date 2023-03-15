package com.nostra13.dcloudimageloader.core.decode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import com.nostra13.dcloudimageloader.core.assist.ImageScaleType;
import com.nostra13.dcloudimageloader.core.assist.ImageSize;
import com.nostra13.dcloudimageloader.core.download.ImageDownloader;
import com.nostra13.dcloudimageloader.utils.ImageSizeUtils;
import com.nostra13.dcloudimageloader.utils.IoUtils;
import com.nostra13.dcloudimageloader.utils.L;
import java.io.IOException;
import java.io.InputStream;

public class BaseImageDecoder implements ImageDecoder {
    protected static final String ERROR_CANT_DECODE_IMAGE = "Image can't be decoded [%s]";
    protected static final String ERROR_NO_IMAGE_STREAM = "No stream for image [%s]";
    protected static final String LOG_FLIP_IMAGE = "Flip image horizontally [%s]";
    protected static final String LOG_ROTATE_IMAGE = "Rotate image on %1$d° [%2$s]";
    protected static final String LOG_SCALE_IMAGE = "Scale subsampled image (%1$s) to %2$s (scale = %3$.5f) [%4$s]";
    protected static final String LOG_SUBSAMPLE_IMAGE = "Subsample original image (%1$s) to %2$s (scale = %3$d) [%4$s]";
    protected final boolean loggingEnabled;

    protected static class ImageFileInfo {
        public final ExifInfo exif;
        public final ImageSize imageSize;

        protected ImageFileInfo(ImageSize imageSize2, ExifInfo exifInfo) {
            this.imageSize = imageSize2;
            this.exif = exifInfo;
        }
    }

    public BaseImageDecoder(boolean z) {
        this.loggingEnabled = z;
    }

    private boolean canDefineExifParams(String str, String str2) {
        return Build.VERSION.SDK_INT >= 5 && "image/jpeg".equalsIgnoreCase(str2) && ImageDownloader.Scheme.ofUri(str) == ImageDownloader.Scheme.FILE;
    }

    /* access modifiers changed from: protected */
    public Bitmap considerExactScaleAndOrientaiton(Bitmap bitmap, ImageDecodingInfo imageDecodingInfo, int i, boolean z) {
        Matrix matrix = new Matrix();
        ImageScaleType imageScaleType = imageDecodingInfo.getImageScaleType();
        if (imageScaleType == ImageScaleType.EXACTLY || imageScaleType == ImageScaleType.EXACTLY_STRETCHED) {
            ImageSize imageSize = new ImageSize(bitmap.getWidth(), bitmap.getHeight(), i);
            float computeImageScale = ImageSizeUtils.computeImageScale(imageSize, imageDecodingInfo.getTargetSize(), imageDecodingInfo.getViewScaleType(), imageScaleType == ImageScaleType.EXACTLY_STRETCHED);
            if (Float.compare(computeImageScale, 1.0f) != 0) {
                matrix.setScale(computeImageScale, computeImageScale);
                if (this.loggingEnabled) {
                    L.d(LOG_SCALE_IMAGE, imageSize, imageSize.scale(computeImageScale), Float.valueOf(computeImageScale), imageDecodingInfo.getImageKey());
                }
            }
        }
        if (z) {
            matrix.postScale(-1.0f, 1.0f);
            if (this.loggingEnabled) {
                L.d(LOG_FLIP_IMAGE, imageDecodingInfo.getImageKey());
            }
        }
        if (i != 0) {
            matrix.postRotate((float) i);
            if (this.loggingEnabled) {
                L.d("Rotate image on %1$d�� [%2$s]", Integer.valueOf(i), imageDecodingInfo.getImageKey());
            }
        }
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (createBitmap != bitmap) {
            bitmap.recycle();
        }
        return createBitmap;
    }

    public Bitmap decode(ImageDecodingInfo imageDecodingInfo) throws IOException {
        InputStream imageStream = getImageStream(imageDecodingInfo);
        ImageFileInfo defineImageSizeAndRotation = defineImageSizeAndRotation(imageStream, imageDecodingInfo);
        Bitmap decodeStream = decodeStream(resetStream(imageStream, imageDecodingInfo), prepareDecodingOptions(defineImageSizeAndRotation.imageSize, imageDecodingInfo));
        if (decodeStream == null) {
            L.e(ERROR_CANT_DECODE_IMAGE, imageDecodingInfo.getImageKey());
            return decodeStream;
        }
        ExifInfo exifInfo = defineImageSizeAndRotation.exif;
        return considerExactScaleAndOrientaiton(decodeStream, imageDecodingInfo, exifInfo.rotation, exifInfo.flipHorizontal);
    }

    /* access modifiers changed from: protected */
    public Bitmap decodeStream(InputStream inputStream, BitmapFactory.Options options) throws IOException {
        try {
            return BitmapFactory.decodeStream(inputStream, (Rect) null, options);
        } finally {
            IoUtils.closeSilently(inputStream);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v0, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v1, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v2, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v2, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v4, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v3, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v4, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v5, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v5, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v6, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v7, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v8, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v6, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v9, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v10, resolved type: int} */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0018, code lost:
        r1 = r0;
        r0 = 90;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x001f, code lost:
        r1 = r0;
        r0 = 270;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0026, code lost:
        r1 = r0;
        r0 = 180;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.nostra13.dcloudimageloader.core.decode.BaseImageDecoder.ExifInfo defineExifOrientation(java.lang.String r5) {
        /*
            r4 = this;
            r0 = 0
            r1 = 1
            android.media.ExifInterface r2 = new android.media.ExifInterface     // Catch:{ IOException -> 0x002c }
            com.nostra13.dcloudimageloader.core.download.ImageDownloader$Scheme r3 = com.nostra13.dcloudimageloader.core.download.ImageDownloader.Scheme.FILE     // Catch:{ IOException -> 0x002c }
            java.lang.String r3 = r3.crop(r5)     // Catch:{ IOException -> 0x002c }
            r2.<init>(r3)     // Catch:{ IOException -> 0x002c }
            java.lang.String r3 = "Orientation"
            int r5 = r2.getAttributeInt(r3, r1)     // Catch:{ IOException -> 0x002c }
            switch(r5) {
                case 1: goto L_0x0035;
                case 2: goto L_0x0036;
                case 3: goto L_0x0026;
                case 4: goto L_0x0025;
                case 5: goto L_0x001e;
                case 6: goto L_0x0018;
                case 7: goto L_0x0017;
                case 8: goto L_0x001f;
                default: goto L_0x0016;
            }
        L_0x0016:
            goto L_0x0035
        L_0x0017:
            r0 = 1
        L_0x0018:
            r5 = 90
            r1 = r0
            r0 = 90
            goto L_0x0036
        L_0x001e:
            r0 = 1
        L_0x001f:
            r5 = 270(0x10e, float:3.78E-43)
            r1 = r0
            r0 = 270(0x10e, float:3.78E-43)
            goto L_0x0036
        L_0x0025:
            r0 = 1
        L_0x0026:
            r5 = 180(0xb4, float:2.52E-43)
            r1 = r0
            r0 = 180(0xb4, float:2.52E-43)
            goto L_0x0036
        L_0x002c:
            java.lang.Object[] r1 = new java.lang.Object[r1]
            r1[r0] = r5
            java.lang.String r5 = "Can't read EXIF tags from file [%s]"
            com.nostra13.dcloudimageloader.utils.L.w(r5, r1)
        L_0x0035:
            r1 = 0
        L_0x0036:
            com.nostra13.dcloudimageloader.core.decode.BaseImageDecoder$ExifInfo r5 = new com.nostra13.dcloudimageloader.core.decode.BaseImageDecoder$ExifInfo
            r5.<init>(r0, r1)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nostra13.dcloudimageloader.core.decode.BaseImageDecoder.defineExifOrientation(java.lang.String):com.nostra13.dcloudimageloader.core.decode.BaseImageDecoder$ExifInfo");
    }

    /* access modifiers changed from: protected */
    public ImageFileInfo defineImageSizeAndRotation(InputStream inputStream, ImageDecodingInfo imageDecodingInfo) throws IOException {
        ExifInfo exifInfo;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, (Rect) null, options);
        String imageUri = imageDecodingInfo.getImageUri();
        if (!imageDecodingInfo.shouldConsiderExifParams() || !canDefineExifParams(imageUri, options.outMimeType)) {
            exifInfo = new ExifInfo();
        } else {
            exifInfo = defineExifOrientation(imageUri);
        }
        return new ImageFileInfo(new ImageSize(options.outWidth, options.outHeight, exifInfo.rotation), exifInfo);
    }

    /* access modifiers changed from: protected */
    public InputStream getImageStream(ImageDecodingInfo imageDecodingInfo) throws IOException {
        return imageDecodingInfo.getDownloader().getStream(imageDecodingInfo.getImageUri(), imageDecodingInfo.getExtraForDownloader());
    }

    /* access modifiers changed from: protected */
    public BitmapFactory.Options prepareDecodingOptions(ImageSize imageSize, ImageDecodingInfo imageDecodingInfo) {
        ImageScaleType imageScaleType = imageDecodingInfo.getImageScaleType();
        ImageSize targetSize = imageDecodingInfo.getTargetSize();
        int i = 1;
        if (imageScaleType != ImageScaleType.NONE) {
            int computeImageSampleSize = ImageSizeUtils.computeImageSampleSize(imageSize, targetSize, imageDecodingInfo.getViewScaleType(), imageScaleType == ImageScaleType.IN_SAMPLE_POWER_OF_2);
            if (this.loggingEnabled) {
                L.d(LOG_SUBSAMPLE_IMAGE, imageSize, imageSize.scaleDown(computeImageSampleSize), Integer.valueOf(computeImageSampleSize), imageDecodingInfo.getImageKey());
            }
            i = computeImageSampleSize;
        }
        BitmapFactory.Options decodingOptions = imageDecodingInfo.getDecodingOptions();
        decodingOptions.inSampleSize = i;
        return decodingOptions;
    }

    /* access modifiers changed from: protected */
    public InputStream resetStream(InputStream inputStream, ImageDecodingInfo imageDecodingInfo) throws IOException {
        try {
            inputStream.reset();
            return inputStream;
        } catch (IOException unused) {
            return getImageStream(imageDecodingInfo);
        }
    }

    protected static class ExifInfo {
        public final boolean flipHorizontal;
        public final int rotation;

        protected ExifInfo() {
            this.rotation = 0;
            this.flipHorizontal = false;
        }

        protected ExifInfo(int i, boolean z) {
            this.rotation = i;
            this.flipHorizontal = z;
        }
    }
}
