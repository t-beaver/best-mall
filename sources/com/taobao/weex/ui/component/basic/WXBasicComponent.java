package com.taobao.weex.ui.component.basic;

import android.view.View;
import com.taobao.weex.dom.CSSShorthand;
import com.taobao.weex.dom.WXAttr;
import com.taobao.weex.dom.WXEvent;
import com.taobao.weex.dom.WXStyle;
import com.taobao.weex.ui.action.BasicComponentData;
import io.dcloud.feature.uniapp.ui.action.AbsComponentData;
import io.dcloud.feature.uniapp.ui.component.AbsBasicComponent;

public abstract class WXBasicComponent<T extends View> extends AbsBasicComponent {
    public WXBasicComponent(AbsComponentData absComponentData) {
        super(absComponentData);
    }

    public final WXStyle getStyles() {
        return (WXStyle) super.getStyles();
    }

    public final WXAttr getAttrs() {
        return (WXAttr) super.getAttrs();
    }

    public final WXEvent getEvents() {
        return (WXEvent) super.getEvents();
    }

    public BasicComponentData getBasicComponentData() {
        return (BasicComponentData) super.getBasicComponentData();
    }

    public final CSSShorthand getBorder() {
        return (CSSShorthand) super.getBorder();
    }

    public final CSSShorthand getPadding() {
        return (CSSShorthand) super.getPadding();
    }

    public final CSSShorthand getMargin() {
        return (CSSShorthand) super.getMargin();
    }
}
