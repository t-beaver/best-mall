package com.taobao.weex.ui.flat;

import com.taobao.weex.ui.flat.widget.Widget;

public interface FlatComponent<T extends Widget> {
    T getOrCreateFlatWidget();

    boolean promoteToView(boolean z);
}
