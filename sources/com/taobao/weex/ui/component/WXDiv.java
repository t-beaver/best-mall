package com.taobao.weex.ui.component;

import android.content.Context;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.ui.ComponentCreator;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.flat.FlatComponent;
import com.taobao.weex.ui.flat.WidgetContainer;
import com.taobao.weex.ui.flat.widget.WidgetGroup;
import com.taobao.weex.ui.view.WXFrameLayout;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

@Component(lazyload = false)
public class WXDiv extends WidgetContainer<WXFrameLayout> implements FlatComponent<WidgetGroup> {
    private WidgetGroup mWidgetGroup;

    public static class Ceator implements ComponentCreator {
        public WXComponent createInstance(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) throws IllegalAccessException, InvocationTargetException, InstantiationException {
            return new WXDiv(wXSDKInstance, wXVContainer, basicComponentData);
        }
    }

    @Deprecated
    public WXDiv(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, String str, boolean z, BasicComponentData basicComponentData) {
        this(wXSDKInstance, wXVContainer, basicComponentData);
    }

    public WXDiv(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    /* access modifiers changed from: protected */
    public WXFrameLayout initComponentHostView(Context context) {
        WXFrameLayout wXFrameLayout = new WXFrameLayout(context);
        wXFrameLayout.holdComponent(this);
        return wXFrameLayout;
    }

    public boolean promoteToView(boolean z) {
        if (getInstance().getFlatUIContext() == null) {
            return false;
        }
        if (!intendToBeFlatContainer() || getInstance().getFlatUIContext().promoteToView(this, z, WXDiv.class)) {
            return true;
        }
        return false;
    }

    public WidgetGroup getOrCreateFlatWidget() {
        if (this.mWidgetGroup == null) {
            this.mWidgetGroup = new WidgetGroup(getInstance().getFlatUIContext());
            for (int i = 0; i < getChildCount(); i++) {
                createChildViewAt(i);
            }
            mountFlatGUI();
        }
        return this.mWidgetGroup;
    }

    /* access modifiers changed from: protected */
    public void mountFlatGUI() {
        if (this.widgets == null) {
            this.widgets = new LinkedList();
        }
        if (!promoteToView(true)) {
            this.mWidgetGroup.replaceAll(this.widgets);
        } else if (getHostView() != null) {
            ((WXFrameLayout) getHostView()).mountFlatGUI(this.widgets);
        }
    }

    public void unmountFlatGUI() {
        if (getHostView() != null) {
            ((WXFrameLayout) getHostView()).unmountFlatGUI();
        }
    }

    public boolean intendToBeFlatContainer() {
        return getInstance().getFlatUIContext().isFlatUIEnabled(this) && WXDiv.class.equals(getClass());
    }

    public boolean isVirtualComponent() {
        return true ^ promoteToView(true);
    }
}
