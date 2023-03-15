package io.dcloud.feature.sensor;

import com.taobao.weex.common.Constants;
import io.dcloud.common.DHInterface.IEventCallback;
import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.adapter.ui.AdaFrameView;
import io.dcloud.common.util.PdrUtil;
import java.util.HashMap;

public class f implements IEventCallback {
    HashMap<IWebview, e> a = new HashMap<>();

    public String a(IWebview iWebview, String str, String[] strArr) {
        e eVar;
        if (str.equals("getCurrentProximity")) {
            String str2 = strArr[0];
            e eVar2 = this.a.get(iWebview);
            if (eVar2 == null) {
                eVar2 = new e(iWebview);
                this.a.put(iWebview, eVar2);
            }
            eVar2.d = str2;
            eVar2.a();
            return null;
        } else if (str.equals("start")) {
            String str3 = strArr[0];
            ((AdaFrameView) iWebview.obtainFrameView()).addFrameViewListener(this);
            e eVar3 = this.a.get(iWebview);
            if (eVar3 == null) {
                eVar3 = new e(iWebview);
                this.a.put(iWebview, eVar3);
            }
            eVar3.e = str3;
            eVar3.a();
            return null;
        } else if (!str.equals(Constants.Value.STOP) || (eVar = this.a.get(iWebview)) == null) {
            return null;
        } else {
            eVar.b();
            return null;
        }
    }

    public Object onCallBack(String str, Object obj) {
        if ((!PdrUtil.isEquals(str, AbsoluteConst.EVENTS_WINDOW_CLOSE) && !PdrUtil.isEquals(str, AbsoluteConst.EVENTS_CLOSE)) || !(obj instanceof IWebview)) {
            return null;
        }
        IWebview iWebview = (IWebview) obj;
        e remove = this.a.remove(iWebview);
        if (remove != null) {
            remove.b();
        }
        ((AdaFrameView) iWebview.obtainFrameView()).removeFrameViewListener(this);
        return null;
    }
}
