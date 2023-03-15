package io.dcloud.feature.nativeObj;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.webkit.JavascriptInterface;
import com.nostra13.dcloudimageloader.core.DisplayImageOptions;
import com.nostra13.dcloudimageloader.core.ImageLoaderL;
import com.nostra13.dcloudimageloader.core.assist.FailReason;
import com.nostra13.dcloudimageloader.core.assist.ImageLoadingListener;
import com.nostra13.dcloudimageloader.core.assist.ImageScaleType;
import com.nostra13.dcloudimageloader.core.assist.MemoryCacheUtil;
import com.nostra13.dcloudimageloader.core.download.ImageDownloader;
import com.taobao.weex.el.parse.Operators;
import io.dcloud.application.DCLoudApplicationImpl;
import io.dcloud.base.R;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.INativeBitmap;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.DeviceInfo;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.FileUtil;
import io.dcloud.common.util.ImageLoaderUtil;
import io.dcloud.common.util.PdrUtil;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import pl.droidsonroids.gif.GifDrawable;

public class NativeBitmap implements INativeBitmap {
    private static final int ERROR = 40;
    private static final int SUCCESS = 10;
    private boolean isNetWorkBitmapDownload = false;
    private IApp mApp;
    /* access modifiers changed from: private */
    public Bitmap mBitmap;
    /* access modifiers changed from: private */
    public ICallBack mErrCallBackLoad = null;
    private String mExt = "jpg";
    private GifDrawable mGifDrawable;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            int i = message.what;
            if (i != 10) {
                if (i == 40 && NativeBitmap.this.mErrCallBackLoad != null) {
                    NativeBitmap.this.mErrCallBackLoad.onCallBack(0, message.obj);
                }
            } else if (NativeBitmap.this.mSucCallBackLoad != null) {
                NativeBitmap.this.mSucCallBackLoad.onCallBack(0, message.obj);
            }
            super.handleMessage(message);
        }
    };
    private String mId;
    /* access modifiers changed from: private */
    public String mPath;
    /* access modifiers changed from: private */
    public ICallBack mSucCallBackLoad = null;
    private String mUUid;

    public NativeBitmap(IApp iApp, String str, String str2, String str3) {
        this.mId = str;
        this.mUUid = str2;
        this.mPath = str3;
        ImageLoaderUtil.addNetIconDownloadUrl(str3);
        this.mExt = getExt(str3);
        this.mApp = iApp;
    }

    private String bitmap2String(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(getCF(), 100, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 2);
    }

    private Bitmap.CompressFormat getCF() {
        if ("png".equals(this.mExt)) {
            return Bitmap.CompressFormat.PNG;
        }
        return Bitmap.CompressFormat.JPEG;
    }

    private String getExt(String str) {
        if (PdrUtil.isNetPath(str)) {
            if (str.contains(".jpg")) {
                return "jpg";
            }
            if (str.contains(".png")) {
                return "png";
            }
            if (str.contains(".gif")) {
                return "gif";
            }
            if (str.contains(".webp")) {
                return "webp";
            }
        }
        return TextUtils.isEmpty(str) ? str : str.substring(str.lastIndexOf(Operators.DOT_STR) + 1, str.length()).toLowerCase(Locale.ENGLISH);
    }

    private void getExtFromBase64(String str) {
        if (str.indexOf(",") != -1) {
            this.mExt = str.split(",")[0].replace("data:image/", "").replace(";base64", "");
        }
    }

    private String getFilePath(String str) {
        Uri fileUri;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (!TextUtils.isEmpty(str) && PdrUtil.isNetPath(str)) {
            return str;
        }
        if (this.mApp != null) {
            File file = new File(str);
            if (file.exists() || str.startsWith("/storage")) {
                Context context = DCLoudApplicationImpl.self().getContext();
                if (FileUtil.needMediaStoreOpenFile(context) && !FileUtil.checkPrivatePath(context, this.mPath) && (fileUri = FileUtil.getFileUri(context, file, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)) != null) {
                    return fileUri.toString();
                }
            } else if (this.mApp.obtainRunningAppMode() == 1) {
                if (str.startsWith("/")) {
                    str = str.substring(1, str.length());
                }
                return ImageDownloader.Scheme.ASSETS.wrap(str);
            }
        }
        if (str.contains(DeviceInfo.FILE_PROTOCOL)) {
            return str;
        }
        return DeviceInfo.FILE_PROTOCOL + str;
    }

    private DisplayImageOptions getImageOptions() {
        return new DisplayImageOptions.Builder().cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.NONE).build();
    }

    /* access modifiers changed from: private */
    public void saveFile(String str, Bitmap bitmap, NativeBitmapSaveOptions nativeBitmapSaveOptions) throws Exception {
        Bitmap.CompressFormat compressFormat;
        if (bitmap != null && bitmap.getHeight() != 0 && bitmap.getWidth() != 0) {
            File file = new File(str.substring(0, str.lastIndexOf("/")));
            if (!file.exists()) {
                file.mkdirs();
            }
            File file2 = new File(str);
            if (!file2.exists()) {
                file2.createNewFile();
            }
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file2));
            if ("png".equals(getExt(str))) {
                compressFormat = Bitmap.CompressFormat.PNG;
            } else {
                compressFormat = Bitmap.CompressFormat.JPEG;
            }
            bitmap.compress(compressFormat, nativeBitmapSaveOptions.mQuality, bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            nativeBitmapSaveOptions.path = file2.getAbsolutePath();
            nativeBitmapSaveOptions.width = bitmap.getWidth();
            nativeBitmapSaveOptions.height = bitmap.getHeight();
            nativeBitmapSaveOptions.size = file2.length();
        }
    }

    private Bitmap string2Bitmap(String str) {
        if (str.indexOf(",") != -1) {
            str = str.substring(str.indexOf(","));
        }
        byte[] decode = Base64.decode(str, 2);
        return BitmapFactory.decodeByteArray(decode, 0, decode.length);
    }

    @JavascriptInterface
    public void clear() {
        recycle();
        this.mBitmap = null;
        this.mGifDrawable = null;
    }

    public Bitmap getBitmap() {
        if (isRecycled()) {
            String filePath = getFilePath(this.mPath);
            if (TextUtils.isEmpty(filePath)) {
                return null;
            }
            if (PdrUtil.isNetPath(filePath) && !this.isNetWorkBitmapDownload) {
                return null;
            }
            this.mBitmap = ImageLoaderL.getInstance().loadImageSync(filePath, getImageOptions());
        }
        return this.mBitmap;
    }

    public GifDrawable getGifDrawable() {
        GifDrawable gifDrawable = this.mGifDrawable;
        if ((gifDrawable == null || (gifDrawable != null && gifDrawable.isRecycled())) && !PdrUtil.isEmpty(this.mPath) && this.mApp != null) {
            if (!PdrUtil.isNetPath(this.mPath)) {
                File file = new File(this.mPath);
                if (this.mApp.obtainRunningAppMode() == 1 && !file.exists()) {
                    String str = this.mPath;
                    if (str.startsWith("/")) {
                        String str2 = this.mPath;
                        str = str2.substring(1, str2.length());
                    }
                    try {
                        this.mGifDrawable = new GifDrawable(this.mApp.getActivity().getAssets(), str);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (file.exists()) {
                    try {
                        this.mGifDrawable = new GifDrawable(file);
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            } else if (!this.isNetWorkBitmapDownload) {
                return null;
            } else {
                File file2 = ImageLoaderL.getInstance().getDiscCache().get(this.mPath);
                if (file2.exists()) {
                    try {
                        this.mGifDrawable = new GifDrawable(file2);
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
            }
        }
        return this.mGifDrawable;
    }

    @JavascriptInterface
    public String getId() {
        return this.mId;
    }

    public Bitmap initNetworkBitmap(ImageLoadingListener imageLoadingListener) {
        if (isRecycled()) {
            this.isNetWorkBitmapDownload = false;
            String filePath = getFilePath(this.mPath);
            if (TextUtils.isEmpty(filePath)) {
                return null;
            }
            ImageLoaderL.getInstance().loadImage(filePath, imageLoadingListener);
        }
        return this.mBitmap;
    }

    public boolean isGif() {
        if (!PdrUtil.isEmpty(this.mExt)) {
            return this.mExt.equalsIgnoreCase("gif");
        }
        return false;
    }

    public boolean isNetWorkBitmap() {
        if (TextUtils.isEmpty(this.mPath) || !PdrUtil.isNetPath(this.mPath)) {
            return false;
        }
        if (!this.isNetWorkBitmapDownload || !ImageLoaderL.getInstance().getDiscCache().get(this.mPath).exists()) {
            return true;
        }
        return false;
    }

    public boolean isRecycled() {
        Bitmap bitmap = this.mBitmap;
        if (bitmap != null) {
            return bitmap.isRecycled();
        }
        GifDrawable gifDrawable = this.mGifDrawable;
        if (gifDrawable != null) {
            return gifDrawable.isRecycled();
        }
        return true;
    }

    @JavascriptInterface
    public void load(IWebview iWebview, Context context, String str, final ICallBack iCallBack, final ICallBack iCallBack2) {
        this.isNetWorkBitmapDownload = false;
        if (TextUtils.isEmpty(str)) {
            this.mErrCallBackLoad = iCallBack2;
            Message obtainMessage = this.mHandler.obtainMessage();
            obtainMessage.what = 40;
            obtainMessage.obj = context.getString(R.string.dcloud_native_obj_path_cannot_empty);
            this.mHandler.sendMessage(obtainMessage);
            return;
        }
        Locale locale = Locale.ENGLISH;
        if (str.toLowerCase(locale).startsWith(DeviceInfo.HTTP_PROTOCOL) || str.toLowerCase(locale).startsWith(DeviceInfo.HTTPS_PROTOCOL) || str.toLowerCase(locale).startsWith("ftp://")) {
            this.mErrCallBackLoad = iCallBack2;
            Message obtainMessage2 = this.mHandler.obtainMessage();
            obtainMessage2.what = 40;
            obtainMessage2.obj = context.getString(R.string.dcloud_native_obj_path_not_network);
            this.mHandler.sendMessage(obtainMessage2);
            return;
        }
        str.toLowerCase(locale).startsWith("_");
        this.mPath = iWebview.obtainApp().convert2AbsFullPath(iWebview.obtainFullUrl(), str);
        if (FileUtil.checkPathAccord(iWebview.getContext(), this.mPath) || FileUtil.isFilePathForPublic(context, this.mPath)) {
            this.mExt = getExt(str);
            ImageLoaderL.getInstance().loadImage(getFilePath(this.mPath), getImageOptions(), (ImageLoadingListener) new ImageLoadingListener() {
                public void onLoadingCancelled(String str, View view) {
                }

                public void onLoadingComplete(String str, View view, Bitmap bitmap) {
                    Bitmap unused = NativeBitmap.this.mBitmap = bitmap;
                    ICallBack unused2 = NativeBitmap.this.mSucCallBackLoad = iCallBack;
                    NativeBitmap.this.mHandler.sendEmptyMessage(10);
                }

                public void onLoadingFailed(String str, View view, FailReason failReason) {
                    ICallBack unused = NativeBitmap.this.mErrCallBackLoad = iCallBack2;
                    NativeBitmap.this.mHandler.sendEmptyMessage(40);
                }

                public void onLoadingStarted(String str, View view) {
                }
            });
            return;
        }
        this.mErrCallBackLoad = iCallBack2;
        Message obtainMessage3 = this.mHandler.obtainMessage();
        obtainMessage3.what = 40;
        obtainMessage3.obj = DOMException.MSG_PATH_NOT_PRIVATE_ERROR;
        this.mHandler.sendMessage(obtainMessage3);
    }

    @JavascriptInterface
    public void loadBase64Data(String str, ICallBack iCallBack, ICallBack iCallBack2) {
        try {
            Bitmap string2Bitmap = string2Bitmap(str);
            this.mBitmap = string2Bitmap;
            if (string2Bitmap != null) {
                getExtFromBase64(str);
                this.mSucCallBackLoad = iCallBack;
                this.mHandler.sendEmptyMessage(10);
                return;
            }
            this.mErrCallBackLoad = iCallBack2;
            this.mHandler.sendEmptyMessage(40);
        } catch (Exception unused) {
            this.mErrCallBackLoad = iCallBack2;
            this.mHandler.sendEmptyMessage(40);
        }
    }

    public void recycle() {
        recycle(false);
    }

    @JavascriptInterface
    public void save(IApp iApp, String str, NativeBitmapSaveOptions nativeBitmapSaveOptions, float f, ICallBack iCallBack, ICallBack iCallBack2) {
        if (TextUtils.isEmpty(str)) {
            this.mErrCallBackLoad = iCallBack2;
            Message obtainMessage = this.mHandler.obtainMessage();
            obtainMessage.what = 40;
            obtainMessage.obj = iApp.getActivity().getString(R.string.dcloud_native_obj_path_cannot_empty);
            this.mHandler.sendMessage(obtainMessage);
            return;
        }
        Locale locale = Locale.ENGLISH;
        if (str.toLowerCase(locale).startsWith(DeviceInfo.HTTP_PROTOCOL) || str.toLowerCase(locale).startsWith(DeviceInfo.HTTPS_PROTOCOL) || str.toLowerCase(locale).startsWith("ftp://")) {
            this.mErrCallBackLoad = iCallBack2;
            Message obtainMessage2 = this.mHandler.obtainMessage();
            obtainMessage2.what = 40;
            obtainMessage2.obj = iApp.getActivity().getString(R.string.dcloud_native_obj_path_not_network);
            this.mHandler.sendMessage(obtainMessage2);
            return;
        }
        final String str2 = this.mPath;
        this.mPath = str;
        final NativeBitmapSaveOptions nativeBitmapSaveOptions2 = nativeBitmapSaveOptions;
        final float f2 = f;
        final ICallBack iCallBack3 = iCallBack;
        final ICallBack iCallBack4 = iCallBack2;
        new Thread() {
            public void run() {
                try {
                    NativeBitmap.this.getBitmap();
                    if (nativeBitmapSaveOptions2.mClip != null) {
                        int width = NativeBitmap.this.mBitmap.getWidth();
                        int height = NativeBitmap.this.mBitmap.getHeight();
                        int convertToScreenInt = PdrUtil.convertToScreenInt(nativeBitmapSaveOptions2.mClip.optString("left"), width, 0, f2);
                        int convertToScreenInt2 = PdrUtil.convertToScreenInt(nativeBitmapSaveOptions2.mClip.optString("top"), height, 0, f2);
                        int convertToScreenInt3 = PdrUtil.convertToScreenInt(nativeBitmapSaveOptions2.mClip.optString("width"), width, width, f2);
                        int convertToScreenInt4 = PdrUtil.convertToScreenInt(nativeBitmapSaveOptions2.mClip.optString("height"), height, height, f2);
                        if (convertToScreenInt + convertToScreenInt3 > width) {
                            convertToScreenInt3 = width - convertToScreenInt;
                        }
                        if (convertToScreenInt2 + convertToScreenInt4 > height) {
                            convertToScreenInt4 = height - convertToScreenInt2;
                        }
                        Bitmap createBitmap = Bitmap.createBitmap(NativeBitmap.this.mBitmap, convertToScreenInt, convertToScreenInt2, convertToScreenInt3, convertToScreenInt4);
                        NativeBitmap nativeBitmap = NativeBitmap.this;
                        nativeBitmap.saveFile(nativeBitmap.mPath, createBitmap, nativeBitmapSaveOptions2);
                        createBitmap.recycle();
                        String unused = NativeBitmap.this.mPath = str2;
                    } else {
                        NativeBitmap nativeBitmap2 = NativeBitmap.this;
                        nativeBitmap2.saveFile(nativeBitmap2.mPath, NativeBitmap.this.mBitmap, nativeBitmapSaveOptions2);
                    }
                    ICallBack unused2 = NativeBitmap.this.mSucCallBackLoad = iCallBack3;
                    Message obtainMessage = NativeBitmap.this.mHandler.obtainMessage();
                    obtainMessage.what = 10;
                    obtainMessage.obj = nativeBitmapSaveOptions2;
                    NativeBitmap.this.mHandler.sendMessage(obtainMessage);
                } catch (Exception e) {
                    ICallBack unused3 = NativeBitmap.this.mErrCallBackLoad = iCallBack4;
                    NativeBitmap.this.mHandler.sendEmptyMessage(40);
                    Logger.e("mabo", "saveFile: " + e.toString());
                }
            }
        }.start();
    }

    public void setBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
    }

    public void setNetWorkBitmapDownload(boolean z) {
        this.isNetWorkBitmapDownload = z;
    }

    @JavascriptInterface
    public String toBase64Data() {
        StringBuilder sb = new StringBuilder();
        sb.append("data:image/");
        sb.append("jpg".equals(this.mExt) ? "jepg" : this.mExt);
        sb.append(";base64,");
        sb.append(bitmap2String(this.mBitmap));
        return sb.toString();
    }

    @JavascriptInterface
    public String toJsString() {
        return "{\"id\":\"" + this.mId + "\",\"__id__\":\"" + this.mUUid + "\"}";
    }

    public void recycle(boolean z) {
        if (!z || !TextUtils.isEmpty(this.mPath)) {
            Bitmap bitmap = this.mBitmap;
            if (bitmap != null && !bitmap.isRecycled()) {
                this.mBitmap.recycle();
                String filePath = getFilePath(this.mPath);
                if (!TextUtils.isEmpty(filePath)) {
                    MemoryCacheUtil.removeFromCache(filePath, ImageLoaderL.getInstance().getMemoryCache());
                }
            }
            GifDrawable gifDrawable = this.mGifDrawable;
            if (gifDrawable != null && !gifDrawable.isRecycled()) {
                this.mGifDrawable.recycle();
            }
        }
    }
}
