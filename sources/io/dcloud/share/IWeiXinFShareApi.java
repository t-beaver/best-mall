package io.dcloud.share;

import io.dcloud.common.DHInterface.IWebview;

public interface IWeiXinFShareApi extends IFShareApi {
    void launchMiniProgram(IWebview iWebview, String str, String str2);

    void openCustomerServiceChat(IWebview iWebview, String str, String str2);
}
