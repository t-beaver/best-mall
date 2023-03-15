package io.dcloud.h.c.b.a;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.ViewGroup;
import com.nostra13.dcloudimageloader.core.download.BaseImageDownloader;
import io.dcloud.WebAppActivity;
import io.dcloud.h.a.c.a;
import io.dcloud.sdk.base.dcloud.ADHandler;
import io.dcloud.sdk.base.dcloud.f;
import io.dcloud.sdk.base.dcloud.j;
import io.dcloud.sdk.core.api.AOLLoader;
import io.dcloud.sdk.core.entry.DCloudAdSlot;
import io.dcloud.sdk.core.module.DCBaseAOLLoader;
import io.dcloud.sdk.core.util.AdErrorUtil;
import io.dcloud.sdk.core.util.MainHandlerUtil;
import io.dcloud.sdk.poly.base.utils.e;
import org.json.JSONArray;
import org.json.JSONObject;

public class b extends DCBaseAOLLoader implements a.c {
    private String A = "";
    /* access modifiers changed from: private */
    public ADHandler.g B;
    private Handler C = new a(Looper.getMainLooper());
    private int D = BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT;
    private io.dcloud.h.a.d.a y;
    private io.dcloud.h.c.c.a.c.a z;

    class a extends Handler {
        a(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            b.this.loadFail(-9999, "");
        }
    }

    /* renamed from: io.dcloud.h.c.b.a.b$b  reason: collision with other inner class name */
    class C0054b implements f {
        C0054b() {
        }

        public void onClicked() {
            if (b.this.getVideoAdCallback() != null) {
                b.this.getVideoAdCallback().onClick();
            }
        }

        public void onFinishShow() {
            if (b.this.getVideoAdCallback() != null) {
                b.this.getVideoAdCallback().onClose();
            }
        }

        public void onShow() {
            if (b.this.getVideoAdCallback() != null) {
                b.this.getVideoAdCallback().onShow();
            }
        }
    }

    class c implements ADHandler.i {
        c() {
        }

        public void a() {
            if (b.this.getAdStatus() == -1) {
                ADHandler.g b = ADHandler.b(b.this.getActivity(), io.dcloud.h.c.a.d().b().getAppId());
                if (b.a()) {
                    ADHandler.g unused = b.this.B = b;
                    b.this.B.a(b.this.h());
                    b.this.loadSuccess();
                    return;
                }
                b.this.loadFail(-9999, "");
            }
        }

        public void b() {
            if (b.this.getAdStatus() == -1) {
                ADHandler.g b = ADHandler.b(b.this.getActivity(), io.dcloud.h.c.a.d().b().getAppId());
                if (b.a()) {
                    ADHandler.g unused = b.this.B = b;
                    b.this.B.a(b.this.h());
                    b.this.loadSuccess();
                    return;
                }
                b.this.loadFail(-9999, "");
            }
        }
    }

