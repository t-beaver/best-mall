package com.taobao.weex.ui.action;

import android.widget.ScrollView;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.dom.transition.WXTransition;
import com.taobao.weex.ui.component.WXBaseScroller;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXLogUtils;
import java.util.Map;
import java.util.Set;

public class GraphicActionCreateBody extends GraphicActionAbstractAddElement {
    private WXComponent component;

    public GraphicActionCreateBody(WXSDKInstance wXSDKInstance, String str, String str2, Map<String, String> map, Map<String, String> map2, Set<String> set, float[] fArr, float[] fArr2, float[] fArr3) {
        super(wXSDKInstance, str);
        this.mComponentType = str2;
        this.mStyle = map;
        this.mAttributes = map2;
        this.mEvents = set;
        this.mMargins = fArr;
        this.mPaddings = fArr2;
        this.mBorders = fArr3;
        if (wXSDKInstance.getContext() != null) {
            WXComponent createComponent = createComponent(wXSDKInstance, (WXVContainer) null, new BasicComponentData(getRef(), this.mComponentType, (String) null));
            this.component = createComponent;
            if (createComponent != null) {
                createComponent.setTransition(WXTransition.fromMap(createComponent.getStyles(), this.component));
            }
        }
    }

    public void executeAction() {
        super.executeAction();
        try {
            this.component.createView();
            WXComponent wXComponent = this.component;
            wXComponent.applyLayoutAndEvent(wXComponent);
            WXComponent wXComponent2 = this.component;
            wXComponent2.bindData(wXComponent2);
            WXSDKInstance wXSDKIntance = getWXSDKIntance();
            WXComponent wXComponent3 = this.component;
            if (wXComponent3 instanceof WXBaseScroller) {
                WXBaseScroller wXBaseScroller = (WXBaseScroller) wXComponent3;
                if (wXBaseScroller.getInnerView() instanceof ScrollView) {
                    wXSDKIntance.setRootScrollView((ScrollView) wXBaseScroller.getInnerView());
                }
            }
            wXSDKIntance.onRootCreated(this.component);
            if (wXSDKIntance.getRenderStrategy() != WXRenderStrategy.APPEND_ONCE) {
                wXSDKIntance.onCreateFinish();
            }
        } catch (Exception e) {
            WXLogUtils.e("create body failed.", (Throwable) e);
        }
    }
}
