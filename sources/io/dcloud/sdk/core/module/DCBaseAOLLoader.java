package io.dcloud.sdk.core.module;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.ViewGroup;
import com.nostra13.dcloudimageloader.core.download.BaseImageDownloader;
import io.dcloud.h.c.c.d.a;
import io.dcloud.sdk.core.entry.DCloudAdSlot;
import io.dcloud.sdk.core.util.Const;
import io.dcloud.sdk.core.util.MainHandlerUtil;
import io.dcloud.sdk.poly.base.config.ThirdSlotConfig;
import io.dcloud.sdk.poly.base.utils.d;
import io.dcloud.sdk.poly.base.utils.e;
import java.util.List;
import java.util.Map;

public abstract class DCBaseAOLLoader extends a {
    private boolean t = false;
    private final Handler u;
    private final int v = 1;
    private int w = BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT;
    /* access modifiers changed from: private */
    public a.C0060a x;

    private static class a implements Runnable {
        private final DCBaseAOLLoader a;
        private final int b;
        private final int c;
        private final String d;
        private final List<? extends DCBaseAOL> e;

        public a(DCBaseAOLLoader dCBaseAOLLoader, List<? extends DCBaseAOL> list, int i, int i2, String str) {
            this.a = dCBaseAOLLoader;
            this.b = i;
            this.c = i2;
            this.d = str;
            this.e = list;
            if (list != null) {
                e.d("sub slot ads:" + list.size());
            }
        }

        public void run() {
            io.dcloud.sdk.poly.base.config.b bVar = new io.dcloud.sdk.poly.base.config.b();
            bVar.a(this.a.e()).c(this.a.getSlotId()).b(this.a.getType()).d(this.a.getTid()).a(this.a.l());
            int i = this.b;
            if (i == 1) {
                bVar.a(i);
                this.a.x.a(this.a, this.e, bVar);
            } else if (i == 0) {
                bVar.a(i);
                bVar.a(this.c, this.d);
                this.a.x.a(this.a, bVar);
            }
        }
    }

    private class b extends Handler {
        public b(Looper looper) {
            super(looper);
        }

        public void dispatchMessage(Message message) {
            DCBaseAOLLoader.this.loadFail(-5000, "timeout");
        }

        public void handleMessage(Message message) {
            super.handleMessage(message);
        }
    }

    public DCBaseAOLLoader(DCloudAdSlot dCloudAdSlot, Activity activity) {
        super(dCloudAdSlot, activity);
        a(-1);
        this.u = new b(d.a().getLooper());
    }

    private boolean o() {
        if (TextUtils.isEmpty(e())) {
            if (io.dcloud.sdk.core.b.a.b().a(getType()) && !getType().equalsIgnoreCase("dcloud")) {
                return false;
            }
            return true;
        } else if (getType().equalsIgnoreCase(Const.TYPE_SGM)) {
            return !TextUtils.isEmpty(f());
        } else {
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public abstract void init(String str, String str2);

    public abstract void load();

    public final void loadFail(int i, String str) {
        if (!this.t) {
            b(i, str);
            this.t = true;
            this.u.removeMessages(1);
            d.a().post(new a(this, (List<? extends DCBaseAOL>) null, 0, i, str));
        }
    }

    public final void loadSuccess() {
        loadSuccess((List<? extends DCBaseAOL>) null);
    }

    public io.dcloud.sdk.poly.base.config.b p() {
        io.dcloud.sdk.poly.base.config.b bVar = new io.dcloud.sdk.poly.base.config.b();
        m();
        bVar.a(e()).c(getSlotId()).b(getType()).d(getTid()).a(l());
        bVar.a(getAdStatus());
        return bVar;
    }

    /* access modifiers changed from: protected */
    public boolean runOnMain() {
        return false;
    }

    public void show(Activity activity) {
    }

    public void showIn(ViewGroup viewGroup) {
    }

    public final void a(Map<String, Object> map) {
        a(-1);
        if (!o()) {
            loadFail(-9999, "");
        } else if (TextUtils.isEmpty(getSlotId())) {
            loadFail(-9999, "");
        } else {
            startLoadTime();
            if (runOnMain()) {
                MainHandlerUtil.getMainHandler().post(new Runnable() {
                    public final void run() {
                        DCBaseAOLLoader.this.load();
                    }
                });
            } else {
                load();
            }
            this.u.sendEmptyMessageDelayed(1, (long) this.w);
        }
    }

    public final void loadSuccess(List<? extends DCBaseAOL> list) {
        if (!this.t) {
            if (list != null) {
                for (DCBaseAOL dCBaseAOL : list) {
                    dCBaseAOL.e = this.e;
                    dCBaseAOL.a(isSlotSupportBidding());
                    dCBaseAOL.setBiddingECPM(g());
                    dCBaseAOL.d = this.d;
                    dCBaseAOL.g = this.g;
                    dCBaseAOL.d(getSlotId());
                    dCBaseAOL.a(e());
                }
            }
            n();
            this.t = true;
            this.u.removeMessages(1);
            d.a().post(new a(this, list, 1, 0, (String) null));
        }
    }

    public void a(ThirdSlotConfig thirdSlotConfig) {
        d(thirdSlotConfig.g());
        a(thirdSlotConfig.k());
        if (!isSlotSupportBidding()) {
            setBiddingECPM(thirdSlotConfig.b());
        }
        this.d = thirdSlotConfig.f();
        this.w = thirdSlotConfig.h();
        this.e = thirdSlotConfig.c();
        this.g = thirdSlotConfig.a();
        e.d("load sub slot cfg:" + thirdSlotConfig.toString());
    }

    public void a(String str, String str2) {
        a(str);
        b(str2);
        init(str, str2);
    }

    public void a(a.C0060a aVar) {
        this.x = aVar;
    }
}
