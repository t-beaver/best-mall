package io.dcloud.feature.barcode2.camera;

import android.graphics.Point;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import io.dcloud.feature.barcode2.decoding.CaptureActivityHandler;
import io.dcloud.feature.barcode2.decoding.IBarHandler;

final class PreviewCallback implements Camera.PreviewCallback {
    private static final String TAG = "PreviewCallback";
    private static boolean mIsVerticalScreen = true;
    private final CameraConfigurationManager configManager;
    private byte[] lastBitmapData = null;
    private IBarHandler mBarHandler;
    private Handler previewHandler;
    private int previewMessage;
    private final boolean useOneShotPreviewCallback;

    PreviewCallback(CameraConfigurationManager cameraConfigurationManager, boolean z) {
        this.configManager = cameraConfigurationManager;
        this.useOneShotPreviewCallback = z;
    }

    public byte[] getLastBitmapData() {
        return this.lastBitmapData;
    }

    public void onPreviewFrame(byte[] bArr, Camera camera) {
        Message message;
        IBarHandler iBarHandler = this.mBarHandler;
        if (iBarHandler != null && iBarHandler.isRunning()) {
            Point cameraResolution = this.configManager.getCameraResolution();
            if (!this.useOneShotPreviewCallback) {
                camera.setPreviewCallback((Camera.PreviewCallback) null);
            }
            Handler handler = this.previewHandler;
            if (handler != null) {
                int i = this.previewMessage;
                if (i != 1004) {
                    message = handler.obtainMessage(i, cameraResolution.x, cameraResolution.y, bArr);
                } else if (mIsVerticalScreen) {
                    message = handler.obtainMessage(CaptureActivityHandler.CODE_DECODE_portrait, cameraResolution.x, cameraResolution.y, bArr);
                } else {
                    message = handler.obtainMessage(1006, cameraResolution.x, cameraResolution.y, bArr);
                }
                message.sendToTarget();
                this.previewHandler = null;
            } else {
                Log.d(TAG, "Got preview callback, but no handler for it");
            }
            this.lastBitmapData = bArr;
        }
    }

    /* access modifiers changed from: package-private */
    public void setHandler(IBarHandler iBarHandler, Handler handler, int i, boolean z) {
        this.mBarHandler = iBarHandler;
        this.previewHandler = handler;
        this.previewMessage = i;
        mIsVerticalScreen = z;
    }

    public void setLastBitmapData(byte[] bArr) {
        this.lastBitmapData = bArr;
    }
}
