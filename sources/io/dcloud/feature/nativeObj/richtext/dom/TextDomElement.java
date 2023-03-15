package io.dcloud.feature.nativeObj.richtext.dom;

import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.widget.TextView;
import com.facebook.common.callercontext.ContextChain;
import com.taobao.weex.common.Constants;
import io.dcloud.common.util.PdrUtil;
import io.dcloud.feature.nativeObj.richtext.IAssets;
import io.dcloud.feature.nativeObj.richtext.span.AHrefSpan;
import io.dcloud.feature.nativeObj.richtext.span.FontSpan;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;

public class TextDomElement extends DomElement {
    public String color;
    public String href;
    public String text;

    private int getFontStyleInt() {
        return Constants.Value.ITALIC.equalsIgnoreCase(getFontStyle()) ? 1 : 0;
    }

    private int getFontWeightInt() {
        return Constants.Value.BOLD.equalsIgnoreCase(getFontWeight()) ? 1 : 0;
    }

    /* access modifiers changed from: package-private */
    public String getColor() {
        HashMap<String, String> hashMap = this.style;
        if (hashMap != null && hashMap.containsKey("color")) {
            return this.style.get("color");
        }
        if (!TextUtils.isEmpty(this.color) && !"a".equalsIgnoreCase(this.tagName)) {
            return this.color;
        }
        DomElement domElement = this.parentDomElement;
        if (domElement == null || !(domElement instanceof TextDomElement)) {
            return null;
        }
        return ((TextDomElement) domElement).getColor();
    }

    /* access modifiers changed from: package-private */
    public String getFontSize() {
        HashMap<String, String> hashMap = this.style;
        if (hashMap != null && hashMap.containsKey("font-size")) {
            return this.style.get("font-size");
        }
        DomElement domElement = this.parentDomElement;
        if (domElement == null || !(domElement instanceof TextDomElement)) {
            return null;
        }
        return ((TextDomElement) domElement).getFontSize();
    }

    /* access modifiers changed from: package-private */
    public String getFontStyle() {
        HashMap<String, String> hashMap = this.style;
        if (hashMap != null && hashMap.containsKey("font-style")) {
            return this.style.get("font-style");
        }
        DomElement domElement = this.parentDomElement;
        if (domElement == null || !(domElement instanceof TextDomElement)) {
            return null;
        }
        return ((TextDomElement) domElement).getFontStyle();
    }

    /* access modifiers changed from: package-private */
    public String getFontWeight() {
        HashMap<String, String> hashMap = this.style;
        if (hashMap != null && hashMap.containsKey("font-weight")) {
            return this.style.get("font-weight");
        }
        DomElement domElement = this.parentDomElement;
        if (domElement == null || !(domElement instanceof TextDomElement)) {
            return null;
        }
        return ((TextDomElement) domElement).getFontWeight();
    }

    /* access modifiers changed from: package-private */
    public String getTextDecoration() {
        HashMap<String, String> hashMap = this.style;
        if (hashMap != null && hashMap.containsKey("text-decoration")) {
            return this.style.get("text-decoration");
        }
        DomElement domElement = this.parentDomElement;
        if (domElement == null || !(domElement instanceof TextDomElement)) {
            return null;
        }
        return ((TextDomElement) domElement).getTextDecoration();
    }

    /* access modifiers changed from: package-private */
    public int getTextDecorationInt() {
        String textDecoration = getTextDecoration();
        if (!"underline".equalsIgnoreCase(textDecoration)) {
            if ("line-through".equalsIgnoreCase(textDecoration)) {
                return 2;
            }
            return "a".equalsIgnoreCase(this.tagName) ? 1 : 0;
        }
    }

    public void makeSpan(IAssets iAssets, TextView textView, SpannableStringBuilder spannableStringBuilder) {
        IAssets iAssets2 = iAssets;
        SpannableStringBuilder spannableStringBuilder2 = spannableStringBuilder;
        if (!TextUtils.isEmpty(this.text)) {
            boolean equalsIgnoreCase = "a".equalsIgnoreCase(this.tagName);
            boolean equalsIgnoreCase2 = ContextChain.TAG_PRODUCT.equalsIgnoreCase(this.tagName);
            if (equalsIgnoreCase2) {
                spannableStringBuilder2.append("\n");
            }
            spannableStringBuilder2.append(this.text);
            float parseFloat = PdrUtil.parseFloat(getFontSize(), 100.0f, FontSpan.DEF_FONT_SIZE, 1.0f);
            int defaultColor = iAssets2.getDefaultColor(equalsIgnoreCase);
            int fontWeightInt = getFontWeightInt();
            int fontStyleInt = getFontStyleInt();
            int textDecorationInt = getTextDecorationInt();
            String color2 = getColor();
            int stringToColor = !TextUtils.isEmpty(color2) ? iAssets2.stringToColor(color2) : defaultColor;
            if (equalsIgnoreCase) {
                spannableStringBuilder2.setSpan(new AHrefSpan(parseFloat, stringToColor, fontWeightInt, fontStyleInt, textDecorationInt, this.onClickEvent, this.href), spannableStringBuilder.length() - this.text.length(), spannableStringBuilder.length(), 17);
            } else {
                spannableStringBuilder2.setSpan(new FontSpan(parseFloat, stringToColor, fontWeightInt, fontStyleInt, textDecorationInt), spannableStringBuilder.length() - this.text.length(), spannableStringBuilder.length(), 17);
            }
            if (equalsIgnoreCase2) {
                spannableStringBuilder2.append("\n");
            }
        }
    }

    public void parseDomElement(XmlPullParser xmlPullParser) {
        super.parseDomElement(xmlPullParser);
        this.color = xmlPullParser.getAttributeValue(xmlPullParser.getNamespace(), "color");
        this.href = xmlPullParser.getAttributeValue(xmlPullParser.getNamespace(), "href");
    }
}
