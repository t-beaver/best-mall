package io.dcloud.common.ui.blur;

import android.graphics.Bitmap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NativeBlurProcess {
    final ExecutorService EXECUTOR;
    final int EXECUTOR_THREADS;

    private static class NativeTask implements Callable<Void> {
        private final Bitmap _bitmapOut;
        private final int _coreIndex;
        private final int _radius;
        private final int _round;
        private final int _totalCores;

        public NativeTask(Bitmap bitmap, int i, int i2, int i3, int i4) {
            this._bitmapOut = bitmap;
            this._radius = i;
            this._totalCores = i2;
            this._coreIndex = i3;
            this._round = i4;
        }

        public Void call() throws Exception {
            BlurNativeLib.blurBitmap(this._bitmapOut, this._radius, this._totalCores, this._coreIndex, this._round);
            return null;
        }
    }

    public NativeBlurProcess() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        this.EXECUTOR_THREADS = availableProcessors;
        this.EXECUTOR = Executors.newFixedThreadPool(availableProcessors);
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.graphics.Bitmap blur(android.graphics.Bitmap r12, float r13, boolean r14) {
        /*
            r11 = this;
            if (r14 == 0) goto L_0x0009
            android.graphics.Bitmap$Config r14 = android.graphics.Bitmap.Config.ARGB_8888
            r0 = 1
            android.graphics.Bitmap r12 = r12.copy(r14, r0)
        L_0x0009:
            int r14 = r11.EXECUTOR_THREADS
            java.util.ArrayList r6 = new java.util.ArrayList
            r6.<init>(r14)
            java.util.ArrayList r7 = new java.util.ArrayList
            r7.<init>(r14)
            r0 = 0
            r8 = 0
        L_0x0017:
            if (r8 >= r14) goto L_0x0035
            io.dcloud.common.ui.blur.NativeBlurProcess$NativeTask r9 = new io.dcloud.common.ui.blur.NativeBlurProcess$NativeTask
            int r10 = (int) r13
            r5 = 1
            r0 = r9
            r1 = r12
            r2 = r10
            r3 = r14
            r4 = r8
            r0.<init>(r1, r2, r3, r4, r5)
            r6.add(r9)
            io.dcloud.common.ui.blur.NativeBlurProcess$NativeTask r9 = new io.dcloud.common.ui.blur.NativeBlurProcess$NativeTask
            r5 = 2
            r0 = r9
            r0.<init>(r1, r2, r3, r4, r5)
            r7.add(r9)
            int r8 = r8 + 1
            goto L_0x0017
        L_0x0035:
            java.util.concurrent.ExecutorService r13 = r11.EXECUTOR     // Catch:{ InterruptedException -> 0x003f }
            r13.invokeAll(r6)     // Catch:{ InterruptedException -> 0x003f }
            java.util.concurrent.ExecutorService r13 = r11.EXECUTOR     // Catch:{  }
            r13.invokeAll(r7)     // Catch:{  }
        L_0x003f:
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.ui.blur.NativeBlurProcess.blur(android.graphics.Bitmap, float, boolean):android.graphics.Bitmap");
    }
}
