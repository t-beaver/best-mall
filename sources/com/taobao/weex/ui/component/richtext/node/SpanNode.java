package com.taobao.weex.ui.component.richtext.node;

import android.content.Context;
import android.text.SpannableStringBuilder;
import com.taobao.weex.dom.TextDecorationSpan;
import com.taobao.weex.dom.WXStyle;
import java.util.Map;

class SpanNode extends RichTextNode {
    public static final String NODE_TYPE = "span";

    /* access modifiers changed from: protected */
    public boolean isInternalNode() {
        return true;
    }

    static class SpanNodeCreator implements RichTextNodeCreator<SpanNode> {
        SpanNodeCreator() {
        }

        public SpanNode createRichTextNode(Context context, String str, String str2) {
            return new SpanNode(context, str, str2);
        }

        public SpanNode createRichTextNode(Context context, String str, String str2, String str3, Map<String, Object> map, Map<String, Object> map2) {
            return new SpanNode(context, str, str2, str3, map, map2);
        }
    }

    private SpanNode(Context context, String str, String str2) {
        super(context, str, str2);
    }

    private SpanNode(Context context, String str, String str2, String str3, Map<String, Object> map, Map<String, Object> map2) {
        super(context, str, str2, str3, map, map2);
    }

    public String toString() {
        return (this.attr == null || !this.attr.containsKey("value")) ? "" : this.attr.get("value").toString();
    }

    /* access modifiers changed from: protected */
    public void updateSpans(SpannableStringBuilder spannableStringBuilder, int i) {
        super.updateSpans(spannableStringBuilder, i);
        spannableStringBuilder.setSpan(new TextDecorationSpan(WXStyle.getTextDecoration(this.style)), 0, spannableStringBuilder.length(), createSpanFlag(i));
    }
}
