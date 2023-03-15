package io.dcloud.feature.barcode2.camera;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import io.dcloud.feature.barcode2.decoding.IBarHandler;
import io.dcloud.feature.barcode2.view.DetectorViewConfig;
import io.dcloud.feature.gg.dcloud.ADSim;
import java.io.IOException;

public final class CameraManager {
    private static final int MAX_FRAME_HEIGHT = 640;
    private static final int MAX_FRAME_WIDTH = 640;
    private static final int MIN_FRAME_HEIGHT = 240;
    private static final int MIN_FRAME_WIDTH = 240;
    static final int SDK_INT;
    private static final String TAG = "CameraManager";
    private static CameraManager cameraManager;
    private static boolean mIsVerticalScreen = true;
    private static Camera.Parameters parameters = null;
    public static int sScreenAllHeight;
    public static int sScreenWidth;
    private final AutoFocusCallback autoFocusCallback;
    private Camera camera;
    private final CameraConfigurationManager configManager;
    private final Context context;
    private Rect framingRect;
    private boolean horizontalOrientation = false;
    private boolean initialized;
    private final PreviewCallback previewCallback;
    private boolean previewing;
    private boolean useOneShotPreviewCallback;

    static {
        int i;
        try {
            i = Integer.parseInt(Build.VERSION.SDK);
        } catch (NumberFormatException unused) {
            i = ADSim.INTISPLSH;
        }
        SDK_INT = i;
    }

    private CameraManager(Context context2) {
        this.context = context2;
        CameraConfigurationManager cameraConfigurationManager = new CameraConfigurationManager(context2);
        this.configManager = cameraConfigurationManager;
        this.useOneShotPreviewCallback = Integer.parseInt(Build.VERSION.SDK) > 3;
        this.previewCallback = new PreviewCallback(cameraConfigurationManager, this.useOneShotPreviewCallback);
        this.useOneShotPreviewCallback = false;
        this.autoFocusCallback = new AutoFocusCallback();
    }

