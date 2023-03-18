package io.dcloud.feature.nativeObj.richtext.dom;

import android.text.SpannableStringBuilder;
import android.widget.TextView;
import io.dcloud.feature.nativeObj.richtext.IAssets;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;

public class DomElement {
    public String id;
    public String onClickEvent;
    public DomElement parentDomElement;
    public HashMap<String, String> style;
    public String tagName;

    public void makeSpan(IAssets iAssets, TextView textView, SpannableStringBuilder spannableStringBuilder) {
        spannableStringBuilder.append("\n");
    }

    public void parseDomElement(XmlPullParser xmlPullParser) {
        this.id = xmlPullParser.getAttributeValue(xmlPullParser.getNamespace(), "id");
        this.tagName = xmlPullParser.getName();
        parseStyle(xmlPullParser.getAttributeValue(xmlPullParser.getNamespace(), "style"));
        this.onClickEvent = xmlPullParser.getAttributeValue(xmlPullParser.getNamespace(), "onclick");
    }

    public void parseStyle(String str) {
        String[] split;
        if (str != null && (split = str.split(";")) != null && split.length > 0) {
            this.style = new HashMap<>(2);
            for (int i = 0; i < split.length; i++) {
                try {
                    String[] split2 = split[i].split(":");
                    this.style.put(split2[0].trim(), split2[1].trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
