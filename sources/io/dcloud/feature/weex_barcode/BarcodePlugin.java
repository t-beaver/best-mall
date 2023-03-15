package io.dcloud.feature.weex_barcode;

import android.content.Context;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;
import com.taobao.weex.ui.component.WXComponent;
import io.dcloud.feature.weex.WeexInstanceMgr;

public class BarcodePlugin {
    public static void initPlugin(Context context) {
        try {
            WXSDKEngine.registerComponent("barcode", (Class<? extends WXComponent>) BarcodeComponent.class);
            WXSDKEngine.registerModule("barcodeScan", BarcodeModule.class);
            WeexInstanceMgr.self().addComponentByName("barcode", BarcodeComponent.class);
        } catch (WXException e) {
            e.printStackTrace();
        }
    }
}
