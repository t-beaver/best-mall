package com.taobao.weex.ui.action;

import android.view.View;
import com.taobao.weex.dom.CSSShorthand;
import com.taobao.weex.dom.WXAttr;
import com.taobao.weex.dom.WXEvent;
import com.taobao.weex.dom.WXStyle;
import com.taobao.weex.ui.component.list.template.jni.NativeRenderObjectUtils;
import io.dcloud.feature.uniapp.ui.action.AbsComponentData;

public class BasicComponentData<T extends View> extends AbsComponentData {
    public BasicComponentData(String str, String str2, String str3) {
        super(str, str2, str3);
    }

    public WXStyle getStyles() {
        return (WXStyle) super.getStyles();
    }

    public WXAttr getAttrs() {
        return (WXAttr) super.getAttrs();
    }

    public WXEvent getEvents() {
        return (WXEvent) super.getEvents();
    }

    public BasicComponentData clone() throws CloneNotSupportedException {
        BasicComponentData basicComponentData = new BasicComponentData(this.mRef, this.mComponentType, this.mParentRef);
        basicComponentData.setBorders(getBorder().clone());
        basicComponentData.setMargins(getMargin().clone());
        basicComponentData.setPaddings(getPadding().clone());
        WXAttr attrs = getAttrs();
        if (attrs != null) {
            basicComponentData.mAttributes = attrs.clone();
        }
        if (this.mStyles != null) {
            basicComponentData.mStyles = this.mStyles.clone();
        }
        if (this.mEvents != null) {
            basicComponentData.mEvents = this.mEvents.clone();
        }
        if (this.renderObjectPr != 0) {
            basicComponentData.setRenderObjectPr(NativeRenderObjectUtils.nativeCopyRenderObject(this.renderObjectPr));
        }
        return basicComponentData;
    }

    public CSSShorthand getBorder() {
        return (CSSShorthand) super.getBorder();
    }

    public CSSShorthand getMargin() {
        return (CSSShorthand) super.getMargin();
    }

    public CSSShorthand getPadding() {
        return (CSSShorthand) super.getPadding();
    }
}