    public static CameraManager get() {
        return cameraManager;
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0032  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0038  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x004b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.graphics.Point getCR(int r6, int r7) {
        /*
            r0 = 0
            android.hardware.Camera r1 = android.hardware.Camera.open()     // Catch:{ Exception -> 0x0026 }
            android.hardware.Camera$Parameters r2 = parameters     // Catch:{ Exception -> 0x0023 }
            if (r2 != 0) goto L_0x000f
            android.hardware.Camera$Parameters r2 = r1.getParameters()     // Catch:{ Exception -> 0x0023 }
            parameters = r2     // Catch:{ Exception -> 0x0023 }
        L_0x000f:
            android.graphics.Point r2 = new android.graphics.Point     // Catch:{ Exception -> 0x0023 }
            r2.<init>(r6, r7)     // Catch:{ Exception -> 0x0023 }
            android.hardware.Camera$Parameters r3 = parameters     // Catch:{ Exception -> 0x0023 }
            android.graphics.Point r2 = io.dcloud.feature.barcode2.camera.CameraConfigurationManager.getCameraResolution(r3, r2)     // Catch:{ Exception -> 0x0023 }
            r1.release()     // Catch:{ Exception -> 0x001e }
            goto L_0x0052
        L_0x001e:
            r3 = move-exception
            r5 = r3
            r3 = r2
            r2 = r5
            goto L_0x0029
        L_0x0023:
            r2 = move-exception
            r3 = r0
            goto L_0x0029
        L_0x0026:
            r2 = move-exception
            r1 = r0
            r3 = r1
        L_0x0029:
            android.graphics.Point r4 = new android.graphics.Point
            r4.<init>(r6, r7)
            android.hardware.Camera$Parameters r6 = parameters
            if (r6 == 0) goto L_0x0038
            android.graphics.Point r6 = io.dcloud.feature.barcode2.camera.CameraConfigurationManager.getCameraResolution(r6, r4)
            r3 = r6
            goto L_0x0048
        L_0x0038:
            if (r1 == 0) goto L_0x0048
            android.hardware.Camera$Parameters r6 = r1.getParameters()     // Catch:{ Exception -> 0x0048 }
            parameters = r6     // Catch:{ Exception -> 0x0048 }
            android.graphics.Point r3 = io.dcloud.feature.barcode2.camera.CameraConfigurationManager.getCameraResolution(r6, r4)     // Catch:{ Exception -> 0x0048 }
            r1.release()     // Catch:{ Exception -> 0x0048 }
            goto L_0x0049
        L_0x0048:
            r0 = r1
        L_0x0049:
            if (r0 == 0) goto L_0x004e
            r0.release()
        L_0x004e:
            r2.printStackTrace()
            r2 = r3
        L_0x0052:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.barcode2.camera.CameraManager.getCR(int, int):android.graphics.Point");
    }

    public static void init(Context context2) {
        if (cameraManager == null) {
            cameraManager = new CameraManager(context2);
        }
    }

    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] bArr, int i, int i2) {
        Rect framingRectInPreview = getFramingRectInPreview();
        int previewFormat = this.configManager.getPreviewFormat();
        String previewFormatString = this.configManager.getPreviewFormatString();
        if (previewFormat == 16 || previewFormat == 17) {
            return new PlanarYUVLuminanceSource(bArr, i, i2, framingRectInPreview.left, framingRectInPreview.top, framingRectInPreview.width(), framingRectInPreview.height());
        } else if ("yuv420p".equals(previewFormatString)) {
            return new PlanarYUVLuminanceSource(bArr, i, i2, framingRectInPreview.left, framingRectInPreview.top, framingRectInPreview.width(), framingRectInPreview.height());
        } else {
            throw new IllegalArgumentException("Unsupported picture format: " + previewFormat + '/' + previewFormatString);
        }
    }

    public void clearLastBitmapData() {
        PreviewCallback previewCallback2 = this.previewCallback;
        if (previewCallback2 != null) {
            previewCallback2.setLastBitmapData((byte[]) null);
        }
    }

    public void closeDriver() {
        if (this.camera != null) {
            FlashlightManager.disableFlashlight();
            this.camera.setPreviewCallback((Camera.PreviewCallback) null);
            this.camera.release();
            this.camera = null;
        }
    }

    public AutoFocusCallback getAutoFocusCallback() {
        return this.autoFocusCallback;
    }

    public Camera getCameraHandler() {
        return this.camera;
    }

    public Rect getFramingRectInPreview() {
        if (mIsVerticalScreen) {
            return getPortraitFramingRectInPreview();
        }
        return getLandscapeFramingRectInPreview();
    }

    public Rect getLandscapeFramingRectInPreview() {
        Point cameraResolution = this.configManager.getCameraResolution();
        Rect detectorRect = DetectorViewConfig.getInstance().getDetectorRect();
        Rect rect = DetectorViewConfig.getInstance().surfaceViewRect;
        int width = rect.width() / cameraResolution.y;
        int width2 = ((detectorRect.left - DetectorViewConfig.detectorRectOffestLeft) * cameraResolution.x) / rect.width();
        int height = ((rect.bottom - detectorRect.bottom) * cameraResolution.y) / rect.height();
        return new Rect(width2, height, ((detectorRect.height() * cameraResolution.y) / rect.height()) + width2, ((detectorRect.width() * cameraResolution.y) / rect.height()) + height);
    }

    public byte[] getLastBitmapData() {
        PreviewCallback previewCallback2 = this.previewCallback;
        if (previewCallback2 == null) {
            return null;
        }
        return previewCallback2.getLastBitmapData();
    }

    public Rect getPortraitFramingRectInPreview() {
        Point cameraResolution = this.configManager.getCameraResolution();
        Rect detectorRect = DetectorViewConfig.getInstance().getDetectorRect();
        Rect rect = DetectorViewConfig.getInstance().surfaceViewRect;
        int width = rect.width() / cameraResolution.y;
        int height = ((detectorRect.top - DetectorViewConfig.detectorRectOffestTop) * cameraResolution.x) / rect.height();
        int width2 = ((rect.right - detectorRect.right) * cameraResolution.y) / rect.width();
        return new Rect(height, width2, ((detectorRect.height() * cameraResolution.x) / rect.height()) + height, ((detectorRect.width() * cameraResolution.x) / rect.height()) + width2);
    }

    public void openDriver(SurfaceTexture surfaceTexture) throws IOException, RuntimeException {
        if (this.camera == null) {
            try {
                Camera open = Camera.open();
                this.camera = open;
                if (open != null) {
                    open.setPreviewTexture(surfaceTexture);
                    if (mIsVerticalScreen) {
                        this.camera.setDisplayOrientation(90);
                    } else {
                        this.camera.setDisplayOrientation(this.horizontalOrientation ? 180 : 0);
                    }
                    this.configManager.initFromCameraParameters(this.camera);
                    this.configManager.setDesiredCameraParameters(this.camera);
                    return;
                }
                throw new IOException();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void removeAutoFocus() {
        Camera camera2 = this.camera;
        if (camera2 != null) {
            camera2.cancelAutoFocus();
        }
    }

    public void requestAutoFocus(Handler handler, int i) {
        if (this.camera != null && this.previewing) {
            this.autoFocusCallback.setHandler(handler, i);
            try {
                this.camera.autoFocus(this.autoFocusCallback);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void requestPreviewFrame(IBarHandler iBarHandler, Handler handler, int i) {
        if (this.camera != null && this.previewing) {
            this.previewCallback.setHandler(iBarHandler, handler, i, mIsVerticalScreen);
            if (this.useOneShotPreviewCallback) {
                this.camera.setOneShotPreviewCallback(this.previewCallback);
            } else {
                this.camera.setPreviewCallback(this.previewCallback);
            }
        }
    }

    public void setFlashlight(boolean z) {
        if (z) {
            FlashlightManager.enableFlashlight();
        } else {
            FlashlightManager.disableFlashlight();
        }
    }

    public void setHorizontalOrientation(boolean z) {
        this.horizontalOrientation = z;
    }

    public void startPreview() {
        try {
            Camera camera2 = this.camera;
            if (camera2 != null && !this.previewing) {
                camera2.startPreview();
                this.previewing = true;
            }
        } catch (Exception unused) {
        }
    }

    public void stopPreview() {
        try {
            Camera camera2 = this.camera;
            if (camera2 != null && this.previewing) {
                if (!this.useOneShotPreviewCallback) {
                    camera2.setPreviewCallback((Camera.PreviewCallback) null);
                }
                this.camera.stopPreview();
                this.previewCallback.setHandler((IBarHandler) null, (Handler) null, 0, mIsVerticalScreen);
                this.autoFocusCallback.setHandler((Handler) null, 0);
                this.previewing = false;
            }
        } catch (Exception unused) {
        }
    }

    public static void init(Context context2, boolean z) {
        mIsVerticalScreen = z;
        init(context2);
    }
}
