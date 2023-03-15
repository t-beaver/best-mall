package io.dcloud.sdk.core.v2.interstitial;

import android.app.Activity;
import io.dcloud.h.c.c.e.a.c;
import io.dcloud.h.c.c.e.c.f.a;
import io.dcloud.sdk.core.api.AOLLoader;
import io.dcloud.sdk.core.entry.DCloudAdSlot;
import io.dcloud.sdk.core.util.AdErrorUtil;
import io.dcloud.sdk.core.v2.base.DCBaseAOL;
import org.json.JSONArray;

public class DCInterstitialAOL extends DCBaseAOL implements AOLLoader.VideoAdInteractionListener {
    private final a b;
    private DCInterstitialAOLListener c;

    public DCInterstitialAOL(Activity activity) {
        super(activity);
        this.b = new a(activity, 15);
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

    public void load(DCloudAdSlot dCloudAdSlot, final DCInterstitialAOLLoadListener dCInterstitialAOLLoadListener) {
        if (getContext() != null && dCloudAdSlot != null) {
            a aVar = this.b;
            if (aVar != null) {
                aVar.a(dCloudAdSlot, new c() {
                    public void onError(int i, String str, JSONArray jSONArray) {
                        DCInterstitialAOLLoadListener dCInterstitialAOLLoadListener = dCInterstitialAOLLoadListener;
                        if (dCInterstitialAOLLoadListener != null) {
                            dCInterstitialAOLLoadListener.onError(i, str, jSONArray);
                        }
                    }

                    public void onLoaded() {
                        DCInterstitialAOLLoadListener dCInterstitialAOLLoadListener = dCInterstitialAOLLoadListener;
                        if (dCInterstitialAOLLoadListener != null) {
                            dCInterstitialAOLLoadListener.onInterstitialAdLoad();
                        }
                    }
                });
            } else if (dCInterstitialAOLLoadListener != null) {
                dCInterstitialAOLLoadListener.onError(-5015, AdErrorUtil.getErrorMsg(-5015), (JSONArray) null);
            }
        } else if (dCInterstitialAOLLoadListener != null) {
            dCInterstitialAOLLoadListener.onError(-5014, AdErrorUtil.getErrorMsg(-5014), (JSONArray) null);
        }
    }

    public void onClick() {
        DCInterstitialAOLListener dCInterstitialAOLListener = this.c;
        if (dCInterstitialAOLListener != null) {
            dCInterstitialAOLListener.onClick();
        }
    }

    public void onClose() {
        DCInterstitialAOLListener dCInterstitialAOLListener = this.c;
        if (dCInterstitialAOLListener != null) {
            dCInterstitialAOLListener.onClose();
        }
    }

    public void onShow() {
        DCInterstitialAOLListener dCInterstitialAOLListener = this.c;
        if (dCInterstitialAOLListener != null) {
            dCInterstitialAOLListener.onShow();
        }
    }

    public void onShowError(int i, String str) {
        DCInterstitialAOLListener dCInterstitialAOLListener = this.c;
        if (dCInterstitialAOLListener != null) {
            dCInterstitialAOLListener.onShowError(i, str);
        }
    }

    public void onSkip() {
        DCInterstitialAOLListener dCInterstitialAOLListener = this.c;
        if (dCInterstitialAOLListener != null) {
            dCInterstitialAOLListener.onSkip();
        }
    }

    public void onVideoPlayEnd() {
        DCInterstitialAOLListener dCInterstitialAOLListener = this.c;
        if (dCInterstitialAOLListener != null) {
            dCInterstitialAOLListener.onVideoPlayEnd();
        }
    }

    public void setInterstitialAdListener(DCInterstitialAOLListener dCInterstitialAOLListener) {
        this.c = dCInterstitialAOLListener;
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
