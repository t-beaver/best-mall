package com.taobao.weex.ui.component.richtext;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.layout.measurefunc.TextContentBoxMeasurement;
import com.taobao.weex.ui.ComponentCreator;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXText;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.component.richtext.node.RichTextNode;
import com.taobao.weex.ui.component.richtext.node.RichTextNodeManager;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WXRichText extends WXText {
    private List<RichTextNode> nodes = new LinkedList();

    static class RichTextContentBoxMeasurement extends TextContentBoxMeasurement {
        public RichTextContentBoxMeasurement(WXComponent wXComponent) {
            super(wXComponent);
        }

        /* access modifiers changed from: protected */
        public Spanned createSpanned(String str) {
            if (!TextUtils.isEmpty(str)) {
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
            Spannable access$000 = ((WXRichText) this.mComponent).toSpan();
            updateSpannable(access$000, RichTextNode.createSpanFlag(0));
            return access$000;
        }
    }

    public static class Creator implements ComponentCreator {
        public WXComponent createInstance(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) throws IllegalAccessException, InvocationTargetException, InstantiationException {
            return new WXRichText(wXSDKInstance, wXVContainer, basicComponentData);
        }
    }

    public WXRichText(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
        setContentBoxMeasurement(new RichTextContentBoxMeasurement(this));
    }

    public void AddChildNode(String str, String str2, String str3, Map<String, String> map, Map<String, String> map2) {
        if (getInstance() != null && getInstance().getUIContext() != null && !TextUtils.isEmpty(getInstanceId()) && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            RichTextNode createRichTextNode = RichTextNodeManager.createRichTextNode(getInstance().getUIContext(), getInstanceId(), getRef(), str, str2, map, map2);
            if (TextUtils.isEmpty(str3)) {
                this.nodes.add(createRichTextNode);
                return;
            }
            RichTextNode findRichNode = findRichNode(str3);
            if (findRichNode != null) {
                findRichNode.addChildNode(createRichTextNode);
            }
        }
    }

    /* access modifiers changed from: private */
    public Spannable toSpan() {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        List<RichTextNode> list = this.nodes;
        if (list != null && !list.isEmpty()) {
            for (RichTextNode span : this.nodes) {
                spannableStringBuilder.append(span.toSpan(1));
            }
        }
        return spannableStringBuilder;
    }

    public void removeChildNode(String str, String str2) {
        List<RichTextNode> list = this.nodes;
        if (list != null && !list.isEmpty()) {
            if (str.equals("")) {
                for (RichTextNode next : this.nodes) {
                    if (TextUtils.equals(next.getRef(), str2)) {
                        this.nodes.remove(next);
                    }
                }
                return;
            }
            RichTextNode findRichNode = findRichNode(str);
            if (findRichNode != null) {
                findRichNode.removeChildNode(str2);
            }
        }
    }

    public void updateChildNodeStyles(String str, Map<String, Object> map) {
        RichTextNode findRichNode = findRichNode(str);
        if (findRichNode != null) {
            findRichNode.updateStyles(map);
        }
    }

    public void updateChildNodeAttrs(String str, Map<String, Object> map) {
        RichTextNode findRichNode = findRichNode(str);
        if (findRichNode != null) {
            findRichNode.updateAttrs(map);
        }
    }

    private RichTextNode findRichNode(String str) {
        List<RichTextNode> list;
        if (TextUtils.isEmpty(str) || (list = this.nodes) == null || list.isEmpty()) {
            return null;
        }
        for (RichTextNode findRichNode : this.nodes) {
            RichTextNode findRichNode2 = findRichNode.findRichNode(str);
            if (findRichNode2 != null) {
                return findRichNode2;
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public WXRichTextView initComponentHostView(Context context) {
        return new WXRichTextView(context);
    }
}
