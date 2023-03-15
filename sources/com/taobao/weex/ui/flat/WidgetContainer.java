package com.taobao.weex.ui.flat;

import android.util.Pair;
import android.view.ViewGroup;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.flat.widget.AndroidViewWidget;
import com.taobao.weex.ui.flat.widget.Widget;
import java.util.LinkedList;
import java.util.List;

public abstract class WidgetContainer<T extends ViewGroup> extends WXVContainer<T> {
    protected List<Widget> widgets;

    public boolean intendToBeFlatContainer() {
        return false;
    }

    /* access modifiers changed from: protected */
    public abstract void mountFlatGUI();

    /* access modifiers changed from: protected */
    public abstract void unmountFlatGUI();

    public WidgetContainer(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    public void createChildViewAt(int i) {
        Widget widget;
        if (intendToBeFlatContainer()) {
            Pair<WXComponent, Integer> rearrangeIndexAndGetChild = rearrangeIndexAndGetChild(i);
            if (rearrangeIndexAndGetChild.first != null) {
                WXComponent wXComponent = (WXComponent) rearrangeIndexAndGetChild.first;
                FlatGUIContext flatUIContext = getInstance().getFlatUIContext();
                WidgetContainer flatComponentAncestor = flatUIContext.getFlatComponentAncestor(this);
                if (flatComponentAncestor == null || flatUIContext.getAndroidViewWidget(this) != null) {
                    flatComponentAncestor = this;
                }
                flatUIContext.register(wXComponent, flatComponentAncestor);
                if (wXComponent instanceof FlatComponent) {
                    FlatComponent flatComponent = (FlatComponent) wXComponent;
                    if (!flatComponent.promoteToView(false)) {
                        widget = flatComponent.getOrCreateFlatWidget();
                        flatUIContext.register(widget, wXComponent);
                        addFlatChild(widget, ((Integer) rearrangeIndexAndGetChild.second).intValue());
                        return;
                    }
                }
                AndroidViewWidget androidViewWidget = new AndroidViewWidget(flatUIContext);
                AndroidViewWidget androidViewWidget2 = androidViewWidget;
                flatUIContext.register(wXComponent, androidViewWidget2);
                wXComponent.createView();
                androidViewWidget2.setContentView(wXComponent.getHostView());
                flatComponentAncestor.addSubView(wXComponent.getHostView(), -1);
                widget = androidViewWidget;
                flatUIContext.register(widget, wXComponent);
                addFlatChild(widget, ((Integer) rearrangeIndexAndGetChild.second).intValue());
                return;
            }
            return;
        }
        super.createChildViewAt(i);
    }

    private void addFlatChild(Widget widget, int i) {
        if (this.widgets == null) {
            this.widgets = new LinkedList();
        }
        if (i >= this.widgets.size()) {
            this.widgets.add(widget);
        } else {
            this.widgets.add(i, widget);
        }
        mountFlatGUI();
    }
}
