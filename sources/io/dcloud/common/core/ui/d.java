package io.dcloud.common.core.ui;

import android.content.Context;
import io.dcloud.common.DHInterface.IApp;
import io.dcloud.common.adapter.ui.AdaUniWebView;
import org.json.JSONObject;

public class d extends AdaUniWebView {
    protected d(Context context, b bVar, String str, String str2, JSONObject jSONObject, boolean z) {
        super(context, bVar.obtainApp(), bVar, str, str2, jSONObject, z);
        bVar.q = this;
        bVar.r = getWebviewParent();
    }

    public IApp obtainApp() {
        if (obtainFrameView() != null) {
            return obtainFrameView().obtainApp();
        }
        return null;
    }
}
