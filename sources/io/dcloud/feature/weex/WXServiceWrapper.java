package io.dcloud.feature.weex;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.bridge.WXModuleManager;
import com.taobao.weex.common.WXRenderStrategy;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ISysEventListener;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.feature.uniapp.UniSDKInstance;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class WXServiceWrapper extends WXBaseWrapper implements IWXRenderListener {
    IApp mApp;
    JSONObject mData;
    ViewGroup mRootView;
    ISysEventListener mSysEventListener;
    String mTemplate;
    long time = 0;

    public String evalJs(String str, int i) {
        return null;
    }

    public String getType() {
        return "service";
    }

    public void onException(WXSDKInstance wXSDKInstance, String str, String str2) {
    }

    public void onRefreshSuccess(WXSDKInstance wXSDKInstance, int i, int i2) {
    }

    public void onRenderSuccess(WXSDKInstance wXSDKInstance, int i, int i2) {
    }

    public WXServiceWrapper(IApp iApp, ViewGroup viewGroup, String str, JSONObject jSONObject) {
        super(viewGroup.getContext());
        this.mApp = iApp;
        this.mRootView = viewGroup;
        this.mWxId = str;
        this.mData = jSONObject;
        this.mSrcPath = jSONObject.optString(AbsoluteConst.XML_PATH);
        String optString = this.mData.optString("template");
        this.mTemplate = optString;
        render(optString, getOptions(), (String) null);
    }

    public IApp obtanApp() {
        return this.mApp;
    }

    /* access modifiers changed from: package-private */
    public void render(String str, Map<String, Object> map, String str2) {
        if (this.mWXSDKInstance == null) {
            this.mWXSDKInstance = new UniSDKInstance(this.mRootView.getContext());
            this.mWXSDKInstance.registerRenderListener(this);
            this.mWXSDKInstance.setBundleUrl(this.mSrcPath);
        }
        this.mWXSDKInstance.render(this.mWxId, str, map, str2, WXRenderStrategy.APPEND_ASYNC);
        com.alibaba.fastjson.JSONObject registerJsModules = WXModuleManager.getRegisterJsModules();
        if (registerJsModules != null) {
            WeexInstanceMgr.self().setUniNViewModules(registerJsModules.toJSONString());
        }
    }

    public void onViewCreated(WXSDKInstance wXSDKInstance, View view) {
        ViewGroup viewGroup = this.mRootView;
        if (viewGroup != null) {
            viewGroup.addView(this, new ViewGroup.LayoutParams(-1, -1));
            addView(view, new LinearLayout.LayoutParams(-1, -1));
            setVisibility(8);
        }
    }

    public void reload() {
        if (this.time == 0 || System.currentTimeMillis() - this.time >= 600) {
            this.time = System.currentTimeMillis();
            if (this.mWXSDKInstance != null) {
                this.mWXSDKInstance.registerRenderListener((IWXRenderListener) null);
                this.mWXSDKInstance.destroy();
                this.mWXSDKInstance = null;
                removeAllViews();
            }
            if (!TextUtils.isEmpty(this.mTemplate)) {
                render(this.mTemplate, getOptions(), (String) null);
            }
        }
    }

    public Map<String, Object> getOptions() {
        HashMap hashMap = new HashMap();
        hashMap.put("plus_appid", this.mApp.obtainAppId());
        hashMap.put("bundleUrl", this.mSrcPath);
        return hashMap;
    }

    public void onDestroy() {
        if (this.mApp != null) {
            this.mApp = null;
        }
        ViewGroup viewGroup = this.mRootView;
        if (viewGroup != null) {
            viewGroup.removeView(this);
        }
        if (this.mWXSDKInstance != null) {
            this.mWXSDKInstance.onActivityDestroy();
            this.mWXSDKInstance = null;
        }
        this.mData = null;
    }

    public void findWebViewToLoadUrL(String str, String str2) {
        if (this.mApp != null) {
            WeexInstanceMgr self = WeexInstanceMgr.self();
            IApp iApp = this.mApp;
            IWebview findWebview = self.findWebview((IWebview) null, iApp, iApp.obtainAppId(), str2);
            if (findWebview != null) {
                findWebview.loadUrl(str);
            }
        }
    }
}
