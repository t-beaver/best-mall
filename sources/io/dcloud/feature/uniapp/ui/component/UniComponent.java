package io.dcloud.feature.uniapp.ui.component;

import android.view.View;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.ui.IFComponentHolder;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXVContainer;
import io.dcloud.feature.uniapp.UniSDKInstance;
import io.dcloud.feature.uniapp.ui.AbsIComponentHolder;
import io.dcloud.feature.uniapp.ui.action.AbsComponentData;

public class UniComponent<T extends View> extends WXComponent<T> {
    public UniSDKInstance mUniSDKInstance;

    public interface OnClickListener extends WXComponent.OnClickListener {
    }

    public interface OnFocusChangeListener extends WXComponent.OnFocusChangeListener {
    }

    public UniComponent(UniSDKInstance uniSDKInstance, AbsVContainer absVContainer, int i, AbsComponentData absComponentData) {
        super((WXSDKInstance) uniSDKInstance, (WXVContainer) absVContainer, i, (BasicComponentData) absComponentData);
        this.mUniSDKInstance = uniSDKInstance;
    }

    public UniComponent(UniSDKInstance uniSDKInstance, AbsVContainer absVContainer, AbsComponentData absComponentData) {
        super(uniSDKInstance, (WXVContainer) absVContainer, (BasicComponentData) absComponentData);
        this.mUniSDKInstance = uniSDKInstance;
    }

    public UniSDKInstance getUniInstance() {
        return this.mUniSDKInstance;
    }

    /* access modifiers changed from: protected */
    public void updateStyles(UniComponent uniComponent) {
        updateStyles(uniComponent);
    }

    public final void updateStyles(WXComponent wXComponent) {
        super.updateStyles(wXComponent);
    }

    public final void bindData(WXComponent wXComponent) {
        super.bindData(wXComponent);
    }

    public final AbsBasicComponent findUniComponent(String str) {
        if (getInstance() == null || str == null) {
            return null;
        }
        return WXSDKManager.getInstance().getWXRenderManager().getWXComponent(getInstanceId(), str);
    }

    public void bindHolder(AbsIComponentHolder absIComponentHolder) {
        super.bindHolder((IFComponentHolder) absIComponentHolder);
    }

    public final void bindHolder(IFComponentHolder iFComponentHolder) {
        super.bindHolder(iFComponentHolder);
    }

    public void postAnimation(UniAnimationHolder uniAnimationHolder) {
        super.postAnimation(uniAnimationHolder);
    }

    /* access modifiers changed from: protected */
    public final void addFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        super.addFocusChangeListener(onFocusChangeListener);
    }

    /* access modifiers changed from: protected */
    public final void addClickListener(OnClickListener onClickListener) {
        super.addClickListener(onClickListener);
    }

    public void destroy() {
        super.destroy();
        this.mUniSDKInstance = null;
    }
}
