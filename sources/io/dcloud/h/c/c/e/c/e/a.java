package io.dcloud.h.c.c.e.c.e;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import io.dcloud.h.a.e.f;
import io.dcloud.h.c.c.a.a;
import io.dcloud.h.c.c.e.a.c;
import io.dcloud.h.c.c.e.c.e.a;
import io.dcloud.sdk.core.adapter.IAdAdapter;
import io.dcloud.sdk.core.api.AOLLoader;
import io.dcloud.sdk.core.entry.DCloudAdSlot;
import io.dcloud.sdk.core.entry.SplashConfig;
import io.dcloud.sdk.core.module.DCBaseAOL;
import io.dcloud.sdk.core.module.DCBaseAOLLoader;
import io.dcloud.sdk.core.util.AdSizeUtil;
import io.dcloud.sdk.core.util.MainHandlerUtil;
import io.dcloud.sdk.poly.base.config.DCloudSlotConfig;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.json.JSONArray;
import org.json.JSONObject;

public class a extends io.dcloud.h.c.c.e.c.f.a {
    private SplashConfig w;
    /* access modifiers changed from: private */
    public final Queue<DCBaseAOLLoader> x = new ConcurrentLinkedQueue();

    public a(Activity activity, int i) {
        super(activity, i);
        a((a.C0055a) new b());
    }

    /* access modifiers changed from: private */
    public DCBaseAOLLoader s() {
        IAdAdapter b2 = io.dcloud.sdk.core.b.a.b().b("dcloud");
        if (b2 == null) {
            return null;
        }
        DCBaseAOLLoader ad = b2.getAd(a(), this.b);
        this.x.add(ad);
        return ad;
    }

    /* access modifiers changed from: protected */
    public int d() {
        return 1;
    }

