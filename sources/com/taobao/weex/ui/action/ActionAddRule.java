package com.taobao.weex.ui.action;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.common.Constants;
import com.taobao.weex.font.FontAdapter;
import com.taobao.weex.utils.FontDO;
import com.taobao.weex.utils.TypefaceUtil;

public class ActionAddRule implements IExecutable {
    private final JSONObject mData;
    private final String mPageId;
    private final String mType;

    public ActionAddRule(String str, String str2, JSONObject jSONObject) {
        this.mPageId = str;
        this.mType = str2;
        this.mData = jSONObject;
    }

    public void executeAction() {
        FontDO parseFontDO;
        WXSDKInstance wXSDKInstance = WXSDKManager.getInstance().getWXRenderManager().getWXSDKInstance(this.mPageId);
        if (wXSDKInstance != null && !wXSDKInstance.isDestroy() && Constants.Name.FONT_FACE.equals(this.mType) && (parseFontDO = parseFontDO(this.mData, wXSDKInstance)) != null && !TextUtils.isEmpty(parseFontDO.getFontFamilyName())) {
            notifyAddFontRule(wXSDKInstance, parseFontDO);
            FontDO fontDO = TypefaceUtil.getFontDO(parseFontDO.getFontFamilyName());
            if (fontDO == null || !TextUtils.equals(fontDO.getUrl(), parseFontDO.getUrl())) {
                TypefaceUtil.putFontDO(parseFontDO);
                TypefaceUtil.loadTypeface(parseFontDO, true);
                return;
            }
            TypefaceUtil.loadTypeface(fontDO, true);
        }
    }

    private FontDO parseFontDO(JSONObject jSONObject, WXSDKInstance wXSDKInstance) {
        if (jSONObject == null) {
            return null;
        }
        return new FontDO(jSONObject.getString(Constants.Name.FONT_FAMILY), jSONObject.getString("src"), wXSDKInstance);
    }

    private void notifyAddFontRule(WXSDKInstance wXSDKInstance, FontDO fontDO) {
        FontAdapter fontAdapter = WXSDKManager.getInstance().getFontAdapter();
        if (fontAdapter != null) {
            fontAdapter.onAddFontRule(wXSDKInstance.getInstanceId(), fontDO.getFontFamilyName(), fontDO.getUrl());
        }
    }
}
