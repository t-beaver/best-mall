package com.taobao.weex.ui.component;

import android.content.Context;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.view.refresh.circlebar.CircleProgressBar;
import com.taobao.weex.utils.WXResourceUtils;
import com.taobao.weex.utils.WXUtils;

@Component(lazyload = false)
public class WXLoadingIndicator extends WXComponent<CircleProgressBar> {
    private static final String ANIMATING = "animating";

    public WXLoadingIndicator(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, z, basicComponentData);
    }

    /* access modifiers changed from: protected */
    public CircleProgressBar initComponentHostView(Context context) {
        return new CircleProgressBar(context);
    }

    /* access modifiers changed from: protected */
    public boolean setProperty(String str, Object obj) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case 93090825:
                if (str.equals("arrow")) {
                    c = 0;
                    break;
                }
                break;
            case 94842723:
                if (str.equals("color")) {
                    c = 1;
                    break;
                }
                break;
            case 1118509918:
                if (str.equals("animating")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                ((CircleProgressBar) getHostView()).setShowArrow(WXUtils.getBoolean(obj, true).booleanValue());
                return true;
            case 1:
                String string = WXUtils.getString(obj, (String) null);
                if (string != null) {
                    setColor(string);
                }
                return true;
            case 2:
                Boolean bool = WXUtils.getBoolean(obj, (Boolean) null);
                if (bool != null) {
                    setAnimating(bool.booleanValue());
                }
                return true;
            default:
                return super.setProperty(str, obj);
        }
    }

    @WXComponentProp(name = "color")
    public void setColor(String str) {
        if (str != null && !str.equals("")) {
            ((CircleProgressBar) getHostView()).setColorSchemeColors(WXResourceUtils.getColor(str, -65536));
        }
    }

    @WXComponentProp(name = "animating")
    public void setAnimating(boolean z) {
        if (z) {
            ((CircleProgressBar) getHostView()).start();
        } else {
            ((CircleProgressBar) getHostView()).stop();
        }
    }

    private void setAnimatingSp(boolean z) {
        if (z) {
            ((CircleProgressBar) getHostView()).start();
        } else {
            ((CircleProgressBar) getHostView()).stop();
        }
    }

    public void destroy() {
        super.destroy();
        if (getHostView() != null) {
            ((CircleProgressBar) getHostView()).destory();
        }
    }
}
