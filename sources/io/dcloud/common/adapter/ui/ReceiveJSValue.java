package io.dcloud.common.adapter.ui;

import android.os.Build;
import android.webkit.JavascriptInterface;
import io.dcloud.common.DHInterface.IReflectAble;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.util.JSONUtil;
import java.util.HashMap;
import org.json.JSONArray;

public class ReceiveJSValue implements IReflectAble {
    public static String SYNC_HANDLER = "SYNC_HANDLER";
    private static HashMap<String, ReceiveJSValueCallback> arrs = new HashMap<>();
    private String android42Js = null;

    public interface ReceiveJSValueCallback {
        String callback(JSONArray jSONArray);
    }

    public static final void addJavascriptInterface(AdaWebview adaWebview) {
        if (Build.VERSION.SDK_INT > 17) {
            adaWebview.mWebViewImpl.addJavascriptInterface(new ReceiveJSValue(), SYNC_HANDLER);
            return;
        }
        ReceiveJSValue receiveJSValue = new ReceiveJSValue();
        adaWebview.mReceiveJSValue_android42 = receiveJSValue;
        receiveJSValue.android42Js = "window.SYNC_HANDLER||(window.SYNC_HANDLER = {__js__call__native__: function(uuid, js) {return window.prompt('__js__call__native__','sync:' + JSON.stringify([uuid, js]));}});";
    }

    public static final String registerCallback(String str, ReceiveJSValueCallback receiveJSValueCallback) {
        return registerCallback((AdaWebview) null, str, receiveJSValueCallback);
    }

    @JavascriptInterface
    public final String __js__call__native__(String str, String str2) {
        ReceiveJSValueCallback remove = arrs.remove(str);
        Logger.d("ReceiveJSValue", "__js__call__native__ js=" + str2);
        return remove != null ? remove.callback(JSONUtil.createJSONArray(str2)) : "";
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x000f, code lost:
        r2 = r2.mReceiveJSValue_android42;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final java.lang.String registerCallback(io.dcloud.common.adapter.ui.AdaWebview r2, java.lang.String r3, io.dcloud.common.adapter.ui.ReceiveJSValue.ReceiveJSValueCallback r4) {
        /*
            int r0 = r4.hashCode()
            java.lang.String r0 = java.lang.String.valueOf(r0)
            java.util.HashMap<java.lang.String, io.dcloud.common.adapter.ui.ReceiveJSValue$ReceiveJSValueCallback> r1 = arrs
            r1.put(r0, r4)
            if (r2 == 0) goto L_0x0016
            io.dcloud.common.adapter.ui.ReceiveJSValue r2 = r2.mReceiveJSValue_android42
            if (r2 == 0) goto L_0x0016
            java.lang.String r2 = r2.android42Js
            goto L_0x0018
        L_0x0016:
            java.lang.String r2 = ""
        L_0x0018:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r4.append(r2)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r1 = "window.SYNC_HANDLER && "
            r2.append(r1)
            java.lang.String r1 = SYNC_HANDLER
            r2.append(r1)
            java.lang.String r1 = ".__js__call__native__('"
            r2.append(r1)
            r2.append(r0)
            java.lang.String r0 = "',(function(){var ret = %s;var type = (typeof ret );return JSON.stringify([type,ret]);})());"
            r2.append(r0)
            java.lang.String r2 = r2.toString()
            r0 = 1
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r1 = 0
            r0[r1] = r3
            java.lang.String r2 = io.dcloud.common.util.StringUtil.format(r2, r0)
            r4.append(r2)
            java.lang.String r2 = r4.toString()
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.common.adapter.ui.ReceiveJSValue.registerCallback(io.dcloud.common.adapter.ui.AdaWebview, java.lang.String, io.dcloud.common.adapter.ui.ReceiveJSValue$ReceiveJSValueCallback):java.lang.String");
    }
}
