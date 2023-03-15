package io.dcloud.e.e;

import android.content.Context;
import android.webkit.URLUtil;
import com.taobao.weex.ui.module.WXModalUIModule;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.DHInterface.IPdrModule;
import io.dcloud.common.adapter.io.DHFile;
import io.dcloud.common.adapter.util.Logger;
import io.dcloud.common.adapter.util.SP;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.NetTool;
import io.dcloud.e.c.h.b;
import org.json.JSONObject;

public class a implements IPdrModule {
    String a = "CommitModule";

    /* renamed from: io.dcloud.e.e.a$a  reason: collision with other inner class name */
    class C0034a implements ICallBack {
        final /* synthetic */ String a;

        C0034a(String str) {
            this.a = str;
        }

        public Object onCallBack(int i, Object obj) {
            Object[] objArr = (Object[]) obj;
            a.this.a((IApp) objArr[0], this.a, (byte[]) objArr[2]);
            return null;
        }
    }

    public String execute(String str, Object obj) {
        str.hashCode();
        if (!str.equals("start_up") || !(obj instanceof Object[])) {
            return null;
        }
        Object[] objArr = (Object[]) obj;
        a((IApp) objArr[0], (String) objArr[1], (String) objArr[2]);
        return null;
    }

    public void onDestroy() {
    }

    private void a(IApp iApp, String str, String str2) {
        b.a(iApp, str, 0, str2, new C0034a(str));
    }

    /* access modifiers changed from: private */
    public void a(IApp iApp, String str, byte[] bArr) {
        if (bArr != null) {
            try {
                JSONObject jSONObject = new JSONObject(new String(bArr));
                if (!jSONObject.isNull("ret")) {
                    if (jSONObject.optInt("ret") == 0 && WXModalUIModule.OK.equals(jSONObject.opt("desc")) && !jSONObject.isNull("did")) {
                        SP.setBundleData(SP.getOrCreateBundle((Context) iApp.getActivity(), "pdr"), SP.STARTUP_DEVICE_ID, jSONObject.optString("did"));
                    }
                }
                if (!BaseInfo.ISDEBUG && !jSONObject.isNull("ret") && jSONObject.optInt("ret") == 0 && WXModalUIModule.OK.equals(jSONObject.opt("desc")) && jSONObject.has("urd")) {
                    String optString = jSONObject.optString("urd");
                    if (URLUtil.isNetworkUrl(optString)) {
                        DHFile.writeFile(NetTool.httpGet(optString, false), 0, BaseInfo.sURDFilePath);
                    }
                }
            } catch (Exception e) {
                Logger.p(this.a, e.getMessage());
            }
        }
    }
}
