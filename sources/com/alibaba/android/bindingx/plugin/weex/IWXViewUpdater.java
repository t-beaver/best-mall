package com.alibaba.android.bindingx.plugin.weex;

import android.view.View;
import com.alibaba.android.bindingx.core.PlatformManager;
import com.taobao.weex.ui.component.WXComponent;
import java.util.Map;

public interface IWXViewUpdater {
    void update(WXComponent wXComponent, View view, Object obj, PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator, Map<String, Object> map);
}
