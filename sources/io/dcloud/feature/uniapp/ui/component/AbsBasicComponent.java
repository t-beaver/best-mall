package io.dcloud.feature.uniapp.ui.component;

import android.view.View;
import com.taobao.weex.dom.CSSShorthand;
import com.taobao.weex.ui.action.GraphicPosition;
import com.taobao.weex.ui.action.GraphicSize;
import io.dcloud.feature.uniapp.dom.AbsAttr;
import io.dcloud.feature.uniapp.dom.AbsCSSShorthand;
import io.dcloud.feature.uniapp.dom.AbsEvent;
import io.dcloud.feature.uniapp.dom.AbsStyle;
import io.dcloud.feature.uniapp.ui.action.AbsComponentData;
import java.util.Map;
import java.util.Set;

public abstract class AbsBasicComponent<T extends View> {
    private AbsComponentData mBasicComponentData;
    private String mComponentType;
    private Object mExtra;
    private boolean mIsLayoutRTL;
    private GraphicPosition mLayoutPosition;
    private GraphicSize mLayoutSize;
    private String mRef;
    private float mViewPortWidth = 750.0f;

    public AbsBasicComponent(AbsComponentData absComponentData) {
        this.mBasicComponentData = absComponentData;
        this.mRef = absComponentData.mRef;
        this.mComponentType = absComponentData.mComponentType;
    }

    public AbsComponentData getBasicComponentData() {
        return this.mBasicComponentData;
    }

    /* access modifiers changed from: protected */
    public void bindComponent(AbsBasicComponent absBasicComponent) {
        this.mComponentType = absBasicComponent.getComponentType();
        this.mRef = absBasicComponent.getRef();
    }

    public AbsStyle getStyles() {
        return this.mBasicComponentData.getStyles();
    }

    public AbsAttr getAttrs() {
        return this.mBasicComponentData.getAttrs();
    }

    public AbsEvent getEvents() {
        return this.mBasicComponentData.getEvents();
    }

    public AbsCSSShorthand getMargin() {
        return this.mBasicComponentData.getMargin();
    }

    public AbsCSSShorthand getPadding() {
        return this.mBasicComponentData.getPadding();
    }

    public AbsCSSShorthand getBorder() {
        return this.mBasicComponentData.getBorder();
    }

    public final void setMargins(CSSShorthand cSSShorthand) {
        this.mBasicComponentData.setMargins(cSSShorthand);
    }

    public final void setPaddings(CSSShorthand cSSShorthand) {
        this.mBasicComponentData.setPaddings(cSSShorthand);
    }

    public final void setBorders(CSSShorthand cSSShorthand) {
        this.mBasicComponentData.setBorders(cSSShorthand);
    }

    public final void addAttr(Map<String, Object> map) {
        if (map != null && !map.isEmpty()) {
            this.mBasicComponentData.addAttr(map);
        }
    }

    public final void addStyle(Map<String, Object> map) {
        if (map != null && !map.isEmpty()) {
            this.mBasicComponentData.addStyle(map);
        }
    }

    public final void addStyle(Map<String, Object> map, boolean z) {
        if (map != null && !map.isEmpty()) {
            this.mBasicComponentData.addStyle(map, z);
        }
    }

    public final void updateStyle(Map<String, Object> map, boolean z) {
        if (map != null && !map.isEmpty()) {
            this.mBasicComponentData.getStyles().updateStyle(map, z);
        }
    }

    public final void addEvent(Set<String> set) {
        if (set != null && !set.isEmpty()) {
            this.mBasicComponentData.addEvent(set);
        }
    }

    public final void addShorthand(Map<String, String> map) {
        AbsComponentData absComponentData;
        if (!map.isEmpty() && (absComponentData = this.mBasicComponentData) != null) {
            absComponentData.addShorthand(map);
        }
    }

    public float getViewPortWidthForFloat() {
        return this.mViewPortWidth;
    }

    public int getViewPortWidth() {
        return Math.round(this.mViewPortWidth);
    }

    public void setViewPortWidth(float f) {
        this.mViewPortWidth = f;
    }

    public Object getExtra() {
        return this.mExtra;
    }

    public void updateExtra(Object obj) {
        this.mExtra = obj;
    }

    public String getComponentType() {
        return this.mComponentType;
    }

    public String getRef() {
        return this.mRef;
    }

    public void setIsLayoutRTL(boolean z) {
        this.mIsLayoutRTL = z;
    }

    public boolean isLayoutRTL() {
        return this.mIsLayoutRTL;
    }

    public GraphicPosition getLayoutPosition() {
        if (this.mLayoutPosition == null) {
            this.mLayoutPosition = new GraphicPosition(0.0f, 0.0f, 0.0f, 0.0f);
        }
        return this.mLayoutPosition;
    }

    /* access modifiers changed from: protected */
    public void setLayoutPosition(GraphicPosition graphicPosition) {
        this.mLayoutPosition = graphicPosition;
    }

    public GraphicSize getLayoutSize() {
        if (this.mLayoutSize == null) {
            this.mLayoutSize = new GraphicSize(0.0f, 0.0f);
        }
        return this.mLayoutSize;
    }

    /* access modifiers changed from: protected */
    public void setLayoutSize(GraphicSize graphicSize) {
        this.mLayoutSize = graphicSize;
    }

    public float getCSSLayoutTop() {
        GraphicPosition graphicPosition = this.mLayoutPosition;
        if (graphicPosition == null) {
            return 0.0f;
        }
        return graphicPosition.getTop();
    }

    public float getCSSLayoutBottom() {
        GraphicPosition graphicPosition = this.mLayoutPosition;
        if (graphicPosition == null) {
            return 0.0f;
        }
        return graphicPosition.getBottom();
    }

    public float getCSSLayoutLeft() {
        GraphicPosition graphicPosition = this.mLayoutPosition;
        if (graphicPosition == null) {
            return 0.0f;
        }
        return graphicPosition.getLeft();
    }

    public float getCSSLayoutRight() {
        GraphicPosition graphicPosition = this.mLayoutPosition;
        if (graphicPosition == null) {
            return 0.0f;
        }
        return graphicPosition.getRight();
    }

    public float getLayoutWidth() {
        GraphicSize graphicSize = this.mLayoutSize;
        if (graphicSize == null) {
            return 0.0f;
        }
        return graphicSize.getWidth();
    }

    public float getLayoutHeight() {
        GraphicSize graphicSize = this.mLayoutSize;
        if (graphicSize == null) {
            return 0.0f;
        }
        return graphicSize.getHeight();
    }
}
