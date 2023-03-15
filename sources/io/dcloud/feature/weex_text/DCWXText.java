package io.dcloud.feature.weex_text;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.ui.ComponentCreator;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXText;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.view.WXTextView;
import java.lang.reflect.InvocationTargetException;

@Component(lazyload = false)
public class DCWXText extends WXText {
    public DCWXText(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
        setContentBoxMeasurement(new DCTextContentBoxMeasurement(this));
    }

    public static class Creator implements ComponentCreator {
        public WXComponent createInstance(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) throws IllegalAccessException, InvocationTargetException, InstantiationException {
            return new DCWXText(wXSDKInstance, wXVContainer, basicComponentData);
        }
    }

    @WXComponentProp(name = "selectable")
    public void setSelectable(boolean z) {
        ((WXTextView) getHostView()).enableCopy(true);
    }
}
