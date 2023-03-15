package io.dcloud.feature.barcode2;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IWaiter;
import io.dcloud.common.DHInterface.IWebview;

public class BarcodeFeatureImpl implements IFeature, IWaiter {
    BarcodeProxyMgr mBProxyMgr;

    public void dispose(String str) {
        this.mBProxyMgr.onDestroy();
    }

    public Object doForFeature(String str, Object obj) {
        return this.mBProxyMgr.doForFeature(str, obj);
    }

    public String execute(IWebview iWebview, String str, String[] strArr) {
        return this.mBProxyMgr.execute(iWebview, str, strArr);
    }

    public void init(AbsMgr absMgr, String str) {
        BarcodeProxyMgr barcodeProxyMgr = BarcodeProxyMgr.getBarcodeProxyMgr();
        this.mBProxyMgr = barcodeProxyMgr;
        barcodeProxyMgr.setFeatureMgr(absMgr);
    }
}
