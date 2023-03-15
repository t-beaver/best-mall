package com.taobao.weex.ui.component.richtext.node;

import android.content.Context;
import android.text.SpannableStringBuilder;
import com.taobao.weex.ui.component.richtext.span.ASpan;
import com.taobao.weex.ui.component.richtext.span.ItemClickSpan;
import java.util.Map;

class ANode extends RichTextNode {
    public static final String HREF = "href";
    public static final String NODE_TYPE = "a";

    /* access modifiers changed from: protected */
    public boolean isInternalNode() {
        return true;
    }

    public String toString() {
        return "";
    }

    static class ANodeCreator implements RichTextNodeCreator<ANode> {
        ANodeCreator() {
        }

        public ANode createRichTextNode(Context context, String str, String str2) {
            return new ANode(context, str, str2);
        }

        public ANode createRichTextNode(Context context, String str, String str2, String str3, Map<String, Object> map, Map<String, Object> map2) {
            return new ANode(context, str, str2, str3, map, map2);
        }
    }

    private ANode(Context context, String str, String str2) {
        super(context, str, str2);
    }

    private ANode(Context context, String str, String str2, String str3, Map<String, Object> map, Map<String, Object> map2) {
        super(context, str, str2, str3, map, map2);
    }

    /* access modifiers changed from: protected */
    public void updateSpans(SpannableStringBuilder spannableStringBuilder, int i) {
        super.updateSpans(spannableStringBuilder, i);
        if (this.attr != null && this.attr.containsKey(RichTextNode.PSEUDO_REF)) {
            spannableStringBuilder.setSpan(new ItemClickSpan(this.mInstanceId, this.mComponentRef, this.attr.get(RichTextNode.PSEUDO_REF).toString()), 0, spannableStringBuilder.length(), createSpanFlag(i));
        } else if (this.attr != null && this.attr.containsKey("href")) {
            spannableStringBuilder.setSpan(new ASpan(this.mInstanceId, this.attr.get("href").toString()), 0, spannableStringBuilder.length(), createSpanFlag(i));
        }
    }
}
