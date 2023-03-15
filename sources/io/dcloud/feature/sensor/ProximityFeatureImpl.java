package io.dcloud.feature.sensor;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IWebview;

public class ProximityFeatureImpl implements IFeature {
    f a = null;

    public void dispose(String str) {
    }

    public String execute(IWebview iWebview, String str, String[] strArr) {
        return this.a.a(iWebview, str, strArr);
    }

    public void init(AbsMgr absMgr, String str) {
        this.a = new f();
    }
}
