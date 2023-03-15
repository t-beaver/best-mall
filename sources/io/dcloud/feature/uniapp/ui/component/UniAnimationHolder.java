package io.dcloud.feature.uniapp.ui.component;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.ui.action.GraphicActionAnimation;
import com.taobao.weex.ui.animation.WXAnimationBean;
import io.dcloud.feature.uniapp.AbsSDKInstance;
import io.dcloud.feature.uniapp.ui.AbsAnimationHolder;
import io.dcloud.feature.uniapp.ui.animation.UniAnimationBean;

public class UniAnimationHolder implements AbsAnimationHolder {
    private String callback;
    private WXAnimationBean wxAnimationBean;

    public UniAnimationHolder(UniAnimationBean uniAnimationBean, String str) {
        this.wxAnimationBean = uniAnimationBean;
        this.callback = str;
    }

    public void execute(AbsSDKInstance absSDKInstance, AbsBasicComponent absBasicComponent) {
        if (absSDKInstance != null && absBasicComponent != null) {
            GraphicActionAnimation graphicActionAnimation = new GraphicActionAnimation((WXSDKInstance) absSDKInstance, absBasicComponent.getRef(), this.wxAnimationBean, this.callback);
            WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(graphicActionAnimation.getPageId(), graphicActionAnimation);
        }
    }
}
