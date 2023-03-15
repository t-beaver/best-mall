package io.dcloud.h.c.c.e.c.d;

import android.app.Activity;
import io.dcloud.sdk.core.api.AOLLoader;
import io.dcloud.sdk.core.util.MainHandlerUtil;
import org.json.JSONObject;

public class a extends io.dcloud.h.c.c.e.c.f.a implements AOLLoader.RewardVideoAdInteractionListener {
    public a(Activity activity) {
        super(activity, 9);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void b(JSONObject jSONObject) {
        AOLLoader.VideoAdInteractionListener videoAdInteractionListener = this.q;
        if (videoAdInteractionListener instanceof AOLLoader.RewardVideoAdInteractionListener) {
            ((AOLLoader.RewardVideoAdInteractionListener) videoAdInteractionListener).onReward(jSONObject);
        }
    }

    public void onReward(JSONObject jSONObject) {
        MainHandlerUtil.getMainHandler().post(new Runnable(jSONObject) {
            public final /* synthetic */ JSONObject f$1;

            {
                this.f$1 = r2;
            }

            public final void run() {
                a.this.b(this.f$1);
            }
        });
    }
}
