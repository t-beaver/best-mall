package io.dcloud.feature.barcode2.decoding;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.dcloud.zxing2.BinaryBitmap;
import com.dcloud.zxing2.DecodeHintType;
import com.dcloud.zxing2.MultiFormatReader;
import com.dcloud.zxing2.NotFoundException;
import com.dcloud.zxing2.ReaderException;
import com.dcloud.zxing2.Result;
import com.dcloud.zxing2.ResultPointCallback;
import com.dcloud.zxing2.common.GlobalHistogramBinarizer;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.feature.barcode2.BarcodeProxy;
import io.dcloud.feature.barcode2.camera.CameraManager;
import io.dcloud.feature.barcode2.camera.PlanarYUVLuminanceSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Vector;

final class DecodeHandler extends Handler {
    private static final String TAG = "DecodeHandler";
    private static boolean mIsVerticalScreen = true;
    private final IBarHandler activity;
    private final MultiFormatReader multiFormatReader;

    DecodeHandler(IBarHandler iBarHandler, Hashtable<DecodeHintType, Object> hashtable) {
        MultiFormatReader multiFormatReader2 = new MultiFormatReader(this);
        this.multiFormatReader = multiFormatReader2;
        multiFormatReader2.setHints(hashtable);
        this.activity = iBarHandler;
    }

    /* JADX INFO: finally extract failed */
    private void decode(byte[] bArr, int i, int i2) {
        Result result;
        PlanarYUVLuminanceSource buildLuminanceSource = CameraManager.get().buildLuminanceSource(bArr, i, i2);
        try {
            result = this.multiFormatReader.decodeWithState(new BinaryBitmap(new GlobalHistogramBinarizer(buildLuminanceSource)));
            this.multiFormatReader.reset();
        } catch (ReaderException unused) {
            this.multiFormatReader.reset();
            result = null;
        } catch (Throwable th) {
            this.multiFormatReader.reset();
            throw th;
        }
        Result result2 = result;
        if (BarcodeProxy.save) {
            Camera.Parameters parameters = CameraManager.get().getCameraHandler().getParameters();
            try {
                Camera.Size previewSize = parameters.getPreviewSize();
                byte[] bArr2 = bArr;
                YuvImage yuvImage = r5;
                YuvImage yuvImage2 = new YuvImage(bArr2, parameters.getPreviewFormat(), previewSize.width, previewSize.height, (int[]) null);
                FileOutputStream fileOutputStream = new FileOutputStream(new File("/sdcard/1/" + System.currentTimeMillis() + "--" + previewSize.width + "*" + previewSize.height + ".jpg"));
                yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 90, fileOutputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap renderCroppedGreyscaleBitmap = buildLuminanceSource.renderCroppedGreyscaleBitmap(true);
            Rect framingRectInPreview = CameraManager.get().getFramingRectInPreview();
            PdrUtil.saveBitmapToFile(renderCroppedGreyscaleBitmap, "/sdcard/1/" + System.currentTimeMillis() + "--" + framingRectInPreview.left + "*" + framingRectInPreview.top + ".png");
            BarcodeProxy.save = false;
            StringBuilder sb = new StringBuilder();
            sb.append("成功 left=");
            sb.append(framingRectInPreview.left);
            sb.append("top:");
            sb.append(framingRectInPreview.top);
            PdrUtil.alert((Activity) BarcodeProxy.context, sb.toString(), renderCroppedGreyscaleBitmap);
        }
        if (result2 != null) {
            Message obtain = Message.obtain(this.activity.getHandler(), 1002, result2);
            Bundle bundle = new Bundle();
            Bitmap renderCroppedGreyscaleBitmap2 = buildLuminanceSource.renderCroppedGreyscaleBitmap(true);
            if (!mIsVerticalScreen) {
                Matrix matrix = new Matrix();
                matrix.postRotate(-90.0f);
                Bitmap createScaledBitmap = Bitmap.createScaledBitmap(renderCroppedGreyscaleBitmap2, renderCroppedGreyscaleBitmap2.getWidth(), renderCroppedGreyscaleBitmap2.getHeight(), true);
                renderCroppedGreyscaleBitmap2 = Bitmap.createBitmap(createScaledBitmap, 0, 0, createScaledBitmap.getWidth(), createScaledBitmap.getHeight(), matrix, true);
            }
            bundle.putParcelable(DecodeThread.BARCODE_BITMAP, renderCroppedGreyscaleBitmap2);
            obtain.setData(bundle);
            obtain.sendToTarget();
            return;
        }
        Message.obtain(this.activity.getHandler(), 1001).sendToTarget();
    }

    private void handleNeedZoom() {
        Camera.Parameters parameters = CameraManager.get().getCameraHandler().getParameters();
        int zoom = parameters.getZoom();
        double maxZoom = (double) parameters.getMaxZoom();
        Double.isNaN(maxZoom);
        int i = (int) (maxZoom * 0.6d);
        double d = (double) i;
        Double.isNaN(d);
        int i2 = (int) (d * 0.2d);
        if (zoom < i) {
            zoom += i2;
        }
        if (zoom <= i) {
            i = zoom;
        }
        parameters.setZoom(i);
        CameraManager.get().getCameraHandler().setParameters(parameters);
    }

    public void handleMessage(Message message) {
        int i = message.what;
        if (i != 1010) {
            switch (i) {
                case 1003:
                    Looper.myLooper().quit();
                    return;
                case 1004:
                    decode((byte[]) message.obj, message.arg1, message.arg2);
                    return;
                case CaptureActivityHandler.CODE_DECODE_portrait /*1005*/:
                    mIsVerticalScreen = true;
                    decode((byte[]) message.obj, message.arg1, message.arg2);
                    return;
                case 1006:
                    mIsVerticalScreen = false;
                    decode((byte[]) message.obj, message.arg1, message.arg2);
                    return;
                default:
                    return;
            }
        } else if (CaptureActivityHandler.isAutoZoom) {
            handleNeedZoom();
        }
    }

    public static Result decode(Bitmap bitmap, ResultPointCallback resultPointCallback, boolean z) {
        MultiFormatReader multiFormatReader2 = new MultiFormatReader((Handler) null);
        Hashtable hashtable = new Hashtable(4);
        Vector vector = new Vector();
        if (vector.isEmpty()) {
            vector = new Vector();
            vector.addAll(DecodeFormatManager.ONE_D_FORMATS);
            vector.addAll(DecodeFormatManager.QR_CODE_FORMATS);
            vector.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
        }
        hashtable.put(DecodeHintType.POSSIBLE_FORMATS, vector);
        DecodeHintType decodeHintType = DecodeHintType.TRY_HARDER;
        Boolean bool = Boolean.TRUE;
        hashtable.put(decodeHintType, bool);
        if (resultPointCallback != null) {
            hashtable.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK, resultPointCallback);
        }
        if (z) {
            hashtable.put(DecodeHintType.autoDecodeCharset, bool);
        }
        multiFormatReader2.setHints(hashtable);
        try {
            return multiFormatReader2.decodeWithState(new BinaryBitmap(new GlobalHistogramBinarizer(new BitmapLuminanceSource(bitmap))));
        } catch (NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Result decode(Bitmap bitmap, boolean z) {
        return decode(bitmap, (ResultPointCallback) null, z);
    }
}
