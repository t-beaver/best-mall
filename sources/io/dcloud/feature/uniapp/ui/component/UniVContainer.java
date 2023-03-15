package io.dcloud.feature.uniapp.ui.component;

import android.view.ViewGroup;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXVContainer;
import io.dcloud.feature.uniapp.UniSDKInstance;
import io.dcloud.feature.uniapp.ui.action.AbsComponentData;

public class UniVContainer<T extends ViewGroup> extends WXVContainer<T> {
    public UniVContainer(UniSDKInstance uniSDKInstance, AbsVContainer absVContainer, String str, boolean z, AbsComponentData absComponentData) {
        super(uniSDKInstance, (WXVContainer) absVContainer, str, z, (BasicComponentData) absComponentData);
    }

    public UniVContainer(UniSDKInstance uniSDKInstance, AbsVContainer absVContainer, boolean z, AbsComponentData absComponentData) {
        super(uniSDKInstance, (WXVContainer) absVContainer, z, (BasicComponentData) absComponentData);
    }

    public UniVContainer(UniSDKInstance uniSDKInstance, AbsVContainer absVContainer, AbsComponentData absComponentData) {
        super(uniSDKInstance, (WXVContainer) absVContainer, (BasicComponentData) absComponentData);
    }

    public UniSDKInstance getInstance() {
        return (UniSDKInstance) super.getInstance();
    }
}
