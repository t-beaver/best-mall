package io.dcloud.share;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IWebview;

public class ShareFeatureImpl implements IFeature {
    private a a;

    public void dispose(String str) {
        a aVar;
        if (str == null && (aVar = this.a) != null) {
            aVar.a();
        }
    }

    public String execute(IWebview iWebview, String str, String[] strArr) {
        return this.a.a(iWebview, str, strArr);
    }

    public void init(AbsMgr absMgr, String str) {
        this.a = new a(absMgr, str);
    }
}
