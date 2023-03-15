package io.dcloud.net;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IWebview;

public class DownloaderFeatureImpl implements IFeature {
    DownloadJSMgr mDownloadMgr = null;

    public void dispose(String str) {
        DownloadJSMgr downloadJSMgr = this.mDownloadMgr;
        if (downloadJSMgr != null) {
            downloadJSMgr.dispose();
        }
    }

    public String execute(IWebview iWebview, String str, String[] strArr) {
        return this.mDownloadMgr.execute(iWebview, str, strArr);
    }

    public void init(AbsMgr absMgr, String str) {
        this.mDownloadMgr = DownloadJSMgr.getInstance();
    }
}
