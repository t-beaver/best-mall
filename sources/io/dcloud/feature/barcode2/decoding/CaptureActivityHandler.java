package io.dcloud.feature.barcode2.decoding;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.dcloud.zxing2.BarcodeFormat;
import com.dcloud.zxing2.Result;
import com.dcloud.zxing2.ResultPoint;
import com.dcloud.zxing2.ResultPointCallback;
import io.dcloud.feature.barcode2.camera.CameraManager;
import io.dcloud.feature.barcode2.view.ViewfinderResultPointCallback;
import java.util.ArrayList;
import java.util.Vector;

public final class CaptureActivityHandler extends Handler {
    public static final int CODE_AUTO_FOCUS = 1000;
    public static final int CODE_DECODE = 1004;
    public static final int CODE_DECODE_FAILED = 1001;
    public static final int CODE_DECODE_NEED_ZOOM = 1010;
    public static final int CODE_DECODE_SUCCEEDED = 1002;
    public static final int CODE_DECODE_landscape = 1006;
    public static final int CODE_DECODE_portrait = 1005;
    public static final int CODE_QUIT = 1003;
    private static final String TAG = "CaptureActivityHandler";
    public static boolean isAutoZoom = true;
    private final IBarHandler activity;
    private final DecodeThread decodeThread;
    private State state = State.SUCCESS;

    private enum State {
        PREVIEW,
        SUCCESS,
        DONE
    }

    public CaptureActivityHandler(IBarHandler iBarHandler, Vector<BarcodeFormat> vector, String str, boolean z) {
        this.activity = iBarHandler;
        DecodeThread decodeThread2 = new DecodeThread(iBarHandler, vector, str, new ViewfinderResultPointCallback(iBarHandler.getViewfinderView()), z);
        this.decodeThread = decodeThread2;
        decodeThread2.start();
        resume();
    }

