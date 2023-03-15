package io.dcloud.feature.barcode2;

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
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import com.dcloud.zxing2.BarcodeFormat;
import com.dcloud.zxing2.Result;
import io.dcloud.application.DCLoudApplicationImpl;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IEventCallback;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameItem;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.adapter.util.CanvasHelper;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.MessageHandler;
import io.dcloud.common.adapter.util.PermissionUtil;
import io.dcloud.common.adapter.util.ViewOptions;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.StringUtil;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class BarcodeFrameItem extends AdaFrameItem implements TextureView.SurfaceTextureListener, IBarHandler {
    static final int AZTEC = 3;
    private static final float BEEP_VOLUME = 0.8f;
    static final int CODABAR = 7;
    static final int CODE128 = 10;
    static final int CODE39 = 8;
    static final int CODE93 = 9;
    static final int DATAMATRIX = 4;
    static final int EAN13 = 1;
    static final int EAN8 = 2;
    static final int ITF = 11;
    static final int MAXICODE = 12;
    static final int PDF417 = 13;
    static final int QR = 0;
    static final int RSS14 = 14;
    static final int RSSEXPANDED = 15;
    public static final String TAG = "BarcodeFrameItem";
    static final int UNKOWN = -1000;
    static final int UPCA = 5;
    static final int UPCE = 6;
    private static final long VIBRATE_DURATION = 200;
    static BarcodeFrameItem sBarcodeFrameItem;
    public boolean autoDecodeCharset = false;
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };
    private String characterSet;
    private Vector<BarcodeFormat> decodeFormats;
    public String errorMsg = null;
    private CaptureActivityHandler handler;
    private boolean hasSurface;
    private InactivityTimer inactivityTimer;
    private boolean isCancelScan = false;
    boolean isVerticalScreen = true;
    private Bitmap lastBitmap = null;
    /* access modifiers changed from: private */
    public Context mAct;
    private IApp mAppHandler;
    Map<String, String> mCallbackIds = null;
    boolean mConserve = false;
    private IWebview mContainerWebview;
    JSONArray mDivRectJson;
    String mFilename = null;
    JSONArray mFilters;
    int mOrientationState;
    private String mPosition = "static";
    BarcodeProxy mProxy;
    private boolean mRunning = false;
    JSONObject mStyles;
    public String mUuid;
    private IWebview mWebViewImpl;
    private MediaPlayer mediaPlayer;
    boolean noPermission = false;
    boolean playBeep = true;
    TextureView surfaceView;
    boolean vibrate = true;
    private ViewfinderView viewfinderView;

    public BarcodeFrameItem(BarcodeProxy barcodeProxy, IWebview iWebview, String str, JSONArray jSONArray, JSONArray jSONArray2, JSONObject jSONObject) {
        super(iWebview.getContext());
        sBarcodeFrameItem = this;
        this.mProxy = barcodeProxy;
        this.mUuid = str;
        this.mCallbackIds = new HashMap();
        this.mAct = iWebview.getContext();
        this.mWebViewImpl = iWebview;
        this.mContainerWebview = iWebview;
        this.mAppHandler = iWebview.obtainApp();
        this.mDivRectJson = jSONArray;
        this.mStyles = jSONObject;
        this.mFilters = jSONArray2;
        final AbsoluteLayout.LayoutParams frameLayoutParam = getFrameLayoutParam(jSONArray, jSONObject);
        AnonymousClass1 r6 = new AbsoluteLayout(this.mAct) {
            Paint paint = new Paint();

            /* access modifiers changed from: protected */
            public void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                if (BarcodeFrameItem.this.noPermission) {
                    this.paint.setColor(-1);
                    this.paint.setTextSize((float) CanvasHelper.dip2px(BarcodeFrameItem.this.mAct, 18.0f));
                    this.paint.setTextAlign(Paint.Align.CENTER);
                    Paint.FontMetrics fontMetrics = this.paint.getFontMetrics();
                    float f = fontMetrics.top;
                    float f2 = fontMetrics.bottom;
                    AbsoluteLayout.LayoutParams layoutParams = frameLayoutParam;
                    if (layoutParams != null) {
                        canvas.drawText(DCLoudApplicationImpl.self().getContext().getString(R.string.dcloud_feature_barcode2_no_camera_permission), (float) (layoutParams.width / 2), (float) ((int) ((((float) (layoutParams.height / 2)) - (f / 2.0f)) - (f2 / 2.0f))), this.paint);
                    }
                }
            }
        };
        setMainView(r6);
        if (jSONObject != null) {
            initStyles(jSONObject, r6);
        }
        initDecodeFormats(jSONArray2);
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

    static int convertTypestrToNum(BarcodeFormat barcodeFormat) {
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

    private AbsoluteLayout.LayoutParams getFrameLayoutParam(JSONArray jSONArray, JSONObject jSONObject) {
        float scale = this.mContainerWebview.getScale();
        if (jSONArray.length() > 3) {
            Rect rect = DetectorViewConfig.getInstance().gatherRect;
            rect.left = PdrUtil.parseInt(JSONUtil.getString(jSONArray, 0), 0);
            rect.top = PdrUtil.parseInt(JSONUtil.getString(jSONArray, 1), 0);
            rect.right = rect.left + PdrUtil.parseInt(JSONUtil.getString(jSONArray, 2), 0);
            int parseInt = rect.top + PdrUtil.parseInt(JSONUtil.getString(jSONArray, 3), 0);
            rect.bottom = parseInt;
            rect.left = (int) (((float) rect.left) * scale);
            rect.top = (int) (((float) rect.top) * scale);
            rect.right = (int) (((float) rect.right) * scale);
            rect.bottom = (int) (((float) parseInt) * scale);
            if (rect.width() == 0 || rect.height() == 0) {
                return null;
            }
            return (AbsoluteLayout.LayoutParams) AdaFrameItem.LayoutParamsUtil.createLayoutParams(rect.left, rect.top, rect.width(), rect.height());
        } else if (jSONObject == null) {
            return null;
        } else {
            AdaFrameView adaFrameView = (AdaFrameView) this.mContainerWebview.obtainFrameView();
            ViewOptions obtainFrameOptions = adaFrameView.obtainFrameOptions();
            int i = this.mPosition.equals(AbsoluteConst.JSON_VALUE_POSITION_ABSOLUTE) ? obtainFrameOptions.height : 0;
            if (i == 0 && (i = ((ViewGroup) adaFrameView.obtainWebviewParent().obtainMainView()).getHeight()) == 0) {
                i = obtainFrameOptions.height;
            }
            int i2 = obtainFrameOptions.width;
            if (i2 == 0) {
                i2 = ((ViewGroup) adaFrameView.obtainWebviewParent().obtainMainView()).getWidth();
            }
            int convertToScreenInt = PdrUtil.convertToScreenInt(JSONUtil.getString(jSONObject, "left"), i2, 0, scale);
            int convertToScreenInt2 = PdrUtil.convertToScreenInt(JSONUtil.getString(jSONObject, "top"), i, 0, scale);
            int convertToScreenInt3 = PdrUtil.convertToScreenInt(JSONUtil.getString(jSONObject, "width"), i2, i2, scale);
            int convertToScreenInt4 = PdrUtil.convertToScreenInt(JSONUtil.getString(jSONObject, "height"), i, i, scale);
            Rect rect2 = DetectorViewConfig.getInstance().gatherRect;
            rect2.left = convertToScreenInt;
            rect2.top = convertToScreenInt2;
            rect2.right = convertToScreenInt + convertToScreenInt3;
            rect2.bottom = convertToScreenInt2 + convertToScreenInt4;
            if (rect2.width() == 0 || rect2.height() == 0) {
                return null;
            }
            return (AbsoluteLayout.LayoutParams) AdaFrameItem.LayoutParamsUtil.createLayoutParams(rect2.left, rect2.top, rect2.width(), rect2.height());
        }
    }

    private void initBeepSound() {
        if (this.mediaPlayer == null) {
            getActivity().setVolumeControlStream(3);
            MediaPlayer mediaPlayer2 = new MediaPlayer();
            this.mediaPlayer = mediaPlayer2;
            mediaPlayer2.setAudioStreamType(3);
            this.mediaPlayer.setOnCompletionListener(this.beepListener);
            try {
                AssetFileDescriptor openFd = this.mAct.getResources().getAssets().openFd(AbsoluteConst.RES_BEEP);
                this.mediaPlayer.setDataSource(openFd.getFileDescriptor(), openFd.getStartOffset(), openFd.getLength());
                openFd.close();
                this.mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                this.mediaPlayer.prepare();
            } catch (IOException unused) {
                this.mediaPlayer = null;
            }
        }
    }

    private void initCamera(SurfaceTexture surfaceTexture) {
        try {
            CameraManager.get().openDriver(surfaceTexture);
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
        } catch (RuntimeException e2) {
            this.errorMsg = e2.getMessage();
        }
    }

    /* access modifiers changed from: private */
    public void initCameraView(AbsoluteLayout.LayoutParams layoutParams, AbsoluteLayout absoluteLayout) {
        CameraManager.init(getActivity().getApplication(), this.mAppHandler.isVerticalScreen());
        boolean z = false;
        CameraManager.sScreenWidth = this.mAppHandler.getInt(0);
        CameraManager.sScreenAllHeight = this.mAppHandler.getInt(2);
        if (getActivity().getResources().getConfiguration().orientation == 1) {
            z = true;
        }
        if (z) {
            initPortraitCameraView(layoutParams, absoluteLayout);
        } else {
            initLandScapeCameraView(layoutParams, absoluteLayout);
        }
    }

    private void initDecodeFormats(JSONArray jSONArray) {
        int i;
        this.decodeFormats = new Vector<>();
        if (jSONArray == null || jSONArray.length() == 0) {
            this.decodeFormats.add(BarcodeFormat.EAN_13);
            this.decodeFormats.add(BarcodeFormat.EAN_8);
            this.decodeFormats.add(BarcodeFormat.QR_CODE);
            return;
        }
        int length = jSONArray.length();
        for (int i2 = 0; i2 < length; i2++) {
            try {
                i = jSONArray.getInt(i2);
            } catch (JSONException e) {
                e.printStackTrace();
                i = -1;
            }
            if (i != -1) {
                this.decodeFormats.add(convertNumToBarcodeFormat(i));
            }
        }
    }

    private void initLandScapeCameraView(AbsoluteLayout.LayoutParams layoutParams, final AbsoluteLayout absoluteLayout) {
        int i;
        int i2;
        Rect rect = DetectorViewConfig.getInstance().gatherRect;
        Point cr = CameraManager.getCR(rect.width(), rect.height());
        if (cr == null) {
            this.noPermission = true;
            MessageHandler.sendMessage(new MessageHandler.IMessages() {
                public void execute(Object obj) {
                    absoluteLayout.setBackgroundColor(-16777216);
                    absoluteLayout.invalidate();
                }
            }, (Object) null);
            return;
        }
        int i3 = layoutParams.height;
        int i4 = cr.x;
        int i5 = cr.y;
        int i6 = (i3 * i4) / i5;
        int i7 = layoutParams.width;
        if (i6 < i7) {
            int i8 = (i5 * i7) / i4;
            int i9 = (i3 - i8) / 2;
            DetectorViewConfig.detectorRectOffestTop = i9;
            DetectorViewConfig.detectorRectOffestLeft = 0;
            i6 = i7;
            i = 0;
            int i10 = i9;
            i3 = i8;
            i2 = i10;
        } else {
            i = (i7 - i6) / 2;
            DetectorViewConfig.detectorRectOffestLeft = i;
            DetectorViewConfig.detectorRectOffestTop = 0;
            i2 = 0;
        }
        this.surfaceView.setClickable(false);
        absoluteLayout.addView(this.surfaceView, new AbsoluteLayout.LayoutParams(i6, i3, i, i2));
        DetectorViewConfig.getInstance().initSurfaceViewRect(i, i2, i6, i3);
        absoluteLayout.addView(this.viewfinderView);
    }

    private void initPortraitCameraView(AbsoluteLayout.LayoutParams layoutParams, final AbsoluteLayout absoluteLayout) {
        int i;
        int i2;
        Rect rect = DetectorViewConfig.getInstance().gatherRect;
        Point cr = CameraManager.getCR(rect.height(), rect.width());
        if (cr == null) {
            this.noPermission = true;
            MessageHandler.sendMessage(new MessageHandler.IMessages() {
                public void execute(Object obj) {
                    absoluteLayout.setBackgroundColor(-16777216);
                    absoluteLayout.invalidate();
                }
            }, (Object) null);
            return;
        }
        int i3 = layoutParams.width;
        int i4 = cr.x;
        int i5 = cr.y;
        int i6 = (i3 * i4) / i5;
        int i7 = layoutParams.height;
        if (i6 < i7) {
            int i8 = (i5 * i7) / i4;
            int i9 = (i3 - i8) / 2;
            DetectorViewConfig.detectorRectOffestLeft = i9;
            DetectorViewConfig.detectorRectOffestTop = 0;
            i6 = i7;
            i = 0;
            int i10 = i9;
            i3 = i8;
            i2 = i10;
        } else {
            i = (i7 - i6) / 2;
            DetectorViewConfig.detectorRectOffestTop = i;
            DetectorViewConfig.detectorRectOffestLeft = 0;
            i2 = 0;
        }
        this.surfaceView.setClickable(false);
        absoluteLayout.addView(this.surfaceView, new AbsoluteLayout.LayoutParams(i3, i6, i2, i));
        DetectorViewConfig.getInstance().initSurfaceViewRect(i2, i, i3, i6);
        absoluteLayout.addView(this.viewfinderView);
    }

    private void initStyles(JSONObject jSONObject, View view) {
        this.mStyles = jSONObject;
        DetectorViewConfig.laserColor = -65536;
        DetectorViewConfig.cornerColor = -65536;
        if (jSONObject.has("position")) {
            this.mPosition = jSONObject.optString("position");
        }
        if (!TextUtils.isEmpty(jSONObject.optString("scanbarColor"))) {
            int stringToColor = PdrUtil.stringToColor(jSONObject.optString("scanbarColor"));
            if (stringToColor == -1) {
                stringToColor = DetectorViewConfig.laserColor;
            }
            DetectorViewConfig.laserColor = stringToColor;
        }
        if (!TextUtils.isEmpty(jSONObject.optString("frameColor"))) {
            int stringToColor2 = PdrUtil.stringToColor(jSONObject.optString("frameColor"));
            if (stringToColor2 == -1) {
                stringToColor2 = DetectorViewConfig.laserColor;
            }
            DetectorViewConfig.cornerColor = stringToColor2;
        }
        if (!TextUtils.isEmpty(jSONObject.optString("background"))) {
            int stringToColor3 = PdrUtil.stringToColor(jSONObject.optString("background"));
            if (stringToColor3 == -1) {
                stringToColor3 = DetectorViewConfig.laserColor;
            }
            view.setBackgroundColor(stringToColor3);
        }
        if (jSONObject.has("autoZoom")) {
            CaptureActivityHandler.isAutoZoom = jSONObject.optBoolean("autoZoom", true);
        }
    }

    private void listenHideAndShow(IWebview iWebview) {
        iWebview.obtainFrameView().addFrameViewListener(new IEventCallback() {
            public Object onCallBack(String str, Object obj) {
                if (PdrUtil.isEquals(str, "hide") || PdrUtil.isEquals(str, AbsoluteConst.EVENTS_WINDOW_CLOSE)) {
                    BarcodeFrameItem.this.onPause();
                    return null;
                } else if (!PdrUtil.isEquals(str, AbsoluteConst.EVENTS_SHOW_ANIMATION_END)) {
                    return null;
                } else {
                    BarcodeFrameItem.this.onResume(true);
                    return null;
                }
            }
        });
    }

    private void playBeepSoundAndVibrate() {
        MediaPlayer mediaPlayer2;
        if (this.playBeep && (mediaPlayer2 = this.mediaPlayer) != null) {
            mediaPlayer2.start();
        }
        if (this.vibrate) {
            try {
                ((Vibrator) this.mAct.getSystemService("vibrator")).vibrate(VIBRATE_DURATION);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void resumeOrientationState() {
        this.mAppHandler.setRequestedOrientation(this.mOrientationState);
    }

    private void saveOrientationState() {
        this.mOrientationState = this.mAppHandler.getRequestedOrientation();
    }

    public void addCallBackId(String str, String str2) {
        if (!this.mCallbackIds.containsKey(str)) {
            this.mCallbackIds.put(str, str2);
        }
    }

    public void appendToFrameView(AdaFrameView adaFrameView) {
        if (!(obtainMainView() == null || obtainMainView().getParent() == null)) {
            removeMapFrameItem(this.mContainerWebview);
        }
        this.mContainerWebview = adaFrameView.obtainWebView();
        toFrameView();
    }

    public void autoFocus() {
        this.handler.autoFocus();
    }

    /* access modifiers changed from: protected */
    public void cancel() {
        if (this.mRunning) {
            CaptureActivityHandler captureActivityHandler = this.handler;
            if (captureActivityHandler != null) {
                captureActivityHandler.stopDecode();
            }
            getViewfinderView().stopUpdateScreenTimer();
            this.mRunning = false;
        }
    }

    /* access modifiers changed from: protected */
    public void cancel_scan() {
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
                this.lastBitmap = byte2bitmap(lastBitmapData, cameraHandler);
            }
            CameraManager.get().closeDriver();
            this.mRunning = false;
            this.isCancelScan = true;
        }
    }

    /* access modifiers changed from: protected */
    public void close_scan() {
        dispose();
        setMainView((View) null);
        System.gc();
    }

    public void dispose() {
        super.dispose();
        Logger.d(IFeature.F_BARCODE, "dispose");
        onPause();
        DetectorViewConfig.clearData();
        this.mProxy.mBarcodeView = null;
        this.surfaceView = null;
        Bitmap bitmap = this.lastBitmap;
        if (bitmap != null && !bitmap.isRecycled()) {
            this.lastBitmap.recycle();
            this.lastBitmap = null;
        }
        CameraManager.get().clearLastBitmapData();
        resumeOrientationState();
        BarcodeProxyMgr.getBarcodeProxyMgr().removeBarcodeProxy(this.mUuid);
    }

    public void drawViewfinder() {
        this.viewfinderView.drawViewfinder();
    }

    public Handler getHandler() {
        return this.handler;
    }

    public JSONObject getJsBarcode() {
        if (obtainMainView() == null) {
            return null;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("uuid", this.mUuid);
            jSONObject.put("filters", this.mFilters);
            jSONObject.put("options", this.mStyles);
            jSONObject.put("autoDecodeCharset", this.autoDecodeCharset);
            return jSONObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return jSONObject;
        }
    }

    public ViewfinderView getViewfinderView() {
        return this.viewfinderView;
    }

    public void handleDecode(Result result, Bitmap bitmap) {
        String str;
        this.inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        boolean saveBitmapToFile = this.mConserve ? PdrUtil.saveBitmapToFile(bitmap, this.mFilename) : false;
        int convertTypestrToNum = convertTypestrToNum(result.getBarcodeFormat());
        if (saveBitmapToFile) {
            String obtainAppDocPath = this.mWebViewImpl.obtainFrameView().obtainApp().obtainAppDocPath();
            Logger.d("doc:" + obtainAppDocPath);
            if (this.mFilename.startsWith(obtainAppDocPath)) {
                this.mFilename = BaseInfo.REL_PRIVATE_DOC_DIR + this.mFilename.substring(obtainAppDocPath.length() - 1);
            }
            String convert2RelPath = this.mWebViewImpl.obtainFrameView().obtainApp().convert2RelPath(this.mFilename);
            Logger.d("Filename:" + this.mFilename + ";relPath:" + convert2RelPath);
            str = StringUtil.format("{type:%d,message:%s,file:'%s',charSet:'%s'}", Integer.valueOf(convertTypestrToNum), JSONUtil.toJSONableString(result.getText()), convert2RelPath, result.textCharset);
        } else {
            str = StringUtil.format("{type:%d,message:%s,charSet:'%s'}", Integer.valueOf(convertTypestrToNum), JSONUtil.toJSONableString(result.getText()), result.textCharset);
        }
        runJsCallBack(str, JSUtil.OK, true, true);
        cancel();
    }

    public boolean isRunning() {
        return this.mRunning;
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.inactivityTimer.shutdown();
        this.hasSurface = false;
        this.decodeFormats = null;
        this.characterSet = null;
        this.mCallbackIds.clear();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        CaptureActivityHandler captureActivityHandler = this.handler;
        if (captureActivityHandler != null) {
            captureActivityHandler.quitSynchronously();
            this.handler = null;
        }
        if (!this.noPermission) {
            CameraManager.get().closeDriver();
        }
        boolean z = this.mRunning;
        cancel();
        this.mRunning = z;
    }

    public void onPopFromStack(boolean z) {
        super.onPopFromStack(z);
        if (z) {
            onPause();
        }
    }

    public void onPushToStack(boolean z) {
        super.onPushToStack(z);
        if (z) {
            onResume(false);
        }
    }

    /* access modifiers changed from: protected */
    public void onResume(boolean z) {
        SurfaceTexture surfaceTexture = this.surfaceView.getSurfaceTexture();
        if (this.lastBitmap != null && this.isCancelScan && z) {
            this.surfaceView.setBackground(new BitmapDrawable(this.mAct.getResources(), this.lastBitmap));
        }
        if (this.hasSurface) {
            initCamera(surfaceTexture);
        } else {
            this.surfaceView.setSurfaceTextureListener(this);
        }
        if (((AudioManager) this.mAct.getSystemService("audio")).getRingerMode() != 2) {
            this.playBeep = false;
        }
        initBeepSound();
        if (z && this.mRunning) {
            this.mRunning = false;
            start();
        }
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
        if (!this.hasSurface) {
            this.hasSurface = true;
            if (!this.isCancelScan) {
                try {
                    initCamera(surfaceTexture);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        this.hasSurface = false;
        return false;
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    public void removeMapFrameItem(IWebview iWebview) {
        if (this.mPosition.equals(AbsoluteConst.JSON_VALUE_POSITION_ABSOLUTE)) {
            iWebview.obtainFrameView().removeFrameItem(this);
        } else {
            iWebview.removeFrameItem(this);
        }
    }

    public void runJsCallBack(String str, int i, boolean z, boolean z2) {
        for (String next : this.mCallbackIds.keySet()) {
            IWebview findWebviewByUuid = BarcodeProxyMgr.getBarcodeProxyMgr().findWebviewByUuid(this.mWebViewImpl, this.mCallbackIds.get(next));
            if (findWebviewByUuid != null) {
                Deprecated_JSUtil.execCallback(findWebviewByUuid, next, str, i, z, z2);
            }
        }
    }

    public void setFlash(boolean z) {
        CameraManager.get().setFlashlight(z);
    }

    /* access modifiers changed from: protected */
    public void start() {
        if (!this.mRunning) {
            getViewfinderView().startUpdateScreenTimer();
            CaptureActivityHandler captureActivityHandler = this.handler;
            if (captureActivityHandler != null) {
                captureActivityHandler.restartPreviewAndDecode();
            } else {
                onResume(false);
            }
            if (this.isCancelScan) {
                this.surfaceView.setBackground((Drawable) null);
                Bitmap bitmap = this.lastBitmap;
                if (bitmap != null && !bitmap.isRecycled()) {
                    this.lastBitmap.recycle();
                    this.lastBitmap = null;
                }
                CameraManager.get().clearLastBitmapData();
                this.surfaceView.postInvalidate();
                initCamera(this.surfaceView.getSurfaceTexture());
            }
            this.mRunning = true;
            this.isCancelScan = false;
        }
    }

    public void toFrameView() {
        final AbsoluteLayout absoluteLayout = (AbsoluteLayout) obtainMainView();
        final AbsoluteLayout.LayoutParams frameLayoutParam = getFrameLayoutParam(this.mDivRectJson, this.mStyles);
        if (frameLayoutParam != null) {
            this.surfaceView = new TextureView(this.mAct);
            this.viewfinderView = new ViewfinderView(this.mAct, this);
            this.hasSurface = false;
            this.inactivityTimer = new InactivityTimer(getActivity());
            PermissionUtil.usePermission(this.mContainerWebview.getActivity(), "barcode", PermissionUtil.PMS_CAMERA, 2, new PermissionUtil.StreamPermissionRequest(this.mContainerWebview.obtainApp()) {
                public void onDenied(String str) {
                    BarcodeFrameItem.this.noPermission = true;
                    MessageHandler.sendMessage(new MessageHandler.IMessages() {
                        public void execute(Object obj) {
                            absoluteLayout.setBackgroundColor(-16777216);
                            absoluteLayout.invalidate();
                        }
                    }, (Object) null);
                }

                public void onGranted(String str) {
                    BarcodeFrameItem.this.initCameraView(frameLayoutParam, absoluteLayout);
                }
            });
            onResume(false);
            saveOrientationState();
            boolean isVerticalScreen2 = this.mAppHandler.isVerticalScreen();
            this.isVerticalScreen = isVerticalScreen2;
            if (isVerticalScreen2) {
                this.mAppHandler.setRequestedOrientation("portrait");
            } else {
                this.mAppHandler.setRequestedOrientation("landscape");
            }
            listenHideAndShow(this.mContainerWebview);
            if (this.mPosition.equals(AbsoluteConst.JSON_VALUE_POSITION_ABSOLUTE)) {
                this.mContainerWebview.obtainFrameView().addFrameItem(this, frameLayoutParam);
            } else {
                this.mContainerWebview.addFrameItem(this, frameLayoutParam);
            }
        }
    }

    public void upateStyles(JSONObject jSONObject) {
        AbsoluteLayout.LayoutParams frameLayoutParam;
        JSONUtil.combinJSONObject(this.mStyles, jSONObject);
        if ((!jSONObject.has("top") && !jSONObject.has("left") && !jSONObject.has("width") && !jSONObject.has("height") && !jSONObject.has("position")) || (frameLayoutParam = getFrameLayoutParam(this.mDivRectJson, this.mStyles)) == null) {
            return;
        }
        if (jSONObject.has("position")) {
            String optString = jSONObject.optString("position");
            if (!optString.equals(this.mPosition)) {
                if (this.mPosition.equals(AbsoluteConst.JSON_VALUE_POSITION_ABSOLUTE)) {
                    this.mContainerWebview.obtainFrameView().removeFrameItem(this);
                    this.mContainerWebview.addFrameItem(this, frameLayoutParam);
                } else {
                    this.mContainerWebview.removeFrameItem(this);
                    this.mContainerWebview.obtainFrameView().addFrameItem(this, frameLayoutParam);
                }
                this.mPosition = optString;
                return;
            }
            return;
        }
        obtainMainView().setLayoutParams(frameLayoutParam);
    }
}
