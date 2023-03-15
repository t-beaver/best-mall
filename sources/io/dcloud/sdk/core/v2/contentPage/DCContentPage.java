package io.dcloud.sdk.core.v2.contentPage;

import android.app.Activity;
import androidx.fragment.app.Fragment;
import io.dcloud.h.c.c.e.a.c;
import io.dcloud.h.c.c.e.c.a.a;
import io.dcloud.sdk.core.api.AOLLoader;
import io.dcloud.sdk.core.api.ContentPage;
import io.dcloud.sdk.core.entry.DCloudAdSlot;
import io.dcloud.sdk.core.util.AdErrorUtil;
import io.dcloud.sdk.core.v2.base.DCBaseAOL;
import org.json.JSONArray;

public class DCContentPage extends DCBaseAOL implements AOLLoader.ContentPageVideoListener {
    private a b;
    private DCContentPageVideoListener c;

    public DCContentPage(Activity activity) {
        super(activity);
        this.b = new a(activity, 14);
    }

    public Fragment getContentPage() {
        a aVar = this.b;
        if (aVar == null) {
            return null;
        }
        return aVar.s();
    }

    public void load(DCloudAdSlot dCloudAdSlot, final DCContentPageLoadListener dCContentPageLoadListener) {
        if (getContext() != null && dCloudAdSlot != null) {
            a aVar = this.b;
            if (aVar != null) {
                aVar.a(dCloudAdSlot, new c() {
                    public void onError(int i, String str, JSONArray jSONArray) {
                        dCContentPageLoadListener.onError(i, str, jSONArray);
                    }

                    public void onLoaded() {
                        dCContentPageLoadListener.onContentPageLoad();
                    }
                });
            } else if (dCContentPageLoadListener != null) {
                dCContentPageLoadListener.onError(-5015, AdErrorUtil.getErrorMsg(-5015), (JSONArray) null);
            }
        } else if (dCContentPageLoadListener != null) {
            dCContentPageLoadListener.onError(-5014, AdErrorUtil.getErrorMsg(-5014), (JSONArray) null);
        }
    }

    public void onClick() {
    }

    public void onClose() {
    }

    public void onComplete(ContentPage.ContentPageItem contentPageItem) {
        DCContentPageVideoListener dCContentPageVideoListener = this.c;
        if (dCContentPageVideoListener != null) {
            dCContentPageVideoListener.onComplete(contentPageItem);
        }
    }

    public void onError(ContentPage.ContentPageItem contentPageItem) {
        DCContentPageVideoListener dCContentPageVideoListener = this.c;
        if (dCContentPageVideoListener != null) {
            dCContentPageVideoListener.onError(contentPageItem);
        }
    }

    public void onPause(ContentPage.ContentPageItem contentPageItem) {
        DCContentPageVideoListener dCContentPageVideoListener = this.c;
        if (dCContentPageVideoListener != null) {
            dCContentPageVideoListener.onPause(contentPageItem);
        }
    }

    public void onResume(ContentPage.ContentPageItem contentPageItem) {
        DCContentPageVideoListener dCContentPageVideoListener = this.c;
        if (dCContentPageVideoListener != null) {
            dCContentPageVideoListener.onResume(contentPageItem);
        }
    }

    public void onShow() {
    }

    public void onShowError(int i, String str) {
    }

    public void onSkip() {
    }

    public void onStart(ContentPage.ContentPageItem contentPageItem) {
        DCContentPageVideoListener dCContentPageVideoListener = this.c;
        if (dCContentPageVideoListener != null) {
            dCContentPageVideoListener.onStart(contentPageItem);
        }
    }

    public void onVideoPlayEnd() {
    }

    public void setContentPageVideoListener(DCContentPageVideoListener dCContentPageVideoListener) {
        this.c = dCContentPageVideoListener;
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
