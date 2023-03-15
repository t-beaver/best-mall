package io.dcloud.feature.pdr;

import com.taobao.weex.common.WXConfig;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.io.DHFile;
import java.io.IOException;

public class LoggerFeatureImpl implements IFeature {
    public void dispose(String str) {
    }

    public String execute(IWebview iWebview, String str, String[] strArr) {
        if (str.equals(WXConfig.logLevel)) {
            b.a(iWebview.obtainFrameView().obtainApp().obtainAppLog());
            if (strArr[0].equals("LOG")) {
                b.d("LOG", strArr[1]);
                return null;
            } else if (strArr[0].equals("ERROR")) {
                b.e("ERROR", strArr[1]);
                return null;
            } else if (strArr[0].equals("WARN")) {
                b.a("WARN", strArr[1]);
                return null;
            } else if (strArr[0].equals("INFO")) {
                b.i("INFO", strArr[1]);
                return null;
            } else {
                strArr[0].equals("ASSERT");
                return null;
            }
        } else if (!str.equals("clear")) {
            return null;
        } else {
            try {
                DHFile.deleteFile(iWebview.obtainFrameView().obtainApp().obtainAppLog());
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public void init(AbsMgr absMgr, String str) {
    }
}
