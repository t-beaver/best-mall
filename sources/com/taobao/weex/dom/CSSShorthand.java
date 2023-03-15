package com.taobao.weex.dom;

import io.dcloud.feature.uniapp.dom.AbsCSSShorthand;
import java.lang.Enum;

public class CSSShorthand<T extends Enum<? extends WXCSSProperty>> extends AbsCSSShorthand {

    public enum CORNER implements WXCSSProperty {
        BORDER_TOP_LEFT,
        BORDER_TOP_RIGHT,
        BORDER_BOTTOM_RIGHT,
        BORDER_BOTTOM_LEFT,
        ALL
    }

    public enum EDGE implements WXCSSProperty {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT,
        ALL
    }

    interface WXCSSProperty extends AbsCSSShorthand.CSSProperty {
    }

    public CSSShorthand(float[] fArr) {
        super(fArr);
    }

    public CSSShorthand() {
    }

    public CSSShorthand clone() throws CloneNotSupportedException {
        return (CSSShorthand) super.clone();
    }
}
