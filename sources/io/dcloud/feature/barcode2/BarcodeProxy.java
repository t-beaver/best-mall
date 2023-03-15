package io.dcloud.feature.barcode2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import com.alibaba.android.bindingx.core.internal.BindingXConstants;
import com.dcloud.zxing2.Result;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.AppRuntime;
import io.dcloud.common.util.FileUtil;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.StringUtil;
import io.dcloud.feature.barcode2.decoding.CaptureActivityHandler;
import java.io.InputStream;
import org.json.JSONArray;
import org.json.JSONObject;

public class BarcodeProxy implements ISysEventListener {
    public static Context context = null;
    public static boolean save = false;
    BarcodeFrameItem mBarcodeView = null;
    public String mId;
    boolean mIsRegisetedSysEvent = false;

    public void appendToFrameView(AdaFrameView adaFrameView) {
        BarcodeFrameItem barcodeFrameItem = this.mBarcodeView;
        if (barcodeFrameItem != null) {
            barcodeFrameItem.appendToFrameView(adaFrameView);
        }
    }

    /* access modifiers changed from: package-private */
    public void execute(IWebview iWebview, String str, String[] strArr) {
        BarcodeFrameItem barcodeFrameItem;
        String str2 = str;
        String[] strArr2 = strArr;
        boolean z = false;
        if ("start".equals(str2)) {
            if (!PdrUtil.isEmpty(this.mBarcodeView.errorMsg)) {
                this.mBarcodeView.runJsCallBack(StringUtil.format(DOMException.JSON_ERROR_INFO, 8, this.mBarcodeView.errorMsg), JSUtil.ERROR, true, true);
                return;
            }
            JSONObject createJSONObject = JSONUtil.createJSONObject(strArr2[1]);
            if (createJSONObject != null) {
                boolean parseBoolean = PdrUtil.parseBoolean(JSONUtil.getString(createJSONObject, "conserve"), false, false);
                if (parseBoolean) {
                    this.mBarcodeView.mFilename = iWebview.obtainFrameView().obtainApp().convert2AbsFullPath(iWebview.obtainFullUrl(), PdrUtil.getDefaultPrivateDocPath(JSONUtil.getString(createJSONObject, AbsoluteConst.JSON_KEY_FILENAME), "png"));
                    Logger.d("Filename:" + this.mBarcodeView.mFilename);
                }
                this.mBarcodeView.vibrate = PdrUtil.parseBoolean(JSONUtil.getString(createJSONObject, "vibrate"), true, false);
                this.mBarcodeView.playBeep = !TextUtils.equals(JSONUtil.getString(createJSONObject, "sound"), "none");
                z = parseBoolean;
            }
            BarcodeFrameItem barcodeFrameItem2 = this.mBarcodeView;
            barcodeFrameItem2.mConserve = z;
            barcodeFrameItem2.start();
        } else if (BindingXConstants.STATE_CANCEL.equals(str2)) {
            this.mBarcodeView.cancel_scan();
        } else if ("setFlash".equals(str2)) {
            this.mBarcodeView.setFlash(Boolean.parseBoolean(strArr2[1]));
        } else {
            Bitmap bitmap = null;
            if (IFeature.F_BARCODE.equals(str2)) {
                AppRuntime.checkPrivacyComplianceAndPrompt(iWebview.getContext(), "Barcode-Create");
                String str3 = strArr2[0];
                if (!this.mIsRegisetedSysEvent) {
                    IApp obtainApp = iWebview.obtainFrameView().obtainApp();
                    obtainApp.registerSysEventListener(this, ISysEventListener.SysEventType.onPause);
                    obtainApp.registerSysEventListener(this, ISysEventListener.SysEventType.onResume);
                    this.mIsRegisetedSysEvent = true;
                }
                JSONArray createJSONArray = !PdrUtil.isEmpty(strArr2[4]) ? JSONUtil.createJSONArray(strArr2[4]) : null;
                JSONObject createJSONObject2 = !PdrUtil.isEmpty(strArr2[5]) ? JSONUtil.createJSONObject(strArr2[5]) : null;
                JSONArray createJSONArray2 = JSONUtil.createJSONArray(strArr2[3]);
                this.mId = strArr2[2];
                BarcodeFrameItem barcodeFrameItem3 = new BarcodeFrameItem(this, iWebview, str3, createJSONArray2, createJSONArray, createJSONObject2);
                this.mBarcodeView = barcodeFrameItem3;
                barcodeFrameItem3.addCallBackId(strArr2[1], iWebview.getWebviewUUID());
                if (strArr2.length > 6) {
                    this.mBarcodeView.autoDecodeCharset = Boolean.parseBoolean(strArr2[6]);
                }
                if (createJSONArray2.length() > 3) {
                    this.mBarcodeView.toFrameView();
                }
            } else if ("scan".equals(str2)) {
                Context context2 = iWebview.getContext();
                AppRuntime.checkPrivacyComplianceAndPrompt(context2, "Barcode-" + str2);
                String str4 = strArr2[0];
                IApp obtainApp2 = iWebview.obtainFrameView().obtainApp();
                String convert2AbsFullPath = obtainApp2.convert2AbsFullPath(iWebview.obtainFullUrl(), strArr2[1]);
                if (FileUtil.needMediaStoreOpenFile(obtainApp2.getActivity())) {
                    InputStream fileInputStream = FileUtil.getFileInputStream((Context) obtainApp2.getActivity(), convert2AbsFullPath);
                    if (fileInputStream != null) {
                        bitmap = BitmapFactory.decodeStream(fileInputStream);
                    }
                } else {
                    bitmap = BitmapFactory.decodeFile(convert2AbsFullPath);
                }
                if (bitmap != null) {
                    Result decode = CaptureActivityHandler.decode(bitmap, strArr2.length > 3 ? Boolean.parseBoolean(strArr2[3]) : false);
                    if (decode != null) {
                        Deprecated_JSUtil.execCallback(iWebview, str4, StringUtil.format("{type:%d,message:%s,file:'%s',charSet:'%s'}", Integer.valueOf(BarcodeFrameItem.convertTypestrToNum(decode.getBarcodeFormat())), JSONUtil.toJSONableString(decode.getText()), convert2AbsFullPath, decode.textCharset), JSUtil.OK, true, false);
                        return;
                    }
                }
                Deprecated_JSUtil.execCallback(iWebview, str4, StringUtil.format(DOMException.JSON_ERROR_INFO, 8, ""), JSUtil.ERROR, true, false);
            } else if (AbsoluteConst.EVENTS_CLOSE.equals(str2)) {
                this.mBarcodeView.close_scan();
            } else if ("setStyle".equals(str2)) {
                JSONObject createJSONObject3 = JSONUtil.createJSONObject(strArr2[1]);
                if (createJSONObject3 != null) {
                    this.mBarcodeView.upateStyles(createJSONObject3);
                }
            } else if ("addCallBack".equals(str2) && (barcodeFrameItem = this.mBarcodeView) != null) {
                barcodeFrameItem.addCallBackId(strArr2[1], iWebview.getWebviewUUID());
            }
        }
    }

    public JSONObject getJsBarcode() {
        BarcodeFrameItem barcodeFrameItem = this.mBarcodeView;
        if (barcodeFrameItem != null) {
            return barcodeFrameItem.getJsBarcode();
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        BarcodeFrameItem barcodeFrameItem = this.mBarcodeView;
        if (barcodeFrameItem != null) {
            barcodeFrameItem.onDestroy();
            this.mBarcodeView = null;
        }
        this.mIsRegisetedSysEvent = false;
    }

    public boolean onExecute(ISysEventListener.SysEventType sysEventType, Object obj) {
        if (sysEventType == ISysEventListener.SysEventType.onResume) {
            onResume();
            return false;
        } else if (sysEventType != ISysEventListener.SysEventType.onPause) {
            return false;
        } else {
            onPause();
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        BarcodeFrameItem barcodeFrameItem = this.mBarcodeView;
        if (barcodeFrameItem != null) {
            barcodeFrameItem.onPause();
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        BarcodeFrameItem barcodeFrameItem = this.mBarcodeView;
        if (barcodeFrameItem != null) {
            barcodeFrameItem.onResume(true);
        }
    }
}
