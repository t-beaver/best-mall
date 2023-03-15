package com.taobao.weex;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.alibaba.fastjson.JSON;
import com.taobao.weex.utils.WXLogUtils;
import java.util.HashMap;

public class WXGlobalEventReceiver extends BroadcastReceiver {
    public static final String EVENT_ACTION = "wx_global_action";
    public static final String EVENT_NAME = "eventName";
    public static final String EVENT_PARAMS = "eventParams";
    public static final String EVENT_WX_INSTANCEID = "wx_instanceid";
    private WXSDKInstance mWXSDKInstance;

    public WXGlobalEventReceiver() {
    }

    public WXGlobalEventReceiver(WXSDKInstance wXSDKInstance) {
        this.mWXSDKInstance = wXSDKInstance;
    }

    public void onReceive(Context context, Intent intent) {
        String stringExtra = intent.getStringExtra(EVENT_NAME);
        try {
            this.mWXSDKInstance.fireGlobalEventCallback(stringExtra, (HashMap) JSON.parseObject(intent.getStringExtra(EVENT_PARAMS), HashMap.class));
        } catch (Exception e) {
            WXLogUtils.e("global-receive", (Throwable) e);
        }
    }
}
