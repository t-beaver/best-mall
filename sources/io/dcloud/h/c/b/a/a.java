package io.dcloud.h.c.b.a;

import android.app.Activity;
import io.dcloud.sdk.core.adapter.IAdAdapter;
import io.dcloud.sdk.core.entry.DCloudAdSlot;
import io.dcloud.sdk.core.module.DCBaseAOLLoader;
import java.util.Map;

public class a implements IAdAdapter {
    public DCBaseAOLLoader getAd(Activity activity, DCloudAdSlot dCloudAdSlot) {
        if (dCloudAdSlot.getType() == 1) {
            return new b(dCloudAdSlot, activity);
        }
        return null;
    }

    public String getAdapterSDKVersion() {
        return "1.9.9.81676";
    }

    public String getSDKVersion() {
        return null;
    }

    public boolean isSupport() {
        return true;
    }

    public void setPersonalAd(boolean z) {
    }

    public void updatePrivacyConfig(Map<String, Boolean> map) {
    }
}
