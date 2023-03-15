package io.dcloud.feature.weex_barcode;

import android.content.Context;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXResourceUtils;
import io.dcloud.common.util.AppRuntime;
import io.dcloud.common.util.PdrUtil;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class BarcodeComponent extends WXComponent<BarcodeView> {
    /* access modifiers changed from: private */
    public boolean isAnimationEnd = false;
    private AtomicBoolean isLoad = new AtomicBoolean(false);

    @JSMethod
    public void close() {
    }

    public BarcodeComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    public BarcodeComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, int i, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, i, basicComponentData);
    }

    /* access modifiers changed from: protected */
    public BarcodeView initComponentHostView(Context context) {
        AppRuntime.checkPrivacyComplianceAndPrompt(context, "nvue-Barcode");
        return new BarcodeView(context, this, getInstance());
    }

    public void updateProperties(Map<String, Object> map) {
        super.updateProperties(map);
        if (getHostView() != null && !getAttrs().containsKey("background")) {
            ((BarcodeView) getHostView()).setBackgroundColor(-16777216);
        }
    }

    @WXComponentProp(name = "frameColor")
    public void setFrameColor(String str) {
        ((BarcodeView) getHostView()).setFrameColor(WXResourceUtils.getColor(str));
    }

    @WXComponentProp(name = "autoDecodeCharset")
    public void setAutoDecodeCharset(boolean z) {
        ((BarcodeView) getHostView()).setAutoDecodeCharset(z);
    }

    @WXComponentProp(name = "autoZoom")
    public void setAutoZoom(boolean z) {
        ((BarcodeView) getHostView()).setAutoZoom(z);
    }

    @WXComponentProp(name = "background")
    public void setBackground(String str) {
        ((BarcodeView) getHostView()).setBackground(WXResourceUtils.getColor(str));
    }

    @WXComponentProp(name = "scanbarColor")
    public void setScanbarColor(String str) {
        ((BarcodeView) getHostView()).setScanBarColor(WXResourceUtils.getColor(str));
    }

    @WXComponentProp(name = "filters")
    public void setFilters(JSONArray jSONArray) {
        ((BarcodeView) getHostView()).initDecodeFormats(jSONArray);
    }

    @WXComponentProp(name = "autostart")
    public void setSutoStart(final boolean z) {
        if (!this.isAnimationEnd) {
            getInstance().addFrameViewEventListener(new WXSDKInstance.FrameViewEventListener() {
                public void onShowAnimationEnd() {
                    boolean unused = BarcodeComponent.this.isAnimationEnd = true;
                    if (z) {
                        BarcodeComponent.this.start((JSONObject) null);
                    }
                    BarcodeComponent.this.getInstance().removeFrameViewEventListener(this);
                }
            });
        } else if (z) {
            start((JSONObject) null);
        }
    }

    @JSMethod
    public void start(JSONObject jSONObject) {
        if (jSONObject != null) {
            boolean z = false;
            ((BarcodeView) getHostView()).setConserve(jSONObject.containsKey("conserve") ? jSONObject.getBoolean("conserve").booleanValue() : false);
            ((BarcodeView) getHostView()).setFilename(PdrUtil.getDefaultPrivateDocPath(jSONObject.getString(AbsoluteConst.JSON_KEY_FILENAME), "png"));
            ((BarcodeView) getHostView()).setVibrate(jSONObject.containsKey("vibrate") ? jSONObject.getBoolean("vibrate").booleanValue() : true);
            BarcodeView barcodeView = (BarcodeView) getHostView();
            if (!jSONObject.containsKey("sound") || jSONObject.getString("sound").equals("default")) {
                z = true;
            }
            barcodeView.setPlayBeep(z);
        }
        ((BarcodeView) getHostView()).start();
    }

    /* access modifiers changed from: protected */
    public void setHostLayoutParams(BarcodeView barcodeView, int i, int i2, int i3, int i4, int i5, int i6) {
        super.setHostLayoutParams(barcodeView, i, i2, i3, i4, i5, i6);
        if (!this.isLoad.get()) {
            this.isLoad.set(true);
            ((BarcodeView) getHostView()).initBarcodeView(i, i2);
            return;
        }
        ((BarcodeView) getHostView()).updateStyles(i, i2);
    }

    @JSMethod
    public void cancel() {
        ((BarcodeView) getHostView()).cancelScan();
    }

    @JSMethod
    public void setFlash(boolean z) {
        if (!PdrUtil.isEmpty(Boolean.valueOf(z))) {
            ((BarcodeView) getHostView()).setFlash(z);
        }
    }

    public void destroy() {
        super.destroy();
        ((BarcodeView) getHostView()).closeScan();
        ((BarcodeView) getHostView()).onDestory();
    }

    public void onActivityResume() {
        super.onActivityResume();
        ((BarcodeView) getHostView()).onResume(true);
    }

    public void onActivityPause() {
        super.onActivityPause();
        ((BarcodeView) getHostView()).onPause();
    }
}
