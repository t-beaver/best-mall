package io.dcloud.common.ui.blur;

import android.graphics.Bitmap;

public class BlurNativeLib {
    static {
        System.loadLibrary("dcblur");
    }

    public static native void blurBitmap(Bitmap bitmap, int i, int i2, int i3, int i4);
}
