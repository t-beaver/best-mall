package com.taobao.weex.ui.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.dom.CSSShorthand;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXViewUtils;
import io.dcloud.feature.uniapp.dom.AbsCSSShorthand;
import io.dcloud.feature.uniapp.ui.component.AbsVContainer;

public abstract class WXVContainer<T extends ViewGroup> extends AbsVContainer<T> {
    private static final String TAG = "WXVContainer";
    private WXVContainer<T>.BoxShadowHost mBoxShadowHost;

    /* access modifiers changed from: protected */
    public int getChildrenLayoutTopOffset() {
        return 0;
    }

    public WXVContainer(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, String str, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, str, z, basicComponentData);
    }

    public WXVContainer(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, z, basicComponentData);
    }

    public WXVContainer(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    public View getBoxShadowHost(boolean z) {
        if (z) {
            return this.mBoxShadowHost;
        }
        ViewGroup viewGroup = (ViewGroup) getHostView();
        if (viewGroup == null) {
            return null;
        }
        try {
            String componentType = getComponentType();
            if (WXBasicComponentType.DIV.equals(componentType) || WXBasicComponentType.VIEW.equals(componentType)) {
                WXLogUtils.d("BoxShadow", "Draw box-shadow with BoxShadowHost on div: " + toString());
                if (this.mBoxShadowHost == null) {
                    WXVContainer<T>.BoxShadowHost boxShadowHost = new BoxShadowHost(getContext());
                    this.mBoxShadowHost = boxShadowHost;
                    WXViewUtils.setBackGround(boxShadowHost, (Drawable) null, this);
                    viewGroup.addView(this.mBoxShadowHost);
                }
                CSSShorthand padding = getPadding();
                CSSShorthand border = getBorder();
                ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(viewGroup.getLayoutParams());
                setMarginsSupportRTL(marginLayoutParams, -((int) (padding.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.LEFT) + border.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.LEFT))), -((int) (padding.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.TOP) + border.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.TOP))), -((int) (padding.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.RIGHT) + border.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.RIGHT))), -((int) (padding.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.BOTTOM) + border.get((Enum<? extends AbsCSSShorthand.CSSProperty>) CSSShorthand.EDGE.BOTTOM))));
                this.mBoxShadowHost.setLayoutParams(marginLayoutParams);
                viewGroup.removeView(this.mBoxShadowHost);
                viewGroup.addView(this.mBoxShadowHost);
                return this.mBoxShadowHost;
            }
        } catch (Throwable th) {
            WXLogUtils.w("BoxShadow", th);
        }
        return viewGroup;
    }

    public void removeBoxShadowHost() {
        ViewGroup viewGroup = (ViewGroup) getHostView();
        if (viewGroup != null) {
            try {
                String componentType = getComponentType();
                if (this.mBoxShadowHost == null) {
                    return;
                }
                if (WXBasicComponentType.DIV.equals(componentType) || WXBasicComponentType.VIEW.equals(componentType)) {
                    viewGroup.removeView(this.mBoxShadowHost);
                }
            } catch (Throwable unused) {
            }
        }
    }

    private class BoxShadowHost extends View {
        public BoxShadowHost(Context context) {
            super(context);
        }
    }
}
