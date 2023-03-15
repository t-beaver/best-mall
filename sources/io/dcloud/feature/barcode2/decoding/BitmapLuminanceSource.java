package io.dcloud.feature.barcode2.decoding;

import android.graphics.Bitmap;
import com.dcloud.zxing2.LuminanceSource;

public class BitmapLuminanceSource extends LuminanceSource {
    private byte[] bitmapPixels;

    protected BitmapLuminanceSource(Bitmap bitmap) {
        super(bitmap.getWidth(), bitmap.getHeight());
        int width = bitmap.getWidth() * bitmap.getHeight();
        int[] iArr = new int[width];
        this.bitmapPixels = new byte[(bitmap.getWidth() * bitmap.getHeight())];
        bitmap.getPixels(iArr, 0, getWidth(), 0, 0, getWidth(), getHeight());
        for (int i = 0; i < width; i++) {
            this.bitmapPixels[i] = (byte) iArr[i];
        }
    }

    public byte[] getMatrix() {
        return this.bitmapPixels;
    }

    public byte[] getRow(int i, byte[] bArr) {
        System.arraycopy(this.bitmapPixels, i * getWidth(), bArr, 0, getWidth());
        return bArr;
    }
}
