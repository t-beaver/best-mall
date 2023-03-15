package io.dcloud.h.c.c.e.c.a;

import android.app.Activity;
import androidx.fragment.app.Fragment;
import io.dcloud.h.c.c.e.a.c;
import io.dcloud.sdk.core.api.AOLLoader;
import io.dcloud.sdk.core.api.ContentPage;
import io.dcloud.sdk.core.entry.DCloudAdSlot;
import io.dcloud.sdk.core.module.DCBaseAOL;
import org.json.JSONArray;

public class a extends io.dcloud.h.c.c.e.c.f.a implements AOLLoader.ContentPageVideoListener {
    public a(Activity activity, int i) {
        super(activity, i);
    }

    public void a(DCloudAdSlot dCloudAdSlot, c cVar) {
        try {
            Class.forName("com.kwad.sdk.api.KsContentPage");
            super.a(dCloudAdSlot, cVar);
        } catch (ClassNotFoundException unused) {
            if (cVar != null) {
                cVar.onError(-5050, "当前环境没有快手内容联盟SDK", (JSONArray) null);
            }
        }
    }

    public void onComplete(ContentPage.ContentPageItem contentPageItem) {
        AOLLoader.VideoAdInteractionListener videoAdInteractionListener = this.q;
        if (videoAdInteractionListener instanceof AOLLoader.ContentPageVideoListener) {
            ((AOLLoader.ContentPageVideoListener) videoAdInteractionListener).onComplete(contentPageItem);
        }
    }

    public void onError(ContentPage.ContentPageItem contentPageItem) {
        AOLLoader.VideoAdInteractionListener videoAdInteractionListener = this.q;
        if (videoAdInteractionListener instanceof AOLLoader.ContentPageVideoListener) {
            ((AOLLoader.ContentPageVideoListener) videoAdInteractionListener).onError(contentPageItem);
        }
    }

    public void onPause(ContentPage.ContentPageItem contentPageItem) {
        AOLLoader.VideoAdInteractionListener videoAdInteractionListener = this.q;
        if (videoAdInteractionListener instanceof AOLLoader.ContentPageVideoListener) {
            ((AOLLoader.ContentPageVideoListener) videoAdInteractionListener).onPause(contentPageItem);
        }
    }

    public void onResume(ContentPage.ContentPageItem contentPageItem) {
        AOLLoader.VideoAdInteractionListener videoAdInteractionListener = this.q;
        if (videoAdInteractionListener instanceof AOLLoader.ContentPageVideoListener) {
            ((AOLLoader.ContentPageVideoListener) videoAdInteractionListener).onResume(contentPageItem);
        }
    }

    public void onStart(ContentPage.ContentPageItem contentPageItem) {
        AOLLoader.VideoAdInteractionListener videoAdInteractionListener = this.q;
        if (videoAdInteractionListener instanceof AOLLoader.ContentPageVideoListener) {
            ((AOLLoader.ContentPageVideoListener) videoAdInteractionListener).onStart(contentPageItem);
        }
    }

    public Fragment s() {
        DCBaseAOL dCBaseAOL = this.s;
        if (dCBaseAOL == null) {
            return null;
        }
        try {
            return (Fragment) dCBaseAOL.getClass().getDeclaredMethod("getContentPage", new Class[0]).invoke(this.s, new Object[0]);
        } catch (Exception unused) {
            return null;
        }
    }
}