    public static Bitmap convertToBMW(Bitmap bitmap, int i, int i2, int i3) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] iArr = new int[(width * height)];
        bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
        for (int i4 = 0; i4 < height; i4++) {
            for (int i5 = 0; i5 < width; i5++) {
                int i6 = (width * i4) + i5;
                int i7 = iArr[i6];
                int i8 = (i7 & -16777216) >> 24;
                int i9 = (16711680 & i7) >> 16;
                int i10 = (65280 & i7) >> 8;
                int i11 = 255;
                int i12 = i7 & 255;
                int i13 = i9 > i3 ? 255 : 0;
                int i14 = i12 > i3 ? 255 : 0;
                if (i10 <= i3) {
                    i11 = 0;
                }
                iArr[i6] = i14 | (i8 << 24) | (i13 << 16) | (i11 << 8);
                if (iArr[i6] == -1) {
                    iArr[i6] = -1;
                } else {
                    iArr[i6] = -16777216;
                }
            }
        }
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        createBitmap.setPixels(iArr, 0, width, 0, 0, width, height);
        return ThumbnailUtils.extractThumbnail(createBitmap, i, i2);
    }

    public static Result decode(Bitmap bitmap, boolean z) {
        Result decode = DecodeHandler.decode(bitmap, z);
        if (decode != null) {
            return decode;
        }
        Result trySmallerBitmap = trySmallerBitmap(bitmap, true, z);
        if (trySmallerBitmap != null) {
            return trySmallerBitmap;
        }
        return tryBiggerBitmap(bitmap, z);
    }

    public static Result tryBiggerBitmap(Bitmap bitmap, boolean z) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width < 1200 && height < 1200) {
            Bitmap createBitmap = Bitmap.createBitmap(width + 200, height + 200, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            canvas.drawColor(-1);
            canvas.drawBitmap(bitmap, 100.0f, 100.0f, new Paint());
            bitmap = createBitmap;
        }
        return DecodeHandler.decode(convertToBMW(bitmap, bitmap.getWidth(), bitmap.getHeight(), 180), z);
    }

    public static Result trySmallerBitmap(Bitmap bitmap, boolean z, boolean z2) {
        Bitmap bitmap2;
        Result result;
        int i;
        Bitmap bitmap3;
        Result result2;
        int i2;
        Bitmap bitmap4;
        Result result3;
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width >= 200 && height >= 200) {
            boolean z3 = width <= height;
            if (z3) {
                bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, width, height / 2);
            } else {
                bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, width / 2, height);
            }
            final ArrayList arrayList = new ArrayList();
            if (z) {
                result = DecodeHandler.decode(bitmap2, (ResultPointCallback) new ResultPointCallback() {
                    public void foundPossibleResultPoint(ResultPoint resultPoint) {
                        arrayList.add(resultPoint);
                    }
                }, z2);
            } else {
                result = DecodeHandler.decode(bitmap2, z2);
            }
            if (result != null) {
                return result;
            }
            if (z) {
                i = arrayList.size();
                arrayList.clear();
            } else {
                i = 0;
            }
            if (z3) {
                int i3 = height / 2;
                bitmap3 = Bitmap.createBitmap(bitmap, 0, i3, width, i3);
            } else {
                int i4 = width / 2;
                bitmap3 = Bitmap.createBitmap(bitmap, i4, 0, i4, height);
            }
            if (z) {
                result2 = DecodeHandler.decode(bitmap3, (ResultPointCallback) new ResultPointCallback() {
                    public void foundPossibleResultPoint(ResultPoint resultPoint) {
                        arrayList.add(resultPoint);
                    }
                }, z2);
            } else {
                result2 = DecodeHandler.decode(bitmap3, z2);
            }
            if (result2 != null) {
                return result2;
            }
            if (z) {
                i2 = arrayList.size();
                arrayList.clear();
            } else {
                i2 = 0;
            }
            if (z3) {
                bitmap4 = Bitmap.createBitmap(bitmap, 0, height / 8, width, (height / 4) * 3);
            } else {
                bitmap4 = Bitmap.createBitmap(bitmap, width / 8, 0, (width / 4) * 3, height);
            }
            if (z) {
                result3 = DecodeHandler.decode(bitmap4, (ResultPointCallback) new ResultPointCallback() {
                    public void foundPossibleResultPoint(ResultPoint resultPoint) {
                        arrayList.add(resultPoint);
                    }
                }, z2);
            } else {
                result3 = DecodeHandler.decode(bitmap4, z2);
            }
            if (result3 != null) {
                return result3;
            }
            if (z) {
                int size = arrayList.size();
                arrayList.clear();
                if (i > size && i > i2) {
                    return trySmallerBitmap(bitmap2, false, z2);
                }
                if (size > i2 && size > i) {
                    return trySmallerBitmap(bitmap4, false, z2);
                }
                if (i2 > size && i2 > i) {
                    return trySmallerBitmap(bitmap3, false, z2);
                }
            }
        }
        return null;
    }

    public void autoFocus() {
        CameraManager.get().requestAutoFocus(this, 1000);
    }

    public void handleMessage(Message message) {
        Bitmap bitmap;
        switch (message.what) {
            case 1000:
                Log.d(TAG, "Got auto-focus message");
                if (this.state == State.PREVIEW) {
                    CameraManager.get().requestAutoFocus(this, 1000);
                    return;
                }
                return;
            case 1001:
                Log.d(TAG, "CODE_DECODE_FAILED");
                this.state = State.PREVIEW;
                CameraManager.get().requestPreviewFrame(this.activity, this.decodeThread.getHandler(), 1004);
                return;
            case 1002:
                Log.d(TAG, "Got decode succeeded message");
                this.state = State.SUCCESS;
                Bundle data = message.getData();
                if (data == null) {
                    bitmap = null;
                } else {
                    bitmap = (Bitmap) data.getParcelable(DecodeThread.BARCODE_BITMAP);
                }
                this.activity.handleDecode((Result) message.obj, bitmap);
                if (bitmap != null) {
                    bitmap.recycle();
                    System.out.println("barcode.recycle");
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void quitSynchronously() {
        this.state = State.DONE;
        CameraManager.get().stopPreview();
        Message.obtain(this.decodeThread.getHandler(), 1003).sendToTarget();
        try {
            this.decodeThread.join();
        } catch (InterruptedException unused) {
        }
        stopDecode();
    }

    public void restartPreviewAndDecode() {
        if (this.state == State.SUCCESS) {
            this.state = State.PREVIEW;
            CameraManager.get().requestPreviewFrame(this.activity, this.decodeThread.getHandler(), 1004);
            autoFocus();
        }
    }

    public void resume() {
        CameraManager.get().startPreview();
        this.activity.drawViewfinder();
        restartPreviewAndDecode();
    }

    public void stopDecode() {
        removeMessages(1002);
        removeMessages(1001);
        CameraManager.get().removeAutoFocus();
        this.state = State.SUCCESS;
    }
}
