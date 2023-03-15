package io.dcloud.sdk.core.api;

import android.app.Activity;
import io.dcloud.sdk.core.api.AOLLoader;

public interface RewardVideoAOL {
    void destroy();

    String getType();

    boolean isValid();

    void setRewardVideoAdInteractionListener(AOLLoader.RewardVideoAdInteractionListener rewardVideoAdInteractionListener);

    void show(Activity activity);
}
