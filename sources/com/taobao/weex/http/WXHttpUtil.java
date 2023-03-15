package com.taobao.weex.http;

import android.content.Context;
import android.text.TextUtils;
import dc.squareup.HttpConstants;
import io.dcloud.common.util.BaseInfo;
import java.util.Map;

public class WXHttpUtil {
    public static final String KEY_USER_AGENT = "user-agent";
    private static String sDefaultUA;

    public static String assembleUserAgent(Context context, Map<String, String> map) {
        if (TextUtils.isEmpty(sDefaultUA)) {
            String defaultUA = HttpConstants.getDefaultUA();
            sDefaultUA = defaultUA;
            if (TextUtils.isEmpty(defaultUA)) {
                HttpConstants.setUA(BaseInfo.sDefWebViewUserAgent);
                sDefaultUA = HttpConstants.getDefaultUA();
            }
        }
        return sDefaultUA;
    }
}
