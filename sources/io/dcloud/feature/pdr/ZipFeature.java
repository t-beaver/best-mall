package io.dcloud.feature.pdr;

import androidtranscoder.VideoCompressor;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.FileUtil;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.common.util.StringUtil;
import io.dcloud.common.util.ZipUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ZipFeature implements IFeature {
    HashMap<String, ArrayList<String[]>> a = null;

    class a implements Runnable {
        boolean a = false;
        String b = null;
        String c;
        String d;
        String e;
        IWebview f;

        a() {
        }

        public void run() {
            try {
                if (this.a) {
                    Logger.d("compress mUnZipDirPath=" + this.d + ";mZipFilePath" + this.c);
                    File file = new File(this.d);
                    if (!file.exists()) {
                        Deprecated_JSUtil.excCallbackError(this.f, this.e, StringUtil.format(DOMException.JSON_ERROR_INFO, 2, this.b + " open failed:ENOENT (No such file or directory)"), true);
                    }
                    if (!JSUtil.checkOperateDirErrorAndCallback(this.f, this.e, this.c)) {
                        File file2 = new File(this.c);
                        File[] fileArr = null;
                        if (file.isDirectory()) {
                            fileArr = file2.listFiles();
                        }
                        if (file.isFile() || fileArr == null) {
                            fileArr = new File[]{file};
                        }
                        if (!FileUtil.checkPathAccord(this.f.getContext(), file.getPath(), file2.getPath())) {
                            Deprecated_JSUtil.excCallbackError(this.f, this.e, StringUtil.format(DOMException.JSON_ERROR_INFO, 2, DOMException.MSG_PATH_NOT_PRIVATE_ERROR), true);
                            return;
                        }
                        ZipUtils.zipFiles(fileArr, file2);
                    } else {
                        return;
                    }
                } else {
                    Logger.d("decompress mUnZipDirPath=" + this.d + ";mZipFilePath" + this.c);
                    if (!JSUtil.checkOperateDirErrorAndCallback(this.f, this.e, this.d)) {
                        File file3 = new File(this.c);
                        if (!file3.exists()) {
                            Deprecated_JSUtil.excCallbackError(this.f, this.e, StringUtil.format(DOMException.JSON_ERROR_INFO, 2, this.b + " open failed:ENOENT (No such file or directory)"), true);
                            return;
                        }
                        if (!FileUtil.checkPathAccord(this.f.getContext(), file3.getPath(), this.d)) {
                            Deprecated_JSUtil.excCallbackError(this.f, this.e, StringUtil.format(DOMException.JSON_ERROR_INFO, 2, DOMException.MSG_PATH_NOT_PRIVATE_ERROR), true);
                            return;
                        }
                        ZipUtils.upZipFile(file3, this.d);
                    } else {
                        return;
                    }
                }
                Deprecated_JSUtil.excCallbackSuccess(this.f, this.e, "");
            } catch (Exception e2) {
                Deprecated_JSUtil.excCallbackError(this.f, this.e, StringUtil.format(DOMException.JSON_ERROR_INFO, 2, e2.getMessage()), true);
            }
        }
    }

    private void a(a aVar) {
        new Thread(aVar).start();
    }

    public void dispose(String str) {
    }

    public String execute(IWebview iWebview, String str, String[] strArr) {
        if (PdrUtil.isEquals(str, "compress")) {
            a aVar = new a();
            aVar.f = iWebview;
            aVar.a = true;
            IApp obtainApp = iWebview.obtainFrameView().obtainApp();
            aVar.b = strArr[0];
            aVar.d = obtainApp.convert2AbsFullPath(iWebview.obtainFullUrl(), strArr[0]);
            String str2 = strArr[1];
            if (str2 == null) {
                str2 = AbsoluteConst.MINI_SERVER_APP_DOC + System.currentTimeMillis();
            }
            if (!str2.endsWith(".zip")) {
                str2 = str2 + ".zip";
            }
            aVar.c = obtainApp.convert2AbsFullPath(iWebview.obtainFullUrl(), str2);
            aVar.e = strArr[2];
            a(aVar);
            return null;
        } else if (PdrUtil.isEquals(str, "decompress")) {
            a aVar2 = new a();
            aVar2.f = iWebview;
            aVar2.a = false;
            IApp obtainApp2 = iWebview.obtainFrameView().obtainApp();
            aVar2.c = obtainApp2.convert2AbsFullPath(iWebview.obtainFullUrl(), obtainApp2.checkPrivateDirAndCopy2Temp(strArr[0]));
            aVar2.d = obtainApp2.convert2AbsFullPath(iWebview.obtainFullUrl(), strArr[1]);
            aVar2.e = strArr[2];
            a(aVar2);
            return null;
        } else if (PdrUtil.isEquals(str, "compressImage")) {
            a.b(iWebview, strArr);
            return null;
        } else if (!PdrUtil.isEquals(str, "compressVideo")) {
            return null;
        } else {
            VideoCompressor.getInstance().compressVideo(iWebview, strArr);
            return null;
        }
    }

    public void init(AbsMgr absMgr, String str) {
        this.a = new HashMap<>(1);
    }
}
