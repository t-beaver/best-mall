package com.taobao.weex.ui.animation;

import android.text.TextUtils;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.ui.action.GraphicActionAnimation;
import io.dcloud.feature.uniapp.AbsSDKInstance;
import io.dcloud.feature.uniapp.ui.AbsAnimationHolder;
import io.dcloud.feature.uniapp.ui.component.AbsBasicComponent;

public class WXAnimationModule extends WXModule {
    @JSMethod
    public void transition(String str, String str2, String str3) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2) && this.mWXSDKInstance != null) {
            GraphicActionAnimation graphicActionAnimation = new GraphicActionAnimation(this.mWXSDKInstance, str, str2, str3);
            WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(graphicActionAnimation.getPageId(), graphicActionAnimation);
        }
    }

    public static class AnimationHolder implements AbsAnimationHolder {
        private String callback;
        private WXAnimationBean wxAnimationBean;

        public AnimationHolder(WXAnimationBean wXAnimationBean, String str) {
            this.wxAnimationBean = wXAnimationBean;
            this.callback = str;
        }

        public void execute(AbsSDKInstance absSDKInstance, AbsBasicComponent absBasicComponent) {
            if (absSDKInstance != null && absBasicComponent != null) {
                GraphicActionAnimation graphicActionAnimation = new GraphicActionAnimation((WXSDKInstance) absSDKInstance, absBasicComponent.getRef(), this.wxAnimationBean, this.callback);
                WXSDKManager.getInstance().getWXRenderManager().postGraphicAction(graphicActionAnimation.getPageId(), graphicActionAnimation);
            }
        }
    }
}
