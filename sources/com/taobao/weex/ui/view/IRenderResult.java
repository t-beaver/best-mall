package com.taobao.weex.ui.view;

import com.taobao.weex.ui.component.WXComponent;

public interface IRenderResult<T extends WXComponent> {
    T getComponent();
}
