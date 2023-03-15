package io.dcloud.h.c.c.e.c.b;

import android.app.Activity;
import io.dcloud.sdk.core.api.AOLLoader;
import io.dcloud.sdk.core.module.DCBaseAOL;
import io.dcloud.sdk.core.util.MainHandlerUtil;

public class a extends io.dcloud.h.c.c.e.c.c.a implements AOLLoader.DrawAdInteractionListener {
    public a(DCBaseAOL dCBaseAOL, Activity activity) {
        super(dCBaseAOL, activity);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void l() {
        AOLLoader.FeedAdInteractionListener feedAdInteractionListener = this.d;
        if (feedAdInteractionListener instanceof AOLLoader.DrawAdInteractionListener) {
            ((AOLLoader.DrawAdInteractionListener) feedAdInteractionListener).onEnd();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void m() {
        AOLLoader.FeedAdInteractionListener feedAdInteractionListener = this.d;
        if (feedAdInteractionListener instanceof AOLLoader.DrawAdInteractionListener) {
            ((AOLLoader.DrawAdInteractionListener) feedAdInteractionListener).onPause();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void n() {
        AOLLoader.FeedAdInteractionListener feedAdInteractionListener = this.d;
        if (feedAdInteractionListener instanceof AOLLoader.DrawAdInteractionListener) {
            ((AOLLoader.DrawAdInteractionListener) feedAdInteractionListener).onResume();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void o() {
        AOLLoader.FeedAdInteractionListener feedAdInteractionListener = this.d;
        if (feedAdInteractionListener instanceof AOLLoader.DrawAdInteractionListener) {
            ((AOLLoader.DrawAdInteractionListener) feedAdInteractionListener).onStart();
        }
    }

    public void onEnd() {
        MainHandlerUtil.getMainHandler().post(new Runnable() {
            public final void run() {
                a.this.l();
            }
        });
    }

    public void onPause() {
        MainHandlerUtil.getMainHandler().post(new Runnable() {
            public final void run() {
                a.this.m();
            }
        });
    }

    public void onResume() {
        MainHandlerUtil.getMainHandler().post(new Runnable() {
            public final void run() {
                a.this.n();
            }
        });
    }

    public void onStart() {
        MainHandlerUtil.getMainHandler().post(new Runnable() {
            public final void run() {
                a.this.o();
            }
        });
    }
}
