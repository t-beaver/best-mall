package com.facebook.animated.webpdrawable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.facebook.animated.webp.WebPFrame;
import com.facebook.animated.webp.WebPImage;
import com.facebook.fresco.animation.backend.AnimationBackend;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import javax.annotation.Nullable;

public class WebpAnimationBackend implements AnimationBackend {
    private Rect mBounds;
    private final Rect mRenderDstRect = new Rect();
    private final Rect mRenderSrcRect = new Rect();
    @Nullable
    private Bitmap mTempBitmap;
    private final WebPImage mWebPImage;

    public int getSizeInBytes() {
        return 0;
    }

    public void setAlpha(int i) {
    }

    public void setColorFilter(@Nullable ColorFilter colorFilter) {
    }

    public static WebpAnimationBackend create(String str) throws IOException {
        BufferedInputStream bufferedInputStream = null;
        try {
            BufferedInputStream bufferedInputStream2 = new BufferedInputStream(new FileInputStream(str));
            try {
                bufferedInputStream2.mark(Integer.MAX_VALUE);
                byte[] bArr = new byte[bufferedInputStream2.available()];
                bufferedInputStream2.read(bArr);
                WebPImage createFromByteArray = WebPImage.createFromByteArray(bArr, (ImageDecodeOptions) null);
                bufferedInputStream2.reset();
                WebpAnimationBackend webpAnimationBackend = new WebpAnimationBackend(createFromByteArray);
                closeSilently(bufferedInputStream2);
                return webpAnimationBackend;
            } catch (Throwable th) {
                th = th;
                bufferedInputStream = bufferedInputStream2;
                closeSilently(bufferedInputStream);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            closeSilently(bufferedInputStream);
            throw th;
        }
    }

    private WebpAnimationBackend(WebPImage webPImage) {
        this.mWebPImage = webPImage;
    }

    public boolean drawFrame(Drawable drawable, Canvas canvas, int i) {
        WebPFrame frame = this.mWebPImage.getFrame(i);
        double width = (double) this.mBounds.width();
        double intrinsicWidth = (double) drawable.getIntrinsicWidth();
        Double.isNaN(width);
        Double.isNaN(intrinsicWidth);
        double d = width / intrinsicWidth;
        double height = (double) this.mBounds.height();
        double intrinsicHeight = (double) drawable.getIntrinsicHeight();
        Double.isNaN(height);
        Double.isNaN(intrinsicHeight);
        double d2 = height / intrinsicHeight;
        double width2 = (double) frame.getWidth();
        Double.isNaN(width2);
        int round = (int) Math.round(width2 * d);
        double height2 = (double) frame.getHeight();
        Double.isNaN(height2);
        int round2 = (int) Math.round(height2 * d2);
        double xOffset = (double) frame.getXOffset();
        Double.isNaN(xOffset);
        int i2 = (int) (xOffset * d);
        double yOffset = (double) frame.getYOffset();
        Double.isNaN(yOffset);
        int i3 = (int) (yOffset * d2);
        synchronized (this) {
            int width3 = this.mBounds.width();
            int height3 = this.mBounds.height();
            prepareTempBitmapForThisSize(width3, height3);
            Bitmap bitmap = this.mTempBitmap;
            if (bitmap == null) {
                return false;
            }
            frame.renderFrame(round, round2, bitmap);
            this.mRenderSrcRect.set(0, 0, width3, height3);
            this.mRenderDstRect.set(i2, i3, width3 + i2, height3 + i3);
            canvas.drawBitmap(this.mTempBitmap, this.mRenderSrcRect, this.mRenderDstRect, (Paint) null);
            return true;
        }
    }

    public synchronized void setBounds(Rect rect) {
        this.mBounds = rect;
    }

    public int getIntrinsicWidth() {
        return this.mWebPImage.getWidth();
    }

    public int getIntrinsicHeight() {
        return this.mWebPImage.getHeight();
    }

    public void clear() {
        this.mWebPImage.dispose();
    }

    public int getFrameCount() {
        return this.mWebPImage.getFrameCount();
    }

    public int getFrameDurationMs(int i) {
        return this.mWebPImage.getFrameDurations()[i];
    }

    public int getLoopCount() {
        return this.mWebPImage.getLoopCount();
    }

    private synchronized void prepareTempBitmapForThisSize(int i, int i2) {
        Bitmap bitmap = this.mTempBitmap;
        if (bitmap != null && (bitmap.getWidth() < i || this.mTempBitmap.getHeight() < i2)) {
            clearTempBitmap();
        }
        if (this.mTempBitmap == null) {
            this.mTempBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        }
        this.mTempBitmap.eraseColor(0);
    }

    private synchronized void clearTempBitmap() {
        Bitmap bitmap = this.mTempBitmap;
        if (bitmap != null) {
            bitmap.recycle();
            this.mTempBitmap = null;
        }
    }

    private static void closeSilently(@Nullable Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException unused) {
            }
        }
    }
}
