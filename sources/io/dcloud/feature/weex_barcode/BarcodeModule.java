package io.dcloud.feature.weex_barcode;

import android.graphics.BitmapFactory;
import android.net.Uri;
import com.alibaba.fastjson.JSONArray;
import com.dcloud.zxing2.Result;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.ui.component.WXImage;
import io.dcloud.common.util.JSONUtil;
import io.dcloud.feature.barcode2.decoding.CaptureActivityHandler;
import java.util.HashMap;

public class BarcodeModule extends WXModule {
    @JSMethod
    public void scan(String str, JSCallback jSCallback, JSONArray jSONArray, boolean z) {
        try {
            String path = this.mWXSDKInstance.rewriteUri(Uri.parse(str), "image").getPath();
            Result decode = CaptureActivityHandler.decode(BitmapFactory.decodeFile(path), z);
            if (decode != null) {
                HashMap hashMap = new HashMap();
                hashMap.put("type", WXImage.SUCCEED);
                hashMap.put("code", decode.getBarcodeFormat().toString());
                hashMap.put("message", JSONUtil.toJSONableString(decode.getText()));
                if (path == null) {
                    path = "";
                }
                hashMap.put("file", path);
                hashMap.put("charSet", decode.textCharset);
                HashMap hashMap2 = new HashMap();
                hashMap2.put("detail", hashMap);
                jSCallback.invoke(hashMap2);
                return;
            }
            HashMap hashMap3 = new HashMap();
            hashMap3.put("type", Constants.Event.FAIL);
            hashMap3.put("code", 8);
            hashMap3.put("message", "");
            hashMap3.put("charSet", decode.textCharset);
            HashMap hashMap4 = new HashMap();
            hashMap4.put("detail", hashMap3);
            jSCallback.invoke(hashMap4);
        } catch (Exception e) {
            HashMap hashMap5 = new HashMap();
            hashMap5.put("type", Constants.Event.FAIL);
            hashMap5.put("code", 8);
            hashMap5.put("message", e.getMessage());
            HashMap hashMap6 = new HashMap();
            hashMap6.put("detail", hashMap5);
            jSCallback.invoke(hashMap6);
        }
    }
}
