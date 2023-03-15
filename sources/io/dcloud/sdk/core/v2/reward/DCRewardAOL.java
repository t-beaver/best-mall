package io.dcloud.sdk.core.v2.reward;

import android.app.Activity;
import io.dcloud.h.c.c.e.a.c;
import io.dcloud.h.c.c.e.c.d.a;
import io.dcloud.sdk.core.api.AOLLoader;
import io.dcloud.sdk.core.entry.DCloudAdSlot;
import io.dcloud.sdk.core.util.AdErrorUtil;
import io.dcloud.sdk.core.v2.base.DCBaseAOL;
import org.json.JSONArray;
import org.json.JSONObject;

public class DCRewardAOL extends DCBaseAOL implements AOLLoader.RewardVideoAdInteractionListener {
    private a b;
    private DCRewardAOLListener c;

    public DCRewardAOL(Activity activity) {
        super(activity);
        this.b = new a(activity);
    }

    public void destroy() {
        a aVar = this.b;
        if (aVar != null) {
            aVar.k();
        }
    }

    public String getType() {
        a aVar = this.b;
        return aVar == null ? "" : aVar.getType();
    }

    public boolean isValid() {
        a aVar = this.b;
        if (aVar != null) {
            return aVar.l();
        }
        return false;
    }

    public void load(DCloudAdSlot dCloudAdSlot, final DCRewardAOLLoadListener dCRewardAOLLoadListener) {
        if (getContext() != null && dCloudAdSlot != null) {
            a aVar = this.b;
            if (aVar != null) {
                aVar.a(dCloudAdSlot, new c() {
                    public void onError(int i, String str, JSONArray jSONArray) {
                        DCRewardAOLLoadListener dCRewardAOLLoadListener = dCRewardAOLLoadListener;
                        if (dCRewardAOLLoadListener != null) {
                            dCRewardAOLLoadListener.onError(i, str, jSONArray);
                        }
                    }

                    public void onLoaded() {
                        DCRewardAOLLoadListener dCRewardAOLLoadListener = dCRewardAOLLoadListener;
                        if (dCRewardAOLLoadListener != null) {
                            dCRewardAOLLoadListener.onRewardAdLoad();
                        }
                    }
                });
            } else if (dCRewardAOLLoadListener != null) {
                dCRewardAOLLoadListener.onError(-5015, AdErrorUtil.getErrorMsg(-5015), (JSONArray) null);
            }
        } else if (dCRewardAOLLoadListener != null) {
            dCRewardAOLLoadListener.onError(-5014, AdErrorUtil.getErrorMsg(-5014), (JSONArray) null);
        }
    }

    public void onClick() {
        DCRewardAOLListener dCRewardAOLListener = this.c;
        if (dCRewardAOLListener != null) {
            dCRewardAOLListener.onClick();
        }
    }

    public void onClose() {
        DCRewardAOLListener dCRewardAOLListener = this.c;
        if (dCRewardAOLListener != null) {
            dCRewardAOLListener.onClose();
        }
    }

    public void onReward(JSONObject jSONObject) {
        DCRewardAOLListener dCRewardAOLListener = this.c;
        if (dCRewardAOLListener != null) {
            dCRewardAOLListener.onReward(jSONObject);
        }
    }

    public void onShow() {
        DCRewardAOLListener dCRewardAOLListener = this.c;
        if (dCRewardAOLListener != null) {
            dCRewardAOLListener.onShow();
        }
    }

    public void onShowError(int i, String str) {
        DCRewardAOLListener dCRewardAOLListener = this.c;
        if (dCRewardAOLListener != null) {
            dCRewardAOLListener.onShowError(i, str);
        }
    }

    public void onSkip() {
        DCRewardAOLListener dCRewardAOLListener = this.c;
        if (dCRewardAOLListener != null) {
            dCRewardAOLListener.onSkip();
        }
    }

    public void onVideoPlayEnd() {
        DCRewardAOLListener dCRewardAOLListener = this.c;
        if (dCRewardAOLListener != null) {
            dCRewardAOLListener.onVideoPlayEnd();
        }
    }

    public void setRewardAdListener(DCRewardAOLListener dCRewardAOLListener) {
        this.c = dCRewardAOLListener;
        a aVar = this.b;
        if (aVar != null) {
            aVar.a((AOLLoader.VideoAdInteractionListener) this);
        }
    }

    public void show(Activity activity) {
        a aVar = this.b;
        if (aVar != null) {
            aVar.a(activity);
        }
    }
}
