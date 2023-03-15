package io.dcloud.feature.weex_barcode;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.dcloud.zxing2.BarcodeFormat;
import com.dcloud.zxing2.Result;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.Constants;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXImage;
import io.dcloud.common.adapter.util.PermissionUtil;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.ThreadPool;
import io.dcloud.feature.barcode2.camera.CameraManager;
import io.dcloud.feature.barcode2.decoding.CaptureActivityHandler;
import io.dcloud.feature.barcode2.decoding.IBarHandler;
import io.dcloud.feature.barcode2.decoding.InactivityTimer;
import io.dcloud.feature.barcode2.view.DetectorViewConfig;
import io.dcloud.feature.barcode2.view.ViewfinderView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class BarcodeView extends AbsoluteLayout implements IBarHandler, TextureView.SurfaceTextureListener {
    static final int AZTEC = 3;
    private static final float BEEP_VOLUME = 0.8f;
    static final int CODABAR = 7;
    static final int CODE128 = 10;
    static final int CODE39 = 8;
    static final int CODE93 = 9;
    static final int DATAMATRIX = 4;
    static final int EAN13 = 1;
    static final int EAN8 = 2;
    private static final int ID_ADD_VIEW = 201;
    private static final int ID_START_SCAN = 203;
    private static final int ID_UPDATE_VIEW = 202;
    static final int ITF = 11;
    static final int MAXICODE = 12;
    static final int PDF417 = 13;
    static final int QR = 0;
    static final int RSS14 = 14;
    static final int RSSEXPANDED = 15;
    static final int UNKOWN = -1000;
    static final int UPCA = 5;
    static final int UPCE = 6;
    private static final long VIBRATE_DURATION = 200;
    public boolean autoDecodeCharset = false;
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };
    private String characterSet;
    private WXComponent component;
    private Context context;
    private Vector<BarcodeFormat> decodeFormats;
    public String errorMsg = null;
    private CaptureActivityHandler handler;
    private boolean hasSurface = false;
    private InactivityTimer inactivityTimer;
    private boolean isCancelScan = false;
    /* access modifiers changed from: private */
    public boolean isSurfaceAvaliable = false;
    private boolean isVerticalScreen = false;
    private Bitmap lastBiptmap;
    private boolean mConserve = false;
    private String mFilename;
    Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == 201) {
                AbsoluteLayout.LayoutParams layoutParams = (AbsoluteLayout.LayoutParams) message.obj;
                if (BarcodeView.this.surfaceView != null) {
                    if (BarcodeView.this.surfaceView.getParent() != null) {
                        ((ViewGroup) BarcodeView.this.surfaceView.getParent()).removeView(BarcodeView.this.surfaceView);
                    }
                    BarcodeView barcodeView = BarcodeView.this;
                    barcodeView.addView(barcodeView.surfaceView, layoutParams);
                }
                BarcodeView barcodeView2 = BarcodeView.this;
                barcodeView2.addView(barcodeView2.viewfinderView);
            } else if (message.what == BarcodeView.ID_UPDATE_VIEW) {
                if (BarcodeView.this.surfaceView != null && BarcodeView.this.surfaceView.getParent() != null) {
                    BarcodeView.this.surfaceView.setLayoutParams((AbsoluteLayout.LayoutParams) message.obj);
                    BarcodeView.this.viewfinderView.drawViewfinder();
                }
            } else if (message.what == BarcodeView.ID_START_SCAN && BarcodeView.this.surfaceView != null && BarcodeView.this.surfaceView.getParent() != null) {
                BarcodeView.this.surfaceView.setLayoutParams((AbsoluteLayout.LayoutParams) message.obj);
                BarcodeView.this.viewfinderView.drawViewfinder();
                BarcodeView.this.startP();
            }
        }
    };
    private WXSDKInstance mInstance;
    int mOrientationState;
    private boolean mRunning = false;
    private MediaPlayer mediaPlayer;
    /* access modifiers changed from: private */
    public boolean nopermission;
    private boolean playBeep;
    /* access modifiers changed from: private */
    public TextureView surfaceView;
    private boolean vibrate;
    private int viewHeight;
    private int viewWidth;
    /* access modifiers changed from: private */
    public ViewfinderView viewfinderView;

    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    public BarcodeView(Context context2, WXComponent wXComponent, WXSDKInstance wXSDKInstance) {
        super(context2);
        this.component = wXComponent;
        this.mInstance = wXSDKInstance;
        this.surfaceView = new TextureView(context2);
        this.viewfinderView = new ViewfinderView(context2, this);
        Activity activity = (Activity) context2;
        this.inactivityTimer = new InactivityTimer(activity);
        this.context = context2;
        saveOrientationState();
        CameraManager.init(this.context, false);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        if (rotation != 0) {
            if (rotation == 1) {
                CameraManager.get().setHorizontalOrientation(false);
                ((Activity) this.context).setRequestedOrientation(0);
            } else if (rotation != 2) {
                if (rotation == 3) {
                    CameraManager.get().setHorizontalOrientation(true);
                    ((Activity) this.context).setRequestedOrientation(8);
                }
            }
            onResume(false);
            this.hasSurface = false;
        }
        ((Activity) this.context).setRequestedOrientation(7);
        CameraManager.init(context2, true);
        this.isVerticalScreen = true;
        onResume(false);
        this.hasSurface = false;
    }

    private void saveOrientationState() {
        this.mOrientationState = ((Activity) this.context).getRequestedOrientation();
    }

    private void resumeOrientationState() {
        ((Activity) this.context).setRequestedOrientation(this.mOrientationState);
    }

    public void initBarcodeView(int i, int i2) {
        this.viewWidth = i;
        this.viewHeight = i2;
        ThreadPool.self().addThreadTask(new Runnable() {
            public void run() {
                BarcodeView.this.addBarcodeView();
            }
        });
    }

    public void updateStyles(int i, int i2) {
        if (this.viewHeight != i2 || this.viewWidth != i) {
            this.viewWidth = i;
            this.viewHeight = i2;
            ThreadPool.self().addThreadTask(new Runnable() {
                public void run() {
                    AbsoluteLayout.LayoutParams access$400 = BarcodeView.this.setLayoutParams();
                    if (access$400 != null) {
                        Message message = new Message();
                        message.what = BarcodeView.ID_UPDATE_VIEW;
                        message.obj = access$400;
                        BarcodeView.this.mHandler.sendMessage(message);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void addBarcodeView() {
        AbsoluteLayout.LayoutParams layoutParams = setLayoutParams();
        if (layoutParams != null) {
            Message message = new Message();
            message.what = 201;
            message.obj = layoutParams;
            this.mHandler.sendMessage(message);
        }
    }

    private void initCamera() {
        try {
            CameraManager.get().openDriver(this.surfaceView.getSurfaceTexture());
            CaptureActivityHandler captureActivityHandler = this.handler;
            if (captureActivityHandler == null) {
                CaptureActivityHandler captureActivityHandler2 = new CaptureActivityHandler(this, this.decodeFormats, this.characterSet, this.autoDecodeCharset);
                this.handler = captureActivityHandler2;
                if (this.mRunning) {
                    captureActivityHandler2.restartPreviewAndDecode();
                    return;
                }
                return;
            }
            captureActivityHandler.resume();
        } catch (IOException e) {
            this.errorMsg = e.getMessage();
        }
    }

    public void setFrameColor(int i) {
        if (i == -1) {
            i = DetectorViewConfig.laserColor;
        }
        DetectorViewConfig.cornerColor = i;
    }

    public void setAutoDecodeCharset(boolean z) {
        this.autoDecodeCharset = z;
    }

    public void setAutoZoom(boolean z) {
        CaptureActivityHandler.isAutoZoom = z;
    }

    public void setBackground(int i) {
        if (i == -1) {
            i = DetectorViewConfig.laserColor;
        }
        setBackgroundColor(i);
    }

    public void setScanBarColor(int i) {
        if (i == -1) {
            i = DetectorViewConfig.laserColor;
        }
        DetectorViewConfig.laserColor = i;
    }

    public void setPlayBeep(boolean z) {
        this.playBeep = z;
    }

    public void setVibrate(boolean z) {
        this.vibrate = z;
    }

    public void setConserve(boolean z) {
        this.mConserve = z;
    }

    public void setFilename(String str) {
        this.mFilename = str;
    }

    public void setFlash(boolean z) {
        CameraManager.get().setFlashlight(z);
    }

    public void cancelScan() {
        if (this.mRunning) {
            CaptureActivityHandler captureActivityHandler = this.handler;
            if (captureActivityHandler != null) {
                captureActivityHandler.quitSynchronously();
                this.handler = null;
            }
            getViewfinderView().stopUpdateScreenTimer();
            CameraManager.get().removeAutoFocus();
            CameraManager.get().stopPreview();
            byte[] lastBitmapData = CameraManager.get().getLastBitmapData();
            Camera cameraHandler = CameraManager.get().getCameraHandler();
            if (!(lastBitmapData == null || cameraHandler == null)) {
                this.lastBiptmap = byte2bitmap(lastBitmapData, cameraHandler);
            }
            CameraManager.get().closeDriver();
            this.mRunning = false;
            this.isCancelScan = true;
        }
    }

    public void closeScan() {
        onPause();
        CameraManager.get().closeDriver();
        DetectorViewConfig.clearData();
        this.surfaceView = null;
        Bitmap bitmap = this.lastBiptmap;
        if (bitmap != null && !bitmap.isRecycled()) {
            this.lastBiptmap.recycle();
            this.lastBiptmap = null;
        }
        CameraManager.get().clearLastBitmapData();
        System.gc();
    }

    public void onPause() {
        CaptureActivityHandler captureActivityHandler = this.handler;
        if (captureActivityHandler != null) {
            captureActivityHandler.quitSynchronously();
            this.handler = null;
        }
        if (!this.nopermission) {
            CameraManager.get().closeDriver();
        }
        boolean z = this.mRunning;
        cancel();
        this.mRunning = z;
    }

    public void onDestory() {
        resumeOrientationState();
        this.inactivityTimer.shutdown();
        this.hasSurface = false;
        this.decodeFormats = null;
        this.characterSet = null;
    }

    private Bitmap byte2bitmap(byte[] bArr, Camera camera) {
        try {
            Camera.Size previewSize = camera.getParameters().getPreviewSize();
            YuvImage yuvImage = new YuvImage(bArr, 17, previewSize.width, previewSize.height, (int[]) null);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            yuvImage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 80, byteArrayOutputStream);
            Bitmap decodeByteArray = BitmapFactory.decodeByteArray(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.size());
            byteArrayOutputStream.close();
            Matrix matrix = new Matrix();
            matrix.postRotate(90.0f);
            return Bitmap.createBitmap(decodeByteArray, 0, 0, decodeByteArray.getWidth(), decodeByteArray.getHeight(), matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void start() {
        PermissionUtil.useSystemPermissions((Activity) this.context, new String[]{"android.permission.CAMERA"}, new PermissionUtil.Request() {
            public void onGranted(String str) {
                BarcodeView.this.postDelayed(new Runnable() {
                    public void run() {
                        if (!BarcodeView.this.isSurfaceAvaliable) {
                            BarcodeView.this.postDelayed(this, 100);
                            return;
                        }
                        AbsoluteLayout.LayoutParams access$400 = BarcodeView.this.setLayoutParams();
                        if (access$400 != null) {
                            Message message = new Message();
                            message.what = BarcodeView.ID_START_SCAN;
                            message.obj = access$400;
                            BarcodeView.this.mHandler.sendMessage(message);
                        }
                    }
                }, BarcodeView.this.isSurfaceAvaliable ? 0 : BarcodeView.VIBRATE_DURATION);
            }

            public void onDenied(String str) {
                boolean unused = BarcodeView.this.nopermission = true;
                BarcodeView.this.setBackground(-16777216);
                BarcodeView.this.invalidate();
            }
        });
    }

    /* access modifiers changed from: private */
    public void startP() {
        initCamera();
        if (this.decodeFormats == null) {
            initDecodeFormats((JSONArray) null);
        }
        if (!TextUtils.isEmpty(this.errorMsg)) {
            HashMap hashMap = new HashMap();
            hashMap.put("code", 8);
            hashMap.put("message", this.errorMsg);
            hashMap.put("type", Constants.Event.FAIL);
            fireEvent("error", hashMap);
        } else if (!this.mRunning) {
            getViewfinderView().startUpdateScreenTimer();
            CaptureActivityHandler captureActivityHandler = this.handler;
            if (captureActivityHandler != null) {
                captureActivityHandler.restartPreviewAndDecode();
            } else {
                onResume(false);
            }
            if (this.isCancelScan) {
                this.surfaceView.setBackgroundDrawable((Drawable) null);
                Bitmap bitmap = this.lastBiptmap;
                if (bitmap != null && !bitmap.isRecycled()) {
                    this.lastBiptmap.recycle();
                    this.lastBiptmap = null;
                }
                CameraManager.get().clearLastBitmapData();
                this.surfaceView.postInvalidate();
                initCamera();
            }
            this.mRunning = true;
            this.isCancelScan = false;
        }
    }

    public void onResume(boolean z) {
        if (this.lastBiptmap != null && this.isCancelScan && z) {
            this.surfaceView.setBackgroundDrawable(new BitmapDrawable(this.context.getResources(), this.lastBiptmap));
        }
        if (!this.hasSurface) {
            this.surfaceView.setSurfaceTextureListener(this);
        }
        if (((AudioManager) this.context.getSystemService("audio")).getRingerMode() != 2) {
            this.playBeep = false;
        }
        initBeepSound();
        if (z && this.mRunning) {
            this.mRunning = false;
            start();
        }
    }

    /* access modifiers changed from: private */
    public AbsoluteLayout.LayoutParams setLayoutParams() {
        Point point;
        int i;
        int i2;
        int i3;
        int i4;
        CameraManager.sScreenWidth = this.context.getResources().getDisplayMetrics().widthPixels;
        CameraManager.sScreenAllHeight = this.context.getResources().getDisplayMetrics().heightPixels;
        Rect rect = DetectorViewConfig.getInstance().gatherRect;
        int i5 = 0;
        rect.left = 0;
        rect.top = 0;
        rect.right = this.viewWidth;
        rect.bottom = this.viewHeight;
        if (this.isVerticalScreen) {
            point = CameraManager.getCR(rect.height(), rect.width());
        } else {
            point = CameraManager.getCR(rect.width(), rect.height());
        }
        if (point == null) {
            point = new Point(this.viewWidth, this.viewHeight);
        }
        if (this.isVerticalScreen) {
            i2 = this.viewWidth;
            int i6 = (point.x * i2) / point.y;
            i = this.viewHeight;
            if (i6 < i) {
                i2 = (point.y * i) / point.x;
                i4 = (this.viewWidth - i2) / 2;
                DetectorViewConfig.detectorRectOffestLeft = i4;
                DetectorViewConfig.detectorRectOffestTop = 0;
            } else {
                i = (point.x * i2) / point.y;
                i3 = (this.viewHeight - i) / 2;
                DetectorViewConfig.detectorRectOffestTop = i3;
                DetectorViewConfig.detectorRectOffestLeft = 0;
                AbsoluteLayout.LayoutParams layoutParams = new AbsoluteLayout.LayoutParams(i2, i, i5, i3);
                DetectorViewConfig.getInstance().initSurfaceViewRect(i5, i3, i2, i);
                return layoutParams;
            }
        } else {
            i = this.viewHeight;
            int i7 = (point.x * i) / point.y;
            int i8 = this.viewWidth;
            if (i7 < i8) {
                i = (point.y * i8) / point.x;
                i3 = (this.viewHeight - i) / 2;
                DetectorViewConfig.detectorRectOffestTop = i3;
                DetectorViewConfig.detectorRectOffestLeft = 0;
                i2 = i8;
                AbsoluteLayout.LayoutParams layoutParams2 = new AbsoluteLayout.LayoutParams(i2, i, i5, i3);
                DetectorViewConfig.getInstance().initSurfaceViewRect(i5, i3, i2, i);
                return layoutParams2;
            }
            i2 = (point.x * i) / point.y;
            i4 = (this.viewWidth - i2) / 2;
            DetectorViewConfig.detectorRectOffestLeft = i4;
            DetectorViewConfig.detectorRectOffestTop = 0;
        }
        i5 = i4;
        i3 = 0;
        AbsoluteLayout.LayoutParams layoutParams22 = new AbsoluteLayout.LayoutParams(i2, i, i5, i3);
        DetectorViewConfig.getInstance().initSurfaceViewRect(i5, i3, i2, i);
        return layoutParams22;
    }

    public void initDecodeFormats(JSONArray jSONArray) {
        int i;
        this.decodeFormats = new Vector<>();
        if (jSONArray == null || jSONArray.size() == 0) {
            this.decodeFormats.add(BarcodeFormat.EAN_13);
            this.decodeFormats.add(BarcodeFormat.EAN_8);
            this.decodeFormats.add(BarcodeFormat.QR_CODE);
            return;
        }
        int size = jSONArray.size();
        for (int i2 = 0; i2 < size; i2++) {
            try {
                i = jSONArray.getInteger(i2).intValue();
            } catch (JSONException e) {
                e.printStackTrace();
                i = -1;
            }
            if (i != -1) {
                this.decodeFormats.add(convertNumToBarcodeFormat(i));
            }
        }
    }

    private BarcodeFormat convertNumToBarcodeFormat(int i) {
        switch (i) {
            case 0:
                return BarcodeFormat.QR_CODE;
            case 1:
                return BarcodeFormat.EAN_13;
            case 2:
                return BarcodeFormat.EAN_8;
            case 3:
                return BarcodeFormat.AZTEC;
            case 4:
                return BarcodeFormat.DATA_MATRIX;
            case 5:
                return BarcodeFormat.UPC_A;
            case 6:
                return BarcodeFormat.UPC_E;
            case 7:
                return BarcodeFormat.CODABAR;
            case 8:
                return BarcodeFormat.CODE_39;
            case 9:
                return BarcodeFormat.CODE_93;
            case 10:
                return BarcodeFormat.CODE_128;
            case 11:
                return BarcodeFormat.ITF;
            case 12:
                return BarcodeFormat.MAXICODE;
            case 13:
                return BarcodeFormat.PDF_417;
            case 14:
                return BarcodeFormat.RSS_14;
            case 15:
                return BarcodeFormat.RSS_EXPANDED;
            default:
                return null;
        }
    }

    private int convertTypestrToNum(BarcodeFormat barcodeFormat) {
        if (barcodeFormat == BarcodeFormat.QR_CODE) {
            return 0;
        }
        if (barcodeFormat == BarcodeFormat.EAN_13) {
            return 1;
        }
        if (barcodeFormat == BarcodeFormat.EAN_8) {
            return 2;
        }
        if (barcodeFormat == BarcodeFormat.AZTEC) {
            return 3;
        }
        if (barcodeFormat == BarcodeFormat.DATA_MATRIX) {
            return 4;
        }
        if (barcodeFormat == BarcodeFormat.UPC_A) {
            return 5;
        }
        if (barcodeFormat == BarcodeFormat.UPC_E) {
            return 6;
        }
        if (barcodeFormat == BarcodeFormat.CODABAR) {
            return 7;
        }
        if (barcodeFormat == BarcodeFormat.CODE_39) {
            return 8;
        }
        if (barcodeFormat == BarcodeFormat.CODE_93) {
            return 9;
        }
        if (barcodeFormat == BarcodeFormat.CODE_128) {
            return 10;
        }
        if (barcodeFormat == BarcodeFormat.ITF) {
            return 11;
        }
        if (barcodeFormat == BarcodeFormat.MAXICODE) {
            return 12;
        }
        if (barcodeFormat == BarcodeFormat.PDF_417) {
            return 13;
        }
        if (barcodeFormat == BarcodeFormat.RSS_14) {
            return 14;
        }
        return barcodeFormat == BarcodeFormat.RSS_EXPANDED ? 15 : -1000;
    }

    public ViewfinderView getViewfinderView() {
        return this.viewfinderView;
    }

    public void autoFocus() {
        this.handler.autoFocus();
    }

    public void handleDecode(Result result, Bitmap bitmap) {
        boolean z;
        String str;
        this.inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        if (this.mConserve) {
            if (!PdrUtil.isEmpty(this.mFilename) && !PdrUtil.isDeviceRootDir(this.mFilename) && !this.mFilename.startsWith(BaseInfo.REL_PRIVATE_DOC_DIR)) {
                this.mFilename = BaseInfo.REL_PRIVATE_DOC_DIR + this.mFilename;
            }
            str = this.mInstance.rewriteUri(Uri.parse(this.mFilename), "image").getPath();
            z = PdrUtil.saveBitmapToFile(bitmap, str);
        } else {
            z = false;
            str = null;
        }
        int convertTypestrToNum = convertTypestrToNum(result.getBarcodeFormat());
        HashMap hashMap = new HashMap();
        hashMap.put("code", Integer.valueOf(convertTypestrToNum));
        hashMap.put("message", result.getText());
        if (z && !PdrUtil.isEmpty(str)) {
            hashMap.put("file", str);
        }
        hashMap.put("type", WXImage.SUCCEED);
        hashMap.put("charSet", result.textCharset);
        fireEvent("marked", hashMap);
        cancelScan();
    }

    private void cancel() {
        if (this.mRunning) {
            CaptureActivityHandler captureActivityHandler = this.handler;
            if (captureActivityHandler != null) {
                captureActivityHandler.stopDecode();
            }
            getViewfinderView().stopUpdateScreenTimer();
            this.mRunning = false;
        }
    }

    private void initBeepSound() {
        if (this.mediaPlayer == null) {
            ((Activity) this.context).setVolumeControlStream(3);
            MediaPlayer mediaPlayer2 = new MediaPlayer();
            this.mediaPlayer = mediaPlayer2;
            mediaPlayer2.setAudioStreamType(3);
            this.mediaPlayer.setOnCompletionListener(this.beepListener);
            try {
                AssetFileDescriptor openFd = this.context.getResources().getAssets().openFd(AbsoluteConst.RES_BEEP);
                this.mediaPlayer.setDataSource(openFd.getFileDescriptor(), openFd.getStartOffset(), openFd.getLength());
                openFd.close();
                this.mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                this.mediaPlayer.prepare();
            } catch (IOException unused) {
                this.mediaPlayer = null;
            }
        }
    }

    private void playBeepSoundAndVibrate() {
        MediaPlayer mediaPlayer2;
        if (this.playBeep && (mediaPlayer2 = this.mediaPlayer) != null) {
            mediaPlayer2.start();
        }
        if (this.vibrate) {
            try {
                ((Vibrator) this.context.getSystemService("vibrator")).vibrate(VIBRATE_DURATION);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void drawViewfinder() {
        this.viewfinderView.drawViewfinder();
    }

    public boolean isRunning() {
        return this.mRunning;
    }

    public Handler getHandler() {
        return this.handler;
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
        this.isSurfaceAvaliable = true;
        if (!this.hasSurface) {
            this.hasSurface = true;
        }
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        this.hasSurface = false;
        return false;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        String str;
        super.onDraw(canvas);
        if (this.nopermission) {
            TextPaint textPaint = new TextPaint();
            textPaint.setColor(-1);
            textPaint.setTextSize((float) PdrUtil.pxFromDp(18.0f, this.context.getResources().getDisplayMetrics()));
            textPaint.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            int i = (int) (((((float) this.viewHeight) / 2.0f) - (fontMetrics.top / 2.0f)) - (fontMetrics.bottom / 2.0f));
            int i2 = this.viewWidth / 2;
            if (Build.VERSION.SDK_INT >= 24) {
                str = getResources().getConfiguration().getLocales().get(0).getLanguage();
            } else {
                str = getResources().getConfiguration().locale.getLanguage();
            }
            if (str.equalsIgnoreCase("en")) {
                canvas.drawText("Need camera permission", (float) i2, (float) i, textPaint);
            } else {
                canvas.drawText("未获得相机权限", (float) i2, (float) i, textPaint);
            }
        }
    }

    private void fireEvent(String str, Map<String, Object> map) {
        if (this.component.containsEvent(str)) {
            HashMap hashMap = new HashMap();
            hashMap.put("detail", map);
            this.component.fireEvent(str, hashMap);
        }
    }
}
