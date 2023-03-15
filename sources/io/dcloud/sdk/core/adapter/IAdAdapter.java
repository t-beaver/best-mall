package io.dcloud.sdk.core.adapter;

import android.app.Activity;
import io.dcloud.sdk.core.entry.DCloudAdSlot;
import io.dcloud.sdk.core.module.DCBaseAOLLoader;
import java.util.Map;

public interface IAdAdapter {
    DCBaseAOLLoader getAd(Activity activity, DCloudAdSlot dCloudAdSlot);

    String getAdapterSDKVersion();

    String getSDKVersion();

    boolean isSupport();

    void setPersonalAd(boolean z);

    void updatePrivacyConfig(Map<String, Boolean> map);
}
