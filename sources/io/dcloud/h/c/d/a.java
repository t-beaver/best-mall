package io.dcloud.h.c.d;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import com.facebook.common.callercontext.ContextChain;
import io.dcloud.common.DHInterface.ICallBack;
import io.dcloud.common.util.BaseInfo;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.feature.gg.AdSplashUtil;
import io.dcloud.feature.gg.dcloud.GGSplashView;
import io.dcloud.feature.ui.navigator.QueryNotchTool;
import io.dcloud.h.c.c.a.a;
import io.dcloud.h.c.c.e.c.e.a;
import io.dcloud.sdk.core.api.AOLLoader;
import io.dcloud.sdk.core.entry.DCloudAdSlot;
import io.dcloud.sdk.core.entry.SplashConfig;
import io.dcloud.sdk.core.module.DCBaseAOL;
import io.dcloud.sdk.core.util.AdErrorUtil;
import io.dcloud.sdk.core.util.Const;
import io.dcloud.sdk.core.v2.interstitial.DCInterstitialAOL;
import io.dcloud.sdk.core.v2.interstitial.DCInterstitialAOLListener;
import io.dcloud.sdk.core.v2.interstitial.DCInterstitialAOLLoadListener;
import io.dcloud.sdk.core.v2.splash.DCSplashAOLLoadListener;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class a extends io.dcloud.h.c.c.e.c.e.a {
    /* access modifiers changed from: private */
    public String A = "";
    /* access modifiers changed from: private */
    public boolean B = false;
    b C;
    /* access modifiers changed from: private */
    public String D = "";
    /* access modifiers changed from: private */
    public String E = "";
    private boolean F = false;
    /* access modifiers changed from: private */
    public boolean G = false;
    GGSplashView H;
    long I = 0;
    /* access modifiers changed from: private */
    public long J = 0;
    DCInterstitialAOL K;
    /* access modifiers changed from: private */
    public boolean L = false;
    /* access modifiers changed from: private */
    public boolean M = false;
    /* access modifiers changed from: private */
    public boolean y = false;
    /* access modifiers changed from: private */
    public int z = 0;

    class b implements AOLLoader.VideoAdInteractionListener {
        b() {
        }

        public void onClick() {
        }

        public void onClose() {
            a.this.u();
        }

        public void onShow() {
        }

        public void onShowError(int i, String str) {
            a.this.u();
        }

        public void onSkip() {
            a.this.u();
        }

        public void onVideoPlayEnd() {
            a.this.u();
        }
    }

    class c implements DCInterstitialAOLListener {
        c() {
        }

        public void onClick() {
        }

        public void onClose() {
            AdSplashUtil.setShowInterstitialAd(false);
        }

        public void onShow() {
            AdSplashUtil.setShowInterstitialAd(true);
        }

        public void onShowError(int i, String str) {
            AdSplashUtil.setShowInterstitialAd(false);
        }

        public void onSkip() {
        }

        public void onVideoPlayEnd() {
        }
    }

    class d implements DCInterstitialAOLLoadListener {
        d() {
        }

        public void onError(int i, String str, JSONArray jSONArray) {
            boolean unused = a.this.L = true;
        }

        public void onInterstitialAdLoad() {
            boolean unused = a.this.L = true;
            if (a.this.M && a.this.J > 0 && a.this.J > SystemClock.elapsedRealtime()) {
                a aVar = a.this;
                aVar.K.show(aVar.a());
            }
        }
    }

    public a(Activity activity) {
        super(activity, 1);
    }

    /* access modifiers changed from: private */
    public void u() {
        GGSplashView gGSplashView = this.H;
        if (gGSplashView != null) {
            gGSplashView.onFinishShow();
        }
    }

    public void v() {
        this.L = false;
        if (!TextUtils.isEmpty(this.A) && !AdSplashUtil.isShowingInterstitialAd()) {
            DCloudAdSlot build = new DCloudAdSlot.Builder().adpid(this.A).build();
            DCInterstitialAOL dCInterstitialAOL = new DCInterstitialAOL(a());
            this.K = dCInterstitialAOL;
            dCInterstitialAOL.setInterstitialAdListener(new c());
            this.K.load(build, new d());
        }
    }

    public void w() {
        if (!this.L) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            int i = this.z;
            if (i <= 0) {
                i = 2500;
            }
            this.J = elapsedRealtime + ((long) i);
            this.M = true;
            return;
        }
        DCInterstitialAOL dCInterstitialAOL = this.K;
        if (dCInterstitialAOL != null && dCInterstitialAOL.isValid()) {
            this.K.show(a());
        }
    }

    /* access modifiers changed from: protected */
    public int d() {
        if (!this.F) {
            return super.d();
        }
        return 3;
    }

    /* access modifiers changed from: protected */
    public void b(List<DCBaseAOL> list) {
        super.b(list);
    }

    /* access modifiers changed from: protected */
    public void a(int i, String str, JSONArray jSONArray) {
        super.a(i, str, jSONArray);
    }

    /* renamed from: io.dcloud.h.c.d.a$a  reason: collision with other inner class name */
    class C0062a extends a.b {
        final /* synthetic */ boolean d;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        C0062a(boolean z) {
            super();
            this.d = z;
        }

        public void a(JSONArray jSONArray) {
            if (jSONArray != null && jSONArray.length() > 0) {
                for (int i = 0; i < jSONArray.length(); i++) {
                    JSONObject optJSONObject = jSONArray.optJSONObject(i);
                    if (optJSONObject != null) {
                        String optString = optJSONObject.optString("action", "");
                        if (optString.equals("push")) {
                            a.this.b(optJSONObject);
                        } else if (optString.equals("redPackage")) {
                            a.this.c(optJSONObject);
                        }
                    }
                }
            }
        }

        public void a(JSONObject jSONObject) {
            if (jSONObject == null || jSONObject.length() == 0) {
                a.this.a(-5001, AdErrorUtil.getErrorMsg(-5001));
                return;
            }
            SplashConfig t = a.this.t();
            b bVar = a.this.C;
            if (bVar != null) {
                try {
                    bVar.a(jSONObject);
                    t = a.this.C.b();
                    if (!a.this.C.a()) {
                        super.a(-5000, "");
                        return;
                    }
                } catch (Exception unused) {
                }
                if (!io.dcloud.sdk.poly.base.utils.a.d(a.this.a())) {
                    super.a(-5000, "");
                    return;
                }
            }
            if (t != null) {
                a.this.a(new DCloudAdSlot.Builder().height(t.getHeight()).width(t.getWidth()).build());
            }
            if (this.d && io.dcloud.sdk.core.b.a.b().c().contains(Const.TYPE_HW)) {
                JSONArray optJSONArray = jSONObject.optJSONArray("cfgs");
                ArrayList<Integer> arrayList = new ArrayList<>();
                if (optJSONArray != null && optJSONArray.length() > 0) {
                    for (int i = 0; i < optJSONArray.length(); i++) {
                        JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                        if (optJSONObject != null && optJSONObject.optString(ContextChain.TAG_PRODUCT).equals(Const.TYPE_HW)) {
                            arrayList.add(Integer.valueOf(i));
                        }
                    }
                    if (arrayList.size() > 0) {
                        for (Integer intValue : arrayList) {
                            optJSONArray.remove(intValue.intValue());
                        }
                        try {
                            jSONObject.put("cfgs", optJSONArray);
                        } catch (JSONException unused2) {
                        }
                    }
                }
            }
            super.a(jSONObject);
        }

        public void a(int i, String str) {
            super.a(i, str);
        }
    }

    public void a(SplashConfig splashConfig, DCSplashAOLLoadListener dCSplashAOLLoadListener, boolean z2) {
        this.H = null;
        this.F = z2;
        a((a.C0055a) new C0062a(z2));
        a(dCSplashAOLLoadListener, z2);
        this.I = SystemClock.elapsedRealtime();
        this.y = false;
        this.G = false;
        super.a(splashConfig, (io.dcloud.h.c.c.e.a.c) this.C);
    }

    class e implements b {
        final /* synthetic */ DCSplashAOLLoadListener a;
        final /* synthetic */ boolean b;

        e(DCSplashAOLLoadListener dCSplashAOLLoadListener, boolean z) {
            this.a = dCSplashAOLLoadListener;
            this.b = z;
        }

        public void a(JSONObject jSONObject) {
            if (jSONObject != null) {
                try {
                    a aVar = a.this;
                    boolean z = false;
                    if (jSONObject.optInt("fs", 0) == 1) {
                        z = true;
                    }
                    boolean unused = aVar.B = z;
                    String unused2 = a.this.D = jSONObject.optString("fr");
                    String unused3 = a.this.E = jSONObject.optString("frt");
                    if (jSONObject.has("cpadpid")) {
                        String unused4 = a.this.A = jSONObject.optString("cpadpid");
                        int unused5 = a.this.z = jSONObject.optInt("fwt");
                        a aVar2 = a.this;
                        int unused6 = aVar2.z = aVar2.z <= 0 ? 2500 : a.this.z;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public SplashConfig b() {
            int i;
            int a2 = a((Context) a.this.c);
            if (a.this.B) {
                i = a(a.this.c);
            } else {
                i = (a(a.this.c) / 20) * 17;
            }
            return new SplashConfig.Builder().width(a2).height(i).build();
        }

        public void onError(int i, String str, JSONArray jSONArray) {
            boolean unused = a.this.y = true;
            boolean unused2 = a.this.G = false;
            a.this.a(false);
            DCSplashAOLLoadListener dCSplashAOLLoadListener = this.a;
            if (dCSplashAOLLoadListener != null) {
                dCSplashAOLLoadListener.onError(i, str, jSONArray);
            }
        }

        public void onLoaded() {
            boolean unused = a.this.y = true;
            boolean unused2 = a.this.G = true;
            a.this.a(false);
            DCSplashAOLLoadListener dCSplashAOLLoadListener = this.a;
            if (dCSplashAOLLoadListener != null) {
                dCSplashAOLLoadListener.onSplashAdLoad();
            }
        }

        private int a(Activity activity) {
            Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            defaultDisplay.getRealMetrics(displayMetrics);
            int i = displayMetrics.heightPixels;
            return (!PdrUtil.hasNavBar(activity) || !PdrUtil.isNavigationBarShowing(activity)) ? i : i - PdrUtil.getNavigationBarHeight(activity);
        }

        private int a(Context context) {
            return context.getResources().getDisplayMetrics().widthPixels;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:2:0x0010, code lost:
            r0 = r2.b;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean a() {
            /*
                r2 = this;
                io.dcloud.h.c.d.a r0 = io.dcloud.h.c.d.a.this
                android.app.Activity r0 = r0.a()
                java.lang.Boolean r0 = io.dcloud.feature.gg.dcloud.ADHandler.SplashAdIsEnable(r0)
                boolean r0 = r0.booleanValue()
                if (r0 == 0) goto L_0x0026
                boolean r0 = r2.b
                if (r0 == 0) goto L_0x0024
                if (r0 == 0) goto L_0x0026
                io.dcloud.h.c.d.a r0 = io.dcloud.h.c.d.a.this
                java.lang.String r0 = r0.D
                java.lang.String r1 = "1"
                boolean r0 = r1.equals(r0)
                if (r0 == 0) goto L_0x0026
            L_0x0024:
                r0 = 1
                goto L_0x0027
            L_0x0026:
                r0 = 0
            L_0x0027:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: io.dcloud.h.c.d.a.e.a():boolean");
        }
    }

    public void a(ViewGroup viewGroup) {
        a((AOLLoader.VideoAdInteractionListener) new b());
        super.a(viewGroup);
    }

    public void a(View view) {
        GGSplashView gGSplashView = this.H;
        if (gGSplashView != null) {
            gGSplashView.onWillCloseSplash();
        }
    }

    public View a(Activity activity, String str, ICallBack iCallBack) {
        if (!this.y || !this.G) {
            return null;
        }
        if (this.H == null) {
            GGSplashView gGSplashView = new GGSplashView(activity);
            this.H = gGSplashView;
            gGSplashView.showAd(this);
        }
        if (this.B) {
            this.H.getBottomIcon().setVisibility(8);
        }
        this.H.setPullTime(this.I);
        this.H.setAppid(str);
        this.H.setCallBack(iCallBack);
        return this.H;
    }

    public void a(Activity activity, String str, ViewGroup viewGroup) {
        v();
        if (!this.y || !this.G) {
            w();
            return;
        }
        a(activity, str, (ICallBack) new ICallBack(viewGroup, activity) {
            public final /* synthetic */ ViewGroup f$1;
            public final /* synthetic */ Activity f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            public final Object onCallBack(int i, Object obj) {
                return a.this.a(this.f$1, this.f$2, i, obj);
            }
        });
        viewGroup.addView(this.H);
        if (!BaseInfo.sGlobalFullScreen) {
            Window window = activity.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.flags |= 1024;
            if (QueryNotchTool.hasNotchInScreen(activity) && Build.VERSION.SDK_INT >= 28) {
                attributes.layoutInDisplayCutoutMode = 1;
            }
            window.setAttributes(attributes);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Object a(ViewGroup viewGroup, Activity activity, int i, Object obj) {
        viewGroup.removeView(this.H);
        if (!BaseInfo.sGlobalFullScreen) {
            Window window = activity.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.flags &= -1025;
            if (QueryNotchTool.hasNotchInScreen(activity) && Build.VERSION.SDK_INT >= 28) {
                attributes.layoutInDisplayCutoutMode = 0;
            }
            window.setAttributes(attributes);
        }
        w();
        return null;
    }

    public boolean a(long j) {
        long j2;
        try {
            j2 = Long.parseLong(this.E);
        } catch (Exception unused) {
            j2 = 0;
        }
        if (j2 <= 0) {
            j2 = 180000;
        }
        if (j + j2 >= SystemClock.elapsedRealtime() || (!"1".equals(this.D) && TextUtils.isEmpty(this.A))) {
            return false;
        }
        return true;
    }

    private void a(DCSplashAOLLoadListener dCSplashAOLLoadListener, boolean z2) {
        this.C = new e(dCSplashAOLLoadListener, z2);
    }
}
