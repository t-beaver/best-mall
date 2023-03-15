package io.dcloud.feature.ui;

import io.dcloud.common.DHInterface.IEventCallback;
import io.dcloud.common.DHInterface.IFrameView;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameItem;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.util.PdrUtil;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

public class d extends b implements IEventCallback {
    ArrayList<IFrameView> u;
    IFrameView v;

    public d(String str, ArrayList<IFrameView> arrayList, JSONObject jSONObject) {
        super(str);
        this.u = arrayList;
    }

    public void a(int i, int i2, int i3, int i4, int i5, int i6) {
    }

    public void a(boolean z) {
        this.v.setVisible(z, true);
    }

    public AdaFrameItem d() {
        return (AdaFrameItem) this.v;
    }

    /* access modifiers changed from: protected */
    public void e() {
    }

    public void i() {
        this.j.c(this.v);
        Iterator<IFrameView> it = this.u.iterator();
        while (it.hasNext()) {
            IFrameView next = it.next();
            this.j.c(next);
            ((AdaFrameView) next).isChildOfFrameView = true;
        }
    }

    public Object onCallBack(String str, Object obj) {
        if (PdrUtil.isEquals(str, AbsoluteConst.EVENTS_PAGER_SELECTED)) {
            a(str, PdrUtil.isEmpty(obj) ? null : String.valueOf(obj), false);
        }
        return null;
    }

    public void a(IFrameView iFrameView) {
        this.v = iFrameView;
    }

    public String a(IWebview iWebview, String str, JSONArray jSONArray) {
        try {
            if (!"addEventListener".equals(str) || iWebview == null) {
                "setSelectIndex".equals(str);
                return null;
            }
            a(jSONArray.getString(1), jSONArray.getString(0), this.i.get(iWebview.getWebviewANID()));
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
