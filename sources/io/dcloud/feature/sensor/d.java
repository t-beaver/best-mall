package io.dcloud.feature.sensor;

import android.content.Context;
import com.taobao.weex.common.Constants;
import io.dcloud.common.DHInterface.IEventCallback;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.util.PdrUtil;
import java.util.HashMap;

public class d implements IEventCallback {
    private HashMap<IWebview, c> a = new HashMap<>();
    Context b;

    public d(Context context) {
        this.b = context;
    }

    public String a(IWebview iWebview, String str, String[] strArr) {
        if (str.equals("start")) {
            a(iWebview, strArr[0]);
            ((AdaFrameView) iWebview.obtainFrameView()).addFrameViewListener(this);
            return "";
        } else if (!str.equals(Constants.Value.STOP)) {
            return "";
        } else {
            a(iWebview);
            return "";
        }
    }

    public Object onCallBack(String str, Object obj) {
        if ((!PdrUtil.isEquals(str, AbsoluteConst.EVENTS_WINDOW_CLOSE) && !PdrUtil.isEquals(str, AbsoluteConst.EVENTS_CLOSE)) || !(obj instanceof IWebview)) {
            return null;
        }
        IWebview iWebview = (IWebview) obj;
        a(iWebview);
        ((AdaFrameView) iWebview.obtainFrameView()).removeFrameViewListener(this);
        return null;
    }

    private void a(IWebview iWebview, String str) {
        c cVar = this.a.get(iWebview);
        if (cVar == null) {
            cVar = new c(iWebview, str);
            this.a.put(iWebview, cVar);
        }
        cVar.a();
    }

    private void a(IWebview iWebview) {
        c remove = this.a.remove(iWebview);
        if (remove != null) {
            remove.b();
        }
    }
}
