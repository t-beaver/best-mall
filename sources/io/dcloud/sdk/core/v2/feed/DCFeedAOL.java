package io.dcloud.sdk.core.v2.feed;

import android.app.Activity;
import android.view.View;
import io.dcloud.h.c.c.e.c.c.a;
import io.dcloud.sdk.core.api.AOLLoader;
import io.dcloud.sdk.core.util.MainHandlerUtil;

public class DCFeedAOL implements AOLLoader.FeedAdInteractionListener {
    private a a;
    protected DCFeedAOLListener b;

    public DCFeedAOL(a aVar) {
        this.a = aVar;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void a() {
        this.a.k();
    }

    public void destroy() {
        a aVar = this.a;
        if (aVar != null) {
            aVar.d();
        }
    }

    public View getFeedAdView(Activity activity) {
        a aVar = this.a;
        if (aVar != null) {
            return aVar.a(activity);
        }
        return null;
    }

    public String getType() {
        a aVar = this.a;
        return aVar != null ? aVar.getType() : "";
    }

    public boolean isValid() {
        a aVar = this.a;
        return aVar != null && aVar.e();
    }

    public void onClicked() {
        DCFeedAOLListener dCFeedAOLListener = this.b;
        if (dCFeedAOLListener != null) {
            dCFeedAOLListener.onClick();
        }
    }

    public void onClosed(String str) {
        DCFeedAOLListener dCFeedAOLListener = this.b;
        if (dCFeedAOLListener != null) {
            dCFeedAOLListener.onClosed(str);
        }
    }

    public void onRenderFail() {
        DCFeedAOLListener dCFeedAOLListener = this.b;
        if (dCFeedAOLListener != null) {
            dCFeedAOLListener.onRenderFail();
        }
    }

    public void onRenderSuccess() {
        DCFeedAOLListener dCFeedAOLListener = this.b;
        if (dCFeedAOLListener != null) {
            dCFeedAOLListener.onRenderSuccess();
        }
    }

    public void onShow() {
        DCFeedAOLListener dCFeedAOLListener = this.b;
        if (dCFeedAOLListener != null) {
            dCFeedAOLListener.onShow();
        }
    }

    public void onShowError() {
        DCFeedAOLListener dCFeedAOLListener = this.b;
        if (dCFeedAOLListener != null) {
            dCFeedAOLListener.onShowError();
        }
    }

    public void render() {
        a aVar = this.a;
        if (aVar != null) {
            aVar.a((AOLLoader.FeedAdInteractionListener) this);
            MainHandlerUtil.getMainHandler().post(new Runnable() {
                public final void run() {
                    DCFeedAOL.this.a();
                }
            });
        }
    }

    public void setFeedAdListener(DCFeedAOLListener dCFeedAOLListener) {
        this.b = dCFeedAOLListener;
    }
}
