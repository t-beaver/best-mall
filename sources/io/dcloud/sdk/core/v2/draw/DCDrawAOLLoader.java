package io.dcloud.sdk.core.v2.draw;

import android.app.Activity;
import io.dcloud.h.c.c.e.c.b.b;
import io.dcloud.h.c.c.e.c.c.a;
import io.dcloud.sdk.core.entry.DCloudAdSlot;
import io.dcloud.sdk.core.util.AdErrorUtil;
import io.dcloud.sdk.core.v2.base.DCBaseAOL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;

public class DCDrawAOLLoader extends DCBaseAOL {
    private b b;

    public DCDrawAOLLoader(Activity activity) {
        super(activity);
        this.b = new b(activity);
    }

    public void load(DCloudAdSlot dCloudAdSlot, final DCDrawAOLLoadListener dCDrawAOLLoadListener) {
        if (getContext() != null && dCloudAdSlot != null) {
            this.b.a(dCloudAdSlot, new io.dcloud.h.c.c.e.a.b() {
                public void onError(int i, String str, JSONArray jSONArray) {
                    DCDrawAOLLoadListener dCDrawAOLLoadListener = dCDrawAOLLoadListener;
                    if (dCDrawAOLLoadListener != null) {
                        dCDrawAOLLoadListener.onError(i, str, jSONArray);
                    }
                }

                public void onLoaded(List<a> list) {
                    ArrayList arrayList = new ArrayList();
                    for (a dCDrawAOL : list) {
                        arrayList.add(new DCDrawAOL(dCDrawAOL));
                    }
                    DCDrawAOLLoadListener dCDrawAOLLoadListener = dCDrawAOLLoadListener;
                    if (dCDrawAOLLoadListener != null) {
                        dCDrawAOLLoadListener.onDrawAdLoad(arrayList);
                    }
                }
            });
        } else if (dCDrawAOLLoadListener != null) {
            dCDrawAOLLoadListener.onError(-5014, AdErrorUtil.getErrorMsg(-5014), (JSONArray) null);
        }
    }
}
