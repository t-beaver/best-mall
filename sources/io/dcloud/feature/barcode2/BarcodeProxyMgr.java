package io.dcloud.feature.barcode2;

import android.content.Context;
import android.text.TextUtils;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.util.AppRuntime;
import io.dcloud.common.util.JSUtil;
import java.util.LinkedHashMap;

public class BarcodeProxyMgr {
    private static BarcodeProxyMgr mInstance;
    private LinkedHashMap<String, BarcodeProxy> mBProxyCaches = new LinkedHashMap<>();
    private AbsMgr mFeatureMgr;

    public static BarcodeProxyMgr getBarcodeProxyMgr() {
        if (mInstance == null) {
            mInstance = new BarcodeProxyMgr();
        }
        return mInstance;
    }

    public Object doForFeature(String str, Object obj) {
        if (!str.equals("appendToFrameView")) {
            return null;
        }
        Object[] objArr = (Object[]) obj;
        AdaFrameView adaFrameView = (AdaFrameView) objArr[0];
        adaFrameView.obtainApp().obtainAppId();
        BarcodeProxy barcodeProxyByUuid = getBarcodeProxyByUuid((String) objArr[1]);
        if (barcodeProxyByUuid == null) {
            return null;
        }
        barcodeProxyByUuid.appendToFrameView(adaFrameView);
        return null;
    }

    public String execute(IWebview iWebview, String str, String[] strArr) {
        BarcodeProxy barcodeProxy;
        if (str.equals("getBarcodeById")) {
            Context context = iWebview.getContext();
            AppRuntime.checkPrivacyComplianceAndPrompt(context, "Barcode-" + str);
            BarcodeProxy barcodeProxyById = getBarcodeProxyById(strArr[0]);
            if (barcodeProxyById != null) {
                return JSUtil.wrapJsVar(barcodeProxyById.getJsBarcode());
            }
        } else {
            String str2 = strArr[0];
            if (!this.mBProxyCaches.containsKey(str2)) {
                barcodeProxy = new BarcodeProxy();
                this.mBProxyCaches.put(str2, barcodeProxy);
            } else {
                barcodeProxy = this.mBProxyCaches.get(str2);
            }
            barcodeProxy.execute(iWebview, str, strArr);
        }
        return null;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public io.dcloud.common.DHInterface.IWebview findWebviewByUuid(io.dcloud.common.DHInterface.IWebview r8, java.lang.String r9) {
        /*
            r7 = this;
            io.dcloud.common.DHInterface.AbsMgr r0 = r7.mFeatureMgr
            if (r0 == 0) goto L_0x0032
            io.dcloud.common.DHInterface.IMgr$MgrType r1 = io.dcloud.common.DHInterface.IMgr.MgrType.FeatureMgr
            r2 = 4
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r3 = 0
            r2[r3] = r8
            java.lang.String r4 = "ui"
            r5 = 1
            r2[r5] = r4
            java.lang.String r4 = "findWebview"
            r6 = 2
            r2[r6] = r4
            java.lang.String[] r4 = new java.lang.String[r6]
            io.dcloud.common.DHInterface.IApp r8 = r8.obtainApp()
            java.lang.String r8 = r8.obtainAppId()
            r4[r3] = r8
            r4[r5] = r9
            r8 = 3
            r2[r8] = r4
            r8 = 10
            java.lang.Object r8 = r0.processEvent(r1, r8, r2)
            if (r8 == 0) goto L_0x0032
            io.dcloud.common.DHInterface.IWebview r8 = (io.dcloud.common.DHInterface.IWebview) r8
            return r8
        L_0x0032:
            r8 = 0
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: io.dcloud.feature.barcode2.BarcodeProxyMgr.findWebviewByUuid(io.dcloud.common.DHInterface.IWebview, java.lang.String):io.dcloud.common.DHInterface.IWebview");
    }

    public BarcodeProxy getBarcodeProxyById(String str) {
        for (String str2 : this.mBProxyCaches.keySet()) {
            BarcodeProxy barcodeProxy = this.mBProxyCaches.get(str2);
            if (!TextUtils.isEmpty(barcodeProxy.mId) && barcodeProxy.mId.equals(str)) {
                return barcodeProxy;
            }
        }
        return null;
    }

    public BarcodeProxy getBarcodeProxyByUuid(String str) {
        if (this.mBProxyCaches.containsKey(str)) {
            return this.mBProxyCaches.get(str);
        }
        return null;
    }

    public void onDestroy() {
        for (String remove : this.mBProxyCaches.keySet()) {
            BarcodeProxy barcodeProxy = (BarcodeProxy) this.mBProxyCaches.remove(remove);
            if (barcodeProxy != null) {
                barcodeProxy.onDestroy();
            }
        }
        this.mBProxyCaches.clear();
    }

    public void removeBarcodeProxy(String str) {
        BarcodeProxy barcodeProxy = (BarcodeProxy) this.mBProxyCaches.remove(str);
        if (barcodeProxy != null) {
            barcodeProxy.onDestroy();
        }
    }

    public void setFeatureMgr(AbsMgr absMgr) {
        this.mFeatureMgr = absMgr;
    }
}
