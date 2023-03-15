package com.taobao.weex.performance;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXConfigAdapter;
import com.taobao.weex.common.Constants;
import org.json.JSONObject;

public class WhiteScreenUtils {
    public static boolean doWhiteScreenCheck() {
        IWXConfigAdapter wxConfigAdapter = WXSDKManager.getInstance().getWxConfigAdapter();
        if (wxConfigAdapter == null) {
            return false;
        }
        double d = 100.0d;
        double random = Math.random() * 100.0d;
        try {
            d = Double.valueOf(wxConfigAdapter.getConfig("wxapm", "new_ws_sampling", "100")).doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (random < d) {
            return true;
        }
        return false;
    }

    public static boolean isWhiteScreen(WXSDKInstance wXSDKInstance) {
        if (wXSDKInstance == null) {
            return false;
        }
        View containerView = wXSDKInstance.getContainerView();
        if ((containerView instanceof ViewGroup) && !isInWhiteList(wXSDKInstance)) {
            return !hasLeafViewOrSizeIgnore(containerView, 3);
        }
        return false;
    }

    private static boolean isInWhiteList(WXSDKInstance wXSDKInstance) {
        IWXConfigAdapter wxConfigAdapter = WXSDKManager.getInstance().getWxConfigAdapter();
        if (wxConfigAdapter == null) {
            return false;
        }
        String config = wxConfigAdapter.getConfig("wxapm", "ws_white_list", (String) null);
        if (TextUtils.isEmpty(config)) {
            return false;
        }
        try {
            for (String str : config.split(";")) {
                if (wXSDKInstance.getBundleUrl() != null && wXSDKInstance.getBundleUrl().contains(str)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean hasLeafViewOrSizeIgnore(View view, int i) {
        if (!(view instanceof ViewGroup)) {
            return true;
        }
        if (i > 0) {
            if (view.getHeight() < 10 || view.getWidth() < 10) {
                return true;
            }
            i--;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        for (int i2 = 0; i2 < viewGroup.getChildCount(); i2++) {
            if (hasLeafViewOrSizeIgnore(viewGroup.getChildAt(i2), i)) {
                return true;
            }
        }
        return false;
    }

    public static String takeViewTreeSnapShot(WXSDKInstance wXSDKInstance) {
        if (wXSDKInstance == null) {
            return "nullInstance";
        }
        JSONObject geViewDetailTreeMsg = geViewDetailTreeMsg(wXSDKInstance.getContainerView());
        return geViewDetailTreeMsg != null ? geViewDetailTreeMsg.toString() : "";
    }

    private static JSONObject geViewDetailTreeMsg(View view) {
        if (view == null) {
            return null;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("width", view.getWidth());
            jSONObject.put("height", view.getHeight());
            int[] iArr = {-1, -1};
            view.getLocationOnScreen(iArr);
            jSONObject.put(Constants.Name.X, iArr[0]);
            jSONObject.put(Constants.Name.Y, iArr[1]);
            if (view instanceof ViewGroup) {
                jSONObject.put("type", view.getClass().getSimpleName());
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    jSONObject.put("child_" + i, geViewDetailTreeMsg(viewGroup.getChildAt(i)));
                }
            } else {
                jSONObject.put("type", view.getClass().getSimpleName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jSONObject;
    }
}