    public b(DCloudAdSlot dCloudAdSlot, Activity activity) {
        super(dCloudAdSlot, activity);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void b(ViewGroup viewGroup) {
        this.y.a(viewGroup);
    }

    /* access modifiers changed from: private */
    /* renamed from: c */
    public void a(ViewGroup viewGroup) {
        new j(getActivity(), this.B, viewGroup, new C0054b()).b();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void q() {
        io.dcloud.h.a.d.a a2 = io.dcloud.h.a.b.a(this, getActivity(), this.z.d(), getSlotId(), this.z.c(), this.z.b());
        this.y = a2;
        a2.c();
    }

    public void d() {
        if (getVideoAdCallback() != null) {
            getVideoAdCallback().onShow();
        }
    }

    public void destroy() {
    }

    public String e() {
        io.dcloud.h.c.c.a.c.a aVar = this.z;
        return aVar == null ? "" : aVar.a();
    }

    public String getTid() {
        io.dcloud.h.c.c.a.c.a aVar = this.z;
        return aVar == null ? "" : aVar.e();
    }

    public String getType() {
        io.dcloud.h.c.c.a.c.a aVar = this.z;
        return aVar == null ? this.A : aVar.f();
    }

    public void init(String str, String str2) {
    }

    public boolean isValid() {
        return true;
    }

    public void load() {
        if (TextUtils.isEmpty(this.A)) {
            loadFail(-9999, "");
        } else if (this.A.equals("dcloud")) {
            e.a("uniAd", "load base");
            this.C.sendEmptyMessageDelayed(this.D, WebAppActivity.SPLASH_SECOND);
        } else if (this.z == null) {
            loadFail(-9999, "");
        } else {
            MainHandlerUtil.getMainHandler().post(new Runnable() {
                public final void run() {
                    b.this.q();
                }
            });
        }
    }

    public void onError(int i, String str) {
        loadFail(i, str);
    }

    public void onSplashAdLoad() {
        loadSuccess();
    }

    public boolean runOnMain() {
        return false;
    }

    public void showIn(ViewGroup viewGroup) {
        if (!(this.y == null && this.B == null) && !TextUtils.isEmpty(this.A)) {
            if (viewGroup == null) {
                if (getVideoAdCallback() != null) {
                    getVideoAdCallback().onShowError(-5014, AdErrorUtil.getErrorMsg(-5014));
                }
            } else if (this.A.equals("dcloud")) {
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    a(viewGroup);
                } else {
                    MainHandlerUtil.getMainHandler().post(new Runnable(viewGroup) {
                        public final /* synthetic */ ViewGroup f$1;

                        {
                            this.f$1 = r2;
                        }

                        public final void run() {
                            b.this.a(this.f$1);
                        }
                    });
                }
            } else if (Looper.myLooper() == Looper.getMainLooper()) {
                this.y.a(viewGroup);
            } else {
                MainHandlerUtil.getMainHandler().post(new Runnable(viewGroup) {
                    public final /* synthetic */ ViewGroup f$1;

                    {
                        this.f$1 = r2;
                    }

                    public final void run() {
                        b.this.b(this.f$1);
                    }
                });
            }
        } else if (getVideoAdCallback() != null) {
            getVideoAdCallback().onShowError(-5008, AdErrorUtil.getErrorMsg(-5008));
        }
    }

    public void b() {
        if (getVideoAdCallback() != null) {
            getVideoAdCallback().onClose();
        }
    }

    public void a(io.dcloud.h.c.c.a.c.a aVar, String str) {
        this.z = aVar;
        this.A = str;
    }

    public void a() {
        if (getVideoAdCallback() != null) {
            getVideoAdCallback().onClick();
        }
    }

    public void a(int i, String str) {
        if (getVideoAdCallback() != null) {
            AOLLoader.VideoAdInteractionListener videoAdCallback = getVideoAdCallback();
            videoAdCallback.onShowError(-5100, "code" + i + ";message:" + str);
        }
    }

    public void a(JSONArray jSONArray, boolean z2) {
        e.a("uniAd-finish", String.valueOf(jSONArray) + "::::::" + z2);
        if (this.C.hasMessages(this.D)) {
            this.C.removeMessages(this.D);
            if (!z2) {
                loadFail(-9999, "");
            } else if (jSONArray == null || jSONArray.length() == 0) {
                ADHandler.g b = ADHandler.b(getActivity(), io.dcloud.h.c.a.d().b().getAppId());
                if (b.a()) {
                    this.B = b;
                    b.a(h());
                    loadSuccess();
                    return;
                }
                loadFail(-9999, "");
            } else {
                c cVar = new c();
                boolean z3 = true;
                for (int i = 0; i < jSONArray.length(); i++) {
                    JSONObject optJSONObject = jSONArray.optJSONObject(i);
                    if (optJSONObject != null) {
                        ADHandler.a(getActivity(), optJSONObject, System.currentTimeMillis(), cVar);
                        z3 = false;
                    }
                }
                if (z3) {
                    loadFail(-9999, "");
                }
            }
        }
    }

    public void c() {
        if (getVideoAdCallback() != null) {
            getVideoAdCallback().onSkip();
        }
    }
}
