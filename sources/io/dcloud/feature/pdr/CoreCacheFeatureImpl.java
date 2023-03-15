package io.dcloud.feature.pdr;

import android.content.SharedPreferences;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.util.JSUtil;
import java.io.File;
import java.io.IOException;

public class CoreCacheFeatureImpl implements IFeature {
    public void dispose(String str) {
    }

    public String execute(IWebview iWebview, String str, String[] strArr) {
        if (str.equals("clear")) {
            try {
                DHFile.deleteFile(iWebview.obtainFrameView().obtainApp().obtainAppWebCachePath());
                DHFile.delete(iWebview.getContext().getCacheDir());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Deprecated_JSUtil.excCallbackSuccess(iWebview, strArr[0], "");
            return null;
        } else if (str.equals("calculate")) {
            File file = new File(iWebview.obtainFrameView().obtainApp().obtainAppWebCachePath());
            long j = 0;
            if (file.exists()) {
                j = DHFile.getFileSize(file);
            }
            JSUtil.execCallback(iWebview, strArr[0], (double) (j + DHFile.getFileSize(iWebview.getContext().getCacheDir())), JSUtil.OK, false);
            return null;
        } else if (!str.equals("setMaxSize")) {
            return null;
        } else {
            long parseLong = Long.parseLong(strArr[0]);
            SharedPreferences.Editor edit = iWebview.getContext().getSharedPreferences(iWebview.obtainFrameView().obtainApp().obtainAppId(), 0).edit();
            edit.putLong("maxSize", parseLong);
            edit.commit();
            return null;
        }
    }

    public void init(AbsMgr absMgr, String str) {
    }
}
