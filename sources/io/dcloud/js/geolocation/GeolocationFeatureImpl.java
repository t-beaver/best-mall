package io.dcloud.js.geolocation;

import android.content.Context;
import android.text.TextUtils;
import io.dcloud.common.DHInterface.AbsMgr;
import io.dcloud.common.DHInterface.FeatureMessageDispatcher;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.IFeature;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.util.PermissionUtil;
import io.dcloud.common.constant.DOMException;
import io.dcloud.common.util.AppRuntime;
import io.dcloud.common.util.JSUtil;
import io.dcloud.common.util.ReflectUtils;

public class GeolocationFeatureImpl implements IFeature {
    /* access modifiers changed from: private */
    public a a;
    /* access modifiers changed from: private */
    public boolean b = false;

    class a extends PermissionUtil.StreamPermissionRequest {
        final /* synthetic */ IWebview a;
        final /* synthetic */ String b;
        final /* synthetic */ String[] c;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        a(IApp iApp, IWebview iWebview, String str, String[] strArr) {
            super(iApp);
            this.a = iWebview;
            this.b = str;
            this.c = strArr;
        }

        public void onDenied(String str) {
            Deprecated_JSUtil.execCallback(this.a, this.c[0], DOMException.toJSON(22, DOMException.MSG_GEOLOCATION_PERMISSION_ERROR), JSUtil.ERROR, true, false);
        }

        public void onGranted(String str) {
            if (!GeolocationFeatureImpl.this.b) {
                boolean unused = GeolocationFeatureImpl.this.b = true;
                GeolocationFeatureImpl.this.a.a(this.a, this.b, this.c);
            }
            int i = ReflectUtils.getApplicationContext().getApplicationInfo().targetSdkVersion;
        }
    }

    public void dispose(String str) {
        if (TextUtils.isEmpty(str)) {
            this.a.a();
        }
    }

    public String execute(IWebview iWebview, String str, String[] strArr) {
        if (FeatureMessageDispatcher.contains("record_address")) {
            this.a.a(iWebview, str, strArr);
            return null;
        }
        Context context = iWebview.getContext();
        AppRuntime.checkPrivacyComplianceAndPrompt(context, "Geolocation-" + str);
        this.b = false;
        PermissionUtil.usePermission(iWebview.getActivity(), IFeature.F_GEOLOCATION, PermissionUtil.PMS_LOCATION, 2, new a(iWebview.obtainApp(), iWebview, str, strArr));
        return null;
    }

    public void init(AbsMgr absMgr, String str) {
        this.a = new a(absMgr);
    }
}
