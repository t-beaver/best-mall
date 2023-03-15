package io.dcloud.common.ui.blur;

import android.graphics.Bitmap;

public class BlurManager {
    private static BlurManager sInstance;
    private NativeBlurProcess nativeBlurProcess;

    private BlurManager() {
        if (this.nativeBlurProcess == null) {
            this.nativeBlurProcess = new NativeBlurProcess();
        }
    }

    public static BlurManager getInstance() {
        if (sInstance == null) {
            sInstance = new BlurManager();
        }
        return sInstance;
    }

    public Bitmap processNatively(Bitmap bitmap, int i, boolean z) {
        if (this.nativeBlurProcess == null) {
            this.nativeBlurProcess = new NativeBlurProcess();
        }
        if (bitmap == null) {
            return null;
        }
        return this.nativeBlurProcess.blur(bitmap, (float) i, z);
    }
}
