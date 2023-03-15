package io.dcloud.sdk.core.v2.reward;

import org.json.JSONObject;

public interface DCRewardAOLListener {
    void onClick();

    void onClose();

    void onReward(JSONObject jSONObject);

    void onShow();

    void onShowError(int i, String str);

    void onSkip();

    void onVideoPlayEnd();
}