    public SplashConfig t() {
        return this.w;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void d(JSONObject jSONObject) {
        if (jSONObject != null) {
            c cVar = this.r;
            if (cVar instanceof AOLLoader.SplashAdLoadListener) {
                ((AOLLoader.SplashAdLoadListener) cVar).pushAd(jSONObject);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void b(JSONObject jSONObject) {
        MainHandlerUtil.getMainHandler().post(new Runnable(jSONObject) {
            public final /* synthetic */ JSONObject f$1;

            {
                this.f$1 = r2;
            }

            public final void run() {
                a.this.d(this.f$1);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void c(JSONObject jSONObject) {
        if (jSONObject != null) {
            String optString = jSONObject.optString("src");
            if (!TextUtils.isEmpty(optString)) {
                Glide.with(a()).asBitmap().load(optString).into(new C0061a(jSONObject));
            }
        }
    }

    public DCBaseAOLLoader b() {
        if (this.x.isEmpty()) {
            return s();
        }
        return this.x.remove();
    }

    public void a(ViewGroup viewGroup) {
        DCBaseAOL dCBaseAOL = this.s;
        if (dCBaseAOL instanceof DCBaseAOLLoader) {
            ((DCBaseAOLLoader) dCBaseAOL).showIn(viewGroup);
        }
    }

    public void a(SplashConfig splashConfig, c cVar) {
        this.w = splashConfig;
        super.a(new DCloudAdSlot.Builder().height(splashConfig.getHeight()).width(splashConfig.getWidth()).build(), cVar);
    }

    /* access modifiers changed from: protected */
    public void a(DCloudSlotConfig dCloudSlotConfig) {
        super.a(dCloudSlotConfig);
    }

    /* access modifiers changed from: protected */
    public void a(int i, String str) {
        super.a(i, str);
    }

    protected class b extends a.b {
        public b() {
            super("");
        }

        public void a(JSONArray jSONArray) {
        }

        public void a(JSONArray jSONArray, boolean z) {
            if (jSONArray == null || jSONArray.length() == 0) {
                for (DCBaseAOL dCBaseAOL : a.this.x) {
                    ((io.dcloud.h.c.b.a.b) dCBaseAOL).a((JSONArray) null, true);
                }
                return;
            }
            if (a.this.x.size() > 0) {
                for (DCBaseAOL dCBaseAOL2 : a.this.x) {
                    if (jSONArray.length() > 0) {
                        JSONArray jSONArray2 = new JSONArray();
                        jSONArray2.put(jSONArray.opt(0));
                        ((io.dcloud.h.c.b.a.b) dCBaseAOL2).a(jSONArray2, true);
                    } else {
                        ((io.dcloud.h.c.b.a.b) dCBaseAOL2).a((JSONArray) null, true);
                    }
                }
                if (jSONArray.length() > 0) {
                    for (int i = 0; i < jSONArray.length(); i++) {
                        io.dcloud.h.c.b.a.b bVar = (io.dcloud.h.c.b.a.b) a.this.s();
                        if (bVar != null) {
                            JSONArray jSONArray3 = new JSONArray();
                            jSONArray3.put(jSONArray.opt(0));
                            bVar.a(jSONArray3, true);
                            a.this.x.add(bVar);
                        }
                    }
                    return;
                }
                return;
            }
            for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                io.dcloud.h.c.b.a.b bVar2 = (io.dcloud.h.c.b.a.b) a.this.s();
                if (bVar2 != null) {
                    JSONArray jSONArray4 = new JSONArray();
                    jSONArray4.put(jSONArray.opt(i2));
                    bVar2.a(jSONArray4, true);
                    a.this.x.add(bVar2);
                }
            }
        }

        public void a(JSONObject jSONObject) {
            super.a(jSONObject);
            a.this.a(jSONObject);
        }

        public void a(int i, String str) {
            super.a(i, str);
            a.this.b(i, str);
        }
    }

    /* renamed from: io.dcloud.h.c.c.e.c.e.a$a  reason: collision with other inner class name */
    class C0061a extends CustomTarget<Bitmap> {
        final /* synthetic */ JSONObject a;

        C0061a(JSONObject jSONObject) {
            this.a = jSONObject;
        }

        /* renamed from: a */
        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
            JSONObject optJSONObject = this.a.optJSONObject("pos");
            if (optJSONObject != null) {
                RelativeLayout relativeLayout = new RelativeLayout(a.this.a());
                ImageView imageView = new ImageView(a.this.a());
                int pxFromDp = AdSizeUtil.pxFromDp((float) optJSONObject.optInt("width", -2), a.this.a().getResources().getDisplayMetrics());
                int pxFromDp2 = AdSizeUtil.pxFromDp((float) optJSONObject.optInt("height", -2), a.this.a().getResources().getDisplayMetrics());
                int pxFromDp3 = AdSizeUtil.pxFromDp((float) optJSONObject.optInt("left", -1), a.this.a().getResources().getDisplayMetrics());
                int pxFromDp4 = AdSizeUtil.pxFromDp((float) optJSONObject.optInt("right", -1), a.this.a().getResources().getDisplayMetrics());
                int pxFromDp5 = AdSizeUtil.pxFromDp((float) optJSONObject.optInt("top", -1), a.this.a().getResources().getDisplayMetrics());
                int pxFromDp6 = AdSizeUtil.pxFromDp((float) optJSONObject.optInt("bottom", -1), a.this.a().getResources().getDisplayMetrics());
                imageView.setImageBitmap(bitmap);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                relativeLayout.addView(imageView, new ViewGroup.LayoutParams(pxFromDp, pxFromDp2));
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
                if (pxFromDp4 >= 0) {
                    layoutParams.rightMargin = pxFromDp4;
                } else if (pxFromDp3 >= 0) {
                    layoutParams.leftMargin = pxFromDp3;
                }
                if (pxFromDp6 >= 0) {
                    layoutParams.bottomMargin = pxFromDp6;
                } else if (pxFromDp5 >= 0) {
                    layoutParams.topMargin = pxFromDp5;
                }
                if (pxFromDp3 >= 0) {
                    if (pxFromDp5 >= 0) {
                        layoutParams.gravity = 8388659;
                    }
                    if (pxFromDp6 >= 0) {
                        layoutParams.gravity = 8388691;
                    }
                }
                if (pxFromDp4 >= 0) {
                    if (pxFromDp5 >= 0) {
                        layoutParams.gravity = 8388661;
                    }
                    if (pxFromDp6 >= 0) {
                        layoutParams.gravity = 8388693;
                    }
                }
                imageView.setOnClickListener(new View.OnClickListener(relativeLayout, this.a) {
                    public final /* synthetic */ RelativeLayout f$1;
                    public final /* synthetic */ JSONObject f$2;

                    {
                        this.f$1 = r2;
                        this.f$2 = r3;
                    }

                    public final void onClick(View view) {
                        a.C0061a.this.a(this.f$1, this.f$2, view);
                    }
                });
                MainHandlerUtil.getMainHandler().post(new Runnable(relativeLayout, layoutParams) {
                    public final /* synthetic */ RelativeLayout f$1;
                    public final /* synthetic */ FrameLayout.LayoutParams f$2;

                    {
                        this.f$1 = r2;
                        this.f$2 = r3;
                    }

                    public final void run() {
                        a.C0061a.this.a(this.f$1, this.f$2);
                    }
                });
            }
        }

        public void onLoadCleared(Drawable drawable) {
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void a(RelativeLayout relativeLayout, JSONObject jSONObject, View view) {
            if (relativeLayout.getParent() != null) {
                ((ViewGroup) relativeLayout.getParent()).removeView(relativeLayout);
            }
            String optString = jSONObject.optString("click_action", "");
            if (optString.equals("browser")) {
                io.dcloud.h.a.e.b.c(a.this.a(), jSONObject.optString("url"));
            } else if (optString.equals("url")) {
                io.dcloud.h.a.e.b.e(a.this.a(), jSONObject.optString("url"));
            }
            f.a().a(new Runnable(io.dcloud.h.c.a.d().b().getAppId(), jSONObject, io.dcloud.h.c.a.d().b().getAdId()) {
                public final /* synthetic */ String f$1;
                public final /* synthetic */ JSONObject f$2;
                public final /* synthetic */ String f$3;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                    this.f$3 = r4;
                }

                public final void run() {
                    a.C0061a.this.a(this.f$1, this.f$2, this.f$3);
                }
            });
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void a(String str, JSONObject jSONObject, String str2) {
            io.dcloud.h.c.c.a.b.b.a((Context) a.this.a(), str, jSONObject.optString("tid"), str2, 10, "");
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void a(RelativeLayout relativeLayout, FrameLayout.LayoutParams layoutParams) {
            if (a.this.r instanceof AOLLoader.SplashAdLoadListener) {
                ((AOLLoader.SplashAdLoadListener) a.this.r).redBag(relativeLayout, layoutParams);
            }
        }
    }
}
