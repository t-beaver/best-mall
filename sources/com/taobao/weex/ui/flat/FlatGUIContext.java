package com.taobao.weex.ui.flat;

import android.text.TextUtils;
import android.view.View;
import androidx.collection.ArrayMap;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.Destroyable;
import com.taobao.weex.dom.WXAttr;
import com.taobao.weex.dom.WXStyle;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.flat.widget.AndroidViewWidget;
import com.taobao.weex.ui.flat.widget.Widget;
import java.util.Map;

public class FlatGUIContext implements Destroyable {
    private Map<WXComponent, AndroidViewWidget> mViewWidgetRegistry = new ArrayMap();
    private Map<WXComponent, WidgetContainer> mWidgetRegistry = new ArrayMap();
    private Map<Widget, WXComponent> widgetToComponent = new ArrayMap();

    public boolean isFlatUIEnabled(WXComponent wXComponent) {
        return false;
    }

    public void register(WXComponent wXComponent, WidgetContainer widgetContainer) {
        if (!(widgetContainer instanceof FlatComponent) || ((FlatComponent) widgetContainer).promoteToView(true)) {
            this.mWidgetRegistry.put(wXComponent, widgetContainer);
        }
    }

    public void register(WXComponent wXComponent, AndroidViewWidget androidViewWidget) {
        this.mViewWidgetRegistry.put(wXComponent, androidViewWidget);
    }

    public void register(Widget widget, WXComponent wXComponent) {
        this.widgetToComponent.put(widget, wXComponent);
    }

    public WidgetContainer getFlatComponentAncestor(WXComponent wXComponent) {
        return this.mWidgetRegistry.get(wXComponent);
    }

    public AndroidViewWidget getAndroidViewWidget(WXComponent wXComponent) {
        return this.mViewWidgetRegistry.get(wXComponent);
    }

    public boolean promoteToView(WXComponent wXComponent, boolean z, Class<? extends WXComponent<?>> cls) {
        return !isFlatUIEnabled(wXComponent) || !cls.equals(wXComponent.getClass()) || TextUtils.equals(wXComponent.getRef(), WXComponent.ROOT) || (z && getFlatComponentAncestor(wXComponent) == null) || checkComponent(wXComponent);
    }

    public View getWidgetContainerView(Widget widget) {
        WidgetContainer flatComponentAncestor;
        WXComponent component = getComponent(widget);
        if (component == null || (flatComponentAncestor = getFlatComponentAncestor(component)) == null) {
            return null;
        }
        return flatComponentAncestor.getHostView();
    }

    public void destroy() {
        this.widgetToComponent.clear();
        for (Map.Entry<WXComponent, AndroidViewWidget> value : this.mViewWidgetRegistry.entrySet()) {
            ((AndroidViewWidget) value.getValue()).destroy();
        }
        this.mViewWidgetRegistry.clear();
        for (Map.Entry<WXComponent, WidgetContainer> value2 : this.mWidgetRegistry.entrySet()) {
            ((WidgetContainer) value2.getValue()).unmountFlatGUI();
        }
        this.mWidgetRegistry.clear();
    }

    private WXComponent getComponent(Widget widget) {
        return this.widgetToComponent.get(widget);
    }

    private boolean checkComponent(WXComponent wXComponent) {
        if (wXComponent != null) {
            WXStyle styles = wXComponent.getStyles();
            WXAttr attrs = wXComponent.getAttrs();
            if (styles.containsKey("opacity") || styles.containsKey("transform") || styles.containsKey(Constants.Name.VISIBILITY) || attrs.containsKey(Constants.Name.ELEVATION) || attrs.containsKey(Constants.Name.ARIA_HIDDEN) || attrs.containsKey(Constants.Name.ARIA_LABEL) || attrs.containsKey(WXComponent.PROP_FIXED_SIZE) || attrs.containsKey(Constants.Name.DISABLED) || styles.isFixed() || styles.isSticky() || !styles.getPesudoStyles().isEmpty() || wXComponent.getEvents().size() > 0) {
                return true;
            }
        }
        return false;
    }
}
