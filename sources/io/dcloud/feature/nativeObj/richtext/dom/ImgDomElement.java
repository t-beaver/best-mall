package io.dcloud.feature.nativeObj.richtext.dom;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.TextView;
import io.dcloud.feature.nativeObj.richtext.IAssets;
import io.dcloud.feature.nativeObj.richtext.span.ImgSpan;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;

public class ImgDomElement extends DomElement {
    public String height;
    public String href;
    AsycLoader mAsycLoader;
    public String src;
    public String width;

    public static class AsycLoader {
        public int height;
        String href = null;
        public ImgSpan self;
        public SpannableStringBuilder spaned;
        public TextView textView;
        public String url;
        public int width;

        public AsycLoader(TextView textView2, SpannableStringBuilder spannableStringBuilder, ImgSpan imgSpan, String str, int i, int i2, String str2) {
            this.textView = textView2;
            this.spaned = spannableStringBuilder;
            this.self = imgSpan;
            this.url = str;
            this.width = i;
            this.height = i2;
            this.href = str2;
        }

        public void onComplete(Bitmap bitmap) {
            SpannableStringBuilder spannableStringBuilder = this.spaned;
            ImgSpan imgSpan = this.self;
            String source = imgSpan.getSource();
            if (bitmap != null) {
                BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                StringBuilder sb = new StringBuilder();
                sb.append("after w=");
                int i = this.width;
                if (i <= 0) {
                    i = bitmap.getWidth();
                }
                sb.append(i);
                sb.append(";h=");
                int i2 = this.height;
                if (i2 <= 0) {
                    i2 = bitmap.getHeight();
                }
                sb.append(i2);
                Log.e("DnetImg", sb.toString());
                int i3 = this.width;
                if (i3 <= 0) {
                    i3 = bitmap.getWidth();
                }
                int i4 = this.height;
                if (i4 <= 0) {
                    i4 = bitmap.getHeight();
                }
                bitmapDrawable.setBounds(0, 0, i3, i4);
                ImgSpan imgSpan2 = new ImgSpan(bitmapDrawable, source, 0, imgSpan.getOnClickEvent(), this.href);
                int spanStart = spannableStringBuilder.getSpanStart(imgSpan);
                int spanEnd = spannableStringBuilder.getSpanEnd(imgSpan);
                if (spanStart >= 0 && spanEnd >= 0) {
                    spannableStringBuilder.removeSpan(imgSpan);
                    spannableStringBuilder.setSpan(imgSpan2, spanStart, spanEnd, 17);
                    this.textView.post(new Runnable() {
                        public void run() {
                            AsycLoader asycLoader = AsycLoader.this;
                            asycLoader.textView.setText(asycLoader.spaned);
                            AsycLoader.this.textView.requestLayout();
                        }
                    });
                }
            }
        }
    }

    public String getSrc() {
        return this.src;
    }

    public void makeSpan(IAssets iAssets, TextView textView, SpannableStringBuilder spannableStringBuilder) {
        String str;
        BitmapDrawable bitmapDrawable;
        int i;
        IAssets iAssets2 = iAssets;
        SpannableStringBuilder spannableStringBuilder2 = spannableStringBuilder;
        HashMap<String, String> hashMap = this.style;
        String str2 = null;
        if (hashMap != null) {
            String str3 = hashMap.get("width");
            str2 = this.style.get("height");
            str = str3;
        } else {
            str = null;
        }
        if (TextUtils.isEmpty(str2)) {
            str2 = this.height;
        }
        if (TextUtils.isEmpty(str)) {
            str = this.width;
        }
        int convertWidth = (int) iAssets2.convertWidth(str, -2.0f);
        int convertHeight = (int) iAssets2.convertHeight(str2, -2.0f);
        boolean isNetworkUrl = URLUtil.isNetworkUrl(this.src);
        if (isNetworkUrl) {
            bitmapDrawable = Resources.getSystem().getDrawable(17301673);
        } else {
            bitmapDrawable = new BitmapDrawable(BitmapFactory.decodeStream(iAssets2.convert2InputStream(this.src)));
        }
        if (convertWidth > 0) {
            i = convertWidth;
        } else {
            i = bitmapDrawable.getIntrinsicWidth();
        }
        bitmapDrawable.setBounds(0, 0, i, convertHeight > 0 ? convertHeight : bitmapDrawable.getIntrinsicHeight());
        ImgSpan imgSpan = new ImgSpan(bitmapDrawable, this.src, 0, this.onClickEvent, this.href);
        spannableStringBuilder2.append("￼");
        spannableStringBuilder2.setSpan(imgSpan, spannableStringBuilder.length() - 1, spannableStringBuilder.length(), 17);
        if (isNetworkUrl) {
            iAssets2.loadResource(new AsycLoader(textView, spannableStringBuilder, imgSpan, this.src, convertWidth, convertHeight, this.href));
        }
    }

    public void parseDomElement(XmlPullParser xmlPullParser) {
        super.parseDomElement(xmlPullParser);
        this.src = xmlPullParser.getAttributeValue(xmlPullParser.getNamespace(), "src");
        this.width = xmlPullParser.getAttributeValue(xmlPullParser.getNamespace(), "width");
        this.height = xmlPullParser.getAttributeValue(xmlPullParser.getNamespace(), "height");
        this.href = xmlPullParser.getAttributeValue(xmlPullParser.getNamespace(), "href");
    }
}
