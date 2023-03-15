package io.dcloud.sdk.core.v2.feed;

import android.app.Activity;
import io.dcloud.h.c.c.e.c.c.a;
import io.dcloud.h.c.c.e.c.c.b;
import io.dcloud.sdk.core.entry.DCloudAdSlot;
import io.dcloud.sdk.core.util.AdErrorUtil;
import io.dcloud.sdk.core.v2.base.DCBaseAOL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;

public class DCFeedAOLLoader extends DCBaseAOL {
    private b b;

    public DCFeedAOLLoader(Activity activity) {
        super(activity);
        this.b = new b(activity, 4);
    }

    public void load(DCloudAdSlot dCloudAdSlot, final DCFeedAOLLoadListener dCFeedAOLLoadListener) {
        if (getContext() != null && dCloudAdSlot != null) {
            this.b.a(dCloudAdSlot, new io.dcloud.h.c.c.e.a.b() {
                public void onError(int i, String str, JSONArray jSONArray) {
                    DCFeedAOLLoadListener dCFeedAOLLoadListener = dCFeedAOLLoadListener;
                    if (dCFeedAOLLoadListener != null) {
                        dCFeedAOLLoadListener.onError(i, str, jSONArray);
                    }
                }

                public void onLoaded(List<a> list) {
                    ArrayList arrayList = new ArrayList();
                    for (a dCFeedAOL : list) {
                        arrayList.add(new DCFeedAOL(dCFeedAOL));
                    }
                    DCFeedAOLLoadListener dCFeedAOLLoadListener = dCFeedAOLLoadListener;
                    if (dCFeedAOLLoadListener != null) {
                        dCFeedAOLLoadListener.onFeedAdLoad(arrayList);
                    }
                }
            });
        } else if (dCFeedAOLLoadListener != null) {
            dCFeedAOLLoadListener.onError(-5014, AdErrorUtil.getErrorMsg(-5014), (JSONArray) null);
        }
    }
}
