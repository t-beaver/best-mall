package com.taobao.weex.ui.action;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.ui.component.richtext.WXRichText;
import java.util.HashMap;

public class GraphicActionAddChildToRichtext extends BasicGraphicAction {
    public void executeAction() {
    }

    public GraphicActionAddChildToRichtext(WXSDKInstance wXSDKInstance, String str, String str2, String str3, String str4, HashMap<String, String> hashMap, HashMap<String, String> hashMap2) {
        super(wXSDKInstance, str4);
        WXRichText wXRichText;
        if (WXSDKManager.getInstance() != null && WXSDKManager.getInstance().getWXRenderManager() != null && (wXRichText = (WXRichText) WXSDKManager.getInstance().getWXRenderManager().getWXComponent(getPageId(), str4)) != null) {
            wXRichText.AddChildNode(str2, str, str3, hashMap, hashMap2);
        }
    }
}
