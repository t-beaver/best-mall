package io.dcloud.sdk.core.v2.splash;

import android.app.Activity;
import android.view.ViewGroup;
import io.dcloud.h.c.c.e.a.c;
import io.dcloud.h.c.c.e.c.e.a;
import io.dcloud.sdk.core.api.AOLLoader;
import io.dcloud.sdk.core.entry.SplashConfig;
import io.dcloud.sdk.core.util.AdErrorUtil;
import io.dcloud.sdk.core.v2.base.DCBaseAOL;
import org.json.JSONArray;

public class DCSplashAOL extends DCBaseAOL implements AOLLoader.VideoAdInteractionListener {
    private final a b;
    private DCSplashAOLListener c;

    public DCSplashAOL(Activity activity) {
        super(activity);
        this.b = new a(activity, 1);
    }

    public boolean isValid() {
        a aVar = this.b;
        return aVar != null && aVar.l();
    }

    public void load(SplashConfig splashConfig, final DCSplashAOLLoadListener dCSplashAOLLoadListener) {
        if (getContext() != null && splashConfig != null) {
            a aVar = this.b;
            if (aVar != null) {
                aVar.a(splashConfig, (c) new c() {
                    public void onError(int i, String str, JSONArray jSONArray) {
                        dCSplashAOLLoadListener.onError(i, str, jSONArray);
                    }

                    public void onLoaded() {
                        dCSplashAOLLoadListener.onSplashAdLoad();
                    }
                });
            } else if (dCSplashAOLLoadListener != null) {
                dCSplashAOLLoadListener.onError(-5015, AdErrorUtil.getErrorMsg(-5015), (JSONArray) null);
            }
        } else if (dCSplashAOLLoadListener != null) {
            dCSplashAOLLoadListener.onError(-5014, AdErrorUtil.getErrorMsg(-5014), (JSONArray) null);
        }
    }

    public void onClick() {
        DCSplashAOLListener dCSplashAOLListener = this.c;
        if (dCSplashAOLListener != null) {
            dCSplashAOLListener.onClick();
        }
    }

    public void onClose() {
        DCSplashAOLListener dCSplashAOLListener = this.c;
        if (dCSplashAOLListener != null) {
            dCSplashAOLListener.onClose();
        }
    }

    public void onShow() {
        DCSplashAOLListener dCSplashAOLListener = this.c;
        if (dCSplashAOLListener != null) {
            dCSplashAOLListener.onShow();
        }
    }

    public void onShowError(int i, String str) {
        DCSplashAOLListener dCSplashAOLListener = this.c;
        if (dCSplashAOLListener != null) {
            dCSplashAOLListener.onShowError(i, str);
        }
    }

    public void onSkip() {
        DCSplashAOLListener dCSplashAOLListener = this.c;
        if (dCSplashAOLListener != null) {
            dCSplashAOLListener.onSkip();
        }
    }

    public void onVideoPlayEnd() {
        DCSplashAOLListener dCSplashAOLListener = this.c;
        if (dCSplashAOLListener != null) {
            dCSplashAOLListener.onVideoPlayEnd();
        }
    }

    public void setSplashAdListener(DCSplashAOLListener dCSplashAOLListener) {
        this.c = dCSplashAOLListener;
        a aVar = this.b;
        if (aVar != null) {
            aVar.a((AOLLoader.VideoAdInteractionListener) this);
        }
    }

    public void show(Activity activity) {
    }

    public void show(ViewGroup viewGroup) {
        showIn(viewGroup);
    }

    @Deprecated
    public void showIn(ViewGroup viewGroup) {
        a aVar = this.b;
        if (aVar != null) {
            aVar.a(viewGroup);
        }
    }
}
