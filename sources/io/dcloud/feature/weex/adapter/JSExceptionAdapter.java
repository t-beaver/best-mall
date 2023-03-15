package io.dcloud.feature.weex.adapter;

import android.text.TextUtils;
import com.taobao.weex.adapter.IWXJSExceptionAdapter;
import com.taobao.weex.common.WXJSExceptionInfo;
import io.dcloud.common.util.AppConsoleLogUtil;
import io.dcloud.common.util.BaseInfo;

public class JSExceptionAdapter implements IWXJSExceptionAdapter {
    public void onJSException(WXJSExceptionInfo wXJSExceptionInfo) {
        if (wXJSExceptionInfo != null && !TextUtils.isEmpty(BaseInfo.sCurrentAppOriginalAppid) && BaseInfo.sCurrentAppOriginalAppid.startsWith("__UNI__")) {
            String str = "reportJSException >>>> exception function:" + wXJSExceptionInfo.getFunction() + ", exception:" + wXJSExceptionInfo.getException();
            if (str.endsWith("__ERROR")) {
                str = str.replace("__ERROR", "");
            }
            AppConsoleLogUtil.DCLog(str, "ERROR");
        }
    }
}
