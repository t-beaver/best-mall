package io.dcloud.feature.ui;

import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.BaseFeature;
import io.dcloud.common.DHInterface.IWaiter;
import io.dcloud.common.DHInterface.IWebview;
import org.json.JSONArray;

public class UIFeatureImpl extends BaseFeature implements IWaiter {
    e a = null;

    public void dispose(String str) {
        this.a.b(str);
    }

    public Object doForFeature(String str, Object obj) {
        c a2;
        if (!"findWebview".equals(str)) {
            return null;
        }
        String[] strArr = (String[]) obj;
        String str2 = strArr[0];
        String str3 = strArr[1];
        a aVar = this.a.c.get(str2);
        if (aVar == null || (a2 = aVar.a(str3, str3, str3)) == null) {
            return null;
        }
        return a2.z.obtainWebView();
    }

    public String execute(IWebview iWebview, String str, JSONArray jSONArray) {
        if (AbsoluteConst.UNI_SYNC_EXEC_METHOD.equals(str)) {
            return this.a.a(iWebview, str, jSONArray);
        }
        return this.a.b(iWebview, str, jSONArray);
    }

    public void init(AbsMgr absMgr, String str) {
        this.a = new e(absMgr, str);
    }
}
