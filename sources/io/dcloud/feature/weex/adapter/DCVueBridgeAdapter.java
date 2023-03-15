package io.dcloud.feature.weex.adapter;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.bridge.IDCVueBridgeAdapter;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaUniWebView;
import io.dcloud.feature.weex.WeexInstanceMgr;

public class DCVueBridgeAdapter implements IDCVueBridgeAdapter {
    public void exec(WXSDKInstance wXSDKInstance, String str, String str2) {
        IWebview findWebview = WeexInstanceMgr.self().findWebview(wXSDKInstance);
        if (findWebview instanceof AdaUniWebView) {
            ((AdaUniWebView) findWebview).prompt(str, str2);
        }
    }

    public String execSync(WXSDKInstance wXSDKInstance, String str, String str2) {
        IWebview findWebview = WeexInstanceMgr.self().findWebview(wXSDKInstance);
        return findWebview instanceof AdaUniWebView ? ((AdaUniWebView) findWebview).prompt(str, str2) : "";
    }
}
