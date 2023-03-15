package io.dcloud.sdk.core.v2.fullscreen;

import android.app.Activity;
import io.dcloud.h.c.c.e.a.c;
import io.dcloud.h.c.c.e.c.f.a;
import io.dcloud.sdk.core.api.AOLLoader;
import io.dcloud.sdk.core.entry.DCloudAdSlot;
import io.dcloud.sdk.core.util.AdErrorUtil;
import io.dcloud.sdk.core.v2.base.DCBaseAOL;
import org.json.JSONArray;

public class DCFullScreenAOL extends DCBaseAOL implements AOLLoader.VideoAdInteractionListener {
    private final a b;
    private DCFullScreenAOLListener c;

    public DCFullScreenAOL(Activity activity) {
        super(activity);
        this.b = new a(activity, 7);
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

    public void load(DCloudAdSlot dCloudAdSlot, final DCFullScreenAOLLoadListener dCFullScreenAOLLoadListener) {
        if (getContext() != null && dCloudAdSlot != null) {
            a aVar = this.b;
            if (aVar != null) {
                aVar.a(dCloudAdSlot, new c() {
                    public void onError(int i, String str, JSONArray jSONArray) {
                        DCFullScreenAOLLoadListener dCFullScreenAOLLoadListener = dCFullScreenAOLLoadListener;
                        if (dCFullScreenAOLLoadListener != null) {
                            dCFullScreenAOLLoadListener.onError(i, str, jSONArray);
                        }
                    }

                    public void onLoaded() {
                        DCFullScreenAOLLoadListener dCFullScreenAOLLoadListener = dCFullScreenAOLLoadListener;
                        if (dCFullScreenAOLLoadListener != null) {
                            dCFullScreenAOLLoadListener.onFullScreenAdLoad();
                        }
                    }
                });
            } else if (dCFullScreenAOLLoadListener != null) {
                dCFullScreenAOLLoadListener.onError(-5015, AdErrorUtil.getErrorMsg(-5015), (JSONArray) null);
            }
        } else if (dCFullScreenAOLLoadListener != null) {
            dCFullScreenAOLLoadListener.onError(-5014, AdErrorUtil.getErrorMsg(-5014), (JSONArray) null);
        }
    }

    public void onClick() {
        DCFullScreenAOLListener dCFullScreenAOLListener = this.c;
        if (dCFullScreenAOLListener != null) {
            dCFullScreenAOLListener.onClick();
        }
    }

    public void onClose() {
        DCFullScreenAOLListener dCFullScreenAOLListener = this.c;
        if (dCFullScreenAOLListener != null) {
            dCFullScreenAOLListener.onClose();
        }
    }

    public void onShow() {
        DCFullScreenAOLListener dCFullScreenAOLListener = this.c;
        if (dCFullScreenAOLListener != null) {
            dCFullScreenAOLListener.onShow();
        }
    }

    public void onShowError(int i, String str) {
        DCFullScreenAOLListener dCFullScreenAOLListener = this.c;
        if (dCFullScreenAOLListener != null) {
            dCFullScreenAOLListener.onShowError(i, str);
        }
    }

    public void onSkip() {
        DCFullScreenAOLListener dCFullScreenAOLListener = this.c;
        if (dCFullScreenAOLListener != null) {
            dCFullScreenAOLListener.onSkip();
        }
    }

    public void onVideoPlayEnd() {
        DCFullScreenAOLListener dCFullScreenAOLListener = this.c;
        if (dCFullScreenAOLListener != null) {
            dCFullScreenAOLListener.onVideoPlayEnd();
        }
    }

    public void setFullScreenAdListener(DCFullScreenAOLListener dCFullScreenAOLListener) {
        this.c = dCFullScreenAOLListener;
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
