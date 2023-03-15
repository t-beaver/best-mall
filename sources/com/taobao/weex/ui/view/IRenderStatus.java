package com.taobao.weex.ui.view;

import com.taobao.weex.ui.component.WXComponent;

public interface IRenderStatus<T extends WXComponent> {
    void holdComponent(T t);
}
