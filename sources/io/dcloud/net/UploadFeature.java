package io.dcloud.net;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IWebview;

public class UploadFeature implements IFeature {
    private JsUploadMgr mJsUploadMgr;

    public void dispose(String str) {
    }

    public String execute(IWebview iWebview, String str, String[] strArr) {
        this.mJsUploadMgr.execute(iWebview, str, strArr);
        return null;
    }

    public void init(AbsMgr absMgr, String str) {
        this.mJsUploadMgr = new JsUploadMgr();
    }
}
