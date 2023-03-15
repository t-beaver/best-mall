package io.dcloud.feature.weex.extend;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.Constants;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXUtils;
import java.util.HashMap;

public class DCCoverViewComponent extends WXVContainer<ViewGroup> {
    HashMap<String, String> CalloutMarkerIds = new HashMap<>();
    private FrameLayout realView;

    public DCCoverViewComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    /* access modifiers changed from: protected */
    public ViewGroup initComponentHostView(Context context) {
        ScrollView scrollView;
        String str = "";
        if (getStyles().containsKey(Constants.Name.OVERFLOW)) {
            str = WXUtils.getString(getStyles().get(Constants.Name.OVERFLOW), str);
        } else if (getStyles().containsKey("overflowY")) {
            str = WXUtils.getString(getStyles().get("overflowY"), str);
        }
        this.realView = new FrameLayout(context);
        if (str.equals("scroll")) {
            ScrollView scrollView2 = new ScrollView(context);
            scrollView2.addView(this.realView, new FrameLayout.LayoutParams(-1, -1));
            scrollView = scrollView2;
        } else {
            scrollView = this.realView;
        }
        Object obj = getAttrs().get("slot");
        if (obj != null && obj.equals("callout")) {
            scrollView.setVisibility(4);
        }
        return scrollView;
    }

    public ViewGroup getRealView() {
        return this.realView;
    }

    public HashMap<String, String> getCalloutMarkerIds() {
        return this.CalloutMarkerIds;
    }

    public void addChild(WXComponent wXComponent, int i) {
        Object obj;
        if ((wXComponent instanceof DCCoverViewComponent) && (obj = wXComponent.getAttrs().get("markerId")) != null) {
            this.CalloutMarkerIds.put(obj.toString(), wXComponent.getRef());
        }
        super.addChild(wXComponent, i);
    }

    public void remove(WXComponent wXComponent, boolean z) {
        if (wXComponent instanceof DCCoverViewComponent) {
            Object obj = wXComponent.getAttrs().get("marker-id");
            if (obj != null && !this.CalloutMarkerIds.get(obj).equals(wXComponent.getRef())) {
                this.CalloutMarkerIds.remove(obj);
                return;
            }
            return;
        }
        super.remove(wXComponent, z);
    }
}
