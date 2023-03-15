package io.dcloud.feature.weex_text;

import android.content.Context;
import android.text.Layout;
import android.text.Spannable;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.ui.ComponentCreator;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXText;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.component.richtext.WXRichTextView;
import com.taobao.weex.ui.component.richtext.node.RichTextNode;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class DCWXRichText extends WXText {
    public DCWXRichText(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
        setContentBoxMeasurement(new RichTextContentBoxMeasurement(this));
    }

    static class RichTextContentBoxMeasurement extends DCTextContentBoxMeasurement {
        public RichTextContentBoxMeasurement(WXComponent wXComponent) {
            super(wXComponent);
        }

        /* access modifiers changed from: protected */
        public Spanned createSpanned(String str) {
            boolean z = true;
            boolean z2 = this.mComponent.getInstance() != null;
            if (this.mComponent.getInstance().getUIContext() == null) {
                z = false;
            }
            if ((!z2 || !z) || TextUtils.isEmpty(this.mComponent.getInstanceId())) {
                return new SpannedString("");
            }
            Spannable parse = RichTextNode.parse(this.mComponent.getInstance().getUIContext(), this.mComponent.getInstanceId(), this.mComponent.getRef(), str);
            updateSpannable(parse, RichTextNode.createSpanFlag(0));
            return parse;
        }
    }

    public static class Creator implements ComponentCreator {
        public WXComponent createInstance(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) throws IllegalAccessException, InvocationTargetException, InstantiationException {
            return new DCWXRichText(wXSDKInstance, wXVContainer, basicComponentData);
        }
    }

    /* access modifiers changed from: protected */
    public WXRichTextView initComponentHostView(Context context) {
        return new WXRichTextView(context);
    }

    public void updateAttrs(Map<String, Object> map) {
        super.updateAttrs(map);
        if (map.containsKey("value")) {
            WXBridgeManager.getInstance().post(new Runnable() {
                public void run() {
                    if (DCWXRichText.this.contentBoxMeasurement instanceof RichTextContentBoxMeasurement) {
                        ((RichTextContentBoxMeasurement) DCWXRichText.this.contentBoxMeasurement).forceRelayout();
                    }
                }
            });
        }
    }

    public void updateExtra(Object obj) {
        super.updateExtra(obj);
        if (obj instanceof Layout) {
            Layout layout = (Layout) obj;
            if (!getStyles().containsKey("height")) {
                WXBridgeManager.getInstance().setStyleHeight(getInstanceId(), getRef(), (float) layout.getHeight());
            }
        }
    }
}
