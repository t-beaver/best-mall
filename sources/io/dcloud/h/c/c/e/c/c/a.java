package io.dcloud.h.c.c.e.c.c;

import android.app.Activity;
import android.view.View;
import io.dcloud.h.c.c.d.b;
import io.dcloud.sdk.core.api.AOLLoader;
import io.dcloud.sdk.core.module.DCBaseAOL;
import io.dcloud.sdk.core.util.MainHandlerUtil;

public class a extends b implements AOLLoader.FeedAdInteractionListener {
    private final DCBaseAOL b;
    private final Activity c;
    protected AOLLoader.FeedAdInteractionListener d;

    public a(DCBaseAOL dCBaseAOL, Activity activity) {
        this.b = dCBaseAOL;
        this.c = activity;
        dCBaseAOL.a((AOLLoader.FeedAdInteractionListener) this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void a(String str) {
        AOLLoader.FeedAdInteractionListener feedAdInteractionListener = this.d;
        if (feedAdInteractionListener != null) {
            feedAdInteractionListener.onClosed(str);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void f() {
        AOLLoader.FeedAdInteractionListener feedAdInteractionListener = this.d;
        if (feedAdInteractionListener != null) {
            feedAdInteractionListener.onClicked();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void g() {
        AOLLoader.FeedAdInteractionListener feedAdInteractionListener = this.d;
        if (feedAdInteractionListener != null) {
            feedAdInteractionListener.onRenderFail();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void h() {
        AOLLoader.FeedAdInteractionListener feedAdInteractionListener = this.d;
        if (feedAdInteractionListener != null) {
            feedAdInteractionListener.onRenderSuccess();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void i() {
        AOLLoader.FeedAdInteractionListener feedAdInteractionListener = this.d;
        if (feedAdInteractionListener != null) {
            feedAdInteractionListener.onShow();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void j() {
        AOLLoader.FeedAdInteractionListener feedAdInteractionListener = this.d;
        if (feedAdInteractionListener != null) {
            feedAdInteractionListener.onShowError();
        }
    }

    /* access modifiers changed from: protected */
    public boolean c() {
        return false;
    }

    public void d() {
        DCBaseAOL dCBaseAOL = this.b;
        if (dCBaseAOL != null) {
            dCBaseAOL.destroy();
        }
    }

    public boolean e() {
        DCBaseAOL dCBaseAOL = this.b;
        return dCBaseAOL != null && dCBaseAOL.isValid();
    }

    public String getType() {
        DCBaseAOL dCBaseAOL = this.b;
        return dCBaseAOL != null ? dCBaseAOL.getType() : "";
    }

    public void k() {
        DCBaseAOL dCBaseAOL = this.b;
        if (dCBaseAOL != null) {
            dCBaseAOL.render();
        }
    }

    public void onClicked() {
        a(this.c, this.b);
        MainHandlerUtil.getMainHandler().post(new Runnable() {
            public final void run() {
                a.this.f();
            }
        });
    }

    public void onClosed(String str) {
        MainHandlerUtil.getMainHandler().post(new Runnable(str) {
            public final /* synthetic */ String f$1;

            {
                this.f$1 = r2;
            }

            public final void run() {
                a.this.a(this.f$1);
            }
        });
    }

    public void onRenderFail() {
        MainHandlerUtil.getMainHandler().post(new Runnable() {
            public final void run() {
                a.this.g();
            }
        });
    }

    public void onRenderSuccess() {
        MainHandlerUtil.getMainHandler().post(new Runnable() {
            public final void run() {
                a.this.h();
            }
        });
    }

    public void onShow() {
        b(this.c, this.b);
        MainHandlerUtil.getMainHandler().post(new Runnable() {
            public final void run() {
                a.this.i();
            }
        });
    }

    public void onShowError() {
        MainHandlerUtil.getMainHandler().post(new Runnable() {
            public final void run() {
                a.this.j();
            }
        });
    }

    public View a(Activity activity) {
        DCBaseAOL dCBaseAOL = this.b;
        if (dCBaseAOL != null) {
            return dCBaseAOL.getExpressAdView(activity);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public Activity a() {
        return this.c;
    }

    public void a(AOLLoader.FeedAdInteractionListener feedAdInteractionListener) {
        this.d = feedAdInteractionListener;
    }
}
