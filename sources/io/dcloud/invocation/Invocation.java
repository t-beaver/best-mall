package io.dcloud.invocation;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.feature.internal.sdk.SDK;

public class Invocation implements IFeature {
    a a;

    public void dispose(String str) {
        this.a.a(str);
    }

    public String execute(IWebview iWebview, String str, String[] strArr) {
        if (!SDK.isUniMPSDK() || SDK.isNJS) {
            return this.a.a(iWebview, str, strArr);
        }
        return "";
    }

    public void init(AbsMgr absMgr, String str) {
        this.a = new a(absMgr);
    }
}
