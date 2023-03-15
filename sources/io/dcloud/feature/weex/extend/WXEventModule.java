package io.dcloud.feature.weex.extend;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import java.util.HashMap;

public class WXEventModule extends WXModule {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 9;

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
    }

    @JSMethod(uiThread = true)
    public void openURL(String str) {
        if (!TextUtils.isEmpty(str)) {
            String scheme = Uri.parse(str).getScheme();
            StringBuilder sb = new StringBuilder();
            if (TextUtils.equals("http", scheme) || TextUtils.equals("https", scheme) || TextUtils.equals("file", scheme)) {
                sb.append(str);
            } else {
                sb.append("http:");
                sb.append(str);
            }
            Uri parse = Uri.parse(sb.toString());
            Intent intent = new Intent("android.intent.action.VIEW", parse);
            intent.setData(parse);
            this.mWXSDKInstance.getContext().startActivity(intent);
            if (this.mWXSDKInstance.checkModuleEventRegistered("event", this)) {
                this.mWXSDKInstance.fireModuleEvent("event", this, new HashMap());
            }
        }
    }

    @JSMethod(uiThread = true)
    public void fireNativeGlobalEvent(String str, JSCallback jSCallback) {
        HashMap hashMap = new HashMap();
        hashMap.put("eventParam", "value");
        this.mWXSDKInstance.fireGlobalEventCallback(str, hashMap);
        if (jSCallback != null) {
            HashMap hashMap2 = new HashMap();
            hashMap2.put("ok", true);
            jSCallback.invoke(hashMap2);
        }
    }
}
