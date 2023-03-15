package com.taobao.weex.ui.action;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.ui.component.richtext.WXRichText;
import java.util.HashMap;

public class GraphicActionUpdateRichtextAttr extends BasicGraphicAction {
    public void executeAction() {
    }

    public GraphicActionUpdateRichtextAttr(WXSDKInstance wXSDKInstance, String str, HashMap<String, String> hashMap, String str2, String str3) {
        super(wXSDKInstance, str3);
        WXRichText wXRichText = (WXRichText) WXSDKManager.getInstance().getWXRenderManager().getWXComponent(getPageId(), str3);
        if (wXRichText != null) {
            HashMap hashMap2 = new HashMap();
            hashMap2.putAll(hashMap);
            wXRichText.updateChildNodeAttrs(str, hashMap2);
        }
    }
}
