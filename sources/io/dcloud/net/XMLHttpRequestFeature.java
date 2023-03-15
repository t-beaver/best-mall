package io.dcloud.net;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IWebview;

public class XMLHttpRequestFeature implements IFeature {
    private XMLHttpRequestMgr mXHRMgr;

    public void dispose(String str) {
    }

    public String execute(IWebview iWebview, String str, String[] strArr) {
        return this.mXHRMgr.execute(iWebview, str, strArr);
    }

    public void init(AbsMgr absMgr, String str) {
        this.mXHRMgr = new XMLHttpRequestMgr();
    }
}
