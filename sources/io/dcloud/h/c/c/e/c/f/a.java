package io.dcloud.h.c.c.e.c.f;

import android.app.Activity;
import io.dcloud.h.c.c.e.a.c;
import io.dcloud.sdk.core.api.AOLLoader;
import io.dcloud.sdk.core.entry.DCloudAdSlot;
import io.dcloud.sdk.core.module.DCBaseAOL;
import io.dcloud.sdk.core.module.DCBaseAOLLoader;
import io.dcloud.sdk.core.util.AdErrorUtil;
import io.dcloud.sdk.core.util.MainHandlerUtil;
import io.dcloud.sdk.poly.base.utils.d;
import io.dcloud.sdk.poly.base.utils.e;
import java.util.List;
import org.json.JSONArray;

public class a extends io.dcloud.h.c.c.d.a implements AOLLoader.VideoAdInteractionListener {
    protected AOLLoader.VideoAdInteractionListener q;
    protected c r;
    protected DCBaseAOL s;
    private boolean t = false;
    private boolean u = false;
    private boolean v = false;

    public a(Activity activity, int i) {
        super(activity);
        this.d = i;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void c(List list) {
        if (list == null || list.size() <= 0) {
            c cVar = this.r;
            if (cVar != null) {
                cVar.onError(-5005, AdErrorUtil.getErrorMsg(-5005), (JSONArray) null);
                return;
            }
            return;
        }
        DCBaseAOL dCBaseAOL = (DCBaseAOL) list.get(0);
        this.s = dCBaseAOL;
        dCBaseAOL.a((AOLLoader.VideoAdInteractionListener) this);
        c cVar2 = this.r;
        if (cVar2 != null) {
            cVar2.onLoaded();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void d(int i, String str) {
        AOLLoader.VideoAdInteractionListener videoAdInteractionListener = this.q;
        if (videoAdInteractionListener != null) {
            videoAdInteractionListener.onShowError(i, str);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void m() {
        AOLLoader.VideoAdInteractionListener videoAdInteractionListener = this.q;
        if (videoAdInteractionListener != null) {
            videoAdInteractionListener.onClick();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void n() {
        AOLLoader.VideoAdInteractionListener videoAdInteractionListener = this.q;
        if (videoAdInteractionListener != null) {
            videoAdInteractionListener.onClose();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void o() {
        AOLLoader.VideoAdInteractionListener videoAdInteractionListener = this.q;
        if (videoAdInteractionListener != null) {
            videoAdInteractionListener.onShow();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void p() {
        AOLLoader.VideoAdInteractionListener videoAdInteractionListener = this.q;
        if (videoAdInteractionListener != null) {
            videoAdInteractionListener.onSkip();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void q() {
        AOLLoader.VideoAdInteractionListener videoAdInteractionListener = this.q;
        if (videoAdInteractionListener != null) {
            videoAdInteractionListener.onVideoPlayEnd();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void r() {
        AOLLoader.VideoAdInteractionListener videoAdInteractionListener = this.q;
        if (videoAdInteractionListener != null) {
            videoAdInteractionListener.onShowError(-5006, AdErrorUtil.getErrorMsg(-5006));
        }
    }

    /* access modifiers changed from: protected */
    public void a(int i, String str, JSONArray jSONArray) {
        this.v = false;
        e.b("uniAd-loadError", "code:" + i + ";message:" + str + ";detail:" + String.valueOf(jSONArray));
        MainHandlerUtil.getMainHandler().post(new Runnable(i, str, jSONArray) {
            public final /* synthetic */ int f$1;
            public final /* synthetic */ String f$2;
            public final /* synthetic */ JSONArray f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            public final void run() {
                a.this.c(this.f$1, this.f$2, this.f$3);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void b(List<DCBaseAOL> list) {
        this.v = false;
        MainHandlerUtil.getMainHandler().post(new Runnable(list) {
            public final /* synthetic */ List f$1;

            {
                this.f$1 = r2;
            }

            public final void run() {
                a.this.c(this.f$1);
            }
        });
    }

    public String getType() {
        DCBaseAOL dCBaseAOL = this.s;
        return dCBaseAOL != null ? dCBaseAOL.getType() : "";
    }

    public void k() {
        DCBaseAOL dCBaseAOL = this.s;
        if (dCBaseAOL != null) {
            dCBaseAOL.destroy();
        }
    }

    public boolean l() {
        DCBaseAOL dCBaseAOL = this.s;
        return dCBaseAOL != null && dCBaseAOL.isValid();
    }

    public void onClick() {
        a(a(), this.s);
        MainHandlerUtil.getMainHandler().post(new Runnable() {
            public final void run() {
                a.this.m();
            }
        });
    }

    public void onClose() {
        this.u = false;
        MainHandlerUtil.getMainHandler().post(new Runnable() {
            public final void run() {
                a.this.n();
            }
        });
    }

    public void onShow() {
        this.t = true;
        this.u = true;
        b(a(), this.s);
        MainHandlerUtil.getMainHandler().post(new Runnable() {
            public final void run() {
                a.this.o();
            }
        });
    }

    public void onShowError(int i, String str) {
        this.u = false;
        MainHandlerUtil.getMainHandler().post(new Runnable(i, str) {
            public final /* synthetic */ int f$1;
            public final /* synthetic */ String f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            public final void run() {
                a.this.d(this.f$1, this.f$2);
            }
        });
    }

    public void onSkip() {
        this.u = false;
        MainHandlerUtil.getMainHandler().post(new Runnable() {
            public final void run() {
                a.this.p();
            }
        });
    }

    public void onVideoPlayEnd() {
        this.u = false;
        MainHandlerUtil.getMainHandler().post(new Runnable() {
            public final void run() {
                a.this.q();
            }
        });
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void b(c cVar) {
        if (cVar != null) {
            cVar.onError(-5021, AdErrorUtil.getErrorMsg(-5021), (JSONArray) null);
        }
    }

    public void a(Activity activity) {
        if (this.t) {
            MainHandlerUtil.getMainHandler().post(new Runnable() {
                public final void run() {
                    a.this.r();
                }
            });
            return;
        }
        DCBaseAOL dCBaseAOL = this.s;
        if (dCBaseAOL instanceof DCBaseAOLLoader) {
            ((DCBaseAOLLoader) dCBaseAOL).show(activity);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void c(int i, String str, JSONArray jSONArray) {
        c cVar = this.r;
        if (cVar != null) {
            cVar.onError(i, str, jSONArray);
        }
    }

    public void a(AOLLoader.VideoAdInteractionListener videoAdInteractionListener) {
        this.q = videoAdInteractionListener;
    }

    public void a(DCloudAdSlot dCloudAdSlot, c cVar) {
        if (this.u) {
            MainHandlerUtil.getMainHandler().post(new Runnable() {
                public final void run() {
                    a.b(c.this);
                }
            });
        } else if (this.v) {
            MainHandlerUtil.getMainHandler().post(new Runnable() {
                public final void run() {
                    a.a(c.this);
                }
            });
        } else {
            this.t = false;
            this.s = null;
            a(dCloudAdSlot);
            this.r = cVar;
            this.v = true;
            d.a().post(this);
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void a(c cVar) {
        if (cVar != null) {
            cVar.onError(-5017, AdErrorUtil.getErrorMsg(-5017), (JSONArray) null);
        }
    }

    /* access modifiers changed from: protected */
    public void a(boolean z) {
        this.v = z;
    }
}
